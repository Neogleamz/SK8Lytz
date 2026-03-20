package n6;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j0 implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        boolean z4 = false;
        boolean z8 = false;
        int i8 = 0;
        RootTelemetryConfiguration rootTelemetryConfiguration = null;
        int[] iArr = null;
        int[] iArr2 = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 1:
                    rootTelemetryConfiguration = (RootTelemetryConfiguration) SafeParcelReader.n(parcel, B, RootTelemetryConfiguration.CREATOR);
                    break;
                case 2:
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
                case 3:
                    z8 = SafeParcelReader.v(parcel, B);
                    break;
                case 4:
                    iArr = SafeParcelReader.j(parcel, B);
                    break;
                case 5:
                    i8 = SafeParcelReader.D(parcel, B);
                    break;
                case 6:
                    iArr2 = SafeParcelReader.j(parcel, B);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new ConnectionTelemetryConfiguration(rootTelemetryConfiguration, z4, z8, iArr, i8, iArr2);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new ConnectionTelemetryConfiguration[i8];
    }
}
