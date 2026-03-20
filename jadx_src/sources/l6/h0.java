package l6;

import android.os.Bundle;
import com.google.android.gms.common.api.internal.LifecycleCallback;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h0 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ LifecycleCallback f21763a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ String f21764b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ i0 f21765c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h0(i0 i0Var, LifecycleCallback lifecycleCallback, String str) {
        this.f21765c = i0Var;
        this.f21763a = lifecycleCallback;
        this.f21764b = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        Bundle bundle;
        Bundle bundle2;
        Bundle bundle3;
        i0 i0Var = this.f21765c;
        i8 = i0Var.f21768q0;
        if (i8 > 0) {
            LifecycleCallback lifecycleCallback = this.f21763a;
            bundle = i0Var.f21769r0;
            if (bundle != null) {
                String str = this.f21764b;
                bundle3 = i0Var.f21769r0;
                bundle2 = bundle3.getBundle(str);
            } else {
                bundle2 = null;
            }
            lifecycleCallback.f(bundle2);
        }
        i9 = this.f21765c.f21768q0;
        if (i9 >= 2) {
            this.f21763a.j();
        }
        i10 = this.f21765c.f21768q0;
        if (i10 >= 3) {
            this.f21763a.h();
        }
        i11 = this.f21765c.f21768q0;
        if (i11 >= 4) {
            this.f21763a.k();
        }
        i12 = this.f21765c.f21768q0;
        if (i12 >= 5) {
            this.f21763a.g();
        }
    }
}
