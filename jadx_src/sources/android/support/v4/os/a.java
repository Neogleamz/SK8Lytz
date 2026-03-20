package android.support.v4.os;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface a extends IInterface {

    /* renamed from: android.support.v4.os.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractBinderC0007a extends Binder implements a {

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: android.support.v4.os.a$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0008a implements a {

            /* renamed from: b  reason: collision with root package name */
            public static a f347b;

            /* renamed from: a  reason: collision with root package name */
            private IBinder f348a;

            C0008a(IBinder iBinder) {
                this.f348a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f348a;
            }

            @Override // android.support.v4.os.a
            public void c2(int i8, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.os.IResultReceiver");
                    obtain.writeInt(i8);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.f348a.transact(1, obtain, null, 1) || AbstractBinderC0007a.e() == null) {
                        return;
                    }
                    AbstractBinderC0007a.e().c2(i8, bundle);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public AbstractBinderC0007a() {
            attachInterface(this, "android.support.v4.os.IResultReceiver");
        }

        public static a d(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("android.support.v4.os.IResultReceiver");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof a)) ? new C0008a(iBinder) : (a) queryLocalInterface;
        }

        public static a e() {
            return C0008a.f347b;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
            if (i8 == 1) {
                parcel.enforceInterface("android.support.v4.os.IResultReceiver");
                c2(parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                return true;
            } else if (i8 != 1598968902) {
                return super.onTransact(i8, parcel, parcel2, i9);
            } else {
                parcel2.writeString("android.support.v4.os.IResultReceiver");
                return true;
            }
        }
    }

    void c2(int i8, Bundle bundle);
}
