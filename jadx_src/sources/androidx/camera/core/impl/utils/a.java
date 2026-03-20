package androidx.camera.core.impl.utils;

import android.util.Rational;
import android.util.Size;
import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    public static final Rational f2618a = new Rational(4, 3);

    /* renamed from: b  reason: collision with root package name */
    public static final Rational f2619b = new Rational(3, 4);

    /* renamed from: c  reason: collision with root package name */
    public static final Rational f2620c = new Rational(16, 9);

    /* renamed from: d  reason: collision with root package name */
    public static final Rational f2621d = new Rational(9, 16);

    /* renamed from: androidx.camera.core.impl.utils.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0019a implements Comparator<Rational> {

        /* renamed from: a  reason: collision with root package name */
        private Rational f2622a;

        public C0019a(Rational rational) {
            this.f2622a = rational;
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(Rational rational, Rational rational2) {
            if (rational.equals(rational2)) {
                return 0;
            }
            return (int) Math.signum(Float.valueOf(Math.abs(rational.floatValue() - this.f2622a.floatValue())).floatValue() - Float.valueOf(Math.abs(rational2.floatValue() - this.f2622a.floatValue())).floatValue());
        }
    }

    public static boolean a(Size size, Rational rational) {
        if (rational == null) {
            return false;
        }
        if (rational.equals(new Rational(size.getWidth(), size.getHeight()))) {
            return true;
        }
        if (f0.c.a(size) >= f0.c.a(f0.c.f19832b)) {
            return b(size, rational);
        }
        return false;
    }

    private static boolean b(Size size, Rational rational) {
        int width = size.getWidth();
        int height = size.getHeight();
        Rational rational2 = new Rational(rational.getDenominator(), rational.getNumerator());
        int i8 = width % 16;
        if (i8 == 0 && height % 16 == 0) {
            return c(Math.max(0, height + (-16)), width, rational) || c(Math.max(0, width + (-16)), height, rational2);
        } else if (i8 == 0) {
            return c(height, width, rational);
        } else {
            if (height % 16 == 0) {
                return c(width, height, rational2);
            }
            return false;
        }
    }

    private static boolean c(int i8, int i9, Rational rational) {
        androidx.core.util.h.a(i9 % 16 == 0);
        double numerator = (i8 * rational.getNumerator()) / rational.getDenominator();
        return numerator > ((double) Math.max(0, i9 + (-16))) && numerator < ((double) (i9 + 16));
    }
}
