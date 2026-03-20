package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f implements Parcelable.Creator<zzac> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzac createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        long j8 = 0;
        long j9 = 0;
        long j10 = 0;
        String str = null;
        String str2 = null;
        zzno zznoVar = null;
        String str3 = null;
        zzbf zzbfVar = null;
        zzbf zzbfVar2 = null;
        zzbf zzbfVar3 = null;
        boolean z4 = false;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 2:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 3:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 4:
                    zznoVar = (zzno) SafeParcelReader.n(parcel, B, zzno.CREATOR);
                    break;
                case 5:
                    j8 = SafeParcelReader.E(parcel, B);
                    break;
                case 6:
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
                case 7:
                    str3 = SafeParcelReader.o(parcel, B);
                    break;
                case 8:
                    zzbfVar = (zzbf) SafeParcelReader.n(parcel, B, zzbf.CREATOR);
                    break;
                case 9:
                    j9 = SafeParcelReader.E(parcel, B);
                    break;
                case 10:
                    zzbfVar2 = (zzbf) SafeParcelReader.n(parcel, B, zzbf.CREATOR);
                    break;
                case 11:
                    j10 = SafeParcelReader.E(parcel, B);
                    break;
                case 12:
                    zzbfVar3 = (zzbf) SafeParcelReader.n(parcel, B, zzbf.CREATOR);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzac(str, str2, zznoVar, j8, z4, str3, zzbfVar, j9, zzbfVar2, j10, zzbfVar3);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzac[] newArray(int i8) {
        return new zzac[i8];
    }
}
