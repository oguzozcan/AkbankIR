package com.akbank.investorrelations.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oguzemreozcan on 07/03/16.
 */
public class DummyContent {

    public static class DummyItem {

        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {

        addItem(new DummyItem("1", "Le terme"));
        addItem(new DummyItem("2", "Le cure termali"));
        addItem(new DummyItem("3", ""));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
