/**
 * Subclass Mahasiswa merepresentasikan entitas mahasiswa dalam Sistem Informasi Akademik (SIAKAD).
 * 
 * Modul 7:
 * 1. Mewarisi Abstract Class CivitasAkademika dan mengimplementasikan abstract method tampilkanPeran().
 * 2. Mengimplementasikan Interface DapatDiotentikasi sebagai kontrak login dan otorisasi hak akses portal.
 * 3. Mempertahankan Enkapsulasi, Relasi P4 (Asosiasi ke Dosen Wali, Komposisi ke KRS), dan Method Overloading P6.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 7 (Abstract Class & Interface)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Mahasiswa extends CivitasAkademika implements DapatDiotentikasi {
    // =========================================================================
    // 1. ATTRIBUTES KHUSUS MAHASISWA (PRIVATE - ENCAPSULATION & INFORMATION HIDING)
    // =========================================================================
    private String prodi;
    private int semester;
    private double ipk;
    private int totalSks;
    private Dosen dosenWali;           // ASOSIASI: Referensi ke Dosen Wali pembimbing
    private KRS krs;                   // KOMPOSISI: Bagian internal yang diciptakan langsung di dalam Mahasiswa
    private boolean loginAktif;        // IMPLEMENTASI INTERFACE: Status autentikasi portal
    private String passwordPortal;     // IMPLEMENTASI INTERFACE: Kredensial kata sandi akun

    // =========================================================================
    // 2. CONSTRUCTOR (CONSTRUCTOR CHAINING DENGAN super)
    // =========================================================================
    public Mahasiswa(String nrp, String nama, String email, String prodi, int semester, double ipk, Dosen dosenWali) {
        super(nrp, nama, email, "Mahasiswa"); // Constructor Chaining ke Abstract Superclass
        setProdi(prodi);
        setSemester(semester);
        setIpk(ipk);
        setDosenWali(dosenWali);
        this.totalSks = 0;
        this.loginAktif = false;
        this.passwordPortal = "mhs" + super.getNomorInduk(); // Password default berbasis NRP

        // Mempertahankan Komposisi dari P4: KRS terikat utuh pada Mahasiswa
        this.krs = new KRS("KRS-" + super.getNomorInduk(), this, "2026/2027 Ganjil", this.semester, 8);
    }

    public Mahasiswa(String nrp, String nama, String prodi, int semester, double ipk, Dosen dosenWali) {
        this(nrp, nama, nrp + "@student.pens.ac.id", prodi, semester, ipk, dosenWali);
    }

    public Mahasiswa(String nrp, String nama, String prodi, int semester, double ipk) {
        this(nrp, nama, prodi, semester, ipk, null);
    }

    // =========================================================================
    // 3. GETTER (ACCESSOR METHODS KHUSUS & DELEGASI)
    // =========================================================================
    public String getNrp() {
        return super.getNomorInduk(); // Mematuhi enkapsulasi via getter superclass
    }

    public String getProdi() {
        return this.prodi;
    }

    public int getSemester() {
        return this.semester;
    }

    public double getIpk() {
        return this.ipk;
    }

    public int getTotalSks() {
        return this.totalSks;
    }

    public Dosen getDosenWali() {
        return this.dosenWali;
    }

    public KRS getKrs() {
        return this.krs;
    }

    // =========================================================================
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS)
    // =========================================================================
    public final void setProdi(String prodi) {
        if (prodi != null && !prodi.trim().isEmpty()) {
            this.prodi = prodi.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Program studi tidak valid! Nilai tidak boleh kosong.");
        }
    }

    public final void setSemester(int semester) {
        if (semester >= 1 && semester <= 14) {
            this.semester = semester;
        } else {
            System.out.println("[VALIDASI DITOLAK] Semester " + semester + " tidak valid! Batas semester adalah 1 s/d 14.");
        }
    }

    public final void setIpk(double ipk) {
        if (ipk >= 0.0 && ipk <= 4.0) {
            this.ipk = ipk;
        } else {
            System.out.printf("[VALIDASI DITOLAK] Nilai IPK %.2f tidak valid! Harus berada di rentang 0.00 s/d 4.00.\n", ipk);
        }
    }

    public final void setDosenWali(Dosen dosenWali) {
        this.dosenWali = dosenWali;
    }

    public final void setDosenWali(String namaDosenWali) {
        if (namaDosenWali != null && namaDosenWali.trim().length() >= 3) {
            this.dosenWali = new Dosen("190000000000000000", namaDosenWali.trim(), "Dosen Wali", "dosen@pens.ac.id");
        }
    }

    // =========================================================================
    // 5. IMPLEMENTASI ABSTRACT METHOD (DARI ABSTRACT SUPERCLASS CivitasAkademika)
    // =========================================================================
    /**
     * Mengimplementasikan abstract method tampilkanPeran() dari CivitasAkademika.
     * Memberikan perilaku spesifik mahasiswa penempuh studi.
     */
    @Override
    public void tampilkanPeran() {
        System.out.println("[PERAN MAHASISWA] " + super.getNama() + " (" + getNrp() + 
                           ") - Menempuh studi pada program " + this.prodi + " (Semester " + this.semester + 
                           "), merencanakan beban SKS, dan melaksanakan pembelajaran akademik.");
    }

    // =========================================================================
    // 6. IMPLEMENTASI KONTRAK INTERFACE DapatDiotentikasi
    // =========================================================================
    @Override
    public boolean login(String nomorInduk, String kataSandi) {
        if (super.getNomorInduk().equals(nomorInduk) && this.passwordPortal.equals(kataSandi)) {
            this.loginAktif = true;
            System.out.println(">> [LOGIN BERHASIL] Mahasiswa " + super.getNama() + " (" + getNrp() + ") berhasil masuk ke Portal SIAKAD.");
            return true;
        } else {
            this.loginAktif = false;
            System.out.println(">> [LOGIN GAGAL] Kredensial tidak valid untuk Mahasiswa " + super.getNama() + ".");
            return false;
        }
    }

    @Override
    public void logout() {
        this.loginAktif = false;
        System.out.println(">> [LOGOUT] Sesi portal Mahasiswa " + super.getNama() + " telah diakhiri secara aman.");
    }

    @Override
    public void tampilkanHakAkses() {
        System.out.println("------------------------------------------------------------");
        System.out.println("        OTORITAS HAK AKSES PORTAL MAHASISWA (SIAKAD)        ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Pengguna   : " + super.getNama() + " (NRP: " + getNrp() + ")");
        System.out.println("Hak Akses  : 1. Registrasi & Pemilihan Mata Kuliah (KRS)");
        System.out.println("             2. Monitoring Kartu Hasil Studi (KHS) & IPK");
        System.out.println("             3. Pengajuan Bimbingan Persetujuan ke Dosen Wali");
        System.out.println("             4. Pengunduhan Dokumen Rencana Studi Resmi");
        System.out.println("------------------------------------------------------------");
    }

    @Override
    public boolean isLoginAktif() {
        return this.loginAktif;
    }

    // =========================================================================
    // 7. METHOD OVERLOADING (COMPILE-TIME POLYMORPHISM) & LOGIKA BISNIS
    // =========================================================================
    /**
     * Overload 1: Mendaftarkan mata kuliah reguler standar.
     */
    public boolean pilihMataKuliah(MataKuliah mk) {
        return pilihMataKuliah(mk, "Reguler");
    }

    /**
     * Overload 2: Mendaftarkan mata kuliah dengan kategori khusus (Overloading).
     */
    public boolean pilihMataKuliah(MataKuliah mk, String kategoriAmbil) {
        if (this.krs != null) {
            System.out.println(">> [REGISTRASI MATA KULIAH] Mahasiswa " + super.getNama() + 
                               " mendaftar mata kuliah kategori: [" + kategoriAmbil + "]");
            return this.krs.tambahMataKuliah(mk);
        } else {
            System.out.println("[ERROR] Dokumen KRS mahasiswa belum terbentuk!");
            return false;
        }
    }

    public int hitungBebanMaksimalSks() {
        if (this.ipk >= 3.00) {
            return 24;
        } else if (this.ipk >= 2.50) {
            return 21;
        } else if (this.ipk >= 2.00) {
            return 18;
        } else {
            return 15;
        }
    }

    public boolean tambahSks(int sks) {
        int batasMaks = this.hitungBebanMaksimalSks();
        if (sks > 0 && (this.totalSks + sks <= batasMaks)) {
            this.totalSks += sks;
            return true;
        } else {
            return false;
        }
    }

    public void resetSks() {
        this.totalSks = 0;
        System.out.println("[INFO MAHASISWA] Total SKS untuk " + super.getNama() + " direset ke 0.");
    }

    public void ajukanPersetujuanKrs() {
        if (this.dosenWali == null) {
            System.out.println("[ERROR] Mahasiswa belum memiliki Dosen Wali pembimbing!");
            return;
        }
        System.out.println("\n>> [PENGAJUAN KRS] Mahasiswa " + super.getNama() + " (" + super.getNomorInduk() + 
                           ") mengajukan persetujuan KRS ke Dosen Wali: " + this.dosenWali.getNama());
        this.dosenWali.validasiDanSetujuiKrs(this.krs);
    }

    public void tampilkanProfil() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                 PROFIL AKADEMIK MAHASISWA                  ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Peran Civitas     : " + super.getJenisCivitas());
        System.out.println("NRP               : " + super.getNomorInduk());
        System.out.println("Nama Lengkap      : " + super.getNama());
        System.out.println("Email Resmi       : " + super.getEmail());
        System.out.println("Program Studi     : " + this.prodi);
        System.out.println("Semester          : " + this.semester);
        System.out.printf("IPK Kumulatif     : %.2f\n", this.ipk);
        System.out.println("Batas Maksimal SKS: " + this.hitungBebanMaksimalSks() + " SKS");
        System.out.println("Total SKS Diambil : " + this.totalSks + " SKS");
        System.out.println("Dosen Wali        : " + (this.dosenWali != null ? this.dosenWali.getNama() : "-"));
        System.out.println("No. Registrasi KRS: " + (this.krs != null ? this.krs.getNomorKrs() : "-"));
        System.out.println("Status KRS        : " + (this.krs != null && this.krs.isDisetujui() ? "DISETUJUI RESMI" : "PENDING"));
        System.out.println("Status Login      : " + (this.loginAktif ? "ONLINE" : "OFFLINE"));
        System.out.println("------------------------------------------------------------");
    }
}
