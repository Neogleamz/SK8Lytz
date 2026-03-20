package com.google.android.gms.measurement.internal;

import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Pair;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ac {

    /* renamed from: a  reason: collision with root package name */
    private com.google.android.gms.internal.measurement.r4 f16321a;

    /* renamed from: b  reason: collision with root package name */
    private Long f16322b;

    /* renamed from: c  reason: collision with root package name */
    private long f16323c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ wb f16324d;

    private ac(wb wbVar) {
        this.f16324d = wbVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final com.google.android.gms.internal.measurement.r4 a(String str, com.google.android.gms.internal.measurement.r4 r4Var) {
        z4 G;
        String str2;
        Object obj;
        String d02 = r4Var.d0();
        List<com.google.android.gms.internal.measurement.t4> e02 = r4Var.e0();
        this.f16324d.m();
        Long l8 = (Long) nb.f0(r4Var, "_eid");
        boolean z4 = l8 != null;
        if (z4 && d02.equals("_ep")) {
            n6.j.l(l8);
            this.f16324d.m();
            d02 = (String) nb.f0(r4Var, "_en");
            if (TextUtils.isEmpty(d02)) {
                this.f16324d.i().G().b("Extra parameter without an event name. eventId", l8);
                return null;
            }
            if (this.f16321a == null || this.f16322b == null || l8.longValue() != this.f16322b.longValue()) {
                Pair<com.google.android.gms.internal.measurement.r4, Long> F = this.f16324d.o().F(str, l8);
                if (F == null || (obj = F.first) == null) {
                    this.f16324d.i().G().c("Extra parameter without existing main event. eventName, eventId", d02, l8);
                    return null;
                }
                this.f16321a = (com.google.android.gms.internal.measurement.r4) obj;
                this.f16323c = ((Long) F.second).longValue();
                this.f16324d.m();
                this.f16322b = (Long) nb.f0(this.f16321a, "_eid");
            }
            long j8 = this.f16323c - 1;
            this.f16323c = j8;
            if (j8 <= 0) {
                l o5 = this.f16324d.o();
                o5.k();
                o5.i().I().b("Clearing complex main event info. appId", str);
                try {
                    o5.z().execSQL("delete from main_event_params where app_id=?", new String[]{str});
                } catch (SQLiteException e8) {
                    o5.i().E().b("Error clearing complex main event", e8);
                }
            } else {
                this.f16324d.o().h0(str, l8, this.f16323c, this.f16321a);
            }
            ArrayList arrayList = new ArrayList();
            for (com.google.android.gms.internal.measurement.t4 t4Var : this.f16321a.e0()) {
                this.f16324d.m();
                if (nb.D(r4Var, t4Var.e0()) == null) {
                    arrayList.add(t4Var);
                }
            }
            if (arrayList.isEmpty()) {
                G = this.f16324d.i().G();
                str2 = "No unique parameters in main event. eventName";
                G.b(str2, d02);
            } else {
                arrayList.addAll(e02);
                e02 = arrayList;
            }
        } else if (z4) {
            this.f16322b = l8;
            this.f16321a = r4Var;
            this.f16324d.m();
            Object f02 = nb.f0(r4Var, "_epc");
            long longValue = ((Long) (f02 != null ? f02 : 0L)).longValue();
            this.f16323c = longValue;
            if (longValue <= 0) {
                G = this.f16324d.i().G();
                str2 = "Complex event with zero extra param count. eventName";
                G.b(str2, d02);
            } else {
                this.f16324d.o().h0(str, (Long) n6.j.l(l8), this.f16323c, r4Var);
            }
        }
        return (com.google.android.gms.internal.measurement.r4) ((com.google.android.gms.internal.measurement.x8) r4Var.z().F(d02).K().E(e02).n());
    }
}
