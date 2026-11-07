package P11_3125522015_MohamadZaky.model;

public class StokTidakCukupException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String namaBahan;
    private final int stokTersedia;
    private final int jumlahDiminta;

    public StokTidakCukupException(String pesan) {
        super(pesan);
        this.namaBahan = "-";
        this.stokTersedia = 0;
        this.jumlahDiminta = 0;
    }

    public StokTidakCukupException(String namaBahan, int stokTersedia, int jumlahDiminta) {
        super("Stok '" + namaBahan + "' tidak mencukupi. Tersedia: " + stokTersedia + ", diminta: " + jumlahDiminta + ".");
        this.namaBahan = namaBahan;
        this.stokTersedia = stokTersedia;
        this.jumlahDiminta = jumlahDiminta;
    }

    public String getNamaBahan() {
        return namaBahan;
    }

    public int getStokTersedia() {
        return stokTersedia;
    }

    public int getJumlahDiminta() {
        return jumlahDiminta;
    }
}

