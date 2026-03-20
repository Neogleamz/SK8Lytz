package com.google.android.exoplayer2;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class PlaybackException extends Exception implements g {

    /* renamed from: c  reason: collision with root package name */
    private static final String f9143c = b6.l0.r0(0);

    /* renamed from: d  reason: collision with root package name */
    private static final String f9144d = b6.l0.r0(1);

    /* renamed from: e  reason: collision with root package name */
    private static final String f9145e = b6.l0.r0(2);

    /* renamed from: f  reason: collision with root package name */
    private static final String f9146f = b6.l0.r0(3);

    /* renamed from: g  reason: collision with root package name */
    private static final String f9147g = b6.l0.r0(4);

    /* renamed from: h  reason: collision with root package name */
    public static final g.a<PlaybackException> f9148h = new g.a() { // from class: com.google.android.exoplayer2.v1
        @Override // com.google.android.exoplayer2.g.a
        public final g a(Bundle bundle) {
            return new PlaybackException(bundle);
        }
    };

    /* renamed from: a  reason: collision with root package name */
    public final int f9149a;

    /* renamed from: b  reason: collision with root package name */
    public final long f9150b;

    /* JADX INFO: Access modifiers changed from: protected */
    public PlaybackException(Bundle bundle) {
        this(bundle.getString(f9145e), c(bundle), bundle.getInt(f9143c, 1000), bundle.getLong(f9144d, SystemClock.elapsedRealtime()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PlaybackException(String str, Throwable th, int i8, long j8) {
        super(str, th);
        this.f9149a = i8;
        this.f9150b = j8;
    }

    private static RemoteException a(String str) {
        return new RemoteException(str);
    }

    private static Throwable b(Class<?> cls, String str) {
        return (Throwable) cls.getConstructor(String.class).newInstance(str);
    }

    private static Throwable c(Bundle bundle) {
        String string = bundle.getString(f9146f);
        String string2 = bundle.getString(f9147g);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        try {
            Class<?> cls = Class.forName(string, true, PlaybackException.class.getClassLoader());
            Throwable b9 = Throwable.class.isAssignableFrom(cls) ? b(cls, string2) : null;
            if (b9 != null) {
                return b9;
            }
        } catch (Throwable unused) {
        }
        return a(string2);
    }
}
