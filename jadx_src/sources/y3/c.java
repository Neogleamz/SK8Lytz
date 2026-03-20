package y3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c<T> implements b<T> {

    /* renamed from: b  reason: collision with root package name */
    private static final c<Object> f24376b = new c<>(null);

    /* renamed from: a  reason: collision with root package name */
    private final T f24377a;

    private c(T t8) {
        this.f24377a = t8;
    }

    public static <T> b<T> a(T t8) {
        return new c(d.c(t8, "instance cannot be null"));
    }

    public T get() {
        return this.f24377a;
    }
}
