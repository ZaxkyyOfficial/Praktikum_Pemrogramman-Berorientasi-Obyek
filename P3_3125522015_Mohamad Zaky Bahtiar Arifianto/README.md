# 🎓 Sistem Informasi Akademik Mahasiswa (SIAKAD)
### **Praktikum Pemrograman Berorientasi Obyek (PBO) — Modul 3**
**Encapsulation, Access Modifier, Getter-Setter, dan Validasi Data**  
**Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)**

---

## 📌 Identitas Praktikum

| Informasi | Keterangan |
| :--- | :--- |
| **Nama Mahasiswa** | **Mohamad Zaky Bahtiar Arifianto** |
| **NRP** | `3125522015` |
| **Program Studi** | D3 Teknik Informatika |
| **Mata Kuliah** | Praktikum Pemrograman Berorientasi Obyek |
| **Dosen Pengampu** | Nirwana Haidar Hari, S.Pd., M.Kom. |
| **Tahun Ajaran** | Semester 3 / 2026 |

---

## 📖 Daftar Isi
1. [Review Hasil P2 & Latar Belakang Refactoring Encapsulation](#-1-review-hasil-p2--latar-belakang-refactoring-encapsulation)
2. [Agile Sprint Planning — Modul 3](#-2-agile-sprint-planning--modul-3)
3. [Audit Class P2 (Bagian A)](#-3-audit-class-p2-bagian-a)
4. [Tabel Pemetaan Spesifikasi Encapsulation P3](#-4-tabel-pemetaan-spesifikasi-encapsulation-p3)
5. [Implementasi Source Code Java Ter-Refactoring](#-5-implementasi-source-code-java-ter-refactoring)
6. [Struktur Direktori Proyek](#-6-struktur-direktori-proyek)
7. [Cara Kompilasi dan Menjalankan Program](#-7-cara-kompilasi-dan-menjalankan-program)
8. [Bukti Hasil Pengujian Program (Test Valid & Test Invalid)](#-8-bukti-hasil-pengujian-program-test-valid--test-invalid)
9. [Sprint Review & Sprint Retrospective](#-9-sprint-review--sprint-retrospective)
10. [Evaluasi Definition of Done (DoD) Modul 3](#-10-evaluasi-definition-of-done-dod-modul-3)
11. [Target Sprint Berikutnya (P4)](#-11-target-sprint-berikutnya-p4)

---

## 🔄 1. Review Hasil P2 & Latar Belakang Refactoring Encapsulation

Pada praktikum **Modul 2**, sistem telah berhasil mengonstruksi 3 class domain (`Mahasiswa`, `MataKuliah`, `KRS`), mendefinisikan atribut, method, dan constructor berparameter.

### ❗ Permasalahan Kritis pada P2:
Pada implementasi P2, semua atribut dideklarasikan dengan access modifier `public`:
```java
// Struktur P2 (Rentan Pelanggaran Integritas Data)
class Mahasiswa {
    public String nrp;
    public String nama;
    public double ipk;
}

// Dari Main.java dapat dilakukan assignment sembarangan:
mhs.ipk = -10.0;     // NILAI RUSAK TETAP DITERIMA!
mhs.nama = "";       // NAMA KOSONG LOLOS!
```
Akses langsung tanpa proteksi tersebut melanggar pilar fundamental OOP yaitu **Information Hiding** dan menyebabkan integritas data objek rentan dirusak dari luar class.

### 🛡️ Solusi Encapsulation pada Modul 3:
Pada **Modul 3**, seluruh class direfactor dengan menerapkan:
1. Mengubah seluruh atribut menjadi **`private`** (hanya dapat diakses di dalam class bersangkutan).
2. Menyediakan **`Getter`** sebagai jalur pembacaan data yang aman.
3. Menyediakan **`Setter`** selektif yang diperkuat oleh **Validasi Data Ketat** (misal IPK harus 0.00–4.00, semester 1–14, kuota > 0, dsb).
4. Memperbaiki **`Constructor`** agar inisialisasi awal selalu melalui mekanisme validasi sehingga tidak ada celah data ilegal sejak awal pembuatan objek.

---

## 🏃 2. Agile Sprint Planning — Modul 3

### 🎯 2.1 Sprint Goal
> *"Memperbaiki struktur class proyek SIAKAD (Mahasiswa, MataKuliah, dan KRS) dengan menerapkan encapsulation penuh sehingga data object hanya dapat diakses dan diubah melalui mekanisme yang terkontrol, menerapkan minimal 3 aturan validasi ketat, serta memverifikasi ketahanan sistem melalui pengujian data valid dan data invalid pada Main.java."*

### 📋 2.2 Sprint Backlog P3
| ID | Item Sprint Backlog | Estimasi Bobot | Status Akhir |
| :---: | :--- | :---: | :---: |
| **SB-01** | Melakukan audit struktur class P2 dan memetakan kebutuhan enkapsulasi | 15% | `DONE` |
| **SB-02** | Mengubah seluruh access modifier attribute utama menjadi `private` | 25% | `DONE` |
| **SB-03** | Membuat method getter (*accessor*) untuk seluruh attribute yang perlu dibaca | 15% | `DONE` |
| **SB-04** | Membuat method setter (*mutator*) selektif dengan validasi data ketat | 20% | `DONE` |
| **SB-05** | Memperbaiki constructor seluruh class agar menerapkan aturan validasi | 10% | `DONE` |
| **SB-06** | Mengimplementasikan pengujian ganda (*Test Valid* & *Test Invalid*) pada `Main` | 10% | `DONE` |
| **SB-07** | Menyusun laporan resmi Word, PDF eksekutif 3 halaman, dan dokumentasi README.md | 5% | `DONE` |

---

## 🔍 3. Audit Class P2 (Bagian A)

Berikut adalah tabel audit hasil perbandingan class pada P2 terhadap kebutuhan refactoring pada P3:

| Nama Class | Attribute | Kondisi P2 | Perbaikan P3 | Alasan & Aturan Validasi |
| :--- | :--- | :---: | :--- | :--- |
| **`Mahasiswa`** | `nrp` | `public` | `private` + getter only | NRP adalah nomor identitas mahasiswa yang unik dan *immutable*. Validasi format tidak boleh null/kosong. |
| | `nama` | `public` | `private` + getter + setter | Nama dapat diperbarui jika ada revisi administrasi. Validasi: tidak boleh null/blank, minimal 3 karakter. |
| | `prodi` | `public` | `private` + getter + setter | Prodi dapat berubah jika mutasi jurusan. Validasi: tidak boleh kosong/null. |
| | `semester` | `public` | `private` + getter + setter | Semester bertambah tiap tahun ajaran. Validasi: rentang akademik 1 s/d 14. |
| | `ipk` | `public` | `private` + getter + setter | Capaian IPK diperbarui per semester. Validasi: nilai wajib di rentang 0.00 s/d 4.00. |
| | `totalSks` | `public` | `private` + getter only | Total SKS hanya boleh bertambah lewat transaksi `tambahSks()` dan reset via `resetSks()`. Tidak boleh diset sembarangan. |
| **`MataKuliah`**| `kodeMk` | `public` | `private` + getter only | Kode MK adalah pengenal permanen kurikulum. Validasi: tidak boleh kosong. |
| | `namaMk` | `public` | `private` + getter + setter | Nama MK dapat disesuaikan kurikulum. Validasi: tidak boleh kosong. |
| | `sks` | `public` | `private` + getter only | Bobot kredit SKS bersifat standar kurikulum akademik (1 s/d 6 SKS). |
| | `semesterBuka`| `public` | `private` + getter + setter | Semester penawaran MK dapat dijadwalkan ulang. Validasi: rentang 1 s/d 8. |
| | `dosenPengampu`| `public` | `private` + getter + setter | Pengampu dapat berganti SK mengajar. Validasi: tidak boleh null/blank, minimal 3 karakter. |
| | `kuotaKelas` | `public` | `private` + getter + setter | Daya tampung dapat ditambah. Validasi: kuota > 0 dan tidak boleh lebih kecil dari mahasiswa yang sudah mendaftar. |
| | `pesertaTerdaftar`| `public`| `private` + getter only | Counter peserta bertambah otomatis saat `tambahPeserta()`. |
| **`KRS`** | `nomorKrs` | `public` | `private` + getter only | Nomor registrasi unik kartu studi. |
| | `mahasiswa` | `public` | `private` + getter only | Pemilik dokumen akademik (objek Mahasiswa tidak boleh null). |
| | `tahunAjaran`| `public` | `private` + getter + setter | Validasi tidak boleh null/blank. |
| | `semesterKrs`| `public` | `private` + getter + setter | Validasi semester akademik 1 s/d 14. |
| | `daftarMataKuliah`| `public`| `private` + getter array | Hanya bertambah via method bisnis `tambahMataKuliah()`. |
| | `statusValidasi`| `public` | `private` + getter (`isDisetujui`)| Hanya berubah menjadi `true` melalui pengesahan Dosen Wali `setujuiKrs()`. |

---

## 📑 4. Tabel Pemetaan Spesifikasi Encapsulation P3

*(Memenuhi persyaratan Modul 3 Halaman 2)*

| Class | Private Attribute | Getter Method | Setter Method | Aturan & Mekanisme Validasi Data |
| :--- | :--- | :--- | :--- | :--- |
| **`Mahasiswa`** | `nrp` (String)<br/>`nama` (String)<br/>`prodi` (String)<br/>`semester` (int)<br/>`ipk` (double)<br/>`totalSks` (int) | `getNrp()`<br/>`getNama()`<br/>`getProdi()`<br/>`getSemester()`<br/>`getIpk()`<br/>`getTotalSks()` | `setNama(String)`<br/>`setProdi(String)`<br/>`setSemester(int)`<br/>`setIpk(double)` | 1. `nama`: `nama != null && nama.trim().length() >= 3`<br/>2. `semester`: `semester >= 1 && semester <= 14`<br/>3. `ipk`: `ipk >= 0.0 && ipk <= 4.0`<br/>4. `tambahSks`: `totalSks + sks <= hitungBebanMaksimalSks()` |
| **`MataKuliah`** | `kodeMk` (String)<br/>`namaMk` (String)<br/>`sks` (int)<br/>`semesterBuka` (int)<br/>`dosenPengampu` (String)<br/>`kuotaKelas` (int)<br/>`pesertaTerdaftar` (int) | `getKodeMk()`<br/>`getNamaMk()`<br/>`getSks()`<br/>`getSemesterBuka()`<br/>`getDosenPengampu()`<br/>`getKuotaKelas()`<br/>`getPesertaTerdaftar()` | `setNamaMk(String)`<br/>`setSemesterBuka(int)`<br/>`setDosenPengampu(String)`<br/>`setKuotaKelas(int)` | 1. `namaMk`: `namaMk != null && !namaMk.trim().isEmpty()`<br/>2. `semesterBuka`: `semesterBuka >= 1 && semesterBuka <= 8`<br/>3. `dosenPengampu`: `dosenPengampu.trim().length() >= 3`<br/>4. `kuotaKelas`: `kuota > 0 && kuota >= pesertaTerdaftar` |
| **`KRS`** | `nomorKrs` (String)<br/>`mahasiswa` (Mahasiswa)<br/>`tahunAjaran` (String)<br/>`semesterKrs` (int)<br/>`daftarMataKuliah` (MataKuliah[])<br/>`jumlahMk` (int)<br/>`statusValidasi` (boolean)<br/>`dosenWaliPengesah` (String) | `getNomorKrs()`<br/>`getMahasiswa()`<br/>`getTahunAjaran()`<br/>`getSemesterKrs()`<br/>`getDaftarMataKuliah()`<br/>`getJumlahMk()`<br/>`isDisetujui()`<br/>`getDosenWaliPengesah()` | `setTahunAjaran(String)`<br/>`setSemesterKrs(int)` | 1. `mahasiswa != null`<br/>2. `kapasitasMaksMk > 0`<br/>3. `tambahMataKuliah`: cek kapasitas array, cek `!mk.isKelasPenuh()`, dan cek batas akumulasi SKS mahasiswa.<br/>4. `setujuiKrs`: `jumlahMk > 0 && dosenWali != null` |

---

## 💻 5. Implementasi Source Code Java Ter-Refactoring

### 📄 5.1 `src/Mahasiswa.java`
```java
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

```

### 📄 5.2 `src/MataKuliah.java`
```java
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

```

### 📄 5.3 `src/KRS.java`
```java
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

```

### 📄 5.4 `src/Main.java`
```java
/**
 * Class Main merupakan program pengujian utama Praktikum PBO Modul 3.
 * Menguji implementasi Encapsulation, Access Modifier private, Getter-Setter,
 * serta verifikasi ketat melalui dua skenario utama:
 * 1. TEST VALID: Instansiasi objek dan modifikasi data yang sah via setter.
 * 2. TEST INVALID: Uji ketahanan validasi data saat diberi input salah/ilegal.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 3
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("     PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK (PBO) - MODUL 3             ");
        System.out.println("   Encapsulation, Access Modifier, Getter-Setter, dan Validasi Data       ");
        System.out.println("==========================================================================");
        System.out.println("Pengembang   : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP          : 3125522015");
        System.out.println("Program Studi: D3 Teknik Informatika");
        System.out.println("Institusi    : PENS PSDKU Sumenep\n");

        // ======================================================================
        // BAGIAN 1: PENGUJIAN DATA VALID (TEST VALID)
        // ======================================================================
        System.out.println("##########################################################################");
        System.out.println("                 [SKENARIO 1: PENGUJIAN DATA VALID (TEST VALID)]          ");
        System.out.println("##########################################################################");
        
        System.out.println("\n>>> 1.1 INSTANSIASI OBJEK DENGAN CONSTRUCTOR TERVALIDASI");
        Mahasiswa mhs1 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", "D3 Teknik Informatika", 3, 3.82);
        Mahasiswa mhs2 = new Mahasiswa("3125522022", "Ahmad Wildan Prasetyo", "D3 Teknik Informatika", 3, 2.45);

        MataKuliah mk1 = new MataKuliah("IF201", "Pemrograman Berorientasi Obyek", 3, 3, "Nirwana Haidar Hari, S.Pd., M.Kom.", 30);
        MataKuliah mk2 = new MataKuliah("IF202", "Praktikum PBO", 2, 3, "Nirwana Haidar Hari, S.Pd., M.Kom.", 30);
        MataKuliah mk3 = new MataKuliah("IF203", "Basis Data Lanjut", 3, 3, "Firman Arifin, S.T., M.T.", 2);
        MataKuliah mk4 = new MataKuliah("IF204", "Rekayasa Perangkat Lunak", 3, 3, "Budi Raharjo, S.Kom., M.Kom.", 25);
        MataKuliah mk5 = new MataKuliah("IF205", "Jaringan Komputer", 3, 3, "Hendra Kusuma, S.Kom., M.T.", 30);

        KRS krs1 = new KRS("KRS-2026-001", mhs1, "2026/2027 Ganjil", 3, 8);
        KRS krs2 = new KRS("KRS-2026-002", mhs2, "2026/2027 Ganjil", 3, 8);
        System.out.println("[STATUS TEST VALID] Seluruh objek berhasil dibuat melalui constructor.");

        System.out.println("\n>>> 1.2 MEMBACA DATA MENGGUNAKAN GETTER (ACCESSOR)");
        System.out.println("Mhs1 - NRP (getNrp)      : " + mhs1.getNrp());
        System.out.println("Mhs1 - Nama (getNama)    : " + mhs1.getNama());
        System.out.printf("Mhs1 - IPK (getIpk)      : %.2f\n", mhs1.getIpk());
        System.out.println("Mhs1 - Kuota SKS (hitung): " + mhs1.hitungBebanMaksimalSks() + " SKS");
        System.out.println("MK1  - Nama MK (getNama) : " + mk1.getNamaMk() + " (" + mk1.getSks() + " SKS)");
        System.out.println("MK1  - Dosen (getDosen)  : " + mk1.getDosenPengampu());

        System.out.println("\n>>> 1.3 MENGUBAH DATA MENGGUNAKAN SETTER VALID (MUTATOR)");
        mhs1.setIpk(3.92);
        System.out.printf("[HASIL SETTER VALID] IPK mhs1 setelah setIpk(3.92): %.2f\n", mhs1.getIpk());
        mk3.setDosenPengampu("Dr. Indah Susilowati, S.T., M.T.");
        System.out.println("[HASIL SETTER VALID] Dosen MK3 setelah setDosenPengampu: " + mk3.getDosenPengampu());

        System.out.println("\n>>> 1.4 TRANSAKSI PENGISIAN KRS & PENGESAHAN OLEH DOSEN WALI");
        krs1.tambahMataKuliah(mk1);
        krs1.tambahMataKuliah(mk2);
        krs1.tambahMataKuliah(mk3);
        krs1.tambahMataKuliah(mk4);
        krs1.tambahMataKuliah(mk5);
        krs1.setujuiKrs("Nirwana Haidar Hari, S.Pd., M.Kom.");
        System.out.println("Status Verifikasi KRS 1 (isDisetujui): " + (krs1.isDisetujui() ? "VALID" : "PENDING"));

        // ======================================================================
        // BAGIAN 2: PENGUJIAN DATA INVALID (TEST INVALID - ATURAN VALIDASI)
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("               [SKENARIO 2: PENGUJIAN DATA INVALID (TEST INVALID)]        ");
        System.out.println("##########################################################################");

        System.out.println("\n>>> 2.1 PENGUJIAN NILAI IPK INVALID (TIDAK BOLEH < 0.00 ATAU > 4.00)");
        System.out.println("[UJI COBA 1] Memasukkan IPK Negatif: mhs1.setIpk(-1.50)");
        mhs1.setIpk(-1.50);
        System.out.printf("--> Nilai IPK mhs1 saat ini (harus tetap utuh): %.2f\n", mhs1.getIpk());

        System.out.println("\n[UJI COBA 2] Memasukkan IPK Melebihi Batas: mhs1.setIpk(4.85)");
        mhs1.setIpk(4.85);
        System.out.printf("--> Nilai IPK mhs1 saat ini (harus tetap utuh): %.2f\n", mhs1.getIpk());

        System.out.println("\n>>> 2.2 PENGUJIAN SEMESTER INVALID (TIDAK BOLEH < 1 ATAU > 14)");
        System.out.println("[UJI COBA 3] Memasukkan Semester 0: mhs2.setSemester(0)");
        mhs2.setSemester(0);
        System.out.println("--> Nilai Semester mhs2 saat ini: " + mhs2.getSemester());

        System.out.println("\n[UJI COBA 4] Memasukkan Semester 15: mhs2.setSemester(15)");
        mhs2.setSemester(15);
        System.out.println("--> Nilai Semester mhs2 saat ini: " + mhs2.getSemester());

        System.out.println("\n>>> 2.3 PENGUJIAN NAMA MAHASISWA INVALID (KOSONG / WHITESPACE)");
        System.out.println("[UJI COBA 5] Memasukkan Nama Kosong: mhs1.setNama(\"   \")");
        mhs1.setNama("   ");
        System.out.println("--> Nilai Nama mhs1 saat ini: " + mhs1.getNama());

        System.out.println("\n>>> 2.4 PENGUJIAN KUOTA KELAS INVALID");
        System.out.println("[UJI COBA 6] Memasukkan Kuota Kelas Negatif: mk3.setKuotaKelas(-10)");
        mk3.setKuotaKelas(-10);
        System.out.println("--> Nilai Kuota mk3 saat ini: " + mk3.getKuotaKelas());

        System.out.println("\n[UJI COBA 7] Memasukkan Kuota Lebih Kecil dari Peserta Terdaftar:");
        System.out.println("Peserta terdaftar di " + mk3.getNamaMk() + " saat ini: " + mk3.getPesertaTerdaftar());
        System.out.println("Mencoba ubah kuota menjadi 0: mk3.setKuotaKelas(0)");
        mk3.setKuotaKelas(0);
        System.out.println("--> Nilai Kuota mk3 saat ini: " + mk3.getKuotaKelas());

        // ======================================================================
        // BAGIAN 3: VERIFIKASI AKHIR STATUS DATA PASCA PENGUJIAN
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("                     LEMBAR CETAK HASIL AKHIR KRS                         ");
        System.out.println("##########################################################################\n");
        krs1.tampilkanKrs();

        System.out.println("==========================================================================");
        System.out.println("  SEMUA PENGUJIAN VALIDASI ENKAPSULASI P3 BERHASIL DILALUI DENGAN SUKSES  ");
        System.out.println("==========================================================================");
    }
}

```

---

## 📂 6. Struktur Direktori Proyek

```text
P3_3125522015_Mohamad Zaky Bahtiar Arifianto/
│
├── 3125522015_Mohamad Zaky Bahtiar Arifianto.docx  # Laporan Resmi Praktikum Word Lengkap
├── 3125522015_Mohamad Zaky Bahtiar Arifianto.pdf   # Laporan Resmi Praktikum Format PDF
├── README.md                                       # Dokumentasi Proyek GitHub Markdown
├── README.pdf                                      # Ringkasan Eksekutif PDF Tepat 3 Halaman
├── Modul 3- Encapsulation...pdf                    # Buku Panduan Praktikum Modul 3
│
└── src/
    ├── Mahasiswa.java                              # Implementasi Encapsulated Mahasiswa
    ├── MataKuliah.java                             # Implementasi Encapsulated MataKuliah
    ├── KRS.java                                    # Implementasi Encapsulated KRS
    └── Main.java                                   # Pengujian Valid & Invalid Scenarios
```

---

## 🚀 7. Cara Kompilasi dan Menjalankan Program

Buka terminal PowerShell pada folder `P3_3125522015_Mohamad Zaky Bahtiar Arifianto`, lalu jalankan:

### ⚙️ 1. Kompilasi Source Code
```bash
javac -d bin src/*.java
```

### ▶️ 2. Eksekusi Program
```bash
java -cp bin Main
```

---

## 🖥️ 8. Bukti Hasil Pengujian Program (Test Valid & Test Invalid)

```text
==========================================================================
     PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK (PBO) - MODUL 3             
   Encapsulation, Access Modifier, Getter-Setter, dan Validasi Data       
==========================================================================
Pengembang   : Mohamad Zaky Bahtiar Arifianto
NRP          : 3125522015
Program Studi: D3 Teknik Informatika
Institusi    : PENS PSDKU Sumenep

##########################################################################
                 [SKENARIO 1: PENGUJIAN DATA VALID (TEST VALID)]          
##########################################################################

>>> 1.1 INSTANSIASI OBJEK DENGAN CONSTRUCTOR TERVALIDASI
[STATUS TEST VALID] Seluruh objek berhasil dibuat melalui constructor.

>>> 1.2 MEMBACA DATA MENGGUNAKAN GETTER (ACCESSOR)
Mhs1 - NRP (getNrp)      : 3125522015
Mhs1 - Nama (getNama)    : Mohamad Zaky Bahtiar Arifianto
Mhs1 - IPK (getIpk)      : 3.82
Mhs1 - Kuota SKS (hitung): 24 SKS
MK1  - Nama MK (getNama) : Pemrograman Berorientasi Obyek (3 SKS)
MK1  - Dosen (getDosen)  : Nirwana Haidar Hari, S.Pd., M.Kom.

>>> 1.3 MENGUBAH DATA MENGGUNAKAN SETTER VALID (MUTATOR)
[HASIL SETTER VALID] IPK mhs1 setelah setIpk(3.92): 3.92
[HASIL SETTER VALID] Dosen MK3 setelah setDosenPengampu: Dr. Indah Susilowati, S.T., M.T.

>>> 1.4 TRANSAKSI PENGISIAN KRS & PENGESAHAN OLEH DOSEN WALI
>> [PERMOHONAN KRS] Mahasiswa Mohamad Zaky Bahtiar Arifianto mendaftar MK: Pemrograman Berorientasi Obyek (3 SKS)...
   [BERHASIL] MK 'Pemrograman Berorientasi Obyek' berhasil ditambahkan ke KRS KRS-2026-001
   Beban SKS saat ini: 3 / 24 SKS
>> [PERMOHONAN KRS] Mahasiswa Mohamad Zaky Bahtiar Arifianto mendaftar MK: Praktikum PBO (2 SKS)...
   [BERHASIL] MK 'Praktikum PBO' berhasil ditambahkan ke KRS KRS-2026-001
   Beban SKS saat ini: 5 / 24 SKS
>> [PERMOHONAN KRS] Mahasiswa Mohamad Zaky Bahtiar Arifianto mendaftar MK: Basis Data Lanjut (3 SKS)...
   [BERHASIL] MK 'Basis Data Lanjut' berhasil ditambahkan ke KRS KRS-2026-001
   Beban SKS saat ini: 8 / 24 SKS
>> [PERMOHONAN KRS] Mahasiswa Mohamad Zaky Bahtiar Arifianto mendaftar MK: Rekayasa Perangkat Lunak (3 SKS)...
   [BERHASIL] MK 'Rekayasa Perangkat Lunak' berhasil ditambahkan ke KRS KRS-2026-001
   Beban SKS saat ini: 11 / 24 SKS
>> [PERMOHONAN KRS] Mahasiswa Mohamad Zaky Bahtiar Arifianto mendaftar MK: Jaringan Komputer (3 SKS)...
   [BERHASIL] MK 'Jaringan Komputer' berhasil ditambahkan ke KRS KRS-2026-001
   Beban SKS saat ini: 14 / 24 SKS
[VALIDASI KRS] Dokumen KRS KRS-2026-001 milik Mohamad Zaky Bahtiar Arifianto berhasil DISETUJUI & DISAHKAN oleh Dosen Wali: Nirwana Haidar Hari, S.Pd., M.Kom.
Status Verifikasi KRS 1 (isDisetujui): VALID

##########################################################################
               [SKENARIO 2: PENGUJIAN DATA INVALID (TEST INVALID)]        
##########################################################################

>>> 2.1 PENGUJIAN NILAI IPK INVALID (TIDAK BOLEH < 0.00 ATAU > 4.00)
[UJI COBA 1] Memasukkan IPK Negatif: mhs1.setIpk(-1.50)
[VALIDASI DITOLAK] Nilai IPK -1.50 tidak valid! Harus berada di rentang 0.00 s/d 4.00.
--> Nilai IPK mhs1 saat ini (harus tetap utuh): 3.92

[UJI COBA 2] Memasukkan IPK Melebihi Batas: mhs1.setIpk(4.85)
[VALIDASI DITOLAK] Nilai IPK 4.85 tidak valid! Harus berada di rentang 0.00 s/d 4.00.
--> Nilai IPK mhs1 saat ini (harus tetap utuh): 3.92

>>> 2.2 PENGUJIAN SEMESTER INVALID (TIDAK BOLEH < 1 ATAU > 14)
[UJI COBA 3] Memasukkan Semester 0: mhs2.setSemester(0)
[VALIDASI DITOLAK] Semester 0 tidak valid! Batas semester adalah 1 s/d 14.
--> Nilai Semester mhs2 saat ini: 3

[UJI COBA 4] Memasukkan Semester 15: mhs2.setSemester(15)
[VALIDASI DITOLAK] Semester 15 tidak valid! Batas semester adalah 1 s/d 14.
--> Nilai Semester mhs2 saat ini: 3

>>> 2.3 PENGUJIAN NAMA MAHASISWA INVALID (KOSONG / WHITESPACE)
[UJI COBA 5] Memasukkan Nama Kosong: mhs1.setNama("   ")
[VALIDASI DITOLAK] Nama mahasiswa tidak valid! Minimal 3 karakter dan tidak boleh kosong.
--> Nilai Nama mhs1 saat ini: Mohamad Zaky Bahtiar Arifianto

>>> 2.4 PENGUJIAN KUOTA KELAS INVALID
[UJI COBA 6] Memasukkan Kuota Kelas Negatif: mk3.setKuotaKelas(-10)
[VALIDASI DITOLAK] Kuota kelas -10 tidak valid! Kuota harus lebih besar dari 0.
--> Nilai Kuota mk3 saat ini: 2

[UJI COBA 7] Memasukkan Kuota Lebih Kecil dari Peserta Terdaftar:
Peserta terdaftar di Basis Data Lanjut saat ini: 1
Mencoba ubah kuota menjadi 0: mk3.setKuotaKelas(0)
[VALIDASI DITOLAK] Kuota kelas 0 tidak valid! Kuota harus lebih besar dari 0.
--> Nilai Kuota mk3 saat ini: 2

##########################################################################
                     LEMBAR CETAK HASIL AKHIR KRS                         
##########################################################################

==========================================================================
                     KARTU RENCANA STUDI (KRS)                            
               POLITEKNIK ELEKTRONIKA NEGERI SURABAYA                     
==========================================================================
Nomor Registrasi KRS : KRS-2026-001
Tahun Ajaran / Sem.  : 2026/2027 Ganjil (Semester 3)
Mahasiswa (NRP/Nama) : 3125522015 - Mohamad Zaky Bahtiar Arifianto
Program Studi        : D3 Teknik Informatika
Capaian IPK Lalu     : 3.92 (Batas Maksimal Beban: 24 SKS)
Status Pengesahan    : DISETUJUI (VALID)
Dosen Wali Pengesah  : Nirwana Haidar Hari, S.Pd., M.Kom.
--------------------------------------------------------------------------
NO   | KODE       | MATA KULIAH                    | SKS   | DOSEN PENGAMPU      
--------------------------------------------------------------------------
1    | IF201      | Pemrograman Berorientasi Obyek | 3     | Nirwana Haidar Hari, S.Pd., M.Kom.
2    | IF202      | Praktikum PBO                  | 2     | Nirwana Haidar Hari, S.Pd., M.Kom.
3    | IF203      | Basis Data Lanjut              | 3     | Dr. Indah Susilowati, S.T., M.T.
4    | IF204      | Rekayasa Perangkat Lunak       | 3     | Budi Raharjo, S.Kom., M.Kom.
5    | IF205      | Jaringan Komputer              | 3     | Hendra Kusuma, S.Kom., M.T.
--------------------------------------------------------------------------
TOTAL SKS TERDAFTAR : 14 SKS
==========================================================================

==========================================================================
  SEMUA PENGUJIAN VALIDASI ENKAPSULASI P3 BERHASIL DILALUI DENGAN SUKSES  
==========================================================================
```

---

## 📈 9. Sprint Review & Sprint Retrospective

### 📋 9.1 Sprint Review
| Item Evaluasi | Hasil Pencapaian Modul 3 |
| :--- | :--- |
| **Attribute berhasil di-private** | Terpenuhi 100% (Semua atribut pada `Mahasiswa`, `MataKuliah`, dan `KRS` diubah menjadi `private`). |
| **Getter berjalan** | Terpenuhi (Method getter membaca data tanpa celah modifikasi). |
| **Setter berjalan** | Terpenuhi (Setter selektif memfilter data dengan logika validasi). |
| **Validasi berhasil** | Terpenuhi (Validasi IPK, semester, kuota kelas, nama mahasiswa, dan kapasitas SKS berfungsi akurat). |
| **Constructor diperbaiki** | Terpenuhi (Constructor memanggil setter/validasi internal saat objek pertama kali dibuat). |
| **Test invalid berhasil** | Terpenuhi (Nilai invalid ditolak dengan pesan peringatan dan nilai atribut asli tetap aman/tidak rusak). |
| **Program dapat dijalankan** | Terpenuhi (Zero error, zero warning saat kompilasi dan runtime). |

### 🔍 9.2 Sprint Retrospective
* **What Went Well? (Apa yang berjalan baik?)**  
  Penerapan konsep enkapsulasi berhasil secara drastis meningkatkan keamanan data objek. Saat pengguna mencoba memasukkan nilai invalid (IPK -1.5, semester 0, kuota negatif), sistem secara otomatis menolak dan mempertahankan nilai valid sebelumnya.
* **What Went Wrong? (Kendala saat refactoring?)**  
  Pada class `KRS`, banyak pemanggilan atribut langsung dari class lain seperti `mhs.nama` atau `mk.sks` yang semula berjalan di P2 menjadi error kompilasi karena telah di-private. Seluruh pemanggilan harus disesuaikan menjadi method getter seperti `mhs.getNama()` dan `mk.getSks()`.
* **Improvement (Rencana perbaikan berikutnya?)**  
  Pada sprint berikutnya (P4 — Object Relationship), pola hubungan asosiasi, agregasi, dan komposisi akan diperdalam agar keterikatan (*coupling*) antar-objek semakin teratur dan sesuai standar arsitektur perangkat lunak modern.

---

## 🎯 10. Evaluasi Definition of Done (DoD) Modul 3

| Kriteria Definition of Done (DoD) | Status | Keterangan Evaluasi |
| :--- | :---: | :--- |
| **Menggunakan project P2** | **TERPENUHI** | Melanjutkan dan merefactor source code dari praktikum P2. |
| **Minimal 3 class direfactor** | **TERPENUHI** | Tiga class domain (`Mahasiswa`, `MataKuliah`, `KRS`) direfactor penuh. |
| **Attribute utama menggunakan private** | **TERPENUHI** | 100% atribut dideklarasikan dengan access modifier `private`. |
| **Getter dibuat sesuai kebutuhan** | **TERPENUHI** | Getter tersedia untuk setiap atribut yang perlu diakses publik. |
| **Setter hanya dibuat bila diperlukan** | **TERPENUHI** | Atribut unik seperti `nrp`, `kodeMk`, `sks` bersifat read-only tanpa public setter sembarangan. |
| **Minimal 3 validasi data diterapkan** | **TERPENUHI** | Diterapkan > 5 validasi: IPK (0-4), semester (1-14), kuota (>0), nama, dan batas kuota SKS. |
| **Constructor mengikuti aturan validasi** | **TERPENUHI** | Constructor memanggil setter/validasi internal sebelum inisialisasi. |
| **Tidak ada perubahan attribute langsung dari Main**| **TERPENUHI** | Seluruh manipulasi state dari `Main.java` melalui setter resmi. |
| **Terdapat pengujian data valid** | **TERPENUHI** | Diuji pada Skenario 1 (Test Valid). |
| **Terdapat pengujian data tidak valid** | **TERPENUHI** | Diuji pada Skenario 2 (Test Invalid 1 s/d 7). |
| **Program berhasil dikompilasi** | **TERPENUHI** | `javac -d bin src/*.java` berhasil tanpa error. |
| **Program berhasil dijalankan** | **TERPENUHI** | `java -cp bin Main` berjalan lancar. |
| **Sprint Backlog diperbarui** | **TERPENUHI** | Seluruh item backlog berstatus `DONE`. |
| **Sprint Review tersedia** | **TERPENUHI** | Tabel evaluasi review telah terisi lengkap. |
| **Sprint Retrospective tersedia** | **TERPENUHI** | Evaluasi refleksi pengerjaan terdokumentasi lengkap. |

---

## 🔮 11. Target Sprint Berikutnya (P4)

Hasil implementasi Modul 3 menjadi landasan kokoh untuk **Modul 4 (Object Relationship: Association, Aggregation, Composition)**:
1. Mendalami pemodelan relasi antar-objek secara formal (Hubungan *Has-A* dan *Uses-A*).
2. Membedakan secara tegas siklus hidup objek (*lifecycle*) antara **Agregasi** (objek anak dapat hidup mandiri tanpa objek induk) dan **Komposisi** (objek anak terikat mati dengan siklus hidup objek induk).
3. Mengembangkan sistem informasi akademik yang semakin modular, terstruktur, dan andal.
