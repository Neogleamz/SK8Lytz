package com.google.android.gms.internal.mlkit_common;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n {

    /* renamed from: a  reason: collision with root package name */
    Object[] f12995a = new Object[8];

    /* renamed from: b  reason: collision with root package name */
    int f12996b = 0;

    /* renamed from: c  reason: collision with root package name */
    m f12997c;

    public final n a(Object obj, Object obj2) {
        int i8 = this.f12996b + 1;
        Object[] objArr = this.f12995a;
        int length = objArr.length;
        int i9 = i8 + i8;
        if (i9 > length) {
            this.f12995a = Arrays.copyOf(objArr, j.a(length, i9));
        }
        g.a(obj, obj2);
        Object[] objArr2 = this.f12995a;
        int i10 = this.f12996b;
        int i11 = i10 + i10;
        objArr2[i11] = obj;
        objArr2[i11 + 1] = obj2;
        this.f12996b = i10 + 1;
        return this;
    }

    public final zzau b() {
        m mVar = this.f12997c;
        if (mVar == null) {
            u i8 = u.i(this.f12996b, this.f12995a, this);
            m mVar2 = this.f12997c;
            if (mVar2 == null) {
                return i8;
            }
            throw mVar2.a();
        }
        throw mVar.a();
    }
}
