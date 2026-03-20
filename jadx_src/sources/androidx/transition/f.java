package androidx.transition;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import java.util.ArrayList;
@SuppressLint({"ViewConstructor"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class f extends FrameLayout {

    /* renamed from: a  reason: collision with root package name */
    private ViewGroup f7550a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f7551b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(ViewGroup viewGroup) {
        super(viewGroup.getContext());
        setClipChildren(false);
        this.f7550a = viewGroup;
        viewGroup.setTag(x1.b.f23761b, this);
        a0.b(this.f7550a).c(this);
        this.f7551b = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static f b(ViewGroup viewGroup) {
        return (f) viewGroup.getTag(x1.b.f23761b);
    }

    private int c(ArrayList<View> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int childCount = getChildCount() - 1;
        int i8 = 0;
        while (i8 <= childCount) {
            int i9 = (i8 + childCount) / 2;
            d(((h) getChildAt(i9)).f7565c, arrayList2);
            if (f(arrayList, arrayList2)) {
                i8 = i9 + 1;
            } else {
                childCount = i9 - 1;
            }
            arrayList2.clear();
        }
        return i8;
    }

    private static void d(View view, ArrayList<View> arrayList) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            d((View) parent, arrayList);
        }
        arrayList.add(view);
    }

    private static boolean e(View view, View view2) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int childCount = viewGroup.getChildCount();
        if (Build.VERSION.SDK_INT >= 21 && view.getZ() != view2.getZ()) {
            return view.getZ() > view2.getZ();
        }
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = viewGroup.getChildAt(a0.a(viewGroup, i8));
            if (childAt == view) {
                return false;
            }
            if (childAt == view2) {
                break;
            }
        }
        return true;
    }

    private static boolean f(ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        if (arrayList.isEmpty() || arrayList2.isEmpty() || arrayList.get(0) != arrayList2.get(0)) {
            return true;
        }
        int min = Math.min(arrayList.size(), arrayList2.size());
        for (int i8 = 1; i8 < min; i8++) {
            View view = arrayList.get(i8);
            View view2 = arrayList2.get(i8);
            if (view != view2) {
                return e(view, view2);
            }
        }
        return arrayList2.size() == min;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(h hVar) {
        ArrayList<View> arrayList = new ArrayList<>();
        d(hVar.f7565c, arrayList);
        int c9 = c(arrayList);
        if (c9 < 0 || c9 >= getChildCount()) {
            addView(hVar);
        } else {
            addView(hVar, c9);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        if (!this.f7551b) {
            throw new IllegalStateException("This GhostViewHolder is detached!");
        }
        a0.b(this.f7550a).d(this);
        a0.b(this.f7550a).c(this);
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        if (!this.f7551b) {
            throw new IllegalStateException("This GhostViewHolder is detached!");
        }
        super.onViewAdded(view);
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if ((getChildCount() == 1 && getChildAt(0) == view) || getChildCount() == 0) {
            this.f7550a.setTag(x1.b.f23761b, null);
            a0.b(this.f7550a).d(this);
            this.f7551b = false;
        }
    }
}
