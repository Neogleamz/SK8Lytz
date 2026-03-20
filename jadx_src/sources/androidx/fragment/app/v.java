package androidx.fragment.app;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.a0;
import androidx.lifecycle.f0;
import androidx.lifecycle.i0;
import androidx.lifecycle.j0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v implements androidx.lifecycle.e, s1.d, j0 {

    /* renamed from: a  reason: collision with root package name */
    private final Fragment f5760a;

    /* renamed from: b  reason: collision with root package name */
    private final i0 f5761b;

    /* renamed from: c  reason: collision with root package name */
    private f0.b f5762c;

    /* renamed from: d  reason: collision with root package name */
    private androidx.lifecycle.k f5763d = null;

    /* renamed from: e  reason: collision with root package name */
    private s1.c f5764e = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(Fragment fragment, i0 i0Var) {
        this.f5760a = fragment;
        this.f5761b = i0Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Lifecycle.Event event) {
        this.f5763d.h(event);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (this.f5763d == null) {
            this.f5763d = new androidx.lifecycle.k(this);
            this.f5764e = s1.c.a(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c() {
        return this.f5763d != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(Bundle bundle) {
        this.f5764e.d(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(Bundle bundle) {
        this.f5764e.e(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(Lifecycle.State state) {
        this.f5763d.n(state);
    }

    @Override // androidx.lifecycle.e
    public f0.b getDefaultViewModelProviderFactory() {
        f0.b defaultViewModelProviderFactory = this.f5760a.getDefaultViewModelProviderFactory();
        if (!defaultViewModelProviderFactory.equals(this.f5760a.f5407j0)) {
            this.f5762c = defaultViewModelProviderFactory;
            return defaultViewModelProviderFactory;
        }
        if (this.f5762c == null) {
            Application application = null;
            Context applicationContext = this.f5760a.l1().getApplicationContext();
            while (true) {
                if (!(applicationContext instanceof ContextWrapper)) {
                    break;
                } else if (applicationContext instanceof Application) {
                    application = applicationContext;
                    break;
                } else {
                    applicationContext = ((ContextWrapper) applicationContext).getBaseContext();
                }
            }
            this.f5762c = new a0(application, this, this.f5760a.p());
        }
        return this.f5762c;
    }

    @Override // androidx.lifecycle.j
    public Lifecycle getLifecycle() {
        b();
        return this.f5763d;
    }

    @Override // s1.d
    public androidx.savedstate.a getSavedStateRegistry() {
        b();
        return this.f5764e.b();
    }

    @Override // androidx.lifecycle.j0
    public i0 getViewModelStore() {
        b();
        return this.f5761b;
    }
}
