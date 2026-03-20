package e5;

import a5.e;
import b6.l0;
import b6.p;
import b6.y;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.BinaryFrame;
import com.google.android.exoplayer2.metadata.id3.ChapterFrame;
import com.google.android.exoplayer2.metadata.id3.ChapterTocFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.GeobFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.metadata.id3.UrlLinkFrame;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.base.c;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends e {

    /* renamed from: b  reason: collision with root package name */
    public static final a f19796b = e5.a.a;

    /* renamed from: a  reason: collision with root package name */
    private final a f19797a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        boolean a(int i8, int i9, int i10, int i11, int i12);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: e5.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0165b {

        /* renamed from: a  reason: collision with root package name */
        private final int f19798a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f19799b;

        /* renamed from: c  reason: collision with root package name */
        private final int f19800c;

        public C0165b(int i8, boolean z4, int i9) {
            this.f19798a = i8;
            this.f19799b = z4;
            this.f19800c = i9;
        }
    }

    public b() {
        this(null);
    }

    public b(a aVar) {
        this.f19797a = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean A(int i8, int i9, int i10, int i11, int i12) {
        return false;
    }

    private static int B(z zVar, int i8) {
        byte[] e8 = zVar.e();
        int f5 = zVar.f();
        int i9 = f5;
        while (true) {
            int i10 = i9 + 1;
            if (i10 >= f5 + i8) {
                return i8;
            }
            if ((e8[i9] & 255) == 255 && e8[i10] == 0) {
                System.arraycopy(e8, i9 + 2, e8, i10, (i8 - (i9 - f5)) - 2);
                i8--;
            }
            i9 = i10;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0076, code lost:
        if ((r10 & 1) != 0) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0079, code lost:
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0086, code lost:
        if ((r10 & com.google.android.libraries.barhopper.RecognitionOptions.ITF) != 0) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean C(b6.z r18, int r19, int r20, boolean r21) {
        /*
            r1 = r18
            r0 = r19
            int r2 = r18.f()
        L8:
            int r3 = r18.a()     // Catch: java.lang.Throwable -> Laf
            r4 = 1
            r5 = r20
            if (r3 < r5) goto Lab
            r3 = 3
            r6 = 0
            if (r0 < r3) goto L22
            int r7 = r18.q()     // Catch: java.lang.Throwable -> Laf
            long r8 = r18.J()     // Catch: java.lang.Throwable -> Laf
            int r10 = r18.N()     // Catch: java.lang.Throwable -> Laf
            goto L2c
        L22:
            int r7 = r18.K()     // Catch: java.lang.Throwable -> Laf
            int r8 = r18.K()     // Catch: java.lang.Throwable -> Laf
            long r8 = (long) r8
            r10 = r6
        L2c:
            r11 = 0
            if (r7 != 0) goto L3a
            int r7 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r7 != 0) goto L3a
            if (r10 != 0) goto L3a
            r1.U(r2)
            return r4
        L3a:
            r7 = 4
            if (r0 != r7) goto L6b
            if (r21 != 0) goto L6b
            r13 = 8421504(0x808080, double:4.160776E-317)
            long r13 = r13 & r8
            int r11 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1))
            if (r11 == 0) goto L4b
            r1.U(r2)
            return r6
        L4b:
            r11 = 255(0xff, double:1.26E-321)
            long r13 = r8 & r11
            r15 = 8
            long r15 = r8 >> r15
            long r15 = r15 & r11
            r17 = 7
            long r15 = r15 << r17
            long r13 = r13 | r15
            r15 = 16
            long r15 = r8 >> r15
            long r15 = r15 & r11
            r17 = 14
            long r15 = r15 << r17
            long r13 = r13 | r15
            r15 = 24
            long r8 = r8 >> r15
            long r8 = r8 & r11
            r11 = 21
            long r8 = r8 << r11
            long r8 = r8 | r13
        L6b:
            if (r0 != r7) goto L7b
            r3 = r10 & 64
            if (r3 == 0) goto L73
            r3 = r4
            goto L74
        L73:
            r3 = r6
        L74:
            r7 = r10 & 1
            if (r7 == 0) goto L79
            goto L8b
        L79:
            r4 = r6
            goto L8b
        L7b:
            if (r0 != r3) goto L89
            r3 = r10 & 32
            if (r3 == 0) goto L83
            r3 = r4
            goto L84
        L83:
            r3 = r6
        L84:
            r7 = r10 & 128(0x80, float:1.794E-43)
            if (r7 == 0) goto L79
            goto L8b
        L89:
            r3 = r6
            r4 = r3
        L8b:
            if (r4 == 0) goto L8f
            int r3 = r3 + 4
        L8f:
            long r3 = (long) r3
            int r3 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r3 >= 0) goto L98
            r1.U(r2)
            return r6
        L98:
            int r3 = r18.a()     // Catch: java.lang.Throwable -> Laf
            long r3 = (long) r3
            int r3 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
            if (r3 >= 0) goto La5
            r1.U(r2)
            return r6
        La5:
            int r3 = (int) r8
            r1.V(r3)     // Catch: java.lang.Throwable -> Laf
            goto L8
        Lab:
            r1.U(r2)
            return r4
        Laf:
            r0 = move-exception
            r1.U(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: e5.b.C(b6.z, int, int, boolean):boolean");
    }

    private static byte[] d(byte[] bArr, int i8, int i9) {
        return i9 <= i8 ? l0.f8068f : Arrays.copyOfRange(bArr, i8, i9);
    }

    private static ApicFrame f(z zVar, int i8, int i9) {
        int z4;
        String str;
        int H = zVar.H();
        Charset w8 = w(H);
        int i10 = i8 - 1;
        byte[] bArr = new byte[i10];
        zVar.l(bArr, 0, i10);
        if (i9 == 2) {
            str = "image/" + c.e(new String(bArr, 0, 3, com.google.common.base.e.f18816b));
            if ("image/jpg".equals(str)) {
                str = "image/jpeg";
            }
            z4 = 2;
        } else {
            z4 = z(bArr, 0);
            String e8 = c.e(new String(bArr, 0, z4, com.google.common.base.e.f18816b));
            if (e8.indexOf(47) == -1) {
                str = "image/" + e8;
            } else {
                str = e8;
            }
        }
        int i11 = z4 + 2;
        int y8 = y(bArr, i11, H);
        return new ApicFrame(str, new String(bArr, i11, y8 - i11, w8), bArr[z4 + 1] & 255, d(bArr, y8 + v(H), i10));
    }

    private static BinaryFrame g(z zVar, int i8, String str) {
        byte[] bArr = new byte[i8];
        zVar.l(bArr, 0, i8);
        return new BinaryFrame(str, bArr);
    }

    private static ChapterFrame h(z zVar, int i8, int i9, boolean z4, int i10, a aVar) {
        int f5 = zVar.f();
        int z8 = z(zVar.e(), f5);
        String str = new String(zVar.e(), f5, z8 - f5, com.google.common.base.e.f18816b);
        zVar.U(z8 + 1);
        int q = zVar.q();
        int q8 = zVar.q();
        long J = zVar.J();
        long j8 = J == 4294967295L ? -1L : J;
        long J2 = zVar.J();
        long j9 = J2 == 4294967295L ? -1L : J2;
        ArrayList arrayList = new ArrayList();
        int i11 = f5 + i8;
        while (zVar.f() < i11) {
            Id3Frame k8 = k(i9, zVar, z4, i10, aVar);
            if (k8 != null) {
                arrayList.add(k8);
            }
        }
        return new ChapterFrame(str, q, q8, j8, j9, (Id3Frame[]) arrayList.toArray(new Id3Frame[0]));
    }

    private static ChapterTocFrame i(z zVar, int i8, int i9, boolean z4, int i10, a aVar) {
        int f5 = zVar.f();
        int z8 = z(zVar.e(), f5);
        String str = new String(zVar.e(), f5, z8 - f5, com.google.common.base.e.f18816b);
        zVar.U(z8 + 1);
        int H = zVar.H();
        boolean z9 = (H & 2) != 0;
        boolean z10 = (H & 1) != 0;
        int H2 = zVar.H();
        String[] strArr = new String[H2];
        for (int i11 = 0; i11 < H2; i11++) {
            int f8 = zVar.f();
            int z11 = z(zVar.e(), f8);
            strArr[i11] = new String(zVar.e(), f8, z11 - f8, com.google.common.base.e.f18816b);
            zVar.U(z11 + 1);
        }
        ArrayList arrayList = new ArrayList();
        int i12 = f5 + i8;
        while (zVar.f() < i12) {
            Id3Frame k8 = k(i9, zVar, z4, i10, aVar);
            if (k8 != null) {
                arrayList.add(k8);
            }
        }
        return new ChapterTocFrame(str, z9, z10, strArr, (Id3Frame[]) arrayList.toArray(new Id3Frame[0]));
    }

    private static CommentFrame j(z zVar, int i8) {
        if (i8 < 4) {
            return null;
        }
        int H = zVar.H();
        Charset w8 = w(H);
        byte[] bArr = new byte[3];
        zVar.l(bArr, 0, 3);
        String str = new String(bArr, 0, 3);
        int i9 = i8 - 4;
        byte[] bArr2 = new byte[i9];
        zVar.l(bArr2, 0, i9);
        int y8 = y(bArr2, 0, H);
        String str2 = new String(bArr2, 0, y8, w8);
        int v8 = y8 + v(H);
        return new CommentFrame(str, str2, p(bArr2, v8, y(bArr2, v8, H), w8));
    }

    /* JADX WARN: Code restructure failed: missing block: B:131:0x0190, code lost:
        if (r13 == 67) goto L98;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.google.android.exoplayer2.metadata.id3.Id3Frame k(int r19, b6.z r20, boolean r21, int r22, e5.b.a r23) {
        /*
            Method dump skipped, instructions count: 549
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: e5.b.k(int, b6.z, boolean, int, e5.b$a):com.google.android.exoplayer2.metadata.id3.Id3Frame");
    }

    private static GeobFrame l(z zVar, int i8) {
        int H = zVar.H();
        Charset w8 = w(H);
        int i9 = i8 - 1;
        byte[] bArr = new byte[i9];
        zVar.l(bArr, 0, i9);
        int z4 = z(bArr, 0);
        String str = new String(bArr, 0, z4, com.google.common.base.e.f18816b);
        int i10 = z4 + 1;
        int y8 = y(bArr, i10, H);
        String p8 = p(bArr, i10, y8, w8);
        int v8 = y8 + v(H);
        int y9 = y(bArr, v8, H);
        return new GeobFrame(str, p8, p(bArr, v8, y9, w8), d(bArr, y9 + v(H), i9));
    }

    private static C0165b m(z zVar) {
        StringBuilder sb;
        String str;
        if (zVar.a() < 10) {
            str = "Data too short to be an ID3 tag";
        } else {
            int K = zVar.K();
            boolean z4 = false;
            if (K == 4801587) {
                int H = zVar.H();
                zVar.V(1);
                int H2 = zVar.H();
                int G = zVar.G();
                if (H == 2) {
                    if ((H2 & 64) != 0) {
                        str = "Skipped ID3 tag with majorVersion=2 and undefined compression scheme";
                    }
                } else if (H == 3) {
                    if ((H2 & 64) != 0) {
                        int q = zVar.q();
                        zVar.V(q);
                        G -= q + 4;
                    }
                } else if (H == 4) {
                    if ((H2 & 64) != 0) {
                        int G2 = zVar.G();
                        zVar.V(G2 - 4);
                        G -= G2;
                    }
                    if ((H2 & 16) != 0) {
                        G -= 10;
                    }
                } else {
                    sb = new StringBuilder();
                    sb.append("Skipped ID3 tag with unsupported majorVersion=");
                    sb.append(H);
                }
                if (H < 4 && (H2 & RecognitionOptions.ITF) != 0) {
                    z4 = true;
                }
                return new C0165b(H, z4, G);
            }
            sb = new StringBuilder();
            sb.append("Unexpected first three bytes of ID3 tag header: 0x");
            sb.append(String.format("%06X", Integer.valueOf(K)));
            str = sb.toString();
        }
        p.i("Id3Decoder", str);
        return null;
    }

    private static MlltFrame n(z zVar, int i8) {
        int N = zVar.N();
        int K = zVar.K();
        int K2 = zVar.K();
        int H = zVar.H();
        int H2 = zVar.H();
        y yVar = new y();
        yVar.m(zVar);
        int i9 = ((i8 - 10) * 8) / (H + H2);
        int[] iArr = new int[i9];
        int[] iArr2 = new int[i9];
        for (int i10 = 0; i10 < i9; i10++) {
            int h8 = yVar.h(H);
            int h9 = yVar.h(H2);
            iArr[i10] = h8;
            iArr2[i10] = h9;
        }
        return new MlltFrame(N, K, K2, iArr, iArr2);
    }

    private static PrivFrame o(z zVar, int i8) {
        byte[] bArr = new byte[i8];
        zVar.l(bArr, 0, i8);
        int z4 = z(bArr, 0);
        return new PrivFrame(new String(bArr, 0, z4, com.google.common.base.e.f18816b), d(bArr, z4 + 1, i8));
    }

    private static String p(byte[] bArr, int i8, int i9, Charset charset) {
        return (i9 <= i8 || i9 > bArr.length) ? BuildConfig.FLAVOR : new String(bArr, i8, i9 - i8, charset);
    }

    private static TextInformationFrame q(z zVar, int i8, String str) {
        if (i8 < 1) {
            return null;
        }
        int H = zVar.H();
        int i9 = i8 - 1;
        byte[] bArr = new byte[i9];
        zVar.l(bArr, 0, i9);
        return new TextInformationFrame(str, null, r(bArr, H, 0));
    }

    private static ImmutableList<String> r(byte[] bArr, int i8, int i9) {
        if (i9 >= bArr.length) {
            return ImmutableList.F(BuildConfig.FLAVOR);
        }
        ImmutableList.a u8 = ImmutableList.u();
        while (true) {
            int y8 = y(bArr, i9, i8);
            if (i9 >= y8) {
                break;
            }
            u8.a(new String(bArr, i9, y8 - i9, w(i8)));
            i9 = v(i8) + y8;
        }
        ImmutableList<String> k8 = u8.k();
        return k8.isEmpty() ? ImmutableList.F(BuildConfig.FLAVOR) : k8;
    }

    private static TextInformationFrame s(z zVar, int i8) {
        if (i8 < 1) {
            return null;
        }
        int H = zVar.H();
        int i9 = i8 - 1;
        byte[] bArr = new byte[i9];
        zVar.l(bArr, 0, i9);
        int y8 = y(bArr, 0, H);
        return new TextInformationFrame("TXXX", new String(bArr, 0, y8, w(H)), r(bArr, H, y8 + v(H)));
    }

    private static UrlLinkFrame t(z zVar, int i8, String str) {
        byte[] bArr = new byte[i8];
        zVar.l(bArr, 0, i8);
        return new UrlLinkFrame(str, null, new String(bArr, 0, z(bArr, 0), com.google.common.base.e.f18816b));
    }

    private static UrlLinkFrame u(z zVar, int i8) {
        if (i8 < 1) {
            return null;
        }
        int H = zVar.H();
        int i9 = i8 - 1;
        byte[] bArr = new byte[i9];
        zVar.l(bArr, 0, i9);
        int y8 = y(bArr, 0, H);
        String str = new String(bArr, 0, y8, w(H));
        int v8 = y8 + v(H);
        return new UrlLinkFrame("WXXX", str, p(bArr, v8, z(bArr, v8), com.google.common.base.e.f18816b));
    }

    private static int v(int i8) {
        return (i8 == 0 || i8 == 3) ? 1 : 2;
    }

    private static Charset w(int i8) {
        return i8 != 1 ? i8 != 2 ? i8 != 3 ? com.google.common.base.e.f18816b : com.google.common.base.e.f18817c : com.google.common.base.e.f18818d : com.google.common.base.e.f18820f;
    }

    private static String x(int i8, int i9, int i10, int i11, int i12) {
        return i8 == 2 ? String.format(Locale.US, "%c%c%c", Integer.valueOf(i9), Integer.valueOf(i10), Integer.valueOf(i11)) : String.format(Locale.US, "%c%c%c%c", Integer.valueOf(i9), Integer.valueOf(i10), Integer.valueOf(i11), Integer.valueOf(i12));
    }

    private static int y(byte[] bArr, int i8, int i9) {
        int z4 = z(bArr, i8);
        if (i9 == 0 || i9 == 3) {
            return z4;
        }
        while (z4 < bArr.length - 1) {
            if ((z4 - i8) % 2 == 0 && bArr[z4 + 1] == 0) {
                return z4;
            }
            z4 = z(bArr, z4 + 1);
        }
        return bArr.length;
    }

    private static int z(byte[] bArr, int i8) {
        while (i8 < bArr.length) {
            if (bArr[i8] == 0) {
                return i8;
            }
            i8++;
        }
        return bArr.length;
    }

    @Override // a5.e
    protected Metadata b(a5.c cVar, ByteBuffer byteBuffer) {
        return e(byteBuffer.array(), byteBuffer.limit());
    }

    public Metadata e(byte[] bArr, int i8) {
        ArrayList arrayList = new ArrayList();
        z zVar = new z(bArr, i8);
        C0165b m8 = m(zVar);
        if (m8 == null) {
            return null;
        }
        int f5 = zVar.f();
        int i9 = m8.f19798a == 2 ? 6 : 10;
        int i10 = m8.f19800c;
        if (m8.f19799b) {
            i10 = B(zVar, m8.f19800c);
        }
        zVar.T(f5 + i10);
        boolean z4 = false;
        if (!C(zVar, m8.f19798a, i9, false)) {
            if (m8.f19798a != 4 || !C(zVar, 4, i9, true)) {
                p.i("Id3Decoder", "Failed to validate ID3 tag with majorVersion=" + m8.f19798a);
                return null;
            }
            z4 = true;
        }
        while (zVar.a() >= i9) {
            Id3Frame k8 = k(m8.f19798a, zVar, z4, i9, this.f19797a);
            if (k8 != null) {
                arrayList.add(k8);
            }
        }
        return new Metadata(arrayList);
    }
}
