package androidx.appcompat.app;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.b;
import androidx.core.view.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i extends androidx.activity.h implements d {

    /* renamed from: d  reason: collision with root package name */
    private f f669d;

    /* renamed from: e  reason: collision with root package name */
    private final g.a f670e;

    public i(Context context, int i8) {
        super(context, e(context, i8));
        this.f670e = new g.a() { // from class: androidx.appcompat.app.h
            @Override // androidx.core.view.g.a
            public final boolean superDispatchKeyEvent(KeyEvent keyEvent) {
                return i.this.f(keyEvent);
            }
        };
        f d8 = d();
        d8.O(e(context, i8));
        d8.y(null);
    }

    private static int e(Context context, int i8) {
        if (i8 == 0) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(g.a.A, typedValue, true);
            return typedValue.resourceId;
        }
        return i8;
    }

    @Override // android.app.Dialog
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        d().e(view, layoutParams);
    }

    public f d() {
        if (this.f669d == null) {
            this.f669d = f.i(this, this);
        }
        return this.f669d;
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        d().z();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return androidx.core.view.g.e(this.f670e, getWindow().getDecorView(), this, keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.app.Dialog
    public <T extends View> T findViewById(int i8) {
        return (T) d().j(i8);
    }

    public boolean g(int i8) {
        return d().H(i8);
    }

    @Override // android.app.Dialog
    public void invalidateOptionsMenu() {
        d().u();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.h, android.app.Dialog
    public void onCreate(Bundle bundle) {
        d().t();
        super.onCreate(bundle);
        d().y(bundle);
    }

    @Override // androidx.activity.h, android.app.Dialog
    protected void onStop() {
        super.onStop();
        d().E();
    }

    @Override // androidx.appcompat.app.d
    public void onSupportActionModeFinished(androidx.appcompat.view.b bVar) {
    }

    @Override // androidx.appcompat.app.d
    public void onSupportActionModeStarted(androidx.appcompat.view.b bVar) {
    }

    @Override // androidx.appcompat.app.d
    public androidx.appcompat.view.b onWindowStartingSupportActionMode(b.a aVar) {
        return null;
    }

    @Override // android.app.Dialog
    public void setContentView(int i8) {
        d().J(i8);
    }

    @Override // android.app.Dialog
    public void setContentView(View view) {
        d().K(view);
    }

    @Override // android.app.Dialog
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        d().L(view, layoutParams);
    }

    @Override // android.app.Dialog
    public void setTitle(int i8) {
        super.setTitle(i8);
        d().P(getContext().getString(i8));
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        d().P(charSequence);
    }
}
