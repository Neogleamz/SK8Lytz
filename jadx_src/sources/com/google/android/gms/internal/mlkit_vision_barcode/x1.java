package com.google.android.gms.internal.mlkit_vision_barcode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x1 extends zzcx {

    /* renamed from: f  reason: collision with root package name */
    static final zzcx f14196f = new x1(null, new Object[0], 0);

    /* renamed from: d  reason: collision with root package name */
    final transient Object[] f14197d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f14198e;

    private x1(Object obj, Object[] objArr, int i8) {
        this.f14197d = objArr;
        this.f14198e = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static x1 i(int i8, Object[] objArr, g1 g1Var) {
        Object obj = objArr[0];
        obj.getClass();
        Object obj2 = objArr[1];
        obj2.getClass();
        n0.b(obj, obj2);
        return new x1(null, objArr, 1);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcx
    final zzcq a() {
        return new w1(this.f14197d, 1, this.f14198e);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcx
    final zzcy d() {
        return new u1(this, this.f14197d, 0, this.f14198e);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcx
    final zzcy f() {
        return new v1(this, new w1(this.f14197d, 0, this.f14198e));
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0020 A[RETURN] */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcx, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object get(java.lang.Object r5) {
        /*
            r4 = this;
            java.lang.Object[] r0 = r4.f14197d
            int r1 = r4.f14198e
            r2 = 0
            if (r5 != 0) goto L9
        L7:
            r5 = r2
            goto L1d
        L9:
            r3 = 1
            if (r1 != r3) goto L7
            r1 = 0
            r1 = r0[r1]
            r1.getClass()
            boolean r5 = r1.equals(r5)
            if (r5 == 0) goto L7
            r5 = r0[r3]
            r5.getClass()
        L1d:
            if (r5 != 0) goto L20
            return r2
        L20:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode.x1.get(java.lang.Object):java.lang.Object");
    }

    @Override // java.util.Map
    public final int size() {
        return this.f14198e;
    }
}
