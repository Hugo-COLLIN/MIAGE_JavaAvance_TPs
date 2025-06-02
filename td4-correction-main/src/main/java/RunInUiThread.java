import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RunInUiThread extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Scene scene;

    private Button btn;

    @Override
    public void start(Stage primaryStage) {
        this.btn = new Button();
        this.btn.setText("Say 'Hello World'");
        // syntaxe "offcielle"
        // this.btn.setOnAction(new EventHandler<ActionEvent>() { }
        this.btn.setOnAction((ActionEvent event) -> {
            asyncedComputation();
        });
        
        final HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(this.btn);

        this.scene = new Scene(root, 300, 250);

        primaryStage.setScene(this.scene);
        primaryStage.setTitle("Hello World!");
        primaryStage.show();
    }

    /*private void syncedComputation() {
        final Cursor oldCursor = this.scene.getCursor();
        this.scene.setCursor(Cursor.WAIT);

        this.btn.setDisable(true);
        final long maxIterations = 2000;
        for (long iterations1 = 0; iterations1 < maxIterations; iterations1 ++) {
            for (long iterations2 = 0; iterations2 < maxIterations; iterations2 ++) {
                System.out.println(iterations1 + "/" + iterations2);
            }
        }
        this.btn.setDisable(false);

        scene.setCursor(oldCursor);
    }*/

    private void asyncedComputation() {
        final Cursor oldCursor = this.scene.getCursor();
        this.scene.setCursor(Cursor.WAIT);
        this.btn.setDisable(true);

        final Task<Void> task = this.offloadTask();
        task.setOnSucceeded((WorkerStateEvent e) -> {
            this.btn.setDisable(false);
            scene.setCursor(oldCursor);
        });
        
    }

    private Task<Void> offloadTask() {
        final Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                final long maxIterations = 2000;
                for (long iterations1 = 0; iterations1 < maxIterations; iterations1 ++) {
                    for (long iterations2 = 0; iterations2 < maxIterations; iterations2 ++) {
                        System.out.println(iterations1 + "/" + iterations2);
                    }
                }
                return null;
            }
        };

        final Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        return task;
    }
    
}
