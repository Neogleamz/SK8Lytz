package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zb extends q {

    /* renamed from: b  reason: collision with root package name */
    private final d f12731b;

    public zb(d dVar) {
        this.f12731b = dVar;
    }

    @Override // com.google.android.gms.internal.measurement.q, com.google.android.gms.internal.measurement.r
    public final r g(String str, g6 g6Var, List<r> list) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case 21624207:
                if (str.equals("getEventName")) {
                    c9 = 0;
                    break;
                }
                break;
            case 45521504:
                if (str.equals("getTimestamp")) {
                    c9 = 1;
                    break;
                }
                break;
            case 146575578:
                if (str.equals("getParamValue")) {
                    c9 = 2;
                    break;
                }
                break;
            case 700587132:
                if (str.equals("getParams")) {
                    c9 = 3;
                    break;
                }
                break;
            case 920706790:
                if (str.equals("setParamValue")) {
                    c9 = 4;
                    break;
                }
                break;
            case 1570616835:
                if (str.equals("setEventName")) {
                    c9 = 5;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                e5.g("getEventName", 0, list);
                return new t(this.f12731b.d().e());
            case 1:
                e5.g("getTimestamp", 0, list);
                return new j(Double.valueOf(this.f12731b.d().a()));
            case 2:
                e5.g("getParamValue", 1, list);
                return g8.b(this.f12731b.d().b(g6Var.b(list.get(0)).e()));
            case 3:
                e5.g("getParams", 0, list);
                Map<String, Object> g8 = this.f12731b.d().g();
                q qVar = new q();
                for (String str2 : g8.keySet()) {
                    qVar.n(str2, g8.b(g8.get(str2)));
                }
                return qVar;
            case 4:
                e5.g("setParamValue", 2, list);
                String e8 = g6Var.b(list.get(0)).e();
                r b9 = g6Var.b(list.get(1));
                this.f12731b.d().d(e8, e5.d(b9));
                return b9;
            case 5:
                e5.g("setEventName", 1, list);
                r b10 = g6Var.b(list.get(0));
                if (r.f12463r.equals(b10) || r.f12464s.equals(b10)) {
                    throw new IllegalArgumentException("Illegal event name");
                }
                this.f12731b.d().f(b10.e());
                return new t(b10.e());
            default:
                return super.g(str, g6Var, list);
        }
    }
}
