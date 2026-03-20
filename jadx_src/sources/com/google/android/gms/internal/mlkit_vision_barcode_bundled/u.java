package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        zzav zzavVar = null;
        String str = null;
        String str2 = null;
        zzaw[] zzawVarArr = null;
        zzat[] zzatVarArr = null;
        String[] strArr = null;
        zzao[] zzaoVarArr = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 1:
                    zzavVar = (zzav) SafeParcelReader.n(parcel, B, zzav.CREATOR);
                    break;
                case 2:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 3:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 4:
                    zzawVarArr = (zzaw[]) SafeParcelReader.r(parcel, B, zzaw.CREATOR);
                    break;
                case 5:
                    zzatVarArr = (zzat[]) SafeParcelReader.r(parcel, B, zzat.CREATOR);
                    break;
                case 6:
                    strArr = SafeParcelReader.p(parcel, B);
                    break;
                case 7:
                    zzaoVarArr = (zzao[]) SafeParcelReader.r(parcel, B, zzao.CREATOR);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzar(zzavVar, str, str2, zzawVarArr, zzatVarArr, strArr, zzaoVarArr);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzar[i8];
    }
}
