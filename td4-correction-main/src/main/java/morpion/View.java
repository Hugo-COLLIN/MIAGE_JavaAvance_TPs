package morpion;
import java.util.stream.IntStream;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class View {

    private ViewListener listener;

    // on conserve les références aux contrôles
    // qui seront manipulés par le controlleur
    
    /**
     * Les 9 cases.
     */
    private final Button[] cases = new Button[9];

    /**
     * Affiche des messages à l'utilisateur.
     */
    private Label message;

    //   _ _ ___                 _ 
    //  | |_|  _|___ ___ _ _ ___| |___
    //  | | |  _| -_|  _| | |  _| | -_|
    //  |_|_|_| |___|___|_  |___|_|___|
    //                  |___|

    public View(Stage stage) {

        final VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        this.message = new Label("Ready to go !");
        this.message.setMinSize(200, 50);
        this.message.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        vbox.getChildren().add(this.message);
    
        // ---

        final GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 10, 0, 0));
        grid.setHgap(5);
        grid.setVgap(5);

        for (int i = 0 ; i < 3 ; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/3.0);
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }

        for(int y=0; y<3; y++) {
            for(int x=0; x<3; x++) {
                final int col = x, row = y;

                final Button element = new Button(" ");
                element.setMinSize(100.0f, 70.0f);
                element.setAlignment(Pos.CENTER);
                element.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                element.setFont(Font.font("Arial", FontWeight.BOLD, 30));
                element.setOnAction((ActionEvent event) -> {
                    if(this.listener != null) {
                        this.listener.play(col, row);
                    }
                });
                grid.add(element, x, y); 
                this.cases[y * 3 + x] = element;
            }
        }
       
        vbox.getChildren().add(grid);
 
        // ---
        
        final Button btnNewGame = new Button();
        btnNewGame.setText("Nouvelle partie");
        btnNewGame.setOnAction((ActionEvent event) -> {
            if(this.listener != null) {
                this.listener.restartGame();
            }
        });
        
        final HBox actions = new HBox();
        actions.getChildren().add(btnNewGame);
        vbox.getChildren().add(actions);
    
        stage.setScene(new Scene(vbox, 330, 360));
        
        reset();
    }

    //  _____ _____ _____
    // |  _  |  _  |     |
    // |     |   __|-   -|
    // |__|__|__|  |_____|
    //

    public void setListener(ViewListener listener) {
        this.listener = listener;
    }

    // /!\ les méthodes ci-dessous DOIVENT être
    // appelées dans le thread graphique

    public void message(String str) {
        this.message.setText(str);
    }
 
    /**
     * Démarre une nouvelle partie.
     */
    public void newGame(Symbol firstPlayerSymbol) {
        reset();
        message("Next is " + firstPlayerSymbol.toString());
        IntStream.range(0, this.cases.length).forEach(i -> {
            this.cases[i].setDisable(false);
        });
    } 

    public void setMark(Symbol s, int x, int y) {
        this.cases[y * 3 + x].setText(s.toString());
    }
    
    public void gameOver(Symbol winner) {
        IntStream.range(0, this.cases.length).forEach(i -> this.cases[i].setDisable(true));
        if(winner != null) {
            message(winner.toString() + " won \\0/");
        } else {
            message("Game over");
        }
    }

    //           _         _ 
    //   ___ ___|_|_ _ ___| |_ ___
    //  | . |  _| | | | .'|  _| -_|
    //  |  _|_| |_|\_/|__,|_| |___|
    //  |_|
    //

    /**
     * (Ré)Initialise l'interface graphique.
     */
    private void reset() {
        message("Ready to go !");
        // les cases sont inactives tant que la partie
        // n'est pas commencée partie n'est commencez
        IntStream.range(0, this.cases.length).forEach(i -> {
            this.cases[i].setDisable(true);
            this.cases[i].setText(" ");
        });
    }

    //   _
    //  |_|___ ___ ___ ___
    //  | |   |   | -_|  _|
    //  |_|_|_|_|_|___|_|  
    //

    /**
     * Permet de se mettre à l'écoute de la vue
     * pour pouvoir réagir aux actions effectuée
     * dans l'ihm (clic de boutons, ...).
     */
    public interface ViewListener {
        /**
         * Un joueur a demandé un redémarrage du jeu.
         */
        void restartGame();

        /**
         * Le joueur actif souhaite placer son marqueur en (x,y).
         * @param x La coordonnée x ([0, 2]).
         * @param y La coordonnée y ([0, 2]).
         */
        void play(int x, int y);
    }

    public static enum Symbol {
        X, O;
    }

}
