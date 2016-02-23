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

import java.io.Serializable;

/**
 * Classe permettant de stocker les données de pagination pour le composant datatable.
 */
public class Pagination implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 4271776656947747254L;

    /** Index de la page courrante. */
    private int pageIndex;

    /** Nombre d'éléments par page. */
    private int itemsPerPage;

    /**
     * Constructeur.
     */
    public Pagination() {

        this.pageIndex = 1;
    }

    /**
     * Gets the page index.
     *
     * @return Returns the pageIndex.
     */
    public int getPageIndex() {

        return this.pageIndex;
    }

    /**
     * Sets the page index.
     *
     * @param pageIndex
     *            The pageIndex to set.
     */
    public void setPageIndex(final int pageIndex) {

        this.pageIndex = pageIndex;
    }

    /**
     * Gets the items per page.
     *
     * @return Returns the itemsPerPage.
     */
    public int getItemsPerPage() {

        return this.itemsPerPage;
    }

    /**
     * Sets the items per page.
     *
     * @param itemsPerPage
     *            The itemsPerPage to set.
     */
    public void setItemsPerPage(final int itemsPerPage) {

        this.itemsPerPage = itemsPerPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("Pagination [pageIndex=").append(pageIndex).append(", itemsPerPage=")
                    .append(itemsPerPage).append("]");
        return builder.toString();
    }
}
