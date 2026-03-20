package androidx.lifecycle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LifecycleService extends Service implements j {

    /* renamed from: a  reason: collision with root package name */
    private final c0 f5816a = new c0(this);

    @Override // androidx.lifecycle.j
    public Lifecycle getLifecycle() {
        return this.f5816a.a();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        kotlin.jvm.internal.p.e(intent, "intent");
        this.f5816a.b();
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        this.f5816a.c();
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.f5816a.d();
        super.onDestroy();
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i8) {
        this.f5816a.e();
        super.onStart(intent, i8);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i8, int i9) {
        return super.onStartCommand(intent, i8, i9);
    }
}
