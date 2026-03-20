package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f16559a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16560b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g8(h7 h7Var, com.google.android.gms.internal.measurement.h2 h2Var) {
        this.f16559a = h2Var;
        this.f16560b = h7Var;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0086 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            r7 = this;
            com.google.android.gms.measurement.internal.h7 r0 = r7.f16560b
            com.google.android.gms.measurement.internal.na r0 = r0.s()
            boolean r1 = com.google.android.gms.internal.measurement.kf.a()
            r2 = 0
            if (r1 == 0) goto L64
            com.google.android.gms.measurement.internal.e r1 = r0.a()
            com.google.android.gms.measurement.internal.n4<java.lang.Boolean> r3 = com.google.android.gms.measurement.internal.c0.B0
            boolean r1 = r1.r(r3)
            if (r1 == 0) goto L64
            com.google.android.gms.measurement.internal.h5 r1 = r0.f()
            com.google.android.gms.measurement.internal.zziq r1 = r1.J()
            boolean r1 = r1.B()
            if (r1 != 0) goto L32
            com.google.android.gms.measurement.internal.x4 r0 = r0.i()
            com.google.android.gms.measurement.internal.z4 r0 = r0.K()
            java.lang.String r1 = "Analytics storage consent denied; will not get session id"
            goto L6e
        L32:
            com.google.android.gms.measurement.internal.h5 r1 = r0.f()
            u6.d r3 = r0.zzb()
            long r3 = r3.a()
            boolean r1 = r1.w(r3)
            if (r1 != 0) goto L71
            com.google.android.gms.measurement.internal.h5 r1 = r0.f()
            com.google.android.gms.measurement.internal.m5 r1 = r1.f16616s
            long r3 = r1.a()
            r5 = 0
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 != 0) goto L55
            goto L71
        L55:
            com.google.android.gms.measurement.internal.h5 r0 = r0.f()
            com.google.android.gms.measurement.internal.m5 r0 = r0.f16616s
            long r0 = r0.a()
            java.lang.Long r0 = java.lang.Long.valueOf(r0)
            goto L72
        L64:
            com.google.android.gms.measurement.internal.x4 r0 = r0.i()
            com.google.android.gms.measurement.internal.z4 r0 = r0.K()
            java.lang.String r1 = "getSessionId has been disabled."
        L6e:
            r0.a(r1)
        L71:
            r0 = r2
        L72:
            if (r0 == 0) goto L86
            com.google.android.gms.measurement.internal.h7 r1 = r7.f16560b
            com.google.android.gms.measurement.internal.f6 r1 = r1.f16485a
            com.google.android.gms.measurement.internal.sb r1 = r1.J()
            com.google.android.gms.internal.measurement.h2 r2 = r7.f16559a
            long r3 = r0.longValue()
            r1.O(r2, r3)
            return
        L86:
            com.google.android.gms.internal.measurement.h2 r0 = r7.f16559a     // Catch: android.os.RemoteException -> L8c
            r0.c(r2)     // Catch: android.os.RemoteException -> L8c
            return
        L8c:
            r0 = move-exception
            com.google.android.gms.measurement.internal.h7 r1 = r7.f16560b
            com.google.android.gms.measurement.internal.f6 r1 = r1.f16485a
            com.google.android.gms.measurement.internal.x4 r1 = r1.i()
            com.google.android.gms.measurement.internal.z4 r1 = r1.E()
            java.lang.String r2 = "getSessionId failed with exception"
            r1.b(r2, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.g8.run():void");
    }
}
