/**
 * Class Dosen merepresentasikan entitas tenaga pengajar dan pembimbing akademik (Dosen Wali)
 * dalam Sistem Informasi Akademik (SIAKAD).
 * 
 * Menerapkan prinsip Encapsulation penuh dengan seluruh atribut private, getter-setter tervalidasi,
 * serta method bisnis untuk pembimbingan dan pengesahan Kartu Rencana Studi (KRS).
 * 
 * Praktikum Pemrograman Berorientasi Obyek (PBO) - Modul 4 (Relasi Antarobject)
 * Pengembang : Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)
 * Institusi  : Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)
 */
public class Dosen {
    // =========================================================================
    // 1. ATTRIBUTES (PRIVATE - ENCAPSULATION & INFORMATION HIDING)
    // =========================================================================
    private String nip;
    private String nama;
    private String bidangKeahlian;
    private String email;

    // =========================================================================
    // 2. CONSTRUCTOR (TERVALIDASI)
    // =========================================================================
    public Dosen(String nip, String nama, String bidangKeahlian, String email) {
        setNipInternal(nip);
        setNama(nama);
        setBidangKeahlian(bidangKeahlian);
        setEmail(email);
    }

    // =========================================================================
    // 3. GETTER (ACCESSOR METHODS)
    // =========================================================================
    public String getNip() {
        return this.nip;
    }

    public String getNama() {
        return this.nama;
    }

    public String getBidangKeahlian() {
        return this.bidangKeahlian;
    }

    public String getEmail() {
        return this.email;
    }

    // =========================================================================
    // 4. SETTER DENGAN VALIDASI KETAT (MUTATOR METHODS)
    // =========================================================================
    private void setNipInternal(String nip) {
        if (nip != null && !nip.trim().isEmpty()) {
            this.nip = nip.trim();
        } else {
            this.nip = "190000000000000000";
            System.out.println("[VALIDASI ERROR] NIP Dosen tidak boleh kosong/null! Diset ke NIP default.");
        }
    }

    public final void setNama(String nama) {
        if (nama != null && nama.trim().length() >= 3) {
            this.nama = nama.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Nama dosen tidak valid! Minimal 3 karakter.");
        }
    }

    public final void setBidangKeahlian(String bidangKeahlian) {
        if (bidangKeahlian != null && !bidangKeahlian.trim().isEmpty()) {
            this.bidangKeahlian = bidangKeahlian.trim();
        } else {
            System.out.println("[VALIDASI DITOLAK] Bidang keahlian tidak boleh kosong!");
        }
    }

    public final void setEmail(String email) {
        if (email != null && email.contains("@") && email.contains(".")) {
            this.email = email.trim();
        } else {
            this.email = "dosen@pens.ac.id";
            System.out.println("[VALIDASI ERROR] Format email dosen tidak valid! Diset ke dosen@pens.ac.id.");
        }
    }

    // =========================================================================
    // 5. BUSINESS LOGIC METHODS (INTERAKSI & KOLABORASI ANTAR-OBJEK)
    // =========================================================================
    /**
     * Interaksi Asosiasi: Dosen Wali memeriksa dan mengesahkan dokumen KRS mahasiswa.
     * Mengakses method dari objek KRS tanpa menyentuh atribut private-nya secara langsung.
     */
    public void validasiDanSetujuiKrs(KRS krs) {
        if (krs == null) {
            System.out.println("[VALIDASI DOSEN] Gagal: Dokumen KRS bernilai null!");
            return;
        }

        System.out.println("\n>> [PROSES BIMBINGAN] Dosen Wali (" + this.nama + ") memeriksa KRS: " + krs.getNomorKrs());
        if (krs.getJumlahMk() == 0) {
            System.out.println("   [PENOLAKAN] KRS belum memiliki mata kuliah yang terdaftar.");
        } else {
            System.out.println("   [DISETUJUI] KRS valid (" + krs.getJumlahMk() + " MK terdaftar, Total " + 
                               krs.hitungTotalSksKrs() + " SKS). Mengesahkan KRS...");
            krs.setujuiKrs(this);
        }
    }

    /**
     * Menampilkan profil ringkas dosen.
     */
    public void tampilkanProfil() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                   PROFIL DOSEN PENGAMPU                    ");
        System.out.println("------------------------------------------------------------");
        System.out.println("NIP              : " + this.nip);
        System.out.println("Nama Dosen       : " + this.nama);
        System.out.println("Bidang Keahlian  : " + this.bidangKeahlian);
        System.out.println("Email Resmi      : " + this.email);
        System.out.println("------------------------------------------------------------");
    }
}
