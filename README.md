# hornet-service

L'artefact maven hornet-service permet de construire les services REST Java d'une application Hornet JS. Ceux-ci assurent l'accès aux données et aux services métier.

Hornet-service est composé de différents modules :

- hornet-service-bom : permet de gérer de façon centralisée les versions des librairies externes utilisées par le framework Hornet et par les applications Hornet (voir la notion maven ["Bill Of Meterials"](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#Importing_Dependencies))
- hornet-service-parent : artefact parent pour les modules applicatifs
- hornet-service-javadoc-resources
- hornet-service-framework-parent : contient les sous-modules composant le framework Hornet pour la partie services REST
- hornet-service-typescript-maven-plugin

## Modules du framework

### hornet-pom-orm

Ajoute les dépendances vers les librairies permettant l'accès aux données persistantes et le "mapping" (Object Relational Mapping (ORM) Data Access) avec les objets métier Java.

### hornet-pom-reports

Ajoute les dépendances nécessaires pour l'exports de données vers différents formats de sorties tel que Excel pou PDF.

### hornet-service-clamav

Fournit un service (`ClamAVCheckService`) permettant de faciliter la réalisation de tests antivirus avec ClamAV.

### hornet-service-clamavsimulateur

Fournit un serveur permettant de simuler le fonctionnement d'un serveur ClamAV. A utiliser pour réaliser des tests.

### hornet-service-core

Couche technique : fournit les différents niveaux d'exceptions, les services d'export vers différents formats de sortie, un service d'envoi de mail, et quelques classes utilitaires.

### hornet-service-web

Couche technique : fournit des éléments basés sur hornet-service-core ainsi qu'un intercepteur d'exceptions pour les controlleurs web.

### hornet-service-httpparam

Fournit une classe utilitaire (`HTTPClientParameterBuilder`) permettant de faciliter le paramétrage d'un client HTTP.

### hornet-service-metrologiefilter

Fournit un aspect Spring (`MetrologieAspect`) permettant de mesurer les temps d'exécution entre différentes couches techniques.

### hornet-service-typemime

Fournit un utilitaire permettant de déterminer le type MIME d'un contenu.

### hornet-service-webservicehelper

Fournit des classes utilitaires utilisables par les services web, notamment pour la gestion de certificats.

## Utilisation

La construction du paquet hornet-service à partir des sources se fait avec maven via la commande suivante réalisée dans le répertoire hornet-service :

```shell
mvn package
```


## Licence

hornet-service est sous [licence cecill 2.1](./LICENCE.md).

Site web : [![http://www.cecill.info](http://www.cecill.info/licences/Licence_CeCILL_V2.1-en.html)](http://www.cecill.info/licences/Licence_CeCILL_V2.1-en.html)
