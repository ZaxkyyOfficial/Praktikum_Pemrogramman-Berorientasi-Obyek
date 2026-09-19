/**
 * Class Mahasiswa merepresentasikan entitas mahasiswa dalam Sistem Informasi Akademik (SIAKAD).
 * Memiliki attribute untuk menyimpan data identitas dan akademik mahasiswa,
 * constructor untuk inisialisasi state awal objek, serta method untuk manipulasi dan kalkulasi data.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 2
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 */
public class Mahasiswa {
    // ==========================================
    // 1. ATTRIBUTES (STATE / KARAKTERISTIK DATA)
    // ==========================================
    public String nrp;
    public String nama;
    public String prodi;
    public int semester;
    public double ipk;
    public int totalSks;

    // ==========================================
    // 2. CONSTRUCTOR (INISIALISASI AWAL OBJEK)
    // ==========================================
    /**
     * Constructor untuk membuat objek Mahasiswa dengan inisialisasi data pokok.
     * Menggunakan kata kunci 'this' untuk membedakan atribut kelas dan parameter.
     */
    public Mahasiswa(String nrp, String nama, String prodi, int semester, double ipk) {
        this.nrp = nrp;
        this.nama = nama;
        this.prodi = prodi;
        this.semester = semester;
        this.ipk = ipk;
        this.totalSks = 0; // Total SKS awal diset 0 sebelum pengisian KRS
    }

    // ==========================================
    // 3. METHODS (BEHAVIOR / PERILAKU OBJEK)
    // ==========================================

    /**
     * Method tanpa parameter (void)
     * Menampilkan profil lengkap dan status akademik mahasiswa ke terminal.
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

    /**
     * Method dengan parameter (void)
     * Memperbarui capaian IPK mahasiswa dengan validasi nilai (0.00 - 4.00).
     */
    public void updateIpk(double ipkBaru) {
        if (ipkBaru >= 0.0 && ipkBaru <= 4.0) {
            double ipkLama = this.ipk;
            this.ipk = ipkBaru;
            System.out.printf("[INFO MAHASISWA] IPK mahasiswa %s (%s) diperbarui: %.2f -> %.2f\n", 
                              this.nama, this.nrp, ipkLama, this.ipk);
        } else {
            System.out.println("[PERINGATAN] Nilai IPK tidak valid! Harus berada di rentang 0.00 s/d 4.00.");
        }
    }

    /**
     * Method tanpa parameter dengan Return Value (int)
     * Menghitung kuota batas beban SKS semester aktif berdasarkan regulasi akademik dan IPK.
     * Aturan:
     * - IPK >= 3.00 : 24 SKS
     * - IPK >= 2.50 : 21 SKS
     * - IPK >= 2.00 : 18 SKS
     * - IPK < 2.00  : 15 SKS
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
     * Method dengan parameter dan Return Value (boolean)
     * Menambahkan beban SKS mahasiswa apabila tidak melampaui batas maksimal.
     */
    public boolean tambahSks(int sks) {
        int batasMaks = this.hitungBebanMaksimalSks();
        if (this.totalSks + sks <= batasMaks) {
            this.totalSks += sks;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method tanpa parameter (void)
     * Mereset akumulasi SKS mahasiswa (misal saat batal pengajuan KRS).
     */
    public void resetSks() {
        this.totalSks = 0;
        System.out.println("[INFO MAHASISWA] Total SKS untuk " + this.nama + " telah direset menjadi 0.");
    }
}
