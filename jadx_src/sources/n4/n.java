package n4;

import com.google.android.exoplayer2.ParserException;
import java.io.EOFException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n {
    public static void a(boolean z4, String str) {
        if (!z4) {
            throw ParserException.a(str, null);
        }
    }

    public static boolean b(l lVar, byte[] bArr, int i8, int i9, boolean z4) {
        try {
            return lVar.d(bArr, i8, i9, z4);
        } catch (EOFException e8) {
            if (z4) {
                return false;
            }
            throw e8;
        }
    }

    public static int c(l lVar, byte[] bArr, int i8, int i9) {
        int i10 = 0;
        while (i10 < i9) {
            int g8 = lVar.g(bArr, i8 + i10, i9 - i10);
            if (g8 == -1) {
                break;
            }
            i10 += g8;
        }
        return i10;
    }

    public static boolean d(l lVar, byte[] bArr, int i8, int i9) {
        try {
            lVar.readFully(bArr, i8, i9);
            return true;
        } catch (EOFException unused) {
            return false;
        }
    }

    public static boolean e(l lVar, int i8) {
        try {
            lVar.i(i8);
            return true;
        } catch (EOFException unused) {
            return false;
        }
    }
}
