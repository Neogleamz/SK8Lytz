package l5;

import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    public final String f21661a;

    /* renamed from: b  reason: collision with root package name */
    public final String f21662b;

    /* renamed from: c  reason: collision with root package name */
    public final String f21663c;

    public e(String str, String str2, String str3) {
        this.f21661a = str;
        this.f21662b = str2;
        this.f21663c = str3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || e.class != obj.getClass()) {
            return false;
        }
        e eVar = (e) obj;
        return l0.c(this.f21661a, eVar.f21661a) && l0.c(this.f21662b, eVar.f21662b) && l0.c(this.f21663c, eVar.f21663c);
    }

    public int hashCode() {
        int hashCode = this.f21661a.hashCode() * 31;
        String str = this.f21662b;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.f21663c;
        return hashCode2 + (str2 != null ? str2.hashCode() : 0);
    }
}
