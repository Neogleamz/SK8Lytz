package androidx.media2.session;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface b extends IInterface {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends Binder implements b {

        /* renamed from: androidx.media2.session.b$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class C0066a implements b {

            /* renamed from: a  reason: collision with root package name */
            private IBinder f6238a;

            C0066a(IBinder iBinder) {
                this.f6238a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f6238a;
            }
        }

        public static b d(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("androidx.media2.session.IMediaSession");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof b)) ? new C0066a(iBinder) : (b) queryLocalInterface;
        }
    }
}
