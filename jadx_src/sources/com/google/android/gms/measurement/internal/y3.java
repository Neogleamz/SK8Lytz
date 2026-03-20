package com.google.android.gms.measurement.internal;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y3 {
    private long A;
    private String B;
    private int C;
    private int D;
    private long E;
    private String F;
    private long G;
    private long H;
    private long I;
    private long J;
    private long K;
    private long L;
    private String M;
    private boolean N;
    private long O;
    private long P;

    /* renamed from: a  reason: collision with root package name */
    private final f6 f17149a;

    /* renamed from: b  reason: collision with root package name */
    private final String f17150b;

    /* renamed from: c  reason: collision with root package name */
    private String f17151c;

    /* renamed from: d  reason: collision with root package name */
    private String f17152d;

    /* renamed from: e  reason: collision with root package name */
    private String f17153e;

    /* renamed from: f  reason: collision with root package name */
    private String f17154f;

    /* renamed from: g  reason: collision with root package name */
    private long f17155g;

    /* renamed from: h  reason: collision with root package name */
    private long f17156h;

    /* renamed from: i  reason: collision with root package name */
    private long f17157i;

    /* renamed from: j  reason: collision with root package name */
    private String f17158j;

    /* renamed from: k  reason: collision with root package name */
    private long f17159k;

    /* renamed from: l  reason: collision with root package name */
    private String f17160l;

    /* renamed from: m  reason: collision with root package name */
    private long f17161m;

    /* renamed from: n  reason: collision with root package name */
    private long f17162n;

    /* renamed from: o  reason: collision with root package name */
    private boolean f17163o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f17164p;
    private String q;

    /* renamed from: r  reason: collision with root package name */
    private Boolean f17165r;

    /* renamed from: s  reason: collision with root package name */
    private long f17166s;

    /* renamed from: t  reason: collision with root package name */
    private List<String> f17167t;

    /* renamed from: u  reason: collision with root package name */
    private String f17168u;

    /* renamed from: v  reason: collision with root package name */
    private boolean f17169v;

    /* renamed from: w  reason: collision with root package name */
    private long f17170w;

    /* renamed from: x  reason: collision with root package name */
    private long f17171x;

    /* renamed from: y  reason: collision with root package name */
    private int f17172y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f17173z;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y3(f6 f6Var, String str) {
        n6.j.l(f6Var);
        n6.j.f(str);
        this.f17149a = f6Var;
        this.f17150b = str;
        f6Var.l().k();
    }

    public final int A() {
        this.f17149a.l().k();
        return this.D;
    }

    public final void A0(long j8) {
        this.f17149a.l().k();
        this.N |= this.f17170w != j8;
        this.f17170w = j8;
    }

    public final void B(int i8) {
        this.f17149a.l().k();
        this.N |= this.D != i8;
        this.D = i8;
    }

    public final long B0() {
        this.f17149a.l().k();
        return this.f17156h;
    }

    public final void C(long j8) {
        this.f17149a.l().k();
        this.N |= this.f17159k != j8;
        this.f17159k = j8;
    }

    public final long C0() {
        this.f17149a.l().k();
        return this.f17171x;
    }

    public final void D(String str) {
        this.f17149a.l().k();
        this.N |= !Objects.equals(this.f17151c, str);
        this.f17151c = str;
    }

    public final long D0() {
        this.f17149a.l().k();
        return this.f17170w;
    }

    public final void E(boolean z4) {
        this.f17149a.l().k();
        this.N |= this.f17163o != z4;
        this.f17163o = z4;
    }

    public final Boolean E0() {
        this.f17149a.l().k();
        return this.f17165r;
    }

    public final int F() {
        this.f17149a.l().k();
        return this.C;
    }

    public final String F0() {
        this.f17149a.l().k();
        return this.q;
    }

    public final void G(int i8) {
        this.f17149a.l().k();
        this.N |= this.C != i8;
        this.C = i8;
    }

    public final String G0() {
        this.f17149a.l().k();
        String str = this.M;
        W(null);
        return str;
    }

    public final void H(long j8) {
        this.f17149a.l().k();
        this.N |= this.A != j8;
        this.A = j8;
    }

    public final void I(String str) {
        this.f17149a.l().k();
        this.N |= !Objects.equals(this.f17160l, str);
        this.f17160l = str;
    }

    public final void J(boolean z4) {
        this.f17149a.l().k();
        this.N |= this.f17169v != z4;
        this.f17169v = z4;
    }

    public final long K() {
        this.f17149a.l().k();
        return 0L;
    }

    public final void L(long j8) {
        this.f17149a.l().k();
        this.N |= this.O != j8;
        this.O = j8;
    }

    public final void M(String str) {
        this.f17149a.l().k();
        this.N |= !Objects.equals(this.f17158j, str);
        this.f17158j = str;
    }

    public final void N(boolean z4) {
        this.f17149a.l().k();
        this.N |= this.f17173z != z4;
        this.f17173z = z4;
    }

    public final long O() {
        this.f17149a.l().k();
        return this.f17159k;
    }

    public final void P(long j8) {
        this.f17149a.l().k();
        this.N |= this.J != j8;
        this.J = j8;
    }

    public final void Q(String str) {
        this.f17149a.l().k();
        this.N |= !Objects.equals(this.f17154f, str);
        this.f17154f = str;
    }

    public final long R() {
        this.f17149a.l().k();
        return this.A;
    }

    public final void S(long j8) {
        this.f17149a.l().k();
        this.N |= this.K != j8;
        this.K = j8;
    }

    public final void T(String str) {
        this.f17149a.l().k();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.N |= !Objects.equals(this.f17152d, str);
        this.f17152d = str;
    }

    public final long U() {
        this.f17149a.l().k();
        return this.O;
    }

    public final void V(long j8) {
        this.f17149a.l().k();
        this.N |= this.I != j8;
        this.I = j8;
    }

    public final void W(String str) {
        this.f17149a.l().k();
        this.N |= !Objects.equals(this.M, str);
        this.M = str;
    }

    public final long X() {
        this.f17149a.l().k();
        return this.J;
    }

    public final void Y(long j8) {
        this.f17149a.l().k();
        this.N |= this.H != j8;
        this.H = j8;
    }

    public final void Z(String str) {
        this.f17149a.l().k();
        this.N |= !Objects.equals(this.f17153e, str);
        this.f17153e = str;
    }

    public final int a() {
        this.f17149a.l().k();
        return this.f17172y;
    }

    public final long a0() {
        this.f17149a.l().k();
        return this.K;
    }

    public final void b(int i8) {
        this.f17149a.l().k();
        this.N |= this.f17172y != i8;
        this.f17172y = i8;
    }

    public final void b0(long j8) {
        this.f17149a.l().k();
        this.N |= this.L != j8;
        this.L = j8;
    }

    public final void c(long j8) {
        this.f17149a.l().k();
        long j9 = this.f17155g + j8;
        if (j9 > 2147483647L) {
            this.f17149a.i().J().b("Bundle index overflow. appId", x4.t(this.f17150b));
            j9 = j8 - 1;
        }
        long j10 = this.E + 1;
        if (j10 > 2147483647L) {
            this.f17149a.i().J().b("Delivery index overflow. appId", x4.t(this.f17150b));
            j10 = 0;
        }
        this.N = true;
        this.f17155g = j9;
        this.E = j10;
    }

    public final void c0(String str) {
        this.f17149a.l().k();
        this.N |= this.F != str;
        this.F = str;
    }

    public final void d(Boolean bool) {
        this.f17149a.l().k();
        this.N |= !Objects.equals(this.f17165r, bool);
        this.f17165r = bool;
    }

    public final long d0() {
        this.f17149a.l().k();
        return this.I;
    }

    public final void e(String str) {
        this.f17149a.l().k();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.N |= !Objects.equals(this.q, str);
        this.q = str;
    }

    public final void e0(long j8) {
        this.f17149a.l().k();
        this.N |= this.G != j8;
        this.G = j8;
    }

    public final void f(List<String> list) {
        this.f17149a.l().k();
        if (Objects.equals(this.f17167t, list)) {
            return;
        }
        this.N = true;
        this.f17167t = list != null ? new ArrayList(list) : null;
    }

    public final void f0(String str) {
        this.f17149a.l().k();
        this.N |= !Objects.equals(this.f17168u, str);
        this.f17168u = str;
    }

    public final void g(boolean z4) {
        this.f17149a.l().k();
        this.N |= this.f17164p != z4;
        this.f17164p = z4;
    }

    public final long g0() {
        this.f17149a.l().k();
        return this.H;
    }

    public final String h() {
        this.f17149a.l().k();
        return this.f17150b;
    }

    public final void h0(long j8) {
        this.f17149a.l().k();
        this.N |= this.f17162n != j8;
        this.f17162n = j8;
    }

    public final String i() {
        this.f17149a.l().k();
        return this.f17151c;
    }

    public final void i0(String str) {
        this.f17149a.l().k();
        this.N |= this.B != str;
        this.B = str;
    }

    public final String j() {
        this.f17149a.l().k();
        return this.f17160l;
    }

    public final long j0() {
        this.f17149a.l().k();
        return this.L;
    }

    public final String k() {
        this.f17149a.l().k();
        return this.f17158j;
    }

    public final void k0(long j8) {
        this.f17149a.l().k();
        this.N |= this.f17166s != j8;
        this.f17166s = j8;
    }

    public final String l() {
        this.f17149a.l().k();
        return this.f17154f;
    }

    public final long l0() {
        this.f17149a.l().k();
        return this.G;
    }

    public final String m() {
        this.f17149a.l().k();
        return this.f17152d;
    }

    public final void m0(long j8) {
        this.f17149a.l().k();
        this.N |= this.P != j8;
        this.P = j8;
    }

    public final String n() {
        this.f17149a.l().k();
        return this.M;
    }

    public final long n0() {
        this.f17149a.l().k();
        return this.f17162n;
    }

    public final String o() {
        this.f17149a.l().k();
        return this.f17153e;
    }

    public final void o0(long j8) {
        this.f17149a.l().k();
        this.N |= this.f17161m != j8;
        this.f17161m = j8;
    }

    public final String p() {
        this.f17149a.l().k();
        return this.F;
    }

    public final long p0() {
        this.f17149a.l().k();
        return this.f17166s;
    }

    public final String q() {
        this.f17149a.l().k();
        return this.f17168u;
    }

    public final void q0(long j8) {
        this.f17149a.l().k();
        this.N |= this.E != j8;
        this.E = j8;
    }

    public final String r() {
        this.f17149a.l().k();
        return this.B;
    }

    public final long r0() {
        this.f17149a.l().k();
        return this.P;
    }

    public final List<String> s() {
        this.f17149a.l().k();
        return this.f17167t;
    }

    public final void s0(long j8) {
        this.f17149a.l().k();
        this.N |= this.f17157i != j8;
        this.f17157i = j8;
    }

    public final void t() {
        this.f17149a.l().k();
        this.N = false;
    }

    public final long t0() {
        this.f17149a.l().k();
        return this.f17161m;
    }

    public final void u() {
        this.f17149a.l().k();
        long j8 = this.f17155g + 1;
        if (j8 > 2147483647L) {
            this.f17149a.i().J().b("Bundle index overflow. appId", x4.t(this.f17150b));
            j8 = 0;
        }
        this.N = true;
        this.f17155g = j8;
    }

    public final void u0(long j8) {
        n6.j.a(j8 >= 0);
        this.f17149a.l().k();
        this.N |= this.f17155g != j8;
        this.f17155g = j8;
    }

    public final boolean v() {
        this.f17149a.l().k();
        return this.f17164p;
    }

    public final long v0() {
        this.f17149a.l().k();
        return this.E;
    }

    public final boolean w() {
        this.f17149a.l().k();
        return this.f17163o;
    }

    public final void w0(long j8) {
        this.f17149a.l().k();
        this.N |= this.f17156h != j8;
        this.f17156h = j8;
    }

    public final boolean x() {
        this.f17149a.l().k();
        return this.N;
    }

    public final long x0() {
        this.f17149a.l().k();
        return this.f17157i;
    }

    public final boolean y() {
        this.f17149a.l().k();
        return this.f17169v;
    }

    public final void y0(long j8) {
        this.f17149a.l().k();
        this.N |= this.f17171x != j8;
        this.f17171x = j8;
    }

    public final boolean z() {
        this.f17149a.l().k();
        return this.f17173z;
    }

    public final long z0() {
        this.f17149a.l().k();
        return this.f17155g;
    }
}
