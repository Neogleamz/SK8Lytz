package com.google.android.exoplayer2.offline;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import b6.l0;
import b6.x;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class DownloadService extends Service {

    /* renamed from: k  reason: collision with root package name */
    private static final HashMap<Class<? extends DownloadService>, a> f10197k = new HashMap<>();

    /* renamed from: a  reason: collision with root package name */
    private final String f10198a;

    /* renamed from: b  reason: collision with root package name */
    private final int f10199b;

    /* renamed from: c  reason: collision with root package name */
    private final int f10200c;

    /* renamed from: d  reason: collision with root package name */
    private a f10201d;

    /* renamed from: e  reason: collision with root package name */
    private int f10202e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f10203f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f10204g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f10205h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f10206j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final g5.a f10207a;

        /* renamed from: b  reason: collision with root package name */
        private DownloadService f10208b;

        public void b(DownloadService downloadService) {
            b6.a.f(this.f10208b == null);
            this.f10208b = downloadService;
            throw null;
        }

        public void c(DownloadService downloadService) {
            b6.a.f(this.f10208b == downloadService);
            this.f10208b = null;
        }
    }

    protected abstract g5.a a();

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }

    @Override // android.app.Service
    public void onCreate() {
        String str = this.f10198a;
        if (str != null) {
            x.a(this, str, this.f10199b, this.f10200c, 2);
        }
        a aVar = f10197k.get(getClass());
        if (aVar != null) {
            this.f10201d = aVar;
            aVar.b(this);
            return;
        }
        int i8 = l0.f8063a;
        a();
        throw null;
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.f10206j = true;
        ((a) b6.a.e(this.f10201d)).c(this);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x009a, code lost:
        if (r2.equals("com.google.android.exoplayer.downloadService.action.RESUME_DOWNLOADS") == false) goto L14;
     */
    /* JADX WARN: Removed duplicated region for block: B:78:0x010c  */
    @Override // android.app.Service
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onStartCommand(android.content.Intent r8, int r9, int r10) {
        /*
            Method dump skipped, instructions count: 338
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.offline.DownloadService.onStartCommand(android.content.Intent, int, int):int");
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent intent) {
        this.f10204g = true;
    }
}
