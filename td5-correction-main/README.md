# TD n°5 - REST + JSON

Amis dresseurs, aujourd'hui nous allons ENCORE capturer des Pokemons !

## REST

La première étape consiste à récupérer la liste. Nous allons cette fois utiliser
[une API REST](https://tyradex.vercel.app/api/v1/pokemon) qui va renvoyer la liste
complète en format JSON.

🚧 Créez un nouveau projet maven et ajoutez en dépendance la librairie 
[GSON](https://mvnrepository.com/artifact/com.google.code.gson/gson).
Il va nous falloir également un client HTTP. On pourrait utiliser
une instance de `HttpURLConnection` mais on va utiliser une librairie un peu
plus « robuste » [fournie par Apache](https://hc.apache.org/httpcomponents-client-5.4.x/index.html).
Ajoutez la dépendance sur `org.apache.httpcomponents.client5:httpclient5:5.4`.

🚧 En vous inspirant de [l'exemple de code fourni par le site officiel](https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/test/java/org/apache/hc/client5/http/examples/ClientResponseProcessing.java),
écrivez la méthode `List<Pokemon> fetch()` de la classe `PokemonRestFetcher`.

Le JSON renvoyé par le service est une liste d'objet, chaque objet
contenant les infos détaillée sur un pokémon. On va s'intéresser
uniquement à son nom français, son type principal (le premier de la liste)
et ses points de vie (hp).

Voici un exemple d'entrée dans la liste (ignorez le pokemon d'identifiant 0).

```json
{
  "pokedex_id": 1,
  "generation": 1,
  "category": "Pokémon Graine",
  "name": {
    "fr": "Bulbizarre",
    "en": "Bulbasaur",
    "jp": "フシギダネ"
  },
  "sprites": { ... },
  "types": [
    {
      "name": "Plante",
      "image": "https://raw.githubusercontent.com/Yarkis01/TyraDex/images/types/plante.png"
    },
    {
      "name": "Poison",
      "image": "https://raw.githubusercontent.com/Yarkis01/TyraDex/images/types/poison.png"
    }
  ],
  "talents": [ ... ],
  "stats": {
    "hp": 45,
    "atk": 49,
    "def": 49,
    "spe_atk": 65,
    "spe_def": 65,
    "vit": 45
  },
  "resistances": [ ... ],
  "evolution": { ... },
  "height": "0,7 m",
  "weight": "6,9 kg",
  "egg_groups": [
    "Monstrueux",
    "Végétal"
  ],
  "sexe": {
    "male": 87.5,
    "female": 12.5
  },
  "catch_rate": 45,
  "level_100": 1059862,
  "formes": null
}
```

Vous devez afficher la liste des pokémons depuis votre `main()`.

🚧 Exécutez votre programme, vous devriez avoir un avertissement SLF4J.
```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```
🤔 Comment résoudre le « problème » ?

> Ce n'est pas vraiment un problème, mais plutôt un avertissement
> qu'aucun log ne sera généré (_nop logger_).
> SL4J est la librairie de log utilisée par la librairie Apache.
> C'est une abstraction de différentes implémentations (logback, log4j, ...).
> Il suffit d'ajouter une dépendance sur `ch.qos.logback:logback-classic:1.2.3`
> qui est l'implémentation par défaut.

🚧 Exécutez à nouveau votre programme. Vous risquez d'avoir des erreurs lors de l'extraction des points de vie.
Par défaut, GSON déserialize tous les nombres en `float`, mais on peut lui dire de faire autrement.
Il suffit pour cela d'utiliser le _builder_ au lieu d'un simple _new_.

Modifiez l'instanciation et reconfigurez la gestion des nombres :

```java
final Gson gson = new GsonBuilder()
  .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
  .create();
```

🚧 Vérifier le bon déroulement de votre application.

## Design(s) pattern(s)

Quand le webservice est indisponible, on souhaite pouvoir utiliser un fichier local,
ce qui permettra un fonctionnement dégradé de l'application (le seul risque est 
que la liste ne soit pas parfaitement à jour).

🚧 Écrivez une seconde classe `PokemonFileFetcher` qui réplique le comportement
de `PokemonRestFetcher`, mais en lisant ses données depuis le fichier.

On veut pouvoir facilement passer d'un système à l'autre (fichier ou webservice).
Pour celà, nous allons utiliser un _[builder](https://refactoring.guru/design-patterns/builder)_
pour instancier le bon service.

🚧 Corrigez le squelette ci-dessous et modifiez votre _main_ :

```java
public class PokemonFetcherFactory {

  public static PokemonFetcher build(Provider provider) {
    throw new UnsupportedOperationException("provider inconnu");
  }

  public static enum Provider {
      API, FILE
  }

}
```

**Challenge**: n'utilisez pas de _if_ ou de _switch_ pour implémenter la fonction _build_.

Pour initialiser le fichier local, on va utiliser le résultat de l'appel au webservice
que l'on va stocker dans un fichier. Mais on veut éviter de « casser » quelque chose
dans notre code initial. On va donc utiliser le pattern « [decorator](https://refactoring.guru/design-patterns/decorator) »
pour encapsuler la classe qui interroge le webservice et exploiter son résultat.

🚧 Complétez la classe suivante :

```java
public class CacheDecorator {

    private final PokemonFetcher implementation;
    
    public CacheDecorator(PokemonFetcher implementation) {
        this.implementation = implementation;
    }

    @Override
    public List<Pokemon> fetch() {
        throw new UnsupportedOperationException("not implemented");
    }
}
```

🤔 Que faut-il faire de plus pour pouvoir l'utiliser correctement ?
(indice: « Liskov’s Substitution Principle »)

> Définir une interface commune, implémentée par les services ET par le décorateur
> et utiliser cette interface dans le code client pour masquer la mécanique interne.
> ```java
> public interface PokemonFetcher {
>   List<Pokemon> fetch();
> }
> ``

🚧 Testez votre décorateur.

## CLI 🏆

On veut pouvoir rechercher un pokemon par son nom (ou une partie de son nom).
On va pour cela écrire une application en ligne de commande (CLI) qui prend
en paramètre le nom à recherche.

Pour éviter de partir de zéro, et respecter [les bonnes pratiques d'une appli CLI](https://clig.dev/),
on va utiliser [Picoli](https://picocli.info). Cherchez la dernière version de picocli sur
le repo central maven et ajoutez la dépendance qui va bien dans votre `pom.xml`.

> ```xml
> <dependency>
>   <groupId>info.picocli</groupId>
>   <artifactId>picocli</artifactId>
>   <version>4.7.6</version>
> </dependency>
> ```

Notre CLI n'a qu'une seule commande, la recherche par nom, mais on veut pouvoir
spécifier la source des données. Utilisez le squelette ci-dessous 👇 pour l'implémenter.

```java
@Command(name = "find", description = "Search pokemons by (partial) name")
public class CatchEmAll implements Runnable {
    
    @Parameters(index = "0", description = "Search filter")
    private String filter;

    // remarquez qu'on peut utiliser directement des enum !
    @Option(names = {"-p", "--provider"}, description = "API (default) or FILE")
    private PokemonFetcherFactory.Provider provider = PokemonFetcherFactory.Provider.API;

    @Override
    public void run() {
        // TODO implémenter cette méthode.
    }

    public static void main(String... args) {
        System.exit( new CommandLine(new CatchEmAll()).execute(args) );
    }
}
```

🤔 Lancez votre application sans argument, que se passe-t'il ?.

> PicoCli génère automatiquement un mode d'emploi ^^
> ```bash
> $ java CatchEmAll
> Missing required parameter: '<filter>'
> Usage: find [-p=<provider>] <filter>
> Search pokemons by (partial) name
>       <filter>   Search filter
>    -p, --provider=<provider>
>                 API (default) or FILE
> ```

🚧 Vérifiez maintenant le comportement avec les différents paramètres possibles.

> ```bash
> $ java CatchEmAll --provider=API chu
> Search results for « chu » among 800 pokemons : 
> * Pikachu (45)  -  Électrik
> * Raichu (60)  -  Électrik
> * Pichu (20)  -  Électrik
> * Chuchmur (64)  -  Normal
> * Bibichut (42)  -  Psy
> ```

🚧 Testez un provider inconnu.

> ```bash
> $ java CatchEmAll --provider=XOXO chu 
> Invalid value for option '--provider': expected one of [API, FILE] (case-sensitive) but was 'XOXO'
> ```

## Bonus 🏆

On veut voir la répartition des pokémeons par type. On va pour celà
[générer un graphique](https://quickchart.io/) permettant de la visualiser.

Appelez le service de génération de graphique avec les bonnes données
permettant de visualiser le nombre de pokémons de chaque type.
La configuration du graphe étant en JSON, vous pouvez créer les beans
« qui vont bien » et les sérialiser avec GSON pour vous assurer
de la validité syntaxique des données.
