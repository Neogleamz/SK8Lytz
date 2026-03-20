package com.google.android.gms.cloudmessaging;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.SoftReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class CloudMessagingReceiver extends BroadcastReceiver {

    /* renamed from: a  reason: collision with root package name */
    private static SoftReference f11471a;

    private final int e(Context context, Intent intent) {
        j7.j c9;
        if (intent.getExtras() == null) {
            return 500;
        }
        CloudMessage cloudMessage = new CloudMessage(intent);
        if (TextUtils.isEmpty(cloudMessage.u())) {
            c9 = j7.m.f(null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("google.message_id", cloudMessage.u());
            Integer Z = cloudMessage.Z();
            if (Z != null) {
                bundle.putInt("google.product_id", Z.intValue());
            }
            bundle.putBoolean("supports_message_handled", true);
            c9 = r.b(context).c(2, bundle);
        }
        int b9 = b(context, cloudMessage);
        try {
            j7.m.b(c9, TimeUnit.SECONDS.toMillis(1L), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e8) {
            Log.w("CloudMessagingReceiver", "Message ack failed: ".concat(e8.toString()));
        }
        return b9;
    }

    private final int f(Context context, Intent intent) {
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException unused) {
                Log.e("CloudMessagingReceiver", "Notification pending intent canceled");
            }
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            extras.remove("pending_intent");
        } else {
            extras = new Bundle();
        }
        String action = intent.getAction();
        if (action == "com.google.firebase.messaging.NOTIFICATION_DISMISS" || (action != null && action.equals("com.google.firebase.messaging.NOTIFICATION_DISMISS"))) {
            c(context, extras);
            return -1;
        }
        Log.e("CloudMessagingReceiver", "Unknown notification action");
        return 500;
    }

    protected Executor a() {
        ExecutorService executorService;
        synchronized (CloudMessagingReceiver.class) {
            SoftReference softReference = f11471a;
            executorService = softReference != null ? (ExecutorService) softReference.get() : null;
            if (executorService == null) {
                b7.e.a();
                executorService = Executors.unconfigurableExecutorService(Executors.newCachedThreadPool(new v6.b("firebase-iid-executor")));
                f11471a = new SoftReference(executorService);
            }
        }
        return executorService;
    }

    protected abstract int b(Context context, CloudMessage cloudMessage);

    protected void c(Context context, Bundle bundle) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void d(Intent intent, Context context, boolean z4, BroadcastReceiver.PendingResult pendingResult) {
        try {
            Parcelable parcelableExtra = intent.getParcelableExtra("wrapped_intent");
            Intent intent2 = parcelableExtra instanceof Intent ? (Intent) parcelableExtra : null;
            int f5 = intent2 != null ? f(context, intent2) : e(context, intent);
            if (z4) {
                pendingResult.setResultCode(f5);
            }
        } finally {
            pendingResult.finish();
        }
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            return;
        }
        final boolean isOrderedBroadcast = isOrderedBroadcast();
        final BroadcastReceiver.PendingResult goAsync = goAsync();
        a().execute(new Runnable() { // from class: com.google.android.gms.cloudmessaging.f
            @Override // java.lang.Runnable
            public final void run() {
                CloudMessagingReceiver.this.d(intent, context, isOrderedBroadcast, goAsync);
            }
        });
    }
}
