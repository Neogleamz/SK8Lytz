package androidx.emoji2.text;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* renamed from: n  reason: collision with root package name */
    private static final Object f5222n = new Object();

    /* renamed from: o  reason: collision with root package name */
    private static final Object f5223o = new Object();

    /* renamed from: p  reason: collision with root package name */
    private static volatile e f5224p;

    /* renamed from: b  reason: collision with root package name */
    private final Set<AbstractC0051e> f5226b;

    /* renamed from: e  reason: collision with root package name */
    private final b f5229e;

    /* renamed from: f  reason: collision with root package name */
    final g f5230f;

    /* renamed from: g  reason: collision with root package name */
    final boolean f5231g;

    /* renamed from: h  reason: collision with root package name */
    final boolean f5232h;

    /* renamed from: i  reason: collision with root package name */
    final int[] f5233i;

    /* renamed from: j  reason: collision with root package name */
    private final boolean f5234j;

    /* renamed from: k  reason: collision with root package name */
    private final int f5235k;

    /* renamed from: l  reason: collision with root package name */
    private final int f5236l;

    /* renamed from: m  reason: collision with root package name */
    private final d f5237m;

    /* renamed from: a  reason: collision with root package name */
    private final ReadWriteLock f5225a = new ReentrantReadWriteLock();

    /* renamed from: c  reason: collision with root package name */
    private volatile int f5227c = 3;

    /* renamed from: d  reason: collision with root package name */
    private final Handler f5228d = new Handler(Looper.getMainLooper());

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a extends b {

        /* renamed from: b  reason: collision with root package name */
        private volatile androidx.emoji2.text.h f5238b;

        /* renamed from: c  reason: collision with root package name */
        private volatile m f5239c;

        /* renamed from: androidx.emoji2.text.e$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0050a extends h {
            C0050a() {
            }

            @Override // androidx.emoji2.text.e.h
            public void a(Throwable th) {
                a.this.f5241a.m(th);
            }

            @Override // androidx.emoji2.text.e.h
            public void b(m mVar) {
                a.this.d(mVar);
            }
        }

        a(e eVar) {
            super(eVar);
        }

        @Override // androidx.emoji2.text.e.b
        void a() {
            try {
                this.f5241a.f5230f.a(new C0050a());
            } catch (Throwable th) {
                this.f5241a.m(th);
            }
        }

        @Override // androidx.emoji2.text.e.b
        CharSequence b(CharSequence charSequence, int i8, int i9, int i10, boolean z4) {
            return this.f5238b.h(charSequence, i8, i9, i10, z4);
        }

        @Override // androidx.emoji2.text.e.b
        void c(EditorInfo editorInfo) {
            editorInfo.extras.putInt("android.support.text.emoji.emojiCompat_metadataVersion", this.f5239c.e());
            editorInfo.extras.putBoolean("android.support.text.emoji.emojiCompat_replaceAll", this.f5241a.f5231g);
        }

        void d(m mVar) {
            if (mVar == null) {
                this.f5241a.m(new IllegalArgumentException("metadataRepo cannot be null"));
                return;
            }
            this.f5239c = mVar;
            m mVar2 = this.f5239c;
            i iVar = new i();
            d dVar = this.f5241a.f5237m;
            e eVar = this.f5241a;
            this.f5238b = new androidx.emoji2.text.h(mVar2, iVar, dVar, eVar.f5232h, eVar.f5233i);
            this.f5241a.n();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        final e f5241a;

        b(e eVar) {
            this.f5241a = eVar;
        }

        void a() {
            this.f5241a.n();
        }

        CharSequence b(CharSequence charSequence, int i8, int i9, int i10, boolean z4) {
            return charSequence;
        }

        void c(EditorInfo editorInfo) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c {

        /* renamed from: a  reason: collision with root package name */
        final g f5242a;

        /* renamed from: b  reason: collision with root package name */
        boolean f5243b;

        /* renamed from: c  reason: collision with root package name */
        boolean f5244c;

        /* renamed from: d  reason: collision with root package name */
        int[] f5245d;

        /* renamed from: e  reason: collision with root package name */
        Set<AbstractC0051e> f5246e;

        /* renamed from: f  reason: collision with root package name */
        boolean f5247f;

        /* renamed from: g  reason: collision with root package name */
        int f5248g = -16711936;

        /* renamed from: h  reason: collision with root package name */
        int f5249h = 0;

        /* renamed from: i  reason: collision with root package name */
        d f5250i = new androidx.emoji2.text.d();

        /* JADX INFO: Access modifiers changed from: protected */
        public c(g gVar) {
            androidx.core.util.h.i(gVar, "metadataLoader cannot be null.");
            this.f5242a = gVar;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final g a() {
            return this.f5242a;
        }

        public c b(int i8) {
            this.f5249h = i8;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        boolean a(CharSequence charSequence, int i8, int i9, int i10);
    }

    /* renamed from: androidx.emoji2.text.e$e  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractC0051e {
        public void a(Throwable th) {
        }

        public void b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final List<AbstractC0051e> f5251a;

        /* renamed from: b  reason: collision with root package name */
        private final Throwable f5252b;

        /* renamed from: c  reason: collision with root package name */
        private final int f5253c;

        f(AbstractC0051e abstractC0051e, int i8) {
            this(Arrays.asList((AbstractC0051e) androidx.core.util.h.i(abstractC0051e, "initCallback cannot be null")), i8, null);
        }

        f(Collection<AbstractC0051e> collection, int i8) {
            this(collection, i8, null);
        }

        f(Collection<AbstractC0051e> collection, int i8, Throwable th) {
            androidx.core.util.h.i(collection, "initCallbacks cannot be null");
            this.f5251a = new ArrayList(collection);
            this.f5253c = i8;
            this.f5252b = th;
        }

        @Override // java.lang.Runnable
        public void run() {
            int size = this.f5251a.size();
            int i8 = 0;
            if (this.f5253c != 1) {
                while (i8 < size) {
                    this.f5251a.get(i8).a(this.f5252b);
                    i8++;
                }
                return;
            }
            while (i8 < size) {
                this.f5251a.get(i8).b();
                i8++;
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        void a(h hVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class h {
        public abstract void a(Throwable th);

        public abstract void b(m mVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class i {
        i() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public androidx.emoji2.text.i a(androidx.emoji2.text.g gVar) {
            return new o(gVar);
        }
    }

    private e(c cVar) {
        this.f5231g = cVar.f5243b;
        this.f5232h = cVar.f5244c;
        this.f5233i = cVar.f5245d;
        this.f5234j = cVar.f5247f;
        this.f5235k = cVar.f5248g;
        this.f5230f = cVar.f5242a;
        this.f5236l = cVar.f5249h;
        this.f5237m = cVar.f5250i;
        k0.b bVar = new k0.b();
        this.f5226b = bVar;
        Set<AbstractC0051e> set = cVar.f5246e;
        if (set != null && !set.isEmpty()) {
            bVar.addAll(cVar.f5246e);
        }
        this.f5229e = Build.VERSION.SDK_INT < 19 ? new b(this) : new a(this);
        l();
    }

    public static e b() {
        e eVar;
        synchronized (f5222n) {
            eVar = f5224p;
            androidx.core.util.h.k(eVar != null, "EmojiCompat is not initialized.\n\nYou must initialize EmojiCompat prior to referencing the EmojiCompat instance.\n\nThe most likely cause of this error is disabling the EmojiCompatInitializer\neither explicitly in AndroidManifest.xml, or by including\nandroidx.emoji2:emoji2-bundled.\n\nAutomatic initialization is typically performed by EmojiCompatInitializer. If\nyou are not expecting to initialize EmojiCompat manually in your application,\nplease check to ensure it has not been removed from your APK's manifest. You can\ndo this in Android Studio using Build > Analyze APK.\n\nIn the APK Analyzer, ensure that the startup entry for\nEmojiCompatInitializer and InitializationProvider is present in\n AndroidManifest.xml. If it is missing or contains tools:node=\"remove\", and you\nintend to use automatic configuration, verify:\n\n  1. Your application does not include emoji2-bundled\n  2. All modules do not contain an exclusion manifest rule for\n     EmojiCompatInitializer or InitializationProvider. For more information\n     about manifest exclusions see the documentation for the androidx startup\n     library.\n\nIf you intend to use emoji2-bundled, please call EmojiCompat.init. You can\nlearn more in the documentation for BundledEmojiCompatConfig.\n\nIf you intended to perform manual configuration, it is recommended that you call\nEmojiCompat.init immediately on application startup.\n\nIf you still cannot resolve this issue, please open a bug with your specific\nconfiguration to help improve error message.");
        }
        return eVar;
    }

    public static boolean e(InputConnection inputConnection, Editable editable, int i8, int i9, boolean z4) {
        if (Build.VERSION.SDK_INT >= 19) {
            return androidx.emoji2.text.h.c(inputConnection, editable, i8, i9, z4);
        }
        return false;
    }

    public static boolean f(Editable editable, int i8, KeyEvent keyEvent) {
        if (Build.VERSION.SDK_INT >= 19) {
            return androidx.emoji2.text.h.d(editable, i8, keyEvent);
        }
        return false;
    }

    public static e g(c cVar) {
        e eVar = f5224p;
        if (eVar == null) {
            synchronized (f5222n) {
                eVar = f5224p;
                if (eVar == null) {
                    eVar = new e(cVar);
                    f5224p = eVar;
                }
            }
        }
        return eVar;
    }

    public static boolean h() {
        return f5224p != null;
    }

    private boolean j() {
        return d() == 1;
    }

    private void l() {
        this.f5225a.writeLock().lock();
        try {
            if (this.f5236l == 0) {
                this.f5227c = 0;
            }
            this.f5225a.writeLock().unlock();
            if (d() == 0) {
                this.f5229e.a();
            }
        } catch (Throwable th) {
            this.f5225a.writeLock().unlock();
            throw th;
        }
    }

    public int c() {
        return this.f5235k;
    }

    public int d() {
        this.f5225a.readLock().lock();
        try {
            return this.f5227c;
        } finally {
            this.f5225a.readLock().unlock();
        }
    }

    public boolean i() {
        return this.f5234j;
    }

    public void k() {
        androidx.core.util.h.k(this.f5236l == 1, "Set metadataLoadStrategy to LOAD_STRATEGY_MANUAL to execute manual loading");
        if (j()) {
            return;
        }
        this.f5225a.writeLock().lock();
        try {
            if (this.f5227c == 0) {
                return;
            }
            this.f5227c = 0;
            this.f5225a.writeLock().unlock();
            this.f5229e.a();
        } finally {
            this.f5225a.writeLock().unlock();
        }
    }

    void m(Throwable th) {
        ArrayList arrayList = new ArrayList();
        this.f5225a.writeLock().lock();
        try {
            this.f5227c = 2;
            arrayList.addAll(this.f5226b);
            this.f5226b.clear();
            this.f5225a.writeLock().unlock();
            this.f5228d.post(new f(arrayList, this.f5227c, th));
        } catch (Throwable th2) {
            this.f5225a.writeLock().unlock();
            throw th2;
        }
    }

    void n() {
        ArrayList arrayList = new ArrayList();
        this.f5225a.writeLock().lock();
        try {
            this.f5227c = 1;
            arrayList.addAll(this.f5226b);
            this.f5226b.clear();
            this.f5225a.writeLock().unlock();
            this.f5228d.post(new f(arrayList, this.f5227c));
        } catch (Throwable th) {
            this.f5225a.writeLock().unlock();
            throw th;
        }
    }

    public CharSequence o(CharSequence charSequence) {
        return p(charSequence, 0, charSequence == null ? 0 : charSequence.length());
    }

    public CharSequence p(CharSequence charSequence, int i8, int i9) {
        return q(charSequence, i8, i9, Integer.MAX_VALUE);
    }

    public CharSequence q(CharSequence charSequence, int i8, int i9, int i10) {
        return r(charSequence, i8, i9, i10, 0);
    }

    public CharSequence r(CharSequence charSequence, int i8, int i9, int i10, int i11) {
        boolean z4;
        androidx.core.util.h.k(j(), "Not initialized yet");
        androidx.core.util.h.f(i8, "start cannot be negative");
        androidx.core.util.h.f(i9, "end cannot be negative");
        androidx.core.util.h.f(i10, "maxEmojiCount cannot be negative");
        androidx.core.util.h.b(i8 <= i9, "start should be <= than end");
        if (charSequence == null) {
            return null;
        }
        androidx.core.util.h.b(i8 <= charSequence.length(), "start should be < than charSequence length");
        androidx.core.util.h.b(i9 <= charSequence.length(), "end should be < than charSequence length");
        if (charSequence.length() == 0 || i8 == i9) {
            return charSequence;
        }
        if (i11 != 1) {
            z4 = i11 != 2 ? this.f5231g : false;
        } else {
            z4 = true;
        }
        return this.f5229e.b(charSequence, i8, i9, i10, z4);
    }

    public void s(AbstractC0051e abstractC0051e) {
        androidx.core.util.h.i(abstractC0051e, "initCallback cannot be null");
        this.f5225a.writeLock().lock();
        try {
            if (this.f5227c != 1 && this.f5227c != 2) {
                this.f5226b.add(abstractC0051e);
            }
            this.f5228d.post(new f(abstractC0051e, this.f5227c));
        } finally {
            this.f5225a.writeLock().unlock();
        }
    }

    public void t(AbstractC0051e abstractC0051e) {
        androidx.core.util.h.i(abstractC0051e, "initCallback cannot be null");
        this.f5225a.writeLock().lock();
        try {
            this.f5226b.remove(abstractC0051e);
        } finally {
            this.f5225a.writeLock().unlock();
        }
    }

    public void u(EditorInfo editorInfo) {
        if (!j() || editorInfo == null) {
            return;
        }
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        this.f5229e.c(editorInfo);
    }
}
