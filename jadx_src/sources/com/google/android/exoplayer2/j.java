package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j implements g {

    /* renamed from: d  reason: collision with root package name */
    public static final j f9810d = new j(0, 0, 0);

    /* renamed from: e  reason: collision with root package name */
    private static final String f9811e = b6.l0.r0(0);

    /* renamed from: f  reason: collision with root package name */
    private static final String f9812f = b6.l0.r0(1);

    /* renamed from: g  reason: collision with root package name */
    private static final String f9813g = b6.l0.r0(2);

    /* renamed from: h  reason: collision with root package name */
    public static final g.a<j> f9814h = i4.e.a;

    /* renamed from: a  reason: collision with root package name */
    public final int f9815a;

    /* renamed from: b  reason: collision with root package name */
    public final int f9816b;

    /* renamed from: c  reason: collision with root package name */
    public final int f9817c;

    public j(int i8, int i9, int i10) {
        this.f9815a = i8;
        this.f9816b = i9;
        this.f9817c = i10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ j b(Bundle bundle) {
        return new j(bundle.getInt(f9811e, 0), bundle.getInt(f9812f, 0), bundle.getInt(f9813g, 0));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof j) {
            j jVar = (j) obj;
            return this.f9815a == jVar.f9815a && this.f9816b == jVar.f9816b && this.f9817c == jVar.f9817c;
        }
        return false;
    }

    public int hashCode() {
        return ((((527 + this.f9815a) * 31) + this.f9816b) * 31) + this.f9817c;
    }
}
