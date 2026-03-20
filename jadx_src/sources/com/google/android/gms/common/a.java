package com.google.android.gms.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;
import androidx.core.app.k;
import androidx.fragment.app.FragmentActivity;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.internal.zabx;
import com.google.errorprone.annotations.RestrictedInheritance;
@RestrictedInheritance(allowedOnPath = ".*java.*/com/google/android/gms.*", allowlistAnnotations = {a7.d.class, a7.e.class}, explanation = "Sub classing of GMS Core's APIs are restricted to GMS Core client libs and testing fakes.", link = "go/gmscore-restrictedinheritance")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends b {

    /* renamed from: c  reason: collision with root package name */
    private String f11540c;

    /* renamed from: e  reason: collision with root package name */
    private static final Object f11538e = new Object();

    /* renamed from: f  reason: collision with root package name */
    private static final a f11539f = new a();

    /* renamed from: d  reason: collision with root package name */
    public static final int f11537d = b.f11718a;

    public static a m() {
        return f11539f;
    }

    @Override // com.google.android.gms.common.b
    public Intent b(Context context, int i8, String str) {
        return super.b(context, i8, str);
    }

    @Override // com.google.android.gms.common.b
    public PendingIntent c(Context context, int i8, int i9) {
        return super.c(context, i8, i9);
    }

    @Override // com.google.android.gms.common.b
    public final String e(int i8) {
        return super.e(i8);
    }

    @Override // com.google.android.gms.common.b
    public int g(Context context) {
        return super.g(context);
    }

    @Override // com.google.android.gms.common.b
    public int h(Context context, int i8) {
        return super.h(context, i8);
    }

    @Override // com.google.android.gms.common.b
    public final boolean j(int i8) {
        return super.j(i8);
    }

    public Dialog k(Activity activity, int i8, int i9, DialogInterface.OnCancelListener onCancelListener) {
        return p(activity, i8, n6.x.b(activity, b(activity, i8, "d"), i9), onCancelListener);
    }

    public PendingIntent l(Context context, ConnectionResult connectionResult) {
        return connectionResult.D0() ? connectionResult.Z() : c(context, connectionResult.t(), 0);
    }

    public boolean n(Activity activity, int i8, int i9, DialogInterface.OnCancelListener onCancelListener) {
        Dialog k8 = k(activity, i8, i9, onCancelListener);
        if (k8 == null) {
            return false;
        }
        s(activity, k8, "GooglePlayServicesErrorDialog", onCancelListener);
        return true;
    }

    public void o(Context context, int i8) {
        t(context, i8, null, d(context, i8, 0, "n"));
    }

    final Dialog p(Context context, int i8, n6.x xVar, DialogInterface.OnCancelListener onCancelListener) {
        if (i8 == 0) {
            return null;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843529, typedValue, true);
        AlertDialog.Builder builder = "Theme.Dialog.Alert".equals(context.getResources().getResourceEntryName(typedValue.resourceId)) ? new AlertDialog.Builder(context, 5) : null;
        if (builder == null) {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage(n6.u.d(context, i8));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        String c9 = n6.u.c(context, i8);
        if (c9 != null) {
            builder.setPositiveButton(c9, xVar);
        }
        String g8 = n6.u.g(context, i8);
        if (g8 != null) {
            builder.setTitle(g8);
        }
        Log.w("GoogleApiAvailability", String.format("Creating dialog for Google Play services availability issue. ConnectionResult=%s", Integer.valueOf(i8)), new IllegalArgumentException());
        return builder.create();
    }

    public final Dialog q(Activity activity, DialogInterface.OnCancelListener onCancelListener) {
        ProgressBar progressBar = new ProgressBar(activity, null, 16842874);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(progressBar);
        builder.setMessage(n6.u.d(activity, 18));
        builder.setPositiveButton(BuildConfig.FLAVOR, (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        s(activity, create, "GooglePlayServicesUpdatingDialog", onCancelListener);
        return create;
    }

    public final zabx r(Context context, l6.p pVar) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        zabx zabxVar = new zabx(pVar);
        a7.i.n(context, zabxVar, intentFilter);
        zabxVar.a(context);
        if (i(context, "com.google.android.gms")) {
            return zabxVar;
        }
        pVar.a();
        zabxVar.b();
        return null;
    }

    final void s(Activity activity, Dialog dialog, String str, DialogInterface.OnCancelListener onCancelListener) {
        try {
            if (activity instanceof FragmentActivity) {
                j6.d.Z1(dialog, onCancelListener).Y1(((FragmentActivity) activity).getSupportFragmentManager(), str);
                return;
            }
        } catch (NoClassDefFoundError unused) {
        }
        j6.b.a(dialog, onCancelListener).show(activity.getFragmentManager(), str);
    }

    @TargetApi(20)
    final void t(Context context, int i8, String str, PendingIntent pendingIntent) {
        int i9;
        String str2;
        Log.w("GoogleApiAvailability", String.format("GMS core API Availability. ConnectionResult=%s, tag=%s", Integer.valueOf(i8), null), new IllegalArgumentException());
        if (i8 == 18) {
            u(context);
        } else if (pendingIntent == null) {
            if (i8 == 6) {
                Log.w("GoogleApiAvailability", "Missing resolution for ConnectionResult.RESOLUTION_REQUIRED. Call GoogleApiAvailability#showErrorNotification(Context, ConnectionResult) instead.");
            }
        } else {
            String f5 = n6.u.f(context, i8);
            String e8 = n6.u.e(context, i8);
            Resources resources = context.getResources();
            NotificationManager notificationManager = (NotificationManager) n6.j.l(context.getSystemService("notification"));
            k.e P = new k.e(context).E(true).m(true).u(f5).P(new k.c().x(e8));
            if (u6.h.c(context)) {
                n6.j.p(u6.m.e());
                P.N(context.getApplicationInfo().icon).I(2);
                if (u6.h.d(context)) {
                    P.a(h6.b.f20322a, resources.getString(h6.c.f20341o), pendingIntent);
                } else {
                    P.s(pendingIntent);
                }
            } else {
                P.N(17301642).R(resources.getString(h6.c.f20334h)).W(System.currentTimeMillis()).s(pendingIntent).t(e8);
            }
            if (u6.m.h()) {
                n6.j.p(u6.m.h());
                synchronized (f11538e) {
                    str2 = this.f11540c;
                }
                if (str2 == null) {
                    str2 = "com.google.android.gms.availability";
                    NotificationChannel notificationChannel = notificationManager.getNotificationChannel("com.google.android.gms.availability");
                    String b9 = n6.u.b(context);
                    if (notificationChannel == null) {
                        notificationChannel = new NotificationChannel("com.google.android.gms.availability", b9, 4);
                    } else if (!b9.contentEquals(notificationChannel.getName())) {
                        notificationChannel.setName(b9);
                    }
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                P.o(str2);
            }
            Notification c9 = P.c();
            if (i8 == 1 || i8 == 2 || i8 == 3) {
                d.f11722b.set(false);
                i9 = 10436;
            } else {
                i9 = 39789;
            }
            notificationManager.notify(i9, c9);
        }
    }

    final void u(Context context) {
        new f(this, context).sendEmptyMessageDelayed(1, 120000L);
    }

    public final boolean v(Activity activity, l6.f fVar, int i8, int i9, DialogInterface.OnCancelListener onCancelListener) {
        Dialog p8 = p(activity, i8, n6.x.c(fVar, b(activity, i8, "d"), 2), onCancelListener);
        if (p8 == null) {
            return false;
        }
        s(activity, p8, "GooglePlayServicesErrorDialog", onCancelListener);
        return true;
    }

    public final boolean w(Context context, ConnectionResult connectionResult, int i8) {
        PendingIntent l8;
        if (w6.a.a(context) || (l8 = l(context, connectionResult)) == null) {
            return false;
        }
        t(context, connectionResult.t(), null, PendingIntent.getActivity(context, 0, GoogleApiActivity.a(context, l8, i8, true), a7.j.f198a | 134217728));
        return true;
    }
}
