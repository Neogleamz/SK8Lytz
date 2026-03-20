package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f extends Drawable.ConstantState {

    /* renamed from: a  reason: collision with root package name */
    int f4736a;

    /* renamed from: b  reason: collision with root package name */
    Drawable.ConstantState f4737b;

    /* renamed from: c  reason: collision with root package name */
    ColorStateList f4738c;

    /* renamed from: d  reason: collision with root package name */
    PorterDuff.Mode f4739d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(f fVar) {
        this.f4738c = null;
        this.f4739d = d.f4728g;
        if (fVar != null) {
            this.f4736a = fVar.f4736a;
            this.f4737b = fVar.f4737b;
            this.f4738c = fVar.f4738c;
            this.f4739d = fVar.f4739d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a() {
        return this.f4737b != null;
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public int getChangingConfigurations() {
        int i8 = this.f4736a;
        Drawable.ConstantState constantState = this.f4737b;
        return i8 | (constantState != null ? constantState.getChangingConfigurations() : 0);
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public Drawable newDrawable() {
        return newDrawable(null);
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public Drawable newDrawable(Resources resources) {
        return Build.VERSION.SDK_INT >= 21 ? new e(this, resources) : new d(this, resources);
    }
}
