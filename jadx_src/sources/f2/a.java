package f2;

import com.zengge.wifi.Model.WifiInfo;
import java.util.ArrayList;
import java.util.Iterator;
import rm.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    public ArrayList<WifiInfo> a(WifiInfo wifiInfo, ArrayList<WifiInfo> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator<WifiInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(it.next().h());
        }
        if (arrayList2.contains(wifiInfo.h()) && d.g(wifiInfo.f().frequency)) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (wifiInfo.h().equals(arrayList.get(size).h())) {
                    arrayList.remove(size);
                    arrayList.add(wifiInfo);
                }
            }
        } else if (!arrayList2.contains(wifiInfo.h())) {
            arrayList.add(wifiInfo);
        }
        return arrayList;
    }
}
