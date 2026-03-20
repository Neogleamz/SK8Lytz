package com.google.common.cache;
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class RemovalCause {

    /* renamed from: a  reason: collision with root package name */
    public static final RemovalCause f18859a = new a("EXPLICIT", 0);

    /* renamed from: b  reason: collision with root package name */
    public static final RemovalCause f18860b = new RemovalCause("REPLACED", 1) { // from class: com.google.common.cache.RemovalCause.b
    };

    /* renamed from: c  reason: collision with root package name */
    public static final RemovalCause f18861c = new RemovalCause("COLLECTED", 2) { // from class: com.google.common.cache.RemovalCause.c
    };

    /* renamed from: d  reason: collision with root package name */
    public static final RemovalCause f18862d = new RemovalCause("EXPIRED", 3) { // from class: com.google.common.cache.RemovalCause.d
    };

    /* renamed from: e  reason: collision with root package name */
    public static final RemovalCause f18863e = new RemovalCause("SIZE", 4) { // from class: com.google.common.cache.RemovalCause.e
    };

    /* renamed from: f  reason: collision with root package name */
    private static final /* synthetic */ RemovalCause[] f18864f = c();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    enum a extends RemovalCause {
        a(String str, int i8) {
            super(str, i8, null);
        }
    }

    private RemovalCause(String str, int i8) {
    }

    /* synthetic */ RemovalCause(String str, int i8, a aVar) {
        this(str, i8);
    }

    private static /* synthetic */ RemovalCause[] c() {
        return new RemovalCause[]{f18859a, f18860b, f18861c, f18862d, f18863e};
    }

    public static RemovalCause valueOf(String str) {
        return (RemovalCause) Enum.valueOf(RemovalCause.class, str);
    }

    public static RemovalCause[] values() {
        return (RemovalCause[]) f18864f.clone();
    }
}
