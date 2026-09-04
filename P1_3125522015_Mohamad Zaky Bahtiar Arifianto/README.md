# 🎓 Sistem Informasi Akademik Mahasiswa (SIAKAD)
### **Praktikum Pemrograman Berorientasi Obyek (PBO) — Modul 1**
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
1. [Deskripsi & Analisis Masalah (Bagian A)](#-1-analisis-permasalahan-bagian-a)
2. [Product Backlog & User Story](#-2-product-backlog-awal-format-agile)
3. [Identifikasi Kandidat Object (Bagian B)](#-3-identifikasi-kandidat-object-bagian-b)
4. [Implementasi Class Java (Bagian C)](#-4-implementasi-class-java-bagian-c)
5. [Struktur Direktori Proyek](#-5-struktur-direktori-proyek)
6. [Cara Kompilasi dan Eksekusi](#-6-cara-kompilasi-dan-menjalankan-program)
7. [Bukti Hasil Running Program](#-7-bukti-hasil-running-program)
8. [Kesesuaian Definition of Done (DoD)](#-8-evaluasi-definition-of-done-dod)

---

## 🎯 1. Analisis Permasalahan (Bagian A)

### 📌 1.1 Nama Proyek
**Sistem Informasi Akademik Mahasiswa (SIAKAD)**

### ❗ 1.2 Latar Belakang Masalah
Pada institusi perguruan tinggi, proses administrasi rencana studi dan pemantauan beban akademik mahasiswa kerap mengalami kendala akibat pencatatan yang terpisah dan manual. Mahasiswa kerap mengalami kesulitan dalam menentukan batas maksimal SKS yang dapat diambil berdasarkan perolehan IPK semester sebelumnya secara otomatis. Di sisi lain, dosen wali dan admin akademik memerlukan mekanisme terstruktur untuk memverifikasi rencana studi mahasiswa.

### 🏆 1.3 Product Goal
> *"Membangun aplikasi Sistem Informasi Akademik sederhana berbasis Java dan OOP untuk mempermudah pengelolaan data mahasiswa, validasi otomatis batas beban SKS berdasarkan capaian IPK, serta pengambilan mata kuliah (KRS) secara terstruktur dan efisien."*

### 👥 1.4 Identifikasi Aktor Sistem
| No | Aktor | Peran & Tanggung Jawab |
|:--:| :--- | :--- |
| **1** | **Mahasiswa** | Mengakses profil akademik pribadi, memantau batas beban SKS, serta melakukan pemilihan mata kuliah ke dalam rencana studi. |
| **2** | **Dosen Wali** | Memantau perkembangan akademik mahasiswa bimbingan serta melakukan review dan validasi pengajuan KRS. |
| **3** | **Admin Akademik** | Mengelola master data akademik (daftar mata kuliah, dosen pengampu, kurikulum, dan registrasi mahasiswa). |

---

## 📋 2. Product Backlog Awal (Format Agile)

Penyusunan backlog menggunakan format standar Agile User Story:  
`Sebagai [aktor], saya ingin [fitur], sehingga [manfaat]`

| ID | Peran (*Sebagai*) | Kebutuhan (*Saya Ingin*) | Manfaat (*Sehingga*) | Prioritas |
| :---: | :--- | :--- | :--- | :---: |
| **US-01** | Mahasiswa | Melihat data profil dan status akademik pribadi | Dapat memastikan identitas dan batas beban SKS semester aktif valid | `High` |
| **US-02** | Mahasiswa | Memilih dan mengambil mata kuliah ke dalam KRS | Mata kuliah terdaftar selama tidak melampaui batas maksimal beban SKS | `High` |
| **US-03** | Mahasiswa | Melihat ringkasan total SKS dan daftar MK yang diambil | Memiliki rekap rencana studi semester aktif yang jelas dan terstruktur | `High` |
| **US-04** | Dosen Wali | Memvalidasi pengajuan KRS mahasiswa bimbingan | Rencana studi mahasiswa dipastikan sesuai kurikulum dan aturan IPK | `Medium` |
| **US-05** | Admin Akademik | Mengelola data master mata kuliah dan kapasitas kelas | Data perkuliahan selalu mutakhir dan terintegrasi di dalam sistem | `Medium` |

---

## 🧩 3. Identifikasi Kandidat Object (Bagian B)

Berikut adalah 3 kandidat entitas objek utama yang dimodelkan dari domain Sistem Informasi Akademik:

| Kandidat Object | State / Data (Atribut) | Behavior (Method / Fungsi) |
| :--- | :--- | :--- |
| **Mahasiswa**<br/>*(Dipilih untuk Implementasi P1)* | • `nrp` : String<br/>• `nama` : String<br/>• `prodi` : String<br/>• `semester` : int<br/>• `ipk` : double<br/>• `totalSks` : int | • `tampilkanData()` : Menampilkan profil lengkap & SKS<br/>• `hitungBebanMaksimalSks()` : Menghitung kuota SKS (15-24 SKS)<br/>• `ambilMataKuliah(String namaMk, int sks)` : Menambah MK & validasi batas SKS |
| **MataKuliah** | • `kodeMk` : String<br/>• `namaMk` : String<br/>• `sks` : int<br/>• `semesterBuka` : int<br/>• `dosenPengampu` : String | • `tampilkanInfoMk()` : Menampilkan detail info mata kuliah<br/>• `cekKapasitasKelas()` : Memeriksa ketersediaan kursi kelas<br/>• `cekPrasyarat()` : Memeriksa syarat kelulusan MK prasyarat |
| **KRS (Rencana Studi)** | • `nomorKrs` : String<br/>• `tahunAjaran` : String<br/>• `semesterKrs` : int<br/>• `statusValidasi` : boolean | • `tambahMataKuliah()` : Memasukkan MK ke kartu rencana studi<br/>• `hitungTotalSks()` : Menghitung akumulasi beban SKS semester<br/>• `validasiKrs()` : Menyetujui pengajuan KRS oleh dosen wali |

---

## 💻 4. Implementasi Class Java (Bagian C)

Sesuai instruksi modul, objek **`Mahasiswa`** dipilih untuk diimplementasikan ke dalam class Java mandiri:

### 📄 `src/Mahasiswa.java`
```java
public class Mahasiswa {
    // State / Atribut Data
    public String nrp;
    public String nama;
    public String prodi;
    public int semester;
    public double ipk;
    public int totalSks;

    // Behavior 1: Menampilkan Data Profil dan Akademik
    public void tampilkanData() {
        System.out.println("--------------------------------------------------");
        System.out.println("            DATA AKADEMIK MAHASISWA               ");
        System.out.println("--------------------------------------------------");
        System.out.println("NRP              : " + nrp);
        System.out.println("Nama Mahasiswa   : " + nama);
        System.out.println("Program Studi    : " + prodi);
        System.out.println("Semester         : " + semester);
        System.out.println("IPK Terakhir     : " + String.format("%.2f", ipk));
        System.out.println("Batas Maks. SKS  : " + hitungBebanMaksimalSks() + " SKS");
        System.out.println("Total SKS Diambil: " + totalSks + " SKS");
        System.out.println("--------------------------------------------------");
    }

    // Behavior 2: Menghitung Batas Maksimal SKS Berdasarkan IPK
    public int hitungBebanMaksimalSks() {
        if (ipk >= 3.00) {
            return 24;
        } else if (ipk >= 2.50) {
            return 21;
        } else if (ipk >= 2.00) {
            return 18;
        } else {
            return 15;
        }
    }

    // Behavior 3: Memproses Pengambilan Mata Kuliah (KRS)
    public void ambilMataKuliah(String namaMk, int sks) {
        int batasMaks = hitungBebanMaksimalSks();
        System.out.println("\n>> Proses Pengambilan Mata Kuliah: " + namaMk + " (" + sks + " SKS)");
        if (totalSks + sks <= batasMaks) {
            totalSks += sks;
            System.out.println("   [STATUS: BERHASIL] Mata kuliah berhasil ditambahkan ke KRS.");
            System.out.println("   Total SKS saat ini: " + totalSks + " / " + batasMaks + " SKS");
        } else {
            System.out.println("   [STATUS: GAGAL] Melebihi batas maksimal SKS (" + batasMaks + " SKS)!");
        }
    }
}
```

### 📄 `src/Main.java`
```java
public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  PROJECT KICKOFF - SISTEM INFORMASI AKADEMIK");
        System.out.println("     Praktikum Pemrograman Berorientasi Obyek     ");
        System.out.println("==================================================");
        System.out.println("Pengembang : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP        : 3125522015\n");

        // Instansiasi Object dari Class Mahasiswa
        Mahasiswa mhs1 = new Mahasiswa();

        // Mengisi State / Atribut Data Objek
        mhs1.nrp = "3125522015";
        mhs1.nama = "Mohamad Zaky Bahtiar Arifianto";
        mhs1.prodi = "D3 Teknik Informatika";
        mhs1.semester = 3;
        mhs1.ipk = 3.75;
        mhs1.totalSks = 0;

        // Memanggil Behavior: Menampilkan Data Mahasiswa
        mhs1.tampilkanData();

        // Simulasi Behavior: Pengambilan Mata Kuliah (KRS)
        mhs1.ambilMataKuliah("Pemrograman Berorientasi Obyek", 3);
        mhs1.ambilMataKuliah("Praktikum PBO", 2);
        mhs1.ambilMataKuliah("Basis Data Lanjut", 3);
        mhs1.ambilMataKuliah("Rekayasa Perangkat Lunak", 3);

        // Menampilkan Rekap Akhir Data Mahasiswa
        System.out.println("\n[REKAP AKHIR]");
        mhs1.tampilkanData();
    }
}
```

---

## 📂 5. Struktur Direktori Proyek

```text
P1_3125522015_Mohamad Zaky Bahtiar Arifianto/
│
├── 3125522015_Mohamad Zaky Bahtiar Arifianto.docx  # Laporan Resmi Praktikum
├── README.md                                       # Dokumentasi Proyek GitHub
├── Modul 1- Pengenalan Java, OOP, dan Project Kickoff Berbasis Agile.pdf
│
└── src/
    ├── Mahasiswa.java                              # Implementasi Class Objek Domain
    └── Main.java                                   # Entry Point & Pengujian Program
```

---

## 🚀 6. Cara Kompilasi dan Menjalankan Program

Buka terminal pada direktori proyek dan jalankan perintah berikut:

### ⚙️ 1. Kompilasi Kode Java
```bash
javac src/*.java
```

### ▶️ 2. Eksekusi Program
```bash
java -cp src Main
```

---

## 🖥️ 7. Bukti Hasil Running Program

```text
==================================================
  PROJECT KICKOFF - SISTEM INFORMASI AKADEMIK
     Praktikum Pemrograman Berorientasi Obyek     
==================================================
Pengembang : Mohamad Zaky Bahtiar Arifianto
NRP        : 3125522015

--------------------------------------------------
            DATA AKADEMIK MAHASISWA               
--------------------------------------------------
NRP              : 3125522015
Nama Mahasiswa   : Mohamad Zaky Bahtiar Arifianto
Program Studi    : D3 Teknik Informatika
Semester         : 3
IPK Terakhir     : 3,75
Batas Maks. SKS  : 24 SKS
Total SKS Diambil: 0 SKS
--------------------------------------------------

>> Proses Pengambilan Mata Kuliah: Pemrograman Berorientasi Obyek (3 SKS)
   [STATUS: BERHASIL] Mata kuliah berhasil ditambahkan ke KRS.
   Total SKS saat ini: 3 / 24 SKS

>> Proses Pengambilan Mata Kuliah: Praktikum PBO (2 SKS)
   [STATUS: BERHASIL] Mata kuliah berhasil ditambahkan ke KRS.
   Total SKS saat ini: 5 / 24 SKS

>> Proses Pengambilan Mata Kuliah: Basis Data Lanjut (3 SKS)
   [STATUS: BERHASIL] Mata kuliah berhasil ditambahkan ke KRS.
   Total SKS saat ini: 8 / 24 SKS

>> Proses Pengambilan Mata Kuliah: Rekayasa Perangkat Lunak (3 SKS)
   [STATUS: BERHASIL] Mata kuliah berhasil ditambahkan ke KRS.
   Total SKS saat ini: 11 / 24 SKS

[REKAP AKHIR]
--------------------------------------------------
            DATA AKADEMIK MAHASISWA               
--------------------------------------------------
NRP              : 3125522015
Nama Mahasiswa   : Mohamad Zaky Bahtiar Arifianto
Program Studi    : D3 Teknik Informatika
Semester         : 3
IPK Terakhir     : 3,75
Batas Maks. SKS  : 24 SKS
Total SKS Diambil: 11 SKS
--------------------------------------------------
```

---

## ✅ 8. Evaluasi Definition of Done (DoD)

| Kriteria Definition of Done | Status | Keterangan |
| :--- | :---: | :--- |
| **Java / JDK dapat digunakan** | ✅ Selesai | JDK 26 terkonfigurasi dengan baik |
| **Program dapat dikompilasi** | ✅ Selesai | `javac` berhasil tanpa error |
| **Program dapat dijalankan** | ✅ Selesai | `java Main` berjalan dengan output valid |
| **Studi kasus telah ditentukan** | ✅ Selesai | Sistem Informasi Akademik Mahasiswa (SIAKAD) |
| **Product Goal tersedia** | ✅ Selesai | Dirumuskan 1-2 kalimat terarah |
| **Minimal 5 User Story tersedia** | ✅ Selesai | 5 User Story format Agile (US-01 s/d US-05) |
| **Minimal 3 kandidat object teridentifikasi** | ✅ Selesai | `Mahasiswa`, `MataKuliah`, dan `KRS` |
| **Minimal 1 class Java dibuat** | ✅ Selesai | Class `Mahasiswa.java` |
| **Minimal 1 object dibuat & digunakan** | ✅ Selesai | Objek `mhs1` berhasil dibuat dan dipanggil method-nya |
| **Bukti hasil running tersedia** | ✅ Selesai | Output terminal console terlampir lengkap |

---
*Dikembangkan oleh Mohamad Zaky Bahtiar Arifianto (3125522015) — Praktikum PBO PENS PSDKU Sumenep (2026)*
