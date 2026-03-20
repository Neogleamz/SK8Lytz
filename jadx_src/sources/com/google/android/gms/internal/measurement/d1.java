package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d1 extends j1 {

    /* renamed from: b  reason: collision with root package name */
    private final String f12128b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f12129c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f12130d;

    /* renamed from: e  reason: collision with root package name */
    private final c1 f12131e;

    /* renamed from: f  reason: collision with root package name */
    private final e1 f12132f;

    /* renamed from: g  reason: collision with root package name */
    private final zzcl f12133g;

    private d1(String str, boolean z4, boolean z8, c1 c1Var, e1 e1Var, zzcl zzclVar) {
        this.f12128b = str;
        this.f12129c = z4;
        this.f12130d = z8;
        this.f12131e = null;
        this.f12132f = null;
        this.f12133g = zzclVar;
    }

    @Override // com.google.android.gms.internal.measurement.j1
    public final c1 a() {
        return this.f12131e;
    }

    @Override // com.google.android.gms.internal.measurement.j1
    public final e1 b() {
        return this.f12132f;
    }

    @Override // com.google.android.gms.internal.measurement.j1
    public final zzcl c() {
        return this.f12133g;
    }

    @Override // com.google.android.gms.internal.measurement.j1
    public final String d() {
        return this.f12128b;
    }

    @Override // com.google.android.gms.internal.measurement.j1
    public final boolean e() {
        return this.f12129c;
    }

    public final boolean equals(Object obj) {
        c1 c1Var;
        e1 e1Var;
        if (obj == this) {
            return true;
        }
        if (obj instanceof j1) {
            j1 j1Var = (j1) obj;
            if (this.f12128b.equals(j1Var.d()) && this.f12129c == j1Var.e() && this.f12130d == j1Var.f() && ((c1Var = this.f12131e) != null ? c1Var.equals(j1Var.a()) : j1Var.a() == null) && ((e1Var = this.f12132f) != null ? e1Var.equals(j1Var.b()) : j1Var.b() == null) && this.f12133g.equals(j1Var.c())) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.j1
    public final boolean f() {
        return this.f12130d;
    }

    public final int hashCode() {
        int hashCode = (((((this.f12128b.hashCode() ^ 1000003) * 1000003) ^ (this.f12129c ? 1231 : 1237)) * 1000003) ^ (this.f12130d ? 1231 : 1237)) * 1000003;
        c1 c1Var = this.f12131e;
        int hashCode2 = (hashCode ^ (c1Var == null ? 0 : c1Var.hashCode())) * 1000003;
        e1 e1Var = this.f12132f;
        return ((hashCode2 ^ (e1Var != null ? e1Var.hashCode() : 0)) * 1000003) ^ this.f12133g.hashCode();
    }

    public final String toString() {
        String str = this.f12128b;
        boolean z4 = this.f12129c;
        boolean z8 = this.f12130d;
        String valueOf = String.valueOf(this.f12131e);
        String valueOf2 = String.valueOf(this.f12132f);
        String valueOf3 = String.valueOf(this.f12133g);
        return "FileComplianceOptions{fileOwner=" + str + ", hasDifferentDmaOwner=" + z4 + ", skipChecks=" + z8 + ", dataForwardingNotAllowedResolver=" + valueOf + ", multipleProductIdGroupsResolver=" + valueOf2 + ", filePurpose=" + valueOf3 + "}";
    }
}
