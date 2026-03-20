package androidx.transition;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.Property;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class m<T> extends Property<T, Float> {

    /* renamed from: a  reason: collision with root package name */
    private final Property<T, PointF> f7584a;

    /* renamed from: b  reason: collision with root package name */
    private final PathMeasure f7585b;

    /* renamed from: c  reason: collision with root package name */
    private final float f7586c;

    /* renamed from: d  reason: collision with root package name */
    private final float[] f7587d;

    /* renamed from: e  reason: collision with root package name */
    private final PointF f7588e;

    /* renamed from: f  reason: collision with root package name */
    private float f7589f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(Property<T, PointF> property, Path path) {
        super(Float.class, property.getName());
        this.f7587d = new float[2];
        this.f7588e = new PointF();
        this.f7584a = property;
        PathMeasure pathMeasure = new PathMeasure(path, false);
        this.f7585b = pathMeasure;
        this.f7586c = pathMeasure.getLength();
    }

    @Override // android.util.Property
    /* renamed from: a */
    public Float get(T t8) {
        return Float.valueOf(this.f7589f);
    }

    @Override // android.util.Property
    /* renamed from: b */
    public void set(T t8, Float f5) {
        this.f7589f = f5.floatValue();
        this.f7585b.getPosTan(this.f7586c * f5.floatValue(), this.f7587d, null);
        PointF pointF = this.f7588e;
        float[] fArr = this.f7587d;
        pointF.x = fArr[0];
        pointF.y = fArr[1];
        this.f7584a.set(t8, pointF);
    }
}
