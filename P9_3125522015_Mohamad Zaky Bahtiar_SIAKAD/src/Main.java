import service.AkademikService;
import service.PengujianSIAKAD;
import view.LoginFrame;
import view.SiakadFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Konfigurasi Render Teks Resolusi Tinggi & Font Anti-Aliasing Sistem
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        cetakHeader();

        // 1. Opsi Eksekusi Suite Pengujian Otomatis via Terminal
        if (args.length > 0 && "test".equalsIgnoreCase(args[0])) {
            boolean sukses = PengujianSIAKAD.jalankanPengujian();
            System.exit(sukses ? 0 : 1);
            return;
        }

        // 2. Inisialisasi Service Layer & State OOP
        AkademikService service = new AkademikService();
        System.out.println("[INIT] Service akademik berhasil dimuat.");
        System.out.println("[INIT] Status database MySQL: " + (service.isDatabaseConnected() ? "TERHUBUNG (XAMPP)" : "IN-MEMORY FALLBACK"));

        // 3. Jalankan Pengujian Logika Bisnis Cepat di Terminal
        System.out.println("\n------------------------------------------------------------");
        System.out.println("VERIFIKASI LOGIKA BISNIS BERORIENTASI OBYEK (OOP TEST RUNNER)");
        System.out.println("------------------------------------------------------------");
        PengujianSIAKAD.jalankanPengujian();

        // 4. Luncurkan Desktop Native GUI Swing (Login Authentication Gate)
        System.out.println("\n============================================================");
        System.out.println("MELUNCURKAN AUTENTIKASI PENGGUNA (LoginFrame)");
        System.out.println("Resolusi : 500 x 420 piksel (Tengah Layar)");
        System.out.println("Gaya     : Light Luxury • Zero Icon • Multi-Mahasiswa Gate");
        System.out.println("============================================================\n");

        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            } catch (Exception ex) {
                System.err.println("[ERROR] Gagal meluncurkan antarmuka Login: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private static void cetakHeader() {
        System.out.println("============================================================");
        System.out.println("  SISTEM INFORMASI AKADEMIK (SIAKAD) - PERTEMUAN 9 PBO      ");
        System.out.println("  Aplikasi Desktop Native Murni Berbasis Java Swing (JFrame)");
        System.out.println("  Pengembang : Mohamad Zaky Bahtiar Arifianto (3125522015)  ");
        System.out.println("  Institusi  : PENS PSDKU Sumenep - Teknik Informatika      ");
        System.out.println("============================================================");
    }
}
