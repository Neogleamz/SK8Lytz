package b;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import b.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface c extends IInterface {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends Binder implements c {
        public a() {
            attachInterface(this, "android.support.customtabs.IPostMessageService");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
            if (i8 >= 1 && i8 <= 16777215) {
                parcel.enforceInterface("android.support.customtabs.IPostMessageService");
            }
            if (i8 == 1598968902) {
                parcel2.writeString("android.support.customtabs.IPostMessageService");
                return true;
            }
            if (i8 == 2) {
                L0(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), (Bundle) b.b(parcel, Bundle.CREATOR));
            } else if (i8 != 3) {
                return super.onTransact(i8, parcel, parcel2, i9);
            } else {
                x1(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), parcel.readString(), (Bundle) b.b(parcel, Bundle.CREATOR));
            }
            parcel2.writeNoException();
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        /* JADX INFO: Access modifiers changed from: private */
        public static <T> T b(Parcel parcel, Parcelable.Creator<T> creator) {
            if (parcel.readInt() != 0) {
                return creator.createFromParcel(parcel);
            }
            return null;
        }
    }

    void L0(b.a aVar, Bundle bundle);

    void x1(b.a aVar, String str, Bundle bundle);
}
