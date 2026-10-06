/**
 * Abstract Class CivitasAkademika merepresentasikan kerangka konseptual umum warga kampus
 * dalam Sistem Informasi Akademik (SIAKAD).
 * 
 * Karena hanya berfungsi sebagai kerangka umum (abstraksi tingkat tinggi), class ini tidak
 * dapat diinstansiasi secara langsung menggunakan operator 'new'.
 * Class ini mendefinisikan atribut umum, constructor chaining, concrete method kartu identitas,
 * serta minimal 1 abstract method (tampilkanPeran) yang wajib diimplementasikan oleh setiap subclass konkret.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 7 (Abstract Class & Interface)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public abstract class CivitasAkademika {
    // =========================================================================
    // 1. ATTRIBUTES UMUM (PRIVATE - ENCAPSULATION & INFORMATION HIDING)
    // =========================================================================
    private String nomorInduk;   // Generalisasi dari NRP (Mahasiswa) dan NIP (Dosen)
    private String nama;         // Nama lengkap civitas
    private String email;        // Email resmi kampus (@pens.ac.id / @student.pens.ac.id)
    private String jenisCivitas; // Kategori peran ("Mahasiswa" atau "Dosen")

    // =========================================================================
    // 2. CONSTRUCTOR ABSTRACT SUPERCLASS (DIPANGGIL VIA CONSTRUCTOR CHAINING super)
    // =========================================================================
    public CivitasAkademika(String nomorInduk, String nama, String email, String jenisCivitas) {
        setNomorIndukInternal(nomorInduk);
        setNama(nama);
        setEmail(email);
        this.jenisCivitas = (jenisCivitas != null && !jenisCivitas.trim().isEmpty()) ? jenisCivitas.trim() : "Warga Kampus";
    }

    // =========================================================================
    // 3. GETTER (ACCESSOR METHODS)
    // =========================================================================
    public String getNomorInduk() {
        return this.nomorInduk;
    }

    public String getNama() {
        return this.nama;
    }

    public String getEmail() {
        return this.email;
    }

    public String getJenisCivitas() {
        return this.jenisCivitas;
    }

    // =========================================================================
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS)
    // =========================================================================
    private void setNomorIndukInternal(String nomorInduk) {
        if (nomorInduk != null && !nomorInduk.trim().isEmpty()) {
            this.nomorInduk = nomorInduk.trim();
        } else {
            this.nomorInduk = "0000000000";
            System.out.println("[VALIDASI ERROR] Nomor induk tidak boleh kosong! Diset ke default.");
        }
    }

    public final void setNama(String nama) {
        if (nama != null && nama.trim().length() >= 3) {
            this.nama = nama.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama tidak valid! Minimal 3 karakter dan tidak boleh kosong.");
        }
    }

    public final void setEmail(String email) {
        if (email != null && email.contains("@") && email.contains(".")) {
            this.email = email.trim();
        } else {
            this.email = "civitas@pens.ac.id";
            System.out.println("[VALIDASI ERROR] Format email tidak valid! Diset ke civitas@pens.ac.id.");
        }
    }

    // =========================================================================
    // 5. ABSTRACT METHOD (KONTRAK WAJIB IMPLEMENTASI PADA SUBCLASS)
    // =========================================================================
    /**
     * Method abstrak yang mendefinisikan kontrak perilaku bahwa setiap entitas
     * warga kampus wajib memiliki deskripsi peran spesifik di lingkungan akademik.
     * Tidak memiliki implementasi (body) pada superclass; wajib di-override oleh subclass konkret.
     */
    public abstract void tampilkanPeran();

    // =========================================================================
    // 6. CONCRETE METHOD & OVERLOADING (COMPILE-TIME POLYMORPHISM)
    // =========================================================================
    /**
     * Overload 1: Mencetak kartu identitas dengan judul header default.
     */
    public void cetakKartuIdentitas() {
        cetakKartuIdentitas("KARTU IDENTITAS RESMI CIVITAS AKADEMIKA");
    }

    /**
     * Overload 2: Mencetak kartu identitas dengan judul header kustom.
     */
    public void cetakKartuIdentitas(String headerCustom) {
        System.out.println("------------------------------------------------------------");
        System.out.println("           " + headerCustom + "           ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Peran Civitas : " + this.jenisCivitas);
        System.out.println("Nomor Induk   : " + this.nomorInduk);
        System.out.println("Nama Lengkap  : " + this.nama);
        System.out.println("Email Resmi   : " + this.email);
        System.out.println("------------------------------------------------------------");
    }
}
