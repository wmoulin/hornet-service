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

import java.util.Map;

/**
 * @author MAE - S. LEDUBY
 * @since 1.0 - 11 févr. 2013
 */
public interface MailService {

    /**
     * @param expediteur
     *            Adresse de courriel de l'expéditeur
     * @param sujet
     *            Sujet du courriel
     * @param message
     *            Corps du message
     * @param paramMap
     *            le paramètres du template
     * @param destinataires
     *            Adresses de courriel des destinataires
     */
    void envoyer(final String expediteur, final String sujet, final String message,
                final Map<String, Object> paramMap, final String... destinataires);

    /**
     * @param expediteur
     *            Adresse de courriel de l'expéditeur
     * @param sujet
     *            Sujet du courriel
     * @param messageTemplate
     *            Template corps du message OU chemin vers un fichier template velocity
     * @param paramMap
     *            le paramètres du template
     * @param destinataires
     *            Adresses de courriel des destinataires
     */
    void envoyerDepuisModele(final String expediteur, final String sujet, final String messageTemplate,
                final Map<String, Object> paramMap, final String... destinataires);

    /**
     * @param message
     *            Texte du message
     * @return Message HTML
     */
    String preparerMessageTexte(final String message);

    /**
     * @param message
     *            Texte du message
     * @return Message HTML
     */
    String preparerMessageHTML(final String message);

}
