/**
 * Class KRS (Kartu Rencana Studi) merepresentasikan dokumen rencana studi semester mahasiswa.
 * Class ini mengaitkan entitas Mahasiswa dengan kumpulan entitas MataKuliah yang diambil,
 * melakukan validasi kuota batas SKS dan ketersediaan kuota kelas, serta mengelola validasi dosen wali.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 2
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 */
public class KRS {
    // ==========================================
    // 1. ATTRIBUTES (STATE / KARAKTERISTIK DATA)
    // ==========================================
    public String nomorKrs;
    public Mahasiswa mahasiswa;            // Asosiasi/Agregasi ke Objek Mahasiswa
    public String tahunAjaran;
    public int semesterKrs;
    public MataKuliah[] daftarMataKuliah;  // Koleksi Array Objek MataKuliah
    public int jumlahMk;
    public boolean statusValidasi;
    public String dosenWaliPengesah;

    // ==========================================
    // 2. CONSTRUCTOR (INISIALISASI AWAL OBJEK)
    // ==========================================
    /**
     * Constructor untuk inisialisasi dokumen KRS.
     * Mengikat objek Mahasiswa pemilik dan mengalokasikan array penampung MataKuliah.
     */
    public KRS(String nomorKrs, Mahasiswa mahasiswa, String tahunAjaran, int semesterKrs, int kapasitasMaksMk) {
        this.nomorKrs = nomorKrs;
        this.mahasiswa = mahasiswa;
        this.tahunAjaran = tahunAjaran;
        this.semesterKrs = semesterKrs;
        this.daftarMataKuliah = new MataKuliah[kapasitasMaksMk];
        this.jumlahMk = 0;
        this.statusValidasi = false; // Status awal belum disetujui (pending validasi dosen wali)
        this.dosenWaliPengesah = "-";
    }

    // ==========================================
    // 3. METHODS (BEHAVIOR / PERILAKU OBJEK)
    // ==========================================

    /**
     * Method dengan parameter (MataKuliah) dan Return Value (boolean)
     * Menambahkan mata kuliah ke dalam kartu rencana studi mahasiswa dengan validasi:
     * 1. Apakah kuota array KRS masih mencukupi.
     * 2. Apakah mata kuliah yang bersangkutan masih memiliki sisa kuota kursi.
     * 3. Apakah akumulasi SKS tidak melampaui batas maksimal SKS mahasiswa berdasarkan IPK.
     */
    public boolean tambahMataKuliah(MataKuliah mk) {
        System.out.println(">> [PERMOHONAN KRS] Mahasiswa " + this.mahasiswa.nama + 
                           " mendaftar MK: " + mk.namaMk + " (" + mk.sks + " SKS)...");

        // Validasi 1: Kapasitas tampung array internal KRS
        if (this.jumlahMk >= this.daftarMataKuliah.length) {
            System.out.println("   [GAGAL] Kapasitas maksimum item kartu rencana studi penuh!");
            return false;
        }

        // Validasi 2: Ketersediaan kuota kelas mata kuliah
        if (mk.isKelasPenuh()) {
            System.out.println("   [GAGAL] Kelas mata kuliah '" + mk.namaMk + "' sudah penuh (" + 
                               mk.pesertaTerdaftar + "/" + mk.kuotaKelas + " Kursi)!");
            return false;
        }

        // Validasi 3: Batas maksimal beban SKS mahasiswa
        int batasSks = this.mahasiswa.hitungBebanMaksimalSks();
        int totalSksSekarang = this.hitungTotalSksKrs();
        if (totalSksSekarang + mk.sks > batasSks) {
            System.out.println("   [GAGAL] Melebihi batas kuota SKS mahasiswa (" + batasSks + " SKS)! " +
                               "Total saat ini: " + totalSksSekarang + " SKS + MK: " + mk.sks + " SKS.");
            return false;
        }

        // Jika seluruh validasi lolos:
        this.daftarMataKuliah[this.jumlahMk] = mk;
        this.jumlahMk++;
        mk.tambahPeserta();               // Tambah counter peserta pada objek MataKuliah
        this.mahasiswa.tambahSks(mk.sks); // Update total SKS pada objek Mahasiswa

        System.out.println("   [BERHASIL] MK '" + mk.namaMk + "' berhasil ditambahkan ke KRS " + this.nomorKrs);
        System.out.println("   Beban SKS saat ini: " + this.hitungTotalSksKrs() + " / " + batasSks + " SKS");
        return true;
    }

