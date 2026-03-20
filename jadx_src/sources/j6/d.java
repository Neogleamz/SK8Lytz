package j6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends androidx.fragment.app.c {
    private Dialog F0;
    private DialogInterface.OnCancelListener G0;
    private Dialog H0;

    public static d Z1(Dialog dialog, DialogInterface.OnCancelListener onCancelListener) {
        d dVar = new d();
        Dialog dialog2 = (Dialog) j.m(dialog, "Cannot display null dialog");
        dialog2.setOnCancelListener(null);
        dialog2.setOnDismissListener(null);
        dVar.F0 = dialog2;
        if (onCancelListener != null) {
            dVar.G0 = onCancelListener;
        }
        return dVar;
    }

    @Override // androidx.fragment.app.c
    public Dialog R1(Bundle bundle) {
        Dialog dialog = this.F0;
        if (dialog == null) {
            W1(false);
            if (this.H0 == null) {
                this.H0 = new AlertDialog.Builder((Context) j.l(getContext())).create();
            }
            return this.H0;
        }
        return dialog;
    }

    @Override // androidx.fragment.app.c
    public void Y1(FragmentManager fragmentManager, String str) {
        super.Y1(fragmentManager, str);
    }

    @Override // androidx.fragment.app.c, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        DialogInterface.OnCancelListener onCancelListener = this.G0;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }
}
