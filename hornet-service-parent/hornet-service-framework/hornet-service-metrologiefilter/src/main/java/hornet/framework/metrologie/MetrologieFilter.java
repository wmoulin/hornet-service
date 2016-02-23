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
package hornet.framework.metrologie;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Projet hornetserver.
 *
 * @author EffiTIC
 * @date 26 mai 2011
 */
public class MetrologieFilter implements Filter {

    /** La clé MDC utilisée pour logguer l'URI de la requête. */
    private static final String MDC_URI = "URI";

    /**
     * La clé MDC utilisée pour logguer l'utilisateur qui a effectué la requête.
     */
    private static final String MDC_UTILISATEUR = "UTILISATEUR";

    /** The Constant ORDRE_AFFICHAGE_CHRONOMETRE. */
    private static final String ORDRE_AFFICHAGE_CHRONOMETRE = "ordreAffichageChronometres";

    /** The Constant NOM_LOGGER. */
    private static final String NOM_LOGGER = "nomLogger";

    /** The Constant NOM_LOGGER_DEFAUT. */
    private static final String NOM_LOGGER_DEFAUT = "hornet.framework.technical.metrologie.log";

    /**
     * logger utilisé par la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MetrologieFilter.class.getName());

    /**
     * Logger utilisé pour tracer les temps mesurés par les chronomètres.
     */
    private transient Logger loggerChronometreSet;

    /**
     * L'objet en charge de l'affichage des temps mesurés par les chronomètres.
     */
    private transient ChronometreSetMessageFormatter chronometreSetMessageFormatter;

    /**
     * Destroy.
     *
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {

        this.loggerChronometreSet = null;
        this.chronometreSetMessageFormatter = null;
    }

    /**
     * <p>
     * Surcharge pour la métrologie.
     * </p>
     * <p>
     * Crée un objet de type <tt>ChronometreSet</tt>, démarre le chronomètre mesurant le temps d'exécution de
     * la requête et un chronomètre mesurant le temps d'affichage.
     * </p>
     * <p>
     * Les temps mesurés sont ensuite tracés.
     * </p>
     *
     * @param req
     *            La requête
     * @param rep
     *            La réponse
     * @param chain
     *            the chain
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ServletException
     *             the servlet exception
     */
    @Override
    public void doFilter(final ServletRequest req, final ServletResponse rep, final FilterChain chain)
        throws IOException, ServletException {

        String uri = "NON-HTTP";
        Chrono chronoRequest;

        ChronometreSet chronometreSet = ChronometreSet.createOrRetrieveFromRequest(req);
        final boolean chronometreSetCreatedByThisCall = chronometreSet == null;

        if (chronometreSetCreatedByThisCall) {
            chronometreSet = ChronometreSet.createOrRetrieveFromRequest(req);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Création de {}", chronometreSet);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Réutilisation de {}", chronometreSet);
            }
        }
        try {
            if (req instanceof HttpServletRequest) {
                final HttpServletRequest httpReq = (HttpServletRequest) req;
                uri = httpReq.getRequestURI();
                final HttpServletRequest httpRequest = (HttpServletRequest) req;
                String userName = "INCONNU";

                if (httpRequest.getRemoteUser() != null) {
                    userName = httpRequest.getRemoteUser();
                }

                MDC.put(MDC_URI, uri);
                MDC.put(MDC_UTILISATEUR, userName);
            }
            LOGGER.debug("DEBUT URI {}", uri);
            chronoRequest = chronometreSet.getChrono("REQUEST");
            chronoRequest.start();
            try {

                chain.doFilter(req, rep);
            } finally {
                chronoRequest.stop();

                this.logChronometreSet(chronometreSet, uri);
            }
        } finally {
            LOGGER.debug("FIN  URI {}", uri);
            MDC.remove(MDC_URI);
            MDC.remove(MDC_UTILISATEUR);
            if (chronometreSetCreatedByThisCall) {
                ChronometreSet.removeFromThread();
            }
        }
    }

    /**
     * Log les temps mesurés par les chronomètres.
     *
     * @param chronometreSet
     *            Les chronomètres.
     */
    private void logChronometreSet(final ChronometreSet chronometreSet, final String uri) {

        if (this.loggerChronometreSet != null && this.loggerChronometreSet.isInfoEnabled()) {
            final String message = this.chronometreSetMessageFormatter.format(chronometreSet);
            this.loggerChronometreSet.info(uri + " - " + message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

        this.initialiserLoggerChronometreSet(filterConfig);
        this.initialiserChronometreSetMessageFormatter(filterConfig);
    }

    /**
     * Initialise le logger en charge de la journalisation des temps mesurés par les chronomètres.
     *
     * @param filterConfig
     *            La configuration du filtre.
     */
    private void initialiserLoggerChronometreSet(final FilterConfig filterConfig) {

        final String nomLogger = filterConfig.getInitParameter(NOM_LOGGER);

        if (StringHelper.isBlank(nomLogger)) {
            this.loggerChronometreSet = LoggerFactory.getLogger(NOM_LOGGER_DEFAUT);
        } else {
            this.loggerChronometreSet = LoggerFactory.getLogger(nomLogger);
        }
    }

    /**
     * Initialise l'objet en charge du formattage des temps mesurés par les chronomètres.
     *
     * @param filterConfig
     *            La configuration du filtre.
     */
    private void initialiserChronometreSetMessageFormatter(final FilterConfig filterConfig) {

        final char separator = ',';

        String ordreAffichage = filterConfig.getInitParameter(ORDRE_AFFICHAGE_CHRONOMETRE);
        if (ordreAffichage == null) {
            ordreAffichage = "";
        }

        final StringBuilder ordreAffichageChronometre = new StringBuilder(ordreAffichage);

        // Le temps d'execution de la requête est toujours affiché en
        // premier.
        ordreAffichageChronometre.append(Chrono.Type.REQUEST).append(separator)
                    .append(Chrono.Type.CONTROLLEUR).append(separator).append(Chrono.Type.SERVICE)
                    .append(separator).append(Chrono.Type.DAO).append(separator)
                    .append(ordreAffichageChronometre);

        this.chronometreSetMessageFormatter =
                    new ChronometreSetMessageFormatter(ordreAffichageChronometre.toString());
    }
}
