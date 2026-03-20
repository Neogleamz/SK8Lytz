package j7;

import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class j<TResult> {
    public j<TResult> a(Executor executor, d dVar) {
        throw new UnsupportedOperationException("addOnCanceledListener is not implemented");
    }

    public j<TResult> b(e<TResult> eVar) {
        throw new UnsupportedOperationException("addOnCompleteListener is not implemented");
    }

    public j<TResult> c(Executor executor, e<TResult> eVar) {
        throw new UnsupportedOperationException("addOnCompleteListener is not implemented");
    }

    public abstract j<TResult> d(f fVar);

    public abstract j<TResult> e(Executor executor, f fVar);

    public abstract j<TResult> f(g<? super TResult> gVar);

    public abstract j<TResult> g(Executor executor, g<? super TResult> gVar);

    public <TContinuationResult> j<TContinuationResult> h(c<TResult, TContinuationResult> cVar) {
        throw new UnsupportedOperationException("continueWith is not implemented");
    }

    public <TContinuationResult> j<TContinuationResult> i(Executor executor, c<TResult, TContinuationResult> cVar) {
        throw new UnsupportedOperationException("continueWith is not implemented");
    }

    public <TContinuationResult> j<TContinuationResult> j(Executor executor, c<TResult, j<TContinuationResult>> cVar) {
        throw new UnsupportedOperationException("continueWithTask is not implemented");
    }

    public abstract Exception k();

    public abstract TResult l();

    public abstract <X extends Throwable> TResult m(Class<X> cls);

    public abstract boolean n();

    public abstract boolean o();

    public abstract boolean p();

    public <TContinuationResult> j<TContinuationResult> q(i<TResult, TContinuationResult> iVar) {
        throw new UnsupportedOperationException("onSuccessTask is not implemented");
    }

    public <TContinuationResult> j<TContinuationResult> r(Executor executor, i<TResult, TContinuationResult> iVar) {
        throw new UnsupportedOperationException("onSuccessTask is not implemented");
    }
}
