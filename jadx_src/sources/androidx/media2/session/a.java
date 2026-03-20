package androidx.media2.session;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface a extends IInterface {

    /* renamed from: androidx.media2.session.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractBinderC0064a extends Binder implements a {

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: androidx.media2.session.a$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0065a implements a {

            /* renamed from: b  reason: collision with root package name */
            public static a f6236b;

            /* renamed from: a  reason: collision with root package name */
            private IBinder f6237a;

            C0065a(IBinder iBinder) {
                this.f6237a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f6237a;
            }

            @Override // androidx.media2.session.a
            public void h(int i8) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("androidx.media2.session.IMediaController");
                    obtain.writeInt(i8);
                    if (this.f6237a.transact(13, obtain, null, 1) || AbstractBinderC0064a.e() == null) {
                        return;
                    }
                    AbstractBinderC0064a.e().h(i8);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static a d(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("androidx.media2.session.IMediaController");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof a)) ? new C0065a(iBinder) : (a) queryLocalInterface;
        }

        public static a e() {
            return C0065a.f6236b;
        }
    }

    void h(int i8);
}
