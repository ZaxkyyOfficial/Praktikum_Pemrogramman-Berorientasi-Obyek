# Sistem Manajemen Program Makan Bergizi Gratis (MBG)
## Modul Praktikum 15 (P15) — Demonstrasi Proyek dan Evaluasi Pemahaman Individu

Aplikasi desktop dan konsol interaktif berbasis Java untuk tata kelola operasional Program Makan Bergizi Gratis (MBG) Badan Gizi Nasional Republik Indonesia. Sistem ini mengelola katalog paket menu makanan bergizi, pemantauan batas kecukupan gizi (kalori, protein, karbohidrat), penghitungan estimasi biaya porsi, alokasi stok bahan baku dapur, pelacakan distribusi pengiriman sekolah, hingga mekanisme persistensi berkas berkonsistensi tinggi (*fail-safe file storage*).

---

## 1. Identitas Pengembang & Proyek
- **Nama Pengembang** : Mohamad Zaky Bahtiar Arifianto
- **NRP**              : 3125522015
- **Kelas / Program**  : 2 D3 Teknik Informatika
- **Institusi**         : Politeknik Elektronika Negeri Surabaya (PENS) PSDKU Sumenep
- **Mata Kuliah**       : Praktikum Pemrograman Berorientasi Obyek (PBO)
- **Modul**            : Praktikum 15 — Demonstrasi Proyek dan Evaluasi Pemahaman Individu
- **Dosen Pengampu**   : Nirwana Haidar Hari, S.Pd., M.Kom.

---

## 2. Kebutuhan Sistem & Dependensi (Prerequisites)
- **Sistem Operasi**: Microsoft Windows 10/11 (64-bit), Linux, atau macOS.
- **Java Development Kit (JDK)**: **JDK 17, JDK 21 LTS, atau JDK 26+** (Oracle JDK / OpenJDK / Eclipse Temurin).
- **Pustaka Eksternal (`lib/`)**:
  - `lib/mysql-connector-j-8.3.0.jar` (Konektor Basis Data Relasional).
  - JavaFX SDK 21+ (`javafx-base`, `javafx-controls`, `javafx-fxml`, `javafx-graphics`) jika mengeksekusi antarmuka GUI grafis.
- **Tools Opsional**: Command Prompt, PowerShell, VS Code, Apache NetBeans, atau IntelliJ IDEA.

---

## 3. Struktur Direktori Proyek

```text
P15_3125522015_Mohamad Zaky Bahtiar/
├── bin/                                  # Bytecode Java terkompilasi (.class)
├── lib/                                  # Driver & library eksternal (JAR)
│   ├── javafx-base-21.0.2-win.jar
│   ├── javafx-controls-21.0.2-win.jar
│   ├── javafx-fxml-21.0.2-win.jar
│   ├── javafx-graphics-21.0.2-win.jar
│   └── mysql-connector-j-8.3.0.jar
├── Screenshot/                           # Dokumentasi visual antarmuka & diagram UML
│   ├── 00_terminal_pengujian.png
│   ├── 01_dashboard_utama.png
│   ├── 02_inventaris_validasi_stok.png
│   ├── 03_menu_kalkulasi_gizi.png
│   ├── 04_distribusi_pelacakan.png
│   ├── 05_sekolah_penerima.png
│   └── 06_uml_class_diagram.png
├── src/                                  # Kode sumber Java
│   └── P15_3125522015_MohamadZaky/
│       ├── AppLauncher.java              # Entry point antarmuka grafis JavaFX
│       ├── Main.java                     # Aplikasi konsol CLI & runner pengujian otomatis
│       ├── controller/
│       │   └── MainController.java       # Pengendali GUI JavaFX
│       ├── model/                        # Entitas domain & domain services
│       │   ├── Admin.java
│       │   ├── BahanBaku.java
│       │   ├── DapatDilacak.java         # Interface pelacakan pengiriman
│       │   ├── DetailPengiriman.java
│       │   ├── Driver.java
│       │   ├── Menu.java                 # Entitas Menu dengan penegakan Class Invariant
│       │   ├── PengelolaDistribusi.java
│       │   ├── PengelolaMenu.java        # Domain Service pengelola koleksi & keunikan kode
│       │   ├── Pengguna.java
│       │   ├── Pengiriman.java
│       │   ├── Sekolah.java
│       │   ├── StafDapur.java
│       │   └── StokTidakCukupException.java
│       ├── utils/                        # Data access & file repositories
│       │   ├── DatabaseConnection.java
│       │   ├── MbgFileRepository.java    # File Repository dengan State Invariance Protection
│       │   └── MbgRepository.java
│       └── view/                         # Desain tampilan FXML & CSS GUI
│           ├── MainView.fxml
│           └── style.css
├── DESIGN.md                             # Pedoman desain visual sistem institusional MBG
├── LAPORAN_P15.md                        # Laporan evaluasi demonstrasi & refleksi individu
├── logombg.jpeg                          # Aset visual resmi Badan Gizi Nasional
├── mbg_data.txt                          # Berkas data operasional aktif (format flat-file)
├── mbg_data_backup.txt                   # Berkas data cadangan baku (tanpa data pribadi)
├── mbg_db.sql                            # Skrip skema database MySQL
├── run.bat                               # Skrip batch kompilasi dan eksekusi instan Windows
├── SLIDES_P15.md                         # Draft 5 slide presentasi evaluasi modul P15
├── test_execution_log.txt                # Log rekaman eksekusi pengujian otomatis
├── 3125522015_Mohamad Zaky Bahtiar Arifianto.docx # Dokumen Word resmi Laporan P15
└── 3125522015_Mohamad Zaky Bahtiar Arifianto.pdf  # Dokumen PDF resmi Laporan P15
```

