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
package hornet.framework.web.table;

import java.util.List;

/**
 * @author MAEDI
 * @since 1.0 - 2 mars 2015
 */
public class Table<T> {

    // Nombre total d'éléments correspondant au filtre
    private int nbTotal;

    // Liste tronquée d'éléments conformément à la pagination demandée
    private List<T> liste;

    // Eléments de pagination
    private Pagination pagination;

    /**
     * @return nbTotal
     */
    public int getNbTotal() {

        return nbTotal;
    }

    /**
     * @param nbTotal
     *            nbTotal
     */
    public void setNbTotal(final int nbTotal) {

        this.nbTotal = nbTotal;
    }

    /**
     * @return liste
     */
    public List<T> getListe() {

        return liste;
    }

    /**
     * @param liste
     *            liste
     */
    public void setListe(final List<T> liste) {

        this.liste = liste;
    }

    /**
     * @return pagination
     */
    public Pagination getPagination() {

        return pagination;
    }

    /**
     * @param pagination
     *            pagination
     */
    public void setPagination(final Pagination pagination) {

        this.pagination = pagination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("Table [nbTotal=").append(nbTotal).append(", liste=").append(liste)
                    .append(", pagination=").append(pagination).append("]");
        return builder.toString();
    }

}
