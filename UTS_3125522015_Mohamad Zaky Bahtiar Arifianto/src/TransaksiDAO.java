import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {

    public boolean create(TransaksiPembayaran tr) {
        String sql = "INSERT INTO transaksi_pembayaran (id_transaksi, id_rekam_medis, biaya_layanan, diskon_bpjs, total_bayar, metode_pembayaran, tanggal_transaksi) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tr.getIdTransaksi());
            ps.setString(2, tr.getIdRekamMedis());
            ps.setDouble(3, tr.getBiayaLayanan());
            ps.setDouble(4, tr.getDiskonBpjs());
            ps.setDouble(5, tr.getTotalBayar());
            ps.setString(6, tr.getMetodePembayaran());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO Error - CREATE] " + e.getMessage());
            return false;
        }
    }

    public List<TransaksiPembayaran> getAll() {
        List<TransaksiPembayaran> list = new ArrayList<>();
        String sql = "SELECT id_transaksi, id_rekam_medis, biaya_layanan, diskon_bpjs, total_bayar, metode_pembayaran, tanggal_transaksi FROM transaksi_pembayaran ORDER BY tanggal_transaksi DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TransaksiPembayaran tr = new TransaksiPembayaran(
                    rs.getString("id_transaksi"),
                    rs.getString("id_rekam_medis"),
                    rs.getDouble("biaya_layanan"),
                    rs.getDouble("diskon_bpjs"),
                    rs.getDouble("total_bayar"),
                    rs.getString("metode_pembayaran"),
                    rs.getString("tanggal_transaksi")
                );
                list.add(tr);
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO Error - GET ALL] " + e.getMessage());
        }
        return list;
    }

    public TransaksiPembayaran getById(String idTransaksi) {
        String sql = "SELECT id_transaksi, id_rekam_medis, biaya_layanan, diskon_bpjs, total_bayar, metode_pembayaran, tanggal_transaksi FROM transaksi_pembayaran WHERE id_transaksi = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TransaksiPembayaran(
                        rs.getString("id_transaksi"),
                        rs.getString("id_rekam_medis"),
                        rs.getDouble("biaya_layanan"),
                        rs.getDouble("diskon_bpjs"),
                        rs.getDouble("total_bayar"),
                        rs.getString("metode_pembayaran"),
                        rs.getString("tanggal_transaksi")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[TransaksiDAO Error - GET BY ID] " + e.getMessage());
        }
        return null;
    }
}
