package y0;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Choreographer;
import java.util.ArrayList;
import k0.g;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: g  reason: collision with root package name */
    public static final ThreadLocal<a> f24319g = new ThreadLocal<>();

    /* renamed from: d  reason: collision with root package name */
    private c f24323d;

    /* renamed from: a  reason: collision with root package name */
    private final g<b, Long> f24320a = new g<>();

    /* renamed from: b  reason: collision with root package name */
    final ArrayList<b> f24321b = new ArrayList<>();

    /* renamed from: c  reason: collision with root package name */
    private final C0228a f24322c = new C0228a();

    /* renamed from: e  reason: collision with root package name */
    long f24324e = 0;

    /* renamed from: f  reason: collision with root package name */
    private boolean f24325f = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: y0.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0228a {
        C0228a() {
        }

        void a() {
            a.this.f24324e = SystemClock.uptimeMillis();
            a aVar = a.this;
            aVar.c(aVar.f24324e);
            if (a.this.f24321b.size() > 0) {
                a.this.e().a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        boolean a(long j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c {

        /* renamed from: a  reason: collision with root package name */
        final C0228a f24327a;

        c(C0228a c0228a) {
            this.f24327a = c0228a;
        }

        abstract void a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends c {

        /* renamed from: b  reason: collision with root package name */
        private final Runnable f24328b;

        /* renamed from: c  reason: collision with root package name */
        private final Handler f24329c;

        /* renamed from: d  reason: collision with root package name */
        long f24330d;

        /* renamed from: y0.a$d$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class RunnableC0229a implements Runnable {
            RunnableC0229a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                d.this.f24330d = SystemClock.uptimeMillis();
                d.this.f24327a.a();
            }
        }

        d(C0228a c0228a) {
            super(c0228a);
            this.f24330d = -1L;
            this.f24328b = new RunnableC0229a();
            this.f24329c = new Handler(Looper.myLooper());
        }

        @Override // y0.a.c
        void a() {
            this.f24329c.postDelayed(this.f24328b, Math.max(10 - (SystemClock.uptimeMillis() - this.f24330d), 0L));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends c {

        /* renamed from: b  reason: collision with root package name */
        private final Choreographer f24332b;

        /* renamed from: c  reason: collision with root package name */
        private final Choreographer.FrameCallback f24333c;

        /* renamed from: y0.a$e$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class Choreographer$FrameCallbackC0230a implements Choreographer.FrameCallback {
            Choreographer$FrameCallbackC0230a() {
            }

            @Override // android.view.Choreographer.FrameCallback
            public void doFrame(long j8) {
                e.this.f24327a.a();
            }
        }

        e(C0228a c0228a) {
            super(c0228a);
            this.f24332b = Choreographer.getInstance();
            this.f24333c = new Choreographer$FrameCallbackC0230a();
        }

        @Override // y0.a.c
        void a() {
            this.f24332b.postFrameCallback(this.f24333c);
        }
    }

    a() {
    }

    private void b() {
        if (this.f24325f) {
            for (int size = this.f24321b.size() - 1; size >= 0; size--) {
                if (this.f24321b.get(size) == null) {
                    this.f24321b.remove(size);
                }
            }
            this.f24325f = false;
        }
    }

    public static a d() {
        ThreadLocal<a> threadLocal = f24319g;
        if (threadLocal.get() == null) {
            threadLocal.set(new a());
        }
        return threadLocal.get();
    }

    private boolean f(b bVar, long j8) {
        Long l8 = this.f24320a.get(bVar);
        if (l8 == null) {
            return true;
        }
        if (l8.longValue() < j8) {
            this.f24320a.remove(bVar);
            return true;
        }
        return false;
    }

    public void a(b bVar, long j8) {
        if (this.f24321b.size() == 0) {
            e().a();
        }
        if (!this.f24321b.contains(bVar)) {
            this.f24321b.add(bVar);
        }
        if (j8 > 0) {
            this.f24320a.put(bVar, Long.valueOf(SystemClock.uptimeMillis() + j8));
        }
    }

    void c(long j8) {
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int i8 = 0; i8 < this.f24321b.size(); i8++) {
            b bVar = this.f24321b.get(i8);
            if (bVar != null && f(bVar, uptimeMillis)) {
                bVar.a(j8);
            }
        }
        b();
    }

    c e() {
        if (this.f24323d == null) {
            this.f24323d = Build.VERSION.SDK_INT >= 16 ? new e(this.f24322c) : new d(this.f24322c);
        }
        return this.f24323d;
    }

    public void g(b bVar) {
        this.f24320a.remove(bVar);
        int indexOf = this.f24321b.indexOf(bVar);
        if (indexOf >= 0) {
            this.f24321b.set(indexOf, null);
            this.f24325f = true;
        }
    }
}
