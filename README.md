# TP5DPBO2425C1

# Janji
Saya Rizka Aulia dengan NIM 2403245 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain Pemrograman Berorientasi Objek untuk keberkahan-Nya maka saya tidak akan melakukan kecurangan seperti yang telah di spesifikasikan

# Desain Program
Program Product Menu dibuat untuk melakukan pengelolaan data produk melalui antarmuka grafis berbasis Java Swing yang terhubung langsung dengan database MySQL. Melalui program ini, pengguna dapat melakukan operasi dasar seperti menambahkan produk baru, mengubah (update) data produk, menghapus produk, serta melihat daftar produk yang tersimpan di dalam database. Program ini bersifat terintegrasi, artinya seluruh data disimpan dan diperbarui secara permanen pada tabel product di database db_product, bukan hanya disimpan sementara di memori.

Program ini terdiri dari tiga file utama, yaitu:
1. Product.java: Berfungsi sebagai class model yang merepresentasikan satu entitas produk. Kelas ini menyimpan atribut produk seperti ID produk, nama, harga, kategori, dan rating, serta menyediakan getter dan setter untuk mengakses atau mengubah data tersebut.
2. Database.java: Berfungsi sebagai class koneksi database yang menangani komunikasi antara program dan MySQL. Kelas ini menyediakan metode untuk menjalankan query SELECT, INSERT, UPDATE, dan DELETE, sehingga operasi CRUD dapat dilakukan langsung ke dalam database.
3. ProductMenu.java: Berfungsi sebagai class utama yang mengatur tampilan GUI dan logika pengolahan data produk. Kelas ini menampilkan form input, tabel produk, serta tombol Add, Update, Delete, dan Cancel untuk melakukan operasi CRUD. Data dari database ditampilkan dalam tabel (JTable), sedangkan input pengguna diambil melalui komponen JTextField, JComboBox, dan JSlider untuk menentukan rating produk.

Dengan desain ini, program Product Menu menjadi aplikasi CRUD sederhana yang memiliki antarmuka interaktif, mampu menampilkan serta mengelola data produk secara langsung melalui koneksi database, dan mudah digunakan untuk kebutuhan administrasi data produk di toko atau sistem inventori kecil.

# Alur Program
Saat program dijalankan, kelas ProductMenu akan menjadi titik awal (melalui method main). Program pertama-tama membuat objek ProductMenu, mengatur ukuran jendela, menempatkannya di tengah layar, menampilkan antarmuka (GUI), dan menghubungkannya dengan database melalui objek dari kelas Database. Setelah koneksi berhasil, program langsung memanggil method setTable() untuk mengambil data dari tabel product di database MySQL dan menampilkannya dalam bentuk tabel (JTable) di dalam GUI.

Pada tampilan utama, pengguna dapat mengisi form yang terdiri dari kolom ID Produk, Nama, Harga, Kategori, dan Rating (melalui slider). Jika pengguna menekan tombol Add, program akan mengambil seluruh data input tersebut, memvalidasi agar tidak ada kolom kosong, dan kemudian menjalankan perintah SQL INSERT untuk menambahkan data baru ke database. Setelah data berhasil dimasukkan, tabel akan diperbarui secara otomatis agar menampilkan data terbaru.

Jika pengguna memilih salah satu baris pada tabel, data dari baris tersebut akan muncul di form input. Tombol Add akan berubah menjadi Update, dan tombol Delete akan muncul. Saat tombol Update ditekan, program akan mengambil nilai baru dari form, lalu menjalankan perintah SQL UPDATE untuk memperbarui data produk berdasarkan ID yang dipilih. Setelah itu, tabel kembali diperbarui dan form dibersihkan.

Apabila pengguna ingin menghapus data, tombol Delete akan menampilkan dialog konfirmasi. Jika pengguna memilih YES, maka perintah SQL DELETE akan dijalankan untuk menghapus data produk dari database, kemudian tabel akan diperbarui. Tombol Cancel berfungsi untuk mengosongkan semua field input dan mengembalikan tampilan form ke kondisi semula (mode tambah data).

Secara keseluruhan, alur program ini bersifat siklik — pengguna dapat terus menambah, mengubah, menghapus, dan melihat data produk tanpa perlu menutup aplikasi. Setiap perubahan langsung disimpan dan ditampilkan kembali melalui koneksi database yang aktif.

# Dokumentasi
- ADD
https://github.com/user-attachments/assets/4d08b118-78c8-498f-ad98-59539d7255eb
- UPDATE
https://github.com/user-attachments/assets/8312c3b2-a1a8-4750-815c-388bf7091519
- DELETE
https://github.com/user-attachments/assets/53d23242-e9c6-46b6-bcd7-3e1ad122131a





