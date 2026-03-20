package com.google.android.gms.measurement.internal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x extends d7 {

    /* renamed from: c  reason: collision with root package name */
    private long f17101c;

    /* renamed from: d  reason: collision with root package name */
    private String f17102d;

    /* renamed from: e  reason: collision with root package name */
    private AccountManager f17103e;

    /* renamed from: f  reason: collision with root package name */
    private Boolean f17104f;

    /* renamed from: g  reason: collision with root package name */
    private long f17105g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x(f6 f6Var) {
        super(f6Var);
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.d7
    protected final boolean r() {
        Calendar calendar = Calendar.getInstance();
        this.f17101c = TimeUnit.MINUTES.convert(calendar.get(15) + calendar.get(16), TimeUnit.MILLISECONDS);
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        Locale locale2 = Locale.ENGLISH;
        String lowerCase = language.toLowerCase(locale2);
        String lowerCase2 = locale.getCountry().toLowerCase(locale2);
        this.f17102d = lowerCase + "-" + lowerCase2;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long s() {
        k();
        return this.f17105g;
    }

    public final long t() {
        n();
        return this.f17101c;
    }

    public final String u() {
        n();
        return this.f17102d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void v() {
        k();
        this.f17104f = null;
        this.f17105g = 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean w() {
        k();
        long a9 = zzb().a();
        if (a9 - this.f17105g > 86400000) {
            this.f17104f = null;
        }
        Boolean bool = this.f17104f;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (androidx.core.content.a.a(zza(), "android.permission.GET_ACCOUNTS") != 0) {
            i().L().a("Permission error checking for dasher/unicorn accounts");
        } else {
            if (this.f17103e == null) {
                this.f17103e = AccountManager.get(zza());
            }
            try {
                Account[] result = this.f17103e.getAccountsByTypeAndFeatures("com.google", new String[]{"service_HOSTED"}, null, null).getResult();
                if (result != null && result.length > 0) {
                    this.f17104f = Boolean.TRUE;
                    this.f17105g = a9;
                    return true;
                }
                Account[] result2 = this.f17103e.getAccountsByTypeAndFeatures("com.google", new String[]{"service_uca"}, null, null).getResult();
                if (result2 != null && result2.length > 0) {
                    this.f17104f = Boolean.TRUE;
                    this.f17105g = a9;
                    return true;
                }
            } catch (AuthenticatorException | OperationCanceledException | IOException e8) {
                i().G().b("Exception checking account types", e8);
            }
        }
        this.f17105g = a9;
        this.f17104f = Boolean.FALSE;
        return false;
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ Context zza() {
        return super.zza();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ u6.d zzb() {
        return super.zzb();
    }
}
