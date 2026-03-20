package com.arthenica.ffmpegkit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p extends a {

    /* renamed from: o  reason: collision with root package name */
    private n f8818o;

    /* renamed from: p  reason: collision with root package name */
    private final q f8819p;

    private p(String[] strArr, q qVar, m mVar) {
        super(strArr, mVar, LogRedirectionStrategy.NEVER_PRINT_LOGS);
        this.f8819p = qVar;
    }

    public static p z(String[] strArr, q qVar, m mVar) {
        return new p(strArr, qVar, mVar);
    }

    public q A() {
        return this.f8819p;
    }

    public n B() {
        return this.f8818o;
    }

    public void C(n nVar) {
        this.f8818o = nVar;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean c() {
        return false;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean p() {
        return false;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean r() {
        return true;
    }

    public String toString() {
        return "MediaInformationSession{sessionId=" + this.f8787a + ", createTime=" + this.f8789c + ", startTime=" + this.f8790d + ", endTime=" + this.f8791e + ", arguments=" + FFmpegKitConfig.c(this.f8792f) + ", logs=" + v() + ", state=" + this.f8796j + ", returnCode=" + this.f8797k + ", failStackTrace='" + this.f8798l + "'}";
    }
}
