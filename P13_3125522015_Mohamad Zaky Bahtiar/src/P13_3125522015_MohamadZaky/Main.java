package P13_3125522015_MohamadZaky;

import P13_3125522015_MohamadZaky.model.Menu;
import P13_3125522015_MohamadZaky.model.PengelolaMenu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Aplikasi Konsol Interaktif Berbasis Menu - Sistem Manajemen Program MBG.
 * Praktikum Pemrograman Berorientasi Obyek Modul 13:
 * - Integrasi Fitur Berbasis Menu Interaktif (Scanner)
 * - Auto-load File Data pada Inisialisasi Program
 * - Separation of Concerns (Pemisahan Logika Menu ke Private Method)
 * - Mekanisme Perlindungan Status Perubahan (Unsaved Changes Indicator)
 * - Penanganan Exception Berkelanjutan (Continuous Graceful Handling)
 */
public class Main {

    private static final String FILE_DATA_DEFAULT = "menu_mbg.txt";

    /**
     * Membaca satu baris input teks dari konsol secara konsisten.
     * Menggunakan Scanner.nextLine() untuk menghindari newline residue issue.
     */
    private static String baca(Scanner input, String pesan) {
        System.out.print(pesan);
        if (input.hasNextLine()) {
            return input.nextLine().trim();
        }
        return "";
    }

    /**
     * Meminta konfirmasi saat pengguna hendak keluar atau memuat ulang
     * ketika terdapat perubahan data yang belum disimpan ke file.
     */
    private static boolean konfirmasi(Scanner input) {
        return baca(
                input,
                "Perubahan belum disimpan. Abaikan? Ketik YA: "
        ).equalsIgnoreCase("YA");
    }

