package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class jb implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    private int f12263a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f12264b;

    /* renamed from: c  reason: collision with root package name */
    private Iterator f12265c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ ya f12266d;

    private jb(ya yaVar) {
        this.f12266d = yaVar;
        this.f12263a = -1;
    }

    private final Iterator a() {
        Map map;
        if (this.f12265c == null) {
            map = this.f12266d.f12689c;
            this.f12265c = map.entrySet().iterator();
        }
        return this.f12265c;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        List list;
        Map map;
        int i8 = this.f12263a + 1;
        list = this.f12266d.f12688b;
        if (i8 >= list.size()) {
            map = this.f12266d.f12689c;
            if (map.isEmpty() || !a().hasNext()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        List list;
        Object next;
        List list2;
        this.f12264b = true;
        int i8 = this.f12263a + 1;
        this.f12263a = i8;
        list = this.f12266d.f12688b;
        if (i8 < list.size()) {
            list2 = this.f12266d.f12688b;
            next = list2.get(this.f12263a);
        } else {
            next = a().next();
        }
        return (Map.Entry) next;
    }

    @Override // java.util.Iterator
    public final void remove() {
        List list;
        if (!this.f12264b) {
            throw new IllegalStateException("remove() was called before next()");
        }
        this.f12264b = false;
        this.f12266d.s();
        int i8 = this.f12263a;
        list = this.f12266d.f12688b;
        if (i8 >= list.size()) {
            a().remove();
            return;
        }
        ya yaVar = this.f12266d;
        int i9 = this.f12263a;
        this.f12263a = i9 - 1;
        yaVar.l(i9);
    }
}
