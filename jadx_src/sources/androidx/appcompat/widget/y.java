package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.Transition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import androidx.appcompat.view.menu.ListMenuItemView;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class y extends ListPopupWindow implements x {
    private static Method Y;
    private x X;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static void a(PopupWindow popupWindow, Transition transition) {
            popupWindow.setEnterTransition(transition);
        }

        static void b(PopupWindow popupWindow, Transition transition) {
            popupWindow.setExitTransition(transition);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static void a(PopupWindow popupWindow, boolean z4) {
            popupWindow.setTouchModal(z4);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends u {

        /* renamed from: p  reason: collision with root package name */
        final int f1648p;
        final int q;

        /* renamed from: t  reason: collision with root package name */
        private x f1649t;

        /* renamed from: w  reason: collision with root package name */
        private MenuItem f1650w;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a {
            static int a(Configuration configuration) {
                return configuration.getLayoutDirection();
            }
        }

        public c(Context context, boolean z4) {
            super(context, z4);
            Configuration configuration = context.getResources().getConfiguration();
            if (Build.VERSION.SDK_INT < 17 || 1 != a.a(configuration)) {
                this.f1648p = 22;
                this.q = 21;
                return;
            }
            this.f1648p = 21;
            this.q = 22;
        }

        @Override // androidx.appcompat.widget.u
        public /* bridge */ /* synthetic */ int d(int i8, int i9, int i10, int i11, int i12) {
            return super.d(i8, i9, i10, i11, i12);
        }

        @Override // androidx.appcompat.widget.u
        public /* bridge */ /* synthetic */ boolean e(MotionEvent motionEvent, int i8) {
            return super.e(motionEvent, i8);
        }

        @Override // androidx.appcompat.widget.u, android.view.ViewGroup, android.view.View
        public /* bridge */ /* synthetic */ boolean hasFocus() {
            return super.hasFocus();
        }

        @Override // androidx.appcompat.widget.u, android.view.View
        public /* bridge */ /* synthetic */ boolean hasWindowFocus() {
            return super.hasWindowFocus();
        }

        @Override // androidx.appcompat.widget.u, android.view.View
        public /* bridge */ /* synthetic */ boolean isFocused() {
            return super.isFocused();
        }

        @Override // androidx.appcompat.widget.u, android.view.View
        public /* bridge */ /* synthetic */ boolean isInTouchMode() {
            return super.isInTouchMode();
        }

        @Override // androidx.appcompat.widget.u, android.view.View
        public boolean onHoverEvent(MotionEvent motionEvent) {
            int i8;
            int pointToPosition;
            int i9;
            if (this.f1649t != null) {
                ListAdapter adapter = getAdapter();
                if (adapter instanceof HeaderViewListAdapter) {
                    HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
                    i8 = headerViewListAdapter.getHeadersCount();
                    adapter = headerViewListAdapter.getWrappedAdapter();
                } else {
                    i8 = 0;
                }
                androidx.appcompat.view.menu.f fVar = (androidx.appcompat.view.menu.f) adapter;
                androidx.appcompat.view.menu.i iVar = null;
                if (motionEvent.getAction() != 10 && (pointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY())) != -1 && (i9 = pointToPosition - i8) >= 0 && i9 < fVar.getCount()) {
                    iVar = fVar.getItem(i9);
                }
                MenuItem menuItem = this.f1650w;
                if (menuItem != iVar) {
                    androidx.appcompat.view.menu.g b9 = fVar.b();
                    if (menuItem != null) {
                        this.f1649t.h(b9, menuItem);
                    }
                    this.f1650w = iVar;
                    if (iVar != null) {
                        this.f1649t.e(b9, iVar);
                    }
                }
            }
            return super.onHoverEvent(motionEvent);
        }

        @Override // android.widget.ListView, android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
        public boolean onKeyDown(int i8, KeyEvent keyEvent) {
            ListMenuItemView listMenuItemView = (ListMenuItemView) getSelectedView();
            if (listMenuItemView != null && i8 == this.f1648p) {
                if (listMenuItemView.isEnabled() && listMenuItemView.getItemData().hasSubMenu()) {
                    performItemClick(listMenuItemView, getSelectedItemPosition(), getSelectedItemId());
                }
                return true;
            } else if (listMenuItemView == null || i8 != this.q) {
                return super.onKeyDown(i8, keyEvent);
            } else {
                setSelection(-1);
                ListAdapter adapter = getAdapter();
                if (adapter instanceof HeaderViewListAdapter) {
                    adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
                }
                ((androidx.appcompat.view.menu.f) adapter).b().e(false);
                return true;
            }
        }

        @Override // androidx.appcompat.widget.u, android.widget.AbsListView, android.view.View
        public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
            return super.onTouchEvent(motionEvent);
        }

        public void setHoverListener(x xVar) {
            this.f1649t = xVar;
        }

        @Override // androidx.appcompat.widget.u, android.widget.AbsListView
        public /* bridge */ /* synthetic */ void setSelector(Drawable drawable) {
            super.setSelector(drawable);
        }
    }

    static {
        try {
            if (Build.VERSION.SDK_INT <= 28) {
                Y = PopupWindow.class.getDeclaredMethod("setTouchModal", Boolean.TYPE);
            }
        } catch (NoSuchMethodException unused) {
            Log.i("MenuPopupWindow", "Could not find method setTouchModal() on PopupWindow. Oh well.");
        }
    }

    public y(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
    }

    public void R(Object obj) {
        if (Build.VERSION.SDK_INT >= 23) {
            a.a(this.Q, (Transition) obj);
        }
    }

    public void S(Object obj) {
        if (Build.VERSION.SDK_INT >= 23) {
            a.b(this.Q, (Transition) obj);
        }
    }

    public void T(x xVar) {
        this.X = xVar;
    }

    public void U(boolean z4) {
        if (Build.VERSION.SDK_INT > 28) {
            b.a(this.Q, z4);
            return;
        }
        Method method = Y;
        if (method != null) {
            try {
                method.invoke(this.Q, Boolean.valueOf(z4));
            } catch (Exception unused) {
                Log.i("MenuPopupWindow", "Could not invoke setTouchModal() on PopupWindow. Oh well.");
            }
        }
    }

    @Override // androidx.appcompat.widget.x
    public void e(androidx.appcompat.view.menu.g gVar, MenuItem menuItem) {
        x xVar = this.X;
        if (xVar != null) {
            xVar.e(gVar, menuItem);
        }
    }

    @Override // androidx.appcompat.widget.x
    public void h(androidx.appcompat.view.menu.g gVar, MenuItem menuItem) {
        x xVar = this.X;
        if (xVar != null) {
            xVar.h(gVar, menuItem);
        }
    }

    @Override // androidx.appcompat.widget.ListPopupWindow
    u s(Context context, boolean z4) {
        c cVar = new c(context, z4);
        cVar.setHoverListener(this);
        return cVar;
    }
}
