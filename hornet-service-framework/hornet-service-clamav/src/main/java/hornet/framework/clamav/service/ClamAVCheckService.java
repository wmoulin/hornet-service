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
package hornet.framework.clamav.service;

import hornet.framework.clamav.bo.ClamAvResponse;
import hornet.framework.clamav.bo.TypeResponse;
import hornet.framework.clamav.exception.ClamAVException;
import hornet.framework.clamav.pool.PooledSocketFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cette classe propose des méthodes pour simplifier les tests antivirus, via ClamAV, sur des fichiers reçus
 * (suite a un upload ou non)
 *
 * - par flux TCP : On envoi le fichier directement comme flux TCP.
 *
 * @author EffiTIC
 */
public final class ClamAVCheckService {

    /** Charset */
    private static final String UTF_8 = "UTF-8";

    /** Message d'erreur. */
    private static final String ERROR_ON_COMMAND = "Erreur lors de l'envoi de la Commande '%s' à l'antivirus";

    /** Constant for buffer size. */
    private static final int BUFFER_SIZE = 1024;

    /** Code d'erreur technique CLAMAV. */
    private static final String ERR_TEC_CLAMAV_01 = "ERR-TEC-CLAMAV-01";

    /** Logger de la classe. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClamAVCheckService.class);

    /**
     * Délai par défaut pour les réponses en millisecondes.
     */
    private static final int DEFAULT_TIMEOUT = 1000;

    /**
     * Délai par défaut pour les connexions en millisecondes.
     */
    private static final int DEFAULT_TIMEOUT_CONNECT = 1000;

    /** Port par défaut. */
    private static final int DEFAULT_PORT = 3310;

    /** Chaine de caractère représentant la commande. */
    private static final String COMMANDE = "nIDSESSION\nnINSTREAM\n";

    /**
     * Contient l'url d'accès au serveur clamAV.
     */
    private String clamAVServer = "";

    /**
     * Contient le port d'accès au serveur clamAV.
     */
    private int clamAVPort = ClamAVCheckService.DEFAULT_PORT;

    /** Délai accordé à ClamAV pour répondre en milli secondes. */
    private int timeout = ClamAVCheckService.DEFAULT_TIMEOUT;

    /** Délai accordé à ClamAV lors des connexions en milli secondes. */
    private int connectTimeout = ClamAVCheckService.DEFAULT_TIMEOUT_CONNECT;

    /** Le pool de socket. */
    private final transient ObjectPool<SocketChannel> socketPool;

    /**
     * Constructeur.
     */
    public ClamAVCheckService() {

        super();
        this.socketPool = new GenericObjectPool<SocketChannel>(new PooledSocketFactory());
    }

    /**
     * Cette méthode retourne la version de ClamAV.
     *
     * @return la version de clamAV
     * @throws ClamAVException
     *             the clam av exception
     */
    public ClamAvResponse checkClamAVVersion() throws ClamAVException {

        return this.callClamAV(TypeResponse.VERSION, "CLamAV");
    }

    /**
     * Cette méthode retourne les statistiques courantes du serveur ClamAV.
     *
     * @return les statistiques du serveur ClamAV
     * @throws ClamAVException
     *             the clam av exception
     */
    public ClamAvResponse checkClamAVStats() throws ClamAVException {

        return this.callClamAV(TypeResponse.STATS, "END");
    }

