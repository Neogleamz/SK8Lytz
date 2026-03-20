package r0;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface a extends IInterface {

    /* renamed from: r0.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractBinderC0202a extends Binder implements a {

        /* renamed from: r0.a$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class C0203a implements a {

            /* renamed from: a  reason: collision with root package name */
            private IBinder f22641a;

            C0203a(IBinder iBinder) {
                this.f22641a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f22641a;
            }
        }

        public static a d(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof a)) ? new C0203a(iBinder) : (a) queryLocalInterface;
        }
    }
}
