package P13_3125522015_MohamadZaky.controller;

import P13_3125522015_MohamadZaky.model.BahanBaku;
import P13_3125522015_MohamadZaky.model.DapatDilacak;
import P13_3125522015_MohamadZaky.model.DetailPengiriman;
import P13_3125522015_MohamadZaky.model.Menu;
import P13_3125522015_MohamadZaky.model.PengelolaDistribusi;
import P13_3125522015_MohamadZaky.model.PengelolaMenu;
import P13_3125522015_MohamadZaky.model.Pengguna;
import P13_3125522015_MohamadZaky.model.Pengiriman;
import P13_3125522015_MohamadZaky.model.Sekolah;
import P13_3125522015_MohamadZaky.model.StokTidakCukupException;
import P13_3125522015_MohamadZaky.utils.DatabaseConnection;
import P13_3125522015_MohamadZaky.utils.MbgRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainController {

    @FXML
    private BorderPane rootPane;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button btnNavDashboard;
    @FXML
    private Button btnNavInventaris;
    @FXML
    private Button btnNavMenuGizi;
    @FXML
    private Button btnNavDistribusi;
    @FXML
    private Button btnNavSekolah;
    @FXML
    private Label lblSidebarDbStatus;
    @FXML
    private Label lblHeaderTitle;
    @FXML
    private Label lblHeaderSubtitle;
    @FXML
    private Button btnMuatUlangData;
    @FXML
    private Button btnTemaToggle;
    @FXML
    private StackPane contentArea;

    private final MbgRepository repo = MbgRepository.getInstance();
    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
    private boolean modeGelap = false;
    private String modulAktif = "DASHBOARD";

    @FXML
    public void initialize() {
        muatLogoBadanGizi();
        perbaruiIndikatorDatabase();
        tampilkanDashboard();
    }

    private void muatLogoBadanGizi() {
        File fileLogo = new File("logombg.jpeg");
        if (fileLogo.exists()) {
            logoImageView.setImage(new Image(fileLogo.toURI().toString()));
        }
    }

    private void perbaruiIndikatorDatabase() {
        if (DatabaseConnection.isMysqlOnline()) {
            lblSidebarDbStatus.setText("MySQL XAMPP Aktif (mbg_db)");
        } else {
            lblSidebarDbStatus.setText("Simulasi Memori (XAMPP Offline)");
        }
    }

    private void aturNavigasiAktif(Button tombolAktif, String judul, String subjudul) {
        Button[] semuaTombol = {btnNavDashboard, btnNavInventaris, btnNavMenuGizi, btnNavDistribusi, btnNavSekolah};
        for (Button b : semuaTombol) {
            b.getStyleClass().remove("nav-button-active");
        }
        if (!tombolAktif.getStyleClass().contains("nav-button-active")) {
            tombolAktif.getStyleClass().add("nav-button-active");
        }
        lblHeaderTitle.setText(judul);
        lblHeaderSubtitle.setText(subjudul);
    }

    @FXML
    public void sinkronkanDatabase() {
        repo.muatSemuaData();
        perbaruiIndikatorDatabase();
        segarkanModulAktif();
        tampilkanAlert(Alert.AlertType.INFORMATION,
                "Sinkronisasi Basis Data Selesai",
                DatabaseConnection.getStatusPesan(),
                "Seluruh objek model dan koleksi telah disinkronkan ulang.");
    }

    @FXML
    public void toggleTema() {
        modeGelap = !modeGelap;
        if (modeGelap) {
            if (!rootPane.getStyleClass().contains("dark-theme")) {
                rootPane.getStyleClass().add("dark-theme");
            }
            btnTemaToggle.setText("Mode Tampilan: Gelap");
        } else {
            rootPane.getStyleClass().remove("dark-theme");
            btnTemaToggle.setText("Mode Tampilan: Terang");
        }
    }

    private void segarkanModulAktif() {
        switch (modulAktif) {
            case "INVENTARIS" -> tampilkanInventaris();
            case "MENU_GIZI" -> tampilkanMenuGizi();
            case "DISTRIBUSI" -> tampilkanDistribusi();
            case "SEKOLAH" -> tampilkanSekolah();
            default -> tampilkanDashboard();
        }
    }

    @FXML
    public void tampilkanDashboard() {
        modulAktif = "DASHBOARD";
        aturNavigasiAktif(btnNavDashboard,
                "Dashboard Operasional Program Makan Bergizi Gratis",
                "Ringkasan eksekutif distribusi porsi, anggaran kuartal, dan polimorfisme personel [P12 Persistensi File]");

        VBox container = new VBox(16);

        HBox rowCards = new HBox(14);
        int totalPorsi = repo.getTotalPorsiTersalurkan();
        double sisaAnggaran = repo.getSisaAnggaran();
        int totalSiswa = repo.getTotalSiswaTerdaftar();
        long bahanKritis = repo.getDaftarBahanBaku().stream()
                .filter(b -> b.getStok() <= 10 || b.getSisaHariKedaluwarsa() <= 7)
                .count();

        VBox cardPorsi = buatMetricCard(
                "TOTAL PORSI TERSALURKAN [SIMULASI]",
                totalPorsi + " Porsi",
                "Dari " + repo.getDaftarPengiriman().size() + " pengiriman terdaftar pada PengelolaDistribusi",
                true
        );
        VBox cardAnggaran = buatMetricCard(
                "SISA ANGGARAN DAPUR [SIMULASI]",
                rupiahFormat.format(sisaAnggaran),
                "Terpakai: " + rupiahFormat.format(repo.getDanaTerpakai()) + " dari pagu kuartal",
                false
        );
        VBox cardSekolah = buatMetricCard(
                "CAKUPAN SEKOLAH & SISWA [SIMULASI]",
                repo.getDaftarSekolah().size() + " Sekolah (" + totalSiswa + " Siswa)",
                "Wilayah koordinasi Kabupaten Sumenep",
                false
        );
        VBox cardStok = buatMetricCard(
                "PERINGATAN STOK & KEDALUWARSA",
                bahanKritis + " Komoditas Perlu Tindakan",
                "Stok <= 10 unit atau kedaluwarsa <= 7 hari",
                bahanKritis > 0
        );

        HBox.setHgrow(cardPorsi, Priority.ALWAYS);
        HBox.setHgrow(cardAnggaran, Priority.ALWAYS);
        HBox.setHgrow(cardSekolah, Priority.ALWAYS);
        HBox.setHgrow(cardStok, Priority.ALWAYS);

        rowCards.getChildren().addAll(cardPorsi, cardAnggaran, cardSekolah, cardStok);

        HBox masterDetailBox = new HBox(16);
        VBox.setVgrow(masterDetailBox, Priority.ALWAYS);

        VBox chartBox = new VBox(10);
        chartBox.getStyleClass().add("card-flat");
        HBox.setHgrow(chartBox, Priority.ALWAYS);

        Label lblChartTitle = new Label("Perbandingan Kuota Siswa vs Realisasi Porsi per Sekolah Tujuan [Simulasi]");
        lblChartTitle.getStyleClass().add("section-header");
        Label lblChartDesc = new Label("Menampilkan data agregasi antara kapasitas sekolah dan realisasi distribusi dari koleksi ArrayList.");
        lblChartDesc.getStyleClass().add("page-subtitle");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Sekolah Penerima Manfaat");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Jumlah Porsi / Siswa");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(true);
        barChart.setAnimated(false);
        VBox.setVgrow(barChart, Priority.ALWAYS);

        XYChart.Series<String, Number> seriesKuota = new XYChart.Series<>();
        seriesKuota.setName("Kuota Siswa Terdaftar");
        XYChart.Series<String, Number> seriesTerkirim = new XYChart.Series<>();
        seriesTerkirim.setName("Porsi Dikirim Hari Ini");

        for (Sekolah s : repo.getDaftarSekolah()) {
            String namaPendek = s.getNamaSekolah().replace(" [Simulasi]", "");
            seriesKuota.getData().add(new XYChart.Data<>(namaPendek, s.getJumlahSiswa()));
            int terkirimSekolah = repo.getDaftarPengiriman().stream()
                    .filter(p -> p.getSekolahTujuan().getNpsn().equals(s.getNpsn()))
                    .mapToInt(Pengiriman::getJumlahPorsi)
                    .sum();
            seriesTerkirim.getData().add(new XYChart.Data<>(namaPendek, terkirimSekolah));
        }
        barChart.getData().add(seriesKuota);
        barChart.getData().add(seriesTerkirim);
        chartBox.getChildren().addAll(lblChartTitle, lblChartDesc, barChart);

        VBox oopDetailBox = new VBox(10);
        oopDetailBox.getStyleClass().add("card-flat");
        oopDetailBox.setPrefWidth(470);
        oopDetailBox.setMinWidth(440);

        Label lblOopHeader = new Label("Inspeksi Polimorfisme & Pelacakan Personel (Modul 5-7)");
        lblOopHeader.getStyleClass().add("section-header");
        Label lblOopSub = new Label("Koleksi List<Pengguna> polimorfis dengan Dynamic Binding pada method tampilkanPeran()");
        lblOopSub.getStyleClass().add("page-subtitle");

        TableView<Pengguna> tblPengguna = new TableView<>();
        tblPengguna.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblPengguna.setPlaceholder(new Label("Belum ada personel terdaftar. Klik Sinkronkan Data SQL untuk memuat personel."));
        VBox.setVgrow(tblPengguna, Priority.ALWAYS);

        TableColumn<Pengguna, String> colId = new TableColumn<>("ID");
        colId.setMaxWidth(95);
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdPengguna()));

        TableColumn<Pengguna, String> colNama = new TableColumn<>("Nama Personel");
        colNama.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNama()));

        TableColumn<Pengguna, String> colPeran = new TableColumn<>("Peran (@Override)");
        colPeran.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().tampilkanPeran()));

        tblPengguna.getColumns().addAll(List.of(colId, colNama, colPeran));
        tblPengguna.setItems(FXCollections.observableArrayList(repo.getDaftarPengguna()));

        VBox detailInspeksi = new VBox(6);
        detailInspeksi.getStyleClass().add("banner-info");
        Label lblDetailNama = new Label("Pilih baris personel di atas untuk melihat rincian tugas dan antarmuka pelacakan.");
        lblDetailNama.setWrapText(true);
        lblDetailNama.getStyleClass().add("field-label");
        Label lblDetailTugas = new Label("-");
        lblDetailTugas.setWrapText(true);
        Label lblDetailLacak = new Label("-");
        lblDetailLacak.setWrapText(true);
        detailInspeksi.getChildren().addAll(lblDetailNama, lblDetailTugas, lblDetailLacak);

        tblPengguna.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, dipilih) -> {
            if (dipilih != null) {
                lblDetailNama.setText(dipilih.getNama() + " (" + dipilih.getEmail() + ")");
                lblDetailTugas.setText("Tugas Spesifik: " + dipilih.getRingkasanTugas());
                if (dipilih instanceof DapatDilacak terlacak) {
                    lblDetailLacak.setText("Implementasi DapatDilacak.getLokasi(): " + terlacak.getLokasi());
                } else {
                    lblDetailLacak.setText("Status Pelacakan GPS: Non-Armada (Berbasis Posko/Dapur Tetap)");
                }
            }
        });
        if (!tblPengguna.getItems().isEmpty()) {
            tblPengguna.getSelectionModel().selectFirst();
        }

        oopDetailBox.getChildren().addAll(lblOopHeader, lblOopSub, tblPengguna, detailInspeksi);
        masterDetailBox.getChildren().addAll(chartBox, oopDetailBox);

        container.getChildren().addAll(rowCards, masterDetailBox);
        setKontenUtama(container);
    }

    private VBox buatMetricCard(String judul, String nilai, String keterangan, boolean elevated) {
        VBox card = new VBox(6);
        card.getStyleClass().add(elevated ? "card-elevated" : "card-flat");
        Label lblJudul = new Label(judul);
        lblJudul.getStyleClass().add("metric-title");
        Label lblNilai = new Label(nilai);
        lblNilai.getStyleClass().add("metric-value");
        lblNilai.setWrapText(true);
        Label lblKet = new Label(keterangan);
        lblKet.getStyleClass().add("metric-caption");
        lblKet.setWrapText(true);
        card.getChildren().addAll(lblJudul, lblNilai, lblKet);
        return card;
    }

    @FXML
    public void tampilkanInventaris() {
        modulAktif = "INVENTARIS";
        aturNavigasiAktif(btnNavInventaris,
                "Inventaris Gudang Bahan Baku & Validasi State Objek (Modul 10)",
                "DataGrid CRUD dengan deteksi masa kedaluwarsa otomatis dan penanganan StokTidakCukupException");

        HBox masterDetail = new HBox(16);

        VBox masterBox = new VBox(12);
        masterBox.getStyleClass().add("card-flat");
        HBox.setHgrow(masterBox, Priority.ALWAYS);

        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        Label lblCari = new Label("Filter Bahan / Status:");
        lblCari.getStyleClass().add("field-label");
        TextField txtCari = new TextField();
        txtCari.setPromptText("Ketik nama bahan atau kategori...");
        HBox.setHgrow(txtCari, Priority.ALWAYS);
        CheckBox chkHanyaKritis = new CheckBox("Hanya Stok Kritis / Kedaluwarsa");
        filterBar.getChildren().addAll(lblCari, txtCari, chkHanyaKritis);

        TableView<BahanBaku> tblBahan = new TableView<>();
        tblBahan.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblBahan.setPlaceholder(new Label("Tidak ada bahan baku yang cocok dengan kriteria pencarian."));
        VBox.setVgrow(tblBahan, Priority.ALWAYS);

        TableColumn<BahanBaku, String> colKode = new TableColumn<>("Kode");
        colKode.setMaxWidth(90);
        colKode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKodeBahan()));

        TableColumn<BahanBaku, String> colNama = new TableColumn<>("Nama Bahan Baku");
        colNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaBahan()));

        TableColumn<BahanBaku, String> colKategori = new TableColumn<>("Kategori");
        colKategori.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKategori()));

        TableColumn<BahanBaku, String> colStok = new TableColumn<>("Stok Gudang");
        colStok.setMaxWidth(110);
        colStok.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStok() + " " + d.getValue().getSatuan()));

        TableColumn<BahanBaku, String> colHarga = new TableColumn<>("Harga/Satuan");
        colHarga.setCellValueFactory(d -> new SimpleStringProperty(rupiahFormat.format(d.getValue().getHargaSatuan())));

        TableColumn<BahanBaku, String> colExp = new TableColumn<>("Tgl Kedaluwarsa");
        colExp.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTanggalKedaluwarsa().toString()));

        TableColumn<BahanBaku, String> colStatus = new TableColumn<>("Status Kelayakan");
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatusKelayakan()));
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    if (item.startsWith("KEDALUWARSA")) {
                        badge.getStyleClass().add("status-badge-danger");
                    } else if (item.startsWith("SEGERA HABIS")) {
                        badge.getStyleClass().add("status-badge-warn");
                    } else {
                        badge.getStyleClass().add("status-badge-ok");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        tblBahan.getColumns().addAll(List.of(colKode, colNama, colKategori, colStok, colHarga, colExp, colStatus));

        Runnable terapkanFilter = () -> {
            String kata = txtCari.getText() == null ? "" : txtCari.getText().trim().toLowerCase();
            boolean kritisSaja = chkHanyaKritis.isSelected();
            ObservableList<BahanBaku> terfilter = FXCollections.observableArrayList();
            for (BahanBaku b : repo.getDaftarBahanBaku()) {
                boolean cocokKata = kata.isEmpty()
                        || b.getNamaBahan().toLowerCase().contains(kata)
                        || b.getKodeBahan().toLowerCase().contains(kata)
                        || b.getKategori().toLowerCase().contains(kata);
                boolean cocokKritis = !kritisSaja || b.getSisaHariKedaluwarsa() <= 7 || b.getStok() <= 10;
                if (cocokKata && cocokKritis) {
                    terfilter.add(b);
                }
            }
            tblBahan.setItems(terfilter);
        };
        txtCari.textProperty().addListener((o, ov, nv) -> terapkanFilter.run());
        chkHanyaKritis.selectedProperty().addListener((o, ov, nv) -> terapkanFilter.run());
        terapkanFilter.run();

        masterBox.getChildren().addAll(
                new Label("Daftar Inventaris Bahan Baku Dapur Umum"),
                filterBar,
                tblBahan
        );
        masterBox.getChildren().get(0).getStyleClass().add("section-header");

        VBox detailBox = new VBox(14);
        detailBox.getStyleClass().add("card-elevated");
        detailBox.setPrefWidth(430);
        detailBox.setMinWidth(410);

        Label lblFormTitle = new Label("Form Kelola Bahan Baku (CRUD)");
        lblFormTitle.getStyleClass().add("section-header");

        GridPane gridForm = new GridPane();
        gridForm.setHgap(10);
        gridForm.setVgap(8);

        TextField txtKode = new TextField();
        txtKode.setPromptText("Contoh: BHN-008");
        TextField txtNama = new TextField();
        txtNama.setPromptText("Nama komoditas pangan");
        ComboBox<String> cmbKategori = new ComboBox<>(FXCollections.observableArrayList(
                "Karbohidrat", "Protein Hewani", "Protein Nabati", "Sayuran", "Pelengkap Gizi", "Bumbu Dapur"
        ));
        cmbKategori.setMaxWidth(Double.MAX_VALUE);
        cmbKategori.getSelectionModel().selectFirst();

        TextField txtStok = new TextField();
        txtStok.setPromptText("Bilangan bulat >= 0");
        ComboBox<String> cmbSatuan = new ComboBox<>(FXCollections.observableArrayList("kg", "liter", "butir", "paket"));
        cmbSatuan.setMaxWidth(Double.MAX_VALUE);
        cmbSatuan.getSelectionModel().selectFirst();

        TextField txtHarga = new TextField();
        txtHarga.setPromptText("Harga per satuan (Rp)");
        DatePicker dpKedaluwarsa = new DatePicker(LocalDate.now().plusDays(14));
        dpKedaluwarsa.setMaxWidth(Double.MAX_VALUE);

        tambahBarisGrid(gridForm, 0, "Kode Bahan:", txtKode);
        tambahBarisGrid(gridForm, 1, "Nama Bahan:", txtNama);
        tambahBarisGrid(gridForm, 2, "Kategori:", cmbKategori);
        tambahBarisGrid(gridForm, 3, "Stok Awal/Saat Ini:", txtStok);
        tambahBarisGrid(gridForm, 4, "Satuan Takar:", cmbSatuan);
        tambahBarisGrid(gridForm, 5, "Harga Satuan (Rp):", txtHarga);
        tambahBarisGrid(gridForm, 6, "Tgl Kedaluwarsa:", dpKedaluwarsa);

        HBox crudButtons = new HBox(8);
        Button btnSimpan = new Button("Simpan Bahan");
        btnSimpan.getStyleClass().add("btn-primary");
        Button btnReset = new Button("Bersihkan Form");
        btnReset.getStyleClass().add("btn-secondary");
        Button btnHapus = new Button("Hapus");
        btnHapus.getStyleClass().add("btn-danger");
        crudButtons.getChildren().addAll(btnSimpan, btnReset, btnHapus);

        Separator sep = new Separator();

        VBox boxUjiStok = new VBox(8);
        boxUjiStok.getStyleClass().add("banner-info");
        Label lblUjiHeader = new Label("Pengeluaran Stok Dapur (Uji Exception Modul 10)");
        lblUjiHeader.getStyleClass().add("section-header");
        Label lblUjiInfo = new Label("Pilih bahan pada tabel, masukkan jumlah pemakaian (uji '2', '0', '999', atau 'abc') untuk verifikasi konsistensi state.");
        lblUjiInfo.setWrapText(true);
        lblUjiInfo.getStyleClass().add("page-subtitle");

        HBox rowAmbil = new HBox(8);
        rowAmbil.setAlignment(Pos.CENTER_LEFT);
        TextField txtJumlahAmbil = new TextField();
        txtJumlahAmbil.setPromptText("Jumlah diambil (bilangan bulat)");
        HBox.setHgrow(txtJumlahAmbil, Priority.ALWAYS);
        Button btnKeluarkanStok = new Button("Gunakan Stok");
        btnKeluarkanStok.getStyleClass().add("btn-accent");
        rowAmbil.getChildren().addAll(txtJumlahAmbil, btnKeluarkanStok);

        Label lblStateAkhir = new Label("State Stok Akhir (Blok finally): Pilih bahan terlebih dahulu.");
        lblStateAkhir.getStyleClass().add("field-label");
        lblStateAkhir.setWrapText(true);

        boxUjiStok.getChildren().addAll(lblUjiHeader, lblUjiInfo, rowAmbil, lblStateAkhir);

        tblBahan.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, b) -> {
            if (b != null) {
                txtKode.setText(b.getKodeBahan());
                txtNama.setText(b.getNamaBahan());
                cmbKategori.setValue(b.getKategori());
                txtStok.setText(String.valueOf(b.getStok()));
                cmbSatuan.setValue(b.getSatuan());
                txtHarga.setText(String.valueOf((long) b.getHargaSatuan()));
                dpKedaluwarsa.setValue(b.getTanggalKedaluwarsa());
                lblStateAkhir.setText("State Stok Saat Ini [" + b.getNamaBahan() + "]: " + b.getStok() + " " + b.getSatuan());
            }
        });

        btnReset.setOnAction(e -> {
            tblBahan.getSelectionModel().clearSelection();
            txtKode.clear();
            txtNama.clear();
            txtStok.clear();
            txtHarga.clear();
            dpKedaluwarsa.setValue(LocalDate.now().plusDays(14));
            lblStateAkhir.setText("Form siap untuk input bahan baku baru.");
        });

        btnSimpan.setOnAction(e -> {
            try {
                int stokInput = Integer.parseInt(txtStok.getText().trim());
                double hargaInput = Double.parseDouble(txtHarga.getText().trim());
                BahanBaku baru = new BahanBaku(
                        txtKode.getText(),
                        txtNama.getText(),
                        cmbKategori.getValue(),
                        stokInput,
                        cmbSatuan.getValue(),
                        hargaInput,
                        dpKedaluwarsa.getValue()
                );
                repo.simpanAtauPerbaruiBahanBaku(baru);
                terapkanFilter.run();
                tblBahan.refresh();
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Data Bahan Baku Tersimpan",
                        "Komoditas " + baru.getNamaBahan() + " berhasil disimpan.",
                        "Stok tercatat: " + baru.getStok() + " " + baru.getSatuan());
            } catch (NumberFormatException ex) {
                tampilkanAlert(Alert.AlertType.ERROR,
                        "Format Angka Tidak Valid",
                        "Ditolak: Stok harus bilangan bulat dan harga harus angka.",
                        "Detail input: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                tampilkanAlert(Alert.AlertType.WARNING,
                        "Validasi Enkapsulasi Menolak Input",
                        "Ditolak: " + ex.getMessage(),
                        "Pastikan seluruh atribut memenuhi syarat state objek BahanBaku.");
            } finally {
                tblBahan.refresh();
            }
        });

        btnHapus.setOnAction(e -> {
            BahanBaku dipilih = tblBahan.getSelectionModel().getSelectedItem();
            if (dipilih == null) {
                tampilkanAlert(Alert.AlertType.WARNING, "Belum Ada Bahan Dipilih",
                        "Pilih baris bahan baku pada tabel sebelum menghapus.", "");
                return;
            }
            repo.hapusBahanBaku(dipilih.getKodeBahan());
            terapkanFilter.run();
            btnReset.fire();
        });

        btnKeluarkanStok.setOnAction(e -> {
            BahanBaku dipilih = tblBahan.getSelectionModel().getSelectedItem();
            if (dipilih == null) {
                tampilkanAlert(Alert.AlertType.WARNING, "Pilih Bahan Baku",
                        "Pilih salah satu komoditas bahan baku di tabel kiri terlebih dahulu.", "");
                return;
            }
            String rawInput = txtJumlahAmbil.getText() == null ? "" : txtJumlahAmbil.getText().trim();
            try {
                int jumlah = Integer.parseInt(rawInput);
                dipilih.kurangiStok(jumlah);
                repo.simpanAtauPerbaruiBahanBaku(dipilih);
                txtStok.setText(String.valueOf(dipilih.getStok()));
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Pengeluaran Stok Berhasil",
                        "Berhasil mengeluarkan " + jumlah + " " + dipilih.getSatuan() + " " + dipilih.getNamaBahan() + ".",
                        "Sisa stok gudang saat ini: " + dipilih.getStok() + " " + dipilih.getSatuan());
            } catch (NumberFormatException ex) {
                tampilkanAlert(Alert.AlertType.ERROR,
                        "Format Input Salah (NumberFormatException)",
                        "Ditolak: input harus bilangan bulat.",
                        "Input: \"" + rawInput + "\". State stok objek tetap utuh.");
            } catch (IllegalArgumentException ex) {
                tampilkanAlert(Alert.AlertType.WARNING,
                        "Input Tidak Valid (IllegalArgumentException)",
                        "Ditolak: " + ex.getMessage(),
                        "Operasi dibatalkan sebelum mengubah atribut stok.");
            } catch (StokTidakCukupException ex) {
                tampilkanAlert(Alert.AlertType.ERROR,
                        "Stok Gudang Tidak Mencukupi (StokTidakCukupException)",
                        "Ditolak: " + ex.getMessage(),
                        "Transaksi ditolak oleh method kurangiStok(). Nilai stok dipertahankan.");
            } finally {
                tblBahan.refresh();
                lblStateAkhir.setText("Stok akhir [" + dipilih.getNamaBahan() + "]: " + dipilih.getStok() + " " + dipilih.getSatuan() + " (Terverifikasi di blok finally)");
            }
        });

        if (!tblBahan.getItems().isEmpty()) {
            tblBahan.getSelectionModel().select(1);
        }

        detailBox.getChildren().addAll(lblFormTitle, gridForm, crudButtons, sep, boxUjiStok);
        masterDetail.getChildren().addAll(masterBox, detailBox);
        setKontenUtama(masterDetail);
    }

    @FXML
    public void tampilkanMenuGizi() {
        modulAktif = "MENU_GIZI";
        aturNavigasiAktif(btnNavMenuGizi,
                "Menu Standar Gizi & Persistensi File I/O (Modul 12)",
                "Penerapan MbgFileRepository & PengelolaMenu dengan operasi simpan, muat, fail-fast, dan state invariance");

        HBox masterDetail = new HBox(16);

        VBox masterBox = new VBox(12);
        masterBox.getStyleClass().add("card-flat");
        HBox.setHgrow(masterBox, Priority.ALWAYS);

        HBox menuHeaderBar = new HBox(10);
        menuHeaderBar.setAlignment(Pos.CENTER_LEFT);
        Label lblMasterTitle = new Label("Katalog Paket Menu Makan Bergizi Gratis (List<Menu>)");
        lblMasterTitle.getStyleClass().add("section-header");
        HBox.setHgrow(lblMasterTitle, Priority.ALWAYS);

        Label lblCariMenu = new Label("Cari Kode:");
        lblCariMenu.getStyleClass().add("field-label");
        TextField txtCariMenu = new TextField();
        txtCariMenu.setPromptText("Contoh: MNU-01");
        txtCariMenu.setPrefWidth(120);

        Button btnSimpanFileMenu = new Button("Simpan File");
        btnSimpanFileMenu.getStyleClass().add("btn-secondary");
        Button btnMuatFileMenu = new Button("Muat File");
        btnMuatFileMenu.getStyleClass().add("btn-secondary");

        menuHeaderBar.getChildren().addAll(lblMasterTitle, lblCariMenu, txtCariMenu, btnSimpanFileMenu, btnMuatFileMenu);

        TableView<Menu> tblMenu = new TableView<>();
        tblMenu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblMenu.setPrefHeight(210);
        tblMenu.setPlaceholder(new Label("Koleksi menu kosong. Tambahkan melalui form di kanan."));

        TableColumn<Menu, String> colKodeMenu = new TableColumn<>("Kode Menu");
        colKodeMenu.setMaxWidth(100);
        colKodeMenu.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKodeMenu()));

        TableColumn<Menu, String> colNamaMenu = new TableColumn<>("Nama Paket Menu");
        colNamaMenu.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaMenu()));

        TableColumn<Menu, String> colKalori = new TableColumn<>("Energi");
        colKalori.setMaxWidth(110);
        colKalori.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKaloriPerPorsi() + " kkal"));

        TableColumn<Menu, String> colProtein = new TableColumn<>("Protein");
        colProtein.setMaxWidth(100);
        colProtein.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getProteinGram() + " g"));

        TableColumn<Menu, String> colKarbo = new TableColumn<>("Karbohidrat");
        colKarbo.setMaxWidth(100);
        colKarbo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKarbohidratGram() + " g"));

        TableColumn<Menu, String> colBiaya = new TableColumn<>("Biaya / Porsi");
        colBiaya.setMaxWidth(130);
        colBiaya.setCellValueFactory(d -> new SimpleStringProperty(rupiahFormat.format(d.getValue().getBiayaPerPorsi())));

        tblMenu.getColumns().addAll(List.of(colKodeMenu, colNamaMenu, colKalori, colProtein, colKarbo, colBiaya));

        PengelolaMenu pengelolaMenu = repo.getPengelolaMenu();

        Runnable segarkanTabelMenu = () -> {
            String cari = txtCariMenu.getText() == null ? "" : txtCariMenu.getText().trim();
            if (cari.isEmpty()) {
                tblMenu.setItems(FXCollections.observableArrayList(pengelolaMenu.getDaftarMenu()));
            } else {
                Menu ketemu = pengelolaMenu.cariMenu(cari);
                if (ketemu != null) {
                    tblMenu.setItems(FXCollections.observableArrayList(List.of(ketemu)));
                } else {
                    tblMenu.setItems(FXCollections.observableArrayList());
                }
            }
        };
        txtCariMenu.textProperty().addListener((o, ov, nv) -> segarkanTabelMenu.run());
        segarkanTabelMenu.run();

        Path pathMenuFile = Paths.get("menu_mbg.txt");

        btnSimpanFileMenu.setOnAction(e -> {
            try {
                repo.simpanMenuKeFile(pathMenuFile);
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Persistensi File Berhasil",
                        "Data koleksi menu MBG berhasil disimpan ke file teks.",
                        "Lokasi file: " + pathMenuFile.toAbsolutePath() + "\nTotal data tersimpan: " + pengelolaMenu.getJumlahMenu() + " menu.");
            } catch (IllegalArgumentException ex) {
                tampilkanAlert(Alert.AlertType.WARNING,
                        "Data Ditolak",
                        "Data mengandung karakter pemisah terlarang.",
                        ex.getMessage());
            } catch (IOException ex) {
                tampilkanAlert(Alert.AlertType.ERROR,
                        "Operasi File Gagal",
                        "Gagal menyimpan data ke file teks.",
                        ex.getMessage());
            }
        });

        btnMuatFileMenu.setOnAction(e -> {
            try {
                repo.muatMenuDariFile(pathMenuFile);
                segarkanTabelMenu.run();
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Pemuatan File Berhasil",
                        "Koleksi menu berhasil dimuat dari file teks ke memori.",
                        "Lokasi file: " + pathMenuFile.toAbsolutePath() + "\nTotal data aktif: " + pengelolaMenu.getJumlahMenu() + " menu.");
            } catch (IOException ex) {
                tampilkanAlert(Alert.AlertType.ERROR,
                        "Operasi File Gagal",
                        "Pemuatan file ditolak (State Invariance: Data memori aman).",
                        ex.getMessage() + "\nKoleksi lama tetap utuh.");
            }
        });

        Label lblKomposisiTitle = new Label("Tabel Kalkulasi Kebutuhan Bahan Baku (Relasi Aggregation)");
        lblKomposisiTitle.getStyleClass().add("section-header");

        TableView<Map.Entry<BahanBaku, Integer>> tblKomposisi = new TableView<>();
        tblKomposisi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblKomposisi.setPlaceholder(new Label("Pilih paket menu di atas untuk melihat komposisi bahan baku."));
        VBox.setVgrow(tblKomposisi, Priority.ALWAYS);

        TextField txtTargetPorsi = new TextField("100");
        txtTargetPorsi.setPromptText("Target porsi produksi (misal: 100)");

        TableColumn<Map.Entry<BahanBaku, Integer>, String> colBhnNama = new TableColumn<>("Bahan Baku");
        colBhnNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKey().getNamaBahan()));

        TableColumn<Map.Entry<BahanBaku, Integer>, String> colTakaran100 = new TableColumn<>("Takaran / 100 Porsi");
        colTakaran100.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getValue() + " " + d.getValue().getKey().getSatuan()
        ));

        TableColumn<Map.Entry<BahanBaku, Integer>, String> colKebutuhanTotal = new TableColumn<>("Kebutuhan Porsi");
        colKebutuhanTotal.setCellValueFactory(d -> {
            Menu m = tblMenu.getSelectionModel().getSelectedItem();
            int porsi = parsePorsiAman(txtTargetPorsi.getText());
            if (m == null || porsi <= 0) {
                return new SimpleStringProperty("-");
            }
            int butuh = m.hitungKebutuhanBahan(d.getValue().getKey(), porsi);
            return new SimpleStringProperty(butuh + " " + d.getValue().getKey().getSatuan());
        });

        TableColumn<Map.Entry<BahanBaku, Integer>, String> colStokTersedia = new TableColumn<>("Stok Gudang");
        colStokTersedia.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getKey().getStok() + " " + d.getValue().getKey().getSatuan()
        ));

        TableColumn<Map.Entry<BahanBaku, Integer>, String> colStatusCukup = new TableColumn<>("Status Kecukupan");
        colStatusCukup.setCellValueFactory(d -> {
            Menu m = tblMenu.getSelectionModel().getSelectedItem();
            int porsi = parsePorsiAman(txtTargetPorsi.getText());
            BahanBaku b = d.getValue().getKey();
            if (b.isKedaluwarsa()) {
                return new SimpleStringProperty("DITOLAK: KEDALUWARSA");
            }
            if (m == null || porsi <= 0) {
                return new SimpleStringProperty("Target porsi > 0");
            }
            int butuh = m.hitungKebutuhanBahan(b, porsi);
            if (butuh > b.getStok()) {
                return new SimpleStringProperty("KURANG " + (butuh - b.getStok()) + " " + b.getSatuan());
            }
            return new SimpleStringProperty("CUKUP (Sisa " + (b.getStok() - butuh) + " " + b.getSatuan() + ")");
        });

        tblKomposisi.getColumns().addAll(List.of(colBhnNama, colTakaran100, colKebutuhanTotal, colStokTersedia, colStatusCukup));

        masterBox.getChildren().addAll(menuHeaderBar, tblMenu, lblKomposisiTitle, tblKomposisi);

        VBox detailBox = new VBox(12);
        detailBox.getStyleClass().add("card-elevated");
        detailBox.setPrefWidth(430);
        detailBox.setMinWidth(410);

        Label lblFormMenuTitle = new Label("Form Kelola Menu (Operasi ArrayList & Persistensi File P12)");
        lblFormMenuTitle.getStyleClass().add("section-header");

        GridPane gridMenu = new GridPane();
        gridMenu.setHgap(8);
        gridMenu.setVgap(6);

        TextField txtKodeMenu = new TextField();
        txtKodeMenu.setPromptText("Contoh: MNU-04 (Identitas Unik)");
        TextField txtNamaMenu = new TextField();
        txtNamaMenu.setPromptText("Nama hidangan bergizi");
        TextField txtKaloriMenu = new TextField();
        txtKaloriMenu.setPromptText("Kalori porsi (kkal > 0)");
        TextField txtProteinMenu = new TextField();
        txtProteinMenu.setPromptText("Protein (gram >= 0)");
        TextField txtKarboMenu = new TextField();
        txtKarboMenu.setPromptText("Karbohidrat (gram >= 0)");
        TextField txtBiayaMenu = new TextField();
        txtBiayaMenu.setPromptText("Biaya per porsi (Rp > 0)");

        tambahBarisGrid(gridMenu, 0, "Kode Menu (ID):", txtKodeMenu);
        tambahBarisGrid(gridMenu, 1, "Nama Menu:", txtNamaMenu);
        tambahBarisGrid(gridMenu, 2, "Kalori (kkal):", txtKaloriMenu);
        tambahBarisGrid(gridMenu, 3, "Protein (g):", txtProteinMenu);
        tambahBarisGrid(gridMenu, 4, "Karbohidrat (g):", txtKarboMenu);
        tambahBarisGrid(gridMenu, 5, "Biaya Porsi (Rp):", txtBiayaMenu);

        HBox boxMenuButtons = new HBox(8);
        Button btnTambahMenu = new Button("Tambah Menu");
        btnTambahMenu.getStyleClass().add("btn-primary");
        Button btnBersihMenu = new Button("Bersihkan");
        btnBersihMenu.getStyleClass().add("btn-secondary");
        Button btnHapusMenu = new Button("Hapus");
        btnHapusMenu.getStyleClass().add("btn-danger");
        boxMenuButtons.getChildren().addAll(btnTambahMenu, btnBersihMenu, btnHapusMenu);

        Separator sepMenu = new Separator();

        Label lblCalcHeader = new Label("Kalkulator & Alokasi Porsi Produksi");
        lblCalcHeader.getStyleClass().add("section-header");

        Label lblMenuDipilih = new Label("-");
        lblMenuDipilih.getStyleClass().add("field-label");
        lblMenuDipilih.setWrapText(true);

        VBox boxInputPorsi = new VBox(6);
        Label lblInputPorsi = new Label("Jumlah Porsi Masak:");
        lblInputPorsi.getStyleClass().add("field-label");
        boxInputPorsi.getChildren().addAll(lblInputPorsi, txtTargetPorsi);

        VBox boxRingkasanGizi = new VBox(6);
        boxRingkasanGizi.getStyleClass().add("banner-info");
        Label lblTotalKalori = new Label("Total Energi: -");
        Label lblTotalProtein = new Label("Total Protein: -");
        Label lblEstimasiBiaya = new Label("Estimasi Biaya: -");
        lblEstimasiBiaya.getStyleClass().add("field-label");
        boxRingkasanGizi.getChildren().addAll(lblTotalKalori, lblTotalProtein, lblEstimasiBiaya);

        Button btnAlokasiProduksi = new Button("Alokasikan & Potong Stok Gudang");
        btnAlokasiProduksi.getStyleClass().add("btn-accent");
        btnAlokasiProduksi.setMaxWidth(Double.MAX_VALUE);

        Label lblLogAlokasi = new Label("Status PengelolaMenu: " + pengelolaMenu.getJumlahMenu() + " menu tersimpan.");
        lblLogAlokasi.setWrapText(true);
        lblLogAlokasi.getStyleClass().add("page-subtitle");

        Runnable perbaruiKalkulasiUI = () -> {
            Menu dipilih = tblMenu.getSelectionModel().getSelectedItem();
            if (dipilih == null) {
                return;
            }
            lblMenuDipilih.setText(dipilih.getKodeMenu() + " - " + dipilih.getNamaMenu());
            tblKomposisi.setItems(FXCollections.observableArrayList(dipilih.getKomposisiPer100Porsi().entrySet()));
            tblKomposisi.refresh();

            int porsi = parsePorsiAman(txtTargetPorsi.getText());
            if (porsi > 0) {
                lblTotalKalori.setText(String.format("Total Energi (%d porsi): %,.0f kkal",
                        porsi, dipilih.getKaloriPerPorsi() * porsi));
                lblTotalProtein.setText(String.format("Total Protein: %,.1f gram",
                        dipilih.getProteinGram() * porsi));
                lblEstimasiBiaya.setText("Estimasi Biaya: " + rupiahFormat.format(dipilih.getBiayaPerPorsi() * porsi));
            } else {
                lblTotalKalori.setText("Masukkan jumlah porsi > 0");
                lblTotalProtein.setText("-");
                lblEstimasiBiaya.setText("-");
            }
        };

        tblMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, m) -> {
            if (m != null) {
                txtKodeMenu.setText(m.getKodeMenu());
                txtNamaMenu.setText(m.getNamaMenu());
                txtKaloriMenu.setText(String.valueOf(m.getKaloriPerPorsi()));
                txtProteinMenu.setText(String.valueOf(m.getProteinGram()));
                txtKarboMenu.setText(String.valueOf(m.getKarbohidratGram()));
                txtBiayaMenu.setText(String.valueOf((long) m.getBiayaPerPorsi()));
                perbaruiKalkulasiUI.run();
            }
        });
        txtTargetPorsi.textProperty().addListener((o, ov, nv) -> perbaruiKalkulasiUI.run());

        btnBersihMenu.setOnAction(e -> {
            tblMenu.getSelectionModel().clearSelection();
            txtKodeMenu.clear();
            txtNamaMenu.clear();
            txtKaloriMenu.clear();
            txtProteinMenu.clear();
            txtKarboMenu.clear();
            txtBiayaMenu.clear();
            txtCariMenu.clear();
            lblLogAlokasi.setText("Status PengelolaMenu: " + pengelolaMenu.getJumlahMenu() + " menu tersimpan.");
        });

        btnTambahMenu.setOnAction(e -> {
            try {
                double kalori = Double.parseDouble(txtKaloriMenu.getText().trim());
                double protein = Double.parseDouble(txtProteinMenu.getText().trim());
                double karbo = Double.parseDouble(txtKarboMenu.getText().trim());
                double biaya = Double.parseDouble(txtBiayaMenu.getText().trim());

                Menu menuBaru = new Menu(
                        txtKodeMenu.getText(),
                        txtNamaMenu.getText(),
                        kalori,
                        protein,
                        karbo,
                        biaya
                );
                pengelolaMenu.tambahMenu(menuBaru);
                repo.simpanAtauPerbaruiMenu(menuBaru);
                segarkanTabelMenu.run();
                lblLogAlokasi.setText("Menu '" + menuBaru.getKodeMenu() + "' berhasil ditambahkan. Total menu: " + pengelolaMenu.getJumlahMenu());
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Menu Berhasil Ditambahkan",
                        "Menu " + menuBaru.getNamaMenu() + " tersimpan dalam List<Menu>.",
                        "Total koleksi: " + pengelolaMenu.getJumlahMenu() + " menu.");
            } catch (NumberFormatException ex) {
                tampilkanAlert(Alert.AlertType.ERROR,
                        "Format Angka Salah",
                        "Ditolak: Kalori, protein, karbohidrat, dan biaya harus berupa bilangan numerik.",
                        ex.getMessage());
            } catch (IllegalArgumentException ex) {
                tampilkanAlert(Alert.AlertType.WARNING,
                        "Validasi Koleksi Ditolak (Duplikasi / Null / Format Kosong)",
                        "Ditolak: " + ex.getMessage(),
                        "Operasi tambah digagalkan. Jumlah data koleksi tidak berubah.");
            } finally {
                tblMenu.refresh();
            }
        });

        btnHapusMenu.setOnAction(e -> {
            Menu dipilih = tblMenu.getSelectionModel().getSelectedItem();
            if (dipilih == null) {
                tampilkanAlert(Alert.AlertType.WARNING, "Pilih Menu", "Pilih menu pada tabel sebelum menghapus.", "");
                return;
            }
            boolean terhapus = pengelolaMenu.hapusMenu(dipilih.getKodeMenu());
            if (terhapus) {
                repo.hapusMenu(dipilih.getKodeMenu());
                segarkanTabelMenu.run();
                btnBersihMenu.fire();
                lblLogAlokasi.setText("Menu '" + dipilih.getKodeMenu() + "' berhasil dihapus. Sisa: " + pengelolaMenu.getJumlahMenu());
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Menu Dihapus",
                        "Menu " + dipilih.getNamaMenu() + " berhasil dihapus dari collection.",
                        "Sisa data: " + pengelolaMenu.getJumlahMenu() + " menu.");
            } else {
                tampilkanAlert(Alert.AlertType.WARNING, "Gagal Menghapus", "Kode menu tidak ditemukan dalam collection.", "");
            }
        });

        btnAlokasiProduksi.setOnAction(e -> {
            Menu dipilih = tblMenu.getSelectionModel().getSelectedItem();
            if (dipilih == null) {
                tampilkanAlert(Alert.AlertType.WARNING, "Pilih Menu", "Pilih paket menu yang akan diproduksi.", "");
                return;
            }
            String inputPorsi = txtTargetPorsi.getText() == null ? "" : txtTargetPorsi.getText().trim();
            try {
                int targetPorsi = Integer.parseInt(inputPorsi);
                dipilih.alokasikanBahanUntukProduksi(targetPorsi);
                for (BahanBaku b : dipilih.getKomposisiPer100Porsi().keySet()) {
                    repo.simpanAtauPerbaruiBahanBaku(b);
                }
                double biayaProduksi = dipilih.getBiayaPerPorsi() * targetPorsi;
                repo.catatBiayaProduksi(biayaProduksi);

                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Produksi Menu Berhasil Dialokasikan",
                        "Stok bahan baku untuk " + targetPorsi + " porsi " + dipilih.getNamaMenu() + " telah dipotong secara atomik.",
                        "Biaya produksi sebesar " + rupiahFormat.format(biayaProduksi) + " telah dicatat pada serapan anggaran.");
            } catch (NumberFormatException ex) {
                tampilkanAlert(Alert.AlertType.ERROR,
                        "Format Porsi Salah",
                        "Ditolak: Jumlah porsi harus berupa bilangan bulat.",
                        "Input Anda: \"" + inputPorsi + "\".");
            } catch (IllegalArgumentException ex) {
                tampilkanAlert(Alert.AlertType.WARNING,
                        "Validasi Produksi Ditolak",
                        "Ditolak: " + ex.getMessage(),
                        "Stok bahan baku tidak berkurang.");
            } catch (StokTidakCukupException ex) {
                tampilkanAlert(Alert.AlertType.ERROR,
                        "Gagal Alokasi Produksi (StokTidakCukupException)",
                        "Ditolak: " + ex.getMessage(),
                        "Validasi stok menggagalkan proses alokasi.");
            } finally {
                perbaruiKalkulasiUI.run();
                lblLogAlokasi.setText("Pemeriksaan state selesai (finally): Komposisi stok diperbarui.");
            }
        });

        if (!tblMenu.getItems().isEmpty()) {
            tblMenu.getSelectionModel().selectFirst();
        }

        detailBox.getChildren().addAll(
                lblFormMenuTitle,
                gridMenu,
                boxMenuButtons,
                sepMenu,
                lblCalcHeader,
                lblMenuDipilih,
                boxInputPorsi,
                boxRingkasanGizi,
                btnAlokasiProduksi,
                lblLogAlokasi
        );

        masterDetail.getChildren().addAll(masterBox, detailBox);
        setKontenUtama(masterDetail);
    }

    private int parsePorsiAman(String teks) {
        try {
            return Integer.parseInt(teks == null ? "0" : teks.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @FXML
    public void tampilkanDistribusi() {
        modulAktif = "DISTRIBUSI";
        aturNavigasiAktif(btnNavDistribusi,
                "Distribusi Armada & Pelacakan Pengiriman (Modul 11)",
                "Pengelolaan PengelolaDistribusi (List<Pengiriman>) dan DetailPengiriman (Composition)");

        HBox masterDetail = new HBox(16);

        VBox masterBox = new VBox(12);
        masterBox.getStyleClass().add("card-flat");
        HBox.setHgrow(masterBox, Priority.ALWAYS);

        HBox distHeaderBar = new HBox(10);
        distHeaderBar.setAlignment(Pos.CENTER_LEFT);
        Label lblMasterTitle = new Label("Tabel Pelacakan Armada Distribusi (List<Pengiriman>)");
        lblMasterTitle.getStyleClass().add("section-header");
        HBox.setHgrow(lblMasterTitle, Priority.ALWAYS);

        Label lblCariDist = new Label("Cari ID:");
        lblCariDist.getStyleClass().add("field-label");
        TextField txtCariDist = new TextField();
        txtCariDist.setPromptText("ID Kirim...");
        txtCariDist.setPrefWidth(130);

        distHeaderBar.getChildren().addAll(lblMasterTitle, lblCariDist, txtCariDist);

        TableView<Pengiriman> tblDistribusi = new TableView<>();
        tblDistribusi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblDistribusi.setPlaceholder(new Label("Belum ada jadwal pengiriman armada."));
        VBox.setVgrow(tblDistribusi, Priority.ALWAYS);

        TableColumn<Pengiriman, String> colId = new TableColumn<>("ID Kirim");
        colId.setMaxWidth(135);
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getIdPengiriman()));

        TableColumn<Pengiriman, String> colSekolah = new TableColumn<>("Sekolah Tujuan");
        colSekolah.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSekolahTujuan().getNamaSekolah()));

        TableColumn<Pengiriman, String> colDriver = new TableColumn<>("Armada & Driver");
        colDriver.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDriverArmada().toString()));

        TableColumn<Pengiriman, String> colPorsi = new TableColumn<>("Porsi");
        colPorsi.setMaxWidth(85);
        colPorsi.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJumlahPorsi() + " porsi"));

        TableColumn<Pengiriman, String> colStatus = new TableColumn<>("Status");
        colStatus.setMaxWidth(115);
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatusDistribusi()));
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    if ("Delivered".equalsIgnoreCase(item)) {
                        badge.getStyleClass().add("status-badge-ok");
                    } else if ("In Transit".equalsIgnoreCase(item)) {
                        badge.getStyleClass().add("status-badge-warn");
                    } else {
                        badge.getStyleClass().add("status-badge-danger");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        TableColumn<Pengiriman, String> colLokasi = new TableColumn<>("Lokasi Terlacak (DapatDilacak.getLokasi())");
        colLokasi.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLokasi()));

        tblDistribusi.getColumns().addAll(List.of(colId, colSekolah, colDriver, colPorsi, colStatus, colLokasi));

        PengelolaDistribusi pengelolaDist = repo.getPengelolaDistribusi();

        Runnable segarkanDistribusi = () -> {
            String cari = txtCariDist.getText() == null ? "" : txtCariDist.getText().trim();
            if (cari.isEmpty()) {
                tblDistribusi.setItems(FXCollections.observableArrayList(pengelolaDist.getDaftarPengiriman()));
            } else {
                Pengiriman ketemu = pengelolaDist.cariPengiriman(cari);
                if (ketemu != null) {
                    tblDistribusi.setItems(FXCollections.observableArrayList(List.of(ketemu)));
                } else {
                    tblDistribusi.setItems(FXCollections.observableArrayList());
                }
            }
        };
        txtCariDist.textProperty().addListener((o, ov, nv) -> segarkanDistribusi.run());
        segarkanDistribusi.run();

        Label lblSubTitle = new Label("Rincian Batch Kemasan Termal (Relasi Composition)");
        lblSubTitle.getStyleClass().add("section-header");

        TableView<DetailPengiriman> tblDetailBatch = new TableView<>();
        tblDetailBatch.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblDetailBatch.setPrefHeight(185);
        tblDetailBatch.setPlaceholder(new Label("Pilih baris pengiriman di atas untuk melihat komposisi batch kemasan."));

        TableColumn<DetailPengiriman, String> colBatchNo = new TableColumn<>("No. Batch");
        colBatchNo.setMaxWidth(110);
        colBatchNo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNomorBatch()));

        TableColumn<DetailPengiriman, String> colKemasan = new TableColumn<>("Jenis Kemasan");
        colKemasan.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJenisKemasan()));

        TableColumn<DetailPengiriman, String> colBatchPorsi = new TableColumn<>("Kuantitas");
        colBatchPorsi.setMaxWidth(100);
        colBatchPorsi.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJumlahPorsiBatch() + " porsi"));

        TableColumn<DetailPengiriman, String> colSuhu = new TableColumn<>("Suhu Kabin");
        colSuhu.setMaxWidth(100);
        colSuhu.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSuhuPenyimpanan() + " C"));

        TableColumn<DetailPengiriman, String> colCatatan = new TableColumn<>("Catatan Inspeksi");
        colCatatan.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCatatanInspeksi()));

        tblDetailBatch.getColumns().addAll(List.of(colBatchNo, colKemasan, colBatchPorsi, colSuhu, colCatatan));

        masterBox.getChildren().addAll(distHeaderBar, tblDistribusi, lblSubTitle, tblDetailBatch);

        VBox detailBox = new VBox(12);
        detailBox.getStyleClass().add("card-elevated");
        detailBox.setPrefWidth(420);
        detailBox.setMinWidth(400);

        Label lblUpdateHeader = new Label("Kontrol Status & Lokasi Armada");
        lblUpdateHeader.getStyleClass().add("section-header");

        ComboBox<String> cmbStatus = new ComboBox<>(FXCollections.observableArrayList("Pending", "In Transit", "Delivered"));
        cmbStatus.setMaxWidth(Double.MAX_VALUE);
        TextField txtLokasiBaru = new TextField();
        txtLokasiBaru.setPromptText("Koordinat / titik jalan terkini armada");

        Button btnUpdatePelacakan = new Button("Perbarui Status & Lokasi Armada");
        btnUpdatePelacakan.getStyleClass().add("btn-primary");
        btnUpdatePelacakan.setMaxWidth(Double.MAX_VALUE);

        Button btnHapusPengiriman = new Button("Hapus Pengiriman Terpilih");
        btnHapusPengiriman.getStyleClass().add("btn-danger");
        btnHapusPengiriman.setMaxWidth(Double.MAX_VALUE);

        Separator sep = new Separator();

        Label lblBatchHeader = new Label("Tambah Batch Kemasan (Composition)");
        lblBatchHeader.getStyleClass().add("section-header");

        GridPane gridBatch = new GridPane();
        gridBatch.setHgap(8);
        gridBatch.setVgap(8);

        TextField txtNoBatch = new TextField();
        txtNoBatch.setPromptText("Contoh: BATCH-05C");
        TextField txtJenisKemasan = new TextField("Food Tray Stainless 304");
        TextField txtPorsiBatch = new TextField();
        txtPorsiBatch.setPromptText("Jumlah porsi (bulat > 0)");
        TextField txtSuhuBatch = new TextField("65.0");
        TextField txtCatatanBatch = new TextField("Segel utuh & layak distribusi");

        tambahBarisGrid(gridBatch, 0, "Nomor Batch:", txtNoBatch);
        tambahBarisGrid(gridBatch, 1, "Kemasan:", txtJenisKemasan);
        tambahBarisGrid(gridBatch, 2, "Porsi Batch:", txtPorsiBatch);
        tambahBarisGrid(gridBatch, 3, "Suhu (C):", txtSuhuBatch);
        tambahBarisGrid(gridBatch, 4, "Inspeksi:", txtCatatanBatch);

        Button btnTambahBatch = new Button("Tambahkan DetailBatch ke Pengiriman");
        btnTambahBatch.getStyleClass().add("btn-accent");
        btnTambahBatch.setMaxWidth(Double.MAX_VALUE);

        tblDistribusi.getSelectionModel().selectedItemProperty().addListener((obs, ov, p) -> {
            if (p != null) {
                cmbStatus.setValue(p.getStatusDistribusi());
                txtLokasiBaru.setText(p.getLokasiTerakhir());
                tblDetailBatch.setItems(FXCollections.observableArrayList(p.getDaftarDetailBatch()));
            }
        });

        btnUpdatePelacakan.setOnAction(e -> {
            Pengiriman p = tblDistribusi.getSelectionModel().getSelectedItem();
            if (p == null) {
                tampilkanAlert(Alert.AlertType.WARNING, "Pilih Pengiriman", "Pilih baris pengiriman pada tabel.", "");
                return;
            }
            try {
                p.setStatusDistribusi(cmbStatus.getValue());
                p.perbaruiLokasi(txtLokasiBaru.getText());
                repo.simpanAtauPerbaruiPengiriman(p);
                tblDistribusi.refresh();
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Pelacakan Armada Diperbarui",
                        "Lokasi terkini: " + p.getLokasi(),
                        "Driver " + p.getDriverArmada().getNama() + " tersinkronisasi via interface DapatDilacak.");
            } catch (IllegalArgumentException ex) {
                tampilkanAlert(Alert.AlertType.WARNING, "Validasi Pelacakan Ditolak", ex.getMessage(), "");
            } finally {
                tblDistribusi.refresh();
            }
        });

        btnHapusPengiriman.setOnAction(e -> {
            Pengiriman p = tblDistribusi.getSelectionModel().getSelectedItem();
            if (p == null) {
                tampilkanAlert(Alert.AlertType.WARNING, "Pilih Pengiriman", "Pilih pengiriman yang akan dihapus.", "");
                return;
            }
            boolean sukses = pengelolaDist.hapusPengiriman(p.getIdPengiriman());
            if (sukses) {
                repo.hapusPengiriman(p.getIdPengiriman());
                segarkanDistribusi.run();
                tblDetailBatch.setItems(FXCollections.observableArrayList());
                tampilkanAlert(Alert.AlertType.INFORMATION, "Pengiriman Dihapus",
                        "Data distribusi " + p.getIdPengiriman() + " dihapus.",
                        "Sisa pengiriman: " + pengelolaDist.getJumlahPengiriman());
            }
        });

        btnTambahBatch.setOnAction(e -> {
            Pengiriman p = tblDistribusi.getSelectionModel().getSelectedItem();
            if (p == null) {
                tampilkanAlert(Alert.AlertType.WARNING, "Pilih Pengiriman", "Pilih pengiriman induk terlebih dahulu.", "");
                return;
            }
            try {
                int porsiB = Integer.parseInt(txtPorsiBatch.getText().trim());
                double suhuB = Double.parseDouble(txtSuhuBatch.getText().trim());
                DetailPengiriman detailBaru = p.buatDetailBatch(
                        txtNoBatch.getText(),
                        txtJenisKemasan.getText(),
                        porsiB,
                        suhuB,
                        txtCatatanBatch.getText()
                );
                repo.simpanDetailBatch(p.getIdPengiriman(), detailBaru);
                tblDetailBatch.setItems(FXCollections.observableArrayList(p.getDaftarDetailBatch()));
                txtNoBatch.clear();
                txtPorsiBatch.clear();
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Detail Batch Ditambahkan (Composition)",
                        "Objek DetailPengiriman berhasil diinstansiasi di dalam pengiriman " + p.getIdPengiriman() + ".",
                        "Total batch: " + p.getDaftarDetailBatch().size());
            } catch (NumberFormatException ex) {
                tampilkanAlert(Alert.AlertType.ERROR, "Format Angka Salah",
                        "Ditolak: Porsi batch harus bilangan bulat dan suhu berupa angka.", ex.getMessage());
            } catch (IllegalArgumentException ex) {
                tampilkanAlert(Alert.AlertType.WARNING, "Validasi Batch Ditolak", ex.getMessage(), "");
            } finally {
                tblDetailBatch.refresh();
            }
        });

        if (!tblDistribusi.getItems().isEmpty()) {
            tblDistribusi.getSelectionModel().selectFirst();
        }

        detailBox.getChildren().addAll(
                lblUpdateHeader,
                new Label("Status Pengiriman:"),
                cmbStatus,
                new Label("Titik Lokasi Armada Terkini:"),
                txtLokasiBaru,
                btnUpdatePelacakan,
                btnHapusPengiriman,
                sep,
                lblBatchHeader,
                gridBatch,
                btnTambahBatch
        );

        masterDetail.getChildren().addAll(masterBox, detailBox);
        setKontenUtama(masterDetail);
    }

    @FXML
    public void tampilkanSekolah() {
        modulAktif = "SEKOLAH";
        aturNavigasiAktif(btnNavSekolah,
                "Data Sekolah Penerima Manfaat & Kuota Siswa (Modul 11)",
                "Koleksi List<Sekolah> dengan validasi NPSN unik 8 digit dan kuota siswa penerima");

        HBox masterDetail = new HBox(16);

        VBox masterBox = new VBox(12);
        masterBox.getStyleClass().add("card-flat");
        HBox.setHgrow(masterBox, Priority.ALWAYS);

        Label lblMasterTitle = new Label("Daftar Sekolah Sasaran Program MBG [Data Simulasi]");
        lblMasterTitle.getStyleClass().add("section-header");

        TableView<Sekolah> tblSekolah = new TableView<>();
        tblSekolah.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblSekolah.setPlaceholder(new Label("Belum ada sekolah terdaftar. Tambahkan melalui form di sebelah kanan."));
        VBox.setVgrow(tblSekolah, Priority.ALWAYS);

        TableColumn<Sekolah, String> colNpsn = new TableColumn<>("NPSN");
        colNpsn.setMaxWidth(100);
        colNpsn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNpsn()));

        TableColumn<Sekolah, String> colNama = new TableColumn<>("Nama Sekolah");
        colNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaSekolah()));

        TableColumn<Sekolah, String> colJenjang = new TableColumn<>("Jenjang");
        colJenjang.setMaxWidth(85);
        colJenjang.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJenjang()));

        TableColumn<Sekolah, String> colSiswa = new TableColumn<>("Jumlah Siswa");
        colSiswa.setMaxWidth(120);
        colSiswa.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJumlahSiswa() + " Siswa"));

        TableColumn<Sekolah, String> colAlamat = new TableColumn<>("Alamat Lengkap");
        colAlamat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAlamat()));

        TableColumn<Sekolah, String> colKoordinat = new TableColumn<>("Koordinat Wilayah");
        colKoordinat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKoordinatWilayah()));

        tblSekolah.getColumns().addAll(List.of(colNpsn, colNama, colJenjang, colSiswa, colAlamat, colKoordinat));
        tblSekolah.setItems(FXCollections.observableArrayList(repo.getDaftarSekolah()));

        masterBox.getChildren().addAll(lblMasterTitle, tblSekolah);

        VBox detailBox = new VBox(12);
        detailBox.getStyleClass().add("card-elevated");
        detailBox.setPrefWidth(410);
        detailBox.setMinWidth(390);

        Label lblFormHeader = new Label("Form Data Sekolah & Kuota Siswa");
        lblFormHeader.getStyleClass().add("section-header");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        TextField txtNpsn = new TextField();
        txtNpsn.setPromptText("8 digit angka (misal: 20529906)");
        TextField txtNama = new TextField();
        txtNama.setPromptText("Nama sekolah penerima");
        ComboBox<String> cmbJenjang = new ComboBox<>(FXCollections.observableArrayList("SD", "SMP", "SMA", "SMK"));
        cmbJenjang.setMaxWidth(Double.MAX_VALUE);
        cmbJenjang.getSelectionModel().selectFirst();
        TextField txtSiswa = new TextField();
        txtSiswa.setPromptText("Jumlah siswa (bulat > 0)");
        TextField txtAlamat = new TextField();
        txtAlamat.setPromptText("Alamat jalan dan kecamatan");
        TextField txtKoordinat = new TextField();
        txtKoordinat.setPromptText("Contoh: -7.0054, 113.8602");

        tambahBarisGrid(grid, 0, "NPSN (8 Digit):", txtNpsn);
        tambahBarisGrid(grid, 1, "Nama Sekolah:", txtNama);
        tambahBarisGrid(grid, 2, "Jenjang:", cmbJenjang);
        tambahBarisGrid(grid, 3, "Jumlah Siswa:", txtSiswa);
        tambahBarisGrid(grid, 4, "Alamat:", txtAlamat);
        tambahBarisGrid(grid, 5, "Koordinat:", txtKoordinat);

        HBox boxButtons = new HBox(8);
        Button btnSimpan = new Button("Simpan Sekolah");
        btnSimpan.getStyleClass().add("btn-primary");
        Button btnBersih = new Button("Bersihkan");
        btnBersih.getStyleClass().add("btn-secondary");
        Button btnHapus = new Button("Hapus");
        btnHapus.getStyleClass().add("btn-danger");
        boxButtons.getChildren().addAll(btnSimpan, btnBersih, btnHapus);

        Label lblTotalFoot = new Label();
        lblTotalFoot.getStyleClass().add("field-label");
        Runnable updateRingkasan = () -> lblTotalFoot.setText(
                "Total penerima terdaftar: " + repo.getDaftarSekolah().size() + " sekolah (" + repo.getTotalSiswaTerdaftar() + " siswa)"
        );
        updateRingkasan.run();

        tblSekolah.getSelectionModel().selectedItemProperty().addListener((obs, ov, s) -> {
            if (s != null) {
                txtNpsn.setText(s.getNpsn());
                txtNama.setText(s.getNamaSekolah());
                cmbJenjang.setValue(s.getJenjang());
                txtSiswa.setText(String.valueOf(s.getJumlahSiswa()));
                txtAlamat.setText(s.getAlamat());
                txtKoordinat.setText(s.getKoordinatWilayah());
            }
        });

        btnBersih.setOnAction(e -> {
            tblSekolah.getSelectionModel().clearSelection();
            txtNpsn.clear();
            txtNama.clear();
            txtSiswa.clear();
            txtAlamat.clear();
            txtKoordinat.clear();
        });

        btnSimpan.setOnAction(e -> {
            try {
                int jmlSiswa = Integer.parseInt(txtSiswa.getText().trim());
                Sekolah s = new Sekolah(
                        txtNpsn.getText(),
                        txtNama.getText(),
                        cmbJenjang.getValue(),
                        txtAlamat.getText(),
                        jmlSiswa,
                        txtKoordinat.getText()
                );
                repo.simpanAtauPerbaruiSekolah(s);
                tblSekolah.setItems(FXCollections.observableArrayList(repo.getDaftarSekolah()));
                updateRingkasan.run();
                tampilkanAlert(Alert.AlertType.INFORMATION,
                        "Data Sekolah Tersimpan",
                        s.getNamaSekolah() + " berhasil disimpan.",
                        "Kuota penerima: " + s.getJumlahSiswa() + " siswa.");
            } catch (NumberFormatException ex) {
                tampilkanAlert(Alert.AlertType.ERROR, "Format Jumlah Siswa Salah",
                        "Ditolak: Jumlah siswa harus bilangan bulat.", ex.getMessage());
            } catch (IllegalArgumentException ex) {
                tampilkanAlert(Alert.AlertType.WARNING, "Validasi Data Sekolah Ditolak",
                        "Ditolak: " + ex.getMessage(), "");
            } finally {
                tblSekolah.refresh();
            }
        });

        btnHapus.setOnAction(e -> {
            Sekolah s = tblSekolah.getSelectionModel().getSelectedItem();
            if (s == null) {
                tampilkanAlert(Alert.AlertType.WARNING, "Pilih Sekolah", "Pilih baris sekolah yang ingin dihapus.", "");
                return;
            }
            repo.hapusSekolah(s.getNpsn());
            tblSekolah.setItems(FXCollections.observableArrayList(repo.getDaftarSekolah()));
            updateRingkasan.run();
            btnBersih.fire();
        });

        if (!tblSekolah.getItems().isEmpty()) {
            tblSekolah.getSelectionModel().selectFirst();
        }

        detailBox.getChildren().addAll(lblFormHeader, grid, boxButtons, new Separator(), lblTotalFoot);
        masterDetail.getChildren().addAll(masterBox, detailBox);
        setKontenUtama(masterDetail);
    }

    private void tambahBarisGrid(GridPane grid, int row, String labelText, Control field) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("field-label");
        lbl.setMinWidth(135);
        GridPane.setHgrow(field, Priority.ALWAYS);
        grid.add(lbl, 0, row);
        grid.add(field, 1, row);
    }

    private void setKontenUtama(Node node) {
        contentArea.getChildren().setAll(node);
    }

    private void tampilkanAlert(Alert.AlertType tipe, String judul, String header, String isi) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(header);
        alert.setContentText(isi);
        alert.showAndWait();
    }
}

