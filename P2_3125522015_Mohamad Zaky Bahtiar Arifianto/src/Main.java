/**
 * Class Main merupakan titik masuk utama (entry point) pengujian program Praktikum PBO Modul 2.
 * Menguji implementasi 3 Class (Mahasiswa, MataKuliah, KRS), instansiasi multi-object,
 * eksekusi method (tanpa parameter, dengan parameter, dan return value), serta manipulasi state.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 2
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("     PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK (PBO) - MODUL 2             ");
        System.out.println("   Implementasi Class, Object, Attribute, Method, dan Constructor         ");
        System.out.println("==========================================================================");
        System.out.println("Pengembang   : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP          : 3125522015");
        System.out.println("Program Studi: D3 Teknik Informatika");
        System.out.println("Institusi    : PENS PSDKU Sumenep\n");

        // ======================================================================
        // TAHAP 1: INSTANSIASI OBJECT MELALUI CONSTRUCTOR (BAGIAN B & C)
        // ======================================================================
        System.out.println(">>> 1. INSTANSIASI MULTI-OBJECT MENGGUNAKAN CONSTRUCTOR");
        System.out.println("--------------------------------------------------------------------------");

        // Instansiasi minimal 2 Objek dari Class Mahasiswa
        Mahasiswa mhs1 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", "D3 Teknik Informatika", 3, 3.82);
        Mahasiswa mhs2 = new Mahasiswa("3125522022", "Ahmad Wildan Prasetyo", "D3 Teknik Informatika", 3, 2.45);

        // Instansiasi beberapa Objek dari Class MataKuliah (dengan variasi kuota)
        MataKuliah mk1 = new MataKuliah("IF201", "Pemrograman Berorientasi Obyek", 3, 3, "Nirwana Haidar Hari, S.Pd., M.Kom.", 30);
        MataKuliah mk2 = new MataKuliah("IF202", "Praktikum PBO", 2, 3, "Nirwana Haidar Hari, S.Pd., M.Kom.", 30);
        MataKuliah mk3 = new MataKuliah("IF203", "Basis Data Lanjut", 3, 3, "Firman Arifin, S.T., M.T.", 2); // Kuota kecil untuk uji batas
        MataKuliah mk4 = new MataKuliah("IF204", "Rekayasa Perangkat Lunak", 3, 3, "Budi Raharjo, S.Kom., M.Kom.", 25);
        MataKuliah mk5 = new MataKuliah("IF205", "Jaringan Komputer", 3, 3, "Hendra Kusuma, S.Kom., M.T.", 30);

        // Instansiasi minimal 2 Objek dari Class KRS
        KRS krs1 = new KRS("KRS-2026-001", mhs1, "2026/2027 Ganjil", 3, 8);
        KRS krs2 = new KRS("KRS-2026-002", mhs2, "2026/2027 Ganjil", 3, 8);

        System.out.println("[STATUS] Objek mhs1, mhs2, mk1-mk5, dan krs1-krs2 berhasil dibuat di memori.");
        System.out.println();

        // ======================================================================
        // TAHAP 2: EKSEKUSI METHOD TANPA PARAMETER (BAGIAN D)
        // ======================================================================
        System.out.println(">>> 2. MENJALANKAN METHOD TANPA PARAMETER (MENAMPILKAN DATA AWAL)");
        System.out.println("--------------------------------------------------------------------------");
        mhs1.tampilkanProfil();
        mhs2.tampilkanProfil();

        System.out.println("\n[Informasi Detail Sebagian Mata Kuliah Dibuka]:");
        mk1.tampilkanDetailMk();
        mk3.tampilkanDetailMk();
        System.out.println();

        // ======================================================================
        // TAHAP 3: EKSEKUSI METHOD DENGAN PARAMETER & PERUBAHAN DATA STATE
        // ======================================================================
        System.out.println(">>> 3. MENJALANKAN METHOD DENGAN PARAMETER (PERUBAHAN DATA STATE)");
        System.out.println("--------------------------------------------------------------------------");
        // Update IPK mahasiswa
        mhs1.updateIpk(3.90);
        
        // Pergantian dosen pengampu mata kuliah
        mk3.ubahDosenPengampu("Dr. Indah Susilowati, S.T., M.T.");
        System.out.println();

        // ======================================================================
        // TAHAP 4: PENGUJIAN OPERASI & INTERAKSI ANTAR-OBJEK (PENGAMBILAN KRS)
        // ======================================================================
        System.out.println(">>> 4. PENGUJIAN TRANSAKSI PENGAMBILAN KRS & VALIDASI LOGIKA BISNIS");
        System.out.println("--------------------------------------------------------------------------");
        
        // Pengambilan KRS untuk mhs1 (Batas SKS = 24)
        System.out.println("[KASUS A] Pengisian KRS Mahasiswa 1 (" + mhs1.nama + "):");
        krs1.tambahMataKuliah(mk1); // 3 SKS
        krs1.tambahMataKuliah(mk2); // 2 SKS
        krs1.tambahMataKuliah(mk3); // 3 SKS (peserta ke-1 dari kuota 2)
        krs1.tambahMataKuliah(mk4); // 3 SKS
        krs1.tambahMataKuliah(mk5); // 3 SKS
        System.out.println();

        // Pengambilan KRS untuk mhs2 (IPK 2.45, Batas SKS = 18)
        System.out.println("[KASUS B] Pengisian KRS Mahasiswa 2 (" + mhs2.nama + "):");
        krs2.tambahMataKuliah(mk1); // 3 SKS
        krs2.tambahMataKuliah(mk2); // 2 SKS
        krs2.tambahMataKuliah(mk3); // 3 SKS (peserta ke-2 dari kuota 2 -> kuota MK3 kini penuh!)
        
        // Simulasi percobaan jika ada yang mendaftar lagi ke MK3 yang kuotanya sudah habis
        System.out.println("\n[UJI KASUS KHUSUS 1: Kelas Penuh]");
        System.out.println("Mata kuliah " + mk3.namaMk + " kuota: " + mk3.kuotaKelas + ", terdaftar: " + mk3.pesertaTerdaftar);
        boolean statusDaftarPenuh = mk3.tambahPeserta();
        System.out.println("Hasil penambahan peserta langsung pada kelas penuh: " + 
                           (statusDaftarPenuh ? "Sukses" : "Ditolak (Kelas Telah Penuh)"));
        System.out.println();

        // ======================================================================
        // TAHAP 5: PENGUJIAN METHOD DENGAN RETURN VALUE
        // ======================================================================
        System.out.println(">>> 5. PENGUJIAN METHOD YANG MENGEMBALIKAN NILAI (RETURN VALUE)");
        System.out.println("--------------------------------------------------------------------------");
        int totalSksMhs1 = krs1.hitungTotalSksKrs();
        int batasSksMhs1 = mhs1.hitungBebanMaksimalSks();
        int sisaKursiMk3 = mk3.getSisaKuota();
        boolean cekPenuhMk3 = mk3.isKelasPenuh();
        boolean statusAccKrs1 = krs1.isDisetujui();

        System.out.println("Total SKS KRS Mahasiswa 1 (hitungTotalSksKrs)    : " + totalSksMhs1 + " SKS");
        System.out.println("Batas Maksimal SKS Mahasiswa 1 (hitungBebanMaks) : " + batasSksMhs1 + " SKS");
        System.out.println("Sisa Kuota Kursi MK Basis Data Lanjut (getSisa)  : " + sisaKursiMk3 + " Kursi");
        System.out.println("Apakah Kelas Basis Data Lanjut Penuh (isPenuh)   : " + cekPenuhMk3);
        System.out.println("Status Pengesahan KRS 1 Saat Ini (isDisetujui)   : " + statusAccKrs1 + " (Belum disahkan)");
        System.out.println();

        // ======================================================================
        // TAHAP 6: PENGESAHAN DOKUMEN OLEH DOSEN WALI (VALIDASI KRS)
        // ======================================================================
        System.out.println(">>> 6. PROSES PENGESAHAN OLEH DOSEN WALI");
        System.out.println("--------------------------------------------------------------------------");
        krs1.setujuiKrs("Nirwana Haidar Hari, S.Pd., M.Kom.");
        krs2.setujuiKrs("Nirwana Haidar Hari, S.Pd., M.Kom.");
        System.out.println("Status Verifikasi KRS 1 Pasca Validasi: " + (krs1.isDisetujui() ? "VALID / RESMI" : "PENDING"));
        System.out.println();

        // ======================================================================
        // TAHAP 7: MENAMPILKAN OUTPUT HASIL AKHIR PROGRAM
        // ======================================================================
        System.out.println(">>> 7. LEMBAR CETAK HASIL AKHIR KARTU RENCANA STUDI (KRS)");
        System.out.println("--------------------------------------------------------------------------");
        krs1.tampilkanKrs();
        krs2.tampilkanKrs();

        System.out.println("[REKAP AKHIR PROFIL MAHASISWA PASCA PENGISIAN KRS]");
        mhs1.tampilkanProfil();
        mhs2.tampilkanProfil();

        System.out.println("==========================================================================");
        System.out.println("  SEMUA PENGUJIAN MODUL 2 BERHASIL DISELESAIKAN DENGAN SEMPURNA (DONE)    ");
        System.out.println("==========================================================================");
    }
}
