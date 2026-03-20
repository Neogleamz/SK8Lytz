package androidx.camera.core;

import android.util.Rational;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g3 {

    /* renamed from: a  reason: collision with root package name */
    private int f2392a;

    /* renamed from: b  reason: collision with root package name */
    private Rational f2393b;

    /* renamed from: c  reason: collision with root package name */
    private int f2394c;

    /* renamed from: d  reason: collision with root package name */
    private int f2395d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: b  reason: collision with root package name */
        private final Rational f2397b;

        /* renamed from: c  reason: collision with root package name */
        private final int f2398c;

        /* renamed from: a  reason: collision with root package name */
        private int f2396a = 1;

        /* renamed from: d  reason: collision with root package name */
        private int f2399d = 0;

        public a(Rational rational, int i8) {
            this.f2397b = rational;
            this.f2398c = i8;
        }

        public g3 a() {
            androidx.core.util.h.i(this.f2397b, "The crop aspect ratio must be set.");
            return new g3(this.f2396a, this.f2397b, this.f2398c, this.f2399d);
        }

        public a b(int i8) {
            this.f2399d = i8;
            return this;
        }

        public a c(int i8) {
            this.f2396a = i8;
            return this;
        }
    }

    g3(int i8, Rational rational, int i9, int i10) {
        this.f2392a = i8;
        this.f2393b = rational;
        this.f2394c = i9;
        this.f2395d = i10;
    }

    public Rational a() {
        return this.f2393b;
    }

    public int b() {
        return this.f2395d;
    }

    public int c() {
        return this.f2394c;
    }

    public int d() {
        return this.f2392a;
    }
}
