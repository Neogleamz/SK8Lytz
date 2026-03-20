package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzfn$zza;
import com.google.android.gms.internal.measurement.zzs;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final /* synthetic */ class y5 {

    /* renamed from: a  reason: collision with root package name */
    static final /* synthetic */ int[] f17175a;

    /* renamed from: b  reason: collision with root package name */
    static final /* synthetic */ int[] f17176b;

    /* renamed from: c  reason: collision with root package name */
    static final /* synthetic */ int[] f17177c;

    static {
        int[] iArr = new int[zzfn$zza.zzd.values().length];
        f17177c = iArr;
        try {
            iArr[zzfn$zza.zzd.DENIED.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            f17177c[zzfn$zza.zzd.GRANTED.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        int[] iArr2 = new int[zzfn$zza.zze.values().length];
        f17176b = iArr2;
        try {
            iArr2[zzfn$zza.zze.AD_STORAGE.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            f17176b[zzfn$zza.zze.ANALYTICS_STORAGE.ordinal()] = 2;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            f17176b[zzfn$zza.zze.AD_USER_DATA.ordinal()] = 3;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            f17176b[zzfn$zza.zze.AD_PERSONALIZATION.ordinal()] = 4;
        } catch (NoSuchFieldError unused6) {
        }
        int[] iArr3 = new int[zzs.values().length];
        f17175a = iArr3;
        try {
            iArr3[zzs.DEBUG.ordinal()] = 1;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            f17175a[zzs.ERROR.ordinal()] = 2;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            f17175a[zzs.WARN.ordinal()] = 3;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            f17175a[zzs.VERBOSE.ordinal()] = 4;
        } catch (NoSuchFieldError unused10) {
        }
    }
}
