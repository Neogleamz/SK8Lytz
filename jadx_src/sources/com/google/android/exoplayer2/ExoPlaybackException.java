package com.google.android.exoplayer2;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.exoplayer2.g;
import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ExoPlaybackException extends PlaybackException {

    /* renamed from: j  reason: collision with root package name */
    public final int f9131j;

    /* renamed from: k  reason: collision with root package name */
    public final String f9132k;

    /* renamed from: l  reason: collision with root package name */
    public final int f9133l;

    /* renamed from: m  reason: collision with root package name */
    public final w0 f9134m;

    /* renamed from: n  reason: collision with root package name */
    public final int f9135n;

    /* renamed from: p  reason: collision with root package name */
    public final h5.j f9136p;
    final boolean q;

    /* renamed from: t  reason: collision with root package name */
    public static final g.a<ExoPlaybackException> f9126t = i4.f.a;

    /* renamed from: w  reason: collision with root package name */
    private static final String f9127w = b6.l0.r0(1001);

    /* renamed from: x  reason: collision with root package name */
    private static final String f9128x = b6.l0.r0(1002);

    /* renamed from: y  reason: collision with root package name */
    private static final String f9129y = b6.l0.r0(1003);

    /* renamed from: z  reason: collision with root package name */
    private static final String f9130z = b6.l0.r0(1004);
    private static final String A = b6.l0.r0(1005);
    private static final String B = b6.l0.r0(1006);

    private ExoPlaybackException(int i8, Throwable th, int i9) {
        this(i8, th, null, i9, null, -1, null, 4, false);
    }

    private ExoPlaybackException(int i8, Throwable th, String str, int i9, String str2, int i10, w0 w0Var, int i11, boolean z4) {
        this(j(i8, str, str2, i10, w0Var, i11), th, i9, i8, str2, i10, w0Var, i11, null, SystemClock.elapsedRealtime(), z4);
    }

    private ExoPlaybackException(Bundle bundle) {
        super(bundle);
        this.f9131j = bundle.getInt(f9127w, 2);
        this.f9132k = bundle.getString(f9128x);
        this.f9133l = bundle.getInt(f9129y, -1);
        Bundle bundle2 = bundle.getBundle(f9130z);
        this.f9134m = bundle2 == null ? null : w0.D0.a(bundle2);
        this.f9135n = bundle.getInt(A, 4);
        this.q = bundle.getBoolean(B, false);
        this.f9136p = null;
    }

    private ExoPlaybackException(String str, Throwable th, int i8, int i9, String str2, int i10, w0 w0Var, int i11, h5.j jVar, long j8, boolean z4) {
        super(str, th, i8, j8);
        boolean z8 = false;
        b6.a.a(!z4 || i9 == 1);
        b6.a.a((th != null || i9 == 3) ? true : z8);
        this.f9131j = i9;
        this.f9132k = str2;
        this.f9133l = i10;
        this.f9134m = w0Var;
        this.f9135n = i11;
        this.f9136p = jVar;
        this.q = z4;
    }

    public static /* synthetic */ ExoPlaybackException d(Bundle bundle) {
        return new ExoPlaybackException(bundle);
    }

    public static ExoPlaybackException f(Throwable th, String str, int i8, w0 w0Var, int i9, boolean z4, int i10) {
        return new ExoPlaybackException(1, th, null, i10, str, i8, w0Var, w0Var == null ? 4 : i9, z4);
    }

    public static ExoPlaybackException g(IOException iOException, int i8) {
        return new ExoPlaybackException(0, iOException, i8);
    }

    @Deprecated
    public static ExoPlaybackException h(RuntimeException runtimeException) {
        return i(runtimeException, 1000);
    }

    public static ExoPlaybackException i(RuntimeException runtimeException, int i8) {
        return new ExoPlaybackException(2, runtimeException, i8);
    }

    private static String j(int i8, String str, String str2, int i9, w0 w0Var, int i10) {
        String str3;
        if (i8 == 0) {
            str3 = "Source error";
        } else if (i8 != 1) {
            str3 = i8 != 3 ? "Unexpected runtime error" : "Remote error";
        } else {
            str3 = str2 + " error, index=" + i9 + ", format=" + w0Var + ", format_supported=" + b6.l0.W(i10);
        }
        if (TextUtils.isEmpty(str)) {
            return str3;
        }
        return str3 + ": " + str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ExoPlaybackException e(h5.j jVar) {
        return new ExoPlaybackException((String) b6.l0.j(getMessage()), getCause(), this.f9149a, this.f9131j, this.f9132k, this.f9133l, this.f9134m, this.f9135n, jVar, this.f9150b, this.q);
    }

    public Exception k() {
        b6.a.f(this.f9131j == 1);
        return (Exception) b6.a.e(getCause());
    }

    public IOException l() {
        b6.a.f(this.f9131j == 0);
        return (IOException) b6.a.e(getCause());
    }

    public RuntimeException m() {
        b6.a.f(this.f9131j == 2);
        return (RuntimeException) b6.a.e(getCause());
    }
}
