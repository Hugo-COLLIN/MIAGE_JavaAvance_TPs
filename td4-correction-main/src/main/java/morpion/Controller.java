package morpion;

import javafx.application.Platform;
import morpion.Model.ModelListener;
import morpion.Model.Symbol;
import morpion.View.ViewListener;

public class Controller implements ViewListener, ModelListener {
    
    private final Model model;

    private final View view;

    //            ┌────────────┐            
    //     ┌──────► Controleur |───────┐    
    //     │      └───┬─────▲──┘       │    
    //     │          │     │          │    
    // ┌────────┐     │     │      ┌───▼───┐
    // │ Modele ◄─────┘     └──────|  Vue  │
    // └────────┘                  └───────┘
    //

    public Controller(View view, Model model)  {
        this.model = model;
        // écoute les modifications de l'état du jeu
        this.model.setListener(this);

        this.view = view;
        // écoute les actions de l'utilisateur
        this.view.setListener(this);
    }

    // ViewListener

    @Override
    public void restartGame() {
        final Model.Symbol modelSym = this.model.restart();
        this.view.newGame( View.Symbol.valueOf(modelSym.toString()) );
    }

    @Override
    public void play(int x, int y) {
        if(this.model.play(x, y)) {
            // si on a réussi à jouer en (x,y), le symbole
            // obtenu par l'appel à current() ci-dessous
            // ne sera **jamais** null
            this.view.setMark(View.Symbol.valueOf(this.model.current(x, y).toString()), x, y);
        }
    }

    // ModelListener

    @Override
    public void endOfGame(final Symbol winner) {
        // cette méthode est appelée pendant play()
        // on utilise un runLater() pour que cet évènement
        // soit bien traité **après** le retour de la fonction
        // (évite par exemple d'afficher 'fin' alors que
        // le dernier coup n'est pas encore affiché)
        Platform.runLater(() -> {
            if(winner != null) {
                this.view.gameOver(View.Symbol.valueOf(winner.toString()));
            } else {
                this.view.gameOver(null);
            }
        });
    }

}
