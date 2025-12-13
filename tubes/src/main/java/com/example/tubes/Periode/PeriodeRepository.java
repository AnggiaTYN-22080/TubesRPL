package com.example.tubes.Periode;

import java.util.List;

public interface PeriodeRepository {
    List<Periode> findTA1();
    List<Periode> findTA2();
    void save(Periode p);
}
