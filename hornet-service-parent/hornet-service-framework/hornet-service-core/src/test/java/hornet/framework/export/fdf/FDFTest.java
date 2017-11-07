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
package hornet.framework.export.fdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Projet hornetserver.
 * 
 * @author EffiTIC - gpi131530
 * @date 29 déc. 2009
 */
public class FDFTest {

    /** The Constant CONST_1. */
    private static final String CONST_1 = "1";

    /** The Constant CONST_5. */
    private static final String CONST_5 = "5";

    /** The Constant CONST_55. */
    private static final String CONST_55 = "55";

    /** The Constant CONST_123. */
    private static final String CONST_123 = "123";

    /** The Constant CONST_555. */
    private static final String CONST_555 = "555";

    /** The Constant CONST_YES. */
    private static final String CONST_YES = "Yes";

    /** The Constant CONST_OFF. */
    private static final String CONST_OFF = "Off";

    /**
     * Test export map add param bloc1.
     * 
     * @param dataBean
     *            the data bean
     */
    private void testExportMapAddParamBloc1(final Map<String, Object> dataBean) {

        dataBean.put("bloc1AdresseEmetteur", "Chemin du parc 78650 NANTES");
        dataBean.put("bloc1AutreCheckBox", CONST_OFF);
        dataBean.put("bloc1CollCheckBox", CONST_YES);
        dataBean.put("bloc1FaxEmetteur", "01.55.91.40.28");
        dataBean.put("bloc1MailEmetteur", "mail@test.com");
        dataBean.put("bloc1NomEmetteur", "DGI-GN Stockage de Nantes");
        dataBean.put("bloc1PersonneCheckBox", CONST_YES);
        dataBean.put("bloc1ProdCheckBox", CONST_OFF);
        dataBean.put("bloc1ResponsableEmetteur", "M. Gilles");
        dataBean.put("bloc1TelEmetteur", "01.34.91.40.22");
    }

    /**
     * Test export map add param bloc2.
     * 
     * @param dataBean
     *            the data bean
     */
    private void testExportMapAddParamBloc2(final Map<String, Object> dataBean) {

        dataBean.put("bloc2AdresseDestinataire", "Chemin du parc 78650 NANTES");
        dataBean.put("bloc2CAP", "1024415");
        dataBean.put("bloc2FaxDestinataire", "01.34.91.40.28");
        dataBean.put("bloc2MailDestinataire", "");
        dataBean.put("bloc2NomDestinataire", "Stockage de Nantes");
        dataBean.put("bloc2Operation", "D10");
        dataBean.put("bloc2ProvisoireNon", CONST_OFF);
        dataBean.put("bloc2ProvisoireOui", CONST_OFF);
        dataBean.put("bloc2ResponsableDestinataire", "M. Gilles");
        dataBean.put("bloc2Siret1", CONST_123);
        dataBean.put("bloc2Siret2", CONST_123);
        dataBean.put("bloc2Siret3", CONST_123);
        dataBean.put("bloc2Siret4", CONST_123);
        dataBean.put("bloc2Siret5", CONST_5);
        dataBean.put("bloc2TelDestinataire", "01.34.91.40.22");
    }

    /**
     * Test export map add param bloc3.
     * 
     * @param dataBean
     *            the data bean
     */
    private void testExportMapAddParamBloc3(final Map<String, Object> dataBean) {

        dataBean.put("bloc3ConsistanceGazeux", "No");
        dataBean.put("bloc3ConsistanceLiquide", CONST_YES);
        dataBean.put("bloc3ConsistanceSolide", true); // test boolean
        dataBean.put("bloc3DenominationDechet", "Effluents dilués cuves station");
        dataBean.put("bloc3Nomenclature1", CONST_1);
        dataBean.put("bloc3Nomenclature2", CONST_1);
        dataBean.put("bloc3Nomenclature3", CONST_1);
        dataBean.put("bloc3Nomenclature4", CONST_1);
        dataBean.put("bloc3Nomenclature5", CONST_1);
        dataBean.put("bloc3Nomenclature6", CONST_1);
        dataBean.put("bloc3Nomenclature7", "*");
    }

    /**
     * Test export map add param bloc456.
     * 
     * @param dataBean
     *            the data bean
     */
    private void testExportMapAddParamBloc456(final Map<String, Object> dataBean) {

        dataBean.put("bloc4DechDesignationTransport", "UN 1993, LIQUIDES INFLAMMABLES NSA");
        dataBean.put("bloc5Autre", CONST_YES);
        dataBean.put("bloc5autrePrecision", "");
        dataBean.put("bloc5Benne", CONST_OFF);
        dataBean.put("bloc5Citerne", CONST_OFF);
        dataBean.put("bloc5Fut", CONST_OFF);
        dataBean.put("bloc5GRV", CONST_OFF);
        dataBean.put("bloc5nbColis", "12");
        dataBean.put("bloc6Estimee", CONST_YES);
        dataBean.put("bloc6nbTonnes", "");
        dataBean.put("bloc6Reelle", CONST_YES);
    }

    /**
     * Test export map add param bloc7.
     * 
     * @param dataBean
     *            the data bean
     */
    private void testExportMapAddParamBloc7(final Map<String, Object> dataBean) {

        dataBean.put("bloc7AdresseNegociant", "3, rue du Negociant");
        dataBean.put("bloc7DepartementNegociant", "");
        dataBean.put("bloc7FaxNegociant", "01.34.91.40.28");
        dataBean.put("bloc7MailNegociant", "");
        dataBean.put("bloc7NomNegociant", "");
        dataBean.put("bloc7RecepisseNegociant", "");
        dataBean.put("bloc7ResponsableNegociant", "");
        dataBean.put("bloc7Siren1", CONST_123);
        dataBean.put("bloc7Siren3", CONST_123);
        dataBean.put("bloc7Sirent2", CONST_123);
        dataBean.put("bloc7TelNegociant", "");
        dataBean.put("bloc7ValiditeNegociant", "");

    }

