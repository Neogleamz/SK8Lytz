package androidx.viewpager2.widget;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d extends ViewPager2.i {

    /* renamed from: a  reason: collision with root package name */
    private final LinearLayoutManager f7883a;

    /* renamed from: b  reason: collision with root package name */
    private ViewPager2.k f7884b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(LinearLayoutManager linearLayoutManager) {
        this.f7883a = linearLayoutManager;
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void a(int i8) {
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void b(int i8, float f5, int i9) {
        if (this.f7884b == null) {
            return;
        }
        float f8 = -f5;
        for (int i10 = 0; i10 < this.f7883a.K(); i10++) {
            View J = this.f7883a.J(i10);
            if (J == null) {
                throw new IllegalStateException(String.format(Locale.US, "LayoutManager returned a null child at pos %d/%d while transforming pages", Integer.valueOf(i10), Integer.valueOf(this.f7883a.K())));
            }
            this.f7884b.a(J, (this.f7883a.i0(J) - i8) + f8);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void c(int i8) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewPager2.k d() {
        return this.f7884b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(ViewPager2.k kVar) {
        this.f7884b = kVar;
    }
}
