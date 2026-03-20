package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.a.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class g<A extends a.b, ResultT> {

    /* renamed from: a  reason: collision with root package name */
    private final Feature[] f11646a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f11647b;

    /* renamed from: c  reason: collision with root package name */
    private final int f11648c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<A extends a.b, ResultT> {

        /* renamed from: a  reason: collision with root package name */
        private l6.i f11649a;

        /* renamed from: c  reason: collision with root package name */
        private Feature[] f11651c;

        /* renamed from: b  reason: collision with root package name */
        private boolean f11650b = true;

        /* renamed from: d  reason: collision with root package name */
        private int f11652d = 0;

        /* synthetic */ a(l6.c0 c0Var) {
        }

        public g<A, ResultT> a() {
            n6.j.b(this.f11649a != null, "execute parameter required");
            return new a0(this, this.f11651c, this.f11650b, this.f11652d);
        }

        public a<A, ResultT> b(l6.i<A, j7.k<ResultT>> iVar) {
            this.f11649a = iVar;
            return this;
        }

        public a<A, ResultT> c(boolean z4) {
            this.f11650b = z4;
            return this;
        }

        public a<A, ResultT> d(Feature... featureArr) {
            this.f11651c = featureArr;
            return this;
        }

        public a<A, ResultT> e(int i8) {
            this.f11652d = i8;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g(Feature[] featureArr, boolean z4, int i8) {
        this.f11646a = featureArr;
        boolean z8 = false;
        if (featureArr != null && z4) {
            z8 = true;
        }
        this.f11647b = z8;
        this.f11648c = i8;
    }

    public static <A extends a.b, ResultT> a<A, ResultT> a() {
        return new a<>(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void b(A a9, j7.k<ResultT> kVar);

    public boolean c() {
        return this.f11647b;
    }

    public final int d() {
        return this.f11648c;
    }

    public final Feature[] e() {
        return this.f11646a;
    }
}
