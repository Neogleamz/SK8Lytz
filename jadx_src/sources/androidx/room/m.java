package androidx.room;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.room.RoomDatabase;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import t1.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m {

    /* renamed from: a  reason: collision with root package name */
    public final c.InterfaceC0207c f7144a;

    /* renamed from: b  reason: collision with root package name */
    public final Context f7145b;

    /* renamed from: c  reason: collision with root package name */
    public final String f7146c;

    /* renamed from: d  reason: collision with root package name */
    public final RoomDatabase.c f7147d;

    /* renamed from: e  reason: collision with root package name */
    public final List<RoomDatabase.b> f7148e;

    /* renamed from: f  reason: collision with root package name */
    public final RoomDatabase.d f7149f;

    /* renamed from: g  reason: collision with root package name */
    public final List<Object> f7150g;

    /* renamed from: h  reason: collision with root package name */
    public final boolean f7151h;

    /* renamed from: i  reason: collision with root package name */
    public final RoomDatabase.JournalMode f7152i;

    /* renamed from: j  reason: collision with root package name */
    public final Executor f7153j;

    /* renamed from: k  reason: collision with root package name */
    public final Executor f7154k;

    /* renamed from: l  reason: collision with root package name */
    public final boolean f7155l;

    /* renamed from: m  reason: collision with root package name */
    public final boolean f7156m;

    /* renamed from: n  reason: collision with root package name */
    public final boolean f7157n;

    /* renamed from: o  reason: collision with root package name */
    private final Set<Integer> f7158o;

    /* renamed from: p  reason: collision with root package name */
    public final String f7159p;
    public final File q;

    /* renamed from: r  reason: collision with root package name */
    public final Callable<InputStream> f7160r;

    @SuppressLint({"LambdaLast"})
    public m(Context context, String str, c.InterfaceC0207c interfaceC0207c, RoomDatabase.c cVar, List<RoomDatabase.b> list, boolean z4, RoomDatabase.JournalMode journalMode, Executor executor, Executor executor2, boolean z8, boolean z9, boolean z10, Set<Integer> set, String str2, File file, Callable<InputStream> callable, RoomDatabase.d dVar, List<Object> list2) {
        this.f7144a = interfaceC0207c;
        this.f7145b = context;
        this.f7146c = str;
        this.f7147d = cVar;
        this.f7148e = list;
        this.f7151h = z4;
        this.f7152i = journalMode;
        this.f7153j = executor;
        this.f7154k = executor2;
        this.f7155l = z8;
        this.f7156m = z9;
        this.f7157n = z10;
        this.f7158o = set;
        this.f7159p = str2;
        this.q = file;
        this.f7160r = callable;
        this.f7150g = list2 == null ? Collections.emptyList() : list2;
    }

    public boolean a(int i8, int i9) {
        Set<Integer> set;
        if ((i8 > i9) && this.f7157n) {
            return false;
        }
        return this.f7156m && ((set = this.f7158o) == null || !set.contains(Integer.valueOf(i8)));
    }
}
