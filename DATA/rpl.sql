DROP TABLE IF EXISTS jadwalKuliahMahasiswa;
DROP TABLE IF EXISTS jadwalKuliahDosen;
DROP TABLE IF EXISTS notifikasi;
DROP TABLE IF EXISTS bimbingan;
DROP TABLE IF EXISTS jadwal_bimbingan;
DROP TABLE IF EXISTS penugasan_ta;
DROP TABLE IF EXISTS ta;
DROP TABLE IF EXISTS jadwal_kuliah;
DROP TABLE IF EXISTS ruangan;
DROP TABLE IF EXISTS periode_ta;
DROP TABLE IF EXISTS dosen;
DROP TABLE IF EXISTS mahasiswa;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS users;

-- table user
CREATE TABLE users(
    idUser INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL
);

-- table admin
CREATE TABLE admin (
    idAdmin INT PRIMARY KEY REFERENCES users(idUser)
);

-- table mahasiswa
CREATE TABLE mahasiswa (
    idMhs INT PRIMARY KEY REFERENCES users(idUser),
    npm VARCHAR(10) NOT NULL
);

-- table dosen
CREATE TABLE dosen (
    idDosen INT PRIMARY KEY REFERENCES users(idUser),
    nik VARCHAR(10) NOT NULL
);

-- table periode TA
CREATE TABLE periode_ta (
    idPeriodeTA INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TahunAjaran VARCHAR(10) NOT NULL,
    Semester INT NOT NULL,
    TanggalMulaiPeriodeTA DATE NOT NULL,
    TanggalSelesaiPeriodeTA DATE NOT NULL
);

-- table jadwal kuliah
CREATE TABLE jadwal_kuliah (
    idJadwalKuliah INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hari VARCHAR(10) NOT NULL,
    jamMulai TIME NOT NULL,
    jamSelesai TIME NOT NULL,
    kelas VARCHAR(10) NOT NULL,
    keterangan VARCHAR(100)
);

-- table ruangan
CREATE TABLE ruangan (
    idRuangan INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    namaRuangan VARCHAR(10) NOT NULL,
    statusRuangan VARCHAR(20) NOT NULL
);

-- TA
CREATE TABLE ta (
    idTA INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	codeTA VARCHAR(10) NOT NULL,
    topikTA VARCHAR(200) NOT NULL,
	idDosen INT,
    idPeriodeTA INT,
	FOREIGN KEY (idPeriodeTA) REFERENCES periode_ta(idPeriodeTA),
    FOREIGN KEY (idDosen) REFERENCES dosen(idDosen)
);

-- table penugasan TA
CREATE TABLE penugasan_ta (
    idMhs INT NOT NULL,
    idTA INT NOT NULL, 
    StatusPersyaratan VARCHAR(50),
    PRIMARY KEY (idMhs, idTA),
    FOREIGN KEY (idMhs) REFERENCES mahasiswa(idMhs),
	FOREIGN KEY (idTA) REFERENCES ta(idTA)
);

-- table jadwal bimbingan
CREATE TABLE jadwal_bimbingan (
    idJadwal INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tanggal DATE NOT NULL,
    waktuMulai TIME NOT NULL,
    waktuSelesai TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    idMhs INT,
    idDosen INT,
    idRuangan INT,
    FOREIGN KEY (idMhs) REFERENCES mahasiswa(idMhs),
    FOREIGN KEY (idDosen) REFERENCES dosen(idDosen),
    FOREIGN KEY (idRuangan) REFERENCES ruangan(idRuangan)
);

-- table bimbingan 
CREATE TABLE bimbingan (
    idBimbingan INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idJadwal INT NOT NULL,
    catatan TEXT,
    fileURL VARCHAR(255),
    FOREIGN KEY (idJadwal) REFERENCES jadwal_bimbingan(idJadwal)
);


-- table notifikasi
CREATE TABLE notifikasi (
    idNotif INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idUser INT NOT NULL,
    tipeNotif VARCHAR(50) NOT NULL,
    keterangan TEXT NOT NULL,
    waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idUser) REFERENCES users(idUser)
);

