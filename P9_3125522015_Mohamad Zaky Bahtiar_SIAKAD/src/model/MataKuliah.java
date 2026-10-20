package model;

import java.util.Objects;

public class MataKuliah {
    private final String kodeMK;
    private final String namaMK;
    private final int sks;
    private final int kuota;
    private int terisi;
    private final int semesterBuka;

    public MataKuliah(String kodeMK, String namaMK, int sks, int kuota, int semesterBuka) {
        if (kodeMK == null || kodeMK.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode MK tidak boleh kosong");
        }
        if (namaMK == null || namaMK.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama MK tidak boleh kosong");
        }
        if (sks <= 0 || sks > 6) {
            throw new IllegalArgumentException("SKS harus berada di rentang 1 hingga 6");
        }
        if (kuota <= 0) {
            throw new IllegalArgumentException("Kuota kelas harus lebih besar dari 0");
        }
        if (semesterBuka <= 0 || semesterBuka > 8) {
            throw new IllegalArgumentException("Semester buka harus antara 1 sampai 8");
        }

        this.kodeMK = kodeMK.trim().toUpperCase();
        this.namaMK = namaMK.trim();
        this.sks = sks;
        this.kuota = kuota;
        this.terisi = 0;
        this.semesterBuka = semesterBuka;
    }

    public synchronized boolean cekKetersediaan() {
        return this.terisi < this.kuota;
    }

    public synchronized boolean alokasiKursi() {
        if (!cekKetersediaan()) {
            return false;
        }
        this.terisi++;
        return true;
    }

    public synchronized boolean batalkanKursi() {
        if (this.terisi > 0) {
            this.terisi--;
            return true;
        }
        return false;
    }

    public synchronized void setTerisi(int terisi) {
        if (terisi >= 0 && terisi <= this.kuota) {
            this.terisi = terisi;
        }
    }

    public String getKodeMK() {
        return kodeMK;
    }

    public String getNamaMK() {
        return namaMK;
    }

    public int getSks() {
        return sks;
    }

    public int getKuota() {
        return kuota;
    }

    public synchronized int getTerisi() {
        return terisi;
    }

    public synchronized int getSisaKuota() {
        return kuota - terisi;
    }

    public int getSemesterBuka() {
        return semesterBuka;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MataKuliah that = (MataKuliah) o;
        return Objects.equals(kodeMK, that.kodeMK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kodeMK);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%d SKS) - Kuota: %d/%d (Sisa: %d)",
                kodeMK, namaMK, sks, terisi, kuota, getSisaKuota());
    }
}
