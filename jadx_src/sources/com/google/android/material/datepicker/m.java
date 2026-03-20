package com.google.android.material.datepicker;

import androidx.fragment.app.Fragment;
import java.util.LinkedHashSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class m<S> extends Fragment {

    /* renamed from: p0  reason: collision with root package name */
    protected final LinkedHashSet<l<S>> f17883p0 = new LinkedHashSet<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean J1(l<S> lVar) {
        return this.f17883p0.add(lVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void K1() {
        this.f17883p0.clear();
    }
}
