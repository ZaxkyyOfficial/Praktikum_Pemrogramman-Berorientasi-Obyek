package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class KRS {

    public enum StatusKRS {
        DRAFT,
        DIAJUKAN,
        DISETUJUI
    }

    private final String idKRS;
    private final Mahasiswa mahasiswa;
    private final Dosen dosenPembimbing;
    private final List<MataKuliah> mataKuliahList;
    private StatusKRS status;

    public KRS(String idKRS, Mahasiswa mahasiswa, Dosen dosenPembimbing) {
        if (idKRS == null || idKRS.trim().isEmpty()) {
            throw new IllegalArgumentException("ID KRS tidak boleh kosong");
        }
        if (mahasiswa == null) {
            throw new IllegalArgumentException("Mahasiswa pemilik KRS tidak boleh null");
        }
        if (dosenPembimbing == null) {
            throw new IllegalArgumentException("Dosen pembimbing KRS tidak boleh null");
        }

        this.idKRS = idKRS.trim();
        this.mahasiswa = mahasiswa;
        this.dosenPembimbing = dosenPembimbing;
        this.mataKuliahList = new ArrayList<>();
        this.status = StatusKRS.DRAFT;
    }

    public synchronized void tambahItem(MataKuliah mk) {
        if (mk == null) {
            throw new IllegalArgumentException("Mata kuliah tidak boleh null");
        }
        if (this.status != StatusKRS.DRAFT) {
            throw new IllegalStateException("KRS tidak dapat diubah karena berstatus: " + this.status);
        }

        for (MataKuliah item : mataKuliahList) {
            if (item.getKodeMK().equalsIgnoreCase(mk.getKodeMK())) {
                throw new IllegalStateException("Mata kuliah [" + mk.getKodeMK() + "] sudah ada di dalam KRS");
            }
        }

        int totalSksBaru = hitungTotalSKS() + mk.getSks();
        if (!mahasiswa.validasiBebanSKS(totalSksBaru)) {
            throw new IllegalStateException(String.format(
                    "Total SKS (%d SKS) melebihi batas beban maksimum mahasiswa (%d SKS)",
                    totalSksBaru, mahasiswa.getMaxSKS()));
        }

        if (!mk.cekKetersediaan()) {
            throw new IllegalStateException("Kuota mata kuliah [" + mk.getKodeMK() + "] sudah penuh (" + mk.getKuota() + "/" + mk.getKuota() + ")");
        }

        boolean dialokasikan = mk.alokasiKursi();
        if (!dialokasikan) {
            throw new IllegalStateException("Gagal mengalokasikan kursi pada mata kuliah [" + mk.getKodeMK() + "]");
        }

        mataKuliahList.add(mk);
    }

    public synchronized void hapusItem(String kodeMK) {
        if (kodeMK == null || kodeMK.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode MK tidak valid");
        }
        if (this.status != StatusKRS.DRAFT) {
            throw new IllegalStateException("KRS tidak dapat dimodifikasi karena berstatus: " + this.status);
        }

        MataKuliah target = null;
        for (MataKuliah item : mataKuliahList) {
            if (item.getKodeMK().equalsIgnoreCase(kodeMK.trim())) {
                target = item;
                break;
            }
        }

        if (target == null) {
            throw new IllegalArgumentException("Mata kuliah [" + kodeMK + "] tidak ditemukan di dalam KRS");
        }

        target.batalkanKursi();
        mataKuliahList.remove(target);
    }

    public synchronized void ajukanKRS() {
        if (this.status != StatusKRS.DRAFT) {
            throw new IllegalStateException("Pengajuan hanya dapat dilakukan jika status KRS adalah DRAFT. Status saat ini: " + this.status);
        }
        if (this.mataKuliahList.isEmpty()) {
            throw new IllegalStateException("Tidak dapat mengajukan KRS yang kosong. Ambil minimal 1 mata kuliah.");
        }
        this.status = StatusKRS.DIAJUKAN;
    }

    public synchronized void setujuiKRS(Dosen d) {
        if (d == null) {
            throw new IllegalArgumentException("Dosen pengesah tidak valid");
        }
        if (!this.dosenPembimbing.equals(d)) {
            throw new IllegalStateException("Hanya dosen pembimbing [" + dosenPembimbing.getNama() + "] yang berwenang menyetujui KRS ini");
        }
        if (this.status != StatusKRS.DIAJUKAN) {
            throw new IllegalStateException("KRS belum diajukan oleh mahasiswa untuk diverifikasi (Status: " + this.status + ")");
        }
        this.status = StatusKRS.DISETUJUI;
    }

    public synchronized void kembalikanKeDraft(String catatan) {
        this.status = StatusKRS.DRAFT;
    }

    public synchronized void setStatus(StatusKRS status) {
        if (status != null) {
            this.status = status;
        }
    }

    public synchronized void muatItemDariDatabase(MataKuliah mk) {
        if (mk == null) return;
        for (MataKuliah item : mataKuliahList) {
            if (item.getKodeMK().equalsIgnoreCase(mk.getKodeMK())) {
                return;
            }
        }
        mataKuliahList.add(mk);
    }

    public synchronized int hitungTotalSKS() {
        int total = 0;
        for (MataKuliah mk : mataKuliahList) {
            total += mk.getSks();
        }
        return total;
    }

    public String getIdKRS() {
        return idKRS;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public Dosen getDosenPembimbing() {
        return dosenPembimbing;
    }

    public synchronized List<MataKuliah> getMataKuliahList() {
        return Collections.unmodifiableList(new ArrayList<>(mataKuliahList));
    }

    public synchronized StatusKRS getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KRS krs = (KRS) o;
        return Objects.equals(idKRS, krs.idKRS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKRS);
    }
}
