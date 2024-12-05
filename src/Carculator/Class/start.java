package Carculator.Class;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class start extends Application{
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
            stage.setResizable(false);
            stage.setTitle("简易计算器");
            stage.getIcons().add(new Image("Carculator/Class/图标.jpg"));
            Pane pane = FXMLLoader.load(getClass().getResource("model.fxml"));
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
    }

}
