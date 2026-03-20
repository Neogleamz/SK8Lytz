package androidx.lifecycle;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import androidx.lifecycle.f0;
import java.lang.reflect.Constructor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0 extends f0.d implements f0.b {

    /* renamed from: b  reason: collision with root package name */
    private Application f5843b;

    /* renamed from: c  reason: collision with root package name */
    private final f0.b f5844c;

    /* renamed from: d  reason: collision with root package name */
    private Bundle f5845d;

    /* renamed from: e  reason: collision with root package name */
    private Lifecycle f5846e;

    /* renamed from: f  reason: collision with root package name */
    private androidx.savedstate.a f5847f;

    @SuppressLint({"LambdaLast"})
    public a0(Application application, s1.d dVar, Bundle bundle) {
        kotlin.jvm.internal.p.e(dVar, "owner");
        this.f5847f = dVar.getSavedStateRegistry();
        this.f5846e = dVar.getLifecycle();
        this.f5845d = bundle;
        this.f5843b = application;
        this.f5844c = application != null ? f0.a.f5872f.b(application) : new f0.a();
    }

    @Override // androidx.lifecycle.f0.b
    public <T extends e0> T a(Class<T> cls) {
        kotlin.jvm.internal.p.e(cls, "modelClass");
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            return (T) d(canonicalName, cls);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    @Override // androidx.lifecycle.f0.b
    public <T extends e0> T b(Class<T> cls, f1.a aVar) {
        kotlin.jvm.internal.p.e(cls, "modelClass");
        kotlin.jvm.internal.p.e(aVar, "extras");
        String str = (String) aVar.a(f0.c.f5881d);
        if (str != null) {
            if (aVar.a(x.f5928a) == null || aVar.a(x.f5929b) == null) {
                if (this.f5846e != null) {
                    return (T) d(str, cls);
                }
                throw new IllegalStateException("SAVED_STATE_REGISTRY_OWNER_KEY andVIEW_MODEL_STORE_OWNER_KEY must be provided in the creation extras tosuccessfully create a ViewModel.");
            }
            Application application = (Application) aVar.a(f0.a.f5874h);
            boolean isAssignableFrom = a.class.isAssignableFrom(cls);
            Constructor c9 = b0.c(cls, (!isAssignableFrom || application == null) ? b0.f5856b : b0.f5855a);
            return c9 == null ? (T) this.f5844c.b(cls, aVar) : (!isAssignableFrom || application == null) ? (T) b0.d(cls, c9, x.a(aVar)) : (T) b0.d(cls, c9, application, x.a(aVar));
        }
        throw new IllegalStateException("VIEW_MODEL_KEY must always be provided by ViewModelProvider");
    }

    @Override // androidx.lifecycle.f0.d
    public void c(e0 e0Var) {
        kotlin.jvm.internal.p.e(e0Var, "viewModel");
        if (this.f5846e != null) {
            androidx.savedstate.a aVar = this.f5847f;
            kotlin.jvm.internal.p.b(aVar);
            Lifecycle lifecycle = this.f5846e;
            kotlin.jvm.internal.p.b(lifecycle);
            LegacySavedStateHandleController.a(e0Var, aVar, lifecycle);
        }
    }

    public final <T extends e0> T d(String str, Class<T> cls) {
        T t8;
        Application application;
        kotlin.jvm.internal.p.e(str, "key");
        kotlin.jvm.internal.p.e(cls, "modelClass");
        Lifecycle lifecycle = this.f5846e;
        if (lifecycle != null) {
            boolean isAssignableFrom = a.class.isAssignableFrom(cls);
            Constructor c9 = b0.c(cls, (!isAssignableFrom || this.f5843b == null) ? b0.f5856b : b0.f5855a);
            if (c9 == null) {
                return this.f5843b != null ? (T) this.f5844c.a(cls) : (T) f0.c.f5879b.a().a(cls);
            }
            androidx.savedstate.a aVar = this.f5847f;
            kotlin.jvm.internal.p.b(aVar);
            SavedStateHandleController b9 = LegacySavedStateHandleController.b(aVar, lifecycle, str, this.f5845d);
            if (!isAssignableFrom || (application = this.f5843b) == null) {
                t8 = (T) b0.d(cls, c9, b9.b());
            } else {
                kotlin.jvm.internal.p.b(application);
                t8 = (T) b0.d(cls, c9, application, b9.b());
            }
            t8.e("androidx.lifecycle.savedstate.vm.tag", b9);
            return t8;
        }
        throw new UnsupportedOperationException("SavedStateViewModelFactory constructed with empty constructor supports only calls to create(modelClass: Class<T>, extras: CreationExtras).");
    }
}
