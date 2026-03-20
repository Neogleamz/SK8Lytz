package androidx.media2.session;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SessionToken implements y1.b {

    /* renamed from: a  reason: collision with root package name */
    SessionTokenImpl f6217a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface SessionTokenImpl extends y1.b {
    }

    public boolean equals(Object obj) {
        if (obj instanceof SessionToken) {
            return this.f6217a.equals(((SessionToken) obj).f6217a);
        }
        return false;
    }

    public int hashCode() {
        return this.f6217a.hashCode();
    }

    public String toString() {
        return this.f6217a.toString();
    }
}
