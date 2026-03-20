package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ci implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        zzp zzpVar = null;
        String str = null;
        String str2 = null;
        zzq[] zzqVarArr = null;
        zzn[] zznVarArr = null;
        String[] strArr = null;
        zzi[] zziVarArr = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 2:
                    zzpVar = (zzp) SafeParcelReader.n(parcel, B, zzp.CREATOR);
                    break;
                case 3:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 4:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 5:
                    zzqVarArr = (zzq[]) SafeParcelReader.r(parcel, B, zzq.CREATOR);
                    break;
                case 6:
                    zznVarArr = (zzn[]) SafeParcelReader.r(parcel, B, zzn.CREATOR);
                    break;
                case 7:
                    strArr = SafeParcelReader.p(parcel, B);
                    break;
                case 8:
                    zziVarArr = (zzi[]) SafeParcelReader.r(parcel, B, zzi.CREATOR);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzl(zzpVar, str, str2, zzqVarArr, zznVarArr, strArr, zziVarArr);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzl[i8];
    }
}
