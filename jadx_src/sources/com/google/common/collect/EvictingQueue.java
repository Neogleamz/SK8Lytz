package com.google.common.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.Queue;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class EvictingQueue<E> extends q0<E> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private final Queue<E> f18913a;

    /* renamed from: b  reason: collision with root package name */
    final int f18914b;

    @Override // com.google.common.collect.j0, java.util.Collection, java.util.Queue
    public boolean add(E e8) {
        com.google.common.base.l.n(e8);
        if (this.f18914b == 0) {
            return true;
        }
        if (size() == this.f18914b) {
            this.f18913a.remove();
        }
        this.f18913a.add(e8);
        return true;
    }

    @Override // com.google.common.collect.j0, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        int size = collection.size();
        if (size >= this.f18914b) {
            clear();
            return f1.a(this, f1.k(collection, size - this.f18914b));
        }
        return k(collection);
    }

    @Override // com.google.common.collect.q0, java.util.Queue
    public boolean offer(E e8) {
        return add(e8);
    }

    @Override // com.google.common.collect.j0, java.util.Collection
    public Object[] toArray() {
        return super.toArray();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.q0, com.google.common.collect.j0
    /* renamed from: v */
    public Queue<E> i() {
        return this.f18913a;
    }
}
