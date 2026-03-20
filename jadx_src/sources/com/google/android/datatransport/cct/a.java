package com.google.android.datatransport.cct;

import com.daimajia.numberprogressbar.BuildConfig;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import w3.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements g {

    /* renamed from: c  reason: collision with root package name */
    static final String f8918c;

    /* renamed from: d  reason: collision with root package name */
    static final String f8919d;

    /* renamed from: e  reason: collision with root package name */
    private static final String f8920e;

    /* renamed from: f  reason: collision with root package name */
    private static final Set<u3.c> f8921f;

    /* renamed from: g  reason: collision with root package name */
    public static final a f8922g;

    /* renamed from: h  reason: collision with root package name */
    public static final a f8923h;

    /* renamed from: a  reason: collision with root package name */
    private final String f8924a;

    /* renamed from: b  reason: collision with root package name */
    private final String f8925b;

    static {
        String a9 = e.a("hts/frbslgiggolai.o/0clgbthfra=snpoo", "tp:/ieaeogn.ogepscmvc/o/ac?omtjo_rt3");
        f8918c = a9;
        String a10 = e.a("hts/frbslgigp.ogepscmv/ieo/eaybtho", "tp:/ieaeogn-agolai.o/1frlglgc/aclg");
        f8919d = a10;
        String a11 = e.a("AzSCki82AwsLzKd5O8zo", "IayckHiZRO1EFl1aGoK");
        f8920e = a11;
        f8921f = Collections.unmodifiableSet(new HashSet(Arrays.asList(u3.c.b("proto"), u3.c.b("json"))));
        f8922g = new a(a9, null);
        f8923h = new a(a10, a11);
    }

    public a(String str, String str2) {
        this.f8924a = str;
        this.f8925b = str2;
    }

    public static a c(byte[] bArr) {
        String str = new String(bArr, Charset.forName("UTF-8"));
        if (str.startsWith("1$")) {
            String[] split = str.substring(2).split(Pattern.quote("\\"), 2);
            if (split.length == 2) {
                String str2 = split[0];
                if (str2.isEmpty()) {
                    throw new IllegalArgumentException("Missing endpoint in CCTDestination extras");
                }
                String str3 = split[1];
                if (str3.isEmpty()) {
                    str3 = null;
                }
                return new a(str2, str3);
            }
            throw new IllegalArgumentException("Extra is not a valid encoded LegacyFlgDestination");
        }
        throw new IllegalArgumentException("Version marker missing from extras");
    }

    @Override // w3.g
    public Set<u3.c> a() {
        return f8921f;
    }

    public byte[] b() {
        String str = this.f8925b;
        if (str == null && this.f8924a == null) {
            return null;
        }
        Object[] objArr = new Object[4];
        objArr[0] = "1$";
        objArr[1] = this.f8924a;
        objArr[2] = "\\";
        if (str == null) {
            str = BuildConfig.FLAVOR;
        }
        objArr[3] = str;
        return String.format("%s%s%s%s", objArr).getBytes(Charset.forName("UTF-8"));
    }

    public String d() {
        return this.f8925b;
    }

    public String e() {
        return this.f8924a;
    }

    @Override // w3.f
    public byte[] getExtras() {
        return b();
    }

    @Override // w3.f
    public String getName() {
        return "cct";
    }
}
