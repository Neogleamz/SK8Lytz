package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x5 {

    /* renamed from: a  reason: collision with root package name */
    private static final u5 f14879a;

    /* renamed from: b  reason: collision with root package name */
    public static final /* synthetic */ int f14880b = 0;

    static {
        if (s5.C() && s5.D()) {
            int i8 = b1.f14727a;
        }
        f14879a = new v5();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ int c(byte[] bArr, int i8, int i9) {
        int i10 = i9 - i8;
        byte b9 = bArr[i8 - 1];
        if (i10 == 0) {
            if (b9 > -12) {
                return -1;
            }
            return b9;
        } else if (i10 != 1) {
            if (i10 == 2) {
                return j(b9, bArr[i8], bArr[i8 + 1]);
            }
            throw new AssertionError();
        } else {
            return i(b9, bArr[i8]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00ff, code lost:
        return r9 + r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int d(java.lang.CharSequence r7, byte[] r8, int r9, int r10) {
        /*
            Method dump skipped, instructions count: 256
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.x5.d(java.lang.CharSequence, byte[], int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int e(CharSequence charSequence) {
        int length = charSequence.length();
        int i8 = 0;
        int i9 = 0;
        while (i9 < length && charSequence.charAt(i9) < 128) {
            i9++;
        }
        int i10 = length;
        while (true) {
            if (i9 >= length) {
                break;
            }
            char charAt = charSequence.charAt(i9);
            if (charAt < 2048) {
                i10 += (127 - charAt) >>> 31;
                i9++;
            } else {
                int length2 = charSequence.length();
                while (i9 < length2) {
                    char charAt2 = charSequence.charAt(i9);
                    if (charAt2 < 2048) {
                        i8 += (127 - charAt2) >>> 31;
                    } else {
                        i8 += 2;
                        if (charAt2 >= 55296 && charAt2 <= 57343) {
                            if (Character.codePointAt(charSequence, i9) < 65536) {
                                throw new w5(i9, length2);
                            }
                            i9++;
                        }
                    }
                    i9++;
                }
                i10 += i8;
            }
        }
        if (i10 >= length) {
            return i10;
        }
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (i10 + 4294967296L));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int f(int i8, byte[] bArr, int i9, int i10) {
        return f14879a.a(i8, bArr, i9, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean g(byte[] bArr) {
        return f14879a.b(bArr, 0, bArr.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean h(byte[] bArr, int i8, int i9) {
        return f14879a.b(bArr, i8, i9);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int i(int i8, int i9) {
        if (i8 > -12 || i9 > -65) {
            return -1;
        }
        return i8 ^ (i9 << 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int j(int i8, int i9, int i10) {
        if (i8 > -12 || i9 > -65 || i10 > -65) {
            return -1;
        }
        return (i8 ^ (i9 << 8)) ^ (i10 << 16);
    }
}
