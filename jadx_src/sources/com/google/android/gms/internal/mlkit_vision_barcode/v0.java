package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v0 extends j0 {

    /* renamed from: a  reason: collision with root package name */
    private final Object f14091a;

    /* renamed from: b  reason: collision with root package name */
    private int f14092b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ x0 f14093c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v0(x0 x0Var, int i8) {
        this.f14093c = x0Var;
        Object[] objArr = x0Var.f14189c;
        objArr.getClass();
        this.f14091a = objArr[i8];
        this.f14092b = i8;
    }

    private final void a() {
        int s8;
        int i8 = this.f14092b;
        if (i8 != -1 && i8 < this.f14093c.size()) {
            Object obj = this.f14091a;
            x0 x0Var = this.f14093c;
            int i9 = this.f14092b;
            Object[] objArr = x0Var.f14189c;
            objArr.getClass();
            if (p.a(obj, objArr[i9])) {
                return;
            }
        }
        s8 = this.f14093c.s(this.f14091a);
        this.f14092b = s8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.j0, java.util.Map.Entry
    public final Object getKey() {
        return this.f14091a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.j0, java.util.Map.Entry
    public final Object getValue() {
        Map l8 = this.f14093c.l();
        if (l8 != null) {
            return l8.get(this.f14091a);
        }
        a();
        int i8 = this.f14092b;
        if (i8 == -1) {
            return null;
        }
        Object[] objArr = this.f14093c.f14190d;
        objArr.getClass();
        return objArr[i8];
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        Map l8 = this.f14093c.l();
        if (l8 != null) {
            return l8.put(this.f14091a, obj);
        }
        a();
        int i8 = this.f14092b;
        if (i8 == -1) {
            this.f14093c.put(this.f14091a, obj);
            return null;
        }
        Object[] objArr = this.f14093c.f14190d;
        objArr.getClass();
        Object obj2 = objArr[i8];
        objArr[i8] = obj;
        return obj2;
    }
}
