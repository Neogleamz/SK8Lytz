package l5;

import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* renamed from: a  reason: collision with root package name */
    public final String f21674a;

    /* renamed from: b  reason: collision with root package name */
    public final String f21675b;

    /* renamed from: c  reason: collision with root package name */
    public final String f21676c;

    /* renamed from: d  reason: collision with root package name */
    public final String f21677d;

    /* renamed from: e  reason: collision with root package name */
    public final String f21678e;

    public h(String str, String str2, String str3, String str4, String str5) {
        this.f21674a = str;
        this.f21675b = str2;
        this.f21676c = str3;
        this.f21677d = str4;
        this.f21678e = str5;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof h) {
            h hVar = (h) obj;
            return l0.c(this.f21674a, hVar.f21674a) && l0.c(this.f21675b, hVar.f21675b) && l0.c(this.f21676c, hVar.f21676c) && l0.c(this.f21677d, hVar.f21677d) && l0.c(this.f21678e, hVar.f21678e);
        }
        return false;
    }

    public int hashCode() {
        String str = this.f21674a;
        int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.f21675b;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.f21676c;
        int hashCode3 = (hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.f21677d;
        int hashCode4 = (hashCode3 + (str4 != null ? str4.hashCode() : 0)) * 31;
        String str5 = this.f21678e;
        return hashCode4 + (str5 != null ? str5.hashCode() : 0);
    }
}
