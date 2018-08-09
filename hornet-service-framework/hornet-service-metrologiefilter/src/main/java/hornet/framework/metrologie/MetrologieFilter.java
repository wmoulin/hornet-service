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
package hornet.framework.metrologie;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        loggerChronometreSet = null;
        chronometreSetMessageFormatter = null;
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
        final HttpServletResponse httpRes = (HttpServletResponse) rep;

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
                String userName = "INCONNU";

                if (httpReq.getRemoteUser() != null) {
                    userName = httpReq.getRemoteUser();
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

                logChronometreSet(chronometreSet, uri, httpRes.getStatus());
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
    private void logChronometreSet(final ChronometreSet chronometreSet, final String uri,
                final Integer httpStatus) {

        if (loggerChronometreSet != null && loggerChronometreSet.isInfoEnabled()) {
            final String message = chronometreSetMessageFormatter.format(chronometreSet);
            loggerChronometreSet.info(uri + " - " + httpStatus + " - " + message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

        initialiserLoggerChronometreSet(filterConfig);
        initialiserChronometreSetMessageFormatter(filterConfig);
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
            loggerChronometreSet = LoggerFactory.getLogger(NOM_LOGGER_DEFAUT);
        } else {
            loggerChronometreSet = LoggerFactory.getLogger(nomLogger);
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
        .append(separator).append(Chrono.Type.DAO);

        chronometreSetMessageFormatter =
                    new ChronometreSetMessageFormatter(ordreAffichageChronometre.toString());
    }
}
