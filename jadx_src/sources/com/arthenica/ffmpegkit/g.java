package com.arthenica.ffmpegkit;

import android.util.Log;
import java.util.LinkedList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g extends a {

    /* renamed from: o  reason: collision with root package name */
    private final w f8808o;

    /* renamed from: p  reason: collision with root package name */
    private final h f8809p;
    private final List<v> q;

    /* renamed from: r  reason: collision with root package name */
    private final Object f8810r;

    private g(String[] strArr, h hVar, m mVar, w wVar, LogRedirectionStrategy logRedirectionStrategy) {
        super(strArr, mVar, logRedirectionStrategy);
        this.f8809p = hVar;
        this.f8808o = wVar;
        this.q = new LinkedList();
        this.f8810r = new Object();
    }

    public static g A(String[] strArr, h hVar, m mVar, w wVar, LogRedirectionStrategy logRedirectionStrategy) {
        return new g(strArr, hVar, mVar, wVar, logRedirectionStrategy);
    }

    public List<v> B(int i8) {
        y(i8);
        if (f()) {
            Log.i("ffmpeg-kit", String.format("getAllStatistics was called to return all statistics but there are still statistics being transmitted for session id %d.", Long.valueOf(this.f8787a)));
        }
        return D();
    }

    public h C() {
        return this.f8809p;
    }

    public List<v> D() {
        List<v> list;
        synchronized (this.f8810r) {
            list = this.q;
        }
        return list;
    }

    public w E() {
        return this.f8808o;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean c() {
        return true;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean p() {
        return false;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean r() {
        return false;
    }

    public String toString() {
        return "FFmpegSession{sessionId=" + this.f8787a + ", createTime=" + this.f8789c + ", startTime=" + this.f8790d + ", endTime=" + this.f8791e + ", arguments=" + FFmpegKitConfig.c(this.f8792f) + ", logs=" + v() + ", state=" + this.f8796j + ", returnCode=" + this.f8797k + ", failStackTrace='" + this.f8798l + "'}";
    }

    public void z(v vVar) {
        synchronized (this.f8810r) {
            this.q.add(vVar);
        }
    }
}
