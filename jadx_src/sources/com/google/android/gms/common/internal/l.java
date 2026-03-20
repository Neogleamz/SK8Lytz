package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.view.View;
import com.google.android.gms.dynamic.RemoteCreator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l extends RemoteCreator {

    /* renamed from: c  reason: collision with root package name */
    private static final l f11851c = new l();

    private l() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }

    public static View c(Context context, int i8, int i9) {
        l lVar = f11851c;
        try {
            zax zaxVar = new zax(1, i8, i9, null);
            return (View) x6.b.f(((h) lVar.b(context)).k(x6.b.g(context), zaxVar));
        } catch (Exception e8) {
            throw new RemoteCreator.RemoteCreatorException("Could not get button with size " + i8 + " and color " + i9, e8);
        }
    }

    @Override // com.google.android.gms.dynamic.RemoteCreator
    public final /* synthetic */ Object a(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
        return queryLocalInterface instanceof h ? (h) queryLocalInterface : new h(iBinder);
    }
}
