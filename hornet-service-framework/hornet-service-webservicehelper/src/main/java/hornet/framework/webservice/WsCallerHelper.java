/**
 * Copyright ou © ou Copr. Ministère de l'Europe et des Affaires étrangères (2017)
 * <p/>
 * pole-architecture.dga-dsi-psi@diplomatie.gouv.fr
 * <p/>
 * Ce logiciel est un programme informatique servant à faciliter la création
 * d'applications Web conformément aux référentiels généraux français : RGI, RGS et RGAA
 * <p/>
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 * <p/>
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 * <p/>
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
 * <p/>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 * <p/>
 * <p/>
 * Copyright or © or Copr. Ministry for Europe and Foreign Affairs (2017)
 * <p/>
 * pole-architecture.dga-dsi-psi@diplomatie.gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to facilitate creation of
 * web application in accordance with french general repositories : RGI, RGS and RGAA.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 */
package hornet.framework.webservice;

import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Classe d'appel du webservice.
 * 
 * @author EffiTIC
 * @date 19 mai 2011
 */
public class WsCallerHelper implements ApplicationContextAware {

    /** The proxy selector. */
    private final transient ProxySelector proxySelector;

    /** The application context. */
    private transient ApplicationContext applicationContext;

    /**
     * Instantiates a new ws caller helper.
     */
    public WsCallerHelper() {

        this(null);
    }

    /**
     * Constructeur.
     * 
     * @param proxySelector
     *            the proxy selector
     */
    public WsCallerHelper(
                final ProxySelector proxySelector) {

        this.proxySelector = proxySelector;
    }

    /**
     * Renvoie une instance du service web.
     * 
     * @param <T>
     *            the generic type
     * @param webServiceClass
     *            La classe du service web.
     * @param webServiceBeanName
     *            Le nom du service web.
     * @return Le service web.
     */
    @SuppressWarnings("unchecked")
    public <T>T getWebService(final Class<T> webServiceClass, final String webServiceBeanName) {

        final Object webService = this.applicationContext.getBean(webServiceBeanName);

        return (T) webService;
    }

    /**
     * Renvoie une instance du service web.
     * 
     * @param <T>
     *            the generic type
     * @param webServiceClass
     *            La classe du service web.
     * @param webServiceBeanName
     *            Le nom du service web.
     * @param customContextMapBeanName
     *            Le nom
     * @return the web service
     */
    public <T>T getWebService(final Class<T> webServiceClass, final String webServiceBeanName,
                final String customContextMapBeanName) {

        final T webService = this.getWebService(webServiceClass, webServiceBeanName);
        this.configureWebService(webService, customContextMapBeanName);
        return webService;
    }

    /**
     * Gets the web service.
     * 
     * @param <T>
     *            the generic type
     * @param webServiceClass
     *            the web service class
     * @param webServiceBeanName
     *            the web service bean name
     * @param customContextMap
     *            the custom context map
     * @return the web service
     */
    public <T>T getWebService(final Class<T> webServiceClass, final String webServiceBeanName,
                final Map<String, Object> customContextMap) {

        final T webService = this.getWebService(webServiceClass, webServiceBeanName);
        this.configureWebService(webService, customContextMap);
        return webService;
    }

    /**
     * Configure web service.
     * 
     * @param webService
     *            the web service
     * @param customContextMap
     *            the custom context map
     */
    public void configureWebService(final Object webService, final Map<String, Object> customContextMap) {

        this.configureProxySelector();
        this.configureHandlerChain((BindingProvider) webService);
        this.configureRequestContext((BindingProvider) webService, customContextMap);
    }

    /**
     * Configure web service.
     * 
     * @param webService
     *            the web service
     * @param customContextMapBeanName
     *            the custom context map bean name
     */
    @SuppressWarnings("unchecked")
    public void configureWebService(final Object webService, final String customContextMapBeanName) {

        final Map<String, Object> customContextMap =
                    (Map<String, Object>) this.applicationContext.getBean(customContextMapBeanName);

        this.configureWebService(webService, customContextMap);
    }

    /**
     * Configure proxy selector.
     */
    private void configureProxySelector() {

        if (this.proxySelector != null) {
            ProxySelector.setDefault(this.proxySelector);
        }
    }

    /**
     * Configure handler chain.
     * 
     * @param bindingProvider
     *            the binding provider
     */
    private void configureHandlerChain(final BindingProvider bindingProvider) {

        final List<Handler> myHandler = new ArrayList<Handler>();
        myHandler.add(new SOAPLoggingHandler());
        bindingProvider.getBinding().setHandlerChain(myHandler);
    }

    /**
     * Configure request context.
     * 
     * @param bindingProvider
     *            the binding provider
     * @param customContextMap
     *            the custom context map
     */
    private void configureRequestContext(final BindingProvider bindingProvider,
                final Map<String, Object> customContextMap) {

        bindingProvider.getRequestContext().putAll(customContextMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

}
