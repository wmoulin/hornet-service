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
package hornet.framework.technical;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * La classe <tt>HttpClientParameterBuilder</tt> propose des méthodes utilitaires effectuant la conversion des
 * informations d'un fichier properties contenant le paramètrage d'un objet de type <tt>HttpClient</tt>.
 */
public final class HTTPClientParameterBuilder {

    /** Constante de chaine pour les logs */
    private static final String IS_FOR_LOG = " : ";

    /** Constante de chaine pour les logs */
    private static final String SEPARATOR_FOR_LOG = ", ";

    /** L'objet en charge de la journalisation des messsages. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPClientParameterBuilder.class);

    // Les paramètres.

    /** The Constant HTTP_SOCKET_TIMEOUT. */
    private static final String HTTP_SOCKET_TIMEOUT = "http.socket.timeout";

    /** The Constant HTTP_CONNECTION_TIMEOUT. */
    private static final String HTTP_CONNECTION_TIMEOUT = "http.connection.timeout";

    /** The Constant HTTP_CONNECTION_MANAGER_MAX_TOTAL. */
    private static final String HTTP_CONNECTION_MANAGER_MAX_TOTAL = "http.connection-manager.max-total";

    /** The Constant HTTP_CONNECTION_MANAGER_TIMEOUT. */
    private static final String HTTP_CONNECTION_MANAGER_TIMEOUT = "http.connection-manager.timeout";

    /** The Constant HTTP_CONNECTION_MANAGER_MAX_PER_HOST. */
    private static final String HTTP_CONNECTION_MANAGER_MAX_PER_HOST = "http.connection-manager.max-per-host";

    /** The Constant HTTP_CONNECTION_MANAGER_CLASS. */
    private static final String HTTP_CONNECTION_MANAGER_CLASS = "http.connection-manager.class";

    /**
     * Les paramètres non gérés par cette classe.
     */
    private static final List<String> PARAMS_TYPE_NON_GENERIQUE = Arrays.asList("http.protocol.version",
        "http.method.retry-handler", "http.dateparser.patterns", "http.default-headers");

    /**
     * Les paramètres de type String.
     */
    private static final List<String> PARAMS_TYPE_STRING = Arrays.asList("http.useragent",
        "http.protocol.credential-charset", "http.protocol.element-charset", "http.protocol.content-charset",
        "http.protocol.cookie-policy", "http.method.multipart.boundary");

    /**
     * Les paramètres de type Boolean.
     */
    private static final List<String> PARAMS_TYPE_BOOLEAN = Arrays.asList(
        "http.protocol.unambiguous-statusline", "http.protocol.single-cookie-header",
        "http.protocol.strict-transfer-encoding", "http.protocol.reject-head-body",
        "http.protocol.expect-continue", "http.protocol.warn-extra-input", "http.tcp.nodelay",
        "http.connection.stalecheck", "http.authentication.preemptive",
        "http.protocol.reject-relative-redirect", "http.protocol.allow-circular-redirects");

    /**
     * Les paramètres de type Integer.
     */
    private static final List<String> PARAMS_TYPE_INTEGER = Arrays.asList("http.protocol.head-body-timeout",
        "http.protocol.status-line-garbage-limit", HTTP_SOCKET_TIMEOUT, "http.socket.sendbuffer",
        HTTP_CONNECTION_TIMEOUT, "http.socket.linger", "http.socket.receivebuffer",
        "http.method.response.buffer.warnlimit", "http.protocol.max-redirects",
        HTTP_CONNECTION_MANAGER_MAX_TOTAL);

    /**
     * Les paramètres de type Long.
     */
    private static final List<String> PARAMS_TYPE_LONG = Arrays.asList(HTTP_CONNECTION_MANAGER_TIMEOUT);

    /**
     * La map qui établit la correspondance entre un paramètre et l'objet en charge de sa conversion.
     */
    private static final Map<String, ParameterConverter> MAP_PARAMETER_CONVERTER =
                new HashMap<String, ParameterConverter>();

    /**
     * La map qui établit la correspondance entre un paramètre et l'objet qui permet de positionner la valeur
     * de ce paramètre dans un champ de l'objet HttpClient.
     */
    private static final Map<String, HttpClientFieldSetter> MAP_HTTP_CLIENT_FIELD_SETTER =
                new HashMap<String, HttpClientFieldSetter>();

