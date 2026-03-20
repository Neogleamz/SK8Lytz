package com.google.android.material.circularreveal;

import android.animation.TypeEvaluator;
import android.graphics.drawable.Drawable;
import android.util.Property;
import com.google.android.material.circularreveal.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface c extends b.a {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements TypeEvaluator<e> {

        /* renamed from: b  reason: collision with root package name */
        public static final TypeEvaluator<e> f17750b = new b();

        /* renamed from: a  reason: collision with root package name */
        private final e f17751a = new e();

        @Override // android.animation.TypeEvaluator
        /* renamed from: a */
        public e evaluate(float f5, e eVar, e eVar2) {
            this.f17751a.b(s7.a.d(eVar.f17754a, eVar2.f17754a, f5), s7.a.d(eVar.f17755b, eVar2.f17755b, f5), s7.a.d(eVar.f17756c, eVar2.f17756c, f5));
            return this.f17751a;
        }
    }

    /* renamed from: com.google.android.material.circularreveal.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0132c extends Property<c, e> {

        /* renamed from: a  reason: collision with root package name */
        public static final Property<c, e> f17752a = new C0132c("circularReveal");

        private C0132c(String str) {
            super(e.class, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public e get(c cVar) {
            return cVar.getRevealInfo();
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(c cVar, e eVar) {
            cVar.setRevealInfo(eVar);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends Property<c, Integer> {

        /* renamed from: a  reason: collision with root package name */
        public static final Property<c, Integer> f17753a = new d("circularRevealScrimColor");

        private d(String str) {
            super(Integer.class, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Integer get(c cVar) {
            return Integer.valueOf(cVar.getCircularRevealScrimColor());
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(c cVar, Integer num) {
            cVar.setCircularRevealScrimColor(num.intValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: a  reason: collision with root package name */
        public float f17754a;

        /* renamed from: b  reason: collision with root package name */
        public float f17755b;

        /* renamed from: c  reason: collision with root package name */
        public float f17756c;

        private e() {
        }

        public e(float f5, float f8, float f9) {
            this.f17754a = f5;
            this.f17755b = f8;
            this.f17756c = f9;
        }

        public e(e eVar) {
            this(eVar.f17754a, eVar.f17755b, eVar.f17756c);
        }

        public boolean a() {
            return this.f17756c == Float.MAX_VALUE;
        }

        public void b(float f5, float f8, float f9) {
            this.f17754a = f5;
            this.f17755b = f8;
            this.f17756c = f9;
        }

        public void c(e eVar) {
            b(eVar.f17754a, eVar.f17755b, eVar.f17756c);
        }
    }

    void a();

    void b();

    int getCircularRevealScrimColor();

    e getRevealInfo();

    void setCircularRevealOverlayDrawable(Drawable drawable);

    void setCircularRevealScrimColor(int i8);

    void setRevealInfo(e eVar);
}
