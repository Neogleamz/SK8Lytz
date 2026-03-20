package com.example.seedpoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.example.seedpoint.utils.HttpClient;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkChangeReceiver.class.getName();
    private androidx.core.util.a<Boolean> consumer;
    private AtomicLong statusVersion = new AtomicLong(0);

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onReceive$0(long j8, Boolean bool) {
        synchronized (this) {
            if (j8 == this.statusVersion.get()) {
                this.consumer.accept(bool);
            } else {
                String str = TAG;
                Log.i(str, "version expired. " + j8);
            }
        }
    }

    public void callback(androidx.core.util.a<Boolean> aVar) {
        this.consumer = aVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        synchronized (this) {
            String action = intent.getAction();
            String str = TAG;
            Log.i(str, "action " + action);
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action) && this.consumer != null) {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                final long incrementAndGet = this.statusVersion.incrementAndGet();
                if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                    this.consumer.accept(Boolean.FALSE);
                } else {
                    HttpClient.getInstance().ping(new androidx.core.util.a() { // from class: com.example.seedpoint.b
                        @Override // androidx.core.util.a
                        public final void accept(Object obj) {
                            NetworkChangeReceiver.this.lambda$onReceive$0(incrementAndGet, (Boolean) obj);
                        }
                    });
                }
            }
        }
    }
}
