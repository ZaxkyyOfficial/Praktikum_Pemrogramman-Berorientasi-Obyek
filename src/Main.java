/**
 * Class Main merupakan program pengujian utama Praktikum PBO Modul 5.
 * Topik: Inheritance, Generalization, Superclass, dan Subclass.
 * 
 * Memuat Skenario Pengujian Komprehensif:
 * 1. Instansiasi objek subclass (Mahasiswa dan Dosen) menggunakan Constructor Chaining via super(...).
 * 2. Pemanggilan method-method yang diwarisi dari superclass (CivitasAkademika).
 * 3. Pemanggilan method-method spesifik dari masing-masing subclass.
 * 4. Pengujian Polimorfisme referensi Superclass terhadap Subclass.
 * 5. Mempertahankan keutuhan relasi objek P4 (Association, Aggregation, dan Composition).
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 5
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("     PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK (PBO) - MODUL 5             ");
        System.out.println("         Inheritance, Generalization, Superclass, dan Subclass            ");
        System.out.println("==========================================================================");
        System.out.println("Nama Mahasiswa : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP            : 3125522015");
        System.out.println("Program Studi  : D3 Teknik Informatika");
        System.out.println("Institusi      : PENS PSDKU Sumenep\n");

        // ======================================================================
        // SKENARIO 1: PEMBUATAN OBJEK DARI SUBCLASS & CONSTRUCTOR CHAINING (super)
        // ======================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 1: INSTANSIASI SUBCLASS & CONSTRUCTOR CHAINING VIA super()]    ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Menguji pembuatan objek dari subclass Dosen dan Mahasiswa.");
        System.out.println("           Constructor subclass memanggil super(...) untuk inisialisasi");
        System.out.println("           atribut umum (nomorInduk, nama, email, jenisCivitas).\n");

        // 1.1 Instansiasi Objek Subclass Dosen (extends CivitasAkademika)
        System.out.println(">>> 1.1 MEMBUAT OBJEK DARI SUBCLASS DOSEN");
        Dosen dosen1 = new Dosen("198504122010121003", "Nirwana Haidar Hari, S.Pd., M.Kom.", 
                                 "Rekayasa Perangkat Lunak & PBO", "nirwana@pens.ac.id");
        Dosen dosen2 = new Dosen("197806212005011002", "Firman Arifin, S.T., M.T.", 
                                 "Basis Data & Sistem Terdistribusi", "firman@pens.ac.id");
        System.out.println("[BERHASIL] Objek Dosen 1 terinstansiasi melalui super()");
        System.out.println("[BERHASIL] Objek Dosen 2 terinstansiasi melalui super()");

        // 1.2 Instansiasi Objek Subclass Mahasiswa (extends CivitasAkademika)
        System.out.println("\n>>> 1.2 MEMBUAT OBJEK DARI SUBCLASS MAHASISWA");
        Mahasiswa mhs1 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", 
                                       "zaky@student.pens.ac.id", "D3 Teknik Informatika", 3, 3.82, dosen1);
        Mahasiswa mhs2 = new Mahasiswa("3125522022", "Ahmad Wildan Prasetyo", 
                                       "wildan@student.pens.ac.id", "D3 Teknik Informatika", 3, 2.40, dosen2);
        System.out.println("[BERHASIL] Objek Mahasiswa 1 terinstansiasi melalui super()");
        System.out.println("[BERHASIL] Objek Mahasiswa 2 terinstansiasi melalui super()");

        // ======================================================================
        // SKENARIO 2: MEMANGGIL METHOD YANG DIWARISI DARI SUPERCLASS
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("      [SKENARIO 2: PEMANGGILAN METHOD DARI SUPERCLASS (INHERITED)]        ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Membuktikan bahwa subclass Mahasiswa dan Dosen mewarisi method");
        System.out.println("           dari superclass CivitasAkademika tanpa duplikasi kode.\n");

        System.out.println(">>> 2.1 MEMANGGIL METHOD GETTER SUPERCLASS PADA OBJEK MAHASISWA:");
        System.out.println("- Jenis Civitas (getJenisCivitas) : " + mhs1.getJenisCivitas());
        System.out.println("- Nomor Induk   (getNomorInduk)   : " + mhs1.getNomorInduk());
        System.out.println("- Nama Lengkap  (getNama)         : " + mhs1.getNama());
        System.out.println("- Email Resmi   (getEmail)        : " + mhs1.getEmail());

        System.out.println("\n>>> 2.2 MEMANGGIL METHOD GETTER SUPERCLASS PADA OBJEK DOSEN:");
        System.out.println("- Jenis Civitas (getJenisCivitas) : " + dosen1.getJenisCivitas());
        System.out.println("- Nomor Induk   (getNomorInduk)   : " + dosen1.getNomorInduk());
        System.out.println("- Nama Lengkap  (getNama)         : " + dosen1.getNama());
        System.out.println("- Email Resmi   (getEmail)        : " + dosen1.getEmail());

        System.out.println("\n>>> 2.3 MEMANGGIL METHOD COMMON tampilkanIdentitas() SUPERCLASS SECARA POLIMORFIS:");
        CivitasAkademika refCivitas1 = mhs1;   // Polimorfisme: Superclass referensi ke Subclass Mahasiswa
        CivitasAkademika refCivitas2 = dosen1; // Polimorfisme: Superclass referensi ke Subclass Dosen
        refCivitas1.tampilkanIdentitas();
        refCivitas2.tampilkanIdentitas();

        // ======================================================================
        // SKENARIO 3: MEMANGGIL METHOD SPESIFIK DARI MASING-MASING SUBCLASS
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("        [SKENARIO 3: PEMANGGILAN METHOD SPESIFIK DARI SUBCLASS]           ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Menguji method dan atribut khusus yang hanya dimiliki oleh");
        System.out.println("           masing-masing subclass (differensiasi kapabilitas).\n");

        System.out.println(">>> 3.1 METHOD KHUSUS SUBCLASS MAHASISWA:");
        System.out.println("- Program Studi (getProdi)                 : " + mhs1.getProdi());
        System.out.println("- Semester Aktif (getSemester)             : Semester " + mhs1.getSemester());
        System.out.printf("- IPK Akademik (getIpk)                    : %.2f\n", mhs1.getIpk());
        System.out.println("- Batas Maksimal SKS (hitungBebanMaksSks) : " + mhs1.hitungBebanMaksimalSks() + " SKS");

        System.out.println("\n>>> 3.2 METHOD KHUSUS SUBCLASS DOSEN:");
        System.out.println("- Bidang Keahlian (getBidangKeahlian)      : " + dosen1.getBidangKeahlian());

        // ======================================================================
        // SKENARIO 4: VERIFIKASI KEUTUHAN RELASI OBJEK P4 (PBO INTEGRATION)
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("     [SKENARIO 4: INTEGRASI DENGAN RELASI OBJEK P4 (TETAP TERJAGA)]       ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Memastikan relasi Association, Aggregation, dan Composition");
        System.out.println("           tetap bekerja 100% sempurna setelah refactoring inheritance.\n");

        // 4.1 Persiapan MataKuliah (Asosiasi dengan Dosen)
        MataKuliah mk1 = new MataKuliah("IF201", "Pemrograman Berorientasi Obyek", 3, 3, dosen1, 30);
        MataKuliah mk2 = new MataKuliah("IF202", "Praktikum PBO", 2, 3, dosen1, 30);
        MataKuliah mk3 = new MataKuliah("IF203", "Basis Data Lanjut", 3, 3, dosen2, 25);
        MataKuliah mk4 = new MataKuliah("IF204", "Rekayasa Perangkat Lunak", 3, 3, dosen1, 28);

        // 4.2 Agregasi & Komposisi: Mahasiswa mengambil mata kuliah ke dalam KRS internalnya
        System.out.println(">>> 4.1 TRANSAKSI AGREGASI MATA KULIAH KE DALAM KRS MAHASISWA:");
        mhs1.pilihMataKuliah(mk1);
        mhs1.pilihMataKuliah(mk2);
        mhs1.pilihMataKuliah(mk3);
        mhs1.pilihMataKuliah(mk4);

        // 4.3 Asosiasi: Mahasiswa mengajukan KRS untuk divalidasi Dosen Wali
        System.out.println("\n>>> 4.2 PENGESAHAN DOKUMEN KRS OLEH DOSEN WALI (ASOSIASI):");
        mhs1.ajukanPersetujuanKrs();

        // 4.4 Menampilkan lembar resmi KRS hasil integrasi Superclass, Subclass, dan Relasi P4
        System.out.println("\n>>> 4.3 LEMBAR RESMI HASIL PENCETAKAN KRS:");
        mhs1.getKrs().tampilkanKrs();

        System.out.println("==========================================================================");
        System.out.println("  SEMUA PENGUJIAN INHERITANCE MODUL 5 BERHASIL DILALUI DENGAN SUKSES!    ");
        System.out.println("==========================================================================");
    }
}
