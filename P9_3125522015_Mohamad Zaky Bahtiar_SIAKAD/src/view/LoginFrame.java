package view;

import database.DatabaseConnection;
import model.Dosen;
import model.Mahasiswa;
import service.AkademikService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {

    private final List<Mahasiswa> daftarMahasiswa;
    private JComboBox<MahasiswaItem> cmbMahasiswa;
    private JLabel lblNama;
    private JLabel lblNim;
    private JLabel lblSemester;
    private JLabel lblDosenPA;
    private JLabel lblDbStatus;

    public static class MahasiswaItem {
        public final Mahasiswa mahasiswa;
        public final Dosen dosenPA;

        public MahasiswaItem(Mahasiswa m, Dosen d) {
            this.mahasiswa = m;
            this.dosenPA = d;
        }

        @Override
        public String toString() {
            return mahasiswa.getNim() + " - " + mahasiswa.getNama();
        }
    }

    public LoginFrame() {
        super("Autentikasi Pengguna - SIAKAD PENS PSDKU Sumenep");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        this.daftarMahasiswa = AkademikService.getDaftarMahasiswaDatabase();

        inisialisasiUI();
    }

    private void inisialisasiUI() {
        setSize(500, 420);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 14));
        mainPanel.setBackground(new Color(0xF8, 0xFA, 0xFC)); // Soft Off-White
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // 1. Header Gate
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel headerText = new JPanel(new GridLayout(3, 1, 0, 2));
        headerText.setOpaque(false);

        JLabel lblInstansi = new JLabel("PENS PSDKU SUMENEP • TEKNIK INFORMATIKA");
        lblInstansi.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblInstansi.setForeground(new Color(0x33, 0x41, 0x55)); // Slate Grey

        JLabel lblTitle = new JLabel("Portal Akademik Mahasiswa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0x0F, 0x17, 0x2A)); // Deep Charcoal Navy

        JLabel lblSub = new JLabel("Pilih akun mahasiswa terdaftar untuk membuka modul KRS");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(0x64, 0x74, 0x8B));

        headerText.add(lblInstansi);
        headerText.add(lblTitle);
        headerText.add(lblSub);

        boolean dbOk = DatabaseConnection.isConnected();
        lblDbStatus = new JLabel(dbOk ? "[MYSQL: AKTIF]" : "[IN-MEMORY]");
        lblDbStatus.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblDbStatus.setOpaque(true);
        lblDbStatus.setBackground(dbOk ? new Color(0xEC, 0xFD, 0xF5) : new Color(0xF1, 0xF5, 0xF9));
        lblDbStatus.setForeground(dbOk ? new Color(0x04, 0x78, 0x57) : new Color(0x64, 0x74, 0x8B));
        lblDbStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(dbOk ? new Color(0xA7, 0xF3, 0xD0) : new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));

        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        badgeWrap.setOpaque(false);
        badgeWrap.add(lblDbStatus);

        headerPanel.add(headerText, BorderLayout.WEST);
        headerPanel.add(badgeWrap, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. Center Card: Pemilihan Akun & Detail Profil
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);

        // Dropdown Pilihan Akun
        JPanel comboPanel = new JPanel(new BorderLayout(0, 4));
        comboPanel.setOpaque(false);

        JLabel lblSelect = new JLabel("PILIH IDENTITAS MAHASISWA:");
        lblSelect.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSelect.setForeground(new Color(0x33, 0x41, 0x55));

        cmbMahasiswa = new JComboBox<>();
        cmbMahasiswa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbMahasiswa.setPreferredSize(new Dimension(100, 34));
        cmbMahasiswa.setBackground(Color.WHITE);
        cmbMahasiswa.setForeground(new Color(0x0F, 0x17, 0x2A));

        for (Mahasiswa m : daftarMahasiswa) {
            Dosen d = AkademikService.getDosenByMahasiswa(m.getNim());
            cmbMahasiswa.addItem(new MahasiswaItem(m, d));
        }

        cmbMahasiswa.addActionListener(e -> perbaruiDetailProfil());
        comboPanel.add(lblSelect, BorderLayout.NORTH);
        comboPanel.add(cmbMahasiswa, BorderLayout.CENTER);
        centerPanel.add(comboPanel, BorderLayout.NORTH);

        // Card Detail Mahasiswa Terpilih
        JPanel cardDetail = new JPanel(new GridLayout(4, 1, 0, 4));
        cardDetail.setBackground(Color.WHITE);
        cardDetail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        lblNama = new JLabel("Nama Mahasiswa: -");
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNama.setForeground(new Color(0x0F, 0x17, 0x2A));

        lblNim = new JLabel("NIM: -");
        lblNim.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNim.setForeground(new Color(0x33, 0x41, 0x55));

        lblSemester = new JLabel("Semester / Beban SKS Maks: -");
        lblSemester.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSemester.setForeground(new Color(0x33, 0x41, 0x55));

        lblDosenPA = new JLabel("Dosen Pembimbing Akademik: -");
        lblDosenPA.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDosenPA.setForeground(new Color(0x04, 0x78, 0x57));

        cardDetail.add(lblNama);
        cardDetail.add(lblNim);
        cardDetail.add(lblSemester);
        cardDetail.add(lblDosenPA);

        centerPanel.add(cardDetail, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 3. Tombol Aksi Login & Keluar
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setOpaque(false);

        JButton btnMasuk = new JButton("[ MASUK KE PORTAL ]");
        btnMasuk.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnMasuk.setBackground(new Color(0x0A, 0x19, 0x2F)); // Deep Navy solid
        btnMasuk.setForeground(Color.WHITE); // Teks putih pekat
        btnMasuk.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMasuk.setOpaque(true);
        btnMasuk.setContentAreaFilled(true);
        btnMasuk.setBorderPainted(false);
        btnMasuk.setFocusPainted(false);
        btnMasuk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMasuk.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btnMasuk.addActionListener(e -> masukKePortal());

        JButton btnKeluar = new JButton("[ KELUAR ]");
        btnKeluar.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnKeluar.setBackground(Color.WHITE); // Latar putih murni
        btnKeluar.setForeground(new Color(0x0F, 0x17, 0x2A)); // Teks Slate pekat
        btnKeluar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKeluar.setOpaque(true);
        btnKeluar.setContentAreaFilled(true);
        btnKeluar.setBorderPainted(true);
        btnKeluar.setFocusPainted(false);
        btnKeluar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKeluar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x94, 0xA3, 0xB8), 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        btnKeluar.addActionListener(e -> System.exit(0));

        bottomPanel.add(btnMasuk);
        bottomPanel.add(btnKeluar);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Muat profil pertama
        perbaruiDetailProfil();
    }

    private void perbaruiDetailProfil() {
        MahasiswaItem selected = (MahasiswaItem) cmbMahasiswa.getSelectedItem();
        if (selected != null) {
            Mahasiswa m = selected.mahasiswa;
            Dosen d = selected.dosenPA;

            lblNama.setText(m.getNama());
            lblNim.setText("Nomor Induk Mahasiswa (NIM): " + m.getNim() + " • Status: [" + m.getStatus() + "]");
            lblSemester.setText("Semester " + m.getSemester() + " • Batas Beban: " + m.getMaxSKS() + " SKS");
            lblDosenPA.setText("Dosen Pembimbing (PA): " + (d != null ? d.getNama() : "-"));
        }
    }

    private void masukKePortal() {
        MahasiswaItem selected = (MahasiswaItem) cmbMahasiswa.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih salah satu akun mahasiswa terlebih dahulu.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nim = selected.mahasiswa.getNim();

        // Inisialisasi Service Layer untuk mahasiswa yang dipilih
        AkademikService service = new AkademikService(nim);

        // Tutup Login Frame dan Buka Portal SiakadFrame
        this.dispose();

        SwingUtilities.invokeLater(() -> {
            try {
                SiakadFrame app = new SiakadFrame(service);
                app.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Gagal meluncurkan SIAKAD: " + ex.getMessage(),
                        "Kesalahan Sistem",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }
}
