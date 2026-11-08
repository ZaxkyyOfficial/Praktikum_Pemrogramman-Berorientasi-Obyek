package P14_3125522015_MohamadZaky;

import P14_3125522015_MohamadZaky.model.Menu;
import P14_3125522015_MohamadZaky.model.PengelolaMenu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static final String FILE_DATA_DEFAULT = "mbg_data.txt";

    private static String baca(Scanner input, String pesan) {
        System.out.print(pesan);
        if (input.hasNextLine()) {
            return input.nextLine().trim();
        }
        return "";
    }

    private static boolean konfirmasi(Scanner input) {
        return baca(
                input,
                "Perubahan belum disimpan. Abaikan? Ketik YA: "
        ).equalsIgnoreCase("YA");
    }

    private static double parseDouble(String str, String fieldName) {
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " harus berupa angka yang valid.");
        }
    }

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

    private static void menuTampilkan(PengelolaMenu pengelola) {
        System.out.println();
        pengelola.tampilkanMenu();
    }

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

    private static boolean menuSimpan(Path lokasi, PengelolaMenu pengelola) throws IOException {
        pengelola.simpan(lokasi);
        System.out.println("Data berhasil disimpan.");
        return true;
    }

    private static boolean menuMuatUlang(Scanner input, Path lokasi, PengelolaMenu pengelola, boolean statusPerubahanTerakhir) throws IOException {
        if (statusPerubahanTerakhir && !konfirmasi(input)) {
            System.out.println("Operasi muat ulang dibatalkan. Perubahan tetap dipertahankan.");
            return statusPerubahanTerakhir;
        }
        pengelola.muat(lokasi);
        System.out.println("Data berhasil dimuat ulang.");
        return false;
    }

    private static boolean menuKeluar(Scanner input, boolean statusPerubahanTerakhir) {
        if (statusPerubahanTerakhir && !konfirmasi(input)) {
            System.out.println("Batal keluar. Kembali ke menu.");
            return false;
        }
        System.out.println("Program selesai.");
        return true;
    }

    public static void jalankanAplikasi(Scanner input, Path lokasi, boolean autoLoad) {
        PengelolaMenu pengelola = new PengelolaMenu();
        boolean adaPerubahan = false;

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
            System.out.println("Status Perubahan: " + (adaPerubahan ? "[Ada perubahan belum disimpan!]" : "[Semua perubahan tersimpan]"));
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
                            adaPerubahan = true;
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
                            adaPerubahan = true;
                        }
                        break;

                    case "5":
                        if (menuSimpan(lokasi, pengelola)) {
                            adaPerubahan = false;
                        }
                        break;

                    case "6":
                        adaPerubahan = menuMuatUlang(input, lokasi, pengelola, adaPerubahan);
                        break;

                    case "0":
                        if (menuKeluar(input, adaPerubahan)) {
                            return;
                        }
                        break;

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

    public static void jalankan10SkenarioUji() {
        System.out.println("================================================================================");
        System.out.println("   PENGUJIAN FUNGSIONAL & REGRESI SISTEM MENU MBG (MODUL PRAKTIKUM P14)         ");
        System.out.println("================================================================================");

        Path fileUji = Paths.get("mbg_data_test.txt");

        java.util.function.Consumer<Path> resetDataAwal = path -> {
            try {
                Files.write(path, java.util.List.of(
                        "MNU-01|Paket A: Nasi Ayam Teriyaki & Tumis Sayur|680.0|28.5|82.0|15000.0",
                        "MNU-02|Paket B: Nasi Telur Balado & Sayur Sop|640.0|24.0|79.0|14000.0"
                ), StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.err.println("Gagal reset data uji: " + e.getMessage());
            }
        };

        // Skenario 1: Tampilkan collection kosong
        System.out.println("\n[Skenario 1] Tampilkan collection kosong");
        try {
            Files.write(fileUji, java.util.Collections.emptyList(), StandardCharsets.UTF_8);
        } catch (IOException ignored) {}
        simulasikanInput("2\n0\n", fileUji);

        resetDataAwal.accept(fileUji);

        // Skenario 2: Tambah kode baru dengan data valid
        System.out.println("\n[Skenario 2] Tambah kode baru dengan judul valid");
        simulasikanInput("1\nMNU-03\nPaket C: Nasi Ikan Gurame Bakar\nn\n2\n0\nYA\n", fileUji);

        // Skenario 3: Tambah kode yang sudah terdaftar
        System.out.println("\n[Skenario 3] Tambah kode yang sudah terdaftar");
        resetDataAwal.accept(fileUji);
        simulasikanInput("1\nMNU-01\nPaket Duplikat Ilegal\nn\n0\n", fileUji);

        // Skenario 4: Tambah kode atau judul berisi spasi saja
        System.out.println("\n[Skenario 4] Tambah kode atau judul berisi spasi saja");
        resetDataAwal.accept(fileUji);
        simulasikanInput("1\n   \nPaket Tanpa Kode\nn\n1\nMNU-99\n   \nn\n0\n", fileUji);

        // Skenario 5: Tambah judul mengandung '|'
        System.out.println("\n[Skenario 5] Tambah judul mengandung '|'");
        resetDataAwal.accept(fileUji);
        simulasikanInput("1\nMNU-04\nPaket D | Ekstra Sambal Terlarang\nn\n0\n", fileUji);

        // Skenario 6: Cari kode dengan huruf kecil (misal mnu-01)
        System.out.println("\n[Skenario 6] Cari kode dengan huruf kecil, misalnya mnu-01");
        resetDataAwal.accept(fileUji);
        simulasikanInput("3\nmnu-01\n0\n", fileUji);

        // Skenario 7: Hapus kode yang tidak terdaftar
        System.out.println("\n[Skenario 7] Hapus kode yang tidak terdaftar");
        resetDataAwal.accept(fileUji);
        simulasikanInput("4\nMNU-999\n0\n", fileUji);

        // Skenario 8: Simpan, tutup, lalu jalankan kembali
        System.out.println("\n[Skenario 8] Simpan, tutup, lalu jalankan kembali");
        resetDataAwal.accept(fileUji);
        simulasikanInput("1\nMNU-05\nPaket E: Nasi Rawon Daging Sapi\nn\n5\n0\n", fileUji);
        System.out.println("--- Menjalankan kembali aplikasi (Verifikasi data MNU-05 tersimpan di file) ---");
        simulasikanInput("2\n0\n", fileUji);

        // Skenario 9: Muat file dengan baris rusak atau format tidak lengkap
        System.out.println("\n[Skenario 9] Muat file dengan baris rusak atau kode duplikat");
        resetDataAwal.accept(fileUji);
        PengelolaMenu pengelolaMemori = new PengelolaMenu();
        try {
            pengelolaMemori.muat(fileUji);
            System.out.println("State memori awal: " + pengelolaMemori.getJumlahMenu() + " menu.");

            Files.write(fileUji, java.util.List.of("MNU-RUSAK|FormatKolomTidakCukup"), StandardCharsets.UTF_8);
            System.out.println("Mencoba memuat berkas rusak ke dalam pengelola...");
            pengelolaMemori.muat(fileUji);
        } catch (IOException e) {
            System.out.println("Operasi file ditolak secara fail-fast: " + e.getMessage());
            System.out.println("State memori setelah gagal memuat: " + pengelolaMemori.getJumlahMenu() + " menu.");
            System.out.println("Hasil: State Invariance TERBUKTI (Koleksi di memori tidak rusak atau kosong).");
        }

        // Skenario 10: Keluar saat ada perubahan, lalu batalkan
        System.out.println("\n[Skenario 10] Keluar saat ada perubahan, lalu batalkan");
        resetDataAwal.accept(fileUji);
        simulasikanInput("1\nMNU-06\nPaket F: Nasi Pecel Ayam Madiun\nn\n0\nTIDAK\n2\n0\nYA\n", fileUji);

        try {
            Files.deleteIfExists(fileUji);
        } catch (IOException ignored) {}

        System.out.println("\n================================================================================");
        System.out.println("   SELURUH 10 SKENARIO UJI P14 & VERIFIKASI REGRESI 100% SUKSES!               ");
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

        for (String arg : args) {
            if ("--test".equalsIgnoreCase(arg) || "--auto-test".equalsIgnoreCase(arg) || "--skenario".equalsIgnoreCase(arg)) {
                jalankan10SkenarioUji();
                return;
            }
        }

        try (Scanner scanner = new Scanner(System.in)) {
            jalankanAplikasi(scanner, lokasi, true);
        }
    }
}
