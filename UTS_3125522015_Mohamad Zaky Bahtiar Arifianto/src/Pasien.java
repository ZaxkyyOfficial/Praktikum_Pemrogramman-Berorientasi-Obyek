public class Pasien {

    // Atribut privat (Encapsulation)
    private String idPasien;
    private String namaPasien;
    private int usia;
    private boolean statusBpjs;

    // Konstanta tarif diskon BPJS (Kreativitas Solusi)
    public static final double PERSENTASE_DISKON_BPJS = 0.50; // Diskon 50% untuk peserta BPJS

    /**
     * Konstruktor lengkap untuk inisialisasi data Pasien.
     * 
     * @param idPasien Nomor rekam medis pasien (misal: PAS-001)
     * @param namaPasien Nama lengkap pasien
     * @param usia Usia pasien dalam satuan tahun
     * @param statusBpjs True jika pasien terdaftar sebagai peserta aktif BPJS
     */
    public Pasien(String idPasien, String namaPasien, int usia, boolean statusBpjs) {
        setIdPasien(idPasien);
        setNamaPasien(namaPasien);
        setUsia(usia);
        setStatusBpjs(statusBpjs);
    }

    // --- Getter & Setter dengan Validasi Ketat ---

    public String getIdPasien() {
        return idPasien;
    }

    public void setIdPasien(String idPasien) {
        if (idPasien == null || idPasien.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] ID Pasien tidak boleh kosong!");
        }
        this.idPasien = idPasien.trim();
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        if (namaPasien == null || namaPasien.trim().length() < 2) {
            throw new IllegalArgumentException("[Validasi Error] Nama pasien minimal harus 2 karakter!");
        }
        this.namaPasien = namaPasien.trim();
    }

    public int getUsia() {
        return usia;
    }

    public void setUsia(int usia) {
        if (usia < 0 || usia > 150) {
            throw new IllegalArgumentException("[Validasi Error] Usia pasien tidak valid (harus berada di rentang 0 - 150 tahun)!");
        }
        this.usia = usia;
    }

    public boolean isStatusBpjs() {
        return statusBpjs;
    }

    public void setStatusBpjs(boolean statusBpjs) {
        this.statusBpjs = statusBpjs;
    }

    /**
     * Fitur Kreativitas: Logika bisnis penghitungan diskon otomatis berbasis kepesertaan BPJS.
     * Pasien BPJS berhak mendapatkan subsidi potongan biaya sebesar 50%.
     * 
     * @param totalBiaya Total tagihan sebelum diskon
     * @return Nominal potongan diskon
     */
    public double hitungDiskonBpjs(double totalBiaya) {
        if (totalBiaya <= 0) return 0.0;
        return this.statusBpjs ? (totalBiaya * PERSENTASE_DISKON_BPJS) : 0.0;
    }

    @Override
    public String toString() {
        return "[" + idPasien + "] " + namaPasien + " (" + usia + " th) - " + (statusBpjs ? "BPJS Aktif" : "Umum / Mandiri");
    }
}
