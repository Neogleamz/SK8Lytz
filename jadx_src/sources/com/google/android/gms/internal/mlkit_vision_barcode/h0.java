package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h0 extends f0 implements List {

    /* renamed from: f  reason: collision with root package name */
    final /* synthetic */ i0 f13514f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h0(i0 i0Var, Object obj, List list, f0 f0Var) {
        super(i0Var, obj, list, f0Var);
        this.f13514f = i0Var;
    }

    @Override // java.util.List
    public final void add(int i8, Object obj) {
        int i9;
        zzb();
        boolean isEmpty = this.f13434b.isEmpty();
        ((List) this.f13434b).add(i8, obj);
        i0 i0Var = this.f13514f;
        i9 = i0Var.f13546d;
        i0Var.f13546d = i9 + 1;
        if (isEmpty) {
            e();
        }
    }

    @Override // java.util.List
    public final boolean addAll(int i8, Collection collection) {
        int i9;
        if (collection.isEmpty()) {
            return false;
        }
        int size = size();
        boolean addAll = ((List) this.f13434b).addAll(i8, collection);
        if (addAll) {
            int size2 = this.f13434b.size();
            i0 i0Var = this.f13514f;
            i9 = i0Var.f13546d;
            i0Var.f13546d = i9 + (size2 - size);
            if (size == 0) {
                e();
                return true;
            }
            return addAll;
        }
        return addAll;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        zzb();
        return ((List) this.f13434b).get(i8);
    }

    @Override // java.util.List
    public final int indexOf(Object obj) {
        zzb();
        return ((List) this.f13434b).indexOf(obj);
    }

    @Override // java.util.List
    public final int lastIndexOf(Object obj) {
        zzb();
        return ((List) this.f13434b).lastIndexOf(obj);
    }

    @Override // java.util.List
    public final ListIterator listIterator() {
        zzb();
        return new g0(this);
    }

    @Override // java.util.List
    public final ListIterator listIterator(int i8) {
        zzb();
        return new g0(this, i8);
    }

    @Override // java.util.List
    public final Object remove(int i8) {
        int i9;
        zzb();
        Object remove = ((List) this.f13434b).remove(i8);
        i0 i0Var = this.f13514f;
        i9 = i0Var.f13546d;
        i0Var.f13546d = i9 - 1;
        g();
        return remove;
    }

    @Override // java.util.List
    public final Object set(int i8, Object obj) {
        zzb();
        return ((List) this.f13434b).set(i8, obj);
    }

    @Override // java.util.List
    public final List subList(int i8, int i9) {
        zzb();
        i0 i0Var = this.f13514f;
        Object obj = this.f13433a;
        List subList = ((List) this.f13434b).subList(i8, i9);
        f0 f0Var = this.f13435c;
        if (f0Var == null) {
            f0Var = this;
        }
        return i0Var.i(obj, subList, f0Var);
    }
}
