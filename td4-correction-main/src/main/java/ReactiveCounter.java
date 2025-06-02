import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ReactiveCounter extends Application implements reactive_counter.View {
    public static void main(String[] args) {
        launch(args);
    }

    private Scene scene;

    private Button btnIncrement;

    private Button btnReset;

    private Label label;

    @Override
    public void start(Stage primaryStage) {
        
        this.label = new Label();
        this.label.setText("0");
        
        this.btnIncrement = new Button();
        this.btnIncrement.setText("+1");
        // this.btnIncrement.setOnAction((ActionEvent event) -> {});

        this.btnReset = new Button();
        this.btnReset.setText("0");
        // this.btnReset.setOnAction((ActionEvent event) -> {});

        final HBox root = new HBox(8);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(this.label);
        root.getChildren().add(this.btnIncrement);
        root.getChildren().add(this.btnReset);
        
        this.scene = new Scene(root, 300, 250);

        // let the magic happen ^^
        new reactive_counter.Controller(this, new reactive_counter.Model());

        primaryStage.setScene(this.scene);
        primaryStage.setTitle("Hello World!");
        primaryStage.show();
    }

    // View public API

    @Override
    public void bind(IntegerProperty prop) {
        this.label.textProperty().bind(prop.asString());
    }

    @Override
    public void onIncrement(Runnable r) {
        this.btnIncrement.setOnAction((ActionEvent event) -> {
            r.run();
        });
    }

    @Override
    public void onReset(Runnable r) {
        this.btnReset.setOnAction((ActionEvent event) -> {
            r.run();
        });
    }

}
