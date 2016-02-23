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
package hornet.framework.export.fdf;

import hornet.framework.export.fdf.exception.FDFExportException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 *
 * Projet hornetserver. Utility class to merge FDF with data to produce a PDF
 *
 * @date 29 déc. 2009
 *
 */
public final class FDF {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(FDF.class);

    /**
     * The Enum FieldBoxPositions.
     */
    enum FieldBoxPositions {

        /** The page. */
        PAGE,
        /** The llx. */
        LLX,
        /** The lly. */
        LLY,
        /** The urx. */
        URX,
        /** The ury. */
        URY
    };

    /**
     * Private constructor for utility class.
     */
    private FDF() {

        // nothing.
    }

    /**
     * Thread-safe utility method for PDF generation, based on a FDF template stream and a data object to
     * merge. Fields defined in FDF that can't be merged (not corresponding field in data object) are stored
     * in FDF Result.
     *
     * @param in
     *            input stream of PDF/FDF (template)
     * @param out
     *            output stream of final PDF
     * @param data
     *            a javabean or a java.util.Map. Nested properties are allowed this field names access pattern
     *            : "propA:propB:propC".
     * @return FDF result object (not null)
     */
    public static FDFRes export(final InputStream in, final OutputStream out, final Object data) {

        PdfReader pdfIn = null;
        PdfStamper stamper = null;
        final FDFRes res = new FDFRes();

        try {
            pdfIn = new PdfReader(in);
            stamper = new PdfStamper(pdfIn, out);
            final AcroFields form = stamper.getAcroFields();

            // fusion champs FDF avec le bean
            for (final Object nomField : form.getFields().keySet()) {
                fusionChamp(data, stamper, res, form, nomField);
            }

            // la ligne suivante supprime le formulaire
            stamper.setFormFlattening(true);

        } catch (final IOException ex) {
            throw new FDFExportException(ex);
        } catch (final DocumentException ex) {
            throw new FDFExportException(ex);
        } finally {
            try {
                stamper.close();
            } catch (final Exception ex) {
                LOG.error("Erreur", ex);
            }
        }

        return res;
    }

    /**
     * Fusion d'un champ FDF.
     *
     * @param data
     *            the data
     * @param stamper
     *            the stamper
     * @param res
     *            the res
     * @param form
     *            the form
     * @param nomField
     *            the nom field
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws DocumentException
     *             the document exception
     */
    private static void fusionChamp(final Object data, final PdfStamper stamper, final FDFRes res,
                final AcroFields form, final Object nomField) throws IOException, DocumentException {

        // utilisation du ":" comme séparateur d'accès.
        // le "." étant remplacé par "_" par openoffice lors
        // de la conversion PDF.
        final String nomFieldStr = nomField.toString().replace(':', '.');

        Object value = null;
        try {
            value = PropertyUtils.getProperty(data, nomFieldStr);
        } catch (final Exception ex) {
            res.getUnmerged().add(nomFieldStr);
        }

        String valueStr;

        if (value == null) {
            valueStr = ""; // itext n'accepte pas les valeurs
            // nulles
            form.setField(nomField.toString(), valueStr);
        } else if (value instanceof FDFImage) {
            final FDFImage imValue = (FDFImage) value;
            final float[] positions = form.getFieldPositions(nomField.toString());
            final PdfContentByte content = stamper.getOverContent(1);
            final Image im = Image.getInstance(imValue.getData());
            if (imValue.isFit()) {
                content.addImage(im, positions[FieldBoxPositions.URX.ordinal()]
                            - positions[FieldBoxPositions.LLX.ordinal()], 0, 0,
                    positions[FieldBoxPositions.URY.ordinal()] - positions[FieldBoxPositions.LLY.ordinal()],
                    positions[FieldBoxPositions.LLX.ordinal()], positions[FieldBoxPositions.LLY.ordinal()]);
            } else {
                content.addImage(im, im.getWidth(), 0, 0, im.getHeight(), positions[1], positions[2]);
            }
        } else if (value instanceof Date) {
            // format par défaut date
            valueStr = DateFormat.getDateInstance(DateFormat.SHORT).format(value);
            form.setField(nomField.toString(), valueStr);
        } else if (value instanceof Boolean) {
            // format par spécial pour Checkbox
            if (Boolean.TRUE.equals(value)) {
                valueStr = "Yes";
            } else {
                valueStr = "No";
            }
            form.setField(nomField.toString(), valueStr);
        } else {
            // format par défaut
            valueStr = value.toString();
            form.setField(nomField.toString(), valueStr);
        }
    }
}
