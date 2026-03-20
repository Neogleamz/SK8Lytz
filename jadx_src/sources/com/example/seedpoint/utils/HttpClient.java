package com.example.seedpoint.utils;

import com.example.seedpoint.ServerUrl;
import com.google.gson.e;
import java.io.IOException;
import java.util.Objects;
import okhttp3.m;
import okhttp3.o;
import okhttp3.p;
import qk.i;
import qk.k;
import qk.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class HttpClient {
    public static final i JSON = i.c("application/json; charset=utf-8");
    private static HttpClient client = null;
    private m okHttpClient = new m();
    private e gson = new e();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class Result {
        public String body;
        public int code;

        public Result(int i8, String str) {
            this.code = i8;
            this.body = str;
        }

        public String toString() {
            return "Result{code=" + this.code + ", body='" + this.body + "'}";
        }
    }

    public static HttpClient getInstance() {
        if (client == null) {
            synchronized (HttpClient.class) {
                if (client == null) {
                    client = new HttpClient();
                }
            }
        }
        return client;
    }

    public void ping(final androidx.core.util.a<Boolean> aVar) {
        this.okHttpClient.b(new o.a().j("http://bigdata.surplife.net:8080/collect/ping").c().b()).I0(new qk.c() { // from class: com.example.seedpoint.utils.HttpClient.1
            public void onFailure(okhttp3.b bVar, IOException iOException) {
                aVar.accept(Boolean.FALSE);
            }

            public void onResponse(okhttp3.b bVar, p pVar) {
                androidx.core.util.a aVar2;
                Boolean bool;
                try {
                    if (pVar.c() == 200) {
                        aVar2 = aVar;
                        bool = Boolean.TRUE;
                    } else {
                        aVar2 = aVar;
                        bool = Boolean.FALSE;
                    }
                    aVar2.accept(bool);
                } finally {
                    pVar.close();
                }
            }
        });
    }

    public Result post(String str, Object obj) {
        k d8 = k.d(JSON, this.gson.u(obj));
        o.a aVar = new o.a();
        p h8 = this.okHttpClient.b(aVar.j(ServerUrl.URL + str).g(d8).b()).h();
        try {
            if (h8.c() != 200) {
                Result result = new Result(h8.c(), h8.l());
                h8.close();
                return result;
            }
            int c9 = h8.c();
            l a9 = h8.a();
            Objects.requireNonNull(a9);
            l lVar = a9;
            Result result2 = new Result(c9, a9.l());
            h8.close();
            return result2;
        } catch (Throwable th) {
            if (h8 != null) {
                try {
                    h8.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
