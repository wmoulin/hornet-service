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

import java.util.HashMap;
import java.util.Map;

import hornet.framework.web.tree.bo.ITreeNode;
import hornet.framework.web.tree.bo.TreeNodeDefault;

/**
 * Helper permettant de gérer une table de builders associés à un type de noeud.
 * 
 * @date 10 avr. 2013
 * @author EffiTIC
 * 
 */
public class TreeFactoryMapHelper implements ITreeFactoryHelper {

    /**
     * Liste des types de noeuds supportés associés au builder à utiliser pour les traiter
     */
    private final transient Map<Class<? extends ITreeNode>, ITreeNodeBuilder> treeItemBuilderMap;

    /**
     * Constructeur par defaut
     */
    public TreeFactoryMapHelper() {

        this.treeItemBuilderMap = new HashMap<Class<? extends ITreeNode>, ITreeNodeBuilder>();

        this.addTreeNodeBuilder(new TreeNodeDefaultBuilder(TreeNodeDefault.class));
        this.addTreeNodeBuilder(new TreeNodeBooleanBuilder());
    }

    /**
     * Ajoute un builder supplémentaire à la table. Il est associé au type de noeud qu'il supporte.
     * 
     * @see ITreeNodeBuilder#getNodeType()
     * 
     * @param builder
     *            Builder à utiliser pour traiter un type de noeud.
     */
    @Override
    public void addTreeNodeBuilder(final ITreeNodeBuilder builder) {

        if (builder.getNodeType() != null) {
            this.treeItemBuilderMap.put(builder.getNodeType(), builder);
        }

    }

    /**
     * Retourne le builder à utiliser pour traiter le noeud courant.
     * 
     * @param node
     *            Noeud courant
     * @return Builder associé au type du noeud, null sinon
     */
    @Override
    public ITreeNodeBuilder selectBuilder(final ITreeNode node) {

        return this.treeItemBuilderMap.get(node.getClass());
    }

}
