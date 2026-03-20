package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum e uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:444)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:391)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:320)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:258)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzkd {

    /* renamed from: d  reason: collision with root package name */
    public static final zzkd f12904d;

    /* renamed from: e  reason: collision with root package name */
    public static final zzkd f12905e;

    /* renamed from: f  reason: collision with root package name */
    public static final zzkd f12906f;

    /* renamed from: g  reason: collision with root package name */
    public static final zzkd f12907g;

    /* renamed from: h  reason: collision with root package name */
    public static final zzkd f12908h;

    /* renamed from: j  reason: collision with root package name */
    public static final zzkd f12909j;

    /* renamed from: k  reason: collision with root package name */
    public static final zzkd f12910k;

    /* renamed from: l  reason: collision with root package name */
    public static final zzkd f12911l;

    /* renamed from: m  reason: collision with root package name */
    public static final zzkd f12912m;

    /* renamed from: n  reason: collision with root package name */
    public static final zzkd f12913n;

    /* renamed from: p  reason: collision with root package name */
    private static final /* synthetic */ zzkd[] f12914p;

    /* renamed from: a  reason: collision with root package name */
    private final Class<?> f12915a;

    /* renamed from: b  reason: collision with root package name */
    private final Class<?> f12916b;

    /* renamed from: c  reason: collision with root package name */
    private final Object f12917c;

    static {
        zzkd zzkdVar = new zzkd("VOID", 0, Void.class, Void.class, null);
        f12904d = zzkdVar;
        Class cls = Integer.TYPE;
        zzkd zzkdVar2 = new zzkd("INT", 1, cls, Integer.class, 0);
        f12905e = zzkdVar2;
        zzkd zzkdVar3 = new zzkd("LONG", 2, Long.TYPE, Long.class, 0L);
        f12906f = zzkdVar3;
        zzkd zzkdVar4 = new zzkd("FLOAT", 3, Float.TYPE, Float.class, Float.valueOf(0.0f));
        f12907g = zzkdVar4;
        zzkd zzkdVar5 = new zzkd("DOUBLE", 4, Double.TYPE, Double.class, Double.valueOf(0.0d));
        f12908h = zzkdVar5;
        zzkd zzkdVar6 = new zzkd("BOOLEAN", 5, Boolean.TYPE, Boolean.class, Boolean.FALSE);
        f12909j = zzkdVar6;
        zzkd zzkdVar7 = new zzkd("STRING", 6, String.class, String.class, BuildConfig.FLAVOR);
        f12910k = zzkdVar7;
        zzkd zzkdVar8 = new zzkd("BYTE_STRING", 7, zzij.class, zzij.class, zzij.f12852b);
        f12911l = zzkdVar8;
        zzkd zzkdVar9 = new zzkd("ENUM", 8, cls, Integer.class, null);
        f12912m = zzkdVar9;
        zzkd zzkdVar10 = new zzkd("MESSAGE", 9, Object.class, Object.class, null);
        f12913n = zzkdVar10;
        f12914p = new zzkd[]{zzkdVar, zzkdVar2, zzkdVar3, zzkdVar4, zzkdVar5, zzkdVar6, zzkdVar7, zzkdVar8, zzkdVar9, zzkdVar10};
    }

    private zzkd(String str, int i8, Class cls, Class cls2, Object obj) {
        this.f12915a = cls;
        this.f12916b = cls2;
        this.f12917c = obj;
    }

    public static zzkd[] values() {
        return (zzkd[]) f12914p.clone();
    }

    public final Class<?> c() {
        return this.f12916b;
    }
}
