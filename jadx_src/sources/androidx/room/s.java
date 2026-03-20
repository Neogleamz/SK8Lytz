package androidx.room;

import androidx.lifecycle.LiveData;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class s {

    /* renamed from: a  reason: collision with root package name */
    final Set<LiveData> f7190a = Collections.newSetFromMap(new IdentityHashMap());

    /* renamed from: b  reason: collision with root package name */
    private final RoomDatabase f7191b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(RoomDatabase roomDatabase) {
        this.f7191b = roomDatabase;
    }
}
