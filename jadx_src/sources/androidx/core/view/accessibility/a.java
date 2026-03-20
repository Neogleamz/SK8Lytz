package androidx.core.view.accessibility;

import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends ClickableSpan {

    /* renamed from: a  reason: collision with root package name */
    private final int f4900a;

    /* renamed from: b  reason: collision with root package name */
    private final c f4901b;

    /* renamed from: c  reason: collision with root package name */
    private final int f4902c;

    public a(int i8, c cVar, int i9) {
        this.f4900a = i8;
        this.f4901b = cVar;
        this.f4902c = i9;
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", this.f4900a);
        this.f4901b.R(this.f4902c, bundle);
    }
}
