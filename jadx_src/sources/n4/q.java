package n4;

import b6.l0;
import com.google.android.exoplayer2.ParserException;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public long f22125a;
    }

    private static boolean a(b6.z zVar, t tVar, int i8) {
        int j8 = j(zVar, i8);
        return j8 != -1 && j8 <= tVar.f22130b;
    }

    private static boolean b(b6.z zVar, int i8) {
        return zVar.H() == l0.u(zVar.e(), i8, zVar.f() - 1, 0);
    }

    private static boolean c(b6.z zVar, t tVar, boolean z4, a aVar) {
        try {
            long O = zVar.O();
            if (!z4) {
                O *= tVar.f22130b;
            }
            aVar.f22125a = O;
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public static boolean d(b6.z zVar, t tVar, int i8, a aVar) {
        int f5 = zVar.f();
        long J = zVar.J();
        long j8 = J >>> 16;
        if (j8 != i8) {
            return false;
        }
        return g((int) ((J >> 4) & 15), tVar) && f((int) ((J >> 1) & 7), tVar) && !(((J & 1) > 1L ? 1 : ((J & 1) == 1L ? 0 : -1)) == 0) && c(zVar, tVar, ((j8 & 1) > 1L ? 1 : ((j8 & 1) == 1L ? 0 : -1)) == 0, aVar) && a(zVar, tVar, (int) ((J >> 12) & 15)) && e(zVar, tVar, (int) ((J >> 8) & 15)) && b(zVar, f5);
    }

    private static boolean e(b6.z zVar, t tVar, int i8) {
        int i9 = tVar.f22133e;
        if (i8 == 0) {
            return true;
        }
        if (i8 <= 11) {
            return i8 == tVar.f22134f;
        } else if (i8 == 12) {
            return zVar.H() * 1000 == i9;
        } else if (i8 <= 14) {
            int N = zVar.N();
            if (i8 == 14) {
                N *= 10;
            }
            return N == i9;
        } else {
            return false;
        }
    }

    private static boolean f(int i8, t tVar) {
        return i8 == 0 || i8 == tVar.f22137i;
    }

    private static boolean g(int i8, t tVar) {
        return i8 <= 7 ? i8 == tVar.f22135g - 1 : i8 <= 10 && tVar.f22135g == 2;
    }

    public static boolean h(l lVar, t tVar, int i8, a aVar) {
        long e8 = lVar.e();
        byte[] bArr = new byte[2];
        lVar.k(bArr, 0, 2);
        if ((((bArr[0] & 255) << 8) | (bArr[1] & 255)) != i8) {
            lVar.h();
            lVar.f((int) (e8 - lVar.getPosition()));
            return false;
        }
        b6.z zVar = new b6.z(16);
        System.arraycopy(bArr, 0, zVar.e(), 0, 2);
        zVar.T(n.c(lVar, zVar.e(), 2, 14));
        lVar.h();
        lVar.f((int) (e8 - lVar.getPosition()));
        return d(zVar, tVar, i8, aVar);
    }

    public static long i(l lVar, t tVar) {
        lVar.h();
        lVar.f(1);
        byte[] bArr = new byte[1];
        lVar.k(bArr, 0, 1);
        boolean z4 = (bArr[0] & 1) == 1;
        lVar.f(2);
        int i8 = z4 ? 7 : 6;
        b6.z zVar = new b6.z(i8);
        zVar.T(n.c(lVar, zVar.e(), 0, i8));
        lVar.h();
        a aVar = new a();
        if (c(zVar, tVar, z4, aVar)) {
            return aVar.f22125a;
        }
        throw ParserException.a(null, null);
    }

    public static int j(b6.z zVar, int i8) {
        switch (i8) {
            case 1:
                return 192;
            case 2:
            case 3:
            case 4:
            case 5:
                return 576 << (i8 - 2);
            case 6:
                return zVar.H() + 1;
            case 7:
                return zVar.N() + 1;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return RecognitionOptions.QR_CODE << (i8 - 8);
            default:
                return -1;
        }
    }
}
