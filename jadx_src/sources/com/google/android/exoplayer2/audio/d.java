package com.google.android.exoplayer2.audio;

import android.media.AudioTrack;
import android.os.SystemClock;
import b6.l0;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d {
    private long A;
    private long B;
    private long C;
    private long D;
    private boolean E;
    private long F;
    private long G;

    /* renamed from: a  reason: collision with root package name */
    private final a f9345a;

    /* renamed from: b  reason: collision with root package name */
    private final long[] f9346b;

    /* renamed from: c  reason: collision with root package name */
    private AudioTrack f9347c;

    /* renamed from: d  reason: collision with root package name */
    private int f9348d;

    /* renamed from: e  reason: collision with root package name */
    private int f9349e;

    /* renamed from: f  reason: collision with root package name */
    private c f9350f;

    /* renamed from: g  reason: collision with root package name */
    private int f9351g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f9352h;

    /* renamed from: i  reason: collision with root package name */
    private long f9353i;

    /* renamed from: j  reason: collision with root package name */
    private float f9354j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f9355k;

    /* renamed from: l  reason: collision with root package name */
    private long f9356l;

    /* renamed from: m  reason: collision with root package name */
    private long f9357m;

    /* renamed from: n  reason: collision with root package name */
    private Method f9358n;

    /* renamed from: o  reason: collision with root package name */
    private long f9359o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f9360p;
    private boolean q;

    /* renamed from: r  reason: collision with root package name */
    private long f9361r;

    /* renamed from: s  reason: collision with root package name */
    private long f9362s;

    /* renamed from: t  reason: collision with root package name */
    private long f9363t;

    /* renamed from: u  reason: collision with root package name */
    private long f9364u;

    /* renamed from: v  reason: collision with root package name */
    private long f9365v;

    /* renamed from: w  reason: collision with root package name */
    private int f9366w;

    /* renamed from: x  reason: collision with root package name */
    private int f9367x;

    /* renamed from: y  reason: collision with root package name */
    private long f9368y;

    /* renamed from: z  reason: collision with root package name */
    private long f9369z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(int i8, long j8);

        void b(long j8);

        void c(long j8);

        void d(long j8, long j9, long j10, long j11);

        void e(long j8, long j9, long j10, long j11);
    }

    public d(a aVar) {
        this.f9345a = (a) b6.a.e(aVar);
        if (l0.f8063a >= 18) {
            try {
                this.f9358n = AudioTrack.class.getMethod("getLatency", null);
            } catch (NoSuchMethodException unused) {
            }
        }
        this.f9346b = new long[10];
    }

    private boolean a() {
        return this.f9352h && ((AudioTrack) b6.a.e(this.f9347c)).getPlayState() == 2 && e() == 0;
    }

    private long b(long j8) {
        return (j8 * 1000000) / this.f9351g;
    }

    private long e() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j8 = this.f9368y;
        if (j8 != -9223372036854775807L) {
            return Math.min(this.B, this.A + ((l0.Z((elapsedRealtime * 1000) - j8, this.f9354j) * this.f9351g) / 1000000));
        }
        if (elapsedRealtime - this.f9362s >= 5) {
            v(elapsedRealtime);
            this.f9362s = elapsedRealtime;
        }
        return this.f9363t + (this.f9364u << 32);
    }

    private long f() {
        return b(e());
    }

    private void l(long j8) {
        c cVar = (c) b6.a.e(this.f9350f);
        if (cVar.e(j8)) {
            long c9 = cVar.c();
            long b9 = cVar.b();
            long f5 = f();
            if (Math.abs(c9 - j8) > 5000000) {
                this.f9345a.e(b9, c9, j8, f5);
            } else if (Math.abs(b(b9) - f5) <= 5000000) {
                cVar.a();
                return;
            } else {
                this.f9345a.d(b9, c9, j8, f5);
            }
            cVar.f();
        }
    }

    private void m() {
        long nanoTime = System.nanoTime() / 1000;
        if (nanoTime - this.f9357m >= 30000) {
            long f5 = f();
            if (f5 != 0) {
                this.f9346b[this.f9366w] = l0.e0(f5, this.f9354j) - nanoTime;
                this.f9366w = (this.f9366w + 1) % 10;
                int i8 = this.f9367x;
                if (i8 < 10) {
                    this.f9367x = i8 + 1;
                }
                this.f9357m = nanoTime;
                this.f9356l = 0L;
                int i9 = 0;
                while (true) {
                    int i10 = this.f9367x;
                    if (i9 >= i10) {
                        break;
                    }
                    this.f9356l += this.f9346b[i9] / i10;
                    i9++;
                }
            } else {
                return;
            }
        }
        if (this.f9352h) {
            return;
        }
        l(nanoTime);
        n(nanoTime);
    }

    private void n(long j8) {
        Method method;
        if (!this.q || (method = this.f9358n) == null || j8 - this.f9361r < 500000) {
            return;
        }
        try {
            long intValue = (((Integer) l0.j((Integer) method.invoke(b6.a.e(this.f9347c), new Object[0]))).intValue() * 1000) - this.f9353i;
            this.f9359o = intValue;
            long max = Math.max(intValue, 0L);
            this.f9359o = max;
            if (max > 5000000) {
                this.f9345a.b(max);
                this.f9359o = 0L;
            }
        } catch (Exception unused) {
            this.f9358n = null;
        }
        this.f9361r = j8;
    }

    private static boolean o(int i8) {
        return l0.f8063a < 23 && (i8 == 5 || i8 == 6);
    }

    private void r() {
        this.f9356l = 0L;
        this.f9367x = 0;
        this.f9366w = 0;
        this.f9357m = 0L;
        this.D = 0L;
        this.G = 0L;
        this.f9355k = false;
    }

    private void v(long j8) {
        AudioTrack audioTrack;
        int playState = ((AudioTrack) b6.a.e(this.f9347c)).getPlayState();
        if (playState == 1) {
            return;
        }
        long playbackHeadPosition = 4294967295L & audioTrack.getPlaybackHeadPosition();
        if (this.f9352h) {
            if (playState == 2 && playbackHeadPosition == 0) {
                this.f9365v = this.f9363t;
            }
            playbackHeadPosition += this.f9365v;
        }
        if (l0.f8063a <= 29) {
            if (playbackHeadPosition == 0 && this.f9363t > 0 && playState == 3) {
                if (this.f9369z == -9223372036854775807L) {
                    this.f9369z = j8;
                    return;
                }
                return;
            }
            this.f9369z = -9223372036854775807L;
        }
        if (this.f9363t > playbackHeadPosition) {
            this.f9364u++;
        }
        this.f9363t = playbackHeadPosition;
    }

    public int c(long j8) {
        return this.f9349e - ((int) (j8 - (e() * this.f9348d)));
    }

    public long d(boolean z4) {
        long f5;
        if (((AudioTrack) b6.a.e(this.f9347c)).getPlayState() == 3) {
            m();
        }
        long nanoTime = System.nanoTime() / 1000;
        c cVar = (c) b6.a.e(this.f9350f);
        boolean d8 = cVar.d();
        if (d8) {
            f5 = b(cVar.b()) + l0.Z(nanoTime - cVar.c(), this.f9354j);
        } else {
            f5 = this.f9367x == 0 ? f() : l0.Z(this.f9356l + nanoTime, this.f9354j);
            if (!z4) {
                f5 = Math.max(0L, f5 - this.f9359o);
            }
        }
        if (this.E != d8) {
            this.G = this.D;
            this.F = this.C;
        }
        long j8 = nanoTime - this.G;
        if (j8 < 1000000) {
            long j9 = (j8 * 1000) / 1000000;
            f5 = ((f5 * j9) + ((1000 - j9) * (this.F + l0.Z(j8, this.f9354j)))) / 1000;
        }
        if (!this.f9355k) {
            long j10 = this.C;
            if (f5 > j10) {
                this.f9355k = true;
                this.f9345a.c(System.currentTimeMillis() - l0.a1(l0.e0(l0.a1(f5 - j10), this.f9354j)));
            }
        }
        this.D = nanoTime;
        this.C = f5;
        this.E = d8;
        return f5;
    }

    public void g(long j8) {
        this.A = e();
        this.f9368y = SystemClock.elapsedRealtime() * 1000;
        this.B = j8;
    }

    public boolean h(long j8) {
        return j8 > e() || a();
    }

    public boolean i() {
        return ((AudioTrack) b6.a.e(this.f9347c)).getPlayState() == 3;
    }

    public boolean j(long j8) {
        return this.f9369z != -9223372036854775807L && j8 > 0 && SystemClock.elapsedRealtime() - this.f9369z >= 200;
    }

    public boolean k(long j8) {
        int playState = ((AudioTrack) b6.a.e(this.f9347c)).getPlayState();
        if (this.f9352h) {
            if (playState == 2) {
                this.f9360p = false;
                return false;
            } else if (playState == 1 && e() == 0) {
                return false;
            }
        }
        boolean z4 = this.f9360p;
        boolean h8 = h(j8);
        this.f9360p = h8;
        if (z4 && !h8 && playState != 1) {
            this.f9345a.a(this.f9349e, l0.a1(this.f9353i));
        }
        return true;
    }

    public boolean p() {
        r();
        if (this.f9368y == -9223372036854775807L) {
            ((c) b6.a.e(this.f9350f)).g();
            return true;
        }
        return false;
    }

    public void q() {
        r();
        this.f9347c = null;
        this.f9350f = null;
    }

    public void s(AudioTrack audioTrack, boolean z4, int i8, int i9, int i10) {
        this.f9347c = audioTrack;
        this.f9348d = i9;
        this.f9349e = i10;
        this.f9350f = new c(audioTrack);
        this.f9351g = audioTrack.getSampleRate();
        this.f9352h = z4 && o(i8);
        boolean u02 = l0.u0(i8);
        this.q = u02;
        this.f9353i = u02 ? b(i10 / i9) : -9223372036854775807L;
        this.f9363t = 0L;
        this.f9364u = 0L;
        this.f9365v = 0L;
        this.f9360p = false;
        this.f9368y = -9223372036854775807L;
        this.f9369z = -9223372036854775807L;
        this.f9361r = 0L;
        this.f9359o = 0L;
        this.f9354j = 1.0f;
    }

    public void t(float f5) {
        this.f9354j = f5;
        c cVar = this.f9350f;
        if (cVar != null) {
            cVar.g();
        }
        r();
    }

    public void u() {
        ((c) b6.a.e(this.f9350f)).g();
    }
}
