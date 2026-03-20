package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        final int[] f4659a;

        /* renamed from: b  reason: collision with root package name */
        final float[] f4660b;

        a(int i8, int i9) {
            this.f4659a = new int[]{i8, i9};
            this.f4660b = new float[]{0.0f, 1.0f};
        }

        a(int i8, int i9, int i10) {
            this.f4659a = new int[]{i8, i9, i10};
            this.f4660b = new float[]{0.0f, 0.5f, 1.0f};
        }

        a(List<Integer> list, List<Float> list2) {
            int size = list.size();
            this.f4659a = new int[size];
            this.f4660b = new float[size];
            for (int i8 = 0; i8 < size; i8++) {
                this.f4659a[i8] = list.get(i8).intValue();
                this.f4660b[i8] = list2.get(i8).floatValue();
            }
        }
    }

    private static a a(a aVar, int i8, int i9, boolean z4, int i10) {
        return aVar != null ? aVar : z4 ? new a(i8, i10, i9) : new a(i8, i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Shader b(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        String name = xmlPullParser.getName();
        if (!name.equals("gradient")) {
            throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": invalid gradient color tag " + name);
        }
        TypedArray k8 = k.k(resources, theme, attributeSet, q0.i.A);
        float f5 = k.f(k8, xmlPullParser, "startX", q0.i.J, 0.0f);
        float f8 = k.f(k8, xmlPullParser, "startY", q0.i.K, 0.0f);
        float f9 = k.f(k8, xmlPullParser, "endX", q0.i.L, 0.0f);
        float f10 = k.f(k8, xmlPullParser, "endY", q0.i.M, 0.0f);
        float f11 = k.f(k8, xmlPullParser, "centerX", q0.i.E, 0.0f);
        float f12 = k.f(k8, xmlPullParser, "centerY", q0.i.F, 0.0f);
        int g8 = k.g(k8, xmlPullParser, "type", q0.i.D, 0);
        int b9 = k.b(k8, xmlPullParser, "startColor", q0.i.B, 0);
        boolean j8 = k.j(xmlPullParser, "centerColor");
        int b10 = k.b(k8, xmlPullParser, "centerColor", q0.i.I, 0);
        int b11 = k.b(k8, xmlPullParser, "endColor", q0.i.C, 0);
        int g9 = k.g(k8, xmlPullParser, "tileMode", q0.i.H, 0);
        float f13 = k.f(k8, xmlPullParser, "gradientRadius", q0.i.G, 0.0f);
        k8.recycle();
        a a9 = a(c(resources, xmlPullParser, attributeSet, theme), b9, b11, j8, b10);
        if (g8 != 1) {
            return g8 != 2 ? new LinearGradient(f5, f8, f9, f10, a9.f4659a, a9.f4660b, d(g9)) : new SweepGradient(f11, f12, a9.f4659a, a9.f4660b);
        } else if (f13 > 0.0f) {
            return new RadialGradient(f11, f12, f13, a9.f4659a, a9.f4660b, d(g9));
        } else {
            throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0080, code lost:
        throw new org.xmlpull.v1.XmlPullParserException(r10.getPositionDescription() + ": <item> tag requires a 'color' attribute and a 'offset' attribute!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static androidx.core.content.res.f.a c(android.content.res.Resources r9, org.xmlpull.v1.XmlPullParser r10, android.util.AttributeSet r11, android.content.res.Resources.Theme r12) {
        /*
            int r0 = r10.getDepth()
            r1 = 1
            int r0 = r0 + r1
            java.util.ArrayList r2 = new java.util.ArrayList
            r3 = 20
            r2.<init>(r3)
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>(r3)
        L12:
            int r3 = r10.next()
            if (r3 == r1) goto L81
            int r5 = r10.getDepth()
            if (r5 >= r0) goto L21
            r6 = 3
            if (r3 == r6) goto L81
        L21:
            r6 = 2
            if (r3 == r6) goto L25
            goto L12
        L25:
            if (r5 > r0) goto L12
            java.lang.String r3 = r10.getName()
            java.lang.String r5 = "item"
            boolean r3 = r3.equals(r5)
            if (r3 != 0) goto L34
            goto L12
        L34:
            int[] r3 = q0.i.N
            android.content.res.TypedArray r3 = androidx.core.content.res.k.k(r9, r12, r11, r3)
            int r5 = q0.i.O
            boolean r6 = r3.hasValue(r5)
            int r7 = q0.i.P
            boolean r8 = r3.hasValue(r7)
            if (r6 == 0) goto L66
            if (r8 == 0) goto L66
            r6 = 0
            int r5 = r3.getColor(r5, r6)
            r6 = 0
            float r6 = r3.getFloat(r7, r6)
            r3.recycle()
            java.lang.Integer r3 = java.lang.Integer.valueOf(r5)
            r4.add(r3)
            java.lang.Float r3 = java.lang.Float.valueOf(r6)
            r2.add(r3)
            goto L12
        L66:
            org.xmlpull.v1.XmlPullParserException r9 = new org.xmlpull.v1.XmlPullParserException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r10 = r10.getPositionDescription()
            r11.append(r10)
            java.lang.String r10 = ": <item> tag requires a 'color' attribute and a 'offset' attribute!"
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            r9.<init>(r10)
            throw r9
        L81:
            int r9 = r4.size()
            if (r9 <= 0) goto L8d
            androidx.core.content.res.f$a r9 = new androidx.core.content.res.f$a
            r9.<init>(r4, r2)
            return r9
        L8d:
            r9 = 0
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.f.c(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):androidx.core.content.res.f$a");
    }

    private static Shader.TileMode d(int i8) {
        return i8 != 1 ? i8 != 2 ? Shader.TileMode.CLAMP : Shader.TileMode.MIRROR : Shader.TileMode.REPEAT;
    }
}
