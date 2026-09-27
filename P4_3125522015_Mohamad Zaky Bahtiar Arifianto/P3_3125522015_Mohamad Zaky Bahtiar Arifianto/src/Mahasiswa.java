/**
 * Class Mahasiswa merepresentasikan entitas mahasiswa dalam Sistem Informasi Akademik (SIAKAD).
 * Menerapkan prinsip Encapsulation penuh dengan access modifier private pada semua atribut,
 * menyediakan getter dan setter selektif dengan validasi data ketat, serta constructor tervalidasi.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 3
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
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

    // =========================================================================
    // 2. CONSTRUCTOR (TERVALIDASI)
    // =========================================================================
    /**
     * Constructor tervalidasi: inisialisasi data pokok melalui setter
     * untuk memastikan tidak ada input data yang melanggar aturan bisnis sejak awal.
     */
    public Mahasiswa(String nrp, String nama, String prodi, int semester, double ipk) {
        setNrpInternal(nrp);
        setNama(nama);
        setProdi(prodi);
        setSemester(semester);
        setIpk(ipk);
        this.totalSks = 0;
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

    // =========================================================================
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS)
    // =========================================================================
    /**
     * Validasi internal NRP saat instansiasi (NRP bersifat unik dan read-only setelah dibuat).
     */
    private void setNrpInternal(String nrp) {
        if (nrp != null && !nrp.trim().isEmpty()) {
            this.nrp = nrp.trim();
        } else {
            this.nrp = "0000000000";
            System.out.println("[VALIDASI ERROR] NRP tidak boleh kosong/null! Diset ke default.");
        }
    }

    /**
     * Validasi Nama: tidak boleh null, tidak boleh kosong/whitespace, minimal 3 karakter.
     */
    public void setNama(String nama) {
        if (nama != null && nama.trim().length() >= 3) {
            this.nama = nama.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama mahasiswa tidak valid! Minimal 3 karakter dan tidak boleh kosong.");
        }
    }

    /**
     * Validasi Program Studi: tidak boleh null atau kosong.
     */
    public void setProdi(String prodi) {
        if (prodi != null && !prodi.trim().isEmpty()) {
            this.prodi = prodi.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Program studi tidak valid! Nilai tidak boleh kosong.");
        }
    }

    /**
     * Validasi Semester: harus berada pada rentang batas akademik perkuliahan (1 s/d 14).
     */
    public void setSemester(int semester) {
        if (semester >= 1 && semester <= 14) {
            this.semester = semester;
        } else {
            System.out.println("[VALIDASI DITOLAK] Semester " + semester + " tidak valid! Batas semester adalah 1 s/d 14.");
        }
    }

    /**
     * Validasi Capaian IPK: harus berada pada rentang 0.00 s/d 4.00.
     */
    public void setIpk(double ipk) {
        if (ipk >= 0.0 && ipk <= 4.0) {
            this.ipk = ipk;
        } else {
            System.out.printf("[VALIDASI DITOLAK] Nilai IPK %.2f tidak valid! Harus berada di rentang 0.00 s/d 4.00.\n", ipk);
        }
    }

    // =========================================================================
    // 5. BUSINESS LOGIC METHODS
    // =========================================================================
    /**
     * Menghitung kuota batas beban SKS semester aktif berdasarkan regulasi akademik.
     */
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

    /**
     * Menambahkan akumulasi beban SKS dengan pengecekan batas maksimal.
     */
    public boolean tambahSks(int sks) {
        int batasMaks = this.hitungBebanMaksimalSks();
        if (sks > 0 && (this.totalSks + sks <= batasMaks)) {
            this.totalSks += sks;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Mereset total SKS kembali ke 0.
     */
    public void resetSks() {
        this.totalSks = 0;
        System.out.println("[INFO MAHASISWA] Total SKS untuk " + this.nama + " direset ke 0.");
    }

    /**
     * Menampilkan profil lengkap mahasiswa.
     */
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
        System.out.println("Status KRS        : " + (this.totalSks > 0 ? "Aktif Terisi" : "Belum Mengisi"));
        System.out.println("------------------------------------------------------------");
    }
}