    /**
     * Valeurs par défaut communes à toutes les API si pas de paramétrage particulier.
     */
    private static final Properties DEFAULT_PROPERTIES = new Properties();

    /**
     * <p>
     * Enumération des types possibles d'un paramètre.
     * </p>
     * <p>
     * Les valeurs possibles sont :
     * <ul>
     * <li><tt>NON_GENERIQUE</tt> : le paramètre ne peut pas être géré par cette classe ;</li>
     * <li><tt>SPECIFIQUE</tt> : le paramètre est d'un autre type que String, Boolean, Integer ou Long ;</li>
     * <li><tt>STRING</tt> : le paramètre est de type String ;</li>
     * <li><tt>BOOLEAN</tt> : le paramètre est de type Boolean ;</li>
     * <li><tt>INTEGER</tt> : le paramètre est de type Integer ;</li>
     * <li><tt>LONG</tt> : le paramètre est de type Long ;</li>
     * <li><tt>INCONNU</tt> : le paramètre n'existe pas (cas d'erreur).</li>
     * </ul>
     * </p>
     */
    private enum ParamType {

        /** The non generique. */
        NON_GENERIQUE,
        /** The specifique. */
        SPECIFIQUE,
        /** The string. */
        STRING,
        /** The boolean. */
        BOOLEAN,
        /** The integer. */
        INTEGER,
        /** The long. */
        LONG,
        /** The inconnu. */
        INCONNU
    }

    /**
     * The Class ParameterConverter.
     */
    private abstract static class ParameterConverter {

        /**
         * Le type du paramètre.
         */
        private transient ParamType type;

        /**
         * Constructeur.
         *
         * @param type
         *            Le type du paramètre.
         */
        public ParameterConverter(
                    final ParamType type) {

            super();
            this.type = type;
        }

        /**
         * Convertit la valeur d'un paramètre.
         *
         * @param key
         *            La clé.
         * @param value
         *            la valeur.
         * @return La valeur après conversion.
         */
        public abstract Object setParameter(String key, Object value);
    }

    /**
     * <p>
     * La classe HttpClientFieldSetter permet de positionner la valeur d'un champ de l'objet HttpClient.
     * </p>
     */
    private abstract static class HttpClientFieldSetter {

        /** Le nom du paramètre. */
        private final transient String name;

        /**
         * Constructeur.
         *
         * @param name
         *            Le nom du paramètre.
         */
        public HttpClientFieldSetter(
                    final String name) {

            this.name = name;
        }

        /**
         * Sets the field value.
         *
         * @param httpClient
         *            the http client
         * @param value
         *            the value
         */
        public abstract void setFieldValue(final HttpClient httpClient, final Object value);

        /**
         * Gets the name.
         *
         * @return Le nom du paramètre.
         */
        public String getName() {

            return this.name;
        }

    }

    /**
     * Initialisation au chargement de la classe : valeurs par défaut et convertion des types.
     */
    static {
        buildDefaultParams();
        buildParamConverterMap();
        buildHttpClientFieldSetterMap();
    }

    /**
     * Constructeur privé.
     */
    private HTTPClientParameterBuilder() {

    }

    /**
     * Initialise les paramètres avec leur valeur par défaut.
     */
    private static void buildDefaultParams() {

        DEFAULT_PROPERTIES.put(HTTP_CONNECTION_TIMEOUT, "3000");
        DEFAULT_PROPERTIES.put(HTTP_CONNECTION_MANAGER_MAX_PER_HOST, "50");
        DEFAULT_PROPERTIES.put(HTTP_CONNECTION_MANAGER_MAX_TOTAL, "50");
        DEFAULT_PROPERTIES.put(HTTP_CONNECTION_MANAGER_TIMEOUT, "3000");
        DEFAULT_PROPERTIES.put(HTTP_SOCKET_TIMEOUT, "60000");
        DEFAULT_PROPERTIES.put(HTTP_CONNECTION_MANAGER_CLASS,
            "org.apache.http.impl.conn.PoolingHttpClientConnectionManager");
    }

