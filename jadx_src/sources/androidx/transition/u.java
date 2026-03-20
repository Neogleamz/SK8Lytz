package androidx.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class u {

    /* renamed from: b  reason: collision with root package name */
    public View f7620b;

    /* renamed from: a  reason: collision with root package name */
    public final Map<String, Object> f7619a = new HashMap();

    /* renamed from: c  reason: collision with root package name */
    final ArrayList<Transition> f7621c = new ArrayList<>();

    @Deprecated
    public u() {
    }

    public u(View view) {
        this.f7620b = view;
    }

    public boolean equals(Object obj) {
        if (obj instanceof u) {
            u uVar = (u) obj;
            return this.f7620b == uVar.f7620b && this.f7619a.equals(uVar.f7619a);
        }
        return false;
    }

    public int hashCode() {
        return (this.f7620b.hashCode() * 31) + this.f7619a.hashCode();
    }

    public String toString() {
        String str = (("TransitionValues@" + Integer.toHexString(hashCode()) + ":\n") + "    view = " + this.f7620b + "\n") + "    values:";
        for (String str2 : this.f7619a.keySet()) {
            str = str + "    " + str2 + ": " + this.f7619a.get(str2) + "\n";
        }
        return str;
    }
}
