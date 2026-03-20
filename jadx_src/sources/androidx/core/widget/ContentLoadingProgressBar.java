package androidx.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ContentLoadingProgressBar extends ProgressBar {

    /* renamed from: a  reason: collision with root package name */
    long f5087a;

    /* renamed from: b  reason: collision with root package name */
    boolean f5088b;

    /* renamed from: c  reason: collision with root package name */
    boolean f5089c;

    /* renamed from: d  reason: collision with root package name */
    boolean f5090d;

    /* renamed from: e  reason: collision with root package name */
    private final Runnable f5091e;

    /* renamed from: f  reason: collision with root package name */
    private final Runnable f5092f;

    public ContentLoadingProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        this.f5087a = -1L;
        this.f5088b = false;
        this.f5089c = false;
        this.f5090d = false;
        this.f5091e = new Runnable() { // from class: androidx.core.widget.d
            @Override // java.lang.Runnable
            public final void run() {
                ContentLoadingProgressBar.this.c();
            }
        };
        this.f5092f = new Runnable() { // from class: androidx.core.widget.e
            @Override // java.lang.Runnable
            public final void run() {
                ContentLoadingProgressBar.this.d();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c() {
        this.f5088b = false;
        this.f5087a = -1L;
        setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        this.f5089c = false;
        if (this.f5090d) {
            return;
        }
        this.f5087a = System.currentTimeMillis();
        setVisibility(0);
    }

    private void e() {
        removeCallbacks(this.f5091e);
        removeCallbacks(this.f5092f);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        e();
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        e();
    }
}
