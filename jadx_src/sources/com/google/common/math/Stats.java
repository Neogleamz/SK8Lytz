package com.google.common.math;

import com.google.common.base.i;
import com.google.common.base.k;
import com.google.common.base.l;
import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Stats implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private final long f19576a;

    /* renamed from: b  reason: collision with root package name */
    private final double f19577b;

    /* renamed from: c  reason: collision with root package name */
    private final double f19578c;

    /* renamed from: d  reason: collision with root package name */
    private final double f19579d;

    /* renamed from: e  reason: collision with root package name */
    private final double f19580e;

    public long a() {
        return this.f19576a;
    }

    public double b() {
        return Math.sqrt(c());
    }

    public double c() {
        l.s(this.f19576a > 0);
        if (Double.isNaN(this.f19578c)) {
            return Double.NaN;
        }
        if (this.f19576a == 1) {
            return 0.0d;
        }
        return a.a(this.f19578c) / a();
    }

    public boolean equals(Object obj) {
        if (obj != null && Stats.class == obj.getClass()) {
            Stats stats = (Stats) obj;
            return this.f19576a == stats.f19576a && Double.doubleToLongBits(this.f19577b) == Double.doubleToLongBits(stats.f19577b) && Double.doubleToLongBits(this.f19578c) == Double.doubleToLongBits(stats.f19578c) && Double.doubleToLongBits(this.f19579d) == Double.doubleToLongBits(stats.f19579d) && Double.doubleToLongBits(this.f19580e) == Double.doubleToLongBits(stats.f19580e);
        }
        return false;
    }

    public int hashCode() {
        return k.b(Long.valueOf(this.f19576a), Double.valueOf(this.f19577b), Double.valueOf(this.f19578c), Double.valueOf(this.f19579d), Double.valueOf(this.f19580e));
    }

    public String toString() {
        return (a() > 0 ? i.b(this).c("count", this.f19576a).a("mean", this.f19577b).a("populationStandardDeviation", b()).a("min", this.f19579d).a("max", this.f19580e) : i.b(this).c("count", this.f19576a)).toString();
    }
}
