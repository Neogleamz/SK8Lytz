package androidx.viewpager2.adapter;

import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.h;
import androidx.lifecycle.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class FragmentStateAdapter$5 implements h {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ Handler f7836a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ Runnable f7837b;

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            this.f7836a.removeCallbacks(this.f7837b);
            jVar.getLifecycle().c(this);
        }
    }
}
