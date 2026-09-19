public class Mahasiswa {
    // State / Data (Atribut)
    public String nrp;
    public String nama;
    public String prodi;
    public int semester;
    public double ipk;
    public int totalSks;

    // Behavior 1: Menampilkan data lengkap mahasiswa
    public void tampilkanData() {
        System.out.println("--------------------------------------------------");
        System.out.println("            DATA AKADEMIK MAHASISWA               ");
        System.out.println("--------------------------------------------------");
        System.out.println("NRP              : " + nrp);
        System.out.println("Nama Mahasiswa   : " + nama);
        System.out.println("Program Studi    : " + prodi);
        System.out.println("Semester         : " + semester);
        System.out.println("IPK Terakhir     : " + String.format("%.2f", ipk));
        System.out.println("Batas Maks. SKS  : " + hitungBebanMaksimalSks() + " SKS");
        System.out.println("Total SKS Diambil: " + totalSks + " SKS");
        System.out.println("--------------------------------------------------");
    }

    // Behavior 2: Menghitung batas beban SKS berdasarkan IPK
    public int hitungBebanMaksimalSks() {
        if (ipk >= 3.00) {
            return 24;
        } else if (ipk >= 2.50) {
            return 21;
        } else if (ipk >= 2.00) {
            return 18;
        } else {
            return 15;
        }
    }

    // Behavior 3: Memproses pengambilan mata kuliah (KRS)
    public void ambilMataKuliah(String namaMk, int sks) {
        int batasMaks = hitungBebanMaksimalSks();
        System.out.println("\n>> Proses Pengambilan Mata Kuliah: " + namaMk + " (" + sks + " SKS)");
        if (totalSks + sks <= batasMaks) {
            totalSks += sks;
            System.out.println("   [STATUS: BERHASIL] Mata kuliah berhasil ditambahkan ke KRS.");
            System.out.println("   Total SKS saat ini: " + totalSks + " / " + batasMaks + " SKS");
        } else {
            System.out.println("   [STATUS: GAGAL] Melebihi batas maksimal SKS (" + batasMaks + " SKS)!");
        }
    }
}
