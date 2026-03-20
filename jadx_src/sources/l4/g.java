package l4;

import com.google.android.exoplayer2.w0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g {

    /* renamed from: a  reason: collision with root package name */
    public final String f21599a;

    /* renamed from: b  reason: collision with root package name */
    public final w0 f21600b;

    /* renamed from: c  reason: collision with root package name */
    public final w0 f21601c;

    /* renamed from: d  reason: collision with root package name */
    public final int f21602d;

    /* renamed from: e  reason: collision with root package name */
    public final int f21603e;

    public g(String str, w0 w0Var, w0 w0Var2, int i8, int i9) {
        b6.a.a(i8 == 0 || i9 == 0);
        this.f21599a = b6.a.d(str);
        this.f21600b = (w0) b6.a.e(w0Var);
        this.f21601c = (w0) b6.a.e(w0Var2);
        this.f21602d = i8;
        this.f21603e = i9;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || g.class != obj.getClass()) {
            return false;
        }
        g gVar = (g) obj;
        return this.f21602d == gVar.f21602d && this.f21603e == gVar.f21603e && this.f21599a.equals(gVar.f21599a) && this.f21600b.equals(gVar.f21600b) && this.f21601c.equals(gVar.f21601c);
    }

    public int hashCode() {
        return ((((((((527 + this.f21602d) * 31) + this.f21603e) * 31) + this.f21599a.hashCode()) * 31) + this.f21600b.hashCode()) * 31) + this.f21601c.hashCode();
    }
}
