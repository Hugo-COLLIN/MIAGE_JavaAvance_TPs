import javafx.application.Application;
import javafx.stage.Stage;
import morpion.Controller;
import morpion.Model;
import morpion.View;

public class Morpion extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        final View view = new View(primaryStage);
        final Model model = new Model();
        new Controller(view, model);

        primaryStage.setTitle("Hello World!");
        primaryStage.show();
    }

}
