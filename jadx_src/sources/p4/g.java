package p4;

import b6.l0;
import b6.p;
import b6.z;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g implements a {

    /* renamed from: a  reason: collision with root package name */
    public final w0 f22369a;

    public g(w0 w0Var) {
        this.f22369a = w0Var;
    }

    private static String a(int i8) {
        switch (i8) {
            case 808802372:
            case 877677894:
            case 1145656883:
            case 1145656920:
            case 1482049860:
            case 1684633208:
            case 2021026148:
                return "video/mp4v-es";
            case 826496577:
            case 828601953:
            case 875967048:
                return "video/avc";
            case 842289229:
                return "video/mp42";
            case 859066445:
                return "video/mp43";
            case 1196444237:
            case 1735420525:
                return "video/mjpeg";
            default:
                return null;
        }
    }

    private static String b(int i8) {
        if (i8 != 1) {
            if (i8 != 85) {
                if (i8 != 255) {
                    if (i8 != 8192) {
                        if (i8 != 8193) {
                            return null;
                        }
                        return "audio/vnd.dts";
                    }
                    return "audio/ac3";
                }
                return "audio/mp4a-latm";
            }
            return "audio/mpeg";
        }
        return "audio/raw";
    }

    private static a c(z zVar) {
        zVar.V(4);
        int u8 = zVar.u();
        int u9 = zVar.u();
        zVar.V(4);
        int u10 = zVar.u();
        String a9 = a(u10);
        if (a9 != null) {
            w0.b bVar = new w0.b();
            bVar.n0(u8).S(u9).g0(a9);
            return new g(bVar.G());
        }
        p.i("StreamFormatChunk", "Ignoring track with unsupported compression " + u10);
        return null;
    }

    public static a d(int i8, z zVar) {
        if (i8 == 2) {
            return c(zVar);
        }
        if (i8 == 1) {
            return e(zVar);
        }
        p.i("StreamFormatChunk", "Ignoring strf box for unsupported track type: " + l0.k0(i8));
        return null;
    }

    private static a e(z zVar) {
        int z4 = zVar.z();
        String b9 = b(z4);
        if (b9 == null) {
            p.i("StreamFormatChunk", "Ignoring track with unsupported format tag " + z4);
            return null;
        }
        int z8 = zVar.z();
        int u8 = zVar.u();
        zVar.V(6);
        int b02 = l0.b0(zVar.N());
        int z9 = zVar.z();
        byte[] bArr = new byte[z9];
        zVar.l(bArr, 0, z9);
        w0.b bVar = new w0.b();
        bVar.g0(b9).J(z8).h0(u8);
        if ("audio/raw".equals(b9) && b02 != 0) {
            bVar.a0(b02);
        }
        if ("audio/mp4a-latm".equals(b9) && z9 > 0) {
            bVar.V(ImmutableList.F(bArr));
        }
        return new g(bVar.G());
    }

    @Override // p4.a
    public int getType() {
        return 1718776947;
    }
}
