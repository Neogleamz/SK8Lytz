package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.i;
import com.google.common.collect.l1;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k1 {

    /* renamed from: a  reason: collision with root package name */
    boolean f19331a;

    /* renamed from: b  reason: collision with root package name */
    int f19332b = -1;

    /* renamed from: c  reason: collision with root package name */
    int f19333c = -1;

    /* renamed from: d  reason: collision with root package name */
    l1.p f19334d;

    /* renamed from: e  reason: collision with root package name */
    l1.p f19335e;

    /* renamed from: f  reason: collision with root package name */
    Equivalence<Object> f19336f;

    public k1 a(int i8) {
        int i9 = this.f19333c;
        com.google.common.base.l.u(i9 == -1, "concurrency level was already set to %s", i9);
        com.google.common.base.l.d(i8 > 0);
        this.f19333c = i8;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        int i8 = this.f19333c;
        if (i8 == -1) {
            return 4;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c() {
        int i8 = this.f19332b;
        if (i8 == -1) {
            return 16;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Equivalence<Object> d() {
        return (Equivalence) com.google.common.base.i.a(this.f19336f, e().f());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public l1.p e() {
        return (l1.p) com.google.common.base.i.a(this.f19334d, l1.p.f19380a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public l1.p f() {
        return (l1.p) com.google.common.base.i.a(this.f19335e, l1.p.f19380a);
    }

    public k1 g(int i8) {
        int i9 = this.f19332b;
        com.google.common.base.l.u(i9 == -1, "initial capacity was already set to %s", i9);
        com.google.common.base.l.d(i8 >= 0);
        this.f19332b = i8;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k1 h(Equivalence<Object> equivalence) {
        Equivalence<Object> equivalence2 = this.f19336f;
        com.google.common.base.l.v(equivalence2 == null, "key equivalence was already set to %s", equivalence2);
        this.f19336f = (Equivalence) com.google.common.base.l.n(equivalence);
        this.f19331a = true;
        return this;
    }

    public <K, V> ConcurrentMap<K, V> i() {
        return !this.f19331a ? new ConcurrentHashMap(c(), 0.75f, b()) : l1.b(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k1 j(l1.p pVar) {
        l1.p pVar2 = this.f19334d;
        com.google.common.base.l.v(pVar2 == null, "Key strength was already set to %s", pVar2);
        this.f19334d = (l1.p) com.google.common.base.l.n(pVar);
        if (pVar != l1.p.f19380a) {
            this.f19331a = true;
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k1 k(l1.p pVar) {
        l1.p pVar2 = this.f19335e;
        com.google.common.base.l.v(pVar2 == null, "Value strength was already set to %s", pVar2);
        this.f19335e = (l1.p) com.google.common.base.l.n(pVar);
        if (pVar != l1.p.f19380a) {
            this.f19331a = true;
        }
        return this;
    }

    public k1 l() {
        return j(l1.p.f19381b);
    }

    public String toString() {
        i.b b9 = com.google.common.base.i.b(this);
        int i8 = this.f19332b;
        if (i8 != -1) {
            b9.b("initialCapacity", i8);
        }
        int i9 = this.f19333c;
        if (i9 != -1) {
            b9.b("concurrencyLevel", i9);
        }
        l1.p pVar = this.f19334d;
        if (pVar != null) {
            b9.d("keyStrength", com.google.common.base.c.e(pVar.toString()));
        }
        l1.p pVar2 = this.f19335e;
        if (pVar2 != null) {
            b9.d("valueStrength", com.google.common.base.c.e(pVar2.toString()));
        }
        if (this.f19336f != null) {
            b9.j("keyEquivalence");
        }
        return b9.toString();
    }
}
