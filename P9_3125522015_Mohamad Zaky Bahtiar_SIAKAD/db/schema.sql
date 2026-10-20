-- ====================================================================
-- SISTEM INFORMASI AKADEMIK (SIAKAD) - SKEMA BASIS DATA RELASIONAL
-- Mata Kuliah : Pemrograman Berorientasi Obyek (Praktek Pertemuan 9)
-- Mahasiswa   : Mohamad Zaky Bahtiar Arifianto (NIM: 3125522015)
-- ====================================================================

-- 1. Tabel Master Dosen
CREATE TABLE IF NOT EXISTS dosen (
    nip VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    kuota_bimbingan INT NOT NULL CHECK (kuota_bimbingan >= 0),
    spesialisasi VARCHAR(100) NOT NULL
);

-- 2. Tabel Master Mahasiswa
CREATE TABLE IF NOT EXISTS mahasiswa (
    nim VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    semester INT NOT NULL CHECK (semester > 0),
    max_sks INT NOT NULL CHECK (max_sks BETWEEN 1 AND 24),
    status VARCHAR(20) NOT NULL DEFAULT 'AKTIF'
);

-- 3. Tabel Master Mata Kuliah
CREATE TABLE IF NOT EXISTS mata_kuliah (
    kode_mk VARCHAR(15) PRIMARY KEY,
    nama_mk VARCHAR(100) NOT NULL,
    sks INT NOT NULL CHECK (sks BETWEEN 1 AND 6),
    kuota INT NOT NULL CHECK (kuota > 0),
    terisi INT NOT NULL DEFAULT 0 CHECK (terisi >= 0 AND terisi <= kuota),
    semester_buka INT NOT NULL CHECK (semester_buka BETWEEN 1 AND 8)
);

-- 4. Tabel Transaksi Kartu Rencana Studi (KRS)
CREATE TABLE IF NOT EXISTS krs (
    id_krs VARCHAR(30) PRIMARY KEY,
    nim VARCHAR(20) NOT NULL,
    nip_dosen VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT', 'DIAJUKAN', 'DISETUJUI')),
    total_sks INT NOT NULL DEFAULT 0 CHECK (total_sks BETWEEN 0 AND 24),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_krs_mahasiswa FOREIGN KEY (nim) REFERENCES mahasiswa (nim) ON DELETE CASCADE,
    CONSTRAINT fk_krs_dosen FOREIGN KEY (nip_dosen) REFERENCES dosen (nip)
);

-- 5. Tabel Detail Item Mata Kuliah dalam KRS (Agregasi)
CREATE TABLE IF NOT EXISTS krs_detail (
    id_krs VARCHAR(30) NOT NULL,
    kode_mk VARCHAR(15) NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_krs, kode_mk),
    CONSTRAINT fk_detail_krs FOREIGN KEY (id_krs) REFERENCES krs (id_krs) ON DELETE CASCADE,
    CONSTRAINT fk_detail_mk FOREIGN KEY (kode_mk) REFERENCES mata_kuliah (kode_mk)
);

-- ====================================================================
-- SEED DATA AWAL (KONSISTEN DENGAN IN-MEMORY REPOSITORY JAVA)
-- ====================================================================

INSERT INTO dosen (nip, nama, kuota_bimbingan, spesialisasi) VALUES
('198203152008121002', 'Dr. Eng. Ahmad Fauzi, M.Kom.', 25, 'Rekayasa Perangkat Lunak & Basis Data'),
('197906212005011003', 'Ir. Nurul Hidayati, M.T.', 20, 'Kecerdasan Artifisial & Data Science')
ON CONFLICT (nip) DO NOTHING;

INSERT INTO mahasiswa (nim, nama, semester, max_sks, status) VALUES
('3125522015', 'Mohamad Zaky Bahtiar Arifianto', 3, 24, 'AKTIF')
ON CONFLICT (nim) DO NOTHING;

INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, kuota, terisi, semester_buka) VALUES
('IF301', 'Pemrograman Berorientasi Obyek', 3, 30, 0, 3),
('IF302', 'Praktikum Pemrograman Berorientasi Obyek', 1, 30, 0, 3),
('IF303', 'Struktur Data & Algoritma', 3, 28, 0, 3),
('IF304', 'Praktikum Struktur Data & Algoritma', 1, 28, 0, 3),
('IF305', 'Basis Data Lanjut', 3, 32, 0, 3),
('IF306', 'Rekayasa Kebutuhan Perangkat Lunak', 3, 35, 0, 3),
('IF307', 'Pemrograman Web Berbasis Komponen', 3, 30, 0, 3),
('IF308', 'Sistem Operasi', 3, 30, 0, 3),
('IF309', 'Jaringan Komputer', 3, 25, 0, 3),
('IF310', 'Etika Profesi & Komunikasi Ilmiah', 2, 40, 0, 3)
ON CONFLICT (kode_mk) DO NOTHING;

INSERT INTO krs (id_krs, nim, nip_dosen, status, total_sks) VALUES
('KRS-2026-3125522015', '3125522015', '198203152008121002', 'DRAFT', 0)
ON CONFLICT (id_krs) DO NOTHING;