-- table relasi jadwal kuliah mahasiswa
CREATE TABLE jadwalKuliahMahasiswa (
    idMhs INT,
    idJadwalKuliah INT,
    FOREIGN KEY (idMhs) REFERENCES mahasiswa(idMhs),
    FOREIGN KEY (idJadwalKuliah) REFERENCES jadwal_kuliah(idJadwalKuliah)
);

-- table relasi jadwal kuliah dosen
CREATE TABLE jadwalKuliahDosen (
    idDosen INT,
    idJadwalKuliah INT,
    FOREIGN KEY (idDosen) REFERENCES dosen(idDosen),
    FOREIGN KEY (idJadwalKuliah) REFERENCES jadwal_kuliah(idJadwalKuliah)
);

-- Dummy Data
-- table users
INSERT INTO users (name, email, password, role) VALUES
--admin
('Admin Sistem', 'admin@unpar.ac.id', 'admin123', 'admin'),
--mahasiswa
('Anggia Tejaningtyas Yudha Negara', '6182201080@unpar.ac.id', 'anggia28', 'mahasiswa'),
('Bagas Wijaya','6182201081@unpar.ac.id', 'bagas123', 'mahasiswa'),
('Clara Anindya', '6182201082@unpar.ac.id', 'clara123', 'mahasiswa'),
('David Nathanael', '6182201083@unpar.ac.id', 'david123', 'mahasiswa'),
('Elisa Monica', '6182201084@unpar.ac.id', 'elisa123', 'mahasiswa'),
('Farhan Adriansyah', '6182201085@unpar.ac.id', 'farhan123', 'mahasiswa'),
('Gabriel Pranata', '6182201086@unpar.ac.id', 'gabriel123', 'mahasiswa'),
('Hanifa Qyara', '6182201087@unpar.ac.id', 'hanifa123', 'mahasiswa'),
('Ivan Sutanto', '6182201088@unpar.ac.id', 'ivan123', 'mahasiswa'),
('Jessica Viona', '6182201089@unpar.ac.id', 'jessica123', 'mahasiswa'),
('Nicholas Martin', '6182201090@unpar.ac.id', 'nicholas123', 'mahasiswa'),
('Raden Mufarriz', '6182201091@unpar.ac.id', 'raden123', 'mahasiswa'),
('Tiara Maulida', '6182201092@unpar.ac.id', 'tiara123', 'mahasiswa'),
('Harry Potter', '6182201093@unpar.ac.id', 'harry123', 'mahasiswa'),
('Hermione Granger', '6182201094@unpar.ac.id', 'hermione123', 'mahasiswa'),
('Ron Weasley', '6182201095@unpar.ac.id', 'ron123', 'mahasiswa'),
('Draco Malfoy', '6182201096@unpar.ac.id', 'draco123', 'mahasiswa'),
('Luna Lovegood', '6182201097@unpar.ac.id', 'luna123', 'mahasiswa'),
('Neville Longbottom', '6182201098@unpar.ac.id', 'neville123', 'mahasiswa'),
('Ginny Weasley', '6182201099@unpar.ac.id', 'ginny123', 'mahasiswa'),
--dosen
('Maria Veronica', 'maria.veronica@unpar.ac.id', 'dosen123', 'dosen'),
('Husnul Hakim', 'husnul.hakim@unpar.ac.id', 'dosen123', 'dosen'),
('Raymond Chandra', 'raymond.chandra@unpar.ac.id', 'dosen123', 'dosen'),
('Vania Natalia', 'vania.natalia@unpar.ac.id', 'dosen123', 'dosen'),
('Keenan Leman', 'keenan.leman@unpar.ac.id', 'dosen123', 'dosen'),
('Gede Karya', 'gede.karya@unpar.ac.id', 'dosen123', 'dosen'),
('Veronica Moertini', 'veronica.moertini@unpar.ac.id', 'dosen123', 'dosen');

-- table admin
INSERT INTO admin (idAdmin) VALUES
(1);

--table mahasiswa
INSERT INTO mahasiswa (idMhs, npm) VALUES
(2, '6182201080'),
(3, '6182201081'),
(4, '6182201082'),
(5, '6182201083'),
(6, '6182201084'),
(7, '6182201085'),
(8, '6182201086'),
(9, '6182201087'),
(10, '6182201088'),
(11, '6182201089'),
(12, '6182201090'),
(13, '6182201091'),
(14, '6182201092'),
(15, '6182201093'),
(16, '6182201094'),
(17, '6182201095'),
(18, '6182201096'),
(19, '6182201097'),
(20, '6182201098'),
(21, '6182201099');


