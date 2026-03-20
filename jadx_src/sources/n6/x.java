package n6;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class x implements DialogInterface.OnClickListener {
    public static x b(Activity activity, Intent intent, int i8) {
        return new v(intent, activity, i8);
    }

    public static x c(l6.f fVar, Intent intent, int i8) {
        return new w(intent, fVar, 2);
    }

    protected abstract void a();

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i8) {
        try {
            a();
        } catch (ActivityNotFoundException e8) {
            Log.e("DialogRedirect", true == Build.FINGERPRINT.contains("generic") ? "Failed to start resolution intent. This may occur when resolving Google Play services connection issues on emulators with Google APIs but not Google Play Store." : "Failed to start resolution intent.", e8);
        } finally {
            dialogInterface.dismiss();
        }
    }
}
