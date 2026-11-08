package P13_3125522015_MohamadZaky.model;

public class DetailPengiriman {
    private String nomorBatch;
    private String jenisKemasan;
    private int jumlahPorsiBatch;
    private double suhuPenyimpanan;
    private String catatanInspeksi;

    public DetailPengiriman(String nomorBatch, String jenisKemasan, int jumlahPorsiBatch,
                            double suhuPenyimpanan, String catatanInspeksi) {
        setNomorBatch(nomorBatch);
        setJenisKemasan(jenisKemasan);
        setJumlahPorsiBatch(jumlahPorsiBatch);
        setSuhuPenyimpanan(suhuPenyimpanan);
        setCatatanInspeksi(catatanInspeksi);
    }

    public String getNomorBatch() {
        return nomorBatch;
    }

    public final void setNomorBatch(String nomorBatch) {
        if (nomorBatch == null || nomorBatch.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor batch pengiriman tidak boleh kosong.");
        }
        this.nomorBatch = nomorBatch.trim();
    }

    public String getJenisKemasan() {
        return jenisKemasan;
    }

    public final void setJenisKemasan(String jenisKemasan) {
        if (jenisKemasan == null || jenisKemasan.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis kemasan tidak boleh kosong.");
        }
        this.jenisKemasan = jenisKemasan.trim();
    }

    public int getJumlahPorsiBatch() {
        return jumlahPorsiBatch;
    }

    public final void setJumlahPorsiBatch(int jumlahPorsiBatch) {
        if (jumlahPorsiBatch <= 0) {
            throw new IllegalArgumentException("Jumlah porsi dalam batch harus lebih dari nol.");
        }
        this.jumlahPorsiBatch = jumlahPorsiBatch;
    }

    public double getSuhuPenyimpanan() {
        return suhuPenyimpanan;
    }

    public final void setSuhuPenyimpanan(double suhuPenyimpanan) {
        if (suhuPenyimpanan < -20.0 || suhuPenyimpanan > 100.0) {
            throw new IllegalArgumentException("Suhu penyimpanan berada di luar rentang wajar (-20 s.d. 100 C).");
        }
        this.suhuPenyimpanan = suhuPenyimpanan;
    }

    public String getCatatanInspeksi() {
        return catatanInspeksi;
    }

    public final void setCatatanInspeksi(String catatanInspeksi) {
        if (catatanInspeksi == null || catatanInspeksi.trim().isEmpty()) {
            throw new IllegalArgumentException("Catatan inspeksi batch tidak boleh kosong.");
        }
        this.catatanInspeksi = catatanInspeksi.trim();
    }
}

