package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

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
public final class zzep {

    /* renamed from: d  reason: collision with root package name */
    public static final zzep f15019d;

    /* renamed from: e  reason: collision with root package name */
    public static final zzep f15020e;

    /* renamed from: f  reason: collision with root package name */
    public static final zzep f15021f;

    /* renamed from: g  reason: collision with root package name */
    public static final zzep f15022g;

    /* renamed from: h  reason: collision with root package name */
    public static final zzep f15023h;

    /* renamed from: j  reason: collision with root package name */
    public static final zzep f15024j;

    /* renamed from: k  reason: collision with root package name */
    public static final zzep f15025k;

    /* renamed from: l  reason: collision with root package name */
    public static final zzep f15026l;

    /* renamed from: m  reason: collision with root package name */
    public static final zzep f15027m;

    /* renamed from: n  reason: collision with root package name */
    public static final zzep f15028n;

    /* renamed from: p  reason: collision with root package name */
    private static final /* synthetic */ zzep[] f15029p;

    /* renamed from: a  reason: collision with root package name */
    private final Class f15030a;

    /* renamed from: b  reason: collision with root package name */
    private final Class f15031b;

    /* renamed from: c  reason: collision with root package name */
    private final Object f15032c;

    static {
        zzep zzepVar = new zzep("VOID", 0, Void.class, Void.class, null);
        f15019d = zzepVar;
        Class cls = Integer.TYPE;
        zzep zzepVar2 = new zzep("INT", 1, cls, Integer.class, 0);
        f15020e = zzepVar2;
        zzep zzepVar3 = new zzep("LONG", 2, Long.TYPE, Long.class, 0L);
        f15021f = zzepVar3;
        zzep zzepVar4 = new zzep("FLOAT", 3, Float.TYPE, Float.class, Float.valueOf(0.0f));
        f15022g = zzepVar4;
        zzep zzepVar5 = new zzep("DOUBLE", 4, Double.TYPE, Double.class, Double.valueOf(0.0d));
        f15023h = zzepVar5;
        zzep zzepVar6 = new zzep("BOOLEAN", 5, Boolean.TYPE, Boolean.class, Boolean.FALSE);
        f15024j = zzepVar6;
        zzep zzepVar7 = new zzep("STRING", 6, String.class, String.class, BuildConfig.FLAVOR);
        f15025k = zzepVar7;
        zzep zzepVar8 = new zzep("BYTE_STRING", 7, zzdb.class, zzdb.class, zzdb.f14977b);
        f15026l = zzepVar8;
        zzep zzepVar9 = new zzep("ENUM", 8, cls, Integer.class, null);
        f15027m = zzepVar9;
        zzep zzepVar10 = new zzep("MESSAGE", 9, Object.class, Object.class, null);
        f15028n = zzepVar10;
        f15029p = new zzep[]{zzepVar, zzepVar2, zzepVar3, zzepVar4, zzepVar5, zzepVar6, zzepVar7, zzepVar8, zzepVar9, zzepVar10};
    }

    private zzep(String str, int i8, Class cls, Class cls2, Object obj) {
        this.f15030a = cls;
        this.f15031b = cls2;
        this.f15032c = obj;
    }

    public static zzep[] values() {
        return (zzep[]) f15029p.clone();
    }

    public final Class c() {
        return this.f15031b;
    }
}
