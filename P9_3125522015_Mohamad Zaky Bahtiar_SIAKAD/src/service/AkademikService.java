package service;

import database.DatabaseConnection;
import model.Dosen;
import model.KRS;
import model.Mahasiswa;
import model.MataKuliah;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AkademikService {
    private Mahasiswa mahasiswa;
    private Dosen dosenPembimbing;
    private final Map<String, MataKuliah> katalogMataKuliah;
    private KRS krsAktif;
    private final List<String> auditLogs;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private boolean isDatabaseConnected;

    public AkademikService() {
        this("3125522015");
    }

    public AkademikService(String nimMahasiswa) {
        this.auditLogs = Collections.synchronizedList(new ArrayList<>());
        this.katalogMataKuliah = new LinkedHashMap<>();

        // Inisialisasi awal dengan data fallback aman
        inisialisasiDataFallback(nimMahasiswa);

        // Jika terhubung ke MySQL XAMPP, muat data riil dari database
        if (DatabaseConnection.isConnected()) {
            this.isDatabaseConnected = true;
            muatDataDariDatabase(nimMahasiswa);
            catatLog("[DATABASE] Terhubung ke MySQL XAMPP (db_siakad_pbo). State tersinkronisasi riil.");
        } else {
            this.isDatabaseConnected = false;
            catatLog("[FALLBACK] MySQL XAMPP tidak aktif. Menjalankan sistem dengan In-Memory State.");
        }
    }

    /**
     * Mengambil daftar seluruh mahasiswa yang terdaftar di database (untuk login gate).
     */
    public static List<Mahasiswa> getDaftarMahasiswaDatabase() {
        List<Mahasiswa> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT nim, nama, semester, max_sks, status FROM mahasiswa ORDER BY nim ASC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Mahasiswa(
                            rs.getString("nim"),
                            rs.getString("nama"),
                            rs.getInt("semester"),
                            rs.getInt("max_sks"),
                            rs.getString("status")
                    ));
                }
            } catch (SQLException e) {
                System.err.println("[SERVICE ERROR] Gagal mengambil daftar mahasiswa: " + e.getMessage());
            }
        }

        // Fallback jika database belum ada data atau offline
        if (list.isEmpty()) {
            list.add(new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", 3, 24, "AKTIF"));
            list.add(new Mahasiswa("3125522016", "Alifia Putri Maharani", 3, 24, "AKTIF"));
            list.add(new Mahasiswa("3125522017", "Dimas Arya Pratama", 5, 24, "AKTIF"));
        }

        return list;
    }

    /**
     * Mengambil Dosen PA dari database berdasarkan NIM mahasiswa.
     */
    public static Dosen getDosenByMahasiswa(String nim) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT d.nip, d.nama, d.kuota_bimbingan, d.spesialisasi " +
                    "FROM dosen d JOIN krs k ON d.nip = k.nip_dosen WHERE k.nim = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nim);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Dosen(
                                rs.getString("nip"),
                                rs.getString("nama"),
                                rs.getInt("kuota_bimbingan"),
                                rs.getString("spesialisasi")
                        );
                    }
                }
            } catch (SQLException e) {
                System.err.println("[SERVICE ERROR] Gagal mencari dosen PA: " + e.getMessage());
            }
        }

        // Fallback mapping
        if ("3125522016".equals(nim)) {
            return new Dosen("199008232015042001", "Siti Aminah, M.Kom.", 20, "Sistem Informasi & Manajemen Basis Data");
        } else if ("3125522017".equals(nim)) {
            return new Dosen("198504122010121004", "Bambang Sudarsono, S.T., M.Kom.", 20, "Jaringan Komputer & Cloud Infrastructure");
        } else {
            return new Dosen("198203152008121002", "Dr. Eng. Ahmad Fauzi, M.Kom.", 25, "Rekayasa Perangkat Lunak & Basis Data");
        }
    }

    private void inisialisasiDataFallback(String nim) {
        if ("3125522016".equals(nim)) {
            this.mahasiswa = new Mahasiswa("3125522016", "Alifia Putri Maharani", 3, 24, "AKTIF");
            this.dosenPembimbing = new Dosen("199008232015042001", "Siti Aminah, M.Kom.", 20, "Sistem Informasi & Basis Data");
        } else if ("3125522017".equals(nim)) {
            this.mahasiswa = new Mahasiswa("3125522017", "Dimas Arya Pratama", 5, 24, "AKTIF");
            this.dosenPembimbing = new Dosen("198504122010121004", "Bambang Sudarsono, S.T., M.Kom.", 20, "Jaringan Komputer & Cloud");
        } else {
            this.mahasiswa = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", 3, 24, "AKTIF");
            this.dosenPembimbing = new Dosen("198203152008121002", "Dr. Eng. Ahmad Fauzi, M.Kom.", 25, "Rekayasa Perangkat Lunak & Basis Data");
        }

        String idKrs = "KRS-2026-" + this.mahasiswa.getNim();
        this.krsAktif = new KRS(idKrs, this.mahasiswa, this.dosenPembimbing);
        this.mahasiswa.setKrsAktif(this.krsAktif);

        // Katalog Dasar + 5 Mata Kuliah Tambahan Baru
        tambahKeKatalog(new MataKuliah("IF301", "Pemrograman Berorientasi Obyek", 3, 30, 3));
        tambahKeKatalog(new MataKuliah("IF302", "Praktikum Pemrograman Berorientasi Obyek", 1, 30, 3));
        tambahKeKatalog(new MataKuliah("IF303", "Struktur Data & Algoritma", 3, 28, 3));
        tambahKeKatalog(new MataKuliah("IF304", "Praktikum Struktur Data & Algoritma", 1, 28, 3));
        tambahKeKatalog(new MataKuliah("IF305", "Basis Data Lanjut", 3, 32, 3));
        tambahKeKatalog(new MataKuliah("IF306", "Rekayasa Kebutuhan Perangkat Lunak", 3, 35, 3));
        tambahKeKatalog(new MataKuliah("IF307", "Pemrograman Web Berbasis Komponen", 3, 30, 3));
        tambahKeKatalog(new MataKuliah("IF308", "Sistem Operasi", 3, 30, 3));
        tambahKeKatalog(new MataKuliah("IF309", "Jaringan Komputer", 3, 25, 3));
        tambahKeKatalog(new MataKuliah("IF310", "Etika Profesi & Komunikasi Ilmiah", 2, 40, 3));
        tambahKeKatalog(new MataKuliah("IF311", "Topik Khusus Sistem Terdistribusi", 3, 1, 3));

        // 5 MATA KULIAH BARU (ENTERPRISE EXPANDED CATALOG)
        tambahKeKatalog(new MataKuliah("IF312", "Keamanan Jaringan & Kriptografi", 3, 30, 3));
        tambahKeKatalog(new MataKuliah("IF313", "Kecerdasan Komputasional", 3, 25, 3));
        tambahKeKatalog(new MataKuliah("IF314", "Komputasi Awan (Cloud Computing)", 3, 30, 3));
        tambahKeKatalog(new MataKuliah("IF315", "Pengolahan Citra Digital", 3, 28, 3));
        tambahKeKatalog(new MataKuliah("IF316", "Manajemen Proyek Perangkat Lunak", 2, 35, 3));

        katalogMataKuliah.get("IF311").alokasiKursi(); // Simulasi kelas penuh
    }

    private void muatDataDariDatabase(String nim) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return;

        try {
            // 1. Muat Mahasiswa
            String sqlMhs = "SELECT nim, nama, semester, max_sks, status FROM mahasiswa WHERE nim = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMhs)) {
                ps.setString(1, nim);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        this.mahasiswa = new Mahasiswa(
                                rs.getString("nim"),
                                rs.getString("nama"),
                                rs.getInt("semester"),
                                rs.getInt("max_sks"),
                                rs.getString("status")
                        );
                    }
                }
            }

            // 2. Muat Dosen PA
            Dosen d = getDosenByMahasiswa(nim);
            if (d != null) {
                this.dosenPembimbing = d;
            }

            // 3. Muat Semua Mata Kuliah dari Database
            katalogMataKuliah.clear();
            String sqlMK = "SELECT kode_mk, nama_mk, sks, kuota, terisi, semester_buka FROM mata_kuliah ORDER BY kode_mk ASC";
            try (PreparedStatement ps = conn.prepareStatement(sqlMK);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MataKuliah mk = new MataKuliah(
                            rs.getString("kode_mk"),
                            rs.getString("nama_mk"),
                            rs.getInt("sks"),
                            rs.getInt("kuota"),
                            rs.getInt("semester_buka")
                    );
                    mk.setTerisi(rs.getInt("terisi"));
                    katalogMataKuliah.put(mk.getKodeMK().toUpperCase(), mk);
                }
            }

            // 4. Muat KRS
            String sqlKrs = "SELECT id_krs, nim, nip_dosen, status, total_sks FROM krs WHERE nim = ?";
            String idKrs = "KRS-2026-" + nim;
            KRS.StatusKRS statusKRS = KRS.StatusKRS.DRAFT;

            try (PreparedStatement ps = conn.prepareStatement(sqlKrs)) {
                ps.setString(1, nim);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idKrs = rs.getString("id_krs");
                        String sStr = rs.getString("status");
                        try {
                            statusKRS = KRS.StatusKRS.valueOf(sStr.toUpperCase());
                        } catch (Exception ignored) {}
                    } else {
                        // Insert krs record if missing
                        try (PreparedStatement insKrs = conn.prepareStatement(
                                "INSERT INTO krs (id_krs, nim, nip_dosen, status, total_sks) VALUES (?, ?, ?, 'DRAFT', 0)")) {
                            insKrs.setString(1, idKrs);
                            insKrs.setString(2, nim);
                            insKrs.setString(3, this.dosenPembimbing.getNip());
                            insKrs.executeUpdate();
                        }
                    }
                }
            }

            this.krsAktif = new KRS(idKrs, this.mahasiswa, this.dosenPembimbing);
            this.mahasiswa.setKrsAktif(this.krsAktif);

            // 5. Muat Detail Mata Kuliah di KRS
            String sqlDetail = "SELECT kode_mk FROM krs_detail WHERE id_krs = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                ps.setString(1, idKrs);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String kodeMK = rs.getString("kode_mk").toUpperCase();
                        MataKuliah mk = katalogMataKuliah.get(kodeMK);
                        if (mk != null) {
                            this.krsAktif.muatItemDariDatabase(mk);
                        }
                    }
                }
            }

            // Set final status KRS
            this.krsAktif.setStatus(statusKRS);

            catatLog(String.format("Data mahasiswa [%s] %s berhasil dimuat dari database. Total SKS: %d, Status: %s",
                    mahasiswa.getNim(), mahasiswa.getNama(), krsAktif.hitungTotalSKS(), krsAktif.getStatus()));

        } catch (SQLException e) {
            System.err.println("[SERVICE ERROR] Gagal sinkronisasi data dari MySQL: " + e.getMessage());
            catatLog("[DATABASE ERROR] Sinkronisasi MySQL gagal: " + e.getMessage());
        }
    }

    private void tambahKeKatalog(MataKuliah mk) {
        katalogMataKuliah.put(mk.getKodeMK().toUpperCase(), mk);
    }

    public synchronized void catatLog(String pesan) {
        String timestamp = LocalDateTime.now().format(timeFormatter);
        String logEntry = "[" + timestamp + "] " + pesan;
        auditLogs.add(logEntry);
        if (auditLogs.size() > 100) {
            auditLogs.remove(0);
        }
    }

    public synchronized void ambilMataKuliah(String kodeMK) {
        if (kodeMK == null || kodeMK.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode MK tidak valid");
        }
        MataKuliah mk = katalogMataKuliah.get(kodeMK.trim().toUpperCase());
        if (mk == null) {
            throw new IllegalArgumentException("Mata kuliah [" + kodeMK + "] tidak terdaftar dalam katalog");
        }

        // 1. Eksekusi Enkapsulasi OOP
        mahasiswa.ambilMataKuliah(mk);

        // 2. Persistensi ke Database MySQL XAMPP jika terhubung
        if (isDatabaseConnected) {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try {
                    conn.setAutoCommit(false);

                    // Insert krs_detail
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO krs_detail (id_krs, kode_mk) VALUES (?, ?)")) {
                        ps.setString(1, krsAktif.getIdKRS());
                        ps.setString(2, mk.getKodeMK());
                        ps.executeUpdate();
                    }

                    // Update terisi mata_kuliah
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE mata_kuliah SET terisi = terisi + 1 WHERE kode_mk = ?")) {
                        ps.setString(1, mk.getKodeMK());
                        ps.executeUpdate();
                    }

                    // Update total_sks krs
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE krs SET total_sks = ?, updated_at = NOW() WHERE id_krs = ?")) {
                        ps.setInt(1, krsAktif.hitungTotalSKS());
                        ps.setString(2, krsAktif.getIdKRS());
                        ps.executeUpdate();
                    }

                    conn.commit();
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    try { conn.rollback(); } catch (SQLException ignored) {}
                    System.err.println("[DATABASE ERROR] Rollback transaksi ambil MK: " + e.getMessage());
                }
            }
        }

        catatLog(String.format("AMBIL MK BERHASIL: [%s] %s (%d SKS) dialokasikan. Total SKS sekarang: %d/%d SKS. Sisa kuota kelas: %d/%d",
                mk.getKodeMK(), mk.getNamaMK(), mk.getSks(),
                krsAktif.hitungTotalSKS(), mahasiswa.getMaxSKS(),
                mk.getSisaKuota(), mk.getKuota()));
    }

    public synchronized void batalkanMataKuliah(String kodeMK) {
        if (kodeMK == null || kodeMK.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode MK tidak valid");
        }
        MataKuliah mk = katalogMataKuliah.get(kodeMK.trim().toUpperCase());
        if (mk == null) {
            throw new IllegalArgumentException("Mata kuliah [" + kodeMK + "] tidak ditemukan");
        }

        // 1. Eksekusi Enkapsulasi OOP
        mahasiswa.batalkanMataKuliah(kodeMK);

        // 2. Persistensi ke Database MySQL
        if (isDatabaseConnected) {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try {
                    conn.setAutoCommit(false);

                    // Delete from krs_detail
                    try (PreparedStatement ps = conn.prepareStatement(
                            "DELETE FROM krs_detail WHERE id_krs = ? AND kode_mk = ?")) {
                        ps.setString(1, krsAktif.getIdKRS());
                        ps.setString(2, mk.getKodeMK());
                        ps.executeUpdate();
                    }

                    // Decrement terisi mata_kuliah
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE mata_kuliah SET terisi = GREATEST(0, terisi - 1) WHERE kode_mk = ?")) {
                        ps.setString(1, mk.getKodeMK());
                        ps.executeUpdate();
                    }

                    // Update total_sks krs
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE krs SET total_sks = ?, updated_at = NOW() WHERE id_krs = ?")) {
                        ps.setInt(1, krsAktif.hitungTotalSKS());
                        ps.setString(2, krsAktif.getIdKRS());
                        ps.executeUpdate();
                    }

                    conn.commit();
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    try { conn.rollback(); } catch (SQLException ignored) {}
                    System.err.println("[DATABASE ERROR] Rollback transaksi batalkan MK: " + e.getMessage());
                }
            }
        }

        catatLog(String.format("PEMBATALAN MK: [%s] %s dilepas dari KRS. Kuota dikembalikan. Total SKS sekarang: %d/%d SKS.",
                mk.getKodeMK(), mk.getNamaMK(),
                krsAktif.hitungTotalSKS(), mahasiswa.getMaxSKS()));
    }

    public synchronized void ajukanKRS() {
        krsAktif.ajukanKRS();

        if (isDatabaseConnected) {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE krs SET status = 'DIAJUKAN', updated_at = NOW() WHERE id_krs = ?")) {
                    ps.setString(1, krsAktif.getIdKRS());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("[DATABASE ERROR] Gagal update status DIAJUKAN: " + e.getMessage());
                }
            }
        }

        catatLog(String.format("PENGAJUAN KRS: [%s] diajukan ke Dosen PA (%s) dengan total %d SKS.",
                krsAktif.getIdKRS(), dosenPembimbing.getNama(), krsAktif.hitungTotalSKS()));
    }

    public synchronized void setujuiKRS() {
        dosenPembimbing.verifikasiKRS(krsAktif);

        if (isDatabaseConnected) {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE krs SET status = 'DISETUJUI', updated_at = NOW() WHERE id_krs = ?")) {
                    ps.setString(1, krsAktif.getIdKRS());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("[DATABASE ERROR] Gagal update status DISETUJUI: " + e.getMessage());
                }
            }
        }

        catatLog(String.format("PERSETUJUAN KRS RESMI: [%s] disetujui resmi oleh Dosen Pembimbing (%s). Dokumen resmi siap dicetak.",
                krsAktif.getIdKRS(), dosenPembimbing.getNama()));
    }

    public synchronized void batalkanPersetujuanDosen() {
        dosenPembimbing.batalkanPersetujuanKRS(krsAktif);

        if (isDatabaseConnected) {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE krs SET status = 'DRAFT', updated_at = NOW() WHERE id_krs = ?")) {
                    ps.setString(1, krsAktif.getIdKRS());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("[DATABASE ERROR] Gagal update status DRAFT: " + e.getMessage());
                }
            }
        }

        catatLog(String.format("BATAL PERSETUJUAN: Status [%s] dikembalikan ke DRAFT oleh Dosen PA (%s).",
                krsAktif.getIdKRS(), dosenPembimbing.getNama()));
    }

    public synchronized void resetKRS() {
        if (krsAktif.getStatus() != KRS.StatusKRS.DRAFT) {
            throw new IllegalStateException("Hanya KRS berstatus DRAFT yang dapat di-reset");
        }

        // Simpan daftar MK untuk pelepasan kuota
        List<MataKuliah> items = new ArrayList<>(krsAktif.getMataKuliahList());
        for (MataKuliah mk : items) {
            krsAktif.hapusItem(mk.getKodeMK());
        }

        if (isDatabaseConnected) {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try {
                    conn.setAutoCommit(false);

                    for (MataKuliah mk : items) {
                        try (PreparedStatement ps = conn.prepareStatement(
                                "UPDATE mata_kuliah SET terisi = GREATEST(0, terisi - 1) WHERE kode_mk = ?")) {
                            ps.setString(1, mk.getKodeMK());
                            ps.executeUpdate();
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement(
                            "DELETE FROM krs_detail WHERE id_krs = ?")) {
                        ps.setString(1, krsAktif.getIdKRS());
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE krs SET status = 'DRAFT', total_sks = 0, updated_at = NOW() WHERE id_krs = ?")) {
                        ps.setString(1, krsAktif.getIdKRS());
                        ps.executeUpdate();
                    }

                    conn.commit();
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    try { conn.rollback(); } catch (SQLException ignored) {}
                    System.err.println("[DATABASE ERROR] Rollback transaksi reset KRS: " + e.getMessage());
                }
            }
        }

        catatLog(String.format("RESET KRS: Seluruh mata kuliah dikosongkan. Total SKS kembali ke 0."));
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public Dosen getDosenPembimbing() {
        return dosenPembimbing;
    }

    public KRS getKrsAktif() {
        return krsAktif;
    }

    public List<MataKuliah> getKatalogMataKuliah() {
        return new ArrayList<>(katalogMataKuliah.values());
    }

    public List<String> getAuditLogs() {
        return Collections.unmodifiableList(new ArrayList<>(auditLogs));
    }

    public boolean isDatabaseConnected() {
        return isDatabaseConnected;
    }
}
