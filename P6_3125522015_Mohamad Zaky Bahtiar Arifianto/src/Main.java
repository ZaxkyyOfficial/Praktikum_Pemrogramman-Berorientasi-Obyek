/**
 * Class Main merupakan program pengujian utama Praktikum PBO Modul 6.
 * Topik: Polymorphism, Method Overriding, Method Overloading, dan Dynamic Binding.
 * 
 * Pengujian Terstruktur:
 * 1. Upcasting & Polymorphic Collection: Menyimpan objek aktual subclass ke array bertipe superclass CivitasAkademika[].
 * 2. Dynamic Binding (Runtime Polymorphism): Melakukan perulangan untuk memanggil method @Override tampilkanPeran().
 * 3. Method Overloading (Compile-Time Polymorphism): Menguji pemanggilan method dengan nama sama namun parameter berbeda.
 * 4. Integrasi Relasi Objek P4: Menjamin relasi Association, Aggregation, dan Composition tetap berfungsi secara harmonis.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 6
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("     PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK (PBO) - MODUL 6             ");
        System.out.println(" Polymorphism, Method Overriding, Method Overloading, dan Dynamic Binding ");
        System.out.println("==========================================================================");
        System.out.println("Nama Mahasiswa : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP            : 3125522015");
        System.out.println("Program Studi  : D3 Teknik Informatika");
        System.out.println("Institusi      : PENS PSDKU Sumenep\n");

        // Inisialisasi Objek Dosen
        Dosen dosen1 = new Dosen("198504122010121003", "Nirwana Haidar Hari, S.Pd., M.Kom.", 
                                 "Rekayasa Perangkat Lunak & PBO", "nirwana@pens.ac.id");
        Dosen dosen2 = new Dosen("197806212005011002", "Firman Arifin, S.T., M.T.", 
                                 "Basis Data & Sistem Terdistribusi", "firman@pens.ac.id");

        // Inisialisasi Objek Mahasiswa
        Mahasiswa mhs1 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", 
                                       "zaky@student.pens.ac.id", "D3 Teknik Informatika", 3, 3.82, dosen1);
        Mahasiswa mhs2 = new Mahasiswa("3125522022", "Ahmad Wildan Prasetyo", 
                                       "wildan@student.pens.ac.id", "D3 Teknik Informatika", 3, 2.40, dosen2);

        // ======================================================================
        // SKENARIO 1: UPCASTING & POLYMORPHIC COLLECTION (CivitasAkademika[])
        // ======================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 1: UPCASTING & POLYMORPHIC COLLECTION (CivitasAkademika[])]    ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Objek aktual Mahasiswa dan Dosen di-upcast ke tipe referensi");
        System.out.println("           Superclass CivitasAkademika dan disimpan dalam array campuran.\n");

        // Demonstrasi Upcasting eksplisit
        CivitasAkademika refCivitas = mhs1; // Upcasting Mahasiswa -> CivitasAkademika
        System.out.println("[UPCASTING BERHASIL] Objek Mahasiswa dirujuk oleh referensi tipe CivitasAkademika.");
        System.out.println("Tipe Referensi : " + refCivitas.getClass().getSuperclass().getSimpleName());
        System.out.println("Objek Aktual   : " + refCivitas.getClass().getSimpleName());

        // Polymorphic Collection: Array bertipe Superclass berisi objek campuran Subclass
        CivitasAkademika[] daftarCivitas = {
            mhs1,   // Objek Aktual: Mahasiswa
            dosen1, // Objek Aktual: Dosen
            mhs2,   // Objek Aktual: Mahasiswa
            dosen2  // Objek Aktual: Dosen
        };
        System.out.println("\n[POLYMORPHIC COLLECTION] Array CivitasAkademika[] terbentuk dengan " + 
                           daftarCivitas.length + " elemen objek campuran.");

        // ======================================================================
        // SKENARIO 2: DYNAMIC BINDING (RUNTIME POLYMORPHISM VIA tampilkanPeran())
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println(" [SKENARIO 2: DYNAMIC BINDING / RUNTIME DISPATCH VIA tampilkanPeran()]    ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Melakukan perulangan pada array CivitasAkademika[]. Meskipun");
        System.out.println("           tipe referensi seragam, JVM menentukan method yang dieksekusi");
        System.out.println("           pada saat runtime berdasarkan tipe objek aktualnya.\n");

        int index = 1;
        for (CivitasAkademika civitas : daftarCivitas) {
            System.out.println("Indeks [" + index + "] Tipe Referensi: CivitasAkademika | Objek Aktual: " + civitas.getClass().getSimpleName());
            // Dynamic Binding terjadi di sini:
            civitas.tampilkanPeran();
            System.out.println();
            index++;
        }

        // ======================================================================
        // SKENARIO 3: METHOD OVERLOADING (COMPILE-TIME POLYMORPHISM)
        // ======================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 3: METHOD OVERLOADING (COMPILE-TIME POLYMORPHISM)]             ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Membuktikan pemanggilan method dengan nama yang sama namun");
        System.out.println("           memiliki parameter berbeda dalam satu hirarki class.\n");

        // 3.1 Overloading pada CivitasAkademika (cetakKartuIdentitas)
        System.out.println(">>> 3.1 OVERLOADING: cetakKartuIdentitas() PADA CivitasAkademika");
        System.out.println("--- Pemanggilan 1: cetakKartuIdentitas() [Default Parameter] ---");
        mhs1.cetakKartuIdentitas();

        System.out.println("--- Pemanggilan 2: cetakKartuIdentitas(String) [Custom Header Parameter] ---");
        dosen1.cetakKartuIdentitas("KARTU IDENTITAS DOSEN & TENAGA PENGAJAR RESMI");

        // 3.2 Overloading pada Mahasiswa (pilihMataKuliah)
        System.out.println("\n>>> 3.2 OVERLOADING: pilihMataKuliah() PADA Mahasiswa");
        MataKuliah mk1 = new MataKuliah("IF201", "Pemrograman Berorientasi Obyek", 3, 3, dosen1, 30);
        MataKuliah mk2 = new MataKuliah("IF202", "Praktikum PBO", 2, 3, dosen1, 30);
        MataKuliah mk3 = new MataKuliah("IF203", "Basis Data Lanjut", 3, 3, dosen2, 25);
        MataKuliah mk4 = new MataKuliah("IF204", "Rekayasa Perangkat Lunak", 3, 3, dosen1, 28);

        System.out.println("--- Pemanggilan Overload 1: pilihMataKuliah(mk) [Reguler] ---");
        mhs1.pilihMataKuliah(mk1);

        System.out.println("\n--- Pemanggilan Overload 2: pilihMataKuliah(mk, kategori) [Kategori Khusus] ---");
        mhs1.pilihMataKuliah(mk2, "Praktikum Wajib Kurikulum");
        mhs1.pilihMataKuliah(mk3, "Mata Kuliah Pilihan Prodi");
        mhs1.pilihMataKuliah(mk4, "Program Akselerasi Kompetensi");

        // 3.3 Overloading pada Dosen (validasiDanSetujuiKrs)
        System.out.println("\n>>> 3.3 OVERLOADING: validasiDanSetujuiKrs() PADA Dosen");
        System.out.println("--- Pemanggilan Overload 2: validasiDanSetujuiKrs(krs, catatan) ---");
        dosen1.validasiDanSetujuiKrs(mhs1.getKrs(), "KRS disetujui, pertahankan IPK di atas 3.50!");

        // ======================================================================
        // SKENARIO 4: VERIFIKASI KEUTUHAN RELASI OBJEK P4 (PBO INTEGRATION)
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println(" [SKENARIO 4: INTEGRASI RELASI P4 (ASSOCIATION, AGGREGATION, COMPOSITION)]");
        System.out.println("##########################################################################");
        System.out.println("Mencetak dokumen resmi KRS hasil kolaborasi seluruh konsep OOP:\n");
        mhs1.getKrs().tampilkanKrs();

        System.out.println("==========================================================================");
        System.out.println("  SEMUA PENGUJIAN POLYMORPHISM MODUL 6 BERHASIL DILALUI DENGAN SUKSES!   ");
        System.out.println("==========================================================================");
    }
}
