package com.example.tubes.KelolaData;

public class KelolaDataTopikView {
    private int idTA;
    private String codeTA;
    private String topikTA;

    private Integer idDosen;     // boleh null (karena di DB idDosen bisa null)
    private String namaDosen;    // hasil join, boleh null
    private Integer idPeriodeTA; // boleh null

    public KelolaDataTopikView() {}

    public KelolaDataTopikView(int idTA, String codeTA, String topikTA,
                               Integer idDosen, String namaDosen, Integer idPeriodeTA) {
        this.idTA = idTA;
        this.codeTA = codeTA;
        this.topikTA = topikTA;
        this.idDosen = idDosen;
        this.namaDosen = namaDosen;
        this.idPeriodeTA = idPeriodeTA;
    }

    public int getIdTA() { return idTA; }
    public void setIdTA(int idTA) { this.idTA = idTA; }
    public String getCodeTA() { return codeTA; }
    public void setCodeTA(String codeTA) { this.codeTA = codeTA; }
    public String getTopikTA() { return topikTA; }
    public void setTopikTA(String topikTA) { this.topikTA = topikTA; }
    public Integer getIdDosen() { return idDosen; }
    public void setIdDosen(Integer idDosen) { this.idDosen = idDosen; }
    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }
    public Integer getIdPeriodeTA() { return idPeriodeTA; }
    public void setIdPeriodeTA(Integer idPeriodeTA) { this.idPeriodeTA = idPeriodeTA; }
}
