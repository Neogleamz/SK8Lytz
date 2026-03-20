package androidx.media2.session;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.media2.session.SessionToken;
import androidx.versionedparcelable.CustomVersionedParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class SessionTokenImplLegacy extends CustomVersionedParcelable implements SessionToken.SessionTokenImpl {

    /* renamed from: a  reason: collision with root package name */
    private MediaSessionCompat.Token f6225a;

    /* renamed from: b  reason: collision with root package name */
    Bundle f6226b;

    /* renamed from: c  reason: collision with root package name */
    int f6227c;

    /* renamed from: d  reason: collision with root package name */
    int f6228d;

    /* renamed from: e  reason: collision with root package name */
    ComponentName f6229e;

    /* renamed from: f  reason: collision with root package name */
    String f6230f;

    /* renamed from: g  reason: collision with root package name */
    Bundle f6231g;

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void c() {
        this.f6225a = MediaSessionCompat.Token.a(this.f6226b);
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void d(boolean z4) {
        MediaSessionCompat.Token token = this.f6225a;
        if (token == null) {
            this.f6226b = null;
            return;
        }
        synchronized (token) {
            y1.b e8 = this.f6225a.e();
            this.f6225a.h(null);
            this.f6226b = this.f6225a.i();
            this.f6225a.h(e8);
        }
    }

    public boolean equals(Object obj) {
        Object obj2;
        Object obj3;
        if (obj instanceof SessionTokenImplLegacy) {
            SessionTokenImplLegacy sessionTokenImplLegacy = (SessionTokenImplLegacy) obj;
            int i8 = this.f6228d;
            if (i8 != sessionTokenImplLegacy.f6228d) {
                return false;
            }
            if (i8 == 100) {
                obj2 = this.f6225a;
                obj3 = sessionTokenImplLegacy.f6225a;
            } else if (i8 != 101) {
                return false;
            } else {
                obj2 = this.f6229e;
                obj3 = sessionTokenImplLegacy.f6229e;
            }
            return androidx.core.util.c.a(obj2, obj3);
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.b(Integer.valueOf(this.f6228d), this.f6229e, this.f6225a);
    }

    public String toString() {
        return "SessionToken {legacyToken=" + this.f6225a + "}";
    }
}
