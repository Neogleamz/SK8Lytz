package l0;

import android.util.Log;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: b  reason: collision with root package name */
    static c f21519b = new c();

    /* renamed from: c  reason: collision with root package name */
    public static String[] f21520c = {"standard", "accelerate", "decelerate", "linear"};

    /* renamed from: a  reason: collision with root package name */
    String f21521a = "identity";

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends c {

        /* renamed from: h  reason: collision with root package name */
        private static double f21522h = 0.01d;

        /* renamed from: i  reason: collision with root package name */
        private static double f21523i = 1.0E-4d;

        /* renamed from: d  reason: collision with root package name */
        double f21524d;

        /* renamed from: e  reason: collision with root package name */
        double f21525e;

        /* renamed from: f  reason: collision with root package name */
        double f21526f;

        /* renamed from: g  reason: collision with root package name */
        double f21527g;

        a(String str) {
            this.f21521a = str;
            int indexOf = str.indexOf(40);
            int indexOf2 = str.indexOf(44, indexOf);
            this.f21524d = Double.parseDouble(str.substring(indexOf + 1, indexOf2).trim());
            int i8 = indexOf2 + 1;
            int indexOf3 = str.indexOf(44, i8);
            this.f21525e = Double.parseDouble(str.substring(i8, indexOf3).trim());
            int i9 = indexOf3 + 1;
            int indexOf4 = str.indexOf(44, i9);
            this.f21526f = Double.parseDouble(str.substring(i9, indexOf4).trim());
            int i10 = indexOf4 + 1;
            this.f21527g = Double.parseDouble(str.substring(i10, str.indexOf(41, i10)).trim());
        }

        private double d(double d8) {
            double d9 = 1.0d - d8;
            double d10 = 3.0d * d9;
            return (this.f21524d * d9 * d10 * d8) + (this.f21526f * d10 * d8 * d8) + (d8 * d8 * d8);
        }

        private double e(double d8) {
            double d9 = 1.0d - d8;
            double d10 = 3.0d * d9;
            return (this.f21525e * d9 * d10 * d8) + (this.f21527g * d10 * d8 * d8) + (d8 * d8 * d8);
        }

        @Override // l0.c
        public double a(double d8) {
            if (d8 <= 0.0d) {
                return 0.0d;
            }
            if (d8 >= 1.0d) {
                return 1.0d;
            }
            double d9 = 0.5d;
            double d10 = 0.5d;
            while (d9 > f21522h) {
                d9 *= 0.5d;
                d10 = d(d10) < d8 ? d10 + d9 : d10 - d9;
            }
            double d11 = d10 - d9;
            double d12 = d(d11);
            double d13 = d10 + d9;
            double d14 = d(d13);
            double e8 = e(d11);
            return (((e(d13) - e8) * (d8 - d12)) / (d14 - d12)) + e8;
        }

        @Override // l0.c
        public double b(double d8) {
            double d9 = 0.5d;
            double d10 = 0.5d;
            while (d9 > f21523i) {
                d9 *= 0.5d;
                d10 = d(d10) < d8 ? d10 + d9 : d10 - d9;
            }
            double d11 = d10 - d9;
            double d12 = d10 + d9;
            return (e(d12) - e(d11)) / (d(d12) - d(d11));
        }
    }

    public static c c(String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("cubic")) {
            return new a(str);
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1354466595:
                if (str.equals("accelerate")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1263948740:
                if (str.equals("decelerate")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1102672091:
                if (str.equals("linear")) {
                    c9 = 2;
                    break;
                }
                break;
            case 1312628413:
                if (str.equals("standard")) {
                    c9 = 3;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return new a("cubic(0.4, 0.05, 0.8, 0.7)");
            case 1:
                return new a("cubic(0.0, 0.0, 0.2, 0.95)");
            case 2:
                return new a("cubic(1, 1, 0, 0)");
            case 3:
                return new a("cubic(0.4, 0.0, 0.2, 1)");
            default:
                Log.e("ConstraintSet", "transitionEasing syntax error syntax:transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or " + Arrays.toString(f21520c));
                return f21519b;
        }
    }

    public double a(double d8) {
        return d8;
    }

    public double b(double d8) {
        return 1.0d;
    }

    public String toString() {
        return this.f21521a;
    }
}
