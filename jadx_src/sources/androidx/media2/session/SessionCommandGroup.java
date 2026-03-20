package androidx.media2.session;

import java.util.HashSet;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SessionCommandGroup implements y1.b {

    /* renamed from: a  reason: collision with root package name */
    Set<SessionCommand> f6211a = new HashSet();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SessionCommandGroup) {
            Set<SessionCommand> set = this.f6211a;
            Set<SessionCommand> set2 = ((SessionCommandGroup) obj).f6211a;
            return set == null ? set2 == null : set.equals(set2);
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.c(this.f6211a);
    }
}
