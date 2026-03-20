package com.example.seedpoint.dao;

import com.example.seedpoint.po.EventPO;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface EventPODao {
    void batchInsert(List<EventPO> list);

    void delete(EventPO eventPO);

    void deleteAll();

    List<EventPO> findEventPOs();

    void insert(EventPO eventPO);
}
