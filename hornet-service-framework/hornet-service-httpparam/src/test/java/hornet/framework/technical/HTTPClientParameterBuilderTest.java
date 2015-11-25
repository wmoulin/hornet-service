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
package hornet.framework.technical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.junit.Test;

/**
 * Projet hornetserver.
 * 
 * @author EffiTIC - Gael.Nedelec
 * @date 5 mai 2011
 */
@SuppressWarnings("deprecation")
public class HTTPClientParameterBuilderTest {

    /** The Constant CONST_50. */
    private static final int CONST_50 = 50;

    /** The Constant CONST_3000. */
    private static final int CONST_3000 = 3000;

    /** The Constant CONST_60000. */
    private static final int CONST_60000 = 60000;

    /**
     * Teste la méthode <tt>loadHttpParamToHttpClientFromConfig</tt> pour le cas où le paramètrage par défaut
     * est défini.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadHttpParamToHttpClientShouldLoadDefaultValue() {

        final HttpClient httpClient = new DefaultHttpClient();

        HTTPClientParameterBuilder.loadHttpParamToHttpClientFromConfig(httpClient, null);

        final HttpParams httpParams = httpClient.getParams();

        assertEquals(CONST_3000, httpParams.getIntParameter("http.connection.timeout", 0));
        final Map<HttpHost, Integer> maxPerHost =
                    (Map<HttpHost, Integer>) httpParams.getParameter("http.connection-manager.max-per-host");
        assertEquals(1, maxPerHost.size());
        assertTrue(maxPerHost.containsKey(new HttpHost("hostname")));
        assertEquals(Integer.valueOf(CONST_50), maxPerHost.get(new HttpHost("hostname")));
        assertEquals(CONST_50, httpParams.getIntParameter("http.connection-manager.max-total", 0));
        assertEquals(CONST_3000, httpParams.getLongParameter("http.connection-manager.timeout", 0));
        assertEquals(CONST_60000, httpParams.getIntParameter("http.socket.timeout", 0));
        assertEquals(org.apache.http.impl.conn.PoolingHttpClientConnectionManager.class,
            httpParams.getParameter("http.connection-manager.class"));
    }

    /**
     * Teste la méthode <tt>loadHttpParamToHttpClient</tt>.
     */
    @Test
    public void testLoadHttpParamToHttpClientBooleanParameter() {

        final HttpClient httpClient = new DefaultHttpClient();

        final String property = "http.protocol.unambiguous-statusline";
        final Properties properties = new Properties();
        properties.put(property, "true");

        HTTPClientParameterBuilder.loadHttpParamToHttpClient(httpClient, properties);

        assertTrue(httpClient.getParams().getBooleanParameter(property, false));
    }

    /**
     * Test connect.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testConnect() throws Exception {

        final HttpClient httpClient = new DefaultHttpClient();

        HTTPClientParameterBuilder.loadHttpParamToHttpClientFromConfig(httpClient, null);

        final HttpGet method = new HttpGet("http://www.google.com");

        try {
            final HttpHost targetHost = new HttpHost("www.google.com", 80, "http");
            final HttpResponse httpResponse = httpClient.execute(targetHost, method);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            assertEquals(HttpStatus.SC_OK, statusCode);

            final InputStream responseBody = httpResponse.getEntity().getContent();

            assertNotNull(responseBody);

        } catch (final Exception e) {
            fail("Fatal transport error: " + e.getMessage());
        }
    }

    /**
     * Test load http param to http client from config.
     */
    @Test
    public void testLoadHttpParamToHttpClientFromConfig() {

        final HttpClient httpClient = new DefaultHttpClient();

        HTTPClientParameterBuilder.loadHttpParamToHttpClientFromConfig(httpClient, "http-parameters");

        assertEquals("mozilla", httpClient.getParams().getParameter("http.useragent"));
    }

    /**
     * Test connection manager class.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testConnectionManagerClass() throws Exception {

        final Properties properties = new Properties();
        properties.put("http.connection-manager.class",
            "org.apache.http.impl.conn.PoolingHttpClientConnectionManager");
        final HttpClient httpClient = new DefaultHttpClient();
        HTTPClientParameterBuilder.loadHttpParamToHttpClient(httpClient, properties);
        assertEquals(BasicClientConnectionManager.class, httpClient.getConnectionManager().getClass());
    }

    /**
     * Test connection manager class default value.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testConnectionManagerClassDefaultValue() throws Exception {

        final HttpClient httpClient = new DefaultHttpClient();
        HTTPClientParameterBuilder.loadHttpParamToHttpClientFromConfig(httpClient, "http-parameters");
        assertEquals(BasicClientConnectionManager.class, httpClient.getConnectionManager().getClass());
    }

    /**
     * Test load http param to http client from config.
     */
    @Test
    public void testParamsBienPrisEnCompte() {

        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");

        final HttpClient httpClient = new DefaultHttpClient();

        HTTPClientParameterBuilder.loadHttpParamToHttpClientFromConfig(httpClient, "http-parameters");

        assertEquals("mozilla", httpClient.getParams().getParameter("http.useragent"));

        final HttpGet method = new HttpGet("http://www.google.com");

        try {
            final HttpHost targetHost = new HttpHost("www.google.com", 80, "http");
            final HttpResponse httpResponse = httpClient.execute(targetHost, method);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            assertEquals(HttpStatus.SC_OK, statusCode);

            final InputStream responseBody = httpResponse.getEntity().getContent();

            assertNotNull(responseBody);

        } catch (final Exception e) {
            fail("Fatal transport error: " + e.getMessage());
        }

    }

}
