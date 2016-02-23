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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import hornet.framework.exception.BusinessException;

/**
 * <p>
 * La classe <tt>UriListProxySelector</tt> étend la classe <tt>ProxySelector</tt>.
 * </p>
 * <p>
 * Lors de l'ouverture d'une connexion, une instance de <tt>URIListProxySelector</tt> vérifie si la connexion
 * doit utiliser le proxy.
 * </p>
 * Projet hornetserver.
 * 
 * @date 16 mai 2011
 */
public class UriListProxySelector extends ProxySelector {

    /**
     * Proxy
     */
    private transient Proxy proxy;

    /**
     * Liste des URI utilisant le proxy.
     */
    private final transient List<String> uris;

    /**
     * Constructeur
     * 
     * @param host
     *            Host du proxy
     * @param port
     *            Port du proxy
     * @param type
     *            Yype de proxy (DIRECT, HTTP ou SOCKS)
     * @param uris
     *            Liste des uris utilisant le proxy
     */
    public UriListProxySelector(
                final String host, final int port, final String type, final List<String> uris) {

        super();
        if (uris == null) {
            this.uris = new ArrayList<String>();
        } else {
            this.uris = uris;
        }

        this.proxy = this.creerProxy(host, port, type);
    }

    /**
     * Crée le proxy.
     * 
     * @param host
     *            Host du proxy.
     * @param port
     *            Port du proxy.
     * @param type
     *            Type de proxy (DIRECT, HTTP ou SOCKS).
     * @return Le proxy. Le type du proxy est positionné par défaut à DIRECT s'il ne correspond à aucune
     *         valeur parmi DIRECT, HTTP ou SOCKS.
     */
    private Proxy creerProxy(final String host, final int port, final String type) {

        Proxy.Type proxyType;

        try {
            proxyType = Proxy.Type.valueOf(type);
        } catch (final NullPointerException e) {
            proxyType = Proxy.Type.DIRECT;
        } catch (final IllegalArgumentException e) {
            proxyType = Proxy.Type.DIRECT;
        }

        return new Proxy(proxyType, new InetSocketAddress(host, port));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {

        throw new BusinessException("PROXY_CONNECT_FAILED");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Proxy> select(final URI uri) {

        final List<Proxy> list = new ArrayList<Proxy>();

        if (this.isConnectedThroughProxy(uri)) {
            list.add(this.proxy);
        } else {
            list.add(Proxy.NO_PROXY);
        }

        return list;
    }

    /**
     * Vérifie si l'URI donnée en paramètre doit utiliser le proxy.
     * 
     * @param uri
     *            L'uri.
     * @return <tt>true</tt> si l'URI doit utiliser le proxy, <tt>false</tt> sinon.
     */
    private boolean isConnectedThroughProxy(final URI uri) {

        for (final String uriTest : this.uris) {
            if (uriTest.equals(uri.toString())) {
                return true;
            }
        }
        return false;
    }
}
