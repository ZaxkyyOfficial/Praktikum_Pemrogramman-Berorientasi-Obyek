/**
 * Class Main merupakan program pengujian utama Praktikum PBO Modul 3.
 * Menguji implementasi Encapsulation, Access Modifier private, Getter-Setter,
 * serta verifikasi ketat melalui dua skenario utama:
 * 1. TEST VALID: Instansiasi objek dan modifikasi data yang sah via setter.
 * 2. TEST INVALID: Uji ketahanan validasi data saat diberi input salah/ilegal.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 3
 * Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("     PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK (PBO) - MODUL 3             ");
        System.out.println("   Encapsulation, Access Modifier, Getter-Setter, dan Validasi Data       ");
        System.out.println("==========================================================================");
        System.out.println("Pengembang   : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP          : 3125522015");
        System.out.println("Program Studi: D3 Teknik Informatika");
        System.out.println("Institusi    : PENS PSDKU Sumenep\n");

        // ======================================================================
        // BAGIAN 1: PENGUJIAN DATA VALID (TEST VALID)
        // ======================================================================
        System.out.println("##########################################################################");
        System.out.println("                 [SKENARIO 1: PENGUJIAN DATA VALID (TEST VALID)]          ");
        System.out.println("##########################################################################");
        
        System.out.println("\n>>> 1.1 INSTANSIASI OBJEK DENGAN CONSTRUCTOR TERVALIDASI");
        Mahasiswa mhs1 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", "D3 Teknik Informatika", 3, 3.82);
        Mahasiswa mhs2 = new Mahasiswa("3125522022", "Ahmad Wildan Prasetyo", "D3 Teknik Informatika", 3, 2.45);

        MataKuliah mk1 = new MataKuliah("IF201", "Pemrograman Berorientasi Obyek", 3, 3, "Nirwana Haidar Hari, S.Pd., M.Kom.", 30);
        MataKuliah mk2 = new MataKuliah("IF202", "Praktikum PBO", 2, 3, "Nirwana Haidar Hari, S.Pd., M.Kom.", 30);
        MataKuliah mk3 = new MataKuliah("IF203", "Basis Data Lanjut", 3, 3, "Firman Arifin, S.T., M.T.", 2);
        MataKuliah mk4 = new MataKuliah("IF204", "Rekayasa Perangkat Lunak", 3, 3, "Budi Raharjo, S.Kom., M.Kom.", 25);
        MataKuliah mk5 = new MataKuliah("IF205", "Jaringan Komputer", 3, 3, "Hendra Kusuma, S.Kom., M.T.", 30);

        KRS krs1 = new KRS("KRS-2026-001", mhs1, "2026/2027 Ganjil", 3, 8);
        KRS krs2 = new KRS("KRS-2026-002", mhs2, "2026/2027 Ganjil", 3, 8);
        System.out.println("[STATUS TEST VALID] Seluruh objek berhasil dibuat melalui constructor.");

        System.out.println("\n>>> 1.2 MEMBACA DATA MENGGUNAKAN GETTER (ACCESSOR)");
        System.out.println("Mhs1 - NRP (getNrp)      : " + mhs1.getNrp());
        System.out.println("Mhs1 - Nama (getNama)    : " + mhs1.getNama());
        System.out.printf("Mhs1 - IPK (getIpk)      : %.2f\n", mhs1.getIpk());
        System.out.println("Mhs1 - Kuota SKS (hitung): " + mhs1.hitungBebanMaksimalSks() + " SKS");
        System.out.println("MK1  - Nama MK (getNama) : " + mk1.getNamaMk() + " (" + mk1.getSks() + " SKS)");
        System.out.println("MK1  - Dosen (getDosen)  : " + mk1.getDosenPengampu());

        System.out.println("\n>>> 1.3 MENGUBAH DATA MENGGUNAKAN SETTER VALID (MUTATOR)");
        mhs1.setIpk(3.92);
        System.out.printf("[HASIL SETTER VALID] IPK mhs1 setelah setIpk(3.92): %.2f\n", mhs1.getIpk());
        mk3.setDosenPengampu("Dr. Indah Susilowati, S.T., M.T.");
        System.out.println("[HASIL SETTER VALID] Dosen MK3 setelah setDosenPengampu: " + mk3.getDosenPengampu());

        System.out.println("\n>>> 1.4 TRANSAKSI PENGISIAN KRS & PENGESAHAN OLEH DOSEN WALI");
        krs1.tambahMataKuliah(mk1);
        krs1.tambahMataKuliah(mk2);
        krs1.tambahMataKuliah(mk3);
        krs1.tambahMataKuliah(mk4);
        krs1.tambahMataKuliah(mk5);
        krs1.setujuiKrs("Nirwana Haidar Hari, S.Pd., M.Kom.");
        System.out.println("Status Verifikasi KRS 1 (isDisetujui): " + (krs1.isDisetujui() ? "VALID" : "PENDING"));

        // ======================================================================
        // BAGIAN 2: PENGUJIAN DATA INVALID (TEST INVALID - ATURAN VALIDASI)
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("               [SKENARIO 2: PENGUJIAN DATA INVALID (TEST INVALID)]        ");
        System.out.println("##########################################################################");

        System.out.println("\n>>> 2.1 PENGUJIAN NILAI IPK INVALID (TIDAK BOLEH < 0.00 ATAU > 4.00)");
        System.out.println("[UJI COBA 1] Memasukkan IPK Negatif: mhs1.setIpk(-1.50)");
        mhs1.setIpk(-1.50);
        System.out.printf("--> Nilai IPK mhs1 saat ini (harus tetap utuh): %.2f\n", mhs1.getIpk());

        System.out.println("\n[UJI COBA 2] Memasukkan IPK Melebihi Batas: mhs1.setIpk(4.85)");
        mhs1.setIpk(4.85);
        System.out.printf("--> Nilai IPK mhs1 saat ini (harus tetap utuh): %.2f\n", mhs1.getIpk());

        System.out.println("\n>>> 2.2 PENGUJIAN SEMESTER INVALID (TIDAK BOLEH < 1 ATAU > 14)");
        System.out.println("[UJI COBA 3] Memasukkan Semester 0: mhs2.setSemester(0)");
        mhs2.setSemester(0);
        System.out.println("--> Nilai Semester mhs2 saat ini: " + mhs2.getSemester());

        System.out.println("\n[UJI COBA 4] Memasukkan Semester 15: mhs2.setSemester(15)");
        mhs2.setSemester(15);
        System.out.println("--> Nilai Semester mhs2 saat ini: " + mhs2.getSemester());

        System.out.println("\n>>> 2.3 PENGUJIAN NAMA MAHASISWA INVALID (KOSONG / WHITESPACE)");
        System.out.println("[UJI COBA 5] Memasukkan Nama Kosong: mhs1.setNama(\"   \")");
        mhs1.setNama("   ");
        System.out.println("--> Nilai Nama mhs1 saat ini: " + mhs1.getNama());

        System.out.println("\n>>> 2.4 PENGUJIAN KUOTA KELAS INVALID");
        System.out.println("[UJI COBA 6] Memasukkan Kuota Kelas Negatif: mk3.setKuotaKelas(-10)");
        mk3.setKuotaKelas(-10);
        System.out.println("--> Nilai Kuota mk3 saat ini: " + mk3.getKuotaKelas());

        System.out.println("\n[UJI COBA 7] Memasukkan Kuota Lebih Kecil dari Peserta Terdaftar:");
        System.out.println("Peserta terdaftar di " + mk3.getNamaMk() + " saat ini: " + mk3.getPesertaTerdaftar());
        System.out.println("Mencoba ubah kuota menjadi 0: mk3.setKuotaKelas(0)");
        mk3.setKuotaKelas(0);
        System.out.println("--> Nilai Kuota mk3 saat ini: " + mk3.getKuotaKelas());

        // ======================================================================
        // BAGIAN 3: VERIFIKASI AKHIR STATUS DATA PASCA PENGUJIAN
        // ======================================================================
        System.out.println("\n##########################################################################");
        System.out.println("                     LEMBAR CETAK HASIL AKHIR KRS                         ");
        System.out.println("##########################################################################\n");
        krs1.tampilkanKrs();

        System.out.println("==========================================================================");
        System.out.println("  SEMUA PENGUJIAN VALIDASI ENKAPSULASI P3 BERHASIL DILALUI DENGAN SUKSES  ");
        System.out.println("==========================================================================");
    }
}
