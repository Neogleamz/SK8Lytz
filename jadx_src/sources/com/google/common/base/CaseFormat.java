package com.google.common.base;

import com.daimajia.numberprogressbar.BuildConfig;
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class CaseFormat {

    /* renamed from: c  reason: collision with root package name */
    public static final CaseFormat f18770c = new a("LOWER_HYPHEN", 0, com.google.common.base.d.e('-'), "-");

    /* renamed from: d  reason: collision with root package name */
    public static final CaseFormat f18771d = new CaseFormat("LOWER_UNDERSCORE", 1, com.google.common.base.d.e('_'), "_") { // from class: com.google.common.base.CaseFormat.b
    };

    /* renamed from: e  reason: collision with root package name */
    public static final CaseFormat f18772e = new CaseFormat("LOWER_CAMEL", 2, com.google.common.base.d.c('A', 'Z'), BuildConfig.FLAVOR) { // from class: com.google.common.base.CaseFormat.c
    };

    /* renamed from: f  reason: collision with root package name */
    public static final CaseFormat f18773f = new CaseFormat("UPPER_CAMEL", 3, com.google.common.base.d.c('A', 'Z'), BuildConfig.FLAVOR) { // from class: com.google.common.base.CaseFormat.d
    };

    /* renamed from: g  reason: collision with root package name */
    public static final CaseFormat f18774g = new CaseFormat("UPPER_UNDERSCORE", 4, com.google.common.base.d.e('_'), "_") { // from class: com.google.common.base.CaseFormat.e
    };

    /* renamed from: h  reason: collision with root package name */
    private static final /* synthetic */ CaseFormat[] f18775h = c();

    /* renamed from: a  reason: collision with root package name */
    private final com.google.common.base.d f18776a;

    /* renamed from: b  reason: collision with root package name */
    private final String f18777b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    enum a extends CaseFormat {
        a(String str, int i8, com.google.common.base.d dVar, String str2) {
            super(str, i8, dVar, str2, null);
        }
    }

    private CaseFormat(String str, int i8, com.google.common.base.d dVar, String str2) {
        this.f18776a = dVar;
        this.f18777b = str2;
    }

    /* synthetic */ CaseFormat(String str, int i8, com.google.common.base.d dVar, String str2, a aVar) {
        this(str, i8, dVar, str2);
    }

    private static /* synthetic */ CaseFormat[] c() {
        return new CaseFormat[]{f18770c, f18771d, f18772e, f18773f, f18774g};
    }

    public static CaseFormat valueOf(String str) {
        return (CaseFormat) Enum.valueOf(CaseFormat.class, str);
    }

    public static CaseFormat[] values() {
        return (CaseFormat[]) f18775h.clone();
    }
}
