package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.List;
import java.util.ListIterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g0 extends e0 implements ListIterator {

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ h0 f13484d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g0(h0 h0Var) {
        super(h0Var);
        this.f13484d = h0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g0(h0 h0Var, int i8) {
        super(h0Var, ((List) h0Var.f13434b).listIterator(i8));
        this.f13484d = h0Var;
    }

    @Override // java.util.ListIterator
    public final void add(Object obj) {
        int i8;
        boolean isEmpty = this.f13484d.isEmpty();
        a();
        ((ListIterator) this.f13405a).add(obj);
        i0 i0Var = this.f13484d.f13514f;
        i8 = i0Var.f13546d;
        i0Var.f13546d = i8 + 1;
        if (isEmpty) {
            this.f13484d.e();
        }
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        a();
        return ((ListIterator) this.f13405a).hasPrevious();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        a();
        return ((ListIterator) this.f13405a).nextIndex();
    }

    @Override // java.util.ListIterator
    public final Object previous() {
        a();
        return ((ListIterator) this.f13405a).previous();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        a();
        return ((ListIterator) this.f13405a).previousIndex();
    }

    @Override // java.util.ListIterator
    public final void set(Object obj) {
        a();
        ((ListIterator) this.f13405a).set(obj);
    }
}
