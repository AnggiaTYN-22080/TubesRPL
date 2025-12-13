package com.example.tubes.Admin;

import java.util.List;

public interface PersyaratanSidangRepository {
    List<PersyaratanSidangItem> findBySemester(int semester); // 1 = TA1, 2 = TA2
}
