package com.arthenica.ffmpegkit;

import android.util.Log;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a implements u {

    /* renamed from: n  reason: collision with root package name */
    protected static final AtomicLong f8786n = new AtomicLong(1);

    /* renamed from: b  reason: collision with root package name */
    protected final m f8788b;

    /* renamed from: f  reason: collision with root package name */
    protected final String[] f8792f;

    /* renamed from: m  reason: collision with root package name */
    protected final LogRedirectionStrategy f8799m;

    /* renamed from: a  reason: collision with root package name */
    protected final long f8787a = f8786n.getAndIncrement();

    /* renamed from: c  reason: collision with root package name */
    protected final Date f8789c = new Date();

    /* renamed from: d  reason: collision with root package name */
    protected Date f8790d = null;

    /* renamed from: e  reason: collision with root package name */
    protected Date f8791e = null;

    /* renamed from: g  reason: collision with root package name */
    protected final List<l> f8793g = new LinkedList();

    /* renamed from: h  reason: collision with root package name */
    protected final Object f8794h = new Object();

    /* renamed from: i  reason: collision with root package name */
    protected Future<?> f8795i = null;

    /* renamed from: j  reason: collision with root package name */
    protected SessionState f8796j = SessionState.CREATED;

    /* renamed from: k  reason: collision with root package name */
    protected t f8797k = null;

    /* renamed from: l  reason: collision with root package name */
    protected String f8798l = null;

    /* JADX INFO: Access modifiers changed from: protected */
    public a(String[] strArr, m mVar, LogRedirectionStrategy logRedirectionStrategy) {
        this.f8788b = mVar;
        this.f8792f = strArr;
        this.f8799m = logRedirectionStrategy;
        FFmpegKitConfig.b(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(t tVar) {
        this.f8797k = tVar;
        this.f8796j = SessionState.COMPLETED;
        this.f8791e = new Date();
    }

    @Override // com.arthenica.ffmpegkit.u
    public long b() {
        Date date = this.f8790d;
        Date date2 = this.f8791e;
        if (date == null || date2 == null) {
            return 0L;
        }
        return date2.getTime() - date.getTime();
    }

    @Override // com.arthenica.ffmpegkit.u
    public String d(int i8) {
        y(i8);
        if (f()) {
            Log.i("ffmpeg-kit", String.format("getAllLogsAsString was called to return all logs but there are still logs being transmitted for session id %d.", Long.valueOf(this.f8787a)));
        }
        return v();
    }

    @Override // com.arthenica.ffmpegkit.u
    public Date e() {
        return this.f8790d;
    }

    @Override // com.arthenica.ffmpegkit.u
    public boolean f() {
        return FFmpegKitConfig.messagesInTransmit(this.f8787a) != 0;
    }

    @Override // com.arthenica.ffmpegkit.u
    public List<l> g(int i8) {
        y(i8);
        if (f()) {
            Log.i("ffmpeg-kit", String.format("getAllLogs was called to return all logs but there are still logs being transmitted for session id %d.", Long.valueOf(this.f8787a)));
        }
        return m();
    }

    @Override // com.arthenica.ffmpegkit.u
    public SessionState getState() {
        return this.f8796j;
    }

    @Override // com.arthenica.ffmpegkit.u
    public String h() {
        return FFmpegKitConfig.c(this.f8792f);
    }

    @Override // com.arthenica.ffmpegkit.u
    public Date i() {
        return this.f8789c;
    }

    @Override // com.arthenica.ffmpegkit.u
    public String j() {
        return this.f8798l;
    }

    @Override // com.arthenica.ffmpegkit.u
    public LogRedirectionStrategy k() {
        return this.f8799m;
    }

    @Override // com.arthenica.ffmpegkit.u
    public Date l() {
        return this.f8791e;
    }

    @Override // com.arthenica.ffmpegkit.u
    public List<l> m() {
        LinkedList linkedList;
        synchronized (this.f8794h) {
            linkedList = new LinkedList(this.f8793g);
        }
        return linkedList;
    }

    @Override // com.arthenica.ffmpegkit.u
    public m n() {
        return this.f8788b;
    }

    @Override // com.arthenica.ffmpegkit.u
    public long o() {
        return this.f8787a;
    }

    @Override // com.arthenica.ffmpegkit.u
    public void q(l lVar) {
        synchronized (this.f8794h) {
            this.f8793g.add(lVar);
        }
    }

    @Override // com.arthenica.ffmpegkit.u
    public t s() {
        return this.f8797k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(Exception exc) {
        this.f8798l = j2.a.a(exc);
        this.f8796j = SessionState.FAILED;
        this.f8791e = new Date();
    }

    public String[] u() {
        return this.f8792f;
    }

    public String v() {
        StringBuilder sb = new StringBuilder();
        synchronized (this.f8794h) {
            for (l lVar : this.f8793g) {
                sb.append(lVar.b());
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(Future<?> future) {
        this.f8795i = future;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x() {
        this.f8796j = SessionState.RUNNING;
        this.f8790d = new Date();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void y(int i8) {
        long currentTimeMillis = System.currentTimeMillis();
        while (f() && System.currentTimeMillis() < i8 + currentTimeMillis) {
            synchronized (this) {
                try {
                    wait(100L);
                } catch (InterruptedException unused) {
                }
            }
        }
    }
}
