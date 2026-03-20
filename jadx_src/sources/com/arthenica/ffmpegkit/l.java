package com.arthenica.ffmpegkit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l {

    /* renamed from: a  reason: collision with root package name */
    private final long f8812a;

    /* renamed from: b  reason: collision with root package name */
    private final Level f8813b;

    /* renamed from: c  reason: collision with root package name */
    private final String f8814c;

    public l(long j8, Level level, String str) {
        this.f8812a = j8;
        this.f8813b = level;
        this.f8814c = str;
    }

    public Level a() {
        return this.f8813b;
    }

    public String b() {
        return this.f8814c;
    }

    public long c() {
        return this.f8812a;
    }

    public String toString() {
        return "Log{sessionId=" + this.f8812a + ", level=" + this.f8813b + ", message='" + this.f8814c + "'}";
    }
}
