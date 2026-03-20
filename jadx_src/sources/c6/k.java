package c6;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Choreographer;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import b6.l0;
import com.google.android.exoplayer2.video.PlaceholderSurface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k {

    /* renamed from: a  reason: collision with root package name */
    private final c6.e f8401a = new c6.e();

    /* renamed from: b  reason: collision with root package name */
    private final b f8402b;

    /* renamed from: c  reason: collision with root package name */
    private final e f8403c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f8404d;

    /* renamed from: e  reason: collision with root package name */
    private Surface f8405e;

    /* renamed from: f  reason: collision with root package name */
    private float f8406f;

    /* renamed from: g  reason: collision with root package name */
    private float f8407g;

    /* renamed from: h  reason: collision with root package name */
    private float f8408h;

    /* renamed from: i  reason: collision with root package name */
    private float f8409i;

    /* renamed from: j  reason: collision with root package name */
    private int f8410j;

    /* renamed from: k  reason: collision with root package name */
    private long f8411k;

    /* renamed from: l  reason: collision with root package name */
    private long f8412l;

    /* renamed from: m  reason: collision with root package name */
    private long f8413m;

    /* renamed from: n  reason: collision with root package name */
    private long f8414n;

    /* renamed from: o  reason: collision with root package name */
    private long f8415o;

    /* renamed from: p  reason: collision with root package name */
    private long f8416p;
    private long q;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        public static void a(Surface surface, float f5) {
            try {
                surface.setFrameRate(f5, f5 == 0.0f ? 0 : 1);
            } catch (IllegalStateException e8) {
                b6.p.d("VideoFrameReleaseHelper", "Failed to call Surface.setFrameRate", e8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface a {
            void a(Display display);
        }

        void a(a aVar);

        void b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements b {

        /* renamed from: a  reason: collision with root package name */
        private final WindowManager f8417a;

        private c(WindowManager windowManager) {
            this.f8417a = windowManager;
        }

        public static b c(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            if (windowManager != null) {
                return new c(windowManager);
            }
            return null;
        }

        @Override // c6.k.b
        public void a(b.a aVar) {
            aVar.a(this.f8417a.getDefaultDisplay());
        }

        @Override // c6.k.b
        public void b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d implements b, DisplayManager.DisplayListener {

        /* renamed from: a  reason: collision with root package name */
        private final DisplayManager f8418a;

        /* renamed from: b  reason: collision with root package name */
        private b.a f8419b;

        private d(DisplayManager displayManager) {
            this.f8418a = displayManager;
        }

        private Display c() {
            return this.f8418a.getDisplay(0);
        }

        public static b d(Context context) {
            DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
            if (displayManager != null) {
                return new d(displayManager);
            }
            return null;
        }

        @Override // c6.k.b
        public void a(b.a aVar) {
            this.f8419b = aVar;
            this.f8418a.registerDisplayListener(this, l0.w());
            aVar.a(c());
        }

        @Override // c6.k.b
        public void b() {
            this.f8418a.unregisterDisplayListener(this);
            this.f8419b = null;
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i8) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i8) {
            b.a aVar = this.f8419b;
            if (aVar == null || i8 != 0) {
                return;
            }
            aVar.a(c());
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i8) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class e implements Choreographer.FrameCallback, Handler.Callback {

        /* renamed from: f  reason: collision with root package name */
        private static final e f8420f = new e();

        /* renamed from: a  reason: collision with root package name */
        public volatile long f8421a = -9223372036854775807L;

        /* renamed from: b  reason: collision with root package name */
        private final Handler f8422b;

        /* renamed from: c  reason: collision with root package name */
        private final HandlerThread f8423c;

        /* renamed from: d  reason: collision with root package name */
        private Choreographer f8424d;

        /* renamed from: e  reason: collision with root package name */
        private int f8425e;

        private e() {
            HandlerThread handlerThread = new HandlerThread("ExoPlayer:FrameReleaseChoreographer");
            this.f8423c = handlerThread;
            handlerThread.start();
            Handler v8 = l0.v(handlerThread.getLooper(), this);
            this.f8422b = v8;
            v8.sendEmptyMessage(0);
        }

        private void b() {
            Choreographer choreographer = this.f8424d;
            if (choreographer != null) {
                int i8 = this.f8425e + 1;
                this.f8425e = i8;
                if (i8 == 1) {
                    choreographer.postFrameCallback(this);
                }
            }
        }

        private void c() {
            try {
                this.f8424d = Choreographer.getInstance();
            } catch (RuntimeException e8) {
                b6.p.j("VideoFrameReleaseHelper", "Vsync sampling disabled due to platform error", e8);
            }
        }

        public static e d() {
            return f8420f;
        }

        private void f() {
            Choreographer choreographer = this.f8424d;
            if (choreographer != null) {
                int i8 = this.f8425e - 1;
                this.f8425e = i8;
                if (i8 == 0) {
                    choreographer.removeFrameCallback(this);
                    this.f8421a = -9223372036854775807L;
                }
            }
        }

        public void a() {
            this.f8422b.sendEmptyMessage(1);
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long j8) {
            this.f8421a = j8;
            ((Choreographer) b6.a.e(this.f8424d)).postFrameCallbackDelayed(this, 500L);
        }

        public void e() {
            this.f8422b.sendEmptyMessage(2);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i8 = message.what;
            if (i8 == 0) {
                c();
                return true;
            } else if (i8 == 1) {
                b();
                return true;
            } else if (i8 != 2) {
                return false;
            } else {
                f();
                return true;
            }
        }
    }

    public k(Context context) {
        b f5 = f(context);
        this.f8402b = f5;
        this.f8403c = f5 != null ? e.d() : null;
        this.f8411k = -9223372036854775807L;
        this.f8412l = -9223372036854775807L;
        this.f8406f = -1.0f;
        this.f8409i = 1.0f;
        this.f8410j = 0;
    }

    private static boolean c(long j8, long j9) {
        return Math.abs(j8 - j9) <= 20000000;
    }

    private void d() {
        Surface surface;
        if (l0.f8063a < 30 || (surface = this.f8405e) == null || this.f8410j == Integer.MIN_VALUE || this.f8408h == 0.0f) {
            return;
        }
        this.f8408h = 0.0f;
        a.a(surface, 0.0f);
    }

    private static long e(long j8, long j9, long j10) {
        long j11;
        long j12 = j9 + (((j8 - j9) / j10) * j10);
        if (j8 <= j12) {
            j11 = j12 - j10;
        } else {
            j12 = j10 + j12;
            j11 = j12;
        }
        return j12 - j8 < j8 - j11 ? j12 : j11;
    }

    private static b f(Context context) {
        if (context != null) {
            Context applicationContext = context.getApplicationContext();
            b d8 = l0.f8063a >= 17 ? d.d(applicationContext) : null;
            return d8 == null ? c.c(applicationContext) : d8;
        }
        return null;
    }

    private void n() {
        this.f8413m = 0L;
        this.f8416p = -1L;
        this.f8414n = -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p(Display display) {
        long j8;
        if (display != null) {
            long refreshRate = (long) (1.0E9d / display.getRefreshRate());
            this.f8411k = refreshRate;
            j8 = (refreshRate * 80) / 100;
        } else {
            b6.p.i("VideoFrameReleaseHelper", "Unable to query display refresh rate");
            j8 = -9223372036854775807L;
            this.f8411k = -9223372036854775807L;
        }
        this.f8412l = j8;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x005c, code lost:
        if (java.lang.Math.abs(r0 - r7.f8407g) >= (r7.f8401a.e() && (r7.f8401a.d() > 5000000000L ? 1 : (r7.f8401a.d() == 5000000000L ? 0 : -1)) >= 0 ? 0.02f : 1.0f)) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x005f, code lost:
        r6 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x006a, code lost:
        if (r7.f8401a.c() >= 30) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void q() {
        /*
            r7 = this;
            int r0 = b6.l0.f8063a
            r1 = 30
            if (r0 < r1) goto L73
            android.view.Surface r0 = r7.f8405e
            if (r0 != 0) goto Lc
            goto L73
        Lc:
            c6.e r0 = r7.f8401a
            boolean r0 = r0.e()
            if (r0 == 0) goto L1b
            c6.e r0 = r7.f8401a
            float r0 = r0.b()
            goto L1d
        L1b:
            float r0 = r7.f8406f
        L1d:
            float r2 = r7.f8407g
            int r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r3 != 0) goto L24
            return
        L24:
            r3 = -1082130432(0xffffffffbf800000, float:-1.0)
            int r4 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            r5 = 0
            r6 = 1
            if (r4 == 0) goto L61
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 == 0) goto L61
            c6.e r1 = r7.f8401a
            boolean r1 = r1.e()
            if (r1 == 0) goto L49
            c6.e r1 = r7.f8401a
            long r1 = r1.d()
            r3 = 5000000000(0x12a05f200, double:2.470328229E-314)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 < 0) goto L49
            r1 = r6
            goto L4a
        L49:
            r1 = r5
        L4a:
            if (r1 == 0) goto L50
            r1 = 1017370378(0x3ca3d70a, float:0.02)
            goto L52
        L50:
            r1 = 1065353216(0x3f800000, float:1.0)
        L52:
            float r2 = r7.f8407g
            float r2 = r0 - r2
            float r2 = java.lang.Math.abs(r2)
            int r1 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1))
            if (r1 < 0) goto L5f
            goto L6c
        L5f:
            r6 = r5
            goto L6c
        L61:
            if (r4 == 0) goto L64
            goto L6c
        L64:
            c6.e r2 = r7.f8401a
            int r2 = r2.c()
            if (r2 < r1) goto L5f
        L6c:
            if (r6 == 0) goto L73
            r7.f8407g = r0
            r7.r(r5)
        L73:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: c6.k.q():void");
    }

    private void r(boolean z4) {
        Surface surface;
        if (l0.f8063a < 30 || (surface = this.f8405e) == null || this.f8410j == Integer.MIN_VALUE) {
            return;
        }
        float f5 = 0.0f;
        if (this.f8404d) {
            float f8 = this.f8407g;
            if (f8 != -1.0f) {
                f5 = this.f8409i * f8;
            }
        }
        if (z4 || this.f8408h != f5) {
            this.f8408h = f5;
            a.a(surface, f5);
        }
    }

    public long b(long j8) {
        long j9;
        e eVar;
        if (this.f8416p != -1 && this.f8401a.e()) {
            long a9 = this.q + (((float) (this.f8401a.a() * (this.f8413m - this.f8416p))) / this.f8409i);
            if (c(j8, a9)) {
                j9 = a9;
                this.f8414n = this.f8413m;
                this.f8415o = j9;
                eVar = this.f8403c;
                if (eVar != null || this.f8411k == -9223372036854775807L) {
                    return j9;
                }
                long j10 = eVar.f8421a;
                return j10 == -9223372036854775807L ? j9 : e(j9, j10, this.f8411k) - this.f8412l;
            }
            n();
        }
        j9 = j8;
        this.f8414n = this.f8413m;
        this.f8415o = j9;
        eVar = this.f8403c;
        if (eVar != null) {
        }
        return j9;
    }

    public void g(float f5) {
        this.f8406f = f5;
        this.f8401a.g();
        q();
    }

    public void h(long j8) {
        long j9 = this.f8414n;
        if (j9 != -1) {
            this.f8416p = j9;
            this.q = this.f8415o;
        }
        this.f8413m++;
        this.f8401a.f(j8 * 1000);
        q();
    }

    public void i(float f5) {
        this.f8409i = f5;
        n();
        r(false);
    }

    public void j() {
        n();
    }

    public void k() {
        this.f8404d = true;
        n();
        if (this.f8402b != null) {
            ((e) b6.a.e(this.f8403c)).a();
            this.f8402b.a(new b.a() { // from class: c6.j
                @Override // c6.k.b.a
                public final void a(Display display) {
                    k.this.p(display);
                }
            });
        }
        r(false);
    }

    public void l() {
        this.f8404d = false;
        b bVar = this.f8402b;
        if (bVar != null) {
            bVar.b();
            ((e) b6.a.e(this.f8403c)).e();
        }
        d();
    }

    public void m(Surface surface) {
        if (surface instanceof PlaceholderSurface) {
            surface = null;
        }
        if (this.f8405e == surface) {
            return;
        }
        d();
        this.f8405e = surface;
        r(true);
    }

    public void o(int i8) {
        if (this.f8410j == i8) {
            return;
        }
        this.f8410j = i8;
        r(true);
    }
}
