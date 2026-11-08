package P13_3125522015_MohamadZaky.utils;

import P13_3125522015_MohamadZaky.model.Admin;
import P13_3125522015_MohamadZaky.model.BahanBaku;
import P13_3125522015_MohamadZaky.model.DetailPengiriman;
import P13_3125522015_MohamadZaky.model.Driver;
import P13_3125522015_MohamadZaky.model.Menu;
import P13_3125522015_MohamadZaky.model.PengelolaDistribusi;
import P13_3125522015_MohamadZaky.model.PengelolaMenu;
import P13_3125522015_MohamadZaky.model.Pengguna;
import P13_3125522015_MohamadZaky.model.Pengiriman;
import P13_3125522015_MohamadZaky.model.Sekolah;
import P13_3125522015_MohamadZaky.model.StafDapur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MbgRepository {
    private static final MbgRepository INSTANCE = new MbgRepository();

    private final List<Pengguna> daftarPengguna = new ArrayList<>();
    private final List<BahanBaku> daftarBahanBaku = new ArrayList<>();
    private final List<Sekolah> daftarSekolah = new ArrayList<>();
    private final PengelolaMenu pengelolaMenu = new PengelolaMenu();
    private final PengelolaDistribusi pengelolaDistribusi = new PengelolaDistribusi();

    private double paguAnggaran = 250_000_000.0;
    private double danaTerpakai = 68_450_000.0;

    private MbgRepository() {
        muatSemuaData();
    }

    public static MbgRepository getInstance() {
        return INSTANCE;
    }

    public final void muatSemuaData() {
        daftarPengguna.clear();
        daftarBahanBaku.clear();
        daftarSekolah.clear();
        pengelolaMenu.kosongkan();
        pengelolaDistribusi.kosongkan();

        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try {
                muatDariDatabase(conn);
                return;
            } catch (SQLException e) {
                System.err.println("Gagal memuat tabel dari MySQL, beralih ke data memori: " + e.getMessage());
            }
        }
        muatDataSimulasiMemori();
    }

    private void muatDariDatabase(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT pagu_total, dana_terpakai FROM anggaran_program LIMIT 1")) {
            if (rs.next()) {
                paguAnggaran = rs.getDouble("pagu_total");
                danaTerpakai = rs.getDouble("dana_terpakai");
            }
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pengguna ORDER BY id_pengguna")) {
            while (rs.next()) {
                String id = rs.getString("id_pengguna");
                String nama = rs.getString("nama");
                String email = rs.getString("email");
                String peran = rs.getString("peran");
                String atribut = rs.getString("atribut_khusus");
                String armada = rs.getString("nomor_armada");
                String lokasi = rs.getString("lokasi_terkini");

                Pengguna user;
                if ("ADMIN".equalsIgnoreCase(peran)) {
                    user = new Admin(id, nama, email, atribut);
                } else if ("STAF_DAPUR".equalsIgnoreCase(peran)) {
                    user = new StafDapur(id, nama, email, atribut);
                } else {
                    user = new Driver(id, nama, email, atribut, armada != null ? armada : "M-0000-VP", lokasi != null ? lokasi : "Dapur Pusat");
                }
                daftarPengguna.add(user);
            }
        }

        Map<String, BahanBaku> mapBahan = new HashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM bahan_baku ORDER BY kode_bahan")) {
            while (rs.next()) {
                BahanBaku b = new BahanBaku(
                        rs.getString("kode_bahan"),
                        rs.getString("nama_bahan"),
                        rs.getString("kategori"),
                        rs.getInt("stok"),
                        rs.getString("satuan"),
                        rs.getDouble("harga_satuan"),
                        rs.getDate("tanggal_kedaluwarsa").toLocalDate()
                );
                daftarBahanBaku.add(b);
                mapBahan.put(b.getKodeBahan(), b);
            }
        }

        Map<String, Sekolah> mapSekolah = new HashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sekolah ORDER BY npsn")) {
            while (rs.next()) {
                Sekolah s = new Sekolah(
                        rs.getString("npsn"),
                        rs.getString("nama_sekolah"),
                        rs.getString("jenjang"),
                        rs.getString("alamat"),
                        rs.getInt("jumlah_siswa"),
                        rs.getString("koordinat_wilayah")
                );
                daftarSekolah.add(s);
                mapSekolah.put(s.getNpsn(), s);
            }
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu_gizi ORDER BY kode_menu")) {
            while (rs.next()) {
                Menu m = new Menu(
                        rs.getString("kode_menu"),
                        rs.getString("nama_menu"),
                        rs.getDouble("kalori_per_porsi"),
                        rs.getDouble("protein_gram"),
                        rs.getDouble("karbohidrat_gram"),
                        rs.getDouble("biaya_per_porsi")
                );
                pengelolaMenu.tambahMenu(m);
            }
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu_komposisi")) {
            while (rs.next()) {
                Menu m = pengelolaMenu.cariMenu(rs.getString("kode_menu"));
                BahanBaku b = mapBahan.get(rs.getString("kode_bahan"));
                int keb = rs.getInt("kebutuhan_per_100_porsi");
                if (m != null && b != null) {
                    m.tambahBahanBaku(b, keb);
                }
            }
        }

        Map<String, Pengiriman> mapPengiriman = new LinkedHashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pengiriman ORDER BY id_pengiriman")) {
            while (rs.next()) {
                String idKirim = rs.getString("id_pengiriman");
                Sekolah s = mapSekolah.get(rs.getString("npsn_sekolah"));
                Driver d = cariDriverById(rs.getString("id_driver"));
                Menu m = pengelolaMenu.cariMenu(rs.getString("kode_menu"));
                if (s != null && d != null && m != null) {
                    Pengiriman p = new Pengiriman(
                            idKirim,
                            s,
                            d,
                            m,
                            rs.getInt("jumlah_porsi"),
                            rs.getString("status_distribusi"),
                            rs.getString("lokasi_terakhir"),
                            rs.getString("waktu_berangkat")
                    );
                    pengelolaDistribusi.tambahPengiriman(p);
                    mapPengiriman.put(idKirim, p);
                }
            }
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM detail_pengiriman ORDER BY id_detail")) {
            while (rs.next()) {
                Pengiriman p = mapPengiriman.get(rs.getString("id_pengiriman"));
                if (p != null) {
                    p.buatDetailBatch(
                            rs.getString("nomor_batch"),
                            rs.getString("jenis_kemasan"),
                            rs.getInt("jumlah_porsi_batch"),
                            rs.getDouble("suhu_penyimpanan"),
                            rs.getString("catatan_inspeksi")
                    );
                }
            }
        }

        for (Pengiriman p : pengelolaDistribusi.getDaftarPengiriman()) {
            if (p.getDaftarDetailBatch().isEmpty()) {
                String kode = p.getIdPengiriman();
                p.buatDetailBatch("BATCH-" + kode.substring(Math.max(0, kode.length() - 2)) + "A",
                        "Food Tray Stainless 304",
                        p.getJumlahPorsi(),
                        65.5,
                        "Segel termal utuh & suhu terverifikasi");
            }
        }
    }

    private void muatDataSimulasiMemori() {
        paguAnggaran = 250_000_000.0;
        danaTerpakai = 68_450_000.0;

        daftarPengguna.add(new Admin("USR-ADM-01", "Mohamad Zaky Bahtiar", "zaky.bahtiar@bgn.simulasi.id", "Wilayah Koordinasi Sumenep Kota"));
        daftarPengguna.add(new StafDapur("USR-DPR-01", "Rina Kartika Sari", "rina.dapur@bgn.simulasi.id", "Dapur Pusat Sektor Utara - Shift Pagi"));
        Driver drv1 = new Driver("USR-DRV-01", "Hendro Wibowo", "hendro.driver@bgn.simulasi.id", "Rute Distribusi Kecamatan Kota", "M-8821-VP", "Jl. Trunojoyo KM 3, Sumenep");
        Driver drv2 = new Driver("USR-DRV-02", "Agus Salim Pratama", "agus.driver@bgn.simulasi.id", "Rute Distribusi Kecamatan Kalianget", "M-8824-VP", "Simpang Tiga Kalianget");
        Driver drv3 = new Driver("USR-DRV-03", "Budi Santoso", "budi.driver@bgn.simulasi.id", "Rute Distribusi Kecamatan Batuan", "M-8829-VP", "Gudang Logistik Dapur Pusat");
        daftarPengguna.add(drv1);
        daftarPengguna.add(drv2);
        daftarPengguna.add(drv3);

        BahanBaku b1 = new BahanBaku("BHN-001", "Beras Premium Fortifikasi", "Karbohidrat", 120, "kg", 14500, LocalDate.of(2027, 3, 15));
        BahanBaku b2 = new BahanBaku("BHN-002", "Dada Ayam Fillet Segar", "Protein Hewani", 5, "kg", 38000, LocalDate.of(2026, 11, 2));
        BahanBaku b3 = new BahanBaku("BHN-003", "Telur Ayam Negeri", "Protein Hewani", 45, "kg", 27000, LocalDate.of(2026, 11, 12));
        BahanBaku b4 = new BahanBaku("BHN-004", "Tahu Putih Padat", "Protein Nabati", 18, "kg", 12000, LocalDate.of(2026, 10, 29));
        BahanBaku b5 = new BahanBaku("BHN-005", "Wortel & Buncis Segar", "Sayuran", 32, "kg", 11000, LocalDate.of(2026, 11, 1));
        BahanBaku b6 = new BahanBaku("BHN-006", "Susu UHT Kotak 200ml", "Pelengkap Gizi", 250, "liter", 16500, LocalDate.of(2027, 1, 20));
        BahanBaku b7 = new BahanBaku("BHN-007", "Ikan Tongkol Fillet", "Protein Hewani", 8, "kg", 34000, LocalDate.of(2026, 10, 25));
        Collections.addAll(daftarBahanBaku, b1, b2, b3, b4, b5, b6, b7);

        Sekolah s1 = new Sekolah("20529901", "SDN Pajagalan I Sumenep [Simulasi]", "SD", "Jl. KH. Mansyur No. 14, Kota Sumenep", 320, "-7.0054, 113.8602");
        Sekolah s2 = new Sekolah("20529902", "SMPN 1 Sumenep [Simulasi]", "SMP", "Jl. Payudan Barat No. 9, Kota Sumenep", 640, "-7.0089, 113.8547");
        Sekolah s3 = new Sekolah("20529903", "SDN Kalianget Timur I [Simulasi]", "SD", "Jl. Raya Pelabuhan Kalianget No. 4", 280, "-7.0421, 113.9315");
        Sekolah s4 = new Sekolah("20529904", "SMAN 1 Sumenep [Simulasi]", "SMA", "Jl. Payudan Timur No. 1, Kota Sumenep", 780, "-7.0098, 113.8611");
        Sekolah s5 = new Sekolah("20529905", "SDN Batuan I [Simulasi]", "SD", "Jl. Raya Lenteng KM 4, Batuan", 210, "-7.0185, 113.8320");
        Collections.addAll(daftarSekolah, s1, s2, s3, s4, s5);

        Menu m1 = new Menu("MNU-01", "Paket A: Nasi Ayam Teriyaki & Tumis Sayur + Susu", 680.0, 28.5, 82.0, 15000.0);
        m1.tambahBahanBaku(b1, 12);
        m1.tambahBahanBaku(b2, 10);
        m1.tambahBahanBaku(b5, 8);
        m1.tambahBahanBaku(b6, 20);

        Menu m2 = new Menu("MNU-02", "Paket B: Nasi Telur Balado, Tahu Bacem & Capcay + Susu", 640.0, 24.0, 79.0, 14000.0);
        m2.tambahBahanBaku(b1, 12);
        m2.tambahBahanBaku(b3, 8);
        m2.tambahBahanBaku(b4, 5);
        m2.tambahBahanBaku(b5, 7);
        m2.tambahBahanBaku(b6, 20);

        Menu m3 = new Menu("MNU-03", "Paket C: Nasi Ikan Tongkol Bumbu Kuning & Sayur Bening", 660.0, 27.0, 80.5, 14500.0);
        m3.tambahBahanBaku(b1, 12);
        m3.tambahBahanBaku(b7, 9);
        m3.tambahBahanBaku(b5, 8);

        pengelolaMenu.tambahMenu(m1);
        pengelolaMenu.tambahMenu(m2);
        pengelolaMenu.tambahMenu(m3);

        Pengiriman p1 = new Pengiriman("DST-20261028-01", s1, drv1, m1, 320, "Delivered", "SDN Pajagalan I Sumenep (Tiba)", "07:15 WIB");
        p1.buatDetailBatch("BATCH-01A", "Food Tray Stainless 304", 160, 65.5, "Segel termal utuh, suhu terverifikasi");
        p1.buatDetailBatch("BATCH-01B", "Food Tray Stainless 304", 160, 64.8, "Diterima koordinator UKS sekolah");

        Pengiriman p2 = new Pengiriman("DST-20261028-02", s2, drv2, m2, 640, "In Transit", "Jl. Payudan Barat KM 1.5 (Dalam Perjalanan)", "07:45 WIB");
        p2.buatDetailBatch("BATCH-02A", "Insulated Box Food Grade", 320, 66.0, "Armada melewati pos pemeriksaan kota");
        p2.buatDetailBatch("BATCH-02B", "Insulated Box Food Grade", 320, 65.2, "Suhu kabin stabil");

        Pengiriman p3 = new Pengiriman("DST-20261028-03", s3, drv3, m1, 280, "Pending", "Gudang Logistik Dapur Pusat (Menunggu Muat)", "08:15 WIB");
        p3.buatDetailBatch("BATCH-03A", "Food Tray Stainless 304", 280, 68.0, "Menunggu verifikasi surat jalan dapur");

        Pengiriman p4 = new Pengiriman("DST-20261028-04", s5, drv1, m3, 210, "Delivered", "SDN Batuan I (Tiba)", "06:50 WIB");
        p4.buatDetailBatch("BATCH-04A", "Food Tray Stainless 304", 210, 65.0, "Selesai disalurkan tepat waktu");

        pengelolaDistribusi.tambahPengiriman(p1);
        pengelolaDistribusi.tambahPengiriman(p2);
        pengelolaDistribusi.tambahPengiriman(p3);
        pengelolaDistribusi.tambahPengiriman(p4);
    }

    private Driver cariDriverById(String idDriver) {
        for (Pengguna p : daftarPengguna) {
            if (p instanceof Driver && p.getIdPengguna().equalsIgnoreCase(idDriver)) {
                return (Driver) p;
            }
        }
        return null;
    }

    public PengelolaMenu getPengelolaMenu() {
        return pengelolaMenu;
    }

    public PengelolaDistribusi getPengelolaDistribusi() {
        return pengelolaDistribusi;
    }

    public List<Pengguna> getDaftarPengguna() {
        return Collections.unmodifiableList(daftarPengguna);
    }

    public List<Driver> getDaftarDriver() {
        List<Driver> drivers = new ArrayList<>();
        for (Pengguna p : daftarPengguna) {
            if (p instanceof Driver) {
                drivers.add((Driver) p);
            }
        }
        return drivers;
    }

    public List<BahanBaku> getDaftarBahanBaku() {
        return Collections.unmodifiableList(daftarBahanBaku);
    }

    public List<Sekolah> getDaftarSekolah() {
        return Collections.unmodifiableList(daftarSekolah);
    }

    public List<Menu> getDaftarMenu() {
        return pengelolaMenu.getDaftarMenu();
    }

    public List<Pengiriman> getDaftarPengiriman() {
        return pengelolaDistribusi.getDaftarPengiriman();
    }

    public double getPaguAnggaran() {
        return paguAnggaran;
    }

    public double getDanaTerpakai() {
        return danaTerpakai;
    }

    public double getSisaAnggaran() {
        return Math.max(0, paguAnggaran - danaTerpakai);
    }

    public int getTotalPorsiTersalurkan() {
        int total = 0;
        for (Pengiriman p : pengelolaDistribusi.getDaftarPengiriman()) {
            total += p.getJumlahPorsi();
        }
        return total;
    }

    public int getTotalSiswaTerdaftar() {
        int total = 0;
        for (Sekolah s : daftarSekolah) {
            total += s.getJumlahSiswa();
        }
        return total;
    }

    public void simpanAtauPerbaruiBahanBaku(BahanBaku bahan) {
        BahanBaku existing = null;
        for (BahanBaku b : daftarBahanBaku) {
            if (b.getKodeBahan().equalsIgnoreCase(bahan.getKodeBahan())) {
                existing = b;
                break;
            }
        }
        if (existing == null) {
            daftarBahanBaku.add(bahan);
        } else if (existing != bahan) {
            existing.setNamaBahan(bahan.getNamaBahan());
            existing.setKategori(bahan.getKategori());
            existing.setStok(bahan.getStok());
            existing.setSatuan(bahan.getSatuan());
            existing.setHargaSatuan(bahan.getHargaSatuan());
            existing.setTanggalKedaluwarsa(bahan.getTanggalKedaluwarsa());
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            String sql = "INSERT INTO bahan_baku (kode_bahan, nama_bahan, kategori, stok, satuan, harga_satuan, tanggal_kedaluwarsa) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE nama_bahan=VALUES(nama_bahan), kategori=VALUES(kategori), " +
                    "stok=VALUES(stok), satuan=VALUES(satuan), harga_satuan=VALUES(harga_satuan), tanggal_kedaluwarsa=VALUES(tanggal_kedaluwarsa)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, bahan.getKodeBahan());
                ps.setString(2, bahan.getNamaBahan());
                ps.setString(3, bahan.getKategori());
                ps.setInt(4, bahan.getStok());
                ps.setString(5, bahan.getSatuan());
                ps.setDouble(6, bahan.getHargaSatuan());
                ps.setDate(7, java.sql.Date.valueOf(bahan.getTanggalKedaluwarsa()));
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Sinkronisasi SQL BahanBaku tertunda: " + e.getMessage());
            }
        }
    }

    public void hapusBahanBaku(String kodeBahan) {
        daftarBahanBaku.removeIf(b -> b.getKodeBahan().equalsIgnoreCase(kodeBahan));
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM bahan_baku WHERE kode_bahan = ?")) {
                ps.setString(1, kodeBahan);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Gagal menghapus bahan baku di SQL: " + e.getMessage());
            }
        }
    }

    public void simpanAtauPerbaruiSekolah(Sekolah sekolah) {
        Sekolah existing = null;
        for (Sekolah s : daftarSekolah) {
            if (s.getNpsn().equalsIgnoreCase(sekolah.getNpsn())) {
                existing = s;
                break;
            }
        }
        if (existing == null) {
            daftarSekolah.add(sekolah);
        } else if (existing != sekolah) {
            existing.setNamaSekolah(sekolah.getNamaSekolah());
            existing.setJenjang(sekolah.getJenjang());
            existing.setAlamat(sekolah.getAlamat());
            existing.setJumlahSiswa(sekolah.getJumlahSiswa());
            existing.setKoordinatWilayah(sekolah.getKoordinatWilayah());
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            String sql = "INSERT INTO sekolah (npsn, nama_sekolah, jenjang, alamat, jumlah_siswa, koordinat_wilayah) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE nama_sekolah=VALUES(nama_sekolah), jenjang=VALUES(jenjang), " +
                    "alamat=VALUES(alamat), jumlah_siswa=VALUES(jumlah_siswa), koordinat_wilayah=VALUES(koordinat_wilayah)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, sekolah.getNpsn());
                ps.setString(2, sekolah.getNamaSekolah());
                ps.setString(3, sekolah.getJenjang());
                ps.setString(4, sekolah.getAlamat());
                ps.setInt(5, sekolah.getJumlahSiswa());
                ps.setString(6, sekolah.getKoordinatWilayah());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Sinkronisasi SQL Sekolah tertunda: " + e.getMessage());
            }
        }
    }

    public void hapusSekolah(String npsn) {
        daftarSekolah.removeIf(s -> s.getNpsn().equalsIgnoreCase(npsn));
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM sekolah WHERE npsn = ?")) {
                ps.setString(1, npsn);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Gagal menghapus sekolah di SQL: " + e.getMessage());
            }
        }
    }

    public void simpanAtauPerbaruiMenu(Menu menu) {
        Menu existing = pengelolaMenu.cariMenu(menu.getKodeMenu());
        if (existing == null) {
            pengelolaMenu.tambahMenu(menu);
        } else if (existing != menu) {
            existing.setNamaMenu(menu.getNamaMenu());
            existing.setKaloriPerPorsi(menu.getKaloriPerPorsi());
            existing.setProteinGram(menu.getProteinGram());
            existing.setKarbohidratGram(menu.getKarbohidratGram());
            existing.setBiayaPerPorsi(menu.getBiayaPerPorsi());
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            String sql = "INSERT INTO menu_gizi (kode_menu, nama_menu, kalori_per_porsi, protein_gram, karbohidrat_gram, biaya_per_porsi) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE nama_menu=VALUES(nama_menu), kalori_per_porsi=VALUES(kalori_per_porsi), " +
                    "protein_gram=VALUES(protein_gram), karbohidrat_gram=VALUES(karbohidrat_gram), biaya_per_porsi=VALUES(biaya_per_porsi)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, menu.getKodeMenu());
                ps.setString(2, menu.getNamaMenu());
                ps.setDouble(3, menu.getKaloriPerPorsi());
                ps.setDouble(4, menu.getProteinGram());
                ps.setDouble(5, menu.getKarbohidratGram());
                ps.setDouble(6, menu.getBiayaPerPorsi());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Sinkronisasi SQL Menu tertunda: " + e.getMessage());
            }
        }
    }

    public void hapusMenu(String kodeMenu) {
        pengelolaMenu.hapusMenu(kodeMenu);
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM menu_gizi WHERE kode_menu = ?")) {
                ps.setString(1, kodeMenu);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Gagal menghapus menu di SQL: " + e.getMessage());
            }
        }
    }

    public void simpanAtauPerbaruiPengiriman(Pengiriman pengiriman) {
        Pengiriman existing = pengelolaDistribusi.cariPengiriman(pengiriman.getIdPengiriman());
        if (existing == null) {
            pengelolaDistribusi.tambahPengiriman(pengiriman);
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            String sql = "INSERT INTO pengiriman (id_pengiriman, npsn_sekolah, id_driver, kode_menu, jumlah_porsi, status_distribusi, lokasi_terakhir, waktu_berangkat) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE status_distribusi=VALUES(status_distribusi), lokasi_terakhir=VALUES(lokasi_terakhir), jumlah_porsi=VALUES(jumlah_porsi)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pengiriman.getIdPengiriman());
                ps.setString(2, pengiriman.getSekolahTujuan().getNpsn());
                ps.setString(3, pengiriman.getDriverArmada().getIdPengguna());
                ps.setString(4, pengiriman.getMenuDikirim().getKodeMenu());
                ps.setInt(5, pengiriman.getJumlahPorsi());
                ps.setString(6, pengiriman.getStatusDistribusi());
                ps.setString(7, pengiriman.getLokasiTerakhir());
                ps.setString(8, pengiriman.getWaktuBerangkat());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Sinkronisasi SQL Pengiriman tertunda: " + e.getMessage());
            }

            try (PreparedStatement psDriver = conn.prepareStatement("UPDATE pengguna SET lokasi_terkini = ? WHERE id_pengguna = ?")) {
                psDriver.setString(1, pengiriman.getDriverArmada().getLokasiTerkini());
                psDriver.setString(2, pengiriman.getDriverArmada().getIdPengguna());
                psDriver.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Sinkronisasi lokasi driver tertunda: " + e.getMessage());
            }
        }
    }

    public void hapusPengiriman(String idPengiriman) {
        pengelolaDistribusi.hapusPengiriman(idPengiriman);
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM pengiriman WHERE id_pengiriman = ?")) {
                ps.setString(1, idPengiriman);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Gagal menghapus pengiriman di SQL: " + e.getMessage());
            }
        }
    }

    public void simpanDetailBatch(String idPengiriman, DetailPengiriman detail) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null && detail != null) {
            String sql = "INSERT INTO detail_pengiriman (id_pengiriman, nomor_batch, jenis_kemasan, jumlah_porsi_batch, suhu_penyimpanan, catatan_inspeksi) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idPengiriman);
                ps.setString(2, detail.getNomorBatch());
                ps.setString(3, detail.getJenisKemasan());
                ps.setInt(4, detail.getJumlahPorsiBatch());
                ps.setDouble(5, detail.getSuhuPenyimpanan());
                ps.setString(6, detail.getCatatanInspeksi());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Sinkronisasi SQL DetailPengiriman tertunda: " + e.getMessage());
            }
        }
    }

    public void catatBiayaProduksi(double tambahanBiaya) {
        if (tambahanBiaya > 0) {
            this.danaTerpakai += tambahanBiaya;
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE anggaran_program SET dana_terpakai = ? WHERE id_anggaran = 'ANG-2026-Q4'")) {
                    ps.setDouble(1, this.danaTerpakai);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("Sinkronisasi anggaran SQL tertunda: " + e.getMessage());
                }
            }
        }
    }

    public void simpanMenuKeFile(java.nio.file.Path lokasi) throws java.io.IOException {
        new MbgFileRepository(pengelolaMenu).simpan(lokasi);
    }

    public void muatMenuDariFile(java.nio.file.Path lokasi) throws java.io.IOException {
        new MbgFileRepository(pengelolaMenu).muat(lokasi);
    }
}

