package com.upb.agripos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.upb.agripos.view.LoginView;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getView(), 400, 300);

        stage.setTitle("AgriPOS - Login");
        stage.setScene(scene);
        stage.show(); // WAJIB
    }

    public static void main(String[] args) {
        launch(args);
    }
}
