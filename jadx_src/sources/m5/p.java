package m5;

import android.net.Uri;
import android.os.Handler;
import android.util.SparseIntArray;
import b6.l0;
import b6.t;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.source.v;
import com.google.android.exoplayer2.source.w;
import com.google.android.exoplayer2.upstream.HttpDataSource$InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.c;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.f1;
import h5.u;
import i4.i0;
import i4.s;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import m5.f;
import n4.b0;
import n4.m;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p implements Loader.b<j5.f>, Loader.f, w, m, v.d {

    /* renamed from: m0  reason: collision with root package name */
    private static final Set<Integer> f21921m0 = Collections.unmodifiableSet(new HashSet(Arrays.asList(1, 2, 5)));
    private j5.f A;
    private d[] B;
    private Set<Integer> E;
    private SparseIntArray F;
    private b0 G;
    private int H;
    private int K;
    private boolean L;
    private boolean O;
    private int P;
    private w0 Q;
    private w0 R;
    private boolean T;
    private h5.w W;
    private Set<u> X;
    private int[] Y;
    private int Z;

    /* renamed from: a  reason: collision with root package name */
    private final String f21922a;

    /* renamed from: a0  reason: collision with root package name */
    private boolean f21923a0;

    /* renamed from: b  reason: collision with root package name */
    private final int f21924b;

    /* renamed from: b0  reason: collision with root package name */
    private boolean[] f21925b0;

    /* renamed from: c  reason: collision with root package name */
    private final b f21926c;

    /* renamed from: c0  reason: collision with root package name */
    private boolean[] f21927c0;

    /* renamed from: d  reason: collision with root package name */
    private final f f21928d;

    /* renamed from: d0  reason: collision with root package name */
    private long f21929d0;

    /* renamed from: e  reason: collision with root package name */
    private final a6.b f21930e;

    /* renamed from: e0  reason: collision with root package name */
    private long f21931e0;

    /* renamed from: f  reason: collision with root package name */
    private final w0 f21932f;

    /* renamed from: f0  reason: collision with root package name */
    private boolean f21933f0;

    /* renamed from: g  reason: collision with root package name */
    private final com.google.android.exoplayer2.drm.i f21934g;

    /* renamed from: g0  reason: collision with root package name */
    private boolean f21935g0;

    /* renamed from: h  reason: collision with root package name */
    private final h.a f21936h;

    /* renamed from: h0  reason: collision with root package name */
    private boolean f21937h0;

    /* renamed from: i0  reason: collision with root package name */
    private boolean f21938i0;

    /* renamed from: j  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f21939j;

    /* renamed from: j0  reason: collision with root package name */
    private long f21940j0;

    /* renamed from: k0  reason: collision with root package name */
    private DrmInitData f21942k0;

    /* renamed from: l  reason: collision with root package name */
    private final l.a f21943l;

    /* renamed from: l0  reason: collision with root package name */
    private i f21944l0;

    /* renamed from: m  reason: collision with root package name */
    private final int f21945m;

    /* renamed from: p  reason: collision with root package name */
    private final ArrayList<i> f21947p;
    private final List<i> q;

    /* renamed from: t  reason: collision with root package name */
    private final Runnable f21948t;

    /* renamed from: w  reason: collision with root package name */
    private final Runnable f21949w;

    /* renamed from: x  reason: collision with root package name */
    private final Handler f21950x;

    /* renamed from: y  reason: collision with root package name */
    private final ArrayList<l> f21951y;

    /* renamed from: z  reason: collision with root package name */
    private final Map<String, DrmInitData> f21952z;

    /* renamed from: k  reason: collision with root package name */
    private final Loader f21941k = new Loader("Loader:HlsSampleStreamWrapper");

    /* renamed from: n  reason: collision with root package name */
    private final f.b f21946n = new f.b();
    private int[] C = new int[0];

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b extends w.a<p> {
        void a();

        void i(Uri uri);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c implements b0 {

        /* renamed from: g  reason: collision with root package name */
        private static final w0 f21953g = new w0.b().g0("application/id3").G();

        /* renamed from: h  reason: collision with root package name */
        private static final w0 f21954h = new w0.b().g0("application/x-emsg").G();

        /* renamed from: a  reason: collision with root package name */
        private final c5.a f21955a = new c5.a();

        /* renamed from: b  reason: collision with root package name */
        private final b0 f21956b;

        /* renamed from: c  reason: collision with root package name */
        private final w0 f21957c;

        /* renamed from: d  reason: collision with root package name */
        private w0 f21958d;

        /* renamed from: e  reason: collision with root package name */
        private byte[] f21959e;

        /* renamed from: f  reason: collision with root package name */
        private int f21960f;

        public c(b0 b0Var, int i8) {
            w0 w0Var;
            this.f21956b = b0Var;
            if (i8 == 1) {
                w0Var = f21953g;
            } else if (i8 != 3) {
                throw new IllegalArgumentException("Unknown metadataType: " + i8);
            } else {
                w0Var = f21954h;
            }
            this.f21957c = w0Var;
            this.f21959e = new byte[0];
            this.f21960f = 0;
        }

        private boolean g(EventMessage eventMessage) {
            w0 V = eventMessage.V();
            return V != null && l0.c(this.f21957c.f11207m, V.f11207m);
        }

        private void h(int i8) {
            byte[] bArr = this.f21959e;
            if (bArr.length < i8) {
                this.f21959e = Arrays.copyOf(bArr, i8 + (i8 / 2));
            }
        }

        private z i(int i8, int i9) {
            int i10 = this.f21960f - i9;
            z zVar = new z(Arrays.copyOfRange(this.f21959e, i10 - i8, i10));
            byte[] bArr = this.f21959e;
            System.arraycopy(bArr, i10, bArr, 0, i9);
            this.f21960f = i9;
            return zVar;
        }

        @Override // n4.b0
        public int a(a6.f fVar, int i8, boolean z4, int i9) {
            h(this.f21960f + i8);
            int read = fVar.read(this.f21959e, this.f21960f, i8);
            if (read != -1) {
                this.f21960f += read;
                return read;
            } else if (z4) {
                return -1;
            } else {
                throw new EOFException();
            }
        }

        @Override // n4.b0
        public void d(long j8, int i8, int i9, int i10, b0.a aVar) {
            b6.a.e(this.f21958d);
            z i11 = i(i9, i10);
            if (!l0.c(this.f21958d.f11207m, this.f21957c.f11207m)) {
                if (!"application/x-emsg".equals(this.f21958d.f11207m)) {
                    b6.p.i("HlsSampleStreamWrapper", "Ignoring sample for unsupported format: " + this.f21958d.f11207m);
                    return;
                }
                EventMessage c9 = this.f21955a.c(i11);
                if (!g(c9)) {
                    b6.p.i("HlsSampleStreamWrapper", String.format("Ignoring EMSG. Expected it to contain wrapped %s but actual wrapped format: %s", this.f21957c.f11207m, c9.V()));
                    return;
                }
                i11 = new z((byte[]) b6.a.e(c9.M0()));
            }
            int a9 = i11.a();
            this.f21956b.b(i11, a9);
            this.f21956b.d(j8, i8, a9, i10, aVar);
        }

        @Override // n4.b0
        public void e(z zVar, int i8, int i9) {
            h(this.f21960f + i8);
            zVar.l(this.f21959e, this.f21960f, i8);
            this.f21960f += i8;
        }

        @Override // n4.b0
        public void f(w0 w0Var) {
            this.f21958d = w0Var;
            this.f21956b.f(this.f21957c);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d extends v {
        private final Map<String, DrmInitData> H;
        private DrmInitData I;

        private d(a6.b bVar, com.google.android.exoplayer2.drm.i iVar, h.a aVar, Map<String, DrmInitData> map) {
            super(bVar, iVar, aVar);
            this.H = map;
        }

        private Metadata h0(Metadata metadata) {
            if (metadata == null) {
                return null;
            }
            int e8 = metadata.e();
            int i8 = 0;
            int i9 = 0;
            while (true) {
                if (i9 >= e8) {
                    i9 = -1;
                    break;
                }
                Metadata.Entry d8 = metadata.d(i9);
                if ((d8 instanceof PrivFrame) && "com.apple.streaming.transportStreamTimestamp".equals(((PrivFrame) d8).f10118b)) {
                    break;
                }
                i9++;
            }
            if (i9 == -1) {
                return metadata;
            }
            if (e8 == 1) {
                return null;
            }
            Metadata.Entry[] entryArr = new Metadata.Entry[e8 - 1];
            while (i8 < e8) {
                if (i8 != i9) {
                    entryArr[i8 < i9 ? i8 : i8 - 1] = metadata.d(i8);
                }
                i8++;
            }
            return new Metadata(entryArr);
        }

        @Override // com.google.android.exoplayer2.source.v, n4.b0
        public void d(long j8, int i8, int i9, int i10, b0.a aVar) {
            super.d(j8, i8, i9, i10, aVar);
        }

        public void i0(DrmInitData drmInitData) {
            this.I = drmInitData;
            I();
        }

        public void j0(i iVar) {
            f0(iVar.f21883k);
        }

        @Override // com.google.android.exoplayer2.source.v
        public w0 w(w0 w0Var) {
            DrmInitData drmInitData;
            DrmInitData drmInitData2 = this.I;
            if (drmInitData2 == null) {
                drmInitData2 = w0Var.q;
            }
            if (drmInitData2 != null && (drmInitData = this.H.get(drmInitData2.f9594c)) != null) {
                drmInitData2 = drmInitData;
            }
            Metadata h02 = h0(w0Var.f11205k);
            if (drmInitData2 != w0Var.q || h02 != w0Var.f11205k) {
                w0Var = w0Var.b().O(drmInitData2).Z(h02).G();
            }
            return super.w(w0Var);
        }
    }

    public p(String str, int i8, b bVar, f fVar, Map<String, DrmInitData> map, a6.b bVar2, long j8, w0 w0Var, com.google.android.exoplayer2.drm.i iVar, h.a aVar, com.google.android.exoplayer2.upstream.c cVar, l.a aVar2, int i9) {
        this.f21922a = str;
        this.f21924b = i8;
        this.f21926c = bVar;
        this.f21928d = fVar;
        this.f21952z = map;
        this.f21930e = bVar2;
        this.f21932f = w0Var;
        this.f21934g = iVar;
        this.f21936h = aVar;
        this.f21939j = cVar;
        this.f21943l = aVar2;
        this.f21945m = i9;
        Set<Integer> set = f21921m0;
        this.E = new HashSet(set.size());
        this.F = new SparseIntArray(set.size());
        this.B = new d[0];
        this.f21927c0 = new boolean[0];
        this.f21925b0 = new boolean[0];
        ArrayList<i> arrayList = new ArrayList<>();
        this.f21947p = arrayList;
        this.q = Collections.unmodifiableList(arrayList);
        this.f21951y = new ArrayList<>();
        this.f21948t = new o(this);
        this.f21949w = new n(this);
        this.f21950x = l0.w();
        this.f21929d0 = j8;
        this.f21931e0 = j8;
    }

    private boolean A(int i8) {
        for (int i9 = i8; i9 < this.f21947p.size(); i9++) {
            if (this.f21947p.get(i9).f21886n) {
                return false;
            }
        }
        i iVar = this.f21947p.get(i8);
        for (int i10 = 0; i10 < this.B.length; i10++) {
            if (this.B[i10].C() > iVar.m(i10)) {
                return false;
            }
        }
        return true;
    }

    private static n4.j C(int i8, int i9) {
        b6.p.i("HlsSampleStreamWrapper", "Unmapped track with id " + i8 + " of type " + i9);
        return new n4.j();
    }

    private v D(int i8, int i9) {
        int length = this.B.length;
        boolean z4 = true;
        if (i9 != 1 && i9 != 2) {
            z4 = false;
        }
        d dVar = new d(this.f21930e, this.f21934g, this.f21936h, this.f21952z);
        dVar.b0(this.f21929d0);
        if (z4) {
            dVar.i0(this.f21942k0);
        }
        dVar.a0(this.f21940j0);
        i iVar = this.f21944l0;
        if (iVar != null) {
            dVar.j0(iVar);
        }
        dVar.d0(this);
        int i10 = length + 1;
        int[] copyOf = Arrays.copyOf(this.C, i10);
        this.C = copyOf;
        copyOf[length] = i8;
        this.B = (d[]) l0.F0(this.B, dVar);
        boolean[] copyOf2 = Arrays.copyOf(this.f21927c0, i10);
        this.f21927c0 = copyOf2;
        copyOf2[length] = z4;
        this.f21923a0 = copyOf2[length] | this.f21923a0;
        this.E.add(Integer.valueOf(i9));
        this.F.append(i9, length);
        if (M(i9) > M(this.H)) {
            this.K = length;
            this.H = i9;
        }
        this.f21925b0 = Arrays.copyOf(this.f21925b0, i10);
        return dVar;
    }

    private h5.w E(u[] uVarArr) {
        for (int i8 = 0; i8 < uVarArr.length; i8++) {
            u uVar = uVarArr[i8];
            w0[] w0VarArr = new w0[uVar.f20308a];
            for (int i9 = 0; i9 < uVar.f20308a; i9++) {
                w0 b9 = uVar.b(i9);
                w0VarArr[i9] = b9.c(this.f21934g.c(b9));
            }
            uVarArr[i8] = new u(uVar.f20309b, w0VarArr);
        }
        return new h5.w(uVarArr);
    }

    private static w0 F(w0 w0Var, w0 w0Var2, boolean z4) {
        String d8;
        String str;
        if (w0Var == null) {
            return w0Var2;
        }
        int k8 = t.k(w0Var2.f11207m);
        if (l0.K(w0Var.f11204j, k8) == 1) {
            d8 = l0.L(w0Var.f11204j, k8);
            str = t.g(d8);
        } else {
            d8 = t.d(w0Var.f11204j, w0Var2.f11207m);
            str = w0Var2.f11207m;
        }
        w0.b K = w0Var2.b().U(w0Var.f11196a).W(w0Var.f11197b).X(w0Var.f11198c).i0(w0Var.f11199d).e0(w0Var.f11200e).I(z4 ? w0Var.f11201f : -1).b0(z4 ? w0Var.f11202g : -1).K(d8);
        if (k8 == 2) {
            K.n0(w0Var.f11211w).S(w0Var.f11212x).R(w0Var.f11213y);
        }
        if (str != null) {
            K.g0(str);
        }
        int i8 = w0Var.F;
        if (i8 != -1 && k8 == 1) {
            K.J(i8);
        }
        Metadata metadata = w0Var.f11205k;
        if (metadata != null) {
            Metadata metadata2 = w0Var2.f11205k;
            if (metadata2 != null) {
                metadata = metadata2.b(metadata);
            }
            K.Z(metadata);
        }
        return K.G();
    }

    private void G(int i8) {
        b6.a.f(!this.f21941k.j());
        while (true) {
            if (i8 >= this.f21947p.size()) {
                i8 = -1;
                break;
            } else if (A(i8)) {
                break;
            } else {
                i8++;
            }
        }
        if (i8 == -1) {
            return;
        }
        long j8 = K().f20748h;
        i H = H(i8);
        if (this.f21947p.isEmpty()) {
            this.f21931e0 = this.f21929d0;
        } else {
            ((i) f1.f(this.f21947p)).o();
        }
        this.f21937h0 = false;
        this.f21943l.D(this.H, H.f20747g, j8);
    }

    private i H(int i8) {
        i iVar = this.f21947p.get(i8);
        ArrayList<i> arrayList = this.f21947p;
        l0.N0(arrayList, i8, arrayList.size());
        for (int i9 = 0; i9 < this.B.length; i9++) {
            this.B[i9].u(iVar.m(i9));
        }
        return iVar;
    }

    private boolean I(i iVar) {
        int i8 = iVar.f21883k;
        int length = this.B.length;
        for (int i9 = 0; i9 < length; i9++) {
            if (this.f21925b0[i9] && this.B[i9].Q() == i8) {
                return false;
            }
        }
        return true;
    }

    private static boolean J(w0 w0Var, w0 w0Var2) {
        String str = w0Var.f11207m;
        String str2 = w0Var2.f11207m;
        int k8 = t.k(str);
        if (k8 != 3) {
            return k8 == t.k(str2);
        } else if (l0.c(str, str2)) {
            return !("application/cea-608".equals(str) || "application/cea-708".equals(str)) || w0Var.O == w0Var2.O;
        } else {
            return false;
        }
    }

    private i K() {
        ArrayList<i> arrayList = this.f21947p;
        return arrayList.get(arrayList.size() - 1);
    }

    private b0 L(int i8, int i9) {
        b6.a.a(f21921m0.contains(Integer.valueOf(i9)));
        int i10 = this.F.get(i9, -1);
        if (i10 == -1) {
            return null;
        }
        if (this.E.add(Integer.valueOf(i9))) {
            this.C[i10] = i8;
        }
        return this.C[i10] == i8 ? this.B[i10] : C(i8, i9);
    }

    private static int M(int i8) {
        if (i8 != 1) {
            if (i8 != 2) {
                return i8 != 3 ? 0 : 1;
            }
            return 3;
        }
        return 2;
    }

    private void N(i iVar) {
        d[] dVarArr;
        this.f21944l0 = iVar;
        this.Q = iVar.f20744d;
        this.f21931e0 = -9223372036854775807L;
        this.f21947p.add(iVar);
        ImmutableList.a u8 = ImmutableList.u();
        for (d dVar : this.B) {
            u8.a(Integer.valueOf(dVar.G()));
        }
        iVar.n(this, u8.k());
        for (d dVar2 : this.B) {
            dVar2.j0(iVar);
            if (iVar.f21886n) {
                dVar2.g0();
            }
        }
    }

    private static boolean O(j5.f fVar) {
        return fVar instanceof i;
    }

    private boolean P() {
        return this.f21931e0 != -9223372036854775807L;
    }

    private void S() {
        int i8 = this.W.f20316a;
        int[] iArr = new int[i8];
        this.Y = iArr;
        Arrays.fill(iArr, -1);
        for (int i9 = 0; i9 < i8; i9++) {
            int i10 = 0;
            while (true) {
                d[] dVarArr = this.B;
                if (i10 >= dVarArr.length) {
                    break;
                } else if (J((w0) b6.a.h(dVarArr[i10].F()), this.W.b(i9).b(0))) {
                    this.Y[i9] = i10;
                    break;
                } else {
                    i10++;
                }
            }
        }
        Iterator<l> it = this.f21951y.iterator();
        while (it.hasNext()) {
            it.next().b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void T() {
        if (!this.T && this.Y == null && this.L) {
            for (d dVar : this.B) {
                if (dVar.F() == null) {
                    return;
                }
            }
            if (this.W != null) {
                S();
                return;
            }
            z();
            l0();
            this.f21926c.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c0() {
        this.L = true;
        T();
    }

    private void g0() {
        for (d dVar : this.B) {
            dVar.W(this.f21933f0);
        }
        this.f21933f0 = false;
    }

    private boolean h0(long j8) {
        int length = this.B.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (!this.B[i8].Z(j8, false) && (this.f21927c0[i8] || !this.f21923a0)) {
                return false;
            }
        }
        return true;
    }

    private void l0() {
        this.O = true;
    }

    private void q0(h5.r[] rVarArr) {
        this.f21951y.clear();
        for (h5.r rVar : rVarArr) {
            if (rVar != null) {
                this.f21951y.add((l) rVar);
            }
        }
    }

    private void x() {
        b6.a.f(this.O);
        b6.a.e(this.W);
        b6.a.e(this.X);
    }

    private void z() {
        w0 w0Var;
        int length = this.B.length;
        int i8 = -2;
        int i9 = -1;
        int i10 = 0;
        while (true) {
            if (i10 >= length) {
                break;
            }
            String str = ((w0) b6.a.h(this.B[i10].F())).f11207m;
            int i11 = t.s(str) ? 2 : t.o(str) ? 1 : t.r(str) ? 3 : -2;
            if (M(i11) > M(i8)) {
                i9 = i10;
                i8 = i11;
            } else if (i11 == i8 && i9 != -1) {
                i9 = -1;
            }
            i10++;
        }
        u j8 = this.f21928d.j();
        int i12 = j8.f20308a;
        this.Z = -1;
        this.Y = new int[length];
        for (int i13 = 0; i13 < length; i13++) {
            this.Y[i13] = i13;
        }
        u[] uVarArr = new u[length];
        int i14 = 0;
        while (i14 < length) {
            w0 w0Var2 = (w0) b6.a.h(this.B[i14].F());
            if (i14 == i9) {
                w0[] w0VarArr = new w0[i12];
                for (int i15 = 0; i15 < i12; i15++) {
                    w0 b9 = j8.b(i15);
                    if (i8 == 1 && (w0Var = this.f21932f) != null) {
                        b9 = b9.j(w0Var);
                    }
                    w0VarArr[i15] = i12 == 1 ? w0Var2.j(b9) : F(b9, w0Var2, true);
                }
                uVarArr[i14] = new u(this.f21922a, w0VarArr);
                this.Z = i14;
            } else {
                w0 w0Var3 = (i8 == 2 && t.o(w0Var2.f11207m)) ? this.f21932f : null;
                StringBuilder sb = new StringBuilder();
                sb.append(this.f21922a);
                sb.append(":muxed:");
                sb.append(i14 < i9 ? i14 : i14 - 1);
                uVarArr[i14] = new u(sb.toString(), F(w0Var3, w0Var2, false));
            }
            i14++;
        }
        this.W = E(uVarArr);
        b6.a.f(this.X == null);
        this.X = Collections.emptySet();
    }

    public void B() {
        if (this.O) {
            return;
        }
        d(this.f21929d0);
    }

    public boolean Q(int i8) {
        return !P() && this.B[i8].K(this.f21937h0);
    }

    public boolean R() {
        return this.H == 2;
    }

    public void U() {
        this.f21941k.a();
        this.f21928d.n();
    }

    public void V(int i8) {
        U();
        this.B[i8].N();
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: W */
    public void j(j5.f fVar, long j8, long j9, boolean z4) {
        this.A = null;
        h5.h hVar = new h5.h(fVar.f20741a, fVar.f20742b, fVar.f(), fVar.e(), j8, j9, fVar.b());
        this.f21939j.c(fVar.f20741a);
        this.f21943l.r(hVar, fVar.f20743c, this.f21924b, fVar.f20744d, fVar.f20745e, fVar.f20746f, fVar.f20747g, fVar.f20748h);
        if (z4) {
            return;
        }
        if (P() || this.P == 0) {
            g0();
        }
        if (this.P > 0) {
            this.f21926c.e(this);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: X */
    public void k(j5.f fVar, long j8, long j9) {
        this.A = null;
        this.f21928d.p(fVar);
        h5.h hVar = new h5.h(fVar.f20741a, fVar.f20742b, fVar.f(), fVar.e(), j8, j9, fVar.b());
        this.f21939j.c(fVar.f20741a);
        this.f21943l.u(hVar, fVar.f20743c, this.f21924b, fVar.f20744d, fVar.f20745e, fVar.f20746f, fVar.f20747g, fVar.f20748h);
        if (this.O) {
            this.f21926c.e(this);
        } else {
            d(this.f21929d0);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: Y */
    public Loader.c t(j5.f fVar, long j8, long j9, IOException iOException, int i8) {
        Loader.c h8;
        int i9;
        boolean O = O(fVar);
        if (O && !((i) fVar).q() && (iOException instanceof HttpDataSource$InvalidResponseCodeException) && ((i9 = ((HttpDataSource$InvalidResponseCodeException) iOException).f10902d) == 410 || i9 == 404)) {
            return Loader.f10906d;
        }
        long b9 = fVar.b();
        h5.h hVar = new h5.h(fVar.f20741a, fVar.f20742b, fVar.f(), fVar.e(), j8, j9, b9);
        c.C0117c c0117c = new c.C0117c(hVar, new h5.i(fVar.f20743c, this.f21924b, fVar.f20744d, fVar.f20745e, fVar.f20746f, l0.a1(fVar.f20747g), l0.a1(fVar.f20748h)), iOException, i8);
        c.b b10 = this.f21939j.b(z5.z.c(this.f21928d.k()), c0117c);
        boolean m8 = (b10 == null || b10.f10968a != 2) ? false : this.f21928d.m(fVar, b10.f10969b);
        if (m8) {
            if (O && b9 == 0) {
                ArrayList<i> arrayList = this.f21947p;
                b6.a.f(arrayList.remove(arrayList.size() - 1) == fVar);
                if (this.f21947p.isEmpty()) {
                    this.f21931e0 = this.f21929d0;
                } else {
                    ((i) f1.f(this.f21947p)).o();
                }
            }
            h8 = Loader.f10908f;
        } else {
            long a9 = this.f21939j.a(c0117c);
            h8 = a9 != -9223372036854775807L ? Loader.h(false, a9) : Loader.f10909g;
        }
        Loader.c cVar = h8;
        boolean z4 = !cVar.c();
        this.f21943l.w(hVar, fVar.f20743c, this.f21924b, fVar.f20744d, fVar.f20745e, fVar.f20746f, fVar.f20747g, fVar.f20748h, iOException, z4);
        if (z4) {
            this.A = null;
            this.f21939j.c(fVar.f20741a);
        }
        if (m8) {
            if (this.O) {
                this.f21926c.e(this);
            } else {
                d(this.f21929d0);
            }
        }
        return cVar;
    }

    public void Z() {
        this.E.clear();
    }

    @Override // com.google.android.exoplayer2.source.v.d
    public void a(w0 w0Var) {
        this.f21950x.post(this.f21948t);
    }

    public boolean a0(Uri uri, c.C0117c c0117c, boolean z4) {
        c.b b9;
        if (this.f21928d.o(uri)) {
            long j8 = (z4 || (b9 = this.f21939j.b(z5.z.c(this.f21928d.k()), c0117c)) == null || b9.f10968a != 2) ? -9223372036854775807L : b9.f10969b;
            return this.f21928d.q(uri, j8) && j8 != -9223372036854775807L;
        }
        return true;
    }

    @Override // com.google.android.exoplayer2.source.w
    public long b() {
        if (P()) {
            return this.f21931e0;
        }
        if (this.f21937h0) {
            return Long.MIN_VALUE;
        }
        return K().f20748h;
    }

    public void b0() {
        if (this.f21947p.isEmpty()) {
            return;
        }
        i iVar = (i) f1.f(this.f21947p);
        int c9 = this.f21928d.c(iVar);
        if (c9 == 1) {
            iVar.v();
        } else if (c9 == 2 && !this.f21937h0 && this.f21941k.j()) {
            this.f21941k.f();
        }
    }

    public long c(long j8, i0 i0Var) {
        return this.f21928d.b(j8, i0Var);
    }

    @Override // com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        List<i> list;
        long max;
        if (this.f21937h0 || this.f21941k.j() || this.f21941k.i()) {
            return false;
        }
        if (P()) {
            list = Collections.emptyList();
            max = this.f21931e0;
            for (d dVar : this.B) {
                dVar.b0(this.f21931e0);
            }
        } else {
            list = this.q;
            i K = K();
            max = K.h() ? K.f20748h : Math.max(this.f21929d0, K.f20747g);
        }
        List<i> list2 = list;
        long j9 = max;
        this.f21946n.a();
        this.f21928d.e(j8, j9, list2, this.O || !list2.isEmpty(), this.f21946n);
        f.b bVar = this.f21946n;
        boolean z4 = bVar.f21872b;
        j5.f fVar = bVar.f21871a;
        Uri uri = bVar.f21873c;
        if (z4) {
            this.f21931e0 = -9223372036854775807L;
            this.f21937h0 = true;
            return true;
        } else if (fVar == null) {
            if (uri != null) {
                this.f21926c.i(uri);
            }
            return false;
        } else {
            if (O(fVar)) {
                N((i) fVar);
            }
            this.A = fVar;
            this.f21943l.A(new h5.h(fVar.f20741a, fVar.f20742b, this.f21941k.n(fVar, this, this.f21939j.d(fVar.f20743c))), fVar.f20743c, this.f21924b, fVar.f20744d, fVar.f20745e, fVar.f20746f, fVar.f20747g, fVar.f20748h);
            return true;
        }
    }

    public void d0(u[] uVarArr, int i8, int... iArr) {
        this.W = E(uVarArr);
        this.X = new HashSet();
        for (int i9 : iArr) {
            this.X.add(this.W.b(i9));
        }
        this.Z = i8;
        Handler handler = this.f21950x;
        b bVar = this.f21926c;
        Objects.requireNonNull(bVar);
        handler.post(new m(bVar));
        l0();
    }

    @Override // n4.m
    public b0 e(int i8, int i9) {
        b0 b0Var;
        if (!f21921m0.contains(Integer.valueOf(i9))) {
            int i10 = 0;
            while (true) {
                b0[] b0VarArr = this.B;
                if (i10 >= b0VarArr.length) {
                    b0Var = null;
                    break;
                } else if (this.C[i10] == i8) {
                    b0Var = b0VarArr[i10];
                    break;
                } else {
                    i10++;
                }
            }
        } else {
            b0Var = L(i8, i9);
        }
        if (b0Var == null) {
            if (this.f21938i0) {
                return C(i8, i9);
            }
            b0Var = D(i8, i9);
        }
        if (i9 == 5) {
            if (this.G == null) {
                this.G = new c(b0Var, this.f21945m);
            }
            return this.G;
        }
        return b0Var;
    }

    public int e0(int i8, s sVar, DecoderInputBuffer decoderInputBuffer, int i9) {
        if (P()) {
            return -3;
        }
        int i10 = 0;
        if (!this.f21947p.isEmpty()) {
            int i11 = 0;
            while (i11 < this.f21947p.size() - 1 && I(this.f21947p.get(i11))) {
                i11++;
            }
            l0.N0(this.f21947p, 0, i11);
            i iVar = this.f21947p.get(0);
            w0 w0Var = iVar.f20744d;
            if (!w0Var.equals(this.R)) {
                this.f21943l.i(this.f21924b, w0Var, iVar.f20745e, iVar.f20746f, iVar.f20747g);
            }
            this.R = w0Var;
        }
        if (this.f21947p.isEmpty() || this.f21947p.get(0).q()) {
            int S = this.B[i8].S(sVar, decoderInputBuffer, i9, this.f21937h0);
            if (S == -5) {
                w0 w0Var2 = (w0) b6.a.e(sVar.f20512b);
                if (i8 == this.K) {
                    int Q = this.B[i8].Q();
                    while (i10 < this.f21947p.size() && this.f21947p.get(i10).f21883k != Q) {
                        i10++;
                    }
                    w0Var2 = w0Var2.j(i10 < this.f21947p.size() ? this.f21947p.get(i10).f20744d : (w0) b6.a.e(this.Q));
                }
                sVar.f20512b = w0Var2;
            }
            return S;
        }
        return -3;
    }

    @Override // com.google.android.exoplayer2.source.w
    public boolean f() {
        return this.f21941k.j();
    }

    public void f0() {
        if (this.O) {
            for (d dVar : this.B) {
                dVar.R();
            }
        }
        this.f21941k.m(this);
        this.f21950x.removeCallbacksAndMessages(null);
        this.T = true;
        this.f21951y.clear();
    }

    @Override // com.google.android.exoplayer2.source.w
    public long g() {
        ArrayList<i> arrayList;
        if (this.f21937h0) {
            return Long.MIN_VALUE;
        }
        if (P()) {
            return this.f21931e0;
        }
        long j8 = this.f21929d0;
        i K = K();
        if (!K.h()) {
            K = this.f21947p.size() > 1 ? this.f21947p.get(arrayList.size() - 2) : null;
        }
        if (K != null) {
            j8 = Math.max(j8, K.f20748h);
        }
        if (this.L) {
            for (d dVar : this.B) {
                j8 = Math.max(j8, dVar.z());
            }
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.w
    public void h(long j8) {
        if (this.f21941k.i() || P()) {
            return;
        }
        if (this.f21941k.j()) {
            b6.a.e(this.A);
            if (this.f21928d.v(j8, this.A, this.q)) {
                this.f21941k.f();
                return;
            }
            return;
        }
        int size = this.q.size();
        while (size > 0 && this.f21928d.c(this.q.get(size - 1)) == 2) {
            size--;
        }
        if (size < this.q.size()) {
            G(size);
        }
        int h8 = this.f21928d.h(j8, this.q);
        if (h8 < this.f21947p.size()) {
            G(h8);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.f
    public void i() {
        for (d dVar : this.B) {
            dVar.T();
        }
    }

    public boolean i0(long j8, boolean z4) {
        this.f21929d0 = j8;
        if (P()) {
            this.f21931e0 = j8;
            return true;
        }
        if (this.L && !z4 && h0(j8)) {
            return false;
        }
        this.f21931e0 = j8;
        this.f21937h0 = false;
        this.f21947p.clear();
        if (this.f21941k.j()) {
            if (this.L) {
                for (d dVar : this.B) {
                    dVar.r();
                }
            }
            this.f21941k.f();
        } else {
            this.f21941k.g();
            g0();
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0131  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean j0(z5.r[] r20, boolean[] r21, h5.r[] r22, boolean[] r23, long r24, boolean r26) {
        /*
            Method dump skipped, instructions count: 326
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: m5.p.j0(z5.r[], boolean[], h5.r[], boolean[], long, boolean):boolean");
    }

    public void k0(DrmInitData drmInitData) {
        if (l0.c(this.f21942k0, drmInitData)) {
            return;
        }
        this.f21942k0 = drmInitData;
        int i8 = 0;
        while (true) {
            d[] dVarArr = this.B;
            if (i8 >= dVarArr.length) {
                return;
            }
            if (this.f21927c0[i8]) {
                dVarArr[i8].i0(drmInitData);
            }
            i8++;
        }
    }

    public void l() {
        U();
        if (this.f21937h0 && !this.O) {
            throw ParserException.a("Loading finished before preparation is complete.", null);
        }
    }

    @Override // n4.m
    public void m(n4.z zVar) {
    }

    public void m0(boolean z4) {
        this.f21928d.t(z4);
    }

    public void n0(long j8) {
        if (this.f21940j0 != j8) {
            this.f21940j0 = j8;
            for (d dVar : this.B) {
                dVar.a0(j8);
            }
        }
    }

    @Override // n4.m
    public void o() {
        this.f21938i0 = true;
        this.f21950x.post(this.f21949w);
    }

    public int o0(int i8, long j8) {
        if (P()) {
            return 0;
        }
        d dVar = this.B[i8];
        int E = dVar.E(j8, this.f21937h0);
        i iVar = (i) f1.g(this.f21947p, null);
        if (iVar != null && !iVar.q()) {
            E = Math.min(E, iVar.m(i8) - dVar.C());
        }
        dVar.e0(E);
        return E;
    }

    public void p0(int i8) {
        x();
        b6.a.e(this.Y);
        int i9 = this.Y[i8];
        b6.a.f(this.f21925b0[i9]);
        this.f21925b0[i9] = false;
    }

    public h5.w r() {
        x();
        return this.W;
    }

    public void u(long j8, boolean z4) {
        if (!this.L || P()) {
            return;
        }
        int length = this.B.length;
        for (int i8 = 0; i8 < length; i8++) {
            this.B[i8].q(j8, z4, this.f21925b0[i8]);
        }
    }

    public int y(int i8) {
        x();
        b6.a.e(this.Y);
        int i9 = this.Y[i8];
        if (i9 == -1) {
            return this.X.contains(this.W.b(i8)) ? -3 : -2;
        }
        boolean[] zArr = this.f21925b0;
        if (zArr[i9]) {
            return -2;
        }
        zArr[i9] = true;
        return i9;
    }
}
