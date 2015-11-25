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

/**
 * <p>
 * Presentation VO for Excel worksheet.<br/>
 * Represents a cell in an Excel worksheet.<br/>
 * Contains the format, value and space for a cell.
 * </p>
 * <p>
 * Instance example :<br/>
 * format : NUMBER<br/>
 * numberspan : 2<br/>
 * value : 1.2<br/>
 * </p>
 * 
 * @version 2.6.1
 * @memberof General
 */
public class ColVO implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4970420653475529076L;

    /**
     * Number of colonne span by the colonne
     */
    private int numberSpan = 1;

    /**
     * Value of the colonne
     */
    private Object value;

    /**
     * the format of colonne ( NUMBER, DATE, STRING, REFERENCE,CHECKBOX )
     */
    private String format;

    /**
     * @return Returns the numberSpan.
     */
    public int getNumberSpan() {

        return this.numberSpan;
    }

    /**
     * Setter of number of colonne span by the colonne
     * 
     * @param numberSpan
     *            The numberSpan to set.
     */
    public void setNumberSpan(final int numberSpan) {

        this.numberSpan = numberSpan;
    }

    /**
     * @return Returns the value.
     */
    public Object getValue() {

        return this.value;
    }

    /**
     * Setter of value : String
     * 
     * @param value
     *            The value to set.
     */
    public void setValue(final Object value) {

        this.value = value;
    }

    /**
     * Getter of format
     * 
     * @return Returns the format.
     */
    public String getFormat() {

        return this.format;
    }

    /**
     * Setter of format ( NUMBER, DATE, STRING, REFERENCE,CHECKBOX )
     * 
     * @param format
     *            The format to set.
     */
    public void setFormat(final String format) {

        this.format = format;
    }
}
