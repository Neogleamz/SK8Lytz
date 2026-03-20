package n4;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.flac.PictureFrame;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import n4.t;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public t f22126a;

        public a(t tVar) {
            this.f22126a = tVar;
        }
    }

    public static boolean a(l lVar) {
        b6.z zVar = new b6.z(4);
        lVar.k(zVar.e(), 0, 4);
        return zVar.J() == 1716281667;
    }

    public static int b(l lVar) {
        lVar.h();
        b6.z zVar = new b6.z(2);
        lVar.k(zVar.e(), 0, 2);
        int N = zVar.N();
        int i8 = N >> 2;
        lVar.h();
        if (i8 == 16382) {
            return N;
        }
        throw ParserException.a("First frame does not start with sync code.", null);
    }

    public static Metadata c(l lVar, boolean z4) {
        Metadata a9 = new w().a(lVar, z4 ? null : e5.b.f19796b);
        if (a9 == null || a9.e() == 0) {
            return null;
        }
        return a9;
    }

    public static Metadata d(l lVar, boolean z4) {
        lVar.h();
        long e8 = lVar.e();
        Metadata c9 = c(lVar, z4);
        lVar.i((int) (lVar.e() - e8));
        return c9;
    }

    public static boolean e(l lVar, a aVar) {
        t a9;
        lVar.h();
        b6.y yVar = new b6.y(new byte[4]);
        lVar.k(yVar.f8152a, 0, 4);
        boolean g8 = yVar.g();
        int h8 = yVar.h(7);
        int h9 = yVar.h(24) + 4;
        if (h8 == 0) {
            a9 = h(lVar);
        } else {
            t tVar = aVar.f22126a;
            if (tVar == null) {
                throw new IllegalArgumentException();
            }
            if (h8 == 3) {
                a9 = tVar.b(g(lVar, h9));
            } else if (h8 == 4) {
                a9 = tVar.c(j(lVar, h9));
            } else if (h8 != 6) {
                lVar.i(h9);
                return g8;
            } else {
                b6.z zVar = new b6.z(h9);
                lVar.readFully(zVar.e(), 0, h9);
                zVar.V(4);
                a9 = tVar.a(ImmutableList.F(PictureFrame.a(zVar)));
            }
        }
        aVar.f22126a = a9;
        return g8;
    }

    public static t.a f(b6.z zVar) {
        zVar.V(1);
        int K = zVar.K();
        long f5 = zVar.f() + K;
        int i8 = K / 18;
        long[] jArr = new long[i8];
        long[] jArr2 = new long[i8];
        int i9 = 0;
        while (true) {
            if (i9 >= i8) {
                break;
            }
            long A = zVar.A();
            if (A == -1) {
                jArr = Arrays.copyOf(jArr, i9);
                jArr2 = Arrays.copyOf(jArr2, i9);
                break;
            }
            jArr[i9] = A;
            jArr2[i9] = zVar.A();
            zVar.V(2);
            i9++;
        }
        zVar.V((int) (f5 - zVar.f()));
        return new t.a(jArr, jArr2);
    }

    private static t.a g(l lVar, int i8) {
        b6.z zVar = new b6.z(i8);
        lVar.readFully(zVar.e(), 0, i8);
        return f(zVar);
    }

    private static t h(l lVar) {
        byte[] bArr = new byte[38];
        lVar.readFully(bArr, 0, 38);
        return new t(bArr, 4);
    }

    public static void i(l lVar) {
        b6.z zVar = new b6.z(4);
        lVar.readFully(zVar.e(), 0, 4);
        if (zVar.J() != 1716281667) {
            throw ParserException.a("Failed to read FLAC stream marker.", null);
        }
    }

    private static List<String> j(l lVar, int i8) {
        b6.z zVar = new b6.z(i8);
        lVar.readFully(zVar.e(), 0, i8);
        zVar.V(4);
        return Arrays.asList(e0.j(zVar, false, false).f22089b);
    }
}