---

## 4. Lokasi Berkas Data & Cadangan Baku

1. **Berkas Data Operasional**: `mbg_data.txt`
   - Berisi rekaman menu gizi yang aktif dibaca dan ditulis oleh aplikasi saat runtime.
2. **Berkas Cadangan Baku (Sample Data)**: `mbg_data_backup.txt`
   - Berisi data contoh bersih tanpa informasi pribadi (*Personal Identifiable Information / PII*) untuk keperluan pemulihan (*rollback*) saat demonstrasi:
   ```text
   MNU-01|Paket A: Nasi Ayam Teriyaki & Tumis Sayur|680.0|28.5|82.0|15000.0
   MNU-02|Paket B: Nasi Telur Balado & Sayur Sop|640.0|24.0|79.0|14000.0
   ```

---

## 5. Panduan Kompilasi & Eksekusi

### Opsi A: Eksekusi Otomatis Menggunakan Skrip Batch (Windows)
Cukup jalankan berkas batch yang telah disediakan:
```cmd
run.bat
```
Skrip ini akan memeriksa keberadaan folder `bin/`, melakukan kompilasi seluruh file Java, dan mengeksekusi aplikasi konsol secara langsung.

---

### Opsi B: Kompilasi & Eksekusi Manual via Terminal / Command Prompt

Buka terminal pada direktori `P15_3125522015_Mohamad Zaky Bahtiar/`:

#### 1. Kompilasi Kode Sumber Java
```cmd
if not exist "bin" mkdir "bin"
javac -cp "lib\mysql-connector-j-8.3.0.jar" -d "bin" src\P15_3125522015_MohamadZaky\*.java src\P15_3125522015_MohamadZaky\model\*.java src\P15_3125522015_MohamadZaky\utils\*.java
```

#### 2. Menjalankan Aplikasi Konsol Interaktif (Mode Utama Demonstrasi)
```cmd
java -cp "bin;lib\mysql-connector-j-8.3.0.jar" P15_3125522015_MohamadZaky.Main
```

#### 3. Menjalankan Mode Uji Regresi & Ketahanan Otomatis (10 Skenario)
Untuk membuktikan ketahanan sistem secara instan di hadapan dosen:
```cmd
java -cp "bin;lib\mysql-connector-j-8.3.0.jar" P15_3125522015_MohamadZaky.Main --test
```

#### 4. Menjalankan Antarmuka Grafis (GUI JavaFX) — Opsional
Pastikan JavaFX SDK tersedia pada classpath / module-path:
```cmd
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib\mysql-connector-j-8.3.0.jar" P15_3125522015_MohamadZaky.AppLauncher
```

---

## 6. Panduan Singkat Alur Demonstrasi 7 Menit (Quick Guide)

1. **Menit 01-02 | Arsitektur & Startup**:
   Jalankan `java -cp "bin;lib\mysql-connector-j-8.3.0.jar" P15_3125522015_MohamadZaky.Main`. Tunjukkan bahwa aplikasi otomatis memuat 2 data awal dari `mbg_data.txt`.
2. **Menit 03 | Alur Normal (Tambah Menu Baru)**:
   Pilih Opsi 1, masukkan kode `MNU-03`, nama `Paket C: Nasi Gurame Bakar Madu`. Tunjukkan data berhasil ditambahkan dan status perubahan berubah menjadi `[Ada perubahan belum disimpan!]`.
3. **Menit 04 | Penolakan Kondisi Gagal (Kode Duplikat & Karakter Terlarang)**:
   Pilih Opsi 1 lagi, masukkan kode `MNU-01`. Sistem menolak seketika (*fail-fast*): `Data ditolak: Kode menu 'MNU-01' sudah terdaftar.` Masukkan nama mengandung `|` untuk membuktikan perlindungan format flat-file.
4. **Menit 05 | Konsistensi State & Persistensi**:
   Pilih Opsi 2 untuk membuktikan koleksi memori tetap utuh (3 data). Pilih Opsi 5 untuk menyimpan ke disk. Status kembali menjadi `[Semua perubahan tersimpan]`.
5. **Menit 06 | Bukti Keandalan Berkas (Tutup & Jalankan Ulang)**:
   Pilih Opsi 0 untuk keluar. Jalankan ulang program. Pilih Opsi 2: data `MNU-03` terbukti tetap ada dan termuat utuh dari disk.
6. **Menit 07 | Sesi Tanya Jawab & Penelusuran Kode**:
   Buka `Menu.java` (validasi enkapsulasi), `PengelolaMenu.java` (keunikan kode), dan `MbgFileRepository.java` (proteksi kegagalan baca).

