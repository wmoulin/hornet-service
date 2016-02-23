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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Buffer de la response.
 */
public class BufferInData {

    /** Nombre de byte dans un int. */
    private static final int NB_BYTE_FOR_INT = 4;

    /** Constant for buffer size. */
    private static final int BUFFER_SIZE = 1024;

    /** Logger de la classe. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BufferInData.class);

    /** Data. */
    private transient ByteArrayOutputStream data;

    /** La commande a t-elle été reçue ?. */
    private transient boolean boolCmdReadden;

    /** Le fichier a t-il été lu ?. */
    private transient boolean boolStreamReaden;

    /** Taille du flux. */
    private transient int streamLength;

    /** Position de la taille du contenu du fichier dans le flux obtenu. */
    private transient int dataSizePositionInStream;

    /**
     * Catégorie de commande pouvant être traité par le simulateur.
     */
    public enum Command {

        /** The inconnu. */
        INCONNU,
        /** The ping. */
        PING,
        /** The analyse. */
        ANALYSE
    }

    /** Type de la commaned courante. */
    private transient Command command;

    /**
     * Constructeur.
     */
    public BufferInData() {

        super();
        this.data = new ByteArrayOutputStream(BUFFER_SIZE);
        this.streamLength = -1;
    }

    /**
     * Conversion d'un tableau d'octet en entier.
     *
     * @param b
     *            tableau d'octet
     * @param offset
     *            position de l'entier à lire
     * @return un entier
     */
    public static int byteArrayToInt(final byte[] b, final int offset) {

        final int byteMask = 0x000000FF;

        int value = 0;
        for (int i = 0; i < NB_BYTE_FOR_INT; i++) {
            final int shift = (NB_BYTE_FOR_INT - 1 - i) * 8;
            value += (b[i + offset] & byteMask) << shift;
        }
        return value;
    }

    /**
     * Adds the.
     *
     * @param newData
     *            the new data
     * @param offset
     *            the offset
     * @param len
     *            the len
     */
    public void add(final byte[] newData, final int offset, final int len) {

        this.data.write(newData, offset, len);
    }

    /**
     * analyse des données reçues pour vérifier si la commande et le flux éventuel ont bien été transmis.
     *
     * @return vrai si toutes les données à lire ont été lues.
     */
    public boolean processData() {

        boolean ret;
        ret = false;
        if (!this.boolCmdReadden) {

            final String sData =
                        new String(this.data.toByteArray(), 0, this.data.size(), Charset.defaultCharset());
            if (sData.startsWith("nPING\n")) {
                this.command = Command.PING;
                this.boolCmdReadden = true;
                BufferInData.LOGGER.debug("PING trouvé");
                ret = true;
            } else if (sData.startsWith("nIDSESSION\nnINSTREAM\n")) {
                this.command = Command.ANALYSE;
                BufferInData.LOGGER.debug("INSTREAM trouvé");
                this.boolCmdReadden = true;
                this.dataSizePositionInStream = "nIDSESSION\nnINSTREAM\n".length();
            }
        }
        if (this.boolCmdReadden && this.command == Command.ANALYSE) {
            doAnalyseCommand();

            // a-t-on tout recu ?
            if (this.streamLength != -1
                        && this.data.size() >= this.dataSizePositionInStream + NB_BYTE_FOR_INT
                                    + this.streamLength + NB_BYTE_FOR_INT) {
                BufferInData.LOGGER.info("Flux reçu");
                this.boolStreamReaden = true;
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Analyse du fichier, on récupère le stream
     */
    private void doAnalyseCommand() {

        // les 4 octets donnant la taille du flux n'ont pas encore
        // été décodés
        if (this.streamLength == -1) {
            readStreamToByteArray();
        }

        if (BufferInData.LOGGER.isInfoEnabled()) {
            BufferInData.LOGGER.info(String.format("Avancement %.2f ", (float) this.data.size()
                        / this.streamLength));

        }
    }

    /**
     * Read stream to byte array.
     */
    private void readStreamToByteArray() {

        // au moins 4 octets reçus.
        if (this.data.size() - this.dataSizePositionInStream >= NB_BYTE_FOR_INT) {

            final byte[] dt = this.data.toByteArray();
            this.streamLength = BufferInData.byteArrayToInt(dt, this.dataSizePositionInStream);

            final ByteArrayOutputStream ndata =
                        new ByteArrayOutputStream(this.streamLength + this.dataSizePositionInStream
                                    + BUFFER_SIZE);
            try {
                this.data.writeTo(ndata);
            } catch (final IOException e) {
                this.streamLength = -1;
            }
            this.data = ndata;

            if (BufferInData.LOGGER.isInfoEnabled()) {
                BufferInData.LOGGER.info("Taille du flux {} bytes {} {} {} {} ", this.streamLength,
                    dt[this.dataSizePositionInStream], dt[this.dataSizePositionInStream + 1],
                    dt[this.dataSizePositionInStream + 2], dt[this.dataSizePositionInStream + NB_BYTE_FOR_INT
                                - 1]);
            }

        }
    }

    /**
     * Getter du champ isStreamReadden.
     *
     * @return boolean
     */
    public boolean isStreamReadden() {

        return this.boolStreamReaden;
    }

    /**
     * Getter du champ isCmdReadden.
     *
     * @return boolean
     */
    public boolean isCmdReadden() {

        return this.boolCmdReadden;
    }

    /**
     * Récupération du contenu du fichier envoyé.
     *
     * @return contenu du fichier
     */
    public String getStreamAsString() {

        String ret = null;
        final byte[] bData = this.data.toByteArray();

        if (BufferInData.LOGGER.isDebugEnabled()) {
            BufferInData.LOGGER.debug(String.format("Stream content  %d %d", this.dataSizePositionInStream,
                bData.length));
        }
        // on copie la chaine sans les 4 octets donnant la taille du
        // flux et sans les 4 octets (à zéro )donnant la fin du flux et
        // sans le \r\n soit -10
        // dans le cas d'une réponse vide, la taille est négative
        final int offset = 10;
        final int nbByteforInt = 4;

        final int tailleReponse = bData.length - this.dataSizePositionInStream - offset;
        if (tailleReponse > 0) {
            ret =
                        new String(bData, this.dataSizePositionInStream + nbByteforInt, tailleReponse,
                                    Charset.defaultCharset());
        }
        return ret;
    }

    /**
     * Récupération de la taille du contenu du fichier.
     *
     * @return taille du contenu du fichier en octet
     */
    public int getStreamLength() {

        return this.getStreamAsString().length();
    }
}
