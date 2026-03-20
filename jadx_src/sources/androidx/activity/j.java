package androidx.activity;

import cj.a0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f399a;

    /* renamed from: b  reason: collision with root package name */
    private final mj.a<a0> f400b;

    /* renamed from: c  reason: collision with root package name */
    private final Object f401c;

    /* renamed from: d  reason: collision with root package name */
    private int f402d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f403e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f404f;

    /* renamed from: g  reason: collision with root package name */
    private final List<mj.a<a0>> f405g;

    /* renamed from: h  reason: collision with root package name */
    private final Runnable f406h;

    public j(Executor executor, mj.a<a0> aVar) {
        kotlin.jvm.internal.p.e(executor, "executor");
        kotlin.jvm.internal.p.e(aVar, "reportFullyDrawn");
        this.f399a = executor;
        this.f400b = aVar;
        this.f401c = new Object();
        this.f405g = new ArrayList();
        this.f406h = new Runnable() { // from class: androidx.activity.i
            @Override // java.lang.Runnable
            public final void run() {
                j.d(j.this);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(j jVar) {
        kotlin.jvm.internal.p.e(jVar, "this$0");
        synchronized (jVar.f401c) {
            jVar.f403e = false;
            if (jVar.f402d == 0 && !jVar.f404f) {
                jVar.f400b.invoke();
                jVar.b();
            }
            a0 a0Var = a0.a;
        }
    }

    public final void b() {
        synchronized (this.f401c) {
            this.f404f = true;
            Iterator<T> it = this.f405g.iterator();
            while (it.hasNext()) {
                ((mj.a) it.next()).invoke();
            }
            this.f405g.clear();
            a0 a0Var = a0.a;
        }
    }

    public final boolean c() {
        boolean z4;
        synchronized (this.f401c) {
            z4 = this.f404f;
        }
        return z4;
    }
}
