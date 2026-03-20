package com.google.android.gms.common.moduleinstall.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.moduleinstall.internal.ApiFeatureRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ApiFeatureRequest extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ApiFeatureRequest> CREATOR = new r6.c();

    /* renamed from: e  reason: collision with root package name */
    private static final Comparator f11906e = new Comparator() { // from class: r6.b
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            Feature feature = (Feature) obj;
            Feature feature2 = (Feature) obj2;
            Parcelable.Creator<ApiFeatureRequest> creator = ApiFeatureRequest.CREATOR;
            return !feature.t().equals(feature2.t()) ? feature.t().compareTo(feature2.t()) : (feature.u() > feature2.u() ? 1 : (feature.u() == feature2.u() ? 0 : -1));
        }
    };

    /* renamed from: a  reason: collision with root package name */
    private final List f11907a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f11908b;

    /* renamed from: c  reason: collision with root package name */
    private final String f11909c;

    /* renamed from: d  reason: collision with root package name */
    private final String f11910d;

    public ApiFeatureRequest(List list, boolean z4, String str, String str2) {
        n6.j.l(list);
        this.f11907a = list;
        this.f11908b = z4;
        this.f11909c = str;
        this.f11910d = str2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ApiFeatureRequest Z(List list, boolean z4) {
        TreeSet treeSet = new TreeSet(f11906e);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Collections.addAll(treeSet, ((k6.b) it.next()).a());
        }
        return new ApiFeatureRequest(new ArrayList(treeSet), z4, null, null);
    }

    public static ApiFeatureRequest t(q6.d dVar) {
        return Z(dVar.a(), true);
    }

    public final boolean equals(Object obj) {
        if (obj != null && (obj instanceof ApiFeatureRequest)) {
            ApiFeatureRequest apiFeatureRequest = (ApiFeatureRequest) obj;
            return this.f11908b == apiFeatureRequest.f11908b && n6.i.a(this.f11907a, apiFeatureRequest.f11907a) && n6.i.a(this.f11909c, apiFeatureRequest.f11909c) && n6.i.a(this.f11910d, apiFeatureRequest.f11910d);
        }
        return false;
    }

    public final int hashCode() {
        return n6.i.b(Boolean.valueOf(this.f11908b), this.f11907a, this.f11909c, this.f11910d);
    }

    public List<Feature> u() {
        return this.f11907a;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.v(parcel, 1, u(), false);
        o6.a.c(parcel, 2, this.f11908b);
        o6.a.r(parcel, 3, this.f11909c, false);
        o6.a.r(parcel, 4, this.f11910d, false);
        o6.a.b(parcel, a9);
    }
}
