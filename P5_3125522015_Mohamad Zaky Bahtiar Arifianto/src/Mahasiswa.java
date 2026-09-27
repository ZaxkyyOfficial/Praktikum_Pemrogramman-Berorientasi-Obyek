/**
 * Subclass Mahasiswa merepresentasikan entitas mahasiswa dalam Sistem Informasi Akademik (SIAKAD).
 * 
 * Menerapkan prinsip Inheritance dengan meng-extends superclass CivitasAkademika.
 * Menggunakan keyword super(...) pada konstruktor (constructor chaining) untuk menginisialisasi
 * atribut umum (nomorInduk/NRP, nama, email, jenisCivitas), serta menambahkan atribut spesifik
 * perkuliahan (prodi, semester, ipk, totalSks).
 * 
 * Tetap mempertahankan relasi objek P4:
 * 1. COMPOSITION : Memiliki objek KRS yang dibuat internal di dalam konstruktor.
 * 2. ASSOCIATION : Memiliki referensi ke objek Dosen sebagai Dosen Wali pembimbing.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 5 (Inheritance & Generalization)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Mahasiswa extends CivitasAkademika {
    // =========================================================================
    // 1. ATTRIBUTES KHUSUS MAHASISWA (PRIVATE - ENCAPSULATION & INFORMATION HIDING)
    // Atribut nomorInduk (NRP), nama, dan email telah dipindahkan ke superclass CivitasAkademika.
    // =========================================================================
    private String prodi;
    private int semester;
    private double ipk;
    private int totalSks;
    private Dosen dosenWali; // ASOSIASI: Referensi ke Dosen Wali
    private KRS krs;         // KOMPOSISI: Bagian internal yang diciptakan langsung di dalam Mahasiswa

    // =========================================================================
    // 2. CONSTRUCTOR (IMPLEMENTASI CONSTRUCTOR CHAINING DENGAN super)
    // =========================================================================
    /**
     * Constructor Utama Modul 5:
     * Menggunakan super(...) untuk meneruskan data umum ke constructor CivitasAkademika.
     */
    public Mahasiswa(String nrp, String nama, String email, String prodi, int semester, double ipk, Dosen dosenWali) {
        super(nrp, nama, email, "Mahasiswa"); // Constructor Chaining ke Superclass
        setProdi(prodi);
        setSemester(semester);
        setIpk(ipk);
        setDosenWali(dosenWali);
        this.totalSks = 0;

        // =====================================================================
        // MEMPERTAHANKAN KOMPOSISI DARI P4:
        // Objek KRS dibuat langsung di dalam constructor Mahasiswa (part-of).
        // Menggunakan super.getNomorInduk() untuk penamaan nomor KRS.
        // =====================================================================
        this.krs = new KRS("KRS-" + super.getNomorInduk(), this, "2026/2027 Ganjil", this.semester, 8);
    }

    /**
     * Constructor Overload (Kompatibilitas P4):
     * Otomatis mengonstruksi email resmi mahasiswa jika email tidak disertakan.
     */
    public Mahasiswa(String nrp, String nama, String prodi, int semester, double ipk, Dosen dosenWali) {
        this(nrp, nama, nrp + "@student.pens.ac.id", prodi, semester, ipk, dosenWali);
    }

    /**
     * Constructor Overload 5 parameter (Kompatibilitas P3/P4).
     */
    public Mahasiswa(String nrp, String nama, String prodi, int semester, double ipk) {
        this(nrp, nama, prodi, semester, ipk, null);
    }

    // =========================================================================
    // 3. GETTER (ACCESSOR METHODS KHUSUS & DELEGASI)
    // =========================================================================
    /**
     * Getter NRP: Mendelegasikan pembacaan ke getNomorInduk() milik superclass
     * sehingga mematuhi enkapsulasi tanpa mengakses field private superclass secara langsung.
     */
    public String getNrp() {
        return super.getNomorInduk();
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
    // 5. BUSINESS LOGIC METHODS (INTERAKSI OBJEK & METODE KHUSUS)
    // =========================================================================
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

    public boolean pilihMataKuliah(MataKuliah mk) {
        if (this.krs != null) {
            return this.krs.tambahMataKuliah(mk);
        } else {
            System.out.println("[ERROR] Dokumen KRS mahasiswa belum terbentuk!");
            return false;
        }
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

    /**
     * Menampilkan profil mahasiswa secara komprehensif dengan memadukan data superclass dan subclass.
     */
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
        System.out.println("------------------------------------------------------------");
    }
}
