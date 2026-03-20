package com.google.common.base;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o<T> extends Optional<T> {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private final T f18840a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(T t8) {
        this.f18840a = t8;
    }

    @Override // com.google.common.base.Optional
    public T b() {
        return this.f18840a;
    }

    @Override // com.google.common.base.Optional
    public boolean c() {
        return true;
    }

    @Override // com.google.common.base.Optional
    public T e(T t8) {
        l.o(t8, "use Optional.orNull() instead of Optional.or(null)");
        return this.f18840a;
    }

    public boolean equals(Object obj) {
        if (obj instanceof o) {
            return this.f18840a.equals(((o) obj).f18840a);
        }
        return false;
    }

    public int hashCode() {
        return this.f18840a.hashCode() + 1502476572;
    }

    public String toString() {
        String valueOf = String.valueOf(this.f18840a);
        StringBuilder sb = new StringBuilder(valueOf.length() + 13);
        sb.append("Optional.of(");
        sb.append(valueOf);
        sb.append(")");
        return sb.toString();
    }
}
