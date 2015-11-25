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
package hornet.framework.template;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MAE - O. ROUSSEIL
 */
public final class VelocityHelper {

    /** Logger */
    private static final Logger LOG = LoggerFactory.getLogger(VelocityHelper.class);

    /**
     * Rend un fragment HTML avec Velocity
     *
     * @param templateString
     *            le modèle velocity sous la forme d'une chaine
     * @param paramMap
     *            la liste des paramètres utilisé par le modèle
     * @return le résultat de l'evalutation du modèle
     */
    public static String renderVelocityFragment(final String templateString,
                final Map<String, Object> paramMap) {

        LOG.debug("Début renderVelocityFragment");

        final VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
            "org.apache.velocity.runtime.log.Log4JLogChute");
        ve.setProperty(Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER, "VelocityHelper");
        ve.init();

        /* create a context and add data */
        final VelocityContext context = new VelocityContext();
        for (final Map.Entry<String, Object> entry : paramMap.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }
        /* now render the template into a StringWriter */
        final StringWriter writer = new StringWriter();
        ve.evaluate(context, writer, "logtag", templateString);

        LOG.debug("Fin renderVelocityFragment");

        return writer.toString();
    }

    /**
     * Construteur masqué pour la règle : Hide Utility Class Constructor
     */
    private VelocityHelper() {

        // not called
    }

}
