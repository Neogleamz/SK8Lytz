package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class na<T> implements xa<T> {

    /* renamed from: a  reason: collision with root package name */
    private final ia f12380a;

    /* renamed from: b  reason: collision with root package name */
    private final tb<?, ?> f12381b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f12382c;

    /* renamed from: d  reason: collision with root package name */
    private final n8<?> f12383d;

    private na(tb<?, ?> tbVar, n8<?> n8Var, ia iaVar) {
        this.f12381b = tbVar;
        this.f12382c = n8Var.e(iaVar);
        this.f12383d = n8Var;
        this.f12380a = iaVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> na<T> i(tb<?, ?> tbVar, n8<?> n8Var, ia iaVar) {
        return new na<>(tbVar, n8Var, iaVar);
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final int a(T t8) {
        tb<?, ?> tbVar = this.f12381b;
        int e8 = tbVar.e(tbVar.k(t8)) + 0;
        return this.f12382c ? e8 + this.f12383d.b(t8).a() : e8;
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final int b(T t8) {
        int hashCode = this.f12381b.k(t8).hashCode();
        return this.f12382c ? (hashCode * 53) + this.f12383d.b(t8).hashCode() : hashCode;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0099 A[EDGE_INSN: B:57:0x0099->B:34:0x0099 ?: BREAK  , SYNTHETIC] */
    @Override // com.google.android.gms.internal.measurement.xa
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void c(T r10, byte[] r11, int r12, int r13, com.google.android.gms.internal.measurement.m7 r14) {
        /*
            r9 = this;
            r0 = r10
            com.google.android.gms.internal.measurement.x8 r0 = (com.google.android.gms.internal.measurement.x8) r0
            com.google.android.gms.internal.measurement.vb r1 = r0.zzb
            com.google.android.gms.internal.measurement.vb r2 = com.google.android.gms.internal.measurement.vb.k()
            if (r1 != r2) goto L11
            com.google.android.gms.internal.measurement.vb r1 = com.google.android.gms.internal.measurement.vb.l()
            r0.zzb = r1
        L11:
            com.google.android.gms.internal.measurement.x8$b r10 = (com.google.android.gms.internal.measurement.x8.b) r10
            r10.H()
            r10 = 0
            r0 = r10
        L18:
            if (r12 >= r13) goto La4
            int r4 = com.google.android.gms.internal.measurement.n7.p(r11, r12, r14)
            int r2 = r14.f12344a
            r12 = 11
            r3 = 2
            if (r2 == r12) goto L51
            r12 = r2 & 7
            if (r12 != r3) goto L4c
            com.google.android.gms.internal.measurement.n8<?> r12 = r9.f12383d
            com.google.android.gms.internal.measurement.l8 r0 = r14.f12347d
            com.google.android.gms.internal.measurement.ia r3 = r9.f12380a
            int r5 = r2 >>> 3
            java.lang.Object r12 = r12.c(r0, r3, r5)
            r0 = r12
            com.google.android.gms.internal.measurement.x8$d r0 = (com.google.android.gms.internal.measurement.x8.d) r0
            if (r0 != 0) goto L43
            r3 = r11
            r5 = r13
            r6 = r1
            r7 = r14
            int r12 = com.google.android.gms.internal.measurement.n7.d(r2, r3, r4, r5, r6, r7)
            goto L18
        L43:
            com.google.android.gms.internal.measurement.ua.a()
            java.lang.NoSuchMethodError r10 = new java.lang.NoSuchMethodError
            r10.<init>()
            throw r10
        L4c:
            int r12 = com.google.android.gms.internal.measurement.n7.b(r2, r11, r4, r13, r14)
            goto L18
        L51:
            r12 = 0
            r2 = r10
        L53:
            if (r4 >= r13) goto L99
            int r4 = com.google.android.gms.internal.measurement.n7.p(r11, r4, r14)
            int r5 = r14.f12344a
            int r6 = r5 >>> 3
            r7 = r5 & 7
            if (r6 == r3) goto L7b
            r8 = 3
            if (r6 == r8) goto L65
            goto L90
        L65:
            if (r0 != 0) goto L72
            if (r7 != r3) goto L90
            int r4 = com.google.android.gms.internal.measurement.n7.k(r11, r4, r14)
            java.lang.Object r2 = r14.f12346c
            com.google.android.gms.internal.measurement.zzij r2 = (com.google.android.gms.internal.measurement.zzij) r2
            goto L53
        L72:
            com.google.android.gms.internal.measurement.ua.a()
            java.lang.NoSuchMethodError r10 = new java.lang.NoSuchMethodError
            r10.<init>()
            throw r10
        L7b:
            if (r7 != 0) goto L90
            int r4 = com.google.android.gms.internal.measurement.n7.p(r11, r4, r14)
            int r12 = r14.f12344a
            com.google.android.gms.internal.measurement.n8<?> r0 = r9.f12383d
            com.google.android.gms.internal.measurement.l8 r5 = r14.f12347d
            com.google.android.gms.internal.measurement.ia r6 = r9.f12380a
            java.lang.Object r0 = r0.c(r5, r6, r12)
            com.google.android.gms.internal.measurement.x8$d r0 = (com.google.android.gms.internal.measurement.x8.d) r0
            goto L53
        L90:
            r6 = 12
            if (r5 == r6) goto L99
            int r4 = com.google.android.gms.internal.measurement.n7.b(r5, r11, r4, r13, r14)
            goto L53
        L99:
            if (r2 == 0) goto La1
            int r12 = r12 << 3
            r12 = r12 | r3
            r1.e(r12, r2)
        La1:
            r12 = r4
            goto L18
        La4:
            if (r12 != r13) goto La7
            return
        La7:
            com.google.android.gms.internal.measurement.zzkb r10 = com.google.android.gms.internal.measurement.zzkb.e()
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.na.c(java.lang.Object, byte[], int, int, com.google.android.gms.internal.measurement.m7):void");
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final void d(T t8, rc rcVar) {
        Iterator<Map.Entry<?, Object>> p8 = this.f12383d.b(t8).p();
        while (p8.hasNext()) {
            Map.Entry<?, Object> next = p8.next();
            q8 q8Var = (q8) next.getKey();
            if (q8Var.a() != zznq.MESSAGE || q8Var.d() || q8Var.b()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            }
            rcVar.r(q8Var.zza(), next instanceof h9 ? ((h9) next).a().d() : next.getValue());
        }
        tb<?, ?> tbVar = this.f12381b;
        tbVar.d(tbVar.k(t8), rcVar);
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final boolean e(T t8) {
        return this.f12383d.b(t8).s();
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final void f(T t8) {
        this.f12381b.l(t8);
        this.f12383d.g(t8);
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final void g(T t8, T t9) {
        ab.n(this.f12381b, t8, t9);
        if (this.f12382c) {
            ab.l(this.f12383d, t8, t9);
        }
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final boolean h(T t8, T t9) {
        if (this.f12381b.k(t8).equals(this.f12381b.k(t9))) {
            if (this.f12382c) {
                return this.f12383d.b(t8).equals(this.f12383d.b(t9));
            }
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final T zza() {
        ia iaVar = this.f12380a;
        return iaVar instanceof x8 ? (T) ((x8) iaVar).A() : (T) iaVar.e().t();
    }
}
