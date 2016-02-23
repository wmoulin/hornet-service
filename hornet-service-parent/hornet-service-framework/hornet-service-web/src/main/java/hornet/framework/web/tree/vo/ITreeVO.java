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

import java.util.Collection;
import java.util.Map;

/**
 * Interface pour représenter une arborescence complète.
 * 
 * @date 10 avr. 2013
 * @author EffiTIC
 * 
 */
public interface ITreeVO {

    /**
     * Setter for the attrs
     * 
     * @return Map
     */
    Map<String, String> getAttrs();

    /**
     * Getter for the data
     * 
     * @return Map
     */
    Map<String, Object> getData();

    /**
     * Getter for the children
     * 
     * @return Collection TreeItem
     */
    Collection<ITreeVO> getChildren();

    /**
     * Setter for the children
     * 
     * @param children
     *            Collection TreeItem
     */
    void setChildren(
            Collection<ITreeVO> children);

    /**
     * Getter for the id
     * 
     * @return String
     */
    String getId();

    /**
     * Setter for the id
     * 
     * @param id
     *            String
     */
    void setId(
            String id);

    /**
     * Getter for the title
     * 
     * @return String
     */
    String getTitle();

    /**
     * Setter for the title
     * 
     * @param title
     *            String
     */
    void setTitle(
            String title);

    /**
     * Getter for the type
     * 
     * @return String
     */
    String getType();

    /**
     * Setter for the type
     * 
     * @param type
     *            String
     */
    void setType(
            String type);

    /**
     * Getter for the url
     * 
     * @return String
     */
    String getUrl();

    /**
     * Setter for the url
     * 
     * @param url
     *            String
     */
    void setUrl(
            String url);

}
