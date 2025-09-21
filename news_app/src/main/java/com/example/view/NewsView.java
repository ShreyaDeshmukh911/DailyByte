package com.example.view;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.controller.ApiController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsView {
    private SignUpView signUpView;
    private static int page = 0;
    private Stage primaryStage;
    private VBox newsContainer;
    private ApiController apiController = new ApiController();

    public NewsView(SignUpView signUpView, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.signUpView = signUpView;
    }

    public void show() {
        showCategorySelection();
    }

     int c2w_index = 0;

    private void showCategorySelection() {

        c2w_index = 0;
        // Create a background image
        ImageView backgroundImage = new ImageView(new Image("/categoryBackground.jpg"));
        backgroundImage.setOpacity(0.9);
        backgroundImage.setFitWidth(545);
        backgroundImage.setFitHeight(800);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(40));
        grid.setStyle("-fx-background-color: transparent;");

        String[] categories = { "general", "business", "entertainment", "health", "science", "sports" };
        String[] gradients = {
            "-fx-background-color: linear-gradient(to right, rgba(50,50,50,0.8), rgba(68,68,68,0.8));",
            "-fx-background-color: linear-gradient(to right, rgba(51,51,51,0.8), rgba(85,85,85,0.8));",
            "-fx-background-color: linear-gradient(to right, rgba(68,68,68,0.8), rgba(102,102,102,0.8));",
            "-fx-background-color: linear-gradient(to right, rgba(51,51,51,0.8), rgba(85,85,85,0.8));",
            "-fx-background-color: linear-gradient(to right, rgba(68,68,68,0.8), rgba(102,102,102,0.8));",
            "-fx-background-color: linear-gradient(to right, rgba(51,51,51,0.8), rgba(85,85,85,0.8));"
        };

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                if (c2w_index < categories.length) {
                    String category = categories[c2w_index];
                    Button c2w_btn = new Button(capitalize(category));
                    c2w_btn.setPrefWidth(190);
                    c2w_btn.setPrefHeight(200);
                    c2w_btn.setStyle(gradients[c2w_index] + " -fx-text-fill: #FFD700; -fx-font-size: 22; -fx-font-weight: bold; -fx-background-radius: 18;");

                    // Hover effect
                    c2w_btn.setOnMouseEntered(e -> c2w_btn.setStyle("-fx-background-color: rgba(102,102,102,0.9); -fx-text-fill: #FFD700; -fx-font-size: 22; -fx-font-weight: bold; -fx-background-radius: 18;"));
                    c2w_btn.setOnMouseExited(e -> c2w_btn.setStyle(gradients[c2w_index] + " -fx-text-fill: #FFD700; -fx-font-size: 22; -fx-font-weight: bold; -fx-background-radius: 18;"));

                    c2w_btn.setOnAction(e -> handleCategorySelection(category));

                    ScaleTransition c2w_stEnlarge = new ScaleTransition(Duration.millis(180), c2w_btn);
                    c2w_stEnlarge.setToX(1.08);
                    c2w_stEnlarge.setToY(1.08);
                    ScaleTransition c2w_stNormal = new ScaleTransition(Duration.millis(180), c2w_btn);
                    c2w_stNormal.setToX(1.0);
                    c2w_stNormal.setToY(1.0);
                    c2w_btn.setOnMouseEntered(e -> c2w_stEnlarge.playFromStart());
                    c2w_btn.setOnMouseExited(e -> c2w_stNormal.playFromStart());

                    grid.add(c2w_btn, col, row);
                    c2w_index++;
                }
            }
        }

        Button c2w_logoutButton = createStyledButton("Logout");
        c2w_logoutButton.setPrefWidth(120);
        c2w_logoutButton.setPrefHeight(50);
        c2w_logoutButton.setOnAction(e -> signUpView.openAuthWindow());
        grid.add(c2w_logoutButton, 0, 3, 2, 1);

        // Create a stack pane to layer the background image and grid
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImage, grid);
        
        Scene c2w_scene = new Scene(root, 545, 800);
        primaryStage.setScene(c2w_scene);
        primaryStage.setTitle("DailyBytes News");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void handleCategorySelection(String c2w_category) {
        if (c2w_category.equals("general")) {
            showGeneralNews();
        } else {
            showCategorySources(c2w_category);
        }
    }

    private void showGeneralNews() {
        primaryStage.setTitle("DailyBytes News");
        newsContainer = new VBox(10);
        newsContainer.setPadding(new Insets(10));
        ScrollPane c2w_scrollPane = new ScrollPane(newsContainer);
        c2w_scrollPane.setFitToWidth(true);
        c2w_scrollPane.setStyle("-fx-background: #1A1A1A;");

        Button c2w_loadNewsButton = createStyledButton("Load News");
        Button c2w_backButton = createStyledButton("Back");
        c2w_backButton.setOnAction(e -> showCategorySelection());

        ProgressIndicator c2w_loader = new ProgressIndicator();
        c2w_loader.setVisible(false);

        c2w_loadNewsButton.setOnAction(e -> {
            c2w_loader.setVisible(true);
            loadNewsAsync(c2w_loader);
        });

        HBox c2w_bottomBox = new HBox(16, c2w_loadNewsButton, c2w_loader, c2w_backButton);
        c2w_bottomBox.setPadding(new Insets(20, 0, 40, 0));
        c2w_bottomBox.setAlignment(Pos.CENTER);

        BorderPane c2w_root = new BorderPane();
        c2w_root.setCenter(c2w_scrollPane);
        c2w_root.setBottom(c2w_bottomBox);
        c2w_root.setStyle("-fx-background-color: #1A1A1A;");

        Scene c2w_scene = new Scene(c2w_root, 545, 800);
        primaryStage.setScene(c2w_scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        c2w_loader.setVisible(true);
        loadNewsAsync(c2w_loader);
    }

    private void showCategorySources(String c2w_category) {
        VBox c2w_sourcesContainer = new VBox(10);
        c2w_sourcesContainer.setPadding(new Insets(10));
        c2w_sourcesContainer.setAlignment(Pos.TOP_CENTER);

        ProgressIndicator c2w_loader = new ProgressIndicator();
        c2w_loader.setVisible(true);

        Button c2w_backButton = createStyledButton("Back");
        c2w_backButton.setOnAction(e -> showCategorySelection());

        ScrollPane c2w_scrollPane = new ScrollPane(c2w_sourcesContainer);
        c2w_scrollPane.setFitToWidth(true);
        c2w_scrollPane.setStyle("-fx-background: #1A1A1A;");

        HBox c2w_bottomBox = new HBox(16, c2w_loader, c2w_backButton);
        c2w_bottomBox.setPadding(new Insets(20, 0, 40, 0));
        c2w_bottomBox.setAlignment(Pos.CENTER);

        BorderPane c2w_root = new BorderPane();
        c2w_root.setCenter(c2w_scrollPane);
        c2w_root.setBottom(c2w_bottomBox);
        c2w_root.setStyle("-fx-background-color: #1A1A1A;");

        Scene scene = new Scene(c2w_root, 545, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        Task<Void> c2w_task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONArray c2w_sources = apiController.getNews(c2w_category);
                Platform.runLater(() -> {
                    c2w_loader.setVisible(false);
                    c2w_sourcesContainer.getChildren().clear();
                    if (c2w_sources.length() == 0) {
                        Label noSourcesLabel = new Label("No sources found for this category.");
                        noSourcesLabel.setStyle("-fx-text-fill: #E0E0E0; -fx-font-size: 16;");
                        c2w_sourcesContainer.getChildren().add(noSourcesLabel);
                    }
                    for (int i = 0; i < c2w_sources.length(); i++) {
                        JSONObject source = c2w_sources.getJSONObject(i);
                        String name = source.getString("name");
                        String description = source.optString("description", "");
                        String urlSource = source.getString("url");
                        HBox c2w_card = createSourceCard(name, description, urlSource);
                        c2w_sourcesContainer.getChildren().add(c2w_card);
                    }
                });
                return null;
            }
        };
        new Thread(c2w_task).start();
    }

    private void loadNewsAsync(ProgressIndicator c2w_loader) {
        Task<Void> c2w_task = new Task<Void>() {
            @Override
            protected Void call() {
                loadNews();
                return null;
            }
        };
        c2w_task.setOnSucceeded(ev -> c2w_loader.setVisible(false));
        c2w_task.setOnFailed(ev -> c2w_loader.setVisible(false));
        new Thread(c2w_task).start();
    }

    private void loadNews() {
        try {
            page++;
            String c2w_articlesUrl = "https://newsapi.org/v2/everything?q=tech&" +
                    "page=" + page + "&apiKey=3d609fde7de445beb00b54dbb26c80e6&pageSize=10";
            URL c2w_url = new URL(c2w_articlesUrl);
            HttpURLConnection conn = (HttpURLConnection) c2w_url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader c2w_in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String c2w_inputLine;
            StringBuilder response = new StringBuilder();
            while ((c2w_inputLine = c2w_in.readLine()) != null) {
                response.append(c2w_inputLine);
            }
            c2w_in.close();
            JSONObject c2w_jsonResponse = new JSONObject(response.toString());
            JSONArray c2w_articles = c2w_jsonResponse.getJSONArray("articles");
            Platform.runLater(() -> {
                newsContainer.getChildren().clear();
                for (int i = 0; i < c2w_articles.length(); i++) {
                    JSONObject article = c2w_articles.getJSONObject(i);
                    String c2w_title = article.getString("title");
                    String c2w_description = article.optString("description", "");
                    String c2w_urlStr = article.getString("url");
                    String c2w_imageUrl = article.optString("urlToImage", "");
                    HBox c2w_card = createNewsCard(c2w_title, c2w_description, c2w_urlStr, c2w_imageUrl);
                    newsContainer.getChildren().add(c2w_card);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> showAlert("Error", "Could not load news articles. Please try again later."));
        }
    }

    private HBox createNewsCard(String c2w_title, String c2w_description, String c2w_url, String c2w_imageUrl) {
        HBox c2w_card = new HBox();
        c2w_card.setPadding(new Insets(18));
        c2w_card.setBackground(new Background(new BackgroundFill(Color.web("#2C2C2C"), new CornerRadii(16), Insets.EMPTY)));
        c2w_card.setStyle("-fx-border-color: #444444; -fx-border-width: 1.5; -fx-border-radius: 16; -fx-background-radius: 16;");
        c2w_card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.5)));
        c2w_card.setCursor(javafx.scene.Cursor.HAND);
        c2w_card.setAlignment(Pos.CENTER);
        VBox c2w_contentBox = new VBox(12);
        c2w_contentBox.setAlignment(Pos.CENTER);

        ImageView c2w_imageView = null;
        if (c2w_imageUrl != null && !c2w_imageUrl.isEmpty()) {
            try {
                Image img = new Image(c2w_imageUrl, 320, 180, true, true);
                c2w_imageView = new ImageView(img);
                c2w_imageView.setFitWidth(320);
                c2w_imageView.setFitHeight(180);
                c2w_imageView.setSmooth(true);
                c2w_imageView.setPreserveRatio(true);
            } catch (Exception ignored) {}
        }

        Text c2w_titleText = new Text(c2w_title);
        c2w_titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-fill: #FFFFFF;");
        c2w_titleText.setWrappingWidth(400);
        c2w_titleText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Text c2w_descText = new Text(c2w_description);
        c2w_descText.setWrappingWidth(400);
        c2w_descText.setStyle("-fx-font-size: 14; -fx-fill: #CCCCCC;");
        c2w_descText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        if (c2w_imageView != null) c2w_contentBox.getChildren().add(c2w_imageView);
        c2w_contentBox.getChildren().addAll(c2w_titleText, c2w_descText);
        c2w_card.getChildren().add(c2w_contentBox);
        c2w_card.setMinWidth(450);
        c2w_card.setMaxWidth(500);

        c2w_card.setOnMouseEntered(e -> {
            c2w_card.setEffect(new DropShadow(25, Color.rgb(255, 255, 255, 0.2)));
            c2w_card.setScaleX(1.03);
            c2w_card.setScaleY(1.03);
            c2w_card.setStyle("-fx-border-color: #666666; -fx-border-width: 1.5; -fx-border-radius: 16; -fx-background-radius: 16;");
        });

        c2w_card.setOnMouseExited(e -> {
            c2w_card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.5)));
            c2w_card.setScaleX(1.0);
            c2w_card.setScaleY(1.0);
            c2w_card.setStyle("-fx-border-color: #444444; -fx-border-width: 1.5; -fx-border-radius: 16; -fx-background-radius: 16;");
        });

        c2w_card.setOnMouseClicked(e -> openWebView(c2w_url));
        return c2w_card;
    }

    private HBox createSourceCard(String c2w_name, String c2w_description, String c2w_url) {
        HBox c2w_card = new HBox();
        c2w_card.setPadding(new Insets(18));
        c2w_card.setBackground(new Background(new BackgroundFill(Color.web("#2C2C2C"), new CornerRadii(16), Insets.EMPTY)));
        c2w_card.setStyle("-fx-border-color: #444444; -fx-border-width: 1.5; -fx-border-radius: 16; -fx-background-radius: 16;");
        c2w_card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.5)));
        c2w_card.setCursor(javafx.scene.Cursor.HAND);
        c2w_card.setAlignment(Pos.CENTER_LEFT);

        VBox c2w_textBox = new VBox(8);
        Text c2w_nameText = new Text(c2w_name);
        c2w_nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-fill: #FFFFFF;");
        Text c2w_descText = new Text(c2w_description);
        c2w_descText.setWrappingWidth(400);
        c2w_descText.setStyle("-fx-font-size: 14; -fx-fill: #CCCCCC;");
        c2w_textBox.getChildren().addAll(c2w_nameText, c2w_descText);
        c2w_card.getChildren().add(c2w_textBox);
        c2w_card.setMinWidth(450);
        c2w_card.setMaxWidth(500);

        c2w_card.setOnMouseEntered(e -> {
            c2w_card.setEffect(new DropShadow(25, Color.rgb(255, 255, 255, 0.2)));
            c2w_card.setScaleX(1.03);
            c2w_card.setScaleY(1.03);
            c2w_card.setStyle("-fx-border-color: #666666; -fx-border-width: 1.5; -fx-border-radius: 16; -fx-background-radius: 16;");
        });

        c2w_card.setOnMouseExited(e -> {
            c2w_card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.5)));
            c2w_card.setScaleX(1.0);
            c2w_card.setScaleY(1.0);
            c2w_card.setStyle("-fx-border-color: #444444; -fx-border-width: 1.5; -fx-border-radius: 16; -fx-background-radius: 16;");
        });

        c2w_card.setOnMouseClicked(e -> openWebView(c2w_url));
        return c2w_card;
    }

    private void openWebView(String c2w_url) {
        Stage c2w_webStage = new Stage();
        WebView c2w_webView = new WebView();
        c2w_webView.getEngine().load(c2w_url);
        Scene c2w_scene = new Scene(c2w_webView, 900, 700);
        c2w_webStage.setScene(c2w_scene);
        c2w_webStage.setTitle("News Source");
        c2w_webStage.show();
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #2C2C2C; -fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-background-radius: 24; -fx-padding: 8 32 8 32; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 2);");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #444444; -fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-background-radius: 24; -fx-padding: 8 32 8 32; -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.2), 15, 0, 0, 3);"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #2C2C2C; -fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-background-radius: 24; -fx-padding: 8 32 8 32; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 2);"));
        return btn;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}