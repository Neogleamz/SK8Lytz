package com.google.android.exoplayer2.video.spherical;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c {

    /* renamed from: a  reason: collision with root package name */
    public final a f11122a;

    /* renamed from: b  reason: collision with root package name */
    public final a f11123b;

    /* renamed from: c  reason: collision with root package name */
    public final int f11124c;

    /* renamed from: d  reason: collision with root package name */
    public final boolean f11125d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final b[] f11126a;

        public a(b... bVarArr) {
            this.f11126a = bVarArr;
        }

        public b a(int i8) {
            return this.f11126a[i8];
        }

        public int b() {
            return this.f11126a.length;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f11127a;

        /* renamed from: b  reason: collision with root package name */
        public final int f11128b;

        /* renamed from: c  reason: collision with root package name */
        public final float[] f11129c;

        /* renamed from: d  reason: collision with root package name */
        public final float[] f11130d;

        public b(int i8, float[] fArr, float[] fArr2, int i9) {
            this.f11127a = i8;
            b6.a.a(((long) fArr.length) * 2 == ((long) fArr2.length) * 3);
            this.f11129c = fArr;
            this.f11130d = fArr2;
            this.f11128b = i9;
        }

        public int a() {
            return this.f11129c.length / 3;
        }
    }

    public c(a aVar, int i8) {
        this(aVar, aVar, i8);
    }

    public c(a aVar, a aVar2, int i8) {
        this.f11122a = aVar;
        this.f11123b = aVar2;
        this.f11124c = i8;
        this.f11125d = aVar == aVar2;
    }

    public static c a(float f5, int i8, int i9, float f8, float f9, int i10) {
        int i11;
        float f10;
        int i12;
        int i13;
        int i14;
        float[] fArr;
        int i15;
        int i16 = i8;
        int i17 = i9;
        b6.a.a(f5 > 0.0f);
        b6.a.a(i16 >= 1);
        b6.a.a(i17 >= 1);
        b6.a.a(f8 > 0.0f && f8 <= 180.0f);
        b6.a.a(f9 > 0.0f && f9 <= 360.0f);
        float radians = (float) Math.toRadians(f8);
        float radians2 = (float) Math.toRadians(f9);
        float f11 = radians / i16;
        float f12 = radians2 / i17;
        int i18 = i17 + 1;
        int i19 = ((i18 * 2) + 2) * i16;
        float[] fArr2 = new float[i19 * 3];
        float[] fArr3 = new float[i19 * 2];
        int i20 = 0;
        int i21 = 0;
        int i22 = 0;
        while (i20 < i16) {
            float f13 = radians / 2.0f;
            float f14 = (i20 * f11) - f13;
            int i23 = i20 + 1;
            float f15 = (i23 * f11) - f13;
            int i24 = 0;
            while (i24 < i18) {
                float f16 = f14;
                int i25 = i23;
                int i26 = 0;
                int i27 = 2;
                while (i26 < i27) {
                    if (i26 == 0) {
                        f10 = f16;
                        i11 = i18;
                    } else {
                        i11 = i18;
                        f10 = f15;
                    }
                    float f17 = i24 * f12;
                    float f18 = f12;
                    int i28 = i21 + 1;
                    int i29 = i24;
                    double d8 = f5;
                    float f19 = f11;
                    double d9 = (f17 + 3.1415927f) - (radians2 / 2.0f);
                    int i30 = i26;
                    double d10 = f10;
                    float[] fArr4 = fArr3;
                    float f20 = f15;
                    fArr2[i21] = -((float) (Math.sin(d9) * d8 * Math.cos(d10)));
                    int i31 = i28 + 1;
                    int i32 = i20;
                    fArr2[i28] = (float) (d8 * Math.sin(d10));
                    int i33 = i31 + 1;
                    fArr2[i31] = (float) (d8 * Math.cos(d9) * Math.cos(d10));
                    int i34 = i22 + 1;
                    fArr4[i22] = f17 / radians2;
                    int i35 = i34 + 1;
                    fArr4[i34] = ((i32 + i30) * f19) / radians;
                    if (i29 == 0 && i30 == 0) {
                        i12 = i9;
                        i13 = i29;
                        i14 = i30;
                    } else {
                        i12 = i9;
                        i13 = i29;
                        i14 = i30;
                        if (i13 != i12 || i14 != 1) {
                            fArr = fArr4;
                            i15 = 2;
                            i22 = i35;
                            i21 = i33;
                            i26 = i14 + 1;
                            i17 = i12;
                            i24 = i13;
                            fArr3 = fArr;
                            i27 = i15;
                            i20 = i32;
                            i18 = i11;
                            f12 = f18;
                            f11 = f19;
                            f15 = f20;
                        }
                    }
                    System.arraycopy(fArr2, i33 - 3, fArr2, i33, 3);
                    i33 += 3;
                    fArr = fArr4;
                    i15 = 2;
                    System.arraycopy(fArr, i35 - 2, fArr, i35, 2);
                    i35 += 2;
                    i22 = i35;
                    i21 = i33;
                    i26 = i14 + 1;
                    i17 = i12;
                    i24 = i13;
                    fArr3 = fArr;
                    i27 = i15;
                    i20 = i32;
                    i18 = i11;
                    f12 = f18;
                    f11 = f19;
                    f15 = f20;
                }
                float f21 = f11;
                int i36 = i24;
                int i37 = i17;
                int i38 = i36 + 1;
                f14 = f16;
                i23 = i25;
                i18 = i18;
                f11 = f21;
                f15 = f15;
                i17 = i37;
                i24 = i38;
            }
            i16 = i8;
            i20 = i23;
        }
        return new c(new a(new b(0, fArr2, fArr3, 1)), i10);
    }

    public static c b(int i8) {
        return a(50.0f, 36, 72, 180.0f, 360.0f, i8);
    }
}
