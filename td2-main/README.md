# TD n°2 - Threads / Concurrence

L'objectif du TD est de manipuler les _threads_ (création, attente, arrêt, ...) et
d'appréhender les problèmes de synchronisation qui peuvent survenir lors de leur utilisation.

## Threads - Ordre

**🚧 Dans une méthode `main`, instanciez et démarrez 2 threads.**
Le premier doit écrire "hello " sur la sortie standard (`System.out.print`), le second doit écrire "world".
Vous devrez construire les threads de deux manières différentes.
Exécutez plusieurs fois votre programme, le résultat est-il consistent ?

💡 Un thread peut être créé à partir d'un `Runnable`, ou par héritage de `java.lang.Thread`.

**🚧 Dans une méthode `main`, créez n thread, chacun affichant une lettre
de la chaîne "hello world :-\)" (n étant la longueur de la chaîne).**
Démarrez les threads dans l'ordre. Exécutez plusieurs fois votre programme.

🤔 Le résultat est-il consistent ? (spoiler: non ^^) Pourquoi il l'était dans
le premier exercice et qu'il ne l'est plus maintenant ?

Si besoin, vous pouvez rajouter une pause avant l'appel à `sysout` 
pour augmenter la variabilité des résultats.

```java
try { Thread.sleep(200); } catch(InterruptedException e) {}
```

💡 La méthode `split("")` découpe la chaîne en un tableau de caractères.

🤔 Comment faire pour avoir un affichage correct ?

## Threads - Exclusion mutuelle (aka mutex)

Pour les exercices suivants, nous allons partir de la classe `Compte` suivante 👇.

```java
public static class Compte {
    private int solde;
    public Compte(int i) { solde = i; }
    public void deposer(int montant) { solde = solde + montant; }
    public void retirer(int montant) { solde = solde - montant; }
    public int consulter() { return solde; }
}
```

🤔 Le code ci-dessous 👇 affiche-t'il le bon résultat ? (spoiler: non ^^) Pourquoi ?

```java
final Compte compte = new Compte(42);

System.out.println("solde début : " + compte.consulter());
IntStream.range(0, 42).mapToObj(i -> new Thread(() -> {
    // ... diverses opérations sur le compte ...
})).forEach(t -> t.start());
System.out.println("solde fin : " + compte.consulter());
```

🤔 Que peut-on utiliser pour mieux gérer ces threads et attendre la fin de leur exécution ?

**🚧 Instanciez un compte avec un solde à 100. Créez 10000 threads qui vont simuler des transactions
bancaires sur le compte. Les _threads_ pairs vont incrémenter le solde de 30, les _threads_ impairs
vont décrémenter le solde de 30.**

🤔 Quel sera le solde final théorique ? réel ? Pouquoi ? Comment résoudre le soucis ?

**Bonus** : Pour appréhender la différence de performance entre `newThreadPerTaskExecutor` et
`newCachedThreadPool`, comparez le temps d'exécution sur 10000 _threads_.

## Threads - Wait/Notify

Dans l'exercice précédent, les opérations sur le solde se font quelque soit sa valeur.
Utilisez désormais la classe `LivretA` comme base de travail.

```java
public static class LivretA implements Compte {
    public LivretA(int solde) {
        super(solde);
    }
    public synchronized void deposer(int montant) {
        if(solde > 1000) {
            throw new IllegalArgumentException("solde trop haut");
        }
        super.deposer(montant);
    }
    public synchronized void retirer(int montant) {
        if(solde < montant) {
            throw new IllegalArgumentException("solde trop bas");
        }
        super.retirer(montant);
    }
}
```

🤔 Plutôt que de jeter une exception, comment faire pour qu'une opération (ajout/retrait)
_mette en pause_ le thread appelant tant que le solde est invalide ? Comment le _notifier_
quand le solde sera à nouveau correct pour réaliser l'opération ?

💡 Attendre en anglais se dit `wait` ^^

