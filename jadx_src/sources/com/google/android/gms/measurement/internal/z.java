package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z {

    /* renamed from: a  reason: collision with root package name */
    final String f17206a;

    /* renamed from: b  reason: collision with root package name */
    final String f17207b;

    /* renamed from: c  reason: collision with root package name */
    final long f17208c;

    /* renamed from: d  reason: collision with root package name */
    final long f17209d;

    /* renamed from: e  reason: collision with root package name */
    final long f17210e;

    /* renamed from: f  reason: collision with root package name */
    final long f17211f;

    /* renamed from: g  reason: collision with root package name */
    final long f17212g;

    /* renamed from: h  reason: collision with root package name */
    final Long f17213h;

    /* renamed from: i  reason: collision with root package name */
    final Long f17214i;

    /* renamed from: j  reason: collision with root package name */
    final Long f17215j;

    /* renamed from: k  reason: collision with root package name */
    final Boolean f17216k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z(String str, String str2, long j8, long j9, long j10, long j11, long j12, Long l8, Long l9, Long l10, Boolean bool) {
        n6.j.f(str);
        n6.j.f(str2);
        n6.j.a(j8 >= 0);
        n6.j.a(j9 >= 0);
        n6.j.a(j10 >= 0);
        n6.j.a(j12 >= 0);
        this.f17206a = str;
        this.f17207b = str2;
        this.f17208c = j8;
        this.f17209d = j9;
        this.f17210e = j10;
        this.f17211f = j11;
        this.f17212g = j12;
        this.f17213h = l8;
        this.f17214i = l9;
        this.f17215j = l10;
        this.f17216k = bool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public z(String str, String str2, long j8, long j9, long j10, long j11, Long l8, Long l9, Long l10, Boolean bool) {
        this(str, str2, 0L, 0L, 0L, j10, 0L, null, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final z a(long j8) {
        return new z(this.f17206a, this.f17207b, this.f17208c, this.f17209d, this.f17210e, j8, this.f17212g, this.f17213h, this.f17214i, this.f17215j, this.f17216k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final z b(long j8, long j9) {
        return new z(this.f17206a, this.f17207b, this.f17208c, this.f17209d, this.f17210e, this.f17211f, j8, Long.valueOf(j9), this.f17214i, this.f17215j, this.f17216k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final z c(Long l8, Long l9, Boolean bool) {
        return new z(this.f17206a, this.f17207b, this.f17208c, this.f17209d, this.f17210e, this.f17211f, this.f17212g, this.f17213h, l8, l9, (bool == null || bool.booleanValue()) ? bool : null);
    }
}
