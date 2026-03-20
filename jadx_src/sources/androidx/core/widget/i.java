package androidx.core.widget;

import android.os.Build;
import android.view.View;
import android.widget.ListView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static boolean a(ListView listView, int i8) {
            return listView.canScrollList(i8);
        }

        static void b(ListView listView, int i8) {
            listView.scrollListBy(i8);
        }
    }

    public static boolean a(ListView listView, int i8) {
        if (Build.VERSION.SDK_INT >= 19) {
            return a.a(listView, i8);
        }
        int childCount = listView.getChildCount();
        if (childCount == 0) {
            return false;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (i8 > 0) {
            return firstVisiblePosition + childCount < listView.getCount() || listView.getChildAt(childCount + (-1)).getBottom() > listView.getHeight() - listView.getListPaddingBottom();
        }
        return firstVisiblePosition > 0 || listView.getChildAt(0).getTop() < listView.getListPaddingTop();
    }

    public static void b(ListView listView, int i8) {
        View childAt;
        if (Build.VERSION.SDK_INT >= 19) {
            a.b(listView, i8);
            return;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (firstVisiblePosition == -1 || (childAt = listView.getChildAt(0)) == null) {
            return;
        }
        listView.setSelectionFromTop(firstVisiblePosition, childAt.getTop() - i8);
    }
}
