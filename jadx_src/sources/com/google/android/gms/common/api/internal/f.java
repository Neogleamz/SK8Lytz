package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.a.b;
import com.google.android.gms.common.api.internal.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f<A extends a.b, L> {

    /* renamed from: a  reason: collision with root package name */
    public final e<A, L> f11635a;

    /* renamed from: b  reason: collision with root package name */
    public final h f11636b;

    /* renamed from: c  reason: collision with root package name */
    public final Runnable f11637c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<A extends a.b, L> {

        /* renamed from: a  reason: collision with root package name */
        private l6.i f11638a;

        /* renamed from: b  reason: collision with root package name */
        private l6.i f11639b;

        /* renamed from: d  reason: collision with root package name */
        private c f11641d;

        /* renamed from: e  reason: collision with root package name */
        private Feature[] f11642e;

        /* renamed from: g  reason: collision with root package name */
        private int f11644g;

        /* renamed from: c  reason: collision with root package name */
        private Runnable f11640c = new Runnable() { // from class: l6.v
            @Override // java.lang.Runnable
            public final void run() {
            }
        };

        /* renamed from: f  reason: collision with root package name */
        private boolean f11643f = true;

        /* synthetic */ a(l6.w wVar) {
        }

        public f<A, L> a() {
            n6.j.b(this.f11638a != null, "Must set register function");
            n6.j.b(this.f11639b != null, "Must set unregister function");
            n6.j.b(this.f11641d != null, "Must set holder");
            return new f<>(new y(this, this.f11641d, this.f11642e, this.f11643f, this.f11644g), new z(this, (c.a) n6.j.m(this.f11641d.b(), "Key must not be null")), this.f11640c, null);
        }

        public a<A, L> b(l6.i<A, j7.k<Void>> iVar) {
            this.f11638a = iVar;
            return this;
        }

        public a<A, L> c(boolean z4) {
            this.f11643f = z4;
            return this;
        }

        public a<A, L> d(Feature... featureArr) {
            this.f11642e = featureArr;
            return this;
        }

        public a<A, L> e(int i8) {
            this.f11644g = i8;
            return this;
        }

        public a<A, L> f(l6.i<A, j7.k<Boolean>> iVar) {
            this.f11639b = iVar;
            return this;
        }

        public a<A, L> g(c<L> cVar) {
            this.f11641d = cVar;
            return this;
        }
    }

    /* synthetic */ f(e eVar, h hVar, Runnable runnable, l6.x xVar) {
        this.f11635a = eVar;
        this.f11636b = hVar;
        this.f11637c = runnable;
    }

    public static <A extends a.b, L> a<A, L> a() {
        return new a<>(null);
    }
}
