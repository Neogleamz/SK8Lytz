package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.HandlerThread;
import java.util.concurrent.Executor;
import n6.k0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class d {

    /* renamed from: a  reason: collision with root package name */
    private static final Object f11844a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private static a0 f11845b = null;

    /* renamed from: c  reason: collision with root package name */
    static HandlerThread f11846c = null;

    /* renamed from: d  reason: collision with root package name */
    private static Executor f11847d = null;

    /* renamed from: e  reason: collision with root package name */
    private static boolean f11848e = false;

    public static int a() {
        return 4225;
    }

    public static d b(Context context) {
        synchronized (f11844a) {
            if (f11845b == null) {
                f11845b = new a0(context.getApplicationContext(), f11848e ? c().getLooper() : context.getMainLooper(), f11847d);
            }
        }
        return f11845b;
    }

    public static HandlerThread c() {
        synchronized (f11844a) {
            HandlerThread handlerThread = f11846c;
            if (handlerThread != null) {
                return handlerThread;
            }
            HandlerThread handlerThread2 = new HandlerThread("GoogleApiHandler", 9);
            f11846c = handlerThread2;
            handlerThread2.start();
            return f11846c;
        }
    }

    protected abstract void d(k0 k0Var, ServiceConnection serviceConnection, String str);

    public final void e(String str, String str2, int i8, ServiceConnection serviceConnection, String str3, boolean z4) {
        d(new k0(str, str2, 4225, z4), serviceConnection, str3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean f(k0 k0Var, ServiceConnection serviceConnection, String str, Executor executor);
}
