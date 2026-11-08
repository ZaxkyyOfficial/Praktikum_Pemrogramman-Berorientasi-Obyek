# Design Direction: Sistem Manajemen Program Makan Bergizi Gratis (MBG)

## Design Read
Reading this as: Aplikasi Desktop Enterprise Operasional untuk Pengelola Dapur & Logistik Badan Gizi Nasional (MBG), dengan bahasa visual institusional bersih beridentitas Navy-Gold korporat, dial `ENERGY 2 / RHYTHM 2 / MOTION 1`.

## Identity & Mood
- **Organisasi**: Badan Gizi Nasional Republik Indonesia (`logombg.jpeg`).
- **Karakter**: Formal, presisi logistik, transparan, dan berfokus pada keputusan operasional harian (ketahanan stok bahan baku, kecukupan gizi porsi, dan ketepatan distribusi sekolah).
- **Dials**:
  - `ENERGY 2` (Balanced): Kontras tegas antara navigasi Navy gelap (`#0B2545`) dengan area kerja terang (`#F4F7FA`) dan aksen Gold (`#C59B27`).
  - `RHYTHM 2` (Consistent with deliberate variation): Area Master-Detail menyesuaikan konteks tugas per modul (ringkasan & grafik alokasi pada Dashboard, tabel & panel inspeksi pada Inventaris/Distribusi/Sekolah, serta kalkulator komposisi dua arah pada Menu & Gizi).
  - `MOTION 1` (Hover & focus states): Transisi warna pada fokus keyboard dan hover tanpa animasi berulang yang mengganggu konsentrasi operator.

## Color Palette (R-01, R-25, R-29)
1. **Core 1 - Navy Badan Gizi (`#0B2545` ke `#134074`)**: Digunakan pada sidebar vertikal dan header utama untuk memisahkan navigasi global dari area kerja (hirarki struktural).
2. **Core 2 - Surface Slate (`#F4F7FA` & `#FFFFFF`)**: Latar belakang area kerja utama dan permukaan kartu/tabel untuk menjaga keterbacaan tabel data padat.
3. **Core 3 - Deep Ink (`#0F172A` & `#334155`)**: Warna teks utama dan sekunder dengan rasio kontras di atas 7:1 terhadap latar terang (memenuhi WCAG AA R-25).
4. **Accent - Emblem Gold (`#C59B27` / `#9A7411` untuk teks)**: Diambil dari cincin dan Garuda pada logo BGN, digunakan khusus sebagai penanda navigasi aktif dan fokus utama (One Deliberate Accent).
5. **Semantic Status Colors**:
   - Hijau Operasional (`#15803D` pada latar `#DCFCE7`): Stok aman / status `Delivered`.
   - Kuning Peringatan (`#B45309` pada latar `#FEF3C7`): Mendekati kedaluwarsa / status `In Transit`.
   - Merah Kritis (`#B91C1C` pada latar `#FEE2E2`): Kedaluwarsa / stok tidak cukup / status `Pending`.

## One-Line Decisions (R-31)
- **Why this color**: Gradasi biru laut tua (`#0B2545`) ke biru korporat (`#134074`) dengan aksen emas (`#C59B27`) diturunkan langsung dari cincin dan perisai logo Badan Gizi Nasional (`logombg.jpeg`) serta memisahkan hirarki navigasi dari kanvas data.
- **Why this layout**: Tata letak Sidebar vertikal di kiri dan Master-Detail di kanan memungkinkan operator memindai tabel di panel Master sambil mengeksekusi form validasi atau melihat rincian komposisi di panel Detail tanpa berpindah layar.
- **Why this typography**: Segoe UI / Sans-Serif sistem berukuran terukur (12px-22px) dipilih untuk ketajaman rendering tabel angka, tanggal kedaluwarsa, dan satuan gramasi pada layar desktop Windows.
- **Why this spacing**: Skala jarak kelipatan 4px/8px (padding 16px-20px pada panel, gap 12px antar kontrol) memberi pemisahan struktural antara grup metrik, tabel, dan form input.
- **Why use cards**: Empat kartu pada Dashboard dan kartu ringkasan pada modul digunakan untuk mengelompokkan metrik kunci yang membutuhkan pemindaian cepat sebelum operator memeriksa rincian tabel.
- **Why use this illustration or icon**: Menggunakan logo resmi `logombg.jpeg` pada identitas sidebar dan indikator teks/simbol status fungsional tanpa ikon dekoratif generik.

