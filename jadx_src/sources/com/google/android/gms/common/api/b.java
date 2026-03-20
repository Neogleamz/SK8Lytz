package com.google.android.gms.common.api;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.common.api.internal.c;
import com.google.android.gms.common.api.internal.f;
import com.google.android.gms.common.api.internal.g;
import com.google.android.gms.common.api.internal.k;
import com.google.android.gms.common.api.internal.r;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import l6.b0;
import l6.j;
import l6.o;
import n6.c;
import u6.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b<O extends a.d> {

    /* renamed from: a  reason: collision with root package name */
    private final Context f11563a;

    /* renamed from: b  reason: collision with root package name */
    private final String f11564b;

    /* renamed from: c  reason: collision with root package name */
    private final com.google.android.gms.common.api.a f11565c;

    /* renamed from: d  reason: collision with root package name */
    private final a.d f11566d;

    /* renamed from: e  reason: collision with root package name */
    private final l6.b f11567e;

    /* renamed from: f  reason: collision with root package name */
    private final Looper f11568f;

    /* renamed from: g  reason: collision with root package name */
    private final int f11569g;

    /* renamed from: h  reason: collision with root package name */
    private final c f11570h;

    /* renamed from: i  reason: collision with root package name */
    private final j f11571i;

    /* renamed from: j  reason: collision with root package name */
    protected final com.google.android.gms.common.api.internal.b f11572j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: c  reason: collision with root package name */
        public static final a f11573c = new C0123a().a();

        /* renamed from: a  reason: collision with root package name */
        public final j f11574a;

        /* renamed from: b  reason: collision with root package name */
        public final Looper f11575b;

        /* renamed from: com.google.android.gms.common.api.b$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0123a {

            /* renamed from: a  reason: collision with root package name */
            private j f11576a;

            /* renamed from: b  reason: collision with root package name */
            private Looper f11577b;

            public a a() {
                if (this.f11576a == null) {
                    this.f11576a = new l6.a();
                }
                if (this.f11577b == null) {
                    this.f11577b = Looper.getMainLooper();
                }
                return new a(this.f11576a, this.f11577b);
            }
        }

        private a(j jVar, Account account, Looper looper) {
            this.f11574a = jVar;
            this.f11575b = looper;
        }
    }

    private b(Context context, Activity activity, com.google.android.gms.common.api.a aVar, a.d dVar, a aVar2) {
        n6.j.m(context, "Null context is not permitted.");
        n6.j.m(aVar, "Api must not be null.");
        n6.j.m(aVar2, "Settings must not be null; use Settings.DEFAULT_SETTINGS instead.");
        this.f11563a = context.getApplicationContext();
        String str = null;
        if (m.k()) {
            try {
                str = (String) Context.class.getMethod("getAttributionTag", new Class[0]).invoke(context, new Object[0]);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
            }
        }
        this.f11564b = str;
        this.f11565c = aVar;
        this.f11566d = dVar;
        this.f11568f = aVar2.f11575b;
        l6.b a9 = l6.b.a(aVar, dVar, str);
        this.f11567e = a9;
        this.f11570h = new o(this);
        com.google.android.gms.common.api.internal.b x8 = com.google.android.gms.common.api.internal.b.x(this.f11563a);
        this.f11572j = x8;
        this.f11569g = x8.m();
        this.f11571i = aVar2.f11574a;
        if (activity != null && !(activity instanceof GoogleApiActivity) && Looper.myLooper() == Looper.getMainLooper()) {
            k.u(activity, x8, a9);
        }
        x8.b(this);
    }

    public b(Context context, com.google.android.gms.common.api.a<O> aVar, O o5, a aVar2) {
        this(context, null, aVar, o5, aVar2);
    }

    private final j7.j o(int i8, g gVar) {
        j7.k kVar = new j7.k();
        this.f11572j.F(this, i8, gVar, kVar, this.f11571i);
        return kVar.a();
    }

    protected c.a d() {
        Account n8;
        GoogleSignInAccount i8;
        GoogleSignInAccount i9;
        c.a aVar = new c.a();
        a.d dVar = this.f11566d;
        if (!(dVar instanceof a.d.b) || (i9 = ((a.d.b) dVar).i()) == null) {
            a.d dVar2 = this.f11566d;
            n8 = dVar2 instanceof a.d.InterfaceC0122a ? ((a.d.InterfaceC0122a) dVar2).n() : null;
        } else {
            n8 = i9.n();
        }
        aVar.d(n8);
        a.d dVar3 = this.f11566d;
        aVar.c((!(dVar3 instanceof a.d.b) || (i8 = ((a.d.b) dVar3).i()) == null) ? Collections.emptySet() : i8.T0());
        aVar.e(this.f11563a.getClass().getName());
        aVar.b(this.f11563a.getPackageName());
        return aVar;
    }

    public <TResult, A extends a.b> j7.j<TResult> e(g<A, TResult> gVar) {
        return o(2, gVar);
    }

    public <TResult, A extends a.b> j7.j<TResult> f(g<A, TResult> gVar) {
        return o(0, gVar);
    }

    public <A extends a.b> j7.j<Void> g(f<A, ?> fVar) {
        n6.j.l(fVar);
        n6.j.m(fVar.f11635a.b(), "Listener has already been released.");
        n6.j.m(fVar.f11636b.a(), "Listener has already been released.");
        return this.f11572j.z(this, fVar.f11635a, fVar.f11636b, fVar.f11637c);
    }

    public j7.j<Boolean> h(c.a<?> aVar, int i8) {
        n6.j.m(aVar, "Listener key cannot be null.");
        return this.f11572j.A(this, aVar, i8);
    }

    public final l6.b<O> i() {
        return this.f11567e;
    }

    protected String j() {
        return this.f11564b;
    }

    public <L> com.google.android.gms.common.api.internal.c<L> k(L l8, String str) {
        return com.google.android.gms.common.api.internal.d.a(l8, this.f11568f, str);
    }

    public final int l() {
        return this.f11569g;
    }

    public final a.f m(Looper looper, r rVar) {
        a.f a9 = ((a.AbstractC0121a) n6.j.l(this.f11565c.a())).a(this.f11563a, looper, d().a(), this.f11566d, rVar, rVar);
        String j8 = j();
        if (j8 != null && (a9 instanceof com.google.android.gms.common.internal.b)) {
            ((com.google.android.gms.common.internal.b) a9).N(j8);
        }
        if (j8 != null && (a9 instanceof l6.g)) {
            ((l6.g) a9).p(j8);
        }
        return a9;
    }

    public final b0 n(Context context, Handler handler) {
        return new b0(context, handler, d().a());
    }
}
