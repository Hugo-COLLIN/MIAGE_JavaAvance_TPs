package pokemon.domain;

public class PokemonBuilder {
    
    private Pokemon pokemon = null;

    public PokemonBuilder() {
        this.pokemon = new Pokemon();
    }

    public PokemonBuilder setNom(String nom) {
        this.pokemon.setNom(nom);
        //this.pokemon.setType(nom);
        return this;
    }
    
    public PokemonBuilder setPv(int pv) {
        this.pokemon.setPv(pv);
        return this;
    }
    
    public PokemonBuilder setType(String type) {
        this.pokemon.setType(type);
        //this.pokemon.setNom(type);
        return this;
    }

    public Pokemon build() {
        return this.pokemon;
    }

    public static PokemonBuilder newPokemon() {
        return new PokemonBuilder();
    }

}