**🚧 Ajoutez à la classe `LivretA` le code permettant de gérer un scénario _wait_/_notify_.**
Instanciez un livret avec un solde à 20, et démarrez trois threads :
* les deux premiers retirent 30 immédiatement
* le dernier attend 1 seconde pour ajouter 40
Vérifiez que le programme se termine bien.**

**🚧 Remplacez `notifyAll()` par `notify()`, que se passe t'il ?**

> Les deux threads de retrait son en attente du verrou, mais notify n'en réveille qu'un seul.
> Le second restera en attente jusqu'à ce qu'on vienne le réveiller (ici jamais, _deadlock_ !).

## Threads - Structures de données

La classe `Messages` ci-dessous permet de stocker des messages de type String
et deux méthode `put`et `get` servent à les produire et les consommer.

```java
public class Messages {
    private List<String> messages = new ArrayList<String>(10);
    public void put(String mesg) { this.messages.add(mesg); }
    public String get() { return this.messages.remove(0); }
}
```
🤔 Quel est le problème de cette classe en environnement concurrentiel ?
Comment rendre cette classe _thread safe_ sans utiliser _synchronized_ ?

💡 Choisir une meilleure structure de données peut-être ?

> La structure `ArrayList` n'est pas _thread safe_, i.e. elle ne peut pas
> être manipulée de manière _safe_ par plusieurs threads en même temps.
> Dans notre cas, on pourrait par exemple utiliser `LinkedBlockingQueue`.
> La nouvelle exécution n'affiche plus d'exceptions ^^.

## Threads - Semaphore

Les sémaphores sont souvent utilisés pour implémenter le _pattern_ producteur/consommateur :
le producteur produit une donnée que le consommateur ... consomme.

Imaginez [le passe](http://blog.ac-versailles.fr/ulislhtg/index.php/post/23/06/2015/A-quoi-sert-un-passe-en-cuisine)
d'un restaurant : les cuisiniers « produisent » les plats que les serveurs
vont « consommer ». Quand il n'y a plus de place disponible pour poser les assiettes,
les cuisiniers sont en attente. Et si il n'y a pas de plat disponbile,
ce sont les serveurs qui sont en attente.

On veut implémenter une _sliding window_, une structure de données utilisée
notammement dans le protocole réseau TCP/IP pour stocker et réguler le nombre
de paquets de données qui transitent sur le réseau.

On dispose d'un buffer d'une taille fixe et de deux pointeurs,
un en écriture pour remplir le buffer et l'autre en lecture pour le vider.

Exemple d'implémentation naïve :

```java
    private char[] buffer = new char[...];

    private int writeIndex = 0;
    private int readIndex = 0;

    public void add(char c) throws InterruptedException {
        this.buffer[writeIndex++] = c;
    }

    public char remove() throws InterruptedException {
        return this.buffer[readIndex++];
    }

```
🤔 Quels sont les problèmes de cette première implémentation ?

Pour gérer la synchronisation, on va utiliser deux sémaphores :
* un pour limiter la production des données, initialisé à la taille du buffer, qui sera décrémenté à chaque dépôt.
* un pour limiter la consommation des données, initialisé à zéro et qui sera incrémenté à chaque retrait d'une donnée.

**🚧 Modifiez l'implémentation pour qu'elle fonctionne correctement en environnement concurrentiel.**

Si votre implémentation est correcte, le `main` ci-dessous 👇 doit afficher « hello world :-\) ».

```java
public static void main(String[] args) {
    final SlidingWindow slidingWindow = new SlidingWindow(5);

    new Thread(() -> {
        try {
            final StringBuilder buffer = new StringBuilder(32);
            char c = slidingWindow.remove();
            while(c != '\0') {
                buffer.append(c);
                c = slidingWindow.remove();
            }
            System.out.println(buffer);
        } catch(InterruptedException ignore) {}
    }).start();

    new Thread(() -> {
        final char[] characters = "hello world :-)\0".toCharArray();
        final Stream<Character> stream = IntStream.range(0, characters.length).mapToObj(i -> characters[i]);
        stream.forEach(c -> 
            {try { slidingWindow.add(c.charValue()); } catch(InterruptedException e) {}}
        );
    }).start();
}
```