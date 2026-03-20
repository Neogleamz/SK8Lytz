package f7;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.measurement.internal.zzba;
import com.google.android.gms.measurement.internal.zzbf;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements Parcelable.Creator<zzbf> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbf createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        String str = null;
        zzba zzbaVar = null;
        String str2 = null;
        long j8 = 0;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 2) {
                str = SafeParcelReader.o(parcel, B);
            } else if (u8 == 3) {
                zzbaVar = (zzba) SafeParcelReader.n(parcel, B, zzba.CREATOR);
            } else if (u8 == 4) {
                str2 = SafeParcelReader.o(parcel, B);
            } else if (u8 != 5) {
                SafeParcelReader.H(parcel, B);
            } else {
                j8 = SafeParcelReader.E(parcel, B);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzbf(str, zzbaVar, str2, j8);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbf[] newArray(int i8) {
        return new zzbf[i8];
    }
}
