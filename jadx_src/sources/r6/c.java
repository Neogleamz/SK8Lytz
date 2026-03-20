package r6;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.common.moduleinstall.internal.ApiFeatureRequest;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        ArrayList arrayList = null;
        String str = null;
        boolean z4 = false;
        String str2 = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 1) {
                arrayList = SafeParcelReader.s(parcel, B, Feature.CREATOR);
            } else if (u8 == 2) {
                z4 = SafeParcelReader.v(parcel, B);
            } else if (u8 == 3) {
                str2 = SafeParcelReader.o(parcel, B);
            } else if (u8 != 4) {
                SafeParcelReader.H(parcel, B);
            } else {
                str = SafeParcelReader.o(parcel, B);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new ApiFeatureRequest(arrayList, z4, str2, str);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new ApiFeatureRequest[i8];
    }
}
