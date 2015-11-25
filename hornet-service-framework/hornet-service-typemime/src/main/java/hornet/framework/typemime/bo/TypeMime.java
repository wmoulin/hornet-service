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
package hornet.framework.typemime.bo;

import java.io.Serializable;

import hornet.framework.typemime.utility.ConstantesTypeMime;

/**
 * Cette classe déclare l'objet TypeMime et ses différents attributs
 */
public class TypeMime implements Serializable {

    /**
     * <code>serialVersionUID</code> the serialVersionUID
     */
    private static final long serialVersionUID = 5071784473615673613L;

    /**
     * <code>nom</code> le nom
     */
    private String nom = "";

    /**
     * Recupere l'attribut nom
     * 
     * @return retourne le nom
     */
    public String getNom() {

        return this.nom;
    }

    /**
     * Valorise l'attribut nom
     * 
     * @param nom
     *            le nom à valoriser
     */
    public void setNom(final String nom) {

        this.nom = nom;
    }

    /**
     * Teste si le fichier est de type PDF (.pdf)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isPDFFile() {

        if (ConstantesTypeMime.DOC_PDF.equals(this.nom)) {
            return true;
        }
        return false;
    }

    /**
     * Teste si le fichier est de type Image (.gif, .jpeg, .png)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isImage() {

        return ConstantesTypeMime.IMG_GIF.equals(this.nom) || ConstantesTypeMime.IMG_JPEG.equals(this.nom)
                    || ConstantesTypeMime.IMG_PNG.equals(this.nom);
    }

    /**
     * Teste si le fichier est de type Archive (.rar, .zip)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isArchiveFile() {

        return this.nom.matches(ConstantesTypeMime.RGX_TAR) || this.nom.matches(ConstantesTypeMime.RGX_ZIP);
    }

    /**
     * Teste si le fichier est de type Texte (.txt, .rtf)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isTextFile() {

        return ConstantesTypeMime.TXT_TEXT.equals(this.nom) || ConstantesTypeMime.TXT_RTF.equals(this.nom);
    }

    /**
     * Teste si le fichier est de type Word (.doc)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isWordFile() {

        return this.nom.matches(ConstantesTypeMime.RGX_WORD_1)
                    || this.nom.matches(ConstantesTypeMime.RGX_WORD_2);
    }

    /**
     * Teste si le fichier est de type Excel (.xls)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isExcelFile() {

        return this.nom.matches(ConstantesTypeMime.RGX_EXCEL_1)
                    || this.nom.matches(ConstantesTypeMime.RGX_EXCEL_2);
    }

    /**
     * Teste si le fichier est de type OpenOffice (.ods, .odt)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isOpenOfficeFile() {

        return this.nom.matches(ConstantesTypeMime.RGX_OO_FILE);
    }

    /**
     * Teste si le fichier est de type OpenDocument Excel (.xlsx)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isOpenOfficeSheetFile() {

        return this.nom.matches(ConstantesTypeMime.RGX_OO_SHEET);
    }

    /**
     * Teste si le fichier est de type OpenDocument Word (.docx)
     * 
     * @return vrai si le type correspond, faux sinon
     */
    public boolean isOpenOfficeTextFile() {

        return this.nom.matches(ConstantesTypeMime.RGX_OO_TEXT);
    }

    /**
     * Teste si le fichier ne fait partie d'aucun type connu
     * 
     * @return vrai si le type n'est pas repertorié, faux sinon
     */
    public boolean isNotClassified() {

        final boolean isOffice = this.isWordFile() || this.isExcelFile() || this.isOpenOfficeFile();
        final boolean isReadText = this.isPDFFile() || this.isTextFile();

        return !(this.isImage() || this.isArchiveFile() || isReadText || isOffice);
    }

}
