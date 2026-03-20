package b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends f {

    /* renamed from: a  reason: collision with root package name */
    private final float f7915a;

    /* renamed from: b  reason: collision with root package name */
    private final float f7916b;

    /* renamed from: c  reason: collision with root package name */
    private final float f7917c;

    /* renamed from: d  reason: collision with root package name */
    private final float f7918d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(float f5, float f8, float f9, float f10) {
        this.f7915a = f5;
        this.f7916b = f8;
        this.f7917c = f9;
        this.f7918d = f10;
    }

    @Override // b0.f, androidx.camera.core.h3
    public float a() {
        return this.f7916b;
    }

    @Override // b0.f, androidx.camera.core.h3
    public float b() {
        return this.f7917c;
    }

    @Override // b0.f, androidx.camera.core.h3
    public float c() {
        return this.f7915a;
    }

    @Override // b0.f, androidx.camera.core.h3
    public float d() {
        return this.f7918d;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof f) {
            f fVar = (f) obj;
            return Float.floatToIntBits(this.f7915a) == Float.floatToIntBits(fVar.c()) && Float.floatToIntBits(this.f7916b) == Float.floatToIntBits(fVar.a()) && Float.floatToIntBits(this.f7917c) == Float.floatToIntBits(fVar.b()) && Float.floatToIntBits(this.f7918d) == Float.floatToIntBits(fVar.d());
        }
        return false;
    }

    public int hashCode() {
        return ((((((Float.floatToIntBits(this.f7915a) ^ 1000003) * 1000003) ^ Float.floatToIntBits(this.f7916b)) * 1000003) ^ Float.floatToIntBits(this.f7917c)) * 1000003) ^ Float.floatToIntBits(this.f7918d);
    }

    public String toString() {
        return "ImmutableZoomState{zoomRatio=" + this.f7915a + ", maxZoomRatio=" + this.f7916b + ", minZoomRatio=" + this.f7917c + ", linearZoom=" + this.f7918d + "}";
    }
}
