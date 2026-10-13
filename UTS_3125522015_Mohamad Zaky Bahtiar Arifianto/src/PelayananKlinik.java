public interface PelayananKlinik {

    /**
     * Melakukan tindakan pelayanan medis terhadap pasien tertentu.
     * Perilaku (behavior) akan bervariasi secara polimorfik tergantung apakah
     * objek konkret adalah Dokter (konsultasi & diagnosis) atau Perawat (asuhan & tindakan).
     * 
     * @param namaPasien Nama pasien yang sedang dilayani
     */
    void melayaniPasien(String namaPasien);

    /**
     * Menghitung total biaya pelayanan berdasarkan biaya dasar tindakan
     * ditambah komponen tarif spesifik profesi (misal tarif konsultasi dokter atau insentif tindakan perawat).
     * 
     * @param biayaDasar Biaya dasar tindakan medis/klinik
     * @return Total biaya layanan setelah kalkulasi profesi
     */
    double hitungBiayaLayanan(double biayaDasar);
}
