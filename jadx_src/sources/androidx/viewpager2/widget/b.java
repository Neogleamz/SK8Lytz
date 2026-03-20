package androidx.viewpager2.widget;

import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends ViewPager2.i {

    /* renamed from: a  reason: collision with root package name */
    private final List<ViewPager2.i> f7879a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(int i8) {
        this.f7879a = new ArrayList(i8);
    }

    private void e(ConcurrentModificationException concurrentModificationException) {
        throw new IllegalStateException("Adding and removing callbacks during dispatch to callbacks is not supported", concurrentModificationException);
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void a(int i8) {
        try {
            for (ViewPager2.i iVar : this.f7879a) {
                iVar.a(i8);
            }
        } catch (ConcurrentModificationException e8) {
            e(e8);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void b(int i8, float f5, int i9) {
        try {
            for (ViewPager2.i iVar : this.f7879a) {
                iVar.b(i8, f5, i9);
            }
        } catch (ConcurrentModificationException e8) {
            e(e8);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void c(int i8) {
        try {
            for (ViewPager2.i iVar : this.f7879a) {
                iVar.c(i8);
            }
        } catch (ConcurrentModificationException e8) {
            e(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(ViewPager2.i iVar) {
        this.f7879a.add(iVar);
    }
}
