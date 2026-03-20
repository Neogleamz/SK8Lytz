package com.google.android.gms.internal.mlkit_common;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z0 {

    /* renamed from: a  reason: collision with root package name */
    private final String f13026a;

    /* renamed from: b  reason: collision with root package name */
    private final x0 f13027b;

    /* renamed from: c  reason: collision with root package name */
    private x0 f13028c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ z0(String str, y0 y0Var) {
        x0 x0Var = new x0(null);
        this.f13027b = x0Var;
        this.f13028c = x0Var;
        Objects.requireNonNull(str);
        this.f13026a = str;
    }

    public final z0 a(String str, Object obj) {
        x0 x0Var = new x0(null);
        this.f13028c.f13024c = x0Var;
        this.f13028c = x0Var;
        x0Var.f13023b = obj;
        x0Var.f13022a = str;
        return this;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.f13026a);
        sb.append('{');
        x0 x0Var = this.f13027b.f13024c;
        String str = BuildConfig.FLAVOR;
        while (x0Var != null) {
            Object obj = x0Var.f13023b;
            sb.append(str);
            String str2 = x0Var.f13022a;
            if (str2 != null) {
                sb.append(str2);
                sb.append('=');
            }
            if (obj == null || !obj.getClass().isArray()) {
                sb.append(obj);
            } else {
                String deepToString = Arrays.deepToString(new Object[]{obj});
                sb.append((CharSequence) deepToString, 1, deepToString.length() - 1);
            }
            x0Var = x0Var.f13024c;
            str = ", ";
        }
        sb.append('}');
        return sb.toString();
    }
}
