/**
 * Subclass Dosen merepresentasikan entitas tenaga pengajar dan pembimbing akademik (Dosen Wali)
 * dalam Sistem Informasi Akademik (SIAKAD).
 * 
 * Modul 7:
 * 1. Mewarisi Abstract Class CivitasAkademika dan mengimplementasikan abstract method tampilkanPeran().
 * 2. Mengimplementasikan Interface DapatDiotentikasi sebagai kontrak login dan otorisasi portal pengajar/wali.
 * 3. Mempertahankan Enkapsulasi, Relasi P4 (Asosiasi pengampu mata kuliah & pembimbing KRS), dan Method Overloading P6.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 7 (Abstract Class & Interface)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Dosen extends CivitasAkademika implements DapatDiotentikasi {
    // =========================================================================
    // 1. ATTRIBUTES KHUSUS DOSEN (PRIVATE - ENCAPSULATION)
    // =========================================================================
    private String bidangKeahlian;
    private boolean loginAktif;        // IMPLEMENTASI INTERFACE: Status autentikasi portal
    private String passwordPortal;     // IMPLEMENTASI INTERFACE: Kredensial kata sandi akun

    // =========================================================================
    // 2. CONSTRUCTOR (CONSTRUCTOR CHAINING DENGAN super)
    // =========================================================================
    public Dosen(String nip, String nama, String bidangKeahlian, String email) {
        super(nip, nama, email, "Dosen"); // Constructor Chaining ke Abstract Superclass
        setBidangKeahlian(bidangKeahlian);
        this.loginAktif = false;
        this.passwordPortal = "dsn" + super.getNomorInduk(); // Password default berbasis NIP
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
    // 4. IMPLEMENTASI ABSTRACT METHOD (DARI ABSTRACT SUPERCLASS CivitasAkademika)
    // =========================================================================
    /**
     * Mengimplementasikan abstract method tampilkanPeran() dari CivitasAkademika.
     * Memberikan perilaku spesifik tenaga pengajar dan dosen wali.
     */
    @Override
    public void tampilkanPeran() {
        System.out.println("[PERAN DOSEN] " + super.getNama() + " (" + getNip() + 
                           ") - Menjalankan Tridharma Perguruan Tinggi, membimbing mahasiswa, " + 
                           "mengajar bidang " + this.bidangKeahlian + ", dan memvalidasi KRS.");
    }

    // =========================================================================
    // 5. IMPLEMENTASI KONTRAK INTERFACE DapatDiotentikasi
    // =========================================================================
    @Override
    public boolean login(String nomorInduk, String kataSandi) {
        if (super.getNomorInduk().equals(nomorInduk) && this.passwordPortal.equals(kataSandi)) {
            this.loginAktif = true;
            System.out.println(">> [LOGIN BERHASIL] Dosen " + super.getNama() + " (" + getNip() + ") berhasil masuk ke Portal SIAKAD.");
            return true;
        } else {
            this.loginAktif = false;
            System.out.println(">> [LOGIN GAGAL] Kredensial tidak valid untuk Dosen " + super.getNama() + ".");
            return false;
        }
    }

    @Override
    public void logout() {
        this.loginAktif = false;
        System.out.println(">> [LOGOUT] Sesi portal Dosen " + super.getNama() + " telah diakhiri secara aman.");
    }

    @Override
    public void tampilkanHakAkses() {
        System.out.println("------------------------------------------------------------");
        System.out.println("          OTORITAS HAK AKSES PORTAL DOSEN (SIAKAD)          ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Pengguna   : " + super.getNama() + " (NIP: " + getNip() + ")");
        System.out.println("Hak Akses  : 1. Pemeriksaan & Pengesahan Dokumen Rencana Studi (KRS)");
        System.out.println("             2. Input & Pembaruan Nilai Capaian Mata Kuliah");
        System.out.println("             3. Monitoring Indeks Prestasi Mahasiswa Bimbingan");
        System.out.println("             4. Pengelolaan Presensi & Berita Acara Perkuliahan");
        System.out.println("------------------------------------------------------------");
    }

    @Override
    public boolean isLoginAktif() {
        return this.loginAktif;
    }

    // =========================================================================
    // 6. METHOD OVERLOADING (COMPILE-TIME POLYMORPHISM) & LOGIKA BISNIS
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

    public void tampilkanProfil() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                   PROFIL DOSEN PENGAMPU                    ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Peran Civitas    : " + super.getJenisCivitas());
        System.out.println("NIP              : " + super.getNomorInduk());
        System.out.println("Nama Lengkap     : " + super.getNama());
        System.out.println("Email Resmi      : " + super.getEmail());
        System.out.println("Bidang Keahlian  : " + this.bidangKeahlian);
        System.out.println("Status Login     : " + (this.loginAktif ? "ONLINE" : "OFFLINE"));
        System.out.println("------------------------------------------------------------");
    }
}
