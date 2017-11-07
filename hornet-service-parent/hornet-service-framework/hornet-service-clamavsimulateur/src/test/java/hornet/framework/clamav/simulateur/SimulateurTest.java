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
package hornet.framework.clamav.simulateur;

import hornet.framework.clamav.Configuration;
import hornet.framework.clamav.bo.ClamAvResponse;
import hornet.framework.clamav.exception.ClamAVException;
import hornet.framework.clamav.service.ClamAVCheckService;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class SimulateurTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-appContext-test.xml"})
public class SimulateurTest {

    /** Constante pour les logs */
    private static final String LOG_REPONSE = "Réponse : {}";

    /** Configuration. */
    @Autowired
    @Qualifier("configuration")
    private transient Configuration config;

    /** Service ClamAV. */
    @Autowired
    @Qualifier("clamAvSimulatorServiceCheck")
    private transient ClamAVCheckService clamAVCheckService;

    /** Logger de la classe de test. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateurTest.class);

    /**
     * Méthode d'appel du service ClamAV.
     *
     * @param fichier
     *            le fichier à analyser
     * @return la réponse
     */
    private ClamAvResponse analyseUnFichier(final File fichier) {

        ClamAvResponse ret = null;

        try {
            ret = this.clamAVCheckService.checkByStream(fichier);
        } catch (final ClamAVException e) {
            LOGGER.error("Erreur ClamAVException", e);
        }

        if (ret != null) {
            LOGGER.debug("Réponse Traitement fichier : {}", ret.getCategorieReponse());
        }

        return ret;
    }

    /**
     * Simulateur test ok.
     *
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @Test
    public void simulateurTestOK() throws URISyntaxException {

        LOGGER.info("Début du test simulateurTest_OK");
        try {
            final ClamAvResponse res = testClamAV("test-OK.txt");
            Assert.assertNotNull(res);
            final String response = res.getResponse();
            LOGGER.info(LOG_REPONSE, response);

            Assert.assertEquals(response, "1: stream: OK\n");

        } catch (final Exception e) {
            LOGGER.error("Error du test : simulateurTest_OK", e);
            Assert.fail("Error du test : simulateurTest_OK : " + e.getMessage());
        }
    }

    /**
     * Simulateur test ko.
     *
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @Test
    public void simulateurTestKO() throws URISyntaxException {

        LOGGER.info("Début du test simulateurTest_KO");
        try {
            final ClamAvResponse res = testClamAV("test-KO.txt");
            Assert.assertNotNull(res);
            final String response = res.getResponse();
            LOGGER.info(LOG_REPONSE, response);

            Assert.assertEquals(response, "1: stream: KO FOUND XXX\n");
        } catch (final Exception e) {
            LOGGER.error("Error du test : simulateurTest_KO", e);
            Assert.fail("Error du test : simulateurTest_KO : " + e.getMessage());
        }
    }

    /**
     * Simulateur test serviceko.
     *
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @Test
    public void simulateurTestSERVICEKO() throws URISyntaxException {

        LOGGER.info("Début du test simulateurTest_SERVICE_KO");
        try {
            final ClamAvResponse res = testClamAV("test-SERVICE_KO.txt");

            Assert.assertNull(res);
        } catch (final Exception e) {
            LOGGER.error("Error du test : simulateurTest_SERVICE_KO", e);
            Assert.fail("Error du test : simulateurTest_SERVICE_KO : " + e.getMessage());
        }
    }

    /**
     * Simulateur test vide.
     *
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @Test
    public void simulateurTestVIDE() throws URISyntaxException {

        LOGGER.info("Début du test simulateurTest_VIDE");
        try {
            final ClamAvResponse res = testClamAV("test-VIDE.txt");

            Assert.assertNotNull(res);
            final String response = res.getResponse();
            LOGGER.info(LOG_REPONSE, response);

            Assert.assertEquals(response, "1: stream: OK\n");
        } catch (final Exception e) {
            LOGGER.error("Error du test : simulateurTest_VIDE", e);
            Assert.fail("Error du test : simulateurTest_VIDE : " + e.getMessage());
        }
    }

    /**
     * Simulateur test autre.
     *
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @Test
    public void simulateurTestAUTRE() throws URISyntaxException {

        LOGGER.info("Début du test simulateurTest_AUTRE");
        try {
            final ClamAvResponse res = testClamAV("test-AUTRE.txt");
            Assert.assertNotNull(res);
            final String response = res.getResponse();
            LOGGER.info(LOG_REPONSE, response);

            Assert.assertEquals(response, "1: stream: OK\n");
        } catch (final Exception e) {
            LOGGER.error("Error du test : simulateurTest_AUTRE", e);
            Assert.fail("Error du test : simulateurTest_AUTRE : " + e.getMessage());
        }
    }

    /**
     * Test clam av.
     *
     * @param file
     *            the file
     * @return the clam av response
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    private ClamAvResponse testClamAV(final String file) throws URISyntaxException {

        final URI fichier =
                    Thread.currentThread().getContextClassLoader()
                                .getResource(this.config.getRessources() + "/" + file).toURI();
        return this.analyseUnFichier(new File(fichier));
    }

}
