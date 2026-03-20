package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.LegacySavedStateHandleController;
import androidx.lifecycle.Lifecycle;
import androidx.savedstate.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class LegacySavedStateHandleController {

    /* renamed from: a  reason: collision with root package name */
    public static final LegacySavedStateHandleController f5802a = new LegacySavedStateHandleController();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements a.InterfaceC0079a {
        @Override // androidx.savedstate.a.InterfaceC0079a
        public void a(s1.d dVar) {
            kotlin.jvm.internal.p.e(dVar, "owner");
            if (!(dVar instanceof j0)) {
                throw new IllegalStateException("Internal error: OnRecreation should be registered only on components that implement ViewModelStoreOwner".toString());
            }
            i0 viewModelStore = ((j0) dVar).getViewModelStore();
            androidx.savedstate.a savedStateRegistry = dVar.getSavedStateRegistry();
            for (String str : viewModelStore.c()) {
                e0 b9 = viewModelStore.b(str);
                kotlin.jvm.internal.p.b(b9);
                LegacySavedStateHandleController.a(b9, savedStateRegistry, dVar.getLifecycle());
            }
            if (!viewModelStore.c().isEmpty()) {
                savedStateRegistry.i(a.class);
            }
        }
    }

    private LegacySavedStateHandleController() {
    }

    public static final void a(e0 e0Var, androidx.savedstate.a aVar, Lifecycle lifecycle) {
        kotlin.jvm.internal.p.e(e0Var, "viewModel");
        kotlin.jvm.internal.p.e(aVar, "registry");
        kotlin.jvm.internal.p.e(lifecycle, "lifecycle");
        SavedStateHandleController savedStateHandleController = (SavedStateHandleController) e0Var.c("androidx.lifecycle.savedstate.vm.tag");
        if (savedStateHandleController == null || savedStateHandleController.d()) {
            return;
        }
        savedStateHandleController.a(aVar, lifecycle);
        f5802a.c(aVar, lifecycle);
    }

    public static final SavedStateHandleController b(androidx.savedstate.a aVar, Lifecycle lifecycle, String str, Bundle bundle) {
        kotlin.jvm.internal.p.e(aVar, "registry");
        kotlin.jvm.internal.p.e(lifecycle, "lifecycle");
        kotlin.jvm.internal.p.b(str);
        SavedStateHandleController savedStateHandleController = new SavedStateHandleController(str, w.f5921f.a(aVar.b(str), bundle));
        savedStateHandleController.a(aVar, lifecycle);
        f5802a.c(aVar, lifecycle);
        return savedStateHandleController;
    }

    private final void c(final androidx.savedstate.a aVar, final Lifecycle lifecycle) {
        Lifecycle.State b9 = lifecycle.b();
        if (b9 == Lifecycle.State.INITIALIZED || b9.f(Lifecycle.State.STARTED)) {
            aVar.i(a.class);
        } else {
            lifecycle.a(new h() { // from class: androidx.lifecycle.LegacySavedStateHandleController$tryToAddRecreator$1
                @Override // androidx.lifecycle.h
                public void c(j jVar, Lifecycle.Event event) {
                    kotlin.jvm.internal.p.e(jVar, "source");
                    kotlin.jvm.internal.p.e(event, "event");
                    if (event == Lifecycle.Event.ON_START) {
                        Lifecycle.this.c(this);
                        aVar.i(LegacySavedStateHandleController.a.class);
                    }
                }
            });
        }
    }
}
