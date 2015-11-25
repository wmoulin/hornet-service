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
package hornet.framework.typemime.utility;

/**
 * ConstantesTypeMime
 */
public final class ConstantesTypeMime {

    /**
     * <code>IMG_JPEG</code> the IMG_JPEG
     */
    public static final String IMG_JPEG = "image/jpeg";

    /**
     * <code>IMG_PNG</code> the IMG_PNG
     */
    public static final String IMG_PNG = "image/png";

    /**
     * <code>IMG_GIF</code> the IMG_GIF
     */
    public static final String IMG_GIF = "image/gif";

    /**
     * <code>TXT_TEXT</code> the TXT_TEXT
     */
    public static final String TXT_TEXT = "text/plain";

    /**
     * <code>TXT_RTF</code> the TXT_RTF
     */
    public static final String TXT_RTF = "text/rtf";

    /**
     * <code>DOC_PDF</code> the DOC_PDF
     */
    public static final String DOC_PDF = "application/pdf";

    /**
     * <code>EXCEL_2K</code> the EXCEL_2K
     */
    public static final String EXCEL_2K = "application/vnd.ms-excel";

    /**
     * <code>EXCEL_2007</code> the EXCEL_2007
     */
    public static final String EXCEL_2007 = "application/vnd.openxmlformats-officedocument.spreadsheetml";

    /**
     * <code>WORD_95</code> the WORD_95
     */
    public static final String WORD_95 = "application/vnd.ms-word";

    /**
     * <code>WORD_2K</code> the WORD_2K
     */
    public static final String WORD_2K = "application/vnd.ms-word";

    /**
     * <code>WORD_2007</code> the WORD_2007
     */
    public static final String WORD_2007 = "application/vnd.openxmlformats-officedocument.wordprocessingml";

    /**
     * <code>OO_SHEET</code> the OO_SHEET
     */
    public static final String OO_SHEET = "application/vnd.oasis.opendocument.spreadsheet";

    /**
     * <code>OO_TEXT</code> the OO_TEXT
     */
    public static final String OO_TEXT = "application/vnd.oasis.opendocument.text";

    /**
     * <code>ARCH_TAR</code> the ARCH_TAR
     */
    public static final String ARCH_TAR = "application/x-tar";

    /**
     * <code>ARCH_TAR_GZ</code> the ARCH_TAR_GZ
     */
    public static final String ARCH_TAR_GZ = "application/gzip";

    /**
     * <code>ARCH_GZIP</code> the ARCH_GZIP
     */
    public static final String ARCH_GZIP = "application/gzip";

    /**
     * <code>ARCH_ZIP</code> the ARCH_ZIP
     */
    public static final String ARCH_ZIP = "application/zip";

    /**
     * <code>RGX_WORD_1</code> the RGX_WORD_1
     */
    public static final String RGX_WORD_1 = "application/[vnd\\.]*ms[-]?word";

    /**
     * <code>RGX_WORD_2</code> the RGX_WORD_2
     */
    public static final String RGX_WORD_2 =
                "application/vnd.openxmlformats-officedocument.wordprocessingml.*";

    /**
     * <code>RGX_EXCEL_1</code> the RGX_EXCEL_1
     */
    public static final String RGX_EXCEL_1 = "application/[vnd\\.]*ms[-]?excel";

    /**
     * <code>RGX_EXCEL_2</code> the RGX_EXCEL_2
     */
    public static final String RGX_EXCEL_2 = "application/vnd.openxmlformats-officedocument.spreadsheetml.*";

    /**
     * <code>RGX_OO_FILE</code> the RGX_OO_FILE
     */
    public static final String RGX_OO_FILE = "application/vnd.oasis.opendocument.*";

    /**
     * <code>RGX_OO_SHEET</code> the RGX_OO_SHEET
     */
    public static final String RGX_OO_SHEET = "application/vnd.oasis.opendocument.spreadsheet";

    /**
     * <code>RGX_OO_TEXT</code> the RGX_OO_TEXT
     */
    public static final String RGX_OO_TEXT = "application/vnd.oasis.opendocument.text";

    /**
     * <code>RGX_TAR</code> the RGX_TAR
     */
    public static final String RGX_TAR = "application/x-[g]?tar";

    /**
     * <code>RGX_ZIP</code> the RGX_ZIP
     */
    public static final String RGX_ZIP = "application/[x]?[-]?[g]?zip";

    /** Classe utilitaire => Constructeur privé */
    private ConstantesTypeMime() {

        // not called
    }
}
