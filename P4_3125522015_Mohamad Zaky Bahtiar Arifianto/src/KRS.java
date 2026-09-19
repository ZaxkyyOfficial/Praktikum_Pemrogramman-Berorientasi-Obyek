/**
 * Class KRS (Kartu Rencana Studi) merepresentasikan dokumen rencana studi semester mahasiswa.
 * Menerapkan prinsip Encapsulation penuh, serta menjadi titik simpul integrasi relasi:
 * 1. AGGREGATION : Mengagregasi kumpulan objek MataKuliah (has-a).
 * 2. COMPOSITION : Dimiliki secara utuh oleh satu objek Mahasiswa (part-of).
 * 3. ASSOCIATION : Berasosiasi dengan objek Dosen untuk proses pengesahan akademik.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 4 (Relasi Antarobject)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class KRS {
    // =========================================================================
    // 1. ATTRIBUTES (PRIVATE - ENCAPSULATION & INFORMATION HIDING)
    // =========================================================================
    private String nomorKrs;
    private Mahasiswa mahasiswa;                 // COMPOSITION: Referensi kembali ke Mahasiswa pemilik dokumen
    private String tahunAjaran;
    private int semesterKrs;
    private MataKuliah[] daftarMataKuliah;       // AGGREGATION: KRS memiliki kumpulan MataKuliah
    private int jumlahMk;
    private boolean statusValidasi;
    private Dosen dosenWaliPengesah;             // ASSOCIATION: KRS disahkan oleh objek Dosen

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
        this.dosenWaliPengesah = null;
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

    public Dosen getDosenWaliPengesah() {
        return this.dosenWaliPengesah;
    }

    // =========================================================================
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS)
    // =========================================================================
    public final void setTahunAjaran(String tahunAjaran) {
        if (tahunAjaran != null && !tahunAjaran.trim().isEmpty()) {
            this.tahunAjaran = tahunAjaran.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Tahun ajaran tidak boleh kosong!");
        }
    }

    public final void setSemesterKrs(int semesterKrs) {
        if (semesterKrs >= 1 && semesterKrs <= 14) {
            this.semesterKrs = semesterKrs;
        } else {
            System.out.println("[VALIDASI DITOLAK] Semester KRS tidak valid (harus 1 s/d 14)!");
        }
    }

    // =========================================================================
    // 5. BUSINESS LOGIC METHODS (INTERAKSI OBJEK MELALUI METHOD)
    // =========================================================================
    public boolean tambahMataKuliah(MataKuliah mk) {
        if (mk == null) {
            System.out.println("   [GAGAL] Objek mata kuliah bernilai null!");
            return false;
        }

        System.out.println(">> [TRANSAKSI KRS] Mendaftarkan Mata Kuliah: " + mk.getNamaMk() + 
                           " (" + mk.getSks() + " SKS) ke KRS " + this.nomorKrs + "...");

        if (this.jumlahMk >= this.daftarMataKuliah.length) {
            System.out.println("   [GAGAL] Kapasitas maksimum item KRS telah penuh (" + this.daftarMataKuliah.length + " MK)!");
            return false;
        }

        if (mk.isKelasPenuh()) {
            System.out.println("   [GAGAL] Kelas mata kuliah '" + mk.getNamaMk() + "' sudah penuh (" + 
                               mk.getPesertaTerdaftar() + "/" + mk.getKuotaKelas() + " Kursi)!");
            return false;
        }

        int batasSks = this.mahasiswa != null ? this.mahasiswa.hitungBebanMaksimalSks() : 24;
        int totalSksSekarang = this.hitungTotalSksKrs();
        if (totalSksSekarang + mk.getSks() > batasSks) {
            System.out.println("   [GAGAL] Melebihi batas beban SKS mahasiswa (" + batasSks + " SKS)! " +
                               "Total saat ini: " + totalSksSekarang + " SKS + MK: " + mk.getSks() + " SKS.");
            return false;
        }

        this.daftarMataKuliah[this.jumlahMk] = mk;
        this.jumlahMk++;

        mk.tambahPeserta();
        if (this.mahasiswa != null) {
            this.mahasiswa.tambahSks(mk.getSks());
        }

        System.out.println("   [BERHASIL] MK '" + mk.getNamaMk() + "' ditambahkan. Total SKS saat ini: " + 
                           this.hitungTotalSksKrs() + " / " + batasSks + " SKS.");
        return true;
    }

    /**
     * Asosiasi: Pengesahan KRS oleh objek Dosen Wali.
     */
    public void setujuiKrs(Dosen dosen) {
        if (this.jumlahMk == 0) {
            System.out.println("[PERINGATAN] KRS " + this.nomorKrs + " belum memiliki mata kuliah, tidak dapat disahkan.");
            return;
        }
        if (dosen == null) {
            System.out.println("[VALIDASI DITOLAK] Objek Dosen pengesah tidak boleh null!");
            return;
        }
        this.statusValidasi = true;
        this.dosenWaliPengesah = dosen;
        String namaMhs = this.mahasiswa != null ? this.mahasiswa.getNama() : "Mahasiswa";
        System.out.println("[PENGESAHAN RESMI] Dokumen KRS " + this.nomorKrs + " milik " + namaMhs + 
                           " telah BERHASIL DISETUJUI oleh Dosen Wali: " + this.dosenWaliPengesah.getNama() + 
                           " (NIP: " + this.dosenWaliPengesah.getNip() + ").");
    }

    /**
     * Overload: Pengesahan KRS dengan input String nama dosen.
     */
    public void setujuiKrs(String namaDosen) {
        if (namaDosen != null && !namaDosen.trim().isEmpty()) {
            this.setujuiKrs(new Dosen("190000000000000000", namaDosen.trim(), "Dosen Wali", "dosen@pens.ac.id"));
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama dosen pengesah tidak boleh kosong!");
        }
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
        if (this.mahasiswa != null) {
            System.out.println("Mahasiswa (NRP/Nama) : " + this.mahasiswa.getNrp() + " - " + this.mahasiswa.getNama());
            System.out.println("Program Studi        : " + this.mahasiswa.getProdi());
            System.out.printf("Capaian IPK Lalu     : %.2f (Batas Maksimal Beban: %d SKS)\n", 
                              this.mahasiswa.getIpk(), this.mahasiswa.hitungBebanMaksimalSks());
        }
        System.out.println("Status Pengesahan    : " + (this.statusValidasi ? "[DISETUJUI / RESMI]" : "[PENDING / BELUM DISETUJUI]"));
        System.out.println("Dosen Wali Pengesah  : " + (this.dosenWaliPengesah != null ? 
                           this.dosenWaliPengesah.getNama() + " (" + this.dosenWaliPengesah.getNip() + ")" : "- (Belum Disahkan)"));
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-4s | %-8s | %-28s | %-5s | %-22s\n", "NO", "KODE", "MATA KULIAH", "SKS", "DOSEN PENGAMPU");
        System.out.println("--------------------------------------------------------------------------");
        if (this.jumlahMk == 0) {
            System.out.println("                  [ Belum ada mata kuliah yang diambil ]                  ");
        } else {
            for (int i = 0; i < this.jumlahMk; i++) {
                MataKuliah mk = this.daftarMataKuliah[i];
                String namaDosenPengampu = mk.getDosenPengampu() != null ? mk.getDosenPengampu().getNama() : "-";
                System.out.printf("%-4d | %-8s | %-28s | %-5d | %-22s\n", 
                                  (i + 1), mk.getKodeMk(), mk.getNamaMk(), mk.getSks(), namaDosenPengampu);
            }
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("TOTAL SKS TERDAFTAR : %d SKS\n", this.hitungTotalSksKrs());
        System.out.println("==========================================================================\n");
    }
}