    /**
     * Initialise la map qui établit la correspondance entre un paramètre et l'objet en charge de sa
     * conversion.
     */
    private static void buildParamConverterMap() {

        final ParameterConverter toStringParameterConverter = new ParameterConverter(
                    ParamType.STRING) {

            @Override
            public Object setParameter(final String key, final Object value) {

                return paramToString(value);
            }
        };

        for (final String parameter : PARAMS_TYPE_STRING) {
            MAP_PARAMETER_CONVERTER.put(parameter, toStringParameterConverter);
        }

        final ParameterConverter toLongParameterConverter = new ParameterConverter(
                    ParamType.LONG) {

            @Override
            public Object setParameter(final String key, final Object value) {

                return paramToLong(key, value);
            }
        };

        for (final String parameter : PARAMS_TYPE_LONG) {
            MAP_PARAMETER_CONVERTER.put(parameter, toLongParameterConverter);
        }

        final ParameterConverter toIntegerParameterConverter = new ParameterConverter(
                    ParamType.INTEGER) {

            @Override
            public Object setParameter(final String key, final Object value) {

                return paramToInteger(key, value);
            }
        };
        for (final String parameter : PARAMS_TYPE_INTEGER) {
            MAP_PARAMETER_CONVERTER.put(parameter, toIntegerParameterConverter);
        }

        final ParameterConverter toBooleanParameterConverter = new ParameterConverter(
                    ParamType.BOOLEAN) {

            @Override
            public Object setParameter(final String key, final Object value) {

                return paramToBoolean(key, value);
            }
        };

        for (final String parameter : PARAMS_TYPE_BOOLEAN) {
            MAP_PARAMETER_CONVERTER.put(parameter, toBooleanParameterConverter);
        }

        final ParameterConverter nonGenericParameterConverter = new ParameterConverter(
                    ParamType.NON_GENERIQUE) {

            @Override
            public Object setParameter(final String key, final Object value) {

                return null;
            }
        };

        for (final String parameter : PARAMS_TYPE_NON_GENERIQUE) {
            MAP_PARAMETER_CONVERTER.put(parameter, nonGenericParameterConverter);
        }

        buildSpecifiqueConverter();
    }

