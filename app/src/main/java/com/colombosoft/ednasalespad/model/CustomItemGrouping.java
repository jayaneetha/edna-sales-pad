package com.colombosoft.ednasalespad.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thahzan on 2/16/15.
 */
public class CustomItemGrouping {

    private int typeId, categoryId;
    private boolean flavoured;
    private List<Item> items = new ArrayList<Item>();
    private Item item;

    public CustomItemGrouping() {}

    public CustomItemGrouping(int categoryId, int typeId, boolean flavoured) {
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.flavoured = flavoured;
    }

    public CustomItemGrouping(int categoryId, int typeId, Item item) {
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.flavoured = false;
        this.item = item;
    }

    public CustomItemGrouping(Item item) {
        this.categoryId = item.getCategoryId();
        this.typeId = item.getTypeId();
        this.flavoured = item.hasFlavours();
        if(item.hasFlavours()){
            this.items.add(item);
        } else {
            this.item = item;
        }
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public boolean isFlavoured() {
        return flavoured;
    }

    public void setFlavoured(boolean flavoured) {
        this.flavoured = flavoured;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public CustomItemGrouping createSelectedGrouping() {
        CustomItemGrouping grouping = new CustomItemGrouping(categoryId, typeId, flavoured);

        if(flavoured){
            List<Item> itemList = new ArrayList<Item>();
            if(items != null) {
                for(Item item : items){
                    if(item.getSelectedQty() > 0){
                        itemList.add(item);
                    }
                }
            }
            grouping.setItems(itemList);
            return grouping;
        } else {

            if(item.getSelectedQty() > 0){
                grouping.setItem(item);

                return grouping;
            }

            return null;
        }


    }

    public CustomItemGrouping createDistributorGrouping(List<Stock> distributorStock) {

        CustomItemGrouping grouping = new CustomItemGrouping(categoryId, typeId, flavoured);

        if(flavoured) {
            List<Item> itemList = new ArrayList<Item>();
            for(Item item : items){
                for(Stock stock : distributorStock) {
                    if(stock.getStockItemId() == item.getItemNo() && stock.getStockItemQty() > 0) {
                        itemList.add(item);
                        break;
                    }
                }
            }

            if(itemList.size() > 0) {
                grouping.setItems(itemList);
                return grouping;
            }

        } else {

            for(Stock stock : distributorStock) {
                if(stock.getStockItemId() == item.getItemNo() && stock.getStockItemQty() > 0) {
                    grouping.setItem(item);

                    return grouping;
                }
            }

        }

        return null;
    }

}
