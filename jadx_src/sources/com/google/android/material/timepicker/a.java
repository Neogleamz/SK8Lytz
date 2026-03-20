package com.google.android.material.timepicker;

import android.text.InputFilter;
import android.text.Spanned;
import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a implements InputFilter {

    /* renamed from: a  reason: collision with root package name */
    private int f18737a;

    public a(int i8) {
        this.f18737a = i8;
    }

    @Override // android.text.InputFilter
    public CharSequence filter(CharSequence charSequence, int i8, int i9, Spanned spanned, int i10, int i11) {
        try {
            StringBuilder sb = new StringBuilder(spanned);
            sb.replace(i10, i11, charSequence.subSequence(i8, i9).toString());
            if (Integer.parseInt(sb.toString()) <= this.f18737a) {
                return null;
            }
            return BuildConfig.FLAVOR;
        } catch (NumberFormatException unused) {
            return BuildConfig.FLAVOR;
        }
    }
}
