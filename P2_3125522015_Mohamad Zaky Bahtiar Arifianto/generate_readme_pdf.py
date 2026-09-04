import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn
import win32com.client
import pythoncom

def set_cell_shading(cell, color_hex):
    shading_elm = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{color_hex}"/>')
    cell._tc.get_or_add_tcPr().append(shading_elm)

def set_cell_margins(cell, top=60, bottom=60, left=80, right=80):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('top', top), ('bottom', bottom), ('left', left), ('right', right)]:
        node = OxmlElement(f'w:{m}')
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def create_readme_doc():
    doc = docx.Document()

    # Set compact margins (0.6 inch) to ensure exact 3 pages
    for s in doc.sections:
        s.top_margin = Inches(0.55)
        s.bottom_margin = Inches(0.55)
        s.left_margin = Inches(0.65)
        s.right_margin = Inches(0.65)

    NAVY = RGBColor(0x0F, 0x2C, 0x59)
    DARK_BLUE = RGBColor(0x1B, 0x36, 0x5D)

    # -------------------------------------------------------------------------
    # HALAMAN 1
    # -------------------------------------------------------------------------
    p_header = doc.add_paragraph()
    p_header.paragraph_format.space_after = Pt(2)
    p_header.paragraph_format.line_spacing = 1.05
    r = p_header.add_run("PRAKTIKUM PEMROGRAMAN BERORIENTASI OBYEK — MODUL 2\n")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = NAVY
    r2 = p_header.add_run("PENS PSDKU Sumenep | D3 Teknik Informatika | Pengembang: Mohamad Zaky Bahtiar Arifianto (NRP: 3125522015)")
    r2.font.size = Pt(9)
    r2.font.color.rgb = RGBColor(0x55, 0x55, 0x55)

    p_div = doc.add_paragraph()
    p_div.paragraph_format.space_after = Pt(8)
    p_div_border = parse_xml(f'<w:pBdr {nsdecls("w")}><w:bottom w:val="single" w:sz="12" w:space="1" w:color="0F2C59"/></w:pBdr>')
    p_div._p.get_or_add_pPr().append(p_div_border)

    p_p1 = doc.add_paragraph()
    p_p1.paragraph_format.space_after = Pt(4)
    r = p_p1.add_run("1. Nama Proyek")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = DARK_BLUE

    p_p1_val = doc.add_paragraph()
    p_p1_val.paragraph_format.space_after = Pt(8)
    r = p_p1_val.add_run("Sistem Informasi Akademik Mahasiswa (SIAKAD)")
    r.bold = True
    r.font.size = Pt(11)

    p_p2 = doc.add_paragraph()
    p_p2.paragraph_format.space_after = Pt(4)
    r = p_p2.add_run("2. Product Goal")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = DARK_BLUE

    p_p2_val = doc.add_paragraph()
    p_p2_val.paragraph_format.space_after = Pt(8)
    p_p2_val.paragraph_format.line_spacing = 1.15
    r = p_p2_val.add_run('"Membangun aplikasi Sistem Informasi Akademik sederhana berbasis Java dan OOP untuk mempermudah pengelolaan data mahasiswa, validasi otomatis batas beban SKS berdasarkan capaian IPK, serta pengambilan mata kuliah (KRS) secara terstruktur dan efisien."')
    r.italic = True
    r.font.size = Pt(10)

    p_p3 = doc.add_paragraph()
    p_p3.paragraph_format.space_after = Pt(4)
    r = p_p3.add_run("3. Sprint Goal (P2)")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = DARK_BLUE

    p_p3_val = doc.add_paragraph()
    p_p3_val.paragraph_format.space_after = Pt(8)
    p_p3_val.paragraph_format.line_spacing = 1.15
    r = p_p3_val.add_run('"Mengimplementasikan class utama proyek SIAKAD (Mahasiswa, MataKuliah, dan KRS) secara utuh mencakup attribute, constructor (dengan keyword this), method (tanpa parameter, dengan parameter, dan return value), serta melakukan instansiasi multi-object dan pengujian operasi interaksi antar-objek pada Main.java."')
    r.italic = True
    r.font.size = Pt(10)

    p_p4 = doc.add_paragraph()
    p_p4.paragraph_format.space_after = Pt(4)
    r = p_p4.add_run("4. Sprint Backlog (P2)")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = DARK_BLUE

    # Sprint Backlog Table
    t_sb = doc.add_table(rows=7, cols=3)
    t_sb.alignment = WD_TABLE_ALIGNMENT.CENTER
    sb_headers = ["ID", "Item Sprint Backlog", "Status"]
    for j, h in enumerate(sb_headers):
        cell = t_sb.cell(0, j)
        set_cell_shading(cell, "1B365D")
        set_cell_margins(cell, top=80, bottom=80, left=100, right=100)
        p = cell.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r = p.add_run(h)
        r.bold = True
        r.font.size = Pt(9.5)
        r.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)

    sb_data = [
        ("SB-01", "Refinement 3 kandidat objek dari P1 dan perancangan Class Diagram", "DONE"),
        ("SB-02", "Membuat class Mahasiswa (6 attribute, constructor, 5 method)", "DONE"),
        ("SB-03", "Membuat class MataKuliah (7 attribute, constructor, 5 method)", "DONE"),
        ("SB-04", "Membuat class KRS (agregasi objek Mahasiswa & MataKuliah, 5 method)", "DONE"),
        ("SB-05", "Membuat class Main untuk instansiasi multi-object dan pengujian operasi", "DONE"),
        ("SB-06", "Penyusunan laporan resmi Word, README.md, dan verifikasi DoD", "DONE"),
    ]
    for i, row in enumerate(sb_data):
        row_idx = i + 1
        shd = "FFFFFF" if row_idx % 2 != 0 else "F7FAFC"
        for j, val in enumerate(row):
            cell = t_sb.cell(row_idx, j)
            set_cell_shading(cell, shd)
            set_cell_margins(cell, top=60, bottom=60, left=90, right=90)
            p = cell.paragraphs[0]
            if j == 0 or j == 2:
                p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            else:
                p.alignment = WD_ALIGN_PARAGRAPH.LEFT
            r = p.add_run(val)
            r.font.size = Pt(9)
            if j == 2:
                r.bold = True
                r.font.color.rgb = RGBColor(0x1B, 0x7A, 0x43)

    # Page Break to Page 2
    doc.add_page_break()

    # -------------------------------------------------------------------------
    # HALAMAN 2
    # -------------------------------------------------------------------------
    p_h2 = doc.add_paragraph()
    p_h2.paragraph_format.space_after = Pt(6)
    r = p_h2.add_run("5. Spesifikasi Class, Attribute, Method, dan Constructor")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = DARK_BLUE

    # Table Refinement
    t_ref = doc.add_table(rows=4, cols=4)
    t_ref.alignment = WD_TABLE_ALIGNMENT.CENTER
    ref_headers = ["Class", "Attribute", "Constructor", "Method Utama"]
    for j, h in enumerate(ref_headers):
        cell = t_ref.cell(0, j)
        set_cell_shading(cell, "1B365D")
        set_cell_margins(cell, top=70, bottom=70, left=80, right=80)
        p = cell.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r = p.add_run(h)
        r.bold = True
        r.font.size = Pt(9)
        r.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)

    ref_data = [
        ("Mahasiswa", 
         "nrp, nama, prodi, semester, ipk, totalSks", 
         "Mahasiswa(nrp, nama, prodi, sem, ipk)", 
         "• tampilkanProfil() [void]\n• updateIpk(double) [void]\n• hitungBebanMaksimalSks() [int]\n• tambahSks(int) [boolean]\n• resetSks() [void]"),
        ("MataKuliah", 
         "kodeMk, namaMk, sks, semBuka, dosen, kuota, peserta", 
         "MataKuliah(kode, nama, sks, sem, dosen, kuota)", 
         "• tampilkanDetailMk() [void]\n• ubahDosenPengampu(str) [void]\n• tambahPeserta() [boolean]\n• getSisaKuota() [int]\n• isKelasPenuh() [boolean]"),
        ("KRS", 
         "nomorKrs, mahasiswa, tahunAjaran, semKrs, daftarMk[], jumlahMk, statusValidasi, dosenWali", 
         "KRS(noKrs, mhs, thn, sem, kapasitas)", 
         "• tambahMataKuliah(mk) [bool]\n• setujuiKrs(dosen) [void]\n• hitungTotalSksKrs() [int]\n• isDisetujui() [boolean]\n• tampilkanKrs() [void]")
    ]
    for i, row in enumerate(ref_data):
        row_idx = i + 1
        shd = "FFFFFF" if row_idx % 2 != 0 else "F7FAFC"
        for j, val in enumerate(row):
            cell = t_ref.cell(row_idx, j)
            set_cell_shading(cell, shd)
            set_cell_margins(cell, top=60, bottom=60, left=70, right=70)
            p = cell.paragraphs[0]
            p.paragraph_format.line_spacing = 1.05
            if j == 0:
                p.alignment = WD_ALIGN_PARAGRAPH.CENTER
                r = p.add_run(val)
                r.bold = True
            else:
                p.alignment = WD_ALIGN_PARAGRAPH.LEFT
                r = p.add_run(val)
            r.font.size = Pt(8)

    p_d_title = doc.add_paragraph()
    p_d_title.paragraph_format.space_before = Pt(8)
    p_d_title.paragraph_format.space_after = Pt(4)
    r = p_d_title.add_run("6. Diagram Sederhana Hubungan Antar-Class (Class Diagram)")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = DARK_BLUE

    diagram_text = """+-----------------------------+       +---------------------------------------------+
|          Mahasiswa          |       |                     KRS                     |
+-----------------------------+       +---------------------------------------------+
| - nrp : String              |       | - nomorKrs : String                         |
| - nama : String             |       | - mahasiswa : Mahasiswa                     |
| - prodi : String            | 1   1 | - tahunAjaran : String                      |
| - semester : int            |<------| - semesterKrs : int                         |
| - ipk : double              |       | - daftarMataKuliah : MataKuliah[]           |
| - totalSks : int            |       | - jumlahMk : int                            |
+-----------------------------+       | - statusValidasi : boolean                  |
| + tampilkanProfil() : void  |       | - dosenWaliPengesah : String                |
| + updateIpk(double) : void  |       +---------------------------------------------+
| + hitungBebanMaks() : int   |       | + tambahMataKuliah(MataKuliah) : boolean    |
| + tambahSks(int) : boolean  |       | + setujuiKrs(String) : void                 |
+-----------------------------+       | + hitungTotalSksKrs() : int                 |
                                      | + isDisetujui() : boolean                   |
                                      | + tampilkanKrs() : void                     |
                                      +---------------------------------------------+
                                                             | 1
                                                             |
                                                             | menampung (1..*)
                                                             v
                                      +---------------------------------------------+
                                      |                 MataKuliah                  |
                                      +---------------------------------------------+
                                      | - kodeMk : String                           |
                                      | - namaMk : String                           |
                                      | - sks : int                                 |
                                      | - semesterBuka : int                        |
                                      | - dosenPengampu : String                    |
                                      | - kuotaKelas : int                          |
                                      | - pesertaTerdaftar : int                    |
                                      +---------------------------------------------+
                                      | + tampilkanDetailMk() : void                |
                                      | + ubahDosenPengampu(String) : void          |
                                      | + tambahPeserta() : boolean                 |
                                      | + getSisaKuota() : int                      |
                                      | + isKelasPenuh() : boolean                  |
                                      +---------------------------------------------+"""

    table_diag = doc.add_table(rows=1, cols=1)
    table_diag.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_diag = table_diag.cell(0, 0)
    set_cell_shading(c_diag, "F4F6F8")
    set_cell_margins(c_diag, top=60, bottom=60, left=80, right=80)
    p_diag = c_diag.paragraphs[0]
    p_diag.paragraph_format.line_spacing = 1.0
    r = p_diag.add_run(diagram_text)
    r.font.name = 'Consolas'
    r.font.size = Pt(7)

    # Page Break to Page 3
    doc.add_page_break()

    # -------------------------------------------------------------------------
    # HALAMAN 3
    # -------------------------------------------------------------------------
    p_h3 = doc.add_paragraph()
    p_h3.paragraph_format.space_after = Pt(4)
    r = p_h3.add_run("7. Bukti Hasil Running Eksekusi Program (Class Main)")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = DARK_BLUE

    run_summary = """>> Kompilasi : javac -d bin src/*.java  [SUKSES]
>> Eksekusi  : java -cp bin Main        [SUKSES]

>>> 1. INSTANSIASI MULTI-OBJECT: mhs1, mhs2, mk1-mk5, krs1, krs2 berhasil dibuat di memori.
>>> 2. METHOD TANPA PARAMETER:
   mhs1: 3125522015 - Mohamad Zaky Bahtiar Arifianto | IPK: 3.82 | Kuota SKS: 24 SKS
   mhs2: 3125522022 - Ahmad Wildan Prasetyo          | IPK: 2.45 | Kuota SKS: 18 SKS
>>> 3. METHOD DENGAN PARAMETER:
   [INFO] IPK mhs1 diupdate: 3.82 -> 3.90
   [INFO] Dosen Pengampu MK Basis Data Lanjut diubah -> Dr. Indah Susilowati, S.T., M.T.
>>> 4. PENGUJIAN TRANSAKSI KRS & VALIDASI LOGIKA BISNIS:
   - mhs1 mengambil 5 MK (Total 14 / 24 SKS) -> [BERHASIL SEMUA]
   - mhs2 mengambil 3 MK (Total 8 / 18 SKS)  -> [BERHASIL SEMUA]
   - Uji batas kelas penuh pada MK Basis Data Lanjut (kuota 2) -> [DITOLAK: KELAS PENUH]
>>> 5. PENGUJIAN METHOD RETURN VALUE:
   - krs1.hitungTotalSksKrs() = 14 SKS | mhs1.hitungBebanMaksimalSks() = 24 SKS
   - mk3.getSisaKuota() = 0 Kursi      | mk3.isKelasPenuh() = true
>>> 6. PENGESAHAN OLEH DOSEN WALI:
   [VALIDASI KRS] Dokumen KRS KRS-2026-001 DISETUJUI & DISAHKAN oleh Dosen Wali.
   Status Verifikasi krs1.isDisetujui() = true (VALID / RESMI)
=========================================================================================="""

    table_run = doc.add_table(rows=1, cols=1)
    table_run.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_run = table_run.cell(0, 0)
    set_cell_shading(c_run, "F4F6F8")
    set_cell_margins(c_run, top=60, bottom=60, left=80, right=80)
    p_run = c_run.paragraphs[0]
    p_run.paragraph_format.line_spacing = 1.0
    r = p_run.add_run(run_summary)
    r.font.name = 'Consolas'
    r.font.size = Pt(7.5)

    p_rev_title = doc.add_paragraph()
    p_rev_title.paragraph_format.space_before = Pt(8)
    p_rev_title.paragraph_format.space_after = Pt(4)
    r = p_rev_title.add_run("8. Sprint Review (Evaluasi Hasil)")
    r.bold = True
    r.font.size = Pt(11)
    r.font.color.rgb = DARK_BLUE

    t_rev = doc.add_table(rows=7, cols=2)
    t_rev.alignment = WD_TABLE_ALIGNMENT.CENTER
    rev_headers = ["Item Evaluasi", "Hasil Capaian Praktikum P2"]
    for j, h in enumerate(rev_headers):
        cell = t_rev.cell(0, j)
        set_cell_shading(cell, "1B365D")
        set_cell_margins(cell, top=60, bottom=60, left=80, right=80)
        p = cell.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r = p.add_run(h)
        r.bold = True
        r.font.size = Pt(9)
        r.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)

    rev_data = [
        ("Class berhasil dibuat", "Terpenuhi 100% (3 class domain: Mahasiswa, MataKuliah, KRS + 1 class Main)."),
        ("Object berhasil dibuat", "Terpenuhi (minimal 2 objek per class: mhs1-mhs2, mk1-mk5, krs1-krs2)."),
        ("Constructor berjalan", "Terpenuhi (seluruh class memiliki constructor inisialisasi berparameter)."),
        ("Method berjalan", "Terpenuhi (method tanpa parameter, dengan parameter, & return value aktif)."),
        ("Program dapat dijalankan", "Terpenuhi (berhasil dikompilasi javac dan dieksekusi tanpa error/warning)."),
        ("Kendala yang ditemukan", "Array penampung KRS memerlukan pengecekan batas kapasitas maksimum (selesai).")
    ]
    for i, row in enumerate(rev_data):
        row_idx = i + 1
        shd = "FFFFFF" if row_idx % 2 != 0 else "F7FAFC"
        for j, val in enumerate(row):
            cell = t_rev.cell(row_idx, j)
            set_cell_shading(cell, shd)
            set_cell_margins(cell, top=50, bottom=50, left=70, right=70)
            p = cell.paragraphs[0]
            p.paragraph_format.line_spacing = 1.05
            if j == 0:
                p.alignment = WD_ALIGN_PARAGRAPH.LEFT
                r = p.add_run(val)
                r.bold = True
            else:
                p.alignment = WD_ALIGN_PARAGRAPH.LEFT
                r = p.add_run(val)
            r.font.size = Pt(8)

    p_kendala = doc.add_paragraph()
    p_kendala.paragraph_format.space_before = Pt(6)
    p_kendala.paragraph_format.space_after = Pt(2)
    r = p_kendala.add_run("9. Kendala yang Ditemukan & Solusi:")
    r.bold = True
    r.font.size = Pt(10)
    r.font.color.rgb = DARK_BLUE

    p_k_desc = doc.add_paragraph()
    p_k_desc.paragraph_format.space_after = Pt(0)
    p_k_desc.paragraph_format.line_spacing = 1.1
    r = p_k_desc.add_run("Penggunaan struktur array statis untuk menyimpan mata kuliah pada KRS memerlukan penjagaan agar indeks tidak out-of-bounds saat kapasitas tercapai. Solusi yang diimplementasikan adalah menambahkan pengecekan ukuran array internal pada method tambahMataKuliah() sebelum objek baru dialokasikan ke dalam koleksi.")
    r.font.size = Pt(8.5)

    readme_docx_path = "README_Summary.docx"
    doc.save(readme_docx_path)
    print(f"README Summary docx created at {readme_docx_path}")

def convert_docx_to_pdf(docx_path, pdf_path):
    pythoncom.CoInitialize()
    word = win32com.client.Dispatch("Word.Application")
    word.Visible = False
    abs_docx = os.path.abspath(docx_path)
    abs_pdf = os.path.abspath(pdf_path)
    print(f"Converting {abs_docx} -> {abs_pdf}...")
    doc = word.Documents.Open(abs_docx)
    wdFormatPDF = 17
    doc.SaveAs(abs_pdf, FileFormat=wdFormatPDF)
    doc.Close(False)
    word.Quit()
    pythoncom.CoUninitialize()
    print(f"Conversion finished: {pdf_path}")

if __name__ == "__main__":
    create_readme_doc()
    convert_docx_to_pdf("README_Summary.docx", "README.pdf")
    convert_docx_to_pdf("3125522015_Mohamad Zaky Bahtiar Arifianto.docx", "3125522015_Mohamad Zaky Bahtiar Arifianto.pdf")
