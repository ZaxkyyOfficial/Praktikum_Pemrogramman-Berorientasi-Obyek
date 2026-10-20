package model;

import java.util.Objects;

/**
 * Model class Dosen
 * Merepresentasikan Dosen Pembimbing Akademik yang memiliki wewenang verifikasi KRS.
 */
public class Dosen {
    private final String nip;
    private final String nama;
    private final int kuotaBimbingan;
    private final String spesialisasi;

    public Dosen(String nip, String nama, int kuotaBimbingan, String spesialisasi) {
        if (nip == null || nip.trim().isEmpty()) {
            throw new IllegalArgumentException("NIP dosen tidak boleh kosong");
        }
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama dosen tidak boleh kosong");
        }
        if (kuotaBimbingan <= 0) {
            throw new IllegalArgumentException("Kuota bimbingan dosen harus lebih dari 0");
        }
        if (spesialisasi == null || spesialisasi.trim().isEmpty()) {
            throw new IllegalArgumentException("Spesialisasi dosen tidak boleh kosong");
        }

        this.nip = nip.trim();
        this.nama = nama.trim();
        this.kuotaBimbingan = kuotaBimbingan;
        this.spesialisasi = spesialisasi.trim();
    }

    public void verifikasiKRS(KRS krs) {
        if (krs == null) {
            throw new IllegalArgumentException("Objek KRS tidak valid");
        }
        if (!krs.getDosenPembimbing().equals(this)) {
            throw new IllegalStateException("Hanya dosen pembimbing akademik bersangkutan yang dapat memverifikasi KRS ini");
        }
        krs.setujuiKRS(this);
    }

    public void batalkanPersetujuanKRS(KRS krs) {
        if (krs == null) {
            throw new IllegalArgumentException("Objek KRS tidak valid");
        }
        if (!krs.getDosenPembimbing().equals(this)) {
            throw new IllegalStateException("Hanya dosen pembimbing akademik bersangkutan yang dapat membatalkan persetujuan");
        }
        krs.kembalikanKeDraft("Persetujuan dibatalkan oleh Dosen Pembimbing (" + this.nama + ")");
    }

    public String getNip() {
        return nip;
    }

    public String getNama() {
        return nama;
    }

    public int getKuotaBimbingan() {
        return kuotaBimbingan;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dosen dosen = (Dosen) o;
        return Objects.equals(nip, dosen.nip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nip);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (Spesialisasi: %s, Kuota: %d)", nip, nama, spesialisasi, kuotaBimbingan);
    }
}
