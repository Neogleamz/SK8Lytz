package y3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a<T> implements bj.a<T> {

    /* renamed from: c  reason: collision with root package name */
    private static final Object f24373c = new Object();

    /* renamed from: a  reason: collision with root package name */
    private volatile bj.a<T> f24374a;

    /* renamed from: b  reason: collision with root package name */
    private volatile Object f24375b = f24373c;

    private a(bj.a<T> aVar) {
        this.f24374a = aVar;
    }

    public static <P extends bj.a<T>, T> bj.a<T> a(P p8) {
        d.b(p8);
        return p8 instanceof a ? p8 : new a(p8);
    }

    public static Object b(Object obj, Object obj2) {
        if (!(obj != f24373c) || obj == obj2) {
            return obj2;
        }
        throw new IllegalStateException("Scoped provider was invoked recursively returning different results: " + obj + " & " + obj2 + ". This is likely due to a circular dependency.");
    }

    public T get() {
        T t8 = (T) this.f24375b;
        Object obj = f24373c;
        if (t8 == obj) {
            synchronized (this) {
                t8 = this.f24375b;
                if (t8 == obj) {
                    t8 = (T) this.f24374a.get();
                    this.f24375b = b(this.f24375b, t8);
                    this.f24374a = null;
                }
            }
        }
        return t8;
    }
}
