package androidx.appcompat.app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertController;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends i {

    /* renamed from: f  reason: collision with root package name */
    final AlertController f654f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private final AlertController.f f655a;

        /* renamed from: b  reason: collision with root package name */
        private final int f656b;

        public a(Context context) {
            this(context, c.i(context, 0));
        }

        public a(Context context, int i8) {
            this.f655a = new AlertController.f(new ContextThemeWrapper(context, c.i(context, i8)));
            this.f656b = i8;
        }

        public c a() {
            c cVar = new c(this.f655a.f510a, this.f656b);
            this.f655a.a(cVar.f654f);
            cVar.setCancelable(this.f655a.f526r);
            if (this.f655a.f526r) {
                cVar.setCanceledOnTouchOutside(true);
            }
            cVar.setOnCancelListener(this.f655a.f527s);
            cVar.setOnDismissListener(this.f655a.f528t);
            DialogInterface.OnKeyListener onKeyListener = this.f655a.f529u;
            if (onKeyListener != null) {
                cVar.setOnKeyListener(onKeyListener);
            }
            return cVar;
        }

        public Context b() {
            return this.f655a.f510a;
        }

        public a c(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
            AlertController.f fVar = this.f655a;
            fVar.f531w = listAdapter;
            fVar.f532x = onClickListener;
            return this;
        }

        public a d(boolean z4) {
            this.f655a.f526r = z4;
            return this;
        }

        public a e(View view) {
            this.f655a.f516g = view;
            return this;
        }

        public a f(Drawable drawable) {
            this.f655a.f513d = drawable;
            return this;
        }

        public a g(CharSequence charSequence) {
            this.f655a.f517h = charSequence;
            return this;
        }

        public a h(int i8, DialogInterface.OnClickListener onClickListener) {
            AlertController.f fVar = this.f655a;
            fVar.f521l = fVar.f510a.getText(i8);
            this.f655a.f523n = onClickListener;
            return this;
        }

        public a i(int i8, DialogInterface.OnClickListener onClickListener) {
            AlertController.f fVar = this.f655a;
            fVar.f524o = fVar.f510a.getText(i8);
            this.f655a.q = onClickListener;
            return this;
        }

        public a j(DialogInterface.OnKeyListener onKeyListener) {
            this.f655a.f529u = onKeyListener;
            return this;
        }

        public a k(int i8, DialogInterface.OnClickListener onClickListener) {
            AlertController.f fVar = this.f655a;
            fVar.f518i = fVar.f510a.getText(i8);
            this.f655a.f520k = onClickListener;
            return this;
        }

        public a l(ListAdapter listAdapter, int i8, DialogInterface.OnClickListener onClickListener) {
            AlertController.f fVar = this.f655a;
            fVar.f531w = listAdapter;
            fVar.f532x = onClickListener;
            fVar.I = i8;
            fVar.H = true;
            return this;
        }

        public a m(CharSequence charSequence) {
            this.f655a.f515f = charSequence;
            return this;
        }

        public a n(View view) {
            AlertController.f fVar = this.f655a;
            fVar.f534z = view;
            fVar.f533y = 0;
            fVar.E = false;
            return this;
        }

        public c o() {
            c a9 = a();
            a9.show();
            return a9;
        }
    }

    protected c(Context context, int i8) {
        super(context, i(context, i8));
        this.f654f = new AlertController(getContext(), this, getWindow());
    }

    static int i(Context context, int i8) {
        if (((i8 >>> 24) & 255) >= 1) {
            return i8;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(g.a.f19876o, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView h() {
        return this.f654f.d();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.i, androidx.activity.h, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f654f.e();
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i8, KeyEvent keyEvent) {
        if (this.f654f.g(i8, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i8, keyEvent);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i8, KeyEvent keyEvent) {
        if (this.f654f.h(i8, keyEvent)) {
            return true;
        }
        return super.onKeyUp(i8, keyEvent);
    }

    @Override // androidx.appcompat.app.i, android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.f654f.q(charSequence);
    }
}
