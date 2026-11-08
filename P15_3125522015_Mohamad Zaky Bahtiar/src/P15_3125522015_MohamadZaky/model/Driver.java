package P15_3125522015_MohamadZaky.model;

public class Driver extends Pengguna implements DapatDilacak {
    private String ruteUtama;
    private String nomorArmada;
    private String lokasiTerkini;

    public Driver(String idPengguna, String nama, String email, String ruteUtama, String nomorArmada, String lokasiTerkini) {
        super(idPengguna, nama, email);
        setRuteUtama(ruteUtama);
        setNomorArmada(nomorArmada);
        perbaruiLokasi(lokasiTerkini);
    }

    @Override
    public String tampilkanPeran() {
        return "Driver Armada [" + nomorArmada + "] - " + ruteUtama;
    }

    @Override
    public String getRingkasanTugas() {
        return "Pengiriman makanan bergizi tepat waktu dengan pemantauan posisi armada.";
    }

    @Override
    public String getLokasi() {
        return "[" + nomorArmada + "] " + lokasiTerkini;
    }

    @Override
    public final void perbaruiLokasi(String lokasiBaru) {
        if (lokasiBaru == null || lokasiBaru.trim().isEmpty()) {
            throw new IllegalArgumentException("Lokasi armada tidak boleh kosong.");
        }
        this.lokasiTerkini = lokasiBaru.trim();
    }

    public String getRuteUtama() {
        return ruteUtama;
    }

    public final void setRuteUtama(String ruteUtama) {
        if (ruteUtama == null || ruteUtama.trim().isEmpty()) {
            throw new IllegalArgumentException("Rute utama driver tidak boleh kosong.");
        }
        this.ruteUtama = ruteUtama.trim();
    }

    public String getNomorArmada() {
        return nomorArmada;
    }

    public final void setNomorArmada(String nomorArmada) {
        if (nomorArmada == null || nomorArmada.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor polisi armada tidak boleh kosong.");
        }
        this.nomorArmada = nomorArmada.trim();
    }

    public String getLokasiTerkini() {
        return lokasiTerkini;
    }

    @Override
    public String toString() {
        return getNama() + " (" + nomorArmada + ")";
    }
}

