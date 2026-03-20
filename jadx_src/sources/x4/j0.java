package x4;

import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j0 {
    public static int a(byte[] bArr, int i8, int i9) {
        while (i8 < i9 && bArr[i8] != 71) {
            i8++;
        }
        return i8;
    }

    public static boolean b(byte[] bArr, int i8, int i9, int i10) {
        int i11 = 0;
        for (int i12 = -4; i12 <= 4; i12++) {
            int i13 = (i12 * 188) + i10;
            if (i13 < i8 || i13 >= i9 || bArr[i13] != 71) {
                i11 = 0;
            } else {
                i11++;
                if (i11 == 5) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long c(z zVar, int i8, int i9) {
        zVar.U(i8);
        if (zVar.a() < 5) {
            return -9223372036854775807L;
        }
        int q = zVar.q();
        if ((8388608 & q) == 0 && ((2096896 & q) >> 8) == i9) {
            if (((q & 32) != 0) && zVar.H() >= 7 && zVar.a() >= 7) {
                if ((zVar.H() & 16) == 16) {
                    byte[] bArr = new byte[6];
                    zVar.l(bArr, 0, 6);
                    return d(bArr);
                }
            }
            return -9223372036854775807L;
        }
        return -9223372036854775807L;
    }

    private static long d(byte[] bArr) {
        return ((bArr[0] & 255) << 25) | ((bArr[1] & 255) << 17) | ((bArr[2] & 255) << 9) | ((bArr[3] & 255) << 1) | ((255 & bArr[4]) >> 7);
    }
}
