package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.c;
import com.google.android.gms.common.internal.b;
import com.google.android.gms.common.internal.zat;
import g7.f;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends com.google.android.gms.common.internal.c<c> implements f {

    /* renamed from: a0  reason: collision with root package name */
    public static final /* synthetic */ int f17316a0 = 0;
    private final boolean W;
    private final n6.c X;
    private final Bundle Y;
    private final Integer Z;

    public a(Context context, Looper looper, boolean z4, n6.c cVar, Bundle bundle, c.a aVar, c.b bVar) {
        super(context, looper, 44, cVar, aVar, bVar);
        this.W = true;
        this.X = cVar;
        this.Y = bundle;
        this.Z = cVar.g();
    }

    public static Bundle j0(n6.c cVar) {
        cVar.f();
        Integer g8 = cVar.g();
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.google.android.gms.signin.internal.clientRequestedAccount", cVar.a());
        if (g8 != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", g8.intValue());
        }
        bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", false);
        bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", false);
        bundle.putString("com.google.android.gms.signin.internal.serverClientId", null);
        bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", true);
        bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", false);
        bundle.putString("com.google.android.gms.signin.internal.hostedDomain", null);
        bundle.putString("com.google.android.gms.signin.internal.logSessionId", null);
        bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", false);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.b
    public final String C() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    @Override // com.google.android.gms.common.internal.b
    protected final String D() {
        return "com.google.android.gms.signin.service.START";
    }

    @Override // g7.f
    public final void d(h7.c cVar) {
        j.m(cVar, "Expecting a valid ISignInCallbacks");
        try {
            Account b9 = this.X.b();
            ((c) B()).k(new zai(1, new zat(b9, ((Integer) j.l(this.Z)).intValue(), "<<default account>>".equals(b9.name) ? g6.b.a(w()).b() : null)), cVar);
        } catch (RemoteException e8) {
            Log.w("SignInClientImpl", "Remote service probably died when signIn is called");
            try {
                cVar.z(new zak(1, new ConnectionResult(8, null), null));
            } catch (RemoteException unused) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onSignInComplete should be executed from the same process, unexpected RemoteException.", e8);
            }
        }
    }

    @Override // com.google.android.gms.common.internal.b, com.google.android.gms.common.api.a.f
    public final int j() {
        return com.google.android.gms.common.d.f11721a;
    }

    @Override // com.google.android.gms.common.internal.b, com.google.android.gms.common.api.a.f
    public final boolean m() {
        return this.W;
    }

    @Override // g7.f
    public final void n() {
        g(new b.d());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.b
    public final /* synthetic */ IInterface q(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
        return queryLocalInterface instanceof c ? (c) queryLocalInterface : new c(iBinder);
    }

    @Override // com.google.android.gms.common.internal.b
    protected final Bundle y() {
        if (!w().getPackageName().equals(this.X.d())) {
            this.Y.putString("com.google.android.gms.signin.internal.realClientPackageName", this.X.d());
        }
        return this.Y;
    }
}
