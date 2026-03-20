package com.all.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MyViewPager extends ViewPager {

    /* renamed from: z0  reason: collision with root package name */
    private boolean f8651z0;

    public MyViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f8651z0 = true;
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.f8651z0) {
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.f8651z0) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setNoScroll(boolean z4) {
        this.f8651z0 = z4;
    }
}
