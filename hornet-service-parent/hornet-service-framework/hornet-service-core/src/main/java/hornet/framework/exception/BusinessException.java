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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * Projet Hornet.
 *
 * @author MAEDI
 *
 */
@JsonAutoDetect(fieldVisibility=Visibility.NONE, getterVisibility=Visibility.NONE)
public class BusinessException extends RuntimeException {

	/**
	 * <code>serialVersionUID</code> the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <code>code</code> the l'exception code
	 */
	private final String code;


	/**
	 * <code>args</code> the args tableau des arguments
	 */
	private final List<String> argsList;

	/**
	 * BusinessException
	 *
	 * @param code
	 *            code de l'exception
	 */
	public BusinessException(final String code) {

		super(code);
		this.code = code;
		this.argsList = new ArrayList<String>();
	}

	/**
	 * @param code
	 *            code de l'exception
	 * @param args
	 *            tableau des arguments
	 */
	public BusinessException(final String code, final String[] args) {

		super();
		this.code = code;
		this.argsList = new ArrayList<String>(Arrays.asList(args));
	}

	/**
	 * @param code
	 *            code de l'exception
	 * @param argsList
	 *            liste des arguments
	 */
	public BusinessException(final String code, final List<String> argsList) {

		super();
		this.code = code;
		this.argsList = argsList;
	}

	/**
	 * BusinessException
	 *
	 * @param code
	 *            code de l'exception
	 * @param ex
	 *            exception
	 */
	public BusinessException(final String code, final Throwable ex) {

		super(ex);
		this.code = code;
		this.argsList = new ArrayList<String>();
	}

	/**
	 * @param code
	 *            code de l'exception
	 * @param args
	 *            tableau des arguments
	 * @param ex
	 *            exception
	 */
	public BusinessException(final String code, final String[] args,
			final Throwable ex) {

		super(ex);
		this.code = code;
		this.argsList = new ArrayList<String>(Arrays.asList(args));
	}

	/**
	 * @param code
	 *            code de l'exception
	 * @param argsList
	 *            liste des arguments
	 * @param ex
	 *            exception
	 */
	public BusinessException(final String code, final List<String> argsList,
			final Throwable ex) {

		super(ex);
		this.code = code;
		this.argsList = argsList;
	}

	/**
	 * Getter of the code
	 *
	 * @return Returns the code.
	 */
	@JsonProperty
	public String getCode() {

		return this.code;
	}

	/**
	 * Getter of the args
	 *
	 * @return Returns the args.
	 */
	@JsonProperty
	public String[] getArgs() {

		return this.argsList.toArray(new String[this.argsList.size()]);
	}

	/**
	 * Getter of the argsList
	 *
	 * @return Returns the argsList.
	 */
	@JsonIgnore
	public List<String> getArgsList() {

		return this.argsList;
	}
    
    /* (non-Javadoc)
     * @see java.lang.Throwable#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()+" [code=").append(this.code).append(", argsList=[").append(this.argsList).append("]").append(", message=").append(this.getMessage());
        return builder.append(", stackTrace=[").toString();

    }
    
}
