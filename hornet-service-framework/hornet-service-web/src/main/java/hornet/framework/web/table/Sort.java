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
 * Classe permettant de stocker les données de tri pour le composant datatable.
 */
public class Sort implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 2554177432668230295L;

    /**
     * The Enum SORT.
     */
    public enum SORT {

        /** The asc. */
        ASC,

        /** The desc. */
        DESC;

        /**
         * Return the enum SORT corresponding with the given value, SORT.ASC by default.
         *
         * @param value
         *            String
         * @return SORT
         */
        public static SORT get(final String value) {

            SORT dirSort = Sort.SORT.ASC;
            if (value != null && Sort.SORT.DESC.toString().equalsIgnoreCase(value)) {
                dirSort = Sort.SORT.DESC;
            }
            return dirSort;
        }
    };

    /** <code>dir</code> the dir. */
    private String dir;

    /** <code>key</code> the key. */
    private String key;

    /**
     * Gets the dir.
     *
     * @return Returns the dir.
     */
    public String getDir() {

        return this.getDirSort().toString();
    }

    /**
     * Setter of the dir.
     *
     * @param dir
     *            The dir to set.
     */
    public final void setDir(final String dir) {

        this.dir = dir;
    }

    /**
     * Gets the key.
     *
     * @return Returns the key.
     */
    public String getKey() {

        return this.key;
    }

    /**
     * Setter of the key.
     *
     * @param key
     *            The key to set.
     */
    public final void setKey(final String key) {

        this.key = key;
    }

    /**
     * Gets the dir sort.
     *
     * @return Returns the sort order if defined, default value otherwise.
     */
    public SORT getDirSort() {

        return Sort.SORT.get(this.dir);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("Sort [dir=").append(dir).append(", key=").append(key).append("]");
        return builder.toString();
    }

}
