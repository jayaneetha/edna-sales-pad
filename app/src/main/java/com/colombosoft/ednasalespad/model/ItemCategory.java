package com.colombosoft.ednasalespad.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thahzan on 1/13/15.
 */
public class ItemCategory implements Serializable {

    private int categoryId;
    private String categoryName;
    private List<Item> itemList;

    private List<CustomItemGrouping> groupings;

    public ItemCategory() {}

    public ItemCategory(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ItemCategory(int categoryId, String categoryName, List<Item> itemList) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.itemList = itemList;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public List<CustomItemGrouping> getGroupings() {
        return groupings;
    }

    public void setGroupings(List<CustomItemGrouping> groupings) {
        this.groupings = groupings;
    }
    //    public static ItemCategory parseCategory(JSONObject instance) throws JSONException{
//
//        if(instance != null) {
//            ItemCategory category = new ItemCategory(instance.getInt("iditem_category_type"), instance.getString("ict_name"));
//            List<Item> items = new ArrayList<Item>();
//            JSONArray itemsJSONArray = instance.getJSONArray("items");
//            for(int i=0; i<itemsJSONArray.length(); i++) {
//                Item item = Item.parseItem(itemsJSONArray.getJSONObject(i), category.getCategoryId());
//                if(item != null) {
//                    items.add(item);
//                }
//            }
//            category.setItemList(items);
//
//            return category;
//        }
//
//        return null;
//    }

//    public static ItemCategory parseCategory(JSONObject instance) throws JSONException{
//
//        if(instance != null) {
//            ItemCategory category = new ItemCategory(instance.getInt("id_category"), instance.getString("category_name"));
////            List<Item> items = new ArrayList<Item>();
////            JSONArray itemsJSONArray = instance.getJSONArray("items");
////            for(int i=0; i<itemsJSONArray.length(); i++) {
////                Item item = Item.parseItem(itemsJSONArray.getJSONObject(i), category.getCategoryId());
////                if(item != null) {
////                    items.add(item);
////                }
////            }
////            category.setItemList(items);
//
//            return category;
//        }
//
//        return null;
//    }

    public static ItemCategory parseCategory(JSONObject instance) throws JSONException{

        if(instance != null) {
            ItemCategory category = new ItemCategory(instance.getInt("id_category"), instance.getString("category_name"));

            List<Item> items = new ArrayList<Item>();
            JSONArray itemsJSONArray = instance.getJSONArray("Product");

            for(int i=0; i<itemsJSONArray.length(); i++) {
                items.add(Item.parseItem(itemsJSONArray.getJSONObject(i), category.getCategoryId()));
            }

            category.setItemList(items);

//            List<Item> items = new ArrayList<Item>();
//            JSONArray itemsJSONArray = instance.getJSONArray("items");
//            for(int i=0; i<itemsJSONArray.length(); i++) {
//                Item item = Item.parseItem(itemsJSONArray.getJSONObject(i), category.getCategoryId());
//                if(item != null) {
//                    items.add(item);
//                }
//            }
//            category.setItemList(items);

            return category;
        }

        return null;
    }

}
