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
package hornet.framework.metrologie;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 
 * Projet hornetserver.
 * 
 * @date 8 juin 2011
 * @author EffiTIC
 * 
 */
final class ChronometreSetMessageFormatter {

    /**
     * 
     */
    private static class StringComparator implements Comparator<String> {

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(final String left, final String right) {

            return left.compareTo(right);
        }

    }

    /**
     * 
     * Compare deux chaines à partir de leur position dans une liste.
     * 
     */
    private static final class ItemComparator implements Comparator<String> {

        /** */
        private transient List<String> itemOrder;

        /**
         * 
         * @param itemOrder
         *            L'ordre de tri des éléments.
         */
        private ItemComparator(
                    final List<String> itemOrder) {

            this.itemOrder = itemOrder;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(final String left, final String right) {

            int leftIndice = this.itemOrder.indexOf(left);

            if (leftIndice == -1) {
                leftIndice = Integer.MAX_VALUE;
            }

            int rightIndice = this.itemOrder.indexOf(right);

            if (rightIndice == -1) {
                rightIndice = Integer.MAX_VALUE;
            }

            if (leftIndice > rightIndice) {
                return 1;
            } else if (leftIndice < rightIndice) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Le comparateur utilisé pour déterminer l'ordre d'affichage des chronometres.
     */
    private transient Comparator<String> comparateur;

    /**
     * Constructeur.
     * 
     * @param ordreAffichageChronometre
     *            Une chaine indiquant l'ordre d'affichage des chronometres.
     * 
     */
    ChronometreSetMessageFormatter(
                final String ordreAffichageChronometre) {

        final List<String> listeOrdreAffichageChronometre =
                    StringHelper.split(ordreAffichageChronometre, ",");

        if (listeOrdreAffichageChronometre.isEmpty()) {
            this.comparateur = new StringComparator();
        } else {
            this.comparateur = new ItemComparator(listeOrdreAffichageChronometre);
        }

    }

    /**
     * 
     * @param chronometreSet
     *            Le chronometreSet à formatter.
     * @return Une chaine de caractères contenant les temps d'exécution mesurés par les chronomètres.
     */
    String format(final ChronometreSet chronometreSet) {

        final Map<String, Long> timesByChrono = chronometreSet.getChronoValues();

        final Map<String, Long> sortedTimeByChrono = new TreeMap<String, Long>(this.comparateur);
        sortedTimeByChrono.putAll(timesByChrono);

        final StringBuilder builder = new StringBuilder();

        String pipe = "";

        for (final Entry<String, Long> entry : sortedTimeByChrono.entrySet()) {
            builder.append(pipe);
            builder.append(String.format("%1$s=%2$d", entry.getKey(), entry.getValue()));
            pipe = "|";
        }

        return builder.toString();
    }

}
