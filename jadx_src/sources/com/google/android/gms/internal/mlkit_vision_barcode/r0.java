package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class r0 extends AbstractSet {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ x0 f13951a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r0(x0 x0Var) {
        this.f13951a = x0Var;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        this.f13951a.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        int s8;
        Map l8 = this.f13951a.l();
        if (l8 != null) {
            return l8.entrySet().contains(obj);
        }
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            s8 = this.f13951a.s(entry.getKey());
            if (s8 != -1) {
                Object[] objArr = this.f13951a.f14190d;
                objArr.getClass();
                if (p.a(objArr[s8], entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        x0 x0Var = this.f13951a;
        Map l8 = x0Var.l();
        return l8 != null ? l8.entrySet().iterator() : new p0(x0Var);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        int r4;
        int i8;
        Map l8 = this.f13951a.l();
        if (l8 != null) {
            return l8.entrySet().remove(obj);
        }
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            x0 x0Var = this.f13951a;
            if (x0Var.q()) {
                return false;
            }
            r4 = x0Var.r();
            Object key = entry.getKey();
            Object value = entry.getValue();
            Object j8 = x0.j(this.f13951a);
            x0 x0Var2 = this.f13951a;
            int[] iArr = x0Var2.f14188b;
            iArr.getClass();
            Object[] objArr = x0Var2.f14189c;
            objArr.getClass();
            Object[] objArr2 = x0Var2.f14190d;
            objArr2.getClass();
            int b9 = y0.b(key, value, r4, j8, iArr, objArr, objArr2);
            if (b9 == -1) {
                return false;
            }
            this.f13951a.p(b9, r4);
            x0 x0Var3 = this.f13951a;
            i8 = x0Var3.f14192f;
            x0Var3.f14192f = i8 - 1;
            this.f13951a.n();
            return true;
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f13951a.size();
    }
}
