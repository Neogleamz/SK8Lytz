package com.arthenica.ffmpegkit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends a {

    /* renamed from: o  reason: collision with root package name */
    private final k f8811o;

    private j(String[] strArr, k kVar, m mVar, LogRedirectionStrategy logRedirectionStrategy) {
        super(strArr, mVar, logRedirectionStrategy);
        this.f8811o = kVar;
    }

    public static j z(String[] strArr, k kVar, m mVar, LogRedirectionStrategy logRedirectionStrategy) {
        return new j(strArr, kVar, mVar, logRedirectionStrategy);
    }

    public k A() {
        return this.f8811o;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean c() {
        return false;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean p() {
        return true;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean r() {
        return false;
    }

    public String toString() {
        return "FFprobeSession{sessionId=" + this.f8787a + ", createTime=" + this.f8789c + ", startTime=" + this.f8790d + ", endTime=" + this.f8791e + ", arguments=" + FFmpegKitConfig.c(this.f8792f) + ", logs=" + v() + ", state=" + this.f8796j + ", returnCode=" + this.f8797k + ", failStackTrace='" + this.f8798l + "'}";
    }
}
