package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import sun.misc.Unsafe;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a4<T> implements r4<T> {

    /* renamed from: p  reason: collision with root package name */
    private static final int[] f14706p = new int[0];
    private static final Unsafe q = s5.l();

    /* renamed from: a  reason: collision with root package name */
    private final int[] f14707a;

    /* renamed from: b  reason: collision with root package name */
    private final Object[] f14708b;

    /* renamed from: c  reason: collision with root package name */
    private final int f14709c;

    /* renamed from: d  reason: collision with root package name */
    private final int f14710d;

    /* renamed from: e  reason: collision with root package name */
    private final x3 f14711e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean f14712f;

    /* renamed from: g  reason: collision with root package name */
    private final int[] f14713g;

    /* renamed from: h  reason: collision with root package name */
    private final int f14714h;

    /* renamed from: i  reason: collision with root package name */
    private final int f14715i;

    /* renamed from: j  reason: collision with root package name */
    private final m3 f14716j;

    /* renamed from: k  reason: collision with root package name */
    private final h5 f14717k;

    /* renamed from: l  reason: collision with root package name */
    private final c2 f14718l;

    /* renamed from: m  reason: collision with root package name */
    private final int f14719m;

    /* renamed from: n  reason: collision with root package name */
    private final d4 f14720n;

    /* renamed from: o  reason: collision with root package name */
    private final s3 f14721o;

    private a4(int[] iArr, Object[] objArr, int i8, int i9, x3 x3Var, int i10, boolean z4, int[] iArr2, int i11, int i12, d4 d4Var, m3 m3Var, h5 h5Var, c2 c2Var, s3 s3Var) {
        this.f14707a = iArr;
        this.f14708b = objArr;
        this.f14709c = i8;
        this.f14710d = i9;
        this.f14719m = i10;
        boolean z8 = false;
        if (c2Var != null && c2Var.g(x3Var)) {
            z8 = true;
        }
        this.f14712f = z8;
        this.f14713g = iArr2;
        this.f14714h = i11;
        this.f14715i = i12;
        this.f14720n = d4Var;
        this.f14716j = m3Var;
        this.f14717k = h5Var;
        this.f14718l = c2Var;
        this.f14711e = x3Var;
        this.f14721o = s3Var;
    }

    private static boolean A(Object obj, int i8, r4 r4Var) {
        return r4Var.f(s5.k(obj, i8 & 1048575));
    }

    private static boolean B(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof p2) {
            return ((p2) obj).E();
        }
        return true;
    }

    private final boolean C(Object obj, int i8, int i9) {
        return s5.h(obj, (long) (R(i9) & 1048575)) == i8;
    }

    private static boolean D(Object obj, long j8) {
        return ((Boolean) s5.k(obj, j8)).booleanValue();
    }

    private static final void E(int i8, Object obj, y5 y5Var) {
        if (obj instanceof String) {
            y5Var.g(i8, (String) obj);
        } else {
            y5Var.h(i8, (zzdb) obj);
        }
    }

    static i5 G(Object obj) {
        p2 p2Var = (p2) obj;
        i5 i5Var = p2Var.zzc;
        if (i5Var == i5.c()) {
            i5 f5 = i5.f();
            p2Var.zzc = f5;
            return f5;
        }
        return i5Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:123:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0252  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x026a  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x026d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4 H(java.lang.Class r30, com.google.android.gms.internal.mlkit_vision_barcode_bundled.u3 r31, com.google.android.gms.internal.mlkit_vision_barcode_bundled.d4 r32, com.google.android.gms.internal.mlkit_vision_barcode_bundled.m3 r33, com.google.android.gms.internal.mlkit_vision_barcode_bundled.h5 r34, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2 r35, com.google.android.gms.internal.mlkit_vision_barcode_bundled.s3 r36) {
        /*
            Method dump skipped, instructions count: 996
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4.H(java.lang.Class, com.google.android.gms.internal.mlkit_vision_barcode_bundled.u3, com.google.android.gms.internal.mlkit_vision_barcode_bundled.d4, com.google.android.gms.internal.mlkit_vision_barcode_bundled.m3, com.google.android.gms.internal.mlkit_vision_barcode_bundled.h5, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2, com.google.android.gms.internal.mlkit_vision_barcode_bundled.s3):com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4");
    }

    private static double I(Object obj, long j8) {
        return ((Double) s5.k(obj, j8)).doubleValue();
    }

    private static float J(Object obj, long j8) {
        return ((Float) s5.k(obj, j8)).floatValue();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x020a, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0217, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0224, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0231, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x023e, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x024b, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x024d, code lost:
        r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1.A(r11 << 3) + com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1.A(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0091, code lost:
        if (C(r17, r11, r5) != false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0099, code lost:
        if (C(r17, r11, r5) != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a1, code lost:
        if (C(r17, r11, r5) != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0116, code lost:
        if (C(r17, r11, r5) != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x011d, code lost:
        if (C(r17, r11, r5) != false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0124, code lost:
        if (C(r17, r11, r5) != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0126, code lost:
        r4 = r11 << 3;
        r3 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1.w(L(r17, r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x013a, code lost:
        if (C(r17, r11, r5) != false) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0141, code lost:
        if (C(r17, r11, r5) != false) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0143, code lost:
        r3 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1.B(V(r17, r3));
        r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1.A(r11 << 3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0157, code lost:
        if (C(r17, r11, r5) != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0159, code lost:
        r3 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1.A(r11 << 3) + 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0167, code lost:
        if (C(r17, r11, r5) != false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0169, code lost:
        r3 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1.A(r11 << 3) + 8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x019a, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01a8, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01b6, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x01c4, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01d2, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01e0, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x01f0, code lost:
        if (r3 > 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x01fd, code lost:
        if (r3 > 0) goto L70;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final int K(java.lang.Object r17) {
        /*
            Method dump skipped, instructions count: 1324
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4.K(java.lang.Object):int");
    }

    private static int L(Object obj, long j8) {
        return ((Integer) s5.k(obj, j8)).intValue();
    }

    private final int M(Object obj, byte[] bArr, int i8, int i9, int i10, long j8, c1 c1Var) {
        Unsafe unsafe = q;
        Object l8 = l(i10);
        Object object = unsafe.getObject(obj, j8);
        if (!((zzfi) object).j()) {
            zzfi c9 = zzfi.b().c();
            s3.b(c9, object);
            unsafe.putObject(obj, j8, c9);
        }
        r3 r3Var = (r3) l8;
        throw null;
    }

    private final int N(Object obj, byte[] bArr, int i8, int i9, int i10, int i11, int i12, int i13, int i14, long j8, int i15, c1 c1Var) {
        Unsafe unsafe = q;
        long j9 = this.f14707a[i15 + 2] & 1048575;
        switch (i14) {
            case 51:
                if (i12 == 1) {
                    unsafe.putObject(obj, j8, Double.valueOf(Double.longBitsToDouble(d1.q(bArr, i8))));
                    int i16 = i8 + 8;
                    unsafe.putInt(obj, j9, i11);
                    return i16;
                }
                break;
            case 52:
                if (i12 == 5) {
                    unsafe.putObject(obj, j8, Float.valueOf(Float.intBitsToFloat(d1.b(bArr, i8))));
                    int i17 = i8 + 4;
                    unsafe.putInt(obj, j9, i11);
                    return i17;
                }
                break;
            case 53:
            case 54:
                if (i12 == 0) {
                    int m8 = d1.m(bArr, i8, c1Var);
                    unsafe.putObject(obj, j8, Long.valueOf(c1Var.f14736b));
                    unsafe.putInt(obj, j9, i11);
                    return m8;
                }
                break;
            case 55:
            case 62:
                if (i12 == 0) {
                    int j10 = d1.j(bArr, i8, c1Var);
                    unsafe.putObject(obj, j8, Integer.valueOf(c1Var.f14735a));
                    unsafe.putInt(obj, j9, i11);
                    return j10;
                }
                break;
            case 56:
            case 65:
                if (i12 == 1) {
                    unsafe.putObject(obj, j8, Long.valueOf(d1.q(bArr, i8)));
                    int i18 = i8 + 8;
                    unsafe.putInt(obj, j9, i11);
                    return i18;
                }
                break;
            case 57:
            case 64:
                if (i12 == 5) {
                    unsafe.putObject(obj, j8, Integer.valueOf(d1.b(bArr, i8)));
                    int i19 = i8 + 4;
                    unsafe.putInt(obj, j9, i11);
                    return i19;
                }
                break;
            case 58:
                if (i12 == 0) {
                    int m9 = d1.m(bArr, i8, c1Var);
                    unsafe.putObject(obj, j8, Boolean.valueOf(c1Var.f14736b != 0));
                    unsafe.putInt(obj, j9, i11);
                    return m9;
                }
                break;
            case 59:
                if (i12 == 2) {
                    int j11 = d1.j(bArr, i8, c1Var);
                    int i20 = c1Var.f14735a;
                    if (i20 == 0) {
                        unsafe.putObject(obj, j8, BuildConfig.FLAVOR);
                    } else if ((i13 & 536870912) != 0 && !x5.h(bArr, j11, j11 + i20)) {
                        throw zzeo.c();
                    } else {
                        unsafe.putObject(obj, j8, new String(bArr, j11, i20, y2.f14886b));
                        j11 += i20;
                    }
                    unsafe.putInt(obj, j9, i11);
                    return j11;
                }
                break;
            case 60:
                if (i12 == 2) {
                    Object n8 = n(obj, i11, i15);
                    int o5 = d1.o(n8, k(i15), bArr, i8, i9, c1Var);
                    v(obj, i11, i15, n8);
                    return o5;
                }
                break;
            case 61:
                if (i12 == 2) {
                    int a9 = d1.a(bArr, i8, c1Var);
                    unsafe.putObject(obj, j8, c1Var.f14737c);
                    unsafe.putInt(obj, j9, i11);
                    return a9;
                }
                break;
            case 63:
                if (i12 == 0) {
                    int j12 = d1.j(bArr, i8, c1Var);
                    int i21 = c1Var.f14735a;
                    t2 j13 = j(i15);
                    if (j13 == null || j13.c(i21)) {
                        unsafe.putObject(obj, j8, Integer.valueOf(i21));
                        unsafe.putInt(obj, j9, i11);
                    } else {
                        G(obj).j(i10, Long.valueOf(i21));
                    }
                    return j12;
                }
                break;
            case 66:
                if (i12 == 0) {
                    int j14 = d1.j(bArr, i8, c1Var);
                    unsafe.putObject(obj, j8, Integer.valueOf(t1.a(c1Var.f14735a)));
                    unsafe.putInt(obj, j9, i11);
                    return j14;
                }
                break;
            case 67:
                if (i12 == 0) {
                    int m10 = d1.m(bArr, i8, c1Var);
                    unsafe.putObject(obj, j8, Long.valueOf(t1.b(c1Var.f14736b)));
                    unsafe.putInt(obj, j9, i11);
                    return m10;
                }
                break;
            case 68:
                if (i12 == 3) {
                    Object n9 = n(obj, i11, i15);
                    int n10 = d1.n(n9, k(i15), bArr, i8, i9, (i10 & (-8)) | 4, c1Var);
                    v(obj, i11, i15, n9);
                    return n10;
                }
                break;
        }
        return i8;
    }

    /* JADX WARN: Code restructure failed: missing block: B:163:0x02a0, code lost:
        if (r30.f14736b != 0) goto L191;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x02a2, code lost:
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x02a4, code lost:
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x02a5, code lost:
        r13.g(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x02a8, code lost:
        if (r1 >= r20) goto L198;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x02aa, code lost:
        r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.j(r18, r1, r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x02b0, code lost:
        if (r21 == r30.f14735a) goto L196;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x02b3, code lost:
        r1 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.m(r18, r4, r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x02bb, code lost:
        if (r30.f14736b == 0) goto L199;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x02be, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x016e, code lost:
        if (r4 == 0) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0170, code lost:
        r13.add(com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb.f14977b);
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0176, code lost:
        r13.add(com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb.F(r18, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x017e, code lost:
        if (r1 >= r20) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0180, code lost:
        r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.j(r18, r1, r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0186, code lost:
        if (r21 == r30.f14735a) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0189, code lost:
        r1 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.d1.j(r18, r4, r30);
        r4 = r30.f14735a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x018f, code lost:
        if (r4 < 0) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0193, code lost:
        if (r4 > (r18.length - r1)) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0195, code lost:
        if (r4 != 0) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x019c, code lost:
        throw com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzeo.g();
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01a1, code lost:
        throw com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzeo.d();
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01a2, code lost:
        return r1;
     */
    /* JADX WARN: Removed duplicated region for block: B:112:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:306:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:308:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:110:0x01e8 -> B:111:0x01ec). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:116:0x01fe -> B:108:0x01dd). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:130:0x0230 -> B:131:0x0234). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:136:0x0246 -> B:126:0x021d). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:165:0x02a4 -> B:166:0x02a5). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:172:0x02bb -> B:164:0x02a2). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:79:0x0176 -> B:80:0x017e). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:88:0x0195 -> B:78:0x0170). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final int O(java.lang.Object r17, byte[] r18, int r19, int r20, int r21, int r22, int r23, int r24, long r25, int r27, long r28, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c1 r30) {
        /*
            Method dump skipped, instructions count: 1146
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4.O(java.lang.Object, byte[], int, int, int, int, int, int, long, int, long, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c1):int");
    }

    private final int P(int i8) {
        if (i8 < this.f14709c || i8 > this.f14710d) {
            return -1;
        }
        return S(i8, 0);
    }

    private final int Q(int i8, int i9) {
        if (i8 < this.f14709c || i8 > this.f14710d) {
            return -1;
        }
        return S(i8, i9);
    }

    private final int R(int i8) {
        return this.f14707a[i8 + 2];
    }

    private final int S(int i8, int i9) {
        int length = (this.f14707a.length / 3) - 1;
        while (i9 <= length) {
            int i10 = (length + i9) >>> 1;
            int i11 = i10 * 3;
            int i12 = this.f14707a[i11];
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

    private static int T(int i8) {
        return (i8 >>> 20) & 255;
    }

    private final int U(int i8) {
        return this.f14707a[i8 + 1];
    }

    private static long V(Object obj, long j8) {
        return ((Long) s5.k(obj, j8)).longValue();
    }

    private final t2 j(int i8) {
        int i9 = i8 / 3;
        return (t2) this.f14708b[i9 + i9 + 1];
    }

    private final r4 k(int i8) {
        int i9 = i8 / 3;
        int i10 = i9 + i9;
        r4 r4Var = (r4) this.f14708b[i10];
        if (r4Var != null) {
            return r4Var;
        }
        r4 b9 = g4.a().b((Class) this.f14708b[i10 + 1]);
        this.f14708b[i10] = b9;
        return b9;
    }

    private final Object l(int i8) {
        int i9 = i8 / 3;
        return this.f14708b[i9 + i9];
    }

    private final Object m(Object obj, int i8) {
        r4 k8 = k(i8);
        int U = U(i8) & 1048575;
        if (y(obj, i8)) {
            Object object = q.getObject(obj, U);
            if (B(object)) {
                return object;
            }
            Object d8 = k8.d();
            if (object != null) {
                k8.g(d8, object);
            }
            return d8;
        }
        return k8.d();
    }

    private final Object n(Object obj, int i8, int i9) {
        r4 k8 = k(i9);
        if (C(obj, i8, i9)) {
            Object object = q.getObject(obj, U(i9) & 1048575);
            if (B(object)) {
                return object;
            }
            Object d8 = k8.d();
            if (object != null) {
                k8.g(d8, object);
            }
            return d8;
        }
        return k8.d();
    }

    private static Field o(Class cls, String str) {
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

    private static void p(Object obj) {
        if (!B(obj)) {
            throw new IllegalArgumentException("Mutating immutable message: ".concat(String.valueOf(obj)));
        }
    }

    private final void q(Object obj, Object obj2, int i8) {
        if (y(obj2, i8)) {
            Unsafe unsafe = q;
            long U = U(i8) & 1048575;
            Object object = unsafe.getObject(obj2, U);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.f14707a[i8] + " is present but null: " + obj2.toString());
            }
            r4 k8 = k(i8);
            if (!y(obj, i8)) {
                if (B(object)) {
                    Object d8 = k8.d();
                    k8.g(d8, object);
                    unsafe.putObject(obj, U, d8);
                } else {
                    unsafe.putObject(obj, U, object);
                }
                s(obj, i8);
                return;
            }
            Object object2 = unsafe.getObject(obj, U);
            if (!B(object2)) {
                Object d9 = k8.d();
                k8.g(d9, object2);
                unsafe.putObject(obj, U, d9);
                object2 = d9;
            }
            k8.g(object2, object);
        }
    }

    private final void r(Object obj, Object obj2, int i8) {
        int i9 = this.f14707a[i8];
        if (C(obj2, i9, i8)) {
            Unsafe unsafe = q;
            long U = U(i8) & 1048575;
            Object object = unsafe.getObject(obj2, U);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.f14707a[i8] + " is present but null: " + obj2.toString());
            }
            r4 k8 = k(i8);
            if (!C(obj, i9, i8)) {
                if (B(object)) {
                    Object d8 = k8.d();
                    k8.g(d8, object);
                    unsafe.putObject(obj, U, d8);
                } else {
                    unsafe.putObject(obj, U, object);
                }
                t(obj, i9, i8);
                return;
            }
            Object object2 = unsafe.getObject(obj, U);
            if (!B(object2)) {
                Object d9 = k8.d();
                k8.g(d9, object2);
                unsafe.putObject(obj, U, d9);
                object2 = d9;
            }
            k8.g(object2, object);
        }
    }

    private final void s(Object obj, int i8) {
        int R = R(i8);
        long j8 = 1048575 & R;
        if (j8 == 1048575) {
            return;
        }
        s5.v(obj, j8, (1 << (R >>> 20)) | s5.h(obj, j8));
    }

    private final void t(Object obj, int i8, int i9) {
        s5.v(obj, R(i9) & 1048575, i8);
    }

    private final void u(Object obj, int i8, Object obj2) {
        q.putObject(obj, U(i8) & 1048575, obj2);
        s(obj, i8);
    }

    private final void v(Object obj, int i8, int i9, Object obj2) {
        q.putObject(obj, U(i9) & 1048575, obj2);
        t(obj, i8, i9);
    }

    private final void w(y5 y5Var, int i8, Object obj, int i9) {
        if (obj == null) {
            return;
        }
        r3 r3Var = (r3) l(i9);
        throw null;
    }

    private final boolean x(Object obj, Object obj2, int i8) {
        return y(obj, i8) == y(obj2, i8);
    }

    private final boolean y(Object obj, int i8) {
        int R = R(i8);
        long j8 = R & 1048575;
        if (j8 != 1048575) {
            return (s5.h(obj, j8) & (1 << (R >>> 20))) != 0;
        }
        int U = U(i8);
        long j9 = U & 1048575;
        switch (T(U)) {
            case 0:
                return Double.doubleToRawLongBits(s5.f(obj, j9)) != 0;
            case 1:
                return Float.floatToRawIntBits(s5.g(obj, j9)) != 0;
            case 2:
                return s5.i(obj, j9) != 0;
            case 3:
                return s5.i(obj, j9) != 0;
            case 4:
                return s5.h(obj, j9) != 0;
            case 5:
                return s5.i(obj, j9) != 0;
            case 6:
                return s5.h(obj, j9) != 0;
            case 7:
                return s5.B(obj, j9);
            case 8:
                Object k8 = s5.k(obj, j9);
                if (k8 instanceof String) {
                    return !((String) k8).isEmpty();
                } else if (k8 instanceof zzdb) {
                    return !zzdb.f14977b.equals(k8);
                } else {
                    throw new IllegalArgumentException();
                }
            case 9:
                return s5.k(obj, j9) != null;
            case 10:
                return !zzdb.f14977b.equals(s5.k(obj, j9));
            case 11:
                return s5.h(obj, j9) != 0;
            case 12:
                return s5.h(obj, j9) != 0;
            case 13:
                return s5.h(obj, j9) != 0;
            case 14:
                return s5.i(obj, j9) != 0;
            case 15:
                return s5.h(obj, j9) != 0;
            case 16:
                return s5.i(obj, j9) != 0;
            case 17:
                return s5.k(obj, j9) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean z(Object obj, int i8, int i9, int i10, int i11) {
        return i9 == 1048575 ? y(obj, i8) : (i10 & i11) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0543, code lost:
        if (r6 == r1) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x0545, code lost:
        r32.putInt(r12, r6, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x054b, code lost:
        r2 = r9.f14714h;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x054f, code lost:
        if (r2 >= r9.f14715i) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x0551, code lost:
        r4 = r9.f14713g[r2];
        r5 = r9.f14707a[r4];
        r5 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.s5.k(r12, r9.U(r4) & r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x0563, code lost:
        if (r5 != null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x056a, code lost:
        if (r9.j(r4) != null) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x056c, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x056f, code lost:
        r5 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzfi) r5;
        r0 = (com.google.android.gms.internal.mlkit_vision_barcode_bundled.r3) r9.l(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x0577, code lost:
        throw null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x057a, code lost:
        if (r7 != 0) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x057c, code lost:
        if (r0 != r38) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x0583, code lost:
        throw com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzeo.e();
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0584, code lost:
        if (r0 > r38) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0586, code lost:
        if (r3 != r7) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0588, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x058d, code lost:
        throw com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzeo.e();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int F(java.lang.Object r35, byte[] r36, int r37, int r38, int r39, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c1 r40) {
        /*
            Method dump skipped, instructions count: 1500
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4.F(java.lang.Object, byte[], int, int, int, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c1):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:163:0x031f, code lost:
        if ((r4 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb) != false) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0322, code lost:
        r5 = r6 << 3;
        r4 = com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1.z((java.lang.String) r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x009f, code lost:
        if ((r4 instanceof com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb) != false) goto L54;
     */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int a(java.lang.Object r12) {
        /*
            Method dump skipped, instructions count: 1088
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4.a(java.lang.Object):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00cf, code lost:
        if (r3 != null) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00e1, code lost:
        if (r3 != null) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00e3, code lost:
        r7 = r3.hashCode();
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00e7, code lost:
        r2 = (r2 * 53) + r7;
     */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int b(java.lang.Object r10) {
        /*
            Method dump skipped, instructions count: 480
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4.b(java.lang.Object):int");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final void c(Object obj) {
        int i8;
        if (B(obj)) {
            if (obj instanceof p2) {
                p2 p2Var = (p2) obj;
                p2Var.C(Integer.MAX_VALUE);
                p2Var.zzb = 0;
                p2Var.A();
            }
            int length = this.f14707a.length;
            while (i8 < length) {
                int U = U(i8);
                int i9 = 1048575 & U;
                int T = T(U);
                long j8 = i9;
                if (T != 9) {
                    if (T != 60 && T != 68) {
                        switch (T) {
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
                                this.f14716j.a(obj, j8);
                                break;
                            case 50:
                                Unsafe unsafe = q;
                                Object object = unsafe.getObject(obj, j8);
                                if (object == null) {
                                    break;
                                } else {
                                    ((zzfi) object).d();
                                    unsafe.putObject(obj, j8, object);
                                    break;
                                }
                        }
                    } else {
                        if (!C(obj, this.f14707a[i8], i8)) {
                        }
                        k(i8).c(q.getObject(obj, j8));
                    }
                }
                i8 = y(obj, i8) ? 0 : i8 + 3;
                k(i8).c(q.getObject(obj, j8));
            }
            this.f14717k.g(obj);
            if (this.f14712f) {
                this.f14718l.e(obj);
            }
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final Object d() {
        return ((p2) this.f14711e).o();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0500  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x0991  */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void e(java.lang.Object r18, com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5 r19) {
        /*
            Method dump skipped, instructions count: 2754
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4.e(java.lang.Object, com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5):void");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final boolean f(Object obj) {
        int i8;
        int i9;
        int i10 = 1048575;
        int i11 = 0;
        int i12 = 0;
        while (i12 < this.f14714h) {
            int i13 = this.f14713g[i12];
            int i14 = this.f14707a[i13];
            int U = U(i13);
            int i15 = this.f14707a[i13 + 2];
            int i16 = i15 & 1048575;
            int i17 = 1 << (i15 >>> 20);
            if (i16 != i10) {
                if (i16 != 1048575) {
                    i11 = q.getInt(obj, i16);
                }
                i9 = i11;
                i8 = i16;
            } else {
                i8 = i10;
                i9 = i11;
            }
            if ((268435456 & U) != 0 && !z(obj, i13, i8, i9, i17)) {
                return false;
            }
            int T = T(U);
            if (T != 9 && T != 17) {
                if (T != 27) {
                    if (T == 60 || T == 68) {
                        if (C(obj, i14, i13) && !A(obj, U, k(i13))) {
                            return false;
                        }
                    } else if (T != 49) {
                        if (T == 50 && !((zzfi) s5.k(obj, U & 1048575)).isEmpty()) {
                            r3 r3Var = (r3) l(i13);
                            throw null;
                        }
                    }
                }
                List list = (List) s5.k(obj, U & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    r4 k8 = k(i13);
                    for (int i18 = 0; i18 < list.size(); i18++) {
                        if (!k8.f(list.get(i18))) {
                            return false;
                        }
                    }
                    continue;
                }
            } else if (z(obj, i13, i8, i9, i17) && !A(obj, U, k(i13))) {
                return false;
            }
            i12++;
            i10 = i8;
            i11 = i9;
        }
        return !this.f14712f || this.f14718l.b(obj).k();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final void g(Object obj, Object obj2) {
        p(obj);
        Objects.requireNonNull(obj2);
        for (int i8 = 0; i8 < this.f14707a.length; i8 += 3) {
            int U = U(i8);
            int i9 = this.f14707a[i8];
            long j8 = 1048575 & U;
            switch (T(U)) {
                case 0:
                    if (y(obj2, i8)) {
                        s5.t(obj, j8, s5.f(obj2, j8));
                        s(obj, i8);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (y(obj2, i8)) {
                        s5.u(obj, j8, s5.g(obj2, j8));
                        s(obj, i8);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.w(obj, j8, s5.i(obj2, j8));
                    s(obj, i8);
                    break;
                case 3:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.w(obj, j8, s5.i(obj2, j8));
                    s(obj, i8);
                    break;
                case 4:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.v(obj, j8, s5.h(obj2, j8));
                    s(obj, i8);
                    break;
                case 5:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.w(obj, j8, s5.i(obj2, j8));
                    s(obj, i8);
                    break;
                case 6:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.v(obj, j8, s5.h(obj2, j8));
                    s(obj, i8);
                    break;
                case 7:
                    if (y(obj2, i8)) {
                        s5.r(obj, j8, s5.B(obj2, j8));
                        s(obj, i8);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.x(obj, j8, s5.k(obj2, j8));
                    s(obj, i8);
                    break;
                case 9:
                case 17:
                    q(obj, obj2, i8);
                    break;
                case 10:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.x(obj, j8, s5.k(obj2, j8));
                    s(obj, i8);
                    break;
                case 11:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.v(obj, j8, s5.h(obj2, j8));
                    s(obj, i8);
                    break;
                case 12:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.v(obj, j8, s5.h(obj2, j8));
                    s(obj, i8);
                    break;
                case 13:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.v(obj, j8, s5.h(obj2, j8));
                    s(obj, i8);
                    break;
                case 14:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.w(obj, j8, s5.i(obj2, j8));
                    s(obj, i8);
                    break;
                case 15:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.v(obj, j8, s5.h(obj2, j8));
                    s(obj, i8);
                    break;
                case 16:
                    if (!y(obj2, i8)) {
                        break;
                    }
                    s5.w(obj, j8, s5.i(obj2, j8));
                    s(obj, i8);
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
                    this.f14716j.b(obj, obj2, j8);
                    break;
                case 50:
                    int i10 = t4.f14864d;
                    s5.x(obj, j8, s3.b(s5.k(obj, j8), s5.k(obj2, j8)));
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
                    if (!C(obj2, i9, i8)) {
                        break;
                    }
                    s5.x(obj, j8, s5.k(obj2, j8));
                    t(obj, i9, i8);
                    break;
                case 60:
                case 68:
                    r(obj, obj2, i8);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (!C(obj2, i9, i8)) {
                        break;
                    }
                    s5.x(obj, j8, s5.k(obj2, j8));
                    t(obj, i9, i8);
                    break;
            }
        }
        t4.c(this.f14717k, obj, obj2);
        if (this.f14712f) {
            t4.b(this.f14718l, obj, obj2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x029e, code lost:
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x02cb, code lost:
        if (r0 != r15) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x02ed, code lost:
        if (r0 != r15) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0286, code lost:
        if (r0 != r5) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0288, code lost:
        r15 = r30;
        r14 = r31;
        r12 = r32;
        r13 = r34;
        r11 = r35;
        r9 = r18;
        r1 = r19;
        r2 = r22;
        r6 = r25;
        r7 = r26;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v12, types: [int] */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void h(java.lang.Object r31, byte[] r32, int r33, int r34, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c1 r35) {
        /*
            Method dump skipped, instructions count: 876
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.a4.h(java.lang.Object, byte[], int, int, com.google.android.gms.internal.mlkit_vision_barcode_bundled.c1):void");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r4
    public final boolean i(Object obj, Object obj2) {
        int length = this.f14707a.length;
        for (int i8 = 0; i8 < length; i8 += 3) {
            int U = U(i8);
            long j8 = U & 1048575;
            switch (T(U)) {
                case 0:
                    if (x(obj, obj2, i8) && Double.doubleToLongBits(s5.f(obj, j8)) == Double.doubleToLongBits(s5.f(obj2, j8))) {
                        break;
                    }
                    return false;
                case 1:
                    if (x(obj, obj2, i8) && Float.floatToIntBits(s5.g(obj, j8)) == Float.floatToIntBits(s5.g(obj2, j8))) {
                        break;
                    }
                    return false;
                case 2:
                    if (x(obj, obj2, i8) && s5.i(obj, j8) == s5.i(obj2, j8)) {
                        break;
                    }
                    return false;
                case 3:
                    if (x(obj, obj2, i8) && s5.i(obj, j8) == s5.i(obj2, j8)) {
                        break;
                    }
                    return false;
                case 4:
                    if (x(obj, obj2, i8) && s5.h(obj, j8) == s5.h(obj2, j8)) {
                        break;
                    }
                    return false;
                case 5:
                    if (x(obj, obj2, i8) && s5.i(obj, j8) == s5.i(obj2, j8)) {
                        break;
                    }
                    return false;
                case 6:
                    if (x(obj, obj2, i8) && s5.h(obj, j8) == s5.h(obj2, j8)) {
                        break;
                    }
                    return false;
                case 7:
                    if (x(obj, obj2, i8) && s5.B(obj, j8) == s5.B(obj2, j8)) {
                        break;
                    }
                    return false;
                case 8:
                    if (x(obj, obj2, i8) && t4.w(s5.k(obj, j8), s5.k(obj2, j8))) {
                        break;
                    }
                    return false;
                case 9:
                    if (x(obj, obj2, i8) && t4.w(s5.k(obj, j8), s5.k(obj2, j8))) {
                        break;
                    }
                    return false;
                case 10:
                    if (x(obj, obj2, i8) && t4.w(s5.k(obj, j8), s5.k(obj2, j8))) {
                        break;
                    }
                    return false;
                case 11:
                    if (x(obj, obj2, i8) && s5.h(obj, j8) == s5.h(obj2, j8)) {
                        break;
                    }
                    return false;
                case 12:
                    if (x(obj, obj2, i8) && s5.h(obj, j8) == s5.h(obj2, j8)) {
                        break;
                    }
                    return false;
                case 13:
                    if (x(obj, obj2, i8) && s5.h(obj, j8) == s5.h(obj2, j8)) {
                        break;
                    }
                    return false;
                case 14:
                    if (x(obj, obj2, i8) && s5.i(obj, j8) == s5.i(obj2, j8)) {
                        break;
                    }
                    return false;
                case 15:
                    if (x(obj, obj2, i8) && s5.h(obj, j8) == s5.h(obj2, j8)) {
                        break;
                    }
                    return false;
                case 16:
                    if (x(obj, obj2, i8) && s5.i(obj, j8) == s5.i(obj2, j8)) {
                        break;
                    }
                    return false;
                case 17:
                    if (x(obj, obj2, i8) && t4.w(s5.k(obj, j8), s5.k(obj2, j8))) {
                        break;
                    }
                    return false;
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
                case 50:
                    if (t4.w(s5.k(obj, j8), s5.k(obj2, j8))) {
                        break;
                    } else {
                        return false;
                    }
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                    long R = R(i8) & 1048575;
                    if (s5.h(obj, R) == s5.h(obj2, R) && t4.w(s5.k(obj, j8), s5.k(obj2, j8))) {
                        break;
                    }
                    return false;
            }
        }
        if (this.f14717k.d(obj).equals(this.f14717k.d(obj2))) {
            if (this.f14712f) {
                return this.f14718l.b(obj).equals(this.f14718l.b(obj2));
            }
            return true;
        }
        return false;
    }
}
