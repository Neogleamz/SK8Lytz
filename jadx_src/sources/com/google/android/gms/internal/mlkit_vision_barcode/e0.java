package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class e0 implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    final Iterator f13405a;

    /* renamed from: b  reason: collision with root package name */
    final Collection f13406b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ f0 f13407c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e0(f0 f0Var) {
        this.f13407c = f0Var;
        Collection collection = f0Var.f13434b;
        this.f13406b = collection;
        this.f13405a = collection instanceof List ? ((List) collection).listIterator() : collection.iterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e0(f0 f0Var, Iterator it) {
        this.f13407c = f0Var;
        this.f13406b = f0Var.f13434b;
        this.f13405a = it;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        this.f13407c.zzb();
        if (this.f13407c.f13434b != this.f13406b) {
            throw new ConcurrentModificationException();
        }
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        a();
        return this.f13405a.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        a();
        return this.f13405a.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        int i8;
        this.f13405a.remove();
        i0 i0Var = this.f13407c.f13437e;
        i8 = i0Var.f13546d;
        i0Var.f13546d = i8 - 1;
        this.f13407c.g();
    }
}
