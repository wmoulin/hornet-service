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
package hornet.framework.export.vo.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Presentation VO for Excel spreadsheet.<br />
 * Represents a table in an Excel worksheet.<br />
 * Contains :<br />
 * - a list of RowsVO (line in an Excel worksheet)<br />
 * - a title of this worksheet<br />
 * - a number of columns<br />
 * - a number of columns to escape<br />
 * - a author<br />
 * - a created date of this worksheet <br />
 * </p>
 * 
 * @version 2.6.1
 * @memberof General
 */
public class TableVO implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 7943690201639419013L;

    /**
     * RowsVO's collection
     */
    private List<RowVO> rows = new ArrayList<RowVO>();

    /**
     * Number of columns of the table
     */
    private int nbColumns;

    /**
     * Number of columns to escape
     */
    private int nbColumnsToEscape = 0;

    /**
     * Title of this worksheet
     */
    private String title;

    /**
     * Author of this worksheet
     */
    private String author;

    /**
     * Created date of this worksheet
     */
    private Date date;

    /**
     * Header of table<br />
     * List of column titles
     */
    private Collection<String> columnsTitles;

    /**
     * Getter of rowsVO's collection
     * 
     * @return Returns the rows.
     */
    public List<RowVO> getRows() {

        return this.rows;
    }

    /**
     * Setter of rowsVO's collection
     * 
     * @param rows
     *            The rows to set.
     */
    public void setRows(final List<RowVO> rows) {

        this.rows = rows;
    }

    /**
     * Getter of number of columns of the table
     * 
     * @return Returns the nbColumns : int.
     */
    public int getNbColumns() {

        return this.nbColumns;
    }

    /**
     * Setter of number of columns of the table
     * 
     * @param nbColumns
     *            The nbColumns to set.
     */
    public void setNbColumns(final int nbColumns) {

        this.nbColumns = nbColumns;
    }

    /**
     * Title of this worksheet
     * 
     * @return String title
     */
    public String getTitle() {

        return this.title;
    }

    /**
     * Setter of title property
     * 
     * @param title
     *            String title of table
     */
    public void setTitle(final String title) {

        this.title = title;
    }

    /**
     * Getter of created date of this worksheet
     * 
     * @return Date
     */
    public Date getDate() {

        return (Date) this.date.clone();
    }

    /**
     * Setter of created date of this worksheet
     * 
     * @param date
     *            created date
     */
    public void setDate(final Date date) {

        this.date = (Date) date.clone();
    }

    /**
     * Getter of author of this worksheet
     * 
     * @return author of this worksheet : String
     */
    public String getAuthor() {

        return this.author;
    }

    /**
     * Setter of author of this worksheet
     * 
     * @param author
     *            String
     */
    public void setAuthor(final String author) {

        this.author = author;
    }

    /**
     * Getter of number of columns to escape
     * 
     * @return number of columns to escape : int
     */
    public int getNbColumnsToEscape() {

        return this.nbColumnsToEscape;
    }

    /**
     * Setter of number of columns to escape
     * 
     * @param nbColumnsToEscape
     *            int
     */
    public void setNbColumnsToEscape(final int nbColumnsToEscape) {

        this.nbColumnsToEscape = nbColumnsToEscape;
    }

    /**
     * Getter of columnsTitles
     * 
     * @return Collection&lt;String&gt;
     */
    public Collection<String> getColumnsTitles() {

        return this.columnsTitles;
    }

    /**
     * Setter of title's collection
     * 
     * @param columnsTitles
     *            Collection&lt;String&gt;
     */
    public void setColumnsTitles(final Collection<String> columnsTitles) {

        this.columnsTitles = columnsTitles;
    }

}
