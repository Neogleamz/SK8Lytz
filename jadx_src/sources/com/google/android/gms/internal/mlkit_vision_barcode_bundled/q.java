package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        int i8 = 0;
        int i9 = 0;
        String str = null;
        String str2 = null;
        byte[] bArr = null;
        Point[] pointArr = null;
        zzat zzatVar = null;
        zzaw zzawVar = null;
        zzax zzaxVar = null;
        zzaz zzazVar = null;
        zzay zzayVar = null;
        zzau zzauVar = null;
        zzaq zzaqVar = null;
        zzar zzarVar = null;
        zzas zzasVar = null;
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
                    zzatVar = (zzat) SafeParcelReader.n(parcel, B, zzat.CREATOR);
                    break;
                case 8:
                    zzawVar = (zzaw) SafeParcelReader.n(parcel, B, zzaw.CREATOR);
                    break;
                case 9:
                    zzaxVar = (zzax) SafeParcelReader.n(parcel, B, zzax.CREATOR);
                    break;
                case 10:
                    zzazVar = (zzaz) SafeParcelReader.n(parcel, B, zzaz.CREATOR);
                    break;
                case 11:
                    zzayVar = (zzay) SafeParcelReader.n(parcel, B, zzay.CREATOR);
                    break;
                case 12:
                    zzauVar = (zzau) SafeParcelReader.n(parcel, B, zzau.CREATOR);
                    break;
                case 13:
                    zzaqVar = (zzaq) SafeParcelReader.n(parcel, B, zzaq.CREATOR);
                    break;
                case 14:
                    zzarVar = (zzar) SafeParcelReader.n(parcel, B, zzar.CREATOR);
                    break;
                case 15:
                    zzasVar = (zzas) SafeParcelReader.n(parcel, B, zzas.CREATOR);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzba(i8, str, str2, bArr, pointArr, i9, zzatVar, zzawVar, zzaxVar, zzazVar, zzayVar, zzauVar, zzaqVar, zzarVar, zzasVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzba[i8];
    }
}
