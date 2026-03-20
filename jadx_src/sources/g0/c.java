package g0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c<T> implements androidx.core.util.a<T> {

    /* renamed from: a  reason: collision with root package name */
    private androidx.core.util.a<T> f20145a;

    public void a(androidx.core.util.a<T> aVar) {
        this.f20145a = aVar;
    }

    @Override // androidx.core.util.a
    public void accept(T t8) {
        kotlin.jvm.internal.p.c(this.f20145a, "Listener is not set.");
        this.f20145a.accept(t8);
    }
}
