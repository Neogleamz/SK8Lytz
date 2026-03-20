package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g {

    /* renamed from: a  reason: collision with root package name */
    public static final g f5883a = new g();

    /* renamed from: b  reason: collision with root package name */
    private static final AtomicBoolean f5884b = new AtomicBoolean(false);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends c {
        @Override // androidx.lifecycle.c, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
            kotlin.jvm.internal.p.e(activity, "activity");
            u.f5918b.c(activity);
        }
    }

    private g() {
    }

    public static final void a(Context context) {
        kotlin.jvm.internal.p.e(context, "context");
        if (f5884b.getAndSet(true)) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        kotlin.jvm.internal.p.c(applicationContext, "null cannot be cast to non-null type android.app.Application");
        ((Application) applicationContext).registerActivityLifecycleCallbacks(new a());
    }
}
