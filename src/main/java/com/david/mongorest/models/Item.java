package com.david.mongorest.models;

import org.json.JSONObject;

import com.github.tomakehurst.wiremock.common.JsonException;

public class Item {
	private int itemLookupID;
	public Item setItemLookupID(int id) {
		this.itemLookupID = id;
		return this;
	}
	public int getItemLookupID() {
		return this.itemLookupID;
	}

	private int quantity;
	public Item setQuantity(int qty) {
		this.quantity = qty;
		return this;
	}
	public int getQuantity() {
		return this.quantity;
	}

	private int cost;
	public Item setCost(int cost) {
		this.cost = cost;
		return this;
	}
	public int getCost() {
		return this.cost;
	}

	private int sellPrice;
	public Item setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
		return this;
	}
	public int getSellPrice() {
		return this.sellPrice;
	}

	public Item() {
		this.itemLookupID = Integer.MAX_VALUE;
		this.quantity = 0;
		this.cost = 0;
		this.sellPrice = 0;
	}

	public Item(int itemLookupID,
			int quantity,
			int cost,
			int sellPrice
			) {
		this.itemLookupID = itemLookupID;
		this.quantity = quantity;
		this.cost = cost;
		this.sellPrice = sellPrice;
	}

	public Item(Item o) {
		this.itemLookupID = o.getItemLookupID();
		this.quantity = o.getQuantity();
		this.cost = o.getCost();
		this.sellPrice = o.getSellPrice();
	}

	//returns item object from the given JSON object.
	public Item loadFromJSON(JSONObject rawJSONObject) {
		this.itemLookupID = rawJSONObject.optInt("itemLookupID");
		this.quantity = rawJSONObject.optInt("quantity");
		this.cost = rawJSONObject.optInt("cost");
		this.sellPrice = rawJSONObject.optInt("sellPrice");
		return this;
	}

	//returns JSON object from the item.
	public JSONObject createJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("itemLookupID", Integer.toString(this.itemLookupID));
			obj.put("quantity", Integer.toString(this.quantity));
			obj.put("cost", Integer.toString(this.cost));
			obj.put("sellPrice", Integer.toString(this.sellPrice));
		}catch (JsonException e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public boolean equals(Object o) {
	    if (o == this)
	    	return true;
	    if (!(o instanceof Item))
	    	return false;
	    Item b = (Item) o;
		boolean isSame = false;
			if(this.getItemLookupID() == b.getItemLookupID() && this.getQuantity() == b.getQuantity()
					&& this.getCost() == b.getCost() && this.getSellPrice() == b.getSellPrice()) {
				isSame = true;
			}
		return isSame;
	}
	
	@Override
	public String toString() {
		String item = "itemLookupID: " + this.itemLookupID + ", quantity: " + this.quantity
				+ ", cost: " + this.cost + ", sellPrice: " + sellPrice + "\n";
		return item;
	}
}
