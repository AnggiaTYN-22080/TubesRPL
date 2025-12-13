package com.example.tubes.KelolaData;

import com.example.tubes.KelolaData.dto.PeriodeOption;
import com.example.tubes.KelolaData.dto.PersonRow;
import java.util.List;

public interface KelolaDataRepository {
    List<PersonRow> findAllMahasiswa();
    List<PersonRow> findAllDosen();

    List<PeriodeOption> findAllPeriode();
    List<PeriodeOption> findPeriodeBySemester(int semester);
}
