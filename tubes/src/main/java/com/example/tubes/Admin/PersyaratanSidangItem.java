package com.example.tubes.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersyaratanSidangItem {
    private int idMhs;
    private int idTA;

    private String nama;      
    private boolean terpenuhi; 
    private String labelStatus; 

    private String email;
    private String npm;
}
