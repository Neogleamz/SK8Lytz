package com.google.android.gms.measurement.internal;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import com.google.android.gms.internal.measurement.ye;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c7 implements Callable<List<zzmv>> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16436a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ Bundle f16437b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ j6 f16438c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c7(j6 j6Var, zzn zznVar, Bundle bundle) {
        this.f16436a = zznVar;
        this.f16437b = bundle;
        this.f16438c = j6Var;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzmv> call() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f16438c.f16700a;
        gbVar.p0();
        gbVar2 = this.f16438c.f16700a;
        zzn zznVar = this.f16436a;
        Bundle bundle = this.f16437b;
        gbVar2.l().k();
        if (ye.a() && gbVar2.c0().B(zznVar.f17288a, c0.J0) && zznVar.f17288a != null) {
            if (bundle != null) {
                int[] intArray = bundle.getIntArray("uriSources");
                long[] longArray = bundle.getLongArray("uriTimestamps");
                if (intArray != null) {
                    if (longArray == null || longArray.length != intArray.length) {
                        gbVar2.i().E().a("Uri sources and timestamps do not match");
                    } else {
                        for (int i8 = 0; i8 < intArray.length; i8++) {
                            l e02 = gbVar2.e0();
                            String str = zznVar.f17288a;
                            int i9 = intArray[i8];
                            long j8 = longArray[i8];
                            n6.j.f(str);
                            e02.k();
                            e02.s();
                            try {
                                int delete = e02.z().delete("trigger_uris", "app_id=? and source=? and timestamp_millis<=?", new String[]{str, String.valueOf(i9), String.valueOf(j8)});
                                e02.i().I().d("Pruned " + delete + " trigger URIs. appId, source, timestamp", str, Integer.valueOf(i9), Long.valueOf(j8));
                            } catch (SQLiteException e8) {
                                e02.i().E().c("Error pruning trigger URIs. appId", x4.t(str), e8);
                            }
                        }
                    }
                }
            }
            return gbVar2.e0().K0(zznVar.f17288a);
        }
        return new ArrayList();
    }
}
