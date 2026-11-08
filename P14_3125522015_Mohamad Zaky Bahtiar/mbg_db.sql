-- Skema Basis Data Sistem Manajemen Program Makan Bergizi Gratis (MBG)
-- Proyek: P10_3125522015_MohamadZaky
-- Catatan Data: Seluruh baris awal di bawah merupakan data simulasi praktikum PBO Pertemuan 10.

CREATE DATABASE IF NOT EXISTS mbg_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mbg_db;

CREATE TABLE IF NOT EXISTS pengguna (
    id_pengguna VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    peran ENUM('ADMIN', 'STAF_DAPUR', 'DRIVER') NOT NULL,
    atribut_khusus VARCHAR(150) NOT NULL,
    nomor_armada VARCHAR(30) DEFAULT NULL,
    lokasi_terkini VARCHAR(150) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS bahan_baku (
    kode_bahan VARCHAR(20) PRIMARY KEY,
    nama_bahan VARCHAR(100) NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    stok INT NOT NULL CHECK (stok >= 0),
    satuan VARCHAR(20) NOT NULL,
    harga_satuan DOUBLE NOT NULL CHECK (harga_satuan >= 0),
    tanggal_kedaluwarsa DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS sekolah (
    npsn VARCHAR(20) PRIMARY KEY,
    nama_sekolah VARCHAR(120) NOT NULL,
    jenjang VARCHAR(30) NOT NULL,
    alamat VARCHAR(200) NOT NULL,
    jumlah_siswa INT NOT NULL CHECK (jumlah_siswa > 0),
    koordinat_wilayah VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS menu_gizi (
    kode_menu VARCHAR(20) PRIMARY KEY,
    nama_menu VARCHAR(100) NOT NULL,
    kalori_per_porsi DOUBLE NOT NULL,
    protein_gram DOUBLE NOT NULL,
    karbohidrat_gram DOUBLE NOT NULL,
    biaya_per_porsi DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS menu_komposisi (
    kode_menu VARCHAR(20) NOT NULL,
    kode_bahan VARCHAR(20) NOT NULL,
    kebutuhan_per_100_porsi INT NOT NULL CHECK (kebutuhan_per_100_porsi > 0),
    PRIMARY KEY (kode_menu, kode_bahan),
    CONSTRAINT fk_komposisi_menu FOREIGN KEY (kode_menu) REFERENCES menu_gizi (kode_menu) ON DELETE CASCADE,
    CONSTRAINT fk_komposisi_bahan FOREIGN KEY (kode_bahan) REFERENCES bahan_baku (kode_bahan) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pengiriman (
    id_pengiriman VARCHAR(25) PRIMARY KEY,
    npsn_sekolah VARCHAR(20) NOT NULL,
    id_driver VARCHAR(20) NOT NULL,
    kode_menu VARCHAR(20) NOT NULL,
    jumlah_porsi INT NOT NULL CHECK (jumlah_porsi > 0),
    status_distribusi VARCHAR(25) NOT NULL,
    lokasi_terakhir VARCHAR(150) NOT NULL,
    waktu_berangkat VARCHAR(30) NOT NULL,
    CONSTRAINT fk_kirim_sekolah FOREIGN KEY (npsn_sekolah) REFERENCES sekolah (npsn) ON DELETE CASCADE,
    CONSTRAINT fk_kirim_driver FOREIGN KEY (id_driver) REFERENCES pengguna (id_pengguna) ON DELETE CASCADE,
    CONSTRAINT fk_kirim_menu FOREIGN KEY (kode_menu) REFERENCES menu_gizi (kode_menu) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS detail_pengiriman (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_pengiriman VARCHAR(25) NOT NULL,
    nomor_batch VARCHAR(30) NOT NULL,
    jenis_kemasan VARCHAR(60) NOT NULL,
    jumlah_porsi_batch INT NOT NULL,
    suhu_penyimpanan DOUBLE NOT NULL,
    catatan_inspeksi VARCHAR(150) NOT NULL,
    CONSTRAINT fk_detail_pengiriman FOREIGN KEY (id_pengiriman) REFERENCES pengiriman (id_pengiriman) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS anggaran_program (
    id_anggaran VARCHAR(20) PRIMARY KEY,
    pagu_total DOUBLE NOT NULL,
    dana_terpakai DOUBLE NOT NULL,
    keterangan VARCHAR(150) NOT NULL
);

INSERT IGNORE INTO anggaran_program (id_anggaran, pagu_total, dana_terpakai, keterangan) VALUES
('ANG-2026-Q4', 250000000, 68450000, '[DATA SIMULASI] Alokasi Dapur Umum MBG Wilayah Sumenep Kuartal IV');

INSERT IGNORE INTO pengguna (id_pengguna, nama, email, peran, atribut_khusus, nomor_armada, lokasi_terkini) VALUES
('USR-ADM-01', 'Mohamad Zaky Bahtiar', 'zaky.bahtiar@bgn.simulasi.id', 'ADMIN', 'Wilayah Koordinasi Sumenep Kota', NULL, 'Posko Utama BGN Sumenep'),
('USR-DPR-01', 'Rina Kartika Sari', 'rina.dapur@bgn.simulasi.id', 'STAF_DAPUR', 'Dapur Pusat Sektor Utara - Shift Pagi', NULL, 'Instalasi Dapur Pusat'),
('USR-DRV-01', 'Hendro Wibowo', 'hendro.driver@bgn.simulasi.id', 'DRIVER', 'Rute Distribusi Kecamatan Kota', 'M-8821-VP', 'Jl. Trunojoyo KM 3, Sumenep'),
('USR-DRV-02', 'Agus Salim Pratama', 'agus.driver@bgn.simulasi.id', 'DRIVER', 'Rute Distribusi Kecamatan Kalianget', 'M-8824-VP', 'Simpang Tiga Kalianget'),
('USR-DRV-03', 'Budi Santoso', 'budi.driver@bgn.simulasi.id', 'DRIVER', 'Rute Distribusi Kecamatan Batuan', 'M-8829-VP', 'Gudang Logistik Dapur Pusat');

INSERT IGNORE INTO bahan_baku (kode_bahan, nama_bahan, kategori, stok, satuan, harga_satuan, tanggal_kedaluwarsa) VALUES
('BHN-001', 'Beras Premium Fortifikasi', 'Karbohidrat', 120, 'kg', 14500, '2027-03-15'),
('BHN-002', 'Dada Ayam Fillet Segar', 'Protein Hewani', 5, 'kg', 38000, '2026-11-02'),
('BHN-003', 'Telur Ayam Negeri', 'Protein Hewani', 45, 'kg', 27000, '2026-11-12'),
('BHN-004', 'Tahu Putih Padat', 'Protein Nabati', 18, 'kg', 12000, '2026-10-29'),
('BHN-005', 'Wortel & Buncis Segar', 'Sayuran', 32, 'kg', 11000, '2026-11-01'),
('BHN-006', 'Susu UHT Kotak 200ml', 'Pelengkap Gizi', 250, 'liter', 16500, '2027-01-20'),
('BHN-007', 'Ikan Tongkol Fillet', 'Protein Hewani', 8, 'kg', 34000, '2026-10-25');

INSERT IGNORE INTO sekolah (npsn, nama_sekolah, jenjang, alamat, jumlah_siswa, koordinat_wilayah) VALUES
('20529901', 'SDN Pajagalan I Sumenep [Simulasi]', 'SD', 'Jl. KH. Mansyur No. 14, Kota Sumenep', 320, '-7.0054, 113.8602'),
('20529902', 'SMPN 1 Sumenep [Simulasi]', 'SMP', 'Jl. Payudan Barat No. 9, Kota Sumenep', 640, '-7.0089, 113.8547'),
('20529903', 'SDN Kalianget Timur I [Simulasi]', 'SD', 'Jl. Raya Pelabuhan Kalianget No. 4', 280, '-7.0421, 113.9315'),
('20529904', 'SMAN 1 Sumenep [Simulasi]', 'SMA', 'Jl. Payudan Timur No. 1, Kota Sumenep', 780, '-7.0098, 113.8611'),
('20529905', 'SDN Batuan I [Simulasi]', 'SD', 'Jl. Raya Lenteng KM 4, Batuan', 210, '-7.0185, 113.8320');

INSERT IGNORE INTO menu_gizi (kode_menu, nama_menu, kalori_per_porsi, protein_gram, karbohidrat_gram, biaya_per_porsi) VALUES
('MNU-01', 'Paket A: Nasi Ayam Teriyaki & Tumis Sayur + Susu', 680.0, 28.5, 82.0, 15000.0),
('MNU-02', 'Paket B: Nasi Telur Balado, Tahu Bacem & Capcay + Susu', 640.0, 24.0, 79.0, 14000.0),
('MNU-03', 'Paket C: Nasi Ikan Tongkol Bumbu Kuning & Sayur Bening', 660.0, 27.0, 80.5, 14500.0);

INSERT IGNORE INTO menu_komposisi (kode_menu, kode_bahan, kebutuhan_per_100_porsi) VALUES
('MNU-01', 'BHN-001', 12),
('MNU-01', 'BHN-002', 10),
('MNU-01', 'BHN-005', 8),
('MNU-01', 'BHN-006', 20),
('MNU-02', 'BHN-001', 12),
('MNU-02', 'BHN-003', 8),
('MNU-02', 'BHN-004', 5),
('MNU-02', 'BHN-005', 7),
('MNU-02', 'BHN-006', 20),
('MNU-03', 'BHN-001', 12),
('MNU-03', 'BHN-007', 9),
('MNU-03', 'BHN-005', 8);

INSERT IGNORE INTO pengiriman (id_pengiriman, npsn_sekolah, id_driver, kode_menu, jumlah_porsi, status_distribusi, lokasi_terakhir, waktu_berangkat) VALUES
('DST-20261028-01', '20529901', 'USR-DRV-01', 'MNU-01', 320, 'Delivered', 'SDN Pajagalan I Sumenep (Tiba)', '07:15 WIB'),
('DST-20261028-02', '20529902', 'USR-DRV-02', 'MNU-02', 640, 'In Transit', 'Jl. Payudan Barat KM 1.5 (Dalam Perjalanan)', '07:45 WIB'),
('DST-20261028-03', '20529903', 'USR-DRV-03', 'MNU-01', 280, 'Pending', 'Gudang Logistik Dapur Pusat (Menunggu Muat)', '08:15 WIB'),
('DST-20261028-04', '20529905', 'USR-DRV-01', 'MNU-03', 210, 'Delivered', 'SDN Batuan I (Tiba)', '06:50 WIB');

INSERT IGNORE INTO detail_pengiriman (id_pengiriman, nomor_batch, jenis_kemasan, jumlah_porsi_batch, suhu_penyimpanan, catatan_inspeksi) VALUES
('DST-20261028-01', 'BATCH-A1', 'Food Tray Stainless 304', 160, 65.5, 'Segel termal utuh, suhu terverifikasi'),
('DST-20261028-01', 'BATCH-A2', 'Food Tray Stainless 304', 160, 64.8, 'Diterima koordinator UKS sekolah'),
('DST-20261028-02', 'BATCH-B1', 'Insulated Box Food Grade', 320, 66.0, 'Armada melewati pos pemeriksaan kota'),
('DST-20261028-02', 'BATCH-B2', 'Insulated Box Food Grade', 320, 65.2, 'Suhu kabin stabil'),
('DST-20261028-03', 'BATCH-C1', 'Food Tray Stainless 304', 280, 68.0, 'Menunggu verifikasi surat jalan dapur'),
('DST-20261028-04', 'BATCH-D1', 'Food Tray Stainless 304', 210, 65.0, 'Selesai disalurkan tepat waktu');

