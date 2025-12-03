-- table admin
CREATE TABLE admin (
    idAdmin SERIAL PRIMARY KEY,
    emailAdmin VARCHAR(30) NOT NULL UNIQUE,
    passwordAdmin VARCHAR(100) NOT NULL
);
ALTER TABLE IF EXISTS admin OWNER TO postgres;


-- table mahasiswa
CREATE TABLE mahasiswa (
    npmMahasiswa VARCHAR(10) PRIMARY KEY,
    emailMahasiswa VARCHAR(30) NOT NULL UNIQUE,
    namaMahasiswa VARCHAR(100) NOT NULL,
    passwordMahasiswa VARCHAR(100) NOT NULL
);
ALTER TABLE IF EXISTS mahasiswa OWNER TO postgres;

-- table dosen

CREATE TABLE dosen (
    nikDosen VARCHAR(10) PRIMARY KEY,
    namaDosen VARCHAR(100) NOT NULL,
    emailDosen VARCHAR(30) NOT NULL UNIQUE,
    passwordDosen VARCHAR(100) NOT NULL
);
ALTER TABLE IF EXISTS dosen OWNER TO postgres;

-- table periode TA

CREATE TABLE periode_ta (
    idPeriodeTA SERIAL PRIMARY KEY,
    TahunAjaran VARCHAR(10) NOT NULL,
    Semester INT NOT NULL,
    TanggalMulaiPeriodeTA DATE NOT NULL,
    TanggalSelesaiPeriodeTA DATE NOT NULL
);
ALTER TABLE IF EXISTS periode_ta OWNER TO postgres;

-- table jadwal kuliah
CREATE TABLE jadwal_kuliah (
    idJadwalKuliah SERIAL PRIMARY KEY,
    hari VARCHAR(10) NOT NULL,
    jamMulai TIME NOT NULL,
    jamSelesai TIME NOT NULL,
	kelas VARCHAR(10) NOT NULL,
    keterangan VARCHAR(100),
    npmMahasiswa VARCHAR(10) REFERENCES mahasiswa(npmMahasiswa),
    nikDosen VARCHAR(10) REFERENCES dosen(nikDosen)
);

ALTER TABLE IF EXISTS jadwal_kuliah OWNER TO postgres;

-- table ruangan
CREATE TABLE ruangan (
    idRuangan SERIAL PRIMARY KEY,
    namaRuangan VARCHAR(10) NOT NULL,
    statusRuangan VARCHAR(20) NOT NULL
);
ALTER TABLE IF EXISTS ruangan OWNER TO postgres;

-- table jadwal bimbingan
CREATE TABLE jadwal_bimbingan (
    idJadwal SERIAL PRIMARY KEY,
    tanggal DATE NOT NULL,
    waktuMulai TIME NOT NULL,
    waktuSelesai TIME NOT NULL,
    nikDosen VARCHAR(10) REFERENCES dosen(nikDosen),
    npmMahasiswa VARCHAR(10) REFERENCES mahasiswa(npmMahasiswa),
	idRuangan INT REFERENCES ruangan(idRuangan),
    status VARCHAR(20) DEFAULT 'pending'
);
ALTER TABLE IF EXISTS jadwal_bimbingan OWNER TO postgres;

-- penugasan TA
CREATE TABLE ta (
    idTA SERIAL PRIMARY KEY,
    TopikTA TEXT NOT NULL,
    StatusPersyaratan VARCHAR(50),
    ProgressTA VARCHAR(50),
    idPeriodeTA INT REFERENCES periode_ta(idPeriodeTA)
);
ALTER TABLE IF EXISTS ta OWNER TO postgres;

-- table penugasan TA
CREATE TABLE penugasan_ta (
	npmMahasiswa VARCHAR(10) NOT NULL REFERENCES mahasiswa(npmMahasiswa),
    nikDosen VARCHAR(10) NOT NULL REFERENCES dosen(nikDosen),
    idTA INT NOT NULL REFERENCES ta(idTA),
    PRIMARY KEY (npmMahasiswa, idTA)
);
ALTER TABLE IF EXISTS penugasan_ta OWNER TO postgres;