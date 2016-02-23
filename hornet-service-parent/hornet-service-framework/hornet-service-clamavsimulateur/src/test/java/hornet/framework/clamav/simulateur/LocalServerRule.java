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

import java.util.concurrent.TimeUnit;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hornet
 * @since 1.0 - 27 janv. 2015
 */
public class LocalServerRule extends ExternalResource {

    /** The Constant NB_THREADS. */
    public static final int NB_THREADS = 10;

    /** The Constant DEFAULT_CLAMAV_PORT. */
    public static final int DEFAULT_CLAMAV_PORT = 3310;

    /** The Constant TIME_TO_SLEEP : temporisation apres le démarrage ou la arrêt du serveur */
    public static final int TIME_TO_SLEEP = 200;

    /** Logger de la classe de test. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalServerRule.class);

    /** Simulator ClamAV. */
    private SimulateurServer clamAvSimulateur;

    @Override
    protected void before() throws Throwable {

        LOGGER.info("Démarrage du serveur");
        clamAvSimulateur = new SimulateurServer(null, DEFAULT_CLAMAV_PORT, NB_THREADS);
        clamAvSimulateur.startServer();
        LocalServerRule.temporise();
    }

    @Override
    protected void after() {

        LOGGER.info("Arrêt du serveur");
        clamAvSimulateur.stopServer();
        LocalServerRule.temporise();
    }

    /**
     * Temporisation
     */
    public static void temporise() {

        try {
            TimeUnit.MILLISECONDS.sleep(TIME_TO_SLEEP);
        } catch (final InterruptedException ex) {
            LOGGER.error("Erreur", ex);
        }
    }

}
