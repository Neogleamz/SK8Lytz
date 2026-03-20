package l6;

import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.a.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b<O extends a.d> {

    /* renamed from: a  reason: collision with root package name */
    private final int f21726a;

    /* renamed from: b  reason: collision with root package name */
    private final com.google.android.gms.common.api.a f21727b;

    /* renamed from: c  reason: collision with root package name */
    private final a.d f21728c;

    /* renamed from: d  reason: collision with root package name */
    private final String f21729d;

    private b(com.google.android.gms.common.api.a aVar, a.d dVar, String str) {
        this.f21727b = aVar;
        this.f21728c = dVar;
        this.f21729d = str;
        this.f21726a = n6.i.b(aVar, dVar, str);
    }

    public static <O extends a.d> b<O> a(com.google.android.gms.common.api.a<O> aVar, O o5, String str) {
        return new b<>(aVar, o5, str);
    }

    public final String b() {
        return this.f21727b.b();
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof b) {
            b bVar = (b) obj;
            return n6.i.a(this.f21727b, bVar.f21727b) && n6.i.a(this.f21728c, bVar.f21728c) && n6.i.a(this.f21729d, bVar.f21729d);
        }
        return false;
    }

    public final int hashCode() {
        return this.f21726a;
    }
}