--table dosen
INSERT INTO dosen (idDosen, nik) VALUES
(22, '200100'),
(23, '200101'),
(24, '200102'),
(25, '200103'),
(26, '200104'),
(27, '200105'),
(28, '200106');

--table periode ta
INSERT INTO periode_ta (TahunAjaran, Semester, TanggalMulaiPeriodeTA, TanggalSelesaiPeriodeTA) VALUES
('2025/2026', 1, '2025-09-01', '2026-01-31'),
('2025/2026', 2, '2026-02-01', '2026-07-31');

--JadwalKuliah
INSERT INTO jadwal_kuliah (hari, jamMulai, jamSelesai, kelas, keterangan) VALUES
('Senin', '07:00', '09:00', 'A', 'Pemrograman Berbasis Web'),
('Selasa', '10:00', '12:00', 'B', 'Pemrograman Berbasis Web'),
('Kamis', '10:00', '12:00', 'C', 'Pemrograman Berbasis Web'),
('Senin', '13:00', '15:00', 'A', 'Artificial Intelligence'),
('Rabu', '15:00', '17:00', 'B', 'Artificial Intelligence'),
('Kamis', '15:00', '17:00', 'C', 'Artificial Intelligence'),
('Senin', '15:00', '17:00', 'A', 'Rekayasa Perangkat Lunak'),
('Rabu', '13:00', '15:00', 'B', 'Rekayasa Perangkat Lunak'),
('Jumat', '07:00', '09:00', 'C', 'Rekayasa Perangkat Lunak'),
('Selasa', '08:00', '10:00', 'A', 'Manajemen Proyek'),
('Kamis', '08:00', '10:00', 'B', 'Manajemen Proyek'),
('Rabu', '10:00', '12:30', 'A', 'Pengantar Sistem Informasi'),
('Selasa', '13:00', '15:00', 'B', 'Pengantar Sistem Informasi'),
('Rabu', '15:00', '17:00', 'A', 'Desain Antarmuka Grafis'),
('Selasa', '15:00', '17:00', 'B', 'Desain Antarmuka Grafis'),
('Jumat', '13:00', '15:00', 'C', 'Desain Antarmuka Grafis');

--ruangan
INSERT INTO ruangan (namaRuangan, statusRuangan) VALUES
('R101', 'tersedia'),
('R102', 'tersedia'),
('R103', 'tersedia'),
('R104', 'tersedia'),
('R105', 'tersedia'),
('R106', 'tersedia');

--table TA
INSERT INTO ta (codeTA, topikTA, idDosen, idPeriodeTA) VALUES
('MVC100BCS', 'Perangkat Lunak Pengelolaan Aset Promosi Program Studi Informatika', 22, 1),
('MVC100CCS', 'Perangkat Lunak Pencatatan Absensi dan Penghitungan Gaji Pegawai Karya Rejeki', 22, 1),
('HUH101ACS', 'Fuzzy Inference System untuk Prediksi Kepribadian Seseorang Berdasarkan Tulisan Tangan', 23, 1),
('HUH101BCS', 'Pemeringkatan Universitas Menggunakan Fuzzy Technique for Order of Preference', 23, 1),
('RCP102BCS', 'Eksplorasi Pengujian Perangkat Lunak Menggunakan sinon.js', 24, 1),
('RCP102CCS', 'Aplikasi Penghitungan Keuntungan Investasi', 24, 1),
('VAN103BCS', 'SI Pengelolaan Kegiatan Keuskupan Bandung', 25, 1),
('VAN103BDS', 'Pengembangan Sistem Intelejen Bisnis untuk Penilaian Kinerja Bidang Pastoral di Keuskupan Bandung', 25, 1),
('KAL104CCS', 'Pembuatan Perangkat Lunak Pencetak Sertifikat secara Masal Berbasis Web', 26, 1),
('KAL104CCS', 'Pengembangan Aplikasi Anotasi PDF Kolaboratif dengan Teknologi Web', 26, 1),
('GKD105ACS', 'Pengembangan Aplikasi Berbasis AI untuk Mendeteksi Kebakaran di Unit Rumah Susun Berbasis Data Sensor Multi Modal', 27, 1),
('GKD105ADS', 'Pengembangan Model AI untuk Memprediksi Kecurangan Penggunaan Utilitas Air dan Listrik di Rumah Susun ', 27, 1),
('VSM106ACS', 'Perangkat Lunak Simulator Gudang Beras', 28, 1),
('VSM106CDS', 'Analisis Data Keluarga Muda dan Pekerjaan Umat di Lingkungan Keuskupan Bandung', 28, 1);

