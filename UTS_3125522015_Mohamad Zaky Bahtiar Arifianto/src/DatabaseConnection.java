import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db_klinik?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection connection = null;

    // Private constructor (mencegah instansiasi langsung - Singleton Pattern)
    private DatabaseConnection() {}

    /**
     * Mendapatkan objek koneksi JDBC aktif.
     * Menggunakan lazy initialization dengan pengecekan status open/closed.
     * 
     * @return Connection JDBC ke database MySQL
     * @throws SQLException jika koneksi gagal dibuka
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Register driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.err.println("[JDBC Error] Driver MySQL Connector/J tidak ditemukan di Classpath!");
                throw new SQLException("Driver MySQL tidak ditemukan: " + e.getMessage(), e);
            } catch (SQLException e) {
                System.err.println("[JDBC Error] Gagal terhubung ke MySQL XAMPP (db_klinik): " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Mengecek status ketersediaan database MySQL secara langsung.
     * 
     * @return True jika database berhasil dihubungi
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
