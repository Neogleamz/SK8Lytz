package androidx.appcompat.app;

import android.app.Dialog;
import android.os.Bundle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends androidx.fragment.app.c {
    @Override // androidx.fragment.app.c
    public Dialog R1(Bundle bundle) {
        return new i(getContext(), Q1());
    }

    @Override // androidx.fragment.app.c
    public void X1(Dialog dialog, int i8) {
        if (!(dialog instanceof i)) {
            super.X1(dialog, i8);
            return;
        }
        i iVar = (i) dialog;
        if (i8 != 1 && i8 != 2) {
            if (i8 != 3) {
                return;
            }
            dialog.getWindow().addFlags(24);
        }
        iVar.g(1);
    }
}
