package P15_3125522015_MohamadZaky;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Entry point peluncuran antarmuka grafis (JavaFX Desktop GUI).
 * Menggunakan arsitektur launcher terpisah (GuiApp) untuk memastikan
 * inisialisasi JavaFX runtime berjalan mulus baik pada modular maupun non-modular execution.
 */
public class AppLauncher {

    public static class GuiApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            URL fxmlUrl = getClass().getResource("/P15_3125522015_MohamadZaky/view/MainView.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getResource("view/MainView.fxml");
            }
            if (fxmlUrl == null) {
                fxmlUrl = AppLauncher.class.getClassLoader().getResource("P15_3125522015_MohamadZaky/view/MainView.fxml");
            }
            if (fxmlUrl == null) {
                throw new IllegalStateException("File MainView.fxml tidak ditemukan di classpath.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root, 1360, 820);
            URL cssUrl = getClass().getResource("/P15_3125522015_MohamadZaky/view/style.css");
            if (cssUrl == null) {
                cssUrl = getClass().getResource("view/style.css");
            }
            if (cssUrl == null) {
                cssUrl = AppLauncher.class.getClassLoader().getResource("P15_3125522015_MohamadZaky/view/style.css");
            }
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setTitle("Sistem Manajemen Program Makan Bergizi Gratis (MBG) - P15_3125522015_MohamadZaky");
            primaryStage.setMinWidth(1100);
            primaryStage.setMinHeight(700);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        Application.launch(GuiApp.class, args);
    }
}
