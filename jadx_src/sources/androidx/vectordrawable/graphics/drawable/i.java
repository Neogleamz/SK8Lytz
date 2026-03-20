package androidx.vectordrawable.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import androidx.core.content.res.k;
import androidx.core.graphics.e;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i extends androidx.vectordrawable.graphics.drawable.h {

    /* renamed from: l  reason: collision with root package name */
    static final PorterDuff.Mode f7666l = PorterDuff.Mode.SRC_IN;

    /* renamed from: b  reason: collision with root package name */
    private h f7667b;

    /* renamed from: c  reason: collision with root package name */
    private PorterDuffColorFilter f7668c;

    /* renamed from: d  reason: collision with root package name */
    private ColorFilter f7669d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f7670e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f7671f;

    /* renamed from: g  reason: collision with root package name */
    private Drawable.ConstantState f7672g;

    /* renamed from: h  reason: collision with root package name */
    private final float[] f7673h;

    /* renamed from: j  reason: collision with root package name */
    private final Matrix f7674j;

    /* renamed from: k  reason: collision with root package name */
    private final Rect f7675k;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends f {
        b() {
        }

        b(b bVar) {
            super(bVar);
        }

        private void f(TypedArray typedArray, XmlPullParser xmlPullParser) {
            String string = typedArray.getString(0);
            if (string != null) {
                this.f7702b = string;
            }
            String string2 = typedArray.getString(1);
            if (string2 != null) {
                this.f7701a = androidx.core.graphics.e.d(string2);
            }
            this.f7703c = k.g(typedArray, xmlPullParser, "fillType", 2, 0);
        }

        @Override // androidx.vectordrawable.graphics.drawable.i.f
        public boolean c() {
            return true;
        }

        public void e(Resources resources, AttributeSet attributeSet, Resources.Theme theme, XmlPullParser xmlPullParser) {
            if (k.j(xmlPullParser, "pathData")) {
                TypedArray k8 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7639d);
                f(k8, xmlPullParser);
                k8.recycle();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends f {

        /* renamed from: e  reason: collision with root package name */
        private int[] f7676e;

        /* renamed from: f  reason: collision with root package name */
        androidx.core.content.res.d f7677f;

        /* renamed from: g  reason: collision with root package name */
        float f7678g;

        /* renamed from: h  reason: collision with root package name */
        androidx.core.content.res.d f7679h;

        /* renamed from: i  reason: collision with root package name */
        float f7680i;

        /* renamed from: j  reason: collision with root package name */
        float f7681j;

        /* renamed from: k  reason: collision with root package name */
        float f7682k;

        /* renamed from: l  reason: collision with root package name */
        float f7683l;

        /* renamed from: m  reason: collision with root package name */
        float f7684m;

        /* renamed from: n  reason: collision with root package name */
        Paint.Cap f7685n;

        /* renamed from: o  reason: collision with root package name */
        Paint.Join f7686o;

        /* renamed from: p  reason: collision with root package name */
        float f7687p;

        c() {
            this.f7678g = 0.0f;
            this.f7680i = 1.0f;
            this.f7681j = 1.0f;
            this.f7682k = 0.0f;
            this.f7683l = 1.0f;
            this.f7684m = 0.0f;
            this.f7685n = Paint.Cap.BUTT;
            this.f7686o = Paint.Join.MITER;
            this.f7687p = 4.0f;
        }

        c(c cVar) {
            super(cVar);
            this.f7678g = 0.0f;
            this.f7680i = 1.0f;
            this.f7681j = 1.0f;
            this.f7682k = 0.0f;
            this.f7683l = 1.0f;
            this.f7684m = 0.0f;
            this.f7685n = Paint.Cap.BUTT;
            this.f7686o = Paint.Join.MITER;
            this.f7687p = 4.0f;
            this.f7676e = cVar.f7676e;
            this.f7677f = cVar.f7677f;
            this.f7678g = cVar.f7678g;
            this.f7680i = cVar.f7680i;
            this.f7679h = cVar.f7679h;
            this.f7703c = cVar.f7703c;
            this.f7681j = cVar.f7681j;
            this.f7682k = cVar.f7682k;
            this.f7683l = cVar.f7683l;
            this.f7684m = cVar.f7684m;
            this.f7685n = cVar.f7685n;
            this.f7686o = cVar.f7686o;
            this.f7687p = cVar.f7687p;
        }

        private Paint.Cap e(int i8, Paint.Cap cap) {
            return i8 != 0 ? i8 != 1 ? i8 != 2 ? cap : Paint.Cap.SQUARE : Paint.Cap.ROUND : Paint.Cap.BUTT;
        }

        private Paint.Join f(int i8, Paint.Join join) {
            return i8 != 0 ? i8 != 1 ? i8 != 2 ? join : Paint.Join.BEVEL : Paint.Join.ROUND : Paint.Join.MITER;
        }

        private void h(TypedArray typedArray, XmlPullParser xmlPullParser, Resources.Theme theme) {
            this.f7676e = null;
            if (k.j(xmlPullParser, "pathData")) {
                String string = typedArray.getString(0);
                if (string != null) {
                    this.f7702b = string;
                }
                String string2 = typedArray.getString(2);
                if (string2 != null) {
                    this.f7701a = androidx.core.graphics.e.d(string2);
                }
                this.f7679h = k.e(typedArray, xmlPullParser, theme, "fillColor", 1, 0);
                this.f7681j = k.f(typedArray, xmlPullParser, "fillAlpha", 12, this.f7681j);
                this.f7685n = e(k.g(typedArray, xmlPullParser, "strokeLineCap", 8, -1), this.f7685n);
                this.f7686o = f(k.g(typedArray, xmlPullParser, "strokeLineJoin", 9, -1), this.f7686o);
                this.f7687p = k.f(typedArray, xmlPullParser, "strokeMiterLimit", 10, this.f7687p);
                this.f7677f = k.e(typedArray, xmlPullParser, theme, "strokeColor", 3, 0);
                this.f7680i = k.f(typedArray, xmlPullParser, "strokeAlpha", 11, this.f7680i);
                this.f7678g = k.f(typedArray, xmlPullParser, "strokeWidth", 4, this.f7678g);
                this.f7683l = k.f(typedArray, xmlPullParser, "trimPathEnd", 6, this.f7683l);
                this.f7684m = k.f(typedArray, xmlPullParser, "trimPathOffset", 7, this.f7684m);
                this.f7682k = k.f(typedArray, xmlPullParser, "trimPathStart", 5, this.f7682k);
                this.f7703c = k.g(typedArray, xmlPullParser, "fillType", 13, this.f7703c);
            }
        }

        @Override // androidx.vectordrawable.graphics.drawable.i.e
        public boolean a() {
            return this.f7679h.i() || this.f7677f.i();
        }

        @Override // androidx.vectordrawable.graphics.drawable.i.e
        public boolean b(int[] iArr) {
            return this.f7677f.j(iArr) | this.f7679h.j(iArr);
        }

        public void g(Resources resources, AttributeSet attributeSet, Resources.Theme theme, XmlPullParser xmlPullParser) {
            TypedArray k8 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7638c);
            h(k8, xmlPullParser, theme);
            k8.recycle();
        }

        float getFillAlpha() {
            return this.f7681j;
        }

        int getFillColor() {
            return this.f7679h.e();
        }

        float getStrokeAlpha() {
            return this.f7680i;
        }

        int getStrokeColor() {
            return this.f7677f.e();
        }

        float getStrokeWidth() {
            return this.f7678g;
        }

        float getTrimPathEnd() {
            return this.f7683l;
        }

        float getTrimPathOffset() {
            return this.f7684m;
        }

        float getTrimPathStart() {
            return this.f7682k;
        }

        void setFillAlpha(float f5) {
            this.f7681j = f5;
        }

        void setFillColor(int i8) {
            this.f7679h.k(i8);
        }

        void setStrokeAlpha(float f5) {
            this.f7680i = f5;
        }

        void setStrokeColor(int i8) {
            this.f7677f.k(i8);
        }

        void setStrokeWidth(float f5) {
            this.f7678g = f5;
        }

        void setTrimPathEnd(float f5) {
            this.f7683l = f5;
        }

        void setTrimPathOffset(float f5) {
            this.f7684m = f5;
        }

        void setTrimPathStart(float f5) {
            this.f7682k = f5;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends e {

        /* renamed from: a  reason: collision with root package name */
        final Matrix f7688a;

        /* renamed from: b  reason: collision with root package name */
        final ArrayList<e> f7689b;

        /* renamed from: c  reason: collision with root package name */
        float f7690c;

        /* renamed from: d  reason: collision with root package name */
        private float f7691d;

        /* renamed from: e  reason: collision with root package name */
        private float f7692e;

        /* renamed from: f  reason: collision with root package name */
        private float f7693f;

        /* renamed from: g  reason: collision with root package name */
        private float f7694g;

        /* renamed from: h  reason: collision with root package name */
        private float f7695h;

        /* renamed from: i  reason: collision with root package name */
        private float f7696i;

        /* renamed from: j  reason: collision with root package name */
        final Matrix f7697j;

        /* renamed from: k  reason: collision with root package name */
        int f7698k;

        /* renamed from: l  reason: collision with root package name */
        private int[] f7699l;

        /* renamed from: m  reason: collision with root package name */
        private String f7700m;

        public d() {
            super();
            this.f7688a = new Matrix();
            this.f7689b = new ArrayList<>();
            this.f7690c = 0.0f;
            this.f7691d = 0.0f;
            this.f7692e = 0.0f;
            this.f7693f = 1.0f;
            this.f7694g = 1.0f;
            this.f7695h = 0.0f;
            this.f7696i = 0.0f;
            this.f7697j = new Matrix();
            this.f7700m = null;
        }

        public d(d dVar, k0.a<String, Object> aVar) {
            super();
            f bVar;
            this.f7688a = new Matrix();
            this.f7689b = new ArrayList<>();
            this.f7690c = 0.0f;
            this.f7691d = 0.0f;
            this.f7692e = 0.0f;
            this.f7693f = 1.0f;
            this.f7694g = 1.0f;
            this.f7695h = 0.0f;
            this.f7696i = 0.0f;
            Matrix matrix = new Matrix();
            this.f7697j = matrix;
            this.f7700m = null;
            this.f7690c = dVar.f7690c;
            this.f7691d = dVar.f7691d;
            this.f7692e = dVar.f7692e;
            this.f7693f = dVar.f7693f;
            this.f7694g = dVar.f7694g;
            this.f7695h = dVar.f7695h;
            this.f7696i = dVar.f7696i;
            this.f7699l = dVar.f7699l;
            String str = dVar.f7700m;
            this.f7700m = str;
            this.f7698k = dVar.f7698k;
            if (str != null) {
                aVar.put(str, this);
            }
            matrix.set(dVar.f7697j);
            ArrayList<e> arrayList = dVar.f7689b;
            for (int i8 = 0; i8 < arrayList.size(); i8++) {
                e eVar = arrayList.get(i8);
                if (eVar instanceof d) {
                    this.f7689b.add(new d((d) eVar, aVar));
                } else {
                    if (eVar instanceof c) {
                        bVar = new c((c) eVar);
                    } else if (!(eVar instanceof b)) {
                        throw new IllegalStateException("Unknown object in the tree!");
                    } else {
                        bVar = new b((b) eVar);
                    }
                    this.f7689b.add(bVar);
                    String str2 = bVar.f7702b;
                    if (str2 != null) {
                        aVar.put(str2, bVar);
                    }
                }
            }
        }

        private void d() {
            this.f7697j.reset();
            this.f7697j.postTranslate(-this.f7691d, -this.f7692e);
            this.f7697j.postScale(this.f7693f, this.f7694g);
            this.f7697j.postRotate(this.f7690c, 0.0f, 0.0f);
            this.f7697j.postTranslate(this.f7695h + this.f7691d, this.f7696i + this.f7692e);
        }

        private void e(TypedArray typedArray, XmlPullParser xmlPullParser) {
            this.f7699l = null;
            this.f7690c = k.f(typedArray, xmlPullParser, "rotation", 5, this.f7690c);
            this.f7691d = typedArray.getFloat(1, this.f7691d);
            this.f7692e = typedArray.getFloat(2, this.f7692e);
            this.f7693f = k.f(typedArray, xmlPullParser, "scaleX", 3, this.f7693f);
            this.f7694g = k.f(typedArray, xmlPullParser, "scaleY", 4, this.f7694g);
            this.f7695h = k.f(typedArray, xmlPullParser, "translateX", 6, this.f7695h);
            this.f7696i = k.f(typedArray, xmlPullParser, "translateY", 7, this.f7696i);
            String string = typedArray.getString(0);
            if (string != null) {
                this.f7700m = string;
            }
            d();
        }

        @Override // androidx.vectordrawable.graphics.drawable.i.e
        public boolean a() {
            for (int i8 = 0; i8 < this.f7689b.size(); i8++) {
                if (this.f7689b.get(i8).a()) {
                    return true;
                }
            }
            return false;
        }

        @Override // androidx.vectordrawable.graphics.drawable.i.e
        public boolean b(int[] iArr) {
            boolean z4 = false;
            for (int i8 = 0; i8 < this.f7689b.size(); i8++) {
                z4 |= this.f7689b.get(i8).b(iArr);
            }
            return z4;
        }

        public void c(Resources resources, AttributeSet attributeSet, Resources.Theme theme, XmlPullParser xmlPullParser) {
            TypedArray k8 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7637b);
            e(k8, xmlPullParser);
            k8.recycle();
        }

        public String getGroupName() {
            return this.f7700m;
        }

        public Matrix getLocalMatrix() {
            return this.f7697j;
        }

        public float getPivotX() {
            return this.f7691d;
        }

        public float getPivotY() {
            return this.f7692e;
        }

        public float getRotation() {
            return this.f7690c;
        }

        public float getScaleX() {
            return this.f7693f;
        }

        public float getScaleY() {
            return this.f7694g;
        }

        public float getTranslateX() {
            return this.f7695h;
        }

        public float getTranslateY() {
            return this.f7696i;
        }

        public void setPivotX(float f5) {
            if (f5 != this.f7691d) {
                this.f7691d = f5;
                d();
            }
        }

        public void setPivotY(float f5) {
            if (f5 != this.f7692e) {
                this.f7692e = f5;
                d();
            }
        }

        public void setRotation(float f5) {
            if (f5 != this.f7690c) {
                this.f7690c = f5;
                d();
            }
        }

        public void setScaleX(float f5) {
            if (f5 != this.f7693f) {
                this.f7693f = f5;
                d();
            }
        }

        public void setScaleY(float f5) {
            if (f5 != this.f7694g) {
                this.f7694g = f5;
                d();
            }
        }

        public void setTranslateX(float f5) {
            if (f5 != this.f7695h) {
                this.f7695h = f5;
                d();
            }
        }

        public void setTranslateY(float f5) {
            if (f5 != this.f7696i) {
                this.f7696i = f5;
                d();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class e {
        private e() {
        }

        public boolean a() {
            return false;
        }

        public boolean b(int[] iArr) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class f extends e {

        /* renamed from: a  reason: collision with root package name */
        protected e.b[] f7701a;

        /* renamed from: b  reason: collision with root package name */
        String f7702b;

        /* renamed from: c  reason: collision with root package name */
        int f7703c;

        /* renamed from: d  reason: collision with root package name */
        int f7704d;

        public f() {
            super();
            this.f7701a = null;
            this.f7703c = 0;
        }

        public f(f fVar) {
            super();
            this.f7701a = null;
            this.f7703c = 0;
            this.f7702b = fVar.f7702b;
            this.f7704d = fVar.f7704d;
            this.f7701a = androidx.core.graphics.e.f(fVar.f7701a);
        }

        public boolean c() {
            return false;
        }

        public void d(Path path) {
            path.reset();
            e.b[] bVarArr = this.f7701a;
            if (bVarArr != null) {
                e.b.e(bVarArr, path);
            }
        }

        public e.b[] getPathData() {
            return this.f7701a;
        }

        public String getPathName() {
            return this.f7702b;
        }

        public void setPathData(e.b[] bVarArr) {
            if (androidx.core.graphics.e.b(this.f7701a, bVarArr)) {
                androidx.core.graphics.e.j(this.f7701a, bVarArr);
            } else {
                this.f7701a = androidx.core.graphics.e.f(bVarArr);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g {
        private static final Matrix q = new Matrix();

        /* renamed from: a  reason: collision with root package name */
        private final Path f7705a;

        /* renamed from: b  reason: collision with root package name */
        private final Path f7706b;

        /* renamed from: c  reason: collision with root package name */
        private final Matrix f7707c;

        /* renamed from: d  reason: collision with root package name */
        Paint f7708d;

        /* renamed from: e  reason: collision with root package name */
        Paint f7709e;

        /* renamed from: f  reason: collision with root package name */
        private PathMeasure f7710f;

        /* renamed from: g  reason: collision with root package name */
        private int f7711g;

        /* renamed from: h  reason: collision with root package name */
        final d f7712h;

        /* renamed from: i  reason: collision with root package name */
        float f7713i;

        /* renamed from: j  reason: collision with root package name */
        float f7714j;

        /* renamed from: k  reason: collision with root package name */
        float f7715k;

        /* renamed from: l  reason: collision with root package name */
        float f7716l;

        /* renamed from: m  reason: collision with root package name */
        int f7717m;

        /* renamed from: n  reason: collision with root package name */
        String f7718n;

        /* renamed from: o  reason: collision with root package name */
        Boolean f7719o;

        /* renamed from: p  reason: collision with root package name */
        final k0.a<String, Object> f7720p;

        public g() {
            this.f7707c = new Matrix();
            this.f7713i = 0.0f;
            this.f7714j = 0.0f;
            this.f7715k = 0.0f;
            this.f7716l = 0.0f;
            this.f7717m = 255;
            this.f7718n = null;
            this.f7719o = null;
            this.f7720p = new k0.a<>();
            this.f7712h = new d();
            this.f7705a = new Path();
            this.f7706b = new Path();
        }

        public g(g gVar) {
            this.f7707c = new Matrix();
            this.f7713i = 0.0f;
            this.f7714j = 0.0f;
            this.f7715k = 0.0f;
            this.f7716l = 0.0f;
            this.f7717m = 255;
            this.f7718n = null;
            this.f7719o = null;
            k0.a<String, Object> aVar = new k0.a<>();
            this.f7720p = aVar;
            this.f7712h = new d(gVar.f7712h, aVar);
            this.f7705a = new Path(gVar.f7705a);
            this.f7706b = new Path(gVar.f7706b);
            this.f7713i = gVar.f7713i;
            this.f7714j = gVar.f7714j;
            this.f7715k = gVar.f7715k;
            this.f7716l = gVar.f7716l;
            this.f7711g = gVar.f7711g;
            this.f7717m = gVar.f7717m;
            this.f7718n = gVar.f7718n;
            String str = gVar.f7718n;
            if (str != null) {
                aVar.put(str, this);
            }
            this.f7719o = gVar.f7719o;
        }

        private static float a(float f5, float f8, float f9, float f10) {
            return (f5 * f10) - (f8 * f9);
        }

        private void c(d dVar, Matrix matrix, Canvas canvas, int i8, int i9, ColorFilter colorFilter) {
            dVar.f7688a.set(matrix);
            dVar.f7688a.preConcat(dVar.f7697j);
            canvas.save();
            for (int i10 = 0; i10 < dVar.f7689b.size(); i10++) {
                e eVar = dVar.f7689b.get(i10);
                if (eVar instanceof d) {
                    c((d) eVar, dVar.f7688a, canvas, i8, i9, colorFilter);
                } else if (eVar instanceof f) {
                    d(dVar, (f) eVar, canvas, i8, i9, colorFilter);
                }
            }
            canvas.restore();
        }

        private void d(d dVar, f fVar, Canvas canvas, int i8, int i9, ColorFilter colorFilter) {
            float f5 = i8 / this.f7715k;
            float f8 = i9 / this.f7716l;
            float min = Math.min(f5, f8);
            Matrix matrix = dVar.f7688a;
            this.f7707c.set(matrix);
            this.f7707c.postScale(f5, f8);
            float e8 = e(matrix);
            if (e8 == 0.0f) {
                return;
            }
            fVar.d(this.f7705a);
            Path path = this.f7705a;
            this.f7706b.reset();
            if (fVar.c()) {
                this.f7706b.setFillType(fVar.f7703c == 0 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD);
                this.f7706b.addPath(path, this.f7707c);
                canvas.clipPath(this.f7706b);
                return;
            }
            c cVar = (c) fVar;
            float f9 = cVar.f7682k;
            if (f9 != 0.0f || cVar.f7683l != 1.0f) {
                float f10 = cVar.f7684m;
                float f11 = (f9 + f10) % 1.0f;
                float f12 = (cVar.f7683l + f10) % 1.0f;
                if (this.f7710f == null) {
                    this.f7710f = new PathMeasure();
                }
                this.f7710f.setPath(this.f7705a, false);
                float length = this.f7710f.getLength();
                float f13 = f11 * length;
                float f14 = f12 * length;
                path.reset();
                if (f13 > f14) {
                    this.f7710f.getSegment(f13, length, path, true);
                    this.f7710f.getSegment(0.0f, f14, path, true);
                } else {
                    this.f7710f.getSegment(f13, f14, path, true);
                }
                path.rLineTo(0.0f, 0.0f);
            }
            this.f7706b.addPath(path, this.f7707c);
            if (cVar.f7679h.l()) {
                androidx.core.content.res.d dVar2 = cVar.f7679h;
                if (this.f7709e == null) {
                    Paint paint = new Paint(1);
                    this.f7709e = paint;
                    paint.setStyle(Paint.Style.FILL);
                }
                Paint paint2 = this.f7709e;
                if (dVar2.h()) {
                    Shader f15 = dVar2.f();
                    f15.setLocalMatrix(this.f7707c);
                    paint2.setShader(f15);
                    paint2.setAlpha(Math.round(cVar.f7681j * 255.0f));
                } else {
                    paint2.setShader(null);
                    paint2.setAlpha(255);
                    paint2.setColor(i.a(dVar2.e(), cVar.f7681j));
                }
                paint2.setColorFilter(colorFilter);
                this.f7706b.setFillType(cVar.f7703c == 0 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD);
                canvas.drawPath(this.f7706b, paint2);
            }
            if (cVar.f7677f.l()) {
                androidx.core.content.res.d dVar3 = cVar.f7677f;
                if (this.f7708d == null) {
                    Paint paint3 = new Paint(1);
                    this.f7708d = paint3;
                    paint3.setStyle(Paint.Style.STROKE);
                }
                Paint paint4 = this.f7708d;
                Paint.Join join = cVar.f7686o;
                if (join != null) {
                    paint4.setStrokeJoin(join);
                }
                Paint.Cap cap = cVar.f7685n;
                if (cap != null) {
                    paint4.setStrokeCap(cap);
                }
                paint4.setStrokeMiter(cVar.f7687p);
                if (dVar3.h()) {
                    Shader f16 = dVar3.f();
                    f16.setLocalMatrix(this.f7707c);
                    paint4.setShader(f16);
                    paint4.setAlpha(Math.round(cVar.f7680i * 255.0f));
                } else {
                    paint4.setShader(null);
                    paint4.setAlpha(255);
                    paint4.setColor(i.a(dVar3.e(), cVar.f7680i));
                }
                paint4.setColorFilter(colorFilter);
                paint4.setStrokeWidth(cVar.f7678g * min * e8);
                canvas.drawPath(this.f7706b, paint4);
            }
        }

        private float e(Matrix matrix) {
            float[] fArr = {0.0f, 1.0f, 1.0f, 0.0f};
            matrix.mapVectors(fArr);
            float a9 = a(fArr[0], fArr[1], fArr[2], fArr[3]);
            float max = Math.max((float) Math.hypot(fArr[0], fArr[1]), (float) Math.hypot(fArr[2], fArr[3]));
            if (max > 0.0f) {
                return Math.abs(a9) / max;
            }
            return 0.0f;
        }

        public void b(Canvas canvas, int i8, int i9, ColorFilter colorFilter) {
            c(this.f7712h, q, canvas, i8, i9, colorFilter);
        }

        public boolean f() {
            if (this.f7719o == null) {
                this.f7719o = Boolean.valueOf(this.f7712h.a());
            }
            return this.f7719o.booleanValue();
        }

        public boolean g(int[] iArr) {
            return this.f7712h.b(iArr);
        }

        public float getAlpha() {
            return getRootAlpha() / 255.0f;
        }

        public int getRootAlpha() {
            return this.f7717m;
        }

        public void setAlpha(float f5) {
            setRootAlpha((int) (f5 * 255.0f));
        }

        public void setRootAlpha(int i8) {
            this.f7717m = i8;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h extends Drawable.ConstantState {

        /* renamed from: a  reason: collision with root package name */
        int f7721a;

        /* renamed from: b  reason: collision with root package name */
        g f7722b;

        /* renamed from: c  reason: collision with root package name */
        ColorStateList f7723c;

        /* renamed from: d  reason: collision with root package name */
        PorterDuff.Mode f7724d;

        /* renamed from: e  reason: collision with root package name */
        boolean f7725e;

        /* renamed from: f  reason: collision with root package name */
        Bitmap f7726f;

        /* renamed from: g  reason: collision with root package name */
        ColorStateList f7727g;

        /* renamed from: h  reason: collision with root package name */
        PorterDuff.Mode f7728h;

        /* renamed from: i  reason: collision with root package name */
        int f7729i;

        /* renamed from: j  reason: collision with root package name */
        boolean f7730j;

        /* renamed from: k  reason: collision with root package name */
        boolean f7731k;

        /* renamed from: l  reason: collision with root package name */
        Paint f7732l;

        public h() {
            this.f7723c = null;
            this.f7724d = i.f7666l;
            this.f7722b = new g();
        }

        public h(h hVar) {
            this.f7723c = null;
            this.f7724d = i.f7666l;
            if (hVar != null) {
                this.f7721a = hVar.f7721a;
                g gVar = new g(hVar.f7722b);
                this.f7722b = gVar;
                if (hVar.f7722b.f7709e != null) {
                    gVar.f7709e = new Paint(hVar.f7722b.f7709e);
                }
                if (hVar.f7722b.f7708d != null) {
                    this.f7722b.f7708d = new Paint(hVar.f7722b.f7708d);
                }
                this.f7723c = hVar.f7723c;
                this.f7724d = hVar.f7724d;
                this.f7725e = hVar.f7725e;
            }
        }

        public boolean a(int i8, int i9) {
            return i8 == this.f7726f.getWidth() && i9 == this.f7726f.getHeight();
        }

        public boolean b() {
            return !this.f7731k && this.f7727g == this.f7723c && this.f7728h == this.f7724d && this.f7730j == this.f7725e && this.f7729i == this.f7722b.getRootAlpha();
        }

        public void c(int i8, int i9) {
            if (this.f7726f == null || !a(i8, i9)) {
                this.f7726f = Bitmap.createBitmap(i8, i9, Bitmap.Config.ARGB_8888);
                this.f7731k = true;
            }
        }

        public void d(Canvas canvas, ColorFilter colorFilter, Rect rect) {
            canvas.drawBitmap(this.f7726f, (Rect) null, rect, e(colorFilter));
        }

        public Paint e(ColorFilter colorFilter) {
            if (f() || colorFilter != null) {
                if (this.f7732l == null) {
                    Paint paint = new Paint();
                    this.f7732l = paint;
                    paint.setFilterBitmap(true);
                }
                this.f7732l.setAlpha(this.f7722b.getRootAlpha());
                this.f7732l.setColorFilter(colorFilter);
                return this.f7732l;
            }
            return null;
        }

        public boolean f() {
            return this.f7722b.getRootAlpha() < 255;
        }

        public boolean g() {
            return this.f7722b.f();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.f7721a;
        }

        public boolean h(int[] iArr) {
            boolean g8 = this.f7722b.g(iArr);
            this.f7731k |= g8;
            return g8;
        }

        public void i() {
            this.f7727g = this.f7723c;
            this.f7728h = this.f7724d;
            this.f7729i = this.f7722b.getRootAlpha();
            this.f7730j = this.f7725e;
            this.f7731k = false;
        }

        public void j(int i8, int i9) {
            this.f7726f.eraseColor(0);
            this.f7722b.b(new Canvas(this.f7726f), i8, i9, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new i(this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            return new i(this);
        }
    }

    /* renamed from: androidx.vectordrawable.graphics.drawable.i$i  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0086i extends Drawable.ConstantState {

        /* renamed from: a  reason: collision with root package name */
        private final Drawable.ConstantState f7733a;

        public C0086i(Drawable.ConstantState constantState) {
            this.f7733a = constantState;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.f7733a.canApplyTheme();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.f7733a.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            i iVar = new i();
            iVar.f7665a = (VectorDrawable) this.f7733a.newDrawable();
            return iVar;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            i iVar = new i();
            iVar.f7665a = (VectorDrawable) this.f7733a.newDrawable(resources);
            return iVar;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            i iVar = new i();
            iVar.f7665a = (VectorDrawable) this.f7733a.newDrawable(resources, theme);
            return iVar;
        }
    }

    i() {
        this.f7671f = true;
        this.f7673h = new float[9];
        this.f7674j = new Matrix();
        this.f7675k = new Rect();
        this.f7667b = new h();
    }

    i(h hVar) {
        this.f7671f = true;
        this.f7673h = new float[9];
        this.f7674j = new Matrix();
        this.f7675k = new Rect();
        this.f7667b = hVar;
        this.f7668c = j(this.f7668c, hVar.f7723c, hVar.f7724d);
    }

    static int a(int i8, float f5) {
        return (i8 & 16777215) | (((int) (Color.alpha(i8) * f5)) << 24);
    }

    public static i b(Resources resources, int i8, Resources.Theme theme) {
        int next;
        if (Build.VERSION.SDK_INT >= 24) {
            i iVar = new i();
            iVar.f7665a = androidx.core.content.res.h.e(resources, i8, theme);
            iVar.f7672g = new C0086i(iVar.f7665a.getConstantState());
            return iVar;
        }
        try {
            XmlResourceParser xml = resources.getXml(i8);
            AttributeSet asAttributeSet = Xml.asAttributeSet(xml);
            while (true) {
                next = xml.next();
                if (next == 2 || next == 1) {
                    break;
                }
            }
            if (next == 2) {
                return c(resources, xml, asAttributeSet, theme);
            }
            throw new XmlPullParserException("No start tag found");
        } catch (IOException | XmlPullParserException e8) {
            Log.e("VectorDrawableCompat", "parser error", e8);
            return null;
        }
    }

    public static i c(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        i iVar = new i();
        iVar.inflate(resources, xmlPullParser, attributeSet, theme);
        return iVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void e(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        int i8;
        int i9;
        b bVar;
        h hVar = this.f7667b;
        g gVar = hVar.f7722b;
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.push(gVar.f7712h);
        int eventType = xmlPullParser.getEventType();
        int depth = xmlPullParser.getDepth() + 1;
        boolean z4 = true;
        while (eventType != 1 && (xmlPullParser.getDepth() >= depth || eventType != 3)) {
            if (eventType == 2) {
                String name = xmlPullParser.getName();
                d dVar = (d) arrayDeque.peek();
                if ("path".equals(name)) {
                    c cVar = new c();
                    cVar.g(resources, attributeSet, theme, xmlPullParser);
                    dVar.f7689b.add(cVar);
                    if (cVar.getPathName() != null) {
                        gVar.f7720p.put(cVar.getPathName(), cVar);
                    }
                    z4 = false;
                    bVar = cVar;
                } else if ("clip-path".equals(name)) {
                    b bVar2 = new b();
                    bVar2.e(resources, attributeSet, theme, xmlPullParser);
                    dVar.f7689b.add(bVar2);
                    String pathName = bVar2.getPathName();
                    bVar = bVar2;
                    if (pathName != null) {
                        gVar.f7720p.put(bVar2.getPathName(), bVar2);
                        bVar = bVar2;
                    }
                } else if ("group".equals(name)) {
                    d dVar2 = new d();
                    dVar2.c(resources, attributeSet, theme, xmlPullParser);
                    dVar.f7689b.add(dVar2);
                    arrayDeque.push(dVar2);
                    if (dVar2.getGroupName() != null) {
                        gVar.f7720p.put(dVar2.getGroupName(), dVar2);
                    }
                    i8 = hVar.f7721a;
                    i9 = dVar2.f7698k;
                    hVar.f7721a = i9 | i8;
                }
                i8 = hVar.f7721a;
                i9 = bVar.f7704d;
                hVar.f7721a = i9 | i8;
            } else if (eventType == 3 && "group".equals(xmlPullParser.getName())) {
                arrayDeque.pop();
            }
            eventType = xmlPullParser.next();
        }
        if (z4) {
            throw new XmlPullParserException("no path defined");
        }
    }

    private boolean f() {
        return Build.VERSION.SDK_INT >= 17 && isAutoMirrored() && androidx.core.graphics.drawable.a.f(this) == 1;
    }

    private static PorterDuff.Mode g(int i8, PorterDuff.Mode mode) {
        if (i8 != 3) {
            if (i8 != 5) {
                if (i8 != 9) {
                    switch (i8) {
                        case 14:
                            return PorterDuff.Mode.MULTIPLY;
                        case 15:
                            return PorterDuff.Mode.SCREEN;
                        case 16:
                            return PorterDuff.Mode.ADD;
                        default:
                            return mode;
                    }
                }
                return PorterDuff.Mode.SRC_ATOP;
            }
            return PorterDuff.Mode.SRC_IN;
        }
        return PorterDuff.Mode.SRC_OVER;
    }

    private void i(TypedArray typedArray, XmlPullParser xmlPullParser, Resources.Theme theme) {
        h hVar = this.f7667b;
        g gVar = hVar.f7722b;
        hVar.f7724d = g(k.g(typedArray, xmlPullParser, "tintMode", 6, -1), PorterDuff.Mode.SRC_IN);
        ColorStateList c9 = k.c(typedArray, xmlPullParser, theme, "tint", 1);
        if (c9 != null) {
            hVar.f7723c = c9;
        }
        hVar.f7725e = k.a(typedArray, xmlPullParser, "autoMirrored", 5, hVar.f7725e);
        gVar.f7715k = k.f(typedArray, xmlPullParser, "viewportWidth", 7, gVar.f7715k);
        float f5 = k.f(typedArray, xmlPullParser, "viewportHeight", 8, gVar.f7716l);
        gVar.f7716l = f5;
        if (gVar.f7715k <= 0.0f) {
            throw new XmlPullParserException(typedArray.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
        } else if (f5 <= 0.0f) {
            throw new XmlPullParserException(typedArray.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
        } else {
            gVar.f7713i = typedArray.getDimension(3, gVar.f7713i);
            float dimension = typedArray.getDimension(2, gVar.f7714j);
            gVar.f7714j = dimension;
            if (gVar.f7713i <= 0.0f) {
                throw new XmlPullParserException(typedArray.getPositionDescription() + "<vector> tag requires width > 0");
            } else if (dimension <= 0.0f) {
                throw new XmlPullParserException(typedArray.getPositionDescription() + "<vector> tag requires height > 0");
            } else {
                gVar.setAlpha(k.f(typedArray, xmlPullParser, "alpha", 4, gVar.getAlpha()));
                String string = typedArray.getString(0);
                if (string != null) {
                    gVar.f7718n = string;
                    gVar.f7720p.put(string, gVar);
                }
            }
        }
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.b(drawable);
            return false;
        }
        return false;
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void clearColorFilter() {
        super.clearColorFilter();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object d(String str) {
        return this.f7667b.f7722b.f7720p.get(str);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.draw(canvas);
            return;
        }
        copyBounds(this.f7675k);
        if (this.f7675k.width() <= 0 || this.f7675k.height() <= 0) {
            return;
        }
        ColorFilter colorFilter = this.f7669d;
        if (colorFilter == null) {
            colorFilter = this.f7668c;
        }
        canvas.getMatrix(this.f7674j);
        this.f7674j.getValues(this.f7673h);
        float abs = Math.abs(this.f7673h[0]);
        float abs2 = Math.abs(this.f7673h[4]);
        float abs3 = Math.abs(this.f7673h[1]);
        float abs4 = Math.abs(this.f7673h[3]);
        if (abs3 != 0.0f || abs4 != 0.0f) {
            abs = 1.0f;
            abs2 = 1.0f;
        }
        int min = Math.min((int) RecognitionOptions.PDF417, (int) (this.f7675k.width() * abs));
        int min2 = Math.min((int) RecognitionOptions.PDF417, (int) (this.f7675k.height() * abs2));
        if (min <= 0 || min2 <= 0) {
            return;
        }
        int save = canvas.save();
        Rect rect = this.f7675k;
        canvas.translate(rect.left, rect.top);
        if (f()) {
            canvas.translate(this.f7675k.width(), 0.0f);
            canvas.scale(-1.0f, 1.0f);
        }
        this.f7675k.offsetTo(0, 0);
        this.f7667b.c(min, min2);
        if (!this.f7671f) {
            this.f7667b.j(min, min2);
        } else if (!this.f7667b.b()) {
            this.f7667b.j(min, min2);
            this.f7667b.i();
        }
        this.f7667b.d(canvas, colorFilter, this.f7675k);
        canvas.restoreToCount(save);
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        Drawable drawable = this.f7665a;
        return drawable != null ? androidx.core.graphics.drawable.a.d(drawable) : this.f7667b.f7722b.getRootAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.getChangingConfigurations() : super.getChangingConfigurations() | this.f7667b.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        Drawable drawable = this.f7665a;
        return drawable != null ? androidx.core.graphics.drawable.a.e(drawable) : this.f7669d;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        if (this.f7665a == null || Build.VERSION.SDK_INT < 24) {
            this.f7667b.f7721a = getChangingConfigurations();
            return this.f7667b;
        }
        return new C0086i(this.f7665a.getConstantState());
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ Drawable getCurrent() {
        return super.getCurrent();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.getIntrinsicHeight() : (int) this.f7667b.f7722b.f7714j;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.getIntrinsicWidth() : (int) this.f7667b.f7722b.f7713i;
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getMinimumHeight() {
        return super.getMinimumHeight();
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getMinimumWidth() {
        return super.getMinimumWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            return drawable.getOpacity();
        }
        return -3;
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ boolean getPadding(Rect rect) {
        return super.getPadding(rect);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int[] getState() {
        return super.getState();
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ Region getTransparentRegion() {
        return super.getTransparentRegion();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(boolean z4) {
        this.f7671f = z4;
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.inflate(resources, xmlPullParser, attributeSet);
        } else {
            inflate(resources, xmlPullParser, attributeSet, null);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.g(drawable, resources, xmlPullParser, attributeSet, theme);
            return;
        }
        h hVar = this.f7667b;
        hVar.f7722b = new g();
        TypedArray k8 = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7636a);
        i(k8, xmlPullParser, theme);
        k8.recycle();
        hVar.f7721a = getChangingConfigurations();
        hVar.f7731k = true;
        e(resources, xmlPullParser, attributeSet, theme);
        this.f7668c = j(this.f7668c, hVar.f7723c, hVar.f7724d);
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.invalidateSelf();
        } else {
            super.invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        Drawable drawable = this.f7665a;
        return drawable != null ? androidx.core.graphics.drawable.a.h(drawable) : this.f7667b.f7725e;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        h hVar;
        ColorStateList colorStateList;
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.isStateful() : super.isStateful() || ((hVar = this.f7667b) != null && (hVar.g() || ((colorStateList = this.f7667b.f7723c) != null && colorStateList.isStateful())));
    }

    PorterDuffColorFilter j(PorterDuffColorFilter porterDuffColorFilter, ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void jumpToCurrentState() {
        super.jumpToCurrentState();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.mutate();
            return this;
        }
        if (!this.f7670e && super.mutate() == this) {
            this.f7667b = new h(this.f7667b);
            this.f7670e = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.setBounds(rect);
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        PorterDuff.Mode mode;
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            return drawable.setState(iArr);
        }
        boolean z4 = false;
        h hVar = this.f7667b;
        ColorStateList colorStateList = hVar.f7723c;
        if (colorStateList != null && (mode = hVar.f7724d) != null) {
            this.f7668c = j(this.f7668c, colorStateList, mode);
            invalidateSelf();
            z4 = true;
        }
        if (hVar.g() && hVar.h(iArr)) {
            invalidateSelf();
            return true;
        }
        return z4;
    }

    @Override // android.graphics.drawable.Drawable
    public void scheduleSelf(Runnable runnable, long j8) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.scheduleSelf(runnable, j8);
        } else {
            super.scheduleSelf(runnable, j8);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.setAlpha(i8);
        } else if (this.f7667b.f7722b.getRootAlpha() != i8) {
            this.f7667b.f7722b.setRootAlpha(i8);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean z4) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.j(drawable, z4);
        } else {
            this.f7667b.f7725e = z4;
        }
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setChangingConfigurations(int i8) {
        super.setChangingConfigurations(i8);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setColorFilter(int i8, PorterDuff.Mode mode) {
        super.setColorFilter(i8, mode);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
            return;
        }
        this.f7669d = colorFilter;
        invalidateSelf();
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setFilterBitmap(boolean z4) {
        super.setFilterBitmap(z4);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setHotspot(float f5, float f8) {
        super.setHotspot(f5, f8);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setHotspotBounds(int i8, int i9, int i10, int i11) {
        super.setHotspotBounds(i8, i9, i10, i11);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ boolean setState(int[] iArr) {
        return super.setState(iArr);
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTint(int i8) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.n(drawable, i8);
        } else {
            setTintList(ColorStateList.valueOf(i8));
        }
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintList(ColorStateList colorStateList) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.o(drawable, colorStateList);
            return;
        }
        h hVar = this.f7667b;
        if (hVar.f7723c != colorStateList) {
            hVar.f7723c = colorStateList;
            this.f7668c = j(this.f7668c, colorStateList, hVar.f7724d);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintMode(PorterDuff.Mode mode) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.p(drawable, mode);
            return;
        }
        h hVar = this.f7667b;
        if (hVar.f7724d != mode) {
            hVar.f7724d = mode;
            this.f7668c = j(this.f7668c, hVar.f7723c, mode);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z4, boolean z8) {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.setVisible(z4, z8) : super.setVisible(z4, z8);
    }

    @Override // android.graphics.drawable.Drawable
    public void unscheduleSelf(Runnable runnable) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.unscheduleSelf(runnable);
        } else {
            super.unscheduleSelf(runnable);
        }
    }
}
