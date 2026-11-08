package P14_3125522015_MohamadZaky.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BahanBaku {
    private String kodeBahan;
    private String namaBahan;
    private String kategori;
    private int stok;
    private String satuan;
    private double hargaSatuan;
    private LocalDate tanggalKedaluwarsa;

    public BahanBaku(String kodeBahan, String namaBahan, String kategori, int stokAwal,
                     String satuan, double hargaSatuan, LocalDate tanggalKedaluwarsa) {
        setKodeBahan(kodeBahan);
        setNamaBahan(namaBahan);
        setKategori(kategori);
        setStok(stokAwal);
        setSatuan(satuan);
        setHargaSatuan(hargaSatuan);
        setTanggalKedaluwarsa(tanggalKedaluwarsa);
    }

    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pemakaian harus lebih dari nol.");
        }
        if (jumlah > this.stok) {
            throw new StokTidakCukupException(
                    "Stok tidak cukup untuk " + namaBahan + ". Tersedia: " + this.stok + " " + satuan + ", diminta: " + jumlah + " " + satuan
            );
        }
        this.stok -= jumlah;
    }

    public void tambahStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah penambahan stok harus lebih dari nol.");
        }
        this.stok += jumlah;
    }

    public boolean isKedaluwarsa() {
        return LocalDate.now().isAfter(tanggalKedaluwarsa);
    }

    public long getSisaHariKedaluwarsa() {
        return ChronoUnit.DAYS.between(LocalDate.now(), tanggalKedaluwarsa);
    }

    public String getStatusKelayakan() {
        long sisaHari = getSisaHariKedaluwarsa();
        if (sisaHari < 0) {
            return "KEDALUWARSA (" + Math.abs(sisaHari) + " hari lalu)";
        } else if (sisaHari <= 7) {
            return "SEGERA HABIS (" + sisaHari + " hari lagi)";
        }
        return "LAYAK PAKAI (" + sisaHari + " hari)";
    }

    public String getKodeBahan() {
        return kodeBahan;
    }

    public final void setKodeBahan(String kodeBahan) {
        if (kodeBahan == null || kodeBahan.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode bahan baku tidak boleh kosong.");
        }
        this.kodeBahan = kodeBahan.trim().toUpperCase();
    }

    public String getNamaBahan() {
        return namaBahan;
    }

    public final void setNamaBahan(String namaBahan) {
        if (namaBahan == null || namaBahan.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama bahan baku tidak boleh kosong.");
        }
        this.namaBahan = namaBahan.trim();
    }

    public String getKategori() {
        return kategori;
    }

    public final void setKategori(String kategori) {
        if (kategori == null || kategori.trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori bahan baku tidak boleh kosong.");
        }
        this.kategori = kategori.trim();
    }

    public int getStok() {
        return stok;
    }

    public final void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok bahan baku tidak boleh bernilai negatif.");
        }
        this.stok = stok;
    }

    public String getSatuan() {
        return satuan;
    }

    public final void setSatuan(String satuan) {
        if (satuan == null || satuan.trim().isEmpty()) {
            throw new IllegalArgumentException("Satuan bahan baku tidak boleh kosong.");
        }
        this.satuan = satuan.trim();
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public final void setHargaSatuan(double hargaSatuan) {
        if (hargaSatuan <= 0) {
            throw new IllegalArgumentException("Harga satuan bahan baku harus bernilai positif.");
        }
        this.hargaSatuan = hargaSatuan;
    }

    public LocalDate getTanggalKedaluwarsa() {
        return tanggalKedaluwarsa;
    }

    public final void setTanggalKedaluwarsa(LocalDate tanggalKedaluwarsa) {
        if (tanggalKedaluwarsa == null) {
            throw new IllegalArgumentException("Tanggal kedaluwarsa bahan baku wajib ditentukan.");
        }
        this.tanggalKedaluwarsa = tanggalKedaluwarsa;
    }

    @Override
    public String toString() {
        return namaBahan + " (" + stok + " " + satuan + ")";
    }
}

