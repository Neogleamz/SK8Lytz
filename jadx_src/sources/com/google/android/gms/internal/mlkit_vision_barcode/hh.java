package com.google.android.gms.internal.mlkit_vision_barcode;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class hh implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        int i8 = 0;
        int i9 = 0;
        boolean z4 = false;
        String str = null;
        String str2 = null;
        Point[] pointArr = null;
        zzn zznVar = null;
        zzq zzqVar = null;
        zzr zzrVar = null;
        zzt zztVar = null;
        zzs zzsVar = null;
        zzo zzoVar = null;
        zzk zzkVar = null;
        zzl zzlVar = null;
        zzm zzmVar = null;
        byte[] bArr = null;
        double d8 = 0.0d;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 2:
                    i8 = SafeParcelReader.D(parcel, B);
                    break;
                case 3:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 4:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 5:
                    i9 = SafeParcelReader.D(parcel, B);
                    break;
                case 6:
                    pointArr = (Point[]) SafeParcelReader.r(parcel, B, Point.CREATOR);
                    break;
                case 7:
                    zznVar = (zzn) SafeParcelReader.n(parcel, B, zzn.CREATOR);
                    break;
                case 8:
                    zzqVar = (zzq) SafeParcelReader.n(parcel, B, zzq.CREATOR);
                    break;
                case 9:
                    zzrVar = (zzr) SafeParcelReader.n(parcel, B, zzr.CREATOR);
                    break;
                case 10:
                    zztVar = (zzt) SafeParcelReader.n(parcel, B, zzt.CREATOR);
                    break;
                case 11:
                    zzsVar = (zzs) SafeParcelReader.n(parcel, B, zzs.CREATOR);
                    break;
                case 12:
                    zzoVar = (zzo) SafeParcelReader.n(parcel, B, zzo.CREATOR);
                    break;
                case 13:
                    zzkVar = (zzk) SafeParcelReader.n(parcel, B, zzk.CREATOR);
                    break;
                case 14:
                    zzlVar = (zzl) SafeParcelReader.n(parcel, B, zzl.CREATOR);
                    break;
                case 15:
                    zzmVar = (zzm) SafeParcelReader.n(parcel, B, zzm.CREATOR);
                    break;
                case 16:
                    bArr = SafeParcelReader.g(parcel, B);
                    break;
                case 17:
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
                case 18:
                    d8 = SafeParcelReader.x(parcel, B);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzu(i8, str, str2, i9, pointArr, zznVar, zzqVar, zzrVar, zztVar, zzsVar, zzoVar, zzkVar, zzlVar, zzmVar, bArr, z4, d8);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzu[i8];
    }
}
