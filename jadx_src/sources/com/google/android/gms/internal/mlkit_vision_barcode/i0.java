package com.google.android.gms.internal.mlkit_vision_barcode;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class i0 extends k0 implements Serializable {

    /* renamed from: c */
    private final transient Map f13545c;

    /* renamed from: d */
    private transient int f13546d;

    public i0(Map map) {
        u.c(map.isEmpty());
        this.f13545c = map;
    }

    public static /* bridge */ /* synthetic */ int g(i0 i0Var) {
        return i0Var.f13546d;
    }

    public static /* bridge */ /* synthetic */ Map j(i0 i0Var) {
        return i0Var.f13545c;
    }

    public static /* bridge */ /* synthetic */ void l(i0 i0Var, int i8) {
        i0Var.f13546d = i8;
    }

    public static /* bridge */ /* synthetic */ void m(i0 i0Var, Object obj) {
        Object obj2;
        try {
            obj2 = i0Var.f13545c.remove(obj);
        } catch (ClassCastException | NullPointerException unused) {
            obj2 = null;
        }
        Collection collection = (Collection) obj2;
        if (collection != null) {
            int size = collection.size();
            collection.clear();
            i0Var.f13546d -= size;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.p1
    public final boolean a(Object obj, Object obj2) {
        Collection collection = (Collection) this.f13545c.get(obj);
        if (collection != null) {
            if (collection.add(obj2)) {
                this.f13546d++;
                return true;
            }
            return false;
        }
        Collection e8 = e();
        if (e8.add(obj2)) {
            this.f13546d++;
            this.f13545c.put(obj, e8);
            return true;
        }
        throw new AssertionError("New Collection violated the Collection spec");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.k0
    final Map b() {
        return new a0(this, this.f13545c);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.k0
    final Set c() {
        return new c0(this, this.f13545c);
    }

    public abstract Collection e();

    public abstract Collection f(Object obj, Collection collection);

    public final Collection h(Object obj) {
        Collection collection = (Collection) this.f13545c.get(obj);
        if (collection == null) {
            collection = e();
        }
        return f(obj, collection);
    }

    public final List i(Object obj, List list, f0 f0Var) {
        return list instanceof RandomAccess ? new d0(this, obj, list, f0Var) : new h0(this, obj, list, f0Var);
    }

    public final void n() {
        for (Collection collection : this.f13545c.values()) {
            collection.clear();
        }
        this.f13545c.clear();
        this.f13546d = 0;
    }
}
