package com.google.common.collect;

import java.io.Serializable;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t0<T> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    private final Comparator<? super T> f19445a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f19446b;

    /* renamed from: c  reason: collision with root package name */
    private final T f19447c;

    /* renamed from: d  reason: collision with root package name */
    private final BoundType f19448d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f19449e;

    /* renamed from: f  reason: collision with root package name */
    private final T f19450f;

    /* renamed from: g  reason: collision with root package name */
    private final BoundType f19451g;

    private t0(Comparator<? super T> comparator, boolean z4, T t8, BoundType boundType, boolean z8, T t9, BoundType boundType2) {
        this.f19445a = (Comparator) com.google.common.base.l.n(comparator);
        this.f19446b = z4;
        this.f19449e = z8;
        this.f19447c = t8;
        this.f19448d = (BoundType) com.google.common.base.l.n(boundType);
        this.f19450f = t9;
        this.f19451g = (BoundType) com.google.common.base.l.n(boundType2);
        if (z4) {
            comparator.compare((Object) u1.a(t8), (Object) u1.a(t8));
        }
        if (z8) {
            comparator.compare((Object) u1.a(t9), (Object) u1.a(t9));
        }
        if (z4 && z8) {
            int compare = comparator.compare((Object) u1.a(t8), (Object) u1.a(t9));
            boolean z9 = true;
            com.google.common.base.l.j(compare <= 0, "lowerEndpoint (%s) > upperEndpoint (%s)", t8, t9);
            if (compare == 0) {
                BoundType boundType3 = BoundType.OPEN;
                if (boundType == boundType3 && boundType2 == boundType3) {
                    z9 = false;
                }
                com.google.common.base.l.d(z9);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> t0<T> a(Comparator<? super T> comparator) {
        BoundType boundType = BoundType.OPEN;
        return new t0<>(comparator, false, null, boundType, false, null, boundType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> t0<T> d(Comparator<? super T> comparator, T t8, BoundType boundType) {
        return new t0<>(comparator, true, t8, boundType, false, null, BoundType.OPEN);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> t0<T> n(Comparator<? super T> comparator, T t8, BoundType boundType) {
        return new t0<>(comparator, false, null, BoundType.OPEN, true, t8, boundType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Comparator<? super T> b() {
        return this.f19445a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c(T t8) {
        return (m(t8) || l(t8)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BoundType e() {
        return this.f19448d;
    }

    public boolean equals(Object obj) {
        if (obj instanceof t0) {
            t0 t0Var = (t0) obj;
            return this.f19445a.equals(t0Var.f19445a) && this.f19446b == t0Var.f19446b && this.f19449e == t0Var.f19449e && e().equals(t0Var.e()) && g().equals(t0Var.g()) && com.google.common.base.k.a(f(), t0Var.f()) && com.google.common.base.k.a(h(), t0Var.h());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T f() {
        return this.f19447c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BoundType g() {
        return this.f19451g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T h() {
        return this.f19450f;
    }

    public int hashCode() {
        return com.google.common.base.k.b(this.f19445a, f(), e(), h(), g());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean i() {
        return this.f19446b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean j() {
        return this.f19449e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0047, code lost:
        if (r12.e() == com.google.common.collect.BoundType.OPEN) goto L4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0084, code lost:
        if (r12.g() == com.google.common.collect.BoundType.OPEN) goto L8;
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x008b A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.google.common.collect.t0<T> k(com.google.common.collect.t0<T> r12) {
        /*
            r11 = this;
            com.google.common.base.l.n(r12)
            java.util.Comparator<? super T> r0 = r11.f19445a
            java.util.Comparator<? super T> r1 = r12.f19445a
            boolean r0 = r0.equals(r1)
            com.google.common.base.l.d(r0)
            boolean r0 = r11.f19446b
            java.lang.Object r1 = r11.f()
            com.google.common.collect.BoundType r2 = r11.e()
            boolean r3 = r11.i()
            if (r3 != 0) goto L29
            boolean r0 = r12.f19446b
        L20:
            java.lang.Object r1 = r12.f()
            com.google.common.collect.BoundType r2 = r12.e()
            goto L4a
        L29:
            boolean r3 = r12.i()
            if (r3 == 0) goto L4a
            java.util.Comparator<? super T> r3 = r11.f19445a
            java.lang.Object r4 = r11.f()
            java.lang.Object r5 = r12.f()
            int r3 = r3.compare(r4, r5)
            if (r3 < 0) goto L20
            if (r3 != 0) goto L4a
            com.google.common.collect.BoundType r3 = r12.e()
            com.google.common.collect.BoundType r4 = com.google.common.collect.BoundType.OPEN
            if (r3 != r4) goto L4a
            goto L20
        L4a:
            r5 = r0
            boolean r0 = r11.f19449e
            java.lang.Object r3 = r11.h()
            com.google.common.collect.BoundType r4 = r11.g()
            boolean r6 = r11.j()
            if (r6 != 0) goto L66
            boolean r0 = r12.f19449e
        L5d:
            java.lang.Object r3 = r12.h()
            com.google.common.collect.BoundType r4 = r12.g()
            goto L87
        L66:
            boolean r6 = r12.j()
            if (r6 == 0) goto L87
            java.util.Comparator<? super T> r6 = r11.f19445a
            java.lang.Object r7 = r11.h()
            java.lang.Object r8 = r12.h()
            int r6 = r6.compare(r7, r8)
            if (r6 > 0) goto L5d
            if (r6 != 0) goto L87
            com.google.common.collect.BoundType r6 = r12.g()
            com.google.common.collect.BoundType r7 = com.google.common.collect.BoundType.OPEN
            if (r6 != r7) goto L87
            goto L5d
        L87:
            r8 = r0
            r9 = r3
            if (r5 == 0) goto La5
            if (r8 == 0) goto La5
            java.util.Comparator<? super T> r12 = r11.f19445a
            int r12 = r12.compare(r1, r9)
            if (r12 > 0) goto L9d
            if (r12 != 0) goto La5
            com.google.common.collect.BoundType r12 = com.google.common.collect.BoundType.OPEN
            if (r2 != r12) goto La5
            if (r4 != r12) goto La5
        L9d:
            com.google.common.collect.BoundType r12 = com.google.common.collect.BoundType.OPEN
            com.google.common.collect.BoundType r0 = com.google.common.collect.BoundType.CLOSED
            r7 = r12
            r10 = r0
            r6 = r9
            goto La8
        La5:
            r6 = r1
            r7 = r2
            r10 = r4
        La8:
            com.google.common.collect.t0 r12 = new com.google.common.collect.t0
            java.util.Comparator<? super T> r4 = r11.f19445a
            r3 = r12
            r3.<init>(r4, r5, r6, r7, r8, r9, r10)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.t0.k(com.google.common.collect.t0):com.google.common.collect.t0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l(T t8) {
        if (j()) {
            int compare = this.f19445a.compare(t8, u1.a(h()));
            return ((compare == 0) & (g() == BoundType.OPEN)) | (compare > 0);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean m(T t8) {
        if (i()) {
            int compare = this.f19445a.compare(t8, u1.a(f()));
            return ((compare == 0) & (e() == BoundType.OPEN)) | (compare < 0);
        }
        return false;
    }

    public String toString() {
        String valueOf = String.valueOf(this.f19445a);
        BoundType boundType = this.f19448d;
        BoundType boundType2 = BoundType.CLOSED;
        char c9 = boundType == boundType2 ? '[' : '(';
        String valueOf2 = String.valueOf(this.f19446b ? this.f19447c : "-∞");
        String valueOf3 = String.valueOf(this.f19449e ? this.f19450f : "∞");
        char c10 = this.f19451g == boundType2 ? ']' : ')';
        StringBuilder sb = new StringBuilder(valueOf.length() + 4 + valueOf2.length() + valueOf3.length());
        sb.append(valueOf);
        sb.append(":");
        sb.append(c9);
        sb.append(valueOf2);
        sb.append(',');
        sb.append(valueOf3);
        sb.append(c10);
        return sb.toString();
    }
}
