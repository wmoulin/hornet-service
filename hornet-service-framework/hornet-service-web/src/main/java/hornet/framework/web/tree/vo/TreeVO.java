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
package hornet.framework.web.tree.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe par défaut pour définir une arborescence complète.
 * 
 * @date 10 avr. 2013
 * @author EffiTIC
 * 
 */
public class TreeVO implements ITreeVO {

    /**
     * Tree Node Attributes
     */
    private final transient Map<String, String> attrs;

    /**
     * Tree Node Children
     */
    private Collection<ITreeVO> children;

    /**
     * Tree Node Data
     */
    private final transient Map<String, Object> data;

    /**
     * Tree Node Id
     */
    private String id;

    /**
     * Tree Node Title
     */
    private String title;

    /**
     * Tree Node Type
     */
    private String type;

    /**
     * Tree Node Url
     */
    private String url;

    /**
     * Constructeur par défaut
     */
    public TreeVO() {

        super();
        this.data = new HashMap<String, Object>();
        this.attrs = new HashMap<String, String>();
        this.children = new ArrayList<ITreeVO>();
    }

    /**
     * Constructeur
     * 
     * @param title
     *            Tree node title
     */
    public TreeVO(
                final String title) {

        this();
        this.title = title;
        this.attrs.put("title", title);
    }

    /**
     * Constructeur
     * 
     * @param title
     *            Tree node title
     * @param children
     *            Tree node children
     */
    public TreeVO(
                final String title, final Collection<ITreeVO> children) {

        this(title);
        this.children = children;
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> getAttrs() {

        return this.attrs;
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getData() {

        return this.data;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ITreeVO> getChildren() {

        return this.children;
    }

    /** {@inheritDoc} */
    @Override
    public void setChildren(final Collection<ITreeVO> children) {

        this.children = children;
    }

    /** {@inheritDoc} */
    @Override
    public String getId() {

        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public void setId(final String id) {

        this.id = id;
        this.attrs.put("id", id);
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {

        return this.title;
    }

    /** {@inheritDoc} */
    @Override
    public void setTitle(final String title) {

        this.title = title;
        this.attrs.put("title", title);
    }

    /** {@inheritDoc} */
    @Override
    public String getType() {

        return this.type;
    }

    /** {@inheritDoc} */
    @Override
    public void setType(final String type) {

        this.type = type;
        this.attrs.put("type", type);
    }

    /** {@inheritDoc} */
    @Override
    public String getUrl() {

        return this.url;
    }

    /** {@inheritDoc} */
    @Override
    public void setUrl(final String url) {

        this.url = url;
        this.attrs.put("url", url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder(60);
        builder.append(this.getClass().getSimpleName()).append(" [id=").append(this.getId())
                    .append(", title=").append(this.getTitle()).append(", type=").append(this.getType())
                    .append(", url=").append(this.getUrl()).append(", attr=").append(this.getAttrs())
                    .append(", data=").append(this.getData()).append(", children=")
                    .append(this.getChildren()).append(']');
        return builder.toString();
    }

}
