package androidx.appcompat.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class s0 {

    /* renamed from: a  reason: collision with root package name */
    private final Context f1597a;

    /* renamed from: b  reason: collision with root package name */
    private final View f1598b;

    /* renamed from: c  reason: collision with root package name */
    private final TextView f1599c;

    /* renamed from: d  reason: collision with root package name */
    private final WindowManager.LayoutParams f1600d;

    /* renamed from: e  reason: collision with root package name */
    private final Rect f1601e;

    /* renamed from: f  reason: collision with root package name */
    private final int[] f1602f;

    /* renamed from: g  reason: collision with root package name */
    private final int[] f1603g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s0(Context context) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.f1600d = layoutParams;
        this.f1601e = new Rect();
        this.f1602f = new int[2];
        this.f1603g = new int[2];
        this.f1597a = context;
        View inflate = LayoutInflater.from(context).inflate(g.g.f19980u, (ViewGroup) null);
        this.f1598b = inflate;
        this.f1599c = (TextView) inflate.findViewById(g.f.f19959y);
        layoutParams.setTitle(getClass().getSimpleName());
        layoutParams.packageName = context.getPackageName();
        layoutParams.type = 1002;
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.format = -3;
        layoutParams.windowAnimations = g.i.f19997a;
        layoutParams.flags = 24;
    }

    private void a(View view, int i8, int i9, boolean z4, WindowManager.LayoutParams layoutParams) {
        int height;
        int i10;
        layoutParams.token = view.getApplicationWindowToken();
        int dimensionPixelOffset = this.f1597a.getResources().getDimensionPixelOffset(g.d.f19908m);
        if (view.getWidth() < dimensionPixelOffset) {
            i8 = view.getWidth() / 2;
        }
        if (view.getHeight() >= dimensionPixelOffset) {
            int dimensionPixelOffset2 = this.f1597a.getResources().getDimensionPixelOffset(g.d.f19907l);
            height = i9 + dimensionPixelOffset2;
            i10 = i9 - dimensionPixelOffset2;
        } else {
            height = view.getHeight();
            i10 = 0;
        }
        layoutParams.gravity = 49;
        int dimensionPixelOffset3 = this.f1597a.getResources().getDimensionPixelOffset(z4 ? g.d.f19910o : g.d.f19909n);
        View b9 = b(view);
        if (b9 == null) {
            Log.e("TooltipPopup", "Cannot find app view");
            return;
        }
        b9.getWindowVisibleDisplayFrame(this.f1601e);
        Rect rect = this.f1601e;
        if (rect.left < 0 && rect.top < 0) {
            Resources resources = this.f1597a.getResources();
            int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
            int dimensionPixelSize = identifier != 0 ? resources.getDimensionPixelSize(identifier) : 0;
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            this.f1601e.set(0, dimensionPixelSize, displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        b9.getLocationOnScreen(this.f1603g);
        view.getLocationOnScreen(this.f1602f);
        int[] iArr = this.f1602f;
        int i11 = iArr[0];
        int[] iArr2 = this.f1603g;
        iArr[0] = i11 - iArr2[0];
        iArr[1] = iArr[1] - iArr2[1];
        layoutParams.x = (iArr[0] + i8) - (b9.getWidth() / 2);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.f1598b.measure(makeMeasureSpec, makeMeasureSpec);
        int measuredHeight = this.f1598b.getMeasuredHeight();
        int[] iArr3 = this.f1602f;
        int i12 = ((iArr3[1] + i10) - dimensionPixelOffset3) - measuredHeight;
        int i13 = iArr3[1] + height + dimensionPixelOffset3;
        if (!z4 ? measuredHeight + i13 <= this.f1601e.height() : i12 < 0) {
            layoutParams.y = i12;
        } else {
            layoutParams.y = i13;
        }
    }

    private static View b(View view) {
        View rootView = view.getRootView();
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        if ((layoutParams instanceof WindowManager.LayoutParams) && ((WindowManager.LayoutParams) layoutParams).type == 2) {
            return rootView;
        }
        for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                return ((Activity) context).getWindow().getDecorView();
            }
        }
        return rootView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        if (d()) {
            ((WindowManager) this.f1597a.getSystemService("window")).removeView(this.f1598b);
        }
    }

    boolean d() {
        return this.f1598b.getParent() != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(View view, int i8, int i9, boolean z4, CharSequence charSequence) {
        if (d()) {
            c();
        }
        this.f1599c.setText(charSequence);
        a(view, i8, i9, z4, this.f1600d);
        ((WindowManager) this.f1597a.getSystemService("window")).addView(this.f1598b, this.f1600d);
    }
}
