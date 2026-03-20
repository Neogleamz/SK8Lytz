package w4;

import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import n4.e0;
import w4.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class j extends i {

    /* renamed from: n  reason: collision with root package name */
    private a f23590n;

    /* renamed from: o  reason: collision with root package name */
    private int f23591o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f23592p;
    private e0.d q;

    /* renamed from: r  reason: collision with root package name */
    private e0.b f23593r;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final e0.d f23594a;

        /* renamed from: b  reason: collision with root package name */
        public final e0.b f23595b;

        /* renamed from: c  reason: collision with root package name */
        public final byte[] f23596c;

        /* renamed from: d  reason: collision with root package name */
        public final e0.c[] f23597d;

        /* renamed from: e  reason: collision with root package name */
        public final int f23598e;

        public a(e0.d dVar, e0.b bVar, byte[] bArr, e0.c[] cVarArr, int i8) {
            this.f23594a = dVar;
            this.f23595b = bVar;
            this.f23596c = bArr;
            this.f23597d = cVarArr;
            this.f23598e = i8;
        }
    }

    static void n(z zVar, long j8) {
        if (zVar.b() < zVar.g() + 4) {
            zVar.R(Arrays.copyOf(zVar.e(), zVar.g() + 4));
        } else {
            zVar.T(zVar.g() + 4);
        }
        byte[] e8 = zVar.e();
        e8[zVar.g() - 4] = (byte) (j8 & 255);
        e8[zVar.g() - 3] = (byte) ((j8 >>> 8) & 255);
        e8[zVar.g() - 2] = (byte) ((j8 >>> 16) & 255);
        e8[zVar.g() - 1] = (byte) ((j8 >>> 24) & 255);
    }

    private static int o(byte b9, a aVar) {
        return !aVar.f23597d[p(b9, aVar.f23598e, 1)].f22091a ? aVar.f23594a.f22101g : aVar.f23594a.f22102h;
    }

    static int p(byte b9, int i8, int i9) {
        return (b9 >> i9) & (255 >>> (8 - i8));
    }

    public static boolean r(z zVar) {
        try {
            return e0.m(1, zVar, true);
        } catch (ParserException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // w4.i
    public void e(long j8) {
        super.e(j8);
        this.f23592p = j8 != 0;
        e0.d dVar = this.q;
        this.f23591o = dVar != null ? dVar.f22101g : 0;
    }

    @Override // w4.i
    protected long f(z zVar) {
        if ((zVar.e()[0] & 1) == 1) {
            return -1L;
        }
        int o5 = o(zVar.e()[0], (a) b6.a.h(this.f23590n));
        long j8 = this.f23592p ? (this.f23591o + o5) / 4 : 0;
        n(zVar, j8);
        this.f23592p = true;
        this.f23591o = o5;
        return j8;
    }

    @Override // w4.i
    protected boolean h(z zVar, long j8, i.b bVar) {
        if (this.f23590n != null) {
            b6.a.e(bVar.f23588a);
            return false;
        }
        a q = q(zVar);
        this.f23590n = q;
        if (q == null) {
            return true;
        }
        e0.d dVar = q.f23594a;
        ArrayList arrayList = new ArrayList();
        arrayList.add(dVar.f22104j);
        arrayList.add(q.f23596c);
        bVar.f23588a = new w0.b().g0("audio/vorbis").I(dVar.f22099e).b0(dVar.f22098d).J(dVar.f22096b).h0(dVar.f22097c).V(arrayList).Z(e0.c(ImmutableList.y(q.f23595b.f22089b))).G();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // w4.i
    public void l(boolean z4) {
        super.l(z4);
        if (z4) {
            this.f23590n = null;
            this.q = null;
            this.f23593r = null;
        }
        this.f23591o = 0;
        this.f23592p = false;
    }

    a q(z zVar) {
        e0.d dVar = this.q;
        if (dVar == null) {
            this.q = e0.k(zVar);
            return null;
        }
        e0.b bVar = this.f23593r;
        if (bVar == null) {
            this.f23593r = e0.i(zVar);
            return null;
        }
        byte[] bArr = new byte[zVar.g()];
        System.arraycopy(zVar.e(), 0, bArr, 0, zVar.g());
        e0.c[] l8 = e0.l(zVar, dVar.f22096b);
        return new a(dVar, bVar, bArr, l8, e0.a(l8.length - 1));
    }
}
