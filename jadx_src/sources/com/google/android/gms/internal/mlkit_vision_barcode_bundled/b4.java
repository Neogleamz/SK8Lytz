package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b4 implements r4 {

    /* renamed from: a  reason: collision with root package name */
    private final x3 f14731a;

    /* renamed from: b  reason: collision with root package name */
    private final h5 f14732b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f14733c;

    /* renamed from: d  reason: collision with root package name */
    private final c2 f14734d;

    private b4(h5 h5Var, c2 c2Var, x3 x3Var) {
        this.f14732b = h5Var;
        this.f14733c = c2Var.g(x3Var);
        this.f14734d = c2Var;
        this.f14731a = x3Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static b4 j(h5 h5Var, c2 c2Var, x3 x3Var) {
        return new b4(h5Var, c2Var, x3Var);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final int a(Object obj) {
        h5 h5Var = this.f14732b;
        int b9 = h5Var.b(h5Var.d(obj));
        return this.f14733c ? b9 + this.f14734d.b(obj).b() : b9;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final int b(Object obj) {
        int hashCode = this.f14732b.d(obj).hashCode();
        return this.f14733c ? (hashCode * 53) + this.f14734d.b(obj).f14766a.hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final void c(Object obj) {
        this.f14732b.g(obj);
        this.f14734d.e(obj);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final Object d() {
        x3 x3Var = this.f14731a;
        return x3Var instanceof p2 ? ((p2) x3Var).o() : x3Var.e().u();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final void e(Object obj, y5 y5Var) {
        Iterator f5 = this.f14734d.b(obj).f();
        while (f5.hasNext()) {
            Map.Entry entry = (Map.Entry) f5.next();
            f2 f2Var = (f2) entry.getKey();
            if (f2Var.d() != zzhp.MESSAGE) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            }
            f2Var.g();
            f2Var.e();
            y5Var.A(f2Var.zza(), entry instanceof a3 ? ((a3) entry).a().b() : entry.getValue());
        }
        h5 h5Var = this.f14732b;
        h5Var.i(h5Var.d(obj), y5Var);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final boolean f(Object obj) {
        return this.f14734d.b(obj).k();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final void g(Object obj, Object obj2) {
        t4.c(this.f14732b, obj, obj2);
        if (this.f14733c) {
            t4.b(this.f14734d, obj, obj2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00bf A[EDGE_INSN: B:56:0x00bf->B:33:0x00bf ?: BREAK  , SYNTHETIC] */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void h(java.lang.Object r11, byte[] r12, int r13, int r14, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c1 r15) {
        /*
            r10 = this;
            r0 = r11
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2 r0 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2) r0
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.i5 r1 = r0.zzc
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.i5 r2 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.i5.c()
            if (r1 != r2) goto L11
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.i5 r1 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.i5.f()
            r0.zzc = r1
        L11:
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.l2 r11 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.l2) r11
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.g2 r11 = r11.I()
            r0 = 0
            r2 = r0
        L19:
            if (r13 >= r14) goto Lca
            int r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.j(r12, r13, r15)
            int r13 = r15.f14735a
            r3 = 11
            r5 = 2
            if (r13 == r3) goto L65
            r3 = r13 & 7
            if (r3 != r5) goto L60
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2 r2 = r10.f14734d
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.b2 r3 = r15.f14738d
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3 r5 = r10.f14731a
            int r6 = r13 >>> 3
            java.lang.Object r8 = r2.d(r3, r5, r6)
            if (r8 == 0) goto L55
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.g4 r13 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.g4.a()
            r2 = r8
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.o2 r2 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.o2) r2
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3 r3 = r2.f14827c
            java.lang.Class r3 = r3.getClass()
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4 r13 = r13.b(r3)
            int r13 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.d(r13, r12, r4, r14, r15)
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.n2 r2 = r2.f14828d
            java.lang.Object r3 = r15.f14737c
            r11.i(r2, r3)
            goto L5e
        L55:
            r2 = r13
            r3 = r12
            r5 = r14
            r6 = r1
            r7 = r15
            int r13 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.i(r2, r3, r4, r5, r6, r7)
        L5e:
            r2 = r8
            goto L19
        L60:
            int r13 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.p(r13, r12, r4, r14, r15)
            goto L19
        L65:
            r13 = 0
            r3 = r0
        L67:
            if (r4 >= r14) goto Lbf
            int r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.j(r12, r4, r15)
            int r6 = r15.f14735a
            int r7 = r6 >>> 3
            r8 = r6 & 7
            if (r7 == r5) goto La3
            r9 = 3
            if (r7 == r9) goto L79
            goto Lb6
        L79:
            if (r2 == 0) goto L98
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.g4 r6 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.g4.a()
            r7 = r2
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.o2 r7 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.o2) r7
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3 r8 = r7.f14827c
            java.lang.Class r8 = r8.getClass()
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4 r6 = r6.b(r8)
            int r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.d(r6, r12, r4, r14, r15)
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.n2 r6 = r7.f14828d
            java.lang.Object r7 = r15.f14737c
            r11.i(r6, r7)
            goto L67
        L98:
            if (r8 != r5) goto Lb6
            int r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.a(r12, r4, r15)
            java.lang.Object r3 = r15.f14737c
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb r3 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb) r3
            goto L67
        La3:
            if (r8 != 0) goto Lb6
            int r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.j(r12, r4, r15)
            int r13 = r15.f14735a
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2 r2 = r10.f14734d
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.b2 r6 = r15.f14738d
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3 r7 = r10.f14731a
            java.lang.Object r2 = r2.d(r6, r7, r13)
            goto L67
        Lb6:
            r7 = 12
            if (r6 == r7) goto Lbf
            int r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.p(r6, r12, r4, r14, r15)
            goto L67
        Lbf:
            if (r3 == 0) goto Lc7
            int r13 = r13 << 3
            r13 = r13 | r5
            r1.j(r13, r3)
        Lc7:
            r13 = r4
            goto L19
        Lca:
            if (r13 != r14) goto Lcd
            return
        Lcd:
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzeo r11 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzeo.e()
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.b4.h(java.lang.Object, byte[], int, int, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c1):void");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final boolean i(Object obj, Object obj2) {
        if (this.f14732b.d(obj).equals(this.f14732b.d(obj2))) {
            if (this.f14733c) {
                return this.f14734d.b(obj).equals(this.f14734d.b(obj2));
            }
            return true;
        }
        return false;
    }
}