-- table penugasanTA
INSERT INTO penugasan_ta (idMhs, idTA, StatusPersyaratan) VALUES
(2, 1, 'terpenuhi'),
(3, 2, 'terpenuhi'),
(4, 3, 'belum terpenuhi'),
(5, 4, 'terpenuhi'),
(6, 5, 'belum terpenuhi'),
(7, 6, 'terpenuhi'),
(8, 7, 'terpenuhi'),
(9, 8, 'belum terpenuhi'),
(10, 9, 'terpenuhi'),
(11, 10, 'belum terpenuhi'),
(12, 11, 'terpenuhi'),
(13, 12, 'terpenuhi'),
(14, 13, 'belum terpenuhi'),
(15, 14, 'terpenuhi');

--jadwalbimbingan
INSERT INTO jadwal_bimbingan (tanggal, waktuMulai, waktuSelesai, status, idMhs, idDosen, idRuangan) VALUES
('2025-09-01', '10:00', '11:00', 'done', 2, 22, 1),
('2025-09-08', '10:00', '11:00', 'done', 2, 22, 1),
('2025-09-02', '09:00', '10:00', 'done', 3, 22, 1),
('2025-09-09', '09:00', '10:00', 'done', 3, 22, 1),
('2025-09-03', '13:00', '14:00', 'done', 5, 23, 2),
('2025-09-10', '13:00', '14:00', 'done', 5, 23, 2),
('2025-10-04', '14:00', '15:00', 'done', 7, 24, 3),
('2025-10-11', '14:00', '15:00', 'done', 7, 24, 3),
('2025-10-05', '15:00', '16:00', 'done', 8, 25, 4),
('2025-10-12', '15:00', '16:00', 'done', 8, 25, 4),
('2025-10-06', '10:00', '11:00', 'done', 10, 26, 5),
('2025-10-13', '10:00', '11:00', 'done', 10, 26, 5),
('2025-11-07', '09:00', '10:00', 'done', 12, 27, 6),
('2025-11-14', '09:00', '10:00', 'done', 12, 27, 6),
('2025-11-08', '11:00', '12:00', 'done', 13, 27, 1),
('2025-11-15', '11:00', '12:00', 'done', 13, 27, 1),
('2025-11-09', '08:00', '09:00', 'done', 15, 28, 2),
('2025-11-16', '08:00', '09:00', 'done', 15, 28, 2),
('2025-12-30', '10:00', '11:00', 'pending', 4, 23, 1),
('2025-12-22', '09:00', '10:00', 'pending', 6, 24, 2),
('2025-12-25', '13:00', '14:00', 'pending', 9, 25, 3),
('2025-12-20', '14:00', '15:00', 'pending', 11, 26, 4),
('2026-12-18', '08:00', '09:00', 'approved', 14, 28, 5);

-- table bimbingan
INSERT INTO bimbingan (idJadwal, catatan, fileURL) VALUES
(1, 'Review awal topik TA', NULL),
(2, 'Perbaikan latar belakang', NULL),
(3, 'Struktur laporan diperbaiki', NULL),
(4, 'Pembahasan lebih diperdalam', NULL),
(5, 'Analisis fuzzy tahap 1', NULL),
(6, 'Penyesuaian parameter fuzzy', NULL),
(7, 'Pengujian awal sinon.js', NULL),
(8, 'Fixing bug pada test case', NULL),
(9, 'Diskusi kebutuhan sistem keuskupan', NULL),
(10, 'Revisi diagram ERD', NULL),
(11, 'Review generator sertifikat', NULL),
(12, 'Perbaikan modul upload', NULL),
(13, 'Pengumpulan data sensor', NULL),
(14, 'Uji awal model AI', NULL),
(15, 'Simulasi gudang versi beta', NULL),
(16, 'Optimasi algoritma simulasi', NULL),
(17, 'Analisis dataset keluarga muda', NULL),
(18, 'Revisi metodologi', NULL);

