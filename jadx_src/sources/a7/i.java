package a7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i extends androidx.core.content.a {
    @Deprecated
    public static Intent n(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        if (h.a()) {
            return context.registerReceiver(broadcastReceiver, intentFilter, true != h.a() ? 0 : 2);
        }
        return context.registerReceiver(broadcastReceiver, intentFilter);
    }
}
