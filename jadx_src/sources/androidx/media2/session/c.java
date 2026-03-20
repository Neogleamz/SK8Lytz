package androidx.media2.session;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.media2.session.a;
import androidx.versionedparcelable.ParcelImpl;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface c extends IInterface {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends Binder implements c {
        public a() {
            attachInterface(this, "androidx.media2.session.IMediaSessionService");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
            if (i8 == 1) {
                parcel.enforceInterface("androidx.media2.session.IMediaSessionService");
                F0(a.AbstractBinderC0064a.d(parcel.readStrongBinder()), parcel.readInt() != 0 ? ParcelImpl.CREATOR.createFromParcel(parcel) : null);
                return true;
            } else if (i8 != 1598968902) {
                return super.onTransact(i8, parcel, parcel2, i9);
            } else {
                parcel2.writeString("androidx.media2.session.IMediaSessionService");
                return true;
            }
        }
    }

    void F0(androidx.media2.session.a aVar, ParcelImpl parcelImpl);
}
