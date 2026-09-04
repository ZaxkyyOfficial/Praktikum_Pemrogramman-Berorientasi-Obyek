/**
 * Class MataKuliah merepresentasikan entitas mata kuliah kurikulum akademik.
 * Menerapkan prinsip Encapsulation dengan seluruh atribut berstatus private,
 * serta dilengkapi getter dan setter selektif dengan validasi kapasitas dan integritas data.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 3
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class MataKuliah {
    // =========================================================================
    // 1. ATTRIBUTES (PRIVATE - ENCAPSULATION)
    // =========================================================================
    private String kodeMk;
    private String namaMk;
    private int sks;
    private int semesterBuka;
    private String dosenPengampu;
    private int kuotaKelas;
    private int pesertaTerdaftar;

    // =========================================================================
    // 2. CONSTRUCTOR (TERVALIDASI)
    // =========================================================================
    public MataKuliah(String kodeMk, String namaMk, int sks, int semesterBuka, String dosenPengampu, int kuotaKelas) {
        setKodeMkInternal(kodeMk);
        setNamaMk(namaMk);
        setSksInternal(sks);
        setSemesterBuka(semesterBuka);
        setDosenPengampu(dosenPengampu);
        this.pesertaTerdaftar = 0;
        setKuotaKelas(kuotaKelas);
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

    public String getDosenPengampu() {
        return this.dosenPengampu;
    }

    public int getKuotaKelas() {
        return this.kuotaKelas;
    }

    public int getPesertaTerdaftar() {
        return this.pesertaTerdaftar;
    }

    // =========================================================================
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS)
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

    public void setNamaMk(String namaMk) {
        if (namaMk != null && !namaMk.trim().isEmpty()) {
            this.namaMk = namaMk.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama mata kuliah tidak boleh kosong!");
        }
    }

    public void setSemesterBuka(int semesterBuka) {
        if (semesterBuka >= 1 && semesterBuka <= 8) {
            this.semesterBuka = semesterBuka;
        } else {
            System.out.println("[VALIDASI DITOLAK] Semester buka " + semesterBuka + " tidak valid! Harus rentang 1 s/d 8.");
        }
    }

    public void setDosenPengampu(String dosenPengampu) {
        if (dosenPengampu != null && dosenPengampu.trim().length() >= 3) {
            this.dosenPengampu = dosenPengampu.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama dosen pengampu tidak valid! Minimal 3 karakter.");
        }
    }

    /**
     * Validasi Kuota Kelas: harus positif (> 0) dan tidak boleh lebih kecil dari peserta yang sudah terdaftar.
     */
    public void setKuotaKelas(int kuotaKelas) {
        if (kuotaKelas <= 0) {
            System.out.println("[VALIDASI DITOLAK] Kuota kelas " + kuotaKelas + " tidak valid! Kuota harus lebih besar dari 0.");
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
        System.out.println("Dosen Pengampu    : " + this.dosenPengampu);
        System.out.println("Kapasitas Kelas   : " + this.pesertaTerdaftar + " / " + this.kuotaKelas + " Mahasiswa");
        System.out.println("Sisa Kuota Kursi  : " + this.getSisaKuota() + " Kursi");
        System.out.println("Status Ketersediaan: " + (this.isKelasPenuh() ? "[PENUH]" : "[TERSEDIA]"));
        System.out.println("------------------------------------------------------------");
    }
}
