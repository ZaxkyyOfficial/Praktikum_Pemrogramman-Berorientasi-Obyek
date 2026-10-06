/**
 * Superclass CivitasAkademika merepresentasikan entitas umum warga kampus
 * dalam Sistem Informasi Akademik (SIAKAD).
 * 
 * Melakukan generalisasi terhadap atribut umum yang sebelumnya terduplikasi pada
 * class Mahasiswa dan Dosen (nomorInduk, nama, email, jenisCivitas).
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 5 (Inheritance & Generalization)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class CivitasAkademika {
    // =========================================================================
    // 1. ATTRIBUTES UMUM (PRIVATE - ENCAPSULATION & INFORMATION HIDING)
    // =========================================================================
    private String nomorInduk;   // Generalisasi dari NRP (Mahasiswa) dan NIP (Dosen)
    private String nama;         // Nama lengkap civitas
    private String email;        // Email resmi kampus (@pens.ac.id / @student.pens.ac.id)
    private String jenisCivitas; // Peran ("Mahasiswa" atau "Dosen")

    // =========================================================================
    // 2. CONSTRUCTOR SUPERCLASS
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
    // 5. COMMON BUSINESS METHODS (DAPAT DIPANGGIL OLEH SUBCLASS)
    // =========================================================================
    /**
     * Menampilkan informasi identitas umum civitas akademika.
     */
    public void tampilkanIdentitas() {
        System.out.println("------------------------------------------------------------");
        System.out.println("               DATA IDENTITAS CIVITAS AKADEMIKA             ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Jenis Civitas : " + this.jenisCivitas);
        System.out.println("Nomor Induk   : " + this.nomorInduk);
        System.out.println("Nama Lengkap  : " + this.nama);
        System.out.println("Email Resmi   : " + this.email);
        System.out.println("------------------------------------------------------------");
    }
}
