-- =====================================================================
-- DATABASE SISTEM INFORMASI KLINIK (db_klinik)
-- Mahasiswa : Mohamad Zaky Bahtiar Arifianto
-- NRP       : 3125522015
-- Kelas     : 3D3PSDKU-SM-IT A
-- Dosen     : Nirwana Haidar Hari, S.Pd., M.Kom.
-- Institusi : PENS PSDKU Sumenep
-- Mata Kuliah : Praktikum Pemrograman Berorientasi Obyek (PBO) - UTS Tipe B
-- =====================================================================

CREATE DATABASE IF NOT EXISTS db_klinik;
USE db_klinik;

-- 1. Tabel Pasien
DROP TABLE IF EXISTS transaksi_pembayaran;
DROP TABLE IF EXISTS rekam_medis;
DROP TABLE IF EXISTS tenaga_medis;
DROP TABLE IF EXISTS pasien;

CREATE TABLE pasien (
    id_pasien VARCHAR(20) PRIMARY KEY,
    nama_pasien VARCHAR(100) NOT NULL,
    usia INT NOT NULL,
    status_bpjs TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Tabel Tenaga Medis (Superclass TenagaMedis -> Subclass Dokter & Perawat)
CREATE TABLE tenaga_medis (
    id_medis VARCHAR(20) PRIMARY KEY,
    tipe_medis ENUM('Dokter', 'Perawat') NOT NULL,
    nama VARCHAR(100) NOT NULL,
    spesialisasi VARCHAR(100) NOT NULL,
    biaya_konsultasi DOUBLE DEFAULT 0.0,
    jam_lembur INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Tabel Rekam Medis (Relasi Many-to-One dengan Pasien dan Tenaga Medis)
CREATE TABLE rekam_medis (
    id_rekam_medis VARCHAR(20) PRIMARY KEY,
    id_pasien VARCHAR(20) NOT NULL,
    id_medis VARCHAR(20) NOT NULL,
    keluhan TEXT NOT NULL,
    diagnosa TEXT NOT NULL,
    tindakan TEXT NOT NULL,
    tanggal_periksa DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rekam_pasien FOREIGN KEY (id_pasien) 
        REFERENCES pasien(id_pasien) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_rekam_medis FOREIGN KEY (id_medis) 
        REFERENCES tenaga_medis(id_medis) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Tabel Transaksi Pembayaran (Relasi One-to-One / Many-to-One dengan Rekam Medis)
CREATE TABLE transaksi_pembayaran (
    id_transaksi VARCHAR(20) PRIMARY KEY,
    id_rekam_medis VARCHAR(20) NOT NULL,
    biaya_layanan DOUBLE NOT NULL,
    diskon_bpjs DOUBLE NOT NULL DEFAULT 0.0,
    total_bayar DOUBLE NOT NULL,
    metode_pembayaran VARCHAR(50) NOT NULL,
    tanggal_transaksi DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaksi_rekam FOREIGN KEY (id_rekam_medis) 
        REFERENCES rekam_medis(id_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================================
-- SEED DATA (DATA AWAL UNTUK PENGUJIAN DAN DEMONSTRASI)
-- =====================================================================

-- Seed Pasien (BPJS & Non-BPJS)
INSERT INTO pasien (id_pasien, nama_pasien, usia, status_bpjs) VALUES
('PAS-001', 'Ahmad Dahlan', 45, 1),
('PAS-002', 'Siti Rahmawati', 28, 0),
('PAS-003', 'Budi Santoso', 62, 1),
('PAS-004', 'Putri Ayu', 19, 0);

-- Seed Tenaga Medis (Dokter dan Perawat)
INSERT INTO tenaga_medis (id_medis, tipe_medis, nama, spesialisasi, biaya_konsultasi, jam_lembur) VALUES
('DOC-001', 'Dokter', 'dr. Farhan Malik, Sp.PD', 'Penyakit Dalam', 150000, 0),
('DOC-002', 'Dokter', 'dr. Annisa Putri, Sp.A', 'Spesialis Anak', 120000, 0),
('NUR-001', 'Perawat', 'Ns. Hendra Pratama, S.Kep', 'Keperawatan Medikal Bedah', 0, 4),
('NUR-002', 'Perawat', 'Ns. Maya Lestari, S.Kep', 'Gawat Darurat & Rawat Jalan', 0, 2);

-- Seed Rekam Medis
INSERT INTO rekam_medis (id_rekam_medis, id_pasien, id_medis, keluhan, diagnosa, tindakan, tanggal_periksa) VALUES
('RM-001', 'PAS-001', 'DOC-001', 'Demam tinggi 3 hari dan mual', 'Gastritis Akut & Febris', 'Pemberian Antipiretik & Antasida Intravena', '2026-10-13 08:30:00'),
('RM-002', 'PAS-002', 'NUR-001', 'Luka robek pada lengan kiri bawah', 'Vulnus Laceratum Regio Antebrachii', 'Wound Debridement dan Penjahitan Luka 4 Jahitan', '2026-10-13 09:15:00'),
('RM-003', 'PAS-003', 'DOC-001', 'Pusing berputar dan tengkuk tegang', 'Hipertensi Grade II', 'Edukasi diet rendah garam dan Amlodipine 10mg', '2026-10-13 09:45:00');

-- Seed Transaksi Pembayaran
-- RM-001: Biaya layanan 250000, Diskon BPJS 50% (125000), Total: 125000
INSERT INTO transaksi_pembayaran (id_transaksi, id_rekam_medis, biaya_layanan, diskon_bpjs, total_bayar, metode_pembayaran, tanggal_transaksi) VALUES
('TRX-001', 'RM-001', 250000, 125000, 125000, 'BPJS Kesehatan', '2026-10-13 09:00:00'),
('TRX-002', 'RM-002', 150000, 0, 150000, 'Tunai (Cash)', '2026-10-13 09:40:00');
