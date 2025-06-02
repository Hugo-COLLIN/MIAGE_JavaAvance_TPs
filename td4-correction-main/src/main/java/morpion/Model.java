package morpion;


import java.util.Arrays;
import java.util.stream.IntStream;

public class Model {

    /**
     * Stocke les marqueurs posés par
     * les joueurs sous forme linéaire.
     * <pre>
     *[0][1][2]
     *[3][4][5]
     *[7][8][9]
     * </pre>
     * La valeur {@code null} indique une case vide.
     */
    private final Symbol[] symbols = new Symbol[9];

    private Symbol nextSymbol = null;

    private ModelListener listener;
    
    public void setListener(ModelListener listener) {
        this.listener = listener;
    }

    public boolean isStarted() {
        return (nextSymbol != null);
    }

    public Symbol current(int x, int y) {
        assert (x >=0 && x < 3) : "coordonnée x incorrecte";
        assert (y >=0 && y < 3) : "coordonnée y incorrecte";

        final int index = y * 3 + x;
        return symbols[index];
    }

    /**
     * Démarre une nouvelle partie.
     * @return Le symbole qui commence à jouer.
     */
    public Symbol restart() {
        IntStream.range(0, symbols.length).forEach(i -> symbols[i] = null);
        return (nextSymbol = (Math.random() < 0.5 ? Symbol.O : Symbol.X));
    }

    /**
     * Tente de placer un marqueur sur le plateau de jeu.
     * @param x La position en abscisse ([0, 3[).
     * @param y La position en ordonnée ([0, 3[).
     * @return {@code true} si le marqueur est posé, false si erreur.
     * @throws AssertionError en cas d'erreur (si les assertions sont activées !)
     */
    public boolean play(int x, int y) {
        assert isStarted() : "aucune partie démarrée"; 
        assert (x >=0 && x < 3) : "coordonnée x incorrecte";
        assert (y >=0 && y < 3) : "coordonnée y incorrecte";

        final int index = y * 3 + x;
        if(symbols[index] != null) {
            return false;
        }

        // place le marqueur
        symbols[index] = nextSymbol;

        // a t'on un gagnant ?
        final Symbol winner = winner();

        if(winner != null) {
            listener.endOfGame(winner);
        } else if(isOver()) {
            listener.endOfGame(null);
        } else {
            // le jeu continue ^^ on calcule le symbole suivant
            nextSymbol = Symbol.O.equals(nextSymbol) ? Symbol.X : Symbol.O;
        }

        return true;
    }

    public boolean isOver() {
         return winner() != null  || Arrays.stream(symbols).filter(s -> s != null).count() >= 9;
    }

    //           _         _ 
    //   ___ ___|_|_ _ ___| |_ ___
    //  | . |  _| | | | .'|  _| -_|
    //  |  _|_| |_|\_/|__,|_| |___|
    //  |_|
    //

    /**
     * Teste si il y a un gagnant.
     * @return Le symbole qui a gagné ou {@code null} sinon.
     */
    private Symbol winner() {
        assert isStarted() : "aucune partie démarrée"; 
        // horizontal
        if(symbols[0] == symbols[1] && symbols[1] == symbols[2])
            return symbols[0];
        if(symbols[3] == symbols[4] && symbols[4] == symbols[5])
            return symbols[3];
        if(symbols[6] == symbols[7] && symbols[7] == symbols[8])
            return symbols[6];
        // vertical
        if(symbols[0] == symbols[3] && symbols[3] == symbols[6])
            return symbols[0];
        if(symbols[1] == symbols[4] && symbols[4] == symbols[7])
            return symbols[1];
        if(symbols[2] == symbols[5] && symbols[5] == symbols[8])
            return symbols[2];
        // diagonal
        if(symbols[0] == symbols[4] && symbols[4] == symbols[8])
            return symbols[0];
        if(symbols[6] == symbols[4] && symbols[4] == symbols[2])
            return symbols[6];
        return null;
    }

    //   _
    //  |_|___ ___ ___ ___
    //  | |   |   | -_|  _|
    //  |_|_|_|_|_|___|_|  
    //

    public static enum Symbol {
        X, O
    }

    /**
     * Permet de se mettre à l'écoute du modèle
     * pour pouvoir réagir aux modifications.
     */
    public static interface ModelListener {
        
        /**
         * Peut être appelé pendant l'exécution de
         * {@link Model#play(int, int)} pour indiquer
         * que le coup joué a mis fin à la partie.
         */
        void endOfGame(Symbol winner);

    }

}
