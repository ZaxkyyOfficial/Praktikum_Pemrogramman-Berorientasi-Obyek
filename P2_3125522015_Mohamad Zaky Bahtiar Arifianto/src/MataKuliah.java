/**
 * Class MataKuliah merepresentasikan entitas mata kuliah yang ditawarkan dalam kurikulum akademik.
 * Memiliki attribute kode, nama mata kuliah, bobot SKS, dosen pengampu, kuota kelas, serta jumlah peserta.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 2
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 */
public class MataKuliah {
    // ==========================================
    // 1. ATTRIBUTES (STATE / KARAKTERISTIK DATA)
    // ==========================================
    public String kodeMk;
    public String namaMk;
    public int sks;
    public int semesterBuka;
    public String dosenPengampu;
    public int kuotaKelas;
    public int pesertaTerdaftar;

    // ==========================================
    // 2. CONSTRUCTOR (INISIALISASI AWAL OBJEK)
    // ==========================================
    /**
     * Constructor untuk inisialisasi master data MataKuliah.
     * Menggunakan kata kunci 'this' untuk mapping parameter ke atribut instance.
     */
    public MataKuliah(String kodeMk, String namaMk, int sks, int semesterBuka, String dosenPengampu, int kuotaKelas) {
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.sks = sks;
        this.semesterBuka = semesterBuka;
        this.dosenPengampu = dosenPengampu;
        this.kuotaKelas = kuotaKelas;
        this.pesertaTerdaftar = 0; // Inisialisasi awal 0 peserta saat mata kuliah dibuka
    }

    // ==========================================
    // 3. METHODS (BEHAVIOR / PERILAKU OBJEK)
    // ==========================================

    /**
     * Method tanpa parameter (void)
     * Menampilkan informasi rincian mata kuliah dan kapasitas kelas.
     */
    public void tampilkanDetailMk() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                 INFORMASI MATA KULIAH                      ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Kode MK           : " + this.kodeMk);
        System.out.println("Nama Mata Kuliah  : " + this.namaMk);
        System.out.println("Bobot SKS         : " + this.sks + " SKS");
        System.out.println("Semester Buka     : Semester " + this.semesterBuka);
        System.out.println("Dosen Pengampu    : " + this.dosenPengampu);
        System.out.println("Kapasitas Kelas   : " + this.pesertaTerdaftar + " / " + this.kuotaKelas + " Mahasiswa");
        System.out.println("Sisa Kuota Kursi  : " + this.getSisaKuota() + " Kursi");
        System.out.println("Status Ketersediaan: " + (this.isKelasPenuh() ? "[PENUH]" : "[TERSEDIA]"));
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Method dengan parameter (void)
     * Mengubah nama dosen pengampu mata kuliah (misal terjadi pergantian SK mengajar).
     */
    public void ubahDosenPengampu(String dosenBaru) {
        String dosenLama = this.dosenPengampu;
        this.dosenPengampu = dosenBaru;
        System.out.printf("[INFO MATA KULIAH] Dosen pengampu %s (%s) diubah: '%s' -> '%s'\n",
                          this.namaMk, this.kodeMk, dosenLama, this.dosenPengampu);
    }

    /**
     * Method tanpa parameter dengan Return Value (boolean)
     * Mendaftarkan 1 peserta baru ke dalam kelas apabila kuota belum penuh.
     */
    public boolean tambahPeserta() {
        if (!this.isKelasPenuh()) {
            this.pesertaTerdaftar++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method tanpa parameter dengan Return Value (int)
     * Mengembalikan jumlah sisa kuota kursi kelas yang masih tersedia.
     */
    public int getSisaKuota() {
        return this.kuotaKelas - this.pesertaTerdaftar;
    }

    /**
     * Method tanpa parameter dengan Return Value (boolean)
     * Mengecek apakah kelas mata kuliah sudah mencapai batas kapasitas maksimum.
     */
    public boolean isKelasPenuh() {
        return this.pesertaTerdaftar >= this.kuotaKelas;
    }
}
