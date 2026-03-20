package com.google.common.util.concurrent;

import com.google.common.base.i;
import com.google.common.base.l;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends c {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a<V> implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final Future<V> f19675a;

        /* renamed from: b  reason: collision with root package name */
        final com.google.common.util.concurrent.a<? super V> f19676b;

        a(Future<V> future, com.google.common.util.concurrent.a<? super V> aVar) {
            this.f19675a = future;
            this.f19676b = aVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            Throwable a9;
            Future<V> future = this.f19675a;
            if ((future instanceof c8.a) && (a9 = c8.b.a((c8.a) future)) != null) {
                this.f19676b.onFailure(a9);
                return;
            }
            try {
                this.f19676b.c(b.b(this.f19675a));
            } catch (Error e8) {
                e = e8;
                this.f19676b.onFailure(e);
            } catch (RuntimeException e9) {
                e = e9;
                this.f19676b.onFailure(e);
            } catch (ExecutionException e10) {
                this.f19676b.onFailure(e10.getCause());
            }
        }

        public String toString() {
            return i.b(this).j(this.f19676b).toString();
        }
    }

    public static <V> void a(d<V> dVar, com.google.common.util.concurrent.a<? super V> aVar, Executor executor) {
        l.n(aVar);
        dVar.c(new a(dVar, aVar), executor);
    }

    public static <V> V b(Future<V> future) {
        l.v(future.isDone(), "Future was expected to be done: %s", future);
        return (V) e.a(future);
    }
}
