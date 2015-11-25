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
package hornet.framework.mail;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Message permettant la surcharge du champ d'en-tête "Message-ID"
 */
public class HornetMimeMessage extends MimeMessage {

    /** base pour horodatage en millisecondes en base 36 */
    private static final int BASE_36 = 36;

    /** nom de domaine par défaut pour la génération du Message-ID */
    public static final String DEFAULT_DOMAIN_NAME = "hornet.framework";

    /**
     * Message-ID
     */
    private final transient String messageId;

    /**
     * Constructeur
     * 
     * @param codeAppli
     *            code de l'application
     * @param session
     *            Session
     */
    public HornetMimeMessage(
                final String codeAppli, final Session session) {

        this(codeAppli, DEFAULT_DOMAIN_NAME, session);
    }

    /**
     * Constructeur
     * 
     * @param codeAppli
     *            code de l'application
     * @param domaine
     *            nom de domaine
     * @param session
     *            Session
     */
    public HornetMimeMessage(
                final String codeAppli, final String domaine, final Session session) {

        super(session);
        this.messageId = this.genererMessageID(codeAppli, domaine);
    }

    /**
     * Génère un identifiant de message unique selon le format préconisé par le guide des bonnes pratique pour
     * la lutte contre le spam
     * 
     * @param codeAppli
     *            code de l'application
     * @param domaine
     *            nom de domaine
     * @return Identifiant de message unique
     */
    private String genererMessageID(final String codeAppli, final String domaine) {

        final long time = Calendar.getInstance().getTimeInMillis();

        final StringBuilder sb = new StringBuilder();
        sb.append(Long.toString(time, BASE_36).toUpperCase(Locale.FRENCH));
        sb.append('.');
        sb.append(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(Locale.FRENCH));
        sb.append('.');
        sb.append(codeAppli);
        sb.append('@');
        sb.append(domaine);

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateMessageID() throws MessagingException {

        setHeader("Message-ID", this.messageId);
    }
}
