package l;

import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends e {

    /* renamed from: c  reason: collision with root package name */
    private static volatile c f21487c;

    /* renamed from: d  reason: collision with root package name */
    private static final Executor f21488d = b.a;

    /* renamed from: e  reason: collision with root package name */
    private static final Executor f21489e = a.a;

    /* renamed from: a  reason: collision with root package name */
    private e f21490a;

    /* renamed from: b  reason: collision with root package name */
    private final e f21491b;

    private c() {
        d dVar = new d();
        this.f21491b = dVar;
        this.f21490a = dVar;
    }

    public static Executor f() {
        return f21489e;
    }

    public static c g() {
        if (f21487c != null) {
            return f21487c;
        }
        synchronized (c.class) {
            if (f21487c == null) {
                f21487c = new c();
            }
        }
        return f21487c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void h(Runnable runnable) {
        g().c(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void i(Runnable runnable) {
        g().a(runnable);
    }

    @Override // l.e
    public void a(Runnable runnable) {
        this.f21490a.a(runnable);
    }

    @Override // l.e
    public boolean b() {
        return this.f21490a.b();
    }

    @Override // l.e
    public void c(Runnable runnable) {
        this.f21490a.c(runnable);
    }
}
