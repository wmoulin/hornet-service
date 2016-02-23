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
package hornet.framework.export.vo.utils;

import hornet.framework.export.vo.presentation.ColVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hornet
 * @since 1.0 - 3 mars 2015
 */
public class TableVOUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TableVOUtils.class);

    /** Format numérique */
    public static final String FORMAT_NUMERIQUE = "NUMERIQUE";

    /** Format booléen */
    public static final String FORMAT_BOOLEEN = "BOOLEEN";

    /** Format date */
    public static final String FORMAT_DATE = "DATE";

    /**
     * Formatte la donnée d'une cellule.
     *
     * @param col
     *            Contenu de la cellule
     * @return Chaîne contenant la donnée formattée
     */
    public static String formaterCellule(final ColVO col) {

        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);

        String colValue = "";
        if (col.getValue() != null) {
            // Formattage en fonction du type
            if (col.getFormat() == null) {
                colValue = col.getValue().toString();
            } else {
                try {
                    if (col.getFormat().equals(FORMAT_DATE)) {
                        colValue = df.format((Date) col.getValue());
                    } else if (col.getFormat().equals(FORMAT_BOOLEEN)) {
                        if (((Boolean) col.getValue()).booleanValue()) {
                            colValue = "oui";
                        } else {
                            colValue = "non";
                        }
                    }
                } catch (final ClassCastException cce) {
                    LOG.warn("Erreur de conversion d'une valeur lors de l'export - Format : {} Type : {}",
                        col.getFormat(), col.getValue().getClass().getSimpleName());
                }
            }
        }
        return colValue;
    }

}
