package com.google.android.material.datepicker;

import android.content.Context;
import android.util.DisplayMetrics;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class n extends LinearLayoutManager {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends androidx.recyclerview.widget.p {
        a(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.p
        protected float v(DisplayMetrics displayMetrics) {
            return 100.0f / displayMetrics.densityDpi;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(Context context, int i8, boolean z4) {
        super(context, i8, z4);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public void J1(RecyclerView recyclerView, RecyclerView.y yVar, int i8) {
        a aVar = new a(recyclerView.getContext());
        aVar.p(i8);
        K1(aVar);
    }
}