    /**
     * Cette méthode permet de tester un fichier par envoi dans un flux TCP du contenu de ce fichier.
     *
     * @param fileForTest
     *            - le fichier à tester
     * @return la chaine "OK" si pas de virus, "le nom du virus" en cas de détection du virus ou la chaine
     *         retourné par clamav en cas d'erreur clamav
     * @throws ClamAVException
     *             Exception en cas d'erreur
     */
    public ClamAvResponse checkByStream(final File fileForTest) throws ClamAVException {

        final StringBuilder resultat = new StringBuilder();
        ClamAvResponse result = null;

        // Récupération de la taille du fichier
        final long fileForTestSize = fileForTest.length();
        SocketChannel channel = null;
        try {
            ClamAVCheckService.LOGGER.debug("Fichier a tester : '{}' ({} octets)",
                fileForTest.getAbsolutePath(), fileForTestSize);

            // Récupération du fichier à stream
            final MappedByteBuffer bufFileForTestRead = this.recupFichierStream(fileForTest, fileForTestSize);

            // Ouverture de la socket
            channel = this.openSocket();
            this.readAndSendFile(resultat, fileForTestSize, channel, bufFileForTestRead);

            ClamAVCheckService.LOGGER.debug("Retour ClamAV {}", resultat);
        } catch (final SocketTimeoutException e) {
            ClamAVCheckService.LOGGER.error(String.format(ERROR_ON_COMMAND, ClamAVCheckService.COMMANDE), e);
            result = ClamAvResponse.createTimeoutResponse();
        } catch (final UnknownHostException e) {
            ClamAVCheckService.LOGGER.error(String.format(ERROR_ON_COMMAND, ClamAVCheckService.COMMANDE), e);
            throw new ClamAVException(ERR_TEC_CLAMAV_01, new String[]{e.getMessage()}, e);
        } catch (final IOException e) {
            ClamAVCheckService.LOGGER.error(String.format(ERROR_ON_COMMAND, ClamAVCheckService.COMMANDE), e);
            throw new ClamAVException(ERR_TEC_CLAMAV_01, new String[]{e.getMessage()}, e);
        } finally {
            closeSocket(channel);
        }

        // ce qui veut dire que l'analyse s'est déroulée sans erreur
        if (result == null && resultat.length() > 0) {
            result = this.traitementReponseClamAV(resultat.toString(), TypeResponse.VIRUS);
        }

        return result;
    }

    /**
     * Ouverture de la socket.
     *
     * @return la socket
     * @throws ClamAVException
     *             the clam av exception
     */
    private SocketChannel openSocket() throws ClamAVException {

        SocketChannel channel = null;
        try {
            // Récuperation de la socket depuis le pool
            channel = this.socketPool.borrowObject();

            if (!channel.isOpen()) {
                channel = SocketChannel.open();
            }
            if (!channel.isConnected()) {
                channel.configureBlocking(true);
                channel.connect(new InetSocketAddress(this.clamAVServer, this.clamAVPort));
            }
        } catch (final Exception e) {
            ClamAVCheckService.LOGGER.error("Unable to borrow socket from pool", e);
            throw new ClamAVException(ERR_TEC_CLAMAV_01, new String[]{e.getMessage()}, e);
        }

        return channel;
    }

    /**
     * Fermeture de la socket.
     *
     * @param channel
     *            la socket
     */
    private void closeSocket(final SocketChannel channel) {

        try {
            if (channel != null) {
                this.socketPool.invalidateObject(channel);
            }
        } catch (final IllegalStateException e) {
            // nothing special to do
            ClamAVCheckService.LOGGER.debug("Object state", e);
        } catch (final Exception e) {
            ClamAVCheckService.LOGGER.error(
                "Erreur lors de la fermeture de la socket pendant l'envoi de la Commande '{}' à l'antivirus",
                ClamAVCheckService.COMMANDE, e);
        }
    }

    /**
     * Lecture du fichier et envoi sur la socket.
     *
     * @param resultat
     *            resultat
     * @param fileForTestSize
     *            fileForTestSize
     * @param channel
     *            channel
     * @param bufFileForTestRead
     *            bufFileForTestRead
     * @throws IOException
     *             IOException
     */
    private void readAndSendFile(final StringBuilder resultat, final long fileForTestSize,
                final SocketChannel channel, final MappedByteBuffer bufFileForTestRead) throws IOException {

        // Envoi de la commande
        final ByteBuffer writeReadBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        writeReadBuffer.put(ClamAVCheckService.COMMANDE.getBytes(UTF_8));
        writeReadBuffer.put(this.intToByteArray((int) fileForTestSize));
        writeReadBuffer.flip();
        channel.write(writeReadBuffer);

        // Envoi du fichier

        long size = fileForTestSize;
        // envoi du fichier
        while (size > 0) {
            size -= channel.write(bufFileForTestRead);
        }
        final ByteBuffer writeBuffer = ByteBuffer.allocate(4);
        writeBuffer.put(new byte[]{
            0,
            0,
            0,
            0});
        writeBuffer.flip();
        channel.write(writeBuffer);

        // lecture de la réponse
        ByteBuffer readBuffer;
        readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        // lecture de la réponse
        readBuffer.clear();
        boolean readLine = false;
        while (!readLine) {
            final int numReaden = channel.read(readBuffer);
            if (numReaden > 0) {
                readLine = readBuffer.get(numReaden - 1) == '\n';
                resultat.append(new String(readBuffer.array(), 0, numReaden, UTF_8));
                readBuffer.clear();
            } else {
                if (numReaden == -1) {
                    readLine = true;
                    readBuffer.clear();
                }
            }
        }
    }

