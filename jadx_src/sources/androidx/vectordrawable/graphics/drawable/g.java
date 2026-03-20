package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import androidx.core.content.res.k;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements Interpolator {

    /* renamed from: a  reason: collision with root package name */
    private float[] f7663a;

    /* renamed from: b  reason: collision with root package name */
    private float[] f7664b;

    public g(Context context, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        this(context.getResources(), context.getTheme(), attributeSet, xmlPullParser);
    }

    public g(Resources resources, Resources.Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        TypedArray k8 = k.k(resources, theme, attributeSet, a.f7647l);
        d(k8, xmlPullParser);
        k8.recycle();
    }

    private void a(float f5, float f8, float f9, float f10) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f5, f8, f9, f10, 1.0f, 1.0f);
        b(path);
    }

    private void b(Path path) {
        int i8 = 0;
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        int min = Math.min(3000, ((int) (length / 0.002f)) + 1);
        if (min <= 0) {
            throw new IllegalArgumentException("The Path has a invalid length " + length);
        }
        this.f7663a = new float[min];
        this.f7664b = new float[min];
        float[] fArr = new float[2];
        for (int i9 = 0; i9 < min; i9++) {
            pathMeasure.getPosTan((i9 * length) / (min - 1), fArr, null);
            this.f7663a[i9] = fArr[0];
            this.f7664b[i9] = fArr[1];
        }
        if (Math.abs(this.f7663a[0]) <= 1.0E-5d && Math.abs(this.f7664b[0]) <= 1.0E-5d) {
            int i10 = min - 1;
            if (Math.abs(this.f7663a[i10] - 1.0f) <= 1.0E-5d && Math.abs(this.f7664b[i10] - 1.0f) <= 1.0E-5d) {
                float f5 = 0.0f;
                int i11 = 0;
                while (i8 < min) {
                    float[] fArr2 = this.f7663a;
                    int i12 = i11 + 1;
                    float f8 = fArr2[i11];
                    if (f8 < f5) {
                        throw new IllegalArgumentException("The Path cannot loop back on itself, x :" + f8);
                    }
                    fArr2[i8] = f8;
                    i8++;
                    f5 = f8;
                    i11 = i12;
                }
                if (pathMeasure.nextContour()) {
                    throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
                }
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The Path must start at (0,0) and end at (1,1) start: ");
        sb.append(this.f7663a[0]);
        sb.append(",");
        sb.append(this.f7664b[0]);
        sb.append(" end:");
        int i13 = min - 1;
        sb.append(this.f7663a[i13]);
        sb.append(",");
        sb.append(this.f7664b[i13]);
        throw new IllegalArgumentException(sb.toString());
    }

    private void c(float f5, float f8) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f5, f8, 1.0f, 1.0f);
        b(path);
    }

    private void d(TypedArray typedArray, XmlPullParser xmlPullParser) {
        if (k.j(xmlPullParser, "pathData")) {
            String i8 = k.i(typedArray, xmlPullParser, "pathData", 4);
            Path e8 = androidx.core.graphics.e.e(i8);
            if (e8 != null) {
                b(e8);
                return;
            }
            throw new InflateException("The path is null, which is created from " + i8);
        } else if (!k.j(xmlPullParser, "controlX1")) {
            throw new InflateException("pathInterpolator requires the controlX1 attribute");
        } else {
            if (!k.j(xmlPullParser, "controlY1")) {
                throw new InflateException("pathInterpolator requires the controlY1 attribute");
            }
            float f5 = k.f(typedArray, xmlPullParser, "controlX1", 0, 0.0f);
            float f8 = k.f(typedArray, xmlPullParser, "controlY1", 1, 0.0f);
            boolean j8 = k.j(xmlPullParser, "controlX2");
            if (j8 != k.j(xmlPullParser, "controlY2")) {
                throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
            }
            if (j8) {
                a(f5, f8, k.f(typedArray, xmlPullParser, "controlX2", 2, 0.0f), k.f(typedArray, xmlPullParser, "controlY2", 3, 0.0f));
            } else {
                c(f5, f8);
            }
        }
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f5) {
        if (f5 <= 0.0f) {
            return 0.0f;
        }
        if (f5 >= 1.0f) {
            return 1.0f;
        }
        int i8 = 0;
        int length = this.f7663a.length - 1;
        while (length - i8 > 1) {
            int i9 = (i8 + length) / 2;
            if (f5 < this.f7663a[i9]) {
                length = i9;
            } else {
                i8 = i9;
            }
        }
        float[] fArr = this.f7663a;
        float f8 = fArr[length] - fArr[i8];
        if (f8 == 0.0f) {
            return this.f7664b[i8];
        }
        float[] fArr2 = this.f7664b;
        float f9 = fArr2[i8];
        return f9 + (((f5 - fArr[i8]) / f8) * (fArr2[length] - f9));
    }
}
