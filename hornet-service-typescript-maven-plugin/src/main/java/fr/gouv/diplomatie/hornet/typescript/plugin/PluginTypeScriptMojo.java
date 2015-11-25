/*
 * Copyright ou © ou Copr. Ministère des Affaires Etrangères (2012)
 * 
 * Ce logiciel est un programme informatique servant de framework J2EE basé sur les normes J2EE (DAO,JDBC,MVC
 * de Struts) et les principes multi-X (multi-couches, multi-canal, multi-formats, multi-langues). Il offre la
 * possibilité de générer facilement des flux XML métier pour consulter ou mettre à jour des sources de
 * données et s'interfacer avec le client riche ACube. Ce logiciel est régi par la licence CeCILL soumise au
 * droit français et respectant les principes de diffusion des logiciels libres. Vous pouvez utiliser,
 * modifier et/ou redistribuer ce programme sous les conditions de la licence CeCILL telle que diffusée par le
 * CEA, le CNRS et l'INRIA sur le site "http://www.cecill.info".
 * 
 * En contrepartie de l'accessibilité au code source et des droits de copie, de modification et de
 * redistribution accordés par cette licence, il n'est offert aux utilisateurs qu'une garantie limitée. Pour
 * les mêmes raisons, seule une responsabilité restreinte pèse sur l'auteur du programme, le titulaire des
 * droits patrimoniaux et les concédants successifs.
 * 
 * A cet égard l'attention de l'utilisateur est attirée sur les risques associés au chargement, à
 * l'utilisation, à la modification et/ou au développement et à la reproduction du logiciel par l'utilisateur
 * étant donné sa spécificité de logiciel libre, qui peut le rendre complexe à manipuler et qui le réserve
 * donc à des développeurs et des professionnels avertis possédant des connaissances informatiques
 * approfondies. Les utilisateurs sont donc invités à charger et tester l'adéquation du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs systèmes et ou de leurs données et,
 * plus généralement, à l'utiliser et l'exploiter dans les mêmes conditions de sécurité.
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez pris connaissance de la licence
 * CeCILL, et que vous en avez accepté les termes.
 */
package fr.gouv.diplomatie.hornet.typescript.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java2typescript.jackson.module.DefinitionGenerator;
import java2typescript.jackson.module.grammar.Module;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * @author Hornet
 * @since 1.0 - 9 févr. 2015
 *
 * @configurator include-project-dependencies
 */
@Mojo(name = "generate-typescript", defaultPhase = LifecyclePhase.PROCESS_CLASSES,
            requiresDependencyResolution = ResolutionScope.RUNTIME)
public class PluginTypeScriptMojo extends AbstractMojo {

    // http://stackoverflow.com/a/16263482
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}")
    private String targetDir;

    @Parameter(defaultValue = "typescript")
    private String typescriptDir;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // Servira de base au nom du module "declare module .."
        final String baseModuleName =
                    Strings.isNullOrEmpty(project.getName()) ? project.getArtifactId() : project.getName();
        final String targetDirTs = targetDir + File.separatorChar + typescriptDir;

        getLog().info("Recherche des classes à traiter pour le module " + baseModuleName);
        getLog().info("Génération des TS dans " + targetDirTs);
        final List<Class<?>> classesATraiter = recupererListeClassesATraiter();

        // Génération TS
        final ObjectMapper mapper = new ObjectMapper();
        final DefinitionGenerator generator = new DefinitionGenerator(mapper);

        for (final Class<?> classe : classesATraiter) {
            final String moduleName = baseModuleName + "-api-" + classe.getSimpleName();
            getLog().info("Génération du module " + moduleName);

            final Module module = genererTypeScript(generator, classe, moduleName);
            ecrireTypeScript(module, targetDirTs, moduleName);
        }

    }

    /**
     * @param module
     * @param targetDirTs
     * @param moduleName
     * @throws MojoExecutionException
     */
    private void ecrireTypeScript(final Module module, final String targetDirTs, final String moduleName)
        throws MojoExecutionException {

        final File fileTs = new File(targetDirTs + File.separatorChar + moduleName.toLowerCase() + ".d.ts");
        try {
            Files.createParentDirs(fileTs);
        } catch (final IOException e) {
            throw new MojoExecutionException("Erreur lors de la création du dossier parent de "
                        + fileTs.getAbsolutePath(), e);
        }

        try {
            final FileWriter writer = new FileWriter(fileTs);
            module.write(writer);
            writer.close();
        } catch (final IOException e) {
            throw new MojoExecutionException("Erreur lors de l'écriture du module " + moduleName
                        + " dans le fichier " + fileTs.getAbsolutePath(), e);
        }
    }

    /**
     * @param generator
     * @param classe
     * @param baseModuleName
     * @return
     * @throws MojoExecutionException
     * @throws JsonMappingException
     */
    private Module genererTypeScript(final DefinitionGenerator generator, final Class<?> classe,
                final String moduleName) throws MojoExecutionException {

        final ArrayList<Class<?>> liste = Lists.newArrayList(classe);
        Module module;
        try {
            module = generator.generateTypeScript(moduleName, liste);
        } catch (final JsonMappingException e) {
            throw new MojoExecutionException("Erreur lors de la génération du typescript pour la classe "
                        + classe.getName(), e);
        }

        return module;
    }

    private final List<Class<?>> recupererListeClassesATraiter() {

        final List<Class<?>> classesATraiter = new ArrayList<Class<?>>();

        try {
            final Set<URL> urls = new HashSet<>();
            final List<String> elements = project.getCompileClasspathElements();

            for (final String element : elements) {
                urls.add(new File(element).toURI().toURL());
            }

            final ClassLoader contextClassLoader =
                        URLClassLoader.newInstance(urls.toArray(new URL[0]), Thread.currentThread()
                                    .getContextClassLoader());

            Thread.currentThread().setContextClassLoader(contextClassLoader);

            final ConfigurationBuilder conf =
                        new ConfigurationBuilder()
                                    .setUrls(ClasspathHelper.forClassLoader(contextClassLoader));

            final Reflections reflections = new Reflections(conf);
            final Set<Class<?>> clazzs = reflections.getTypesAnnotatedWith(Controller.class);
            clazzs.addAll(reflections.getTypesAnnotatedWith(RestController.class));

            for (final Class<?> clazz : clazzs) {
                final Set<Method> methodes =
                            ReflectionUtils.getAllMethods(clazz,
                                ReflectionUtils.withAnnotation(RequestMapping.class));
                getLog().debug("Classe Spring MVC : " + clazz.getName());
                if (methodes.size() > 0) {
                    classesATraiter.add(clazz);
                }
                for (final Method methode : methodes) {
                    getLog().debug("\tMéthode Spring MVC :" + methode.getName());
                }
            }

        } catch (final DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return classesATraiter;
    }
}
