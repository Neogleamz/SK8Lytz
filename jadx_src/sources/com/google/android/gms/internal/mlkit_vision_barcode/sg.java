package com.google.android.gms.internal.mlkit_vision_barcode;

import android.content.Context;
import android.os.SystemClock;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.TelemetryData;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class sg {

    /* renamed from: a  reason: collision with root package name */
    private final n6.n f14018a;

    /* renamed from: b  reason: collision with root package name */
    private final AtomicLong f14019b = new AtomicLong(-1);

    sg(Context context, String str) {
        this.f14018a = n6.m.b(context, n6.o.a().b("mlkit:vision").a());
    }

    public static sg a(Context context) {
        return new sg(context, "mlkit:vision");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void b(long j8, Exception exc) {
        this.f14019b.set(j8);
    }

    public final synchronized void c(int i8, int i9, long j8, long j9) {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.f14019b.get() != -1 && elapsedRealtime - this.f14019b.get() <= TimeUnit.MINUTES.toMillis(30L)) {
            return;
        }
        this.f14018a.a(new TelemetryData(0, Arrays.asList(new MethodInvocation(i8, i9, 0, j8, j9, null, null, 0)))).d(new j7.f() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.rg
            @Override // j7.f
            public final void b(Exception exc) {
                sg.this.b(elapsedRealtime, exc);
            }
        });
    }
}
