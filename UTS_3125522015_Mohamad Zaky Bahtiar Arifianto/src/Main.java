import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.text.NumberFormat;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        System.out.println("==========================================================================");
        System.out.println("     UJIAN TENGAH SEMESTER (UTS) - PEMROGRAMAN BERORIENTASI OBYEK         ");
        System.out.println("                 TIPE B : SISTEM INFORMASI KLINIK                         ");
        System.out.println("==========================================================================");
        System.out.println("Nama Mahasiswa : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP            : 3125522015");
        System.out.println("Kelas          : 3D3PSDKU-SM-IT A");
        System.out.println("Dosen Pengampu : Nirwana Haidar Hari, S.Pd., M.Kom.");
        System.out.println("Institusi      : PENS PSDKU Sumenep\n");

        // =====================================================================
        // SKENARIO 1: INSTANSIASI OBJECT DARI SUBCLASS & ENTITAS (MINIMAL 3 OBJEK)
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 1: INSTANSIASI MINIMAL 3 OBJEK BERBEDA DENGAN DATA VALID]     ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Menginstansiasi objek Dokter, Perawat, dan Pasien secara konkret.");
        System.out.println("Catatan  : TenagaMedis adalah Abstract Class sehingga tidak dapat diinstansiasi langsung.\n");

        // Objek 1: Dokter (Subclass konkret TenagaMedis yang mengimplementasikan PelayananKlinik)
        Dokter drFarhan = new Dokter(
            "DOC-001",
            "dr. Farhan Malik, Sp.PD",
            "Spesialis Penyakit Dalam",
            150000.0
        );

        // Objek 2: Perawat (Subclass konkret TenagaMedis yang mengimplementasikan PelayananKlinik)
        Perawat nsHendra = new Perawat(
            "NUR-001",
            "Ns. Hendra Pratama, S.Kep",
            "Keperawatan Medikal Bedah",
            4
        );

        // Objek 3: Pasien (Entitas penerima layanan dengan status kepesertaan BPJS)
        Pasien pasAhmad = new Pasien(
            "PAS-001",
            "Ahmad Dahlan",
            45,
            true // Pasien BPJS Aktif
        );

        // Objek 4: Pasien Non-BPJS untuk komparasi validasi
        Pasien pasSiti = new Pasien(
            "PAS-002",
            "Siti Rahmawati",
            28,
            false // Pasien Umum / Mandiri
        );

        System.out.println("✓ Objek 1 (Dokter)  berhasil dibuat -> " + drFarhan);
        System.out.println("✓ Objek 2 (Perawat) berhasil dibuat -> " + nsHendra);
        System.out.println("✓ Objek 3 (Pasien)  berhasil dibuat -> " + pasAhmad);
        System.out.println("✓ Objek 4 (Pasien)  berhasil dibuat -> " + pasSiti);
        System.out.println();

        // =====================================================================
        // SKENARIO 2: ENCAPSULATION & DATA VALIDATION (BUSINESS RULES)
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 2: ENCAPSULATION & VALIDASI BUSINESS RULES KETAT]              ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Membuktikan seluruh variabel instance private dan akses setter");
        System.out.println("           memiliki validasi aturan bisnis yang menolak input ilegal.\n");

        // Uji coba 1: Validasi usia pasien negatif
        System.out.println("1. Menguji setter usia pasien dengan nilai negatif (-15):");
        try {
            pasAhmad.setUsia(-15);
            System.out.println("   [GAGAL] Sistem mengizinkan usia negatif!");
        } catch (IllegalArgumentException e) {
            System.out.println("   ✓ [VALIDASI BERHASIL]: Eksepsi ditangkap dengan aman -> " + e.getMessage());
        }

        // Uji coba 2: Validasi nama pasien kosong
        System.out.println("2. Menguji setter nama pasien dengan string kosong (\"\"):");
        try {
            pasAhmad.setNamaPasien("   ");
            System.out.println("   [GAGAL] Sistem mengizinkan nama kosong!");
        } catch (IllegalArgumentException e) {
            System.out.println("   ✓ [VALIDASI BERHASIL]: Eksepsi ditangkap dengan aman -> " + e.getMessage());
        }

        // Uji coba 3: Validasi biaya konsultasi dokter bernilai negatif
        System.out.println("3. Menguji setter tarif konsultasi dokter dengan nilai negatif (-50.000):");
        try {
            drFarhan.setBiayaKonsultasi(-50000);
            System.out.println("   [GAGAL] Sistem mengizinkan tarif negatif!");
        } catch (IllegalArgumentException e) {
            System.out.println("   ✓ [VALIDASI BERHASIL]: Eksepsi ditangkap dengan aman -> " + e.getMessage());
        }

        // Uji coba 4: Validasi jam lembur perawat bernilai negatif
        System.out.println("4. Menguji setter jam lembur perawat dengan nilai negatif (-3):");
        try {
            nsHendra.setJamLembur(-3);
            System.out.println("   [GAGAL] Sistem mengizinkan jam lembur negatif!");
        } catch (IllegalArgumentException e) {
            System.out.println("   ✓ [VALIDASI BERHASIL]: Eksepsi ditangkap dengan aman -> " + e.getMessage());
        }
        System.out.println();

        // =====================================================================
        // SKENARIO 3: INHERITANCE & ABSTRACT CLASS REFERENCE
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 3: INHERITANCE & ABSTRACT CLASS (TenagaMedis Reference)]       ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Memanggil method konkret dan method abstrak melalui referensi ");
        System.out.println("           Superclass TenagaMedis (Subclass Dokter & Perawat).\n");

        TenagaMedis refMedisDokter = drFarhan; // Superclass reference merujuk ke Subclass Dokter
        TenagaMedis refMedisPerawat = nsHendra; // Superclass reference merujuk ke Subclass Perawat

        System.out.println("--- Pemanggilan melalui referensi TenagaMedis (Dokter) ---");
        refMedisDokter.tampilkanPeran();
        System.out.println();

        System.out.println("--- Pemanggilan melalui referensi TenagaMedis (Perawat) ---");
        refMedisPerawat.tampilkanPeran();
        System.out.println();

        // =====================================================================
        // SKENARIO 4: POLYMORPHISM & INTERFACE (DYNAMIC METHOD BINDING)
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 4: POLYMORPHISM KOLEKTIF VIA INTERFACE PelayananKlinik]        ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Eksekusi melayaniPasien() dan hitungBiayaLayanan() secara dinamis");
        System.out.println("           menggunakan polymorphic array of interface PelayananKlinik.\n");

        PelayananKlinik[] daftarLayanan = new PelayananKlinik[]{drFarhan, nsHendra};
        double biayaDasarTindakan = 120000.0;

        for (int i = 0; i < daftarLayanan.length; i++) {
            PelayananKlinik layanan = daftarLayanan[i];
            System.out.println("Petugas Medis #" + (i + 1) + ":");
            // Perilaku berbeda secara dinamis (Dynamic Binding):
            layanan.melayaniPasien(pasAhmad.getNamaPasien());
            double totalBiaya = layanan.hitungBiayaLayanan(biayaDasarTindakan);
            System.out.println("  Biaya Dasar Tindakan : " + nf.format(biayaDasarTindakan));
            System.out.println("  Total Biaya Layanan  : " + nf.format(totalBiaya));
            System.out.println();
        }

        // =====================================================================
        // SKENARIO 5: FITUR KREATIVITAS (DISKON BPJS & INSENTIF LEMBUR & STRUK)
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 5: FITUR KREATIVITAS & CETAK STRUK TRANSAKSI KLINIK]          ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Menghitung diskon BPJS 50%, insentif lembur, dan mencetak struk.\n");

        // 1. Uji Fitur Kreativitas Diskon BPJS
        double subtotalLayanan = drFarhan.hitungBiayaLayanan(150000.0); // 150000 + 150000 (konsultasi) = 300000
        double diskonBpjsAhmad = pasAhmad.hitungDiskonBpjs(subtotalLayanan); // BPJS Aktif -> 150000
        double diskonBpjsSiti = pasSiti.hitungDiskonBpjs(subtotalLayanan);   // Non-BPJS -> 0

        System.out.println("1. Komparasi Diskon Otomatis Kepesertaan BPJS:");
        System.out.println("   - Pasien BPJS (" + pasAhmad.getNamaPasien() + "):");
        System.out.println("     Subtotal: " + nf.format(subtotalLayanan) + " | Diskon: " + nf.format(diskonBpjsAhmad) + 
                           " | Total Bayar: " + nf.format(subtotalLayanan - diskonBpjsAhmad));
        System.out.println("   - Pasien Umum (" + pasSiti.getNamaPasien() + "):");
        System.out.println("     Subtotal: " + nf.format(subtotalLayanan) + " | Diskon: " + nf.format(diskonBpjsSiti) + 
                           " | Total Bayar: " + nf.format(subtotalLayanan - diskonBpjsSiti));
        System.out.println();

        // 2. Uji Fitur Kreativitas Insentif Lembur Perawat
        System.out.println("2. Kalkulasi Insentif Lembur Tenaga Perawat:");
        System.out.println("   - Perawat: " + nsHendra.getNama() + " (" + nsHendra.getJamLembur() + " jam lembur)");
        System.out.println("   - Hak Insentif: " + nf.format(nsHendra.hitungInsentifLembur()) + 
                           " (Rp 50.000 / jam)");
        System.out.println();

        // 3. Uji Cetak Struk Transaksi Resmi
        System.out.println("3. Output Struk Transaksi Resmi Klinik (Format Termal):");
        RekamMedis rm1 = new RekamMedis(
            "RM-999", pasAhmad.getIdPasien(), drFarhan.getIdMedis(),
            "Demam tinggi dan sakit kepala", "Febris Akut & Dehidrasi Ringan",
            "Infus Ringer Laktat & Paracetamol IV", "2026-10-13 10:00:00"
        );
        TransaksiPembayaran trx1 = new TransaksiPembayaran(
            "TRX-001", rm1.getIdRekamMedis(), subtotalLayanan,
            diskonBpjsAhmad, subtotalLayanan - diskonBpjsAhmad,
            "BPJS Kesehatan", "2026-10-13 10:15:00"
        );
        System.out.println(trx1.generateStruk(pasAhmad, drFarhan, rm1));

        // =====================================================================
        // SKENARIO 6: PELUNCURAN ANTARMUKA GUI DESKTOP (SWING)
        // =====================================================================
        boolean launchGui = true;
        for (String arg : args) {
            if ("--nogui".equalsIgnoreCase(arg) || "--console".equalsIgnoreCase(arg)) {
                launchGui = false;
                break;
            }
        }

        if (launchGui) {
            System.out.println(">> Meluncurkan Antarmuka Pengguna Interaktif (Java Swing GUI Dashboard)...");
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {}
                KlinikMainFrame frame = new KlinikMainFrame();
                frame.setVisible(true);
            });
        } else {
            System.out.println(">> Mode CLI selesai (--nogui aktif). Sistem siap digunakan.");
        }
    }
}
