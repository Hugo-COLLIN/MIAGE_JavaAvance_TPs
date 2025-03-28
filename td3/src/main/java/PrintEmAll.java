import pokemon.PokemonRegistry;

public class PrintEmAll {
    public static void main(String[] args) {
        PokemonRegistry pr = new PokemonRegistry("https://www.data.gouv.fr/fr/datasets/r/04df345d-03dd-45f3-aed2-bf3dd55201a2");
        //Afficher les 100 premiers
        pr.fetch().stream().limit(100).forEach(System.out::println);
    }
}
