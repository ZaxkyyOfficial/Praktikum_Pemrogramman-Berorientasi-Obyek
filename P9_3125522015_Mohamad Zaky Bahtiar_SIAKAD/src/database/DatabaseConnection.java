package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "db_siakad_pbo";
    private static final String URL_SERVER = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    private static final String URL_DB = "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;
    private static boolean driverLoaded = false;
    private static boolean serverAvailable = false;

    static {
        try {
            Class.forName(DRIVER);
            driverLoaded = true;
        } catch (ClassNotFoundException e) {
            System.err.println("[DATABASE ERROR] Driver MySQL JDBC tidak ditemukan: " + e.getMessage());
            driverLoaded = false;
        }
    }

    private DatabaseConnection() {}
    public static synchronized Connection getConnection() {
        if (!driverLoaded) {
            return null;
        }

        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                pastikanDatabaseTersedia();
                connection = DriverManager.getConnection(URL_DB, USER, PASSWORD);
                serverAvailable = true;
            }
            return connection;
        } catch (SQLException e) {
            serverAvailable = false;
            System.err.println("[DATABASE WARNING] Gagal terhubung ke MySQL XAMPP: " + e.getMessage());
            System.err.println("[DATABASE WARNING] Sistem akan otomatis menggunakan In-Memory Mode fallback.");
            return null;
        }
    }

    public static boolean isConnected() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    private static void pastikanDatabaseTersedia() {
        try (Connection serverConn = DriverManager.getConnection(URL_SERVER, USER, PASSWORD);
             Statement stmt = serverConn.createStatement()) {

            // 1. Buat Database jika belum ada
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

            // 2. Buat Skema Tabel di dalam db_siakad_pbo
            try (Connection dbConn = DriverManager.getConnection(URL_DB, USER, PASSWORD);
                 Statement dbStmt = dbConn.createStatement()) {

                dbStmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS dosen (" +
                                "nip VARCHAR(20) PRIMARY KEY, " +
                                "nama VARCHAR(100) NOT NULL, " +
                                "kuota_bimbingan INT NOT NULL, " +
                                "spesialisasi VARCHAR(100) NOT NULL)"
                );

                dbStmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS mahasiswa (" +
                                "nim VARCHAR(20) PRIMARY KEY, " +
                                "nama VARCHAR(100) NOT NULL, " +
                                "semester INT NOT NULL, " +
                                "max_sks INT NOT NULL, " +
                                "status VARCHAR(20) NOT NULL DEFAULT 'AKTIF')"
                );

                dbStmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS mata_kuliah (" +
                                "kode_mk VARCHAR(15) PRIMARY KEY, " +
                                "nama_mk VARCHAR(100) NOT NULL, " +
                                "sks INT NOT NULL, " +
                                "kuota INT NOT NULL, " +
                                "terisi INT NOT NULL DEFAULT 0, " +
                                "semester_buka INT NOT NULL)"
                );

                dbStmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS krs (" +
                                "id_krs VARCHAR(30) PRIMARY KEY, " +
                                "nim VARCHAR(20) NOT NULL, " +
                                "nip_dosen VARCHAR(20) NOT NULL, " +
                                "status VARCHAR(20) NOT NULL, " +
                                "total_sks INT NOT NULL DEFAULT 0, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                                "CONSTRAINT fk_krs_mahasiswa FOREIGN KEY (nim) REFERENCES mahasiswa (nim) ON DELETE CASCADE, " +
                                "CONSTRAINT fk_krs_dosen FOREIGN KEY (nip_dosen) REFERENCES dosen (nip))"
                );

                dbStmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS krs_detail (" +
                                "id_krs VARCHAR(30) NOT NULL, " +
                                "kode_mk VARCHAR(15) NOT NULL, " +
                                "added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "PRIMARY KEY (id_krs, kode_mk), " +
                                "CONSTRAINT fk_detail_krs FOREIGN KEY (id_krs) REFERENCES krs (id_krs) ON DELETE CASCADE, " +
                                "CONSTRAINT fk_detail_mk FOREIGN KEY (kode_mk) REFERENCES mata_kuliah (kode_mk))"
                );

                // Auto-seed data master jika kosong
                seedMasterData(dbStmt);
            }
        } catch (SQLException e) {
            System.err.println("[DATABASE INIT] Log peringatan auto-provision: " + e.getMessage());
        }
    }

    private static void seedMasterData(Statement stmt) {
        try {
            // Seed Dosen
            stmt.executeUpdate("INSERT IGNORE INTO dosen (nip, nama, kuota_bimbingan, spesialisasi) VALUES " +
                    "('198203152008121002', 'Dr. Eng. Ahmad Fauzi, M.Kom.', 25, 'Rekayasa Perangkat Lunak & Basis Data'), " +
                    "('199008232015042001', 'Siti Aminah, M.Kom.', 20, 'Sistem Informasi & Manajemen Basis Data'), " +
                    "('198504122010121004', 'Bambang Sudarsono, S.T., M.Kom.', 20, 'Jaringan Komputer & Cloud Infrastructure'), " +
                    "('197906212005011003', 'Ir. Nurul Hidayati, M.T.', 20, 'Kecerdasan Artifisial & Data Science')");

            // Seed Mahasiswa (3 Akun Multi-Mahasiswa)
            stmt.executeUpdate("INSERT IGNORE INTO mahasiswa (nim, nama, semester, max_sks, status) VALUES " +
                    "('3125522015', 'Mohamad Zaky Bahtiar Arifianto', 3, 24, 'AKTIF'), " +
                    "('3125522016', 'Alifia Putri Maharani', 3, 24, 'AKTIF'), " +
                    "('3125522017', 'Dimas Arya Pratama', 5, 24, 'AKTIF')");

            // Seed KRS
            stmt.executeUpdate("INSERT IGNORE INTO krs (id_krs, nim, nip_dosen, status, total_sks) VALUES " +
                    "('KRS-2026-3125522015', '3125522015', '198203152008121002', 'DRAFT', 0), " +
                    "('KRS-2026-3125522016', '3125522016', '199008232015042001', 'DRAFT', 0), " +
                    "('KRS-2026-3125522017', '3125522017', '198504122010121004', 'DRAFT', 0)");

            // Seed Katalog Mata Kuliah (Termasuk 5 MK Baru)
            stmt.executeUpdate("INSERT IGNORE INTO mata_kuliah (kode_mk, nama_mk, sks, kuota, terisi, semester_buka) VALUES " +
                    "('IF301', 'Pemrograman Berorientasi Obyek', 3, 30, 0, 3), " +
                    "('IF302', 'Praktikum Pemrograman Berorientasi Obyek', 1, 30, 0, 3), " +
                    "('IF303', 'Struktur Data & Algoritma', 3, 28, 0, 3), " +
                    "('IF304', 'Praktikum Struktur Data & Algoritma', 1, 28, 0, 3), " +
                    "('IF305', 'Basis Data Lanjut', 3, 32, 0, 3), " +
                    "('IF306', 'Rekayasa Kebutuhan Perangkat Lunak', 3, 35, 0, 3), " +
                    "('IF307', 'Pemrograman Web Berbasis Komponen', 3, 30, 0, 3), " +
                    "('IF308', 'Sistem Operasi', 3, 30, 0, 3), " +
                    "('IF309', 'Jaringan Komputer', 3, 25, 0, 3), " +
                    "('IF310', 'Etika Profesi & Komunikasi Ilmiah', 2, 40, 0, 3), " +
                    "('IF311', 'Topik Khusus Sistem Terdistribusi', 3, 1, 1, 3), " +
                    "('IF312', 'Keamanan Jaringan & Kriptografi', 3, 30, 0, 3), " +
                    "('IF313', 'Kecerdasan Komputasional', 3, 25, 0, 3), " +
                    "('IF314', 'Komputasi Awan (Cloud Computing)', 3, 30, 0, 3), " +
                    "('IF315', 'Pengolahan Citra Digital', 3, 28, 0, 3), " +
                    "('IF316', 'Manajemen Proyek Perangkat Lunak', 2, 35, 0, 3)");
        } catch (SQLException e) {
            System.err.println("[DATABASE SEED] Info: " + e.getMessage());
        }
    }
}
