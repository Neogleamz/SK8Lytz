package n6;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        String str = null;
        String str2 = null;
        long j8 = 0;
        long j9 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = -1;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 1:
                    i8 = SafeParcelReader.D(parcel, B);
                    break;
                case 2:
                    i9 = SafeParcelReader.D(parcel, B);
                    break;
                case 3:
                    i10 = SafeParcelReader.D(parcel, B);
                    break;
                case 4:
                    j8 = SafeParcelReader.E(parcel, B);
                    break;
                case 5:
                    j9 = SafeParcelReader.E(parcel, B);
                    break;
                case 6:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 7:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 8:
                    i11 = SafeParcelReader.D(parcel, B);
                    break;
                case 9:
                    i12 = SafeParcelReader.D(parcel, B);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new MethodInvocation(i8, i9, i10, j8, j9, str, str2, i11, i12);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new MethodInvocation[i8];
    }
}
