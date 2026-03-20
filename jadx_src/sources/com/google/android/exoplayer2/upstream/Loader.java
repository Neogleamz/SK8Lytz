package com.google.android.exoplayer2.upstream;

import a6.t;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import b6.i0;
import b6.l0;
import b6.p;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Loader implements t {

    /* renamed from: d  reason: collision with root package name */
    public static final c f10906d = h(false, -9223372036854775807L);

    /* renamed from: e  reason: collision with root package name */
    public static final c f10907e = h(true, -9223372036854775807L);

    /* renamed from: f  reason: collision with root package name */
    public static final c f10908f = new c(2, -9223372036854775807L);

    /* renamed from: g  reason: collision with root package name */
    public static final c f10909g = new c(3, -9223372036854775807L);

    /* renamed from: a  reason: collision with root package name */
    private final ExecutorService f10910a;

    /* renamed from: b  reason: collision with root package name */
    private d<? extends e> f10911b;

    /* renamed from: c  reason: collision with root package name */
    private IOException f10912c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class UnexpectedLoaderException extends IOException {
        public UnexpectedLoaderException(Throwable th) {
            super("Unexpected " + th.getClass().getSimpleName() + ": " + th.getMessage(), th);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b<T extends e> {
        void j(T t8, long j8, long j9, boolean z4);

        void k(T t8, long j8, long j9);

        c t(T t8, long j8, long j9, IOException iOException, int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        private final int f10913a;

        /* renamed from: b  reason: collision with root package name */
        private final long f10914b;

        private c(int i8, long j8) {
            this.f10913a = i8;
            this.f10914b = j8;
        }

        public boolean c() {
            int i8 = this.f10913a;
            return i8 == 0 || i8 == 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"HandlerLeak"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class d<T extends e> extends Handler implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        public final int f10915a;

        /* renamed from: b  reason: collision with root package name */
        private final T f10916b;

        /* renamed from: c  reason: collision with root package name */
        private final long f10917c;

        /* renamed from: d  reason: collision with root package name */
        private b<T> f10918d;

        /* renamed from: e  reason: collision with root package name */
        private IOException f10919e;

        /* renamed from: f  reason: collision with root package name */
        private int f10920f;

        /* renamed from: g  reason: collision with root package name */
        private Thread f10921g;

        /* renamed from: h  reason: collision with root package name */
        private boolean f10922h;

        /* renamed from: j  reason: collision with root package name */
        private volatile boolean f10923j;

        public d(Looper looper, T t8, b<T> bVar, int i8, long j8) {
            super(looper);
            this.f10916b = t8;
            this.f10918d = bVar;
            this.f10915a = i8;
            this.f10917c = j8;
        }

        private void b() {
            this.f10919e = null;
            Loader.this.f10910a.execute((Runnable) b6.a.e(Loader.this.f10911b));
        }

        private void c() {
            Loader.this.f10911b = null;
        }

        private long d() {
            return Math.min((this.f10920f - 1) * 1000, 5000);
        }

        public void a(boolean z4) {
            this.f10923j = z4;
            this.f10919e = null;
            if (hasMessages(0)) {
                this.f10922h = true;
                removeMessages(0);
                if (!z4) {
                    sendEmptyMessage(1);
                }
            } else {
                synchronized (this) {
                    this.f10922h = true;
                    this.f10916b.c();
                    Thread thread = this.f10921g;
                    if (thread != null) {
                        thread.interrupt();
                    }
                }
            }
            if (z4) {
                c();
                long elapsedRealtime = SystemClock.elapsedRealtime();
                ((b) b6.a.e(this.f10918d)).j(this.f10916b, elapsedRealtime, elapsedRealtime - this.f10917c, true);
                this.f10918d = null;
            }
        }

        public void e(int i8) {
            IOException iOException = this.f10919e;
            if (iOException != null && this.f10920f > i8) {
                throw iOException;
            }
        }

        public void f(long j8) {
            b6.a.f(Loader.this.f10911b == null);
            Loader.this.f10911b = this;
            if (j8 > 0) {
                sendEmptyMessageDelayed(0, j8);
            } else {
                b();
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (this.f10923j) {
                return;
            }
            int i8 = message.what;
            if (i8 == 0) {
                b();
            } else if (i8 == 3) {
                throw ((Error) message.obj);
            } else {
                c();
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long j8 = elapsedRealtime - this.f10917c;
                b bVar = (b) b6.a.e(this.f10918d);
                if (this.f10922h) {
                    bVar.j(this.f10916b, elapsedRealtime, j8, false);
                    return;
                }
                int i9 = message.what;
                if (i9 == 1) {
                    try {
                        bVar.k(this.f10916b, elapsedRealtime, j8);
                    } catch (RuntimeException e8) {
                        p.d("LoadTask", "Unexpected exception handling load completed", e8);
                        Loader.this.f10912c = new UnexpectedLoaderException(e8);
                    }
                } else if (i9 != 2) {
                } else {
                    IOException iOException = (IOException) message.obj;
                    this.f10919e = iOException;
                    int i10 = this.f10920f + 1;
                    this.f10920f = i10;
                    c t8 = bVar.t(this.f10916b, elapsedRealtime, j8, iOException, i10);
                    if (t8.f10913a == 3) {
                        Loader.this.f10912c = this.f10919e;
                    } else if (t8.f10913a != 2) {
                        if (t8.f10913a == 1) {
                            this.f10920f = 1;
                        }
                        f(t8.f10914b != -9223372036854775807L ? t8.f10914b : d());
                    }
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            UnexpectedLoaderException unexpectedLoaderException;
            Message obtainMessage;
            boolean z4;
            try {
                synchronized (this) {
                    z4 = !this.f10922h;
                    this.f10921g = Thread.currentThread();
                }
                if (z4) {
                    i0.a("load:" + this.f10916b.getClass().getSimpleName());
                    try {
                        this.f10916b.a();
                        i0.c();
                    } catch (Throwable th) {
                        i0.c();
                        throw th;
                    }
                }
                synchronized (this) {
                    this.f10921g = null;
                    Thread.interrupted();
                }
                if (this.f10923j) {
                    return;
                }
                sendEmptyMessage(1);
            } catch (IOException e8) {
                if (this.f10923j) {
                    return;
                }
                obtainMessage = obtainMessage(2, e8);
                obtainMessage.sendToTarget();
            } catch (Error e9) {
                if (!this.f10923j) {
                    p.d("LoadTask", "Unexpected error loading stream", e9);
                    obtainMessage(3, e9).sendToTarget();
                }
                throw e9;
            } catch (Exception e10) {
                if (this.f10923j) {
                    return;
                }
                p.d("LoadTask", "Unexpected exception loading stream", e10);
                unexpectedLoaderException = new UnexpectedLoaderException(e10);
                obtainMessage = obtainMessage(2, unexpectedLoaderException);
                obtainMessage.sendToTarget();
            } catch (OutOfMemoryError e11) {
                if (this.f10923j) {
                    return;
                }
                p.d("LoadTask", "OutOfMemory error loading stream", e11);
                unexpectedLoaderException = new UnexpectedLoaderException(e11);
                obtainMessage = obtainMessage(2, unexpectedLoaderException);
                obtainMessage.sendToTarget();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        void a();

        void c();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        void i();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final f f10925a;

        public g(f fVar) {
            this.f10925a = fVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f10925a.i();
        }
    }

    public Loader(String str) {
        this.f10910a = l0.D0("ExoPlayer:Loader:" + str);
    }

    public static c h(boolean z4, long j8) {
        return new c(z4 ? 1 : 0, j8);
    }

    @Override // a6.t
    public void a() {
        k(Integer.MIN_VALUE);
    }

    public void f() {
        ((d) b6.a.h(this.f10911b)).a(false);
    }

    public void g() {
        this.f10912c = null;
    }

    public boolean i() {
        return this.f10912c != null;
    }

    public boolean j() {
        return this.f10911b != null;
    }

    public void k(int i8) {
        IOException iOException = this.f10912c;
        if (iOException != null) {
            throw iOException;
        }
        d<? extends e> dVar = this.f10911b;
        if (dVar != null) {
            if (i8 == Integer.MIN_VALUE) {
                i8 = dVar.f10915a;
            }
            dVar.e(i8);
        }
    }

    public void l() {
        m(null);
    }

    public void m(f fVar) {
        d<? extends e> dVar = this.f10911b;
        if (dVar != null) {
            dVar.a(true);
        }
        if (fVar != null) {
            this.f10910a.execute(new g(fVar));
        }
        this.f10910a.shutdown();
    }

    public <T extends e> long n(T t8, b<T> bVar, int i8) {
        this.f10912c = null;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        new d((Looper) b6.a.h(Looper.myLooper()), t8, bVar, i8, elapsedRealtime).f(0L);
        return elapsedRealtime;
    }
}
