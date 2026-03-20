package com.google.common.net;

import com.google.common.base.k;
import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HostAndPort implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private final String f19581a;

    /* renamed from: b  reason: collision with root package name */
    private final int f19582b;

    public boolean a() {
        return this.f19582b >= 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof HostAndPort) {
            HostAndPort hostAndPort = (HostAndPort) obj;
            return k.a(this.f19581a, hostAndPort.f19581a) && this.f19582b == hostAndPort.f19582b;
        }
        return false;
    }

    public int hashCode() {
        return k.b(this.f19581a, Integer.valueOf(this.f19582b));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.f19581a.length() + 8);
        if (this.f19581a.indexOf(58) >= 0) {
            sb.append('[');
            sb.append(this.f19581a);
            sb.append(']');
        } else {
            sb.append(this.f19581a);
        }
        if (a()) {
            sb.append(':');
            sb.append(this.f19582b);
        }
        return sb.toString();
    }
}
