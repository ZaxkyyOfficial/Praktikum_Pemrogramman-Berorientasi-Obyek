package model;

import java.util.Objects;

/**
 * Model class Mahasiswa
 * Merepresentasikan mahasiswa yang berhak merencanakan studi akademik.
 */
public class Mahasiswa {
    private final String nim;
    private final String nama;
    private final int semester;
    private final int maxSKS;
    private final String status;
    private KRS krsAktif;

    public Mahasiswa(String nim, String nama, int semester, int maxSKS, String status) {
        if (nim == null || nim.trim().isEmpty()) {
            throw new IllegalArgumentException("NIM tidak boleh kosong");
        }
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama mahasiswa tidak boleh kosong");
        }
        if (semester <= 0) {
            throw new IllegalArgumentException("Semester harus lebih dari 0");
        }
        if (maxSKS <= 0 || maxSKS > 24) {
            throw new IllegalArgumentException("Maksimum SKS harus dalam rentang 1 s.d. 24");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status mahasiswa tidak boleh kosong");
        }

        this.nim = nim.trim();
        this.nama = nama.trim();
        this.semester = semester;
        this.maxSKS = maxSKS;
        this.status = status.trim().toUpperCase();
    }

    public void setKrsAktif(KRS krs) {
        this.krsAktif = krs;
    }

    public KRS getKrsAktif() {
        return krsAktif;
    }

    public void ambilMataKuliah(MataKuliah mk) {
        if (!"AKTIF".equalsIgnoreCase(this.status)) {
            throw new IllegalStateException("Mahasiswa dengan status " + this.status + " tidak diizinkan mengambil mata kuliah");
        }
        if (this.krsAktif == null) {
            throw new IllegalStateException("KRS aktif belum diinisialisasi untuk mahasiswa ini");
        }
        this.krsAktif.tambahItem(mk);
    }

    public void batalkanMataKuliah(String kodeMK) {
        if (this.krsAktif == null) {
            throw new IllegalStateException("KRS aktif belum terdaftar");
        }
        this.krsAktif.hapusItem(kodeMK);
    }

    public int hitungTotalSKS() {
        return this.krsAktif != null ? this.krsAktif.hitungTotalSKS() : 0;
    }

    public boolean validasiBebanSKS(int targetSKS) {
        return targetSKS <= this.maxSKS;
    }

    public String getNim() {
        return nim;
    }

    public String getNama() {
        return nama;
    }

    public int getSemester() {
        return semester;
    }

    public int getMaxSKS() {
        return maxSKS;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mahasiswa mahasiswa = (Mahasiswa) o;
        return Objects.equals(nim, mahasiswa.nim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nim);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (Semester %d, Status: %s, Max SKS: %d)",
                nim, nama, semester, status, maxSKS);
    }
}
