package P11_3125522015_MohamadZaky.model;

public class StafDapur extends Pengguna {
    private String instalasiDapur;

    public StafDapur(String idPengguna, String nama, String email, String instalasiDapur) {
        super(idPengguna, nama, email);
        setInstalasiDapur(instalasiDapur);
    }

    @Override
    public String tampilkanPeran() {
        return "Staf Dapur & Gizi (" + instalasiDapur + ")";
    }

    @Override
    public String getRingkasanTugas() {
        return "Inspeksi masa kedaluwarsa bahan baku, takaran porsi menu, dan kontrol suhu masak.";
    }

    public String getInstalasiDapur() {
        return instalasiDapur;
    }

    public final void setInstalasiDapur(String instalasiDapur) {
        if (instalasiDapur == null || instalasiDapur.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama instalasi dapur tidak boleh kosong.");
        }
        this.instalasiDapur = instalasiDapur.trim();
    }
}

