import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TenagaMedisDAO {

    /**
     * Menyimpan tenaga medis (Dokter atau Perawat) ke database (CREATE).
     * Menerapkan pengecekan tipe objek polimorfik (instanceof).
     */
    public boolean create(TenagaMedis medis) {
        String sql = "INSERT INTO tenaga_medis (id_medis, tipe_medis, nama, spesialisasi, biaya_konsultasi, jam_lembur) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, medis.getIdMedis());
            ps.setString(3, medis.getNama());
            ps.setString(4, medis.getSpesialisasi());

            if (medis instanceof Dokter) {
                Dokter d = (Dokter) medis;
                ps.setString(2, "Dokter");
                ps.setDouble(5, d.getBiayaKonsultasi());
                ps.setInt(6, 0);
            } else if (medis instanceof Perawat) {
                Perawat p = (Perawat) medis;
                ps.setString(2, "Perawat");
                ps.setDouble(5, 0.0);
                ps.setInt(6, p.getJamLembur());
            } else {
                throw new IllegalArgumentException("Tipe TenagaMedis tidak dikenali!");
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TenagaMedisDAO Error - CREATE] " + e.getMessage());
            return false;
        }
    }

    /**
     * Mengambil seluruh data tenaga medis dan mengonversinya menjadi List polimorfik (READ ALL).
     */
    public List<TenagaMedis> getAll() {
        List<TenagaMedis> list = new ArrayList<>();
        String sql = "SELECT id_medis, tipe_medis, nama, spesialisasi, biaya_konsultasi, jam_lembur FROM tenaga_medis ORDER BY id_medis ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String id = rs.getString("id_medis");
                String tipe = rs.getString("tipe_medis");
                String nama = rs.getString("nama");
                String spesialisasi = rs.getString("spesialisasi");

                if ("Dokter".equalsIgnoreCase(tipe)) {
                    double biaya = rs.getDouble("biaya_konsultasi");
                    list.add(new Dokter(id, nama, spesialisasi, biaya));
                } else if ("Perawat".equalsIgnoreCase(tipe)) {
                    int lembur = rs.getInt("jam_lembur");
                    list.add(new Perawat(id, nama, spesialisasi, lembur));
                }
            }
        } catch (SQLException e) {
            System.err.println("[TenagaMedisDAO Error - GET ALL] " + e.getMessage());
        }
        return list;
    }

    /**
     * Mengambil satu tenaga medis berdasarkan ID (READ BY ID).
     */
    public TenagaMedis getById(String idMedis) {
        String sql = "SELECT id_medis, tipe_medis, nama, spesialisasi, biaya_konsultasi, jam_lembur FROM tenaga_medis WHERE id_medis = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idMedis);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id_medis");
                    String tipe = rs.getString("tipe_medis");
                    String nama = rs.getString("nama");
                    String spesialisasi = rs.getString("spesialisasi");

                    if ("Dokter".equalsIgnoreCase(tipe)) {
                        double biaya = rs.getDouble("biaya_konsultasi");
                        return new Dokter(id, nama, spesialisasi, biaya);
                    } else if ("Perawat".equalsIgnoreCase(tipe)) {
                        int lembur = rs.getInt("jam_lembur");
                        return new Perawat(id, nama, spesialisasi, lembur);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[TenagaMedisDAO Error - GET BY ID] " + e.getMessage());
        }
        return null;
    }

    /**
     * Memperbarui data tenaga medis (UPDATE).
     */
    public boolean update(TenagaMedis medis) {
        String sql = "UPDATE tenaga_medis SET nama = ?, spesialisasi = ?, biaya_konsultasi = ?, jam_lembur = ? WHERE id_medis = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, medis.getNama());
            ps.setString(2, medis.getSpesialisasi());

            if (medis instanceof Dokter) {
                Dokter d = (Dokter) medis;
                ps.setDouble(3, d.getBiayaKonsultasi());
                ps.setInt(4, 0);
            } else if (medis instanceof Perawat) {
                Perawat p = (Perawat) medis;
                ps.setDouble(3, 0.0);
                ps.setInt(4, p.getJamLembur());
            }

            ps.setString(5, medis.getIdMedis());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TenagaMedisDAO Error - UPDATE] " + e.getMessage());
            return false;
        }
    }

    /**
     * Menghapus tenaga medis berdasarkan ID (DELETE).
     */
    public boolean delete(String idMedis) {
        String sql = "DELETE FROM tenaga_medis WHERE id_medis = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idMedis);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TenagaMedisDAO Error - DELETE] " + e.getMessage());
            return false;
        }
    }
}
