/**
 * ﻿Copyright Ministère des Affaires étrangères et du Développement international , 22 avril 2015
 * https://adullact.net/projects/hornet/
 *
 *
 * Ce logiciel est un programme informatique servant à faciliter la création
 *  d'applications Web accessibles conforémement au RGAA et performantes.
 *
 * Ce logiciel est régi par la licence CeCILL v2.1 soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement,
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package hornet.framework.clamav.simulateur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simulateur ClamAV
 */
public class SimulateurServer {

    /**
     * Logger de la classe
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateurServer.class);

    /**
     * Port d'utilisation du serveur clamAV
     */
    private static final int CLAMAV_DEFAULT_PORT = 3310;

    /**
     * Nombre de thread par défaut
     */
    private static final int NB_THREAD = 100;

    /**
     * Attente d'evenements sur la socket avec timeout
     */
    private static final int TIMEOUT_SELECT = 5000;

    /**
     * Adresse du serveur
     */
    private final transient InetAddress addr;

    /**
     * Port du serveur
     */
    private final transient int port;

    /**
     * LoopSelector
     */
    private transient SelectorRun loopSelector;

    /**
     * Pool d'exécution des analyses anti-virales
     */
    private final transient ExecutorService pool;

    /**
     * Thread du serveur
     */
    private transient Thread selectorThread;

    /**
     * Classe de gestion de l'événement
     *
     */
    class Acceptor implements Runnable { // inner

        /**
         * Evénement lié à la socket
         */
        private final transient SelectionKey key;

        /**
         * Constructeur
         *
         * @param key
         *            key
         */
        public Acceptor(
                    final SelectionKey key) {

            super();
            this.key = key;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {

            try {
                final ServerSocketChannel serverChannel = (ServerSocketChannel) this.key.channel();
                final SocketChannel channel = serverChannel.accept();

                final Socket socket = channel.socket();
                final SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                SimulateurServer.LOGGER.info("Connected to: {}", remoteAddr);

                // Lancement du traitement de la requête client
                new AnalyseWorker(channel, this.key.selector(), SimulateurServer.this.pool);

            } catch (final IOException e) {
                SimulateurServer.LOGGER.info("Erreur lors de la tentative d'accès à la socket", e);
            }

        }
    }

    /**
     * Classe gérant le traitement d'écoute
     *
     */
    class SelectorRun implements Runnable { // inner

        /**
         * Doit-on arrêter le boucle d'écoute ?
         */
        private transient boolean isStopped = false;

        /**
         * Gestionnaire d'évenement sur la socket
         */
        private transient Selector selector;

        /**
         * Socket serveur
         */
        private transient ServerSocketChannel serverChannel;

        /**
         * Constructeur
         */
        public SelectorRun() {

            super();
            this.isStopped = false;
        }

