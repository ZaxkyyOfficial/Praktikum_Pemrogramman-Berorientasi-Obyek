/**
 * Class Main merupakan program pengujian utama Praktikum PBO Modul 4.
 * Topik: Relasi Antarobject: Association, Aggregation, dan Composition.
 * 
 * Memuat 3 Skenario Pengujian Komprehensif:
 * 1. SKENARIO 1 (Test 1): Instansiasi Objek Berhasil Dibuat (Dosen, MataKuliah, Mahasiswa, KRS).
 * 2. SKENARIO 2 (Test 2): Interaksi & Kolaborasi Antar-Objek (Asosiasi, Agregasi, dan Komposisi).
 * 3. SKENARIO 3 (Test 3): Penggunaan Data Objek Lain Melalui Method (Information Hiding & Enkapsulasi Terjaga).
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 4
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("     PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK (PBO) - MODUL 4             ");
        System.out.println("      Relasi Antarobject: Association, Aggregation, dan Composition       ");
        System.out.println("==========================================================================");
        System.out.println("Nama Mahasiswa : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP            : 3125522015");
        System.out.println("Program Studi  : D3 Teknik Informatika");
        System.out.println("Institusi      : PENS PSDKU Sumenep\n");

        // ======================================================================
        // SKENARIO 1 (TEST 1): OBJECT BERHASIL DIBUAT
        // ======================================================================
        System.out.println("##########################################################################");
        System.out.println("               [SKENARIO 1: PEMBUATAN OBJEK (TEST 1)]                     ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Memastikan semua objek dapat diinstansiasi dengan nilai valid,");
        System.out.println("           prinsip enkapsulasi terjaga, dan relasi terbentuk dengan baik.\n");

        // 1.1 Instansiasi Objek Dosen
        System.out.println(">>> 1.1 MEMBUAT OBJEK DOSEN");
        Dosen dosen1 = new Dosen("198504122010121003", "Nirwana Haidar Hari, S.Pd., M.Kom.", 
                                 "Rekayasa Perangkat Lunak & PBO", "nirwana@pens.ac.id");
        Dosen dosen2 = new Dosen("197806212005011002", "Firman Arifin, S.T., M.T.", 
                                 "Basis Data & Sistem Terdistribusi", "firman@pens.ac.id");
        System.out.println("[BERHASIL] Objek Dosen 1: " + dosen1.getNama() + " (" + dosen1.getNip() + ")");
        System.out.println("[BERHASIL] Objek Dosen 2: " + dosen2.getNama() + " (" + dosen2.getNip() + ")");

        // 1.2 Instansiasi Objek MataKuliah (Berasosiasi dengan Dosen Pengampu)
        System.out.println("\n>>> 1.2 MEMBUAT OBJEK MATA KULIAH (ASOSIASI DENGAN DOSEN PENGAMPU)");
        MataKuliah mk1 = new MataKuliah("IF201", "Pemrograman Berorientasi Obyek", 3, 3, dosen1, 30);
        MataKuliah mk2 = new MataKuliah("IF202", "Praktikum PBO", 2, 3, dosen1, 30);
        MataKuliah mk3 = new MataKuliah("IF203", "Basis Data Lanjut", 3, 3, dosen2, 25);
        MataKuliah mk4 = new MataKuliah("IF204", "Rekayasa Perangkat Lunak", 3, 3, dosen1, 28);
        System.out.println("[BERHASIL] Objek MK 1: " + mk1.getNamaMk() + " (" + mk1.getSks() + " SKS) - Pengampu: " + mk1.getDosenPengampu().getNama());
        System.out.println("[BERHASIL] Objek MK 2: " + mk2.getNamaMk() + " (" + mk2.getSks() + " SKS) - Pengampu: " + mk2.getDosenPengampu().getNama());
        System.out.println("[BERHASIL] Objek MK 3: " + mk3.getNamaMk() + " (" + mk3.getSks() + " SKS) - Pengampu: " + mk3.getDosenPengampu().getNama());
        System.out.println("[BERHASIL] Objek MK 4: " + mk4.getNamaMk() + " (" + mk4.getSks() + " SKS) - Pengampu: " + mk4.getDosenPengampu().getNama());

        // 1.3 Instansiasi Objek Mahasiswa (Mengonstruksi KRS secara Komposisi & Berasosiasi dengan Dosen Wali)
        System.out.println("\n>>> 1.3 MEMBUAT OBJEK MAHASISWA & INTI KOMPOSISI KRS");
        System.out.println("Catatan: Objek KRS otomatis dikonstruksi secara internal di dalam class Mahasiswa.");
        Mahasiswa mhs1 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", "D3 Teknik Informatika", 3, 3.82, dosen1);
        Mahasiswa mhs2 = new Mahasiswa("3125522022", "Ahmad Wildan Prasetyo", "D3 Teknik Informatika", 3, 2.40, dosen2);

        System.out.println("[BERHASIL] Objek Mahasiswa 1 : " + mhs1.getNama() + " (" + mhs1.getNrp() + ")");
        System.out.println("           Dosen Wali (Asosiasi)  : " + mhs1.getDosenWali().getNama());
        System.out.println("           KRS Internal (Komposisi): " + mhs1.getKrs().getNomorKrs() + 
                           " (Status Validasi: " + mhs1.getKrs().isDisetujui() + ")");

        System.out.println("[BERHASIL] Objek Mahasiswa 2 : " + mhs2.getNama() + " (" + mhs2.getNrp() + ")");
        System.out.println("           Dosen Wali (Asosiasi)  : " + mhs2.getDosenWali().getNama());
        System.out.println("           KRS Internal (Komposisi): " + mhs2.getKrs().getNomorKrs() + 
                           " (Status Validasi: " + mhs2.getKrs().isDisetujui() + ")");

        // ======================================================================
        // SKENARIO 2 (TEST 2): DUA ATAU LEBIH OBJECT BERINTERAKSI
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("          [SKENARIO 2: INTERAKSI & KOLABORASI ANTAR-OBJECT (TEST 2)]      ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Menguji kolaborasi objek pada Agregasi (KRS - MataKuliah),");
        System.out.println("           Komposisi (Mahasiswa - KRS), dan Asosiasi (Mahasiswa - Dosen Wali).\n");

        // 2.1 Interaksi Komposisi & Agregasi: Mahasiswa mendaftarkan MataKuliah ke KRS miliknya
        System.out.println(">>> 2.1 TRANSAKSI PENDAFTARAN MATA KULIAH (AGREGASI & KOMPOSISI)");
        System.out.println("Mahasiswa " + mhs1.getNama() + " mengambil mata kuliah semester 3:");
        mhs1.pilihMataKuliah(mk1);
        mhs1.pilihMataKuliah(mk2);
        mhs1.pilihMataKuliah(mk3);
        mhs1.pilihMataKuliah(mk4);

        System.out.println("\nStatus Peserta MK Pasca Pendaftaran:");
        System.out.println("- " + mk1.getNamaMk() + " : " + mk1.getPesertaTerdaftar() + "/" + mk1.getKuotaKelas() + " peserta");
        System.out.println("- " + mk2.getNamaMk() + " : " + mk2.getPesertaTerdaftar() + "/" + mk2.getKuotaKelas() + " peserta");
        System.out.println("- " + mk3.getNamaMk() + " : " + mk3.getPesertaTerdaftar() + "/" + mk3.getKuotaKelas() + " peserta");
        System.out.println("- " + mk4.getNamaMk() + " : " + mk4.getPesertaTerdaftar() + "/" + mk4.getKuotaKelas() + " peserta");

        // 2.2 Interaksi Asosiasi: Mahasiswa mengajukan KRS untuk divalidasi dan disahkan Dosen Wali
        System.out.println("\n>>> 2.2 ALUR PENGESAHAN DOKUMEN KRS (ASOSIASI MAHASISWA - DOSEN WALI)");
        System.out.println("Status persetujuan KRS sebelum disahkan: " + 
                           (mhs1.getKrs().isDisetujui() ? "DISETUJUI" : "BELUM DISETUJUI"));
        mhs1.ajukanPersetujuanKrs();
        System.out.println("Status persetujuan KRS setelah disahkan: " + 
                           (mhs1.getKrs().isDisetujui() ? "DISETUJUI RESMI" : "BELUM DISETUJUI"));

        // ======================================================================
        // SKENARIO 3 (TEST 3): DATA DARI OBJECT LAIN DIGUNAKAN MELALUI METHOD
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("    [SKENARIO 3: PENGGUNAAN DATA OBJECT LAIN MELALUI METHOD (TEST 3)]     ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Membuktikan bahwa seluruh pertukaran data antar-objek terjadi");
        System.out.println("           secara aman melalui method getter/kalkulasi (Enkapsulasi terjaga).\n");

        System.out.println(">>> 3.1 PENGHITUNGAN BEBAN SKS SECARA DINAMIS OLEH KRS");
        System.out.println("Objek KRS menghitung akumulasi total SKS dari array objek MataKuliah:");
        System.out.println("- Jumlah MK di KRS        : " + mhs1.getKrs().getJumlahMk() + " Mata Kuliah");
        System.out.println("- Total SKS via hitungKrs : " + mhs1.getKrs().hitungTotalSksKrs() + " SKS");
        System.out.println("- Total SKS pada Mahasiswa: " + mhs1.getTotalSks() + " SKS");
        System.out.println("- Batas Maks SKS Mahasiswa: " + mhs1.hitungBebanMaksimalSks() + " SKS (Berdasarkan IPK " + mhs1.getIpk() + ")");

        System.out.println("\n>>> 3.2 PEMANFAATAN DATA DOSEN PENGAMPU MELALUI OBJEK MATA KULIAH");
        System.out.println("Mata Kuliah: " + mk1.getNamaMk());
        System.out.println("Nama Pengampu (via mk1.getDosenPengampu().getNama())   : " + mk1.getDosenPengampu().getNama());
        System.out.println("Bidang Riset  (via mk1.getDosenPengampu().getKeahlian()): " + mk1.getDosenPengampu().getBidangKeahlian());
        System.out.println("Email Dosen   (via mk1.getDosenPengampu().getEmail())   : " + mk1.getDosenPengampu().getEmail());

        System.out.println("\n>>> 3.3 INTEGRASI DATA SELURUH OBJEK DALAM LEMBAR RESMI KRS");
        System.out.println("Mencetak dokumen resmi KRS hasil kolaborasi Mahasiswa, Dosen Wali, dan Mata Kuliah:\n");
        mhs1.getKrs().tampilkanKrs();

        System.out.println("==========================================================================");
        System.out.println("  SEMUA SKENARIO PENGUJIAN RELASI OBJEK MODUL 4 BERHASIL DILALUI DENGAN SUKSES! ");
        System.out.println("==========================================================================");
    }
}
