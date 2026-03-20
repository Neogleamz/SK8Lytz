package androidx.window.java.layout;

import androidx.core.util.a;
import bk.b;
import cj.a0;
import cj.n;
import fj.c;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.coroutines.jvm.internal.d;
import mj.p;
import xj.g0;
@d(c = "androidx.window.java.layout.WindowInfoTrackerCallbackAdapter$addListener$1$1", f = "WindowInfoTrackerCallbackAdapter.kt", l = {96}, m = "invokeSuspend")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class WindowInfoTrackerCallbackAdapter$addListener$1$1 extends SuspendLambda implements p<g0, c<? super a0>, Object> {
    final /* synthetic */ a<T> $consumer;
    final /* synthetic */ b<T> $flow;
    int label;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public WindowInfoTrackerCallbackAdapter$addListener$1$1(b<? extends T> bVar, a<T> aVar, c<? super WindowInfoTrackerCallbackAdapter$addListener$1$1> cVar) {
        super(2, cVar);
        this.$flow = bVar;
        this.$consumer = aVar;
    }

    public final c<a0> create(Object obj, c<?> cVar) {
        return new WindowInfoTrackerCallbackAdapter$addListener$1$1(this.$flow, this.$consumer, cVar);
    }

    public final Object invoke(g0 g0Var, c<? super a0> cVar) {
        return create(g0Var, cVar).invokeSuspend(a0.a);
    }

    public final Object invokeSuspend(Object obj) {
        Object e8 = kotlin.coroutines.intrinsics.a.e();
        int i8 = this.label;
        if (i8 == 0) {
            n.b(obj);
            b<T> bVar = this.$flow;
            final a<T> aVar = this.$consumer;
            bk.c<T> cVar = new bk.c<T>() { // from class: androidx.window.java.layout.WindowInfoTrackerCallbackAdapter$addListener$1$1$invokeSuspend$$inlined$collect$1
                public Object emit(T t8, c<? super a0> cVar2) {
                    a.this.accept(t8);
                    return a0.a;
                }
            };
            this.label = 1;
            if (bVar.a(cVar, this) == e8) {
                return e8;
            }
        } else if (i8 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            n.b(obj);
        }
        return a0.a;
    }
}
