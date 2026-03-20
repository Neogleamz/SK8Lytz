package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class bb implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    private int f12098a;

    /* renamed from: b  reason: collision with root package name */
    private Iterator f12099b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ ya f12100c;

    private bb(ya yaVar) {
        List list;
        this.f12100c = yaVar;
        list = yaVar.f12688b;
        this.f12098a = list.size();
    }

    private final Iterator a() {
        Map map;
        if (this.f12099b == null) {
            map = this.f12100c.f12692f;
            this.f12099b = map.entrySet().iterator();
        }
        return this.f12099b;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        List list;
        int i8 = this.f12098a;
        if (i8 > 0) {
            list = this.f12100c.f12688b;
            if (i8 <= list.size()) {
                return true;
            }
        }
        return a().hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        List list;
        Object obj;
        if (a().hasNext()) {
            obj = a().next();
        } else {
            list = this.f12100c.f12688b;
            int i8 = this.f12098a - 1;
            this.f12098a = i8;
            obj = list.get(i8);
        }
        return (Map.Entry) obj;
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
