package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;
import n6.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Status extends AbstractSafeParcelable implements k6.e, ReflectedParcelable {

    /* renamed from: a  reason: collision with root package name */
    private final int f11554a;

    /* renamed from: b  reason: collision with root package name */
    private final String f11555b;

    /* renamed from: c  reason: collision with root package name */
    private final PendingIntent f11556c;

    /* renamed from: d  reason: collision with root package name */
    private final ConnectionResult f11557d;

    /* renamed from: e  reason: collision with root package name */
    public static final Status f11546e = new Status(-1);

    /* renamed from: f  reason: collision with root package name */
    public static final Status f11547f = new Status(0);

    /* renamed from: g  reason: collision with root package name */
    public static final Status f11548g = new Status(14);

    /* renamed from: h  reason: collision with root package name */
    public static final Status f11549h = new Status(8);

    /* renamed from: j  reason: collision with root package name */
    public static final Status f11550j = new Status(15);

    /* renamed from: k  reason: collision with root package name */
    public static final Status f11551k = new Status(16);

    /* renamed from: m  reason: collision with root package name */
    public static final Status f11553m = new Status(17);

    /* renamed from: l  reason: collision with root package name */
    public static final Status f11552l = new Status(18);
    public static final Parcelable.Creator<Status> CREATOR = new e();

    public Status(int i8) {
        this(i8, (String) null);
    }

    public Status(int i8, String str) {
        this(i8, str, (PendingIntent) null);
    }

    public Status(int i8, String str, PendingIntent pendingIntent) {
        this(i8, str, pendingIntent, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Status(int i8, String str, PendingIntent pendingIntent, ConnectionResult connectionResult) {
        this.f11554a = i8;
        this.f11555b = str;
        this.f11556c = pendingIntent;
        this.f11557d = connectionResult;
    }

    public Status(ConnectionResult connectionResult, String str) {
        this(connectionResult, str, 17);
    }

    @Deprecated
    public Status(ConnectionResult connectionResult, String str, int i8) {
        this(i8, str, connectionResult.Z(), connectionResult);
    }

    public boolean D0() {
        return this.f11556c != null;
    }

    public boolean E0() {
        return this.f11554a <= 0;
    }

    public final String I0() {
        String str = this.f11555b;
        return str != null ? str : k6.a.a(this.f11554a);
    }

    public String Z() {
        return this.f11555b;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Status) {
            Status status = (Status) obj;
            return this.f11554a == status.f11554a && i.a(this.f11555b, status.f11555b) && i.a(this.f11556c, status.f11556c) && i.a(this.f11557d, status.f11557d);
        }
        return false;
    }

    public int hashCode() {
        return i.b(Integer.valueOf(this.f11554a), this.f11555b, this.f11556c, this.f11557d);
    }

    @Override // k6.e
    public Status p() {
        return this;
    }

    public ConnectionResult t() {
        return this.f11557d;
    }

    public String toString() {
        i.a c9 = i.c(this);
        c9.a("statusCode", I0());
        c9.a("resolution", this.f11556c);
        return c9.toString();
    }

    @ResultIgnorabilityUnspecified
    public int u() {
        return this.f11554a;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, u());
        o6.a.r(parcel, 2, Z(), false);
        o6.a.q(parcel, 3, this.f11556c, i8, false);
        o6.a.q(parcel, 4, t(), i8, false);
        o6.a.b(parcel, a9);
    }
}
