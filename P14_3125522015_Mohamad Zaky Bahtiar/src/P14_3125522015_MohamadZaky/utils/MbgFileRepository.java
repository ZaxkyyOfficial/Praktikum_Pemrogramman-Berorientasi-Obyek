package P14_3125522015_MohamadZaky.utils;

import P14_3125522015_MohamadZaky.model.Menu;
import P14_3125522015_MohamadZaky.model.PengelolaMenu;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository khusus untuk mengelola persistensi data Program MBG ke media file teks (.txt).
 * Menerapkan prinsip Separation of Concerns, Fail-Fast Validation, dan State Invariance.
 */
public class MbgFileRepository {

    private final PengelolaMenu pengelolaMenu;

    public MbgFileRepository() {
        this(new PengelolaMenu());
    }

    public MbgFileRepository(PengelolaMenu pengelolaMenu) {
        if (pengelolaMenu == null) {
            throw new IllegalArgumentException("PengelolaMenu tidak boleh null.");
        }
        this.pengelolaMenu = pengelolaMenu;
    }

    public PengelolaMenu getPengelolaMenu() {
        return pengelolaMenu;
    }

    /**
     * Menyimpan seluruh koleksi Menu ke dalam file teks dengan delimiter '|'.
     * Seluruh data divalidasi terlebih dahulu sebelum operasi penulisan file dimulai.
     *
     * @param lokasi Path file tujuan penyimpanan
     * @throws IOException jika terjadi kesalahan saat penulisan file
     * @throws IllegalArgumentException jika ada atribut objek yang mengandung delimiter '|', '\n', atau '\r'
     */
    public void simpan(Path lokasi) throws IOException {
        simpan(lokasi, this.pengelolaMenu);
    }

    /**
     * Menyimpan data dari instance PengelolaMenu tertentu ke file teks.
     */
    public void simpan(Path lokasi, PengelolaMenu sumber) throws IOException {
        if (lokasi == null) {
            throw new IllegalArgumentException("Lokasi file penyimpanan tidak boleh null.");
        }
        if (sumber == null) {
            throw new IllegalArgumentException("Sumber PengelolaMenu tidak boleh null.");
        }

        List<String> baris = new ArrayList<>();
        for (Menu menu : sumber.getDaftarMenu()) {
            String kode = menu.getKodeMenu();
            String nama = menu.getNamaMenu();
            String kalori = String.valueOf(menu.getKaloriPerPorsi());
            String protein = String.valueOf(menu.getProteinGram());
            String karbo = String.valueOf(menu.getKarbohidratGram());
            String biaya = String.valueOf(menu.getBiayaPerPorsi());

            // Validasi karakter delimiter terlarang
            for (String nilai : new String[]{kode, nama, kalori, protein, karbo, biaya}) {
                if (nilai != null && (nilai.contains("|") || nilai.contains("\n") || nilai.contains("\r"))) {
                    throw new IllegalArgumentException("Kode/judul mengandung pemisah terlarang.");
                }
            }

            baris.add(kode + "|" + nama + "|" + kalori + "|" + protein + "|" + karbo + "|" + biaya);
        }

        if (lokasi.getParent() != null && !Files.exists(lokasi.getParent())) {
            Files.createDirectories(lokasi.getParent());
        }

        Files.write(lokasi, baris, StandardCharsets.UTF_8);
    }

    /**
     * Membaca file teks dan membentuk kembali kumpulan objek Menu ke dalam koleksi.
     * Menggunakan koleksi sementara (temporary) untuk menjamin konsistensi state.
     * Koleksi utama TIDAK akan dikosongkan sebelum seluruh baris selesai divalidasi.
     *
     * @param lokasi Path file sumber pembacaan
     * @throws IOException jika file tidak ditemukan, format baris salah, atau data domain tidak valid
     */
    public void muat(Path lokasi) throws IOException {
        muat(lokasi, this.pengelolaMenu);
    }

    /**
     * Membaca file teks dan memuat isinya ke dalam target PengelolaMenu tertentu.
     */
    public void muat(Path lokasi, PengelolaMenu target) throws IOException {
        if (lokasi == null) {
            throw new IllegalArgumentException("Lokasi file pembacaan tidak boleh null.");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target PengelolaMenu tidak boleh null.");
        }

        List<String> baris = Files.readAllLines(lokasi, StandardCharsets.UTF_8);
        PengelolaMenu sementara = new PengelolaMenu();

        for (int i = 0; i < baris.size(); i++) {
            String barisTeks = baris.get(i);
            String[] bagian = barisTeks.split("\\|", -1);

            if (bagian.length != 6) {
                throw new IOException("Format salah pada baris " + (i + 1));
            }

            try {
                String kode = bagian[0];
                String nama = bagian[1];
                double kalori = Double.parseDouble(bagian[2].trim());
                double protein = Double.parseDouble(bagian[3].trim());
                double karbo = Double.parseDouble(bagian[4].trim());
                double biaya = Double.parseDouble(bagian[5].trim());

                Menu menu = new Menu(kode, nama, kalori, protein, karbo, biaya);
                sementara.tambahMenu(menu);
            } catch (NumberFormatException e) {
                throw new IOException("Data tidak valid pada baris " + (i + 1) + ": Format angka tidak sesuai.", e);
            } catch (IllegalArgumentException e) {
                throw new IOException("Data tidak valid pada baris " + (i + 1) + ": " + e.getMessage(), e);
            }
        }

        // Ganti collection hanya setelah semua baris valid tanpa exception
        target.gantiSemua(sementara.getDaftarMenu());
    }
}

