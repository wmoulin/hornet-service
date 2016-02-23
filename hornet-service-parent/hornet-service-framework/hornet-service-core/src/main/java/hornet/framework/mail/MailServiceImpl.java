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

import hornet.framework.exception.BusinessException;
import hornet.framework.template.VelocityHelper;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * The Class MailServiceImpl.
 *
 * @author MAE - S. LEDUBY
 */
public class MailServiceImpl implements MailService {

    /** champ d'entete smtp "Reply-to". */
    public static final String SMTP_HEADER_REPLYTO = "smtp.header.replyto";

    /** champ d'entete smtp "Bcc". */
    public static final String SMTP_HEADER_BCC = "smtp.header.bcc";

    /** champ d'entete smtp "Cc". */
    public static final String SMTP_HEADER_CC = "smtp.header.cc";

    /** champ d'entete smtp "Priority". */
    public static final String SMTP_HEADER_PRIORITY = "smtp.header.priority";

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

    /** The Constant DEFAULT_MESSAGE_TEMPLATE. */
    private static final MessageFormat DEFAULT_MESSAGE_TEMPLATE = new MessageFormat("<HTML>" //
                + "<HEAD><META http-equiv=\"Content-Type\"" //
                + " content=\"text/html; charset=utf-8\"></HEAD>" //
                + "<BODY>{0}</BODY>" //
                + "</HTML>");

    /** JavaMailSender. */
    private final transient JavaMailSender mailSender;

    /** Nom de l'application envoyant les mails. */
    private final transient String nomApplication;

    /** Nom de domaine à utiliser pour le MessageID de l'entete SMTP. */
    private final transient String messageIdDomainName;

