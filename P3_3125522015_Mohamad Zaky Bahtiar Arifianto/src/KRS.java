/**
 * Class KRS (Kartu Rencana Studi) merepresentasikan dokumen rencana studi semester mahasiswa.
 * Menerapkan prinsip Encapsulation dengan membungkus agregasi Mahasiswa dan array MataKuliah,
 * menjamin validasi aturan beban SKS, ketersediaan kuota kelas, serta persetujuan resmi Dosen Wali.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 3
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class KRS {
    // =========================================================================
    // 1. ATTRIBUTES (PRIVATE - ENCAPSULATION)
    // =========================================================================
    private String nomorKrs;
    private Mahasiswa mahasiswa;
    private String tahunAjaran;
    private int semesterKrs;
    private MataKuliah[] daftarMataKuliah;
    private int jumlahMk;
    private boolean statusValidasi;
    private String dosenWaliPengesah;

    // =========================================================================
    // 2. CONSTRUCTOR (TERVALIDASI)
    // =========================================================================
    public KRS(String nomorKrs, Mahasiswa mahasiswa, String tahunAjaran, int semesterKrs, int kapasitasMaksMk) {
        if (nomorKrs != null && !nomorKrs.trim().isEmpty()) {
            this.nomorKrs = nomorKrs.trim();
        } else {
            this.nomorKrs = "KRS-DEFAULT";
        }

        if (mahasiswa != null) {
            this.mahasiswa = mahasiswa;
        } else {
            System.out.println("[FATAL ERROR] Objek mahasiswa tidak boleh null saat inisialisasi KRS!");
        }

        setTahunAjaran(tahunAjaran);
        setSemesterKrs(semesterKrs);

        int kapasitas = kapasitasMaksMk > 0 ? kapasitasMaksMk : 10;
        this.daftarMataKuliah = new MataKuliah[kapasitas];
        this.jumlahMk = 0;
        this.statusValidasi = false;
        this.dosenWaliPengesah = "-";
    }

    // =========================================================================
    // 3. GETTER (ACCESSOR METHODS)
    // =========================================================================
    public String getNomorKrs() {
        return this.nomorKrs;
    }

    public Mahasiswa getMahasiswa() {
        return this.mahasiswa;
    }

    public String getTahunAjaran() {
        return this.tahunAjaran;
    }

    public int getSemesterKrs() {
        return this.semesterKrs;
    }

    public MataKuliah[] getDaftarMataKuliah() {
        return this.daftarMataKuliah;
    }

    public int getJumlahMk() {
        return this.jumlahMk;
    }

    public boolean isDisetujui() {
        return this.statusValidasi;
    }

    public String getDosenWaliPengesah() {
        return this.dosenWaliPengesah;
    }

    // =========================================================================
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS)
    // =========================================================================
    public void setTahunAjaran(String tahunAjaran) {
        if (tahunAjaran != null && !tahunAjaran.trim().isEmpty()) {
            this.tahunAjaran = tahunAjaran.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Tahun ajaran tidak boleh kosong!");
        }
    }

    public void setSemesterKrs(int semesterKrs) {
        if (semesterKrs >= 1 && semesterKrs <= 14) {
            this.semesterKrs = semesterKrs;
        } else {
            System.out.println("[VALIDASI DITOLAK] Semester KRS tidak valid (harus 1 s/d 14)!");
        }
    }

    // =========================================================================
    // 5. BUSINESS LOGIC METHODS
    // =========================================================================
    public boolean tambahMataKuliah(MataKuliah mk) {
        if (mk == null) {
            System.out.println("   [GAGAL] Objek mata kuliah null!");
            return false;
        }

        System.out.println(">> [PERMOHONAN KRS] Mahasiswa " + this.mahasiswa.getNama() + 
                           " mendaftar MK: " + mk.getNamaMk() + " (" + mk.getSks() + " SKS)...");

        if (this.jumlahMk >= this.daftarMataKuliah.length) {
            System.out.println("   [GAGAL] Kapasitas maksimum item kartu rencana studi penuh!");
            return false;
        }

        if (mk.isKelasPenuh()) {
            System.out.println("   [GAGAL] Kelas mata kuliah '" + mk.getNamaMk() + "' sudah penuh (" + 
                               mk.getPesertaTerdaftar() + "/" + mk.getKuotaKelas() + " Kursi)!");
            return false;
        }

        int batasSks = this.mahasiswa.hitungBebanMaksimalSks();
        int totalSksSekarang = this.hitungTotalSksKrs();
        if (totalSksSekarang + mk.getSks() > batasSks) {
            System.out.println("   [GAGAL] Melebihi batas kuota SKS mahasiswa (" + batasSks + " SKS)! " +
                               "Total saat ini: " + totalSksSekarang + " SKS + MK: " + mk.getSks() + " SKS.");
            return false;
        }

        this.daftarMataKuliah[this.jumlahMk] = mk;
        this.jumlahMk++;
        mk.tambahPeserta();
        this.mahasiswa.tambahSks(mk.getSks());

        System.out.println("   [BERHASIL] MK '" + mk.getNamaMk() + "' berhasil ditambahkan ke KRS " + this.nomorKrs);
        System.out.println("   Beban SKS saat ini: " + this.hitungTotalSksKrs() + " / " + batasSks + " SKS");
        return true;
    }

    public void setujuiKrs(String namaDosenWali) {
        if (this.jumlahMk == 0) {
            System.out.println("[PERINGATAN] KRS " + this.nomorKrs + " belum memiliki mata kuliah, tidak dapat disahkan.");
            return;
        }
        if (namaDosenWali == null || namaDosenWali.trim().isEmpty()) {
            System.out.println("[VALIDASI DITOLAK] Nama dosen wali pengesah tidak boleh kosong!");
            return;
        }
        this.statusValidasi = true;
        this.dosenWaliPengesah = namaDosenWali.trim();
        System.out.println("[VALIDASI KRS] Dokumen KRS " + this.nomorKrs + " milik " + this.mahasiswa.getNama() + 
                           " berhasil DISETUJUI & DISAHKAN oleh Dosen Wali: " + this.dosenWaliPengesah);
    }

    public int hitungTotalSksKrs() {
        int total = 0;
        for (int i = 0; i < this.jumlahMk; i++) {
            total += this.daftarMataKuliah[i].getSks();
        }
        return total;
    }

    public void tampilkanKrs() {
        System.out.println("==========================================================================");
        System.out.println("                     KARTU RENCANA STUDI (KRS)                            ");
        System.out.println("               POLITEKNIK ELEKTRONIKA NEGERI SURABAYA                     ");
        System.out.println("==========================================================================");
        System.out.println("Nomor Registrasi KRS : " + this.nomorKrs);
        System.out.println("Tahun Ajaran / Sem.  : " + this.tahunAjaran + " (Semester " + this.semesterKrs + ")");
        System.out.println("Mahasiswa (NRP/Nama) : " + this.mahasiswa.getNrp() + " - " + this.mahasiswa.getNama());
        System.out.println("Program Studi        : " + this.mahasiswa.getProdi());
        System.out.printf("Capaian IPK Lalu     : %.2f (Batas Maksimal Beban: %d SKS)\n", 
                          this.mahasiswa.getIpk(), this.mahasiswa.hitungBebanMaksimalSks());
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
                                  (i + 1), mk.getKodeMk(), mk.getNamaMk(), mk.getSks(), mk.getDosenPengampu());
            }
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("TOTAL SKS TERDAFTAR : %d SKS\n", this.hitungTotalSksKrs());
        System.out.println("==========================================================================\n");
    }
}
