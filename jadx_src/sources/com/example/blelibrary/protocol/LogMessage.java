package com.example.blelibrary.protocol;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LogMessage {
    private String AK;
    private int Conn;
    private String DN;
    private long F;
    private long HBC;
    private int L;
    private long LIC;
    private long P;
    private int R;
    private int SW;
    private long WFDC;
    private long cc;
    private int code;
    private int dns;
    private long st1;
    private long st2;

    public String getAK() {
        return this.AK;
    }

    public long getCc() {
        return this.cc;
    }

    public int getCode() {
        return this.code;
    }

    public int getConn() {
        return this.Conn;
    }

    public String getDN() {
        return this.DN;
    }

    public int getDns() {
        return this.dns;
    }

    public long getF() {
        return this.F;
    }

    public long getHBC() {
        return this.HBC;
    }

    public int getL() {
        return this.L;
    }

    public long getLIC() {
        return this.LIC;
    }

    public long getP() {
        return this.P;
    }

    public int getR() {
        return this.R;
    }

    public int getSW() {
        return this.SW;
    }

    public long getSt1() {
        return this.st1;
    }

    public long getSt2() {
        return this.st2;
    }

    public long getWFDC() {
        return this.WFDC;
    }

    public void setAK(String str) {
        this.AK = str;
    }

    public void setCc(long j8) {
        this.cc = j8;
    }

    public void setCode(int i8) {
        this.code = i8;
    }

    public void setConn(int i8) {
        this.Conn = i8;
    }

    public void setDN(String str) {
        this.DN = str;
    }

    public void setDns(int i8) {
        this.dns = i8;
    }

    public void setF(long j8) {
        this.F = j8;
    }

    public void setHBC(long j8) {
        this.HBC = j8;
    }

    public void setL(int i8) {
        this.L = i8;
    }

    public void setLIC(long j8) {
        this.LIC = j8;
    }

    public void setP(long j8) {
        this.P = j8;
    }

    public void setR(int i8) {
        this.R = i8;
    }

    public void setSW(int i8) {
        this.SW = i8;
    }

    public void setSt1(long j8) {
        this.st1 = j8;
    }

    public void setSt2(long j8) {
        this.st2 = j8;
    }

    public void setWFDC(long j8) {
        this.WFDC = j8;
    }

    public String toString() {
        return "LogMessage{SW=" + this.SW + ", Conn=" + this.Conn + ", R=" + this.R + ", AK='" + this.AK + "', P=" + this.P + ", DN='" + this.DN + "', L=" + this.L + ", F=" + this.F + ", st1=" + this.st1 + ", st2=" + this.st2 + ", LIC=" + this.LIC + ", HBC=" + this.HBC + ", WFDC=" + this.WFDC + ", dns=" + this.dns + ", cc=" + this.cc + ", code=" + this.code + '}';
    }
}
