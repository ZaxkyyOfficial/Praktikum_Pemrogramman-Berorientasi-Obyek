package P15_3125522015_MohamadZaky.model;

public class Sekolah {
    private String npsn;
    private String namaSekolah;
    private String jenjang;
    private String alamat;
    private int jumlahSiswa;
    private String koordinatWilayah;

    public Sekolah(String npsn, String namaSekolah, String jenjang, String alamat, int jumlahSiswa, String koordinatWilayah) {
        setNpsn(npsn);
        setNamaSekolah(namaSekolah);
        setJenjang(jenjang);
        setAlamat(alamat);
        setJumlahSiswa(jumlahSiswa);
        setKoordinatWilayah(koordinatWilayah);
    }

    public String getNpsn() {
        return npsn;
    }

    public final void setNpsn(String npsn) {
        if (npsn == null || !npsn.trim().matches("\\d{8}")) {
            throw new IllegalArgumentException("NPSN sekolah harus terdiri atas 8 digit angka.");
        }
        this.npsn = npsn.trim();
    }

    public String getNamaSekolah() {
        return namaSekolah;
    }

    public final void setNamaSekolah(String namaSekolah) {
        if (namaSekolah == null || namaSekolah.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama sekolah tidak boleh kosong.");
        }
        this.namaSekolah = namaSekolah.trim();
    }

    public String getJenjang() {
        return jenjang;
    }

    public final void setJenjang(String jenjang) {
        if (jenjang == null || jenjang.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenjang sekolah (SD/SMP/SMA) tidak boleh kosong.");
        }
        this.jenjang = jenjang.trim().toUpperCase();
    }

    public String getAlamat() {
        return alamat;
    }

    public final void setAlamat(String alamat) {
        if (alamat == null || alamat.trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat sekolah tidak boleh kosong.");
        }
        this.alamat = alamat.trim();
    }

    public int getJumlahSiswa() {
        return jumlahSiswa;
    }

    public final void setJumlahSiswa(int jumlahSiswa) {
        if (jumlahSiswa <= 0) {
            throw new IllegalArgumentException("Jumlah siswa penerima harus berupa bilangan bulat lebih dari nol.");
        }
        this.jumlahSiswa = jumlahSiswa;
    }

    public String getKoordinatWilayah() {
        return koordinatWilayah;
    }

    public final void setKoordinatWilayah(String koordinatWilayah) {
        if (koordinatWilayah == null || koordinatWilayah.trim().isEmpty()) {
            throw new IllegalArgumentException("Koordinat wilayah sekolah tidak boleh kosong.");
        }
        this.koordinatWilayah = koordinatWilayah.trim();
    }

    @Override
    public String toString() {
        return npsn + " - " + namaSekolah + " (" + jumlahSiswa + " siswa)";
    }
}

