package h5;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j {

    /* renamed from: a  reason: collision with root package name */
    public final Object f20286a;

    /* renamed from: b  reason: collision with root package name */
    public final int f20287b;

    /* renamed from: c  reason: collision with root package name */
    public final int f20288c;

    /* renamed from: d  reason: collision with root package name */
    public final long f20289d;

    /* renamed from: e  reason: collision with root package name */
    public final int f20290e;

    /* JADX INFO: Access modifiers changed from: protected */
    public j(j jVar) {
        this.f20286a = jVar.f20286a;
        this.f20287b = jVar.f20287b;
        this.f20288c = jVar.f20288c;
        this.f20289d = jVar.f20289d;
        this.f20290e = jVar.f20290e;
    }

    public j(Object obj) {
        this(obj, -1L);
    }

    public j(Object obj, int i8, int i9, long j8) {
        this(obj, i8, i9, j8, -1);
    }

    private j(Object obj, int i8, int i9, long j8, int i10) {
        this.f20286a = obj;
        this.f20287b = i8;
        this.f20288c = i9;
        this.f20289d = j8;
        this.f20290e = i10;
    }

    public j(Object obj, long j8) {
        this(obj, -1, -1, j8, -1);
    }

    public j(Object obj, long j8, int i8) {
        this(obj, -1, -1, j8, i8);
    }

    public j a(Object obj) {
        return this.f20286a.equals(obj) ? this : new j(obj, this.f20287b, this.f20288c, this.f20289d, this.f20290e);
    }

    public boolean b() {
        return this.f20287b != -1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof j) {
            j jVar = (j) obj;
            return this.f20286a.equals(jVar.f20286a) && this.f20287b == jVar.f20287b && this.f20288c == jVar.f20288c && this.f20289d == jVar.f20289d && this.f20290e == jVar.f20290e;
        }
        return false;
    }

    public int hashCode() {
        return ((((((((527 + this.f20286a.hashCode()) * 31) + this.f20287b) * 31) + this.f20288c) * 31) + ((int) this.f20289d)) * 31) + this.f20290e;
    }
}
