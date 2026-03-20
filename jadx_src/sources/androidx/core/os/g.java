package androidx.core.os;

import android.os.OutcomeReceiver;
import java.lang.Throwable;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Result;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g<R, E extends Throwable> extends AtomicBoolean implements OutcomeReceiver<R, E> {

    /* renamed from: a  reason: collision with root package name */
    private final fj.c<R> f4772a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public g(fj.c<? super R> cVar) {
        super(false);
        kotlin.jvm.internal.p.e(cVar, "continuation");
        this.f4772a = cVar;
    }

    @Override // android.os.OutcomeReceiver
    public void onError(E e8) {
        kotlin.jvm.internal.p.e(e8, "error");
        if (compareAndSet(false, true)) {
            fj.c<R> cVar = this.f4772a;
            Result.a aVar = Result.b;
            cVar.resumeWith(Result.b(cj.n.a(e8)));
        }
    }

    @Override // android.os.OutcomeReceiver
    public void onResult(R r4) {
        if (compareAndSet(false, true)) {
            fj.c<R> cVar = this.f4772a;
            Result.a aVar = Result.b;
            cVar.resumeWith(Result.b(r4));
        }
    }

    @Override // java.util.concurrent.atomic.AtomicBoolean
    public String toString() {
        return "ContinuationOutcomeReceiver(outcomeReceived = " + get() + ')';
    }
}
