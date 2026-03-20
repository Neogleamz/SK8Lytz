package p5;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import b6.a;
import b6.l0;
import b6.p;
import b6.t;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import i4.f0;
import i4.s;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n extends com.google.android.exoplayer2.f implements Handler.Callback {
    private int A;
    private w0 B;
    private i C;
    private k E;
    private l F;
    private l G;
    private int H;
    private long K;
    private long L;
    private long O;

    /* renamed from: p  reason: collision with root package name */
    private final Handler f22426p;
    private final m q;

    /* renamed from: t  reason: collision with root package name */
    private final j f22427t;

    /* renamed from: w  reason: collision with root package name */
    private final s f22428w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f22429x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f22430y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f22431z;

    public n(m mVar, Looper looper) {
        this(mVar, looper, j.f22422a);
    }

    public n(m mVar, Looper looper, j jVar) {
        super(3);
        this.q = (m) a.e(mVar);
        this.f22426p = looper == null ? null : l0.v(looper, this);
        this.f22427t = jVar;
        this.f22428w = new s();
        this.K = -9223372036854775807L;
        this.L = -9223372036854775807L;
        this.O = -9223372036854775807L;
    }

    private void Y() {
        j0(new e(ImmutableList.E(), b0(this.O)));
    }

    private long Z(long j8) {
        int c9 = this.F.c(j8);
        if (c9 == 0 || this.F.i() == 0) {
            return this.F.f21597b;
        }
        if (c9 == -1) {
            l lVar = this.F;
            return lVar.f(lVar.i() - 1);
        }
        return this.F.f(c9 - 1);
    }

    private long a0() {
        if (this.H == -1) {
            return Long.MAX_VALUE;
        }
        a.e(this.F);
        if (this.H >= this.F.i()) {
            return Long.MAX_VALUE;
        }
        return this.F.f(this.H);
    }

    private long b0(long j8) {
        a.f(j8 != -9223372036854775807L);
        a.f(this.L != -9223372036854775807L);
        return j8 - this.L;
    }

    private void c0(SubtitleDecoderException subtitleDecoderException) {
        p.d("TextRenderer", "Subtitle decoding failed. streamFormat=" + this.B, subtitleDecoderException);
        Y();
        h0();
    }

    private void d0() {
        this.f22431z = true;
        this.C = this.f22427t.b((w0) a.e(this.B));
    }

    private void e0(e eVar) {
        this.q.r(eVar.f22410a);
        this.q.q(eVar);
    }

    private void f0() {
        this.E = null;
        this.H = -1;
        l lVar = this.F;
        if (lVar != null) {
            lVar.y();
            this.F = null;
        }
        l lVar2 = this.G;
        if (lVar2 != null) {
            lVar2.y();
            this.G = null;
        }
    }

    private void g0() {
        f0();
        ((i) a.e(this.C)).release();
        this.C = null;
        this.A = 0;
    }

    private void h0() {
        g0();
        d0();
    }

    private void j0(e eVar) {
        Handler handler = this.f22426p;
        if (handler != null) {
            handler.obtainMessage(0, eVar).sendToTarget();
        } else {
            e0(eVar);
        }
    }

    @Override // com.google.android.exoplayer2.f
    protected void O() {
        this.B = null;
        this.K = -9223372036854775807L;
        Y();
        this.L = -9223372036854775807L;
        this.O = -9223372036854775807L;
        g0();
    }

    @Override // com.google.android.exoplayer2.f
    protected void Q(long j8, boolean z4) {
        this.O = j8;
        Y();
        this.f22429x = false;
        this.f22430y = false;
        this.K = -9223372036854775807L;
        if (this.A != 0) {
            h0();
            return;
        }
        f0();
        ((i) a.e(this.C)).flush();
    }

    @Override // com.google.android.exoplayer2.f
    protected void U(w0[] w0VarArr, long j8, long j9) {
        this.L = j9;
        this.B = w0VarArr[0];
        if (this.C != null) {
            this.A = 1;
        } else {
            d0();
        }
    }

    @Override // i4.f0
    public int a(w0 w0Var) {
        if (this.f22427t.a(w0Var)) {
            return f0.u(w0Var.R == 0 ? 4 : 2);
        }
        return f0.u(t.r(w0Var.f11207m) ? 1 : 0);
    }

    @Override // com.google.android.exoplayer2.c2
    public boolean b() {
        return this.f22430y;
    }

    @Override // com.google.android.exoplayer2.c2
    public boolean e() {
        return true;
    }

    @Override // com.google.android.exoplayer2.c2, i4.f0
    public String getName() {
        return "TextRenderer";
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what == 0) {
            e0((e) message.obj);
            return true;
        }
        throw new IllegalStateException();
    }

    public void i0(long j8) {
        a.f(D());
        this.K = j8;
    }

    @Override // com.google.android.exoplayer2.c2
    public void w(long j8, long j9) {
        boolean z4;
        this.O = j8;
        if (D()) {
            long j10 = this.K;
            if (j10 != -9223372036854775807L && j8 >= j10) {
                f0();
                this.f22430y = true;
            }
        }
        if (this.f22430y) {
            return;
        }
        if (this.G == null) {
            ((i) a.e(this.C)).a(j8);
            try {
                this.G = ((i) a.e(this.C)).b();
            } catch (SubtitleDecoderException e8) {
                c0(e8);
                return;
            }
        }
        if (getState() != 2) {
            return;
        }
        if (this.F != null) {
            long a02 = a0();
            z4 = false;
            while (a02 <= j8) {
                this.H++;
                a02 = a0();
                z4 = true;
            }
        } else {
            z4 = false;
        }
        l lVar = this.G;
        if (lVar != null) {
            if (lVar.t()) {
                if (!z4 && a0() == Long.MAX_VALUE) {
                    if (this.A == 2) {
                        h0();
                    } else {
                        f0();
                        this.f22430y = true;
                    }
                }
            } else if (lVar.f21597b <= j8) {
                l lVar2 = this.F;
                if (lVar2 != null) {
                    lVar2.y();
                }
                this.H = lVar.c(j8);
                this.F = lVar;
                this.G = null;
                z4 = true;
            }
        }
        if (z4) {
            a.e(this.F);
            j0(new e(this.F.h(j8), b0(Z(j8))));
        }
        if (this.A == 2) {
            return;
        }
        while (!this.f22429x) {
            try {
                k kVar = this.E;
                if (kVar == null) {
                    kVar = ((i) a.e(this.C)).c();
                    if (kVar == null) {
                        return;
                    }
                    this.E = kVar;
                }
                if (this.A == 1) {
                    kVar.x(4);
                    ((i) a.e(this.C)).d(kVar);
                    this.E = null;
                    this.A = 2;
                    return;
                }
                int V = V(this.f22428w, kVar, 0);
                if (V == -4) {
                    if (kVar.t()) {
                        this.f22429x = true;
                        this.f22431z = false;
                    } else {
                        w0 w0Var = this.f22428w.f20512b;
                        if (w0Var == null) {
                            return;
                        }
                        kVar.f22423j = w0Var.f11210t;
                        kVar.A();
                        this.f22431z &= !kVar.v();
                    }
                    if (!this.f22431z) {
                        ((i) a.e(this.C)).d(kVar);
                        this.E = null;
                    }
                } else if (V == -3) {
                    return;
                }
            } catch (SubtitleDecoderException e9) {
                c0(e9);
                return;
            }
        }
    }
}
