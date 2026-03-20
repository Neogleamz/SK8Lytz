package com.google.common.collect;

import com.google.common.base.Optional;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class i0<E> implements Iterable<E> {

    /* renamed from: a  reason: collision with root package name */
    private final Optional<Iterable<E>> f19325a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends i0<E> {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Iterable f19326b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(Iterable iterable, Iterable iterable2) {
            super(iterable);
            this.f19326b = iterable2;
        }

        @Override // java.lang.Iterable
        public Iterator<E> iterator() {
            return this.f19326b.iterator();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public i0() {
        this.f19325a = Optional.a();
    }

    i0(Iterable<E> iterable) {
        this.f19325a = Optional.d(iterable);
    }

    public static <E> i0<E> g(Iterable<E> iterable) {
        return iterable instanceof i0 ? (i0) iterable : new a(iterable, iterable);
    }

    private Iterable<E> h() {
        return this.f19325a.e(this);
    }

    public final i0<E> e(com.google.common.base.m<? super E> mVar) {
        return g(f1.d(h(), mVar));
    }

    public final ImmutableSet<E> i() {
        return ImmutableSet.y(h());
    }

    public String toString() {
        return f1.n(h());
    }
}
