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
package hornet.framework.clamav.bo;

/**
 * Classe correspondant à la réponse de clamAV.
 * 
 */
public class ClamAvResponse {

    /**
     * Catégorie des réponses de ClamAV.
     */
    public enum CategorieReponse {

        /** The no service. */
        NO_SERVICE,
        /** The timeout. */
        TIMEOUT,
        /** The virus. */
        VIRUS,
        /** The no virus. */
        NO_VIRUS,
        /** The stats. */
        STATS,
        /** The version. */
        VERSION,
        /** The undefined. */
        UNDEFINED
    }

    /** RESP_OK. */
    private static final String RESP_OK = "OK";

    /** Réponse retournée par ClamAV. */
    private String response;

    /** Etat de la réponse : virus ou non. */
    private transient String etat;

    /** Nom du virus. */
    private transient String virusName;

    /** Statistique ClamAV. */
    private transient String stats;

    /** version ClamAV. */
    private transient String version;

    /** categorieReponse. */
    private transient CategorieReponse categorieReponse;

    /**
     * 
     */
    public ClamAvResponse() {

        super();
        this.categorieReponse = CategorieReponse.UNDEFINED;
    }

    /**
     * Réponse ClamAV.
     * 
     * @return la réponse
     */
    public final String getResponse() {

        return this.response;
    }

    /**
     * ajout de la réponse.
     * 
     * @param response
     *            la réponse
     */
    public final void setResponse(final String response) {

        this.response = response;
    }

    /**
     * Etat de la réponse.
     * 
     * @return l'état
     */
    public final String getEtat() {

        return this.etat;
    }

    /**
     * Nom du virus.
     * 
     * @return le virus
     */
    public final String getVirusName() {

        return this.virusName;
    }

    /**
     * Récupération des stats clamAV.
     * 
     * @return les statistiques
     */
    public final String getStats() {

        return this.stats;
    }

    /**
     * Récupération de la version de ClamAV.
     * 
     * @return la version
     */
    public final String getVersion() {

        return this.version;
    }

    /**
     * A utiliser pour savoir si un virus a été détecté.
     * 
     * @return the categorie reponse
     */
    public final CategorieReponse getCategorieReponse() {

        return this.categorieReponse;
    }

    /**
     * Position du nom du virus dans la réponse.
     */
    private static final int POS_NOM_VIRUS = 11;

    /**
     * Creer une instance à partir d'une réponse d'analyse virale. Si on a le mot OK sans le mot FOUND alors
     * c'est qu'il n'y a pas de virus, ni d'erreur
     * 
     * @param resultat
     *            resultat
     * @return ClamAvResponse
     */
    public static final ClamAvResponse createVirusResponse(final String resultat) {

        ClamAvResponse response;
        response = new ClamAvResponse();
        response.setResponse(resultat);
        if (resultat.length() != 0) {
            if ((resultat.lastIndexOf("FOUND") != -1) || (resultat.indexOf(ClamAvResponse.RESP_OK) == -1)) {
                // le nom du virus comme après le message
                // "1: stream: "
                // soit à la 11ième position
                response.etat = "KO";
                response.virusName = resultat.substring(ClamAvResponse.POS_NOM_VIRUS);
                response.categorieReponse = CategorieReponse.VIRUS;
            } else if (resultat.indexOf(ClamAvResponse.RESP_OK) == -1) {
                response.etat = "KO";
                response.virusName = resultat.substring(ClamAvResponse.POS_NOM_VIRUS);
                response.categorieReponse = CategorieReponse.NO_SERVICE;
            } else {
                response.etat = ClamAvResponse.RESP_OK;
                response.virusName = "Pas de virus trouvé";
                response.categorieReponse = CategorieReponse.NO_VIRUS;
            }
        }

        return response;
    }

    /**
     * Creer une instance à partir d'une réponse issue de la commande stats.
     * 
     * @param resultat
     *            resultat
     * @return ClamAvResponse
     */
    public static final ClamAvResponse createStatsResponse(final String resultat) {

        ClamAvResponse response;
        response = new ClamAvResponse();
        response.categorieReponse = CategorieReponse.STATS;
        response.stats = resultat;

        return response;
    }

    /**
     * Creer une instance à partir d'une réponse issue de la commande version.
     * 
     * @param resultat
     *            resultat
     * @return ClamAvResponse
     */
    public static final ClamAvResponse createVersionResponse(final String resultat) {

        ClamAvResponse response;
        response = new ClamAvResponse();

        response.version = resultat;
        response.categorieReponse = CategorieReponse.VERSION;

        return response;
    }

    /**
     * Créer une instance représentant un appel en timeout.
     * 
     * @return ClamAvResponse
     */
    public static final ClamAvResponse createTimeoutResponse() {

        ClamAvResponse response;
        response = new ClamAvResponse();
        response.categorieReponse = CategorieReponse.TIMEOUT;
        return response;
    }

    /**
     * Créer une instance représentant un appel en échec suite à un service non opérationnel .
     * 
     * @return ClamAvResponse
     */
    public static final ClamAvResponse createNoServiceResponse() {

        ClamAvResponse response;
        response = new ClamAvResponse();
        response.categorieReponse = CategorieReponse.NO_SERVICE;
        return response;
    }

    /**
     * Méthode d'impression.
     * 
     * @return l'état de la réponse
     */
    @Override
    public final String toString() {

        final StringBuilder reponse = new StringBuilder("ClamAvResponse : \n");
        reponse.append(this.categorieReponse.toString());
        if (this.etat != null) {
            reponse.append("\netat=").append(this.etat);
        }
        if (this.response != null) {
            reponse.append("\nresponse=").append(this.response);
        }
        if (this.stats != null) {
            reponse.append("\nstats=").append(this.stats);
        }
        if (this.version != null) {
            reponse.append("\nversion=").append(this.version);
        }
        if (this.virusName != null) {
            reponse.append("\nvirusName=").append(this.virusName);
        }
        return reponse.toString();
    }

}
