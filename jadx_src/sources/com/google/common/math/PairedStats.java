package com.google.common.math;

import com.google.common.base.i;
import com.google.common.base.k;
import com.google.common.base.l;
import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PairedStats implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private final Stats f19573a;

    /* renamed from: b  reason: collision with root package name */
    private final Stats f19574b;

    /* renamed from: c  reason: collision with root package name */
    private final double f19575c;

    public long a() {
        return this.f19573a.a();
    }

    public double b() {
        l.s(a() != 0);
        return this.f19575c / a();
    }

    public boolean equals(Object obj) {
        if (obj != null && PairedStats.class == obj.getClass()) {
            PairedStats pairedStats = (PairedStats) obj;
            return this.f19573a.equals(pairedStats.f19573a) && this.f19574b.equals(pairedStats.f19574b) && Double.doubleToLongBits(this.f19575c) == Double.doubleToLongBits(pairedStats.f19575c);
        }
        return false;
    }

    public int hashCode() {
        return k.b(this.f19573a, this.f19574b, Double.valueOf(this.f19575c));
    }

    public String toString() {
        return (a() > 0 ? i.b(this).d("xStats", this.f19573a).d("yStats", this.f19574b).a("populationCovariance", b()) : i.b(this).d("xStats", this.f19573a).d("yStats", this.f19574b)).toString();
    }
}
