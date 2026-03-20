package androidx.lifecycle;

import android.content.Context;
import androidx.lifecycle.t;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ProcessLifecycleInitializer implements v1.a<j> {
    @Override // v1.a
    public List<Class<? extends v1.a<?>>> a() {
        return kotlin.collections.q.f();
    }

    @Override // v1.a
    /* renamed from: c */
    public j b(Context context) {
        kotlin.jvm.internal.p.e(context, "context");
        androidx.startup.a e8 = androidx.startup.a.e(context);
        kotlin.jvm.internal.p.d(e8, "getInstance(context)");
        if (e8.g(ProcessLifecycleInitializer.class)) {
            g.a(context);
            t.b bVar = t.f5906j;
            bVar.b(context);
            return bVar.a();
        }
        throw new IllegalStateException("ProcessLifecycleInitializer cannot be initialized lazily.\n               Please ensure that you have:\n               <meta-data\n                   android:name='androidx.lifecycle.ProcessLifecycleInitializer'\n                   android:value='androidx.startup' />\n               under InitializationProvider in your AndroidManifest.xml".toString());
    }
}
