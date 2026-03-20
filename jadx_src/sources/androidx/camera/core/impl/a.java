package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a<T> extends Config.a<T> {

    /* renamed from: a  reason: collision with root package name */
    private final String f2529a;

    /* renamed from: b  reason: collision with root package name */
    private final Class<T> f2530b;

    /* renamed from: c  reason: collision with root package name */
    private final Object f2531c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(String str, Class<T> cls, Object obj) {
        Objects.requireNonNull(str, "Null id");
        this.f2529a = str;
        Objects.requireNonNull(cls, "Null valueClass");
        this.f2530b = cls;
        this.f2531c = obj;
    }

    @Override // androidx.camera.core.impl.Config.a
    public String c() {
        return this.f2529a;
    }

    @Override // androidx.camera.core.impl.Config.a
    public Object d() {
        return this.f2531c;
    }

    @Override // androidx.camera.core.impl.Config.a
    public Class<T> e() {
        return this.f2530b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Config.a) {
            Config.a aVar = (Config.a) obj;
            if (this.f2529a.equals(aVar.c()) && this.f2530b.equals(aVar.e())) {
                Object obj2 = this.f2531c;
                Object d8 = aVar.d();
                if (obj2 == null) {
                    if (d8 == null) {
                        return true;
                    }
                } else if (obj2.equals(d8)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = (((this.f2529a.hashCode() ^ 1000003) * 1000003) ^ this.f2530b.hashCode()) * 1000003;
        Object obj = this.f2531c;
        return hashCode ^ (obj == null ? 0 : obj.hashCode());
    }

    public String toString() {
        return "Option{id=" + this.f2529a + ", valueClass=" + this.f2530b + ", token=" + this.f2531c + "}";
    }
}
