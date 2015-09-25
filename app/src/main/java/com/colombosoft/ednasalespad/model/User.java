package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Admin on 9/20/15.
 */
public class User implements Serializable {

    public static final int TYPE_DISTRIBUTOR = 1;
    public static final int TYPE_MOBILE = 2;

    private int id, type, locationId, salesType;
    private String name, username, position, contact, territory, imageURI;
    private int territoryId;

    /**
     * Default Constructor
     */
    public User() {}

    /**
     * Constructor
     * @param id
     * @param type
     * @param locationId
     * @param salesType
     * @param name
     * @param username
     * @param position
     * @param contact
     * @param territoryId
     * @param territory
     * @param imageURI
     */
    public User(int id, int type, int locationId, int salesType, String name, String username, String position, String contact, int territoryId, String territory, String imageURI) {
        this.id = id;
        this.type = type;
        this.locationId = locationId;
        this.salesType = salesType;
        this.name = name;
        this.username = username;
        this.position = position;
        this.contact = contact;
        this.territory = territory;
        this.imageURI = imageURI;
        this.territoryId = territoryId;
    }

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSalesType() {
        return salesType;
    }

    public void setSalesType(int salesType) {
        this.salesType = salesType;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public boolean isInvalidUser(){
        return ((this.id == 0)
                || (this.name.equals(""))
                || (this.position.equals(""))
                || (this.contact.equals("")));
    }

    public int getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(int territoryId) {
        this.territoryId = territoryId;
    }

    /**
     * Parses a relevant JSONObject into a User object
     * @param instance Valid JSONObject instance
     * @return Returns a User object using the JSONObject values
     * @throws JSONException Throws when the JSONObject is not in format
     * @throws NumberFormatException Throws when the ID is not an integer
     */
    public static User parseUser(JSONObject instance) throws JSONException, NumberFormatException {

        if(instance != null) {
            User user = new User();

            user.setId(instance.getInt("user_type_id"));
            user.setType(instance.getInt("sales_type"));
            user.setName(instance.getString("name"));
            user.setContact("N/A");
            user.setPosition("Sales Rep");
            user.setTerritory(instance.getString("territory_name"));
            user.setTerritoryId(instance.getInt("territory_id"));
            user.setLocationId(instance.getInt("user_location_id"));
            user.setSalesType(instance.getInt("sales_type"));

            return user;
        }

        return null;
    }

    @Override
    public String toString() {
        return "[" + "Id : " + String.valueOf(this.id) + ", "
                + "Name : " + this.name + ", "
                + "Position : " + this.position + ", "
                + "Contact : " + this.contact + ", "
                + "Territory : " + this.territory + "(" + String.valueOf(this.territoryId) + "), "
                + "Image URI : " + this.imageURI + "]";
    }
}
