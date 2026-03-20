package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n2 implements Parcelable.Creator<zzdq> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzdq createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        long j8 = 0;
        long j9 = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        Bundle bundle = null;
        String str4 = null;
        boolean z4 = false;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 1:
                    j8 = SafeParcelReader.E(parcel, B);
                    break;
                case 2:
                    j9 = SafeParcelReader.E(parcel, B);
                    break;
                case 3:
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
                case 4:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 5:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 6:
                    str3 = SafeParcelReader.o(parcel, B);
                    break;
                case 7:
                    bundle = SafeParcelReader.f(parcel, B);
                    break;
                case 8:
                    str4 = SafeParcelReader.o(parcel, B);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzdq(j8, j9, z4, str, str2, str3, bundle, str4);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzdq[] newArray(int i8) {
        return new zzdq[i8];
    }
}
