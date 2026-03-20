package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ya implements Parcelable.Creator<zzmv> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzmv createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        String str = null;
        long j8 = 0;
        int i8 = 0;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 1) {
                str = SafeParcelReader.o(parcel, B);
            } else if (u8 == 2) {
                j8 = SafeParcelReader.E(parcel, B);
            } else if (u8 != 3) {
                SafeParcelReader.H(parcel, B);
            } else {
                i8 = SafeParcelReader.D(parcel, B);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzmv(str, j8, i8);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzmv[] newArray(int i8) {
        return new zzmv[i8];
    }
}
