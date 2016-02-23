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
import hornet.framework.web.tree.vo.ITreeVO;

/**
 * Structure commune aux builders pour créer un élément d'arborescence à partir d'un noeud donné si le type du
 * noeud est supporté.
 * 
 * @date 10 avr. 2013
 * @author EffiTIC
 * 
 */
public abstract class AbstractTreeNodeBuilder implements ITreeNodeBuilder {

    /**
     * Valeur par défaut pour les noeuds gérés par le builder
     */
    private Object defaultNodeValue;

    /**
     * Type de noeud supporté par le builder
     */
    private Class<? extends ITreeNode> nodeType;

    /**
     * Constructeur
     * 
     * @param nodeType
     *            Type de noeud supporté par le builder
     */
    public AbstractTreeNodeBuilder(
                final Class<? extends ITreeNode> nodeType) {

        this.nodeType = nodeType;
    }

    /**
     * Constructeur
     * 
     * @param nodeType
     *            Type de noeud supporté par le builder
     * @param defaultNodeValue
     *            Valeur par défaut pour les noeuds gérés par le builder
     */
    public AbstractTreeNodeBuilder(
                final Class<? extends ITreeNode> nodeType, final String defaultNodeValue) {

        this.nodeType = nodeType;
        this.defaultNodeValue = defaultNodeValue;
    }

    /**
     * 
     * {@inheritDoc} Si le type de noeud n'est pas supporté, aucun élément d'arborescence n'est construit.
     */
    @Override
    public ITreeVO convertNode(final ITreeNode node) {

        ITreeVO newTreeItem = null;
        if (this.isSupportedNode(node)) {

            this.initTreeItem(node);
            this.buildTreeItem(node);
            newTreeItem = this.getTreeItem();
        }

        return newTreeItem;
    }

    /**
     * Vérifie si le noeud courant est supporté par le builder
     * 
     * @param node
     *            Noeud courant
     * @return <tt>true</tt> si le noeud courant n'est pas nul et si son type correspond à la classe supportée
     *         par le builder, <tt>false</tt> sinon.
     */
    protected boolean isSupportedNode(final ITreeNode node) {

        return node != null && (node.getClass() == this.getNodeType());
    }

    /**
     * Initialise l'élément généré par le builder. Méthode à surcharger par les sous-classes.
     * 
     * @param node
     *            Noeud courant
     */
    protected abstract void initTreeItem(ITreeNode node);

    /**
     * Met a jour l'élément généré par le builder à partir des données du noeud courant. Méthode à surcharger
     * par les sous-classes.
     * 
     * @param node
     *            Noeud courant
     */
    protected abstract void buildTreeItem(ITreeNode node);

    /**
     * Retourne l'élément d'arborescence en cours de génération
     * 
     * @return Elément d'arborescence créé
     */
    public abstract ITreeVO getTreeItem();

    /**
     * Getter defaultNodeValue
     * 
     * @return Objet par défaut à utiliser pour construire l'élément d'arborescence
     */
    public Object getDefaultNodeValue() {

        return this.defaultNodeValue;
    }

    /**
     * Setter defaultNodeValue
     * 
     * @param defaultNodeValue
     *            Objet par défaut à utiliser pour construire l'élément d'arborescence
     */
    public void setDefaultNodeValue(final Object defaultNodeValue) {

        this.defaultNodeValue = defaultNodeValue;
    }

    /**
     * Getter nodeType {@inheritDoc}
     * 
     */
    @Override
    public Class<? extends ITreeNode> getNodeType() {

        return this.nodeType;
    }

    /**
     * Setter nodeType
     * 
     * @param nodeType
     *            Classe implémentant ITreeNode
     */
    public void setNodeType(final Class<? extends ITreeNode> nodeType) {

        this.nodeType = nodeType;
    }

}
