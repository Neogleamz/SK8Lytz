package androidx.media2.session;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import androidx.media2.session.SessionToken;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class SessionTokenImplBase implements SessionToken.SessionTokenImpl {

    /* renamed from: a  reason: collision with root package name */
    int f6218a;

    /* renamed from: b  reason: collision with root package name */
    int f6219b;

    /* renamed from: c  reason: collision with root package name */
    String f6220c;

    /* renamed from: d  reason: collision with root package name */
    String f6221d;

    /* renamed from: e  reason: collision with root package name */
    IBinder f6222e;

    /* renamed from: f  reason: collision with root package name */
    ComponentName f6223f;

    /* renamed from: g  reason: collision with root package name */
    Bundle f6224g;

    public boolean equals(Object obj) {
        if (obj instanceof SessionTokenImplBase) {
            SessionTokenImplBase sessionTokenImplBase = (SessionTokenImplBase) obj;
            return this.f6218a == sessionTokenImplBase.f6218a && TextUtils.equals(this.f6220c, sessionTokenImplBase.f6220c) && TextUtils.equals(this.f6221d, sessionTokenImplBase.f6221d) && this.f6219b == sessionTokenImplBase.f6219b && androidx.core.util.c.a(this.f6222e, sessionTokenImplBase.f6222e);
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.b(Integer.valueOf(this.f6219b), Integer.valueOf(this.f6218a), this.f6220c, this.f6221d);
    }

    public String toString() {
        return "SessionToken {pkg=" + this.f6220c + " type=" + this.f6219b + " service=" + this.f6221d + " IMediaSession=" + this.f6222e + " extras=" + this.f6224g + "}";
    }
}