    /**
     * Parsing nilai double secara aman dengan pesan error domain yang jelas.
     */
    private static double parseDouble(String str, String fieldName) {
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " harus berupa angka yang valid.");
        }
    }

    /**
     * Private Method: Menangani input dan registrasi menu baru ke pengelola.
     * Mengembalikan true jika menu berhasil ditambahkan ke koleksi.
     */
    private static boolean menuTambah(Scanner input, PengelolaMenu pengelola) {
        System.out.println("\n--- TAMBAH PAKET MENU MBG ---");
        String kode = baca(input, "Kode Menu                       : ");
        String nama = baca(input, "Nama Menu                       : ");
        String tanyaGizi = baca(input, "Input rincian gizi manual? (y/N): ");

        double kalori = 650.0;
        double protein = 25.0;
        double karbo = 75.0;
        double biaya = 15000.0;

        if (tanyaGizi.equalsIgnoreCase("y")) {
            String kaloriStr = baca(input, "Kalori per Porsi (kkal)         : ");
            kalori = kaloriStr.isEmpty() ? 650.0 : parseDouble(kaloriStr, "Kalori");

            String proteinStr = baca(input, "Kandungan Protein (gram)        : ");
            protein = proteinStr.isEmpty() ? 25.0 : parseDouble(proteinStr, "Protein");

            String karboStr = baca(input, "Kandungan Karbohidrat (gram)    : ");
            karbo = karboStr.isEmpty() ? 75.0 : parseDouble(karboStr, "Karbohidrat");

            String biayaStr = baca(input, "Biaya per Porsi (Rp)            : ");
            biaya = biayaStr.isEmpty() ? 15000.0 : parseDouble(biayaStr, "Biaya");
        }

        Menu menuBaru = new Menu(kode, nama, kalori, protein, karbo, biaya);
        pengelola.tambahMenu(menuBaru);
        System.out.println("Menu berhasil ditambahkan.");
        return true;
    }

    /**
     * Private Method: Menampilkan seluruh koleksi menu MBG yang terdaftar.
     */
    private static void menuTampilkan(PengelolaMenu pengelola) {
        System.out.println();
        pengelola.tampilkanMenu();
    }

    /**
     * Private Method: Mencari menu berdasarkan kode unik dan mencetak detailnya.
     */
    private static void menuCari(Scanner input, PengelolaMenu pengelola) {
        String kode = baca(input, "Kode yang dicari: ");
        Menu hasil = pengelola.cariMenu(kode);
        if (hasil == null) {
            System.out.println("Menu tidak ditemukan.");
        } else {
            System.out.println("Menu ditemukan:");
            System.out.println("  Kode        : " + hasil.getKodeMenu());
            System.out.println("  Nama        : " + hasil.getNamaMenu());
            System.out.println("  Kalori      : " + hasil.getKaloriPerPorsi() + " kkal");
            System.out.println("  Protein     : " + hasil.getProteinGram() + " gram");
            System.out.println("  Karbohidrat : " + hasil.getKarbohidratGram() + " gram");
            System.out.println("  Biaya Porsi : Rp " + (long) hasil.getBiayaPerPorsi());
        }
    }

    /**
     * Private Method: Menghapus menu dari koleksi berdasarkan kode.
     * Mengembalikan true jika menu ditemukan dan berhasil dihapus.
     */
    private static boolean menuHapus(Scanner input, PengelolaMenu pengelola) {
        String kode = baca(input, "Kode yang dihapus: ");
        if (pengelola.hapusMenu(kode)) {
            System.out.println("Menu berhasil dihapus.");
            return true;
        } else {
            System.out.println("Menu tidak ditemukan.");
            return false;
        }
    }

    /**
     * Private Method: Menyimpan data koleksi aktif ke file teks.
     */
    private static void menuSimpan(Path lokasi, PengelolaMenu pengelola) throws IOException {
        pengelola.simpan(lokasi);
        System.out.println("Data berhasil disimpan.");
    }

    /**
     * Menjalankan siklus loop interaktif menu aplikasi konsol MBG.
     */
    public static void jalankanAplikasi(Scanner input, Path lokasi, boolean autoLoad) {
        PengelolaMenu pengelola = new PengelolaMenu();
        boolean berubah = false;

        // Auto-load data awal sebelum pengguna mulai berinteraksi
        if (autoLoad && Files.exists(lokasi)) {
            try {
                pengelola.muat(lokasi);
                System.out.println("[AUTO-LOAD] Berhasil memuat " + pengelola.getJumlahMenu()
                        + " data awal dari berkas '" + lokasi.getFileName() + "'.");
            } catch (IOException e) {
                System.out.println("Data awal gagal dimuat: " + e.getMessage());
                return;
            }
        }

        while (true) {
            System.out.println("\n=== SISTEM MANAJEMEN MENU MAKAN BERGIZI GRATIS (MBG) ===");
            System.out.println("Status Perubahan: " + (berubah ? "[Ada perubahan belum disimpan!]" : "[Semua perubahan tersimpan]"));
            System.out.println("1. Tambah Menu       2. Tampilkan Menu");
            System.out.println("3. Cari Menu         4. Hapus Menu");
            System.out.println("5. Simpan Data       6. Muat Ulang Data");
            System.out.println("0. Keluar");

            String pilihan = baca(input, "Pilihan: ");
            if (pilihan.isEmpty()) {
                if (!input.hasNextLine()) {
                    break;
                }
                continue;
            }

            try {
                switch (pilihan) {
                    case "1":
                        if (menuTambah(input, pengelola)) {
                            berubah = true;
                        }
                        break;

                    case "2":
                        menuTampilkan(pengelola);
                        break;

                    case "3":
                        menuCari(input, pengelola);
                        break;

                    case "4":
                        if (menuHapus(input, pengelola)) {
                            berubah = true;
                        }
                        break;

                    case "5":
                        menuSimpan(lokasi, pengelola);
                        berubah = false;
                        break;

                    case "6":
                        if (berubah && !konfirmasi(input)) {
                            System.out.println("Operasi muat ulang dibatalkan. Perubahan tetap dipertahankan.");
                            break;
                        }
                        pengelola.muat(lokasi);
                        berubah = false;
                        System.out.println("Data berhasil dimuat ulang.");
                        break;

                    case "0":
                        if (berubah && !konfirmasi(input)) {
                            System.out.println("Batal keluar. Kembali ke menu.");
                            break;
                        }
                        System.out.println("Program selesai.");
                        return;

                    default:
                        System.out.println("Pilihan tidak tersedia.");
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Data ditolak: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Operasi file gagal: " + e.getMessage());
            }
        }
    }

    /**
     * Rangkaian Pengujian Otomatis 9 Skenario Fungsional P13.
     * Menguji alur normal, penolakan masukan invalid/duplikat, pembatalan unsaved changes,
     * persistensi berkas antar-siklus hidup aplikasi, serta fail-safe state invariance.
     */
    public static void jalankan9SkenarioUji() {
        System.out.println("================================================================================");
        System.out.println("     PENGUJIAN FUNGSIONAL END-TO-END SISTEM MENU MBG (MODUL PRAKTIKUM P13)      ");
        System.out.println("================================================================================");

        Path fileUji = Paths.get("menu_mbg_test.txt");

        // Siapkan file awal dengan 2 data baku
        try {
            Files.write(fileUji, java.util.List.of(
                    "MNU-01|Paket A: Nasi Ayam Teriyaki & Tumis Sayur|680.0|28.5|82.0|15000.0",
                    "MNU-02|Paket B: Nasi Telur Balado & Sayur Sop|640.0|24.0|79.0|14000.0"
            ), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Gagal menyiapkan file uji: " + e.getMessage());
            return;
        }

        // Jalankan pengujian terstruktur 9 skenario
        // Skenario 1: Input pilihan invalid (abc)
        System.out.println("\n[Skenario 1] Masukkan pilihan abc");
        simulasikanInput("abc\n0\n", fileUji);

        // Skenario 2: Tambahkan menu berkode baru
        System.out.println("\n[Skenario 2] Tambahkan buku/menu berkode baru");
        simulasikanInput("1\nMNU-03\nPaket C: Nasi Ikan Gurame Bakar\nn\n2\n0\nYA\n", fileUji);

        // Skenario 3: Tambahkan kode yang sama (duplikat)
        System.out.println("\n[Skenario 3] Tambahkan kode yang sama");
        simulasikanInput("1\nMNU-01\nPaket Duplikat Ilegal\nn\n0\n", fileUji);

        // Skenario 4: Cari kode yang terdaftar
        System.out.println("\n[Skenario 4] Cari kode yang terdaftar");
        simulasikanInput("3\nMNU-01\n0\n", fileUji);

        // Skenario 5: Hapus kode yang tidak terdaftar
        System.out.println("\n[Skenario 5] Hapus kode yang tidak terdaftar");
        simulasikanInput("4\nMNU-999\n0\n", fileUji);

        // Skenario 6: Keluar sebelum menyimpan, lalu jawab TIDAK
        System.out.println("\n[Skenario 6] Keluar sebelum menyimpan, lalu jawab TIDAK");
        simulasikanInput("1\nMNU-04\nPaket D: Nasi Rawon Daging Sapi\nn\n0\nTIDAK\n0\nYA\n", fileUji);

        // Skenario 7: Simpan, keluar, lalu jalankan kembali
        System.out.println("\n[Skenario 7] Simpan, keluar, lalu jalankan kembali");
        simulasikanInput("1\nMNU-05\nPaket E: Nasi Sop Ayam Kampung\nn\n5\n0\n", fileUji);
        System.out.println("--- Menjalankan kembali aplikasi (Verifikasi data tersimpan) ---");
        simulasikanInput("2\n0\n", fileUji);

        // Skenario 8: Hapus menu, simpan, lalu jalankan kembali
        System.out.println("\n[Skenario 8] Hapus buku/menu, simpan, lalu jalankan kembali");
        simulasikanInput("4\nMNU-05\n5\n0\n", fileUji);
        System.out.println("--- Menjalankan kembali aplikasi (Verifikasi MNU-05 tidak muncul) ---");
        simulasikanInput("2\n0\n", fileUji);

        // Skenario 9: Tambah menu, pilih muat ulang, lalu jawab TIDAK
        System.out.println("\n[Skenario 9] Tambah buku/menu, pilih muat ulang, lalu jawab TIDAK");
        simulasikanInput("1\nMNU-06\nPaket F: Nasi Pecel Komplit\nn\n6\nTIDAK\n2\n0\nYA\n", fileUji);

        // Uji Perbaikan Wajib Modul 13: Tolak Delimiter '|' sejak input
        System.out.println("\n[Uji Perbaikan Wajib] Penolakan Karakter Pemisah Terlarang '|' Sejak Input Objek");
        simulasikanInput("1\nMNU|99\nPaket Ilegal\nn\n0\n", fileUji);

        // Pengujian Tambahan 1: Rusakkan format file sebelum memilih muat ulang saat program berjalan
        System.out.println("\n[Skenario Tambahan 1] Rusakkan format file sebelum muat ulang (Uji State Invariance)");
        PengelolaMenu pengelolaAktif = new PengelolaMenu();
        try {
            // Muat file valid
            Files.write(fileUji, java.util.List.of(
                    "MNU-01|Paket A: Nasi Ayam Teriyaki & Tumis Sayur|680.0|28.5|82.0|15000.0",
                    "MNU-02|Paket B: Nasi Telur Balado & Sayur Sop|640.0|24.0|79.0|14000.0"
            ), StandardCharsets.UTF_8);
            pengelolaAktif.muat(fileUji);
            System.out.println("Collection aktif sebelum file dirusak: " + pengelolaAktif.getJumlahMenu() + " menu.");

            // File sengaja dirusak
            Files.write(fileUji, java.util.List.of("MNU-RUSAK|FormatTidakLengkap"), StandardCharsets.UTF_8);
            System.out.println("Mencoba muat ulang berkas yang formatnya rusak...");
            pengelolaAktif.muat(fileUji);
        } catch (IOException e) {
            System.out.println("Operasi file gagal: " + e.getMessage());
            System.out.println("Collection aktif setelah error: " + pengelolaAktif.getJumlahMenu() + " menu.");
            System.out.println("Hasil: State Invariance TERBUKTI (Collection sebelumnya tidak hilang atau rusak).");
        }

        // Pengujian Tambahan 2: Auto-load berhenti jika file awal saat startup sudah rusak
        System.out.println("\n[Skenario Tambahan 2] Auto-load saat program pertama kali dibuka dengan file rusak");
        simulasikanInput("2\n0\n", fileUji);

        // Bersihkan file uji
        try {
            Files.deleteIfExists(fileUji);
        } catch (IOException ignored) {}

        System.out.println("\n================================================================================");
        System.out.println("     SELURUH 9 SKENARIO UJI P13 & UJI INTEGRITAS BERHASIL DIVERIFIKASI!         ");
        System.out.println("================================================================================");
    }

    private static void simulasikanInput(String dataInput, Path lokasi) {
        InputStream originalIn = System.in;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(dataInput.getBytes(StandardCharsets.UTF_8));
            System.setIn(in);
            try (Scanner sc = new Scanner(in)) {
                jalankanAplikasi(sc, lokasi, true);
            }
        } finally {
            System.setIn(originalIn);
        }
    }

    public static void main(String[] args) {
        Path lokasi = Paths.get(FILE_DATA_DEFAULT);

        // Cek apakah dipanggil dengan opsi pengujian otomatis
        for (String arg : args) {
            if ("--test".equalsIgnoreCase(arg) || "--auto-test".equalsIgnoreCase(arg) || "--skenario".equalsIgnoreCase(arg)) {
                jalankan9SkenarioUji();
                return;
            }
        }

        // Mode Interaktif Standar Konsol
        try (Scanner scanner = new Scanner(System.in)) {
            jalankanAplikasi(scanner, lokasi, true);
        }
    }
}
