package androidx.lifecycle;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n<T> extends p<T> {

    /* renamed from: l  reason: collision with root package name */
    private m.b<LiveData<?>, a<?>> f5900l = new m.b<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a<V> implements q<V> {

        /* renamed from: a  reason: collision with root package name */
        final LiveData<V> f5901a;

        /* renamed from: b  reason: collision with root package name */
        final q<? super V> f5902b;

        /* renamed from: c  reason: collision with root package name */
        int f5903c = -1;

        a(LiveData<V> liveData, q<? super V> qVar) {
            this.f5901a = liveData;
            this.f5902b = qVar;
        }

        void a() {
            this.f5901a.i(this);
        }

        @Override // androidx.lifecycle.q
        public void b(V v8) {
            if (this.f5903c != this.f5901a.f()) {
                this.f5903c = this.f5901a.f();
                this.f5902b.b(v8);
            }
        }

        void c() {
            this.f5901a.m(this);
        }
    }

    @Override // androidx.lifecycle.LiveData
    protected void j() {
        Iterator<Map.Entry<LiveData<?>, a<?>>> it = this.f5900l.iterator();
        while (it.hasNext()) {
            it.next().getValue().a();
        }
    }

    @Override // androidx.lifecycle.LiveData
    protected void k() {
        Iterator<Map.Entry<LiveData<?>, a<?>>> it = this.f5900l.iterator();
        while (it.hasNext()) {
            it.next().getValue().c();
        }
    }

    public <S> void p(LiveData<S> liveData, q<? super S> qVar) {
        Objects.requireNonNull(liveData, "source cannot be null");
        a<?> aVar = new a<>(liveData, qVar);
        a<?> n8 = this.f5900l.n(liveData, aVar);
        if (n8 != null && n8.f5902b != qVar) {
            throw new IllegalArgumentException("This source was already added with the different observer");
        }
        if (n8 == null && g()) {
            aVar.a();
        }
    }

    public <S> void q(LiveData<S> liveData) {
        a<?> p8 = this.f5900l.p(liveData);
        if (p8 != null) {
            p8.c();
        }
    }
}
