package com.google.android.gms.measurement;

import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.measurement.internal.f6;
import com.google.android.gms.measurement.internal.h7;
import java.util.List;
import java.util.Map;
import n6.j;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends AppMeasurement.a {

    /* renamed from: a  reason: collision with root package name */
    private final f6 f16286a;

    /* renamed from: b  reason: collision with root package name */
    private final h7 f16287b;

    public b(f6 f6Var) {
        super();
        j.l(f6Var);
        this.f16286a = f6Var;
        this.f16287b = f6Var.F();
    }

    @Override // f7.w
    public final void a(String str, String str2, Bundle bundle) {
        this.f16286a.F().W(str, str2, bundle);
    }

    @Override // f7.w
    public final void b(String str) {
        this.f16286a.w().x(str, this.f16286a.zzb().b());
    }

    @Override // f7.w
    public final void c(Bundle bundle) {
        this.f16287b.v0(bundle);
    }

    @Override // f7.w
    public final List<Bundle> d(String str, String str2) {
        return this.f16287b.A(str, str2);
    }

    @Override // f7.w
    public final long e() {
        return this.f16286a.J().P0();
    }

    @Override // f7.w
    public final String f() {
        return this.f16287b.i0();
    }

    @Override // f7.w
    public final String g() {
        return this.f16287b.h0();
    }

    @Override // f7.w
    public final int h(String str) {
        j.f(str);
        return 25;
    }

    @Override // f7.w
    public final String i() {
        return this.f16287b.h0();
    }

    @Override // f7.w
    public final String j() {
        return this.f16287b.j0();
    }

    @Override // f7.w
    public final void k(String str) {
        this.f16286a.w().B(str, this.f16286a.zzb().b());
    }

    @Override // f7.w
    public final Map<String, Object> l(String str, String str2, boolean z4) {
        return this.f16287b.B(str, str2, z4);
    }

    @Override // f7.w
    public final void m(String str, String str2, Bundle bundle) {
        this.f16287b.z0(str, str2, bundle);
    }
}
