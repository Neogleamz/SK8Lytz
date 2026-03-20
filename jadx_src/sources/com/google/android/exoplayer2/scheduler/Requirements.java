package com.google.android.exoplayer2.scheduler;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Requirements implements Parcelable {
    public static final Parcelable.Creator<Requirements> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final int f10239a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<Requirements> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public Requirements createFromParcel(Parcel parcel) {
            return new Requirements(parcel.readInt());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public Requirements[] newArray(int i8) {
            return new Requirements[i8];
        }
    }

    public Requirements(int i8) {
        this.f10239a = (i8 & 2) != 0 ? i8 | 1 : i8;
    }

    private int a(Context context) {
        if (h()) {
            ConnectivityManager connectivityManager = (ConnectivityManager) b6.a.e(context.getSystemService("connectivity"));
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return (activeNetworkInfo != null && activeNetworkInfo.isConnected() && g(connectivityManager)) ? (k() && connectivityManager.isActiveNetworkMetered()) ? 2 : 0 : this.f10239a & 3;
        }
        return 0;
    }

    private boolean d(Context context) {
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null) {
            return false;
        }
        int intExtra = registerReceiver.getIntExtra("status", -1);
        return intExtra == 2 || intExtra == 5;
    }

    private boolean e(Context context) {
        PowerManager powerManager = (PowerManager) b6.a.e(context.getSystemService("power"));
        int i8 = l0.f8063a;
        if (i8 >= 23) {
            return powerManager.isDeviceIdleMode();
        }
        if (i8 >= 20) {
            if (!powerManager.isInteractive()) {
                return true;
            }
        } else if (!powerManager.isScreenOn()) {
            return true;
        }
        return false;
    }

    private static boolean g(ConnectivityManager connectivityManager) {
        if (l0.f8063a < 24) {
            return true;
        }
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            return false;
        }
        try {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities != null) {
                if (networkCapabilities.hasCapability(16)) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException unused) {
            return true;
        }
    }

    private boolean i(Context context) {
        return context.registerReceiver(null, new IntentFilter("android.intent.action.DEVICE_STORAGE_LOW")) == null;
    }

    public int b(Context context) {
        int a9 = a(context);
        if (c() && !d(context)) {
            a9 |= 8;
        }
        if (f() && !e(context)) {
            a9 |= 4;
        }
        return (!j() || i(context)) ? a9 : a9 | 16;
    }

    public boolean c() {
        return (this.f10239a & 8) != 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && Requirements.class == obj.getClass() && this.f10239a == ((Requirements) obj).f10239a;
    }

    public boolean f() {
        return (this.f10239a & 4) != 0;
    }

    public boolean h() {
        return (this.f10239a & 1) != 0;
    }

    public int hashCode() {
        return this.f10239a;
    }

    public boolean j() {
        return (this.f10239a & 16) != 0;
    }

    public boolean k() {
        return (this.f10239a & 2) != 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f10239a);
    }
}
