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
package hornet.framework.webservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import hornet.framework.exception.BusinessException;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Projet hornetserver.
 *
 * @author EffiTIC
 * @date 18 mai 2011
 */
public class UriListProxySelectorTest {

    /**
     * Test connect failed.
     */
    @Test
    public void testConnectFailed() {

        final ProxySelector proxySelector = new UriListProxySelector("localhost", 0, "HTTP", null);

        try {
            proxySelector.connectFailed(null, null, null);
            fail("connectFailed method should have thrown a BusinessException");
        } catch (final BusinessException e) {
            assertEquals("PROXY_CONNECT_FAILED", e.getCode());
        }

    }

    /**
     * Test select uri with empty uri list.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testSelectURIWithEmptyUriList() throws Exception {

        final String host = "localhost";
        final int port = 8088;
        final String type = "SOCKS";
        final ProxySelector proxySelector = new UriListProxySelector(host, port, type, null);

        final List<Proxy> proxyList = proxySelector.select(new URI("www.google.fr"));

        assertNotNull(proxyList);
        assertEquals(1, proxyList.size());
        assertSame(Proxy.NO_PROXY, proxyList.get(0));
    }

    /**
     * Test select uri with uri connected through proxy.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testSelectURIWithUriConnectedThroughProxy() throws Exception {

        final String host = "localhost";
        final int port = 8088;
        final String type = "SOCKS";
        final String uriStr = "www.google.fr";

        final ProxySelector proxySelector = new UriListProxySelector(host, port, type, Arrays.asList(uriStr));

        final List<Proxy> proxyList = proxySelector.select(new URI(uriStr));

        assertNotNull(proxyList);
        assertEquals(1, proxyList.size());

        final Proxy proxy = proxyList.get(0);

        assertEquals(Proxy.Type.SOCKS, proxy.type());
        assertEquals(host, ((InetSocketAddress) proxy.address()).getHostName());
        assertEquals(port, ((InetSocketAddress) proxy.address()).getPort());
    }

}
