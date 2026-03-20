package n4;

import android.util.Base64;
import b6.l0;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.flac.PictureFrame;
import com.google.android.exoplayer2.metadata.vorbis.VorbisComment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e0 {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f22083a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22084b;

        /* renamed from: c  reason: collision with root package name */
        public final long[] f22085c;

        /* renamed from: d  reason: collision with root package name */
        public final int f22086d;

        /* renamed from: e  reason: collision with root package name */
        public final boolean f22087e;

        public a(int i8, int i9, long[] jArr, int i10, boolean z4) {
            this.f22083a = i8;
            this.f22084b = i9;
            this.f22085c = jArr;
            this.f22086d = i10;
            this.f22087e = z4;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final String f22088a;

        /* renamed from: b  reason: collision with root package name */
        public final String[] f22089b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22090c;

        public b(String str, String[] strArr, int i8) {
            this.f22088a = str;
            this.f22089b = strArr;
            this.f22090c = i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public final boolean f22091a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22092b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22093c;

        /* renamed from: d  reason: collision with root package name */
        public final int f22094d;

        public c(boolean z4, int i8, int i9, int i10) {
            this.f22091a = z4;
            this.f22092b = i8;
            this.f22093c = i9;
            this.f22094d = i10;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        public final int f22095a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22096b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22097c;

        /* renamed from: d  reason: collision with root package name */
        public final int f22098d;

        /* renamed from: e  reason: collision with root package name */
        public final int f22099e;

        /* renamed from: f  reason: collision with root package name */
        public final int f22100f;

        /* renamed from: g  reason: collision with root package name */
        public final int f22101g;

        /* renamed from: h  reason: collision with root package name */
        public final int f22102h;

        /* renamed from: i  reason: collision with root package name */
        public final boolean f22103i;

        /* renamed from: j  reason: collision with root package name */
        public final byte[] f22104j;

        public d(int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, boolean z4, byte[] bArr) {
            this.f22095a = i8;
            this.f22096b = i9;
            this.f22097c = i10;
            this.f22098d = i11;
            this.f22099e = i12;
            this.f22100f = i13;
            this.f22101g = i14;
            this.f22102h = i15;
            this.f22103i = z4;
            this.f22104j = bArr;
        }
    }

    public static int a(int i8) {
        int i9 = 0;
        while (i8 > 0) {
            i9++;
            i8 >>>= 1;
        }
        return i9;
    }

    private static long b(long j8, long j9) {
        return (long) Math.floor(Math.pow(j8, 1.0d / j9));
    }

    public static Metadata c(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            String str = list.get(i8);
            String[] S0 = l0.S0(str, "=");
            if (S0.length != 2) {
                b6.p.i("VorbisUtil", "Failed to parse Vorbis comment: " + str);
            } else if (S0[0].equals("METADATA_BLOCK_PICTURE")) {
                try {
                    arrayList.add(PictureFrame.a(new b6.z(Base64.decode(S0[1], 0))));
                } catch (RuntimeException e8) {
                    b6.p.j("VorbisUtil", "Failed to parse vorbis picture", e8);
                }
            } else {
                arrayList.add(new VorbisComment(S0[0], S0[1]));
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new Metadata(arrayList);
    }

    private static a d(d0 d0Var) {
        if (d0Var.d(24) != 5653314) {
            throw ParserException.a("expected code book to start with [0x56, 0x43, 0x42] at " + d0Var.b(), null);
        }
        int d8 = d0Var.d(16);
        int d9 = d0Var.d(24);
        long[] jArr = new long[d9];
        boolean c9 = d0Var.c();
        long j8 = 0;
        if (c9) {
            int d10 = d0Var.d(5) + 1;
            int i8 = 0;
            while (i8 < d9) {
                int d11 = d0Var.d(a(d9 - i8));
                for (int i9 = 0; i9 < d11 && i8 < d9; i9++) {
                    jArr[i8] = d10;
                    i8++;
                }
                d10++;
            }
        } else {
            boolean c10 = d0Var.c();
            for (int i10 = 0; i10 < d9; i10++) {
                if (!c10) {
                    jArr[i10] = d0Var.d(5) + 1;
                } else if (d0Var.c()) {
                    jArr[i10] = d0Var.d(5) + 1;
                } else {
                    jArr[i10] = 0;
                }
            }
        }
        int d12 = d0Var.d(4);
        if (d12 > 2) {
            throw ParserException.a("lookup type greater than 2 not decodable: " + d12, null);
        }
        if (d12 == 1 || d12 == 2) {
            d0Var.e(32);
            d0Var.e(32);
            int d13 = d0Var.d(4) + 1;
            d0Var.e(1);
            if (d12 != 1) {
                j8 = d9 * d8;
            } else if (d8 != 0) {
                j8 = b(d9, d8);
            }
            d0Var.e((int) (j8 * d13));
        }
        return new a(d8, d9, jArr, d12, c9);
    }

    private static void e(d0 d0Var) {
        int d8 = d0Var.d(6) + 1;
        for (int i8 = 0; i8 < d8; i8++) {
            int d9 = d0Var.d(16);
            if (d9 == 0) {
                d0Var.e(8);
                d0Var.e(16);
                d0Var.e(16);
                d0Var.e(6);
                d0Var.e(8);
                int d10 = d0Var.d(4) + 1;
                for (int i9 = 0; i9 < d10; i9++) {
                    d0Var.e(8);
                }
            } else if (d9 != 1) {
                throw ParserException.a("floor type greater than 1 not decodable: " + d9, null);
            } else {
                int d11 = d0Var.d(5);
                int i10 = -1;
                int[] iArr = new int[d11];
                for (int i11 = 0; i11 < d11; i11++) {
                    iArr[i11] = d0Var.d(4);
                    if (iArr[i11] > i10) {
                        i10 = iArr[i11];
                    }
                }
                int i12 = i10 + 1;
                int[] iArr2 = new int[i12];
                for (int i13 = 0; i13 < i12; i13++) {
                    iArr2[i13] = d0Var.d(3) + 1;
                    int d12 = d0Var.d(2);
                    if (d12 > 0) {
                        d0Var.e(8);
                    }
                    for (int i14 = 0; i14 < (1 << d12); i14++) {
                        d0Var.e(8);
                    }
                }
                d0Var.e(2);
                int d13 = d0Var.d(4);
                int i15 = 0;
                int i16 = 0;
                for (int i17 = 0; i17 < d11; i17++) {
                    i15 += iArr2[iArr[i17]];
                    while (i16 < i15) {
                        d0Var.e(d13);
                        i16++;
                    }
                }
            }
        }
    }

    private static void f(int i8, d0 d0Var) {
        int d8 = d0Var.d(6) + 1;
        for (int i9 = 0; i9 < d8; i9++) {
            int d9 = d0Var.d(16);
            if (d9 != 0) {
                b6.p.c("VorbisUtil", "mapping type other than 0 not supported: " + d9);
            } else {
                int d10 = d0Var.c() ? d0Var.d(4) + 1 : 1;
                if (d0Var.c()) {
                    int d11 = d0Var.d(8) + 1;
                    for (int i10 = 0; i10 < d11; i10++) {
                        int i11 = i8 - 1;
                        d0Var.e(a(i11));
                        d0Var.e(a(i11));
                    }
                }
                if (d0Var.d(2) != 0) {
                    throw ParserException.a("to reserved bits must be zero after mapping coupling steps", null);
                }
                if (d10 > 1) {
                    for (int i12 = 0; i12 < i8; i12++) {
                        d0Var.e(4);
                    }
                }
                for (int i13 = 0; i13 < d10; i13++) {
                    d0Var.e(8);
                    d0Var.e(8);
                    d0Var.e(8);
                }
            }
        }
    }

    private static c[] g(d0 d0Var) {
        int d8 = d0Var.d(6) + 1;
        c[] cVarArr = new c[d8];
        for (int i8 = 0; i8 < d8; i8++) {
            cVarArr[i8] = new c(d0Var.c(), d0Var.d(16), d0Var.d(16), d0Var.d(8));
        }
        return cVarArr;
    }

    private static void h(d0 d0Var) {
        int d8 = d0Var.d(6) + 1;
        for (int i8 = 0; i8 < d8; i8++) {
            if (d0Var.d(16) > 2) {
                throw ParserException.a("residueType greater than 2 is not decodable", null);
            }
            d0Var.e(24);
            d0Var.e(24);
            d0Var.e(24);
            int d9 = d0Var.d(6) + 1;
            d0Var.e(8);
            int[] iArr = new int[d9];
            for (int i9 = 0; i9 < d9; i9++) {
                iArr[i9] = ((d0Var.c() ? d0Var.d(5) : 0) * 8) + d0Var.d(3);
            }
            for (int i10 = 0; i10 < d9; i10++) {
                for (int i11 = 0; i11 < 8; i11++) {
                    if ((iArr[i10] & (1 << i11)) != 0) {
                        d0Var.e(8);
                    }
                }
            }
        }
    }

    public static b i(b6.z zVar) {
        return j(zVar, true, true);
    }

    public static b j(b6.z zVar, boolean z4, boolean z8) {
        if (z4) {
            m(3, zVar, false);
        }
        String E = zVar.E((int) zVar.x());
        int length = 11 + E.length();
        long x8 = zVar.x();
        String[] strArr = new String[(int) x8];
        int i8 = length + 4;
        for (int i9 = 0; i9 < x8; i9++) {
            strArr[i9] = zVar.E((int) zVar.x());
            i8 = i8 + 4 + strArr[i9].length();
        }
        if (z8 && (zVar.H() & 1) == 0) {
            throw ParserException.a("framing bit expected to be set", null);
        }
        return new b(E, strArr, i8 + 1);
    }

    public static d k(b6.z zVar) {
        m(1, zVar, false);
        int y8 = zVar.y();
        int H = zVar.H();
        int y9 = zVar.y();
        int u8 = zVar.u();
        if (u8 <= 0) {
            u8 = -1;
        }
        int u9 = zVar.u();
        if (u9 <= 0) {
            u9 = -1;
        }
        int u10 = zVar.u();
        if (u10 <= 0) {
            u10 = -1;
        }
        int H2 = zVar.H();
        return new d(y8, H, y9, u8, u9, u10, (int) Math.pow(2.0d, H2 & 15), (int) Math.pow(2.0d, (H2 & 240) >> 4), (zVar.H() & 1) > 0, Arrays.copyOf(zVar.e(), zVar.g()));
    }

    public static c[] l(b6.z zVar, int i8) {
        m(5, zVar, false);
        int H = zVar.H() + 1;
        d0 d0Var = new d0(zVar.e());
        d0Var.e(zVar.f() * 8);
        for (int i9 = 0; i9 < H; i9++) {
            d(d0Var);
        }
        int d8 = d0Var.d(6) + 1;
        for (int i10 = 0; i10 < d8; i10++) {
            if (d0Var.d(16) != 0) {
                throw ParserException.a("placeholder of time domain transforms not zeroed out", null);
            }
        }
        e(d0Var);
        h(d0Var);
        f(i8, d0Var);
        c[] g8 = g(d0Var);
        if (d0Var.c()) {
            return g8;
        }
        throw ParserException.a("framing bit after modes not set as expected", null);
    }

    public static boolean m(int i8, b6.z zVar, boolean z4) {
        if (zVar.a() < 7) {
            if (z4) {
                return false;
            }
            throw ParserException.a("too short header: " + zVar.a(), null);
        } else if (zVar.H() != i8) {
            if (z4) {
                return false;
            }
            throw ParserException.a("expected header type " + Integer.toHexString(i8), null);
        } else if (zVar.H() == 118 && zVar.H() == 111 && zVar.H() == 114 && zVar.H() == 98 && zVar.H() == 105 && zVar.H() == 115) {
            return true;
        } else {
            if (z4) {
                return false;
            }
            throw ParserException.a("expected characters 'vorbis'", null);
        }
    }
}
