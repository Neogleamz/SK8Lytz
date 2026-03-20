package com.google.common.collect;

import java.util.Queue;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class q0<E> extends j0<E> implements Queue<E> {
    protected q0() {
    }

    @Override // java.util.Queue
    public E element() {
        return i().element();
    }

    public boolean offer(E e8) {
        return i().offer(e8);
    }

    @Override // java.util.Queue
    public E peek() {
        return i().peek();
    }

    @Override // java.util.Queue
    public E poll() {
        return i().poll();
    }

    @Override // java.util.Queue
    public E remove() {
        return i().remove();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.j0
    /* renamed from: v */
    public abstract Queue<E> i();
}
