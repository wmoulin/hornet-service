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
package hornet.framework.web.listener;

import hornet.framework.exception.ConfigurationException;
import hornet.framework.util.UrlDynamicLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.jndi.JndiObjectFactoryBean;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

/**
 * Projet hornetserver.
 *
 * @author MAE - S. LEDUBY
 * @since 9 août 2013
 */
public class DynamicLoaderListener implements ServletContextListener {

    /** */
    private static final String CONFIG_LOCATION = "appConfigLocation";

    /**
     * @param event
     *            Évènement d'initialisation
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {

        try {
            this.intialize(event.getServletContext());
            /*
             * re-configuration du logger avec le paramétrage de l'application si la configuration a réussi
             */
            final String confLoggerLocator = "logback-hornet.xml";
            final Resource logConfiguration = new DefaultResourceLoader().getResource(confLoggerLocator);
            if (!logConfiguration.exists()) {
                throw new ConfigurationException("Fichier de configuration " + confLoggerLocator
                            + " non trouvé dans le classpath");
            }

            LoggerFactory.getLogger(DynamicLoaderListener.class).info("Réinitialisation du logger");

            final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            final JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(context);
            context.reset(); // réinitialisation de la configuration

            try {
                jc.doConfigure(logConfiguration.getFile());
            } catch (final Exception e) {
                throw new ConfigurationException("Erreur lors de la configuration de l'application", e);
            }

        } catch (final RuntimeException e) {
            LoggerFactory.getLogger(DynamicLoaderListener.class).error(e.getMessage(), e);
        }
    }

    /**
     * @param context
     *            Contexte
     */
    private void intialize(final ServletContext context) {

        // récupération du nom de la ressource JNDI à charger
        final String configLocation = context.getInitParameter(DynamicLoaderListener.CONFIG_LOCATION);

        // récupération de la ressource JNDI
        final JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
        jndiFactory.setResourceRef(true);
        jndiFactory.setJndiName(configLocation);
        try {
            jndiFactory.afterPropertiesSet();
        } catch (final Exception e) {
            LoggerFactory.getLogger(DynamicLoaderListener.class).error(e.getMessage(), e);
            throw new ConfigurationException("Erreur lors de la récupération de la ressource JNDI "
                        + "correspondant au répertoire de la configuration de l'application", e);
        }
        final String url = (String) jndiFactory.getObject();

        LoggerFactory.getLogger(DynamicLoaderListener.class).info(
            "Configuration : " + configLocation + " = " + url);

        new UrlDynamicLoader().initialize(url);
    }

    /**
     * @param event
     *            Évènement d'arrêt
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {

        // empty method
    }
}
