package com.example.tubes.Periode;

import java.util.List;
import java.util.Optional;

public interface PeriodeTARepository {
    List<PeriodeTA> findAll();
    Optional<PeriodeTA> findById(int idPeriodeTA);
    void insert(PeriodeTA p);
    void update(PeriodeTA p);
    void delete(int idPeriodeTA);
}
