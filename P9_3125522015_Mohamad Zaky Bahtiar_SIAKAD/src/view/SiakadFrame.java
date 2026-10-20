package view;

import model.Dosen;
import model.KRS;
import model.Mahasiswa;
import model.MataKuliah;
import service.AkademikService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SiakadFrame extends JFrame {

    public static final Color COLOR_BG = new Color(0xF8, 0xFA, 0xFC); // Soft Off-White / Paper Ivory
    public static final Color COLOR_CARD = new Color(0xFF, 0xFF, 0xFF); // Pure White
    public static final Color COLOR_BORDER = new Color(0xE2, 0xE8, 0xF0); // Soft Neutral Gray Border
    public static final Color COLOR_TEXT_MAIN = new Color(0x0F, 0x17, 0x2A); // Deep Charcoal Navy (Header/Utama)
    public static final Color COLOR_TEXT_SUB = new Color(0x33, 0x41, 0x55); // Slate Grey Pekat Bold (Sub-header)
    public static final Color COLOR_TEXT_MUTED = new Color(0x64, 0x74, 0x8B); // Slate Muted Gray
    public static final Color COLOR_SLATE_BORDER = new Color(0x94, 0xA3, 0xB8); // Slate Border for Outlined Buttons

    public static final Color COLOR_NAVY = new Color(0x0A, 0x19, 0x2F); // Deep Navy Primary Solid
    public static final Color COLOR_NAVY_HOVER = new Color(0x1E, 0x29, 0x3B);

    public static final Color COLOR_BRONZE = new Color(0x92, 0x40, 0x0E); // Antique Bronze Gold Gelap (Aksen Mewah)
    public static final Color COLOR_BRONZE_BG = new Color(0xFE, 0xF3, 0xC7); // Soft Amber/Bronze Tint
    public static final Color COLOR_BRONZE_BORDER = new Color(0xFD, 0xBA, 0x74);

    public static final Color COLOR_EMERALD = new Color(0x04, 0x78, 0x57); // Classic Emerald Green
    public static final Color COLOR_EMERALD_BG = new Color(0xEC, 0xFD, 0xF5);
    public static final Color COLOR_EMERALD_BORDER = new Color(0xA7, 0xF3, 0xD0);

    public static final Color COLOR_RED = new Color(0xDC, 0x26, 0x26); // Crimson Red
    public static final Color COLOR_RED_BG = new Color(0xFE, 0xF2, 0xF2);
    public static final Color COLOR_RED_BORDER = new Color(0xFE, 0xCA, 0xCA);

    public static final Color COLOR_DISABLED_BG = new Color(0xF1, 0xF5, 0xF9);
    public static final Color COLOR_DISABLED_TEXT = new Color(0x64, 0x74, 0x8B);

    public static final Font FONT_DISPLAY = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_DISPLAY_SUB = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TEXT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_TEXT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_LOG = new Font("Consolas", Font.PLAIN, 11);

    private final AkademikService service;

    // Komponen UI Dinamis
    private JLabel lblNamaMhs;
    private JLabel lblNimMhs;
    private JLabel lblDosenPA;
    private JLabel lblNipDosen;
    private JLabel lblSksText;
    private JProgressBar progressSks;
    private JLabel lblKrsStatus;
    private JLabel lblKrsTotalSks;
    private JLabel lblNotifikasi;

    private JTextField txtSearchKatalog;
    private JTable tblKatalog;
    private DefaultTableModel modelKatalog;

    private JTable tblKrs;
    private DefaultTableModel modelKrs;

    private ActionButton btnAjukan;
    private ActionButton btnSetujui;
    private ActionButton btnCetakKrs;
    private ActionButton btnBatalSetujui;
    private ActionButton btnResetKrs;
    private ActionButton btnGantiAkun;

    private JTextArea txtAuditLog;

    public SiakadFrame() {
        this(new AkademikService());
    }

    public SiakadFrame(AkademikService service) {
        super("SIAKAD - Politeknik Elektronika Negeri Surabaya (PENS PSDKU Sumenep)");
        this.service = service;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        inisialisasiUI();
        muatUlangData();
    }

    private void inisialisasiUI() {
        setSize(1280, 800);
        setMinimumSize(new Dimension(1100, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 14));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(16, 22, 16, 22));

        // 1. Top Section (Header + Toast Notifikasi + Profil Mahasiswa)
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);
        topContainer.add(buatHeaderBar(), BorderLayout.NORTH);

        lblNotifikasi = new JLabel(" ");
        lblNotifikasi.setFont(FONT_TEXT_BOLD);
        lblNotifikasi.setOpaque(true);
        lblNotifikasi.setBackground(COLOR_CARD);
        lblNotifikasi.setForeground(COLOR_TEXT_MAIN);
        lblNotifikasi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        lblNotifikasi.setVisible(false);
        topContainer.add(lblNotifikasi, BorderLayout.CENTER);

        topContainer.add(buatPanelProfil(), BorderLayout.SOUTH);
        mainPanel.add(topContainer, BorderLayout.NORTH);

        // 2. Middle Section (Katalog MK kiri & KRS Kanan)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buatPanelKatalog(), buatPanelKRS());
        splitPane.setResizeWeight(0.55);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // 3. Bottom Section (Audit Log Panel)
        mainPanel.add(buatPanelAuditLog(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel buatHeaderBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_CARD);
        // Garis aksen tipis Antique Bronze Gold di atas panel header
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 1, 1, 1, COLOR_BRONZE),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));

        JPanel leftBlock = new JPanel(new GridLayout(3, 1, 0, 2));
        leftBlock.setOpaque(false);

        JLabel lblInstansi = new JLabel("PENS PSDKU SUMENEP • DEPARTEMEN TEKNIK INFORMATIKA");
        lblInstansi.setFont(FONT_TEXT_SMALL_BOLD);
        lblInstansi.setForeground(COLOR_TEXT_SUB); // Slate Grey pekat bold, kontras tinggi

        JLabel lblJudul = new JLabel("Sistem Informasi Akademik (SIAKAD)");
        lblJudul.setFont(FONT_DISPLAY);
        lblJudul.setForeground(COLOR_TEXT_MAIN); // Deep Charcoal Navy

        JLabel lblSub = new JLabel("Praktikum Pemrograman Berorientasi Obyek • Modul 9 (UML Class to Java Desktop)");
        lblSub.setFont(FONT_TEXT);
        lblSub.setForeground(COLOR_TEXT_MUTED);

        leftBlock.add(lblInstansi);
        leftBlock.add(lblJudul);
        leftBlock.add(lblSub);

        JPanel rightBlock = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightBlock.setOpaque(false);

        boolean dbConnected = service.isDatabaseConnected();
        JLabel badgeStatus = buatBadge("[SISTEM: AKTIF]", COLOR_BRONZE_BG, COLOR_BRONZE_BORDER, COLOR_BRONZE);
        JLabel badgeMode = buatBadge(
                dbConnected ? "[DATABASE: MYSQL XAMPP]" : "[MODE: IN-MEMORY FALLBACK]",
                dbConnected ? COLOR_EMERALD_BG : COLOR_DISABLED_BG,
                dbConnected ? COLOR_EMERALD_BORDER : COLOR_BORDER,
                dbConnected ? COLOR_EMERALD : COLOR_TEXT_MAIN
        );

        rightBlock.add(badgeStatus);
        rightBlock.add(badgeMode);

        header.add(leftBlock, BorderLayout.WEST);
        header.add(rightBlock, BorderLayout.EAST);
        return header;
    }

    private JPanel buatPanelProfil() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(COLOR_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));

        // Kolom 1: Mahasiswa
        JPanel col1 = new JPanel(new GridLayout(3, 1, 0, 2));
        col1.setOpaque(false);
        JLabel l1 = new JLabel("MAHASISWA TERDAFTAR");
        l1.setFont(FONT_TEXT_SMALL_BOLD);
        l1.setForeground(COLOR_TEXT_SUB);

        lblNamaMhs = new JLabel("-");
        lblNamaMhs.setFont(FONT_DISPLAY_SUB);
        lblNamaMhs.setForeground(COLOR_TEXT_MAIN);

        lblNimMhs = new JLabel("-");
        lblNimMhs.setFont(FONT_TEXT);
        lblNimMhs.setForeground(COLOR_TEXT_SUB);

        col1.add(l1);
        col1.add(lblNamaMhs);
        col1.add(lblNimMhs);

        // Kolom 2: Dosen PA
        JPanel col2 = new JPanel(new GridLayout(3, 1, 0, 2));
        col2.setOpaque(false);
        JLabel l2 = new JLabel("DOSEN PEMBIMBING AKADEMIK (PA)");
        l2.setFont(FONT_TEXT_SMALL_BOLD);
        l2.setForeground(COLOR_TEXT_SUB);

        lblDosenPA = new JLabel("-");
        lblDosenPA.setFont(FONT_DISPLAY_SUB);
        lblDosenPA.setForeground(COLOR_TEXT_MAIN);

        lblNipDosen = new JLabel("-");
        lblNipDosen.setFont(FONT_TEXT);
        lblNipDosen.setForeground(COLOR_TEXT_SUB);

        col2.add(l2);
        col2.add(lblDosenPA);
        col2.add(lblNipDosen);

        // Kolom 3: Alokasi Beban SKS Meter
        JPanel col3 = new JPanel(new BorderLayout(0, 8));
        col3.setOpaque(false);

        JPanel meterHeader = new JPanel(new BorderLayout());
        meterHeader.setOpaque(false);
        JLabel l3 = new JLabel("ALOKASI BEBAN SKS");
        l3.setFont(FONT_TEXT_SMALL_BOLD);
        l3.setForeground(COLOR_TEXT_SUB);

        lblSksText = new JLabel("0 / 24 SKS (0%)");
        lblSksText.setFont(FONT_TEXT_BOLD);
        lblSksText.setForeground(COLOR_TEXT_MAIN);

        meterHeader.add(l3, BorderLayout.WEST);
        meterHeader.add(lblSksText, BorderLayout.EAST);

        progressSks = new JProgressBar(0, 24);
        progressSks.setValue(0);
        progressSks.setPreferredSize(new Dimension(100, 10));
        progressSks.setForeground(COLOR_NAVY);
        progressSks.setBackground(COLOR_BORDER);
        progressSks.setBorder(null);

        // Custom Renderer Progress Bar: Solid Deep Navy / Classic Emerald tanpa silau
        progressSks.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = c.getWidth();
                int height = c.getHeight();

                // Track Abu-Abu Terang
                g2.setColor(COLOR_BORDER);
                g2.fillRoundRect(0, 0, width, height, 4, 4);

                // Progress Bar Solid
                double percent = progressSks.getPercentComplete();
                int fillWidth = (int) Math.round(width * percent);
                if (fillWidth > 0) {
                    g2.setColor(progressSks.getForeground());
                    g2.fillRoundRect(0, 0, fillWidth, height, 4, 4);
                }
                g2.dispose();
            }
        });

        col3.add(meterHeader, BorderLayout.NORTH);
        col3.add(progressSks, BorderLayout.CENTER);

        panel.add(col1);
        panel.add(col2);
        panel.add(col3);
        return panel;
    }

    private JPanel buatPanelKatalog() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        // Header Panel
        JPanel topHeader = new JPanel(new BorderLayout(10, 0));
        topHeader.setOpaque(false);

        JLabel lblTitle = new JLabel("Katalog Mata Kuliah Tersedia");
        lblTitle.setFont(FONT_DISPLAY_SUB);
        lblTitle.setForeground(COLOR_TEXT_MAIN);
        topHeader.add(lblTitle, BorderLayout.WEST);

        txtSearchKatalog = new JTextField();
        txtSearchKatalog.setFont(FONT_TEXT);
        txtSearchKatalog.setPreferredSize(new Dimension(240, 30));
        txtSearchKatalog.setForeground(COLOR_TEXT_MAIN);
        txtSearchKatalog.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_SLATE_BORDER, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        txtSearchKatalog.putClientProperty("JTextField.placeholderText", "Cari Kode atau Nama...");
        txtSearchKatalog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterKatalog(txtSearchKatalog.getText());
            }
        });

        JPanel searchWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        searchWrap.setOpaque(false);
        JLabel lblCari = new JLabel("Cari: ");
        lblCari.setFont(FONT_TEXT_BOLD);
        lblCari.setForeground(COLOR_TEXT_SUB);
        searchWrap.add(lblCari);
        searchWrap.add(txtSearchKatalog);
        topHeader.add(searchWrap, BorderLayout.EAST);

        panel.add(topHeader, BorderLayout.NORTH);

        // Tabel Katalog MK
        String[] columns = {"Kode", "Nama Mata Kuliah", "SKS", "Kapasitas", "Status", "Aksi"};
        modelKatalog = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Hanya kolom tombol aksi yang interaktif
            }
        };

        tblKatalog = new JTable(modelKatalog);
        tblKatalog.setRowHeight(36); // Minimal 34 piksel
        tblKatalog.setFont(FONT_TEXT);
        tblKatalog.setSelectionBackground(COLOR_DISABLED_BG);
        tblKatalog.setSelectionForeground(COLOR_TEXT_MAIN);
        tblKatalog.setShowGrid(true);
        tblKatalog.setGridColor(COLOR_BORDER);

        // Table Header Styling
        JTableHeader th = tblKatalog.getTableHeader();
        th.setFont(FONT_TEXT_BOLD);
        th.setBackground(COLOR_BG);
        th.setForeground(COLOR_TEXT_MAIN);
        th.setPreferredSize(new Dimension(th.getWidth(), 36));
        th.setReorderingAllowed(false);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        tblKatalog.getColumnModel().getColumn(0).setPreferredWidth(65);
        tblKatalog.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblKatalog.getColumnModel().getColumn(2).setPreferredWidth(45);
        tblKatalog.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblKatalog.getColumnModel().getColumn(4).setPreferredWidth(95);
        tblKatalog.getColumnModel().getColumn(5).setPreferredWidth(95);

        // Custom Center & Badge Renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblKatalog.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblKatalog.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblKatalog.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblKatalog.getColumnModel().getColumn(4).setCellRenderer(new BadgeCellRenderer());

        // Button Column Renderer & Editor
        tblKatalog.getColumnModel().getColumn(5).setCellRenderer(new TableActionButtonRenderer());
        tblKatalog.getColumnModel().getColumn(5).setCellEditor(new TableActionButtonEditor(kodeMK -> ambilMataKuliah(kodeMK)));

        JScrollPane scroll = new JScrollPane(tblKatalog);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(COLOR_CARD);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buatPanelKRS() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        // Header Panel
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);

        JLabel lblTitle = new JLabel("Kartu Rencana Studi (KRS)");
        lblTitle.setFont(FONT_DISPLAY_SUB);
        lblTitle.setForeground(COLOR_TEXT_MAIN);

        lblKrsStatus = buatBadge("[DRAFT]", COLOR_DISABLED_BG, COLOR_BORDER, COLOR_TEXT_SUB);

        topHeader.add(lblTitle, BorderLayout.WEST);
        topHeader.add(lblKrsStatus, BorderLayout.EAST);
        panel.add(topHeader, BorderLayout.NORTH);

        // Tabel KRS
        String[] columns = {"Kode", "Mata Kuliah Terpilih", "SKS", "Aksi"};
        modelKrs = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        tblKrs = new JTable(modelKrs);
        tblKrs.setRowHeight(36); // Minimal 34 piksel
        tblKrs.setFont(FONT_TEXT);
        tblKrs.setSelectionBackground(COLOR_DISABLED_BG);
        tblKrs.setSelectionForeground(COLOR_TEXT_MAIN);
        tblKrs.setShowGrid(true);
        tblKrs.setGridColor(COLOR_BORDER);

        JTableHeader th = tblKrs.getTableHeader();
        th.setFont(FONT_TEXT_BOLD);
        th.setBackground(COLOR_BG);
        th.setForeground(COLOR_TEXT_MAIN);
        th.setPreferredSize(new Dimension(th.getWidth(), 36));
        th.setReorderingAllowed(false);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        tblKrs.getColumnModel().getColumn(0).setPreferredWidth(65);
        tblKrs.getColumnModel().getColumn(1).setPreferredWidth(230);
        tblKrs.getColumnModel().getColumn(2).setPreferredWidth(45);
        tblKrs.getColumnModel().getColumn(3).setPreferredWidth(100);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblKrs.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblKrs.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        tblKrs.getColumnModel().getColumn(3).setCellRenderer(new TableActionButtonRenderer());
        tblKrs.getColumnModel().getColumn(3).setCellEditor(new TableActionButtonEditor(kodeMK -> batalkanMataKuliah(kodeMK)));

        JScrollPane scroll = new JScrollPane(tblKrs);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(COLOR_CARD);
        panel.add(scroll, BorderLayout.CENTER);

        // Bottom Summary & Actions
        JPanel bottomWrap = new JPanel(new BorderLayout(0, 10));
        bottomWrap.setOpaque(false);

        // Summary Box
        JPanel summaryBox = new JPanel(new BorderLayout());
        summaryBox.setBackground(COLOR_BG);
        summaryBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        JLabel lTotal = new JLabel("TOTAL BEBAN SKS");
        lTotal.setFont(FONT_TEXT_SMALL_BOLD);
        lTotal.setForeground(COLOR_TEXT_SUB);

        lblKrsTotalSks = new JLabel("0 SKS");
        lblKrsTotalSks.setFont(FONT_DISPLAY);
        lblKrsTotalSks.setForeground(COLOR_TEXT_MAIN);

        summaryBox.add(lTotal, BorderLayout.WEST);
        summaryBox.add(lblKrsTotalSks, BorderLayout.EAST);
        bottomWrap.add(summaryBox, BorderLayout.NORTH);

        // Action Buttons Row (3x2 Grid)
        JPanel btnRow = new JPanel(new GridLayout(3, 2, 8, 8));
        btnRow.setOpaque(false);

        btnAjukan = new ActionButton("[ AJUKAN KRS ]", ActionButton.ButtonStyle.PRIMARY);
        btnAjukan.addActionListener(e -> ajukanKRS());

        btnSetujui = new ActionButton("[ VERIFIKASI DOSEN PA ]", ActionButton.ButtonStyle.PRIMARY);
        btnSetujui.addActionListener(e -> setujuiKRS());

        btnCetakKrs = new ActionButton("[ CETAK KRS RESMI ]", ActionButton.ButtonStyle.EMERALD);
        btnCetakKrs.addActionListener(e -> bukaCetakKRS());

        btnBatalSetujui = new ActionButton("[ BATALKAN VERIFIKASI ]", ActionButton.ButtonStyle.DANGER_OUTLINE);
        btnBatalSetujui.addActionListener(e -> batalkanPersetujuan());

        btnResetKrs = new ActionButton("[ RESET KRS ]", ActionButton.ButtonStyle.SECONDARY);
        btnResetKrs.addActionListener(e -> resetKRS());

        btnGantiAkun = new ActionButton("[ GANTI AKUN / KELUAR ]", ActionButton.ButtonStyle.SECONDARY);
        btnGantiAkun.addActionListener(e -> gantiAkun());

        btnRow.add(btnAjukan);
        btnRow.add(btnSetujui);
        btnRow.add(btnCetakKrs);
        btnRow.add(btnBatalSetujui);
        btnRow.add(btnResetKrs);
        btnRow.add(btnGantiAkun);

        bottomWrap.add(btnRow, BorderLayout.SOUTH);
        panel.add(bottomWrap, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatPanelAuditLog() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(COLOR_NAVY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        panel.setPreferredSize(new Dimension(100, 130));

        JPanel logHeader = new JPanel(new BorderLayout());
        logHeader.setOpaque(false);
        JLabel lblLogTitle = new JLabel("TERMINAL AUDIT LOG • OOP STATE VALIDATION TRACE");
        lblLogTitle.setFont(FONT_TEXT_SMALL_BOLD);
        lblLogTitle.setForeground(COLOR_BRONZE_BORDER); // Antique Bronze / Amber Accent

        JButton btnClear = new JButton("[ Kosongkan Log ]");
        btnClear.setFont(FONT_TEXT_SMALL_BOLD);
        btnClear.setFocusPainted(false);
        btnClear.setContentAreaFilled(false);
        btnClear.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_SLATE_BORDER, 1),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        btnClear.setForeground(COLOR_BORDER);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.addActionListener(e -> txtAuditLog.setText(""));

        logHeader.add(lblLogTitle, BorderLayout.WEST);
        logHeader.add(btnClear, BorderLayout.EAST);
        panel.add(logHeader, BorderLayout.NORTH);

        txtAuditLog = new JTextArea();
        txtAuditLog.setFont(FONT_LOG);
        txtAuditLog.setBackground(new Color(0x06, 0x0D, 0x17));
        txtAuditLog.setForeground(new Color(0xE2, 0xE8, 0xF0));
        txtAuditLog.setEditable(false);
        txtAuditLog.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(txtAuditLog);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JLabel buatBadge(String text, Color bg, Color border, Color fg) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TEXT_SMALL_BOLD);
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setForeground(fg);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        return lbl;
    }

    public void muatUlangData() {
        Mahasiswa m = service.getMahasiswa();
        Dosen d = service.getDosenPembimbing();
        KRS krs = service.getKrsAktif();

        // 1. Profil Info
        lblNamaMhs.setText(m.getNama());
        lblNimMhs.setText("NIM: " + m.getNim() + " • Semester " + m.getSemester() + " • [" + m.getStatus() + "]");
        lblDosenPA.setText(d.getNama());
        lblNipDosen.setText("NIP: " + d.getNip() + " • Spesialisasi: " + d.getSpesialisasi());

        // 2. SKS Meter
        int totalSks = krs.hitungTotalSKS();
        int maxSks = m.getMaxSKS();
        int pct = (int) Math.round(((double) totalSks / maxSks) * 100);
        lblSksText.setText(totalSks + " / " + maxSks + " SKS (" + pct + "%)");
        progressSks.setValue(totalSks);

        if (totalSks >= maxSks) {
            progressSks.setForeground(COLOR_RED);
        } else {
            progressSks.setForeground(COLOR_NAVY);
        }

        // 3. Status KRS & Badge High-Contrast
        KRS.StatusKRS status = krs.getStatus();
        lblKrsStatus.setText("[" + status.name() + "]");
        if (status == KRS.StatusKRS.DISETUJUI) {
            lblKrsStatus.setBackground(COLOR_EMERALD_BG);
            lblKrsStatus.setForeground(COLOR_EMERALD);
            lblKrsStatus.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_EMERALD_BORDER, 1),
                    BorderFactory.createEmptyBorder(2, 8, 2, 8)
            ));
        } else if (status == KRS.StatusKRS.DIAJUKAN) {
            lblKrsStatus.setBackground(COLOR_BRONZE_BG);
            lblKrsStatus.setForeground(COLOR_BRONZE);
            lblKrsStatus.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BRONZE_BORDER, 1),
                    BorderFactory.createEmptyBorder(2, 8, 2, 8)
            ));
        } else {
            lblKrsStatus.setBackground(COLOR_DISABLED_BG);
            lblKrsStatus.setForeground(COLOR_TEXT_SUB);
            lblKrsStatus.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDER, 1),
                    BorderFactory.createEmptyBorder(2, 8, 2, 8)
            ));
        }

        lblKrsTotalSks.setText(totalSks + " SKS");

        // 4. Button State Machine
        boolean isDraft = (status == KRS.StatusKRS.DRAFT);
        boolean isDiajukan = (status == KRS.StatusKRS.DIAJUKAN);
        boolean isDisetujui = (status == KRS.StatusKRS.DISETUJUI);

        btnAjukan.setText("[ AJUKAN KRS ]");
        btnSetujui.setText("[ VERIFIKASI DOSEN PA ]");
        btnCetakKrs.setText("[ CETAK KRS RESMI ]");
        btnBatalSetujui.setText("[ BATALKAN VERIFIKASI ]");
        btnResetKrs.setText("[ RESET KRS ]");
        btnGantiAkun.setText("[ GANTI AKUN / KELUAR ]");

        btnAjukan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSetujui.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCetakKrs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBatalSetujui.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnResetKrs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGantiAkun.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnAjukan.setEnabled(isDraft && !krs.getMataKuliahList().isEmpty());
        btnSetujui.setEnabled(isDiajukan);
        btnCetakKrs.setEnabled(isDisetujui);
        btnBatalSetujui.setEnabled(isDisetujui);
        btnResetKrs.setEnabled(isDraft && !krs.getMataKuliahList().isEmpty());
        btnGantiAkun.setEnabled(true);

        // 5. Muat Tabel KRS
        modelKrs.setRowCount(0);
        for (MataKuliah mk : krs.getMataKuliahList()) {
            modelKrs.addRow(new Object[]{
                    mk.getKodeMK(),
                    mk.getNamaMK(),
                    mk.getSks(),
                    isDraft ? "[ BATAL ]" : "[ TERKUNCI ]"
            });
        }

        // 6. Muat Tabel Katalog
        filterKatalog(txtSearchKatalog != null ? txtSearchKatalog.getText() : "");

        // 7. Muat Audit Logs
        StringBuilder sb = new StringBuilder();
        List<String> logs = service.getAuditLogs();
        for (String log : logs) {
            sb.append(log).append("\n");
        }
        txtAuditLog.setText(sb.toString());
        txtAuditLog.setCaretPosition(txtAuditLog.getDocument().getLength());
    }

    private void filterKatalog(String query) {
        if (query == null) query = "";
        String q = query.trim().toLowerCase();
        modelKatalog.setRowCount(0);

        KRS krs = service.getKrsAktif();
        List<MataKuliah> krsItems = krs.getMataKuliahList();
        boolean isDraft = (krs.getStatus() == KRS.StatusKRS.DRAFT);

        for (MataKuliah mk : service.getKatalogMataKuliah()) {
            if (!q.isEmpty() && !mk.getKodeMK().toLowerCase().contains(q) && !mk.getNamaMK().toLowerCase().contains(q)) {
                continue;
            }

            boolean diambil = krsItems.contains(mk);
            boolean penuh = !mk.cekKetersediaan();

            String statusStr;
            String aksiStr;

            if (diambil) {
                statusStr = "[DIAMBIL]";
                aksiStr = "[ TERDAFTAR ]";
            } else if (penuh) {
                statusStr = "[PENUH]";
                aksiStr = "[ PENUH ]";
            } else {
                statusStr = "[TERSEDIA]";
                aksiStr = isDraft ? "[ AMBIL ]" : "[ TERKUNCI ]";
            }

            String kuotaStr = mk.getTerisi() + "/" + mk.getKuota() + " (sisa " + mk.getSisaKuota() + ")";

            modelKatalog.addRow(new Object[]{
                    mk.getKodeMK(),
                    mk.getNamaMK(),
                    mk.getSks(),
                    kuotaStr,
                    statusStr,
                    aksiStr
            });
        }
    }

    private void ambilMataKuliah(String kodeMK) {
        try {
            service.ambilMataKuliah(kodeMK);
            tampilkanNotifikasi("Mata kuliah [" + kodeMK + "] berhasil dialokasikan ke dalam KRS.", false);
        } catch (Exception ex) {
            tampilkanNotifikasi("Gagal mengambil [" + kodeMK + "]: " + ex.getMessage(), true);
        }
        muatUlangData();
    }

    private void batalkanMataKuliah(String kodeMK) {
        try {
            service.batalkanMataKuliah(kodeMK);
            tampilkanNotifikasi("Mata kuliah [" + kodeMK + "] berhasil dibatalkan dari KRS.", false);
        } catch (Exception ex) {
            tampilkanNotifikasi("Gagal membatalkan [" + kodeMK + "]: " + ex.getMessage(), true);
        }
        muatUlangData();
    }

    private void ajukanKRS() {
        try {
            service.ajukanKRS();
            tampilkanNotifikasi("KRS berhasil diajukan untuk verifikasi Dosen Pembimbing.", false);
        } catch (Exception ex) {
            tampilkanNotifikasi("Pengajuan gagal: " + ex.getMessage(), true);
        }
        muatUlangData();
    }

    private void setujuiKRS() {
        try {
            service.setujuiKRS();
            tampilkanNotifikasi("KRS telah diverifikasi dan disetujui resmi oleh Dosen PA.", false);
        } catch (Exception ex) {
            tampilkanNotifikasi("Verifikasi ditolak: " + ex.getMessage(), true);
        }
        muatUlangData();
    }

    private void batalkanPersetujuan() {
        try {
            service.batalkanPersetujuanDosen();
            tampilkanNotifikasi("Persetujuan dibatalkan oleh Dosen. KRS kembali berstatus DRAFT.", false);
        } catch (Exception ex) {
            tampilkanNotifikasi("Gagal membatalkan: " + ex.getMessage(), true);
        }
        muatUlangData();
    }

    private void resetKRS() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin mengosongkan seluruh mata kuliah dan mereset status KRS ke DRAFT?",
                "Konfirmasi Reset KRS",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            service.resetKRS();
            tampilkanNotifikasi("KRS berhasil dikosongkan dan di-reset ke status DRAFT.", false);
            muatUlangData();
        }
    }

    private void bukaCetakKRS() {
        if (service.getKrsAktif().getStatus() != KRS.StatusKRS.DISETUJUI) {
            tampilkanNotifikasi("KRS belum disetujui resmi oleh Dosen Pembimbing. Dokumen tidak dapat dicetak.", true);
            return;
        }

        CetakKRSViewer viewer = new CetakKRSViewer(this, service.getMahasiswa(), service.getDosenPembimbing(), service.getKrsAktif());
        viewer.setVisible(true);
    }

    private void gantiAkun() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda ingin keluar dari sesi mahasiswa ini dan kembali ke menu autentikasi akun?",
                "Konfirmasi Ganti Akun",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            });
        }
    }

    private void tampilkanNotifikasi(String pesan, boolean isError) {
        lblNotifikasi.setText((isError ? "[PENOLAKAN VALIDASI] " : "[KONFIRMASI SUKSES] ") + pesan);
        lblNotifikasi.setForeground(isError ? COLOR_RED : COLOR_TEXT_MAIN);
        lblNotifikasi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isError ? COLOR_RED : COLOR_BRONZE, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        lblNotifikasi.setVisible(true);

        Timer timer = new Timer(5000, e -> lblNotifikasi.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }

    public static class ActionButton extends JButton {
        public enum ButtonStyle {
            PRIMARY, SECONDARY, DANGER_OUTLINE, EMERALD
        }

        private final ButtonStyle style;

        public ActionButton(String text, ButtonStyle style) {
            super(text != null ? text : "");
            this.style = style;
            setUI(new javax.swing.plaf.basic.BasicButtonUI());
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        }

        @Override
        public void setEnabled(boolean b) {
            super.setEnabled(b);
            setCursor(b ? new Cursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int w = getWidth();
            int h = getHeight();

            Color bg;
            Color fg;
            Color border;

            if (!isEnabled()) {
                // Disabled state: Background abu-abu terang, Foreground abu-abu terbaca
                bg = new Color(0xE2, 0xE8, 0xF0);
                fg = new Color(0x94, 0xA3, 0xB8);
                border = new Color(0xE2, 0xE8, 0xF0);
            } else if (style == ButtonStyle.PRIMARY) {
                // Primary Action: Deep Navy + Pure White Text Bold
                bg = getModel().isPressed() ? COLOR_NAVY.darker() : (getModel().isRollover() ? COLOR_NAVY_HOVER : COLOR_NAVY);
                fg = Color.WHITE;
                border = bg;
            } else if (style == ButtonStyle.EMERALD) {
                // Status DISETUJUI: Background Emerald Green pekat, Foreground Color.WHITE solid
                bg = getModel().isPressed() ? new Color(0x03, 0x5e, 0x43) : (getModel().isRollover() ? new Color(0x05, 0x96, 0x69) : new Color(0x04, 0x78, 0x57));
                fg = Color.WHITE;
                border = bg;
            } else if (style == ButtonStyle.DANGER_OUTLINE) {
                // Tombol [ BATALKAN VERIFIKASI ]: Background putih, garis tepi merah bata, teks merah marun pekat
                bg = getModel().isPressed() ? new Color(0xFE, 0xE2, 0xE2) : (getModel().isRollover() ? new Color(0xFE, 0xF2, 0xF2) : Color.WHITE);
                fg = new Color(0x99, 0x1B, 0x1B);
                border = new Color(0xDC, 0x26, 0x26);
            } else {
                // Tombol [ GANTI AKUN / KELUAR ] & [ RESET KRS ]: Background putih, garis tepi abu-abu, teks Deep Slate
                bg = getModel().isPressed() ? new Color(0xF1, 0xF5, 0xF9) : (getModel().isRollover() ? new Color(0xF8, 0xFA, 0xFC) : Color.WHITE);
                fg = new Color(0x0F, 0x17, 0x2A);
                border = new Color(0x94, 0xA3, 0xB8);
            }

            // Fill solid background
            g2.setColor(bg);
            g2.fillRect(0, 0, w, h);

            // Draw border jika berbingkai
            if (border != null && border != bg) {
                g2.setColor(border);
                g2.drawRect(0, 0, w - 1, h - 1);
            }

            // Draw centered text
            String txt = getText();
            if (txt != null && !txt.isEmpty()) {
                g2.setFont(getFont() != null ? getFont() : new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(fg);
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(txt)) / 2;
                int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(txt, textX, textY);
            }

            g2.dispose();
        }
    }

    private static class BadgeCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setFont(FONT_TEXT_SMALL_BOLD);

            String text = String.valueOf(value);
            if (text.contains("TERSEDIA")) {
                lbl.setForeground(COLOR_EMERALD);
                lbl.setBackground(COLOR_EMERALD_BG);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_EMERALD_BORDER, 1),
                        BorderFactory.createEmptyBorder(2, 6, 2, 6)
                ));
            } else if (text.contains("PENUH")) {
                lbl.setForeground(COLOR_RED);
                lbl.setBackground(COLOR_RED_BG);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_RED_BORDER, 1),
                        BorderFactory.createEmptyBorder(2, 6, 2, 6)
                ));
            } else if (text.contains("DIAMBIL")) {
                lbl.setForeground(COLOR_BRONZE);
                lbl.setBackground(COLOR_BRONZE_BG);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BRONZE_BORDER, 1),
                        BorderFactory.createEmptyBorder(2, 6, 2, 6)
                ));
            } else {
                lbl.setForeground(COLOR_TEXT_MAIN);
                lbl.setBackground(COLOR_DISABLED_BG);
                lbl.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            }
            lbl.setOpaque(true);
            return lbl;
        }
    }

    private interface ActionCallback {
        void execute(String kodeMK);
    }

    private static class TableActionButtonRenderer extends JButton implements TableCellRenderer {
        public TableActionButtonRenderer() {
            setUI(new javax.swing.plaf.basic.BasicButtonUI());
            setFont(FONT_TEXT_SMALL_BOLD);
            setFocusPainted(false);
            setContentAreaFilled(true);
            setOpaque(true);
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            String text = getText();
            Color bg, fg, border;

            if ("[ AMBIL ]".equals(text)) {
                bg = COLOR_NAVY;
                fg = Color.WHITE;
                border = COLOR_NAVY;
            } else if ("[ BATAL ]".equals(text)) {
                bg = Color.WHITE;
                fg = COLOR_RED;
                border = COLOR_RED;
            } else if ("[ TERDAFTAR ]".equals(text)) {
                bg = COLOR_EMERALD_BG;
                fg = COLOR_EMERALD;
                border = COLOR_EMERALD_BORDER;
            } else { // [ TERKUNCI ], [ PENUH ]
                bg = COLOR_DISABLED_BG;
                fg = COLOR_DISABLED_TEXT;
                border = COLOR_BORDER;
            }

            int w = getWidth() - 8;
            int h = getHeight() - 6;
            int x = 4;
            int y = 3;

            g2.setColor(bg);
            g2.fillRoundRect(x, y, w, h, 4, 4);

            g2.setColor(border);
            g2.drawRoundRect(x, y, w - 1, h - 1, 4, 4);

            g2.setFont(getFont());
            g2.setColor(fg);
            FontMetrics fm = g2.getFontMetrics();
            int tx = x + (w - fm.stringWidth(text)) / 2;
            int ty = y + (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(text, tx, ty);

            g2.dispose();
        }
    }

    private class TableActionButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private final JButton button;
        private final ActionCallback callback;
        private String currentKodeMK;
        private String currentAction;

        public TableActionButtonEditor(ActionCallback callback) {
            this.callback = callback;
            this.button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                    String text = getText();
                    Color bg, fg, border;

                    if ("[ AMBIL ]".equals(text)) {
                        bg = COLOR_NAVY_HOVER;
                        fg = Color.WHITE;
                        border = COLOR_NAVY;
                    } else if ("[ BATAL ]".equals(text)) {
                        bg = COLOR_RED_BG;
                        fg = COLOR_RED;
                        border = COLOR_RED;
                    } else if ("[ TERDAFTAR ]".equals(text)) {
                        bg = COLOR_EMERALD_BG;
                        fg = COLOR_EMERALD;
                        border = COLOR_EMERALD_BORDER;
                    } else {
                        bg = COLOR_DISABLED_BG;
                        fg = COLOR_DISABLED_TEXT;
                        border = COLOR_BORDER;
                    }

                    int w = getWidth() - 8;
                    int h = getHeight() - 6;
                    int x = 4;
                    int y = 3;

                    g2.setColor(bg);
                    g2.fillRoundRect(x, y, w, h, 4, 4);

                    g2.setColor(border);
                    g2.drawRoundRect(x, y, w - 1, h - 1, 4, 4);

                    g2.setFont(getFont());
                    g2.setColor(fg);
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = x + (w - fm.stringWidth(text)) / 2;
                    int ty = y + (h - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(text, tx, ty);

                    g2.dispose();
                }
            };
            this.button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            this.button.setOpaque(true);
            this.button.setContentAreaFilled(true);
            this.button.setBorderPainted(false);
            this.button.setFocusPainted(false);
            this.button.setFont(FONT_TEXT_SMALL_BOLD);
            this.button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentKodeMK = String.valueOf(table.getValueAt(row, 0));
            this.currentAction = String.valueOf(value);
            button.setText(this.currentAction);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            if (callback != null && currentKodeMK != null) {
                if ("[ AMBIL ]".equals(currentAction) || "[ BATAL ]".equals(currentAction)) {
                    callback.execute(currentKodeMK);
                }
            }
        }
    }
}
