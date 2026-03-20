package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.List;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class WakeLockEvent extends StatsEvent {
    public static final Parcelable.Creator<WakeLockEvent> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final int f11979a;

    /* renamed from: b  reason: collision with root package name */
    private final long f11980b;

    /* renamed from: c  reason: collision with root package name */
    private final int f11981c;

    /* renamed from: d  reason: collision with root package name */
    private final String f11982d;

    /* renamed from: e  reason: collision with root package name */
    private final String f11983e;

    /* renamed from: f  reason: collision with root package name */
    private final String f11984f;

    /* renamed from: g  reason: collision with root package name */
    private final int f11985g;

    /* renamed from: h  reason: collision with root package name */
    private final List f11986h;

    /* renamed from: j  reason: collision with root package name */
    private final String f11987j;

    /* renamed from: k  reason: collision with root package name */
    private final long f11988k;

    /* renamed from: l  reason: collision with root package name */
    private final int f11989l;

    /* renamed from: m  reason: collision with root package name */
    private final String f11990m;

    /* renamed from: n  reason: collision with root package name */
    private final float f11991n;

    /* renamed from: p  reason: collision with root package name */
    private final long f11992p;
    private final boolean q;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WakeLockEvent(int i8, long j8, int i9, String str, int i10, List list, String str2, long j9, int i11, String str3, String str4, float f5, long j10, String str5, boolean z4) {
        this.f11979a = i8;
        this.f11980b = j8;
        this.f11981c = i9;
        this.f11982d = str;
        this.f11983e = str3;
        this.f11984f = str5;
        this.f11985g = i10;
        this.f11986h = list;
        this.f11987j = str2;
        this.f11988k = j9;
        this.f11989l = i11;
        this.f11990m = str4;
        this.f11991n = f5;
        this.f11992p = j10;
        this.q = z4;
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final String Z() {
        List list = this.f11986h;
        String str = BuildConfig.FLAVOR;
        String join = list == null ? BuildConfig.FLAVOR : TextUtils.join(",", list);
        int i8 = this.f11989l;
        String str2 = this.f11983e;
        String str3 = this.f11990m;
        float f5 = this.f11991n;
        String str4 = this.f11984f;
        int i9 = this.f11985g;
        String str5 = this.f11982d;
        boolean z4 = this.q;
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        sb.append(str5);
        sb.append("\t");
        sb.append(i9);
        sb.append("\t");
        sb.append(join);
        sb.append("\t");
        sb.append(i8);
        sb.append("\t");
        if (str2 == null) {
            str2 = BuildConfig.FLAVOR;
        }
        sb.append(str2);
        sb.append("\t");
        if (str3 == null) {
            str3 = BuildConfig.FLAVOR;
        }
        sb.append(str3);
        sb.append("\t");
        sb.append(f5);
        sb.append("\t");
        if (str4 != null) {
            str = str4;
        }
        sb.append(str);
        sb.append("\t");
        sb.append(z4);
        return sb.toString();
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final int t() {
        return this.f11981c;
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final long u() {
        return this.f11980b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11979a);
        o6.a.n(parcel, 2, this.f11980b);
        o6.a.r(parcel, 4, this.f11982d, false);
        o6.a.l(parcel, 5, this.f11985g);
        o6.a.t(parcel, 6, this.f11986h, false);
        o6.a.n(parcel, 8, this.f11988k);
        o6.a.r(parcel, 10, this.f11983e, false);
        o6.a.l(parcel, 11, this.f11981c);
        o6.a.r(parcel, 12, this.f11987j, false);
        o6.a.r(parcel, 13, this.f11990m, false);
        o6.a.l(parcel, 14, this.f11989l);
        o6.a.i(parcel, 15, this.f11991n);
        o6.a.n(parcel, 16, this.f11992p);
        o6.a.r(parcel, 17, this.f11984f, false);
        o6.a.c(parcel, 18, this.q);
        o6.a.b(parcel, a9);
    }
}