        /**
         * Arrête les tâches de fond gérant les demandes d'analyse. bascule le booléen et signale au selector
         * (via wakeup) de sortir de l'appel bloquant (select).
         *
         */
        public void stopLoop() {

            SimulateurServer.LOGGER.info("Arrêt demandé");

            this.isStopped = true;
            this.selector.wakeup();
            SimulateurServer.LOGGER.info("Signal Wakeup");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {

            try {
                this.createAndListen();

                // processing
                this.loopSelector();
            } catch (final IOException e) {
                SimulateurServer.LOGGER.info("Erreur lors de la tentative d'accès à la socket", e);
            }
            SimulateurServer.LOGGER.info("Arrêt");
            try {
                this.serverChannel.close();
            } catch (final IOException e) {
                SimulateurServer.LOGGER.info("Erreur lors de la l'arrêt du serveur", e);
            }
            SimulateurServer.LOGGER.info("Fermeture de la socket serveur OK");
        }

        /**
         * Boucle d'attente des événements
         *
         * @throws IOException
         *             IOException
         */
        private void loopSelector() throws IOException {

            while (!this.isStopped) {
                // attente d'evenements sur la socket avec timeout
                // (code
                // défensif à priori inutile - wakeup dans le
                // stopserver)
                this.selector.select(SimulateurServer.TIMEOUT_SELECT);

                // wakeup to work on selected keys
                final Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                while (keys.hasNext()) {

                    try {

                        final SelectionKey key = keys.next();

                        // this is necessary to prevent the same key
                        // from
                        // coming up
                        // again the next time around.
                        keys.remove();
                        if (!key.isValid()) {
                            continue;
                        }

                        final Runnable r = (Runnable) key.attachment();
                        if (r != null) {
                            r.run();
                        }
                    } catch (final CancelledKeyException e) {
                        SimulateurServer.LOGGER.warn("Un événement a été annulé");
                    }
                }
                this.selector.selectedKeys().clear();
            }
            if (this.isStopped) {
                SimulateurServer.LOGGER.info("Arrêt reçu");

            }
        }

        /**
         * Méthode permettant de créer la socket
         *
         * @throws IOException
         *             IOException
         */
        private void createAndListen() throws IOException {

            // create selector and channel
            this.selector = Selector.open();
            this.serverChannel = ServerSocketChannel.open();
            this.serverChannel.configureBlocking(false);

            // bind to port
            final InetSocketAddress listenAddr =
                        new InetSocketAddress(SimulateurServer.this.addr, SimulateurServer.this.port);
            this.serverChannel.socket().bind(listenAddr);

            // on définit le traitement à lancer lors de la réception
            // d'un événement sur la socket
            final SelectionKey acceptKey = this.serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            acceptKey.attach(new Acceptor(acceptKey));
            SimulateurServer.LOGGER.info("Simulateur ClamAV en écoute \n");
        }
    }

    /**
     * Constructeur
     *
     * @param addr
     *            addr
     * @param port
     *            port
     * @param nbThread
     *            nbThread
     * @throws IOException
     *             IOException
     * @throws InterruptedException
     *             InterruptedException
     */
    public SimulateurServer(
                final InetAddress addr, final int port, final int nbThread) throws IOException,
        InterruptedException {

        this.addr = addr;
        this.port = port;

        this.pool = Executors.newFixedThreadPool(nbThread);

    }

    /**
     * Méthode permettant de démarrer l'écoute
     *
     * La boucle d'écoute est lancé dans un thread afin de redonner la main au programme principal
     *
     * @throws IOException
     *             IOException
     * @throws InterruptedException
     *             InterruptedException
     */
    public void startServer() throws IOException, InterruptedException {

        SimulateurServer.LOGGER.info(String.format("Simulateur ClamAV Adresse et port : %s %s%n", this.addr,
            this.port));

        // Instanciation de la boule d'écoute
        this.loopSelector = new SelectorRun();

        // Démarrage du thread
        this.selectorThread = new Thread(this.loopSelector);
        this.selectorThread.setDaemon(true);
        this.selectorThread.start();

        SimulateurServer.LOGGER.debug("Démarré");
    }

    /**
     * Méthode permettant de lancer le simulateur en mode autonome
     *
     * @param args
     *            args
     * @throws IOException
     *             Exception
     * @throws InterruptedException
     *             Exception
     */
    public static void main(final String[] args) throws IOException, InterruptedException {

        final SimulateurServer simulateurServer =
                    new SimulateurServer(null, SimulateurServer.CLAMAV_DEFAULT_PORT,
                                SimulateurServer.NB_THREAD);

        simulateurServer.startServer();
        simulateurServer.selectorThread.join();
    }

    /**
     * Arrête les tâches de fond gérant les demandes d'analyse. bascule le booléen et signale au selector (via
     * wakeup) de sortir de l'appel bloquant (select).
     *
     */
    public void stopServer() {

        if (this.loopSelector != null) {
            this.loopSelector.stopLoop();
            this.loopSelector = null;
        }
        this.pool.shutdown();
    }
}
