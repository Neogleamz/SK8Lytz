package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class f0 extends AbstractCollection {

    /* renamed from: a  reason: collision with root package name */
    final Object f13433a;

    /* renamed from: b  reason: collision with root package name */
    Collection f13434b;

    /* renamed from: c  reason: collision with root package name */
    final f0 f13435c;

    /* renamed from: d  reason: collision with root package name */
    final Collection f13436d;

    /* renamed from: e  reason: collision with root package name */
    final /* synthetic */ i0 f13437e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f0(i0 i0Var, Object obj, Collection collection, f0 f0Var) {
        this.f13437e = i0Var;
        this.f13433a = obj;
        this.f13434b = collection;
        this.f13435c = f0Var;
        this.f13436d = f0Var == null ? null : f0Var.f13434b;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean add(Object obj) {
        zzb();
        boolean isEmpty = this.f13434b.isEmpty();
        boolean add = this.f13434b.add(obj);
        if (add) {
            i0 i0Var = this.f13437e;
            i0.l(i0Var, i0.g(i0Var) + 1);
            if (isEmpty) {
                e();
                return true;
            }
        }
        return add;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean addAll(Collection collection) {
        if (collection.isEmpty()) {
            return false;
        }
        int size = size();
        boolean addAll = this.f13434b.addAll(collection);
        if (addAll) {
            int size2 = this.f13434b.size();
            i0 i0Var = this.f13437e;
            i0.l(i0Var, i0.g(i0Var) + (size2 - size));
            if (size == 0) {
                e();
                return true;
            }
            return addAll;
        }
        return addAll;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final void clear() {
        int size = size();
        if (size == 0) {
            return;
        }
        this.f13434b.clear();
        i0 i0Var = this.f13437e;
        i0.l(i0Var, i0.g(i0Var) - size);
        g();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean contains(Object obj) {
        zzb();
        return this.f13434b.contains(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean containsAll(Collection collection) {
        zzb();
        return this.f13434b.containsAll(collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e() {
        f0 f0Var = this.f13435c;
        if (f0Var != null) {
            f0Var.e();
        } else {
            i0.j(this.f13437e).put(this.f13433a, this.f13434b);
        }
    }

    @Override // java.util.Collection
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        zzb();
        return this.f13434b.equals(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void g() {
        f0 f0Var = this.f13435c;
        if (f0Var != null) {
            f0Var.g();
        } else if (this.f13434b.isEmpty()) {
            i0.j(this.f13437e).remove(this.f13433a);
        }
    }

    @Override // java.util.Collection
    public final int hashCode() {
        zzb();
        return this.f13434b.hashCode();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public final Iterator iterator() {
        zzb();
        return new e0(this);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean remove(Object obj) {
        zzb();
        boolean remove = this.f13434b.remove(obj);
        if (remove) {
            i0 i0Var = this.f13437e;
            i0.l(i0Var, i0.g(i0Var) - 1);
            g();
        }
        return remove;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean removeAll(Collection collection) {
        if (collection.isEmpty()) {
            return false;
        }
        int size = size();
        boolean removeAll = this.f13434b.removeAll(collection);
        if (removeAll) {
            int size2 = this.f13434b.size();
            i0 i0Var = this.f13437e;
            i0.l(i0Var, i0.g(i0Var) + (size2 - size));
            g();
        }
        return removeAll;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean retainAll(Collection collection) {
        Objects.requireNonNull(collection);
        int size = size();
        boolean retainAll = this.f13434b.retainAll(collection);
        if (retainAll) {
            int size2 = this.f13434b.size();
            i0 i0Var = this.f13437e;
            i0.l(i0Var, i0.g(i0Var) + (size2 - size));
            g();
        }
        return retainAll;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final int size() {
        zzb();
        return this.f13434b.size();
    }

    @Override // java.util.AbstractCollection
    public final String toString() {
        zzb();
        return this.f13434b.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzb() {
        Collection collection;
        f0 f0Var = this.f13435c;
        if (f0Var != null) {
            f0Var.zzb();
            if (this.f13435c.f13434b != this.f13436d) {
                throw new ConcurrentModificationException();
            }
        } else if (!this.f13434b.isEmpty() || (collection = (Collection) i0.j(this.f13437e).get(this.f13433a)) == null) {
        } else {
            this.f13434b = collection;
        }
    }
}
