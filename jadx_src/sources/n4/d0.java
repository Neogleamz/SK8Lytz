package n4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0 {

    /* renamed from: a  reason: collision with root package name */
    private final byte[] f22072a;

    /* renamed from: b  reason: collision with root package name */
    private final int f22073b;

    /* renamed from: c  reason: collision with root package name */
    private int f22074c;

    /* renamed from: d  reason: collision with root package name */
    private int f22075d;

    public d0(byte[] bArr) {
        this.f22072a = bArr;
        this.f22073b = bArr.length;
    }

    private void a() {
        int i8;
        int i9 = this.f22074c;
        b6.a.f(i9 >= 0 && (i9 < (i8 = this.f22073b) || (i9 == i8 && this.f22075d == 0)));
    }

    public int b() {
        return (this.f22074c * 8) + this.f22075d;
    }

    public boolean c() {
        boolean z4 = (((this.f22072a[this.f22074c] & 255) >> this.f22075d) & 1) == 1;
        e(1);
        return z4;
    }

    public int d(int i8) {
        int i9 = this.f22074c;
        int min = Math.min(i8, 8 - this.f22075d);
        int i10 = i9 + 1;
        int i11 = ((this.f22072a[i9] & 255) >> this.f22075d) & (255 >> (8 - min));
        while (min < i8) {
            i11 |= (this.f22072a[i10] & 255) << min;
            min += 8;
            i10++;
        }
        int i12 = i11 & ((-1) >>> (32 - i8));
        e(i8);
        return i12;
    }

    public void e(int i8) {
        int i9 = i8 / 8;
        int i10 = this.f22074c + i9;
        this.f22074c = i10;
        int i11 = this.f22075d + (i8 - (i9 * 8));
        this.f22075d = i11;
        if (i11 > 7) {
            this.f22074c = i10 + 1;
            this.f22075d = i11 - 8;
        }
        a();
    }
}
