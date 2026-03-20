package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h3 extends m3 {

    /* renamed from: c  reason: collision with root package name */
    private static final Class f14776c = Collections.unmodifiableList(Collections.emptyList()).getClass();

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ h3(g3 g3Var) {
        super(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m3
    public final void a(Object obj, long j8) {
        Object unmodifiableList;
        List list = (List) s5.k(obj, j8);
        if (list instanceof f3) {
            unmodifiableList = ((f3) list).d();
        } else if (f14776c.isAssignableFrom(list.getClass())) {
            return;
        } else {
            if ((list instanceof f4) && (list instanceof x2)) {
                x2 x2Var = (x2) list;
                if (x2Var.a()) {
                    x2Var.zzb();
                    return;
                }
                return;
            }
            unmodifiableList = Collections.unmodifiableList(list);
        }
        s5.x(obj, j8, unmodifiableList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0094 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009c  */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m3
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void b(java.lang.Object r5, java.lang.Object r6, long r7) {
        /*
            r4 = this;
            java.lang.Object r6 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.s5.k(r6, r7)
            java.util.List r6 = (java.util.List) r6
            int r0 = r6.size()
            java.lang.Object r1 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.s5.k(r5, r7)
            java.util.List r1 = (java.util.List) r1
            boolean r2 = r1.isEmpty()
            if (r2 == 0) goto L39
            boolean r2 = r1 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.f3
            if (r2 == 0) goto L20
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.e3 r1 = new com.google.android.gms.internal.mlkit_vision_barcode_bundled.e3
            r1.<init>(r0)
            goto L35
        L20:
            boolean r2 = r1 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.f4
            if (r2 == 0) goto L30
            boolean r2 = r1 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
            if (r2 == 0) goto L30
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2 r1 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2) r1
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2 r0 = r1.z(r0)
            r1 = r0
            goto L35
        L30:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>(r0)
        L35:
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.s5.x(r5, r7, r1)
            goto L8a
        L39:
            java.lang.Class r2 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.h3.f14776c
            java.lang.Class r3 = r1.getClass()
            boolean r2 = r2.isAssignableFrom(r3)
            if (r2 == 0) goto L57
            java.util.ArrayList r2 = new java.util.ArrayList
            int r3 = r1.size()
            int r3 = r3 + r0
            r2.<init>(r3)
            r2.addAll(r1)
        L52:
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.s5.x(r5, r7, r2)
            r1 = r2
            goto L8a
        L57:
            boolean r2 = r1 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.n5
            if (r2 == 0) goto L6f
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.e3 r2 = new com.google.android.gms.internal.mlkit_vision_barcode_bundled.e3
            int r3 = r1.size()
            int r3 = r3 + r0
            r2.<init>(r3)
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.n5 r1 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.n5) r1
            int r0 = r2.size()
            r2.addAll(r0, r1)
            goto L52
        L6f:
            boolean r2 = r1 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.f4
            if (r2 == 0) goto L8a
            boolean r2 = r1 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
            if (r2 == 0) goto L8a
            r2 = r1
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2 r2 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2) r2
            boolean r3 = r2.a()
            if (r3 != 0) goto L8a
            int r1 = r1.size()
            int r1 = r1 + r0
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2 r1 = r2.z(r1)
            goto L35
        L8a:
            int r0 = r1.size()
            int r2 = r6.size()
            if (r0 <= 0) goto L99
            if (r2 <= 0) goto L99
            r1.addAll(r6)
        L99:
            if (r0 > 0) goto L9c
            goto L9d
        L9c:
            r6 = r1
        L9d:
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.s5.x(r5, r7, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.h3.b(java.lang.Object, java.lang.Object, long):void");
    }
}
