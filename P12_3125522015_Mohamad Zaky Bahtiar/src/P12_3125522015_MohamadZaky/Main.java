package P12_3125522015_MohamadZaky;

import P12_3125522015_MohamadZaky.model.Menu;
import P12_3125522015_MohamadZaky.model.PengelolaMenu;
import P12_3125522015_MohamadZaky.utils.MbgFileRepository;
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
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {
    private static boolean smokeTestUi = false;
    private static boolean captureScreenshots = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = getClass().getResource("/P12_3125522015_MohamadZaky/view/MainView.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("File MainView.fxml tidak ditemukan di dalam package view.");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root, 1360, 820);
        URL cssUrl = getClass().getResource("/P12_3125522015_MohamadZaky/view/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setTitle("Sistem Manajemen Program Makan Bergizi Gratis (MBG) - P12_3125522015_MohamadZaky");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();

        if (captureScreenshots) {
            P12_3125522015_MohamadZaky.controller.MainController ctrl = loader.getController();
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
            P12_3125522015_MohamadZaky.controller.MainController ctrl = loader.getController();
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

    /**
     * Rangkaian Pengujian Otomatis Persistensi File I/O (Praktikum Modul 12).
     * Memverifikasi 6+ skenario operasi simpan-muat, validasi delimitasi, fail-fast,
     * serta pembuktian konsistensi state koleksi memori.
     */
    public static void jalankanVerifikasiPersistensiFileP12() {
        System.out.println("========================================================================");
        System.out.println("  VERIFIKASI PERSISTENSI FILE I/O DAN KONSISTENSI STATE (MODUL 12)      ");
        System.out.println("========================================================================");

        Path fileUji = Paths.get("menu_mbg.txt");
        Path fileTidakAda = Paths.get("file_mbg_tidak_ada.txt");
        Path fileFormatRusak = Paths.get("menu_rusak_format.txt");
        Path fileDuplikat = Paths.get("menu_duplikat.txt");
        Path fileKosong = Paths.get("menu_kosong.txt");

        MbgFileRepository fileRepo = new MbgFileRepository();
        PengelolaMenu pengelola = fileRepo.getPengelolaMenu();

        // ---------------------------------------------------------------------
        // Skenario 1: Simpan dua data valid ke file teks
        // ---------------------------------------------------------------------
        System.out.println("\n[Skenario 1] Simpan dua data menu valid ke file teks:");
        pengelola.kosongkan();
        Menu m1 = new Menu("MNU-01", "Paket A: Nasi Ayam Teriyaki & Tumis Sayur", 680.0, 28.5, 82.0, 15000.0);
        Menu m2 = new Menu("MNU-02", "Paket B: Nasi Telur Balado & Sayur Sop", 640.0, 24.0, 79.0, 14000.0);
        pengelola.tambahMenu(m1);
        pengelola.tambahMenu(m2);

        try {
            fileRepo.simpan(fileUji);
            List<String> lines = Files.readAllLines(fileUji, StandardCharsets.UTF_8);
            System.out.println("Hasil: File berhasil disimpan ke '" + fileUji.getFileName() + "'.");
            System.out.println("Jumlah baris dalam file: " + lines.size());
            for (String l : lines) {
                System.out.println("  -> " + l);
            }
        } catch (IOException e) {
            System.out.println("Hasil: Gagal simpan file - " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // Skenario 2: Tutup program (reset memori) dan muat kembali datanya
        // ---------------------------------------------------------------------
        System.out.println("\n[Skenario 2] Tutup program (reset memori) dan muat kembali datanya:");
        PengelolaMenu pengelolaBaru = new PengelolaMenu();
        MbgFileRepository repoBaru = new MbgFileRepository(pengelolaBaru);
        System.out.println("State memori baru sebelum muat: " + pengelolaBaru.getJumlahMenu() + " menu.");

        try {
            repoBaru.muat(fileUji);
            System.out.println("Hasil: Kedua objek terbentuk kembali secara sempurna.");
            pengelolaBaru.tampilkanMenu();
        } catch (IOException e) {
            System.out.println("Operasi file gagal: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // Skenario 3: Muat file yang tidak ada
        // ---------------------------------------------------------------------
        System.out.println("\n[Skenario 3] Muat file yang tidak ada ('" + fileTidakAda.getFileName() + "'):");
        try {
            if (Files.exists(fileTidakAda)) Files.delete(fileTidakAda);
            repoBaru.muat(fileTidakAda);
            System.out.println("Hasil: Lolos tanpa error (SALAH - Harus melempar IOException).");
        } catch (IOException e) {
            System.out.println("Hasil: Operasi file gagal -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
            System.out.println("Status: Tanggapan elegan berhasil tanpa program crash.");
        }

        // ---------------------------------------------------------------------
        // Skenario 4: Muat file dengan format kolom yang sengaja dirusak
        // ---------------------------------------------------------------------
        System.out.println("\n[Skenario 4] Muat file dengan format kolom yang sengaja dirusak (jumlah kolom kurang):");
        try {
            Files.write(fileFormatRusak, List.of("MNU-03|Paket Format Kurang"), StandardCharsets.UTF_8);
            repoBaru.muat(fileFormatRusak);
            System.out.println("Hasil: Berhasil termuat (SALAH - Format rusak harus ditolak).");
        } catch (IOException e) {
            System.out.println("Hasil: Ditolak karena format salah -> " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // Skenario 5: Muat file yang berisi data dengan ID duplikat
        // ---------------------------------------------------------------------
        System.out.println("\n[Skenario 5] Muat file yang berisi data dengan ID duplikat ('MNU-01'):");
        try {
            List<String> barisDuplikat = List.of(
                    "MNU-01|Paket A: Nasi Ayam Teriyaki|680.0|28.5|82.0|15000.0",
                    "MNU-01|Paket A Duplikasi Ilegal|700.0|30.0|80.0|16000.0"
            );
            Files.write(fileDuplikat, barisDuplikat, StandardCharsets.UTF_8);
            repoBaru.muat(fileDuplikat);
            System.out.println("Hasil: Pemuatan berhasil (SALAH - Duplikasi kode harus ditolak).");
        } catch (IOException e) {
            System.out.println("Hasil: Pemuatan ditolak -> " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // Skenario 6: Uji konsistensi state (memuat file rusak saat collection utama sudah ada isinya)
        // ---------------------------------------------------------------------
        System.out.println("\n[Skenario 6] Uji Konsistensi State (Fail-Safe State Invariance):");
        PengelolaMenu pengelolaAktif = new PengelolaMenu();
        MbgFileRepository repoAktif = new MbgFileRepository(pengelolaAktif);
        pengelolaAktif.tambahMenu(new Menu("MNU-EXISTING", "Paket Utama Terjaga", 650.0, 25.0, 80.0, 15000.0));
        System.out.println("Isi collection aktif sebelum muat file rusak:");
        pengelolaAktif.tampilkanMenu();

        try {
            repoAktif.muat(fileFormatRusak);
            System.out.println("Hasil: File rusak termuat (SALAH).");
        } catch (IOException e) {
            System.out.println("Hasil Exception Ditangkap: " + e.getMessage());
        }

        System.out.println("Pemeriksaan state collection aktif setelah exception:");
        pengelolaAktif.tampilkanMenu();
        if (pengelolaAktif.getJumlahMenu() == 1 && pengelolaAktif.cariMenu("MNU-EXISTING") != null) {
            System.out.println("Pembuktian: State Invariance TERJAMIN. Collection utama tidak hilang atau rusak!");
        } else {
            System.out.println("Pembuktian: State Invariance GAGAL (Koleksi lama terhapus prematur).");
        }

        // ---------------------------------------------------------------------
        // Skenario 7: Muat file kosong
        // ---------------------------------------------------------------------
        System.out.println("\n[Skenario 7] Muat file kosong:");
        try {
            Files.write(fileKosong, List.of(), StandardCharsets.UTF_8);
            repoAktif.muat(fileKosong);
            System.out.println("Hasil: Berhasil memuat file kosong. Total menu: " + pengelolaAktif.getJumlahMenu() + " (Collection menjadi kosong sah).");
        } catch (IOException e) {
            System.out.println("Hasil: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // Skenario 8: Simpan data dengan atribut mengandung pemisah terlarang
        // ---------------------------------------------------------------------
        System.out.println("\n[Skenario 8] Simpan data dengan atribut mengandung delimiter terlarang ('|'):");
        PengelolaMenu pengelolaPemisah = new PengelolaMenu();
        pengelolaPemisah.tambahMenu(new Menu("MNU-ERR", "Paket|Bermasalah|Karakter", 600.0, 20.0, 70.0, 12000.0));
        try {
            new MbgFileRepository(pengelolaPemisah).simpan(Paths.get("menu_ilegal.txt"));
            System.out.println("Hasil: Berhasil simpan (SALAH - Karakter delimiter lolos).");
        } catch (IllegalArgumentException e) {
            System.out.println("Hasil: Data ditolak -> " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Operasi file gagal: " + e.getMessage());
        }

        // Bersihkan file uji sementara
        try {
            if (Files.exists(fileFormatRusak)) Files.delete(fileFormatRusak);
            if (Files.exists(fileDuplikat)) Files.delete(fileDuplikat);
            if (Files.exists(fileKosong)) Files.delete(fileKosong);
            Path ilegal = Paths.get("menu_ilegal.txt");
            if (Files.exists(ilegal)) Files.delete(ilegal);
        } catch (IOException ignored) {
        }
    }

    public static void main(String[] args) {
        // Mode CLI Sesuai Panduan Praktikum Modul 12 (Langkah 4 & 5)
        if (args.length == 1 && (args[0].equalsIgnoreCase("simpan") || args[0].equalsIgnoreCase("muat"))) {
            PengelolaMenu pengelola = new PengelolaMenu();
            Path lokasi = Paths.get("menu_mbg.txt");

            try {
                if (args[0].equalsIgnoreCase("simpan")) {
                    pengelola.tambahMenu(new Menu("MNU-01", "Paket A: Nasi Ayam Teriyaki & Tumis Sayur", 680.0, 28.5, 82.0, 15000.0));
                    pengelola.tambahMenu(new Menu("MNU-02", "Paket B: Nasi Telur Balado & Sayur Sop", 640.0, 24.0, 79.0, 14000.0));
                    pengelola.simpan(lokasi);
                    System.out.println("Data berhasil disimpan.");
                } else {
                    pengelola.muat(lokasi);
                    pengelola.tampilkanMenu();
                }
            } catch (IOException e) {
                System.out.println("Operasi file gagal: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Data ditolak: " + e.getMessage());
            }
            System.exit(0);
        }

        // Mode Otomatisasi Verifikasi Lengkap
        jalankanVerifikasiPersistensiFileP12();

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

        // Luncurkan Antarmuka Desktop JavaFX
        launch(args);
    }
}
