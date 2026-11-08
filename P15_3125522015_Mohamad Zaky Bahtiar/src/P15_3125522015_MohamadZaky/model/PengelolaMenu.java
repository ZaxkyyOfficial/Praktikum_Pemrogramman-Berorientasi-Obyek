package P15_3125522015_MohamadZaky.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PengelolaMenu {
    private final List<Menu> daftarMenu = new ArrayList<>();

    public void tambahMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Objek menu tidak boleh null.");
        }
        if (cariMenu(menu.getKodeMenu()) != null) {
            throw new IllegalArgumentException("Kode menu '" + menu.getKodeMenu() + "' sudah terdaftar.");
        }
        daftarMenu.add(menu);
    }

    public Menu cariMenu(String kodeMenu) {
        if (kodeMenu == null || kodeMenu.trim().isEmpty()) {
            return null;
        }
        for (Menu menu : daftarMenu) {
            if (menu.getKodeMenu().equalsIgnoreCase(kodeMenu.trim())) {
                return menu;
            }
        }
        return null;
    }

    public boolean hapusMenu(String kodeMenu) {
        Menu menu = cariMenu(kodeMenu);
        if (menu == null) {
            return false;
        }
        return daftarMenu.remove(menu);
    }

    public void tampilkanMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("Koleksi menu MBG masih kosong.");
            return;
        }
        System.out.println("=== DAFTAR MENU GIZI MBG ===");
        for (Menu menu : daftarMenu) {
            System.out.println(menu.getKodeMenu() + " - " + menu.getNamaMenu()
                    + " | Kalori: " + menu.getKaloriPerPorsi() + " kkal"
                    + " | Protein: " + menu.getProteinGram() + "g"
                    + " | Biaya: Rp " + (long) menu.getBiayaPerPorsi());
        }
        System.out.println("Total menu terdaftar: " + daftarMenu.size());
    }

    public int getJumlahMenu() {
        return daftarMenu.size();
    }

    public boolean isEmpty() {
        return daftarMenu.isEmpty();
    }

    public List<Menu> getDaftarMenu() {
        return Collections.unmodifiableList(daftarMenu);
    }

    public void kosongkan() {
        daftarMenu.clear();
    }

    public void gantiSemua(List<Menu> listBaru) {
        if (listBaru == null) {
            throw new IllegalArgumentException("Daftar menu baru tidak boleh null.");
        }
        daftarMenu.clear();
        daftarMenu.addAll(listBaru);
    }

    public void simpan(Path lokasi) throws IOException {
        new P15_3125522015_MohamadZaky.utils.MbgFileRepository(this).simpan(lokasi);
    }

    public void muat(Path lokasi) throws IOException {
        new P15_3125522015_MohamadZaky.utils.MbgFileRepository(this).muat(lokasi);
    }
}