    /**
     * Constructeur.
     *
     * @param mailSender
     *            JavaMailSender Spring
     * @param nomApplication
     *            Nom de l'application à l'origine de l'envoi du mail
     * @param messageIdDomainName
     *            Nom de domaine à utiliser pour le MessageID de l'entete SMTP
     */
    public MailServiceImpl(
                final JavaMailSender mailSender, final String nomApplication, final String messageIdDomainName) {

        this.mailSender = mailSender;
        this.nomApplication = nomApplication;
        this.messageIdDomainName = messageIdDomainName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void envoyer(final String expediteur, final String sujet, final String message,
                final Map<String, Object> paramMap, final String... destinataires) {

        try {
            final Session session = ((JavaMailSenderImpl) this.mailSender).getSession();
            HornetMimeMessage mMessage = null;
            if (this.messageIdDomainName == null || this.messageIdDomainName.length() == 0) {
                mMessage = new HornetMimeMessage(this.nomApplication, session);
            } else {
                mMessage = new HornetMimeMessage(this.nomApplication, this.messageIdDomainName, session);
            }

            mMessage.setHeader("Content-Type", "text/html");
            final MimeMessageHelper helper = new MimeMessageHelper(mMessage, true, CharEncoding.UTF_8);

            addExtraSmtpField(paramMap, helper);

            helper.setFrom(expediteur);

            this.ajouterDestinataires(helper, destinataires);

            helper.setSubject(sujet);
            // message aux formats texte et html
            helper.setText(this.preparerMessageTexte(message), this.preparerMessageHTML(message));

            this.mailSender.send(mMessage);
        } catch (final MailSendException mse) {
            throw this.toBusinessException(mse);
        } catch (final Exception e) {
            throw new BusinessException("erreur.envoi.courriel", e);
        }
    }

    /**
     * Adds the extra smtp field.
     *
     * @param paramMap
     *            the param map
     * @param helper
     *            the helper
     * @throws MessagingException
     *             the messaging exception
     */
    private void addExtraSmtpField(final Map<String, Object> paramMap, final MimeMessageHelper helper)
        throws MessagingException {

        if (paramMap != null) {
            if (paramMap.containsKey(SMTP_HEADER_REPLYTO)) {
                helper.setReplyTo((String) paramMap.get(SMTP_HEADER_REPLYTO));
            }
            if (paramMap.containsKey(SMTP_HEADER_BCC)) {
                helper.setBcc((String) paramMap.get(SMTP_HEADER_BCC));
            }
            if (paramMap.containsKey(SMTP_HEADER_CC)) {
                helper.setCc((String) paramMap.get(SMTP_HEADER_CC));
            }
            if (paramMap.containsKey(SMTP_HEADER_PRIORITY)) {
                helper.setPriority((Integer) paramMap.get(SMTP_HEADER_PRIORITY));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void envoyerDepuisModele(final String expediteur, final String sujet,
                final String messageTemplate, final Map<String, Object> paramMap,
                final String... destinataires) {

        String corpsMail = messageTemplate;
        String message;
        try {
            if (messageTemplate.endsWith(".vm")) {
                final InputStream is = this.getClass().getResourceAsStream("/" + messageTemplate);
                corpsMail = IOUtils.toString(is, "UTF-8");
            }

            message = VelocityHelper.renderVelocityFragment(corpsMail, paramMap);
        } catch (final IOException e) {
            throw new BusinessException("erreur.envoi.courriel", e);
        }
        this.envoyer(expediteur, sujet, message, paramMap, destinataires);
    }

    /**
     * Converti l'exception fourni en {@link BusinessException}.
     *
     * @param mse
     *            MailSendException
     * @return Une {@link BusinessException}
     */
    private BusinessException toBusinessException(final MailSendException mse) {

        MailServiceImpl.LOG.debug("Conversion d'une erreur d'envoi de courriel");

        final List<String> adressesInvalides = new ArrayList<String>();
        for (final Exception exception : mse.getMessageExceptions()) {
            if (exception instanceof SendFailedException) {
                final SendFailedException sendFailedEx = (SendFailedException) exception;
                for (final Address adresseInvalide : sendFailedEx.getInvalidAddresses()) {
                    adressesInvalides.add(adresseInvalide.toString());
                    MailServiceImpl.LOG.debug(String.format("adresse invalide : %s", adresseInvalide));
                }
            } else if (exception instanceof AuthenticationFailedException) {
                final AuthenticationFailedException authenticationFailedEx =
                            (AuthenticationFailedException) exception;
                return new BusinessException("erreur.envoi.courriel.authentification", authenticationFailedEx);
            }
        }

        if (adressesInvalides.isEmpty()) {
            return new BusinessException("erreur.envoi.courriel", mse);
        } else {
            return new BusinessException("erreur.envoi.courriel.addressesInvalides",
                        new String[]{StringUtils.join(adressesInvalides, ", ")}, mse);
        }
    }

    /**
     * Ajouter destinataires.
     *
     * @param helper
     *            Composant de préparation de message
     * @param destinataires
     *            Les destinataires
     * @throws MessagingException
     *             En cas d'invalidité d'adresse de courriel
     */
    private void ajouterDestinataires(final MimeMessageHelper helper, final String... destinataires)
        throws MessagingException {

        helper.setTo(destinataires);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String preparerMessageTexte(final String message) {

        String msg = message;
        if (StringUtils.isNotBlank(msg)) {
            // suppression des balises HTML
            msg = msg.replaceAll("<br\\s*/?>", "\n"); // <br>, <br/>, <br /> -> \n
            msg = msg.replaceAll("</?p[^>]*>", "\n"); // <p>, <p/>, <p />, </p> -> \n
            // <a href="url">texte</a> -> texte (url)
            msg = msg.replaceAll("<a\\s[^>]*href=['\"]([^'\"]*)['\"][^>]*>(.*)</a[^>]*>", "$2 ($1)");
            msg = msg.replaceAll("<[^>]+>", ""); // balise ouvrante ou fermante <ppp> ou </ppp>
            msg = msg.replaceAll("&nbsp;", " ");
            msg = msg.replaceAll("^\n+", ""); // \n au début
            msg = msg.replaceAll("\n+$", ""); // \n à la fin
        }

        return msg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String preparerMessageHTML(final String message) {

        String msg = message;
        if (StringUtils.isNotBlank(msg)) {
            // Encapsulation du message dans le code HTML
            msg = MailServiceImpl.DEFAULT_MESSAGE_TEMPLATE.format(new Object[]{msg});
        }

        return msg;
    }

}
