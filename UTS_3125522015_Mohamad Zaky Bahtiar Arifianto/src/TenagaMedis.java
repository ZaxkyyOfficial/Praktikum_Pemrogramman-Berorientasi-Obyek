public abstract class TenagaMedis {

    // Atribut privat (Encapsulation)
    private String idMedis;
    private String nama;
    private String spesialisasi;

    /**
     * Konstruktor berparameter untuk inisialisasi identitas tenaga medis.
     * Menerapkan validasi bisnis langsung saat instansiasi.
     * 
     * @param idMedis Kode unik tenaga medis (misal: DOC-001, NUR-001)
     * @param nama Nama lengkap beserta gelar
     * @param spesialisasi Bidang keahlian klinis
     */
    public TenagaMedis(String idMedis, String nama, String spesialisasi) {
        setIdMedis(idMedis);
        setNama(nama);
        setSpesialisasi(spesialisasi);
    }

    // --- Getter & Setter dengan Validasi Bisnis (Encapsulation) ---

    public String getIdMedis() {
        return idMedis;
    }

    public void setIdMedis(String idMedis) {
        if (idMedis == null || idMedis.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] ID Medis tidak boleh kosong!");
        }
        this.idMedis = idMedis.trim();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().length() < 2) {
            throw new IllegalArgumentException("[Validasi Error] Nama tenaga medis minimal harus 2 karakter dan tidak boleh kosong!");
        }
        this.nama = nama.trim();
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(String spesialisasi) {
        if (spesialisasi == null || spesialisasi.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] Spesialisasi klinis tidak boleh kosong!");
        }
        this.spesialisasi = spesialisasi.trim();
    }

    /**
     * Abstract Method yang mewajibkan seluruh subclass untuk mendefinisikan
     * perilaku spesifik dari profesinya masing-masing (Dynamic Polymorphism).
     */
    public abstract void tampilkanPeran();

    @Override
    public String toString() {
        return "[" + idMedis + "] " + nama + " (" + spesialisasi + ")";
    }
}
