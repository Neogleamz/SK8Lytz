package androidx.transition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ArcMotion extends PathMotion {

    /* renamed from: g  reason: collision with root package name */
    private static final float f7369g = (float) Math.tan(Math.toRadians(35.0d));

    /* renamed from: a  reason: collision with root package name */
    private float f7370a;

    /* renamed from: b  reason: collision with root package name */
    private float f7371b;

    /* renamed from: c  reason: collision with root package name */
    private float f7372c;

    /* renamed from: d  reason: collision with root package name */
    private float f7373d;

    /* renamed from: e  reason: collision with root package name */
    private float f7374e;

    /* renamed from: f  reason: collision with root package name */
    private float f7375f;

    public ArcMotion() {
        this.f7370a = 0.0f;
        this.f7371b = 0.0f;
        this.f7372c = 70.0f;
        this.f7373d = 0.0f;
        this.f7374e = 0.0f;
        this.f7375f = f7369g;
    }

    @SuppressLint({"RestrictedApi"})
    public ArcMotion(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7370a = 0.0f;
        this.f7371b = 0.0f;
        this.f7372c = 70.0f;
        this.f7373d = 0.0f;
        this.f7374e = 0.0f;
        this.f7375f = f7369g;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7604j);
        XmlPullParser xmlPullParser = (XmlPullParser) attributeSet;
        d(androidx.core.content.res.k.f(obtainStyledAttributes, xmlPullParser, "minimumVerticalAngle", 1, 0.0f));
        c(androidx.core.content.res.k.f(obtainStyledAttributes, xmlPullParser, "minimumHorizontalAngle", 0, 0.0f));
        b(androidx.core.content.res.k.f(obtainStyledAttributes, xmlPullParser, "maximumAngle", 2, 70.0f));
        obtainStyledAttributes.recycle();
    }

    private static float e(float f5) {
        if (f5 < 0.0f || f5 > 90.0f) {
            throw new IllegalArgumentException("Arc must be between 0 and 90 degrees");
        }
        return (float) Math.tan(Math.toRadians(f5 / 2.0f));
    }

    @Override // androidx.transition.PathMotion
    public Path a(float f5, float f8, float f9, float f10) {
        float f11;
        float f12;
        float f13;
        Path path = new Path();
        path.moveTo(f5, f8);
        float f14 = f9 - f5;
        float f15 = f10 - f8;
        float f16 = (f14 * f14) + (f15 * f15);
        float f17 = (f5 + f9) / 2.0f;
        float f18 = (f8 + f10) / 2.0f;
        float f19 = 0.25f * f16;
        boolean z4 = f8 > f10;
        if (Math.abs(f14) < Math.abs(f15)) {
            float abs = Math.abs(f16 / (f15 * 2.0f));
            if (z4) {
                f12 = abs + f10;
                f11 = f9;
            } else {
                f12 = abs + f8;
                f11 = f5;
            }
            f13 = this.f7374e;
        } else {
            float f20 = f16 / (f14 * 2.0f);
            if (z4) {
                f12 = f8;
                f11 = f20 + f5;
            } else {
                f11 = f9 - f20;
                f12 = f10;
            }
            f13 = this.f7373d;
        }
        float f21 = f19 * f13 * f13;
        float f22 = f17 - f11;
        float f23 = f18 - f12;
        float f24 = (f22 * f22) + (f23 * f23);
        float f25 = this.f7375f;
        float f26 = f19 * f25 * f25;
        if (f24 >= f21) {
            f21 = f24 > f26 ? f26 : 0.0f;
        }
        if (f21 != 0.0f) {
            float sqrt = (float) Math.sqrt(f21 / f24);
            f11 = ((f11 - f17) * sqrt) + f17;
            f12 = f18 + (sqrt * (f12 - f18));
        }
        path.cubicTo((f5 + f11) / 2.0f, (f8 + f12) / 2.0f, (f11 + f9) / 2.0f, (f12 + f10) / 2.0f, f9, f10);
        return path;
    }

    public void b(float f5) {
        this.f7372c = f5;
        this.f7375f = e(f5);
    }

    public void c(float f5) {
        this.f7370a = f5;
        this.f7373d = e(f5);
    }

    public void d(float f5) {
        this.f7371b = f5;
        this.f7374e = e(f5);
    }
}
