package q6;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        Long l8 = null;
        Long l9 = null;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 1) {
                i8 = SafeParcelReader.D(parcel, B);
            } else if (u8 == 2) {
                i9 = SafeParcelReader.D(parcel, B);
            } else if (u8 == 3) {
                l8 = SafeParcelReader.F(parcel, B);
            } else if (u8 == 4) {
                l9 = SafeParcelReader.F(parcel, B);
            } else if (u8 != 5) {
                SafeParcelReader.H(parcel, B);
            } else {
                i10 = SafeParcelReader.D(parcel, B);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new ModuleInstallStatusUpdate(i8, i9, l8, l9, i10);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new ModuleInstallStatusUpdate[i8];
    }
}
