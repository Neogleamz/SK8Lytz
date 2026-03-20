package k1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: e  reason: collision with root package name */
    public static final c f20951e;

    /* renamed from: f  reason: collision with root package name */
    public static final c f20952f;

    /* renamed from: g  reason: collision with root package name */
    public static final c f20953g;

    /* renamed from: h  reason: collision with root package name */
    public static final c f20954h;

    /* renamed from: i  reason: collision with root package name */
    public static final c f20955i;

    /* renamed from: j  reason: collision with root package name */
    public static final c f20956j;

    /* renamed from: a  reason: collision with root package name */
    final float[] f20957a;

    /* renamed from: b  reason: collision with root package name */
    final float[] f20958b;

    /* renamed from: c  reason: collision with root package name */
    final float[] f20959c = new float[3];

    /* renamed from: d  reason: collision with root package name */
    boolean f20960d = true;

    static {
        c cVar = new c();
        f20951e = cVar;
        m(cVar);
        p(cVar);
        c cVar2 = new c();
        f20952f = cVar2;
        o(cVar2);
        p(cVar2);
        c cVar3 = new c();
        f20953g = cVar3;
        l(cVar3);
        p(cVar3);
        c cVar4 = new c();
        f20954h = cVar4;
        m(cVar4);
        n(cVar4);
        c cVar5 = new c();
        f20955i = cVar5;
        o(cVar5);
        n(cVar5);
        c cVar6 = new c();
        f20956j = cVar6;
        l(cVar6);
        n(cVar6);
    }

    c() {
        float[] fArr = new float[3];
        this.f20957a = fArr;
        float[] fArr2 = new float[3];
        this.f20958b = fArr2;
        r(fArr);
        r(fArr2);
        q();
    }

    private static void l(c cVar) {
        float[] fArr = cVar.f20958b;
        fArr[1] = 0.26f;
        fArr[2] = 0.45f;
    }

    private static void m(c cVar) {
        float[] fArr = cVar.f20958b;
        fArr[0] = 0.55f;
        fArr[1] = 0.74f;
    }

    private static void n(c cVar) {
        float[] fArr = cVar.f20957a;
        fArr[1] = 0.3f;
        fArr[2] = 0.4f;
    }

    private static void o(c cVar) {
        float[] fArr = cVar.f20958b;
        fArr[0] = 0.3f;
        fArr[1] = 0.5f;
        fArr[2] = 0.7f;
    }

    private static void p(c cVar) {
        float[] fArr = cVar.f20957a;
        fArr[0] = 0.35f;
        fArr[1] = 1.0f;
    }

    private void q() {
        float[] fArr = this.f20959c;
        fArr[0] = 0.24f;
        fArr[1] = 0.52f;
        fArr[2] = 0.24f;
    }

    private static void r(float[] fArr) {
        fArr[0] = 0.0f;
        fArr[1] = 0.5f;
        fArr[2] = 1.0f;
    }

    public float a() {
        return this.f20959c[1];
    }

    public float b() {
        return this.f20958b[2];
    }

    public float c() {
        return this.f20957a[2];
    }

    public float d() {
        return this.f20958b[0];
    }

    public float e() {
        return this.f20957a[0];
    }

    public float f() {
        return this.f20959c[2];
    }

    public float g() {
        return this.f20959c[0];
    }

    public float h() {
        return this.f20958b[1];
    }

    public float i() {
        return this.f20957a[1];
    }

    public boolean j() {
        return this.f20960d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        int length = this.f20959c.length;
        float f5 = 0.0f;
        for (int i8 = 0; i8 < length; i8++) {
            float f8 = this.f20959c[i8];
            if (f8 > 0.0f) {
                f5 += f8;
            }
        }
        if (f5 != 0.0f) {
            int length2 = this.f20959c.length;
            for (int i9 = 0; i9 < length2; i9++) {
                float[] fArr = this.f20959c;
                if (fArr[i9] > 0.0f) {
                    fArr[i9] = fArr[i9] / f5;
                }
            }
        }
    }
}