    /**
     * Méthode de traitement de la réponse à renvoyer.
     *
     * @param resultat
     *            le résultat ClamAV
     * @param type
     *            le type de réponse ClamAV
     * @return le résultat
     */
    private ClamAvResponse traitementReponseClamAV(final String resultat, final TypeResponse type) {

        ClamAvResponse response = null;
        // traitement du retour de ClamAV

        switch (type) {
            case VIRUS:
                response = ClamAvResponse.createVirusResponse(resultat);
                break;
            case STATS:
                response = ClamAvResponse.createStatsResponse(resultat);
                break;
            case VERSION:
                response = ClamAvResponse.createVersionResponse(resultat);
                break;
            default:
                break;
        }

        return response;
    }

    /**
     * Récupération du fichier à stream.
     *
     * @param fileForTest
     *            fichier de test
     * @param fileForTestSize
     *            taille du fichier
     * @return MappedByteBuffer
     * @throws IOException
     *             probleme de lecture
     */
    private MappedByteBuffer recupFichierStream(final File fileForTest, final long fileForTestSize)
        throws IOException {

        // Récupération du fichier à stream
        final RandomAccessFile raf = new RandomAccessFile(fileForTest, "r");
        final FileChannel readChannel = raf.getChannel();
        MappedByteBuffer bufFile = null;
        try {
            bufFile = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileForTestSize);
        } finally {
            if (readChannel != null) {
                try {
                    readChannel.close();
                } catch (final IOException e) {
                    ClamAVCheckService.LOGGER.error("Erreur lors de la fermeture de la socket", e);
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (final IOException e) {
                    ClamAVCheckService.LOGGER.error("Erreur lors de la fermeture de la socket", e);
                }
            }
        }
        return bufFile;
    }

    /**
     * Cette méthode transforme un integer en un tableau de 4 bytes.
     *
     * @param value
     *            'entier à transformé
     * @return le tableau de byte représentatnt l'integer non signé
     */
    private byte[] intToByteArray(final int value) {

        final int shiftForFirstByte = 24;
        final int shiftForSecondByte = 16;
        final int shiftForThirdByte = 8;

        return new byte[]{
            (byte) (value >>> shiftForFirstByte),
            (byte) (value >>> shiftForSecondByte),
            (byte) (value >>> shiftForThirdByte),
            (byte) value};
    }

    /**
     * Cette méthode permet l'envoie d'une commande à ClamAV et retourne le résultat sous la forme d'une
     * chaine de caractère.
     *
     * @param command
     *            - la commade à transmettre
     * @param strEndDetection
     *            - la chaine de caractère permettant de détecter la dernière ligne du retour.
     * @return La réponse de clamAV sans traitement
     * @throws ClamAVException
     *             the clam av exception
     */
    private ClamAvResponse callClamAV(final TypeResponse command, final String strEndDetection)
        throws ClamAVException {

        ClamAvResponse result;

        final StringBuilder resultat = new StringBuilder();
        final Date dateDebut = new Date();
        Socket socket = null;
        BufferedReader buffer = null;

        try {
            socket = this.connect(command);
            if (socket == null) {
                result = ClamAvResponse.createNoServiceResponse();
            } else {
                // timeout pour l'ensemble des opérations sur la
                // socket
                socket.setSoTimeout(this.timeout);
                final InputStream input = socket.getInputStream();
                final OutputStream ouput = socket.getOutputStream();

                // envoi de la commande à clamAV
                ouput.write(("n" + command.toString() + "\n").getBytes(UTF_8));

                // Attente et traitement de la réponse
                buffer = new BufferedReader(new InputStreamReader(input, UTF_8));

                String retour = "";
                int indexResultat = -1;
                while (retour != null && indexResultat == -1) {
                    retour = buffer.readLine();
                    if (retour != null) {
                        indexResultat = retour.indexOf(strEndDetection);
                        resultat.append(retour);
                        resultat.append('\n');
                    }
                }
                ClamAVCheckService.LOGGER.debug("Retour ClamAV (en {} ms) :\n{}", new Date().getTime()
                            - dateDebut.getTime(), resultat);
                result = this.traitementReponseClamAV(resultat.toString(), command);
            }
        } catch (final UnknownHostException e) {
            ClamAVCheckService.LOGGER.error(String.format(ERROR_ON_COMMAND, command), e);
            throw new ClamAVException(ERR_TEC_CLAMAV_01, new String[]{e.getMessage()}, e);
        } catch (final IOException e) {
            ClamAVCheckService.LOGGER.error(String.format(ERROR_ON_COMMAND, command), e);
            throw new ClamAVException(ERR_TEC_CLAMAV_01, new String[]{e.getMessage()}, e);
        } finally {
            this.safeClose(command, socket, buffer);
        }

        return result;
    }

