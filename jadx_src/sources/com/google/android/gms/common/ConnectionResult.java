package com.google.android.gms.common;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import n6.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ConnectionResult extends AbstractSafeParcelable {

    /* renamed from: a  reason: collision with root package name */
    final int f11524a;

    /* renamed from: b  reason: collision with root package name */
    private final int f11525b;

    /* renamed from: c  reason: collision with root package name */
    private final PendingIntent f11526c;

    /* renamed from: d  reason: collision with root package name */
    private final String f11527d;

    /* renamed from: e  reason: collision with root package name */
    public static final ConnectionResult f11523e = new ConnectionResult(0);
    public static final Parcelable.Creator<ConnectionResult> CREATOR = new g();

    public ConnectionResult(int i8) {
        this(i8, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConnectionResult(int i8, int i9, PendingIntent pendingIntent, String str) {
        this.f11524a = i8;
        this.f11525b = i9;
        this.f11526c = pendingIntent;
        this.f11527d = str;
    }

    public ConnectionResult(int i8, PendingIntent pendingIntent) {
        this(i8, pendingIntent, null);
    }

    public ConnectionResult(int i8, PendingIntent pendingIntent, String str) {
        this(1, i8, pendingIntent, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String I0(int i8) {
        if (i8 != 99) {
            if (i8 != 1500) {
                switch (i8) {
                    case -1:
                        return "UNKNOWN";
                    case 0:
                        return "SUCCESS";
                    case 1:
                        return "SERVICE_MISSING";
                    case 2:
                        return "SERVICE_VERSION_UPDATE_REQUIRED";
                    case 3:
                        return "SERVICE_DISABLED";
                    case 4:
                        return "SIGN_IN_REQUIRED";
                    case 5:
                        return "INVALID_ACCOUNT";
                    case 6:
                        return "RESOLUTION_REQUIRED";
                    case 7:
                        return "NETWORK_ERROR";
                    case 8:
                        return "INTERNAL_ERROR";
                    case 9:
                        return "SERVICE_INVALID";
                    case 10:
                        return "DEVELOPER_ERROR";
                    case 11:
                        return "LICENSE_CHECK_FAILED";
                    default:
                        switch (i8) {
                            case 13:
                                return "CANCELED";
                            case 14:
                                return "TIMEOUT";
                            case 15:
                                return "INTERRUPTED";
                            case 16:
                                return "API_UNAVAILABLE";
                            case 17:
                                return "SIGN_IN_FAILED";
                            case 18:
                                return "SERVICE_UPDATING";
                            case 19:
                                return "SERVICE_MISSING_PERMISSION";
                            case 20:
                                return "RESTRICTED_PROFILE";
                            case 21:
                                return "API_VERSION_UPDATE_REQUIRED";
                            case 22:
                                return "RESOLUTION_ACTIVITY_NOT_FOUND";
                            case 23:
                                return "API_DISABLED";
                            case 24:
                                return "API_DISABLED_FOR_CONNECTION";
                            default:
                                return "UNKNOWN_ERROR_CODE(" + i8 + ")";
                        }
                }
            }
            return "DRIVE_EXTERNAL_STORAGE_REQUIRED";
        }
        return "UNFINISHED";
    }

    public boolean D0() {
        return (this.f11525b == 0 || this.f11526c == null) ? false : true;
    }

    public boolean E0() {
        return this.f11525b == 0;
    }

    public PendingIntent Z() {
        return this.f11526c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ConnectionResult) {
            ConnectionResult connectionResult = (ConnectionResult) obj;
            return this.f11525b == connectionResult.f11525b && n6.i.a(this.f11526c, connectionResult.f11526c) && n6.i.a(this.f11527d, connectionResult.f11527d);
        }
        return false;
    }

    public int hashCode() {
        return n6.i.b(Integer.valueOf(this.f11525b), this.f11526c, this.f11527d);
    }

    public int t() {
        return this.f11525b;
    }

    public String toString() {
        i.a c9 = n6.i.c(this);
        c9.a("statusCode", I0(this.f11525b));
        c9.a("resolution", this.f11526c);
        c9.a("message", this.f11527d);
        return c9.toString();
    }

    public String u() {
        return this.f11527d;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int i9 = this.f11524a;
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, i9);
        o6.a.l(parcel, 2, t());
        o6.a.q(parcel, 3, Z(), i8, false);
        o6.a.r(parcel, 4, u(), false);
        o6.a.b(parcel, a9);
    }
}
