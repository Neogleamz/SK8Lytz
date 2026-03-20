package x4;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import b6.l0;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import n4.z;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h0 implements n4.k {

    /* renamed from: t  reason: collision with root package name */
    public static final n4.p f23880t = g0.b;

    /* renamed from: a  reason: collision with root package name */
    private final int f23881a;

    /* renamed from: b  reason: collision with root package name */
    private final int f23882b;

    /* renamed from: c  reason: collision with root package name */
    private final List<b6.h0> f23883c;

    /* renamed from: d  reason: collision with root package name */
    private final z f23884d;

    /* renamed from: e  reason: collision with root package name */
    private final SparseIntArray f23885e;

    /* renamed from: f  reason: collision with root package name */
    private final i0.c f23886f;

    /* renamed from: g  reason: collision with root package name */
    private final SparseArray<i0> f23887g;

    /* renamed from: h  reason: collision with root package name */
    private final SparseBooleanArray f23888h;

    /* renamed from: i  reason: collision with root package name */
    private final SparseBooleanArray f23889i;

    /* renamed from: j  reason: collision with root package name */
    private final f0 f23890j;

    /* renamed from: k  reason: collision with root package name */
    private e0 f23891k;

    /* renamed from: l  reason: collision with root package name */
    private n4.m f23892l;

    /* renamed from: m  reason: collision with root package name */
    private int f23893m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f23894n;

    /* renamed from: o  reason: collision with root package name */
    private boolean f23895o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f23896p;
    private i0 q;

    /* renamed from: r  reason: collision with root package name */
    private int f23897r;

    /* renamed from: s  reason: collision with root package name */
    private int f23898s;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements b0 {

        /* renamed from: a  reason: collision with root package name */
        private final b6.y f23899a = new b6.y(new byte[4]);

        public a() {
        }

        @Override // x4.b0
        public void a(b6.h0 h0Var, n4.m mVar, i0.d dVar) {
        }

        @Override // x4.b0
        public void b(z zVar) {
            if (zVar.H() == 0 && (zVar.H() & RecognitionOptions.ITF) != 0) {
                zVar.V(6);
                int a9 = zVar.a() / 4;
                for (int i8 = 0; i8 < a9; i8++) {
                    zVar.k(this.f23899a, 4);
                    int h8 = this.f23899a.h(16);
                    this.f23899a.r(3);
                    if (h8 == 0) {
                        this.f23899a.r(13);
                    } else {
                        int h9 = this.f23899a.h(13);
                        if (h0.this.f23887g.get(h9) == null) {
                            h0.this.f23887g.put(h9, new c0(new b(h9)));
                            h0.k(h0.this);
                        }
                    }
                }
                if (h0.this.f23881a != 2) {
                    h0.this.f23887g.remove(0);
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b implements b0 {

        /* renamed from: a  reason: collision with root package name */
        private final b6.y f23901a = new b6.y(new byte[5]);

        /* renamed from: b  reason: collision with root package name */
        private final SparseArray<i0> f23902b = new SparseArray<>();

        /* renamed from: c  reason: collision with root package name */
        private final SparseIntArray f23903c = new SparseIntArray();

        /* renamed from: d  reason: collision with root package name */
        private final int f23904d;

        public b(int i8) {
            this.f23904d = i8;
        }

        private i0.b c(z zVar, int i8) {
            int f5 = zVar.f();
            int i9 = i8 + f5;
            String str = null;
            int i10 = -1;
            ArrayList arrayList = null;
            while (zVar.f() < i9) {
                int H = zVar.H();
                int f8 = zVar.f() + zVar.H();
                if (f8 > i9) {
                    break;
                }
                if (H == 5) {
                    long J = zVar.J();
                    if (J != 1094921523) {
                        if (J != 1161904947) {
                            if (J != 1094921524) {
                                if (J == 1212503619) {
                                    i10 = 36;
                                }
                            }
                            i10 = 172;
                        }
                        i10 = 135;
                    }
                    i10 = 129;
                } else {
                    if (H != 106) {
                        if (H != 122) {
                            if (H == 127) {
                                if (zVar.H() != 21) {
                                }
                                i10 = 172;
                            } else if (H == 123) {
                                i10 = 138;
                            } else if (H == 10) {
                                str = zVar.E(3).trim();
                            } else if (H == 89) {
                                arrayList = new ArrayList();
                                while (zVar.f() < f8) {
                                    String trim = zVar.E(3).trim();
                                    int H2 = zVar.H();
                                    byte[] bArr = new byte[4];
                                    zVar.l(bArr, 0, 4);
                                    arrayList.add(new i0.a(trim, H2, bArr));
                                }
                                i10 = 89;
                            } else if (H == 111) {
                                i10 = 257;
                            }
                        }
                        i10 = 135;
                    }
                    i10 = 129;
                }
                zVar.V(f8 - zVar.f());
            }
            zVar.U(i9);
            return new i0.b(i10, str, arrayList, Arrays.copyOfRange(zVar.e(), f5, i9));
        }

        @Override // x4.b0
        public void a(b6.h0 h0Var, n4.m mVar, i0.d dVar) {
        }

        @Override // x4.b0
        public void b(z zVar) {
            b6.h0 h0Var;
            if (zVar.H() != 2) {
                return;
            }
            if (h0.this.f23881a == 1 || h0.this.f23881a == 2 || h0.this.f23893m == 1) {
                h0Var = (b6.h0) h0.this.f23883c.get(0);
            } else {
                h0Var = new b6.h0(((b6.h0) h0.this.f23883c.get(0)).c());
                h0.this.f23883c.add(h0Var);
            }
            if ((zVar.H() & RecognitionOptions.ITF) == 0) {
                return;
            }
            zVar.V(1);
            int N = zVar.N();
            int i8 = 3;
            zVar.V(3);
            zVar.k(this.f23901a, 2);
            this.f23901a.r(3);
            int i9 = 13;
            h0.this.f23898s = this.f23901a.h(13);
            zVar.k(this.f23901a, 2);
            int i10 = 4;
            this.f23901a.r(4);
            zVar.V(this.f23901a.h(12));
            if (h0.this.f23881a == 2 && h0.this.q == null) {
                i0.b bVar = new i0.b(21, null, null, l0.f8068f);
                h0 h0Var2 = h0.this;
                h0Var2.q = h0Var2.f23886f.a(21, bVar);
                if (h0.this.q != null) {
                    h0.this.q.a(h0Var, h0.this.f23892l, new i0.d(N, 21, 8192));
                }
            }
            this.f23902b.clear();
            this.f23903c.clear();
            int a9 = zVar.a();
            while (a9 > 0) {
                zVar.k(this.f23901a, 5);
                int h8 = this.f23901a.h(8);
                this.f23901a.r(i8);
                int h9 = this.f23901a.h(i9);
                this.f23901a.r(i10);
                int h10 = this.f23901a.h(12);
                i0.b c9 = c(zVar, h10);
                if (h8 == 6 || h8 == 5) {
                    h8 = c9.f23930a;
                }
                a9 -= h10 + 5;
                int i11 = h0.this.f23881a == 2 ? h8 : h9;
                if (!h0.this.f23888h.get(i11)) {
                    i0 a10 = (h0.this.f23881a == 2 && h8 == 21) ? h0.this.q : h0.this.f23886f.a(h8, c9);
                    if (h0.this.f23881a != 2 || h9 < this.f23903c.get(i11, 8192)) {
                        this.f23903c.put(i11, h9);
                        this.f23902b.put(i11, a10);
                    }
                }
                i8 = 3;
                i10 = 4;
                i9 = 13;
            }
            int size = this.f23903c.size();
            for (int i12 = 0; i12 < size; i12++) {
                int keyAt = this.f23903c.keyAt(i12);
                int valueAt = this.f23903c.valueAt(i12);
                h0.this.f23888h.put(keyAt, true);
                h0.this.f23889i.put(valueAt, true);
                i0 valueAt2 = this.f23902b.valueAt(i12);
                if (valueAt2 != null) {
                    if (valueAt2 != h0.this.q) {
                        valueAt2.a(h0Var, h0.this.f23892l, new i0.d(N, keyAt, 8192));
                    }
                    h0.this.f23887g.put(valueAt, valueAt2);
                }
            }
            if (h0.this.f23881a != 2) {
                h0.this.f23887g.remove(this.f23904d);
                h0 h0Var3 = h0.this;
                h0Var3.f23893m = h0Var3.f23881a == 1 ? 0 : h0.this.f23893m - 1;
                if (h0.this.f23893m != 0) {
                    return;
                }
                h0.this.f23892l.o();
            } else if (h0.this.f23894n) {
                return;
            } else {
                h0.this.f23892l.o();
                h0.this.f23893m = 0;
            }
            h0.this.f23894n = true;
        }
    }

    public h0() {
        this(0);
    }

    public h0(int i8) {
        this(1, i8, 112800);
    }

    public h0(int i8, int i9, int i10) {
        this(i8, new b6.h0(0L), new j(i9), i10);
    }

    public h0(int i8, b6.h0 h0Var, i0.c cVar) {
        this(i8, h0Var, cVar, 112800);
    }

    public h0(int i8, b6.h0 h0Var, i0.c cVar, int i9) {
        this.f23886f = (i0.c) b6.a.e(cVar);
        this.f23882b = i9;
        this.f23881a = i8;
        if (i8 == 1 || i8 == 2) {
            this.f23883c = Collections.singletonList(h0Var);
        } else {
            ArrayList arrayList = new ArrayList();
            this.f23883c = arrayList;
            arrayList.add(h0Var);
        }
        this.f23884d = new z(new byte[9400], 0);
        this.f23888h = new SparseBooleanArray();
        this.f23889i = new SparseBooleanArray();
        this.f23887g = new SparseArray<>();
        this.f23885e = new SparseIntArray();
        this.f23890j = new f0(i9);
        this.f23892l = n4.m.S;
        this.f23898s = -1;
        y();
    }

    static /* synthetic */ int k(h0 h0Var) {
        int i8 = h0Var.f23893m;
        h0Var.f23893m = i8 + 1;
        return i8;
    }

    private boolean u(n4.l lVar) {
        byte[] e8 = this.f23884d.e();
        if (9400 - this.f23884d.f() < 188) {
            int a9 = this.f23884d.a();
            if (a9 > 0) {
                System.arraycopy(e8, this.f23884d.f(), e8, 0, a9);
            }
            this.f23884d.S(e8, a9);
        }
        while (this.f23884d.a() < 188) {
            int g8 = this.f23884d.g();
            int read = lVar.read(e8, g8, 9400 - g8);
            if (read == -1) {
                return false;
            }
            this.f23884d.T(g8 + read);
        }
        return true;
    }

    private int v() {
        int f5 = this.f23884d.f();
        int g8 = this.f23884d.g();
        int a9 = j0.a(this.f23884d.e(), f5, g8);
        this.f23884d.U(a9);
        int i8 = a9 + 188;
        if (i8 > g8) {
            int i9 = this.f23897r + (a9 - f5);
            this.f23897r = i9;
            if (this.f23881a == 2 && i9 > 376) {
                throw ParserException.a("Cannot find sync byte. Most likely not a Transport Stream.", null);
            }
        } else {
            this.f23897r = 0;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ n4.k[] w() {
        return new n4.k[]{new h0()};
    }

    private void x(long j8) {
        n4.m mVar;
        n4.z bVar;
        if (this.f23895o) {
            return;
        }
        this.f23895o = true;
        if (this.f23890j.b() != -9223372036854775807L) {
            e0 e0Var = new e0(this.f23890j.c(), this.f23890j.b(), j8, this.f23898s, this.f23882b);
            this.f23891k = e0Var;
            mVar = this.f23892l;
            bVar = e0Var.b();
        } else {
            mVar = this.f23892l;
            bVar = new z.b(this.f23890j.b());
        }
        mVar.m(bVar);
    }

    private void y() {
        this.f23888h.clear();
        this.f23887g.clear();
        SparseArray<i0> b9 = this.f23886f.b();
        int size = b9.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.f23887g.put(b9.keyAt(i8), b9.valueAt(i8));
        }
        this.f23887g.put(0, new c0(new a()));
        this.q = null;
    }

    private boolean z(int i8) {
        return this.f23881a == 2 || this.f23894n || !this.f23889i.get(i8, false);
    }

    @Override // n4.k
    public void b(n4.m mVar) {
        this.f23892l = mVar;
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        e0 e0Var;
        b6.a.f(this.f23881a != 2);
        int size = this.f23883c.size();
        for (int i8 = 0; i8 < size; i8++) {
            b6.h0 h0Var = this.f23883c.get(i8);
            boolean z4 = h0Var.e() == -9223372036854775807L;
            if (!z4) {
                long c9 = h0Var.c();
                z4 = (c9 == -9223372036854775807L || c9 == 0 || c9 == j9) ? false : true;
            }
            if (z4) {
                h0Var.g(j9);
            }
        }
        if (j9 != 0 && (e0Var = this.f23891k) != null) {
            e0Var.h(j9);
        }
        this.f23884d.Q(0);
        this.f23885e.clear();
        for (int i9 = 0; i9 < this.f23887g.size(); i9++) {
            this.f23887g.valueAt(i9).c();
        }
        this.f23897r = 0;
    }

    @Override // n4.k
    public int e(n4.l lVar, n4.y yVar) {
        long b9 = lVar.b();
        if (this.f23894n) {
            if (((b9 == -1 || this.f23881a == 2) ? false : true) && !this.f23890j.d()) {
                return this.f23890j.e(lVar, yVar, this.f23898s);
            }
            x(b9);
            if (this.f23896p) {
                this.f23896p = false;
                c(0L, 0L);
                if (lVar.getPosition() != 0) {
                    yVar.f22152a = 0L;
                    return 1;
                }
            }
            e0 e0Var = this.f23891k;
            if (e0Var != null && e0Var.d()) {
                return this.f23891k.c(lVar, yVar);
            }
        }
        if (u(lVar)) {
            int v8 = v();
            int g8 = this.f23884d.g();
            if (v8 > g8) {
                return 0;
            }
            int q = this.f23884d.q();
            if ((8388608 & q) == 0) {
                int i8 = ((4194304 & q) != 0 ? 1 : 0) | 0;
                int i9 = (2096896 & q) >> 8;
                boolean z4 = (q & 32) != 0;
                i0 i0Var = (q & 16) != 0 ? this.f23887g.get(i9) : null;
                if (i0Var != null) {
                    if (this.f23881a != 2) {
                        int i10 = q & 15;
                        int i11 = this.f23885e.get(i9, i10 - 1);
                        this.f23885e.put(i9, i10);
                        if (i11 != i10) {
                            if (i10 != ((i11 + 1) & 15)) {
                                i0Var.c();
                            }
                        }
                    }
                    if (z4) {
                        int H = this.f23884d.H();
                        i8 |= (this.f23884d.H() & 64) != 0 ? 2 : 0;
                        this.f23884d.V(H - 1);
                    }
                    boolean z8 = this.f23894n;
                    if (z(i9)) {
                        this.f23884d.T(v8);
                        i0Var.b(this.f23884d, i8);
                        this.f23884d.T(g8);
                    }
                    if (this.f23881a != 2 && !z8 && this.f23894n && b9 != -1) {
                        this.f23896p = true;
                    }
                }
            }
            this.f23884d.U(v8);
            return 0;
        }
        return -1;
    }

    @Override // n4.k
    public boolean g(n4.l lVar) {
        boolean z4;
        byte[] e8 = this.f23884d.e();
        lVar.k(e8, 0, 940);
        for (int i8 = 0; i8 < 188; i8++) {
            int i9 = 0;
            while (true) {
                if (i9 >= 5) {
                    z4 = true;
                    break;
                } else if (e8[(i9 * 188) + i8] != 71) {
                    z4 = false;
                    break;
                } else {
                    i9++;
                }
            }
            if (z4) {
                lVar.i(i8);
                return true;
            }
        }
        return false;
    }

    @Override // n4.k
    public void release() {
    }
}
