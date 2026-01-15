package com.upb.agripos;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.view.PosView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println("Hello World, I am Agan Chois-240202893");

        PosController controller = new PosController();
        PosView view = new PosView(controller);

        Scene scene = new Scene(view.getRoot(), 900, 650);
        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Agri-POS Week 14");
        stage.setScene(scene);
        stage.show();
    }
}
