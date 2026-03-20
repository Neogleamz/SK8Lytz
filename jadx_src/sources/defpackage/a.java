package defpackage;

import java.util.List;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* renamed from: a  reason: default package */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: b  reason: collision with root package name */
    public static final C0000a f3b = new C0000a(null);

    /* renamed from: a  reason: collision with root package name */
    private final Boolean f4a;

    /* renamed from: a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0000a {
        private C0000a() {
        }

        public /* synthetic */ C0000a(i iVar) {
            this();
        }

        public final a a(List<? extends Object> list) {
            p.e(list, "list");
            return new a((Boolean) list.get(0));
        }
    }

    public a() {
        this(null, 1, null);
    }

    public a(Boolean bool) {
        this.f4a = bool;
    }

    public /* synthetic */ a(Boolean bool, int i8, i iVar) {
        this((i8 & 1) != 0 ? null : bool);
    }

    public final List<Object> a() {
        return q.d(this.f4a);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof a) && p.a(this.f4a, ((a) obj).f4a);
    }

    public int hashCode() {
        Boolean bool = this.f4a;
        if (bool == null) {
            return 0;
        }
        return bool.hashCode();
    }

    public String toString() {
        return "IsEnabledMessage(enabled=" + this.f4a + ')';
    }
}
