package P12_3125522015_MohamadZaky.model;

public abstract class Pengguna {
    private String idPengguna;
    private String nama;
    private String email;

    public Pengguna(String idPengguna, String nama, String email) {
        setIdPengguna(idPengguna);
        setNama(nama);
        setEmail(email);
    }

    public abstract String tampilkanPeran();

    public abstract String getRingkasanTugas();

    public String getIdPengguna() {
        return idPengguna;
    }

    public final void setIdPengguna(String idPengguna) {
        if (idPengguna == null || idPengguna.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Pengguna tidak boleh kosong.");
        }
        this.idPengguna = idPengguna.trim();
    }

    public String getNama() {
        return nama;
    }

    public final void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pengguna tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Format email pengguna tidak valid.");
        }
        this.email = email.trim();
    }
}

