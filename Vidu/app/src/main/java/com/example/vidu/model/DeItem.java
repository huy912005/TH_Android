package com.example.vidu.model;
public class DeItem {
    private String tenDe;
    private int diem;
    private boolean daLam;

    public DeItem(String tenDe, int diem, boolean daLam) {
        this.tenDe = tenDe;
        this.diem = diem;
        this.daLam = daLam;
    }

    public String getTenDe() { return tenDe; }
    public int getDiem() { return diem; }
    public boolean isDaLam() { return daLam; }
}
