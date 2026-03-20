package n6;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.zzk;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b0 extends com.google.android.gms.internal.common.g implements f {
    public b0() {
        super("com.google.android.gms.common.internal.IGmsCallbacks");
    }

    @Override // com.google.android.gms.internal.common.g
    protected final boolean d(int i8, Parcel parcel, Parcel parcel2, int i9) {
        if (i8 == 1) {
            com.google.android.gms.internal.common.h.b(parcel);
            D0(parcel.readInt(), parcel.readStrongBinder(), (Bundle) com.google.android.gms.internal.common.h.a(parcel, Bundle.CREATOR));
        } else if (i8 == 2) {
            com.google.android.gms.internal.common.h.b(parcel);
            a0(parcel.readInt(), (Bundle) com.google.android.gms.internal.common.h.a(parcel, Bundle.CREATOR));
        } else if (i8 != 3) {
            return false;
        } else {
            com.google.android.gms.internal.common.h.b(parcel);
            O1(parcel.readInt(), parcel.readStrongBinder(), (zzk) com.google.android.gms.internal.common.h.a(parcel, zzk.CREATOR));
        }
        parcel2.writeNoException();
        return true;
    }
}
