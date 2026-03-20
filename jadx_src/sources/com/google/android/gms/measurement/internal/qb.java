package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class qb implements Parcelable.Creator<zzno> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzno createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        String str = null;
        Long l8 = null;
        Float f5 = null;
        String str2 = null;
        String str3 = null;
        Double d8 = null;
        int i8 = 0;
        long j8 = 0;
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
                    j8 = SafeParcelReader.E(parcel, B);
                    break;
                case 4:
                    l8 = SafeParcelReader.F(parcel, B);
                    break;
                case 5:
                    f5 = SafeParcelReader.A(parcel, B);
                    break;
                case 6:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 7:
                    str3 = SafeParcelReader.o(parcel, B);
                    break;
                case 8:
                    d8 = SafeParcelReader.y(parcel, B);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzno(i8, str, j8, l8, f5, str2, str3, d8);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzno[] newArray(int i8) {
        return new zzno[i8];
    }
}
