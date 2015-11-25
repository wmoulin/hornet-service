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

import hornet.framework.export.ExportCsvModelService;
import hornet.framework.export.vo.presentation.ColVO;
import hornet.framework.export.vo.presentation.RowVO;
import hornet.framework.export.vo.presentation.TableVO;
import hornet.framework.export.vo.utils.TableVOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;


/**
 * @author Hornet
 * @since 1.0 - 3 mars 2015
 *
 *        <p>
 *        Converter chargé de l'export CSV.<br>
 *        Ce converter délègue la partie récupération du modèle CSV {@link TableVO} à exporter à un service
 *        implémentant {@link ExportCsvModelService}<br>
 *        A partir de ces éléments, ce converter se charge de la transformation du modèle en fichier CSV et
 *        l'écrit dans l'outputstream.
 *        </p>
 */
@Service
public class CsvHttpMessageConverter<T> extends
            AbstractHornetHttpMessageConverter<T, ExportCsvModelService<T>> {

    /** Caractère séparateur du fichier CSV. */
    private static final char SEPARATEUR = ';';

    /** Caractère saut de ligne. */
    private static final char SAUT_LIGNE = '\n';

    /** CR. */
    private static final char CR = '\r';

    /** LF. */
    private static final char LF = '\n';

    /** Guillemet double. */
    private static final char GUILLEMET = '"';

    /** Guillemet double. */
    private static final String GUILLEMET_STR = "\"";

    /** Guillemet doublé. */
    private static final String GUILLEMET_DOUBLE = "\"\"";

    /** Marqueur UTF-8 (BOM) à placer en début de fichier. */
    private static final byte[] UTF8_BOM = new byte[]{
        (byte) 239,
        (byte) 187,
        (byte) 191};

    /** Code UTF-8. */
    private static final String UTF8 = "UTF-8";

    @Resource
    private ExportCsvModelService<T>[] exportServices;

    public CsvHttpMessageConverter() {

        super(HornetMediaType.TEXT_CSV);
    }

    @Override
    protected void ecrireFichier(final T toExport, final OutputStream os) throws IOException,
        HttpMessageNotWritableException {

        final ExportCsvModelService<T> exportService = getServiceExport(toExport);

        // Récupération du Table VO
        final TableVO model = exportService.constuireCSVModel(toExport);

        // Ecriture du BOM
        os.write(UTF8_BOM);
        os.flush();

        final Collection<String> colTitles = model.getColumnsTitles();

        // Writer UTF-8
        final OutputStreamWriter osw = new OutputStreamWriter(os, UTF8);

        // Génération de l'entête
        this.ecrireLigneEntete(osw, colTitles);

        // Génération des lignes de données
        this.ecrireLignesDonnees(osw, colTitles, model);
    }

    @Override
    protected ExportCsvModelService<T>[] getServicesExport() {

        return exportServices;
    }

    /**
     * Ecrire ligne entete.
     *
     * @param osw
     *            the osw
     * @param colTitles
     *            the col titles
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void ecrireLigneEntete(final OutputStreamWriter osw, final Collection<String> colTitles)
        throws IOException {

        final Iterator<String> itTitles = colTitles.iterator();

        // Titre des colonnes
        int cptCols = 0;
        final int nbCol = colTitles.size();
        while (itTitles.hasNext()) {
            final String title = itTitles.next();
            osw.write(title);
            if (cptCols < nbCol - 1) {
                osw.write(SEPARATEUR);
            }
            cptCols++;
        }
        osw.flush();

        // Saut de ligne après l'en-tête
        osw.write(SAUT_LIGNE);
    }

    /**
     * Ecrire lignes donnees.
     *
     * @param osw
     *            the osw
     * @param colTitles
     *            the col titles
     * @param tableVo
     *            the table vo
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void ecrireLignesDonnees(final OutputStreamWriter osw, final Collection<String> colTitles,
                final TableVO tableVo) throws IOException {

        final int nbCol = colTitles.size();

        if (tableVo.getRows() != null) {
            final List<RowVO> rows = tableVo.getRows();
            final Iterator<RowVO> itRows = rows.iterator();
            Iterator<ColVO> itCols;
            int cptCells = 0;
            // Lignes
            while (itRows.hasNext()) {
                final RowVO row = itRows.next();
                if (row.getCols() != null) {
                    itCols = row.getCols().iterator();
                    // Cellules
                    while (itCols.hasNext()) {
                        final ColVO col = itCols.next();
                        if (col.getValue() != null) {
                            osw.write(this.echapper(TableVOUtils.formaterCellule(col)));
                        }
                        if (cptCells < nbCol - 1) {
                            osw.write(SEPARATEUR);
                        }
                        cptCells++;
                    }
                    cptCells = 0;
                }
                osw.write(SAUT_LIGNE);
                osw.flush();
            }
        }

    }

    /**
     * Echappe les caractères spéciaux d'un champ CSV.
     *
     * @param champ
     *            Champ à echapper
     * @return Champ echappé
     */
    private String echapper(final String champ) {

        final StringBuffer sbChamp = new StringBuffer();
        if (champ != null) {
            // Booléen indiquant si la chaîne doit être échapée
            final boolean escape =
                        champ.indexOf(SEPARATEUR) != -1 || champ.indexOf(GUILLEMET) != -1
                                    || champ.indexOf(LF) != -1 || champ.indexOf(CR) != -1;

            if (escape) {
                sbChamp.append(GUILLEMET);
            }

            // Prise en charge des caractères réservés
            sbChamp.append(champ.replaceAll(GUILLEMET_STR, GUILLEMET_DOUBLE).replace(CR, LF));

            if (escape) {
                sbChamp.append(GUILLEMET);
            }
        }
        return sbChamp.toString();
    }

}
