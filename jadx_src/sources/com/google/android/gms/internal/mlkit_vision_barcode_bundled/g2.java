package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g2 {

    /* renamed from: d  reason: collision with root package name */
    private static final g2 f14765d = new g2(true);

    /* renamed from: a  reason: collision with root package name */
    final e5 f14766a = new u4(16);

    /* renamed from: b  reason: collision with root package name */
    private boolean f14767b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f14768c;

    private g2() {
    }

    private g2(boolean z4) {
        g();
        g();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int a(f2 f2Var, Object obj) {
        long longValue;
        int intValue;
        int a9;
        int intValue2;
        zzho b9 = f2Var.b();
        int zza = f2Var.zza();
        f2Var.g();
        int i8 = w1.f14874d;
        int A = w1.A(zza << 3);
        if (b9 == zzho.f15044l) {
            x3 x3Var = (x3) obj;
            byte[] bArr = y2.f14888d;
            if (x3Var instanceof z0) {
                z0 z0Var = (z0) x3Var;
                throw null;
            }
            A += A;
        }
        zzhp zzhpVar = zzhp.INT;
        int i9 = 4;
        switch (b9.ordinal()) {
            case 0:
                ((Double) obj).doubleValue();
                i9 = 8;
                break;
            case 1:
                ((Float) obj).floatValue();
                break;
            case 2:
            case 3:
                longValue = ((Long) obj).longValue();
                i9 = w1.B(longValue);
                break;
            case 4:
                intValue = ((Integer) obj).intValue();
                i9 = w1.w(intValue);
                break;
            case 5:
            case 15:
                ((Long) obj).longValue();
                i9 = 8;
                break;
            case 6:
            case 14:
                ((Integer) obj).intValue();
                break;
            case 7:
                ((Boolean) obj).booleanValue();
                i9 = 1;
                break;
            case 8:
                if (!(obj instanceof zzdb)) {
                    i9 = w1.z((String) obj);
                    break;
                }
                a9 = ((zzdb) obj).i();
                i9 = w1.A(a9) + a9;
                break;
            case 9:
                i9 = ((x3) obj).b();
                break;
            case 10:
                if (!(obj instanceof c3)) {
                    i9 = w1.x((x3) obj);
                    break;
                } else {
                    a9 = ((c3) obj).a();
                    i9 = w1.A(a9) + a9;
                    break;
                }
            case 11:
                if (!(obj instanceof zzdb)) {
                    a9 = ((byte[]) obj).length;
                    i9 = w1.A(a9) + a9;
                    break;
                }
                a9 = ((zzdb) obj).i();
                i9 = w1.A(a9) + a9;
            case 12:
                intValue2 = ((Integer) obj).intValue();
                i9 = w1.A(intValue2);
                break;
            case 13:
                if (obj instanceof r2) {
                    intValue = ((r2) obj).zza();
                    i9 = w1.w(intValue);
                    break;
                }
                intValue = ((Integer) obj).intValue();
                i9 = w1.w(intValue);
            case 16:
                int intValue3 = ((Integer) obj).intValue();
                intValue2 = (intValue3 >> 31) ^ (intValue3 + intValue3);
                i9 = w1.A(intValue2);
                break;
            case 17:
                long longValue2 = ((Long) obj).longValue();
                longValue = (longValue2 >> 63) ^ (longValue2 + longValue2);
                i9 = w1.B(longValue);
                break;
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
        return A + i9;
    }

    public static g2 d() {
        return f14765d;
    }

    private static Object l(Object obj) {
        if (obj instanceof c4) {
            return ((c4) obj).a();
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            int length = bArr.length;
            byte[] bArr2 = new byte[length];
            System.arraycopy(bArr, 0, bArr2, 0, length);
            return bArr2;
        }
        return obj;
    }

    private final void m(Map.Entry entry) {
        AbstractMap abstractMap;
        Object l8;
        Object e8;
        f2 f2Var = (f2) entry.getKey();
        Object value = entry.getValue();
        if (value instanceof c3) {
            c3 c3Var = (c3) value;
            throw null;
        }
        f2Var.g();
        if (f2Var.d() != zzhp.MESSAGE || (e8 = e(f2Var)) == null) {
            abstractMap = this.f14766a;
            l8 = l(value);
        } else {
            if (e8 instanceof c4) {
                l8 = f2Var.m((c4) e8, (c4) value);
            } else {
                w3 f5 = ((x3) e8).f();
                f2Var.p(f5, (x3) value);
                l8 = f5.i();
            }
            abstractMap = this.f14766a;
        }
        abstractMap.put(f2Var, l8);
    }

    private static boolean n(Map.Entry entry) {
        f2 f2Var = (f2) entry.getKey();
        if (f2Var.d() == zzhp.MESSAGE) {
            f2Var.g();
            Object value = entry.getValue();
            if (value instanceof y3) {
                return ((y3) value).m();
            }
            if (value instanceof c3) {
                return true;
            }
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
        return true;
    }

    private static final int o(Map.Entry entry) {
        f2 f2Var = (f2) entry.getKey();
        Object value = entry.getValue();
        if (f2Var.d() == zzhp.MESSAGE) {
            f2Var.g();
            f2Var.e();
            boolean z4 = value instanceof c3;
            int zza = ((f2) entry.getKey()).zza();
            if (!z4) {
                int A = w1.A(zza);
                int A2 = w1.A(24) + w1.x((x3) value);
                int A3 = w1.A(16);
                int A4 = w1.A(8);
                return A4 + A4 + A3 + A + A2;
            }
            int A5 = w1.A(zza);
            int a9 = ((c3) value).a();
            int A6 = w1.A(a9) + a9;
            int A7 = w1.A(24);
            int A8 = w1.A(16);
            int A9 = w1.A(8);
            return A9 + A9 + A8 + A5 + A7 + A6;
        }
        return a(f2Var, value);
    }

    public final int b() {
        int i8 = 0;
        for (int i9 = 0; i9 < this.f14766a.b(); i9++) {
            i8 += o(this.f14766a.i(i9));
        }
        for (Map.Entry entry : this.f14766a.c()) {
            i8 += o(entry);
        }
        return i8;
    }

    /* renamed from: c */
    public final g2 clone() {
        g2 g2Var = new g2();
        for (int i8 = 0; i8 < this.f14766a.b(); i8++) {
            Map.Entry i9 = this.f14766a.i(i8);
            g2Var.i((f2) i9.getKey(), i9.getValue());
        }
        for (Map.Entry entry : this.f14766a.c()) {
            g2Var.i((f2) entry.getKey(), entry.getValue());
        }
        g2Var.f14768c = this.f14768c;
        return g2Var;
    }

    public final Object e(f2 f2Var) {
        Object obj = this.f14766a.get(f2Var);
        if (obj instanceof c3) {
            c3 c3Var = (c3) obj;
            throw null;
        }
        return obj;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof g2) {
            return this.f14766a.equals(((g2) obj).f14766a);
        }
        return false;
    }

    public final Iterator f() {
        return this.f14768c ? new b3(this.f14766a.entrySet().iterator()) : this.f14766a.entrySet().iterator();
    }

    public final void g() {
        if (this.f14767b) {
            return;
        }
        for (int i8 = 0; i8 < this.f14766a.b(); i8++) {
            Map.Entry i9 = this.f14766a.i(i8);
            if (i9.getValue() instanceof p2) {
                ((p2) i9.getValue()).z();
            }
        }
        this.f14766a.a();
        this.f14767b = true;
    }

    public final void h(g2 g2Var) {
        for (int i8 = 0; i8 < g2Var.f14766a.b(); i8++) {
            m(g2Var.f14766a.i(i8));
        }
        for (Map.Entry entry : g2Var.f14766a.c()) {
            m(entry);
        }
    }

    public final int hashCode() {
        return this.f14766a.hashCode();
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002c, code lost:
        if ((r7 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.r2) == false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0035, code lost:
        if ((r7 instanceof byte[]) == false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0049, code lost:
        if (r0 == false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0023, code lost:
        if ((r7 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.c3) == false) goto L28;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void i(com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2 r6, java.lang.Object r7) {
        /*
            r5 = this;
            r6.g()
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzho r0 = r6.b()
            byte[] r1 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.y2.f14888d
            java.util.Objects.requireNonNull(r7)
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzho r1 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzho.f15035b
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzhp r1 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzhp.INT
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzhp r0 = r0.c()
            int r0 = r0.ordinal()
            r1 = 1
            switch(r0) {
                case 0: goto L47;
                case 1: goto L44;
                case 2: goto L41;
                case 3: goto L3e;
                case 4: goto L3b;
                case 5: goto L38;
                case 6: goto L2f;
                case 7: goto L26;
                case 8: goto L1d;
                default: goto L1c;
            }
        L1c:
            goto L57
        L1d:
            boolean r0 = r7 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3
            if (r0 != 0) goto L4b
            boolean r0 = r7 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.c3
            if (r0 == 0) goto L57
            goto L4b
        L26:
            boolean r0 = r7 instanceof java.lang.Integer
            if (r0 != 0) goto L4b
            boolean r0 = r7 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.r2
            if (r0 == 0) goto L57
            goto L4b
        L2f:
            boolean r0 = r7 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
            if (r0 != 0) goto L4b
            boolean r0 = r7 instanceof byte[]
            if (r0 == 0) goto L57
            goto L4b
        L38:
            boolean r0 = r7 instanceof java.lang.String
            goto L49
        L3b:
            boolean r0 = r7 instanceof java.lang.Boolean
            goto L49
        L3e:
            boolean r0 = r7 instanceof java.lang.Double
            goto L49
        L41:
            boolean r0 = r7 instanceof java.lang.Float
            goto L49
        L44:
            boolean r0 = r7 instanceof java.lang.Long
            goto L49
        L47:
            boolean r0 = r7 instanceof java.lang.Integer
        L49:
            if (r0 == 0) goto L57
        L4b:
            boolean r0 = r7 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.c3
            if (r0 == 0) goto L51
            r5.f14768c = r1
        L51:
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.e5 r0 = r5.f14766a
            r0.put(r6, r7)
            return
        L57:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r2 = 3
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            int r4 = r6.zza()
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r2[r3] = r4
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzho r6 = r6.b()
            com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzhp r6 = r6.c()
            r2[r1] = r6
            r6 = 2
            java.lang.Class r7 = r7.getClass()
            java.lang.String r7 = r7.getName()
            r2[r6] = r7
            java.lang.String r6 = "Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n"
            java.lang.String r6 = java.lang.String.format(r6, r2)
            r0.<init>(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.g2.i(com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2, java.lang.Object):void");
    }

    public final boolean j() {
        return this.f14767b;
    }

    public final boolean k() {
        for (int i8 = 0; i8 < this.f14766a.b(); i8++) {
            if (!n(this.f14766a.i(i8))) {
                return false;
            }
        }
        for (Map.Entry entry : this.f14766a.c()) {
            if (!n(entry)) {
                return false;
            }
        }
        return true;
    }
}
