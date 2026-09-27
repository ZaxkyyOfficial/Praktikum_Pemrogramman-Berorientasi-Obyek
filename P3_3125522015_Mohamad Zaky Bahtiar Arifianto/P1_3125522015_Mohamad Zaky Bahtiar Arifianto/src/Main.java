public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  PROJECT KICKOFF - SISTEM INFORMASI AKADEMIK");
        System.out.println("     Praktikum Pemrograman Berorientasi Obyek     ");
        System.out.println("==================================================");
        System.out.println("Pengembang : Mohamad Zaky Bahtiar Arifianto");
        System.out.println("NRP        : 3125522015\n");

        // Instansiasi Object dari Class Mahasiswa
        Mahasiswa mhs1 = new Mahasiswa();

        // Mengisi State / Atribut Data Objek
        mhs1.nrp = "3125522015";
        mhs1.nama = "Mohamad Zaky Bahtiar Arifianto";
        mhs1.prodi = "D3 Teknik Informatika";
        mhs1.semester = 3;
        mhs1.ipk = 3.75;
        mhs1.totalSks = 0;

        // Memanggil Behavior: Menampilkan Data Mahasiswa
        mhs1.tampilkanData();

        // Simulasi Behavior: Pengambilan Mata Kuliah (KRS)
        mhs1.ambilMataKuliah("Pemrograman Berorientasi Obyek", 3);
        mhs1.ambilMataKuliah("Praktikum PBO", 2);
        mhs1.ambilMataKuliah("Basis Data Lanjut", 3);
        mhs1.ambilMataKuliah("Rekayasa Perangkat Lunak", 3);

        // Menampilkan Rekap Akhir Data Mahasiswa
        System.out.println("\n[REKAP AKHIR]");
        mhs1.tampilkanData();
    }
}
