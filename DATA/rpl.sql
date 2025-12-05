DROP TABLE IF EXISTS penugasan_ta;
DROP TABLE IF EXISTS ta;
DROP TABLE IF EXISTS jadwal_bimbingan;
DROP TABLE IF EXISTS jadwal_kuliah;
DROP TABLE IF EXISTS ruangan;
DROP TABLE IF EXISTS periode_ta;
DROP TABLE IF EXISTS dosen;
DROP TABLE IF EXISTS mahasiswa;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS users;

-- table user
CREATE TABLE users(
	idUser SERIAL PRIMARY KEY,
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
    idPeriodeTA SERIAL PRIMARY KEY,
    TahunAjaran VARCHAR(10) NOT NULL,
    Semester INT NOT NULL,
    TanggalMulaiPeriodeTA DATE NOT NULL,
    TanggalSelesaiPeriodeTA DATE NOT NULL
);

-- table jadwal kuliah
CREATE TABLE jadwal_kuliah (
    idJadwalKuliah SERIAL PRIMARY KEY,
    hari VARCHAR(10) NOT NULL,
    jamMulai TIME NOT NULL,
    jamSelesai TIME NOT NULL,
	kelas VARCHAR(10) NOT NULL,
    keterangan VARCHAR(100),
    idMhs INT,
    idDosen INT,
	FOREIGN KEY (idMhs) REFERENCES mahasiswa(idMhs),
	FOREIGN KEY (idDosen) REFERENCES dosen(idDosen)
);


-- table ruangan
CREATE TABLE ruangan (
    idRuangan SERIAL PRIMARY KEY,
    namaRuangan VARCHAR(10) NOT NULL,
    statusRuangan VARCHAR(20) NOT NULL
);

-- table jadwal bimbingan
CREATE TABLE jadwal_bimbingan (
    idJadwal SERIAL PRIMARY KEY,
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

-- penugasan TA
CREATE TABLE ta (
    idTA SERIAL PRIMARY KEY,
    TopikTA TEXT NOT NULL,
    StatusPersyaratan VARCHAR(50),
    ProgressTA VARCHAR(50),
    idPeriodeTA INT REFERENCES periode_ta(idPeriodeTA)
);

-- table penugasan TA
CREATE TABLE penugasan_ta (
    idMhs INT,
    idDosen INT,
	FOREIGN KEY (idMhs) REFERENCES mahasiswa(idMhs),
	FOREIGN KEY (idDosen) REFERENCES dosen(idDosen),
    idTA INT NOT NULL REFERENCES ta(idTA),
    PRIMARY KEY (idMhs, idTA)
);

-- Dummy Data
-- table users
INSERT INTO users (name, email, password, role) VALUES
--admin
('ADMIN SISTEM', 'admin@unpar.ac.id', 'admin123', 'admin'),
--mahasiswa
('ANGGIA TEJANINGTYAS YUDHA NEGARA', '6182201080@unpar.ac.id', 'anggia28', 'mahasiswa'),
('BAGAS WIJAYA','6182201081@unpar.ac.id', 'bagas123', 'mahasiswa'),
('CLARA ANINDYA', '6182201082@unpar.ac.id', 'clara123', 'mahasiswa'),
( 'DAVID NATHANAEL', '6182201083@unpar.ac.id', 'david123', 'mahasiswa'),
( 'ELISA MONICA', '6182201084@unpar.ac.id', 'elisa123', 'mahasiswa'),
('FARHAN ADRIANSYAH', '6182201085@unpar.ac.id', 'farhan123', 'mahasiswa'),
('GABRIEL PRANATA', '6182201086@unpar.ac.id', 'gabriel123', 'mahasiswa'),
('HANIFA QYARA', '6182201087@unpar.ac.id', 'hanifa123', 'mahasiswa'),
('IVAN SUTANTO', '6182201088@unpar.ac.id', 'ivan123', 'mahasiswa'),
('JESSICA VIONA', '6182201089@unpar.ac.id', 'jessica123', 'mahasiswa'),
--dosen
('MARIA VERONICA', 'maria.veronica@unpar.ac.id', 'dosen123', 'dosen'),
('HUSNUL HAKIM', 'husnul.hakim@unpar.ac.id',    'dosen123', 'dosen'),
('NICHOLAS MARTIN', 'nicholas.martin@unpar.ac.id', 'dosen123', 'dosen');

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
(11, '6182201089');


--table dosen
INSERT INTO dosen (idDosen, nik) VALUES
(12, 'D00100'),
(13, 'D00101'),
(14, 'D00102');

--table periode ta
INSERT INTO periode_ta (TahunAjaran, Semester, TanggalMulaiPeriodeTA, TanggalSelesaiPeriodeTA) VALUES
('2023/2024', 1, '2023-09-01', '2024-01-31'),
('2023/2024', 2, '2024-02-01', '2024-07-31');

--JadwalKuliah
INSERT INTO jadwal_kuliah (hari, jamMulai, jamSelesai, kelas, keterangan, idMhs, idDosen) VALUES
-- Mahasiswa
('SENIN', '07:00', '09:00', 'A301', 'Matematika Dasar', 2, NULL),
('SELASA', '09:00', '11:00', 'B201', 'Algoritma Pemrograman', 3, NULL),
('RABU', '10:00', '12:00', 'C101', 'Jaringan Komputer', 4, NULL),
-- Dosen
('SENIN', '07:00', '09:00', 'A301', 'Megajar Matematika Dasar', NULL, 12),
('SELASA', '09:00', '11:00', 'B201', 'Megajar Algoritma Pemrograman', NULL, 13),
('RABU', '10:00', '12:00', 'C101', 'Megajar Jaringan Komputer', NULL, 14);

--ruangan
INSERT INTO ruangan (namaRuangan, statusRuangan) VALUES
('R101', 'tersedia'),
('R102', 'tersedia'),
('R103', 'dipakai');

--jadwalbimbingan
INSERT INTO jadwal_bimbingan (tanggal, waktuMulai, waktuSelesai, idDosen, idMhs, idRuangan, status) VALUES
('2024-03-10', '13:00', '14:00', 12, 2, 1, 'pending'),
('2024-03-12', '10:00', '11:00', 13, 3, 2, 'approved'),
('2024-03-15', '09:00', '10:00', 14, 4, 3, 'pending');

--table TA
INSERT INTO ta (TopikTA, StatusPersyaratan, ProgressTA, idPeriodeTA) VALUES
('SISTEM INFORMASI BIMBINGAN TA', 'terpenuhi', 'Proposal', 1),
('OPTIMALISASI MACHINE LEARNING', 'belum terpenihu', 'Bab 1', 1),
('PREDIKSI CUACA DENGAN AI', 'terpenuhi', 'Bab 2', 1);

-- table penugasanTA
INSERT INTO penugasan_ta (idMhs, idDosen, idTA) VALUES
(2, 12, 1),
(3, 13, 2),
(4, 14, 3);


SELECT * FROM users;
SELECT * FROM admin;
SELECT * FROM mahasiswa;
SELECT * FROM dosen;
SELECT * FROM periode_ta;
SELECT * FROM jadwal_kuliah;
SELECT * FROM ruangan;
SELECT * FROM jadwal_bimbingan;
SELECT * FROM ta;
SELECT * FROM penugasan_ta;