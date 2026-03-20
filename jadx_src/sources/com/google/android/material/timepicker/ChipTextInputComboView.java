package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.material.chip.Chip;
import com.google.android.material.internal.l;
import com.google.android.material.textfield.TextInputLayout;
import k7.f;
import k7.h;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ChipTextInputComboView extends FrameLayout implements Checkable {

    /* renamed from: a  reason: collision with root package name */
    private final Chip f18693a;

    /* renamed from: b  reason: collision with root package name */
    private final TextInputLayout f18694b;

    /* renamed from: c  reason: collision with root package name */
    private final EditText f18695c;

    /* renamed from: d  reason: collision with root package name */
    private TextWatcher f18696d;

    /* renamed from: e  reason: collision with root package name */
    private TextView f18697e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b extends l {
        private b() {
        }

        @Override // com.google.android.material.internal.l, android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(editable)) {
                ChipTextInputComboView.this.f18693a.setText(ChipTextInputComboView.this.c("00"));
            } else {
                ChipTextInputComboView.this.f18693a.setText(ChipTextInputComboView.this.c(editable));
            }
        }
    }

    public ChipTextInputComboView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ChipTextInputComboView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        LayoutInflater from = LayoutInflater.from(context);
        Chip chip = (Chip) from.inflate(h.f21195r, (ViewGroup) this, false);
        this.f18693a = chip;
        TextInputLayout textInputLayout = (TextInputLayout) from.inflate(h.f21196s, (ViewGroup) this, false);
        this.f18694b = textInputLayout;
        EditText editText = textInputLayout.getEditText();
        this.f18695c = editText;
        editText.setVisibility(4);
        b bVar = new b();
        this.f18696d = bVar;
        editText.addTextChangedListener(bVar);
        d();
        addView(chip);
        addView(textInputLayout);
        this.f18697e = (TextView) findViewById(f.f21165n);
        editText.setSaveEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String c(CharSequence charSequence) {
        return TimeModel.a(getResources(), charSequence);
    }

    private void d() {
        if (Build.VERSION.SDK_INT >= 24) {
            this.f18695c.setImeHintLocales(getContext().getResources().getConfiguration().getLocales());
        }
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.f18693a.isChecked();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        d();
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z4) {
        this.f18693a.setChecked(z4);
        this.f18695c.setVisibility(z4 ? 0 : 4);
        this.f18693a.setVisibility(z4 ? 8 : 0);
        if (isChecked()) {
            this.f18695c.requestFocus();
            if (TextUtils.isEmpty(this.f18695c.getText())) {
                return;
            }
            EditText editText = this.f18695c;
            editText.setSelection(editText.getText().length());
        }
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.f18693a.setOnClickListener(onClickListener);
    }

    @Override // android.view.View
    public void setTag(int i8, Object obj) {
        this.f18693a.setTag(i8, obj);
    }

    @Override // android.widget.Checkable
    public void toggle() {
        this.f18693a.toggle();
    }
}
