package androidx.core.util;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g<T> extends f<T> {

    /* renamed from: c  reason: collision with root package name */
    private final Object f4893c;

    public g(int i8) {
        super(i8);
        this.f4893c = new Object();
    }

    @Override // androidx.core.util.f, androidx.core.util.e
    public boolean a(T t8) {
        boolean a9;
        synchronized (this.f4893c) {
            a9 = super.a(t8);
        }
        return a9;
    }

    @Override // androidx.core.util.f, androidx.core.util.e
    public T b() {
        T t8;
        synchronized (this.f4893c) {
            t8 = (T) super.b();
        }
        return t8;
    }
}
