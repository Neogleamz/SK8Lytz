package l6;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.c;
import com.google.android.gms.common.internal.zav;
import com.google.android.gms.signin.internal.zak;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b0 extends h7.a implements c.a, c.b {

    /* renamed from: h  reason: collision with root package name */
    private static final a.AbstractC0121a f21730h = g7.e.f20216c;

    /* renamed from: a  reason: collision with root package name */
    private final Context f21731a;

    /* renamed from: b  reason: collision with root package name */
    private final Handler f21732b;

    /* renamed from: c  reason: collision with root package name */
    private final a.AbstractC0121a f21733c;

    /* renamed from: d  reason: collision with root package name */
    private final Set f21734d;

    /* renamed from: e  reason: collision with root package name */
    private final n6.c f21735e;

    /* renamed from: f  reason: collision with root package name */
    private g7.f f21736f;

    /* renamed from: g  reason: collision with root package name */
    private a0 f21737g;

    public b0(Context context, Handler handler, n6.c cVar) {
        a.AbstractC0121a abstractC0121a = f21730h;
        this.f21731a = context;
        this.f21732b = handler;
        this.f21735e = (n6.c) n6.j.m(cVar, "ClientSettings must not be null");
        this.f21734d = cVar.e();
        this.f21733c = abstractC0121a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void q(b0 b0Var, zak zakVar) {
        ConnectionResult t8 = zakVar.t();
        if (t8.E0()) {
            zav zavVar = (zav) n6.j.l(zakVar.u());
            t8 = zavVar.t();
            if (t8.E0()) {
                b0Var.f21737g.c(zavVar.u(), b0Var.f21734d);
                b0Var.f21736f.disconnect();
            }
            String valueOf = String.valueOf(t8);
            Log.wtf("SignInCoordinator", "Sign-in succeeded with resolve account failure: ".concat(valueOf), new Exception());
        }
        b0Var.f21737g.b(t8);
        b0Var.f21736f.disconnect();
    }

    @Override // l6.c
    public final void d(int i8) {
        this.f21736f.disconnect();
    }

    @Override // l6.h
    public final void e(ConnectionResult connectionResult) {
        this.f21737g.b(connectionResult);
    }

    @Override // l6.c
    public final void f(Bundle bundle) {
        this.f21736f.d(this);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.google.android.gms.common.api.a$f, g7.f] */
    public final void v(a0 a0Var) {
        g7.f fVar = this.f21736f;
        if (fVar != null) {
            fVar.disconnect();
        }
        this.f21735e.i(Integer.valueOf(System.identityHashCode(this)));
        a.AbstractC0121a abstractC0121a = this.f21733c;
        Context context = this.f21731a;
        Looper looper = this.f21732b.getLooper();
        n6.c cVar = this.f21735e;
        this.f21736f = abstractC0121a.a(context, looper, cVar, cVar.f(), this, this);
        this.f21737g = a0Var;
        Set set = this.f21734d;
        if (set == null || set.isEmpty()) {
            this.f21732b.post(new y(this));
        } else {
            this.f21736f.n();
        }
    }

    public final void x() {
        g7.f fVar = this.f21736f;
        if (fVar != null) {
            fVar.disconnect();
        }
    }

    @Override // h7.c
    public final void z(zak zakVar) {
        this.f21732b.post(new z(this, zakVar));
    }
}
