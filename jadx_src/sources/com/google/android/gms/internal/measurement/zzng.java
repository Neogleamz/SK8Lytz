package com.google.android.gms.internal.measurement;
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
public class zzng {
    private static final /* synthetic */ zzng[] A;

    /* renamed from: c  reason: collision with root package name */
    public static final zzng f12925c;

    /* renamed from: d  reason: collision with root package name */
    public static final zzng f12926d;

    /* renamed from: e  reason: collision with root package name */
    public static final zzng f12927e;

    /* renamed from: f  reason: collision with root package name */
    public static final zzng f12928f;

    /* renamed from: g  reason: collision with root package name */
    public static final zzng f12929g;

    /* renamed from: h  reason: collision with root package name */
    public static final zzng f12930h;

    /* renamed from: j  reason: collision with root package name */
    public static final zzng f12931j;

    /* renamed from: k  reason: collision with root package name */
    public static final zzng f12932k;

    /* renamed from: l  reason: collision with root package name */
    public static final zzng f12933l;

    /* renamed from: m  reason: collision with root package name */
    public static final zzng f12934m;

    /* renamed from: n  reason: collision with root package name */
    public static final zzng f12935n;

    /* renamed from: p  reason: collision with root package name */
    public static final zzng f12936p;
    public static final zzng q;

    /* renamed from: t  reason: collision with root package name */
    public static final zzng f12937t;

    /* renamed from: w  reason: collision with root package name */
    public static final zzng f12938w;

    /* renamed from: x  reason: collision with root package name */
    public static final zzng f12939x;

    /* renamed from: y  reason: collision with root package name */
    public static final zzng f12940y;

    /* renamed from: z  reason: collision with root package name */
    public static final zzng f12941z;

    /* renamed from: a  reason: collision with root package name */
    private final zznq f12942a;

    /* renamed from: b  reason: collision with root package name */
    private final int f12943b;

    static {
        zzng zzngVar = new zzng("DOUBLE", 0, zznq.DOUBLE, 1);
        f12925c = zzngVar;
        zzng zzngVar2 = new zzng("FLOAT", 1, zznq.FLOAT, 5);
        f12926d = zzngVar2;
        zznq zznqVar = zznq.LONG;
        zzng zzngVar3 = new zzng("INT64", 2, zznqVar, 0);
        f12927e = zzngVar3;
        zzng zzngVar4 = new zzng("UINT64", 3, zznqVar, 0);
        f12928f = zzngVar4;
        zznq zznqVar2 = zznq.INT;
        zzng zzngVar5 = new zzng("INT32", 4, zznqVar2, 0);
        f12929g = zzngVar5;
        zzng zzngVar6 = new zzng("FIXED64", 5, zznqVar, 1);
        f12930h = zzngVar6;
        zzng zzngVar7 = new zzng("FIXED32", 6, zznqVar2, 5);
        f12931j = zzngVar7;
        zzng zzngVar8 = new zzng("BOOL", 7, zznq.BOOLEAN, 0);
        f12932k = zzngVar8;
        ic icVar = new ic("STRING", zznq.STRING);
        f12933l = icVar;
        zznq zznqVar3 = zznq.MESSAGE;
        kc kcVar = new kc("GROUP", zznqVar3);
        f12934m = kcVar;
        mc mcVar = new mc("MESSAGE", zznqVar3);
        f12935n = mcVar;
        oc ocVar = new oc("BYTES", zznq.BYTE_STRING);
        f12936p = ocVar;
        zzng zzngVar9 = new zzng("UINT32", 12, zznqVar2, 0);
        q = zzngVar9;
        zzng zzngVar10 = new zzng("ENUM", 13, zznq.ENUM, 0);
        f12937t = zzngVar10;
        zzng zzngVar11 = new zzng("SFIXED32", 14, zznqVar2, 5);
        f12938w = zzngVar11;
        zzng zzngVar12 = new zzng("SFIXED64", 15, zznqVar, 1);
        f12939x = zzngVar12;
        zzng zzngVar13 = new zzng("SINT32", 16, zznqVar2, 0);
        f12940y = zzngVar13;
        zzng zzngVar14 = new zzng("SINT64", 17, zznqVar, 0);
        f12941z = zzngVar14;
        A = new zzng[]{zzngVar, zzngVar2, zzngVar3, zzngVar4, zzngVar5, zzngVar6, zzngVar7, zzngVar8, icVar, kcVar, mcVar, ocVar, zzngVar9, zzngVar10, zzngVar11, zzngVar12, zzngVar13, zzngVar14};
    }

    private zzng(String str, int i8, zznq zznqVar, int i9) {
        this.f12942a = zznqVar;
        this.f12943b = i9;
    }

    public static zzng[] values() {
        return (zzng[]) A.clone();
    }

    public final zznq c() {
        return this.f12942a;
    }
}
