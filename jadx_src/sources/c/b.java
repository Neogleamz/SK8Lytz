package c;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface b extends IInterface {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends Binder implements b {
        public a() {
            attachInterface(this, "android.support.customtabs.trusted.ITrustedWebActivityService");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
            Bundle r12;
            if (i8 >= 1 && i8 <= 16777215) {
                parcel.enforceInterface("android.support.customtabs.trusted.ITrustedWebActivityService");
            }
            if (i8 == 1598968902) {
                parcel2.writeString("android.support.customtabs.trusted.ITrustedWebActivityService");
                return true;
            }
            switch (i8) {
                case 2:
                    r12 = r1((Bundle) C0096b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    C0096b.d(parcel2, r12, 1);
                    break;
                case 3:
                    p1((Bundle) C0096b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    break;
                case 4:
                    int S0 = S0();
                    parcel2.writeNoException();
                    parcel2.writeInt(S0);
                    break;
                case 5:
                    r12 = r();
                    parcel2.writeNoException();
                    C0096b.d(parcel2, r12, 1);
                    break;
                case 6:
                    r12 = c1((Bundle) C0096b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    C0096b.d(parcel2, r12, 1);
                    break;
                case 7:
                    r12 = W0();
                    parcel2.writeNoException();
                    C0096b.d(parcel2, r12, 1);
                    break;
                case 8:
                default:
                    return super.onTransact(i8, parcel, parcel2, i9);
                case 9:
                    r12 = j0(parcel.readString(), (Bundle) C0096b.c(parcel, Bundle.CREATOR), parcel.readStrongBinder());
                    parcel2.writeNoException();
                    C0096b.d(parcel2, r12, 1);
                    break;
            }
            return true;
        }
    }

    /* renamed from: c.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0096b {
        /* JADX INFO: Access modifiers changed from: private */
        public static <T> T c(Parcel parcel, Parcelable.Creator<T> creator) {
            if (parcel.readInt() != 0) {
                return creator.createFromParcel(parcel);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static <T extends Parcelable> void d(Parcel parcel, T t8, int i8) {
            if (t8 == null) {
                parcel.writeInt(0);
                return;
            }
            parcel.writeInt(1);
            t8.writeToParcel(parcel, i8);
        }
    }

    int S0();

    Bundle W0();

    Bundle c1(Bundle bundle);

    Bundle j0(String str, Bundle bundle, IBinder iBinder);

    void p1(Bundle bundle);

    Bundle r();

    Bundle r1(Bundle bundle);
}
