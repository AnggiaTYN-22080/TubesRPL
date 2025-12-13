package com.example.tubes.KelolaData;

import com.example.tubes.KelolaData.dto.PeriodeOption;
import com.example.tubes.KelolaData.dto.PersonRow;
import com.example.tubes.KelolaData.dto.TopikRow;
import com.example.tubes.KelolaData.dto.TopikSaveAllForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KelolaDataService {

    private final KelolaDataRepository repo;
    private final JdbcTopikTARepository topikRepo;

    public KelolaDataService(KelolaDataRepository repo, JdbcTopikTARepository topikRepo) {
        this.repo = repo;
        this.topikRepo = topikRepo;
    }

    public List<PersonRow> getAllMahasiswa() { return repo.findAllMahasiswa(); }
    public List<PersonRow> getAllDosen() { return repo.findAllDosen(); }

    public List<PeriodeOption> getAllPeriode() { return repo.findAllPeriode(); }
    public List<PeriodeOption> getPeriodeSemester(int semester) { return repo.findPeriodeBySemester(semester); }

    public List<TopikRow> getTopikRows(int periodeId) { return topikRepo.findRowsByPeriode(periodeId); }

    @Transactional
    public void simpanSemuaTopik(TopikSaveAllForm form) { /* pakai punyamu */ }

    public void resetTopik(int mahasiswaId, int periodeId) {
        topikRepo.resetMahasiswaPeriode(mahasiswaId, periodeId);
    }
}
