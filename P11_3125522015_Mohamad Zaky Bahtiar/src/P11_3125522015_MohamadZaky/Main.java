package P11_3125522015_MohamadZaky;

import P11_3125522015_MohamadZaky.model.*;
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
        URL fxmlUrl = getClass().getResource("/P11_3125522015_MohamadZaky/view/MainView.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("File MainView.fxml tidak ditemukan di dalam package view.");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root, 1360, 820);
        URL cssUrl = getClass().getResource("/P11_3125522015_MohamadZaky/view/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setTitle("Sistem Manajemen Program Makan Bergizi Gratis (MBG) - P11_3125522015_MohamadZaky");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();

        if (captureScreenshots) {
            P11_3125522015_MohamadZaky.controller.MainController ctrl = loader.getController();
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
            P11_3125522015_MohamadZaky.controller.MainController ctrl = loader.getController();
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

    public static void jalankanVerifikasiPboDanCollection() {
        System.out.println("========================================================================");
        System.out.println("  VERIFIKASI PBO & PENGELOLAAN KOLEKSI ARRAYLIST (MODUL PRAKTIKUM 11)   ");
        System.out.println("========================================================================");

        PengelolaMenu pengelola = new PengelolaMenu();

        System.out.println("\n[Skenario 1] Menampilkan koleksi awal saat masih kosong:");
        pengelola.tampilkanMenu();
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 2] Tambah Menu MNU-01 dan MNU-02 (Data Valid):");
        Menu m1 = new Menu("MNU-01", "Paket A: Nasi Ayam Teriyaki & Tumis Sayur", 680.0, 28.5, 82.0, 15000.0);
        Menu m2 = new Menu("MNU-02", "Paket B: Nasi Telur Balado & Sayur Sop", 640.0, 24.0, 79.0, 14000.0);
        pengelola.tambahMenu(m1);
        pengelola.tambahMenu(m2);
        System.out.println("Hasil: Kedua menu berhasil disimpan ke dalam List<Menu>.");
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 3] Cari Menu MNU-02 berdasarkan kode unik:");
        Menu hasilCari = pengelola.cariMenu("MNU-02");
        if (hasilCari != null) {
            System.out.println("Hasil: Ditemukan -> " + hasilCari.getKodeMenu() + " - " + hasilCari.getNamaMenu());
        } else {
            System.out.println("Hasil: Menu tidak ditemukan.");
        }
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 4] Tambah Menu dengan Kode Duplikat (MNU-01):");
        try {
            Menu mDuplikat = new Menu("MNU-01", "Paket A Duplikat Varian", 700.0, 30.0, 80.0, 16000.0);
            pengelola.tambahMenu(mDuplikat);
            System.out.println("Hasil: Berhasil (SALAH - Duplikasi lolos).");
        } catch (IllegalArgumentException e) {
            System.out.println("Hasil: Ditolak (" + e.getMessage() + ")");
        }
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 5] Tambah Menu Objek Null:");
        try {
            pengelola.tambahMenu(null);
            System.out.println("Hasil: Berhasil (SALAH - Objek null lolos).");
        } catch (IllegalArgumentException e) {
            System.out.println("Hasil: Ditolak (" + e.getMessage() + ")");
        }
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 6] Tambah Menu dengan Atribut Tidak Valid (Kode Kosong):");
        try {
            new Menu("   ", "Paket Menu Tanpa Kode", 500.0, 20.0, 70.0, 12000.0);
            System.out.println("Hasil: Berhasil (SALAH - Validasi atribut gagal).");
        } catch (IllegalArgumentException e) {
            System.out.println("Hasil: Ditolak (" + e.getMessage() + ")");
        }
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 7] Hapus Menu MNU-01:");
        boolean statusHapus = pengelola.hapusMenu("MNU-01");
        System.out.println("Hasil: Status penghapusan = " + statusHapus);
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 8] Cari Menu MNU-01 yang telah dihapus:");
        Menu cariTerhapus = pengelola.cariMenu("MNU-01");
        System.out.println("Hasil: " + (cariTerhapus == null ? "null (Menu tidak ditemukan)" : cariTerhapus.getNamaMenu()));
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 9] Hapus Menu yang Tidak Terdaftar (MNU-999):");
        boolean statusHapusNonExist = pengelola.hapusMenu("MNU-999");
        System.out.println("Hasil: Status penghapusan = " + statusHapusNonExist + " (menghasilkan false tanpa crash)");
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n[Skenario 10] Tampilkan data akhir koleksi Menu:");
        pengelola.tampilkanMenu();

        System.out.println("\n[Skenario 11] Uji Enkapsulasi Koleksi (Cegah Mutasi Liar dari Luar):");
        try {
            pengelola.getDaftarMenu().add(new Menu("MNU-99", "Injeksi Liar", 500.0, 20.0, 70.0, 10000.0));
            System.out.println("Hasil: Bocor (SALAH - Collection internal dapat dimodifikasi langsung).");
        } catch (UnsupportedOperationException e) {
            System.out.println("Hasil: Sukses terlindungi (UnsupportedOperationException). Collection bersifat unmodifiable.");
        }
        System.out.println("Jumlah data: " + pengelola.getJumlahMenu());

        System.out.println("\n========================================================================");
        System.out.println("  PENGUJIAN KONSISTENSI STATE OBJEK BAHAN BAKU (MODUL 10 RE-VERIFY)    ");
        System.out.println("========================================================================");
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
        jalankanVerifikasiPboDanCollection();
        for (String arg : args) {
            if ("--cli-test".equalsIgnoreCase(arg)) {
                System.exit(0);
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
