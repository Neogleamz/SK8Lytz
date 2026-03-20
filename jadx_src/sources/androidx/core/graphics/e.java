package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import com.example.seedpoint.R;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        int f4740a;

        /* renamed from: b  reason: collision with root package name */
        boolean f4741b;

        a() {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        public char f4742a;

        /* renamed from: b  reason: collision with root package name */
        public float[] f4743b;

        b(char c9, float[] fArr) {
            this.f4742a = c9;
            this.f4743b = fArr;
        }

        b(b bVar) {
            this.f4742a = bVar.f4742a;
            float[] fArr = bVar.f4743b;
            this.f4743b = e.c(fArr, 0, fArr.length);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        private static void a(Path path, float[] fArr, char c9, char c10, float[] fArr2) {
            int i8;
            int i9;
            int i10;
            float f5;
            float f8;
            float f9;
            float f10;
            float f11;
            float f12;
            float f13;
            float f14;
            char c11 = c10;
            char c12 = 0;
            float f15 = fArr[0];
            float f16 = fArr[1];
            float f17 = fArr[2];
            float f18 = fArr[3];
            float f19 = fArr[4];
            float f20 = fArr[5];
            switch (c11) {
                case 'A':
                case 'a':
                    i8 = 7;
                    i9 = i8;
                    break;
                case 'C':
                case 'c':
                    i8 = 6;
                    i9 = i8;
                    break;
                case 'H':
                case 'V':
                case 'h':
                case 'v':
                    i9 = 1;
                    break;
                case 'L':
                case 'M':
                case 'T':
                case 'l':
                case R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu /* 109 */:
                case 't':
                default:
                    i9 = 2;
                    break;
                case 'Q':
                case 'S':
                case R.styleable.AppCompatTheme_toolbarStyle /* 113 */:
                case R.styleable.AppCompatTheme_tooltipFrameBackground /* 115 */:
                    i9 = 4;
                    break;
                case 'Z':
                case 'z':
                    path.close();
                    path.moveTo(f19, f20);
                    f15 = f19;
                    f17 = f15;
                    f16 = f20;
                    f18 = f16;
                    i9 = 2;
                    break;
            }
            float f21 = f15;
            float f22 = f16;
            float f23 = f19;
            float f24 = f20;
            int i11 = 0;
            char c13 = c9;
            while (i11 < fArr2.length) {
                if (c11 != 'A') {
                    if (c11 == 'C') {
                        i10 = i11;
                        int i12 = i10 + 2;
                        int i13 = i10 + 3;
                        int i14 = i10 + 4;
                        int i15 = i10 + 5;
                        path.cubicTo(fArr2[i10 + 0], fArr2[i10 + 1], fArr2[i12], fArr2[i13], fArr2[i14], fArr2[i15]);
                        f21 = fArr2[i14];
                        float f25 = fArr2[i15];
                        float f26 = fArr2[i12];
                        float f27 = fArr2[i13];
                        f22 = f25;
                        f18 = f27;
                        f17 = f26;
                    } else if (c11 == 'H') {
                        i10 = i11;
                        int i16 = i10 + 0;
                        path.lineTo(fArr2[i16], f22);
                        f21 = fArr2[i16];
                    } else if (c11 == 'Q') {
                        i10 = i11;
                        int i17 = i10 + 0;
                        int i18 = i10 + 1;
                        int i19 = i10 + 2;
                        int i20 = i10 + 3;
                        path.quadTo(fArr2[i17], fArr2[i18], fArr2[i19], fArr2[i20]);
                        float f28 = fArr2[i17];
                        float f29 = fArr2[i18];
                        f21 = fArr2[i19];
                        f22 = fArr2[i20];
                        f17 = f28;
                        f18 = f29;
                    } else if (c11 == 'V') {
                        i10 = i11;
                        int i21 = i10 + 0;
                        path.lineTo(f21, fArr2[i21]);
                        f22 = fArr2[i21];
                    } else if (c11 != 'a') {
                        if (c11 != 'c') {
                            if (c11 == 'h') {
                                int i22 = i11 + 0;
                                path.rLineTo(fArr2[i22], 0.0f);
                                f21 += fArr2[i22];
                            } else if (c11 != 'q') {
                                if (c11 == 'v') {
                                    int i23 = i11 + 0;
                                    path.rLineTo(0.0f, fArr2[i23]);
                                    f10 = fArr2[i23];
                                } else if (c11 == 'L') {
                                    int i24 = i11 + 0;
                                    int i25 = i11 + 1;
                                    path.lineTo(fArr2[i24], fArr2[i25]);
                                    f21 = fArr2[i24];
                                    f22 = fArr2[i25];
                                } else if (c11 == 'M') {
                                    int i26 = i11 + 0;
                                    f21 = fArr2[i26];
                                    int i27 = i11 + 1;
                                    f22 = fArr2[i27];
                                    if (i11 > 0) {
                                        path.lineTo(fArr2[i26], fArr2[i27]);
                                    } else {
                                        path.moveTo(fArr2[i26], fArr2[i27]);
                                        i10 = i11;
                                        f24 = f22;
                                        f23 = f21;
                                    }
                                } else if (c11 == 'S') {
                                    if (c13 == 'c' || c13 == 's' || c13 == 'C' || c13 == 'S') {
                                        f21 = (f21 * 2.0f) - f17;
                                        f22 = (f22 * 2.0f) - f18;
                                    }
                                    float f30 = f22;
                                    int i28 = i11 + 0;
                                    int i29 = i11 + 1;
                                    int i30 = i11 + 2;
                                    int i31 = i11 + 3;
                                    path.cubicTo(f21, f30, fArr2[i28], fArr2[i29], fArr2[i30], fArr2[i31]);
                                    f5 = fArr2[i28];
                                    f8 = fArr2[i29];
                                    f21 = fArr2[i30];
                                    f22 = fArr2[i31];
                                    f17 = f5;
                                    f18 = f8;
                                } else if (c11 == 'T') {
                                    if (c13 == 'q' || c13 == 't' || c13 == 'Q' || c13 == 'T') {
                                        f21 = (f21 * 2.0f) - f17;
                                        f22 = (f22 * 2.0f) - f18;
                                    }
                                    int i32 = i11 + 0;
                                    int i33 = i11 + 1;
                                    path.quadTo(f21, f22, fArr2[i32], fArr2[i33]);
                                    float f31 = fArr2[i32];
                                    float f32 = fArr2[i33];
                                    i10 = i11;
                                    f18 = f22;
                                    f17 = f21;
                                    f21 = f31;
                                    f22 = f32;
                                } else if (c11 == 'l') {
                                    int i34 = i11 + 0;
                                    int i35 = i11 + 1;
                                    path.rLineTo(fArr2[i34], fArr2[i35]);
                                    f21 += fArr2[i34];
                                    f10 = fArr2[i35];
                                } else if (c11 == 'm') {
                                    int i36 = i11 + 0;
                                    f21 += fArr2[i36];
                                    int i37 = i11 + 1;
                                    f22 += fArr2[i37];
                                    if (i11 > 0) {
                                        path.rLineTo(fArr2[i36], fArr2[i37]);
                                    } else {
                                        path.rMoveTo(fArr2[i36], fArr2[i37]);
                                        i10 = i11;
                                        f24 = f22;
                                        f23 = f21;
                                    }
                                } else if (c11 == 's') {
                                    if (c13 == 'c' || c13 == 's' || c13 == 'C' || c13 == 'S') {
                                        float f33 = f21 - f17;
                                        f11 = f22 - f18;
                                        f12 = f33;
                                    } else {
                                        f12 = 0.0f;
                                        f11 = 0.0f;
                                    }
                                    int i38 = i11 + 0;
                                    int i39 = i11 + 1;
                                    int i40 = i11 + 2;
                                    int i41 = i11 + 3;
                                    path.rCubicTo(f12, f11, fArr2[i38], fArr2[i39], fArr2[i40], fArr2[i41]);
                                    f5 = fArr2[i38] + f21;
                                    f8 = fArr2[i39] + f22;
                                    f21 += fArr2[i40];
                                    f9 = fArr2[i41];
                                } else if (c11 == 't') {
                                    if (c13 == 'q' || c13 == 't' || c13 == 'Q' || c13 == 'T') {
                                        f13 = f21 - f17;
                                        f14 = f22 - f18;
                                    } else {
                                        f14 = 0.0f;
                                        f13 = 0.0f;
                                    }
                                    int i42 = i11 + 0;
                                    int i43 = i11 + 1;
                                    path.rQuadTo(f13, f14, fArr2[i42], fArr2[i43]);
                                    float f34 = f13 + f21;
                                    float f35 = f14 + f22;
                                    f21 += fArr2[i42];
                                    f22 += fArr2[i43];
                                    f18 = f35;
                                    f17 = f34;
                                }
                                f22 += f10;
                            } else {
                                int i44 = i11 + 0;
                                int i45 = i11 + 1;
                                int i46 = i11 + 2;
                                int i47 = i11 + 3;
                                path.rQuadTo(fArr2[i44], fArr2[i45], fArr2[i46], fArr2[i47]);
                                f5 = fArr2[i44] + f21;
                                f8 = fArr2[i45] + f22;
                                f21 += fArr2[i46];
                                f9 = fArr2[i47];
                            }
                            i10 = i11;
                        } else {
                            int i48 = i11 + 2;
                            int i49 = i11 + 3;
                            int i50 = i11 + 4;
                            int i51 = i11 + 5;
                            path.rCubicTo(fArr2[i11 + 0], fArr2[i11 + 1], fArr2[i48], fArr2[i49], fArr2[i50], fArr2[i51]);
                            f5 = fArr2[i48] + f21;
                            f8 = fArr2[i49] + f22;
                            f21 += fArr2[i50];
                            f9 = fArr2[i51];
                        }
                        f22 += f9;
                        f17 = f5;
                        f18 = f8;
                        i10 = i11;
                    } else {
                        int i52 = i11 + 5;
                        int i53 = i11 + 6;
                        i10 = i11;
                        c(path, f21, f22, fArr2[i52] + f21, fArr2[i53] + f22, fArr2[i11 + 0], fArr2[i11 + 1], fArr2[i11 + 2], fArr2[i11 + 3] != 0.0f, fArr2[i11 + 4] != 0.0f);
                        f21 += fArr2[i52];
                        f22 += fArr2[i53];
                    }
                    i11 = i10 + i9;
                    c13 = c10;
                    c11 = c13;
                    c12 = 0;
                } else {
                    i10 = i11;
                    int i54 = i10 + 5;
                    int i55 = i10 + 6;
                    c(path, f21, f22, fArr2[i54], fArr2[i55], fArr2[i10 + 0], fArr2[i10 + 1], fArr2[i10 + 2], fArr2[i10 + 3] != 0.0f, fArr2[i10 + 4] != 0.0f);
                    f21 = fArr2[i54];
                    f22 = fArr2[i55];
                }
                f18 = f22;
                f17 = f21;
                i11 = i10 + i9;
                c13 = c10;
                c11 = c13;
                c12 = 0;
            }
            fArr[c12] = f21;
            fArr[1] = f22;
            fArr[2] = f17;
            fArr[3] = f18;
            fArr[4] = f23;
            fArr[5] = f24;
        }

        private static void b(Path path, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15, double d16) {
            double d17 = d10;
            int ceil = (int) Math.ceil(Math.abs((d16 * 4.0d) / 3.141592653589793d));
            double cos = Math.cos(d14);
            double sin = Math.sin(d14);
            double cos2 = Math.cos(d15);
            double sin2 = Math.sin(d15);
            double d18 = -d17;
            double d19 = d18 * cos;
            double d20 = d11 * sin;
            double d21 = (d19 * sin2) - (d20 * cos2);
            double d22 = d18 * sin;
            double d23 = d11 * cos;
            double d24 = (sin2 * d22) + (cos2 * d23);
            double d25 = d16 / ceil;
            double d26 = d24;
            double d27 = d21;
            int i8 = 0;
            double d28 = d12;
            double d29 = d13;
            double d30 = d15;
            while (i8 < ceil) {
                double d31 = d30 + d25;
                double sin3 = Math.sin(d31);
                double cos3 = Math.cos(d31);
                double d32 = (d8 + ((d17 * cos) * cos3)) - (d20 * sin3);
                double d33 = d9 + (d17 * sin * cos3) + (d23 * sin3);
                double d34 = (d19 * sin3) - (d20 * cos3);
                double d35 = (sin3 * d22) + (cos3 * d23);
                double d36 = d31 - d30;
                double tan = Math.tan(d36 / 2.0d);
                double sin4 = (Math.sin(d36) * (Math.sqrt(((tan * 3.0d) * tan) + 4.0d) - 1.0d)) / 3.0d;
                double d37 = d28 + (d27 * sin4);
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float) d37, (float) (d29 + (d26 * sin4)), (float) (d32 - (sin4 * d34)), (float) (d33 - (sin4 * d35)), (float) d32, (float) d33);
                i8++;
                d25 = d25;
                sin = sin;
                d28 = d32;
                d22 = d22;
                cos = cos;
                d30 = d31;
                d26 = d35;
                d27 = d34;
                ceil = ceil;
                d29 = d33;
                d17 = d10;
            }
        }

        private static void c(Path path, float f5, float f8, float f9, float f10, float f11, float f12, float f13, boolean z4, boolean z8) {
            double d8;
            double d9;
            double radians = Math.toRadians(f13);
            double cos = Math.cos(radians);
            double sin = Math.sin(radians);
            double d10 = f5;
            double d11 = d10 * cos;
            double d12 = f8;
            double d13 = f11;
            double d14 = (d11 + (d12 * sin)) / d13;
            double d15 = f12;
            double d16 = (((-f5) * sin) + (d12 * cos)) / d15;
            double d17 = f10;
            double d18 = ((f9 * cos) + (d17 * sin)) / d13;
            double d19 = (((-f9) * sin) + (d17 * cos)) / d15;
            double d20 = d14 - d18;
            double d21 = d16 - d19;
            double d22 = (d14 + d18) / 2.0d;
            double d23 = (d16 + d19) / 2.0d;
            double d24 = (d20 * d20) + (d21 * d21);
            if (d24 == 0.0d) {
                Log.w("PathParser", " Points are coincident");
                return;
            }
            double d25 = (1.0d / d24) - 0.25d;
            if (d25 < 0.0d) {
                Log.w("PathParser", "Points are too far apart " + d24);
                float sqrt = (float) (Math.sqrt(d24) / 1.99999d);
                c(path, f5, f8, f9, f10, f11 * sqrt, f12 * sqrt, f13, z4, z8);
                return;
            }
            double sqrt2 = Math.sqrt(d25);
            double d26 = d20 * sqrt2;
            double d27 = sqrt2 * d21;
            if (z4 == z8) {
                d8 = d22 - d27;
                d9 = d23 + d26;
            } else {
                d8 = d22 + d27;
                d9 = d23 - d26;
            }
            double atan2 = Math.atan2(d16 - d9, d14 - d8);
            double atan22 = Math.atan2(d19 - d9, d18 - d8) - atan2;
            int i8 = (atan22 > 0.0d ? 1 : (atan22 == 0.0d ? 0 : -1));
            if (z8 != (i8 >= 0)) {
                atan22 = i8 > 0 ? atan22 - 6.283185307179586d : atan22 + 6.283185307179586d;
            }
            double d28 = d8 * d13;
            double d29 = d9 * d15;
            b(path, (d28 * cos) - (d29 * sin), (d28 * sin) + (d29 * cos), d13, d15, d10, d12, radians, atan2, atan22);
        }

        public static void e(b[] bVarArr, Path path) {
            float[] fArr = new float[6];
            char c9 = 'm';
            for (int i8 = 0; i8 < bVarArr.length; i8++) {
                a(path, fArr, c9, bVarArr[i8].f4742a, bVarArr[i8].f4743b);
                c9 = bVarArr[i8].f4742a;
            }
        }

        public void d(b bVar, b bVar2, float f5) {
            this.f4742a = bVar.f4742a;
            int i8 = 0;
            while (true) {
                float[] fArr = bVar.f4743b;
                if (i8 >= fArr.length) {
                    return;
                }
                this.f4743b[i8] = (fArr[i8] * (1.0f - f5)) + (bVar2.f4743b[i8] * f5);
                i8++;
            }
        }
    }

    private static void a(ArrayList<b> arrayList, char c9, float[] fArr) {
        arrayList.add(new b(c9, fArr));
    }

    public static boolean b(b[] bVarArr, b[] bVarArr2) {
        if (bVarArr == null || bVarArr2 == null || bVarArr.length != bVarArr2.length) {
            return false;
        }
        for (int i8 = 0; i8 < bVarArr.length; i8++) {
            if (bVarArr[i8].f4742a != bVarArr2[i8].f4742a || bVarArr[i8].f4743b.length != bVarArr2[i8].f4743b.length) {
                return false;
            }
        }
        return true;
    }

    static float[] c(float[] fArr, int i8, int i9) {
        if (i8 <= i9) {
            int length = fArr.length;
            if (i8 < 0 || i8 > length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int i10 = i9 - i8;
            int min = Math.min(i10, length - i8);
            float[] fArr2 = new float[i10];
            System.arraycopy(fArr, i8, fArr2, 0, min);
            return fArr2;
        }
        throw new IllegalArgumentException();
    }

    public static b[] d(String str) {
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int i8 = 1;
        int i9 = 0;
        while (i8 < str.length()) {
            int i10 = i(str, i8);
            String trim = str.substring(i9, i10).trim();
            if (trim.length() > 0) {
                a(arrayList, trim.charAt(0), h(trim));
            }
            i9 = i10;
            i8 = i10 + 1;
        }
        if (i8 - i9 == 1 && i9 < str.length()) {
            a(arrayList, str.charAt(i9), new float[0]);
        }
        return (b[]) arrayList.toArray(new b[arrayList.size()]);
    }

    public static Path e(String str) {
        Path path = new Path();
        b[] d8 = d(str);
        if (d8 != null) {
            try {
                b.e(d8, path);
                return path;
            } catch (RuntimeException e8) {
                throw new RuntimeException("Error in parsing " + str, e8);
            }
        }
        return null;
    }

    public static b[] f(b[] bVarArr) {
        if (bVarArr == null) {
            return null;
        }
        b[] bVarArr2 = new b[bVarArr.length];
        for (int i8 = 0; i8 < bVarArr.length; i8++) {
            bVarArr2[i8] = new b(bVarArr[i8]);
        }
        return bVarArr2;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x002c, code lost:
        if (r2 == false) goto L19;
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0038 A[LOOP:0: B:3:0x0007->B:24:0x0038, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x003b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void g(java.lang.String r8, int r9, androidx.core.graphics.e.a r10) {
        /*
            r0 = 0
            r10.f4741b = r0
            r1 = r9
            r2 = r0
            r3 = r2
            r4 = r3
        L7:
            int r5 = r8.length()
            if (r1 >= r5) goto L3b
            char r5 = r8.charAt(r1)
            r6 = 32
            r7 = 1
            if (r5 == r6) goto L33
            r6 = 69
            if (r5 == r6) goto L31
            r6 = 101(0x65, float:1.42E-43)
            if (r5 == r6) goto L31
            switch(r5) {
                case 44: goto L33;
                case 45: goto L2a;
                case 46: goto L22;
                default: goto L21;
            }
        L21:
            goto L2f
        L22:
            if (r3 != 0) goto L27
            r2 = r0
            r3 = r7
            goto L35
        L27:
            r10.f4741b = r7
            goto L33
        L2a:
            if (r1 == r9) goto L2f
            if (r2 != 0) goto L2f
            goto L27
        L2f:
            r2 = r0
            goto L35
        L31:
            r2 = r7
            goto L35
        L33:
            r2 = r0
            r4 = r7
        L35:
            if (r4 == 0) goto L38
            goto L3b
        L38:
            int r1 = r1 + 1
            goto L7
        L3b:
            r10.f4740a = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.e.g(java.lang.String, int, androidx.core.graphics.e$a):void");
    }

    private static float[] h(String str) {
        if (str.charAt(0) == 'z' || str.charAt(0) == 'Z') {
            return new float[0];
        }
        try {
            float[] fArr = new float[str.length()];
            a aVar = new a();
            int length = str.length();
            int i8 = 1;
            int i9 = 0;
            while (i8 < length) {
                g(str, i8, aVar);
                int i10 = aVar.f4740a;
                if (i8 < i10) {
                    fArr[i9] = Float.parseFloat(str.substring(i8, i10));
                    i9++;
                }
                i8 = aVar.f4741b ? i10 : i10 + 1;
            }
            return c(fArr, 0, i9);
        } catch (NumberFormatException e8) {
            throw new RuntimeException("error in parsing \"" + str + "\"", e8);
        }
    }

    private static int i(String str, int i8) {
        while (i8 < str.length()) {
            char charAt = str.charAt(i8);
            if (((charAt - 'A') * (charAt - 'Z') <= 0 || (charAt - 'a') * (charAt - 'z') <= 0) && charAt != 'e' && charAt != 'E') {
                return i8;
            }
            i8++;
        }
        return i8;
    }

    public static void j(b[] bVarArr, b[] bVarArr2) {
        for (int i8 = 0; i8 < bVarArr2.length; i8++) {
            bVarArr[i8].f4742a = bVarArr2[i8].f4742a;
            for (int i9 = 0; i9 < bVarArr2[i8].f4743b.length; i9++) {
                bVarArr[i8].f4743b[i9] = bVarArr2[i8].f4743b[i9];
            }
        }
    }
}