    /**
     * Test export map add param bloc8.
     * 
     * @param dataBean
     *            the data bean
     */
    private void testExportMapAddParamBloc8(final Map<String, Object> dataBean) {

        dataBean.put("bloc8AdresseCollecteur", "Adresse coloborateur");
        dataBean.put("bloc8DepartementCollecteur", "");
        dataBean.put("bloc8FaxCollecteur", "");
        dataBean.put("bloc8MailCollecteur", "collaborateur@mail.com");
        dataBean.put("bloc8ModeTransport", "");
        dataBean.put("bloc8Multimodal", CONST_YES);
        dataBean.put("bloc8NomCollecteur", "collaborateurNom");
        dataBean.put("bloc8RecepisseCollecteur", "");
        dataBean.put("bloc8ResponsableCollecteur", "");
        dataBean.put("bloc8Siren1", CONST_123);
        dataBean.put("bloc8Siren3", CONST_123);
        dataBean.put("bloc8Sirent2", CONST_123);
        dataBean.put("bloc8TelCollecteur", "+332.34.91.40.22");
        dataBean.put("bloc8ValiditeCollecteur", "");
    }

    /**
     * Test method for
     * {@link hornet.framework.export.fdf.FDF#export(java.io.InputStream, java.io.OutputStream, java.lang.Object)}
     * .
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testExportMap() throws Exception {

        final InputStream in = this.getClass().getResourceAsStream("cerfa.pdf");

        final File tempOutputFile = File.createTempFile("cerfa.out", "pdf");
        final OutputStream out = new FileOutputStream(tempOutputFile);

        final Map<String, Object> dataBean = new HashMap<String, Object>();

        final Map<String, Object> dataBeanSiret = new HashMap<String, Object>();

        // test entier + champ nested
        dataBeanSiret.put("num1", CONST_555);
        dataBeanSiret.put("num2", CONST_555);
        dataBeanSiret.put("num3", CONST_123);
        dataBeanSiret.put("num4", CONST_555);
        dataBeanSiret.put("num5", CONST_55);

        // alimentation des champs variables
        this.testExportMapAddParamBloc1(dataBean);
        this.testExportMapAddParamBloc2(dataBean);
        this.testExportMapAddParamBloc3(dataBean);
        this.testExportMapAddParamBloc456(dataBean);
        this.testExportMapAddParamBloc7(dataBean);
        this.testExportMapAddParamBloc8(dataBean);

        dataBean.put("bloc9NomSignataire", "Signataire");
        dataBean.put("siret1", dataBeanSiret);

        dataBean.put("bloc8datePriseEnCharge", new Date()); // test date

        dataBean.put("blocImageCerfa", new FDFImage(this.getClass().getResource("cerfa.gif"))); // test image

        final FDFRes res = export(in, tempOutputFile, out, dataBean);

        Assert.assertEquals(0, res.getUnmerged().size());
    }

    /**
     * Test method for
     * {@link hornet.framework.export.fdf.FDF#export(java.io.InputStream, java.io.OutputStream, java.lang.Object)}
     * .
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testExportBean() throws Exception {

        final int unmergedFields = 76;

        final InputStream in = this.getClass().getResourceAsStream("cerfa.pdf");

        final File tempOutputFile = File.createTempFile("cerfa.out2.pdf", "pdf");
        final OutputStream out = new FileOutputStream(tempOutputFile);

        final FDFBean dataBean = new FDFBean();
        final FDFBeanSiret dataBeanSiret = new FDFBeanSiret();
        dataBean.setSiret1(dataBeanSiret);

        // alimentation des champs variables
        dataBean.getSiret1().setNum1(CONST_5);
        dataBean.getSiret1().setNum2(CONST_5);
        dataBean.getSiret1().setNum3(CONST_5);
        dataBean.getSiret1().setNum4(CONST_5);
        dataBean.getSiret1().setNum5(CONST_5);
        final FDFRes res = export(in, tempOutputFile, out, dataBean);
        // bon ok y'a pas tous les champs dans le bean...
        Assert.assertEquals(unmergedFields, res.getUnmerged().size());
    }

    /**
     * Test export unmerged.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testExportUnmerged() throws Exception {

        final int unmergedFields = 81;

        final InputStream in = this.getClass().getResourceAsStream("cerfa.pdf");

        final File tempOutputFile = File.createTempFile("cerfa.out3.pdf", "pdf");
        final OutputStream out = new FileOutputStream(tempOutputFile);

        final FDFBean dataBean = new FDFBean();

        final FDFRes res = FDF.export(in, out, dataBean);

        Assert.assertEquals(unmergedFields, res.getUnmerged().size());
    }

    /**
     * Export.
     * 
     * @param in
     *            the in
     * @param tempOutputFile
     *            the temp output file
     * @param out
     *            the out
     * @param dataBean
     *            the data bean
     * @return the FDF res
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private FDFRes export(final InputStream in, final File tempOutputFile, final OutputStream out,
                final Object dataBean) throws IOException {

        FDFRes res = null;
        try {
            res = FDF.export(in, out, dataBean);
        } finally {
            in.close();
            out.close();
            tempOutputFile.delete();
        }
        return res;
    }

}
