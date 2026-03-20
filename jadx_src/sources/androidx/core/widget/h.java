package androidx.core.widget;

import android.widget.ListView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends a {

    /* renamed from: y  reason: collision with root package name */
    private final ListView f5151y;

    public h(ListView listView) {
        super(listView);
        this.f5151y = listView;
    }

    @Override // androidx.core.widget.a
    public boolean a(int i8) {
        return false;
    }

    @Override // androidx.core.widget.a
    public boolean b(int i8) {
        ListView listView = this.f5151y;
        int count = listView.getCount();
        if (count == 0) {
            return false;
        }
        int childCount = listView.getChildCount();
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int i9 = firstVisiblePosition + childCount;
        if (i8 > 0) {
            if (i9 >= count && listView.getChildAt(childCount - 1).getBottom() <= listView.getHeight()) {
                return false;
            }
        } else if (i8 >= 0) {
            return false;
        } else {
            if (firstVisiblePosition <= 0 && listView.getChildAt(0).getTop() >= 0) {
                return false;
            }
        }
        return true;
    }

    @Override // androidx.core.widget.a
    public void j(int i8, int i9) {
        i.b(this.f5151y, i9);
    }
}
