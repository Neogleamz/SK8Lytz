package com.google.android.gms.internal.measurement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j implements r {

    /* renamed from: a  reason: collision with root package name */
    private final Double f12249a;

    public j(Double d8) {
        if (d8 == null) {
            this.f12249a = Double.valueOf(Double.NaN);
        } else {
            this.f12249a = d8;
        }
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r a() {
        return new j(this.f12249a);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        return Boolean.valueOf((Double.isNaN(this.f12249a.doubleValue()) || this.f12249a.doubleValue() == 0.0d) ? false : true);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        return this.f12249a;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        if (Double.isNaN(this.f12249a.doubleValue())) {
            return "NaN";
        }
        if (Double.isInfinite(this.f12249a.doubleValue())) {
            return this.f12249a.doubleValue() > 0.0d ? "Infinity" : "-Infinity";
        }
        BigDecimal valueOf = BigDecimal.valueOf(this.f12249a.doubleValue());
        BigDecimal bigDecimal = valueOf.signum() == 0 ? new BigDecimal(BigInteger.ZERO, 0) : valueOf.stripTrailingZeros();
        DecimalFormat decimalFormat = new DecimalFormat("0E0");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormat.setMinimumFractionDigits((bigDecimal.scale() > 0 ? bigDecimal.precision() : bigDecimal.scale()) - 1);
        String format = decimalFormat.format(bigDecimal);
        int indexOf = format.indexOf("E");
        if (indexOf > 0) {
            int parseInt = Integer.parseInt(format.substring(indexOf + 1));
            return ((parseInt >= 0 || parseInt <= -7) && (parseInt < 0 || parseInt >= 21)) ? format.replace("E-", "e-").replace("E", "e+") : bigDecimal.toPlainString();
        }
        return format;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof j) {
            return this.f12249a.equals(((j) obj).f12249a);
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return null;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r g(String str, g6 g6Var, List<r> list) {
        if ("toString".equals(str)) {
            return new t(e());
        }
        throw new IllegalArgumentException(String.format("%s.%s is not a function.", e(), str));
    }

    public final int hashCode() {
        return this.f12249a.hashCode();
    }

    public final String toString() {
        return e();
    }
}
