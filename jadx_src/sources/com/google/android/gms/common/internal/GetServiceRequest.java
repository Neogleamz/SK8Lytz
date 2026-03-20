package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.e;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class GetServiceRequest extends AbstractSafeParcelable {
    public static final Parcelable.Creator<GetServiceRequest> CREATOR = new x();
    static final Scope[] q = new Scope[0];

    /* renamed from: t  reason: collision with root package name */
    static final Feature[] f11782t = new Feature[0];

    /* renamed from: a  reason: collision with root package name */
    final int f11783a;

    /* renamed from: b  reason: collision with root package name */
    final int f11784b;

    /* renamed from: c  reason: collision with root package name */
    final int f11785c;

    /* renamed from: d  reason: collision with root package name */
    String f11786d;

    /* renamed from: e  reason: collision with root package name */
    IBinder f11787e;

    /* renamed from: f  reason: collision with root package name */
    Scope[] f11788f;

    /* renamed from: g  reason: collision with root package name */
    Bundle f11789g;

    /* renamed from: h  reason: collision with root package name */
    Account f11790h;

    /* renamed from: j  reason: collision with root package name */
    Feature[] f11791j;

    /* renamed from: k  reason: collision with root package name */
    Feature[] f11792k;

    /* renamed from: l  reason: collision with root package name */
    final boolean f11793l;

    /* renamed from: m  reason: collision with root package name */
    final int f11794m;

    /* renamed from: n  reason: collision with root package name */
    boolean f11795n;

    /* renamed from: p  reason: collision with root package name */
    private final String f11796p;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GetServiceRequest(int i8, int i9, int i10, String str, IBinder iBinder, Scope[] scopeArr, Bundle bundle, Account account, Feature[] featureArr, Feature[] featureArr2, boolean z4, int i11, boolean z8, String str2) {
        scopeArr = scopeArr == null ? q : scopeArr;
        bundle = bundle == null ? new Bundle() : bundle;
        featureArr = featureArr == null ? f11782t : featureArr;
        featureArr2 = featureArr2 == null ? f11782t : featureArr2;
        this.f11783a = i8;
        this.f11784b = i9;
        this.f11785c = i10;
        if ("com.google.android.gms".equals(str)) {
            this.f11786d = "com.google.android.gms";
        } else {
            this.f11786d = str;
        }
        if (i8 < 2) {
            this.f11790h = iBinder != null ? a.f(e.a.e(iBinder)) : null;
        } else {
            this.f11787e = iBinder;
            this.f11790h = account;
        }
        this.f11788f = scopeArr;
        this.f11789g = bundle;
        this.f11791j = featureArr;
        this.f11792k = featureArr2;
        this.f11793l = z4;
        this.f11794m = i11;
        this.f11795n = z8;
        this.f11796p = str2;
    }

    public final String t() {
        return this.f11796p;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        x.a(this, parcel, i8);
    }
}
