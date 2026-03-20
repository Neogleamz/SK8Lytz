package androidx.lifecycle;

import android.os.Handler;
import androidx.lifecycle.Lifecycle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c0 {

    /* renamed from: a  reason: collision with root package name */
    private final k f5857a;

    /* renamed from: b  reason: collision with root package name */
    private final Handler f5858b;

    /* renamed from: c  reason: collision with root package name */
    private a f5859c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final k f5860a;

        /* renamed from: b  reason: collision with root package name */
        private final Lifecycle.Event f5861b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f5862c;

        public a(k kVar, Lifecycle.Event event) {
            kotlin.jvm.internal.p.e(kVar, "registry");
            kotlin.jvm.internal.p.e(event, "event");
            this.f5860a = kVar;
            this.f5861b = event;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f5862c) {
                return;
            }
            this.f5860a.h(this.f5861b);
            this.f5862c = true;
        }
    }

    public c0(j jVar) {
        kotlin.jvm.internal.p.e(jVar, "provider");
        this.f5857a = new k(jVar);
        this.f5858b = new Handler();
    }

    private final void f(Lifecycle.Event event) {
        a aVar = this.f5859c;
        if (aVar != null) {
            aVar.run();
        }
        a aVar2 = new a(this.f5857a, event);
        this.f5859c = aVar2;
        Handler handler = this.f5858b;
        kotlin.jvm.internal.p.b(aVar2);
        handler.postAtFrontOfQueue(aVar2);
    }

    public Lifecycle a() {
        return this.f5857a;
    }

    public void b() {
        f(Lifecycle.Event.ON_START);
    }

    public void c() {
        f(Lifecycle.Event.ON_CREATE);
    }

    public void d() {
        f(Lifecycle.Event.ON_STOP);
        f(Lifecycle.Event.ON_DESTROY);
    }

    public void e() {
        f(Lifecycle.Event.ON_START);
    }
}
