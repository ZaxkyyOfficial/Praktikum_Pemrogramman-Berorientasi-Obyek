package P14_3125522015_MohamadZaky.model;

public class Admin extends Pengguna {
    private String wilayahKoordinasi;

    public Admin(String idPengguna, String nama, String email, String wilayahKoordinasi) {
        super(idPengguna, nama, email);
        setWilayahKoordinasi(wilayahKoordinasi);
    }

    @Override
    public String tampilkanPeran() {
        return "Administrator Wilayah (" + wilayahKoordinasi + ")";
    }

    @Override
    public String getRingkasanTugas() {
        return "Otorisasi anggaran MBG, verifikasi sekolah penerima, dan audit kepatuhan gizi.";
    }

    public String getWilayahKoordinasi() {
        return wilayahKoordinasi;
    }

    public final void setWilayahKoordinasi(String wilayahKoordinasi) {
        if (wilayahKoordinasi == null || wilayahKoordinasi.trim().isEmpty()) {
            throw new IllegalArgumentException("Wilayah koordinasi admin tidak boleh kosong.");
        }
        this.wilayahKoordinasi = wilayahKoordinasi.trim();
    }
}

