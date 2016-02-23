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
import hornet.framework.web.tree.bo.TreeNodeDefault;
import hornet.framework.web.tree.vo.ITreeVO;
import hornet.framework.web.tree.vo.TreeVO;

/**
 * Builder par défaut pour créer un élément d'arborescence à partir d'un noeud donné. Initialise uniquement le
 * libellé des éléments.
 * 
 * @date 10 avr. 2013
 * @author EffiTIC
 * 
 */
public class TreeNodeDefaultBuilder extends AbstractTreeNodeBuilder {

    /**
     * Arbre courant géneré par le builder
     */
    private ITreeVO treeItem;

    /**
     * Constructeur par défaut
     */
    public TreeNodeDefaultBuilder() {

        super(TreeNodeDefault.class);
    }

    /**
     * Constructeur
     * 
     * @param nodeType
     *            Type de noeud supporte par le builder
     */
    public TreeNodeDefaultBuilder(
                final Class<? extends ITreeNode> nodeType) {

        super(nodeType);
    }

    /**
     * Constructeur
     * 
     * @param nodeType
     *            Type de noeud supporté par le builder
     * @param defaultNodeValue
     *            Valeur par défaut pour les noeuds gérés par le builder
     */
    public TreeNodeDefaultBuilder(
                final Class<? extends ITreeNode> nodeType, final String defaultNodeValue) {

        super(nodeType, defaultNodeValue);
    }

    /**
     * {@inheritDoc}
     * 
     * Met à jour la valeur du noeud courant si celle-ci est nulle et qu'une valeur par défaut est définie.
     * 
     */
    @Override
    protected void initTreeItem(final ITreeNode node) {

        if (node.getData() == null && this.getDefaultNodeValue() != null) {
            node.setData(this.getDefaultNodeValue());
        }
        this.setTreeItem(new TreeVO());
    }

    /**
     * {@inheritDoc}
     * 
     * Initialise le libellé à partir de la valeur du noeud courant si celle-ci est définie.
     * 
     */
    @Override
    protected void buildTreeItem(final ITreeNode node) {

        if (node != null && node.getData() != null) {
            this.buildTitle(node.getData());
        }
    }

    /**
     * Met à jour le libellé à partir de l'objet courant
     * 
     * @param data
     *            Objet courant utilisé pour construire l'élément d'arborescence
     */
    private void buildTitle(final Object data) {

        this.getTreeItem().setTitle(data.toString());
    }

    /**
     * Getter treeItem. {@inheritDoc}
     */
    @Override
    public ITreeVO getTreeItem() {

        return this.treeItem;
    }

    /**
     * Setter treeItem
     * 
     * @param treeItem
     *            Elément d'arborescence en cours de génération
     */
    public void setTreeItem(final ITreeVO treeItem) {

        this.treeItem = treeItem;
    }

}
