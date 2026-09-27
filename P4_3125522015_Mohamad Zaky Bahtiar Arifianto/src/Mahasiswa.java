/**
 * Class Mahasiswa merepresentasikan entitas mahasiswa dalam Sistem Informasi Akademik (SIAKAD).
 * Menerapkan prinsip Encapsulation penuh dengan access modifier private pada seluruh atribut.
 * 
 * Memiliki relasi:
 * 1. COMPOSITION : Memiliki objek KRS yang dibuat secara internal di dalam konstruktor.
 *                  Siklus hidup dokumen KRS terikat mutlak pada keberadaan objek Mahasiswa.
 * 2. ASSOCIATION : Memiliki referensi ke objek Dosen sebagai Dosen Wali untuk konsultasi akademik.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 4 (Relasi Antarobject)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Mahasiswa {
    // =========================================================================
    // 1. ATTRIBUTES (PRIVATE - ENCAPSULATION & INFORMATION HIDING)
    // =========================================================================
    private String nrp;
    private String nama;
    private String prodi;
    private int semester;
    private double ipk;
    private int totalSks;
    private Dosen dosenWali; // ASOSIASI: Referensi mandiri ke Dosen Wali
    private KRS krs;         // KOMPOSISI: Bagian internal yang diciptakan langsung di dalam Mahasiswa

    // =========================================================================
    // 2. CONSTRUCTOR (TERVALIDASI & OVERLOADED)
    // =========================================================================
    /**
     * Constructor Utama Modul 4: Melakukan validasi data diri, mengaitkan Dosen Wali (Asosiasi),
     * dan mengonstruksi dokumen KRS secara langsung di dalam objek Mahasiswa (Komposisi).
     */
    public Mahasiswa(String nrp, String nama, String prodi, int semester, double ipk, Dosen dosenWali) {
        setNrpInternal(nrp);
        setNama(nama);
        setProdi(prodi);
        setSemester(semester);
        setIpk(ipk);
        setDosenWali(dosenWali);
        this.totalSks = 0;

        // =====================================================================
        // IMPLEMENTASI KOMPOSISI (COMPOSITION):
        // Objek KRS dibuat langsung di dalam class Mahasiswa (part-of).
        // Objek KRS tidak dapat berdiri sendiri tanpa Mahasiswa pemiliknya.
        // =====================================================================
        this.krs = new KRS("KRS-" + this.nrp, this, "2026/2027 Ganjil", this.semester, 8);
    }

    /**
     * Constructor Overload: Kompatibel dengan pemanggilan konstruktor 5 parameter.
     */
    public Mahasiswa(String nrp, String nama, String prodi, int semester, double ipk) {
        this(nrp, nama, prodi, semester, ipk, null);
    }

    // =========================================================================
    // 3. GETTER (ACCESSOR METHODS)
    // =========================================================================
    public String getNrp() {
        return this.nrp;
    }

    public String getNama() {
        return this.nama;
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
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS & OVERLOADS)
    // =========================================================================
    private void setNrpInternal(String nrp) {
        if (nrp != null && !nrp.trim().isEmpty()) {
            this.nrp = nrp.trim();
        } else {
            this.nrp = "0000000000";
            System.out.println("[VALIDASI ERROR] NRP tidak boleh kosong/null! Diset ke default.");
        }
    }

    public final void setNama(String nama) {
        if (nama != null && nama.trim().length() >= 3) {
            this.nama = nama.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama mahasiswa tidak valid! Minimal 3 karakter dan tidak boleh kosong.");
        }
    }

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

    /**
     * Mutator Asosiasi: Menetapkan atau memperbarui Dosen Wali pembimbing mahasiswa.
     */
    public final void setDosenWali(Dosen dosenWali) {
        this.dosenWali = dosenWali;
    }

    public final void setDosenWali(String namaDosenWali) {
        if (namaDosenWali != null && namaDosenWali.trim().length() >= 3) {
            this.dosenWali = new Dosen("190000000000000000", namaDosenWali.trim(), "Dosen Wali", "dosen@pens.ac.id");
        }
    }

    // =========================================================================
    // 5. BUSINESS LOGIC METHODS (INTERAKSI ANTAR-OBJEK)
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
        System.out.println("[INFO MAHASISWA] Total SKS untuk " + this.nama + " direset ke 0.");
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
        System.out.println("\n>> [PENGAJUAN KRS] Mahasiswa " + this.nama + " (" + this.nrp + 
                           ") mengajukan persetujuan KRS ke Dosen Wali: " + this.dosenWali.getNama());
        this.dosenWali.validasiDanSetujuiKrs(this.krs);
    }

    public void tampilkanProfil() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                 PROFIL AKADEMIK MAHASISWA                  ");
        System.out.println("------------------------------------------------------------");
        System.out.println("NRP               : " + this.nrp);
        System.out.println("Nama Lengkap      : " + this.nama);
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
