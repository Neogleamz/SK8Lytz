package androidx.recyclerview.widget;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e implements s {

    /* renamed from: a  reason: collision with root package name */
    final s f6821a;

    /* renamed from: b  reason: collision with root package name */
    int f6822b = 0;

    /* renamed from: c  reason: collision with root package name */
    int f6823c = -1;

    /* renamed from: d  reason: collision with root package name */
    int f6824d = -1;

    /* renamed from: e  reason: collision with root package name */
    Object f6825e = null;

    public e(s sVar) {
        this.f6821a = sVar;
    }

    @Override // androidx.recyclerview.widget.s
    public void a(int i8, int i9) {
        e();
        this.f6821a.a(i8, i9);
    }

    @Override // androidx.recyclerview.widget.s
    public void b(int i8, int i9) {
        int i10;
        if (this.f6822b == 1 && i8 >= (i10 = this.f6823c)) {
            int i11 = this.f6824d;
            if (i8 <= i10 + i11) {
                this.f6824d = i11 + i9;
                this.f6823c = Math.min(i8, i10);
                return;
            }
        }
        e();
        this.f6823c = i8;
        this.f6824d = i9;
        this.f6822b = 1;
    }

    @Override // androidx.recyclerview.widget.s
    public void c(int i8, int i9) {
        int i10;
        if (this.f6822b == 2 && (i10 = this.f6823c) >= i8 && i10 <= i8 + i9) {
            this.f6824d += i9;
            this.f6823c = i8;
            return;
        }
        e();
        this.f6823c = i8;
        this.f6824d = i9;
        this.f6822b = 2;
    }

    @Override // androidx.recyclerview.widget.s
    public void d(int i8, int i9, Object obj) {
        int i10;
        if (this.f6822b == 3) {
            int i11 = this.f6823c;
            int i12 = this.f6824d;
            if (i8 <= i11 + i12 && (i10 = i8 + i9) >= i11 && this.f6825e == obj) {
                this.f6823c = Math.min(i8, i11);
                this.f6824d = Math.max(i12 + i11, i10) - this.f6823c;
                return;
            }
        }
        e();
        this.f6823c = i8;
        this.f6824d = i9;
        this.f6825e = obj;
        this.f6822b = 3;
    }

    public void e() {
        int i8 = this.f6822b;
        if (i8 == 0) {
            return;
        }
        if (i8 == 1) {
            this.f6821a.b(this.f6823c, this.f6824d);
        } else if (i8 == 2) {
            this.f6821a.c(this.f6823c, this.f6824d);
        } else if (i8 == 3) {
            this.f6821a.d(this.f6823c, this.f6824d, this.f6825e);
        }
        this.f6825e = null;
        this.f6822b = 0;
    }
}
