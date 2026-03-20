package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n7 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static double a(byte[] bArr, int i8) {
        return Double.longBitsToDouble(r(bArr, i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(int i8, byte[] bArr, int i9, int i10, m7 m7Var) {
        if ((i8 >>> 3) != 0) {
            int i11 = i8 & 7;
            if (i11 != 0) {
                if (i11 != 1) {
                    if (i11 != 2) {
                        if (i11 != 3) {
                            if (i11 == 5) {
                                return i9 + 4;
                            }
                            throw zzkb.b();
                        }
                        int i12 = (i8 & (-8)) | 4;
                        int i13 = 0;
                        while (i9 < i10) {
                            i9 = p(bArr, i9, m7Var);
                            i13 = m7Var.f12344a;
                            if (i13 == i12) {
                                break;
                            }
                            i9 = b(i13, bArr, i9, i10, m7Var);
                        }
                        if (i9 > i10 || i13 != i12) {
                            throw zzkb.e();
                        }
                        return i9;
                    }
                    return p(bArr, i9, m7Var) + m7Var.f12344a;
                }
                return i9 + 8;
            }
            return q(bArr, i9, m7Var);
        }
        throw zzkb.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(int i8, byte[] bArr, int i9, int i10, g9<?> g9Var, m7 m7Var) {
        y8 y8Var = (y8) g9Var;
        int p8 = p(bArr, i9, m7Var);
        while (true) {
            y8Var.i(m7Var.f12344a);
            if (p8 >= i10) {
                break;
            }
            int p9 = p(bArr, p8, m7Var);
            if (i8 != m7Var.f12344a) {
                break;
            }
            p8 = p(bArr, p9, m7Var);
        }
        return p8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(int i8, byte[] bArr, int i9, int i10, vb vbVar, m7 m7Var) {
        if ((i8 >>> 3) != 0) {
            int i11 = i8 & 7;
            if (i11 == 0) {
                int q = q(bArr, i9, m7Var);
                vbVar.e(i8, Long.valueOf(m7Var.f12345b));
                return q;
            } else if (i11 == 1) {
                vbVar.e(i8, Long.valueOf(r(bArr, i9)));
                return i9 + 8;
            } else if (i11 == 2) {
                int p8 = p(bArr, i9, m7Var);
                int i12 = m7Var.f12344a;
                if (i12 >= 0) {
                    if (i12 <= bArr.length - p8) {
                        vbVar.e(i8, i12 == 0 ? zzij.f12852b : zzij.p(bArr, p8, i12));
                        return p8 + i12;
                    }
                    throw zzkb.f();
                }
                throw zzkb.d();
            } else if (i11 != 3) {
                if (i11 == 5) {
                    vbVar.e(i8, Integer.valueOf(o(bArr, i9)));
                    return i9 + 4;
                }
                throw zzkb.b();
            } else {
                vb l8 = vb.l();
                int i13 = (i8 & (-8)) | 4;
                int i14 = 0;
                while (true) {
                    if (i9 >= i10) {
                        break;
                    }
                    int p9 = p(bArr, i9, m7Var);
                    int i15 = m7Var.f12344a;
                    i14 = i15;
                    if (i15 == i13) {
                        i9 = p9;
                        break;
                    }
                    int d8 = d(i14, bArr, p9, i10, l8, m7Var);
                    i14 = i15;
                    i9 = d8;
                }
                if (i9 > i10 || i14 != i13) {
                    throw zzkb.e();
                }
                vbVar.e(i8, l8);
                return i9;
            }
        }
        throw zzkb.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int e(int i8, byte[] bArr, int i9, m7 m7Var) {
        int i10;
        int i11;
        int i12 = i8 & 127;
        int i13 = i9 + 1;
        byte b9 = bArr[i9];
        if (b9 < 0) {
            int i14 = i12 | ((b9 & Byte.MAX_VALUE) << 7);
            int i15 = i13 + 1;
            byte b10 = bArr[i13];
            if (b10 >= 0) {
                i10 = b10 << 14;
            } else {
                i12 = i14 | ((b10 & Byte.MAX_VALUE) << 14);
                i13 = i15 + 1;
                byte b11 = bArr[i15];
                if (b11 >= 0) {
                    i11 = b11 << 21;
                } else {
                    i14 = i12 | ((b11 & Byte.MAX_VALUE) << 21);
                    i15 = i13 + 1;
                    byte b12 = bArr[i13];
                    if (b12 >= 0) {
                        i10 = b12 << 28;
                    } else {
                        int i16 = i14 | ((b12 & Byte.MAX_VALUE) << 28);
                        while (true) {
                            int i17 = i15 + 1;
                            if (bArr[i15] >= 0) {
                                m7Var.f12344a = i16;
                                return i17;
                            }
                            i15 = i17;
                        }
                    }
                }
            }
            m7Var.f12344a = i14 | i10;
            return i15;
        }
        i11 = b9 << 7;
        m7Var.f12344a = i12 | i11;
        return i13;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int f(xa<?> xaVar, int i8, byte[] bArr, int i9, int i10, g9<?> g9Var, m7 m7Var) {
        int h8 = h(xaVar, bArr, i9, i10, m7Var);
        while (true) {
            g9Var.add(m7Var.f12346c);
            if (h8 >= i10) {
                break;
            }
            int p8 = p(bArr, h8, m7Var);
            if (i8 != m7Var.f12344a) {
                break;
            }
            h8 = h(xaVar, bArr, p8, i10, m7Var);
        }
        return h8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int g(xa xaVar, byte[] bArr, int i8, int i9, int i10, m7 m7Var) {
        Object zza = xaVar.zza();
        int i11 = i(zza, xaVar, bArr, i8, i9, i10, m7Var);
        xaVar.f(zza);
        m7Var.f12346c = zza;
        return i11;
    }

    static int h(xa xaVar, byte[] bArr, int i8, int i9, m7 m7Var) {
        Object zza = xaVar.zza();
        int j8 = j(zza, xaVar, bArr, i8, i9, m7Var);
        xaVar.f(zza);
        m7Var.f12346c = zza;
        return j8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int i(Object obj, xa xaVar, byte[] bArr, int i8, int i9, int i10, m7 m7Var) {
        int l8 = ((la) xaVar).l(obj, bArr, i8, i9, i10, m7Var);
        m7Var.f12346c = obj;
        return l8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int j(Object obj, xa xaVar, byte[] bArr, int i8, int i9, m7 m7Var) {
        int i10 = i8 + 1;
        int i11 = bArr[i8];
        if (i11 < 0) {
            i10 = e(i11, bArr, i10, m7Var);
            i11 = m7Var.f12344a;
        }
        int i12 = i10;
        if (i11 < 0 || i11 > i9 - i12) {
            throw zzkb.f();
        }
        int i13 = i11 + i12;
        xaVar.c(obj, bArr, i12, i13, m7Var);
        m7Var.f12346c = obj;
        return i13;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int k(byte[] bArr, int i8, m7 m7Var) {
        int p8 = p(bArr, i8, m7Var);
        int i9 = m7Var.f12344a;
        if (i9 >= 0) {
            if (i9 <= bArr.length - p8) {
                if (i9 == 0) {
                    m7Var.f12346c = zzij.f12852b;
                    return p8;
                }
                m7Var.f12346c = zzij.p(bArr, p8, i9);
                return p8 + i9;
            }
            throw zzkb.f();
        }
        throw zzkb.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int l(byte[] bArr, int i8, g9<?> g9Var, m7 m7Var) {
        y8 y8Var = (y8) g9Var;
        int p8 = p(bArr, i8, m7Var);
        int i9 = m7Var.f12344a + p8;
        while (p8 < i9) {
            p8 = p(bArr, p8, m7Var);
            y8Var.i(m7Var.f12344a);
        }
        if (p8 == i9) {
            return p8;
        }
        throw zzkb.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float m(byte[] bArr, int i8) {
        return Float.intBitsToFloat(o(bArr, i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int n(byte[] bArr, int i8, m7 m7Var) {
        int p8 = p(bArr, i8, m7Var);
        int i9 = m7Var.f12344a;
        if (i9 >= 0) {
            if (i9 == 0) {
                m7Var.f12346c = BuildConfig.FLAVOR;
                return p8;
            }
            m7Var.f12346c = dc.e(bArr, p8, i9);
            return p8 + i9;
        }
        throw zzkb.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int o(byte[] bArr, int i8) {
        return ((bArr[i8 + 3] & 255) << 24) | (bArr[i8] & 255) | ((bArr[i8 + 1] & 255) << 8) | ((bArr[i8 + 2] & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int p(byte[] bArr, int i8, m7 m7Var) {
        int i9 = i8 + 1;
        byte b9 = bArr[i8];
        if (b9 >= 0) {
            m7Var.f12344a = b9;
            return i9;
        }
        return e(b9, bArr, i9, m7Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int q(byte[] bArr, int i8, m7 m7Var) {
        byte b9;
        int i9 = i8 + 1;
        long j8 = bArr[i8];
        if (j8 >= 0) {
            m7Var.f12345b = j8;
            return i9;
        }
        int i10 = i9 + 1;
        byte b10 = bArr[i9];
        long j9 = (j8 & 127) | ((b10 & Byte.MAX_VALUE) << 7);
        int i11 = 7;
        while (b10 < 0) {
            int i12 = i10 + 1;
            i11 += 7;
            j9 |= (b9 & Byte.MAX_VALUE) << i11;
            b10 = bArr[i10];
            i10 = i12;
        }
        m7Var.f12345b = j9;
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long r(byte[] bArr, int i8) {
        return ((bArr[i8 + 7] & 255) << 56) | (bArr[i8] & 255) | ((bArr[i8 + 1] & 255) << 8) | ((bArr[i8 + 2] & 255) << 16) | ((bArr[i8 + 3] & 255) << 24) | ((bArr[i8 + 4] & 255) << 32) | ((bArr[i8 + 5] & 255) << 40) | ((bArr[i8 + 6] & 255) << 48);
    }
}
