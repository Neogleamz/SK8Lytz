package b;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import b.a;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface b extends IInterface {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends Binder implements b {
        public a() {
            attachInterface(this, "android.support.customtabs.ICustomTabsService");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
            int i10;
            if (i8 >= 1 && i8 <= 16777215) {
                parcel.enforceInterface("android.support.customtabs.ICustomTabsService");
            }
            if (i8 == 1598968902) {
                parcel2.writeString("android.support.customtabs.ICustomTabsService");
                return true;
            }
            switch (i8) {
                case 2:
                    i10 = p0(parcel.readLong());
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 3:
                    i10 = y0(a.AbstractBinderC0088a.d(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 4:
                    Parcelable.Creator creator = Bundle.CREATOR;
                    i10 = d0(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), (Uri) C0090b.c(parcel, Uri.CREATOR), (Bundle) C0090b.c(parcel, creator), parcel.createTypedArrayList(creator));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 5:
                    Bundle c02 = c0(parcel.readString(), (Bundle) C0090b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    C0090b.d(parcel2, c02, 1);
                    break;
                case 6:
                    i10 = m1(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), (Bundle) C0090b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 7:
                    i10 = P0(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), (Uri) C0090b.c(parcel, Uri.CREATOR));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 8:
                    i10 = o(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), parcel.readString(), (Bundle) C0090b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 9:
                    i10 = l1(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), parcel.readInt(), (Uri) C0090b.c(parcel, Uri.CREATOR), (Bundle) C0090b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 10:
                    i10 = s1(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), (Bundle) C0090b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 11:
                    i10 = n(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), (Uri) C0090b.c(parcel, Uri.CREATOR), (Bundle) C0090b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                case 12:
                    i10 = g0(a.AbstractBinderC0088a.d(parcel.readStrongBinder()), (Uri) C0090b.c(parcel, Uri.CREATOR), parcel.readInt(), (Bundle) C0090b.c(parcel, Bundle.CREATOR));
                    parcel2.writeNoException();
                    parcel2.writeInt(i10);
                    break;
                default:
                    return super.onTransact(i8, parcel, parcel2, i9);
            }
            return true;
        }
    }

    /* renamed from: b.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0090b {
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

    boolean P0(b.a aVar, Uri uri);

    Bundle c0(String str, Bundle bundle);

    boolean d0(b.a aVar, Uri uri, Bundle bundle, List<Bundle> list);

    boolean g0(b.a aVar, Uri uri, int i8, Bundle bundle);

    boolean l1(b.a aVar, int i8, Uri uri, Bundle bundle);

    boolean m1(b.a aVar, Bundle bundle);

    boolean n(b.a aVar, Uri uri, Bundle bundle);

    int o(b.a aVar, String str, Bundle bundle);

    boolean p0(long j8);

    boolean s1(b.a aVar, Bundle bundle);

    boolean y0(b.a aVar);
}
