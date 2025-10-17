import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductMenu extends JFrame{
    public static void main(String[] args){
        //Buat object window
        ProductMenu menu = new ProductMenu();

        //Atur ukuran window
        menu.setSize(700, 600);

        //Letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        //Isi window
        menu.setContentPane(menu.mainPanel);

        //Ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        //Tampilkan window
        menu.setVisible(true);

        //Agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // index baris yang diklik
    private int selectedIndex = -1;
    private Database database;

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JSlider ratingSlider;

    //Constructor
    public ProductMenu(){
        //Buat objek database
        database = new Database();

        //Isi tabel produk
        productTable.setModel(setTable());

        //Ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        //Atur isi combo box
        String[] kategoriData = { "???", "Elektronik", "Makanan", "Minuman", "Pakaian", "Alat Tulis"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        //Atur nilai (minimal sampai maksimal) dari slider untuk rating produk
        ratingSlider.setMinimum(1);
        ratingSlider.setMaximum(5);
        ratingSlider.setValue(3);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);

        //Sembunyikan button delete
        deleteButton.setVisible(false);

        //Saat tombol add atau update ditekan
        addUpdateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(selectedIndex == -1){
                    insertData();
                }else{
                    updateData();
                }
            }
        });

        //Saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // Tampilkan dialog konfirmasi
                int konfirmasi = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION
                );

                // Jika pengguna menekan YES, lanjutkan proses hapus
                if (konfirmasi == JOptionPane.YES_OPTION){
                    deleteData();
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                }else{
                    // Jika pengguna memilih NO, batalkan penghapusan
                    JOptionPane.showMessageDialog(null, "Penghapusan dibatalkan.");
                }
            }
        });

        //Saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                clearForm();
            }
        });

        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                selectedIndex = productTable.getSelectedRow();
                if (selectedIndex != -1) {
                    //Ambil data dari tabel
                    String curID = productTable.getValueAt(selectedIndex, 0).toString();
                    String curNama = productTable.getValueAt(selectedIndex, 1).toString();
                    String curHarga = productTable.getValueAt(selectedIndex, 2).toString();
                    String curKategori = productTable.getValueAt(selectedIndex, 3).toString();
                    String curRating = productTable.getValueAt(selectedIndex, 4).toString();

                    //Tampilkan di form
                    idField.setText(curID);
                    namaField.setText(curNama);
                    hargaField.setText(curHarga);
                    kategoriComboBox.setSelectedItem(curKategori);
                    ratingSlider.setValue(Integer.parseInt(curRating));

                    //Nonaktifkan edit ID saat update
                    idField.setEnabled(false);

                    //Ubah tombol
                    addUpdateButton.setText("Update");
                    deleteButton.setVisible(true);
                }
            }
        });
    }

    public final DefaultTableModel setTable(){
        //Tentukan kolom tabel
        Object[] cols = { "ID Produk", "Nama", "Harga", "Kategori", "Rating" };

        //Buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        //Isi tabel dengan hasil query
        try{
            ResultSet resultSet = database.selectQuery("SELECT * FROM product");

            int i = 0;
            while (resultSet.next()){
                Object[] row = new Object[5];
                row[0] = resultSet.getString("id");
                row[1] = resultSet.getString("nama");
                row[2] = resultSet.getString("harga");
                row[3] = resultSet.getString("kategori");
                row[4] = resultSet.getString("rating");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return tmp;
    }

    public void insertData() {
        try {
            // Ambil value dari textfield dan combobox
            String id = idField.getText();
            String nama = namaField.getText();
            String hargaText = hargaField.getText();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int rating = ratingSlider.getValue();

            // Cek apakah ada yang kosong
            if (id.isEmpty() || nama.isEmpty() || hargaText.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null,
                        "Semua kolom wajib diisi!",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga = Double.parseDouble(hargaText);

            // 🔍 CEK APAKAH ID SUDAH ADA DI DATABASE
            try (ResultSet rs = database.selectQuery("SELECT * FROM product WHERE id = '" + id + "'")) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null,
                            "ID Produk '" + id + "' sudah terdaftar!\nGunakan ID lain.",
                            "Duplikasi ID",
                            JOptionPane.ERROR_MESSAGE);
                    return; // hentikan proses insert
                }
            }

            // Jika belum ada, tambahkan data ke dalam database
            String sqlQuery = "INSERT INTO product VALUES ('" + id + "', '" + nama + "', " + harga + ", '" + kategori + "', '" + rating + "')";
            database.insertUpdateDeleteQuery(sqlQuery);

            // Update tabel
            productTable.setModel(setTable());

            // Bersihkan form
            clearForm();

            // Feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan database:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData(){
        try{
            //Ambil data dari form
            String id = idField.getText();
            String nama = namaField.getText();
            String hargaText = hargaField.getText();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int rating = ratingSlider.getValue();

            // ✅ Cek input kosong
            if(id.isEmpty() || nama.isEmpty() || hargaText.isEmpty() || kategori.equals("???")){
                JOptionPane.showMessageDialog(null,
                        "Semua kolom wajib diisi untuk update data!",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga = Double.parseDouble(hargaText);

            //Ubah data produk di list
            String sqlQuery = "UPDATE product SET " +
                    "nama = '" + nama + "', " +
                    "harga = " + harga + ", " +
                    "kategori = '" + kategori + "', " +
                    "rating = " + rating +
                    " WHERE id = '" + id + "'";

            //Jalankan query
            database.insertUpdateDeleteQuery(sqlQuery);

            //Update tabel
            productTable.setModel(setTable());

            //Bersihkan form
            clearForm();

            //Feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData(){
        //Ambil ID produk yang dipilih
        String id = idField.getText();

        //Jalankan query delete di database
        String sqlQuery = "DELETE FROM product WHERE id = '" + id + "'";
        database.insertUpdateDeleteQuery(sqlQuery);

        //Update tabel
        productTable.setModel(setTable());

        //Bersihkan form
        clearForm();

        //Feedback
        System.out.println("Delete berhasil");
    }

    public void clearForm(){
        //Kosongkan semua texfield dan combo box
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        kategoriComboBox.setSelectedIndex(0);

        //Aktifkan kembali field ID
        idField.setEnabled(true);

        //Ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        //Sembunyikan button delete
        deleteButton.setVisible(false);

        //Ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
}