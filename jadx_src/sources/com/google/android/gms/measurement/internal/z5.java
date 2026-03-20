package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.yc;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z5 implements yc {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f17225a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ r5 f17226b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z5(r5 r5Var, String str) {
        this.f17225a = str;
        this.f17226b = r5Var;
    }

    @Override // com.google.android.gms.internal.measurement.yc
    public final String h(String str) {
        Map map;
        map = this.f17226b.f16932d;
        Map map2 = (Map) map.get(this.f17225a);
        if (map2 == null || !map2.containsKey(str)) {
            return null;
        }
        return (String) map2.get(str);
    }
}
