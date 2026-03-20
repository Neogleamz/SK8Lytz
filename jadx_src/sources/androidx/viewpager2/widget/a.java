package androidx.viewpager2.widget;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: b  reason: collision with root package name */
    private static final ViewGroup.MarginLayoutParams f7876b;

    /* renamed from: a  reason: collision with root package name */
    private LinearLayoutManager f7877a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.viewpager2.widget.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0087a implements Comparator<int[]> {
        C0087a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(int[] iArr, int[] iArr2) {
            return iArr[0] - iArr2[0];
        }
    }

    static {
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(-1, -1);
        f7876b = marginLayoutParams;
        marginLayoutParams.setMargins(0, 0, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(LinearLayoutManager linearLayoutManager) {
        this.f7877a = linearLayoutManager;
    }

    private boolean a() {
        int top;
        int i8;
        int bottom;
        int i9;
        int K = this.f7877a.K();
        if (K == 0) {
            return true;
        }
        boolean z4 = this.f7877a.q2() == 0;
        int[][] iArr = (int[][]) Array.newInstance(int.class, K, 2);
        for (int i10 = 0; i10 < K; i10++) {
            View J = this.f7877a.J(i10);
            if (J == null) {
                throw new IllegalStateException("null view contained in the view hierarchy");
            }
            ViewGroup.LayoutParams layoutParams = J.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams = layoutParams instanceof ViewGroup.MarginLayoutParams ? (ViewGroup.MarginLayoutParams) layoutParams : f7876b;
            int[] iArr2 = iArr[i10];
            if (z4) {
                top = J.getLeft();
                i8 = marginLayoutParams.leftMargin;
            } else {
                top = J.getTop();
                i8 = marginLayoutParams.topMargin;
            }
            iArr2[0] = top - i8;
            int[] iArr3 = iArr[i10];
            if (z4) {
                bottom = J.getRight();
                i9 = marginLayoutParams.rightMargin;
            } else {
                bottom = J.getBottom();
                i9 = marginLayoutParams.bottomMargin;
            }
            iArr3[1] = bottom + i9;
        }
        Arrays.sort(iArr, new C0087a());
        for (int i11 = 1; i11 < K; i11++) {
            if (iArr[i11 - 1][1] != iArr[i11][0]) {
                return false;
            }
        }
        return iArr[0][0] <= 0 && iArr[K - 1][1] >= iArr[0][1] - iArr[0][0];
    }

    private boolean b() {
        int K = this.f7877a.K();
        for (int i8 = 0; i8 < K; i8++) {
            if (c(this.f7877a.J(i8))) {
                return true;
            }
        }
        return false;
    }

    private static boolean c(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
            if (layoutTransition != null && layoutTransition.isChangingLayout()) {
                return true;
            }
            int childCount = viewGroup.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                if (c(viewGroup.getChildAt(i8))) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        return (!a() || this.f7877a.K() <= 1) && b();
    }
}
