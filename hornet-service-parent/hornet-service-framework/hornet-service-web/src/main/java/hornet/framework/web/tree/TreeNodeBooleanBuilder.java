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
/**
 * 
 */
package hornet.framework.web.tree;

import hornet.framework.web.tree.bo.ITreeNode;
import hornet.framework.web.tree.bo.TreeNodeBoolean;

/**
 * Builder pour créer un élément d'arborescence à partir d'un noeud associé à un objet de type booléen.
 * Initialise le libellé d'un élément en fonction de la valeur de l'objet.
 * 
 * @date 10 avr. 2013
 * @author EffiTIC
 * 
 */
public class TreeNodeBooleanBuilder extends TreeNodeDefaultBuilder {

    /**
     * Texte utilisé pour générer les libellés à associer aux éléments de l'arborescence ayant la valeur TRUE
     */
    private transient String titleTrue = Boolean.TRUE.toString();

    /**
     * Texte utilisé pour générer les libellés à associer aux éléments de l'arborescence ayant la valeur FALSE
     */
    private transient String titleFalse = Boolean.FALSE.toString();

    /**
     * Constructeur par défaut
     */
    public TreeNodeBooleanBuilder() {

        super(TreeNodeBoolean.class);
    }

    /**
     * Constructeur
     * 
     * @param titleTrue
     *            Libellé pour les noeuds à TRUE
     * @param titleFalse
     *            Libellé pour les noeuds à FALSE
     * 
     */
    public TreeNodeBooleanBuilder(
                final String titleTrue, final String titleFalse) {

        this();
        this.titleTrue = titleTrue;
        this.titleFalse = titleFalse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildTreeItem(final ITreeNode node) {

        if (node != null && node.getData() != null) {
            this.buildTitle(Boolean.TRUE.equals(node.getData()));
        }
    }

    /**
     * Met à jour le libellé à partir de l'objet courant
     * 
     * @param data
     *            Booléen utilisé pour construire l'élément d'arborescence
     */
    private void buildTitle(final Boolean data) {

        if (Boolean.TRUE.equals(data)) {

            this.getTreeItem().setTitle(this.titleTrue);
        } else {

            this.getTreeItem().setTitle(this.titleFalse);
        }
    }

}
