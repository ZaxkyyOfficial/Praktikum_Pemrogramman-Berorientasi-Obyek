package P14_3125522015_MohamadZaky.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PengelolaDistribusi {
    private final List<Pengiriman> daftarPengiriman = new ArrayList<>();

    public void tambahPengiriman(Pengiriman pengiriman) {
        if (pengiriman == null) {
            throw new IllegalArgumentException("Objek pengiriman tidak boleh null.");
        }
        if (cariPengiriman(pengiriman.getIdPengiriman()) != null) {
            throw new IllegalArgumentException("ID Pengiriman '" + pengiriman.getIdPengiriman() + "' sudah terdaftar.");
        }
        daftarPengiriman.add(pengiriman);
    }

    public Pengiriman cariPengiriman(String idPengiriman) {
        if (idPengiriman == null || idPengiriman.trim().isEmpty()) {
            return null;
        }
        for (Pengiriman p : daftarPengiriman) {
            if (p.getIdPengiriman().equalsIgnoreCase(idPengiriman.trim())) {
                return p;
            }
        }
        return null;
    }

    public boolean hapusPengiriman(String idPengiriman) {
        Pengiriman p = cariPengiriman(idPengiriman);
        if (p == null) {
            return false;
        }
        return daftarPengiriman.remove(p);
    }

    public void tampilkanPengiriman() {
        if (daftarPengiriman.isEmpty()) {
            System.out.println("Koleksi data pengiriman distribusi masih kosong.");
            return;
        }
        System.out.println("=== DAFTAR PENGIRIMAN DISTRIBUSI MBG ===");
        for (Pengiriman p : daftarPengiriman) {
            System.out.println(p.getIdPengiriman() + " -> " + p.getSekolahTujuan().getNamaSekolah()
                    + " | Menu: " + p.getMenuDikirim().getNamaMenu()
                    + " | Porsi: " + p.getJumlahPorsi()
                    + " | Status: " + p.getStatusDistribusi()
                    + " | Armada: " + p.getDriverArmada().getNomorArmada());
        }
        System.out.println("Total pengiriman: " + daftarPengiriman.size());
    }

    public int getJumlahPengiriman() {
        return daftarPengiriman.size();
    }

    public boolean isEmpty() {
        return daftarPengiriman.isEmpty();
    }

    public List<Pengiriman> getDaftarPengiriman() {
        return Collections.unmodifiableList(daftarPengiriman);
    }

    public void kosongkan() {
        daftarPengiriman.clear();
    }
}

