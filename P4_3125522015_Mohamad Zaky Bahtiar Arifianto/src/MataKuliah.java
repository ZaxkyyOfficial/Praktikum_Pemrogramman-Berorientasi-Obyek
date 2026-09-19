/**
 * Class MataKuliah merepresentasikan entitas mata kuliah kurikulum akademik.
 * Memiliki relasi Asosiasi dengan class Dosen sebagai pengampu mata kuliah.
 * 
 * Menerapkan prinsip Encapsulation penuh dengan seluruh atribut private,
 * serta dilengkapi getter dan setter selektif dengan validasi kapasitas dan integritas data.
 * Dilengkapi constructor dan setter overloading untuk kompatibilitas penuh.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 4 (Relasi Antarobject)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class MataKuliah {
    // =========================================================================
    // 1. ATTRIBUTES (PRIVATE - ENCAPSULATION & INFORMATION HIDING)
    // =========================================================================
    private String kodeMk;
    private String namaMk;
    private int sks;
    private int semesterBuka;
    private Dosen dosenPengampu; // ASOSIASI: MataKuliah berasosiasi dengan Dosen Pengampu
    private int kuotaKelas;
    private int pesertaTerdaftar;

    // =========================================================================
    // 2. CONSTRUCTOR (TERVALIDASI & OVERLOADED)
    // =========================================================================
    /**
     * Constructor Utama Modul 4: Menerima objek Dosen sebagai dosen pengampu (Relasi Asosiasi OOP).
     */
    public MataKuliah(String kodeMk, String namaMk, int sks, int semesterBuka, Dosen dosenPengampu, int kuotaKelas) {
        setKodeMkInternal(kodeMk);
        setNamaMk(namaMk);
        setSksInternal(sks);
        setSemesterBuka(semesterBuka);
        setDosenPengampu(dosenPengampu);
        this.pesertaTerdaftar = 0;
        setKuotaKelas(kuotaKelas);
    }

    /**
     * Constructor Overload: Menerima String nama dosen untuk backward-compatibility.
     * Mengonversi nama dosen string secara otomatis menjadi objek Dosen ber-enkapsulasi.
     */
    public MataKuliah(String kodeMk, String namaMk, int sks, int semesterBuka, String namaDosen, int kuotaKelas) {
        this(kodeMk, namaMk, sks, semesterBuka, 
             new Dosen("190000000000000000", (namaDosen != null && !namaDosen.trim().isEmpty() ? namaDosen.trim() : "Dosen Pengampu"), "Teknik Informatika", "dosen@pens.ac.id"), 
             kuotaKelas);
    }

    // =========================================================================
    // 3. GETTER (ACCESSOR METHODS)
    // =========================================================================
    public String getKodeMk() {
        return this.kodeMk;
    }

    public String getNamaMk() {
        return this.namaMk;
    }

    public int getSks() {
        return this.sks;
    }

    public int getSemesterBuka() {
        return this.semesterBuka;
    }

    public Dosen getDosenPengampu() {
        return this.dosenPengampu;
    }

    public int getKuotaKelas() {
        return this.kuotaKelas;
    }

    public int getPesertaTerdaftar() {
        return this.pesertaTerdaftar;
    }

    // =========================================================================
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS & OVERLOADS)
    // =========================================================================
    private void setKodeMkInternal(String kodeMk) {
        if (kodeMk != null && !kodeMk.trim().isEmpty()) {
            this.kodeMk = kodeMk.trim();
        } else {
            this.kodeMk = "MK000";
            System.out.println("[VALIDASI ERROR] Kode MK tidak boleh kosong! Diset ke MK000.");
        }
    }

    private void setSksInternal(int sks) {
        if (sks >= 1 && sks <= 6) {
            this.sks = sks;
        } else {
            this.sks = 2; // Default fallback SKS
            System.out.println("[VALIDASI ERROR] Bobot SKS " + sks + " tidak valid (harus 1-6)! Diset ke default 2 SKS.");
        }
    }

    public final void setNamaMk(String namaMk) {
        if (namaMk != null && !namaMk.trim().isEmpty()) {
            this.namaMk = namaMk.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama mata kuliah tidak boleh kosong!");
        }
    }

    public final void setSemesterBuka(int semesterBuka) {
        if (semesterBuka >= 1 && semesterBuka <= 8) {
            this.semesterBuka = semesterBuka;
        } else {
            System.out.println("[VALIDASI DITOLAK] Semester buka " + semesterBuka + " tidak valid! Harus rentang 1 s/d 8.");
        }
    }

    /**
     * Mutator Asosiasi: Mengubah referensi objek Dosen pengampu.
     */
    public final void setDosenPengampu(Dosen dosenPengampu) {
        if (dosenPengampu != null) {
            this.dosenPengampu = dosenPengampu;
        } else {
            System.out.println("[VALIDASI DITOLAK] Dosen pengampu tidak boleh null!");
        }
    }

    /**
     * Mutator Overload: Mendukung input String nama dosen dan mengemasnya ke objek Dosen.
     */
    public final void setDosenPengampu(String namaDosen) {
        if (namaDosen != null && namaDosen.trim().length() >= 3) {
            if (this.dosenPengampu != null) {
                this.dosenPengampu.setNama(namaDosen.trim());
            } else {
                this.dosenPengampu = new Dosen("190000000000000000", namaDosen.trim(), "Teknik Informatika", "dosen@pens.ac.id");
            }
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama dosen pengampu tidak valid! Minimal 3 karakter.");
        }
    }

    public final void setKuotaKelas(int kuotaKelas) {
        if (kuotaKelas <= 0) {
            System.out.println("[VALIDASI DITOLAK] Kuota kelas " + kuotaKelas + " tidak valid! Kuota harus > 0.");
        } else if (kuotaKelas < this.pesertaTerdaftar) {
            System.out.println("[VALIDASI DITOLAK] Kuota baru (" + kuotaKelas + 
                               ") tidak boleh lebih kecil dari jumlah mahasiswa yang sudah terdaftar (" + 
                               this.pesertaTerdaftar + ")!");
        } else {
            this.kuotaKelas = kuotaKelas;
        }
    }

    // =========================================================================
    // 5. BUSINESS LOGIC METHODS
    // =========================================================================
    public boolean tambahPeserta() {
        if (!isKelasPenuh()) {
            this.pesertaTerdaftar++;
            return true;
        } else {
            return false;
        }
    }

    public int getSisaKuota() {
        return this.kuotaKelas - this.pesertaTerdaftar;
    }

    public boolean isKelasPenuh() {
        return this.pesertaTerdaftar >= this.kuotaKelas;
    }

    public void tampilkanDetailMk() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                 INFORMASI MATA KULIAH                      ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Kode MK           : " + this.kodeMk);
        System.out.println("Nama Mata Kuliah  : " + this.namaMk);
        System.out.println("Bobot SKS         : " + this.sks + " SKS");
        System.out.println("Semester Buka     : Semester " + this.semesterBuka);
        System.out.println("Dosen Pengampu    : " + (this.dosenPengampu != null ? this.dosenPengampu.getNama() : "-"));
        System.out.println("Kapasitas Kelas   : " + this.pesertaTerdaftar + " / " + this.kuotaKelas + " Mahasiswa");
        System.out.println("Sisa Kuota Kursi  : " + this.getSisaKuota() + " Kursi");
        System.out.println("Status Ketersediaan: " + (this.isKelasPenuh() ? "[PENUH]" : "[TERSEDIA]"));
        System.out.println("------------------------------------------------------------");
    }
}
