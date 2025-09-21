package com.example.view;

import com.example.Main;
import com.example.controller.LoginController;
import com.example.controller.SignUpController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SignUpView extends Application {
    private Stage c2w_primaryStage;
    private TextField c2w_emailField;
    private PasswordField c2w_passwordField;
    private Main c2w_mainApp;
    private Stage c2w_stage;

    public SignUpView() {
        // No-arg constructor required
    }

    @Override
    public void start(Stage c2w_primaryStage) {
        this.c2w_primaryStage = c2w_primaryStage;
        openAuthWindow();
    }

    public void openAuthWindow() {
        SignUpView signUpView = new SignUpView(c2w_primaryStage);
        signUpView.show();
    }

    public void openMainAppWindow() {
        c2w_primaryStage = new Stage();
        NewsView newsView = new NewsView(this, c2w_primaryStage);
        newsView.show();
    }

    public SignUpView(Stage c2w_primaryStage) {
        this.c2w_stage = c2w_primaryStage;

        // Input fields
        c2w_emailField = new TextField();
        c2w_emailField.setPromptText("Enter your email");
        c2w_emailField.setFocusTraversable(false);

        c2w_passwordField = new PasswordField();
        c2w_passwordField.setPromptText("Enter your password");
        c2w_passwordField.setFocusTraversable(false);

        // Buttons
        Button c2w_signUpButton = new Button("Sign Up");
        Button c2w_loginButton = new Button("Login");

        String c2w_buttonStyle = "-fx-background-color: #2C2C2C; -fx-text-fill: white; -fx-font-size: 16; "
                + "-fx-background-radius: 24; -fx-padding: 8 32 8 32;";
        c2w_signUpButton.setStyle(c2w_buttonStyle);
        c2w_loginButton.setStyle(c2w_buttonStyle);

        // Hover effects
        c2w_signUpButton.setOnMouseEntered(e -> c2w_signUpButton.setStyle(c2w_buttonStyle + "-fx-background-color: #666666;"));
        c2w_signUpButton.setOnMouseExited(e -> c2w_signUpButton.setStyle(c2w_buttonStyle));
        c2w_loginButton.setOnMouseEntered(e -> c2w_loginButton.setStyle(c2w_buttonStyle + "-fx-background-color: #666666;"));
        c2w_loginButton.setOnMouseExited(e -> c2w_loginButton.setStyle(c2w_buttonStyle));

        // Controllers
        SignUpController c2w_controller = new SignUpController();

        // Sign-up action
        c2w_signUpButton.setOnAction(event -> {
            String c2w_email = c2w_emailField.getText();
            String c2w_password = c2w_passwordField.getText();
            SignUpController.Result c2w_result = c2w_controller.signUp(c2w_email, c2w_password);

            if (!c2w_result.success) {
                showAlert(Alert.AlertType.ERROR, c2w_result.message);
            } else {
                c2w_stage.close();
                this.openMainAppWindow();
            }
        });

        // Login action
        c2w_loginButton.setOnAction(event -> {
            String c2w_email = c2w_emailField.getText();
            String c2w_password = c2w_passwordField.getText();
            LoginController.Result c2w_result = new LoginController().login(c2w_email, c2w_password);

            if (!c2w_result.success) {
                showAlert(Alert.AlertType.ERROR, c2w_result.message);
            } else {
                c2w_stage.close();
                this.openMainAppWindow();
            }
        });

        // Background image (if needed)
        Image c2w_backgroundImage = new Image("/newsBackground.jpg");
        BackgroundImage c2w_background = new BackgroundImage(
                c2w_backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false)
        );

        Pane c2w_pane = new Pane();
        c2w_pane.setBackground(new Background(c2w_background));

        // Center container - retro newspaper theme
        VBox c2w_layout = new VBox(18);
        c2w_layout.setPadding(new Insets(60, 60, 60, 60));
        c2w_layout.setMaxWidth(420);
        c2w_layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #f5f0e1, #e8e0c4);" +  // light cream fade
                "-fx-background-radius: 20;" +
                "-fx-border-color: #e6d19c;" +  // old yellow border
                "-fx-border-width: 3;" +
                "-fx-border-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0.3, 0, 6);" // subtle shadow
        );

        // Title label
        Label c2w_titleLabel = new Label("📰 Daily News");
        c2w_titleLabel.setStyle(
                "-fx-font-size: 30;" +
                "-fx-font-family: 'Georgia';" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #3a2f0b;" + // dark vintage brown
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0.1, 0, 2);"
        );

        // Labels
        Label c2w_emailLabel = new Label("Email:");
        c2w_emailLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #4b3f2a; -fx-font-family: 'Times New Roman';");

        Label c2w_passwordLabel = new Label("Password:");
        c2w_passwordLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #4b3f2a; -fx-font-family: 'Times New Roman';");

        // Input fields styling
        c2w_emailField.setStyle(
                "-fx-font-size: 16;" +
                "-fx-background-color: #fbf6e8;" +
                "-fx-text-fill: #2b1f0d;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #e6d19c;" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1.5;" +
                "-fx-padding: 8;"
        );

        c2w_passwordField.setStyle(
                "-fx-font-size: 16;" +
                "-fx-background-color: #fbf6e8;" +
                "-fx-text-fill: #2b1f0d;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #e6d19c;" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1.5;" +
                "-fx-padding: 8;"
        );

        // Add all components
        c2w_layout.getChildren().addAll(
                c2w_titleLabel,
                c2w_emailLabel, c2w_emailField,
                c2w_passwordLabel, c2w_passwordField,
                c2w_loginButton, c2w_signUpButton
        );

        c2w_layout.setAlignment(Pos.CENTER);
        c2w_pane.getChildren().add(c2w_layout);

        // Center layout
        c2w_layout.layoutXProperty().bind(c2w_pane.widthProperty().subtract(c2w_layout.widthProperty()).divide(2));
        c2w_layout.layoutYProperty().bind(c2w_pane.heightProperty().subtract(c2w_layout.heightProperty()).divide(2));

        Scene c2w_scene = new Scene(c2w_pane, 545, 800);
        c2w_stage.setScene(c2w_scene);
        c2w_stage.setTitle("Daily Byte");
        c2w_stage.setResizable(false);
    }

    public void show() {
        c2w_stage.show();
    }

    private void showAlert(Alert.AlertType c2w_type, String c2w_message) {
        Alert c2w_alert = new Alert(c2w_type);
        c2w_alert.setTitle(c2w_type == Alert.AlertType.ERROR ? "Error" : "Info");
        c2w_alert.setHeaderText(null);
        c2w_alert.setContentText(c2w_message);
        c2w_alert.showAndWait();
    }
}
