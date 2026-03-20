package l6;

import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.common.api.Status;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface d extends IInterface {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends a7.b implements d {
        public a() {
            super("com.google.android.gms.common.api.internal.IStatusCallback");
        }

        @Override // a7.b
        protected final boolean g(int i8, Parcel parcel, Parcel parcel2, int i9) {
            if (i8 == 1) {
                a7.c.b(parcel);
                V((Status) a7.c.a(parcel, Status.CREATOR));
                return true;
            }
            return false;
        }
    }

    void V(Status status);
}
