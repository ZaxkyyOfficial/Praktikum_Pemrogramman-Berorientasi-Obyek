package P14_3125522015_MohamadZaky.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pengiriman implements DapatDilacak {
    private String idPengiriman;
    private Sekolah sekolahTujuan;
    private Driver driverArmada;
    private Menu menuDikirim;
    private int jumlahPorsi;
    private String statusDistribusi;
    private String lokasiTerakhir;
    private String waktuBerangkat;
    private final List<DetailPengiriman> daftarDetailBatch;

    public Pengiriman(String idPengiriman, Sekolah sekolahTujuan, Driver driverArmada,
                      Menu menuDikirim, int jumlahPorsi, String statusDistribusi,
                      String lokasiTerakhir, String waktuBerangkat) {
        setIdPengiriman(idPengiriman);
        setSekolahTujuan(sekolahTujuan);
        setDriverArmada(driverArmada);
        setMenuDikirim(menuDikirim);
        setJumlahPorsi(jumlahPorsi);
        setStatusDistribusi(statusDistribusi);
        perbaruiLokasi(lokasiTerakhir);
        setWaktuBerangkat(waktuBerangkat);
        this.daftarDetailBatch = new ArrayList<>();
    }

    public DetailPengiriman buatDetailBatch(String nomorBatch, String jenisKemasan, int porsiBatch,
                                            double suhuPenyimpanan, String catatanInspeksi) {
        DetailPengiriman detailBaru = new DetailPengiriman(
                nomorBatch, jenisKemasan, porsiBatch, suhuPenyimpanan, catatanInspeksi
        );
        this.daftarDetailBatch.add(detailBaru);
        return detailBaru;
    }

    public List<DetailPengiriman> getDaftarDetailBatch() {
        return Collections.unmodifiableList(daftarDetailBatch);
    }

    @Override
    public String getLokasi() {
        return "[" + statusDistribusi + "] " + driverArmada.getNomorArmada() + " - " + lokasiTerakhir;
    }

    @Override
    public final void perbaruiLokasi(String lokasiBaru) {
        if (lokasiBaru == null || lokasiBaru.trim().isEmpty()) {
            throw new IllegalArgumentException("Lokasi pelacakan pengiriman tidak boleh kosong.");
        }
        this.lokasiTerakhir = lokasiBaru.trim();
        if (this.driverArmada != null) {
            this.driverArmada.perbaruiLokasi(this.lokasiTerakhir);
        }
    }

    public String getIdPengiriman() {
        return idPengiriman;
    }

    public final void setIdPengiriman(String idPengiriman) {
        if (idPengiriman == null || idPengiriman.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Pengiriman tidak boleh kosong.");
        }
        this.idPengiriman = idPengiriman.trim().toUpperCase();
    }

    public Sekolah getSekolahTujuan() {
        return sekolahTujuan;
    }

    public final void setSekolahTujuan(Sekolah sekolahTujuan) {
        if (sekolahTujuan == null) {
            throw new IllegalArgumentException("Sekolah tujuan pengiriman wajib dipilih.");
        }
        this.sekolahTujuan = sekolahTujuan;
    }

    public Driver getDriverArmada() {
        return driverArmada;
    }

    public final void setDriverArmada(Driver driverArmada) {
        if (driverArmada == null) {
            throw new IllegalArgumentException("Driver armada pengiriman wajib dipilih.");
        }
        this.driverArmada = driverArmada;
    }

    public Menu getMenuDikirim() {
        return menuDikirim;
    }

    public final void setMenuDikirim(Menu menuDikirim) {
        if (menuDikirim == null) {
            throw new IllegalArgumentException("Menu makanan yang dikirim wajib dipilih.");
        }
        this.menuDikirim = menuDikirim;
    }

    public int getJumlahPorsi() {
        return jumlahPorsi;
    }

    public final void setJumlahPorsi(int jumlahPorsi) {
        if (jumlahPorsi <= 0) {
            throw new IllegalArgumentException("Jumlah porsi pengiriman harus lebih dari nol.");
        }
        this.jumlahPorsi = jumlahPorsi;
    }

    public String getStatusDistribusi() {
        return statusDistribusi;
    }

    public final void setStatusDistribusi(String statusDistribusi) {
        if (statusDistribusi == null || statusDistribusi.trim().isEmpty()) {
            throw new IllegalArgumentException("Status distribusi tidak boleh kosong.");
        }
        String s = statusDistribusi.trim();
        if (!s.equals("Pending") && !s.equals("In Transit") && !s.equals("Delivered")) {
            throw new IllegalArgumentException("Status distribusi harus bernilai Pending, In Transit, atau Delivered.");
        }
        this.statusDistribusi = s;
    }

    public String getLokasiTerakhir() {
        return lokasiTerakhir;
    }

    public String getWaktuBerangkat() {
        return waktuBerangkat;
    }

    public final void setWaktuBerangkat(String waktuBerangkat) {
        if (waktuBerangkat == null || waktuBerangkat.trim().isEmpty()) {
            throw new IllegalArgumentException("Waktu keberangkatan harus diisi.");
        }
        this.waktuBerangkat = waktuBerangkat.trim();
    }

    @Override
    public String toString() {
        return idPengiriman + " -> " + sekolahTujuan.getNamaSekolah() + " (" + jumlahPorsi + " porsi)";
    }
}

