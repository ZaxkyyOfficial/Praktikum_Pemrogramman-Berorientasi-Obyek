import java.text.NumberFormat;
import java.util.Locale;

public class Dokter extends TenagaMedis implements PelayananKlinik {

    // Atribut privat khusus Dokter (Encapsulation)
    private double biayaKonsultasi;

    /**
     * Konstruktor lengkap untuk Dokter.
     * Menggunakan super(...) untuk meneruskan data ke abstract class TenagaMedis.
     * 
     * @param idMedis Kode unik dokter (DOC-xxx)
     * @param nama Nama lengkap dokter beserta gelar
     * @param spesialisasi Spesialisasi kedokteran
     * @param biayaKonsultasi Tarif jasa konsultasi dokter
     */
    public Dokter(String idMedis, String nama, String spesialisasi, double biayaKonsultasi) {
        super(idMedis, nama, spesialisasi);
        setBiayaKonsultasi(biayaKonsultasi);
    }

    // --- Getter & Setter dengan Validasi Bisnis ---

    public double getBiayaKonsultasi() {
        return biayaKonsultasi;
    }

    public void setBiayaKonsultasi(double biayaKonsultasi) {
        if (biayaKonsultasi < 0) {
            throw new IllegalArgumentException("[Validasi Error] Biaya konsultasi dokter tidak boleh bernilai negatif!");
        }
        this.biayaKonsultasi = biayaKonsultasi;
    }

    // --- Method Overriding (Polymorphism) ---

    @Override
    public void tampilkanPeran() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        System.out.println("[PERAN: DOKTER SPESIALIS]");
        System.out.println("  ID Dokter        : " + getIdMedis());
        System.out.println("  Nama Dokter      : " + getNama());
        System.out.println("  Bidang Keahlian  : " + getSpesialisasi());
        System.out.println("  Tarif Konsultasi : " + nf.format(biayaKonsultasi));
        System.out.println("  Tanggung Jawab   : Anamnesis, diagnosis medis, penentuan terapi & resep obat.");
    }

    @Override
    public void melayaniPasien(String namaPasien) {
        String prefix = getNama().toLowerCase().startsWith("dr.") ? "" : "dr. ";
        System.out.println(">> [DOKTER MELAYANI] " + prefix + getNama() + " (" + getSpesialisasi() + 
                           ") sedang melakukan pemeriksaan klinis & diagnosis terhadap pasien: '" + namaPasien + "'.");
    }

    @Override
    public double hitungBiayaLayanan(double biayaDasar) {
        if (biayaDasar < 0) {
            throw new IllegalArgumentException("[Validasi Error] Biaya dasar tidak boleh negatif!");
        }
        // Total biaya layanan = biaya tindakan medis dasar + tarif konsultasi dokter
        return biayaDasar + this.biayaKonsultasi;
    }
}
