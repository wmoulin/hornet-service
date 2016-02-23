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
package hornet.framework.metrologie.aspect;

import hornet.framework.metrologie.ChronometreSet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect pour la gestion des Chronometres (ChronometreFilter).
 *
 * @author Hornet
 * @since 1.0 - 30 juil. 2014
 */
public class MetrologieAspect {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MetrologieAspect.class.getName());

    /** {@code chronoName} nom du chrono a declancher. */
    private transient String chronoNametoStart;

    /** {@code previousChronoName} nom du chrono a arreter. */
    private transient String previousChronoName;

    /**
     * Instantiates a new metrologie aspect.
     *
     * @param previousChronoName
     *            the previous chrono name
     * @param chronoNametoStart
     *            the chrono name to start
     */
    public MetrologieAspect(
                final String previousChronoName, final String chronoNametoStart) {

        this.chronoNametoStart = chronoNametoStart;
        this.previousChronoName = previousChronoName;

        LOGGER.debug("{}->{}", this.previousChronoName, this.chronoNametoStart);

    }

    /**
     * ArroundMethod pour pour la gestion des chronometres : - Arret du chronometre precedent. - Demarrage du
     * chronometre. - Execution de la methode. - Arret du chronometre. - Redemarrage du chronometre precedent.
     *
     * @param jointPoint
     *            the joint point
     * @return the object
     * @throws Throwable
     *             the Throwable
     */
    public Object loggerTraceMetrologie(final ProceedingJoinPoint jointPoint) throws Throwable {

        LOGGER.debug("{}->{} : {}", this.previousChronoName, this.chronoNametoStart,
            jointPoint.toLongString());

        if (this.previousChronoName != null) {
            ChronometreSet.get().getChrono(this.previousChronoName).stop();
        }
        ChronometreSet.get().getChrono(this.chronoNametoStart).start();

        Object obj;

        try {
            obj = jointPoint.proceed();
        } finally {
            ChronometreSet.get().getChrono(this.chronoNametoStart).stop();
            if (this.previousChronoName != null) {
                ChronometreSet.get().getChrono(this.previousChronoName).start();
            }
        }
        return obj;
    }

}
