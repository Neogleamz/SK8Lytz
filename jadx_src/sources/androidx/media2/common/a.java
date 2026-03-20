package androidx.media2.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {
    public static PendingIntent a(Context context, int i8, Intent intent, int i9) {
        return PendingIntent.getForegroundService(context, i8, intent, i9);
    }
}
