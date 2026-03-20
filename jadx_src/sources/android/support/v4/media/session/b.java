package android.support.v4.media.session;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.view.KeyEvent;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface b extends IInterface {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends Binder implements b {

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: android.support.v4.media.session.b$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0006a implements b {

            /* renamed from: b  reason: collision with root package name */
            public static b f338b;

            /* renamed from: a  reason: collision with root package name */
            private IBinder f339a;

            C0006a(IBinder iBinder) {
                this.f339a = iBinder;
            }

            @Override // android.support.v4.media.session.b
            public boolean E0(KeyEvent keyEvent) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                    if (keyEvent != null) {
                        obtain.writeInt(1);
                        keyEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.f339a.transact(2, obtain, obtain2, 0) || a.e() == null) {
                        obtain2.readException();
                        return obtain2.readInt() != 0;
                    }
                    return a.e().E0(keyEvent);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f339a;
            }

            @Override // android.support.v4.media.session.b
            public void i(android.support.v4.media.session.a aVar) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                    obtain.writeStrongBinder(aVar != null ? aVar.asBinder() : null);
                    if (this.f339a.transact(3, obtain, obtain2, 0) || a.e() == null) {
                        obtain2.readException();
                    } else {
                        a.e().i(aVar);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static b d(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("android.support.v4.media.session.IMediaSession");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof b)) ? new C0006a(iBinder) : (b) queryLocalInterface;
        }

        public static b e() {
            return C0006a.f338b;
        }
    }

    boolean E0(KeyEvent keyEvent);

    void i(android.support.v4.media.session.a aVar);
}
