import java.text.NumberFormat;
import java.util.Locale;

public class TransaksiPembayaran {

    private String idTransaksi;
    private String idRekamMedis;
    private double biayaLayanan;
    private double diskonBpjs;
    private double totalBayar;
    private String metodePembayaran;
    private String tanggalTransaksi;

    public TransaksiPembayaran(String idTransaksi, String idRekamMedis, double biayaLayanan, 
                               double diskonBpjs, double totalBayar, String metodePembayaran, String tanggalTransaksi) {
        setIdTransaksi(idTransaksi);
        setIdRekamMedis(idRekamMedis);
        setBiayaLayanan(biayaLayanan);
        setDiskonBpjs(diskonBpjs);
        setTotalBayar(totalBayar);
        setMetodePembayaran(metodePembayaran);
        this.tanggalTransaksi = (tanggalTransaksi != null && !tanggalTransaksi.isEmpty()) ? tanggalTransaksi : "Sekarang";
    }

    // --- Getter & Setter dengan Validasi ---

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        if (idTransaksi == null || idTransaksi.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] ID Transaksi tidak boleh kosong!");
        }
        this.idTransaksi = idTransaksi.trim();
    }

    public String getIdRekamMedis() {
        return idRekamMedis;
    }

    public void setIdRekamMedis(String idRekamMedis) {
        if (idRekamMedis == null || idRekamMedis.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] ID Rekam Medis wajib terhubung!");
        }
        this.idRekamMedis = idRekamMedis.trim();
    }

    public double getBiayaLayanan() {
        return biayaLayanan;
    }

    public void setBiayaLayanan(double biayaLayanan) {
        if (biayaLayanan < 0) {
            throw new IllegalArgumentException("[Validasi Error] Biaya layanan tidak boleh bernilai negatif!");
        }
        this.biayaLayanan = biayaLayanan;
    }

    public double getDiskonBpjs() {
        return diskonBpjs;
    }

    public void setDiskonBpjs(double diskonBpjs) {
        if (diskonBpjs < 0) {
            throw new IllegalArgumentException("[Validasi Error] Diskon BPJS tidak boleh bernilai negatif!");
        }
        this.diskonBpjs = diskonBpjs;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(double totalBayar) {
        if (totalBayar < 0) {
            throw new IllegalArgumentException("[Validasi Error] Total bayar tidak boleh bernilai negatif!");
        }
        this.totalBayar = totalBayar;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        if (metodePembayaran == null || metodePembayaran.trim().isEmpty()) {
            this.metodePembayaran = "Tunai";
        } else {
            this.metodePembayaran = metodePembayaran.trim();
        }
    }

    public String getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(String tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    /**
     * Fitur Kreativitas: Pencetakan Struk Resmi Transaksi Klinik.
     * Menggabungkan informasi objek Pasien, Tenaga Medis, dan Rekam Medis.
     * 
     * @param pasien Objek Pasien terkait
     * @param medis Objek Tenaga Medis yang melayani
     * @param rm Objek Rekam Medis
     * @return Teks terformat struk pembayaran kasir klinik
     */
    public String generateStruk(Pasien pasien, TenagaMedis medis, RekamMedis rm) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================================\n");
        sb.append("               KLINIK PRATAMA SEHAT PENS                 \n");
        sb.append("        Jl. Raya Lenteng Km. 2, Batuan, Sumenep          \n");
        sb.append("                 Telp: (0328) 662-8888                   \n");
        sb.append("=========================================================\n");
        sb.append(String.format(" No. Transaksi  : %s\n", idTransaksi));
        sb.append(String.format(" Waktu Cetak    : %s\n", tanggalTransaksi));
        sb.append(String.format(" ID Rekam Medis : %s\n", idRekamMedis));
        sb.append("---------------------------------------------------------\n");
        sb.append(" INFORMASI PASIEN:\n");
        if (pasien != null) {
            sb.append(String.format("   Nama Pasien  : %s (%d th)\n", pasien.getNamaPasien(), pasien.getUsia()));
            sb.append(String.format("   Status BPJS  : %s\n", pasien.isStatusBpjs() ? "AKTIF (Diskon 50%)" : "NON-BPJS / UMUM"));
        } else {
            sb.append("   Nama Pasien  : Pasien Umum\n");
        }
        sb.append(" TENAGA KESEHATAN:\n");
        if (medis != null) {
            sb.append(String.format("   Petugas      : %s\n", medis.getNama()));
            sb.append(String.format("   Spesialisasi : %s\n", medis.getSpesialisasi()));
        }
        if (rm != null) {
            sb.append(String.format("   Diagnosa     : %s\n", rm.getDiagnosa()));
            sb.append(String.format("   Tindakan     : %s\n", rm.getTindakan()));
        }
        sb.append("---------------------------------------------------------\n");
        sb.append(String.format(" Subtotal Biaya Layanan : %25s\n", nf.format(biayaLayanan)));
        sb.append(String.format(" Potongan Diskon BPJS   : -%24s\n", nf.format(diskonBpjs)));
        sb.append("=========================================================\n");
        sb.append(String.format(" TOTAL DIBAYAR          : %25s\n", nf.format(totalBayar)));
        sb.append(String.format(" Metode Pembayaran      : %25s\n", metodePembayaran));
        sb.append("=========================================================\n");
        sb.append("        Semoga Lekas Sembuh & Sehat Selalu!              \n");
        sb.append("        Struk ini merupakan bukti pembayaran sah         \n");
        sb.append("=========================================================\n");
        return sb.toString();
    }
}
