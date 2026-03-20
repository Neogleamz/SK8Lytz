package y4;

import android.util.Pair;
import b6.l0;
import b6.p;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import n4.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f24415a;

        /* renamed from: b  reason: collision with root package name */
        public final long f24416b;

        private a(int i8, long j8) {
            this.f24415a = i8;
            this.f24416b = j8;
        }

        public static a a(l lVar, z zVar) {
            lVar.k(zVar.e(), 0, 8);
            zVar.U(0);
            return new a(zVar.q(), zVar.x());
        }
    }

    public static boolean a(l lVar) {
        z zVar = new z(8);
        int i8 = a.a(lVar, zVar).f24415a;
        if (i8 == 1380533830 || i8 == 1380333108) {
            lVar.k(zVar.e(), 0, 4);
            zVar.U(0);
            int q = zVar.q();
            if (q != 1463899717) {
                p.c("WavHeaderReader", "Unsupported form type: " + q);
                return false;
            }
            return true;
        }
        return false;
    }

    public static c b(l lVar) {
        byte[] bArr;
        z zVar = new z(16);
        a d8 = d(1718449184, lVar, zVar);
        b6.a.f(d8.f24416b >= 16);
        lVar.k(zVar.e(), 0, 16);
        zVar.U(0);
        int z4 = zVar.z();
        int z8 = zVar.z();
        int y8 = zVar.y();
        int y9 = zVar.y();
        int z9 = zVar.z();
        int z10 = zVar.z();
        int i8 = ((int) d8.f24416b) - 16;
        if (i8 > 0) {
            byte[] bArr2 = new byte[i8];
            lVar.k(bArr2, 0, i8);
            bArr = bArr2;
        } else {
            bArr = l0.f8068f;
        }
        lVar.i((int) (lVar.e() - lVar.getPosition()));
        return new c(z4, z8, y8, y9, z9, z10, bArr);
    }

    public static long c(l lVar) {
        z zVar = new z(8);
        a a9 = a.a(lVar, zVar);
        if (a9.f24415a != 1685272116) {
            lVar.h();
            return -1L;
        }
        lVar.f(8);
        zVar.U(0);
        lVar.k(zVar.e(), 0, 8);
        long v8 = zVar.v();
        lVar.i(((int) a9.f24416b) + 8);
        return v8;
    }

    private static a d(int i8, l lVar, z zVar) {
        while (true) {
            a a9 = a.a(lVar, zVar);
            if (a9.f24415a == i8) {
                return a9;
            }
            p.i("WavHeaderReader", "Ignoring unknown WAV chunk: " + a9.f24415a);
            long j8 = a9.f24416b + 8;
            if (j8 > 2147483647L) {
                throw ParserException.d("Chunk is too large (~2GB+) to skip; id: " + a9.f24415a);
            }
            lVar.i((int) j8);
        }
    }

    public static Pair<Long, Long> e(l lVar) {
        lVar.h();
        a d8 = d(1684108385, lVar, new z(8));
        lVar.i(8);
        return Pair.create(Long.valueOf(lVar.getPosition()), Long.valueOf(d8.f24416b));
    }
}
