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
package hornet.framework.clamav;

import hornet.framework.clamav.bo.ClamAvResponse;
import hornet.framework.clamav.exception.ClamAVException;
import hornet.framework.clamav.service.ClamAVCheckService;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class ParallelTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-appContext-test.xml"})
public final class ParallelTest {

    /** Configuration. */
    @Autowired
    @Qualifier("configuration")
    private transient Configuration config;

    /** Service ClamAV. */
    @Autowired
    @Qualifier("clamServiceCheck")
    private transient ClamAVCheckService clamAVCheckService;

    /** Logger de la classe. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelTest.class.getName());

    /**
     * The Class ThreadIt.
     */
    private class ThreadIt extends Thread {

        /** Time to sleep for thread. */
        private static final int THREAD_SLEEP_TIME = 100;

        /** The num thread. */
        private final transient int numThread;

        /** The nb fichier. */
        private final transient int nbFichier;

        /** The chemin fichier. */
        private final transient URI cheminFichier;

        /** The nb fichier no virus. */
        private transient int nbFichierNoVirus;

        /** The nb fichier virus. */
        private transient int nbFichierVirus;

        /** The nb fichier no service. */
        private transient int nbFichierNoService;

        /** The nb fichier timeout. */
        private transient int nbFichierTimeout;

        /**
         * Instantiates a new thread it.
         *
         * @param numThread
         *            the num thread
         * @param nbFichier
         *            the nb fichier
         * @param cheminFichier
         *            the chemin fichier
         */
        public ThreadIt(
                    final int numThread, final int nbFichier, final URI cheminFichier) {

            super();
            this.numThread = numThread;
            this.nbFichier = nbFichier;
            this.cheminFichier = cheminFichier;
            this.setName("Injecteur ClamAV n°" + numThread);
            this.nbFichierNoVirus = 0;
            this.nbFichierVirus = 0;
            this.nbFichierNoService = 0;
            this.nbFichierTimeout = 0;
        }

        /**
         * Stats.
         *
         * @param clamAvResponse
         *            the clam av response
         */
        public void stats(final ClamAvResponse clamAvResponse) {

            if (clamAvResponse != null) {

                if (ClamAvResponse.CategorieReponse.NO_VIRUS.equals(clamAvResponse.getCategorieReponse())) {
                    this.nbFichierNoVirus++;

                } else {
                    if (ClamAvResponse.CategorieReponse.VIRUS.equals(clamAvResponse.getCategorieReponse())) {
                        this.nbFichierVirus++;

                    } else {
                        if (ClamAvResponse.CategorieReponse.NO_SERVICE.equals(clamAvResponse
                                    .getCategorieReponse())) {
                            this.nbFichierNoService++;

                        } else {
                            if (ClamAvResponse.CategorieReponse.TIMEOUT.equals(clamAvResponse
                                        .getCategorieReponse())) {
                                this.nbFichierTimeout++;

                            }
                        }
                    }
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {

            ParallelTest.LOGGER.warn(String.format("Envoi des %d fichiers", this.nbFichier));
            ClamAvResponse clamAvResponse = null;
            for (int i = 0; i < this.nbFichier; i++) {
                clamAvResponse = ParallelTest.this.analyseUnFichier(this.cheminFichier);
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (final Exception e) {
                    ParallelTest.LOGGER.error(e.getMessage(), e);
                }

                this.stats(clamAvResponse);
            }
            ParallelTest.LOGGER
                        .warn(
                            "numThread : {}, nbFichierNoVirus : {}, nbFichierVirus : {}, nbFichierNoService : {}, nbFichierTimeout : {} - Fin",
                            this.numThread, this.nbFichierNoVirus, this.nbFichierVirus,
                            this.nbFichierNoService, this.nbFichierTimeout);
        }
    }

    /**
     * Analyse un fichier.
     *
     * @param path
     *            the path
     * @return the clam av response
     */
    private ClamAvResponse analyseUnFichier(final URI path) {

        ClamAvResponse ret = null;

        final File file = new File(path);

        try {
            ret = this.clamAVCheckService.checkByStream(file);
        } catch (final ClamAVException e) {
            ParallelTest.LOGGER.error(e.getMessage());
        }

        if (ret != null) {
            ParallelTest.LOGGER.debug("Réponse Traitement fichier : {}", ret.getCategorieReponse());
        }

        return ret;
    }

    /**
     * Test parrallel.
     *
     * @throws InterruptedException
     *             the interrupted exception
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @Test
    public void testParrallel() throws InterruptedException, URISyntaxException {

        // Ignore le test si le fichier cible n'existe pas
        final URL url =
                    Thread.currentThread().getContextClassLoader()
                                .getResource(this.config.getCheminFichier());
        Assume.assumeNotNull(url);

        long startTime;
        long endTime;
        long refTime;
        Thread[] lesThreads;

        startTime = System.currentTimeMillis();

        this.analyseUnFichier(url.toURI());
        // this.analyseUnFichier(this.config.getCheminFichier());
        endTime = System.currentTimeMillis();
        refTime = endTime - startTime;
        // Configuration du nombre de thread à créer
        lesThreads = new Thread[this.config.getNbThread()];

        for (int i = 0; i < lesThreads.length; i++) {
            lesThreads[i] = new ThreadIt(i + 1, this.config.getNbFichier(), url.toURI());
        }

        startTime = System.currentTimeMillis();
        for (final Thread lesThread : lesThreads) {
            lesThread.start();
        }

        for (final Thread lesThread : lesThreads) {
            lesThread.join();
        }

        endTime = System.currentTimeMillis();
        Assert.assertEquals(refTime, endTime - startTime, this.config.getTpsAcceptable());
        ParallelTest.LOGGER.info("nbThread : {}", this.config.getNbThread());
        ParallelTest.LOGGER.info("nbFichier : {}", this.config.getNbFichier());
        ParallelTest.LOGGER.info("Durée de traitement des threads : {}", endTime - startTime);
        ParallelTest.LOGGER.info("Temps de référence pour un fichier : {}", refTime);
    }
}
