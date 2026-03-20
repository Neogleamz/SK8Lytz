package androidx.core.os;

import android.os.LocaleList;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m implements l {

    /* renamed from: a  reason: collision with root package name */
    private final LocaleList f4782a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(Object obj) {
        this.f4782a = (LocaleList) obj;
    }

    @Override // androidx.core.os.l
    public String a() {
        return this.f4782a.toLanguageTags();
    }

    @Override // androidx.core.os.l
    public Object b() {
        return this.f4782a;
    }

    public boolean equals(Object obj) {
        return this.f4782a.equals(((l) obj).b());
    }

    @Override // androidx.core.os.l
    public Locale get(int i8) {
        return this.f4782a.get(i8);
    }

    public int hashCode() {
        return this.f4782a.hashCode();
    }

    @Override // androidx.core.os.l
    public boolean isEmpty() {
        return this.f4782a.isEmpty();
    }

    @Override // androidx.core.os.l
    public int size() {
        return this.f4782a.size();
    }

    public String toString() {
        return this.f4782a.toString();
    }
}
