package com.google.android.exoplayer2;

import android.os.Looper;
import java.util.concurrent.TimeoutException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z1 {

    /* renamed from: a  reason: collision with root package name */
    private final b f11412a;

    /* renamed from: b  reason: collision with root package name */
    private final a f11413b;

    /* renamed from: c  reason: collision with root package name */
    private final b6.d f11414c;

    /* renamed from: d  reason: collision with root package name */
    private final h2 f11415d;

    /* renamed from: e  reason: collision with root package name */
    private int f11416e;

    /* renamed from: f  reason: collision with root package name */
    private Object f11417f;

    /* renamed from: g  reason: collision with root package name */
    private Looper f11418g;

    /* renamed from: h  reason: collision with root package name */
    private int f11419h;

    /* renamed from: i  reason: collision with root package name */
    private long f11420i = -9223372036854775807L;

    /* renamed from: j  reason: collision with root package name */
    private boolean f11421j = true;

    /* renamed from: k  reason: collision with root package name */
    private boolean f11422k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f11423l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f11424m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f11425n;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void c(z1 z1Var);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void x(int i8, Object obj);
    }

    public z1(a aVar, b bVar, h2 h2Var, int i8, b6.d dVar, Looper looper) {
        this.f11413b = aVar;
        this.f11412a = bVar;
        this.f11415d = h2Var;
        this.f11418g = looper;
        this.f11414c = dVar;
        this.f11419h = i8;
    }

    public synchronized boolean a(long j8) {
        boolean z4;
        b6.a.f(this.f11422k);
        b6.a.f(this.f11418g.getThread() != Thread.currentThread());
        long b9 = this.f11414c.b() + j8;
        while (true) {
            z4 = this.f11424m;
            if (z4 || j8 <= 0) {
                break;
            }
            this.f11414c.e();
            wait(j8);
            j8 = b9 - this.f11414c.b();
        }
        if (!z4) {
            throw new TimeoutException("Message delivery timed out.");
        }
        return this.f11423l;
    }

    public boolean b() {
        return this.f11421j;
    }

    public Looper c() {
        return this.f11418g;
    }

    public int d() {
        return this.f11419h;
    }

    public Object e() {
        return this.f11417f;
    }

    public long f() {
        return this.f11420i;
    }

    public b g() {
        return this.f11412a;
    }

    public h2 h() {
        return this.f11415d;
    }

    public int i() {
        return this.f11416e;
    }

    public synchronized boolean j() {
        return this.f11425n;
    }

    public synchronized void k(boolean z4) {
        this.f11423l = z4 | this.f11423l;
        this.f11424m = true;
        notifyAll();
    }

    public z1 l() {
        b6.a.f(!this.f11422k);
        if (this.f11420i == -9223372036854775807L) {
            b6.a.a(this.f11421j);
        }
        this.f11422k = true;
        this.f11413b.c(this);
        return this;
    }

    public z1 m(Object obj) {
        b6.a.f(!this.f11422k);
        this.f11417f = obj;
        return this;
    }

    public z1 n(int i8) {
        b6.a.f(!this.f11422k);
        this.f11416e = i8;
        return this;
    }
}
