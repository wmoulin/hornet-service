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
