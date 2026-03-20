package o7;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements View.OnTouchListener {

    /* renamed from: a  reason: collision with root package name */
    private final Dialog f22242a;

    /* renamed from: b  reason: collision with root package name */
    private final int f22243b;

    /* renamed from: c  reason: collision with root package name */
    private final int f22244c;

    /* renamed from: d  reason: collision with root package name */
    private final int f22245d;

    public a(Dialog dialog, Rect rect) {
        this.f22242a = dialog;
        this.f22243b = rect.left;
        this.f22244c = rect.top;
        this.f22245d = ViewConfiguration.get(dialog.getContext()).getScaledWindowTouchSlop();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        View findViewById = view.findViewById(16908290);
        int left = this.f22243b + findViewById.getLeft();
        int width = findViewById.getWidth() + left;
        int top = this.f22244c + findViewById.getTop();
        if (new RectF(left, top, width, findViewById.getHeight() + top).contains(motionEvent.getX(), motionEvent.getY())) {
            return false;
        }
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        if (motionEvent.getAction() == 1) {
            obtain.setAction(4);
        }
        if (Build.VERSION.SDK_INT < 28) {
            obtain.setAction(0);
            int i8 = this.f22245d;
            obtain.setLocation((-i8) - 1, (-i8) - 1);
        }
        view.performClick();
        return this.f22242a.onTouchEvent(obtain);
    }
}
