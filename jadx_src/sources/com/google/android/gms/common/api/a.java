package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.common.api.c;
import com.google.android.gms.common.internal.b;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Set;
import l6.h;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a<O extends d> {

    /* renamed from: a  reason: collision with root package name */
    private final AbstractC0121a f11559a;

    /* renamed from: b  reason: collision with root package name */
    private final g f11560b;

    /* renamed from: c  reason: collision with root package name */
    private final String f11561c;

    @VisibleForTesting
    /* renamed from: com.google.android.gms.common.api.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractC0121a<T extends f, O> extends e<T, O> {
        @Deprecated
        public T a(Context context, Looper looper, n6.c cVar, O o5, c.a aVar, c.b bVar) {
            return b(context, looper, cVar, o5, aVar, bVar);
        }

        public T b(Context context, Looper looper, n6.c cVar, O o5, l6.c cVar2, h hVar) {
            throw new UnsupportedOperationException("buildClient must be implemented");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c<C extends b> {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {

        /* renamed from: o  reason: collision with root package name */
        public static final c f11562o = new c(null);

        /* renamed from: com.google.android.gms.common.api.a$d$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface InterfaceC0122a extends d {
            Account n();
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface b extends d {
            GoogleSignInAccount i();
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class c implements d {
            private c() {
            }

            /* synthetic */ c(k6.g gVar) {
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class e<T extends b, O> {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f extends b {
        Set<Scope> a();

        void b(com.google.android.gms.common.internal.e eVar, Set<Scope> set);

        void c(String str);

        void disconnect();

        boolean e();

        String f();

        void g(b.c cVar);

        void h(b.e eVar);

        boolean i();

        boolean isConnected();

        int j();

        Feature[] k();

        String l();

        boolean m();
    }

    @VisibleForTesting
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g<C extends f> extends c<C> {
    }

    public <C extends f> a(String str, AbstractC0121a<C, O> abstractC0121a, g<C> gVar) {
        j.m(abstractC0121a, "Cannot construct an Api with a null ClientBuilder");
        j.m(gVar, "Cannot construct an Api with a null ClientKey");
        this.f11561c = str;
        this.f11559a = abstractC0121a;
        this.f11560b = gVar;
    }

    public final AbstractC0121a a() {
        return this.f11559a;
    }

    public final String b() {
        return this.f11561c;
    }
}
