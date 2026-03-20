package com.google.android.gms.internal.mlkit_vision_common;

import android.os.SystemClock;
import java.io.Closeable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class eb implements Closeable {

    /* renamed from: h  reason: collision with root package name */
    private static final Map f15427h = new HashMap();

    /* renamed from: a  reason: collision with root package name */
    private final String f15428a;

    /* renamed from: b  reason: collision with root package name */
    private int f15429b;

    /* renamed from: c  reason: collision with root package name */
    private double f15430c;

    /* renamed from: d  reason: collision with root package name */
    private long f15431d;

    /* renamed from: e  reason: collision with root package name */
    private long f15432e;

    /* renamed from: f  reason: collision with root package name */
    private long f15433f;

    /* renamed from: g  reason: collision with root package name */
    private long f15434g;

    private eb(String str) {
        this.f15433f = 2147483647L;
        this.f15434g = -2147483648L;
        this.f15428a = str;
    }

    private final void a() {
        this.f15429b = 0;
        this.f15430c = 0.0d;
        this.f15431d = 0L;
        this.f15433f = 2147483647L;
        this.f15434g = -2147483648L;
    }

    public static eb f(String str) {
        cb cbVar;
        ec.a();
        if (!ec.b()) {
            cbVar = cb.f15378j;
            return cbVar;
        }
        Map map = f15427h;
        if (map.get("detectorTaskWithResource#run") == null) {
            map.put("detectorTaskWithResource#run", new eb("detectorTaskWithResource#run"));
        }
        return (eb) map.get("detectorTaskWithResource#run");
    }

    public eb b() {
        this.f15431d = SystemClock.elapsedRealtimeNanos() / 1000;
        return this;
    }

    public void c(long j8) {
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos() / 1000;
        long j9 = this.f15432e;
        if (j9 != 0 && elapsedRealtimeNanos - j9 >= 1000000) {
            a();
        }
        this.f15432e = elapsedRealtimeNanos;
        this.f15429b++;
        this.f15430c += j8;
        this.f15433f = Math.min(this.f15433f, j8);
        this.f15434g = Math.max(this.f15434g, j8);
        if (this.f15429b % 50 == 0) {
            String.format(Locale.US, "[%s] cur=%dus, counts=%d, min=%dus, max=%dus, avg=%dus", this.f15428a, Long.valueOf(j8), Integer.valueOf(this.f15429b), Long.valueOf(this.f15433f), Long.valueOf(this.f15434g), Integer.valueOf((int) (this.f15430c / this.f15429b)));
            ec.a();
        }
        if (this.f15429b % 500 == 0) {
            a();
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        long j8 = this.f15431d;
        if (j8 == 0) {
            throw new IllegalStateException("Did you forget to call start()?");
        }
        d(j8);
    }

    public void d(long j8) {
        c((SystemClock.elapsedRealtimeNanos() / 1000) - j8);
    }
}
