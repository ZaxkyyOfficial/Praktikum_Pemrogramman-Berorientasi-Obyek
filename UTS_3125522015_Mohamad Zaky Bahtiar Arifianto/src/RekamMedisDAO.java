import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RekamMedisDAO {

    public boolean create(RekamMedis rm) {
        String sql = "INSERT INTO rekam_medis (id_rekam_medis, id_pasien, id_medis, keluhan, diagnosa, tindakan, tanggal_periksa) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rm.getIdRekamMedis());
            ps.setString(2, rm.getIdPasien());
            ps.setString(3, rm.getIdMedis());
            ps.setString(4, rm.getKeluhan());
            ps.setString(5, rm.getDiagnosa());
            ps.setString(6, rm.getTindakan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RekamMedisDAO Error - CREATE] " + e.getMessage());
            return false;
        }
    }

    public List<RekamMedis> getAll() {
        List<RekamMedis> list = new ArrayList<>();
        String sql = "SELECT id_rekam_medis, id_pasien, id_medis, keluhan, diagnosa, tindakan, tanggal_periksa FROM rekam_medis ORDER BY tanggal_periksa DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RekamMedis rm = new RekamMedis(
                    rs.getString("id_rekam_medis"),
                    rs.getString("id_pasien"),
                    rs.getString("id_medis"),
                    rs.getString("keluhan"),
                    rs.getString("diagnosa"),
                    rs.getString("tindakan"),
                    rs.getString("tanggal_periksa")
                );
                list.add(rm);
            }
        } catch (SQLException e) {
            System.err.println("[RekamMedisDAO Error - GET ALL] " + e.getMessage());
        }
        return list;
    }

    public RekamMedis getById(String idRekamMedis) {
        String sql = "SELECT id_rekam_medis, id_pasien, id_medis, keluhan, diagnosa, tindakan, tanggal_periksa FROM rekam_medis WHERE id_rekam_medis = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idRekamMedis);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RekamMedis(
                        rs.getString("id_rekam_medis"),
                        rs.getString("id_pasien"),
                        rs.getString("id_medis"),
                        rs.getString("keluhan"),
                        rs.getString("diagnosa"),
                        rs.getString("tindakan"),
                        rs.getString("tanggal_periksa")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[RekamMedisDAO Error - GET BY ID] " + e.getMessage());
        }
        return null;
    }

    public boolean delete(String idRekamMedis) {
        String sql = "DELETE FROM rekam_medis WHERE id_rekam_medis = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idRekamMedis);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RekamMedisDAO Error - DELETE] " + e.getMessage());
            return false;
        }
    }
}
