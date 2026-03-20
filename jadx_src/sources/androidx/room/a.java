package androidx.room;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a {

    /* renamed from: e  reason: collision with root package name */
    final long f7086e;

    /* renamed from: f  reason: collision with root package name */
    final Executor f7087f;

    /* renamed from: i  reason: collision with root package name */
    t1.b f7090i;

    /* renamed from: a  reason: collision with root package name */
    private t1.c f7082a = null;

    /* renamed from: b  reason: collision with root package name */
    private final Handler f7083b = new Handler(Looper.getMainLooper());

    /* renamed from: c  reason: collision with root package name */
    Runnable f7084c = null;

    /* renamed from: d  reason: collision with root package name */
    final Object f7085d = new Object();

    /* renamed from: g  reason: collision with root package name */
    int f7088g = 0;

    /* renamed from: h  reason: collision with root package name */
    long f7089h = SystemClock.uptimeMillis();

    /* renamed from: j  reason: collision with root package name */
    private boolean f7091j = false;

    /* renamed from: k  reason: collision with root package name */
    private final Runnable f7092k = new RunnableC0075a();

    /* renamed from: l  reason: collision with root package name */
    final Runnable f7093l = new b();

    /* renamed from: androidx.room.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class RunnableC0075a implements Runnable {
        RunnableC0075a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            a aVar = a.this;
            aVar.f7087f.execute(aVar.f7093l);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (a.this.f7085d) {
                long uptimeMillis = SystemClock.uptimeMillis();
                a aVar = a.this;
                if (uptimeMillis - aVar.f7089h < aVar.f7086e) {
                    return;
                }
                if (aVar.f7088g != 0) {
                    return;
                }
                Runnable runnable = aVar.f7084c;
                if (runnable == null) {
                    throw new IllegalStateException("mOnAutoCloseCallback is null but it should have been set before use. Please file a bug against Room at: https://issuetracker.google.com/issues/new?component=413107&template=1096568");
                }
                runnable.run();
                t1.b bVar = a.this.f7090i;
                if (bVar != null && bVar.isOpen()) {
                    try {
                        a.this.f7090i.close();
                    } catch (IOException e8) {
                        r1.e.a(e8);
                    }
                    a.this.f7090i = null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(long j8, TimeUnit timeUnit, Executor executor) {
        this.f7086e = timeUnit.toMillis(j8);
        this.f7087f = executor;
    }

    public void a() {
        synchronized (this.f7085d) {
            this.f7091j = true;
            t1.b bVar = this.f7090i;
            if (bVar != null) {
                bVar.close();
            }
            this.f7090i = null;
        }
    }

    public void b() {
        synchronized (this.f7085d) {
            int i8 = this.f7088g;
            if (i8 <= 0) {
                throw new IllegalStateException("ref count is 0 or lower but we're supposed to decrement");
            }
            int i9 = i8 - 1;
            this.f7088g = i9;
            if (i9 == 0) {
                if (this.f7090i == null) {
                    return;
                }
                this.f7083b.postDelayed(this.f7092k, this.f7086e);
            }
        }
    }

    public <V> V c(n.a<t1.b, V> aVar) {
        try {
            return aVar.apply(e());
        } finally {
            b();
        }
    }

    public t1.b d() {
        t1.b bVar;
        synchronized (this.f7085d) {
            bVar = this.f7090i;
        }
        return bVar;
    }

    public t1.b e() {
        synchronized (this.f7085d) {
            this.f7083b.removeCallbacks(this.f7092k);
            this.f7088g++;
            if (this.f7091j) {
                throw new IllegalStateException("Attempting to open already closed database.");
            }
            t1.b bVar = this.f7090i;
            if (bVar != null && bVar.isOpen()) {
                return this.f7090i;
            }
            t1.c cVar = this.f7082a;
            if (cVar != null) {
                t1.b v02 = cVar.v0();
                this.f7090i = v02;
                return v02;
            }
            throw new IllegalStateException("AutoCloser has not been initialized. Please file a bug against Room at: https://issuetracker.google.com/issues/new?component=413107&template=1096568");
        }
    }

    public void f(t1.c cVar) {
        if (this.f7082a != null) {
            Log.e("ROOM", "AutoCloser initialized multiple times. Please file a bug against room at: https://issuetracker.google.com/issues/new?component=413107&template=1096568");
        } else {
            this.f7082a = cVar;
        }
    }

    public boolean g() {
        return !this.f7091j;
    }

    public void h(Runnable runnable) {
        this.f7084c = runnable;
    }
}
