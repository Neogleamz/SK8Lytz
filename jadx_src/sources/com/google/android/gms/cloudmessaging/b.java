package com.google.android.gms.cloudmessaging;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import j7.j;
import j7.k;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: h  reason: collision with root package name */
    private static int f11473h;

    /* renamed from: i  reason: collision with root package name */
    private static PendingIntent f11474i;

    /* renamed from: j  reason: collision with root package name */
    private static final Executor f11475j = new Executor() { // from class: i6.i
        @Override // java.util.concurrent.Executor
        public final void execute(Runnable runnable) {
            runnable.run();
        }
    };

    /* renamed from: k  reason: collision with root package name */
    private static final Pattern f11476k = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)");

    /* renamed from: b  reason: collision with root package name */
    private final Context f11478b;

    /* renamed from: c  reason: collision with root package name */
    private final i6.g f11479c;

    /* renamed from: d  reason: collision with root package name */
    private final ScheduledExecutorService f11480d;

    /* renamed from: f  reason: collision with root package name */
    private Messenger f11482f;

    /* renamed from: g  reason: collision with root package name */
    private zze f11483g;

    /* renamed from: a  reason: collision with root package name */
    private final k0.g f11477a = new k0.g();

    /* renamed from: e  reason: collision with root package name */
    private final Messenger f11481e = new Messenger(new d(this, Looper.getMainLooper()));

    public b(Context context) {
        this.f11478b = context;
        this.f11479c = new i6.g(context);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.setKeepAliveTime(60L, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.allowCoreThreadTimeOut(true);
        this.f11480d = scheduledThreadPoolExecutor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ j7.j c(Bundle bundle) {
        return k(bundle) ? j7.m.f(null) : j7.m.f(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void e(b bVar, Message message) {
        if (message != null) {
            Object obj = message.obj;
            if (obj instanceof Intent) {
                Intent intent = (Intent) obj;
                intent.setExtrasClassLoader(new i6.d());
                if (intent.hasExtra("google.messenger")) {
                    Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
                    if (parcelableExtra instanceof zze) {
                        bVar.f11483g = (zze) parcelableExtra;
                    }
                    if (parcelableExtra instanceof Messenger) {
                        bVar.f11482f = (Messenger) parcelableExtra;
                    }
                }
                Intent intent2 = (Intent) message.obj;
                String action = intent2.getAction();
                if (!i6.h.a(action, "com.google.android.c2dm.intent.REGISTRATION")) {
                    if (Log.isLoggable("Rpc", 3)) {
                        Log.d("Rpc", "Unexpected response action: ".concat(String.valueOf(action)));
                        return;
                    }
                    return;
                }
                String stringExtra = intent2.getStringExtra("registration_id");
                if (stringExtra == null) {
                    stringExtra = intent2.getStringExtra("unregistered");
                }
                if (stringExtra != null) {
                    Matcher matcher = f11476k.matcher(stringExtra);
                    if (!matcher.matches()) {
                        if (Log.isLoggable("Rpc", 3)) {
                            Log.d("Rpc", "Unexpected response string: ".concat(stringExtra));
                            return;
                        }
                        return;
                    }
                    String group = matcher.group(1);
                    String group2 = matcher.group(2);
                    if (group != null) {
                        Bundle extras = intent2.getExtras();
                        extras.putString("registration_id", group2);
                        bVar.j(group, extras);
                        return;
                    }
                    return;
                }
                String stringExtra2 = intent2.getStringExtra("error");
                if (stringExtra2 == null) {
                    Log.w("Rpc", "Unexpected response, no error or registration id ".concat(String.valueOf(intent2.getExtras())));
                    return;
                }
                if (Log.isLoggable("Rpc", 3)) {
                    Log.d("Rpc", "Received InstanceID error ".concat(stringExtra2));
                }
                if (!stringExtra2.startsWith("|")) {
                    synchronized (bVar.f11477a) {
                        for (int i8 = 0; i8 < bVar.f11477a.size(); i8++) {
                            bVar.j((String) bVar.f11477a.k(i8), intent2.getExtras());
                        }
                    }
                    return;
                }
                String[] split = stringExtra2.split("\\|");
                if (split.length <= 2 || !i6.h.a(split[1], "ID")) {
                    Log.w("Rpc", "Unexpected structured response ".concat(stringExtra2));
                    return;
                }
                String str = split[2];
                String str2 = split[3];
                if (str2.startsWith(":")) {
                    str2 = str2.substring(1);
                }
                bVar.j(str, intent2.putExtra("error", str2).getExtras());
                return;
            }
        }
        Log.w("Rpc", "Dropping invalid message");
    }

    private final j7.j g(Bundle bundle) {
        final String h8 = h();
        final j7.k kVar = new j7.k();
        synchronized (this.f11477a) {
            this.f11477a.put(h8, kVar);
        }
        Intent intent = new Intent();
        intent.setPackage("com.google.android.gms");
        intent.setAction(this.f11479c.b() == 2 ? "com.google.iid.TOKEN_REQUEST" : "com.google.android.c2dm.intent.REGISTER");
        intent.putExtras(bundle);
        i(this.f11478b, intent);
        intent.putExtra("kid", "|ID|" + h8 + "|");
        if (Log.isLoggable("Rpc", 3)) {
            Log.d("Rpc", "Sending ".concat(String.valueOf(intent.getExtras())));
        }
        intent.putExtra("google.messenger", this.f11481e);
        if (this.f11482f != null || this.f11483g != null) {
            Message obtain = Message.obtain();
            obtain.obj = intent;
            try {
                Messenger messenger = this.f11482f;
                if (messenger != null) {
                    messenger.send(obtain);
                } else {
                    this.f11483g.b(obtain);
                }
            } catch (RemoteException unused) {
                if (Log.isLoggable("Rpc", 3)) {
                    Log.d("Rpc", "Messenger failed, fallback to startService");
                }
            }
            final ScheduledFuture<?> schedule = this.f11480d.schedule(new Runnable() { // from class: i6.b
                @Override // java.lang.Runnable
                public final void run() {
                    if (k.this.d(new IOException("TIMEOUT"))) {
                        Log.w("Rpc", "No response");
                    }
                }
            }, 30L, TimeUnit.SECONDS);
            kVar.a().c(f11475j, new j7.e() { // from class: com.google.android.gms.cloudmessaging.c
                @Override // j7.e
                public final void a(j7.j jVar) {
                    b.this.f(h8, schedule, jVar);
                }
            });
            return kVar.a();
        }
        if (this.f11479c.b() == 2) {
            this.f11478b.sendBroadcast(intent);
        } else {
            this.f11478b.startService(intent);
        }
        final ScheduledFuture schedule2 = this.f11480d.schedule(new Runnable() { // from class: i6.b
            @Override // java.lang.Runnable
            public final void run() {
                if (k.this.d(new IOException("TIMEOUT"))) {
                    Log.w("Rpc", "No response");
                }
            }
        }, 30L, TimeUnit.SECONDS);
        kVar.a().c(f11475j, new j7.e() { // from class: com.google.android.gms.cloudmessaging.c
            @Override // j7.e
            public final void a(j7.j jVar) {
                b.this.f(h8, schedule2, jVar);
            }
        });
        return kVar.a();
    }

    private static synchronized String h() {
        String num;
        synchronized (b.class) {
            int i8 = f11473h;
            f11473h = i8 + 1;
            num = Integer.toString(i8);
        }
        return num;
    }

    private static synchronized void i(Context context, Intent intent) {
        synchronized (b.class) {
            if (f11474i == null) {
                Intent intent2 = new Intent();
                intent2.setPackage("com.google.example.invalidpackage");
                f11474i = PendingIntent.getBroadcast(context, 0, intent2, b7.a.f8162a);
            }
            intent.putExtra("app", f11474i);
        }
    }

    private final void j(String str, Bundle bundle) {
        synchronized (this.f11477a) {
            j7.k kVar = (j7.k) this.f11477a.remove(str);
            if (kVar != null) {
                kVar.c(bundle);
                return;
            }
            Log.w("Rpc", "Missing callback for " + str);
        }
    }

    private static boolean k(Bundle bundle) {
        return bundle != null && bundle.containsKey("google.messenger");
    }

    public j7.j<Void> a(CloudMessage cloudMessage) {
        if (this.f11479c.a() >= 233700000) {
            Bundle bundle = new Bundle();
            bundle.putString("google.message_id", cloudMessage.u());
            Integer Z = cloudMessage.Z();
            if (Z != null) {
                bundle.putInt("google.product_id", Z.intValue());
            }
            return r.b(this.f11478b).c(3, bundle);
        }
        return j7.m.e(new IOException("SERVICE_NOT_AVAILABLE"));
    }

    public j7.j<Bundle> b(final Bundle bundle) {
        return this.f11479c.a() < 12000000 ? this.f11479c.b() != 0 ? g(bundle).j(f11475j, new j7.c() { // from class: com.google.android.gms.cloudmessaging.t
            @Override // j7.c
            public final Object a(j7.j jVar) {
                return b.this.d(bundle, jVar);
            }
        }) : j7.m.e(new IOException("MISSING_INSTANCEID_SERVICE")) : r.b(this.f11478b).d(1, bundle).i(f11475j, new j7.c() { // from class: i6.a
            @Override // j7.c
            public final Object a(j jVar) {
                if (jVar.p()) {
                    return (Bundle) jVar.l();
                }
                if (Log.isLoggable("Rpc", 3)) {
                    Log.d("Rpc", "Error making request: ".concat(String.valueOf(jVar.k())));
                }
                throw new IOException("SERVICE_NOT_AVAILABLE", jVar.k());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ j7.j d(Bundle bundle, j7.j jVar) {
        return (jVar.p() && k((Bundle) jVar.l())) ? g(bundle).r(f11475j, new j7.i() { // from class: com.google.android.gms.cloudmessaging.s
            @Override // j7.i
            public final j7.j a(Object obj) {
                return b.c((Bundle) obj);
            }
        }) : jVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void f(String str, ScheduledFuture scheduledFuture, j7.j jVar) {
        synchronized (this.f11477a) {
            this.f11477a.remove(str);
        }
        scheduledFuture.cancel(false);
    }
}
