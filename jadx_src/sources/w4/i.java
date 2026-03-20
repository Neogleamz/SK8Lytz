package w4;

import b6.l0;
import com.google.android.exoplayer2.w0;
import n4.b0;
import n4.l;
import n4.m;
import n4.y;
import n4.z;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class i {

    /* renamed from: b  reason: collision with root package name */
    private b0 f23576b;

    /* renamed from: c  reason: collision with root package name */
    private m f23577c;

    /* renamed from: d  reason: collision with root package name */
    private g f23578d;

    /* renamed from: e  reason: collision with root package name */
    private long f23579e;

    /* renamed from: f  reason: collision with root package name */
    private long f23580f;

    /* renamed from: g  reason: collision with root package name */
    private long f23581g;

    /* renamed from: h  reason: collision with root package name */
    private int f23582h;

    /* renamed from: i  reason: collision with root package name */
    private int f23583i;

    /* renamed from: k  reason: collision with root package name */
    private long f23585k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f23586l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f23587m;

    /* renamed from: a  reason: collision with root package name */
    private final e f23575a = new e();

    /* renamed from: j  reason: collision with root package name */
    private b f23584j = new b();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        w0 f23588a;

        /* renamed from: b  reason: collision with root package name */
        g f23589b;

        b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements g {
        private c() {
        }

        @Override // w4.g
        public long a(l lVar) {
            return -1L;
        }

        @Override // w4.g
        public z b() {
            return new z.b(-9223372036854775807L);
        }

        @Override // w4.g
        public void c(long j8) {
        }
    }

    private void a() {
        b6.a.h(this.f23576b);
        l0.j(this.f23577c);
    }

    private boolean i(l lVar) {
        while (this.f23575a.d(lVar)) {
            this.f23585k = lVar.getPosition() - this.f23580f;
            if (!h(this.f23575a.c(), this.f23580f, this.f23584j)) {
                return true;
            }
            this.f23580f = lVar.getPosition();
        }
        this.f23582h = 3;
        return false;
    }

    private int j(l lVar) {
        if (i(lVar)) {
            w0 w0Var = this.f23584j.f23588a;
            this.f23583i = w0Var.G;
            if (!this.f23587m) {
                this.f23576b.f(w0Var);
                this.f23587m = true;
            }
            g gVar = this.f23584j.f23589b;
            if (gVar == null) {
                if (lVar.b() != -1) {
                    f b9 = this.f23575a.b();
                    this.f23578d = new w4.a(this, this.f23580f, lVar.b(), b9.f23568h + b9.f23569i, b9.f23563c, (b9.f23562b & 4) != 0);
                    this.f23582h = 2;
                    this.f23575a.f();
                    return 0;
                }
                gVar = new c();
            }
            this.f23578d = gVar;
            this.f23582h = 2;
            this.f23575a.f();
            return 0;
        }
        return -1;
    }

    private int k(l lVar, y yVar) {
        long a9 = this.f23578d.a(lVar);
        if (a9 >= 0) {
            yVar.f22152a = a9;
            return 1;
        }
        if (a9 < -1) {
            e(-(a9 + 2));
        }
        if (!this.f23586l) {
            this.f23577c.m((z) b6.a.h(this.f23578d.b()));
            this.f23586l = true;
        }
        if (this.f23585k <= 0 && !this.f23575a.d(lVar)) {
            this.f23582h = 3;
            return -1;
        }
        this.f23585k = 0L;
        b6.z c9 = this.f23575a.c();
        long f5 = f(c9);
        if (f5 >= 0) {
            long j8 = this.f23581g;
            if (j8 + f5 >= this.f23579e) {
                long b9 = b(j8);
                this.f23576b.b(c9, c9.g());
                this.f23576b.d(b9, 1, c9.g(), 0, null);
                this.f23579e = -1L;
            }
        }
        this.f23581g += f5;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long b(long j8) {
        return (j8 * 1000000) / this.f23583i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long c(long j8) {
        return (this.f23583i * j8) / 1000000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(m mVar, b0 b0Var) {
        this.f23577c = mVar;
        this.f23576b = b0Var;
        l(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void e(long j8) {
        this.f23581g = j8;
    }

    protected abstract long f(b6.z zVar);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int g(l lVar, y yVar) {
        a();
        int i8 = this.f23582h;
        if (i8 != 0) {
            if (i8 == 1) {
                lVar.i((int) this.f23580f);
                this.f23582h = 2;
                return 0;
            } else if (i8 == 2) {
                l0.j(this.f23578d);
                return k(lVar, yVar);
            } else if (i8 == 3) {
                return -1;
            } else {
                throw new IllegalStateException();
            }
        }
        return j(lVar);
    }

    protected abstract boolean h(b6.z zVar, long j8, b bVar);

    /* JADX INFO: Access modifiers changed from: protected */
    public void l(boolean z4) {
        int i8;
        if (z4) {
            this.f23584j = new b();
            this.f23580f = 0L;
            i8 = 0;
        } else {
            i8 = 1;
        }
        this.f23582h = i8;
        this.f23579e = -1L;
        this.f23581g = 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void m(long j8, long j9) {
        this.f23575a.e();
        if (j8 == 0) {
            l(!this.f23586l);
        } else if (this.f23582h != 0) {
            this.f23579e = c(j9);
            ((g) l0.j(this.f23578d)).c(this.f23579e);
            this.f23582h = 2;
        }
    }
}