    /**
     * Fermeture sécurisé de la socket.
     *
     * @param command
     *            command
     * @param socket
     *            socket
     * @param buffer
     *            buffer
     */
    private void safeClose(final TypeResponse command, final Socket socket, final BufferedReader buffer) {

        try {
            if (buffer != null) {
                buffer.close();
            }
        } catch (final IOException e) {
            ClamAVCheckService.LOGGER.error(
                "Problème de fermeture du buffer lors de l'envoi de la commande'{}' à l'antivirus", command,
                e);
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (final IOException e) {
            ClamAVCheckService.LOGGER.error(
                "Problème de fermeture de la socket lors de l'envoi de la commande'{}' à l'antivirus",
                command, e);
        }
    }

    /**
     * Ouvre la socket avec délai max.
     *
     * @param command
     *            le type de réponse ClamAV
     * @return null si le délai imparti à la connexion a été atteint
     * @throws IOException
     *             si erreur autre que timeout atteint
     */
    private Socket connect(final TypeResponse command) throws IOException {

        Socket socket = null;
        ClamAVCheckService.LOGGER.debug("Ouverture du socket pour la commande : '{}'", command);

        final InetSocketAddress hostport = new InetSocketAddress(this.clamAVServer, this.clamAVPort);
        socket = new Socket();
        // connexion avec timeout
        try {
            socket.connect(hostport, this.connectTimeout);
        } catch (final ConnectException e) {
            ClamAVCheckService.LOGGER.error("Timeout système lors de la connexion ", e);
            return null;
        } catch (final SocketTimeoutException e) {
            ClamAVCheckService.LOGGER.error("Timeout atteint lors de la connexion ", e);
            return null;
        }
        return socket;
    }

    /**
     * Getter pour clamAVServer.
     *
     * @return la valeur de clamAVServer
     */
    public String getClamAVServer() {

        return this.clamAVServer;
    }

    /**
     * Setter pour clamAVServer.
     *
     * @param clamAVServer
     *            le serveur
     */
    public void setClamAVServer(final String clamAVServer) {

        this.clamAVServer = clamAVServer;
    }

    /**
     * Getter pour clamAVPort.
     *
     * @return la valeur de clamAVPort
     */
    public int getClamAVPort() {

        return this.clamAVPort;
    }

    /**
     * Setter pour clamAVPort.
     *
     * @param clamAVPort
     *            la valeur du port
     */
    public void setClamAVPort(final int clamAVPort) {

        this.clamAVPort = clamAVPort;
    }

    /**
     * Getter pour timeout.
     *
     * @return la valeur de timeout
     */
    public int getTimeout() {

        return this.timeout;
    }

    /**
     * Setter pour timeout.
     *
     * @param readTimeout
     *            la valeur du timeout
     */
    public void setTimeout(final int readTimeout) {

        this.timeout = readTimeout;
    }

    /**
     * Getter pour connectTimeout.
     *
     * @return la valeur de connectTimeout
     */
    public int getConnectTimeout() {

        return this.connectTimeout;
    }

    /**
     * Setter pour connectTimeout.
     *
     * @param connectTimeout
     *            connectTimeout
     */
    public void setConnectTimeout(final int connectTimeout) {

        this.connectTimeout = connectTimeout;
    }

}
