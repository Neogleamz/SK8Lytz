package j6;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        long j8 = -1;
        int i8 = 0;
        String str = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 1) {
                str = SafeParcelReader.o(parcel, B);
            } else if (u8 == 2) {
                i8 = SafeParcelReader.D(parcel, B);
            } else if (u8 != 3) {
                SafeParcelReader.H(parcel, B);
            } else {
                j8 = SafeParcelReader.E(parcel, B);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new Feature(str, i8, j8);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new Feature[i8];
    }
}
