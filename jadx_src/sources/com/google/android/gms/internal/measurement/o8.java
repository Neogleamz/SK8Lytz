package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.q8;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o8<T extends q8<T>> {

    /* renamed from: d  reason: collision with root package name */
    private static final o8 f12401d = new o8(true);

    /* renamed from: a  reason: collision with root package name */
    final ya<T, Object> f12402a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f12403b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f12404c;

    private o8() {
        this.f12402a = ya.c(16);
    }

    private o8(ya<T, Object> yaVar) {
        this.f12402a = yaVar;
        q();
    }

    private o8(boolean z4) {
        this(ya.c(0));
        q();
    }

    public static int b(q8<?> q8Var, Object obj) {
        zzng zzb = q8Var.zzb();
        int zza = q8Var.zza();
        if (q8Var.d()) {
            List<Object> list = (List) obj;
            int i8 = 0;
            if (!q8Var.b()) {
                for (Object obj2 : list) {
                    i8 += c(zzb, zza, obj2);
                }
                return i8;
            } else if (list.isEmpty()) {
                return 0;
            } else {
                for (Object obj3 : list) {
                    i8 += d(zzb, obj3);
                }
                return zzja.k0(zza) + i8 + zzja.o0(i8);
            }
        }
        return c(zzb, zza, obj);
    }

    static int c(zzng zzngVar, int i8, Object obj) {
        int k02 = zzja.k0(i8);
        if (zzngVar == zzng.f12934m) {
            a9.g((ia) obj);
            k02 <<= 1;
        }
        return k02 + d(zzngVar, obj);
    }

    private static int d(zzng zzngVar, Object obj) {
        switch (r8.f12487b[zzngVar.ordinal()]) {
            case 1:
                return zzja.c(((Double) obj).doubleValue());
            case 2:
                return zzja.d(((Float) obj).floatValue());
            case 3:
                return zzja.F(((Long) obj).longValue());
            case 4:
                return zzja.j0(((Long) obj).longValue());
            case 5:
                return zzja.T(((Integer) obj).intValue());
            case 6:
                return zzja.p(((Long) obj).longValue());
            case 7:
                return zzja.z(((Integer) obj).intValue());
            case 8:
                return zzja.v(((Boolean) obj).booleanValue());
            case 9:
                return zzja.s((ia) obj);
            case 10:
                return obj instanceof i9 ? zzja.r((i9) obj) : zzja.G((ia) obj);
            case 11:
                return obj instanceof zzij ? zzja.q((zzij) obj) : zzja.u((String) obj);
            case 12:
                return obj instanceof zzij ? zzja.q((zzij) obj) : zzja.w((byte[]) obj);
            case 13:
                return zzja.o0(((Integer) obj).intValue());
            case 14:
                return zzja.b0(((Integer) obj).intValue());
            case 15:
                return zzja.W(((Long) obj).longValue());
            case 16:
                return zzja.g0(((Integer) obj).intValue());
            case 17:
                return zzja.e0(((Long) obj).longValue());
            case 18:
                return obj instanceof z8 ? zzja.e(((z8) obj).zza()) : zzja.e(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    private static int e(Map.Entry<T, Object> entry) {
        T key = entry.getKey();
        Object value = entry.getValue();
        if (key.a() != zznq.MESSAGE || key.d() || key.b()) {
            return b(key, value);
        }
        boolean z4 = value instanceof i9;
        int zza = entry.getKey().zza();
        return z4 ? zzja.k(zza, (i9) value) : zzja.l(zza, (ia) value);
    }

    private final Object f(T t8) {
        Object obj = this.f12402a.get(t8);
        if (obj instanceof i9) {
            i9 i9Var = (i9) obj;
            return i9.e();
        }
        return obj;
    }

    private static Object g(Object obj) {
        if (obj instanceof ma) {
            return ((ma) obj).zza();
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            byte[] bArr2 = new byte[bArr.length];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            return bArr2;
        }
        return obj;
    }

    public static <T extends q8<T>> o8<T> i() {
        return f12401d;
    }

    private final void j(T t8, Object obj) {
        if (!t8.d()) {
            n(t8, obj);
        } else if (!(obj instanceof List)) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            int size = arrayList.size();
            int i8 = 0;
            while (i8 < size) {
                Object obj2 = arrayList.get(i8);
                i8++;
                n(t8, obj2);
            }
            obj = arrayList;
        }
        if (obj instanceof i9) {
            this.f12404c = true;
        }
        this.f12402a.put(t8, obj);
    }

    private final void k(Map.Entry<T, Object> entry) {
        T key = entry.getKey();
        Object value = entry.getValue();
        boolean z4 = value instanceof i9;
        if (key.d()) {
            if (z4) {
                throw new IllegalStateException("Lazy fields can not be repeated");
            }
            Object f5 = f(key);
            if (f5 == null) {
                f5 = new ArrayList();
            }
            for (Object obj : (List) value) {
                ((List) f5).add(g(obj));
            }
            this.f12402a.put(key, f5);
        } else if (key.a() != zznq.MESSAGE) {
            if (z4) {
                throw new IllegalStateException("Lazy fields must be message-valued");
            }
            this.f12402a.put(key, g(value));
        } else {
            Object f8 = f(key);
            if (f8 == null) {
                this.f12402a.put(key, g(value));
                if (z4) {
                    this.f12404c = true;
                    return;
                }
                return;
            }
            if (z4) {
                i9 i9Var = (i9) value;
                value = i9.e();
            }
            this.f12402a.put(key, f8 instanceof ma ? key.l((ma) f8, (ma) value) : key.n(((ia) f8).d(), (ia) value).n());
        }
    }

    private static boolean l(Object obj) {
        if (obj instanceof ka) {
            return ((ka) obj).g();
        }
        if (obj instanceof i9) {
            return true;
        }
        throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0029, code lost:
        if ((r6 instanceof com.google.android.gms.internal.measurement.z8) == false) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0032, code lost:
        if ((r6 instanceof byte[]) == false) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0020, code lost:
        if ((r6 instanceof com.google.android.gms.internal.measurement.i9) == false) goto L3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void n(T r5, java.lang.Object r6) {
        /*
            com.google.android.gms.internal.measurement.zzng r0 = r5.zzb()
            com.google.android.gms.internal.measurement.a9.e(r6)
            int[] r1 = com.google.android.gms.internal.measurement.r8.f12486a
            com.google.android.gms.internal.measurement.zznq r0 = r0.c()
            int r0 = r0.ordinal()
            r0 = r1[r0]
            r1 = 1
            r2 = 0
            switch(r0) {
                case 1: goto L45;
                case 2: goto L42;
                case 3: goto L3f;
                case 4: goto L3c;
                case 5: goto L39;
                case 6: goto L36;
                case 7: goto L2c;
                case 8: goto L23;
                case 9: goto L1a;
                default: goto L18;
            }
        L18:
            r0 = r2
            goto L47
        L1a:
            boolean r0 = r6 instanceof com.google.android.gms.internal.measurement.ia
            if (r0 != 0) goto L34
            boolean r0 = r6 instanceof com.google.android.gms.internal.measurement.i9
            if (r0 == 0) goto L18
            goto L34
        L23:
            boolean r0 = r6 instanceof java.lang.Integer
            if (r0 != 0) goto L34
            boolean r0 = r6 instanceof com.google.android.gms.internal.measurement.z8
            if (r0 == 0) goto L18
            goto L34
        L2c:
            boolean r0 = r6 instanceof com.google.android.gms.internal.measurement.zzij
            if (r0 != 0) goto L34
            boolean r0 = r6 instanceof byte[]
            if (r0 == 0) goto L18
        L34:
            r0 = r1
            goto L47
        L36:
            boolean r0 = r6 instanceof java.lang.String
            goto L47
        L39:
            boolean r0 = r6 instanceof java.lang.Boolean
            goto L47
        L3c:
            boolean r0 = r6 instanceof java.lang.Double
            goto L47
        L3f:
            boolean r0 = r6 instanceof java.lang.Float
            goto L47
        L42:
            boolean r0 = r6 instanceof java.lang.Long
            goto L47
        L45:
            boolean r0 = r6 instanceof java.lang.Integer
        L47:
            if (r0 == 0) goto L4a
            return
        L4a:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]
            int r4 = r5.zza()
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r3[r2] = r4
            com.google.android.gms.internal.measurement.zzng r5 = r5.zzb()
            com.google.android.gms.internal.measurement.zznq r5 = r5.c()
            r3[r1] = r5
            r5 = 2
            java.lang.Class r6 = r6.getClass()
            java.lang.String r6 = r6.getName()
            r3[r5] = r6
            java.lang.String r5 = "Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n"
            java.lang.String r5 = java.lang.String.format(r5, r3)
            r0.<init>(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.o8.n(com.google.android.gms.internal.measurement.q8, java.lang.Object):void");
    }

    private static <T extends q8<T>> boolean o(Map.Entry<T, Object> entry) {
        T key = entry.getKey();
        if (key.a() == zznq.MESSAGE) {
            boolean d8 = key.d();
            Object value = entry.getValue();
            if (d8) {
                for (Object obj : (List) value) {
                    if (!l(obj)) {
                        return false;
                    }
                }
                return true;
            }
            return l(value);
        }
        return true;
    }

    public final int a() {
        int i8 = 0;
        for (int i9 = 0; i9 < this.f12402a.a(); i9++) {
            i8 += e(this.f12402a.j(i9));
        }
        for (Map.Entry<T, Object> entry : this.f12402a.i()) {
            i8 += e(entry);
        }
        return i8;
    }

    public final /* synthetic */ Object clone() {
        o8 o8Var = new o8();
        for (int i8 = 0; i8 < this.f12402a.a(); i8++) {
            Map.Entry<T, Object> j8 = this.f12402a.j(i8);
            o8Var.j(j8.getKey(), j8.getValue());
        }
        for (Map.Entry<T, Object> entry : this.f12402a.i()) {
            o8Var.j(entry.getKey(), entry.getValue());
        }
        o8Var.f12404c = this.f12404c;
        return o8Var;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof o8) {
            return this.f12402a.equals(((o8) obj).f12402a);
        }
        return false;
    }

    public final void h(o8<T> o8Var) {
        for (int i8 = 0; i8 < o8Var.f12402a.a(); i8++) {
            k(o8Var.f12402a.j(i8));
        }
        for (Map.Entry<T, Object> entry : o8Var.f12402a.i()) {
            k(entry);
        }
    }

    public final int hashCode() {
        return this.f12402a.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Iterator<Map.Entry<T, Object>> m() {
        return this.f12404c ? new j9(this.f12402a.n().iterator()) : this.f12402a.n().iterator();
    }

    public final Iterator<Map.Entry<T, Object>> p() {
        return this.f12404c ? new j9(this.f12402a.entrySet().iterator()) : this.f12402a.entrySet().iterator();
    }

    public final void q() {
        if (this.f12403b) {
            return;
        }
        for (int i8 = 0; i8 < this.f12402a.a(); i8++) {
            Map.Entry<T, Object> j8 = this.f12402a.j(i8);
            if (j8.getValue() instanceof x8) {
                ((x8) j8.getValue()).E();
            }
        }
        this.f12402a.o();
        this.f12403b = true;
    }

    public final boolean r() {
        return this.f12403b;
    }

    public final boolean s() {
        for (int i8 = 0; i8 < this.f12402a.a(); i8++) {
            if (!o(this.f12402a.j(i8))) {
                return false;
            }
        }
        for (Map.Entry<T, Object> entry : this.f12402a.i()) {
            if (!o(entry)) {
                return false;
            }
        }
        return true;
    }
}
