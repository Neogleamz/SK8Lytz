package i4;

import a6.k;
import b6.l0;
import b6.p;
import com.google.android.exoplayer2.c2;
import h5.w;
import z5.r;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements u {

    /* renamed from: a  reason: collision with root package name */
    private final k f20470a;

    /* renamed from: b  reason: collision with root package name */
    private final long f20471b;

    /* renamed from: c  reason: collision with root package name */
    private final long f20472c;

    /* renamed from: d  reason: collision with root package name */
    private final long f20473d;

    /* renamed from: e  reason: collision with root package name */
    private final long f20474e;

    /* renamed from: f  reason: collision with root package name */
    private final int f20475f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f20476g;

    /* renamed from: h  reason: collision with root package name */
    private final long f20477h;

    /* renamed from: i  reason: collision with root package name */
    private final boolean f20478i;

    /* renamed from: j  reason: collision with root package name */
    private int f20479j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f20480k;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private k f20481a;

        /* renamed from: b  reason: collision with root package name */
        private int f20482b = 50000;

        /* renamed from: c  reason: collision with root package name */
        private int f20483c = 50000;

        /* renamed from: d  reason: collision with root package name */
        private int f20484d = 2500;

        /* renamed from: e  reason: collision with root package name */
        private int f20485e = 5000;

        /* renamed from: f  reason: collision with root package name */
        private int f20486f = -1;

        /* renamed from: g  reason: collision with root package name */
        private boolean f20487g = false;

        /* renamed from: h  reason: collision with root package name */
        private int f20488h = 0;

        /* renamed from: i  reason: collision with root package name */
        private boolean f20489i = false;

        /* renamed from: j  reason: collision with root package name */
        private boolean f20490j;

        public c a() {
            b6.a.f(!this.f20490j);
            this.f20490j = true;
            if (this.f20481a == null) {
                this.f20481a = new k(true, 65536);
            }
            return new c(this.f20481a, this.f20482b, this.f20483c, this.f20484d, this.f20485e, this.f20486f, this.f20487g, this.f20488h, this.f20489i);
        }

        public a b(int i8, boolean z4) {
            b6.a.f(!this.f20490j);
            c.k(i8, 0, "backBufferDurationMs", "0");
            this.f20488h = i8;
            this.f20489i = z4;
            return this;
        }

        public a c(int i8, int i9, int i10, int i11) {
            b6.a.f(!this.f20490j);
            c.k(i10, 0, "bufferForPlaybackMs", "0");
            c.k(i11, 0, "bufferForPlaybackAfterRebufferMs", "0");
            c.k(i8, i10, "minBufferMs", "bufferForPlaybackMs");
            c.k(i8, i11, "minBufferMs", "bufferForPlaybackAfterRebufferMs");
            c.k(i9, i8, "maxBufferMs", "minBufferMs");
            this.f20482b = i8;
            this.f20483c = i9;
            this.f20484d = i10;
            this.f20485e = i11;
            return this;
        }

        public a d(boolean z4) {
            b6.a.f(!this.f20490j);
            this.f20487g = z4;
            return this;
        }

        public a e(int i8) {
            b6.a.f(!this.f20490j);
            this.f20486f = i8;
            return this;
        }
    }

    public c() {
        this(new k(true, 65536), 50000, 50000, 2500, 5000, -1, false, 0, false);
    }

    protected c(k kVar, int i8, int i9, int i10, int i11, int i12, boolean z4, int i13, boolean z8) {
        k(i10, 0, "bufferForPlaybackMs", "0");
        k(i11, 0, "bufferForPlaybackAfterRebufferMs", "0");
        k(i8, i10, "minBufferMs", "bufferForPlaybackMs");
        k(i8, i11, "minBufferMs", "bufferForPlaybackAfterRebufferMs");
        k(i9, i8, "maxBufferMs", "minBufferMs");
        k(i13, 0, "backBufferDurationMs", "0");
        this.f20470a = kVar;
        this.f20471b = l0.C0(i8);
        this.f20472c = l0.C0(i9);
        this.f20473d = l0.C0(i10);
        this.f20474e = l0.C0(i11);
        this.f20475f = i12;
        this.f20479j = i12 == -1 ? 13107200 : i12;
        this.f20476g = z4;
        this.f20477h = l0.C0(i13);
        this.f20478i = z8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void k(int i8, int i9, String str, String str2) {
        boolean z4 = i8 >= i9;
        b6.a.b(z4, str + " cannot be less than " + str2);
    }

    private static int m(int i8) {
        switch (i8) {
            case -2:
                return 0;
            case -1:
            default:
                throw new IllegalArgumentException();
            case 0:
                return 144310272;
            case 1:
                return 13107200;
            case 2:
                return 131072000;
            case 3:
            case 4:
            case 5:
            case 6:
                return 131072;
        }
    }

    private void n(boolean z4) {
        int i8 = this.f20475f;
        if (i8 == -1) {
            i8 = 13107200;
        }
        this.f20479j = i8;
        this.f20480k = false;
        if (z4) {
            this.f20470a.g();
        }
    }

    @Override // i4.u
    public void a() {
        n(false);
    }

    @Override // i4.u
    public boolean b() {
        return this.f20478i;
    }

    @Override // i4.u
    public long c() {
        return this.f20477h;
    }

    @Override // i4.u
    public void d() {
        n(true);
    }

    @Override // i4.u
    public void e(c2[] c2VarArr, w wVar, r[] rVarArr) {
        int i8 = this.f20475f;
        if (i8 == -1) {
            i8 = l(c2VarArr, rVarArr);
        }
        this.f20479j = i8;
        this.f20470a.h(i8);
    }

    @Override // i4.u
    public boolean f(long j8, float f5, boolean z4, long j9) {
        long e02 = l0.e0(j8, f5);
        long j10 = z4 ? this.f20474e : this.f20473d;
        if (j9 != -9223372036854775807L) {
            j10 = Math.min(j9 / 2, j10);
        }
        return j10 <= 0 || e02 >= j10 || (!this.f20476g && this.f20470a.f() >= this.f20479j);
    }

    @Override // i4.u
    public boolean g(long j8, long j9, float f5) {
        boolean z4 = true;
        boolean z8 = this.f20470a.f() >= this.f20479j;
        long j10 = this.f20471b;
        if (f5 > 1.0f) {
            j10 = Math.min(l0.Z(j10, f5), this.f20472c);
        }
        if (j9 < Math.max(j10, 500000L)) {
            if (!this.f20476g && z8) {
                z4 = false;
            }
            this.f20480k = z4;
            if (!z4 && j9 < 500000) {
                p.i("DefaultLoadControl", "Target buffer size reached with less than 500ms of buffered media data.");
            }
        } else if (j9 >= this.f20472c || z8) {
            this.f20480k = false;
        }
        return this.f20480k;
    }

    @Override // i4.u
    public a6.b h() {
        return this.f20470a;
    }

    @Override // i4.u
    public void i() {
        n(true);
    }

    protected int l(c2[] c2VarArr, r[] rVarArr) {
        int i8 = 0;
        for (int i9 = 0; i9 < c2VarArr.length; i9++) {
            if (rVarArr[i9] != null) {
                i8 += m(c2VarArr[i9].h());
            }
        }
        return Math.max(13107200, i8);
    }
}
