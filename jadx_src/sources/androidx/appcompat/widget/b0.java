package androidx.appcompat.widget;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b0 {

    /* renamed from: a  reason: collision with root package name */
    private int f1414a = 0;

    /* renamed from: b  reason: collision with root package name */
    private int f1415b = 0;

    /* renamed from: c  reason: collision with root package name */
    private int f1416c = Integer.MIN_VALUE;

    /* renamed from: d  reason: collision with root package name */
    private int f1417d = Integer.MIN_VALUE;

    /* renamed from: e  reason: collision with root package name */
    private int f1418e = 0;

    /* renamed from: f  reason: collision with root package name */
    private int f1419f = 0;

    /* renamed from: g  reason: collision with root package name */
    private boolean f1420g = false;

    /* renamed from: h  reason: collision with root package name */
    private boolean f1421h = false;

    public int a() {
        return this.f1420g ? this.f1414a : this.f1415b;
    }

    public int b() {
        return this.f1414a;
    }

    public int c() {
        return this.f1415b;
    }

    public int d() {
        return this.f1420g ? this.f1415b : this.f1414a;
    }

    public void e(int i8, int i9) {
        this.f1421h = false;
        if (i8 != Integer.MIN_VALUE) {
            this.f1418e = i8;
            this.f1414a = i8;
        }
        if (i9 != Integer.MIN_VALUE) {
            this.f1419f = i9;
            this.f1415b = i9;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x001a, code lost:
        if (r2 != Integer.MIN_VALUE) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0028, code lost:
        if (r2 != Integer.MIN_VALUE) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void f(boolean r2) {
        /*
            r1 = this;
            boolean r0 = r1.f1420g
            if (r2 != r0) goto L5
            return
        L5:
            r1.f1420g = r2
            boolean r0 = r1.f1421h
            if (r0 == 0) goto L2b
            r0 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r2 == 0) goto L1d
            int r2 = r1.f1417d
            if (r2 == r0) goto L14
            goto L16
        L14:
            int r2 = r1.f1418e
        L16:
            r1.f1414a = r2
            int r2 = r1.f1416c
            if (r2 == r0) goto L2f
            goto L31
        L1d:
            int r2 = r1.f1416c
            if (r2 == r0) goto L22
            goto L24
        L22:
            int r2 = r1.f1418e
        L24:
            r1.f1414a = r2
            int r2 = r1.f1417d
            if (r2 == r0) goto L2f
            goto L31
        L2b:
            int r2 = r1.f1418e
            r1.f1414a = r2
        L2f:
            int r2 = r1.f1419f
        L31:
            r1.f1415b = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.b0.f(boolean):void");
    }

    public void g(int i8, int i9) {
        this.f1416c = i8;
        this.f1417d = i9;
        this.f1421h = true;
        if (this.f1420g) {
            if (i9 != Integer.MIN_VALUE) {
                this.f1414a = i9;
            }
            if (i8 != Integer.MIN_VALUE) {
                this.f1415b = i8;
                return;
            }
            return;
        }
        if (i8 != Integer.MIN_VALUE) {
            this.f1414a = i8;
        }
        if (i9 != Integer.MIN_VALUE) {
            this.f1415b = i9;
        }
    }
}
