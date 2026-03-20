package t4;

import android.util.Pair;
import android.util.SparseArray;
import b6.l0;
import b6.q;
import b6.u;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import n4.b0;
import n4.c0;
import n4.k;
import n4.l;
import n4.m;
import n4.p;
import n4.y;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e implements k {

    /* renamed from: c0  reason: collision with root package name */
    public static final p f22858c0 = d.b;

    /* renamed from: d0  reason: collision with root package name */
    private static final byte[] f22859d0 = {49, 10, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 10};

    /* renamed from: e0  reason: collision with root package name */
    private static final byte[] f22860e0 = l0.m0("Format: Start, End, ReadOrder, Layer, Style, Name, MarginL, MarginR, MarginV, Effect, Text");

    /* renamed from: f0  reason: collision with root package name */
    private static final byte[] f22861f0 = {68, 105, 97, 108, 111, 103, 117, 101, 58, 32, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44};

    /* renamed from: g0  reason: collision with root package name */
    private static final byte[] f22862g0 = {87, 69, 66, 86, 84, 84, 10, 10, 48, 48, 58, 48, 48, 58, 48, 48, 46, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 46, 48, 48, 48, 10};

    /* renamed from: h0  reason: collision with root package name */
    private static final UUID f22863h0 = new UUID(72057594037932032L, -9223371306706625679L);

    /* renamed from: i0  reason: collision with root package name */
    private static final Map<String, Integer> f22864i0;
    private long A;
    private long B;
    private q C;
    private q D;
    private boolean E;
    private boolean F;
    private int G;
    private long H;
    private long I;
    private int J;
    private int K;
    private int[] L;
    private int M;
    private int N;
    private int O;
    private int P;
    private boolean Q;
    private long R;
    private int S;
    private int T;
    private int U;
    private boolean V;
    private boolean W;
    private boolean X;
    private int Y;
    private byte Z;

    /* renamed from: a  reason: collision with root package name */
    private final t4.c f22865a;

    /* renamed from: a0  reason: collision with root package name */
    private boolean f22866a0;

    /* renamed from: b  reason: collision with root package name */
    private final g f22867b;

    /* renamed from: b0  reason: collision with root package name */
    private m f22868b0;

    /* renamed from: c  reason: collision with root package name */
    private final SparseArray<c> f22869c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f22870d;

    /* renamed from: e  reason: collision with root package name */
    private final z f22871e;

    /* renamed from: f  reason: collision with root package name */
    private final z f22872f;

    /* renamed from: g  reason: collision with root package name */
    private final z f22873g;

    /* renamed from: h  reason: collision with root package name */
    private final z f22874h;

    /* renamed from: i  reason: collision with root package name */
    private final z f22875i;

    /* renamed from: j  reason: collision with root package name */
    private final z f22876j;

    /* renamed from: k  reason: collision with root package name */
    private final z f22877k;

    /* renamed from: l  reason: collision with root package name */
    private final z f22878l;

    /* renamed from: m  reason: collision with root package name */
    private final z f22879m;

    /* renamed from: n  reason: collision with root package name */
    private final z f22880n;

    /* renamed from: o  reason: collision with root package name */
    private ByteBuffer f22881o;

    /* renamed from: p  reason: collision with root package name */
    private long f22882p;
    private long q;

    /* renamed from: r  reason: collision with root package name */
    private long f22883r;

    /* renamed from: s  reason: collision with root package name */
    private long f22884s;

    /* renamed from: t  reason: collision with root package name */
    private long f22885t;

    /* renamed from: u  reason: collision with root package name */
    private c f22886u;

    /* renamed from: v  reason: collision with root package name */
    private boolean f22887v;

    /* renamed from: w  reason: collision with root package name */
    private int f22888w;

    /* renamed from: x  reason: collision with root package name */
    private long f22889x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f22890y;

    /* renamed from: z  reason: collision with root package name */
    private long f22891z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class b implements t4.b {
        private b() {
        }

        @Override // t4.b
        public void a(int i8) {
            e.this.o(i8);
        }

        @Override // t4.b
        public int b(int i8) {
            return e.this.u(i8);
        }

        @Override // t4.b
        public boolean c(int i8) {
            return e.this.z(i8);
        }

        @Override // t4.b
        public void d(int i8, String str) {
            e.this.H(i8, str);
        }

        @Override // t4.b
        public void e(int i8, int i9, l lVar) {
            e.this.l(i8, i9, lVar);
        }

        @Override // t4.b
        public void f(int i8, double d8) {
            e.this.r(i8, d8);
        }

        @Override // t4.b
        public void g(int i8, long j8, long j9) {
            e.this.G(i8, j8, j9);
        }

        @Override // t4.b
        public void h(int i8, long j8) {
            e.this.x(i8, j8);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {
        public byte[] N;
        public c0 T;
        public boolean U;
        public b0 X;
        public int Y;

        /* renamed from: a  reason: collision with root package name */
        public String f22893a;

        /* renamed from: b  reason: collision with root package name */
        public String f22894b;

        /* renamed from: c  reason: collision with root package name */
        public int f22895c;

        /* renamed from: d  reason: collision with root package name */
        public int f22896d;

        /* renamed from: e  reason: collision with root package name */
        public int f22897e;

        /* renamed from: f  reason: collision with root package name */
        public int f22898f;

        /* renamed from: g  reason: collision with root package name */
        private int f22899g;

        /* renamed from: h  reason: collision with root package name */
        public boolean f22900h;

        /* renamed from: i  reason: collision with root package name */
        public byte[] f22901i;

        /* renamed from: j  reason: collision with root package name */
        public b0.a f22902j;

        /* renamed from: k  reason: collision with root package name */
        public byte[] f22903k;

        /* renamed from: l  reason: collision with root package name */
        public DrmInitData f22904l;

        /* renamed from: m  reason: collision with root package name */
        public int f22905m = -1;

        /* renamed from: n  reason: collision with root package name */
        public int f22906n = -1;

        /* renamed from: o  reason: collision with root package name */
        public int f22907o = -1;

        /* renamed from: p  reason: collision with root package name */
        public int f22908p = -1;
        public int q = 0;

        /* renamed from: r  reason: collision with root package name */
        public int f22909r = -1;

        /* renamed from: s  reason: collision with root package name */
        public float f22910s = 0.0f;

        /* renamed from: t  reason: collision with root package name */
        public float f22911t = 0.0f;

        /* renamed from: u  reason: collision with root package name */
        public float f22912u = 0.0f;

        /* renamed from: v  reason: collision with root package name */
        public byte[] f22913v = null;

        /* renamed from: w  reason: collision with root package name */
        public int f22914w = -1;

        /* renamed from: x  reason: collision with root package name */
        public boolean f22915x = false;

        /* renamed from: y  reason: collision with root package name */
        public int f22916y = -1;

        /* renamed from: z  reason: collision with root package name */
        public int f22917z = -1;
        public int A = -1;
        public int B = 1000;
        public int C = 200;
        public float D = -1.0f;
        public float E = -1.0f;
        public float F = -1.0f;
        public float G = -1.0f;
        public float H = -1.0f;
        public float I = -1.0f;
        public float J = -1.0f;
        public float K = -1.0f;
        public float L = -1.0f;
        public float M = -1.0f;
        public int O = 1;
        public int P = -1;
        public int Q = 8000;
        public long R = 0;
        public long S = 0;
        public boolean V = true;
        private String W = "eng";

        protected c() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void f() {
            b6.a.e(this.X);
        }

        private byte[] g(String str) {
            byte[] bArr = this.f22903k;
            if (bArr != null) {
                return bArr;
            }
            throw ParserException.a("Missing CodecPrivate for codec " + str, null);
        }

        private byte[] h() {
            if (this.D == -1.0f || this.E == -1.0f || this.F == -1.0f || this.G == -1.0f || this.H == -1.0f || this.I == -1.0f || this.J == -1.0f || this.K == -1.0f || this.L == -1.0f || this.M == -1.0f) {
                return null;
            }
            byte[] bArr = new byte[25];
            ByteBuffer order = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
            order.put((byte) 0);
            order.putShort((short) ((this.D * 50000.0f) + 0.5f));
            order.putShort((short) ((this.E * 50000.0f) + 0.5f));
            order.putShort((short) ((this.F * 50000.0f) + 0.5f));
            order.putShort((short) ((this.G * 50000.0f) + 0.5f));
            order.putShort((short) ((this.H * 50000.0f) + 0.5f));
            order.putShort((short) ((this.I * 50000.0f) + 0.5f));
            order.putShort((short) ((this.J * 50000.0f) + 0.5f));
            order.putShort((short) ((this.K * 50000.0f) + 0.5f));
            order.putShort((short) (this.L + 0.5f));
            order.putShort((short) (this.M + 0.5f));
            order.putShort((short) this.B);
            order.putShort((short) this.C);
            return bArr;
        }

        private static Pair<String, List<byte[]>> k(z zVar) {
            try {
                zVar.V(16);
                long x8 = zVar.x();
                if (x8 == 1482049860) {
                    return new Pair<>("video/divx", null);
                }
                if (x8 == 859189832) {
                    return new Pair<>("video/3gpp", null);
                }
                if (x8 != 826496599) {
                    b6.p.i("MatroskaExtractor", "Unknown FourCC. Setting mimeType to video/x-unknown");
                    return new Pair<>("video/x-unknown", null);
                }
                byte[] e8 = zVar.e();
                for (int f5 = zVar.f() + 20; f5 < e8.length - 4; f5++) {
                    if (e8[f5] == 0 && e8[f5 + 1] == 0 && e8[f5 + 2] == 1 && e8[f5 + 3] == 15) {
                        return new Pair<>("video/wvc1", Collections.singletonList(Arrays.copyOfRange(e8, f5, e8.length)));
                    }
                }
                throw ParserException.a("Failed to find FourCC VC1 initialization data", null);
            } catch (ArrayIndexOutOfBoundsException unused) {
                throw ParserException.a("Error parsing FourCC private data", null);
            }
        }

        private static boolean l(z zVar) {
            try {
                int z4 = zVar.z();
                if (z4 == 1) {
                    return true;
                }
                if (z4 == 65534) {
                    zVar.U(24);
                    if (zVar.A() == e.f22863h0.getMostSignificantBits()) {
                        if (zVar.A() == e.f22863h0.getLeastSignificantBits()) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            } catch (ArrayIndexOutOfBoundsException unused) {
                throw ParserException.a("Error parsing MS/ACM codec private", null);
            }
        }

        private static List<byte[]> m(byte[] bArr) {
            try {
                if (bArr[0] == 2) {
                    int i8 = 0;
                    int i9 = 1;
                    while ((bArr[i9] & 255) == 255) {
                        i8 += 255;
                        i9++;
                    }
                    int i10 = i9 + 1;
                    int i11 = i8 + (bArr[i9] & 255);
                    int i12 = 0;
                    while ((bArr[i10] & 255) == 255) {
                        i12 += 255;
                        i10++;
                    }
                    int i13 = i10 + 1;
                    int i14 = i12 + (bArr[i10] & 255);
                    if (bArr[i13] == 1) {
                        byte[] bArr2 = new byte[i11];
                        System.arraycopy(bArr, i13, bArr2, 0, i11);
                        int i15 = i13 + i11;
                        if (bArr[i15] == 3) {
                            int i16 = i15 + i14;
                            if (bArr[i16] == 5) {
                                byte[] bArr3 = new byte[bArr.length - i16];
                                System.arraycopy(bArr, i16, bArr3, 0, bArr.length - i16);
                                ArrayList arrayList = new ArrayList(2);
                                arrayList.add(bArr2);
                                arrayList.add(bArr3);
                                return arrayList;
                            }
                            throw ParserException.a("Error parsing vorbis codec private", null);
                        }
                        throw ParserException.a("Error parsing vorbis codec private", null);
                    }
                    throw ParserException.a("Error parsing vorbis codec private", null);
                }
                throw ParserException.a("Error parsing vorbis codec private", null);
            } catch (ArrayIndexOutOfBoundsException unused) {
                throw ParserException.a("Error parsing vorbis codec private", null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean o(boolean z4) {
            return "A_OPUS".equals(this.f22894b) ? z4 : this.f22898f > 0;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:203:0x03cb  */
        /* JADX WARN: Removed duplicated region for block: B:208:0x03e6  */
        /* JADX WARN: Removed duplicated region for block: B:209:0x03e8  */
        /* JADX WARN: Removed duplicated region for block: B:212:0x03f5  */
        /* JADX WARN: Removed duplicated region for block: B:213:0x0407  */
        /* JADX WARN: Removed duplicated region for block: B:278:0x0510  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void i(n4.m r20, int r21) {
            /*
                Method dump skipped, instructions count: 1574
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: t4.e.c.i(n4.m, int):void");
        }

        public void j() {
            c0 c0Var = this.T;
            if (c0Var != null) {
                c0Var.a(this.X, this.f22902j);
            }
        }

        public void n() {
            c0 c0Var = this.T;
            if (c0Var != null) {
                c0Var.b();
            }
        }
    }

    static {
        HashMap hashMap = new HashMap();
        hashMap.put("htc_video_rotA-000", 0);
        hashMap.put("htc_video_rotA-090", 90);
        hashMap.put("htc_video_rotA-180", 180);
        hashMap.put("htc_video_rotA-270", 270);
        f22864i0 = Collections.unmodifiableMap(hashMap);
    }

    public e() {
        this(0);
    }

    public e(int i8) {
        this(new t4.a(), i8);
    }

    e(t4.c cVar, int i8) {
        this.q = -1L;
        this.f22883r = -9223372036854775807L;
        this.f22884s = -9223372036854775807L;
        this.f22885t = -9223372036854775807L;
        this.f22891z = -1L;
        this.A = -1L;
        this.B = -9223372036854775807L;
        this.f22865a = cVar;
        cVar.b(new b());
        this.f22870d = (i8 & 1) == 0;
        this.f22867b = new g();
        this.f22869c = new SparseArray<>();
        this.f22873g = new z(4);
        this.f22874h = new z(ByteBuffer.allocate(4).putInt(-1).array());
        this.f22875i = new z(4);
        this.f22871e = new z(u.f8109a);
        this.f22872f = new z(4);
        this.f22876j = new z();
        this.f22877k = new z();
        this.f22878l = new z(8);
        this.f22879m = new z();
        this.f22880n = new z();
        this.L = new int[1];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ k[] A() {
        return new k[]{new e()};
    }

    private boolean B(y yVar, long j8) {
        if (this.f22890y) {
            this.A = j8;
            yVar.f22152a = this.f22891z;
            this.f22890y = false;
            return true;
        }
        if (this.f22887v) {
            long j9 = this.A;
            if (j9 != -1) {
                yVar.f22152a = j9;
                this.A = -1L;
                return true;
            }
        }
        return false;
    }

    private void C(l lVar, int i8) {
        if (this.f22873g.g() >= i8) {
            return;
        }
        if (this.f22873g.b() < i8) {
            z zVar = this.f22873g;
            zVar.c(Math.max(zVar.b() * 2, i8));
        }
        lVar.readFully(this.f22873g.e(), this.f22873g.g(), i8 - this.f22873g.g());
        this.f22873g.T(i8);
    }

    private void D() {
        this.S = 0;
        this.T = 0;
        this.U = 0;
        this.V = false;
        this.W = false;
        this.X = false;
        this.Y = 0;
        this.Z = (byte) 0;
        this.f22866a0 = false;
        this.f22876j.Q(0);
    }

    private long E(long j8) {
        long j9 = this.f22883r;
        if (j9 != -9223372036854775807L) {
            return l0.O0(j8, j9, 1000L);
        }
        throw ParserException.a("Can't scale timecode prior to timecodeScale being set.", null);
    }

    private static void F(String str, long j8, byte[] bArr) {
        byte[] s8;
        int i8;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case 738597099:
                if (str.equals("S_TEXT/ASS")) {
                    c9 = 0;
                    break;
                }
                break;
            case 1045209816:
                if (str.equals("S_TEXT/WEBVTT")) {
                    c9 = 1;
                    break;
                }
                break;
            case 1422270023:
                if (str.equals("S_TEXT/UTF8")) {
                    c9 = 2;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                s8 = s(j8, "%01d:%02d:%02d:%02d", 10000L);
                i8 = 21;
                break;
            case 1:
                s8 = s(j8, "%02d:%02d:%02d.%03d", 1000L);
                i8 = 25;
                break;
            case 2:
                s8 = s(j8, "%02d:%02d:%02d,%03d", 1000L);
                i8 = 19;
                break;
            default:
                throw new IllegalArgumentException();
        }
        System.arraycopy(s8, 0, bArr, i8, s8.length);
    }

    private int I(l lVar, c cVar, int i8, boolean z4) {
        int i9;
        if ("S_TEXT/UTF8".equals(cVar.f22894b)) {
            J(lVar, f22859d0, i8);
        } else if ("S_TEXT/ASS".equals(cVar.f22894b)) {
            J(lVar, f22861f0, i8);
        } else if (!"S_TEXT/WEBVTT".equals(cVar.f22894b)) {
            b0 b0Var = cVar.X;
            if (!this.V) {
                if (cVar.f22900h) {
                    this.O &= -1073741825;
                    boolean z8 = this.W;
                    int i10 = RecognitionOptions.ITF;
                    if (!z8) {
                        lVar.readFully(this.f22873g.e(), 0, 1);
                        this.S++;
                        if ((this.f22873g.e()[0] & 128) == 128) {
                            throw ParserException.a("Extension bit is set in signal byte", null);
                        }
                        this.Z = this.f22873g.e()[0];
                        this.W = true;
                    }
                    byte b9 = this.Z;
                    if ((b9 & 1) == 1) {
                        boolean z9 = (b9 & 2) == 2;
                        this.O |= 1073741824;
                        if (!this.f22866a0) {
                            lVar.readFully(this.f22878l.e(), 0, 8);
                            this.S += 8;
                            this.f22866a0 = true;
                            byte[] e8 = this.f22873g.e();
                            if (!z9) {
                                i10 = 0;
                            }
                            e8[0] = (byte) (i10 | 8);
                            this.f22873g.U(0);
                            b0Var.e(this.f22873g, 1, 1);
                            this.T++;
                            this.f22878l.U(0);
                            b0Var.e(this.f22878l, 8, 1);
                            this.T += 8;
                        }
                        if (z9) {
                            if (!this.X) {
                                lVar.readFully(this.f22873g.e(), 0, 1);
                                this.S++;
                                this.f22873g.U(0);
                                this.Y = this.f22873g.H();
                                this.X = true;
                            }
                            int i11 = this.Y * 4;
                            this.f22873g.Q(i11);
                            lVar.readFully(this.f22873g.e(), 0, i11);
                            this.S += i11;
                            short s8 = (short) ((this.Y / 2) + 1);
                            int i12 = (s8 * 6) + 2;
                            ByteBuffer byteBuffer = this.f22881o;
                            if (byteBuffer == null || byteBuffer.capacity() < i12) {
                                this.f22881o = ByteBuffer.allocate(i12);
                            }
                            this.f22881o.position(0);
                            this.f22881o.putShort(s8);
                            int i13 = 0;
                            int i14 = 0;
                            while (true) {
                                i9 = this.Y;
                                if (i13 >= i9) {
                                    break;
                                }
                                int L = this.f22873g.L();
                                if (i13 % 2 == 0) {
                                    this.f22881o.putShort((short) (L - i14));
                                } else {
                                    this.f22881o.putInt(L - i14);
                                }
                                i13++;
                                i14 = L;
                            }
                            int i15 = (i8 - this.S) - i14;
                            int i16 = i9 % 2;
                            ByteBuffer byteBuffer2 = this.f22881o;
                            if (i16 == 1) {
                                byteBuffer2.putInt(i15);
                            } else {
                                byteBuffer2.putShort((short) i15);
                                this.f22881o.putInt(0);
                            }
                            this.f22879m.S(this.f22881o.array(), i12);
                            b0Var.e(this.f22879m, i12, 1);
                            this.T += i12;
                        }
                    }
                } else {
                    byte[] bArr = cVar.f22901i;
                    if (bArr != null) {
                        this.f22876j.S(bArr, bArr.length);
                    }
                }
                if (cVar.o(z4)) {
                    this.O |= 268435456;
                    this.f22880n.Q(0);
                    int g8 = (this.f22876j.g() + i8) - this.S;
                    this.f22873g.Q(4);
                    this.f22873g.e()[0] = (byte) ((g8 >> 24) & 255);
                    this.f22873g.e()[1] = (byte) ((g8 >> 16) & 255);
                    this.f22873g.e()[2] = (byte) ((g8 >> 8) & 255);
                    this.f22873g.e()[3] = (byte) (g8 & 255);
                    b0Var.e(this.f22873g, 4, 2);
                    this.T += 4;
                }
                this.V = true;
            }
            int g9 = i8 + this.f22876j.g();
            if (!"V_MPEG4/ISO/AVC".equals(cVar.f22894b) && !"V_MPEGH/ISO/HEVC".equals(cVar.f22894b)) {
                if (cVar.T != null) {
                    b6.a.f(this.f22876j.g() == 0);
                    cVar.T.d(lVar);
                }
                while (true) {
                    int i17 = this.S;
                    if (i17 >= g9) {
                        break;
                    }
                    int K = K(lVar, b0Var, g9 - i17);
                    this.S += K;
                    this.T += K;
                }
            } else {
                byte[] e9 = this.f22872f.e();
                e9[0] = 0;
                e9[1] = 0;
                e9[2] = 0;
                int i18 = cVar.Y;
                int i19 = 4 - i18;
                while (this.S < g9) {
                    int i20 = this.U;
                    if (i20 == 0) {
                        L(lVar, e9, i19, i18);
                        this.S += i18;
                        this.f22872f.U(0);
                        this.U = this.f22872f.L();
                        this.f22871e.U(0);
                        b0Var.b(this.f22871e, 4);
                        this.T += 4;
                    } else {
                        int K2 = K(lVar, b0Var, i20);
                        this.S += K2;
                        this.T += K2;
                        this.U -= K2;
                    }
                }
            }
            if ("A_VORBIS".equals(cVar.f22894b)) {
                this.f22874h.U(0);
                b0Var.b(this.f22874h, 4);
                this.T += 4;
            }
            return q();
        } else {
            J(lVar, f22862g0, i8);
        }
        return q();
    }

    private void J(l lVar, byte[] bArr, int i8) {
        int length = bArr.length + i8;
        if (this.f22877k.b() < length) {
            this.f22877k.R(Arrays.copyOf(bArr, length + i8));
        } else {
            System.arraycopy(bArr, 0, this.f22877k.e(), 0, bArr.length);
        }
        lVar.readFully(this.f22877k.e(), bArr.length, i8);
        this.f22877k.U(0);
        this.f22877k.T(length);
    }

    private int K(l lVar, b0 b0Var, int i8) {
        int a9 = this.f22876j.a();
        if (a9 > 0) {
            int min = Math.min(i8, a9);
            b0Var.b(this.f22876j, min);
            return min;
        }
        return b0Var.c(lVar, i8, false);
    }

    private void L(l lVar, byte[] bArr, int i8, int i9) {
        int min = Math.min(i9, this.f22876j.a());
        lVar.readFully(bArr, i8 + min, i9 - min);
        if (min > 0) {
            this.f22876j.l(bArr, i8, min);
        }
    }

    private void i(int i8) {
        if (this.C == null || this.D == null) {
            throw ParserException.a("Element " + i8 + " must be in a Cues", null);
        }
    }

    private void j(int i8) {
        if (this.f22886u != null) {
            return;
        }
        throw ParserException.a("Element " + i8 + " must be in a TrackEntry", null);
    }

    private void k() {
        b6.a.h(this.f22868b0);
    }

    private n4.z m(q qVar, q qVar2) {
        int i8;
        if (this.q == -1 || this.f22885t == -9223372036854775807L || qVar == null || qVar.c() == 0 || qVar2 == null || qVar2.c() != qVar.c()) {
            return new z.b(this.f22885t);
        }
        int c9 = qVar.c();
        int[] iArr = new int[c9];
        long[] jArr = new long[c9];
        long[] jArr2 = new long[c9];
        long[] jArr3 = new long[c9];
        int i9 = 0;
        for (int i10 = 0; i10 < c9; i10++) {
            jArr3[i10] = qVar.b(i10);
            jArr[i10] = this.q + qVar2.b(i10);
        }
        while (true) {
            i8 = c9 - 1;
            if (i9 >= i8) {
                break;
            }
            int i11 = i9 + 1;
            iArr[i9] = (int) (jArr[i11] - jArr[i9]);
            jArr2[i9] = jArr3[i11] - jArr3[i9];
            i9 = i11;
        }
        iArr[i8] = (int) ((this.q + this.f22882p) - jArr[i8]);
        jArr2[i8] = this.f22885t - jArr3[i8];
        long j8 = jArr2[i8];
        if (j8 <= 0) {
            b6.p.i("MatroskaExtractor", "Discarding last cue point with unexpected duration: " + j8);
            iArr = Arrays.copyOf(iArr, i8);
            jArr = Arrays.copyOf(jArr, i8);
            jArr2 = Arrays.copyOf(jArr2, i8);
            jArr3 = Arrays.copyOf(jArr3, i8);
        }
        return new n4.c(iArr, jArr, jArr2, jArr3);
    }

    private void n(c cVar, long j8, int i8, int i9, int i10) {
        String str;
        c0 c0Var = cVar.T;
        if (c0Var != null) {
            c0Var.c(cVar.X, j8, i8, i9, i10, cVar.f22902j);
        } else {
            if ("S_TEXT/UTF8".equals(cVar.f22894b) || "S_TEXT/ASS".equals(cVar.f22894b) || "S_TEXT/WEBVTT".equals(cVar.f22894b)) {
                if (this.K > 1) {
                    str = "Skipping subtitle sample in laced block.";
                } else {
                    long j9 = this.I;
                    if (j9 == -9223372036854775807L) {
                        str = "Skipping subtitle sample with no duration.";
                    } else {
                        F(cVar.f22894b, j9, this.f22877k.e());
                        int f5 = this.f22877k.f();
                        while (true) {
                            if (f5 >= this.f22877k.g()) {
                                break;
                            } else if (this.f22877k.e()[f5] == 0) {
                                this.f22877k.T(f5);
                                break;
                            } else {
                                f5++;
                            }
                        }
                        b0 b0Var = cVar.X;
                        b6.z zVar = this.f22877k;
                        b0Var.b(zVar, zVar.g());
                        i9 += this.f22877k.g();
                    }
                }
                b6.p.i("MatroskaExtractor", str);
            }
            if ((268435456 & i8) != 0) {
                if (this.K > 1) {
                    this.f22880n.Q(0);
                } else {
                    int g8 = this.f22880n.g();
                    cVar.X.e(this.f22880n, g8, 2);
                    i9 += g8;
                }
            }
            cVar.X.d(j8, i8, i9, i10, cVar.f22902j);
        }
        this.F = true;
    }

    private static int[] p(int[] iArr, int i8) {
        return iArr == null ? new int[i8] : iArr.length >= i8 ? iArr : new int[Math.max(iArr.length * 2, i8)];
    }

    private int q() {
        int i8 = this.T;
        D();
        return i8;
    }

    private static byte[] s(long j8, String str, long j9) {
        b6.a.a(j8 != -9223372036854775807L);
        int i8 = (int) (j8 / 3600000000L);
        long j10 = j8 - ((i8 * 3600) * 1000000);
        int i9 = (int) (j10 / 60000000);
        long j11 = j10 - ((i9 * 60) * 1000000);
        int i10 = (int) (j11 / 1000000);
        return l0.m0(String.format(Locale.US, str, Integer.valueOf(i8), Integer.valueOf(i9), Integer.valueOf(i10), Integer.valueOf((int) ((j11 - (i10 * 1000000)) / j9))));
    }

    private static boolean y(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -2095576542:
                if (str.equals("V_MPEG4/ISO/AP")) {
                    c9 = 0;
                    break;
                }
                break;
            case -2095575984:
                if (str.equals("V_MPEG4/ISO/SP")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1985379776:
                if (str.equals("A_MS/ACM")) {
                    c9 = 2;
                    break;
                }
                break;
            case -1784763192:
                if (str.equals("A_TRUEHD")) {
                    c9 = 3;
                    break;
                }
                break;
            case -1730367663:
                if (str.equals("A_VORBIS")) {
                    c9 = 4;
                    break;
                }
                break;
            case -1482641358:
                if (str.equals("A_MPEG/L2")) {
                    c9 = 5;
                    break;
                }
                break;
            case -1482641357:
                if (str.equals("A_MPEG/L3")) {
                    c9 = 6;
                    break;
                }
                break;
            case -1373388978:
                if (str.equals("V_MS/VFW/FOURCC")) {
                    c9 = 7;
                    break;
                }
                break;
            case -933872740:
                if (str.equals("S_DVBSUB")) {
                    c9 = '\b';
                    break;
                }
                break;
            case -538363189:
                if (str.equals("V_MPEG4/ISO/ASP")) {
                    c9 = '\t';
                    break;
                }
                break;
            case -538363109:
                if (str.equals("V_MPEG4/ISO/AVC")) {
                    c9 = '\n';
                    break;
                }
                break;
            case -425012669:
                if (str.equals("S_VOBSUB")) {
                    c9 = 11;
                    break;
                }
                break;
            case -356037306:
                if (str.equals("A_DTS/LOSSLESS")) {
                    c9 = '\f';
                    break;
                }
                break;
            case 62923557:
                if (str.equals("A_AAC")) {
                    c9 = '\r';
                    break;
                }
                break;
            case 62923603:
                if (str.equals("A_AC3")) {
                    c9 = 14;
                    break;
                }
                break;
            case 62927045:
                if (str.equals("A_DTS")) {
                    c9 = 15;
                    break;
                }
                break;
            case 82318131:
                if (str.equals("V_AV1")) {
                    c9 = 16;
                    break;
                }
                break;
            case 82338133:
                if (str.equals("V_VP8")) {
                    c9 = 17;
                    break;
                }
                break;
            case 82338134:
                if (str.equals("V_VP9")) {
                    c9 = 18;
                    break;
                }
                break;
            case 99146302:
                if (str.equals("S_HDMV/PGS")) {
                    c9 = 19;
                    break;
                }
                break;
            case 444813526:
                if (str.equals("V_THEORA")) {
                    c9 = 20;
                    break;
                }
                break;
            case 542569478:
                if (str.equals("A_DTS/EXPRESS")) {
                    c9 = 21;
                    break;
                }
                break;
            case 635596514:
                if (str.equals("A_PCM/FLOAT/IEEE")) {
                    c9 = 22;
                    break;
                }
                break;
            case 725948237:
                if (str.equals("A_PCM/INT/BIG")) {
                    c9 = 23;
                    break;
                }
                break;
            case 725957860:
                if (str.equals("A_PCM/INT/LIT")) {
                    c9 = 24;
                    break;
                }
                break;
            case 738597099:
                if (str.equals("S_TEXT/ASS")) {
                    c9 = 25;
                    break;
                }
                break;
            case 855502857:
                if (str.equals("V_MPEGH/ISO/HEVC")) {
                    c9 = 26;
                    break;
                }
                break;
            case 1045209816:
                if (str.equals("S_TEXT/WEBVTT")) {
                    c9 = 27;
                    break;
                }
                break;
            case 1422270023:
                if (str.equals("S_TEXT/UTF8")) {
                    c9 = 28;
                    break;
                }
                break;
            case 1809237540:
                if (str.equals("V_MPEG2")) {
                    c9 = 29;
                    break;
                }
                break;
            case 1950749482:
                if (str.equals("A_EAC3")) {
                    c9 = 30;
                    break;
                }
                break;
            case 1950789798:
                if (str.equals("A_FLAC")) {
                    c9 = 31;
                    break;
                }
                break;
            case 1951062397:
                if (str.equals("A_OPUS")) {
                    c9 = ' ';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case ' ':
                return true;
            default:
                return false;
        }
    }

    protected void G(int i8, long j8, long j9) {
        k();
        if (i8 == 160) {
            this.Q = false;
            this.R = 0L;
        } else if (i8 == 174) {
            this.f22886u = new c();
        } else if (i8 == 187) {
            this.E = false;
        } else if (i8 == 19899) {
            this.f22888w = -1;
            this.f22889x = -1L;
        } else if (i8 == 20533) {
            t(i8).f22900h = true;
        } else if (i8 == 21968) {
            t(i8).f22915x = true;
        } else if (i8 == 408125543) {
            long j10 = this.q;
            if (j10 != -1 && j10 != j8) {
                throw ParserException.a("Multiple Segment elements not supported", null);
            }
            this.q = j8;
            this.f22882p = j9;
        } else if (i8 == 475249515) {
            this.C = new q();
            this.D = new q();
        } else if (i8 == 524531317 && !this.f22887v) {
            if (this.f22870d && this.f22891z != -1) {
                this.f22890y = true;
                return;
            }
            this.f22868b0.m(new z.b(this.f22885t));
            this.f22887v = true;
        }
    }

    protected void H(int i8, String str) {
        if (i8 == 134) {
            t(i8).f22894b = str;
        } else if (i8 != 17026) {
            if (i8 == 21358) {
                t(i8).f22893a = str;
            } else if (i8 != 2274716) {
            } else {
                t(i8).W = str;
            }
        } else if ("webm".equals(str) || "matroska".equals(str)) {
        } else {
            throw ParserException.a("DocType " + str + " not supported", null);
        }
    }

    @Override // n4.k
    public final void b(m mVar) {
        this.f22868b0 = mVar;
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        this.B = -9223372036854775807L;
        this.G = 0;
        this.f22865a.reset();
        this.f22867b.e();
        D();
        for (int i8 = 0; i8 < this.f22869c.size(); i8++) {
            this.f22869c.valueAt(i8).n();
        }
    }

    @Override // n4.k
    public final int e(l lVar, y yVar) {
        this.F = false;
        boolean z4 = true;
        while (z4 && !this.F) {
            z4 = this.f22865a.a(lVar);
            if (z4 && B(yVar, lVar.getPosition())) {
                return 1;
            }
        }
        if (z4) {
            return 0;
        }
        for (int i8 = 0; i8 < this.f22869c.size(); i8++) {
            c valueAt = this.f22869c.valueAt(i8);
            valueAt.f();
            valueAt.j();
        }
        return -1;
    }

    @Override // n4.k
    public final boolean g(l lVar) {
        return new f().b(lVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:86:0x0241, code lost:
        throw com.google.android.exoplayer2.ParserException.a("EBML lacing sample size out of range.", null);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void l(int r22, int r23, n4.l r24) {
        /*
            Method dump skipped, instructions count: 766
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: t4.e.l(int, int, n4.l):void");
    }

    protected void o(int i8) {
        k();
        if (i8 == 160) {
            if (this.G != 2) {
                return;
            }
            c cVar = this.f22869c.get(this.M);
            cVar.f();
            if (this.R > 0 && "A_OPUS".equals(cVar.f22894b)) {
                this.f22880n.R(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this.R).array());
            }
            int i9 = 0;
            for (int i10 = 0; i10 < this.K; i10++) {
                i9 += this.L[i10];
            }
            int i11 = 0;
            while (i11 < this.K) {
                long j8 = this.H + ((cVar.f22897e * i11) / 1000);
                int i12 = this.O;
                if (i11 == 0 && !this.Q) {
                    i12 |= 1;
                }
                int i13 = this.L[i11];
                int i14 = i9 - i13;
                n(cVar, j8, i12, i13, i14);
                i11++;
                i9 = i14;
            }
            this.G = 0;
        } else if (i8 == 174) {
            c cVar2 = (c) b6.a.h(this.f22886u);
            String str = cVar2.f22894b;
            if (str == null) {
                throw ParserException.a("CodecId is missing in TrackEntry element", null);
            }
            if (y(str)) {
                cVar2.i(this.f22868b0, cVar2.f22895c);
                this.f22869c.put(cVar2.f22895c, cVar2);
            }
            this.f22886u = null;
        } else if (i8 == 19899) {
            int i15 = this.f22888w;
            if (i15 != -1) {
                long j9 = this.f22889x;
                if (j9 != -1) {
                    if (i15 == 475249515) {
                        this.f22891z = j9;
                        return;
                    }
                    return;
                }
            }
            throw ParserException.a("Mandatory element SeekID or SeekPosition not found", null);
        } else if (i8 == 25152) {
            j(i8);
            c cVar3 = this.f22886u;
            if (cVar3.f22900h) {
                if (cVar3.f22902j == null) {
                    throw ParserException.a("Encrypted Track found but ContentEncKeyID was not found", null);
                }
                cVar3.f22904l = new DrmInitData(new DrmInitData.SchemeData(i4.b.f20465a, "video/webm", this.f22886u.f22902j.f22049b));
            }
        } else if (i8 == 28032) {
            j(i8);
            c cVar4 = this.f22886u;
            if (cVar4.f22900h && cVar4.f22901i != null) {
                throw ParserException.a("Combining encryption and compression is not supported", null);
            }
        } else if (i8 == 357149030) {
            if (this.f22883r == -9223372036854775807L) {
                this.f22883r = 1000000L;
            }
            long j10 = this.f22884s;
            if (j10 != -9223372036854775807L) {
                this.f22885t = E(j10);
            }
        } else if (i8 == 374648427) {
            if (this.f22869c.size() == 0) {
                throw ParserException.a("No valid tracks were found", null);
            }
            this.f22868b0.o();
        } else if (i8 == 475249515) {
            if (!this.f22887v) {
                this.f22868b0.m(m(this.C, this.D));
                this.f22887v = true;
            }
            this.C = null;
            this.D = null;
        }
    }

    protected void r(int i8, double d8) {
        if (i8 == 181) {
            t(i8).Q = (int) d8;
        } else if (i8 == 17545) {
            this.f22884s = (long) d8;
        } else {
            switch (i8) {
                case 21969:
                    t(i8).D = (float) d8;
                    return;
                case 21970:
                    t(i8).E = (float) d8;
                    return;
                case 21971:
                    t(i8).F = (float) d8;
                    return;
                case 21972:
                    t(i8).G = (float) d8;
                    return;
                case 21973:
                    t(i8).H = (float) d8;
                    return;
                case 21974:
                    t(i8).I = (float) d8;
                    return;
                case 21975:
                    t(i8).J = (float) d8;
                    return;
                case 21976:
                    t(i8).K = (float) d8;
                    return;
                case 21977:
                    t(i8).L = (float) d8;
                    return;
                case 21978:
                    t(i8).M = (float) d8;
                    return;
                default:
                    switch (i8) {
                        case 30323:
                            t(i8).f22910s = (float) d8;
                            return;
                        case 30324:
                            t(i8).f22911t = (float) d8;
                            return;
                        case 30325:
                            t(i8).f22912u = (float) d8;
                            return;
                        default:
                            return;
                    }
            }
        }
    }

    @Override // n4.k
    public final void release() {
    }

    protected c t(int i8) {
        j(i8);
        return this.f22886u;
    }

    protected int u(int i8) {
        switch (i8) {
            case 131:
            case 136:
            case 155:
            case 159:
            case 176:
            case 179:
            case 186:
            case 215:
            case 231:
            case 238:
            case 241:
            case 251:
            case 16871:
            case 16980:
            case 17029:
            case 17143:
            case 18401:
            case 18408:
            case 20529:
            case 20530:
            case 21420:
            case 21432:
            case 21680:
            case 21682:
            case 21690:
            case 21930:
            case 21945:
            case 21946:
            case 21947:
            case 21948:
            case 21949:
            case 21998:
            case 22186:
            case 22203:
            case 25188:
            case 30114:
            case 30321:
            case 2352003:
            case 2807729:
                return 2;
            case 134:
            case 17026:
            case 21358:
            case 2274716:
                return 3;
            case 160:
            case 166:
            case 174:
            case 183:
            case 187:
            case 224:
            case 225:
            case 16868:
            case 18407:
            case 19899:
            case 20532:
            case 20533:
            case 21936:
            case 21968:
            case 25152:
            case 28032:
            case 30113:
            case 30320:
            case 290298740:
            case 357149030:
            case 374648427:
            case 408125543:
            case 440786851:
            case 475249515:
            case 524531317:
                return 1;
            case 161:
            case 163:
            case 165:
            case 16877:
            case 16981:
            case 18402:
            case 21419:
            case 25506:
            case 30322:
                return 4;
            case 181:
            case 17545:
            case 21969:
            case 21970:
            case 21971:
            case 21972:
            case 21973:
            case 21974:
            case 21975:
            case 21976:
            case 21977:
            case 21978:
            case 30323:
            case 30324:
            case 30325:
                return 5;
            default:
                return 0;
        }
    }

    protected void v(c cVar, l lVar, int i8) {
        if (cVar.f22899g != 1685485123 && cVar.f22899g != 1685480259) {
            lVar.i(i8);
            return;
        }
        byte[] bArr = new byte[i8];
        cVar.N = bArr;
        lVar.readFully(bArr, 0, i8);
    }

    protected void w(c cVar, int i8, l lVar, int i9) {
        if (i8 != 4 || !"V_VP9".equals(cVar.f22894b)) {
            lVar.i(i9);
            return;
        }
        this.f22880n.Q(i9);
        lVar.readFully(this.f22880n.e(), 0, i9);
    }

    protected void x(int i8, long j8) {
        if (i8 == 20529) {
            if (j8 == 0) {
                return;
            }
            throw ParserException.a("ContentEncodingOrder " + j8 + " not supported", null);
        } else if (i8 == 20530) {
            if (j8 == 1) {
                return;
            }
            throw ParserException.a("ContentEncodingScope " + j8 + " not supported", null);
        } else {
            switch (i8) {
                case 131:
                    t(i8).f22896d = (int) j8;
                    return;
                case 136:
                    t(i8).V = j8 == 1;
                    return;
                case 155:
                    this.I = E(j8);
                    return;
                case 159:
                    t(i8).O = (int) j8;
                    return;
                case 176:
                    t(i8).f22905m = (int) j8;
                    return;
                case 179:
                    i(i8);
                    this.C.a(E(j8));
                    return;
                case 186:
                    t(i8).f22906n = (int) j8;
                    return;
                case 215:
                    t(i8).f22895c = (int) j8;
                    return;
                case 231:
                    this.B = E(j8);
                    return;
                case 238:
                    this.P = (int) j8;
                    return;
                case 241:
                    if (this.E) {
                        return;
                    }
                    i(i8);
                    this.D.a(j8);
                    this.E = true;
                    return;
                case 251:
                    this.Q = true;
                    return;
                case 16871:
                    t(i8).f22899g = (int) j8;
                    return;
                case 16980:
                    if (j8 == 3) {
                        return;
                    }
                    throw ParserException.a("ContentCompAlgo " + j8 + " not supported", null);
                case 17029:
                    if (j8 < 1 || j8 > 2) {
                        throw ParserException.a("DocTypeReadVersion " + j8 + " not supported", null);
                    }
                    return;
                case 17143:
                    if (j8 == 1) {
                        return;
                    }
                    throw ParserException.a("EBMLReadVersion " + j8 + " not supported", null);
                case 18401:
                    if (j8 == 5) {
                        return;
                    }
                    throw ParserException.a("ContentEncAlgo " + j8 + " not supported", null);
                case 18408:
                    if (j8 == 1) {
                        return;
                    }
                    throw ParserException.a("AESSettingsCipherMode " + j8 + " not supported", null);
                case 21420:
                    this.f22889x = j8 + this.q;
                    return;
                case 21432:
                    int i9 = (int) j8;
                    j(i8);
                    if (i9 == 0) {
                        this.f22886u.f22914w = 0;
                        return;
                    } else if (i9 == 1) {
                        this.f22886u.f22914w = 2;
                        return;
                    } else if (i9 == 3) {
                        this.f22886u.f22914w = 1;
                        return;
                    } else if (i9 != 15) {
                        return;
                    } else {
                        this.f22886u.f22914w = 3;
                        return;
                    }
                case 21680:
                    t(i8).f22907o = (int) j8;
                    return;
                case 21682:
                    t(i8).q = (int) j8;
                    return;
                case 21690:
                    t(i8).f22908p = (int) j8;
                    return;
                case 21930:
                    t(i8).U = j8 == 1;
                    return;
                case 21998:
                    t(i8).f22898f = (int) j8;
                    return;
                case 22186:
                    t(i8).R = j8;
                    return;
                case 22203:
                    t(i8).S = j8;
                    return;
                case 25188:
                    t(i8).P = (int) j8;
                    return;
                case 30114:
                    this.R = j8;
                    return;
                case 30321:
                    j(i8);
                    int i10 = (int) j8;
                    if (i10 == 0) {
                        this.f22886u.f22909r = 0;
                        return;
                    } else if (i10 == 1) {
                        this.f22886u.f22909r = 1;
                        return;
                    } else if (i10 == 2) {
                        this.f22886u.f22909r = 2;
                        return;
                    } else if (i10 != 3) {
                        return;
                    } else {
                        this.f22886u.f22909r = 3;
                        return;
                    }
                case 2352003:
                    t(i8).f22897e = (int) j8;
                    return;
                case 2807729:
                    this.f22883r = j8;
                    return;
                default:
                    switch (i8) {
                        case 21945:
                            j(i8);
                            int i11 = (int) j8;
                            if (i11 == 1) {
                                this.f22886u.A = 2;
                                return;
                            } else if (i11 != 2) {
                                return;
                            } else {
                                this.f22886u.A = 1;
                                return;
                            }
                        case 21946:
                            j(i8);
                            int c9 = c6.c.c((int) j8);
                            if (c9 != -1) {
                                this.f22886u.f22917z = c9;
                                return;
                            }
                            return;
                        case 21947:
                            j(i8);
                            this.f22886u.f22915x = true;
                            int b9 = c6.c.b((int) j8);
                            if (b9 != -1) {
                                this.f22886u.f22916y = b9;
                                return;
                            }
                            return;
                        case 21948:
                            t(i8).B = (int) j8;
                            return;
                        case 21949:
                            t(i8).C = (int) j8;
                            return;
                        default:
                            return;
                    }
            }
        }
    }

    protected boolean z(int i8) {
        return i8 == 357149030 || i8 == 524531317 || i8 == 475249515 || i8 == 374648427;
    }
}
