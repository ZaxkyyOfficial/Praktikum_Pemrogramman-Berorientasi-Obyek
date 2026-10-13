import java.text.NumberFormat;
import java.util.Locale;

public class Perawat extends TenagaMedis implements PelayananKlinik {

    // Atribut privat khusus Perawat (Encapsulation)
    private int jamLembur;
    
    // Konstanta tarif lembur & jasa asuhan keperawatan (Business Logic)
    public static final double TARIF_INSENTIF_PER_JAM = 50000.0;
    public static final double TARIF_ASUHAN_DASAR = 35000.0;

    /**
     * Konstruktor lengkap untuk Perawat.
     * 
     * @param idMedis Kode unik perawat (NUR-xxx)
     * @param nama Nama lengkap perawat beserta gelar keperawatan
     * @param spesialisasi Unit atau divisi keperawatan
     * @param jamLembur Durasi jam lembur yang telah ditempuh
     */
    public Perawat(String idMedis, String nama, String spesialisasi, int jamLembur) {
        super(idMedis, nama, spesialisasi);
        setJamLembur(jamLembur);
    }

    // --- Getter & Setter dengan Validasi Bisnis ---

    public int getJamLembur() {
        return jamLembur;
    }

    public void setJamLembur(int jamLembur) {
        if (jamLembur < 0) {
            throw new IllegalArgumentException("[Validasi Error] Jam lembur perawat tidak boleh bernilai negatif!");
        }
        this.jamLembur = jamLembur;
    }

    /**
     * Fitur Kreativitas: Kalkulasi insentif lembur perawat untuk sistem penggajian/reward klinik.
     * 
     * @return Nominal insentif lembur yang berhak diterima perawat
     */
    public double hitungInsentifLembur() {
        return this.jamLembur * TARIF_INSENTIF_PER_JAM;
    }

    // --- Method Overriding (Polymorphism) ---

    @Override
    public void tampilkanPeran() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        System.out.println("[PERAN: PERAWAT KLINIS & ASUHAN]");
        System.out.println("  ID Perawat       : " + getIdMedis());
        System.out.println("  Nama Perawat     : " + getNama());
        System.out.println("  Divisi/Unit      : " + getSpesialisasi());
        System.out.println("  Jam Lembur Aktif : " + jamLembur + " Jam");
        System.out.println("  Estimasi Insentif: " + nf.format(hitungInsentifLembur()));
        System.out.println("  Tanggung Jawab   : Pemeriksaan tanda vital (TTV), observasi pasien, asuhan keperawatan, & asistensi dokter.");
    }

    @Override
    public void melayaniPasien(String namaPasien) {
        String prefix = getNama().toLowerCase().startsWith("ns.") ? "" : "Ns. ";
        System.out.println(">> [PERAWAT MELAYANI] " + prefix + getNama() + " (" + getSpesialisasi() + 
                           ") sedang melakukan triase, cek tanda-tanda vital, dan edukasi perawatan pada pasien: '" + namaPasien + "'.");
    }

    @Override
    public double hitungBiayaLayanan(double biayaDasar) {
        if (biayaDasar < 0) {
            throw new IllegalArgumentException("[Validasi Error] Biaya dasar tidak boleh negatif!");
        }
        // Total biaya = biaya tindakan dasar + tarif asuhan keperawatan dasar
        return biayaDasar + TARIF_ASUHAN_DASAR;
    }
}
