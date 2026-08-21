package com.example.likelionhackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MfdsPrdtPrmsnResponse {
    private Body body;
    public Body getBody() { return body; }
    public void setBody(Body body) { this.body = body; }

    public static class Body {
        private List<Item> items;
        public List<Item> getItems() { return items; }
        public void setItems(List<Item> items) { this.items = items; }
    }

    public static class Item {
        @JsonProperty("ITEM_NAME")
        private String itemName;
        @JsonProperty("ITEM_ENG_NAME")
        private String itemEngName;
        @JsonProperty("ITEM_INGR_NAME")
        private String itemIngrName;
        @JsonProperty("ITEM_INGR_CNT")
        private String itemIngrCnt;
        @JsonProperty("ENTP_NAME")
        private String entpName;

        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public String getItemEngName() { return itemEngName; }
        public void setItemEngName(String itemEngName) { this.itemEngName = itemEngName; }
        public String getItemIngrName() { return itemIngrName; }
        public void setItemIngrName(String itemIngrName) { this.itemIngrName = itemIngrName; }
        public String getItemIngrCnt() { return itemIngrCnt; }
        public void setItemIngrCnt(String itemIngrCnt) { this.itemIngrCnt = itemIngrCnt; }
        public String getEntpName() { return entpName; }
        public void setEntpName(String entpName) { this.entpName = entpName; }
    }
}