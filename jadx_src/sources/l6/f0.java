package l6;

import android.os.Bundle;
import com.google.android.gms.common.api.internal.LifecycleCallback;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f0 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ LifecycleCallback f21744a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ String f21745b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ g0 f21746c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f0(g0 g0Var, LifecycleCallback lifecycleCallback, String str) {
        this.f21746c = g0Var;
        this.f21744a = lifecycleCallback;
        this.f21745b = str;
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
        g0 g0Var = this.f21746c;
        i8 = g0Var.f21761b;
        if (i8 > 0) {
            LifecycleCallback lifecycleCallback = this.f21744a;
            bundle = g0Var.f21762c;
            if (bundle != null) {
                String str = this.f21745b;
                bundle3 = g0Var.f21762c;
                bundle2 = bundle3.getBundle(str);
            } else {
                bundle2 = null;
            }
            lifecycleCallback.f(bundle2);
        }
        i9 = this.f21746c.f21761b;
        if (i9 >= 2) {
            this.f21744a.j();
        }
        i10 = this.f21746c.f21761b;
        if (i10 >= 3) {
            this.f21744a.h();
        }
        i11 = this.f21746c.f21761b;
        if (i11 >= 4) {
            this.f21744a.k();
        }
        i12 = this.f21746c.f21761b;
        if (i12 >= 5) {
            this.f21744a.g();
        }
    }
}
