package m1;

import androidx.concurrent.futures.c;
import cj.a0;
import com.google.common.util.concurrent.d;
import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.p;
import mj.l;
import xj.m0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends Lambda implements l<Throwable, a0> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c.a<T> f21823a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ m0<T> f21824b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        a(c.a<T> aVar, m0<? extends T> m0Var) {
            super(1);
            this.f21823a = aVar;
            this.f21824b = m0Var;
        }

        public final void c(Throwable th) {
            if (th == null) {
                this.f21823a.c(this.f21824b.i());
            } else if (th instanceof CancellationException) {
                this.f21823a.d();
            } else {
                this.f21823a.f(th);
            }
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            c((Throwable) obj);
            return a0.a;
        }
    }

    public static final <T> d<T> b(m0<? extends T> m0Var, Object obj) {
        p.e(m0Var, "<this>");
        d<T> a9 = c.a(new m1.a(m0Var, obj));
        p.d(a9, "getFuture { completer ->…        }\n    }\n    tag\n}");
        return a9;
    }

    public static /* synthetic */ d c(m0 m0Var, Object obj, int i8, Object obj2) {
        if ((i8 & 1) != 0) {
            obj = "Deferred.asListenableFuture";
        }
        return b(m0Var, obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object d(m0 m0Var, Object obj, c.a aVar) {
        p.e(m0Var, "$this_asListenableFuture");
        p.e(aVar, "completer");
        m0Var.z(new a(aVar, m0Var));
        return obj;
    }
}
