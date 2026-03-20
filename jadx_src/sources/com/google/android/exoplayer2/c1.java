package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c1 {

    /* renamed from: a  reason: collision with root package name */
    public final k.b f9480a;

    /* renamed from: b  reason: collision with root package name */
    public final long f9481b;

    /* renamed from: c  reason: collision with root package name */
    public final long f9482c;

    /* renamed from: d  reason: collision with root package name */
    public final long f9483d;

    /* renamed from: e  reason: collision with root package name */
    public final long f9484e;

    /* renamed from: f  reason: collision with root package name */
    public final boolean f9485f;

    /* renamed from: g  reason: collision with root package name */
    public final boolean f9486g;

    /* renamed from: h  reason: collision with root package name */
    public final boolean f9487h;

    /* renamed from: i  reason: collision with root package name */
    public final boolean f9488i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c1(k.b bVar, long j8, long j9, long j10, long j11, boolean z4, boolean z8, boolean z9, boolean z10) {
        boolean z11 = false;
        b6.a.a(!z10 || z8);
        b6.a.a(!z9 || z8);
        if (!z4 || (!z8 && !z9 && !z10)) {
            z11 = true;
        }
        b6.a.a(z11);
        this.f9480a = bVar;
        this.f9481b = j8;
        this.f9482c = j9;
        this.f9483d = j10;
        this.f9484e = j11;
        this.f9485f = z4;
        this.f9486g = z8;
        this.f9487h = z9;
        this.f9488i = z10;
    }

    public c1 a(long j8) {
        return j8 == this.f9482c ? this : new c1(this.f9480a, this.f9481b, j8, this.f9483d, this.f9484e, this.f9485f, this.f9486g, this.f9487h, this.f9488i);
    }

    public c1 b(long j8) {
        return j8 == this.f9481b ? this : new c1(this.f9480a, j8, this.f9482c, this.f9483d, this.f9484e, this.f9485f, this.f9486g, this.f9487h, this.f9488i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || c1.class != obj.getClass()) {
            return false;
        }
        c1 c1Var = (c1) obj;
        return this.f9481b == c1Var.f9481b && this.f9482c == c1Var.f9482c && this.f9483d == c1Var.f9483d && this.f9484e == c1Var.f9484e && this.f9485f == c1Var.f9485f && this.f9486g == c1Var.f9486g && this.f9487h == c1Var.f9487h && this.f9488i == c1Var.f9488i && b6.l0.c(this.f9480a, c1Var.f9480a);
    }

    public int hashCode() {
        return ((((((((((((((((527 + this.f9480a.hashCode()) * 31) + ((int) this.f9481b)) * 31) + ((int) this.f9482c)) * 31) + ((int) this.f9483d)) * 31) + ((int) this.f9484e)) * 31) + (this.f9485f ? 1 : 0)) * 31) + (this.f9486g ? 1 : 0)) * 31) + (this.f9487h ? 1 : 0)) * 31) + (this.f9488i ? 1 : 0);
    }
}
