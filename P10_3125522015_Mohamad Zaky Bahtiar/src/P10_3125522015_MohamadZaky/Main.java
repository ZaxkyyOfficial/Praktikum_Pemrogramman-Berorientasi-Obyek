package P10_3125522015_MohamadZaky;

import P10_3125522015_MohamadZaky.model.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class Main extends Application {
    private static boolean smokeTestUi = false;
    private static boolean captureScreenshots = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = getClass().getResource("/P10_3125522015_MohamadZaky/view/MainView.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("File MainView.fxml tidak ditemukan di dalam package view.");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root, 1360, 820);
        URL cssUrl = getClass().getResource("/P10_3125522015_MohamadZaky/view/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setTitle("Sistem Manajemen Program Makan Bergizi Gratis (MBG) - P10_3125522015_MohamadZaky");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();

        if (captureScreenshots) {
            P10_3125522015_MohamadZaky.controller.MainController ctrl = loader.getController();
            File outDir = new File("Screenshot");
            if (!outDir.exists()) outDir.mkdirs();

            PauseTransition step1 = new PauseTransition(Duration.millis(800));
            step1.setOnFinished(e1 -> {
                saveNodeScreenshot(scene, "Screenshot/01_dashboard_utama.png");

                ctrl.tampilkanInventaris();
                PauseTransition step2 = new PauseTransition(Duration.millis(800));
                step2.setOnFinished(e2 -> {
                    saveNodeScreenshot(scene, "Screenshot/02_inventaris_validasi_stok.png");

                    ctrl.tampilkanMenuGizi();
                    PauseTransition step3 = new PauseTransition(Duration.millis(800));
                    step3.setOnFinished(e3 -> {
                        saveNodeScreenshot(scene, "Screenshot/03_menu_kalkulasi_gizi.png");

                        ctrl.tampilkanDistribusi();
                        PauseTransition step4 = new PauseTransition(Duration.millis(800));
                        step4.setOnFinished(e4 -> {
                            saveNodeScreenshot(scene, "Screenshot/04_distribusi_pelacakan.png");

                            ctrl.tampilkanSekolah();
                            PauseTransition step5 = new PauseTransition(Duration.millis(800));
                            step5.setOnFinished(e5 -> {
                                saveNodeScreenshot(scene, "Screenshot/05_sekolah_penerima.png");
                                System.out.println("[SCREENSHOT CAPTURE COMPLETE] Seluruh modul berhasil ditangkap ke folder Screenshot/");
                                Platform.exit();
                            });
                            step5.play();
                        });
                        step4.play();
                    });
                    step3.play();
                });
                step2.play();
            });
            step1.play();
            return;
        }

        if (smokeTestUi) {
            P10_3125522015_MohamadZaky.controller.MainController ctrl = loader.getController();
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(ev -> {
                ctrl.tampilkanInventaris();
                ctrl.tampilkanMenuGizi();
                ctrl.tampilkanDistribusi();
                ctrl.tampilkanSekolah();
                ctrl.toggleTema();
                ctrl.toggleTema();
                ctrl.tampilkanDashboard();
                System.out.println("[UI SMOKE TEST] Seluruh modul FXML, style.css, dan navigasi controller berhasil dirender tanpa error.");
                Platform.exit();
            });
            pause.play();
        }
    }

    private static void saveNodeScreenshot(Scene scene, String filePath) {
        try {
            WritableImage fxImage = scene.snapshot(null);
            int width = (int) fxImage.getWidth();
            int height = (int) fxImage.getHeight();
            BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            PixelReader reader = fxImage.getPixelReader();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    bImage.setRGB(x, y, reader.getArgb(x, y));
                }
            }
            File file = new File(filePath);
            ImageIO.write(bImage, "png", file);
            System.out.println("Tangkapan layar tersimpan: " + filePath);
        } catch (Exception ex) {
            System.err.println("Gagal menyimpan screenshot " + filePath + ": " + ex.getMessage());
        }
    }

    // Demonstrasi verifikasi PBO (Modul 3 s.d. Modul 10) dan tabel skenario pengujian Exception Handling.
    public static void jalankanVerifikasiPboDanException() {
        System.out.println("=== VERIFIKASI PRINSIP PBO & EXCEPTION HANDLING (MODUL 10) ===");

        // 1. Inheritance, Abstract Class, Upcasting & Dynamic Binding (Modul 5, 6, 7)
        List<Pengguna> daftarPersonel = List.of(
                new Admin("USR-ADM-01", "Mohamad Zaky Bahtiar", "zaky@bgn.simulasi.id", "Wilayah Sumenep Kota"),
                new StafDapur("USR-DPR-01", "Rina Kartika Sari", "rina@bgn.simulasi.id", "Dapur Pusat Sektor Utara"),
                new Driver("USR-DRV-01", "Hendro Wibowo", "hendro@bgn.simulasi.id", "Rute Kecamatan Kota", "M-8821-VP", "Jl. Trunojoyo KM 3")
        );

        for (Pengguna p : daftarPersonel) {
            System.out.println("Personel: " + p.getNama() + " -> " + p.tampilkanPeran());
            if (p instanceof DapatDilacak terlacak) {
                System.out.println("  Lokasi Terlacak: " + terlacak.getLokasi());
            }
        }

        // 2. Studi Kasus Modul 10: Pengujian berurutan pada objek BahanBaku yang sama (stok awal 5 unit)
        System.out.println("\n--- Uji Konsistensi State Objek BahanBaku (Stok Awal = 5 kg) ---");
        BahanBaku ayamFillet = new BahanBaku(
                "BHN-UJI", "Dada Ayam Fillet Segar", "Protein Hewani",
                5, "kg", 38000, LocalDate.now().plusDays(7)
        );

        String[] daftarInput = {"2", "0", "9", "abc", "-1", "3"};
        for (String input : daftarInput) {
            System.out.println("Input pemakaian stok: \"" + input + "\"");
            try {
                int jumlah = Integer.parseInt(input);
                ayamFillet.kurangiStok(jumlah);
                System.out.println("  Hasil: Pemakaian stok berhasil.");
            } catch (NumberFormatException e) {
                System.out.println("  Hasil: Ditolak karena format angka salah (input harus bilangan bulat).");
            } catch (IllegalArgumentException e) {
                System.out.println("  Hasil: Ditolak (" + e.getMessage() + ")");
            } catch (StokTidakCukupException e) {
                System.out.println("  Hasil: Ditolak (" + e.getMessage() + ")");
            } finally {
                System.out.println("  Stok akhir (finally): " + ayamFillet.getStok() + " " + ayamFillet.getSatuan());
            }
        }
    }

    public static void main(String[] args) {
        jalankanVerifikasiPboDanException();
        for (String arg : args) {
            if ("--cli-test".equalsIgnoreCase(arg)) {
                return;
            }
            if ("--smoke-test-ui".equalsIgnoreCase(arg)) {
                smokeTestUi = true;
            }
            if ("--capture-screens".equalsIgnoreCase(arg)) {
                captureScreenshots = true;
            }
        }
        launch(args);
    }
}

