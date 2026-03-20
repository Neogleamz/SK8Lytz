package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o {

    /* renamed from: b  reason: collision with root package name */
    int f7005b;

    /* renamed from: c  reason: collision with root package name */
    int f7006c;

    /* renamed from: d  reason: collision with root package name */
    int f7007d;

    /* renamed from: e  reason: collision with root package name */
    int f7008e;

    /* renamed from: h  reason: collision with root package name */
    boolean f7011h;

    /* renamed from: i  reason: collision with root package name */
    boolean f7012i;

    /* renamed from: a  reason: collision with root package name */
    boolean f7004a = true;

    /* renamed from: f  reason: collision with root package name */
    int f7009f = 0;

    /* renamed from: g  reason: collision with root package name */
    int f7010g = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(RecyclerView.y yVar) {
        int i8 = this.f7006c;
        return i8 >= 0 && i8 < yVar.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View b(RecyclerView.u uVar) {
        View o5 = uVar.o(this.f7006c);
        this.f7006c += this.f7007d;
        return o5;
    }

    public String toString() {
        return "LayoutState{mAvailable=" + this.f7005b + ", mCurrentPosition=" + this.f7006c + ", mItemDirection=" + this.f7007d + ", mLayoutDirection=" + this.f7008e + ", mStartLine=" + this.f7009f + ", mEndLine=" + this.f7010g + '}';
    }
}
