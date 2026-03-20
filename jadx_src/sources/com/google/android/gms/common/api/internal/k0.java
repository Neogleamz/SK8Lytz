package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class k0 extends LifecycleCallback implements DialogInterface.OnCancelListener {

    /* renamed from: b  reason: collision with root package name */
    protected volatile boolean f11667b;

    /* renamed from: c  reason: collision with root package name */
    protected final AtomicReference f11668c;

    /* renamed from: d  reason: collision with root package name */
    private final Handler f11669d;

    /* renamed from: e  reason: collision with root package name */
    protected final com.google.android.gms.common.a f11670e;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public k0(l6.f fVar, com.google.android.gms.common.a aVar) {
        super(fVar);
        this.f11668c = new AtomicReference(null);
        this.f11669d = new a7.k(Looper.getMainLooper());
        this.f11670e = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void l(ConnectionResult connectionResult, int i8) {
        this.f11668c.set(null);
        m(connectionResult, i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void o() {
        this.f11668c.set(null);
        n();
    }

    private static final int p(h0 h0Var) {
        if (h0Var == null) {
            return -1;
        }
        return h0Var.a();
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public final void e(int i8, int i9, Intent intent) {
        h0 h0Var = (h0) this.f11668c.get();
        if (i8 != 1) {
            if (i8 == 2) {
                int g8 = this.f11670e.g(b());
                if (g8 == 0) {
                    o();
                    return;
                } else if (h0Var == null) {
                    return;
                } else {
                    if (h0Var.b().t() == 18 && g8 == 18) {
                        return;
                    }
                }
            }
        } else if (i9 == -1) {
            o();
            return;
        } else if (i9 == 0) {
            if (h0Var == null) {
                return;
            }
            l(new ConnectionResult(intent != null ? intent.getIntExtra("<<ResolutionFailureErrorDetail>>", 13) : 13, null, h0Var.b().toString()), p(h0Var));
            return;
        }
        if (h0Var != null) {
            l(h0Var.b(), h0Var.a());
        }
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public final void f(Bundle bundle) {
        super.f(bundle);
        if (bundle != null) {
            this.f11668c.set(bundle.getBoolean("resolving_error", false) ? new h0(new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution")), bundle.getInt("failed_client_id", -1)) : null);
        }
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public final void i(Bundle bundle) {
        super.i(bundle);
        h0 h0Var = (h0) this.f11668c.get();
        if (h0Var == null) {
            return;
        }
        bundle.putBoolean("resolving_error", true);
        bundle.putInt("failed_client_id", h0Var.a());
        bundle.putInt("failed_status", h0Var.b().t());
        bundle.putParcelable("failed_resolution", h0Var.b().Z());
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public void j() {
        super.j();
        this.f11667b = true;
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public void k() {
        super.k();
        this.f11667b = false;
    }

    protected abstract void m(ConnectionResult connectionResult, int i8);

    protected abstract void n();

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        l(new ConnectionResult(13, null), p((h0) this.f11668c.get()));
    }

    public final void s(ConnectionResult connectionResult, int i8) {
        h0 h0Var = new h0(connectionResult, i8);
        AtomicReference atomicReference = this.f11668c;
        while (!atomicReference.compareAndSet(null, h0Var)) {
            if (atomicReference.get() != null) {
                return;
            }
        }
        this.f11669d.post(new j0(this, h0Var));
    }
}
