package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r8 implements Application.ActivityLifecycleCallbacks {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ h7 f16950a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r8(h7 h7Var) {
        this.f16950a = h7Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00c5 A[Catch: RuntimeException -> 0x01d2, TRY_ENTER, TryCatch #0 {RuntimeException -> 0x01d2, blocks: (B:3:0x000b, B:5:0x0018, B:9:0x0029, B:11:0x002f, B:15:0x0040, B:45:0x00c5, B:47:0x00d1, B:51:0x00e2, B:53:0x00e8, B:57:0x00fd, B:59:0x0103, B:62:0x0110, B:64:0x0116, B:66:0x012e, B:68:0x013d, B:71:0x0144, B:75:0x0167, B:77:0x0183, B:76:0x0174, B:79:0x018a, B:81:0x0190, B:83:0x0196, B:85:0x019c, B:87:0x01a2, B:89:0x01aa, B:94:0x01b8, B:96:0x01c6, B:98:0x01cc, B:19:0x0054, B:22:0x005c, B:24:0x0064, B:26:0x006a, B:28:0x0070, B:30:0x0076, B:32:0x007e, B:34:0x0086, B:37:0x0090, B:39:0x0098, B:40:0x00a4, B:42:0x00bc), top: B:103:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0143 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0144 A[Catch: RuntimeException -> 0x01d2, TRY_LEAVE, TryCatch #0 {RuntimeException -> 0x01d2, blocks: (B:3:0x000b, B:5:0x0018, B:9:0x0029, B:11:0x002f, B:15:0x0040, B:45:0x00c5, B:47:0x00d1, B:51:0x00e2, B:53:0x00e8, B:57:0x00fd, B:59:0x0103, B:62:0x0110, B:64:0x0116, B:66:0x012e, B:68:0x013d, B:71:0x0144, B:75:0x0167, B:77:0x0183, B:76:0x0174, B:79:0x018a, B:81:0x0190, B:83:0x0196, B:85:0x019c, B:87:0x01a2, B:89:0x01aa, B:94:0x01b8, B:96:0x01c6, B:98:0x01cc, B:19:0x0054, B:22:0x005c, B:24:0x0064, B:26:0x006a, B:28:0x0070, B:30:0x0076, B:32:0x007e, B:34:0x0086, B:37:0x0090, B:39:0x0098, B:40:0x00a4, B:42:0x00bc), top: B:103:0x000b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static /* synthetic */ void a(com.google.android.gms.measurement.internal.r8 r17, boolean r18, android.net.Uri r19, java.lang.String r20, java.lang.String r21) {
        /*
            Method dump skipped, instructions count: 483
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.r8.a(com.google.android.gms.measurement.internal.r8, boolean, android.net.Uri, java.lang.String, java.lang.String):void");
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        try {
            try {
                this.f16950a.i().I().a("onActivityCreated");
                Intent intent = activity.getIntent();
                if (intent != null) {
                    Uri data = intent.getData();
                    if (data == null || !data.isHierarchical()) {
                        Bundle extras = intent.getExtras();
                        if (extras != null) {
                            String string = extras.getString("com.android.vending.referral_url");
                            if (!TextUtils.isEmpty(string)) {
                                data = Uri.parse(string);
                            }
                        }
                        data = null;
                    }
                    Uri uri = data;
                    if (uri != null && uri.isHierarchical()) {
                        this.f16950a.g();
                        this.f16950a.l().B(new q8(this, bundle == null, uri, sb.d0(intent) ? "gs" : "auto", uri.getQueryParameter("referrer")));
                    }
                }
            } catch (RuntimeException e8) {
                this.f16950a.i().E().b("Throwable caught in onActivityCreated", e8);
            }
        } finally {
            this.f16950a.q().D(activity, bundle);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityDestroyed(Activity activity) {
        this.f16950a.q().C(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityPaused(Activity activity) {
        this.f16950a.q().O(activity);
        na s8 = this.f16950a.s();
        s8.l().B(new oa(s8, s8.zzb().b()));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityResumed(Activity activity) {
        na s8 = this.f16950a.s();
        s8.l().B(new pa(s8, s8.zzb().b()));
        this.f16950a.q().Q(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.f16950a.q().P(activity, bundle);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStopped(Activity activity) {
    }
}