-- notif
INSERT INTO notifikasi (idUser, tipeNotif, keterangan) VALUES
(2,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(2,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(3,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(3,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(5,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(5,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(7,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(7,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(8,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(8,  'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(10, 'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(10, 'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(12, 'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(12, 'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(13, 'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(13, 'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(15, 'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(15, 'Bimbingan', 'Bimbingan selesai. Silakan cek catatan dosen.'),
(23, 'Pengajuan Bimbingan', 'Ada pengajuan bimbingan baru.'),
(24, 'Pengajuan Bimbingan', 'Ada pengajuan bimbingan baru.'),
(25, 'Pengajuan Bimbingan', 'Ada pengajuan bimbingan baru.'),
(26, 'Pengajuan Bimbingan', 'Ada pengajuan bimbingan baru.'),
(28, 'Pengajuan Bimbingan', 'Ada pengajuan bimbingan baru.'),
(4,  'Peringatan', 'Anda belum melakukan bimbingan.'),
(6,  'Peringatan', 'Anda belum melakukan bimbingan.'),
(9,  'Peringatan', 'Anda belum melakukan bimbingan.'),
(11, 'Peringatan', 'Anda belum melakukan bimbingan.'),
(14, 'Peringatan', 'Anda belum melakukan bimbingan.'),
(14, 'Pengajuan Bimbingan', 'Pengajuan bimbingan di setujui.');

-- relasi jadwal kuliah Dosen
INSERT INTO jadwalKuliahDosen VALUES
(26, 1),
(26, 2),
(26, 3),
(24, 4),
(24, 5),
(24, 6),
(25, 7),
(25, 8),
(25, 9),
(28, 10),
(28, 11),
(27, 12),
(27, 13),
(22, 14),
(22, 15),
(22, 16);


-- relasi jadwal kuliah Mahasiswa
INSERT INTO jadwalKuliahMahasiswa (idMhs, idJadwalKuliah) VALUES
(2,1),
(3,1),
(4,1),
(5,2),
(6,2),
(7,2),
(8,3),
(9,3),
(10,3),
(2,4),
(5,4),
(8,4),
(3,5),
(6,5),
(9,5),
(4,6),
(7,6),
(10,6),
(11,7),
(12,7),
(13,7),
(14,8),
(15,8),
(16,8),
(17,9),
(18,9),
(19,9),
(2,10),
(3,10),
(4,10),
(5,11),
(6,11),
(7,11),
(8,12),
(9,12),
(10,12),
(11,13),
(12,13),
(13,13),
(14,14),
(15,14),
(16,14),
(17,15),
(18,15),
(19,15),
(20,16),
(21,16),
(11,16);



SELECT * FROM users;
SELECT * FROM admin;
SELECT * FROM mahasiswa;
SELECT * FROM dosen;
SELECT * FROM periode_ta;
SELECT * FROM jadwal_kuliah;
SELECT * FROM ruangan;
SELECT * FROM ta;
SELECT * FROM penugasan_ta;
SELECT * FROM jadwal_bimbingan;
SELECT * FROM bimbingan;
SELECT * FROM notifikasi;
SELECT * FROM jadwalKuliahMahasiswa;
SELECT * FROM jadwalKuliahDosen;


-- Cek jadwal mahasiswa
SELECT m.idMhs, jk1.hari, jk1.jamMulai, jk1.jamSelesai, jk1.keterangan
FROM jadwalKuliahMahasiswa m
JOIN jadwal_kuliah jk1 ON m.idJadwalKuliah = jk1.idJadwalKuliah;


-- Cek Jadwal Dosen
SELECT d.idDosen, jk.hari, jk.jamMulai, jk.jamSelesai, jk.keterangan
FROM jadwalKuliahDosen d
JOIN jadwal_kuliah jk ON d.idJadwalKuliah = jk.idJadwalKuliah;


