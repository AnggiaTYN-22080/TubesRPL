package com.example.tubes.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersyaratanSidangService {

    @Autowired
    private PersyaratanSidangRepository repo;

    public List<PersyaratanSidangItem> getTA1() {
        return repo.findBySemester(1);
    }

    public List<PersyaratanSidangItem> getTA2() {
        return repo.findBySemester(2);
    }
}
