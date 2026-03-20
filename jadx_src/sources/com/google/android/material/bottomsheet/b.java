package com.google.android.material.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.j;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends j {
    private boolean F0;

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: com.google.android.material.bottomsheet.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0128b extends BottomSheetBehavior.g {
        private C0128b() {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.g
        public void a(View view, float f5) {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.g
        public void b(View view, int i8) {
            if (i8 == 5) {
                b.this.a2();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a2() {
        if (this.F0) {
            super.N1();
        } else {
            super.M1();
        }
    }

    private void b2(BottomSheetBehavior<?> bottomSheetBehavior, boolean z4) {
        this.F0 = z4;
        if (bottomSheetBehavior.f0() == 5) {
            a2();
            return;
        }
        if (P1() instanceof com.google.android.material.bottomsheet.a) {
            ((com.google.android.material.bottomsheet.a) P1()).o();
        }
        bottomSheetBehavior.S(new C0128b());
        bottomSheetBehavior.y0(5);
    }

    private boolean c2(boolean z4) {
        Dialog P1 = P1();
        if (P1 instanceof com.google.android.material.bottomsheet.a) {
            com.google.android.material.bottomsheet.a aVar = (com.google.android.material.bottomsheet.a) P1;
            BottomSheetBehavior<FrameLayout> m8 = aVar.m();
            if (m8.i0() && aVar.n()) {
                b2(m8, z4);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // androidx.fragment.app.c
    public void M1() {
        if (c2(false)) {
            return;
        }
        super.M1();
    }

    @Override // androidx.appcompat.app.j, androidx.fragment.app.c
    public Dialog R1(Bundle bundle) {
        return new com.google.android.material.bottomsheet.a(getContext(), Q1());
    }
}
