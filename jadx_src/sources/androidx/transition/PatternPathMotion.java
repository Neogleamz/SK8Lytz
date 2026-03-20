package androidx.transition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class PatternPathMotion extends PathMotion {

    /* renamed from: a  reason: collision with root package name */
    private Path f7450a;

    /* renamed from: b  reason: collision with root package name */
    private final Path f7451b;

    /* renamed from: c  reason: collision with root package name */
    private final Matrix f7452c;

    public PatternPathMotion() {
        Path path = new Path();
        this.f7451b = path;
        this.f7452c = new Matrix();
        path.lineTo(1.0f, 0.0f);
        this.f7450a = path;
    }

    @SuppressLint({"RestrictedApi"})
    public PatternPathMotion(Context context, AttributeSet attributeSet) {
        this.f7451b = new Path();
        this.f7452c = new Matrix();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7605k);
        try {
            String i8 = androidx.core.content.res.k.i(obtainStyledAttributes, (XmlPullParser) attributeSet, "patternPathData", 0);
            if (i8 == null) {
                throw new RuntimeException("pathData must be supplied for patternPathMotion");
            }
            c(androidx.core.graphics.e.e(i8));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private static float b(float f5, float f8) {
        return (float) Math.sqrt((f5 * f5) + (f8 * f8));
    }

    @Override // androidx.transition.PathMotion
    public Path a(float f5, float f8, float f9, float f10) {
        float f11 = f9 - f5;
        float f12 = f10 - f8;
        float b9 = b(f11, f12);
        double atan2 = Math.atan2(f12, f11);
        this.f7452c.setScale(b9, b9);
        this.f7452c.postRotate((float) Math.toDegrees(atan2));
        this.f7452c.postTranslate(f5, f8);
        Path path = new Path();
        this.f7451b.transform(this.f7452c, path);
        return path;
    }

    public void c(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float[] fArr = new float[2];
        pathMeasure.getPosTan(pathMeasure.getLength(), fArr, null);
        float f5 = fArr[0];
        float f8 = fArr[1];
        pathMeasure.getPosTan(0.0f, fArr, null);
        float f9 = fArr[0];
        float f10 = fArr[1];
        if (f9 == f5 && f10 == f8) {
            throw new IllegalArgumentException("pattern must not end at the starting point");
        }
        this.f7452c.setTranslate(-f9, -f10);
        float f11 = f5 - f9;
        float f12 = f8 - f10;
        float b9 = 1.0f / b(f11, f12);
        this.f7452c.postScale(b9, b9);
        this.f7452c.postRotate((float) Math.toDegrees(-Math.atan2(f12, f11)));
        path.transform(this.f7452c, this.f7451b);
        this.f7450a = path;
    }
}
