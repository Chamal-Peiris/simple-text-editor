import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        AnchorPane root = FXMLLoader.load(this.getClass().getResource("/view/TextEdiorForm.fxml"));

        Scene mainScene = new Scene(root);
        stage=primaryStage;
        stage.setResizable(false);
        stage.setScene(mainScene);
        stage.setTitle("Untitled.txt");
        stage.centerOnScreen();
        stage.show();
    }
    public void setStageTitle(String newTitle) {
        AppInitializer.this.setStageTitle(newTitle);
    }
    public static Stage getStage() {
        return stage;
    }
}
