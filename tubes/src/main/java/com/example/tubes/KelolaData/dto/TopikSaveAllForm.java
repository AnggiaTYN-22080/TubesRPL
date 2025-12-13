package com.example.tubes.KelolaData.dto;

import java.util.ArrayList;
import java.util.List;

public class TopikSaveAllForm {

    private Integer periodeId;
    private List<Item> items = new ArrayList<>();

    public Integer getPeriodeId() { return periodeId; }
    public void setPeriodeId(Integer periodeId) { this.periodeId = periodeId; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public static class Item {
        private Integer mahasiswaId;
        private String kodeTa;
        private Integer dosenId;
        private String topik;

        public Integer getMahasiswaId() { return mahasiswaId; }
        public void setMahasiswaId(Integer mahasiswaId) { this.mahasiswaId = mahasiswaId; }

        public String getKodeTa() { return kodeTa; }
        public void setKodeTa(String kodeTa) { this.kodeTa = kodeTa; }

        public Integer getDosenId() { return dosenId; }
        public void setDosenId(Integer dosenId) { this.dosenId = dosenId; }

        public String getTopik() { return topik; }
        public void setTopik(String topik) { this.topik = topik; }
    }
}
