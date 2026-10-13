import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PasienDAO {

    /**
     * Menyimpan data pasien baru ke database (CREATE).
     */
    public boolean create(Pasien pasien) {
        String sql = "INSERT INTO pasien (id_pasien, nama_pasien, usia, status_bpjs) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pasien.getIdPasien());
            ps.setString(2, pasien.getNamaPasien());
            ps.setInt(3, pasien.getUsia());
            ps.setBoolean(4, pasien.isStatusBpjs());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PasienDAO Error - CREATE] " + e.getMessage());
            return false;
        }
    }

    /**
     * Mengambil seluruh data pasien dari database (READ ALL).
     */
    public List<Pasien> getAll() {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT id_pasien, nama_pasien, usia, status_bpjs FROM pasien ORDER BY id_pasien ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pasien p = new Pasien(
                    rs.getString("id_pasien"),
                    rs.getString("nama_pasien"),
                    rs.getInt("usia"),
                    rs.getBoolean("status_bpjs")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("[PasienDAO Error - GET ALL] " + e.getMessage());
        }
        return list;
    }

    /**
     * Mengambil data spesifik satu pasien berdasarkan ID (READ BY ID).
     */
    public Pasien getById(String idPasien) {
        String sql = "SELECT id_pasien, nama_pasien, usia, status_bpjs FROM pasien WHERE id_pasien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPasien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pasien(
                        rs.getString("id_pasien"),
                        rs.getString("nama_pasien"),
                        rs.getInt("usia"),
                        rs.getBoolean("status_bpjs")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[PasienDAO Error - GET BY ID] " + e.getMessage());
        }
        return null;
    }

    /**
     * Memperbarui data pasien yang sudah ada (UPDATE).
     */
    public boolean update(Pasien pasien) {
        String sql = "UPDATE pasien SET nama_pasien = ?, usia = ?, status_bpjs = ? WHERE id_pasien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pasien.getNamaPasien());
            ps.setInt(2, pasien.getUsia());
            ps.setBoolean(3, pasien.isStatusBpjs());
            ps.setString(4, pasien.getIdPasien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PasienDAO Error - UPDATE] " + e.getMessage());
            return false;
        }
    }

    /**
     * Menghapus data pasien berdasarkan ID (DELETE).
     */
    public boolean delete(String idPasien) {
        String sql = "DELETE FROM pasien WHERE id_pasien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPasien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PasienDAO Error - DELETE] " + e.getMessage());
            return false;
        }
    }
}
