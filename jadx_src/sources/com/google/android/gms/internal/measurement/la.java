package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import sun.misc.Unsafe;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class la<T> implements xa<T> {

    /* renamed from: r  reason: collision with root package name */
    private static final int[] f12304r = new int[0];

    /* renamed from: s  reason: collision with root package name */
    private static final Unsafe f12305s = yb.p();

    /* renamed from: a  reason: collision with root package name */
    private final int[] f12306a;

    /* renamed from: b  reason: collision with root package name */
    private final Object[] f12307b;

    /* renamed from: c  reason: collision with root package name */
    private final int f12308c;

    /* renamed from: d  reason: collision with root package name */
    private final int f12309d;

    /* renamed from: e  reason: collision with root package name */
    private final ia f12310e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean f12311f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f12312g;

    /* renamed from: h  reason: collision with root package name */
    private final zzlr f12313h;

    /* renamed from: i  reason: collision with root package name */
    private final boolean f12314i;

    /* renamed from: j  reason: collision with root package name */
    private final int[] f12315j;

    /* renamed from: k  reason: collision with root package name */
    private final int f12316k;

    /* renamed from: l  reason: collision with root package name */
    private final int f12317l;

    /* renamed from: m  reason: collision with root package name */
    private final pa f12318m;

    /* renamed from: n  reason: collision with root package name */
    private final n9 f12319n;

    /* renamed from: o  reason: collision with root package name */
    private final tb<?, ?> f12320o;

    /* renamed from: p  reason: collision with root package name */
    private final n8<?> f12321p;
    private final ba q;

    private la(int[] iArr, Object[] objArr, int i8, int i9, ia iaVar, zzlr zzlrVar, boolean z4, int[] iArr2, int i10, int i11, pa paVar, n9 n9Var, tb<?, ?> tbVar, n8<?> n8Var, ba baVar) {
        this.f12306a = iArr;
        this.f12307b = objArr;
        this.f12308c = i8;
        this.f12309d = i9;
        this.f12312g = iaVar instanceof x8;
        this.f12313h = zzlrVar;
        this.f12311f = n8Var != null && n8Var.e(iaVar);
        this.f12314i = false;
        this.f12315j = iArr2;
        this.f12316k = i10;
        this.f12317l = i11;
        this.f12318m = paVar;
        this.f12319n = n9Var;
        this.f12320o = tbVar;
        this.f12321p = n8Var;
        this.f12310e = iaVar;
        this.q = baVar;
    }

    private static <T> float A(T t8, long j8) {
        return ((Float) yb.B(t8, j8)).floatValue();
    }

    private final int B(int i8) {
        return this.f12306a[i8 + 2];
    }

    private final void C(T t8, int i8) {
        int B = B(i8);
        long j8 = 1048575 & B;
        if (j8 == 1048575) {
            return;
        }
        yb.h(t8, j8, (1 << (B >>> 20)) | yb.t(t8, j8));
    }

    private final void D(T t8, int i8, int i9) {
        yb.h(t8, B(i9) & 1048575, i8);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void E(T t8, T t9, int i8) {
        int i9 = this.f12306a[i8];
        if (I(t9, i9, i8)) {
            long F = F(i8) & 1048575;
            Unsafe unsafe = f12305s;
            Object object = unsafe.getObject(t9, F);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.f12306a[i8] + " is present but null: " + String.valueOf(t9));
            }
            xa M = M(i8);
            if (!I(t8, i9, i8)) {
                if (S(object)) {
                    Object zza = M.zza();
                    M.g(zza, object);
                    unsafe.putObject(t8, F, zza);
                } else {
                    unsafe.putObject(t8, F, object);
                }
                D(t8, i9, i8);
                return;
            }
            Object object2 = unsafe.getObject(t8, F);
            if (!S(object2)) {
                Object zza2 = M.zza();
                M.g(zza2, object2);
                unsafe.putObject(t8, F, zza2);
                object2 = zza2;
            }
            M.g(object2, object);
        }
    }

    private final int F(int i8) {
        return this.f12306a[i8 + 1];
    }

    private static <T> int G(T t8, long j8) {
        return ((Integer) yb.B(t8, j8)).intValue();
    }

    private final boolean H(T t8, int i8) {
        int B = B(i8);
        long j8 = B & 1048575;
        if (j8 != 1048575) {
            return (yb.t(t8, j8) & (1 << (B >>> 20))) != 0;
        }
        int F = F(i8);
        long j9 = F & 1048575;
        switch ((F & 267386880) >>> 20) {
            case 0:
                return Double.doubleToRawLongBits(yb.a(t8, j9)) != 0;
            case 1:
                return Float.floatToRawIntBits(yb.n(t8, j9)) != 0;
            case 2:
                return yb.x(t8, j9) != 0;
            case 3:
                return yb.x(t8, j9) != 0;
            case 4:
                return yb.t(t8, j9) != 0;
            case 5:
                return yb.x(t8, j9) != 0;
            case 6:
                return yb.t(t8, j9) != 0;
            case 7:
                return yb.F(t8, j9);
            case 8:
                Object B2 = yb.B(t8, j9);
                if (B2 instanceof String) {
                    return !((String) B2).isEmpty();
                } else if (B2 instanceof zzij) {
                    return !zzij.f12852b.equals(B2);
                } else {
                    throw new IllegalArgumentException();
                }
            case 9:
                return yb.B(t8, j9) != null;
            case 10:
                return !zzij.f12852b.equals(yb.B(t8, j9));
            case 11:
                return yb.t(t8, j9) != 0;
            case 12:
                return yb.t(t8, j9) != 0;
            case 13:
                return yb.t(t8, j9) != 0;
            case 14:
                return yb.x(t8, j9) != 0;
            case 15:
                return yb.t(t8, j9) != 0;
            case 16:
                return yb.x(t8, j9) != 0;
            case 17:
                return yb.B(t8, j9) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean I(T t8, int i8, int i9) {
        return yb.t(t8, (long) (B(i9) & 1048575)) == i8;
    }

    private final boolean J(T t8, T t9, int i8) {
        return H(t8, i8) == H(t9, i8);
    }

    private static <T> long K(T t8, long j8) {
        return ((Long) yb.B(t8, j8)).longValue();
    }

    private final b9 L(int i8) {
        return (b9) this.f12307b[((i8 / 3) << 1) + 1];
    }

    private final xa M(int i8) {
        int i9 = (i8 / 3) << 1;
        xa xaVar = (xa) this.f12307b[i9];
        if (xaVar != null) {
            return xaVar;
        }
        xa<T> b9 = ua.a().b((Class) this.f12307b[i9 + 1]);
        this.f12307b[i9] = b9;
        return b9;
    }

    private static vb N(Object obj) {
        x8 x8Var = (x8) obj;
        vb vbVar = x8Var.zzb;
        if (vbVar == vb.k()) {
            vb l8 = vb.l();
            x8Var.zzb = l8;
            return l8;
        }
        return vbVar;
    }

    private static <T> boolean O(T t8, long j8) {
        return ((Boolean) yb.B(t8, j8)).booleanValue();
    }

    private final Object P(int i8) {
        return this.f12307b[(i8 / 3) << 1];
    }

    private static void Q(Object obj) {
        if (S(obj)) {
            return;
        }
        String valueOf = String.valueOf(obj);
        throw new IllegalArgumentException("Mutating immutable message: " + valueOf);
    }

    private static boolean R(int i8) {
        return (i8 & 536870912) != 0;
    }

    private static boolean S(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof x8) {
            return ((x8) obj).G();
        }
        return true;
    }

    private static <T> double i(T t8, long j8) {
        return ((Double) yb.B(t8, j8)).doubleValue();
    }

    private final int j(int i8) {
        if (i8 < this.f12308c || i8 > this.f12309d) {
            return -1;
        }
        return k(i8, 0);
    }

    private final int k(int i8, int i9) {
        int length = (this.f12306a.length / 3) - 1;
        while (i9 <= length) {
            int i10 = (length + i9) >>> 1;
            int i11 = i10 * 3;
            int i12 = this.f12306a[i11];
            if (i8 == i12) {
                return i11;
            }
            if (i8 < i12) {
                length = i10 - 1;
            } else {
                i9 = i10 + 1;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0259  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x025c  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0273  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0276  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <T> com.google.android.gms.internal.measurement.la<T> m(java.lang.Class<T> r32, com.google.android.gms.internal.measurement.ga r33, com.google.android.gms.internal.measurement.pa r34, com.google.android.gms.internal.measurement.n9 r35, com.google.android.gms.internal.measurement.tb<?, ?> r36, com.google.android.gms.internal.measurement.n8<?> r37, com.google.android.gms.internal.measurement.ba r38) {
        /*
            Method dump skipped, instructions count: 1021
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.la.m(java.lang.Class, com.google.android.gms.internal.measurement.ga, com.google.android.gms.internal.measurement.pa, com.google.android.gms.internal.measurement.n9, com.google.android.gms.internal.measurement.tb, com.google.android.gms.internal.measurement.n8, com.google.android.gms.internal.measurement.ba):com.google.android.gms.internal.measurement.la");
    }

    private final <K, V, UT, UB> UB n(int i8, int i9, Map<K, V> map, b9 b9Var, UB ub, tb<UT, UB> tbVar, Object obj) {
        z9<?, ?> a9 = this.q.a(P(i8));
        Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> next = it.next();
            if (!b9Var.c(((Integer) next.getValue()).intValue())) {
                if (ub == null) {
                    ub = tbVar.i(obj);
                }
                v7 y8 = zzij.y(aa.a(a9, next.getKey(), next.getValue()));
                try {
                    aa.b(y8.b(), a9, next.getKey(), next.getValue());
                    tbVar.c(ub, i9, y8.a());
                    it.remove();
                } catch (IOException e8) {
                    throw new RuntimeException(e8);
                }
            }
        }
        return ub;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final Object o(T t8, int i8) {
        xa M = M(i8);
        long F = F(i8) & 1048575;
        if (H(t8, i8)) {
            Object object = f12305s.getObject(t8, F);
            if (S(object)) {
                return object;
            }
            Object zza = M.zza();
            if (object != null) {
                M.g(zza, object);
            }
            return zza;
        }
        return M.zza();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final Object p(T t8, int i8, int i9) {
        xa M = M(i9);
        if (I(t8, i8, i9)) {
            Object object = f12305s.getObject(t8, F(i9) & 1048575);
            if (S(object)) {
                return object;
            }
            Object zza = M.zza();
            if (object != null) {
                M.g(zza, object);
            }
            return zza;
        }
        return M.zza();
    }

    private final <UT, UB> UB q(Object obj, int i8, UB ub, tb<UT, UB> tbVar, Object obj2) {
        b9 L;
        int i9 = this.f12306a[i8];
        Object B = yb.B(obj, F(i8) & 1048575);
        return (B == null || (L = L(i8)) == null) ? ub : (UB) n(i8, i9, this.q.d(B), L, ub, tbVar, obj2);
    }

    private static Field r(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            throw new RuntimeException("Field " + str + " for " + cls.getName() + " not found. Known fields are " + Arrays.toString(declaredFields));
        }
    }

    private static void s(int i8, Object obj, rc rcVar) {
        if (obj instanceof String) {
            rcVar.t(i8, (String) obj);
        } else {
            rcVar.G(i8, (zzij) obj);
        }
    }

    private static <UT, UB> void t(tb<UT, UB> tbVar, T t8, rc rcVar) {
        tbVar.g(tbVar.k(t8), rcVar);
    }

    private final <K, V> void u(rc rcVar, int i8, Object obj, int i9) {
        if (obj != null) {
            rcVar.B(i8, this.q.a(P(i9)), this.q.e(obj));
        }
    }

    private final void v(T t8, int i8, int i9, Object obj) {
        f12305s.putObject(t8, F(i9) & 1048575, obj);
        D(t8, i8, i9);
    }

    private final void w(T t8, int i8, Object obj) {
        f12305s.putObject(t8, F(i8) & 1048575, obj);
        C(t8, i8);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void x(T t8, T t9, int i8) {
        if (H(t9, i8)) {
            long F = F(i8) & 1048575;
            Unsafe unsafe = f12305s;
            Object object = unsafe.getObject(t9, F);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.f12306a[i8] + " is present but null: " + String.valueOf(t9));
            }
            xa M = M(i8);
            if (!H(t8, i8)) {
                if (S(object)) {
                    Object zza = M.zza();
                    M.g(zza, object);
                    unsafe.putObject(t8, F, zza);
                } else {
                    unsafe.putObject(t8, F, object);
                }
                C(t8, i8);
                return;
            }
            Object object2 = unsafe.getObject(t8, F);
            if (!S(object2)) {
                Object zza2 = M.zza();
                M.g(zza2, object2);
                unsafe.putObject(t8, F, zza2);
                object2 = zza2;
            }
            M.g(object2, object);
        }
    }

    private final boolean y(T t8, int i8, int i9, int i10, int i11) {
        return i9 == 1048575 ? H(t8, i8) : (i10 & i11) != 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean z(Object obj, int i8, xa xaVar) {
        return xaVar.e(yb.B(obj, i8 & 1048575));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0204, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0211, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x021e, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x022b, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0238, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0245, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0252, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0254, code lost:
        r12 = r12 + ((com.google.android.gms.internal.measurement.zzja.k0(r13) + com.google.android.gms.internal.measurement.zzja.o0(r0)) + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01a3, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x01b1, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x01bf, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01cd, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01db, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x01e9, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x01f7, code lost:
        if (r0 > 0) goto L79;
     */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r10v5 */
    @Override // com.google.android.gms.internal.measurement.xa
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int a(T r19) {
        /*
            Method dump skipped, instructions count: 1492
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.la.a(java.lang.Object):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00ce, code lost:
        if (r3 != null) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00e0, code lost:
        if (r3 != null) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00e2, code lost:
        r7 = r3.hashCode();
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00e6, code lost:
        r2 = (r2 * 53) + r7;
     */
    @Override // com.google.android.gms.internal.measurement.xa
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int b(T r9) {
        /*
            Method dump skipped, instructions count: 476
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.la.b(java.lang.Object):int");
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final void c(T t8, byte[] bArr, int i8, int i9, m7 m7Var) {
        l(t8, bArr, i8, i9, 0, m7Var);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x04ff  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x0b32  */
    @Override // com.google.android.gms.internal.measurement.xa
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void d(T r24, com.google.android.gms.internal.measurement.rc r25) {
        /*
            Method dump skipped, instructions count: 3178
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.la.d(java.lang.Object, com.google.android.gms.internal.measurement.rc):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.measurement.xa
    public final boolean e(T t8) {
        int i8;
        int i9;
        int i10 = 1048575;
        int i11 = 0;
        int i12 = 0;
        while (true) {
            boolean z4 = true;
            if (i12 >= this.f12316k) {
                return !this.f12311f || this.f12321p.b(t8).s();
            }
            int i13 = this.f12315j[i12];
            int i14 = this.f12306a[i13];
            int F = F(i13);
            int i15 = this.f12306a[i13 + 2];
            int i16 = i15 & 1048575;
            int i17 = 1 << (i15 >>> 20);
            if (i16 != i10) {
                if (i16 != 1048575) {
                    i11 = f12305s.getInt(t8, i16);
                }
                i9 = i11;
                i8 = i16;
            } else {
                i8 = i10;
                i9 = i11;
            }
            if (((268435456 & F) != 0) && !y(t8, i13, i8, i9, i17)) {
                return false;
            }
            int i18 = (267386880 & F) >>> 20;
            if (i18 != 9 && i18 != 17) {
                if (i18 != 27) {
                    if (i18 == 60 || i18 == 68) {
                        if (I(t8, i14, i13) && !z(t8, F, M(i13))) {
                            return false;
                        }
                    } else if (i18 != 49) {
                        if (i18 == 50 && !this.q.e(yb.B(t8, F & 1048575)).isEmpty()) {
                            this.q.a(P(i13));
                            throw null;
                        }
                    }
                }
                List list = (List) yb.B(t8, F & 1048575);
                if (!list.isEmpty()) {
                    xa M = M(i13);
                    int i19 = 0;
                    while (true) {
                        if (i19 >= list.size()) {
                            break;
                        } else if (!M.e(list.get(i19))) {
                            z4 = false;
                            break;
                        } else {
                            i19++;
                        }
                    }
                }
                if (!z4) {
                    return false;
                }
            } else if (y(t8, i13, i8, i9, i17) && !z(t8, F, M(i13))) {
                return false;
            }
            i12++;
            i10 = i8;
            i11 = i9;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.measurement.xa
    public final void f(T t8) {
        int i8;
        if (S(t8)) {
            if (t8 instanceof x8) {
                x8 x8Var = (x8) t8;
                x8Var.l(Integer.MAX_VALUE);
                x8Var.zza = 0;
                x8Var.F();
            }
            int length = this.f12306a.length;
            while (i8 < length) {
                int F = F(i8);
                long j8 = 1048575 & F;
                int i9 = (F & 267386880) >>> 20;
                if (i9 != 9) {
                    if (i9 != 60 && i9 != 68) {
                        switch (i9) {
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                            case 26:
                            case 27:
                            case 28:
                            case 29:
                            case 30:
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 38:
                            case 39:
                            case 40:
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                            case 45:
                            case 46:
                            case 47:
                            case 48:
                            case 49:
                                this.f12319n.d(t8, j8);
                                break;
                            case 50:
                                Unsafe unsafe = f12305s;
                                Object object = unsafe.getObject(t8, j8);
                                if (object == null) {
                                    break;
                                } else {
                                    unsafe.putObject(t8, j8, this.q.f(object));
                                    break;
                                }
                        }
                    } else {
                        if (!I(t8, this.f12306a[i8], i8)) {
                        }
                        M(i8).f(f12305s.getObject(t8, j8));
                    }
                }
                i8 = H(t8, i8) ? 0 : i8 + 3;
                M(i8).f(f12305s.getObject(t8, j8));
            }
            this.f12320o.l(t8);
            if (this.f12311f) {
                this.f12321p.g(t8);
            }
        }
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final void g(T t8, T t9) {
        Q(t8);
        Objects.requireNonNull(t9);
        for (int i8 = 0; i8 < this.f12306a.length; i8 += 3) {
            int F = F(i8);
            long j8 = 1048575 & F;
            int i9 = this.f12306a[i8];
            switch ((F & 267386880) >>> 20) {
                case 0:
                    if (H(t9, i8)) {
                        yb.f(t8, j8, yb.a(t9, j8));
                        C(t8, i8);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (H(t9, i8)) {
                        yb.g(t8, j8, yb.n(t9, j8));
                        C(t8, i8);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.i(t8, j8, yb.x(t9, j8));
                    C(t8, i8);
                    break;
                case 3:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.i(t8, j8, yb.x(t9, j8));
                    C(t8, i8);
                    break;
                case 4:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.h(t8, j8, yb.t(t9, j8));
                    C(t8, i8);
                    break;
                case 5:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.i(t8, j8, yb.x(t9, j8));
                    C(t8, i8);
                    break;
                case 6:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.h(t8, j8, yb.t(t9, j8));
                    C(t8, i8);
                    break;
                case 7:
                    if (H(t9, i8)) {
                        yb.v(t8, j8, yb.F(t9, j8));
                        C(t8, i8);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.j(t8, j8, yb.B(t9, j8));
                    C(t8, i8);
                    break;
                case 9:
                case 17:
                    x(t8, t9, i8);
                    break;
                case 10:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.j(t8, j8, yb.B(t9, j8));
                    C(t8, i8);
                    break;
                case 11:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.h(t8, j8, yb.t(t9, j8));
                    C(t8, i8);
                    break;
                case 12:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.h(t8, j8, yb.t(t9, j8));
                    C(t8, i8);
                    break;
                case 13:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.h(t8, j8, yb.t(t9, j8));
                    C(t8, i8);
                    break;
                case 14:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.i(t8, j8, yb.x(t9, j8));
                    C(t8, i8);
                    break;
                case 15:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.h(t8, j8, yb.t(t9, j8));
                    C(t8, i8);
                    break;
                case 16:
                    if (!H(t9, i8)) {
                        break;
                    }
                    yb.i(t8, j8, yb.x(t9, j8));
                    C(t8, i8);
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    this.f12319n.b(t8, t9, j8);
                    break;
                case 50:
                    ab.m(this.q, t8, t9, j8);
                    break;
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                    if (!I(t9, i9, i8)) {
                        break;
                    }
                    yb.j(t8, j8, yb.B(t9, j8));
                    D(t8, i9, i8);
                    break;
                case 60:
                case 68:
                    E(t8, t9, i8);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (!I(t9, i9, i8)) {
                        break;
                    }
                    yb.j(t8, j8, yb.B(t9, j8));
                    D(t8, i9, i8);
                    break;
            }
        }
        ab.n(this.f12320o, t8, t9);
        if (this.f12311f) {
            ab.l(this.f12321p, t8, t9);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x01b0, code lost:
        if (java.lang.Double.doubleToLongBits(com.google.android.gms.internal.measurement.yb.a(r10, r6)) == java.lang.Double.doubleToLongBits(com.google.android.gms.internal.measurement.yb.a(r11, r6))) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0038, code lost:
        if (com.google.android.gms.internal.measurement.ab.p(com.google.android.gms.internal.measurement.yb.B(r10, r6), com.google.android.gms.internal.measurement.yb.B(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x005c, code lost:
        if (com.google.android.gms.internal.measurement.ab.p(com.google.android.gms.internal.measurement.yb.B(r10, r6), com.google.android.gms.internal.measurement.yb.B(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0070, code lost:
        if (com.google.android.gms.internal.measurement.yb.x(r10, r6) == com.google.android.gms.internal.measurement.yb.x(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0082, code lost:
        if (com.google.android.gms.internal.measurement.yb.t(r10, r6) == com.google.android.gms.internal.measurement.yb.t(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0096, code lost:
        if (com.google.android.gms.internal.measurement.yb.x(r10, r6) == com.google.android.gms.internal.measurement.yb.x(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00a8, code lost:
        if (com.google.android.gms.internal.measurement.yb.t(r10, r6) == com.google.android.gms.internal.measurement.yb.t(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00ba, code lost:
        if (com.google.android.gms.internal.measurement.yb.t(r10, r6) == com.google.android.gms.internal.measurement.yb.t(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00cc, code lost:
        if (com.google.android.gms.internal.measurement.yb.t(r10, r6) == com.google.android.gms.internal.measurement.yb.t(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00e2, code lost:
        if (com.google.android.gms.internal.measurement.ab.p(com.google.android.gms.internal.measurement.yb.B(r10, r6), com.google.android.gms.internal.measurement.yb.B(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00f8, code lost:
        if (com.google.android.gms.internal.measurement.ab.p(com.google.android.gms.internal.measurement.yb.B(r10, r6), com.google.android.gms.internal.measurement.yb.B(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x010e, code lost:
        if (com.google.android.gms.internal.measurement.ab.p(com.google.android.gms.internal.measurement.yb.B(r10, r6), com.google.android.gms.internal.measurement.yb.B(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0120, code lost:
        if (com.google.android.gms.internal.measurement.yb.F(r10, r6) == com.google.android.gms.internal.measurement.yb.F(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0132, code lost:
        if (com.google.android.gms.internal.measurement.yb.t(r10, r6) == com.google.android.gms.internal.measurement.yb.t(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0145, code lost:
        if (com.google.android.gms.internal.measurement.yb.x(r10, r6) == com.google.android.gms.internal.measurement.yb.x(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0156, code lost:
        if (com.google.android.gms.internal.measurement.yb.t(r10, r6) == com.google.android.gms.internal.measurement.yb.t(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0169, code lost:
        if (com.google.android.gms.internal.measurement.yb.x(r10, r6) == com.google.android.gms.internal.measurement.yb.x(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x017c, code lost:
        if (com.google.android.gms.internal.measurement.yb.x(r10, r6) == com.google.android.gms.internal.measurement.yb.x(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0195, code lost:
        if (java.lang.Float.floatToIntBits(com.google.android.gms.internal.measurement.yb.n(r10, r6)) == java.lang.Float.floatToIntBits(com.google.android.gms.internal.measurement.yb.n(r11, r6))) goto L84;
     */
    @Override // com.google.android.gms.internal.measurement.xa
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean h(T r10, T r11) {
        /*
            Method dump skipped, instructions count: 626
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.la.h(java.lang.Object, java.lang.Object):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x0581, code lost:
        if (r1 == 0) goto L248;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0583, code lost:
        r12.add(com.google.android.gms.internal.measurement.zzij.f12852b);
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0589, code lost:
        r12.add(com.google.android.gms.internal.measurement.zzij.p(r35, r0, r1));
        r0 = r0 + r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0591, code lost:
        if (r0 >= r14) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0593, code lost:
        r1 = com.google.android.gms.internal.measurement.n7.p(r35, r0, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0599, code lost:
        if (r19 != r13.f12344a) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x059b, code lost:
        r0 = com.google.android.gms.internal.measurement.n7.p(r35, r1, r13);
        r1 = r13.f12344a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x05a1, code lost:
        if (r1 < 0) goto L260;
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x05a5, code lost:
        if (r1 > (r35.length - r0)) goto L257;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x05a7, code lost:
        if (r1 != 0) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x05ae, code lost:
        throw com.google.android.gms.internal.measurement.zzkb.f();
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x05b3, code lost:
        throw com.google.android.gms.internal.measurement.zzkb.d();
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x06d4, code lost:
        if (r1.f12345b != 0) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x06d6, code lost:
        r2 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x06d8, code lost:
        r2 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x06da, code lost:
        r12.g(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x06dd, code lost:
        if (r0 >= r14) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x06df, code lost:
        r2 = com.google.android.gms.internal.measurement.n7.p(r35, r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x06e5, code lost:
        if (r5 != r1.f12344a) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x06e7, code lost:
        r0 = com.google.android.gms.internal.measurement.n7.q(r35, r2, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x06ef, code lost:
        if (r1.f12345b == 0) goto L355;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0bf3, code lost:
        if (r14 == 1048575) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0bf5, code lost:
        r20.putInt(r10, r14, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0bfb, code lost:
        r9 = r7.f12316k;
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0c02, code lost:
        if (r9 >= r7.f12317l) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0c04, code lost:
        r3 = (com.google.android.gms.internal.measurement.vb) q(r34, r7.f12315j[r9], r3, r7.f12320o, r34);
        r9 = r9 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0c1a, code lost:
        if (r3 == null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0c1c, code lost:
        r7.f12320o.h(r10, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0c23, code lost:
        if (r6 != 0) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0c25, code lost:
        if (r8 != r37) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0c2c, code lost:
        throw com.google.android.gms.internal.measurement.zzkb.e();
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0c2d, code lost:
        if (r8 > r37) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0c2f, code lost:
        if (r11 != r6) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0c31, code lost:
        return r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0c36, code lost:
        throw com.google.android.gms.internal.measurement.zzkb.e();
     */
    /* JADX WARN: Removed duplicated region for block: B:232:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0659  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:199:0x0589 -> B:200:0x0591). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:207:0x05a7 -> B:198:0x0583). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:230:0x0609 -> B:231:0x060d). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:236:0x061f -> B:228:0x05fe). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:250:0x0653 -> B:251:0x0657). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:256:0x0669 -> B:246:0x0640). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:287:0x06d8 -> B:288:0x06da). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:293:0x06ef -> B:286:0x06d6). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int l(T r34, byte[] r35, int r36, int r37, int r38, com.google.android.gms.internal.measurement.m7 r39) {
        /*
            Method dump skipped, instructions count: 3276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.la.l(java.lang.Object, byte[], int, int, int, com.google.android.gms.internal.measurement.m7):int");
    }

    @Override // com.google.android.gms.internal.measurement.xa
    public final T zza() {
        return (T) this.f12318m.a(this.f12310e);
    }
}
