package service;

import model.Dosen;
import model.KRS;
import model.Mahasiswa;
import model.MataKuliah;

import java.util.ArrayList;
import java.util.List;

/**
 * Automated Test Runner untuk Verifikasi Logika Bisnis PBO SIAKAD
 * Menguji perilaku enkapsulasi, validasi state machine, dan relasi antarobjek.
 */
public class PengujianSIAKAD {

    public static class HasilUji {
        public final int no;
        public final String namaUji;
        public final String ekspektasi;
        public final boolean passed;
        public final String detail;

        public HasilUji(int no, String namaUji, String ekspektasi, boolean passed, String detail) {
            this.no = no;
            this.namaUji = namaUji;
            this.ekspektasi = ekspektasi;
            this.passed = passed;
            this.detail = detail;
        }
    }

    public static void main(String[] args) {
        jalankanPengujian();
    }

    public static boolean jalankanPengujian() {
        System.out.println("==========================================================================================");
        System.out.println("            SUITE PENGUJIAN OTOMATIS LOGIKA BISNIS PBO - SISTEM SIAKAD                    ");
        System.out.println("       Mata Kuliah : Praktikum Pemrograman Berorientasi Obyek (Modul 9)                  ");
        System.out.println("       Penguji     : Mohamad Zaky Bahtiar Arifianto (NIM: 3125522015)                     ");
        System.out.println("==========================================================================================");

        List<HasilUji> hasilList = new ArrayList<>();
        int no = 1;

        // ---------------------------------------------------------------------
        // TEST CASE 1: Uji Pengambilan MK & Pengurangan Sisa Kuota Kelas
        // ---------------------------------------------------------------------
        try {
            Mahasiswa m1 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", 3, 24, "AKTIF");
            Dosen d1 = new Dosen("198203152008121002", "Dr. Eng. Ahmad Fauzi, M.Kom.", 25, "RPL");
            KRS krs1 = new KRS("KRS-TEST-001", m1, d1);
            m1.setKrsAktif(krs1);

            MataKuliah mkPBO = new MataKuliah("IF301", "Pemrograman Berorientasi Obyek", 3, 30, 3);
            int sisaAwal = mkPBO.getSisaKuota();

            m1.ambilMataKuliah(mkPBO);

            boolean terdaftar = krs1.getMataKuliahList().contains(mkPBO);
            boolean kuotaBerkurang = (mkPBO.getSisaKuota() == sisaAwal - 1) && (mkPBO.getTerisi() == 1);
            boolean sksBertambah = (krs1.hitungTotalSKS() == 3);

            boolean pass = terdaftar && kuotaBerkurang && sksBertambah;
            hasilList.add(new HasilUji(no++,
                    "Pengambilan MK & Alokasi Kuota",
                    "Sisa kuota berkurang 1, total SKS bertambah 3",
                    pass,
                    String.format("Sisa: %d->%d, Terisi: %d, SKS: %d", sisaAwal, mkPBO.getSisaKuota(), mkPBO.getTerisi(), krs1.hitungTotalSKS())));
        } catch (Exception e) {
            hasilList.add(new HasilUji(no++, "Pengambilan MK & Alokasi Kuota", "Berhasil mengambil MK", false, "Eksepsi: " + e.getMessage()));
        }

        // ---------------------------------------------------------------------
        // TEST CASE 2: Uji Batas Beban SKS (Penolakan Beban Melebihi Maksimum)
        // ---------------------------------------------------------------------
        try {
            // Mahasiswa semester bawah dengan batas beban 6 SKS
            Mahasiswa m2 = new Mahasiswa("3125522099", "Mahasiswa Uji SKS", 1, 6, "AKTIF");
            Dosen d1 = new Dosen("198203152008121002", "Dr. Eng. Ahmad Fauzi, M.Kom.", 25, "RPL");
            KRS krs2 = new KRS("KRS-TEST-002", m2, d1);
            m2.setKrsAktif(krs2);

            MataKuliah mk1 = new MataKuliah("IF101", "Dasar Pemrograman", 4, 30, 1);
            MataKuliah mk2 = new MataKuliah("IF102", "Matematika Diskrit", 3, 30, 1);

            m2.ambilMataKuliah(mk1); // 4 SKS (Lolos)
            boolean exceptionThrown = false;
            String errorMsg = "";

            try {
                m2.ambilMataKuliah(mk2); // 4 + 3 = 7 SKS > 6 SKS (Harus Ditolak!)
            } catch (IllegalStateException ex) {
                exceptionThrown = true;
                errorMsg = ex.getMessage();
            }

            boolean pass = exceptionThrown && (krs2.hitungTotalSKS() == 4) && (mk2.getTerisi() == 0);
            hasilList.add(new HasilUji(no++,
                    "Validasi Batas Beban SKS",
                    "Melempar IllegalStateException saat beban > maxSKS (6 SKS)",
                    pass,
                    "Pesan ditangkap: \"" + errorMsg + "\" (Total SKS tetap 4 SKS)"));
        } catch (Exception e) {
            hasilList.add(new HasilUji(no++, "Validasi Batas Beban SKS", "Penolakan overload SKS", false, "Eksepsi: " + e.getMessage()));
        }

        // ---------------------------------------------------------------------
        // TEST CASE 3: Uji Mutasi State Machine KRS (DRAFT -> DIAJUKAN -> DISETUJUI)
        // ---------------------------------------------------------------------
        try {
            Mahasiswa m3 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", 3, 24, "AKTIF");
            Dosen dPA = new Dosen("198203152008121002", "Dr. Eng. Ahmad Fauzi, M.Kom.", 25, "RPL");
            Dosen dLain = new Dosen("197906212005011003", "Ir. Nurul Hidayati, M.T.", 20, "AI");
            KRS krs3 = new KRS("KRS-TEST-003", m3, dPA);
            m3.setKrsAktif(krs3);

            MataKuliah mk = new MataKuliah("IF303", "Struktur Data", 3, 25, 3);
            m3.ambilMataKuliah(mk);

            boolean draftOk = (krs3.getStatus() == KRS.StatusKRS.DRAFT);

            // Transisi 1: Mahasiswa mengajukan KRS
            krs3.ajukanKRS();
            boolean diajukanOk = (krs3.getStatus() == KRS.StatusKRS.DIAJUKAN);

            // Mutasi dilarang saat status DIAJUKAN
            boolean editDitolakSaatDiajukan = false;
            try {
                krs3.tambahItem(new MataKuliah("IF304", "Praktikum", 1, 20, 3));
            } catch (IllegalStateException ex) {
                editDitolakSaatDiajukan = true;
            }

            // Dosen lain tidak berwenang menyetujui
            boolean unauthorizedDitolak = false;
            try {
                dLain.verifikasiKRS(krs3);
            } catch (IllegalStateException ex) {
                unauthorizedDitolak = true;
            }

            // Dosen PA resmi menyetujui
            dPA.verifikasiKRS(krs3);
            boolean disetujuiOk = (krs3.getStatus() == KRS.StatusKRS.DISETUJUI);

            boolean pass = draftOk && diajukanOk && editDitolakSaatDiajukan && unauthorizedDitolak && disetujuiOk;
            hasilList.add(new HasilUji(no++,
                    "State Machine Alur Persetujuan KRS",
                    "DRAFT -> DIAJUKAN -> DISETUJUI (Hanya Dosen PA Sah)",
                    pass,
                    String.format("DRAFT:%b, DIAJUKAN:%b, LockEdit:%b, PA-Auth:%b, DISETUJUI:%b",
                            draftOk, diajukanOk, editDitolakSaatDiajukan, unauthorizedDitolak, disetujuiOk)));
        } catch (Exception e) {
            hasilList.add(new HasilUji(no++, "State Machine Alur Persetujuan KRS", "Transisi status valid", false, "Eksepsi: " + e.getMessage()));
        }

        // ---------------------------------------------------------------------
        // TEST CASE 4: Uji Pembatalan MK & Pengembalian Kuota
        // ---------------------------------------------------------------------
        try {
            Mahasiswa m4 = new Mahasiswa("3125522015", "Mohamad Zaky Bahtiar Arifianto", 3, 24, "AKTIF");
            Dosen d1 = new Dosen("198203152008121002", "Dr. Eng. Ahmad Fauzi, M.Kom.", 25, "RPL");
            KRS krs4 = new KRS("KRS-TEST-004", m4, d1);
            m4.setKrsAktif(krs4);

            MataKuliah mk = new MataKuliah("IF305", "Basis Data Lanjut", 3, 20, 3);
            m4.ambilMataKuliah(mk);
            int sisaSetelahAmbil = mk.getSisaKuota(); // 19

            m4.batalkanMataKuliah("IF305");
            int sisaSetelahBatal = mk.getSisaKuota(); // 20

            boolean tidakAdaDiKrs = !krs4.getMataKuliahList().contains(mk);
            boolean kuotaKembali = (sisaSetelahBatal == sisaSetelahAmbil + 1) && (mk.getTerisi() == 0);
            boolean sksNol = (krs4.hitungTotalSKS() == 0);

            boolean pass = tidakAdaDiKrs && kuotaKembali && sksNol;
            hasilList.add(new HasilUji(no++,
                    "Pembatalan MK & Restorasi Kuota",
                    "Item terhapus dari KRS, kuota kembali utuh, total SKS berkurang",
                    pass,
                    String.format("Sisa: %d->%d, Terisi: %d, Total SKS: %d", sisaSetelahAmbil, sisaSetelahBatal, mk.getTerisi(), krs4.hitungTotalSKS())));
        } catch (Exception e) {
            hasilList.add(new HasilUji(no++, "Pembatalan MK & Restorasi Kuota", "Kuota kembali utuh", false, "Eksepsi: " + e.getMessage()));
        }

        // ---------------------------------------------------------------------
        // TEST CASE 5: Uji Penolakan Saat Kuota Kelas Penuh (Edge Case)
        // ---------------------------------------------------------------------
        try {
            Mahasiswa mA = new Mahasiswa("3125522011", "Mahasiswa A", 3, 24, "AKTIF");
            Mahasiswa mB = new Mahasiswa("3125522012", "Mahasiswa B", 3, 24, "AKTIF");
            Dosen d1 = new Dosen("198203152008121002", "Dr. Eng. Ahmad Fauzi, M.Kom.", 25, "RPL");

            KRS krsA = new KRS("KRS-A", mA, d1);
            KRS krsB = new KRS("KRS-B", mB, d1);
            mA.setKrsAktif(krsA);
            mB.setKrsAktif(krsB);

            MataKuliah mkSatuKursi = new MataKuliah("IF399", "Seminar Riset Khusus", 2, 1, 3);
            mA.ambilMataKuliah(mkSatuKursi); // Kursi terisi penuh (1/1)

            boolean kuotaPenuhDitolak = false;
            String msg = "";
            try {
                mB.ambilMataKuliah(mkSatuKursi); // Kursi habis (Harus Ditolak!)
            } catch (IllegalStateException ex) {
                kuotaPenuhDitolak = true;
                msg = ex.getMessage();
            }

            boolean pass = kuotaPenuhDitolak && (mkSatuKursi.getTerisi() == 1);
            hasilList.add(new HasilUji(no++,
                    "Validasi Kuota Kelas Penuh",
                    "Melempar IllegalStateException saat kuota kelas habis",
                    pass,
                    "Pesan ditangkap: \"" + msg + "\" (Terisi tetap 1/1)"));
        } catch (Exception e) {
            hasilList.add(new HasilUji(no++, "Validasi Kuota Kelas Penuh", "Penolakan over-enrollment", false, "Eksepsi: " + e.getMessage()));
        }

        // ---------------------------------------------------------------------
        // CETAK TABEL LAPORAN HASIL PENGUJIAN
        // ---------------------------------------------------------------------
        System.out.printf("%-4s | %-32s | %-50s | %-8s%n", "NO", "NAMA PENGUJIAN", "DETAIL / DIAGNOSTIK", "HASIL");
        System.out.println("-----+----------------------------------+----------------------------------------------------+---------");

        boolean allPassed = true;
        for (HasilUji h : hasilList) {
            String badge = h.passed ? "[PASS]" : "[FAIL]";
            if (!h.passed) allPassed = false;
            System.out.printf("%-4d | %-32s | %-50s | %-8s%n", h.no, h.namaUji, potongString(h.detail, 50), badge);
        }
        System.out.println("==========================================================================================");

        int totalPass = (int) hasilList.stream().filter(h -> h.passed).count();
        System.out.printf("RINGKASAN: %d dari %d pengujian BERHASIL (%s)%n%n",
                totalPass, hasilList.size(), allPassed ? "SEMUA LOLOS - VERIFIKASI PARIPURNA" : "ADA KEGAGALAN");

        return allPassed;
    }

    private static String potongString(String str, int maxLen) {
        if (str == null) return "-";
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen - 3) + "...";
    }
}
