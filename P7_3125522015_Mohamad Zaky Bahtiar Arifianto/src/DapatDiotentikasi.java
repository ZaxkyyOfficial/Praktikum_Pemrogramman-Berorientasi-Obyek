/**
 * Interface DapatDiotentikasi mendefinisikan kontrak perilaku otentikasi akun dan manajemen hak akses
 * bagi entitas pengguna yang berinteraksi dengan portal Sistem Informasi Akademik (SIAKAD).
 * 
 * Kontrak ini menjamin bahwa setiap class yang mengimplementasikannya (Mahasiswa dan Dosen)
 * memiliki mekanisme standar untuk masuk ke portal, mengakhiri sesi, dan mengecek otoritas akses.
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 7 (Abstract Class & Interface)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public interface DapatDiotentikasi {
    /**
     * Memvalidasi kredensial login pengguna portal SIAKAD.
     * @param nomorInduk nomor identitas resmi (NRP untuk mahasiswa, NIP untuk dosen)
     * @param kataSandi kata sandi akun
     * @return true jika autentikasi berhasil, false jika ditolak
     */
    boolean login(String nomorInduk, String kataSandi);

    /**
     * Mengakhiri sesi aktif pengguna di portal SIAKAD.
     */
    void logout();

    /**
     * Menampilkan daftar hak akses operasional dan kewenangan spesifik pengguna.
     */
    void tampilkanHakAkses();

    /**
     * Memeriksa status keaktifan sesi pengguna saat ini.
     * @return true jika pengguna sedang aktif dalam sesi login
     */
    boolean isLoginAktif();

    /**
     * Default method (fitur modern Java Interface) untuk mencetak ringkasan status sesi portal.
     */
    default void cetakInfoSesi() {
        System.out.println("[STATUS SESI PORTAL] " + (isLoginAktif() ? "AKTIF (Terotentikasi)" : "TIDAK AKTIF (Silakan login terlebih dahulu)"));
    }
}