    /**
     * Initialise les convertisseurs spécifiques pour les paramètres suivants :
     * <ul>
     * <li><tt>CONNECTION_MANAGER_CLASS</tt> : renvoie un objet de type <tt>Class</tt> ;</li>
     * <li><tt>MAX_HOST_CONNECTIONS</tt> : renvoie un objet de type
     * <tt>Map&lt;HostConfiguration, Integer&gt;</tt>.</li>
     * </ul>
     */
    private static void buildSpecifiqueConverter() {

        ParameterConverter p;
        p = new ParameterConverter(
                    ParamType.SPECIFIQUE) {

            @Override
            public Object setParameter(final String key, final Object value) {

                Object ret = null;
                try {
                    ret = Class.forName((String) value);
                } catch (final ClassNotFoundException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
                return ret;
            }
        };
        MAP_PARAMETER_CONVERTER.put(HTTP_CONNECTION_MANAGER_CLASS, p);

        p = new ParameterConverter(
                    ParamType.SPECIFIQUE) {

            @Override
            public Object setParameter(final String key, final Object value) {

                Object ret = null;
                Integer valInt = null;
                try {
                    valInt = Integer.parseInt((String) value);
                } catch (final NumberFormatException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
                final Map<HttpHost, Integer> hosts = new HashMap<HttpHost, Integer>();
                hosts.put(new HttpHost("hostname"), valInt);
                ret = hosts;
                return ret;
            }
        };
        MAP_PARAMETER_CONVERTER.put(HTTP_CONNECTION_MANAGER_MAX_PER_HOST, p);
    }

    /**
     * Initialise la map MAP_HTTP_CLIENT_FIELD_SETTER avec l'objet HttpClientFieldSetter
     */
    private static void buildHttpClientFieldSetterMap() {

        final HttpClientFieldSetter httpClientFieldSetter = new HttpClientFieldSetter(
                    HTTPClientParameterBuilder.HTTP_CONNECTION_MANAGER_CLASS) {

            /**
             * {@inheritDoc}
             */
            @Override
            public void setFieldValue(final HttpClient httpClient, final Object value) {

                HttpClientConnectionManager httpConnectionManager = null;
                try {
                    final Class<?> clazz = (Class<?>) value;
                    httpConnectionManager = (PoolingHttpClientConnectionManager) clazz.newInstance();
                } catch (final InstantiationException e) {
                    LOGGER.warn(String.format(
                        "Impossible d'instancier une instance de la classe du paramètre %1s, classe = %2s",
                        getName(), value));
                } catch (final IllegalAccessException e) {
                    LOGGER.warn(String.format(
                        "Impossible d'instancier une instance de la classe du paramètre %1s, classe = %2s",
                        getName(), value));
                }
                if (httpConnectionManager != null) {
                    HttpClients.custom().setConnectionManager(httpConnectionManager).build();
                }
            }
        };

        MAP_HTTP_CLIENT_FIELD_SETTER.put(HTTP_CONNECTION_MANAGER_CLASS, httpClientFieldSetter);
    }

    /**
     * Convertit la représentation String d'un paramétre en sa version objet. Ex. 1 en version String est
     * transformé en Integer(1)
     *
     * @param key
     *            Le paramètre
     * @param value
     *            La valeur du paramètre avant conversion.
     * @return La valeur du paramètre convertie, <tt>null<tt> si la conversion est impossible.
     */
    private static Object fromStringToParamType(final String key, final Object value) {

        final Object ret;

        if (MAP_PARAMETER_CONVERTER.containsKey(key)) {
            ret = MAP_PARAMETER_CONVERTER.get(key).setParameter(key, value);
        } else {
            LOGGER.warn("Paramétre non trouvé {}{}{}", key, SEPARATOR_FOR_LOG, value);
            ret = value;
        }

        return ret;
    }

    /**
     * Donne le type de paramètre.
     *
     * @param key
     *            Le nom du paramètre.
     * @return Le type du paramètre.
     */
    private static ParamType findParamType(final String key) {

        ParamType typeKey;

        if (MAP_PARAMETER_CONVERTER.containsKey(key)) {
            typeKey = MAP_PARAMETER_CONVERTER.get(key).type;
        } else {
            typeKey = ParamType.INCONNU;
            LOGGER.info("Paramètre {} inexistant", key);
        }

        return typeKey;
    }

    /**
     * Convertit un objet en String.
     *
     * @param value
     *            La valeur à convertir.
     * @return La valeur sous forme de chaîne.
     */
    private static Object paramToString(final Object value) {

        final Object ret;

        if (value == null) {
            ret = "";
        } else if (value instanceof String) {
            ret = value;
        } else {
            ret = value.toString();
        }

        return ret;
    }

    /**
     * Convertit un paramètre en Long.
     *
     * @param key
     *            Le paramètre.
     * @param value
     *            La valeur du paramètre.
     * @return La valeur du paramètre après conversion.
     */
    private static Long paramToLong(final String key, final Object value) {

        Long ret = null;
        if (value instanceof Long) {
            ret = (Long) value;
        } else if (value instanceof String) {
            try {
                ret = Long.valueOf((String) value);
            } catch (final NumberFormatException e) {
                LOGGER.error("Erreur lors de la conversion Long", e);
            }
        } else {
            LOGGER.warn("Valeur Long attendue pour {}{}{}", key, IS_FOR_LOG, value);
        }
        return ret;
    }

    /**
     * Convertit un paramètre en Integer.
     *
     * @param key
     *            Le paramètre.
     * @param value
     *            La valeur.
     * @return La valeur du paramètre après conversion.
     */
    private static Object paramToInteger(final String key, final Object value) {

        Object ret = null;
        if (value instanceof Integer) {
            ret = value;
        } else if (value instanceof String) {
            try {
                ret = Integer.valueOf((String) value);
            } catch (final NumberFormatException e) {
                LOGGER.error("Erreur lors de la conversion Integer", e);
            }
        } else {
            LOGGER.warn("Valeur Integer attendue pour {}{}{}", key, IS_FOR_LOG, value);
        }
        return ret;
    }

    /**
     * Convertit la valeur d'un paramètre en booléen.
     *
     * @param key
     *            Le nom du paramètre.
     * @param value
     *            La valeur du paramètre.
     * @return La valeur du paramètre (Boolean).
     */
    private static Object paramToBoolean(final String key, final Object value) {

        Object ret = null;
        if (value instanceof Boolean) {
            ret = value;
        } else if (value instanceof String) {
            ret = Boolean.valueOf((String) value);
        } else {
            LOGGER.warn("Valeur Boolean attendue pour {}{}{}", key, IS_FOR_LOG, value);
        }
        return ret;
    }

    /**
     * Valorise le paramètrage.
     *
     * @param httpClient
     *            L'objet de type <tt>HttpClient</tt> dans lequel on place le paramètre et sa valeur.
     * @param key
     *            Le paramètre.
     * @param value
     *            La valeur du paramètre.
     */
    private static void setParamValueToHttpClient(final HttpClient httpClient, final String key,
                final String value) {

        final ParamType typeKey = findParamType(key);
        switch (typeKey) {
            case INCONNU:
                LOGGER.warn("Paramètre Inconnu: {}{}{}", key, SEPARATOR_FOR_LOG, value);
                break;
            case NON_GENERIQUE:
                LOGGER.warn("Paramètre non pris en compte : {}{}{}", key, SEPARATOR_FOR_LOG, value);
                break;
            default:
                if (value != null) {
                    final Object convertValue = fromStringToParamType(key, value);
                    if (convertValue == null) {
                        LOGGER.error("Erreur lors de la conversion {}{}{}", key, IS_FOR_LOG, value);
                    } else {
                        httpClient.getParams().setParameter(key, convertValue);
                        LOGGER.info("Paramètre pris en compte : {}{}{}", key, SEPARATOR_FOR_LOG, convertValue);
                        if (MAP_HTTP_CLIENT_FIELD_SETTER.containsKey(key)) {
                            MAP_HTTP_CLIENT_FIELD_SETTER.get(key).setFieldValue(httpClient, convertValue);
                        }
                    }
                }
                break;
        }
    }

    /**
     * Chargement depuis un properties.
     *
     * @param httpClient
     *            le client à paramétrer
     * @param props
     *            les propriétés de paramétrage
     */
    public static void loadHttpParamToHttpClient(final HttpClient httpClient, final Properties props) {

        for (final Map.Entry<Object, Object> entry : props.entrySet()) {
            setParamValueToHttpClient(httpClient, (String) entry.getKey(), (String) entry.getValue());
        }
    }

    /**
     * Renseigne le paramètrage de l'objet <tt>HttpClient</tt> depuis un fichier properties.
     *
     * @param httpClient
     *            L'objet de type <tt>HttpClient</tt>.
     * @param propsFile
     *            Le chemin du fichier properties.
     */
    public static void loadHttpParamToHttpClientFromFile(final HttpClient httpClient, final String propsFile) {

        LOGGER.info("Chargement du paramétrage par défaut ");
        loadHttpParamToHttpClient(httpClient, DEFAULT_PROPERTIES);
        LOGGER.info("Chargement du paramétrage depuis {}", propsFile);
        final Properties props = new Properties();
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(propsFile);
            props.load(fstream);
        } catch (final FileNotFoundException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (final IOException e) {
            LOGGER.warn(e.getMessage(), e);
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (final IOException e) {
                    LOGGER.error("Erreur", e);
                }
            }
        }
        loadHttpParamToHttpClient(httpClient, props);
        LOGGER.info("Chargement du paramétrage fini ");
    }

    /**
     * Renseigne le paramètrage de l'objet <tt>HttpClient</tt> à partir d'un fichier properties accessible
     * depuis le classpath.
     *
     * @param httpClient
     *            L'objet de type <tt>HttpClient</tt>.
     * @param propsFile
     *            Le fichier properties.
     */
    public static void loadHttpParamToHttpClientFromConfig(final HttpClient httpClient, final String propsFile) {

        ResourceBundle p = null;

        if (propsFile != null) {
            try {
                p = ResourceBundle.getBundle(propsFile);
            } catch (final MissingResourceException e) {
                LOGGER.warn("Paramétrage du HTTPClient introuvable ", e);
            }
        }
        LOGGER.info("Chargement du paramétrage par défaut ");
        loadHttpParamToHttpClient(httpClient, DEFAULT_PROPERTIES);

        if (p != null) {
            LOGGER.info("Chargement du paramétrage depuis {}", propsFile);

            final Enumeration<String> keys = p.getKeys();

            while (keys.hasMoreElements()) {
                final String key = keys.nextElement();
                final Object value = p.getObject(key);
                setParamValueToHttpClient(httpClient, key, (String) value);
            }
            LOGGER.info("Chargement du paramétrage depuis {} fini", propsFile);
        }
        LOGGER.info("Fin de chargement du paramétrage");
    }
}
