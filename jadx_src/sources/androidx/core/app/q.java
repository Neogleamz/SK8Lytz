package androidx.core.app;

import android.app.RemoteInput;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q {

    /* renamed from: a  reason: collision with root package name */
    private final String f4602a;

    /* renamed from: b  reason: collision with root package name */
    private final CharSequence f4603b;

    /* renamed from: c  reason: collision with root package name */
    private final CharSequence[] f4604c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f4605d;

    /* renamed from: e  reason: collision with root package name */
    private final int f4606e;

    /* renamed from: f  reason: collision with root package name */
    private final Bundle f4607f;

    /* renamed from: g  reason: collision with root package name */
    private final Set<String> f4608g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static ClipData a(Intent intent) {
            return intent.getClipData();
        }

        static void b(Intent intent, ClipData clipData) {
            intent.setClipData(clipData);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static void a(Object obj, Intent intent, Bundle bundle) {
            RemoteInput.addResultsToIntent((RemoteInput[]) obj, intent, bundle);
        }

        public static RemoteInput b(q qVar) {
            Set<String> d8;
            RemoteInput.Builder addExtras = new RemoteInput.Builder(qVar.j()).setLabel(qVar.i()).setChoices(qVar.e()).setAllowFreeFormInput(qVar.c()).addExtras(qVar.h());
            if (Build.VERSION.SDK_INT >= 26 && (d8 = qVar.d()) != null) {
                for (String str : d8) {
                    c.d(addExtras, str, true);
                }
            }
            if (Build.VERSION.SDK_INT >= 29) {
                d.b(addExtras, qVar.g());
            }
            return addExtras.build();
        }

        static Bundle c(Intent intent) {
            return RemoteInput.getResultsFromIntent(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {
        static void a(q qVar, Intent intent, Map<String, Uri> map) {
            RemoteInput.addDataResultToIntent(q.a(qVar), intent, map);
        }

        static Set<String> b(Object obj) {
            return ((RemoteInput) obj).getAllowedDataTypes();
        }

        static Map<String, Uri> c(Intent intent, String str) {
            return RemoteInput.getDataResultsFromIntent(intent, str);
        }

        static RemoteInput.Builder d(RemoteInput.Builder builder, String str, boolean z4) {
            return builder.setAllowDataType(str, z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {
        static int a(Object obj) {
            return ((RemoteInput) obj).getEditChoicesBeforeSending();
        }

        static RemoteInput.Builder b(RemoteInput.Builder builder, int i8) {
            return builder.setEditChoicesBeforeSending(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        private final String f4609a;

        /* renamed from: d  reason: collision with root package name */
        private CharSequence f4612d;

        /* renamed from: e  reason: collision with root package name */
        private CharSequence[] f4613e;

        /* renamed from: b  reason: collision with root package name */
        private final Set<String> f4610b = new HashSet();

        /* renamed from: c  reason: collision with root package name */
        private final Bundle f4611c = new Bundle();

        /* renamed from: f  reason: collision with root package name */
        private boolean f4614f = true;

        /* renamed from: g  reason: collision with root package name */
        private int f4615g = 0;

        public e(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Result key can't be null");
            }
            this.f4609a = str;
        }

        public q a() {
            return new q(this.f4609a, this.f4612d, this.f4613e, this.f4614f, this.f4615g, this.f4611c, this.f4610b);
        }

        public e b(String str, boolean z4) {
            if (z4) {
                this.f4610b.add(str);
            } else {
                this.f4610b.remove(str);
            }
            return this;
        }

        public e c(boolean z4) {
            this.f4614f = z4;
            return this;
        }

        public e d(CharSequence[] charSequenceArr) {
            this.f4613e = charSequenceArr;
            return this;
        }

        public e e(CharSequence charSequence) {
            this.f4612d = charSequence;
            return this;
        }
    }

    q(String str, CharSequence charSequence, CharSequence[] charSequenceArr, boolean z4, int i8, Bundle bundle, Set<String> set) {
        this.f4602a = str;
        this.f4603b = charSequence;
        this.f4604c = charSequenceArr;
        this.f4605d = z4;
        this.f4606e = i8;
        this.f4607f = bundle;
        this.f4608g = set;
        if (g() == 2 && !c()) {
            throw new IllegalArgumentException("setEditChoicesBeforeSending requires setAllowFreeFormInput");
        }
    }

    static RemoteInput a(q qVar) {
        return b.b(qVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RemoteInput[] b(q[] qVarArr) {
        if (qVarArr == null) {
            return null;
        }
        RemoteInput[] remoteInputArr = new RemoteInput[qVarArr.length];
        for (int i8 = 0; i8 < qVarArr.length; i8++) {
            remoteInputArr[i8] = a(qVarArr[i8]);
        }
        return remoteInputArr;
    }

    private static Intent f(Intent intent) {
        ClipData a9 = a.a(intent);
        if (a9 == null) {
            return null;
        }
        ClipDescription description = a9.getDescription();
        if (description.hasMimeType("text/vnd.android.intent") && description.getLabel().toString().contentEquals("android.remoteinput.results")) {
            return a9.getItemAt(0).getIntent();
        }
        return null;
    }

    public static Bundle k(Intent intent) {
        Intent f5;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 20) {
            return b.c(intent);
        }
        if (i8 < 16 || (f5 = f(intent)) == null) {
            return null;
        }
        return (Bundle) f5.getExtras().getParcelable("android.remoteinput.resultsData");
    }

    public boolean c() {
        return this.f4605d;
    }

    public Set<String> d() {
        return this.f4608g;
    }

    public CharSequence[] e() {
        return this.f4604c;
    }

    public int g() {
        return this.f4606e;
    }

    public Bundle h() {
        return this.f4607f;
    }

    public CharSequence i() {
        return this.f4603b;
    }

    public String j() {
        return this.f4602a;
    }

    public boolean l() {
        return (c() || (e() != null && e().length != 0) || d() == null || d().isEmpty()) ? false : true;
    }
}
