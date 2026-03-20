package com.google.android.gms.internal.mlkit_vision_barcode;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ih implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        int i8 = 0;
        int i9 = 0;
        String str = null;
        String str2 = null;
        byte[] bArr = null;
        Point[] pointArr = null;
        zzvc zzvcVar = null;
        zzvf zzvfVar = null;
        zzvg zzvgVar = null;
        zzvi zzviVar = null;
        zzvh zzvhVar = null;
        zzvd zzvdVar = null;
        zzuz zzuzVar = null;
        zzva zzvaVar = null;
        zzvb zzvbVar = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 1:
                    i8 = SafeParcelReader.D(parcel, B);
                    break;
                case 2:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 3:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 4:
                    bArr = SafeParcelReader.g(parcel, B);
                    break;
                case 5:
                    pointArr = (Point[]) SafeParcelReader.r(parcel, B, Point.CREATOR);
                    break;
                case 6:
                    i9 = SafeParcelReader.D(parcel, B);
                    break;
                case 7:
                    zzvcVar = (zzvc) SafeParcelReader.n(parcel, B, zzvc.CREATOR);
                    break;
                case 8:
                    zzvfVar = (zzvf) SafeParcelReader.n(parcel, B, zzvf.CREATOR);
                    break;
                case 9:
                    zzvgVar = (zzvg) SafeParcelReader.n(parcel, B, zzvg.CREATOR);
                    break;
                case 10:
                    zzviVar = (zzvi) SafeParcelReader.n(parcel, B, zzvi.CREATOR);
                    break;
                case 11:
                    zzvhVar = (zzvh) SafeParcelReader.n(parcel, B, zzvh.CREATOR);
                    break;
                case 12:
                    zzvdVar = (zzvd) SafeParcelReader.n(parcel, B, zzvd.CREATOR);
                    break;
                case 13:
                    zzuzVar = (zzuz) SafeParcelReader.n(parcel, B, zzuz.CREATOR);
                    break;
                case 14:
                    zzvaVar = (zzva) SafeParcelReader.n(parcel, B, zzva.CREATOR);
                    break;
                case 15:
                    zzvbVar = (zzvb) SafeParcelReader.n(parcel, B, zzvb.CREATOR);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzvj(i8, str, str2, bArr, pointArr, i9, zzvcVar, zzvfVar, zzvgVar, zzviVar, zzvhVar, zzvdVar, zzuzVar, zzvaVar, zzvbVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzvj[i8];
    }
}
