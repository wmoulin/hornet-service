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
package hornet.framework.metrologie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StringHelper {

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * 
     * @param str
     *            the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     */
    public static boolean isBlank(final String str) {

        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Scinde une chaine de caractères en sous-chaines de caractères.
     * </p>
     * 
     * @param str
     *            La chaine à scinder.
     * @param separator
     *            Le separateur de chaine.
     * @return Une liste contenant les sous-chaines de caractères ou une liste vide si
     *         <code>str</tt> est null ou vide.
     */
    public static List<String> split(final String str, final String separator) {

        if (isBlank(str)) {
            return Collections.emptyList();
        }

        final List<String> result = new ArrayList<String>();
        final String[] tokens = str.trim().split(separator);

        for (final String token : tokens) {
            if (!isBlank(token)) {
                result.add(token.trim());
            }
        }

        return result;
    }

    /**
     * Constructeur privé
     */
    private StringHelper() {

        // constructeur privé
    }
}
