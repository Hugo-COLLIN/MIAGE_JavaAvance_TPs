import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DirtyCounter extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Scene scene;

    private Button btnIncrement;

    private Button btnReset;

    private Label label;

    private int count = 0;

    @Override
    public void start(Stage primaryStage) {
        
        this.label = new Label();
        this.label.setText("0");
        
        this.btnIncrement = new Button();
        this.btnIncrement.setText("+1");
        this.btnIncrement.setOnAction((ActionEvent event) -> {
            this.label.setText(Integer.toString(++count));
        });

        this.btnReset = new Button();
        this.btnReset.setText("0");
        this.btnReset.setOnAction((ActionEvent event) -> {
            this.label.setText(Integer.toString(count = 0));
        });

        final HBox root = new HBox(8);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(this.label);
        root.getChildren().add(this.btnIncrement);
        root.getChildren().add(this.btnReset);
        
        this.scene = new Scene(root, 300, 250);

        primaryStage.setScene(this.scene);
        primaryStage.setTitle("Hello World!");
        primaryStage.show();
    }

}
