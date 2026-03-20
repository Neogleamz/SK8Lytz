package androidx.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.window.OnBackInvokedDispatcher;
import androidx.lifecycle.Lifecycle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends Dialog implements androidx.lifecycle.j, n, s1.d {

    /* renamed from: a  reason: collision with root package name */
    private androidx.lifecycle.k f395a;

    /* renamed from: b  reason: collision with root package name */
    private final s1.c f396b;

    /* renamed from: c  reason: collision with root package name */
    private final OnBackPressedDispatcher f397c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(Context context, int i8) {
        super(context, i8);
        kotlin.jvm.internal.p.e(context, "context");
        this.f396b = s1.c.f22767d.a(this);
        this.f397c = new OnBackPressedDispatcher(new Runnable() { // from class: androidx.activity.g
            @Override // java.lang.Runnable
            public final void run() {
                h.c(h.this);
            }
        });
    }

    private final androidx.lifecycle.k b() {
        androidx.lifecycle.k kVar = this.f395a;
        if (kVar == null) {
            androidx.lifecycle.k kVar2 = new androidx.lifecycle.k(this);
            this.f395a = kVar2;
            return kVar2;
        }
        return kVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(h hVar) {
        kotlin.jvm.internal.p.e(hVar, "this$0");
        super.onBackPressed();
    }

    @Override // androidx.lifecycle.j
    public Lifecycle getLifecycle() {
        return b();
    }

    @Override // androidx.activity.n
    public final OnBackPressedDispatcher getOnBackPressedDispatcher() {
        return this.f397c;
    }

    @Override // s1.d
    public androidx.savedstate.a getSavedStateRegistry() {
        return this.f396b.b();
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        this.f397c.e();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 33) {
            OnBackPressedDispatcher onBackPressedDispatcher = this.f397c;
            OnBackInvokedDispatcher onBackInvokedDispatcher = getOnBackInvokedDispatcher();
            kotlin.jvm.internal.p.d(onBackInvokedDispatcher, "onBackInvokedDispatcher");
            onBackPressedDispatcher.f(onBackInvokedDispatcher);
        }
        this.f396b.d(bundle);
        b().h(Lifecycle.Event.ON_CREATE);
    }

    @Override // android.app.Dialog
    public Bundle onSaveInstanceState() {
        Bundle onSaveInstanceState = super.onSaveInstanceState();
        kotlin.jvm.internal.p.d(onSaveInstanceState, "super.onSaveInstanceState()");
        this.f396b.e(onSaveInstanceState);
        return onSaveInstanceState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onStart() {
        super.onStart();
        b().h(Lifecycle.Event.ON_RESUME);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onStop() {
        b().h(Lifecycle.Event.ON_DESTROY);
        this.f395a = null;
        super.onStop();
    }
}
