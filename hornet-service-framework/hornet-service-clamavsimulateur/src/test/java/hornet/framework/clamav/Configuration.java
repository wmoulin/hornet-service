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
package hornet.framework.clamav;

/**
 * Classe de configuration des tests clamav Projet fwhornetj2ee.
 * 
 * @author EffiTIC - she200028
 * @date 5 déc. 2011
 */
public class Configuration {

    /** The nb fichier. */
    private int nbFichier;

    /** The nb thread. */
    private int nbThread;

    /** The tps acceptable. */
    private int tpsAcceptable;

    /** The chemin fichier. */
    private String cheminFichier;

    /** The chemin. */
    private String chemin;

    /** The ressources. */
    private String ressources;

    /**
     * Gets the nb fichier.
     * 
     * @return Returns the nbFichier.
     */
    public final int getNbFichier() {

        return this.nbFichier;
    }

    /**
     * Sets the nb fichier.
     * 
     * @param nbFichier
     *            The nbFichier to set.
     */
    public final void setNbFichier(final int nbFichier) {

        this.nbFichier = nbFichier;
    }

    /**
     * Gets the nb thread.
     * 
     * @return Returns the nbThread.
     */
    public final int getNbThread() {

        return this.nbThread;
    }

    /**
     * Sets the nb thread.
     * 
     * @param nbThread
     *            The nbThread to set.
     */
    public final void setNbThread(final int nbThread) {

        this.nbThread = nbThread;
    }

    /**
     * Gets the tps acceptable.
     * 
     * @return Returns the tpsAcceptable.
     */
    public final int getTpsAcceptable() {

        return this.tpsAcceptable;
    }

    /**
     * Sets the tps acceptable.
     * 
     * @param tpsAcceptable
     *            The tpsAcceptable to set.
     */
    public final void setTpsAcceptable(final int tpsAcceptable) {

        this.tpsAcceptable = tpsAcceptable;
    }

    /**
     * Gets the chemin fichier.
     * 
     * @return Returns the cheminFichier.
     */
    public final String getCheminFichier() {

        return this.cheminFichier;
    }

    /**
     * Sets the chemin fichier.
     * 
     * @param cheminFichier
     *            The cheminFichier to set.
     */
    public final void setCheminFichier(final String cheminFichier) {

        this.cheminFichier = cheminFichier;
    }

    /**
     * Retourne le chemin à analyser.
     * 
     * @return chemin d'analyse
     */
    public String getChemin() {

        return this.chemin;
    }

    /**
     * Défini le chemin à analyser.
     * 
     * @param chemin
     *            the new chemin
     */
    public void setChemin(final String chemin) {

        this.chemin = chemin;
    }

    /**
     * chemin des ressources.
     * 
     * @return le chemin
     */
    public String getRessources() {

        return this.ressources;
    }

    /**
     * Ajout du chemin des ressources.
     * 
     * @param ressources
     *            la chemin
     */
    public void setRessources(final String ressources) {

        this.ressources = ressources;
    }

}