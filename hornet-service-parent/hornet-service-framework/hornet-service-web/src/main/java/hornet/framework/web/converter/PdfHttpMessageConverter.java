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
package hornet.framework.web.converter;

import hornet.framework.export.ExportPdfModelService;
import hornet.framework.export.vo.pdf.PdfConverterIn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;

import javax.annotation.Resource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;


/**
 * @author Hornet
 * @since 1.0 - 4 mars 2015
 *
 *        <p>
 *        Converter chargé de l'export PDF. Utilise la librairie JasperReports.<br>
 *        Ce converter délègue la partie récupération du template et récupération du bean à exporter à un
 *        service implémentant {@link ExportPdfModelService}<br>
 *        A partir de ces éléments, ce converter se charge de la transformation du template + bean en fichier
 *        PDF et l'écrit dans l'outputstream.
 *        </p>
 */
@Service
public class PdfHttpMessageConverter<T> extends
            AbstractHornetHttpMessageConverter<T, ExportPdfModelService<T>> {

    private static final Logger LOG = LoggerFactory.getLogger(PdfHttpMessageConverter.class);

    @Resource
    private ExportPdfModelService<T>[] exportPdfModelServices;

    public PdfHttpMessageConverter() {

        super(HornetMediaType.APPLICATION_PDF);
    }

    @Override
    protected ExportPdfModelService<T>[] getServicesExport() {

        return exportPdfModelServices;
    }

    @Override
    protected void ecrireFichier(final T toExport, final OutputStream os) throws IOException,
        HttpMessageNotWritableException {

        // Récupération du model (Template + Bean à écrire)
        final PdfConverterIn model = getServiceExport(toExport).construirePdfModel(toExport);

        // Récupération de l'InputStream du template
        final InputStream isTemplate = model.getTemplate().getInputStream();

        try {
            // Création d'une dataSource Jasper avec le bean à exporter
            // Les dataSources sont forcément sous forme de map ou de collection
            final JRDataSource jasperDs = new JRBeanCollectionDataSource(Collections.singletonList(toExport));

            // Merge de la dataSource et du template vers un JasperPrint (non dépendant du format final)
            final JasperPrint jasperPrint = JasperFillManager.fillReport(isTemplate, null, jasperDs);

            // Export du JasperPrint au format PDF (JRPdfExporter) dans l'outputStream
            final JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
            exporter.exportReport();

        } catch (final JRException jre) {
            LOG.error("Erreur lors de l'export PDF avec le template : {}", model.getTemplate()
                        .getDescription());
            throw new HttpMessageNotWritableException("Erreur lors de l'export Jasper avec le template "
                        + model.getTemplate().getDescription(), jre);
        } catch (final Exception e) {
            LOG.error("Erreur lors de l'export PDF avec le template : {}", model.getTemplate()
                        .getDescription());
            throw e;
        } finally {
            IOUtils.closeQuietly(isTemplate);
        }
    }
}
