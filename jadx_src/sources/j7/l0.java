package j7;

import com.google.android.gms.tasks.DuplicateTaskCompletionException;
import com.google.android.gms.tasks.RuntimeExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l0<TResult> extends j<TResult> {

    /* renamed from: a  reason: collision with root package name */
    private final Object f20822a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private final h0 f20823b = new h0();

    /* renamed from: c  reason: collision with root package name */
    private boolean f20824c;

    /* renamed from: d  reason: collision with root package name */
    private volatile boolean f20825d;

    /* renamed from: e  reason: collision with root package name */
    private Object f20826e;

    /* renamed from: f  reason: collision with root package name */
    private Exception f20827f;

    private final void A() {
        synchronized (this.f20822a) {
            if (this.f20824c) {
                this.f20823b.b(this);
            }
        }
    }

    private final void x() {
        n6.j.q(this.f20824c, "Task is not yet complete");
    }

    private final void y() {
        if (this.f20825d) {
            throw new CancellationException("Task is already canceled.");
        }
    }

    private final void z() {
        if (this.f20824c) {
            throw DuplicateTaskCompletionException.a(this);
        }
    }

    @Override // j7.j
    public final j<TResult> a(Executor executor, d dVar) {
        this.f20823b.a(new x(executor, dVar));
        A();
        return this;
    }

    @Override // j7.j
    public final j<TResult> b(e<TResult> eVar) {
        this.f20823b.a(new z(l.f20820a, eVar));
        A();
        return this;
    }

    @Override // j7.j
    public final j<TResult> c(Executor executor, e<TResult> eVar) {
        this.f20823b.a(new z(executor, eVar));
        A();
        return this;
    }

    @Override // j7.j
    public final j<TResult> d(f fVar) {
        e(l.f20820a, fVar);
        return this;
    }

    @Override // j7.j
    public final j<TResult> e(Executor executor, f fVar) {
        this.f20823b.a(new b0(executor, fVar));
        A();
        return this;
    }

    @Override // j7.j
    public final j<TResult> f(g<? super TResult> gVar) {
        g(l.f20820a, gVar);
        return this;
    }

    @Override // j7.j
    public final j<TResult> g(Executor executor, g<? super TResult> gVar) {
        this.f20823b.a(new d0(executor, gVar));
        A();
        return this;
    }

    @Override // j7.j
    public final <TContinuationResult> j<TContinuationResult> h(c<TResult, TContinuationResult> cVar) {
        return i(l.f20820a, cVar);
    }

    @Override // j7.j
    public final <TContinuationResult> j<TContinuationResult> i(Executor executor, c<TResult, TContinuationResult> cVar) {
        l0 l0Var = new l0();
        this.f20823b.a(new t(executor, cVar, l0Var));
        A();
        return l0Var;
    }

    @Override // j7.j
    public final <TContinuationResult> j<TContinuationResult> j(Executor executor, c<TResult, j<TContinuationResult>> cVar) {
        l0 l0Var = new l0();
        this.f20823b.a(new v(executor, cVar, l0Var));
        A();
        return l0Var;
    }

    @Override // j7.j
    public final Exception k() {
        Exception exc;
        synchronized (this.f20822a) {
            exc = this.f20827f;
        }
        return exc;
    }

    @Override // j7.j
    public final TResult l() {
        TResult tresult;
        synchronized (this.f20822a) {
            x();
            y();
            Exception exc = this.f20827f;
            if (exc != null) {
                throw new RuntimeExecutionException(exc);
            }
            tresult = (TResult) this.f20826e;
        }
        return tresult;
    }

    @Override // j7.j
    public final <X extends Throwable> TResult m(Class<X> cls) {
        TResult tresult;
        synchronized (this.f20822a) {
            x();
            y();
            if (cls.isInstance(this.f20827f)) {
                throw cls.cast(this.f20827f);
            }
            Exception exc = this.f20827f;
            if (exc != null) {
                throw new RuntimeExecutionException(exc);
            }
            tresult = (TResult) this.f20826e;
        }
        return tresult;
    }

    @Override // j7.j
    public final boolean n() {
        return this.f20825d;
    }

    @Override // j7.j
    public final boolean o() {
        boolean z4;
        synchronized (this.f20822a) {
            z4 = this.f20824c;
        }
        return z4;
    }

    @Override // j7.j
    public final boolean p() {
        boolean z4;
        synchronized (this.f20822a) {
            z4 = false;
            if (this.f20824c && !this.f20825d && this.f20827f == null) {
                z4 = true;
            }
        }
        return z4;
    }

    @Override // j7.j
    public final <TContinuationResult> j<TContinuationResult> q(i<TResult, TContinuationResult> iVar) {
        Executor executor = l.f20820a;
        l0 l0Var = new l0();
        this.f20823b.a(new f0(executor, iVar, l0Var));
        A();
        return l0Var;
    }

    @Override // j7.j
    public final <TContinuationResult> j<TContinuationResult> r(Executor executor, i<TResult, TContinuationResult> iVar) {
        l0 l0Var = new l0();
        this.f20823b.a(new f0(executor, iVar, l0Var));
        A();
        return l0Var;
    }

    public final void s(Exception exc) {
        n6.j.m(exc, "Exception must not be null");
        synchronized (this.f20822a) {
            z();
            this.f20824c = true;
            this.f20827f = exc;
        }
        this.f20823b.b(this);
    }

    public final void t(Object obj) {
        synchronized (this.f20822a) {
            z();
            this.f20824c = true;
            this.f20826e = obj;
        }
        this.f20823b.b(this);
    }

    public final boolean u() {
        synchronized (this.f20822a) {
            if (this.f20824c) {
                return false;
            }
            this.f20824c = true;
            this.f20825d = true;
            this.f20823b.b(this);
            return true;
        }
    }

    public final boolean v(Exception exc) {
        n6.j.m(exc, "Exception must not be null");
        synchronized (this.f20822a) {
            if (this.f20824c) {
                return false;
            }
            this.f20824c = true;
            this.f20827f = exc;
            this.f20823b.b(this);
            return true;
        }
    }

    public final boolean w(Object obj) {
        synchronized (this.f20822a) {
            if (this.f20824c) {
                return false;
            }
            this.f20824c = true;
            this.f20826e = obj;
            this.f20823b.b(this);
            return true;
        }
    }
}
