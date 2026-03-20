package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(byte[] bArr, int i8, c1 c1Var) {
        int j8 = j(bArr, i8, c1Var);
        int i9 = c1Var.f14735a;
        if (i9 >= 0) {
            if (i9 <= bArr.length - j8) {
                if (i9 == 0) {
                    c1Var.f14737c = zzdb.f14977b;
                    return j8;
                }
                c1Var.f14737c = zzdb.F(bArr, j8, i9);
                return j8 + i9;
            }
            throw zzeo.g();
        }
        throw zzeo.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(byte[] bArr, int i8) {
        int i9 = (bArr[i8 + 1] & 255) << 8;
        return ((bArr[i8 + 3] & 255) << 24) | i9 | (bArr[i8] & 255) | ((bArr[i8 + 2] & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(r4 r4Var, byte[] bArr, int i8, int i9, int i10, c1 c1Var) {
        Object d8 = r4Var.d();
        int n8 = n(d8, r4Var, bArr, i8, i9, i10, c1Var);
        r4Var.c(d8);
        c1Var.f14737c = d8;
        return n8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(r4 r4Var, byte[] bArr, int i8, int i9, c1 c1Var) {
        Object d8 = r4Var.d();
        int o5 = o(d8, r4Var, bArr, i8, i9, c1Var);
        r4Var.c(d8);
        c1Var.f14737c = d8;
        return o5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int e(r4 r4Var, int i8, byte[] bArr, int i9, int i10, x2 x2Var, c1 c1Var) {
        int d8 = d(r4Var, bArr, i9, i10, c1Var);
        while (true) {
            x2Var.add(c1Var.f14737c);
            if (d8 >= i10) {
                break;
            }
            int j8 = j(bArr, d8, c1Var);
            if (i8 != c1Var.f14735a) {
                break;
            }
            d8 = d(r4Var, bArr, j8, i10, c1Var);
        }
        return d8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int f(byte[] bArr, int i8, x2 x2Var, c1 c1Var) {
        q2 q2Var = (q2) x2Var;
        int j8 = j(bArr, i8, c1Var);
        int i9 = c1Var.f14735a + j8;
        while (j8 < i9) {
            j8 = j(bArr, j8, c1Var);
            q2Var.i(c1Var.f14735a);
        }
        if (j8 == i9) {
            return j8;
        }
        throw zzeo.g();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int g(byte[] bArr, int i8, c1 c1Var) {
        int j8 = j(bArr, i8, c1Var);
        int i9 = c1Var.f14735a;
        if (i9 >= 0) {
            if (i9 == 0) {
                c1Var.f14737c = BuildConfig.FLAVOR;
                return j8;
            }
            c1Var.f14737c = new String(bArr, j8, i9, y2.f14886b);
            return j8 + i9;
        }
        throw zzeo.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int h(byte[] bArr, int i8, c1 c1Var) {
        int j8 = j(bArr, i8, c1Var);
        int i9 = c1Var.f14735a;
        if (i9 >= 0) {
            if (i9 == 0) {
                c1Var.f14737c = BuildConfig.FLAVOR;
                return j8;
            }
            int length = bArr.length;
            int i10 = x5.f14880b;
            if ((j8 | i9 | ((length - j8) - i9)) >= 0) {
                int i11 = j8 + i9;
                char[] cArr = new char[i9];
                int i12 = 0;
                while (j8 < i11) {
                    byte b9 = bArr[j8];
                    if (!t5.d(b9)) {
                        break;
                    }
                    j8++;
                    cArr[i12] = (char) b9;
                    i12++;
                }
                while (j8 < i11) {
                    int i13 = j8 + 1;
                    byte b10 = bArr[j8];
                    if (t5.d(b10)) {
                        int i14 = i12 + 1;
                        cArr[i12] = (char) b10;
                        j8 = i13;
                        while (true) {
                            i12 = i14;
                            if (j8 < i11) {
                                byte b11 = bArr[j8];
                                if (t5.d(b11)) {
                                    j8++;
                                    i14 = i12 + 1;
                                    cArr[i12] = (char) b11;
                                }
                            }
                        }
                    } else if (b10 < -32) {
                        if (i13 >= i11) {
                            throw zzeo.c();
                        }
                        t5.c(b10, bArr[i13], cArr, i12);
                        j8 = i13 + 1;
                        i12++;
                    } else if (b10 < -16) {
                        if (i13 >= i11 - 1) {
                            throw zzeo.c();
                        }
                        int i15 = i13 + 1;
                        t5.b(b10, bArr[i13], bArr[i15], cArr, i12);
                        j8 = i15 + 1;
                        i12++;
                    } else if (i13 >= i11 - 2) {
                        throw zzeo.c();
                    } else {
                        int i16 = i13 + 1;
                        byte b12 = bArr[i13];
                        int i17 = i16 + 1;
                        t5.a(b10, b12, bArr[i16], bArr[i17], cArr, i12);
                        i12 += 2;
                        j8 = i17 + 1;
                    }
                }
                c1Var.f14737c = new String(cArr, 0, i12);
                return i11;
            }
            throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(length), Integer.valueOf(j8), Integer.valueOf(i9)));
        }
        throw zzeo.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int i(int i8, byte[] bArr, int i9, int i10, i5 i5Var, c1 c1Var) {
        if ((i8 >>> 3) != 0) {
            int i11 = i8 & 7;
            if (i11 == 0) {
                int m8 = m(bArr, i9, c1Var);
                i5Var.j(i8, Long.valueOf(c1Var.f14736b));
                return m8;
            } else if (i11 == 1) {
                i5Var.j(i8, Long.valueOf(q(bArr, i9)));
                return i9 + 8;
            } else if (i11 == 2) {
                int j8 = j(bArr, i9, c1Var);
                int i12 = c1Var.f14735a;
                if (i12 >= 0) {
                    if (i12 <= bArr.length - j8) {
                        i5Var.j(i8, i12 == 0 ? zzdb.f14977b : zzdb.F(bArr, j8, i12));
                        return j8 + i12;
                    }
                    throw zzeo.g();
                }
                throw zzeo.d();
            } else if (i11 != 3) {
                if (i11 == 5) {
                    i5Var.j(i8, Integer.valueOf(b(bArr, i9)));
                    return i9 + 4;
                }
                throw zzeo.b();
            } else {
                int i13 = (i8 & (-8)) | 4;
                i5 f5 = i5.f();
                int i14 = 0;
                while (true) {
                    if (i9 >= i10) {
                        break;
                    }
                    int j9 = j(bArr, i9, c1Var);
                    int i15 = c1Var.f14735a;
                    i14 = i15;
                    if (i15 == i13) {
                        i9 = j9;
                        break;
                    }
                    int i16 = i(i14, bArr, j9, i10, f5, c1Var);
                    i14 = i15;
                    i9 = i16;
                }
                if (i9 > i10 || i14 != i13) {
                    throw zzeo.e();
                }
                i5Var.j(i8, f5);
                return i9;
            }
        }
        throw zzeo.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int j(byte[] bArr, int i8, c1 c1Var) {
        int i9 = i8 + 1;
        byte b9 = bArr[i8];
        if (b9 >= 0) {
            c1Var.f14735a = b9;
            return i9;
        }
        return k(b9, bArr, i9, c1Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int k(int i8, byte[] bArr, int i9, c1 c1Var) {
        int i10;
        int i11;
        byte b9 = bArr[i9];
        int i12 = i9 + 1;
        int i13 = i8 & 127;
        if (b9 < 0) {
            int i14 = i13 | ((b9 & Byte.MAX_VALUE) << 7);
            int i15 = i12 + 1;
            byte b10 = bArr[i12];
            if (b10 >= 0) {
                i10 = b10 << 14;
            } else {
                i13 = i14 | ((b10 & Byte.MAX_VALUE) << 14);
                i12 = i15 + 1;
                byte b11 = bArr[i15];
                if (b11 >= 0) {
                    i11 = b11 << 21;
                } else {
                    i14 = i13 | ((b11 & Byte.MAX_VALUE) << 21);
                    i15 = i12 + 1;
                    byte b12 = bArr[i12];
                    if (b12 >= 0) {
                        i10 = b12 << 28;
                    } else {
                        int i16 = i14 | ((b12 & Byte.MAX_VALUE) << 28);
                        while (true) {
                            int i17 = i15 + 1;
                            if (bArr[i15] >= 0) {
                                c1Var.f14735a = i16;
                                return i17;
                            }
                            i15 = i17;
                        }
                    }
                }
            }
            c1Var.f14735a = i14 | i10;
            return i15;
        }
        i11 = b9 << 7;
        c1Var.f14735a = i13 | i11;
        return i12;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int l(int i8, byte[] bArr, int i9, int i10, x2 x2Var, c1 c1Var) {
        q2 q2Var = (q2) x2Var;
        int j8 = j(bArr, i9, c1Var);
        while (true) {
            q2Var.i(c1Var.f14735a);
            if (j8 >= i10) {
                break;
            }
            int j9 = j(bArr, j8, c1Var);
            if (i8 != c1Var.f14735a) {
                break;
            }
            j8 = j(bArr, j9, c1Var);
        }
        return j8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int m(byte[] bArr, int i8, c1 c1Var) {
        long j8 = bArr[i8];
        int i9 = i8 + 1;
        if (j8 >= 0) {
            c1Var.f14736b = j8;
            return i9;
        }
        int i10 = i9 + 1;
        byte b9 = bArr[i9];
        long j9 = (j8 & 127) | ((b9 & Byte.MAX_VALUE) << 7);
        int i11 = 7;
        while (b9 < 0) {
            int i12 = i10 + 1;
            byte b10 = bArr[i10];
            i11 += 7;
            j9 |= (b10 & Byte.MAX_VALUE) << i11;
            i10 = i12;
            b9 = b10;
        }
        c1Var.f14736b = j9;
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int n(Object obj, r4 r4Var, byte[] bArr, int i8, int i9, int i10, c1 c1Var) {
        int F = ((a4) r4Var).F(obj, bArr, i8, i9, i10, c1Var);
        c1Var.f14737c = obj;
        return F;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int o(Object obj, r4 r4Var, byte[] bArr, int i8, int i9, c1 c1Var) {
        int i10 = i8 + 1;
        int i11 = bArr[i8];
        if (i11 < 0) {
            i10 = k(i11, bArr, i10, c1Var);
            i11 = c1Var.f14735a;
        }
        int i12 = i10;
        if (i11 < 0 || i11 > i9 - i12) {
            throw zzeo.g();
        }
        int i13 = i11 + i12;
        r4Var.h(obj, bArr, i12, i13, c1Var);
        c1Var.f14737c = obj;
        return i13;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int p(int i8, byte[] bArr, int i9, int i10, c1 c1Var) {
        if ((i8 >>> 3) != 0) {
            int i11 = i8 & 7;
            if (i11 != 0) {
                if (i11 != 1) {
                    if (i11 != 2) {
                        if (i11 != 3) {
                            if (i11 == 5) {
                                return i9 + 4;
                            }
                            throw zzeo.b();
                        }
                        int i12 = (i8 & (-8)) | 4;
                        int i13 = 0;
                        while (i9 < i10) {
                            i9 = j(bArr, i9, c1Var);
                            i13 = c1Var.f14735a;
                            if (i13 == i12) {
                                break;
                            }
                            i9 = p(i13, bArr, i9, i10, c1Var);
                        }
                        if (i9 > i10 || i13 != i12) {
                            throw zzeo.e();
                        }
                        return i9;
                    }
                    return j(bArr, i9, c1Var) + c1Var.f14735a;
                }
                return i9 + 8;
            }
            return m(bArr, i9, c1Var);
        }
        throw zzeo.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long q(byte[] bArr, int i8) {
        return (bArr[i8] & 255) | ((bArr[i8 + 1] & 255) << 8) | ((bArr[i8 + 2] & 255) << 16) | ((bArr[i8 + 3] & 255) << 24) | ((bArr[i8 + 4] & 255) << 32) | ((bArr[i8 + 5] & 255) << 40) | ((bArr[i8 + 6] & 255) << 48) | ((bArr[i8 + 7] & 255) << 56);
    }
}
