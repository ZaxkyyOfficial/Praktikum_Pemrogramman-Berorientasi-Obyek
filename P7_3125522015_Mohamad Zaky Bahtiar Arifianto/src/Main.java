/**
 * Class Main mengeksekusi 4 skenario pengujian polymorphism utama sesuai ketentuan Modul 7:
 * 1. Skenario 1: Instansiasi objek subclass konkret berhasil dibuat (Mahasiswa & Dosen).
 * 2. Skenario 2: Pemanggilan abstract method melalui reference superclass (Abstract Class Reference -> Subclass Object).
 * 3. Skenario 3: Pemanggilan method kontrak melalui reference interface (Interface Reference -> Concrete Object).
 * 4. Skenario 4: Polymorphism kolektif menggunakan Array bertipe superclass & interface dengan loop for-each (Dynamic Binding).
 * Serta skenario integrasi menyeluruh yang membuktikan relasi P4-P6 tetap berjalan utuh tanpa regresi.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 7 (Abstract Class & Interface)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("     PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK (PBO) - MODUL 7             ");
        System.out.println("          Abstract Class, Abstract Method, dan Interface                  ");
        System.out.println("==========================================================================");
        System.out.println("Nama Mahasiswa : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP            : 3125522015");
        System.out.println("Program Studi  : D3 Teknik Informatika");
        System.out.println("Institusi      : PENS PSDKU Sumenep\n");

        // =====================================================================
        // SKENARIO 1: INSTANSIASI OBJECT SUBCLASS KONKRET BERHASIL DIBUAT
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 1: INSTANSIASI OBJECT SUBCLASS KONKRET BERHASIL DIBUAT]       ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Menginstansiasi objek konkret dari subclass Mahasiswa dan Dosen.");
        System.out.println("Catatan  : Superclass CivitasAkademika bersifat 'abstract', sehingga pemanggilan");
        System.out.println("           'new CivitasAkademika(...)' dicegah oleh compiler secara ketat.\n");

        // 1. Instansiasi objek Dosen (Subclass konkret 1)
        Dosen dsnWali = new Dosen(
            "198504122010121003",
            "Nirwana Haidar Hari, S.Pd., M.Kom.",
            "Rekayasa Perangkat Lunak & PBO",
            "nirwana@pens.ac.id"
        );

        Dosen dsnPengampu = new Dosen(
            "197806212005011002",
            "Firman Arifin, S.T., M.T.",
            "Basis Data & Sistem Terdistribusi",
            "firman@pens.ac.id"
        );

        // 2. Instansiasi objek Mahasiswa (Subclass konkret 2)
        Mahasiswa mhs1 = new Mahasiswa(
            "3125522015",
            "Mohamad Zaky Bahtiar Arifianto",
            "zaky@student.pens.ac.id",
            "D3 Teknik Informatika",
            3,
            3.82,
            dsnWali
        );

        Mahasiswa mhs2 = new Mahasiswa(
            "3125522022",
            "Ahmad Wildan Prasetyo",
            "wildan@student.pens.ac.id",
            "D3 Teknik Informatika",
            3,
            3.65,
            dsnWali
        );

        System.out.println("[SUKSES] Objek Subclass Konkret Berhasil Dibuat:");
        System.out.println("1. Mahasiswa : " + mhs1.getNama() + " (NRP: " + mhs1.getNrp() + ")");
        System.out.println("2. Mahasiswa : " + mhs2.getNama() + " (NRP: " + mhs2.getNrp() + ")");
        System.out.println("3. Dosen     : " + dsnWali.getNama() + " (NIP: " + dsnWali.getNip() + ")");
        System.out.println("4. Dosen     : " + dsnPengampu.getNama() + " (NIP: " + dsnPengampu.getNip() + ")\n");

        // =====================================================================
        // SKENARIO 2: PEMANGGILAN ABSTRACT METHOD VIA REFERENCE SUPERCLASS
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 2: PEMANGGILAN ABSTRACT METHOD VIA REFERENCE SUPERCLASS]      ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Referensi bertipe Abstract Class CivitasAkademika merujuk ke");
        System.out.println("           objek aktual Mahasiswa dan Dosen. Pemanggilan method abstrak");
        System.out.println("           tampilkanPeran() diarahkan secara dinamis ke subclass (Dynamic Binding).\n");

        // Upcasting ke referensi Abstract Superclass
        CivitasAkademika refCivitas1 = mhs1;
        CivitasAkademika refCivitas2 = dsnWali;

        System.out.println("Tipe Referensi : CivitasAkademika (Abstract Class)");
        System.out.println("Objek Aktual 1 : Mahasiswa");
        System.out.print("Output Eksekusi: ");
        refCivitas1.tampilkanPeran();

        System.out.println("\nTipe Referensi : CivitasAkademika (Abstract Class)");
        System.out.println("Objek Aktual 2 : Dosen");
        System.out.print("Output Eksekusi: ");
        refCivitas2.tampilkanPeran();
        System.out.println();

        // =====================================================================
        // SKENARIO 3: PEMANGGILAN METHOD MELALUI REFERENCE INTERFACE
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 3: PEMANGGILAN METHOD MELALUI REFERENCE INTERFACE]            ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Referensi bertipe Interface DapatDiotentikasi merujuk ke objek");
        System.out.println("           aktual Mahasiswa dan Dosen untuk menguji kontrak otentikasi portal.\n");

        // Polimorfisme melalui interface reference
        DapatDiotentikasi authUserMhs = mhs1;
        DapatDiotentikasi authUserDsn = dsnWali;

        System.out.println(">>> 3.1 PENGUJIAN OTENTIKASI & KONTRAK PORTAL: MAHASISWA");
        authUserMhs.login("3125522015", "mhs3125522015");
        authUserMhs.cetakInfoSesi();
        authUserMhs.tampilkanHakAkses();

        System.out.println("\n>>> 3.2 PENGUJIAN OTENTIKASI & KONTRAK PORTAL: DOSEN");
        authUserDsn.login("198504122010121003", "dsn198504122010121003");
        authUserDsn.cetakInfoSesi();
        authUserDsn.tampilkanHakAkses();
        System.out.println();

        // =====================================================================
        // SKENARIO 4: POLYMORPHISM KOLEKTIF VIA ARRAY SUPERCLASS & INTERFACE
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO 4: POLYMORPHISM KOLEKTIF DENGAN PERULANGAN FOR-EACH]          ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Mengelompokkan objek campuran ke dalam array superclass dan");
        System.out.println("           array interface, lalu melakukan iterasi polimorfik.\n");

        // 4.1 Koleksi Polimorfik Superclass CivitasAkademika[]
        System.out.println(">>> 4.1 KOLEKSI POLIMORFIK BERTIPE CivitasAkademika[] (Superclass Array)");
        CivitasAkademika[] daftarWargaKampus = { mhs1, dsnWali, mhs2, dsnPengampu };

        int noCiv = 1;
        for (CivitasAkademika warga : daftarWargaKampus) {
            System.out.println("Elemen ke-[" + (noCiv++) + "] | Tipe Objek: " + warga.getClass().getSimpleName());
            warga.tampilkanPeran();
            System.out.println();
        }

        // 4.2 Koleksi Polimorfik Interface DapatDiotentikasi[]
        System.out.println(">>> 4.2 KOLEKSI POLIMORFIK BERTIPE DapatDiotentikasi[] (Interface Array)");
        DapatDiotentikasi[] penggunaPortal = { mhs1, dsnWali, mhs2, dsnPengampu };

        int noAuth = 1;
        for (DapatDiotentikasi user : penggunaPortal) {
            System.out.println("Portal User [" + (noAuth++) + "]:");
            user.cetakInfoSesi();
        }
        System.out.println();

        // =====================================================================
        // SKENARIO TAMBAHAN: INTEGRASI RELASI P4 & FITUR BISNIS P5-P6
        // =====================================================================
        System.out.println("##########################################################################");
        System.out.println(" [SKENARIO INTEGRASI: PEMBUKTIAN FITUR LAMA TIDAK MENGALAMI REGRESI]    ");
        System.out.println("##########################################################################");
        System.out.println("Deskripsi: Menjalankan aliran bisnis registrasi mata kuliah (Overloading),");
        System.out.println("           pengesahan KRS oleh Dosen Wali (Asosiasi), dan pencetakan KRS.\n");

        // Objek MataKuliah (Asosiasi ke Dosen pengampu)
        MataKuliah mk1 = new MataKuliah("IF201", "Pemrograman Berorientasi Obyek", 3, 3, dsnWali, 30);
        MataKuliah mk2 = new MataKuliah("IF202", "Praktikum PBO", 2, 3, dsnWali, 30);
        MataKuliah mk3 = new MataKuliah("IF203", "Basis Data Lanjut", 3, 3, dsnPengampu, 30);
        MataKuliah mk4 = new MataKuliah("IF204", "Rekayasa Perangkat Lunak", 3, 3, dsnWali, 30);

        // Mahasiswa mendaftar mata kuliah via method overloading
        mhs1.pilihMataKuliah(mk1);                                      // Overload 1 (Reguler)
        mhs1.pilihMataKuliah(mk2, "Praktikum Wajib Kurikulum");          // Overload 2 (Kategori Khusus)
        mhs1.pilihMataKuliah(mk3, "Mata Kuliah Pilihan Prodi");          // Overload 2
        mhs1.pilihMataKuliah(mk4, "Program Akselerasi Kompetensi");      // Overload 2

        // Pengajuan persetujuan ke Dosen Wali
        mhs1.ajukanPersetujuanKrs();

        // Cetak dokumen resmi KRS
        System.out.println("\nDokumen Rencana Studi Terverifikasi:");
        mhs1.getKrs().tampilkanKrs();

        // Logout akun portal
        System.out.println("\nPengujian Keamanan Sesi:");
        authUserMhs.logout();
        authUserDsn.logout();

        System.out.println("\n==========================================================================");
        System.out.println("  SEMUA PENGUJIAN MODUL 7 (ABSTRACT CLASS & INTERFACE) SUKSES 100%!       ");
        System.out.println("==========================================================================");
    }
}
