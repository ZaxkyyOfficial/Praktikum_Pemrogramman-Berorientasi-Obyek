public class RekamMedis {

    private String idRekamMedis;
    private String idPasien;
    private String idMedis;
    private String keluhan;
    private String diagnosa;
    private String tindakan;
    private String tanggalPeriksa;

    public RekamMedis(String idRekamMedis, String idPasien, String idMedis, 
                      String keluhan, String diagnosa, String tindakan, String tanggalPeriksa) {
        setIdRekamMedis(idRekamMedis);
        setIdPasien(idPasien);
        setIdMedis(idMedis);
        setKeluhan(keluhan);
        setDiagnosa(diagnosa);
        setTindakan(tindakan);
        this.tanggalPeriksa = (tanggalPeriksa != null && !tanggalPeriksa.isEmpty()) ? tanggalPeriksa : "Sekarang";
    }

    // --- Getter & Setter dengan Validasi ---

    public String getIdRekamMedis() {
        return idRekamMedis;
    }

    public void setIdRekamMedis(String idRekamMedis) {
        if (idRekamMedis == null || idRekamMedis.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] ID Rekam Medis tidak boleh kosong!");
        }
        this.idRekamMedis = idRekamMedis.trim();
    }

    public String getIdPasien() {
        return idPasien;
    }

    public void setIdPasien(String idPasien) {
        if (idPasien == null || idPasien.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] ID Pasien tidak boleh kosong pada rekam medis!");
        }
        this.idPasien = idPasien.trim();
    }

    public String getIdMedis() {
        return idMedis;
    }

    public void setIdMedis(String idMedis) {
        if (idMedis == null || idMedis.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] ID Tenaga Medis tidak boleh kosong pada rekam medis!");
        }
        this.idMedis = idMedis.trim();
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        if (keluhan == null || keluhan.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] Keluhan pasien wajib diisi!");
        }
        this.keluhan = keluhan.trim();
    }

    public String getDiagnosa() {
        return diagnosa;
    }

    public void setDiagnosa(String diagnosa) {
        if (diagnosa == null || diagnosa.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] Diagnosa klinis wajib diisi!");
        }
        this.diagnosa = diagnosa.trim();
    }

    public String getTindakan() {
        return tindakan;
    }

    public void setTindakan(String tindakan) {
        if (tindakan == null || tindakan.trim().isEmpty()) {
            throw new IllegalArgumentException("[Validasi Error] Tindakan medis wajib diisi!");
        }
        this.tindakan = tindakan.trim();
    }

    public String getTanggalPeriksa() {
        return tanggalPeriksa;
    }

    public void setTanggalPeriksa(String tanggalPeriksa) {
        this.tanggalPeriksa = tanggalPeriksa;
    }

    @Override
    public String toString() {
        return "[" + idRekamMedis + "] Pasien: " + idPasien + " | Medis: " + idMedis + " | Diagnosa: " + diagnosa;
    }
}
