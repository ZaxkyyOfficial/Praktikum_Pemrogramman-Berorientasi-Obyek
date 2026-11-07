package P11_3125522015_MohamadZaky.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Menu {
    private String kodeMenu;
    private String namaMenu;
    private double kaloriPerPorsi;
    private double proteinGram;
    private double karbohidratGram;
    private double biayaPerPorsi;
    private final Map<BahanBaku, Integer> komposisiPer100Porsi;

    public Menu(String kodeMenu, String namaMenu, double kaloriPerPorsi,
                double proteinGram, double karbohidratGram, double biayaPerPorsi) {
        setKodeMenu(kodeMenu);
        setNamaMenu(namaMenu);
        setKaloriPerPorsi(kaloriPerPorsi);
        setProteinGram(proteinGram);
        setKarbohidratGram(karbohidratGram);
        setBiayaPerPorsi(biayaPerPorsi);
        this.komposisiPer100Porsi = new LinkedHashMap<>();
    }

    public void tambahBahanBaku(BahanBaku bahan, int kebutuhanPer100Porsi) {
        if (bahan == null) {
            throw new IllegalArgumentException("Objek BahanBaku tidak boleh null.");
        }
        if (kebutuhanPer100Porsi <= 0) {
            throw new IllegalArgumentException("Kebutuhan bahan per 100 porsi harus lebih dari nol.");
        }
        komposisiPer100Porsi.put(bahan, kebutuhanPer100Porsi);
    }

    public int hitungKebutuhanBahan(BahanBaku bahan, int targetPorsi) {
        if (targetPorsi <= 0) {
            throw new IllegalArgumentException("Target porsi harus lebih dari nol.");
        }
        Integer per100 = komposisiPer100Porsi.get(bahan);
        if (per100 == null) {
            return 0;
        }
        return (int) Math.ceil((per100 * (double) targetPorsi) / 100.0);
    }

    public void alokasikanBahanUntukProduksi(int targetPorsi) throws StokTidakCukupException {
        if (targetPorsi <= 0) {
            throw new IllegalArgumentException("Target porsi produksi harus lebih dari nol.");
        }
        if (komposisiPer100Porsi.isEmpty()) {
            throw new IllegalArgumentException("Menu belum memiliki komposisi bahan baku.");
        }

        for (Map.Entry<BahanBaku, Integer> entry : komposisiPer100Porsi.entrySet()) {
            BahanBaku bahan = entry.getKey();
            int dibutuhkan = hitungKebutuhanBahan(bahan, targetPorsi);
            if (bahan.isKedaluwarsa()) {
                throw new IllegalArgumentException(
                        "Bahan '" + bahan.getNamaBahan() + "' sudah kedaluwarsa (" + bahan.getTanggalKedaluwarsa() + ") dan tidak boleh diproduksi."
                );
            }
            if (dibutuhkan > bahan.getStok()) {
                throw new StokTidakCukupException(bahan.getNamaBahan(), bahan.getStok(), dibutuhkan);
            }
        }

        for (Map.Entry<BahanBaku, Integer> entry : komposisiPer100Porsi.entrySet()) {
            BahanBaku bahan = entry.getKey();
            int dibutuhkan = hitungKebutuhanBahan(bahan, targetPorsi);
            bahan.kurangiStok(dibutuhkan);
        }
    }

    public Map<BahanBaku, Integer> getKomposisiPer100Porsi() {
        return Collections.unmodifiableMap(komposisiPer100Porsi);
    }

    public String getKodeMenu() {
        return kodeMenu;
    }

    public final void setKodeMenu(String kodeMenu) {
        if (kodeMenu == null || kodeMenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode menu tidak boleh kosong.");
        }
        this.kodeMenu = kodeMenu.trim().toUpperCase();
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public final void setNamaMenu(String namaMenu) {
        if (namaMenu == null || namaMenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama menu tidak boleh kosong.");
        }
        this.namaMenu = namaMenu.trim();
    }

    public double getKaloriPerPorsi() {
        return kaloriPerPorsi;
    }

    public final void setKaloriPerPorsi(double kaloriPerPorsi) {
        if (kaloriPerPorsi <= 0) {
            throw new IllegalArgumentException("Kalori per porsi harus lebih dari nol.");
        }
        this.kaloriPerPorsi = kaloriPerPorsi;
    }

    public double getProteinGram() {
        return proteinGram;
    }

    public final void setProteinGram(double proteinGram) {
        if (proteinGram < 0) {
            throw new IllegalArgumentException("Kandungan protein tidak boleh bernilai negatif.");
        }
        this.proteinGram = proteinGram;
    }

    public double getKarbohidratGram() {
        return karbohidratGram;
    }

    public final void setKarbohidratGram(double karbohidratGram) {
        if (karbohidratGram < 0) {
            throw new IllegalArgumentException("Kandungan karbohidrat tidak boleh bernilai negatif.");
        }
        this.karbohidratGram = karbohidratGram;
    }

    public double getBiayaPerPorsi() {
        return biayaPerPorsi;
    }

    public final void setBiayaPerPorsi(double biayaPerPorsi) {
        if (biayaPerPorsi <= 0) {
            throw new IllegalArgumentException("Biaya per porsi harus lebih dari nol.");
        }
        this.biayaPerPorsi = biayaPerPorsi;
    }

    @Override
    public String toString() {
        return kodeMenu + " - " + namaMenu + " (" + (long) kaloriPerPorsi + " kkal)";
    }
}

