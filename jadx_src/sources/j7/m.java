package j7;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m {
    public static <TResult> TResult a(j<TResult> jVar) {
        n6.j.j();
        n6.j.h();
        n6.j.m(jVar, "Task must not be null");
        if (jVar.o()) {
            return (TResult) g(jVar);
        }
        p pVar = new p(null);
        h(jVar, pVar);
        pVar.d();
        return (TResult) g(jVar);
    }

    public static <TResult> TResult b(j<TResult> jVar, long j8, TimeUnit timeUnit) {
        n6.j.j();
        n6.j.h();
        n6.j.m(jVar, "Task must not be null");
        n6.j.m(timeUnit, "TimeUnit must not be null");
        if (jVar.o()) {
            return (TResult) g(jVar);
        }
        p pVar = new p(null);
        h(jVar, pVar);
        if (pVar.e(j8, timeUnit)) {
            return (TResult) g(jVar);
        }
        throw new TimeoutException("Timed out waiting for Task");
    }

    @Deprecated
    public static <TResult> j<TResult> c(Executor executor, Callable<TResult> callable) {
        n6.j.m(executor, "Executor must not be null");
        n6.j.m(callable, "Callback must not be null");
        l0 l0Var = new l0();
        executor.execute(new m0(l0Var, callable));
        return l0Var;
    }

    public static <TResult> j<TResult> d() {
        l0 l0Var = new l0();
        l0Var.u();
        return l0Var;
    }

    public static <TResult> j<TResult> e(Exception exc) {
        l0 l0Var = new l0();
        l0Var.s(exc);
        return l0Var;
    }

    public static <TResult> j<TResult> f(TResult tresult) {
        l0 l0Var = new l0();
        l0Var.t(tresult);
        return l0Var;
    }

    private static Object g(j jVar) {
        if (jVar.p()) {
            return jVar.l();
        }
        if (jVar.n()) {
            throw new CancellationException("Task is already canceled");
        }
        throw new ExecutionException(jVar.k());
    }

    private static void h(j jVar, q qVar) {
        Executor executor = l.f20821b;
        jVar.g(executor, qVar);
        jVar.e(executor, qVar);
        jVar.a(executor, qVar);
    }
}
