package f0;

import java.util.ArrayDeque;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a<T> {

    /* renamed from: a  reason: collision with root package name */
    private final int f19827a;

    /* renamed from: b  reason: collision with root package name */
    private final ArrayDeque<T> f19828b;

    /* renamed from: c  reason: collision with root package name */
    private final Object f19829c = new Object();

    /* renamed from: d  reason: collision with root package name */
    final b<T> f19830d;

    public a(int i8, b<T> bVar) {
        this.f19827a = i8;
        this.f19828b = new ArrayDeque<>(i8);
        this.f19830d = bVar;
    }

    public T a() {
        T removeLast;
        synchronized (this.f19829c) {
            removeLast = this.f19828b.removeLast();
        }
        return removeLast;
    }

    public void b(T t8) {
        T a9;
        synchronized (this.f19829c) {
            a9 = this.f19828b.size() >= this.f19827a ? a() : null;
            this.f19828b.addFirst(t8);
        }
        b<T> bVar = this.f19830d;
        if (bVar == null || a9 == null) {
            return;
        }
        bVar.a(a9);
    }

    public boolean c() {
        boolean isEmpty;
        synchronized (this.f19829c) {
            isEmpty = this.f19828b.isEmpty();
        }
        return isEmpty;
    }
}
