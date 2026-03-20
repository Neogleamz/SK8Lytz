package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p4 extends com.google.android.gms.internal.measurement.w0 implements f7.d {
    /* JADX INFO: Access modifiers changed from: package-private */
    public p4(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    @Override // f7.d
    public final void B(zzac zzacVar, zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zzacVar);
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        f(12, d8);
    }

    @Override // f7.d
    public final void G1(Bundle bundle, zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, bundle);
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        f(19, d8);
    }

    @Override // f7.d
    public final List<zzno> I0(String str, String str2, boolean z4, zzn zznVar) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        com.google.android.gms.internal.measurement.x0.e(d8, z4);
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        Parcel e8 = e(14, d8);
        ArrayList createTypedArrayList = e8.createTypedArrayList(zzno.CREATOR);
        e8.recycle();
        return createTypedArrayList;
    }

    @Override // f7.d
    public final List<zzmv> K(zzn zznVar, Bundle bundle) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        com.google.android.gms.internal.measurement.x0.d(d8, bundle);
        Parcel e8 = e(24, d8);
        ArrayList createTypedArrayList = e8.createTypedArrayList(zzmv.CREATOR);
        e8.recycle();
        return createTypedArrayList;
    }

    @Override // f7.d
    public final byte[] K1(zzbf zzbfVar, String str) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zzbfVar);
        d8.writeString(str);
        Parcel e8 = e(9, d8);
        byte[] createByteArray = e8.createByteArray();
        e8.recycle();
        return createByteArray;
    }

    @Override // f7.d
    public final zzal M0(zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        Parcel e8 = e(21, d8);
        zzal zzalVar = (zzal) com.google.android.gms.internal.measurement.x0.a(e8, zzal.CREATOR);
        e8.recycle();
        return zzalVar;
    }

    @Override // f7.d
    public final void Z(zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        f(4, d8);
    }

    @Override // f7.d
    public final void a1(zzbf zzbfVar, String str, String str2) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zzbfVar);
        d8.writeString(str);
        d8.writeString(str2);
        f(5, d8);
    }

    @Override // f7.d
    public final void f1(zzbf zzbfVar, zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zzbfVar);
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        f(1, d8);
    }

    @Override // f7.d
    public final void m(zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        f(20, d8);
    }

    @Override // f7.d
    public final void m0(long j8, String str, String str2, String str3) {
        Parcel d8 = d();
        d8.writeLong(j8);
        d8.writeString(str);
        d8.writeString(str2);
        d8.writeString(str3);
        f(10, d8);
    }

    @Override // f7.d
    public final String n1(zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        Parcel e8 = e(11, d8);
        String readString = e8.readString();
        e8.recycle();
        return readString;
    }

    @Override // f7.d
    public final void r0(zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        f(18, d8);
    }

    @Override // f7.d
    public final List<zzno> s(String str, String str2, String str3, boolean z4) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        d8.writeString(str3);
        com.google.android.gms.internal.measurement.x0.e(d8, z4);
        Parcel e8 = e(15, d8);
        ArrayList createTypedArrayList = e8.createTypedArrayList(zzno.CREATOR);
        e8.recycle();
        return createTypedArrayList;
    }

    @Override // f7.d
    public final List<zzac> s0(String str, String str2, String str3) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        d8.writeString(str3);
        Parcel e8 = e(17, d8);
        ArrayList createTypedArrayList = e8.createTypedArrayList(zzac.CREATOR);
        e8.recycle();
        return createTypedArrayList;
    }

    @Override // f7.d
    public final List<zzac> t0(String str, String str2, zzn zznVar) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        Parcel e8 = e(16, d8);
        ArrayList createTypedArrayList = e8.createTypedArrayList(zzac.CREATOR);
        e8.recycle();
        return createTypedArrayList;
    }

    @Override // f7.d
    public final void u1(zzac zzacVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zzacVar);
        f(13, d8);
    }

    @Override // f7.d
    public final void w(zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        f(6, d8);
    }

    @Override // f7.d
    public final void z0(zzno zznoVar, zzn zznVar) {
        Parcel d8 = d();
        com.google.android.gms.internal.measurement.x0.d(d8, zznoVar);
        com.google.android.gms.internal.measurement.x0.d(d8, zznVar);
        f(2, d8);
    }
}
