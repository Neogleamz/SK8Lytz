package com.example.blelibrary.scan;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LEDNetWFDevice implements Parcelable {
    public static final Parcelable.Creator<LEDNetWFDevice> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private String f8887a;

    /* renamed from: b  reason: collision with root package name */
    private int f8888b;

    /* renamed from: c  reason: collision with root package name */
    private int f8889c;

    /* renamed from: d  reason: collision with root package name */
    private int f8890d;

    /* renamed from: e  reason: collision with root package name */
    private String f8891e;

    /* renamed from: f  reason: collision with root package name */
    private int f8892f;

    /* renamed from: g  reason: collision with root package name */
    private int f8893g;

    /* renamed from: h  reason: collision with root package name */
    private int f8894h;

    /* renamed from: j  reason: collision with root package name */
    private byte[] f8895j;

    /* renamed from: k  reason: collision with root package name */
    private ScanResult f8896k;

    /* renamed from: l  reason: collision with root package name */
    private BluetoothDevice f8897l;

    /* renamed from: m  reason: collision with root package name */
    private byte[] f8898m;

    /* renamed from: n  reason: collision with root package name */
    private byte[] f8899n;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<LEDNetWFDevice> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public LEDNetWFDevice createFromParcel(Parcel parcel) {
            return new LEDNetWFDevice(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public LEDNetWFDevice[] newArray(int i8) {
            return new LEDNetWFDevice[i8];
        }
    }

    public LEDNetWFDevice() {
    }

    protected LEDNetWFDevice(Parcel parcel) {
        this.f8887a = parcel.readString();
        this.f8888b = parcel.readInt();
        this.f8889c = parcel.readInt();
        this.f8890d = parcel.readInt();
        this.f8891e = parcel.readString();
        this.f8892f = parcel.readInt();
        this.f8893g = parcel.readInt();
        this.f8894h = parcel.readInt();
        this.f8895j = parcel.createByteArray();
        this.f8896k = (ScanResult) parcel.readParcelable(ScanResult.class.getClassLoader());
        this.f8897l = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.f8898m = parcel.createByteArray();
        this.f8899n = parcel.createByteArray();
    }

    public static boolean a(int i8) {
        return i8 == 23121 || i8 == 23122 || i8 == 23120;
    }

    public int b() {
        return this.f8890d;
    }

    public BluetoothDevice c() {
        return this.f8897l;
    }

    public String d() {
        return this.f8887a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int e() {
        return this.f8893g;
    }

    public int f() {
        return this.f8894h;
    }

    public String g() {
        String str = this.f8891e;
        if (str != null) {
            return str.toUpperCase();
        }
        return null;
    }

    public int h() {
        return this.f8889c;
    }

    public int i() {
        return this.f8892f;
    }

    public ScanResult j() {
        return this.f8896k;
    }

    public byte[] k() {
        return this.f8895j;
    }

    public int l() {
        return this.f8888b;
    }

    public void m(int i8) {
        this.f8890d = i8;
    }

    public void n(byte[] bArr) {
        this.f8899n = bArr;
    }

    public void o(BluetoothDevice bluetoothDevice) {
        this.f8897l = bluetoothDevice;
    }

    public void p(String str) {
        this.f8887a = str;
    }

    public void q(int i8) {
        this.f8893g = i8;
    }

    public void r(int i8) {
        this.f8894h = i8;
    }

    public void s(String str) {
        this.f8891e = str;
    }

    public void t(int i8) {
        this.f8889c = i8;
    }

    public String toString() {
        return "LEDNetWFDevice{deviceName='" + this.f8887a + "', sta=" + this.f8888b + ", manufacturer=" + this.f8889c + ", bleVersion=" + this.f8890d + ", macAddress='" + this.f8891e + "', productId=" + this.f8892f + ", firmwareVer=" + this.f8893g + ", ledVersion=" + this.f8894h + ", rfu=" + Arrays.toString(this.f8895j) + ", result=" + this.f8896k + ", device=" + this.f8897l + ", stateInfo=" + Arrays.toString(this.f8898m) + ", devInfo=" + Arrays.toString(this.f8899n) + '}';
    }

    public void u(int i8) {
        this.f8892f = i8;
    }

    public void v(ScanResult scanResult) {
        this.f8896k = scanResult;
    }

    public void w(byte[] bArr) {
        this.f8895j = bArr;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f8887a);
        parcel.writeInt(this.f8888b);
        parcel.writeInt(this.f8889c);
        parcel.writeInt(this.f8890d);
        parcel.writeString(this.f8891e);
        parcel.writeInt(this.f8892f);
        parcel.writeInt(this.f8893g);
        parcel.writeInt(this.f8894h);
        parcel.writeByteArray(this.f8895j);
        parcel.writeParcelable(this.f8896k, i8);
        parcel.writeParcelable(this.f8897l, i8);
        parcel.writeByteArray(this.f8898m);
        parcel.writeByteArray(this.f8899n);
    }

    public void x(int i8) {
        this.f8888b = i8;
    }

    public void y(byte[] bArr) {
        this.f8898m = bArr;
    }
}