    /**
     * Method dengan parameter (void)
     * Mengesahkan/memvalidasi KRS oleh Dosen Wali pembimbing akademik.
     */
    public void setujuiKrs(String namaDosenWali) {
        if (this.jumlahMk == 0) {
            System.out.println("[PERINGATAN] KRS " + this.nomorKrs + " belum memiliki mata kuliah, tidak dapat disahkan.");
            return;
        }
        this.statusValidasi = true;
        this.dosenWaliPengesah = namaDosenWali;
        System.out.println("[VALIDASI KRS] Dokumen KRS " + this.nomorKrs + " milik " + this.mahasiswa.nama + 
                           " berhasil DISETUJUI & DISAHKAN oleh Dosen Wali: " + this.dosenWaliPengesah);
    }

    /**
     * Method tanpa parameter dengan Return Value (int)
     * Menghitung total akumulasi SKS dari seluruh mata kuliah yang telah didaftarkan.
     */
    public int hitungTotalSksKrs() {
        int total = 0;
        for (int i = 0; i < this.jumlahMk; i++) {
            total += this.daftarMataKuliah[i].sks;
        }
        return total;
    }

    /**
     * Method tanpa parameter dengan Return Value (boolean)
     * Memeriksa status persetujuan KRS oleh dosen wali.
     */
    public boolean isDisetujui() {
        return this.statusValidasi;
    }

    /**
     * Method tanpa parameter (void)
     * Menampilkan lembar Kartu Rencana Studi (KRS) secara terformat dan rapi.
     */
    public void tampilkanKrs() {
        System.out.println("==========================================================================");
        System.out.println("                     KARTU RENCANA STUDI (KRS)                            ");
        System.out.println("               POLITEKNIK ELEKTRONIKA NEGERI SURABAYA                     ");
        System.out.println("==========================================================================");
        System.out.println("Nomor Registrasi KRS : " + this.nomorKrs);
        System.out.println("Tahun Ajaran / Sem.  : " + this.tahunAjaran + " (Semester " + this.semesterKrs + ")");
        System.out.println("Mahasiswa (NRP/Nama) : " + this.mahasiswa.nrp + " - " + this.mahasiswa.nama);
        System.out.println("Program Studi        : " + this.mahasiswa.prodi);
        System.out.printf("Capaian IPK Lalu     : %.2f (Batas Maksimal Beban: %d SKS)\n", 
                          this.mahasiswa.ipk, this.mahasiswa.hitungBebanMaksimalSks());
        System.out.println("Status Pengesahan    : " + (this.statusValidasi ? "DISETUJUI (VALID)" : "PENDING (BELUM DISETUJUI)"));
        System.out.println("Dosen Wali Pengesah  : " + this.dosenWaliPengesah);
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-4s | %-10s | %-30s | %-5s | %-20s\n", "NO", "KODE", "MATA KULIAH", "SKS", "DOSEN PENGAMPU");
        System.out.println("--------------------------------------------------------------------------");
        if (this.jumlahMk == 0) {
            System.out.println("                  [ Belum ada mata kuliah yang diambil ]                  ");
        } else {
            for (int i = 0; i < this.jumlahMk; i++) {
                MataKuliah mk = this.daftarMataKuliah[i];
                System.out.printf("%-4d | %-10s | %-30s | %-5d | %-20s\n", 
                                  (i + 1), mk.kodeMk, mk.namaMk, mk.sks, mk.dosenPengampu);
            }
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("TOTAL SKS TERDAFTAR : %d SKS\n", this.hitungTotalSksKrs());
        System.out.println("==========================================================================\n");
    }
}
