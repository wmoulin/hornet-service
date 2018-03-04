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
package hornet.framework.web.controller;

import java.util.Arrays;

import hornet.framework.exception.BusinessException;
import hornet.framework.exception.BusinessListException;
import hornet.framework.web.bo.TechnicalError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import hornet.framework.exception.TechnicalException;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Projet Hornet.
 * 
 * @author MEAE - Ministère de l'Europe et des Affaires étrangères
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * BusinessException Handler
     * @param req http request
     * @param res http response
     * @param ex exception
     * @return List of BusinessException
     */
    @ExceptionHandler({BusinessException.class, BusinessListException.class})
    public @ResponseBody BusinessListException defaultBusinessErrorHandler(final HttpServletRequest req, final HttpServletResponse res,
                final Exception ex) {

        final String url = req.getRequestURI();
        BusinessListException returnValue = null;
        

        // If the exception is annotated with @ResponseStatus 
        ResponseStatus rs = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (rs != null && rs.value() != null) {
        	res.setStatus(rs.value().value());
        } else {
        	res.setStatus(HttpStatus.OK.value());
        }

        // La liste d'exceptions récapitule les erreurs et les success dans le cas d'actions de masse
        if(ex instanceof BusinessListException) {
        	returnValue = (BusinessListException) ex;
        } else if(ex instanceof BusinessException ){
        	returnValue = new BusinessListException(Arrays.asList((BusinessException) ex));
        }
        
        returnValue.setUrl(url);
        
        LoggerFactory.getLogger(this.getClass())
        .debug(
        		"Erreur métier survenue lors de l'appel à l'URL " + url + " : "
        				+ returnValue.toString());
        return returnValue;
    }

    
    /**
     * BusinessException Handler
     *
     * @param req
     *            http request
     * @param res
     *            http response
     * @param ex
     *            exception
     * @return List of BusinessException
     */
    @ExceptionHandler({
        NoHandlerFoundException.class})
    public @ResponseBody TechnicalException defaultNotFoundErrorHandler(final HttpServletRequest req,
                final HttpServletResponse res, final Exception ex) {

    	final Long numErreur = System.currentTimeMillis();
    	final String url = req.getRequestURI();
    	
        // If the exception is annotated with @ResponseStatus 
        ResponseStatus rs = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (rs != null && rs.value() != null) {
        	res.setStatus(rs.value().value());
        } else {
        	res.setStatus(HttpStatus.NOT_FOUND.value());
        }
    	
    	LoggerFactory.getLAogger(this.getClass())
    	.error(
    			"Erreur #" + numErreur + " survenue lors de l'appel à l'URL " + url + " : "
    					+ ex.getMessage(), ex);
    	

    	return new TechnicalError(ex.getClass().getName(), numErreur.toString(), ex.getMessage(), null);
    }
    
    /**
     * Exception Handler
     * @param req http request
     * @param res http response
     * @param ex exception
     * @return a technical error
     */
    @ExceptionHandler(value=Exception.class)
    public @ResponseBody TechnicalError defaultErrorHandler(final HttpServletRequest req,final HttpServletResponse res, 
    		final Exception ex) {
    	
    	final Long numErreur = System.currentTimeMillis();
    	final String url = req.getRequestURI();
    	
        // If the exception is annotated with @ResponseStatus 
        ResponseStatus rs = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (rs != null && rs.value() != null) {
        	res.setStatus(rs.value().value());
        } else {
        	res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    	
    	LoggerFactory.getLogger(this.getClass())
    	.error(
    			"Erreur #" + numErreur + " survenue lors de l'appel à l'URL " + url + " : "
    					+ ex.getMessage(), ex);
    	

    	return new TechnicalError(ex.getClass().getName(), numErreur.toString(), ex.getMessage(), null);
    }
}
