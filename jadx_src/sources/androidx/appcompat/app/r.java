package androidx.appcompat.app;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class r {

    /* renamed from: d  reason: collision with root package name */
    private static r f713d;

    /* renamed from: a  reason: collision with root package name */
    public long f714a;

    /* renamed from: b  reason: collision with root package name */
    public long f715b;

    /* renamed from: c  reason: collision with root package name */
    public int f716c;

    r() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static r b() {
        if (f713d == null) {
            f713d = new r();
        }
        return f713d;
    }

    public void a(long j8, double d8, double d9) {
        float f5;
        float f8;
        double d10;
        double d11 = (0.01720197f * (((float) (j8 - 946728000000L)) / 8.64E7f)) + 6.24006f;
        double sin = (Math.sin(d11) * 0.03341960161924362d) + d11 + (Math.sin(2.0f * f8) * 3.4906598739326E-4d) + (Math.sin(f8 * 3.0f) * 5.236000106378924E-6d) + 1.796593063d + 3.141592653589793d;
        double round = ((float) Math.round((f5 - 9.0E-4f) - d10)) + 9.0E-4f + ((-d9) / 360.0d) + (Math.sin(d11) * 0.0053d) + (Math.sin(2.0d * sin) * (-0.0069d));
        double asin = Math.asin(Math.sin(sin) * Math.sin(0.4092797040939331d));
        double d12 = 0.01745329238474369d * d8;
        double sin2 = (Math.sin(-0.10471975803375244d) - (Math.sin(d12) * Math.sin(asin))) / (Math.cos(d12) * Math.cos(asin));
        if (sin2 >= 1.0d) {
            this.f716c = 1;
        } else if (sin2 > -1.0d) {
            double acos = (float) (Math.acos(sin2) / 6.283185307179586d);
            this.f714a = Math.round((round + acos) * 8.64E7d) + 946728000000L;
            long round2 = Math.round((round - acos) * 8.64E7d) + 946728000000L;
            this.f715b = round2;
            if (round2 >= j8 || this.f714a <= j8) {
                this.f716c = 1;
                return;
            } else {
                this.f716c = 0;
                return;
            }
        } else {
            this.f716c = 0;
        }
        this.f714a = -1L;
        this.f715b = -1L;
    }
}
