package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class dc {

    /* renamed from: a  reason: collision with root package name */
    private static final fc f12145a;

    static {
        if (yb.w()) {
            yb.z();
        }
        f12145a = new ec();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(String str) {
        int length = str.length();
        int i8 = 0;
        int i9 = 0;
        while (i9 < length && str.charAt(i9) < 128) {
            i9++;
        }
        int i10 = length;
        while (true) {
            if (i9 >= length) {
                break;
            }
            char charAt = str.charAt(i9);
            if (charAt < 2048) {
                i10 += (127 - charAt) >>> 31;
                i9++;
            } else {
                int length2 = str.length();
                while (i9 < length2) {
                    char charAt2 = str.charAt(i9);
                    if (charAt2 < 2048) {
                        i8 += (127 - charAt2) >>> 31;
                    } else {
                        i8 += 2;
                        if (55296 <= charAt2 && charAt2 <= 57343) {
                            if (Character.codePointAt(str, i9) < 65536) {
                                throw new gc(i9, length2);
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
    public static int b(String str, byte[] bArr, int i8, int i9) {
        return f12145a.b(str, bArr, i8, i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int c(byte[] bArr, int i8, int i9) {
        byte b9 = bArr[i8 - 1];
        int i10 = i9 - i8;
        if (i10 == 0) {
            if (b9 > -12) {
                return -1;
            }
            return b9;
        } else if (i10 == 1) {
            byte b10 = bArr[i8];
            if (b9 > -12 || b10 > -65) {
                return -1;
            }
            return (b10 << 8) ^ b9;
        } else if (i10 == 2) {
            byte b11 = bArr[i8];
            byte b12 = bArr[i8 + 1];
            if (b9 > -12 || b11 > -65 || b12 > -65) {
                return -1;
            }
            return (b12 << 16) ^ ((b11 << 8) ^ b9);
        } else {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d(byte[] bArr) {
        return f12145a.d(bArr, 0, bArr.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String e(byte[] bArr, int i8, int i9) {
        return f12145a.c(bArr, i8, i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean f(byte[] bArr, int i8, int i9) {
        return f12145a.d(bArr, i8, i9);
    }
}
