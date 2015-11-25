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

/**
 * 
 * Projet hornetserver.
 * 
 * @date 25 mai 2011
 * @author EffiTIC
 * 
 */
public class Chrono {

    /**
     * 
     */
    static final class Type {

        /**
         * 
         */
        public static final String REQUEST = "REQUEST";

        /**
         * CONTROLLEUR
         */
        public static final String CONTROLLEUR = "CONTROLLEUR";

        /**
         * SERVICE
         */
        public static final String SERVICE = "SERVICE";

        /**
         * DAO
         */
        public static final String DAO = "DAO";

        /**
         * 
         */
        private Type() {

        }

    }

    /**
     * Le temps mesuré par le chrono.
     */
    private long time;

    /**
     * L'instant à partir duquel le chrono a été démarré.
     */
    private transient long currentStart;

    /**
     * Le libellé du chronomètre.
     */
    private String libelle;

    /**
     * Remet à zéro le compteur.
     */
    public void reset() {

        this.time = 0L;
        this.currentStart = 0L;
    }

    /**
     * Démarre le chrono.
     */
    public void start() {

        this.currentStart = System.currentTimeMillis();

    }

    /**
     * <p>
     * Arrête le chrono.
     * </p>
     * <p>
     * Le temps écoulé depuis le dernier appel à <code>start</code> est ajouté au temps global sauf si la
     * méthode <code>start</code> n'a pas été appelée ou si la méthode <code>stop</code> a déjà été appelée.
     * </p>
     */
    public void stop() {

        if (this.currentStart != 0L) {
            this.time = this.time + System.currentTimeMillis() - this.currentStart;
            if (this.time == 0L) {
                // Le temps mesuré a pour valeur minimum 1 ms.
                this.time = 1L;
            }
            this.currentStart = 0L;
        }
    }

    /**
     * @return Le libellé du chronomètre.
     */
    protected final String getLibelle() {

        return this.libelle;
    }

    /**
     * Renseigné le libellé du chronomètre.
     * 
     * @param libelle
     *            Le libellé.
     */
    protected final void setLibelle(final String libelle) {

        this.libelle = libelle;
    }

    /**
     * 
     * @see #stop .
     * @return Le temps mesuré par le chronomètre.
     */
    public final long getTime() {

        return this.time;
    }

    /**
     * Renseigne le temps mesuré par le chronomètre.
     * 
     * @param time
     *            Le temps mesuré.
     */
    protected final void setTime(final long time) {

        this.time = time;
    }
}
