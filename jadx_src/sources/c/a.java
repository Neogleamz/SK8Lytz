package c;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface a extends IInterface {

    /* renamed from: c.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractBinderC0094a extends Binder implements a {

        /* renamed from: c.a$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class C0095a implements a {

            /* renamed from: a  reason: collision with root package name */
            private IBinder f8207a;

            C0095a(IBinder iBinder) {
                this.f8207a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f8207a;
            }
        }

        public static a d(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("android.support.customtabs.trusted.ITrustedWebActivityCallback");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof a)) ? new C0095a(iBinder) : (a) queryLocalInterface;
        }
    }
}
