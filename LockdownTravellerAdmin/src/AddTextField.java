import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddTextField extends Application {
    int i = 1;

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        TextField textField[] = new TextField[15];
        Button btn = new Button("Add TextField");
        root.add(btn, 0, 0);
        btn.setOnAction(e -> {
            textField[i] = new TextField();
            root.add(textField[i], 5, i);
            i = i + 1;

        });

        final Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }}

