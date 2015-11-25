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
package hornet.framework.web.bo;

/**
 * @author Hornet
 * @since 1.0 - 4 févr. 2015
 */
public class ErreurServiceInfos {

    protected String url;

    protected String ex;

    protected Long numErreur;
    
    protected String errorType;  

    public ErreurServiceInfos(
                final String url, final Long numErreur, final String errorType, final Exception ex) {

        this.url = url;
        this.ex = ex.getLocalizedMessage();
        this.numErreur = numErreur;
        this.errorType = errorType;
    }

    /**
     * @return url
     */
    public String getUrl() {

        return url;
    }

    /**
     * @param url
     *            url
     */
    public void setUrl(final String url) {

        this.url = url;
    }

    /**
     * @return ex
     */
    public String getEx() {

        return ex;
    }

    /**
     * @param ex
     *            ex
     */
    public void setEx(final String ex) {

        this.ex = ex;
    }

    /**
     * @return numErreur
     */
    public Long getNumErreur() {

        return numErreur;
    }

    /**
     * @param numErreur
     *            numErreur
     */
    public void setNumErreur(final Long numErreur) {

        this.numErreur = numErreur;
    }
    
    /**
     * @return errorType
     */
    public String getErrorType() {

        return errorType;
    }

    /**
     * @param errorType
     *            errorType
     */
    public void setErrorType(final String errorType) {

        this.errorType = errorType;
    }

}
