package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import androidx.core.content.res.k;
import androidx.core.graphics.e;
import com.daimajia.numberprogressbar.BuildConfig;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements TypeEvaluator<e.b[]> {

        /* renamed from: a  reason: collision with root package name */
        private e.b[] f7661a;

        a() {
        }

        @Override // android.animation.TypeEvaluator
        /* renamed from: a */
        public e.b[] evaluate(float f5, e.b[] bVarArr, e.b[] bVarArr2) {
            if (androidx.core.graphics.e.b(bVarArr, bVarArr2)) {
                if (!androidx.core.graphics.e.b(this.f7661a, bVarArr)) {
                    this.f7661a = androidx.core.graphics.e.f(bVarArr);
                }
                for (int i8 = 0; i8 < bVarArr.length; i8++) {
                    this.f7661a[i8].d(bVarArr[i8], bVarArr2[i8], f5);
                }
                return this.f7661a;
            }
            throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
        }
    }

    private static Animator a(Context context, Resources resources, Resources.Theme theme, XmlPullParser xmlPullParser, float f5) {
        return b(context, resources, theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser), null, 0, f5);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.animation.Animator b(android.content.Context r18, android.content.res.Resources r19, android.content.res.Resources.Theme r20, org.xmlpull.v1.XmlPullParser r21, android.util.AttributeSet r22, android.animation.AnimatorSet r23, int r24, float r25) {
        /*
            Method dump skipped, instructions count: 263
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.vectordrawable.graphics.drawable.e.b(android.content.Context, android.content.res.Resources, android.content.res.Resources$Theme, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.animation.AnimatorSet, int, float):android.animation.Animator");
    }

    private static Keyframe c(Keyframe keyframe, float f5) {
        return keyframe.getType() == Float.TYPE ? Keyframe.ofFloat(f5) : keyframe.getType() == Integer.TYPE ? Keyframe.ofInt(f5) : Keyframe.ofObject(f5);
    }

    private static void d(Keyframe[] keyframeArr, float f5, int i8, int i9) {
        float f8 = f5 / ((i9 - i8) + 2);
        while (i8 <= i9) {
            keyframeArr[i8].setFraction(keyframeArr[i8 - 1].getFraction() + f8);
            i8++;
        }
    }

    private static PropertyValuesHolder e(TypedArray typedArray, int i8, int i9, int i10, String str) {
        PropertyValuesHolder ofFloat;
        PropertyValuesHolder ofObject;
        TypedValue peekValue = typedArray.peekValue(i9);
        boolean z4 = peekValue != null;
        int i11 = z4 ? peekValue.type : 0;
        TypedValue peekValue2 = typedArray.peekValue(i10);
        boolean z8 = peekValue2 != null;
        int i12 = z8 ? peekValue2.type : 0;
        if (i8 == 4) {
            i8 = ((z4 && h(i11)) || (z8 && h(i12))) ? 3 : 0;
        }
        boolean z9 = i8 == 0;
        PropertyValuesHolder propertyValuesHolder = null;
        if (i8 != 2) {
            f a9 = i8 == 3 ? f.a() : null;
            if (z9) {
                if (z4) {
                    float dimension = i11 == 5 ? typedArray.getDimension(i9, 0.0f) : typedArray.getFloat(i9, 0.0f);
                    if (z8) {
                        ofFloat = PropertyValuesHolder.ofFloat(str, dimension, i12 == 5 ? typedArray.getDimension(i10, 0.0f) : typedArray.getFloat(i10, 0.0f));
                    } else {
                        ofFloat = PropertyValuesHolder.ofFloat(str, dimension);
                    }
                } else {
                    ofFloat = PropertyValuesHolder.ofFloat(str, i12 == 5 ? typedArray.getDimension(i10, 0.0f) : typedArray.getFloat(i10, 0.0f));
                }
                propertyValuesHolder = ofFloat;
            } else if (z4) {
                int dimension2 = i11 == 5 ? (int) typedArray.getDimension(i9, 0.0f) : h(i11) ? typedArray.getColor(i9, 0) : typedArray.getInt(i9, 0);
                if (z8) {
                    propertyValuesHolder = PropertyValuesHolder.ofInt(str, dimension2, i12 == 5 ? (int) typedArray.getDimension(i10, 0.0f) : h(i12) ? typedArray.getColor(i10, 0) : typedArray.getInt(i10, 0));
                } else {
                    propertyValuesHolder = PropertyValuesHolder.ofInt(str, dimension2);
                }
            } else if (z8) {
                propertyValuesHolder = PropertyValuesHolder.ofInt(str, i12 == 5 ? (int) typedArray.getDimension(i10, 0.0f) : h(i12) ? typedArray.getColor(i10, 0) : typedArray.getInt(i10, 0));
            }
            if (propertyValuesHolder == null || a9 == null) {
                return propertyValuesHolder;
            }
            propertyValuesHolder.setEvaluator(a9);
            return propertyValuesHolder;
        }
        String string = typedArray.getString(i9);
        String string2 = typedArray.getString(i10);
        e.b[] d8 = androidx.core.graphics.e.d(string);
        e.b[] d9 = androidx.core.graphics.e.d(string2);
        if (d8 == null && d9 == null) {
            return null;
        }
        if (d8 == null) {
            if (d9 != null) {
                return PropertyValuesHolder.ofObject(str, new a(), d9);
            }
            return null;
        }
        a aVar = new a();
        if (d9 == null) {
            ofObject = PropertyValuesHolder.ofObject(str, aVar, d8);
        } else if (!androidx.core.graphics.e.b(d8, d9)) {
            throw new InflateException(" Can't morph from " + string + " to " + string2);
        } else {
            ofObject = PropertyValuesHolder.ofObject(str, aVar, d8, d9);
        }
        return ofObject;
    }

    private static int f(TypedArray typedArray, int i8, int i9) {
        TypedValue peekValue = typedArray.peekValue(i8);
        boolean z4 = peekValue != null;
        int i10 = z4 ? peekValue.type : 0;
        TypedValue peekValue2 = typedArray.peekValue(i9);
        boolean z8 = peekValue2 != null;
        return ((z4 && h(i10)) || (z8 && h(z8 ? peekValue2.type : 0))) ? 3 : 0;
    }

    private static int g(Resources resources, Resources.Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        TypedArray k8 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7645j);
        int i8 = 0;
        TypedValue l8 = k.l(k8, xmlPullParser, "value", 0);
        if ((l8 != null) && h(l8.type)) {
            i8 = 3;
        }
        k8.recycle();
        return i8;
    }

    private static boolean h(int i8) {
        return i8 >= 28 && i8 <= 31;
    }

    public static Animator i(Context context, int i8) {
        return Build.VERSION.SDK_INT >= 24 ? AnimatorInflater.loadAnimator(context, i8) : j(context, context.getResources(), context.getTheme(), i8);
    }

    public static Animator j(Context context, Resources resources, Resources.Theme theme, int i8) {
        return k(context, resources, theme, i8, 1.0f);
    }

    public static Animator k(Context context, Resources resources, Resources.Theme theme, int i8, float f5) {
        XmlResourceParser xmlResourceParser = null;
        try {
            try {
                try {
                    xmlResourceParser = resources.getAnimation(i8);
                    return a(context, resources, theme, xmlResourceParser, f5);
                } catch (XmlPullParserException e8) {
                    Resources.NotFoundException notFoundException = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(i8));
                    notFoundException.initCause(e8);
                    throw notFoundException;
                }
            } catch (IOException e9) {
                Resources.NotFoundException notFoundException2 = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(i8));
                notFoundException2.initCause(e9);
                throw notFoundException2;
            }
        } finally {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
        }
    }

    private static ValueAnimator l(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, ValueAnimator valueAnimator, float f5, XmlPullParser xmlPullParser) {
        TypedArray k8 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7642g);
        TypedArray k9 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7646k);
        if (valueAnimator == null) {
            valueAnimator = new ValueAnimator();
        }
        q(valueAnimator, k8, k9, f5, xmlPullParser);
        int h8 = k.h(k8, xmlPullParser, "interpolator", 0, 0);
        if (h8 > 0) {
            valueAnimator.setInterpolator(d.b(context, h8));
        }
        k8.recycle();
        if (k9 != null) {
            k9.recycle();
        }
        return valueAnimator;
    }

    private static Keyframe m(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, int i8, XmlPullParser xmlPullParser) {
        TypedArray k8 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7645j);
        float f5 = k.f(k8, xmlPullParser, "fraction", 3, -1.0f);
        TypedValue l8 = k.l(k8, xmlPullParser, "value", 0);
        boolean z4 = l8 != null;
        if (i8 == 4) {
            i8 = (z4 && h(l8.type)) ? 3 : 0;
        }
        Keyframe ofInt = z4 ? i8 != 0 ? (i8 == 1 || i8 == 3) ? Keyframe.ofInt(f5, k.g(k8, xmlPullParser, "value", 0, 0)) : null : Keyframe.ofFloat(f5, k.f(k8, xmlPullParser, "value", 0, 0.0f)) : i8 == 0 ? Keyframe.ofFloat(f5) : Keyframe.ofInt(f5);
        int h8 = k.h(k8, xmlPullParser, "interpolator", 1, 0);
        if (h8 > 0) {
            ofInt.setInterpolator(d.b(context, h8));
        }
        k8.recycle();
        return ofInt;
    }

    private static ObjectAnimator n(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, float f5, XmlPullParser xmlPullParser) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        l(context, resources, theme, attributeSet, objectAnimator, f5, xmlPullParser);
        return objectAnimator;
    }

    private static PropertyValuesHolder o(Context context, Resources resources, Resources.Theme theme, XmlPullParser xmlPullParser, String str, int i8) {
        int size;
        PropertyValuesHolder propertyValuesHolder = null;
        ArrayList arrayList = null;
        while (true) {
            int next = xmlPullParser.next();
            if (next == 3 || next == 1) {
                break;
            } else if (xmlPullParser.getName().equals("keyframe")) {
                if (i8 == 4) {
                    i8 = g(resources, theme, Xml.asAttributeSet(xmlPullParser), xmlPullParser);
                }
                Keyframe m8 = m(context, resources, theme, Xml.asAttributeSet(xmlPullParser), i8, xmlPullParser);
                if (m8 != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(m8);
                }
                xmlPullParser.next();
            }
        }
        if (arrayList != null && (size = arrayList.size()) > 0) {
            Keyframe keyframe = (Keyframe) arrayList.get(0);
            Keyframe keyframe2 = (Keyframe) arrayList.get(size - 1);
            float fraction = keyframe2.getFraction();
            if (fraction < 1.0f) {
                if (fraction < 0.0f) {
                    keyframe2.setFraction(1.0f);
                } else {
                    arrayList.add(arrayList.size(), c(keyframe2, 1.0f));
                    size++;
                }
            }
            float fraction2 = keyframe.getFraction();
            if (fraction2 != 0.0f) {
                if (fraction2 < 0.0f) {
                    keyframe.setFraction(0.0f);
                } else {
                    arrayList.add(0, c(keyframe, 0.0f));
                    size++;
                }
            }
            Keyframe[] keyframeArr = new Keyframe[size];
            arrayList.toArray(keyframeArr);
            for (int i9 = 0; i9 < size; i9++) {
                Keyframe keyframe3 = keyframeArr[i9];
                if (keyframe3.getFraction() < 0.0f) {
                    if (i9 == 0) {
                        keyframe3.setFraction(0.0f);
                    } else {
                        int i10 = size - 1;
                        if (i9 == i10) {
                            keyframe3.setFraction(1.0f);
                        } else {
                            int i11 = i9;
                            for (int i12 = i9 + 1; i12 < i10 && keyframeArr[i12].getFraction() < 0.0f; i12++) {
                                i11 = i12;
                            }
                            d(keyframeArr, keyframeArr[i11 + 1].getFraction() - keyframeArr[i9 - 1].getFraction(), i9, i11);
                        }
                    }
                }
            }
            propertyValuesHolder = PropertyValuesHolder.ofKeyframe(str, keyframeArr);
            if (i8 == 3) {
                propertyValuesHolder.setEvaluator(f.a());
            }
        }
        return propertyValuesHolder;
    }

    private static PropertyValuesHolder[] p(Context context, Resources resources, Resources.Theme theme, XmlPullParser xmlPullParser, AttributeSet attributeSet) {
        int i8;
        PropertyValuesHolder[] propertyValuesHolderArr = null;
        ArrayList arrayList = null;
        while (true) {
            int eventType = xmlPullParser.getEventType();
            if (eventType == 3 || eventType == 1) {
                break;
            }
            if (eventType == 2 && xmlPullParser.getName().equals("propertyValuesHolder")) {
                TypedArray k8 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7644i);
                String i9 = k.i(k8, xmlPullParser, "propertyName", 3);
                int g8 = k.g(k8, xmlPullParser, "valueType", 2, 4);
                PropertyValuesHolder o5 = o(context, resources, theme, xmlPullParser, i9, g8);
                if (o5 == null) {
                    o5 = e(k8, g8, 0, 1, i9);
                }
                if (o5 != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(o5);
                }
                k8.recycle();
            }
            xmlPullParser.next();
        }
        if (arrayList != null) {
            int size = arrayList.size();
            propertyValuesHolderArr = new PropertyValuesHolder[size];
            for (i8 = 0; i8 < size; i8++) {
                propertyValuesHolderArr[i8] = (PropertyValuesHolder) arrayList.get(i8);
            }
        }
        return propertyValuesHolderArr;
    }

    private static void q(ValueAnimator valueAnimator, TypedArray typedArray, TypedArray typedArray2, float f5, XmlPullParser xmlPullParser) {
        long g8 = k.g(typedArray, xmlPullParser, "duration", 1, 300);
        long g9 = k.g(typedArray, xmlPullParser, "startOffset", 2, 0);
        int g10 = k.g(typedArray, xmlPullParser, "valueType", 7, 4);
        if (k.j(xmlPullParser, "valueFrom") && k.j(xmlPullParser, "valueTo")) {
            if (g10 == 4) {
                g10 = f(typedArray, 5, 6);
            }
            PropertyValuesHolder e8 = e(typedArray, g10, 5, 6, BuildConfig.FLAVOR);
            if (e8 != null) {
                valueAnimator.setValues(e8);
            }
        }
        valueAnimator.setDuration(g8);
        valueAnimator.setStartDelay(g9);
        valueAnimator.setRepeatCount(k.g(typedArray, xmlPullParser, "repeatCount", 3, 0));
        valueAnimator.setRepeatMode(k.g(typedArray, xmlPullParser, "repeatMode", 4, 1));
        if (typedArray2 != null) {
            r(valueAnimator, typedArray2, g10, f5, xmlPullParser);
        }
    }

    private static void r(ValueAnimator valueAnimator, TypedArray typedArray, int i8, float f5, XmlPullParser xmlPullParser) {
        ObjectAnimator objectAnimator = (ObjectAnimator) valueAnimator;
        String i9 = k.i(typedArray, xmlPullParser, "pathData", 1);
        if (i9 == null) {
            objectAnimator.setPropertyName(k.i(typedArray, xmlPullParser, "propertyName", 0));
            return;
        }
        String i10 = k.i(typedArray, xmlPullParser, "propertyXName", 2);
        String i11 = k.i(typedArray, xmlPullParser, "propertyYName", 3);
        if (i8 != 2) {
        }
        if (i10 != null || i11 != null) {
            s(androidx.core.graphics.e.e(i9), objectAnimator, f5 * 0.5f, i10, i11);
            return;
        }
        throw new InflateException(typedArray.getPositionDescription() + " propertyXName or propertyYName is needed for PathData");
    }

    private static void s(Path path, ObjectAnimator objectAnimator, float f5, String str, String str2) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        ArrayList arrayList = new ArrayList();
        float f8 = 0.0f;
        arrayList.add(Float.valueOf(0.0f));
        float f9 = 0.0f;
        do {
            f9 += pathMeasure.getLength();
            arrayList.add(Float.valueOf(f9));
        } while (pathMeasure.nextContour());
        PathMeasure pathMeasure2 = new PathMeasure(path, false);
        int min = Math.min(100, ((int) (f9 / f5)) + 1);
        float[] fArr = new float[min];
        float[] fArr2 = new float[min];
        float[] fArr3 = new float[2];
        float f10 = f9 / (min - 1);
        int i8 = 0;
        int i9 = 0;
        while (true) {
            if (i8 >= min) {
                break;
            }
            pathMeasure2.getPosTan(f8 - ((Float) arrayList.get(i9)).floatValue(), fArr3, null);
            fArr[i8] = fArr3[0];
            fArr2[i8] = fArr3[1];
            f8 += f10;
            int i10 = i9 + 1;
            if (i10 < arrayList.size() && f8 > ((Float) arrayList.get(i10)).floatValue()) {
                pathMeasure2.nextContour();
                i9 = i10;
            }
            i8++;
        }
        PropertyValuesHolder ofFloat = str != null ? PropertyValuesHolder.ofFloat(str, fArr) : null;
        PropertyValuesHolder ofFloat2 = str2 != null ? PropertyValuesHolder.ofFloat(str2, fArr2) : null;
        if (ofFloat == null) {
            objectAnimator.setValues(ofFloat2);
        } else if (ofFloat2 == null) {
            objectAnimator.setValues(ofFloat);
        } else {
            objectAnimator.setValues(ofFloat, ofFloat2);
        }
    }
}
