package com.google.android.gms.common.api.internal;

import android.os.Looper;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c<L> {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f11622a;

    /* renamed from: b  reason: collision with root package name */
    private volatile Object f11623b;

    /* renamed from: c  reason: collision with root package name */
    private volatile a f11624c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<L> {

        /* renamed from: a  reason: collision with root package name */
        private final Object f11625a;

        /* renamed from: b  reason: collision with root package name */
        private final String f11626b;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(L l8, String str) {
            this.f11625a = l8;
            this.f11626b = str;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof a) {
                a aVar = (a) obj;
                return this.f11625a == aVar.f11625a && this.f11626b.equals(aVar.f11626b);
            }
            return false;
        }

        public int hashCode() {
            return (System.identityHashCode(this.f11625a) * 31) + this.f11626b.hashCode();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b<L> {
        void a(L l8);

        void b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(Looper looper, L l8, String str) {
        this.f11622a = new v6.a(looper);
        this.f11623b = n6.j.m(l8, "Listener must not be null");
        this.f11624c = new a(l8, n6.j.f(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(Executor executor, L l8, String str) {
        this.f11622a = (Executor) n6.j.m(executor, "Executor must not be null");
        this.f11623b = n6.j.m(l8, "Listener must not be null");
        this.f11624c = new a(l8, n6.j.f(str));
    }

    public void a() {
        this.f11623b = null;
        this.f11624c = null;
    }

    public a<L> b() {
        return this.f11624c;
    }

    public void c(final b<? super L> bVar) {
        n6.j.m(bVar, "Notifier must not be null");
        this.f11622a.execute(new Runnable() { // from class: com.google.android.gms.common.api.internal.v
            @Override // java.lang.Runnable
            public final void run() {
                c.this.d(bVar);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public final void d(b bVar) {
        Object obj = this.f11623b;
        if (obj == null) {
            bVar.b();
            return;
        }
        try {
            bVar.a(obj);
        } catch (RuntimeException e8) {
            bVar.b();
            throw e8;
        }
    }
}
