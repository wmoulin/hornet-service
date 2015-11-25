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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Projet hornetserver.
 *
 * @date 25 mai 2011
 * @author EffiTIC
 *
 */
public class ChronometreSet {

    /**
     * Nom de l'attribut de requête contenant l'objet ChronometreSet.
     */
    public static final String ATTRIBUTE_CHONOMETRE_SET = "Metrologie.ChronometreSet";

    /**
     * Logger utilisé pour la classe.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ChronometreSet.class);

    /**
     *
     */
    private static ThreadLocal<ChronometreSet> chronometreSetByThread = new ThreadLocal<ChronometreSet>();

    /**
     * Les chronomètres.
     */
    private transient Map<String, Chrono> chronos;

    /**
     * Constructeur.
     */
    public ChronometreSet() {

        this.chronos = new HashMap<String, Chrono>();
    }

    /**
     * Retourne un chronomètre à partir de son libellé.
     *
     * @param libelle
     *            Le Libellé.
     * @return Le chronomètre correspondant au libellé.
     */
    public final Chrono getChrono(final String libelle) {

        Chrono chrono = this.chronos.get(libelle);

        if (chrono == null) {
            chrono = new Chrono();
            chrono.setLibelle(libelle);
            this.chronos.put(libelle, chrono);
        }

        return chrono;
    }

    /**
     *
     * @return Le chronomètre utilisé pour mesurer le temps d'exécution d'une requête.
     */
    public final Chrono getChronoRequest() {

        return this.getChrono(Chrono.Type.REQUEST);
    }

    /**
     * Renvoie les temps mesurés par les chronomètres.
     *
     * @return Une map dont les clés sont les libellés des chronométres et les valeurs les temps mesurés par
     *         chaque chronomètre.
     */
    public Map<String, Long> getChronoValues() {

        final Map<String, Long> times = new HashMap<String, Long>();

        if (this.chronos != null) {
            for (final Entry<String, Chrono> entry : this.chronos.entrySet()) {
                times.put(entry.getKey(), entry.getValue().getTime());
            }
        }

        return times;
    }

    /**
     * <p>
     * Récupère l'instance de <code>ChronometreSet</code> à utiliser pour la requête HTTP.
     * </p>
     * <p>
     * Si la requête ne contient pas l'instance de <code>ChronometreSet</code> alors l'instance est récupérée
     * à partir du thread courant.
     * </p>
     *
     * @see #ATTRIBUTE_CHONOMETRE_SET
     * @param request
     *            La requête HTTP en cours
     * @return L'instance de <code>ChronometreSet</code> ou null si elle n'existe pas.
     */
    static ChronometreSet retrieveFromRequest(final ServletRequest request) {

        ChronometreSet chronometreSet = (ChronometreSet) request.getAttribute(ATTRIBUTE_CHONOMETRE_SET);

        if (chronometreSet == null) {
            chronometreSet = retrieveFromThread();
        }

        return chronometreSet;
    }

    /**
     * Récupère ou crée l'instance de <code>ChronometreSet</code> à utiliser pour la requête HTTP. L'instance
     * est stockée comme attribut de requête
     *
     * @see #ATTRIBUTE_CHONOMETRE_SET
     * @param request
     *            La requête HTTP.
     * @return L'instance de <code>ChronometreSet</code>.
     */
    static ChronometreSet createOrRetrieveFromRequest(final ServletRequest request) {

        // Récupération/Création du chronometreSet
        ChronometreSet chronometreSet = (ChronometreSet) request.getAttribute(ATTRIBUTE_CHONOMETRE_SET);

        if (chronometreSet == null) {
            chronometreSet = retrieveFromThread();

            if (chronometreSet == null) {
                chronometreSet = new ChronometreSet();
                LOGGER.debug("Création du chronometreSet {}", chronometreSet);
                chronometreSetByThread.set(chronometreSet);
            }

            request.setAttribute(ATTRIBUTE_CHONOMETRE_SET, chronometreSet);

        }
        return chronometreSet;
    }

    /**
     * Récupère ou crée l'instance de <code>ChronometreSet</code> à utiliser pour le thread courant.
     *
     * @return Une instance de <code>ChronometreSet</code>
     */
    static ChronometreSet retrieveFromThread() {

        // Récupération/Création du chronometreSet
        final ChronometreSet chronometreSet = chronometreSetByThread.get();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("retrieve chronometreSet {}", chronometreSet);
        }
        return chronometreSet;
    }

    /**
     * Récupère ou crée l'instance de MetroRequest à utiliser pour le thread courant.
     *
     * @return Une instance de MétroRequest
     */
    static ChronometreSet createOrRetrieveFromThread() {

        // Récupération/Création du chronometreSet
        ChronometreSet chronometreSet = chronometreSetByThread.get();
        if (chronometreSet == null) {
            chronometreSet = new ChronometreSet();
            if (LOGGER.isDebugEnabled()) {

                LOGGER.debug("Création du chronometreSet {}", chronometreSet);
            }
            chronometreSetByThread.set(chronometreSet);
        }
        return chronometreSet;
    }

    /**
     * Supprime l'instance Metrologie du thread courant.
     *
     */
    static void removeFromThread() {

        // Récupération/Création du MetroRequest
        final ChronometreSet chronometreSet = chronometreSetByThread.get();
        if (LOGGER.isDebugEnabled()) {

            LOGGER.debug("Suppression du chronometreSet {}", chronometreSet);
        }
        chronometreSetByThread.set(null);
    }

    /**
     * Récupère une instance de chronometre set.
     *
     * @return L'instance de chronometreset.
     */
    public static ChronometreSet get() {

        ChronometreSet chronometreSet = retrieveFromThread();

        if (chronometreSet == null) {
            chronometreSet = new ChronometreSet();
        }

        return chronometreSet;
    }

    /**
     * Récupère une instance de chronometre set.
     *
     * @param servletRequest
     *            La requête.
     * @return L'instance de chronometreset.
     */
    public static ChronometreSet get(final ServletRequest servletRequest) {

        ChronometreSet chronometreSet = retrieveFromRequest(servletRequest);

        if (chronometreSet == null) {
            chronometreSet = new ChronometreSet();
        }

        return chronometreSet;
    }

}
