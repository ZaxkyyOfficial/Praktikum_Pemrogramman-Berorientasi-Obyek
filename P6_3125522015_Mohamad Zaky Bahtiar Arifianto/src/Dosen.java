/**
 * Subclass Dosen merepresentasikan entitas tenaga pengajar dan pembimbing akademik (Dosen Wali)
 * dalam Sistem Informasi Akademik (SIAKAD).
 * 
 * Modul 6: Menerapkan Method Overriding (@Override tampilkanPeran) untuk mendukung Dynamic Binding,
 * serta Method Overloading pada proses persetujuan dokumen KRS (validasiDanSetujuiKrs).
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 6 (Polymorphism & Dynamic Binding)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Dosen extends CivitasAkademika {
    // =========================================================================
    // 1. ATTRIBUTES KHUSUS DOSEN (PRIVATE - ENCAPSULATION)
    // =========================================================================
    private String bidangKeahlian;

    // =========================================================================
    // 2. CONSTRUCTOR (CONSTRUCTOR CHAINING DENGAN super)
    // =========================================================================
    public Dosen(String nip, String nama, String bidangKeahlian, String email) {
        super(nip, nama, email, "Dosen"); // Constructor Chaining ke Superclass
        setBidangKeahlian(bidangKeahlian);
    }

    public Dosen(String nip, String nama) {
        this(nip, nama, "Umum", "dosen@pens.ac.id");
    }

    // =========================================================================
    // 3. GETTER & SETTER KHUSUS
    // =========================================================================
    public String getNip() {
        return super.getNomorInduk(); // Mematuhi enkapsulasi via getter superclass
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
    // 4. METHOD OVERRIDING (RUNTIME POLYMORPHISM / DYNAMIC BINDING)
    // =========================================================================
    /**
     * Meng-override method tampilkanPeran() dari CivitasAkademika.
     * Mengimplementasikan perilaku spesifik tenaga pengajar dan dosen wali.
     */
    @Override
    public void tampilkanPeran() {
        System.out.println("[PERAN DOSEN] " + super.getNama() + " (" + getNip() + 
                           ") - Menjalankan Tridharma Perguruan Tinggi, membimbing mahasiswa, " + 
                           "mengajar pada bidang " + this.bidangKeahlian + ", serta memvalidasi KRS.");
    }

    // =========================================================================
    // 5. METHOD OVERLOADING (COMPILE-TIME POLYMORPHISM) & BUSINESS LOGIC
    // =========================================================================
    /**
     * Overload 1: Persetujuan KRS standar tanpa catatan khusus.
     */
    public void validasiDanSetujuiKrs(KRS krs) {
        validasiDanSetujuiKrs(krs, "Disetujui tanpa catatan khusus.");
    }

    /**
     * Overload 2: Persetujuan KRS disertai catatan bimbingan akademik (Overloading).
     */
    public void validasiDanSetujuiKrs(KRS krs, String catatanWali) {
        if (krs == null) {
            System.out.println("[VALIDASI DOSEN] Gagal: Dokumen KRS bernilai null!");
            return;
        }

        System.out.println("\n>> [PROSES BIMBINGAN] Dosen Wali (" + super.getNama() + ") memeriksa KRS: " + krs.getNomorKrs());
        if (krs.getJumlahMk() == 0) {
            System.out.println("   [PENOLAKAN] KRS belum memiliki mata kuliah yang terdaftar.");
        } else {
            System.out.println("   [DISETUJUI] KRS valid (" + krs.getJumlahMk() + " MK terdaftar, Total " + 
                               krs.hitungTotalSksKrs() + " SKS). Catatan Wali: \"" + catatanWali + "\"");
            krs.setujuiKrs(this);
        }
    }

    /**
     * Menampilkan profil lengkap dosen.
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
