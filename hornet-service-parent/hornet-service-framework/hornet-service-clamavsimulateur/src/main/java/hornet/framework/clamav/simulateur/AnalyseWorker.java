/**
 * Copyright ou © ou Copr. Ministère de l'Europe et des Affaires étrangères (2017)
 * <p/>
 * pole-architecture.dga-dsi-psi@diplomatie.gouv.fr
 * <p/>
 * Ce logiciel est un programme informatique servant à faciliter la création
 * d'applications Web conformément aux référentiels généraux français : RGI, RGS et RGAA
 * <p/>
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 * <p/>
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 * <p/>
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
 * <p/>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 * <p/>
 * <p/>
 * Copyright or © or Copr. Ministry for Europe and Foreign Affairs (2017)
 * <p/>
 * pole-architecture.dga-dsi-psi@diplomatie.gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to facilitate creation of
 * web application in accordance with french general repositories : RGI, RGS and RGAA.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 */
package hornet.framework.clamav.simulateur;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe permettant de traiter les requêtes du client.
 */
public class AnalyseWorker implements Runnable {

    /** Logger de la classe. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseWorker.class);

    /** Socket (dialogue entre le simulateur et le client). */
    private final transient SocketChannel channel;

    /** Evénement lié à  la socket. */
    private final transient SelectionKey sk;

    /** Pool de connexion. */
    private final transient ExecutorService pool;

    /**
     * Différents états que peut prendre le traitement courant.
     */
    private enum State {

        /** The reading. */
        READING,
        /** The process cmd. */
        PROCESS_CMD,
        /** The process flux. */
        PROCESS_FLUX,
        /** The writing. */
        WRITING,
        /** The finish. */
        FINISH
    };

    /** Etat courant du traitement. */
    private transient State state;

    /** Requête encoyé par le client (commande + contenu du fichier à  traiter). */
    private final transient BufferInData readBuffer;

    /** Réponse à  envoyer au client. */
    private transient String response;

    /**
     * Constructeur.
     *
     * @param channel
     *            channel
     * @param selector
     *            selector
     * @param apool
     *            apool
     * @throws IOException
     *             IOException
     */
    public AnalyseWorker(
                final SocketChannel channel, final Selector selector, final ExecutorService apool)
        throws IOException {

        super();
        this.channel = channel;
        this.pool = apool;
        channel.configureBlocking(false);

        this.readBuffer = new BufferInData();

        // On spécifie que le traitement doit commencer par une
        // lecture de la requête client
        this.state = State.READING;
        this.sk = channel.register(selector, 0);
        this.sk.attach(this);
        this.sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    /**
     * Lecture des données provenant de la socket.
     *
     * @param channelSock
     *            channel
     * @param readBuf
     *            readBuffer
     * @return le nombre d'octet lus
     * @throws IOException
     *             IOException
     */
    private int readData(final SocketChannel channelSock, final BufferInData readBuf) throws IOException {

        final ByteBuffer buffer = ByteBuffer.allocate(8192);
        int numRead = -1;

        numRead = channelSock.read(buffer);

        if (numRead != -1) {
            readBuf.add(buffer.array(), 0, numRead);
        }
        return numRead;
    }

    /**
     * Lecture des données de la socket.
     *
     * @return nombre d'octet lus
     * @throws IOException
     *             IOException
     */
    private int readStep() throws IOException {

        // lecture des données
        final int ret = this.readData(this.channel, this.readBuffer);
        if (this.readBuffer.isCmdReadden()) {
            AnalyseWorker.LOGGER.debug("Commande reçu");

        } else if (this.readBuffer.isStreamReadden()) {
            AnalyseWorker.LOGGER.debug("Flux à  analyser reçu");
        }

        return ret;
    }

    /**
     * Analyse des données reçues.
     *
     * @return vrai si toutes les données à  lire ont été lues.
     */
    private boolean postReadStep() {

        boolean ret;
        ret = this.readBuffer.processData();
        if (this.readBuffer.isCmdReadden()) {
            AnalyseWorker.LOGGER.debug("Commande reçue");

        } else if (this.readBuffer.isStreamReadden()) {
            AnalyseWorker.LOGGER.debug("Flux à analyser reçu");
        }
        return ret;
    }

    /**
     * Traitement du contenu du fichier et sélection de la réponse à  envoyer au client.
     */
    private void processStep() {

        final String data;

        // Reponse OK par defaut
        this.response = "1: stream: OK\n";

        // Recuperation du contenu du fichier
        data = this.readBuffer.getStreamAsString();

        if (data != null) {
            if ("KO".equals(data)) {
                this.response = "1: stream: KO FOUND XXX\n";
            } else if ("SERVICE_KO".equals(data)) {
                this.response = null;
            }
        }
    }

    /**
     * Envoi de la réponse au client.
     *
     * @return vrai si la socket doit être fermée
     * @throws IOException
     *             IOException
     */
    private boolean writeStep() throws IOException {

        AnalyseWorker.LOGGER.debug("writeStep");
        // renvoie la réponse
        if (this.response != null) {
            this.channel.write(ByteBuffer.wrap(this.response.getBytes("UTF-8")));
        }
        if (this.response == null) {
            AnalyseWorker.LOGGER.debug("Close sans réponse");
        } else {
            AnalyseWorker.LOGGER.debug("Réponse envoyée");
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        synchronized (this) {

            try {
                switch (this.state) {

                // Lecture des données de la socket
                    case READING:
                        if (this.readStep() != -1) {
                            this.state = State.PROCESS_CMD;
                            this.pool.execute(this);
                        }

                        break;

                    // Traitement du fichier envoyé par le client
                    case PROCESS_FLUX:
                        this.processStep();

                        this.state = State.WRITING;
                        this.sk.interestOps(SelectionKey.OP_WRITE);
                        AnalyseWorker.LOGGER.debug("process_flux->writing");
                        this.sk.selector().wakeup();

                        break;

                    // Envoi de la réponse au client
                    case WRITING:
                        this.writeStep();
                        this.closeAndRemoveChannel();

                        this.state = State.FINISH;
                        break;

                    // Traitement de la commande provenant du client
                    case PROCESS_CMD:
                        doProcessCmd();
                        break;

                    case FINISH:
                        if (this.sk.isValid()) {
                            AnalyseWorker.LOGGER.warn("Finish alors que la sk est valide");
                        }
                        break;

                    default:
                        AnalyseWorker.LOGGER.error("default condition state, bug ??");

                        break;
                }
            } catch (final IOException e) {
                AnalyseWorker.LOGGER.info("Arrêt ", e);

                // Fermeture de la socket
                this.closeAndRemoveChannel();
            }
        }
    }

    /**
     *
     */
    private void doProcessCmd() {

        if (this.postReadStep()) {
            this.state = State.PROCESS_FLUX;
            this.pool.execute(this);
        } else {
            this.state = State.READING;
            this.sk.interestOps(SelectionKey.OP_READ);
            AnalyseWorker.LOGGER.debug("process_cmd->reading");
            this.sk.selector().wakeup();
        }
    }

    /**
     * Ferme de façon sûre la socket sans lever d'erreur. Peut être appelée même si la scoket a déjà  été
     * fermé par le simulateur ou le client.
     */
    private void closeAndRemoveChannel() {

        final Socket socket = this.channel.socket();
        final SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        AnalyseWorker.LOGGER.debug("Connection closed by client: {}", remoteAddr);
        try {
            this.channel.close();
        } catch (final IOException e) {
            AnalyseWorker.LOGGER.info("Erreur lors de la fermeture, la socket était peut-être déjà  fermée",
                e);
        }
        this.sk.cancel();
    }
}
