package com.example.photo_location.models;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ResultBox {
    public int[] address;
    public float[][] boxes;
    public boolean[] fake;

    public static ResultBox getResultBox(Object obj) {
        ResultBox resultBox = new ResultBox();
        List list = (List) obj;
        resultBox.fake = new boolean[list.size()];
        resultBox.address = new int[list.size()];
        resultBox.boxes = (float[][]) Array.newInstance(float.class, list.size(), 4);
        for (int i8 = 0; i8 < list.size(); i8++) {
            Map map = (Map) list.get(i8);
            int intValue = ((Integer) map.get("address")).intValue();
            boolean booleanValue = ((Boolean) map.get("fake")).booleanValue();
            resultBox.address[i8] = intValue;
            resultBox.fake[i8] = booleanValue;
            resultBox.boxes[i8] = (float[]) map.get("boxes");
        }
        return resultBox;
    }
}
