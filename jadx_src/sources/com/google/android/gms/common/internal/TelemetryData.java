package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TelemetryData extends AbstractSafeParcelable {
    public static final Parcelable.Creator<TelemetryData> CREATOR = new n6.q();

    /* renamed from: a  reason: collision with root package name */
    private final int f11811a;

    /* renamed from: b  reason: collision with root package name */
    private List f11812b;

    public TelemetryData(int i8, List list) {
        this.f11811a = i8;
        this.f11812b = list;
    }

    public final void Z(MethodInvocation methodInvocation) {
        if (this.f11812b == null) {
            this.f11812b = new ArrayList();
        }
        this.f11812b.add(methodInvocation);
    }

    public final int t() {
        return this.f11811a;
    }

    public final List u() {
        return this.f11812b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11811a);
        o6.a.v(parcel, 2, this.f11812b, false);
        o6.a.b(parcel, a9);
    }
}
