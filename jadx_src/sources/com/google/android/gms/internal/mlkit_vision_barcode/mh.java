package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class mh implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        zzve zzveVar = null;
        String str = null;
        String str2 = null;
        zzvf[] zzvfVarArr = null;
        zzvc[] zzvcVarArr = null;
        String[] strArr = null;
        zzux[] zzuxVarArr = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 1:
                    zzveVar = (zzve) SafeParcelReader.n(parcel, B, zzve.CREATOR);
                    break;
                case 2:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 3:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 4:
                    zzvfVarArr = (zzvf[]) SafeParcelReader.r(parcel, B, zzvf.CREATOR);
                    break;
                case 5:
                    zzvcVarArr = (zzvc[]) SafeParcelReader.r(parcel, B, zzvc.CREATOR);
                    break;
                case 6:
                    strArr = SafeParcelReader.p(parcel, B);
                    break;
                case 7:
                    zzuxVarArr = (zzux[]) SafeParcelReader.r(parcel, B, zzux.CREATOR);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzva(zzveVar, str, str2, zzvfVarArr, zzvcVarArr, strArr, zzuxVarArr);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzva[i8];
    }
}
