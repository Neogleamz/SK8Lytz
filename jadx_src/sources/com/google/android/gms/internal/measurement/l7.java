package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class l7<E> extends AbstractList<E> implements g9<E> {

    /* renamed from: a  reason: collision with root package name */
    private boolean f12295a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l7() {
        this(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public l7(boolean z4) {
        this.f12295a = z4;
    }

    @Override // com.google.android.gms.internal.measurement.g9
    public boolean a() {
        return this.f12295a;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e8) {
        e();
        return super.add(e8);
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i8, Collection<? extends E> collection) {
        e();
        return super.addAll(i8, collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends E> collection) {
        e();
        return super.addAll(collection);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        e();
        super.clear();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void e() {
        if (!this.f12295a) {
            throw new UnsupportedOperationException();
        }
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof List) {
            if (obj instanceof RandomAccess) {
                List list = (List) obj;
                int size = size();
                if (size != list.size()) {
                    return false;
                }
                for (int i8 = 0; i8 < size; i8++) {
                    if (!get(i8).equals(list.get(i8))) {
                        return false;
                    }
                }
                return true;
            }
            return super.equals(obj);
        }
        return false;
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        int size = size();
        int i8 = 1;
        for (int i9 = 0; i9 < size; i9++) {
            i8 = (i8 * 31) + get(i9).hashCode();
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public abstract E remove(int i8);

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object obj) {
        e();
        int indexOf = indexOf(obj);
        if (indexOf == -1) {
            return false;
        }
        remove(indexOf);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(Collection<?> collection) {
        e();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean retainAll(Collection<?> collection) {
        e();
        return super.retainAll(collection);
    }

    @Override // com.google.android.gms.internal.measurement.g9
    public final void zzb() {
        if (this.f12295a) {
            this.f12295a = false;
        }
    }
}
