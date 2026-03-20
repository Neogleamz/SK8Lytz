package com.google.android.gms.common.api.internal;

import android.os.SystemClock;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.util.VisibleForTesting;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w implements j7.e {

    /* renamed from: a  reason: collision with root package name */
    private final b f11705a;

    /* renamed from: b  reason: collision with root package name */
    private final int f11706b;

    /* renamed from: c  reason: collision with root package name */
    private final l6.b f11707c;

    /* renamed from: d  reason: collision with root package name */
    private final long f11708d;

    /* renamed from: e  reason: collision with root package name */
    private final long f11709e;

    @VisibleForTesting
    w(b bVar, int i8, l6.b bVar2, long j8, long j9, String str, String str2) {
        this.f11705a = bVar;
        this.f11706b = i8;
        this.f11707c = bVar2;
        this.f11708d = j8;
        this.f11709e = j9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w b(b bVar, int i8, l6.b bVar2) {
        boolean z4;
        if (bVar.f()) {
            RootTelemetryConfiguration a9 = n6.k.b().a();
            if (a9 == null) {
                z4 = true;
            } else if (!a9.Z()) {
                return null;
            } else {
                z4 = a9.D0();
                r w8 = bVar.w(bVar2);
                if (w8 != null) {
                    if (!(w8.v() instanceof com.google.android.gms.common.internal.b)) {
                        return null;
                    }
                    com.google.android.gms.common.internal.b bVar3 = (com.google.android.gms.common.internal.b) w8.v();
                    if (bVar3.H() && !bVar3.e()) {
                        ConnectionTelemetryConfiguration c9 = c(w8, bVar3, i8);
                        if (c9 == null) {
                            return null;
                        }
                        w8.G();
                        z4 = c9.E0();
                    }
                }
            }
            return new w(bVar, i8, bVar2, z4 ? System.currentTimeMillis() : 0L, z4 ? SystemClock.elapsedRealtime() : 0L, null, null);
        }
        return null;
    }

    private static ConnectionTelemetryConfiguration c(r rVar, com.google.android.gms.common.internal.b bVar, int i8) {
        int[] u8;
        int[] Z;
        ConnectionTelemetryConfiguration F = bVar.F();
        if (F == null || !F.D0() || ((u8 = F.u()) != null ? !u6.b.a(u8, i8) : !((Z = F.Z()) == null || !u6.b.a(Z, i8))) || rVar.s() >= F.t()) {
            return null;
        }
        return F;
    }

    @Override // j7.e
    public final void a(j7.j jVar) {
        r w8;
        int i8;
        int i9;
        int i10;
        int t8;
        long j8;
        long j9;
        int i11;
        if (this.f11705a.f()) {
            RootTelemetryConfiguration a9 = n6.k.b().a();
            if ((a9 == null || a9.Z()) && (w8 = this.f11705a.w(this.f11707c)) != null && (w8.v() instanceof com.google.android.gms.common.internal.b)) {
                com.google.android.gms.common.internal.b bVar = (com.google.android.gms.common.internal.b) w8.v();
                boolean z4 = true;
                int i12 = 0;
                boolean z8 = this.f11708d > 0;
                int x8 = bVar.x();
                if (a9 != null) {
                    z8 &= a9.D0();
                    int t9 = a9.t();
                    int u8 = a9.u();
                    i8 = a9.E0();
                    if (bVar.H() && !bVar.e()) {
                        ConnectionTelemetryConfiguration c9 = c(w8, bVar, this.f11706b);
                        if (c9 == null) {
                            return;
                        }
                        if (!c9.E0() || this.f11708d <= 0) {
                            z4 = false;
                        }
                        u8 = c9.t();
                        z8 = z4;
                    }
                    i10 = t9;
                    i9 = u8;
                } else {
                    i8 = 0;
                    i9 = 100;
                    i10 = 5000;
                }
                b bVar2 = this.f11705a;
                if (jVar.p()) {
                    t8 = 0;
                } else {
                    if (jVar.n()) {
                        i12 = 100;
                    } else {
                        Exception k8 = jVar.k();
                        if (k8 instanceof ApiException) {
                            Status a10 = ((ApiException) k8).a();
                            int u9 = a10.u();
                            ConnectionResult t10 = a10.t();
                            t8 = t10 == null ? -1 : t10.t();
                            i12 = u9;
                        } else {
                            i12 = 101;
                        }
                    }
                    t8 = -1;
                }
                if (z8) {
                    long j10 = this.f11708d;
                    long currentTimeMillis = System.currentTimeMillis();
                    i11 = (int) (SystemClock.elapsedRealtime() - this.f11709e);
                    j8 = j10;
                    j9 = currentTimeMillis;
                } else {
                    j8 = 0;
                    j9 = 0;
                    i11 = -1;
                }
                bVar2.G(new MethodInvocation(this.f11706b, i12, t8, j8, j9, null, null, x8, i11), i8, i10, i9);
            }
        }
    }
}
