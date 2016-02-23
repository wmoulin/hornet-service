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
package hornet.framework.web.service.export;

import hornet.framework.export.ExportCsvModelService;
import hornet.framework.export.ExportXlsModelService;
import hornet.framework.export.vo.presentation.ColVO;
import hornet.framework.export.vo.presentation.RowVO;
import hornet.framework.export.vo.presentation.TableVO;
import hornet.framework.export.vo.utils.TableVOUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hornet
 * @since 1.0 - 3 mars 2015
 */
public abstract class AbstractTableExportService<T> implements ExportCsvModelService<T>,
            ExportXlsModelService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTableExportService.class);

    @Override
    public boolean supports(final Class<?> clazz) {

        return getSupportedClass().isAssignableFrom(clazz);
    }

    /**
     * Permet à l'implémentation de définir quel est le contenu de son tableau
     *
     * @return
     */
    public abstract Class<T> getSupportedClass();

    /**
     * Méthode à implémenter par l'application en fonction du type de Table. <br>
     * Utilisé par l'export CSV et l'export Excel
     *
     * @param toExport
     * @return
     */
    abstract protected TableVO construireTableauExport(final T toExport);

    @Override
    public TableVO constuireCSVModel(final T toExport) {

        return construireTableauExport(toExport);
    }

    @Override
    public HSSFWorkbook construireXlsModel(final T toExport) {

        // Blank workbook
        final HSSFWorkbook workbook = new HSSFWorkbook();

        // Create a blank sheet
        final HSSFSheet sheet = workbook.createSheet();
        int rownum = 0;

        // Style pour la bordure des cellules
        final CellStyle styleBordure = workbook.createCellStyle();
        styleBordure.setBorderBottom(CellStyle.BORDER_THIN);
        styleBordure.setBorderTop(CellStyle.BORDER_THIN);
        styleBordure.setBorderRight(CellStyle.BORDER_THIN);
        styleBordure.setBorderLeft(CellStyle.BORDER_THIN);

        final CellStyle styleEntete = workbook.createCellStyle();
        styleEntete.cloneStyleFrom(styleBordure);
        styleEntete.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        styleEntete.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // Récupération du Table VO
        final TableVO tableVo = construireTableauExport(toExport);

        final Collection<String> colTitles = tableVo.getColumnsTitles();
        final Iterator<String> itTitles = colTitles.iterator();

        // Titre des colonnes
        int cellnum = 0;
        final Row xlsRow = sheet.createRow(rownum++);
        while (itTitles.hasNext()) {
            final String title = itTitles.next();
            final Cell cell = xlsRow.createCell(cellnum++);
            cell.setCellValue(title);
            cell.setCellStyle(styleEntete);
        }

        if (tableVo.getRows() != null) {
            final List<RowVO> rows = tableVo.getRows();
            final Iterator<RowVO> itRows = rows.iterator();
            // Lignes
            while (itRows.hasNext()) {
                this.exporteLigne(itRows, rownum, sheet, styleBordure);
                rownum++;
            }

            for (int i = 0; i < cellnum; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        return workbook;
    }

    /**
     * Exporte une ligne de tableau.
     *
     * @param itRows
     *            the it rows
     * @param rownum
     *            the rownum
     * @param sheet
     *            the sheet
     * @param styleBordure
     *            the style bordure
     */
    private void exporteLigne(final Iterator<RowVO> itRows, final int rownum, final HSSFSheet sheet,
                final CellStyle styleBordure) {

        final RowVO row = itRows.next();
        final Iterator<ColVO> itCols;
        final Row xlsRow = sheet.createRow(rownum);
        if (row.getCols() != null) {
            itCols = row.getCols().iterator();
            // Cellules
            int cellnum = 0;
            while (itCols.hasNext()) {
                final ColVO col = itCols.next();
                final Cell cell = xlsRow.createCell(cellnum++);
                if (col.getValue() != null) {
                    cell.setCellValue(TableVOUtils.formaterCellule(col));
                    cell.setCellStyle(styleBordure);
                }
            }
        }
    }

}
