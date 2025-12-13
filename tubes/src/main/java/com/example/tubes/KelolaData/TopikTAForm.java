package com.example.tubes.KelolaData;

public class TopikTAForm {
    private String codeTA;
    private String topikTA;
    private Integer idDosen;      // optional
    private Integer idPeriodeTA;  // optional (kalau null, kita ambil periode terbaru)

    public String getCodeTA() { return codeTA; }
    public void setCodeTA(String codeTA) { this.codeTA = codeTA; }
    public String getTopikTA() { return topikTA; }
    public void setTopikTA(String topikTA) { this.topikTA = topikTA; }
    public Integer getIdDosen() { return idDosen; }
    public void setIdDosen(Integer idDosen) { this.idDosen = idDosen; }
    public Integer getIdPeriodeTA() { return idPeriodeTA; }
    public void setIdPeriodeTA(Integer idPeriodeTA) { this.idPeriodeTA = idPeriodeTA; }
}
