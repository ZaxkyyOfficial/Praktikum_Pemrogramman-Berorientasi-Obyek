/**
 * Subclass Dosen merepresentasikan entitas tenaga pengajar dan pembimbing akademik (Dosen Wali)
 * dalam Sistem Informasi Akademik (SIAKAD).
 * 
 * Menerapkan prinsip Inheritance dengan meng-extends superclass CivitasAkademika.
 * Menggunakan keyword super(...) pada konstruktor (constructor chaining) untuk menginisialisasi
 * atribut umum (nomorInduk/NIP, nama, email, jenisCivitas), serta menambahkan atribut spesifik
 * bidangKeahlian.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 5 (Inheritance & Generalization)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Dosen extends CivitasAkademika {
    // =========================================================================
    // 1. ATTRIBUTES KHUSUS DOSEN (PRIVATE - ENCAPSULATION)
    // Atribut nomorInduk (NIP), nama, dan email telah dipindahkan ke superclass.
    // =========================================================================
    private String bidangKeahlian;

    // =========================================================================
    // 2. CONSTRUCTOR (IMPLEMENTASI CONSTRUCTOR CHAINING DENGAN super)
    // =========================================================================
    /**
     * Constructor Subclass Dosen:
     * Memanggil constructor superclass CivitasAkademika menggunakan keyword super(...).
     */
    public Dosen(String nip, String nama, String bidangKeahlian, String email) {
        super(nip, nama, email, "Dosen"); // Constructor Chaining ke Superclass
        setBidangKeahlian(bidangKeahlian);
    }

    /**
     * Constructor Overload untuk fleksibilitas instansiasi.
     */
    public Dosen(String nip, String nama) {
        this(nip, nama, "Umum", "dosen@pens.ac.id");
    }

    // =========================================================================
    // 3. GETTER & SETTER KHUSUS
    // =========================================================================
    /**
     * Getter NIP: Mendelegasikan pembacaan ke getNomorInduk() milik superclass
     * sehingga mematuhi enkapsulasi tanpa mengakses field private superclass secara langsung.
     */
    public String getNip() {
        return super.getNomorInduk();
    }

    public String getBidangKeahlian() {
        return this.bidangKeahlian;
    }

    public final void setBidangKeahlian(String bidangKeahlian) {
        if (bidangKeahlian != null && !bidangKeahlian.trim().isEmpty()) {
            this.bidangKeahlian = bidangKeahlian.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Bidang keahlian tidak boleh kosong!");
        }
    }

    // =========================================================================
    // 4. BUSINESS LOGIC METHODS (INTERAKSI & RELASI ASOSIASI DENGAN KRS)
    // =========================================================================
    /**
     * Interaksi Asosiasi: Dosen Wali memeriksa dan mengesahkan dokumen KRS mahasiswa.
     * Mengakses data nama dosen via getter superclass getNama().
     */
    public void validasiDanSetujuiKrs(KRS krs) {
        if (krs == null) {
            System.out.println("[VALIDASI DOSEN] Gagal: Dokumen KRS bernilai null!");
            return;
        }

        System.out.println("\n>> [PROSES BIMBINGAN] Dosen Wali (" + super.getNama() + ") memeriksa KRS: " + krs.getNomorKrs());
        if (krs.getJumlahMk() == 0) {
            System.out.println("   [PENOLAKAN] KRS belum memiliki mata kuliah yang terdaftar.");
        } else {
            System.out.println("   [DISETUJUI] KRS valid (" + krs.getJumlahMk() + " MK terdaftar, Total " + 
                               krs.hitungTotalSksKrs() + " SKS). Mengesahkan KRS...");
            krs.setujuiKrs(this);
        }
    }

    /**
     * Menampilkan profil lengkap dosen dengan memadukan data superclass dan subclass.
     */
    public void tampilkanProfil() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                   PROFIL DOSEN PENGAMPU                    ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Peran Civitas    : " + super.getJenisCivitas());
        System.out.println("NIP              : " + super.getNomorInduk());
        System.out.println("Nama Lengkap     : " + super.getNama());
        System.out.println("Email Resmi      : " + super.getEmail());
        System.out.println("Bidang Keahlian  : " + this.bidangKeahlian);
        System.out.println("------------------------------------------------------------");
    }
}
