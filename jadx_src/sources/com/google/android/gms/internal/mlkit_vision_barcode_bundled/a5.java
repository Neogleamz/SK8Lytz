package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a5 implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    private int f14722a = -1;

    /* renamed from: b  reason: collision with root package name */
    private boolean f14723b;

    /* renamed from: c  reason: collision with root package name */
    private Iterator f14724c;

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ e5 f14725d;

    private final Iterator a() {
        Map map;
        if (this.f14724c == null) {
            map = this.f14725d.f14758c;
            this.f14724c = map.entrySet().iterator();
        }
        return this.f14724c;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        List list;
        Map map;
        int i8 = this.f14722a + 1;
        list = this.f14725d.f14757b;
        if (i8 >= list.size()) {
            map = this.f14725d.f14758c;
            return !map.isEmpty() && a().hasNext();
        }
        return true;
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ Object next() {
        List list;
        Object next;
        List list2;
        this.f14723b = true;
        int i8 = this.f14722a + 1;
        this.f14722a = i8;
        list = this.f14725d.f14757b;
        if (i8 < list.size()) {
            list2 = this.f14725d.f14757b;
            next = list2.get(this.f14722a);
        } else {
            next = a().next();
        }
        return (Map.Entry) next;
    }

    @Override // java.util.Iterator
    public final void remove() {
        List list;
        if (!this.f14723b) {
            throw new IllegalStateException("remove() was called before next()");
        }
        this.f14723b = false;
        this.f14725d.p();
        int i8 = this.f14722a;
        list = this.f14725d.f14757b;
        if (i8 >= list.size()) {
            a().remove();
            return;
        }
        e5 e5Var = this.f14725d;
        int i9 = this.f14722a;
        this.f14722a = i9 - 1;
        e5Var.n(i9);
    }
}
