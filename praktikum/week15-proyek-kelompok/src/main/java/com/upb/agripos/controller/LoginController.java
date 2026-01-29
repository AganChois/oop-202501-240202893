package com.upb.agripos.controller;

import com.upb.agripos.service.AuthService;
import com.upb.agripos.view.AdminView;
import com.upb.agripos.view.KasirView;

import javafx.stage.Stage;

public class LoginController {

    private final Stage stage;
    private final AuthService authService = new AuthService();
    private Runnable onLoginFailure = () -> {};

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void setOnLoginFailure(Runnable callback) {
        this.onLoginFailure = callback;
    }

    public String login(String username, String password, String role) {

        String result = authService.login(username, password, role);

        if ("SUCCESS".equals(result)) {
            if ("KASIR".equals(role)) {
                KasirView kasirView = new KasirView(stage, username);
                kasirView.show();
            } else if ("ADMIN".equals(role)) {
                AdminView adminView = new AdminView(stage, username);
                adminView.show();
            }
            return null;
        } else {
            return result;
        }
    }

    public Stage getStage() {
        return stage;
    }
}
