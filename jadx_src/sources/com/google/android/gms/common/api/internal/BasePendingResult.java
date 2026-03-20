package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import k6.c;
import k6.e;
@KeepName
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class BasePendingResult<R extends k6.e> extends k6.c<R> {

    /* renamed from: o */
    static final ThreadLocal f11579o = new l0();

    /* renamed from: p */
    public static final /* synthetic */ int f11580p = 0;

    /* renamed from: f */
    private k6.f f11586f;

    /* renamed from: h */
    private k6.e f11588h;

    /* renamed from: i */
    private Status f11589i;

    /* renamed from: j */
    private volatile boolean f11590j;

    /* renamed from: k */
    private boolean f11591k;

    /* renamed from: l */
    private boolean f11592l;

    /* renamed from: m */
    private n6.e f11593m;
    @KeepName
    private m0 mResultGuardian;

    /* renamed from: a */
    private final Object f11581a = new Object();

    /* renamed from: d */
    private final CountDownLatch f11584d = new CountDownLatch(1);

    /* renamed from: e */
    private final ArrayList f11585e = new ArrayList();

    /* renamed from: g */
    private final AtomicReference f11587g = new AtomicReference();

    /* renamed from: n */
    private boolean f11594n = false;

    /* renamed from: b */
    protected final a f11582b = new a(Looper.getMainLooper());

    /* renamed from: c */
    protected final WeakReference f11583c = new WeakReference(null);

    @VisibleForTesting
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<R extends k6.e> extends a7.k {
        public a(Looper looper) {
            super(looper);
        }

        public final void a(k6.f fVar, k6.e eVar) {
            int i8 = BasePendingResult.f11580p;
            sendMessage(obtainMessage(1, new Pair((k6.f) n6.j.l(fVar), eVar)));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i8 = message.what;
            if (i8 == 1) {
                Pair pair = (Pair) message.obj;
                k6.f fVar = (k6.f) pair.first;
                k6.e eVar = (k6.e) pair.second;
                try {
                    fVar.a(eVar);
                } catch (RuntimeException e8) {
                    BasePendingResult.h(eVar);
                    throw e8;
                }
            } else if (i8 == 2) {
                ((BasePendingResult) message.obj).b(Status.f11550j);
            } else {
                Log.wtf("BasePendingResult", "Don't know how to handle message: " + i8, new Exception());
            }
        }
    }

    @Deprecated
    BasePendingResult() {
    }

    private final k6.e e() {
        k6.e eVar;
        synchronized (this.f11581a) {
            n6.j.q(!this.f11590j, "Result has already been consumed.");
            n6.j.q(c(), "Result is not ready.");
            eVar = this.f11588h;
            this.f11588h = null;
            this.f11586f = null;
            this.f11590j = true;
        }
        if (((c0) this.f11587g.getAndSet(null)) == null) {
            return (k6.e) n6.j.l(eVar);
        }
        throw null;
    }

    private final void f(k6.e eVar) {
        this.f11588h = eVar;
        this.f11589i = eVar.p();
        this.f11593m = null;
        this.f11584d.countDown();
        if (this.f11591k) {
            this.f11586f = null;
        } else {
            k6.f fVar = this.f11586f;
            if (fVar != null) {
                this.f11582b.removeMessages(2);
                this.f11582b.a(fVar, e());
            } else if (this.f11588h instanceof k6.d) {
                this.mResultGuardian = new m0(this, null);
            }
        }
        ArrayList arrayList = this.f11585e;
        int size = arrayList.size();
        for (int i8 = 0; i8 < size; i8++) {
            ((c.a) arrayList.get(i8)).a(this.f11589i);
        }
        this.f11585e.clear();
    }

    public static void h(k6.e eVar) {
        if (eVar instanceof k6.d) {
            try {
                ((k6.d) eVar).release();
            } catch (RuntimeException e8) {
                Log.w("BasePendingResult", "Unable to release ".concat(String.valueOf(eVar)), e8);
            }
        }
    }

    protected abstract R a(Status status);

    @Deprecated
    public final void b(Status status) {
        synchronized (this.f11581a) {
            if (!c()) {
                d(a(status));
                this.f11592l = true;
            }
        }
    }

    public final boolean c() {
        return this.f11584d.getCount() == 0;
    }

    public final void d(R r4) {
        synchronized (this.f11581a) {
            if (this.f11592l || this.f11591k) {
                h(r4);
                return;
            }
            c();
            n6.j.q(!c(), "Results have already been set");
            n6.j.q(!this.f11590j, "Result has already been consumed");
            f(r4);
        }
    }
}
