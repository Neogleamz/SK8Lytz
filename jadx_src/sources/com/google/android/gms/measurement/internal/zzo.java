package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.internal.measurement.ye;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzo extends BroadcastReceiver {

    /* renamed from: a  reason: collision with root package name */
    private final f6 f17315a;

    public zzo(f6 f6Var) {
        this.f17315a = f6Var;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (intent == null) {
            this.f17315a.i().J().a("App receiver called with null intent");
            return;
        }
        String action = intent.getAction();
        if (action == null) {
            this.f17315a.i().J().a("App receiver called with null action");
        } else if (!action.equals("com.google.android.gms.measurement.TRIGGERS_AVAILABLE")) {
            this.f17315a.i().J().a("App receiver called with unknown action");
        } else {
            final f6 f6Var = this.f17315a;
            if (ye.a() && f6Var.x().D(null, c0.K0)) {
                f6Var.i().I().a("App receiver notified triggers are available");
                f6Var.l().B(new Runnable() { // from class: com.google.android.gms.measurement.internal.ub
                    @Override // java.lang.Runnable
                    public final void run() {
                        f6 f6Var2 = f6.this;
                        if (!f6Var2.J().V0()) {
                            f6Var2.i().J().a("registerTrigger called but app not eligible");
                            return;
                        }
                        final h7 F = f6Var2.F();
                        Objects.requireNonNull(F);
                        new Thread(new Runnable() { // from class: com.google.android.gms.measurement.internal.vb
                            @Override // java.lang.Runnable
                            public final void run() {
                                h7.this.o0();
                            }
                        }).start();
                    }
                });
            }
        }
    }
}
