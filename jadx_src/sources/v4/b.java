package v4;

import android.util.Pair;
import b6.l0;
import b6.t;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.mp4.MdtaMetadataEntry;
import com.google.android.exoplayer2.metadata.mp4.SmtaMetadataEntry;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import n4.v;
import v4.a;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private static final byte[] f23178a = l0.m0("OpusHead");

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f23179a;

        /* renamed from: b  reason: collision with root package name */
        public int f23180b;

        /* renamed from: c  reason: collision with root package name */
        public int f23181c;

        /* renamed from: d  reason: collision with root package name */
        public long f23182d;

        /* renamed from: e  reason: collision with root package name */
        private final boolean f23183e;

        /* renamed from: f  reason: collision with root package name */
        private final z f23184f;

        /* renamed from: g  reason: collision with root package name */
        private final z f23185g;

        /* renamed from: h  reason: collision with root package name */
        private int f23186h;

        /* renamed from: i  reason: collision with root package name */
        private int f23187i;

        public a(z zVar, z zVar2, boolean z4) {
            this.f23185g = zVar;
            this.f23184f = zVar2;
            this.f23183e = z4;
            zVar2.U(12);
            this.f23179a = zVar2.L();
            zVar.U(12);
            this.f23187i = zVar.L();
            n4.n.a(zVar.q() == 1, "first_chunk must be 1");
            this.f23180b = -1;
        }

        public boolean a() {
            int i8 = this.f23180b + 1;
            this.f23180b = i8;
            if (i8 == this.f23179a) {
                return false;
            }
            this.f23182d = this.f23183e ? this.f23184f.M() : this.f23184f.J();
            if (this.f23180b == this.f23186h) {
                this.f23181c = this.f23185g.L();
                this.f23185g.V(4);
                int i9 = this.f23187i - 1;
                this.f23187i = i9;
                this.f23186h = i9 > 0 ? this.f23185g.L() - 1 : -1;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: v4.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0217b {

        /* renamed from: a  reason: collision with root package name */
        private final String f23188a;

        /* renamed from: b  reason: collision with root package name */
        private final byte[] f23189b;

        /* renamed from: c  reason: collision with root package name */
        private final long f23190c;

        /* renamed from: d  reason: collision with root package name */
        private final long f23191d;

        public C0217b(String str, byte[] bArr, long j8, long j9) {
            this.f23188a = str;
            this.f23189b = bArr;
            this.f23190c = j8;
            this.f23191d = j9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        int a();

        int b();

        int c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        public final p[] f23192a;

        /* renamed from: b  reason: collision with root package name */
        public w0 f23193b;

        /* renamed from: c  reason: collision with root package name */
        public int f23194c;

        /* renamed from: d  reason: collision with root package name */
        public int f23195d = 0;

        public d(int i8) {
            this.f23192a = new p[i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e implements c {

        /* renamed from: a  reason: collision with root package name */
        private final int f23196a;

        /* renamed from: b  reason: collision with root package name */
        private final int f23197b;

        /* renamed from: c  reason: collision with root package name */
        private final z f23198c;

        public e(a.b bVar, w0 w0Var) {
            z zVar = bVar.f23177b;
            this.f23198c = zVar;
            zVar.U(12);
            int L = zVar.L();
            if ("audio/raw".equals(w0Var.f11207m)) {
                int d02 = l0.d0(w0Var.H, w0Var.F);
                if (L == 0 || L % d02 != 0) {
                    b6.p.i("AtomParsers", "Audio sample size mismatch. stsd sample size: " + d02 + ", stsz sample size: " + L);
                    L = d02;
                }
            }
            this.f23196a = L == 0 ? -1 : L;
            this.f23197b = zVar.L();
        }

        @Override // v4.b.c
        public int a() {
            return this.f23196a;
        }

        @Override // v4.b.c
        public int b() {
            return this.f23197b;
        }

        @Override // v4.b.c
        public int c() {
            int i8 = this.f23196a;
            return i8 == -1 ? this.f23198c.L() : i8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f implements c {

        /* renamed from: a  reason: collision with root package name */
        private final z f23199a;

        /* renamed from: b  reason: collision with root package name */
        private final int f23200b;

        /* renamed from: c  reason: collision with root package name */
        private final int f23201c;

        /* renamed from: d  reason: collision with root package name */
        private int f23202d;

        /* renamed from: e  reason: collision with root package name */
        private int f23203e;

        public f(a.b bVar) {
            z zVar = bVar.f23177b;
            this.f23199a = zVar;
            zVar.U(12);
            this.f23201c = zVar.L() & 255;
            this.f23200b = zVar.L();
        }

        @Override // v4.b.c
        public int a() {
            return -1;
        }

        @Override // v4.b.c
        public int b() {
            return this.f23200b;
        }

        @Override // v4.b.c
        public int c() {
            int i8 = this.f23201c;
            if (i8 == 8) {
                return this.f23199a.H();
            }
            if (i8 == 16) {
                return this.f23199a.N();
            }
            int i9 = this.f23202d;
            this.f23202d = i9 + 1;
            if (i9 % 2 == 0) {
                int H = this.f23199a.H();
                this.f23203e = H;
                return (H & 240) >> 4;
            }
            return this.f23203e & 15;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g {

        /* renamed from: a  reason: collision with root package name */
        private final int f23204a;

        /* renamed from: b  reason: collision with root package name */
        private final long f23205b;

        /* renamed from: c  reason: collision with root package name */
        private final int f23206c;

        public g(int i8, long j8, int i9) {
            this.f23204a = i8;
            this.f23205b = j8;
            this.f23206c = i9;
        }
    }

    public static List<r> A(a.C0216a c0216a, v vVar, long j8, DrmInitData drmInitData, boolean z4, boolean z8, com.google.common.base.g<o, o> gVar) {
        o apply;
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < c0216a.f23176d.size(); i8++) {
            a.C0216a c0216a2 = c0216a.f23176d.get(i8);
            if (c0216a2.f23173a == 1953653099 && (apply = gVar.apply(z(c0216a2, (a.b) b6.a.e(c0216a.g(1836476516)), j8, drmInitData, z4, z8))) != null) {
                arrayList.add(v(apply, (a.C0216a) b6.a.e(((a.C0216a) b6.a.e(((a.C0216a) b6.a.e(c0216a2.f(1835297121))).f(1835626086))).f(1937007212)), vVar));
            }
        }
        return arrayList;
    }

    public static Pair<Metadata, Metadata> B(a.b bVar) {
        z zVar = bVar.f23177b;
        zVar.U(8);
        Metadata metadata = null;
        Metadata metadata2 = null;
        while (zVar.a() >= 8) {
            int f5 = zVar.f();
            int q = zVar.q();
            int q8 = zVar.q();
            if (q8 == 1835365473) {
                zVar.U(f5);
                metadata = C(zVar, f5 + q);
            } else if (q8 == 1936553057) {
                zVar.U(f5);
                metadata2 = u(zVar, f5 + q);
            }
            zVar.U(f5 + q);
        }
        return Pair.create(metadata, metadata2);
    }

    private static Metadata C(z zVar, int i8) {
        zVar.V(8);
        e(zVar);
        while (zVar.f() < i8) {
            int f5 = zVar.f();
            int q = zVar.q();
            if (zVar.q() == 1768715124) {
                zVar.U(f5);
                return l(zVar, f5 + q);
            }
            zVar.U(f5 + q);
        }
        return null;
    }

    private static void D(z zVar, int i8, int i9, int i10, int i11, int i12, DrmInitData drmInitData, d dVar, int i13) {
        DrmInitData drmInitData2;
        int i14;
        int i15;
        int i16;
        float f5;
        List<byte[]> list;
        int i17;
        int i18;
        String str;
        int i19 = i9;
        int i20 = i10;
        DrmInitData drmInitData3 = drmInitData;
        d dVar2 = dVar;
        zVar.U(i19 + 8 + 8);
        zVar.V(16);
        int N = zVar.N();
        int N2 = zVar.N();
        zVar.V(50);
        int f8 = zVar.f();
        int i21 = i8;
        if (i21 == 1701733238) {
            Pair<Integer, p> s8 = s(zVar, i19, i20);
            if (s8 != null) {
                i21 = ((Integer) s8.first).intValue();
                drmInitData3 = drmInitData3 == null ? null : drmInitData3.c(((p) s8.second).f23311b);
                dVar2.f23192a[i13] = (p) s8.second;
            }
            zVar.U(f8);
        }
        String str2 = "video/3gpp";
        String str3 = i21 == 1831958048 ? "video/mpeg" : i21 == 1211250227 ? "video/3gpp" : null;
        float f9 = 1.0f;
        int i22 = -1;
        String str4 = null;
        List<byte[]> list2 = null;
        byte[] bArr = null;
        int i23 = -1;
        int i24 = -1;
        int i25 = -1;
        ByteBuffer byteBuffer = null;
        C0217b c0217b = null;
        boolean z4 = false;
        while (true) {
            if (f8 - i19 >= i20) {
                drmInitData2 = drmInitData3;
                break;
            }
            zVar.U(f8);
            int f10 = zVar.f();
            String str5 = str2;
            int q = zVar.q();
            if (q == 0) {
                drmInitData2 = drmInitData3;
                if (zVar.f() - i19 == i20) {
                    break;
                }
            } else {
                drmInitData2 = drmInitData3;
            }
            n4.n.a(q > 0, "childAtomSize must be positive");
            int q8 = zVar.q();
            if (q8 == 1635148611) {
                n4.n.a(str3 == null, null);
                zVar.U(f10 + 8);
                c6.a b9 = c6.a.b(zVar);
                list2 = b9.f8325a;
                dVar2.f23194c = b9.f8326b;
                if (!z4) {
                    f9 = b9.f8329e;
                }
                str4 = b9.f8330f;
                str = "video/avc";
            } else if (q8 == 1752589123) {
                n4.n.a(str3 == null, null);
                zVar.U(f10 + 8);
                c6.f a9 = c6.f.a(zVar);
                list2 = a9.f8360a;
                dVar2.f23194c = a9.f8361b;
                if (!z4) {
                    f9 = a9.f8364e;
                }
                str4 = a9.f8368i;
                i22 = a9.f8365f;
                int i26 = a9.f8366g;
                i25 = a9.f8367h;
                i24 = i26;
                i14 = N;
                i15 = N2;
                str3 = "video/hevc";
                i16 = i21;
                f8 += q;
                i19 = i9;
                i20 = i10;
                dVar2 = dVar;
                str2 = str5;
                drmInitData3 = drmInitData2;
                i21 = i16;
                N2 = i15;
                N = i14;
            } else {
                if (q8 == 1685480259 || q8 == 1685485123) {
                    i14 = N;
                    i15 = N2;
                    i16 = i21;
                    f5 = f9;
                    list = list2;
                    i17 = i24;
                    i18 = i25;
                    c6.d a10 = c6.d.a(zVar);
                    if (a10 != null) {
                        str4 = a10.f8345c;
                        str3 = "video/dolby-vision";
                    }
                } else if (q8 == 1987076931) {
                    n4.n.a(str3 == null, null);
                    str = i21 == 1987063864 ? "video/x-vnd.on2.vp8" : "video/x-vnd.on2.vp9";
                    zVar.U(f10 + 12);
                    zVar.V(2);
                    boolean z8 = (zVar.H() & 1) != 0;
                    int H = zVar.H();
                    int H2 = zVar.H();
                    i22 = c6.c.b(H);
                    i24 = z8 ? 1 : 2;
                    i25 = c6.c.c(H2);
                } else if (q8 == 1635135811) {
                    n4.n.a(str3 == null, null);
                    str = "video/av01";
                } else if (q8 == 1668050025) {
                    ByteBuffer a11 = byteBuffer == null ? a() : byteBuffer;
                    a11.position(21);
                    a11.putShort(zVar.D());
                    a11.putShort(zVar.D());
                    byteBuffer = a11;
                    i14 = N;
                    i15 = N2;
                    i16 = i21;
                    f8 += q;
                    i19 = i9;
                    i20 = i10;
                    dVar2 = dVar;
                    str2 = str5;
                    drmInitData3 = drmInitData2;
                    i21 = i16;
                    N2 = i15;
                    N = i14;
                } else if (q8 == 1835295606) {
                    ByteBuffer a12 = byteBuffer == null ? a() : byteBuffer;
                    short D = zVar.D();
                    short D2 = zVar.D();
                    short D3 = zVar.D();
                    i16 = i21;
                    short D4 = zVar.D();
                    short D5 = zVar.D();
                    List<byte[]> list3 = list2;
                    short D6 = zVar.D();
                    float f11 = f9;
                    short D7 = zVar.D();
                    i15 = N2;
                    short D8 = zVar.D();
                    long J = zVar.J();
                    long J2 = zVar.J();
                    i14 = N;
                    a12.position(1);
                    a12.putShort(D5);
                    a12.putShort(D6);
                    a12.putShort(D);
                    a12.putShort(D2);
                    a12.putShort(D3);
                    a12.putShort(D4);
                    a12.putShort(D7);
                    a12.putShort(D8);
                    a12.putShort((short) (J / 10000));
                    a12.putShort((short) (J2 / 10000));
                    byteBuffer = a12;
                    list2 = list3;
                    f9 = f11;
                    f8 += q;
                    i19 = i9;
                    i20 = i10;
                    dVar2 = dVar;
                    str2 = str5;
                    drmInitData3 = drmInitData2;
                    i21 = i16;
                    N2 = i15;
                    N = i14;
                } else {
                    i14 = N;
                    i15 = N2;
                    i16 = i21;
                    f5 = f9;
                    list = list2;
                    if (q8 == 1681012275) {
                        n4.n.a(str3 == null, null);
                        str3 = str5;
                    } else if (q8 == 1702061171) {
                        n4.n.a(str3 == null, null);
                        c0217b = i(zVar, f10);
                        String str6 = c0217b.f23188a;
                        byte[] bArr2 = c0217b.f23189b;
                        list2 = bArr2 != null ? ImmutableList.F(bArr2) : list;
                        str3 = str6;
                        f9 = f5;
                        f8 += q;
                        i19 = i9;
                        i20 = i10;
                        dVar2 = dVar;
                        str2 = str5;
                        drmInitData3 = drmInitData2;
                        i21 = i16;
                        N2 = i15;
                        N = i14;
                    } else if (q8 == 1885434736) {
                        f9 = q(zVar, f10);
                        list2 = list;
                        z4 = true;
                        f8 += q;
                        i19 = i9;
                        i20 = i10;
                        dVar2 = dVar;
                        str2 = str5;
                        drmInitData3 = drmInitData2;
                        i21 = i16;
                        N2 = i15;
                        N = i14;
                    } else if (q8 == 1937126244) {
                        bArr = r(zVar, f10, q);
                    } else if (q8 == 1936995172) {
                        int H3 = zVar.H();
                        zVar.V(3);
                        if (H3 == 0) {
                            int H4 = zVar.H();
                            if (H4 == 0) {
                                i23 = 0;
                            } else if (H4 == 1) {
                                i23 = 1;
                            } else if (H4 == 2) {
                                i23 = 2;
                            } else if (H4 == 3) {
                                i23 = 3;
                            }
                        }
                    } else if (q8 == 1668246642 && i22 == -1) {
                        i17 = i24;
                        i18 = i25;
                        if (i17 == -1 && i18 == -1) {
                            int q9 = zVar.q();
                            if (q9 == 1852009592 || q9 == 1852009571) {
                                int N3 = zVar.N();
                                int N4 = zVar.N();
                                zVar.V(2);
                                boolean z9 = q == 19 && (zVar.H() & RecognitionOptions.ITF) != 0;
                                i22 = c6.c.b(N3);
                                i24 = z9 ? 1 : 2;
                                i25 = c6.c.c(N4);
                            } else {
                                b6.p.i("AtomParsers", "Unsupported color type: " + v4.a.a(q9));
                            }
                        }
                    } else {
                        i17 = i24;
                        i18 = i25;
                    }
                    list2 = list;
                    f9 = f5;
                    f8 += q;
                    i19 = i9;
                    i20 = i10;
                    dVar2 = dVar;
                    str2 = str5;
                    drmInitData3 = drmInitData2;
                    i21 = i16;
                    N2 = i15;
                    N = i14;
                }
                i24 = i17;
                i25 = i18;
                list2 = list;
                f9 = f5;
                f8 += q;
                i19 = i9;
                i20 = i10;
                dVar2 = dVar;
                str2 = str5;
                drmInitData3 = drmInitData2;
                i21 = i16;
                N2 = i15;
                N = i14;
            }
            str3 = str;
            i14 = N;
            i15 = N2;
            i16 = i21;
            f8 += q;
            i19 = i9;
            i20 = i10;
            dVar2 = dVar;
            str2 = str5;
            drmInitData3 = drmInitData2;
            i21 = i16;
            N2 = i15;
            N = i14;
        }
        int i27 = N;
        int i28 = N2;
        float f12 = f9;
        List<byte[]> list4 = list2;
        int i29 = i24;
        int i30 = i25;
        if (str3 == null) {
            return;
        }
        w0.b O = new w0.b().T(i11).g0(str3).K(str4).n0(i27).S(i28).c0(f12).f0(i12).d0(bArr).j0(i23).V(list4).O(drmInitData2);
        if (i22 != -1 || i29 != -1 || i30 != -1 || byteBuffer != null) {
            O.L(new c6.c(i22, i29, i30, byteBuffer != null ? byteBuffer.array() : null));
        }
        if (c0217b != null) {
            O.I(com.google.common.primitives.g.k(c0217b.f23190c)).b0(com.google.common.primitives.g.k(c0217b.f23191d));
        }
        dVar.f23193b = O.G();
    }

    private static ByteBuffer a() {
        return ByteBuffer.allocate(25).order(ByteOrder.LITTLE_ENDIAN);
    }

    private static boolean b(long[] jArr, long j8, long j9, long j10) {
        int length = jArr.length - 1;
        return jArr[0] <= j9 && j9 < jArr[l0.q(4, 0, length)] && jArr[l0.q(jArr.length - 4, 0, length)] < j10 && j10 <= j8;
    }

    private static int c(z zVar, int i8, int i9, int i10) {
        int f5 = zVar.f();
        n4.n.a(f5 >= i9, null);
        while (f5 - i9 < i10) {
            zVar.U(f5);
            int q = zVar.q();
            n4.n.a(q > 0, "childAtomSize must be positive");
            if (zVar.q() == i8) {
                return f5;
            }
            f5 += q;
        }
        return -1;
    }

    private static int d(int i8) {
        if (i8 == 1936684398) {
            return 1;
        }
        if (i8 == 1986618469) {
            return 2;
        }
        if (i8 == 1952807028 || i8 == 1935832172 || i8 == 1937072756 || i8 == 1668047728) {
            return 3;
        }
        return i8 == 1835365473 ? 5 : -1;
    }

    public static void e(z zVar) {
        int f5 = zVar.f();
        zVar.V(4);
        if (zVar.q() != 1751411826) {
            f5 += 4;
        }
        zVar.U(f5);
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0165  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void f(b6.z r22, int r23, int r24, int r25, int r26, java.lang.String r27, boolean r28, com.google.android.exoplayer2.drm.DrmInitData r29, v4.b.d r30, int r31) {
        /*
            Method dump skipped, instructions count: 874
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: v4.b.f(b6.z, int, int, int, int, java.lang.String, boolean, com.google.android.exoplayer2.drm.DrmInitData, v4.b$d, int):void");
    }

    static Pair<Integer, p> g(z zVar, int i8, int i9) {
        int i10 = i8 + 8;
        int i11 = -1;
        String str = null;
        Integer num = null;
        int i12 = 0;
        while (i10 - i8 < i9) {
            zVar.U(i10);
            int q = zVar.q();
            int q8 = zVar.q();
            if (q8 == 1718775137) {
                num = Integer.valueOf(zVar.q());
            } else if (q8 == 1935894637) {
                zVar.V(4);
                str = zVar.E(4);
            } else if (q8 == 1935894633) {
                i11 = i10;
                i12 = q;
            }
            i10 += q;
        }
        if ("cenc".equals(str) || "cbc1".equals(str) || "cens".equals(str) || "cbcs".equals(str)) {
            n4.n.a(num != null, "frma atom is mandatory");
            n4.n.a(i11 != -1, "schi atom is mandatory");
            p t8 = t(zVar, i11, i12, str);
            n4.n.a(t8 != null, "tenc atom is mandatory");
            return Pair.create(num, (p) l0.j(t8));
        }
        return null;
    }

    private static Pair<long[], long[]> h(a.C0216a c0216a) {
        a.b g8 = c0216a.g(1701606260);
        if (g8 == null) {
            return null;
        }
        z zVar = g8.f23177b;
        zVar.U(8);
        int c9 = v4.a.c(zVar.q());
        int L = zVar.L();
        long[] jArr = new long[L];
        long[] jArr2 = new long[L];
        for (int i8 = 0; i8 < L; i8++) {
            jArr[i8] = c9 == 1 ? zVar.M() : zVar.J();
            jArr2[i8] = c9 == 1 ? zVar.A() : zVar.q();
            if (zVar.D() != 1) {
                throw new IllegalArgumentException("Unsupported media rate.");
            }
            zVar.V(2);
        }
        return Pair.create(jArr, jArr2);
    }

    private static C0217b i(z zVar, int i8) {
        zVar.U(i8 + 8 + 4);
        zVar.V(1);
        j(zVar);
        zVar.V(2);
        int H = zVar.H();
        if ((H & RecognitionOptions.ITF) != 0) {
            zVar.V(2);
        }
        if ((H & 64) != 0) {
            zVar.V(zVar.H());
        }
        if ((H & 32) != 0) {
            zVar.V(2);
        }
        zVar.V(1);
        j(zVar);
        String h8 = t.h(zVar.H());
        if ("audio/mpeg".equals(h8) || "audio/vnd.dts".equals(h8) || "audio/vnd.dts.hd".equals(h8)) {
            return new C0217b(h8, null, -1L, -1L);
        }
        zVar.V(4);
        long J = zVar.J();
        long J2 = zVar.J();
        zVar.V(1);
        int j8 = j(zVar);
        byte[] bArr = new byte[j8];
        zVar.l(bArr, 0, j8);
        return new C0217b(h8, bArr, J2 > 0 ? J2 : -1L, J > 0 ? J : -1L);
    }

    private static int j(z zVar) {
        int H = zVar.H();
        int i8 = H & 127;
        while ((H & RecognitionOptions.ITF) == 128) {
            H = zVar.H();
            i8 = (i8 << 7) | (H & 127);
        }
        return i8;
    }

    private static int k(z zVar) {
        zVar.U(16);
        return zVar.q();
    }

    private static Metadata l(z zVar, int i8) {
        zVar.V(8);
        ArrayList arrayList = new ArrayList();
        while (zVar.f() < i8) {
            Metadata.Entry c9 = h.c(zVar);
            if (c9 != null) {
                arrayList.add(c9);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new Metadata(arrayList);
    }

    private static Pair<Long, String> m(z zVar) {
        zVar.U(8);
        int c9 = v4.a.c(zVar.q());
        zVar.V(c9 == 0 ? 8 : 16);
        long J = zVar.J();
        zVar.V(c9 == 0 ? 4 : 8);
        int N = zVar.N();
        return Pair.create(Long.valueOf(J), BuildConfig.FLAVOR + ((char) (((N >> 10) & 31) + 96)) + ((char) (((N >> 5) & 31) + 96)) + ((char) ((N & 31) + 96)));
    }

    public static Metadata n(a.C0216a c0216a) {
        a.b g8 = c0216a.g(1751411826);
        a.b g9 = c0216a.g(1801812339);
        a.b g10 = c0216a.g(1768715124);
        if (g8 == null || g9 == null || g10 == null || k(g8.f23177b) != 1835299937) {
            return null;
        }
        z zVar = g9.f23177b;
        zVar.U(12);
        int q = zVar.q();
        String[] strArr = new String[q];
        for (int i8 = 0; i8 < q; i8++) {
            int q8 = zVar.q();
            zVar.V(4);
            strArr[i8] = zVar.E(q8 - 8);
        }
        z zVar2 = g10.f23177b;
        zVar2.U(8);
        ArrayList arrayList = new ArrayList();
        while (zVar2.a() > 8) {
            int f5 = zVar2.f();
            int q9 = zVar2.q();
            int q10 = zVar2.q() - 1;
            if (q10 < 0 || q10 >= q) {
                b6.p.i("AtomParsers", "Skipped metadata with unknown key index: " + q10);
            } else {
                MdtaMetadataEntry f8 = h.f(zVar2, f5 + q9, strArr[q10]);
                if (f8 != null) {
                    arrayList.add(f8);
                }
            }
            zVar2.U(f5 + q9);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new Metadata(arrayList);
    }

    private static void o(z zVar, int i8, int i9, int i10, d dVar) {
        zVar.U(i9 + 8 + 8);
        if (i8 == 1835365492) {
            zVar.B();
            String B = zVar.B();
            if (B != null) {
                dVar.f23193b = new w0.b().T(i10).g0(B).G();
            }
        }
    }

    private static long p(z zVar) {
        zVar.U(8);
        zVar.V(v4.a.c(zVar.q()) != 0 ? 16 : 8);
        return zVar.J();
    }

    private static float q(z zVar, int i8) {
        zVar.U(i8 + 8);
        return zVar.L() / zVar.L();
    }

    private static byte[] r(z zVar, int i8, int i9) {
        int i10 = i8 + 8;
        while (i10 - i8 < i9) {
            zVar.U(i10);
            int q = zVar.q();
            if (zVar.q() == 1886547818) {
                return Arrays.copyOfRange(zVar.e(), i10, q + i10);
            }
            i10 += q;
        }
        return null;
    }

    private static Pair<Integer, p> s(z zVar, int i8, int i9) {
        Pair<Integer, p> g8;
        int f5 = zVar.f();
        while (f5 - i8 < i9) {
            zVar.U(f5);
            int q = zVar.q();
            n4.n.a(q > 0, "childAtomSize must be positive");
            if (zVar.q() == 1936289382 && (g8 = g(zVar, f5, q)) != null) {
                return g8;
            }
            f5 += q;
        }
        return null;
    }

    private static p t(z zVar, int i8, int i9, String str) {
        int i10;
        int i11;
        int i12 = i8 + 8;
        while (true) {
            byte[] bArr = null;
            if (i12 - i8 >= i9) {
                return null;
            }
            zVar.U(i12);
            int q = zVar.q();
            if (zVar.q() == 1952804451) {
                int c9 = v4.a.c(zVar.q());
                zVar.V(1);
                if (c9 == 0) {
                    zVar.V(1);
                    i11 = 0;
                    i10 = 0;
                } else {
                    int H = zVar.H();
                    i10 = H & 15;
                    i11 = (H & 240) >> 4;
                }
                boolean z4 = zVar.H() == 1;
                int H2 = zVar.H();
                byte[] bArr2 = new byte[16];
                zVar.l(bArr2, 0, 16);
                if (z4 && H2 == 0) {
                    int H3 = zVar.H();
                    bArr = new byte[H3];
                    zVar.l(bArr, 0, H3);
                }
                return new p(z4, str, H2, bArr2, i11, i10, bArr);
            }
            i12 += q;
        }
    }

    private static Metadata u(z zVar, int i8) {
        zVar.V(12);
        while (zVar.f() < i8) {
            int f5 = zVar.f();
            int q = zVar.q();
            if (zVar.q() == 1935766900) {
                if (q < 14) {
                    return null;
                }
                zVar.V(5);
                int H = zVar.H();
                if (H == 12 || H == 13) {
                    float f8 = H == 12 ? 240.0f : 120.0f;
                    zVar.V(1);
                    return new Metadata(new SmtaMetadataEntry(f8, zVar.H()));
                }
                return null;
            }
            zVar.U(f5 + q);
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:149:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x03ce  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x043e  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0443  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0446  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0449  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x044c  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x044f  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0451  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0455  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0458  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0467  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0433 A[EDGE_INSN: B:210:0x0433->B:169:0x0433 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static v4.r v(v4.o r38, v4.a.C0216a r39, n4.v r40) {
        /*
            Method dump skipped, instructions count: 1321
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: v4.b.v(v4.o, v4.a$a, n4.v):v4.r");
    }

    private static d w(z zVar, int i8, int i9, String str, DrmInitData drmInitData, boolean z4) {
        int i10;
        zVar.U(12);
        int q = zVar.q();
        d dVar = new d(q);
        for (int i11 = 0; i11 < q; i11++) {
            int f5 = zVar.f();
            int q8 = zVar.q();
            n4.n.a(q8 > 0, "childAtomSize must be positive");
            int q9 = zVar.q();
            if (q9 == 1635148593 || q9 == 1635148595 || q9 == 1701733238 || q9 == 1831958048 || q9 == 1836070006 || q9 == 1752589105 || q9 == 1751479857 || q9 == 1932670515 || q9 == 1211250227 || q9 == 1987063864 || q9 == 1987063865 || q9 == 1635135537 || q9 == 1685479798 || q9 == 1685479729 || q9 == 1685481573 || q9 == 1685481521) {
                i10 = f5;
                D(zVar, q9, i10, q8, i8, i9, drmInitData, dVar, i11);
            } else if (q9 == 1836069985 || q9 == 1701733217 || q9 == 1633889587 || q9 == 1700998451 || q9 == 1633889588 || q9 == 1835823201 || q9 == 1685353315 || q9 == 1685353317 || q9 == 1685353320 || q9 == 1685353324 || q9 == 1685353336 || q9 == 1935764850 || q9 == 1935767394 || q9 == 1819304813 || q9 == 1936684916 || q9 == 1953984371 || q9 == 778924082 || q9 == 778924083 || q9 == 1835557169 || q9 == 1835560241 || q9 == 1634492771 || q9 == 1634492791 || q9 == 1970037111 || q9 == 1332770163 || q9 == 1716281667) {
                i10 = f5;
                f(zVar, q9, f5, q8, i8, str, z4, drmInitData, dVar, i11);
            } else {
                if (q9 == 1414810956 || q9 == 1954034535 || q9 == 2004251764 || q9 == 1937010800 || q9 == 1664495672) {
                    x(zVar, q9, f5, q8, i8, str, dVar);
                } else if (q9 == 1835365492) {
                    o(zVar, q9, f5, i8, dVar);
                } else if (q9 == 1667329389) {
                    dVar.f23193b = new w0.b().T(i8).g0("application/x-camera-motion").G();
                }
                i10 = f5;
            }
            zVar.U(i10 + q8);
        }
        return dVar;
    }

    private static void x(z zVar, int i8, int i9, int i10, int i11, String str, d dVar) {
        zVar.U(i9 + 8 + 8);
        String str2 = "application/ttml+xml";
        ImmutableList immutableList = null;
        long j8 = Long.MAX_VALUE;
        if (i8 != 1414810956) {
            if (i8 == 1954034535) {
                int i12 = (i10 - 8) - 8;
                byte[] bArr = new byte[i12];
                zVar.l(bArr, 0, i12);
                immutableList = ImmutableList.F(bArr);
                str2 = "application/x-quicktime-tx3g";
            } else if (i8 == 2004251764) {
                str2 = "application/x-mp4-vtt";
            } else if (i8 == 1937010800) {
                j8 = 0;
            } else if (i8 != 1664495672) {
                throw new IllegalStateException();
            } else {
                dVar.f23195d = 1;
                str2 = "application/x-mp4-cea-608";
            }
        }
        dVar.f23193b = new w0.b().T(i11).g0(str2).X(str).k0(j8).V(immutableList).G();
    }

    private static g y(z zVar) {
        boolean z4;
        zVar.U(8);
        int c9 = v4.a.c(zVar.q());
        zVar.V(c9 == 0 ? 8 : 16);
        int q = zVar.q();
        zVar.V(4);
        int f5 = zVar.f();
        int i8 = c9 == 0 ? 4 : 8;
        int i9 = 0;
        int i10 = 0;
        while (true) {
            if (i10 >= i8) {
                z4 = true;
                break;
            } else if (zVar.e()[f5 + i10] != -1) {
                z4 = false;
                break;
            } else {
                i10++;
            }
        }
        long j8 = -9223372036854775807L;
        if (z4) {
            zVar.V(i8);
        } else {
            long J = c9 == 0 ? zVar.J() : zVar.M();
            if (J != 0) {
                j8 = J;
            }
        }
        zVar.V(16);
        int q8 = zVar.q();
        int q9 = zVar.q();
        zVar.V(4);
        int q10 = zVar.q();
        int q11 = zVar.q();
        if (q8 == 0 && q9 == 65536 && q10 == -65536 && q11 == 0) {
            i9 = 90;
        } else if (q8 == 0 && q9 == -65536 && q10 == 65536 && q11 == 0) {
            i9 = 270;
        } else if (q8 == -65536 && q9 == 0 && q10 == 0 && q11 == -65536) {
            i9 = 180;
        }
        return new g(q, j8, i9);
    }

    private static o z(a.C0216a c0216a, a.b bVar, long j8, DrmInitData drmInitData, boolean z4, boolean z8) {
        a.b bVar2;
        long j9;
        long[] jArr;
        long[] jArr2;
        a.C0216a f5;
        Pair<long[], long[]> h8;
        a.C0216a c0216a2 = (a.C0216a) b6.a.e(c0216a.f(1835297121));
        int d8 = d(k(((a.b) b6.a.e(c0216a2.g(1751411826))).f23177b));
        if (d8 == -1) {
            return null;
        }
        g y8 = y(((a.b) b6.a.e(c0216a.g(1953196132))).f23177b);
        if (j8 == -9223372036854775807L) {
            bVar2 = bVar;
            j9 = y8.f23205b;
        } else {
            bVar2 = bVar;
            j9 = j8;
        }
        long p8 = p(bVar2.f23177b);
        long O0 = j9 != -9223372036854775807L ? l0.O0(j9, 1000000L, p8) : -9223372036854775807L;
        Pair<Long, String> m8 = m(((a.b) b6.a.e(c0216a2.g(1835296868))).f23177b);
        a.b g8 = ((a.C0216a) b6.a.e(((a.C0216a) b6.a.e(c0216a2.f(1835626086))).f(1937007212))).g(1937011556);
        if (g8 != null) {
            d w8 = w(g8.f23177b, y8.f23204a, y8.f23206c, (String) m8.second, drmInitData, z8);
            if (z4 || (f5 = c0216a.f(1701082227)) == null || (h8 = h(f5)) == null) {
                jArr = null;
                jArr2 = null;
            } else {
                jArr2 = (long[]) h8.second;
                jArr = (long[]) h8.first;
            }
            if (w8.f23193b == null) {
                return null;
            }
            return new o(y8.f23204a, d8, ((Long) m8.first).longValue(), p8, O0, w8.f23193b, w8.f23195d, w8.f23192a, w8.f23194c, jArr, jArr2);
        }
        throw ParserException.a("Malformed sample table (stbl) missing sample description (stsd)", null);
    }
}
