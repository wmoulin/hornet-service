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
package hornet.framework.exception;

import hornet.framework.exception.BusinessException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * Projet Hornet.
 *
 * @author MAEDI
 *
 */
@JsonAutoDetect(fieldVisibility=Visibility.NONE, getterVisibility=Visibility.NONE)
public class BusinessListException extends RuntimeException {

    /**
     * <code>serialVersionUID</code> the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

	/**
	 * <code>url</code> the service url called
	 */
	private String url;
    
    /**
     * <code>exList</code> the exceptions list
     */
    private transient List<BusinessException> exList;

    /**
     * BusinessListException
     *
     */
    public BusinessListException() {

        super();
        this.exList = new ArrayList<BusinessException>();
        
    }

    /**
     * BusinessListException
     * 
     * @param exList
     *            the exceptions list
     */
    public BusinessListException(final List<BusinessException> exList) {

        super();
        this.exList = exList;
    }

	/**
	 * Getter of the url
	 * 
	 * @return the url
	 */
    @JsonProperty
	public String getUrl() {
		return url;
	}

	/**
	 * Setter of the url
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}
	
    /**
     * Getter of the exs
     *
     * @return Returns the exs.
     */
	@JsonProperty
    public BusinessException[] getExs() {

        return this.exList.toArray(new BusinessException[this.exList.size()]);
    }

    /**
     * Getter of the exList
     *
     * @return Returns the exList.
     */
    public List<BusinessException> getExList() {

        return this.exList;
    }
    
    /**
     * Add BusinessException to the exList.
     * @param e the exList ta add
     */
    public void addBusinessException(BusinessException e) {
    	if (this.exList == null) {
    		this.exList = new ArrayList<BusinessException>();
    	}
    	this.exList.add(e);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Throwable#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()+" [url=").append(this.url).append(", exList=[");
        
        if (CollectionUtils.isNotEmpty(exList)) {
	        for (BusinessException businessException : exList) {
	        	if (businessException != null) {
	                StringWriter sw = new StringWriter();
	                // printStackTrace call ToString()
	                businessException.printStackTrace(new PrintWriter(sw));
	                builder.append(sw.toString()).append("]");

	        	}
			}
        }
        
        return builder.append("]").toString();

    }
    
}
