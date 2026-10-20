package view;

import model.Dosen;
import model.KRS;
import model.Mahasiswa;
import model.MataKuliah;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.print.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class CetakKRSViewer extends JDialog implements Printable {

    private final Mahasiswa mahasiswa;
    private final Dosen dosen;
    private final KRS krs;
    private final String tanggalCetak;

    private static final int PAGE_WIDTH = 595;  // Standar A4 72 DPI (Points)
    private static final int PAGE_HEIGHT = 842;

    public CetakKRSViewer(Frame parent, Mahasiswa mahasiswa, Dosen dosen, KRS krs) {
        super(parent, "Pratinjau Dokumen Cetak - Kartu Rencana Studi Resmi (PENS PSDKU Sumenep)", true);
        this.mahasiswa = mahasiswa;
        this.dosen = dosen;
        this.krs = krs;

        Locale idLocale = new Locale("id", "ID");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMMM yyyy", idLocale);
        this.tanggalCetak = "Sumenep, " + LocalDate.now().format(df);

        inisialisasiUI();
    }

    private void inisialisasiUI() {
        setSize(780, 880);
        setMinimumSize(new Dimension(680, 720));
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // 1. Toolbar Atas (Aksi Cetak & Tutup)
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(new Color(0x0A, 0x19, 0x2F)); // Navy pekat
        toolbar.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel("DOKUMEN RESMI KRS • SIAP DICETAK / DIEKSPOR PDF");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(Color.WHITE); // Teks putih tebal

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnGroup.setOpaque(false);

        JButton btnPrint = new JButton("[ CETAK KE PRINTER / PDF ]");
        btnPrint.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnPrint.setBackground(new Color(0x04, 0x78, 0x57)); // Classic Emerald Green
        btnPrint.setForeground(Color.WHITE); // Teks putih kontras tinggi
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPrint.setOpaque(true);
        btnPrint.setContentAreaFilled(true);
        btnPrint.setBorderPainted(false);
        btnPrint.setFocusPainted(false);
        btnPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPrint.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnPrint.addActionListener(e -> eksekusiPencetakan());

        JButton btnClose = new JButton("[ TUTUP ]");
        btnClose.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnClose.setBackground(Color.WHITE); // Latar putih
        btnClose.setForeground(new Color(0x0F, 0x17, 0x2A)); // Teks charcoal pekat
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClose.setOpaque(true);
        btnClose.setContentAreaFilled(true);
        btnClose.setBorderPainted(true);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x94, 0xA3, 0xB8), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnClose.addActionListener(e -> dispose());

        btnGroup.add(btnPrint);
        btnGroup.add(btnClose);

        toolbar.add(lblTitle, BorderLayout.WEST);
        toolbar.add(btnGroup, BorderLayout.EAST);
        add(toolbar, BorderLayout.NORTH);

        // 2. Kertas Dokumen A4 di dalam ScrollPane
        JPanel pageContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        pageContainer.setBackground(new Color(0x64, 0x74, 0x8B)); // Slate Desk Background

        DocumentPanel docPanel = new DocumentPanel();
        pageContainer.add(docPanel);

        JScrollPane scroll = new JScrollPane(pageContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Panel Kertas A4 yang menggambar konten dokumen secara proporsional.
     */
    private class DocumentPanel extends JPanel {
        public DocumentPanel() {
            setPreferredSize(new Dimension(PAGE_WIDTH, PAGE_HEIGHT));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0x33, 0x41, 0x55), 1),
                    new EmptyBorder(0, 0, 0, 0)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            renderDokumenA4(g2d, getWidth(), getHeight());
        }
    }

    /**
     * Engine render grafis dokumen formal A4 monokrom.
     */
    private void renderDokumenA4(Graphics2D g2, int width, int height) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int marginX = 42;
        int y = 46;
        int contentWidth = width - (2 * marginX);

        // 1. KOP SURAT RESMI
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        drawCenteredString(g2, "KEMENTERIAN PENDIDIKAN TINGGI, SAINS, DAN TEKNOLOGI", width, y);
        y += 18;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        drawCenteredString(g2, "POLITEKNIK ELEKTRONIKA NEGERI SURABAYA", width, y);
        y += 16;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        drawCenteredString(g2, "PROGRAM STUDI D3 TEKNIK INFORMATIKA • PSDKU SUMENEP", width, y);
        y += 14;

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        drawCenteredString(g2, "Jl. Raya Sumenep-Pamekasan KM. 4, Sumenep, Jawa Timur | Laman: www.pens.ac.id", width, y);
        y += 12;

        // Garis Pemisah Kop Ganda (Tebal & Tipis)
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawLine(marginX, y, width - marginX, y);
        y += 3;
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawLine(marginX, y, width - marginX, y);
        y += 24;

        // 2. JUDUL DOKUMEN
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        drawCenteredString(g2, "KARTU RENCANA STUDI (KRS)", width, y);
        y += 14;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        drawCenteredString(g2, "SEMESTER GASAL TAHUN AKADEMIK 2026/2027", width, y);
        y += 26;

        // 3. BIODATA MAHASISWA & DOSEN PEMBIMBING (GRID DUA KOLOM)
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        int col1X = marginX;
        int col2X = marginX + 260;
        int bioLineH = 15;

        // Kolom Kiri
        g2.drawString("Nama Mahasiswa", col1X, y);
        g2.drawString(":  " + mahasiswa.getNama(), col1X + 90, y);
        y += bioLineH;

        g2.drawString("Nomor Induk (NIM)", col1X, y);
        g2.drawString(":  " + mahasiswa.getNim(), col1X + 90, y);
        y += bioLineH;

        g2.drawString("Program Studi", col1X, y);
        g2.drawString(":  D3 Teknik Informatika", col1X + 90, y);

        // Kolom Kanan (kembali ke atas y)
        int yRight = y - (2 * bioLineH);
        g2.drawString("Semester / Status", col2X, yRight);
        g2.drawString(":  " + mahasiswa.getSemester() + " / " + mahasiswa.getStatus(), col2X + 95, yRight);
        yRight += bioLineH;

        g2.drawString("Batas Beban SKS", col2X, yRight);
        g2.drawString(":  " + mahasiswa.getMaxSKS() + " SKS", col2X + 95, yRight);
        yRight += bioLineH;

        g2.drawString("Dosen Pembimbing", col2X, yRight);
        g2.drawString(":  " + dosen.getNama(), col2X + 95, yRight);

        y += 24;

        // 4. TABEL RENCANA STUDI
        int[] colW = {30, 65, 230, 45, 141}; // Total: 511
        String[] headers = {"NO", "KODE", "MATA KULIAH", "SKS", "KETERANGAN / JADWAL"};
        int rowH = 22;

        // Header Table
        g2.setColor(new Color(0xF1, 0xF5, 0xF9));
        g2.fillRect(marginX, y, contentWidth, rowH);
        g2.setColor(Color.BLACK);
        g2.drawRect(marginX, y, contentWidth, rowH);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
        int curX = marginX;
        for (int i = 0; i < headers.length; i++) {
            g2.drawRect(curX, y, colW[i], rowH);
            if (i == 0 || i == 1 || i == 3) {
                drawCenteredInBox(g2, headers[i], curX, y, colW[i], rowH);
            } else {
                g2.drawString(headers[i], curX + 6, y + 15);
            }
            curX += colW[i];
        }
        y += rowH;

        // Data Rows Table
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        List<MataKuliah> listMK = krs.getMataKuliahList();
        int totalSks = 0;

        for (int idx = 0; idx < listMK.size(); idx++) {
            MataKuliah mk = listMK.get(idx);
            totalSks += mk.getSks();

            curX = marginX;
            g2.drawRect(curX, y, colW[0], rowH);
            drawCenteredInBox(g2, String.valueOf(idx + 1), curX, y, colW[0], rowH);
            curX += colW[0];

            g2.drawRect(curX, y, colW[1], rowH);
            drawCenteredInBox(g2, mk.getKodeMK(), curX, y, colW[1], rowH);
            curX += colW[1];

            g2.drawRect(curX, y, colW[2], rowH);
            g2.drawString(mk.getNamaMK(), curX + 6, y + 15);
            curX += colW[2];

            g2.drawRect(curX, y, colW[3], rowH);
            drawCenteredInBox(g2, String.valueOf(mk.getSks()), curX, y, colW[3], rowH);
            curX += colW[3];

            g2.drawRect(curX, y, colW[4], rowH);
            g2.drawString("Disetujui Resmi", curX + 6, y + 15);

            y += rowH;
        }

        // Baris Total SKS
        g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
        int spanW = colW[0] + colW[1] + colW[2];
        g2.drawRect(marginX, y, spanW, rowH);
        g2.drawString("TOTAL SKS YANG DISETUJUI", marginX + 10, y + 15);

        g2.drawRect(marginX + spanW, y, colW[3], rowH);
        drawCenteredInBox(g2, String.valueOf(totalSks), marginX + spanW, y, colW[3], rowH);

        g2.drawRect(marginX + spanW + colW[3], y, colW[4], rowH);
        g2.drawString("BEBAN MEMENUHI SYARAT", marginX + spanW + colW[3] + 6, y + 15);
        y += rowH + 28;

        // 5. STATUS VALIDASI RESMI
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 8));
        g2.drawString("* Dokumen ini sah dan diterbitkan secara digital oleh SIAKAD PENS PSDKU Sumenep. Kode Verifikasi: " + krs.getIdKRS(), marginX, y);
        y += 24;

        // 6. LEMBAR PENGESAHAN & TANDA TANGAN (DUA SISI)
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        int sigCol1X = marginX + 30;
        int sigCol2X = width - marginX - 180;

        // Sisi Kiri: Mahasiswa
        g2.drawString("Mengetahui,", sigCol1X, y);
        // Sisi Kanan: Dosen Pembimbing
        g2.drawString(tanggalCetak, sigCol2X, y);
        y += 14;

        g2.drawString("Mahasiswa Yang Bersangkutan,", sigCol1X, y);
        g2.drawString("Dosen Pembimbing Akademik,", sigCol2X, y);
        y += 62; // Ruang Tanda Tangan

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.drawString(mahasiswa.getNama(), sigCol1X, y);
        g2.drawString(dosen.getNama(), sigCol2X, y);
        y += 12;

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        g2.drawString("NIM: " + mahasiswa.getNim(), sigCol1X, y);
        g2.drawString("NIP: " + dosen.getNip(), sigCol2X, y);
    }

    private void drawCenteredString(Graphics2D g2, String text, int totalWidth, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (totalWidth - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }

    private void drawCenteredInBox(Graphics2D g2, String text, int x, int y, int w, int h) {
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (w - fm.stringWidth(text)) / 2;
        int ty = y + (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, tx, ty);
    }

    /**
     * Implementasi interface Printable untuk dialog Java Printing API.
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Skala render agar pas dengan ukuran fisik kertas A4 printer
        double scaleX = pageFormat.getImageableWidth() / PAGE_WIDTH;
        double scaleY = pageFormat.getImageableHeight() / PAGE_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        g2d.scale(scale, scale);

        renderDokumenA4(g2d, PAGE_WIDTH, PAGE_HEIGHT);
        return PAGE_EXISTS;
    }

    private void eksekusiPencetakan() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("KRS_" + mahasiswa.getNim() + "_" + mahasiswa.getNama().replace(" ", "_"));
        job.setPrintable(this);

        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this,
                        "Dokumen Kartu Rencana Studi (KRS) berhasil dikirim ke antrean cetak / PDF.",
                        "Status Pencetakan",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this,
                        "Gagal mencetak dokumen: " + ex.getMessage(),
                        "Kesalahan Pencetakan",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
