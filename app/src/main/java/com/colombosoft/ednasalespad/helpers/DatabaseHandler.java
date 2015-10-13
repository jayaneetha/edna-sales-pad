package com.colombosoft.ednasalespad.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.colombosoft.ednasalespad.model.Attendance;
import com.colombosoft.ednasalespad.model.Bank;
import com.colombosoft.ednasalespad.model.BankBranch;
import com.colombosoft.ednasalespad.model.CashPayment;
import com.colombosoft.ednasalespad.model.Cheque;
import com.colombosoft.ednasalespad.model.CustomItemGrouping;
import com.colombosoft.ednasalespad.model.Flavour;
import com.colombosoft.ednasalespad.model.HistoryDetail;
import com.colombosoft.ednasalespad.model.Invoice;
import com.colombosoft.ednasalespad.model.Item;
import com.colombosoft.ednasalespad.model.ItemCategory;
import com.colombosoft.ednasalespad.model.Order;
import com.colombosoft.ednasalespad.model.OrderDetail;
import com.colombosoft.ednasalespad.model.Outlet;
import com.colombosoft.ednasalespad.model.OutletClass;
import com.colombosoft.ednasalespad.model.OutletType;
import com.colombosoft.ednasalespad.model.Payment;
import com.colombosoft.ednasalespad.model.PaymentAllocator;
import com.colombosoft.ednasalespad.model.PaymentPinHolder;
import com.colombosoft.ednasalespad.model.ProductType;
import com.colombosoft.ednasalespad.model.ReturnDetail;
import com.colombosoft.ednasalespad.model.Route;
import com.colombosoft.ednasalespad.model.Stock;
import com.colombosoft.ednasalespad.model.TrackedError;
import com.colombosoft.ednasalespad.model.UnproductiveCall;
import com.colombosoft.ednasalespad.model.User;
import com.colombosoft.ednasalespad.model.VisitDetail;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler dbHandler;
    private final String LOG_TAG = DatabaseHandler.class.getSimpleName();

    //Table session
    private final String tableSession = "tbl_session";
    private final String keyLoggedInTimestamp = "logged_in_time";

    // Table user
    private final String tableUser = "tbl_user";
    private final String keyUserId = "user_id";
    private final String keyUserType = "user_type";
    private final String keyUserName = "user_name";
    private final String keyUserUsername = "user_username";
    private final String keyUserPosition = "user_position";
    private final String keyUserContact = "user_contact";
    private final String keyUserTerritory = "territory_name";
    private final String keyUserImageUri = "user_image_uri";
    private final String keyUserTerritoryId = "user_territory_id";
    private final String keyUserSalesType = "sales_type";

    // Table user session
    private final String keyUserLocationId = "user_location_id";

    // Table route
    private final String tableRoute = "tbl_route";
    private final String keyRouteIndex = "route_index";
    private final String keyRouteId = "route_id";
    private final String keyRouteCode = "route_code";
    private final String keyRouteName = "route_name";
    private final String keyRouteFixedTarget = "fixed_target";
    private final String keyRouteSelectedTarget = "selected_target";

    // Table outlet type
    private final String tableOutletType = "tbl_outlet_type";
    private final String keyOutletTypeIndex = "outlet_type_index";
    private final String keyOutletTypeId = "outlet_type_id";
    private final String keyOutletTypeName = "outlet_type_name";

    // Table outlet class
    private final String tableOutletClass = "tbl_outlet_class";
    private final String keyOutletClassIndex = "outlet_class_index";
    private final String keyOutletClassId = "outlet_class_id";
    private final String keyOutletClassName = "outlet_class_name";

    // Table outlet
    private final String tableOutlet = "tbl_outlet";
    private final String keyOutletIndex = "outlet_index";
    private final String keyOutletId = "outlet_id";
    private final String keyOutletName = "outlet_name";
    private final String keyOutletCode = "outlet_code";
    private final String keyOutletAddress = "outlet_address";
    private final String keyOutletOwnerName = "outlet_owner";
    private final String keyOutletContactLand = "outlet_land";
    private final String keyOutletFrontImageUri = "outlet_f_image";
    private final String keyOutletShowcaseImageUri = "outlet_s_image";
    private final String keyOutletPromotion1ImageUri = "outlet_p_1_image";
    private final String keyOutletPromotion2ImageUri = "outlet_p_2_image";
    private final String keyOutletTarget = "target";

    // Table outlet history
    private final String tableOutletHistory = "tbl_outlet_history";
    private final String keyOutletHistoryIndex = "history_index";
    private final String keyOutletHistoryType = "history_type";
    private final String keyInvoiceDate = "inv_date";
    private final String keyInvoiceNo = "inv_no";
    private final String keyInvoiceAmount = "tot_amount";
    private final String keyInvoiceRemark = "remark";

    // Table cash payment
    private final String tableCashPayment = "tbl_cash_payment";
    private final String keyCashIndex = "cash_index";

    // inv_no
    private final String keyCashDate = "cash_date";
    private final String keyCashAmount = "cash_amount";

    // Table cheque payment
    private final String tableChequePayment = "tbl_cheq_payment";
    private final String keyChequeIndex = "cheq_index";

    // inv_no
    private final String keyChequeNo = "cheq_no";
    private final String keyChequeDate = "cheq_date";

    // bank id and branch_id
    private final String keyChequeAmount = "cheq_amount";

    // Table item category
    private final String tableItemCategory = "tbl_item_category";
    private final String keyItemCategoryIndex = "item_category_index";
    private final String keyItemCategoryId = "item_category_id";
    private final String keyItemCategoryName = "item_category_name";
    // Table item
    private final String tableItem = "tbl_item";
    private final String keyItemIndex = "item_index";
    private final String keyItemSequenceNumber = "item_seq_num";
    private final String keyItemId = "item_id";
    private final String keyItemName = "item_name";
    private final String keyItemWeight = "item_weight";
    private final String keyItemUnit = "item_unit";
    private final String keyItemUnitQty = "item_unit_qty";
    private final String keyItemPackage = "item_package";
    private final String keyItemStockQty = "stock_qty";
    private final String keyItemWholeSalePrice = "ws_price";
    private final String keyItemConsumerPrice = "ret_price";
    private final String keyItemImageUri = "image_uri";
    // item category id
    private final String keyItemHasFlavours = "has_flavours";
    // Table item type
    private final String tableItemType = "tbl_item_type";
    private final String keyTypeIndex = "item_type_index";
    private final String keyTypeId = "item_type_id";
    private final String keyTypeName = "item_type_name";
    // Table flavour
    private final String tableFlavour = "tbl_flavour";
    private final String keyFlavourIndex = "flavour_index";
    private final String keyFlavourId = "flavour_id";
    private final String keyFlavourName = "flavour_name";
    private final String keyFlavourColour = "flavour_colour";
    // Table item-flavour
    private final String tableItemFlavour = "tbl_item_flavour";
    // Also item id and flavour id
    private final String keyItemFlavourIndex = "if_index";
    // Table focus-item
    private final String tableFocusItem = "tbl_focus_item";
    // Also item id
    private final String keyFocusItemIndex = "fi_index";
    // Table category
    private final String tableCategory = "tbl_category";
    private final String keyCategoryIndex = "cat_index";
    private final String keyCategoryId = "cat_id";
    private final String keyCategoryName = "cat_name";
    // Table bank
    private final String tableBank = "tbl_bank";
    private final String keyBankId = "bank_id";
    private final String keyBankName = "bank_name";
    // Table branch
    private final String tableBankBranch = "tbl_bank_branch";
    private final String keyBankBranchIndex = "branch_index";
    private final String keyBankBranchId = "branch_id";
    // Also bank id
    private final String keyBankBranchName = "branch_name";
    // Table unproductive reasons
    private final String tableUnproductiveReason = "tbl_unp_reason";
    private final String keyUnproductiveReasonId = "reason_id";
    private final String keyUnproductiveReasonName = "reason_name";
    private final String keyUnproductiveReasonImageRequired = "image_required";
    private final String tableErrorTracker = "tbl_error_tracker";
    private final String keyErrorIndex = "error_index";
    private final String keyErrorTime = "error_time";
    private final String keyErrorFunction = "error_function";
    private final String keyErrorStackTrace = "error_stack_trace";
    private final String keyErrorResponse = "error_response";
    private final String tableUnproductiveCall = "tbl_unproductive_call";
    private final String keyUnproductiveIndex = "unprod_index";
    private final String keyUnproductiveTime = "unprod_time";
    private final String keyUnproductiveReason = "unprod_reason";
    //out_id, lat, bat and lon
    private final String keyUnproductiveRemark = "unprod_remark";
    private final String tableOrder = "tbl_order";
    private final String keyOrderIndex = "order_index";
    private final String keyOrderId = "order_id";
    private final String keyGrossAmount = "order_gross";
    private final String keyReturnAmount = "order_return";
    private final String keyEligibleAmount = "order_eligible";
    private final String keyOrderDiscount = "order_discount";
    private final String keyMarginAmount = "order_margin";
    private final String keyNetAmount = "order_net";
    private final String keyDiscountPercentage = "order_discount_percentage";
    // out_id, lat, lon, is_synced, bat
    private final String keyOrderTime = "order_time";
    private final String tableOrderDetail = "tbl_order_detail";
    private final String keyDetailIndex = "detail_index";
    // order_id, item_id, flavour_id, return_qty
    private final String keyDetailSelectedQty = "detail_selected_qty";
    private final String keyDetailFreeQty = "detail_free_qty";
    private final String tableReturnDetail = "tbl_return_detail";
    private final String keyReturnIndex = "return_index";
    // order_id, item_id, flavour_id
    private final String keyReturnQty = "return_qty";
    private final String keyReturnPrice = "return_price";
    private final String tableOrderPayment = "tbl_order_payment";
    // order_id, cash_amount, cheque_amount, cheque_date, cheque_number, bank_id, branch_id
    private final String keyPaymentIndex = "payment_index";
    private final String tableVisitDetails = "tbl_visit_details";
    private final String keyVisitIndex = "visit_index";
    // session_id
    // out_id
    private final String keyVisitStatus = "visit_status";
    private final String tableAttendance = "tbl_attendance";
    private final String keyAttendanceIndex = "attendance_index";
    private final String keyAttendanceTime = "attendance_time";
    // is_synced, lat, lon, local_session
    private final String keyAttendanceStatus = "attendance_status";
    private final String keyAttendanceLocation = "attendance_location";

    private final String tableDistributorStock = "tbl_dis_stock";
    private final String keyDistributorStockIndex = "dis_stock_index";
    private final String keyDistributorStockId = "dis_stock_id";
    private final String keyDistributorStockQty = "dis_stock_qty";
    private final String tableMobileStock = "tbl_mob_stock";
    private final String keyMobileStockIndex = "mob_stock_index";
    private final String keyMobileStockId = "mob_stock_id";
    private final String keyMobileStockQty = "mob_stock_qty";
    private final String flagIsSynced = "is_synced";
    private final String keyLatitude = "latitude";
    private final String keyLongitude = "longitude";
    private final String keyBatteryLevel = "battery_level";
    private final String keyLocalSession = "local_session";

    private DatabaseHandler(Context context) {
        super(context, "edna_db", null, 1);
    }

    public static synchronized DatabaseHandler getDbHandler(Context context) {
        if (dbHandler == null) {
            dbHandler = new DatabaseHandler(context);
        }
        return dbHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + tableUser + "("
                + keyUserId + " integer(8) primary key, "
                + keyUserType + " integer(3), "
                + keyUserLocationId + " integer(8), "
                + keyUserName + " varchar(64), "
                + keyUserUsername + " varchar(32), "
                + keyUserPosition + " varchar(32), "
                + keyUserContact + " varchar(10), "
                + keyUserTerritoryId + " integer(8), "
                + keyUserTerritory + " varchar(32), "
                + keyUserImageUri + " varchar(128), "
                + keyUserSalesType + " integer(8)"
                + ")");
        db.execSQL("create table if not exists " + tableRoute + "("
                + keyRouteIndex + " integer primary key autoincrement, "
                + keyRouteId + " integer(8), "
                + keyRouteCode + " varchar(12), "
                + keyRouteName + " varchar(64), "
                + keyRouteFixedTarget + " double(12), "
                + keyRouteSelectedTarget + " double(12)"
                + ")");
        db.execSQL("create table if not exists " + tableOutletType + "("
                + keyOutletTypeIndex + " integer primary key autoincrement, "
                + keyOutletTypeId + " integer(8), "
                + keyOutletTypeName + " varchar(32)"
                + ")");
        db.execSQL("create table if not exists " + tableOutletClass + "("
                + keyOutletClassIndex + " integer primary key autoincrement, "
                + keyOutletClassId + " integer(8), "
                + keyOutletClassName + " varchar(32)"
                + ")");
        db.execSQL("create table if not exists " + tableOutlet + "("
                + keyOutletIndex + " integer primary key autoincrement, "
                + keyOutletId + " integer(8), "
                + keyRouteId + " integer(8), "
                + keyOutletTypeId + " integer(8), "
                + keyOutletClassId + " integer(8), "
                + keyOutletName + " varchar(64), "
                + keyOutletCode + " varchar(16), "
                + keyOutletAddress + " varchar(64), "
                + keyOutletOwnerName + " varchar(64), "
//                + keyOutletOwnerDOB + " long(16), "
                + keyOutletContactLand + " varchar(10), "
//                + keyOutletContactMobile + " varchar(10), "
//                + keyOutletAssistantName + " varchar(64), "
//                + keyOutletAssistantDOB + " long(16), "
                + keyOutletFrontImageUri + " varchar(128), "
                + keyOutletShowcaseImageUri + " varchar(128), "
                + keyOutletPromotion1ImageUri + " varchar(128), "
                + keyOutletPromotion2ImageUri + " varchar(128), "
                + keyOutletTarget + " double(12)"
                + ")");
        db.execSQL("create table if not exists " + tableOutletHistory + "("
                + keyOutletHistoryIndex + " integer primary key autoincrement, "
                + keyOutletId + " integer(8), "
                + keyInvoiceDate + " long(16), "
                + keyInvoiceNo + " integer(8), "
                + keyInvoiceAmount + " double(8), "
                + keyOrderDiscount + " double(8), "
                + keyReturnAmount + " double(8), "
                + keyInvoiceRemark + " varchar(64), "
                + keyOutletHistoryType + " integer(1)"
                + ")");
        db.execSQL("create table if not exists " + tableCashPayment + "("
                + keyCashIndex + " integer primary key autoincrement, "
                + keyInvoiceNo + " integer(8), "
                + keyCashDate + " long(16), "
                + keyCashAmount + " double(8), "
                + flagIsSynced + " boolean default 1"
                + ")");
        db.execSQL("create table if not exists " + tableChequePayment + "("
                + keyChequeIndex + " integer primary key autoincrement, "
                + keyInvoiceNo + " integer(8), "
                + keyChequeNo + " varchar(8), "
                + keyChequeDate + " long(16), "
                + keyChequeAmount + " double(8), "
                + keyBankId + " integer(8), "
                + keyBankBranchId + " integer(8), "
                + flagIsSynced + " boolean default 1"
                + ")");
        db.execSQL("create table if not exists " + tableItemCategory + "("
                + keyItemCategoryIndex + " integer primary key autoincrement, "
                + keyItemCategoryId + " integer(8), "
                + keyItemCategoryName + " varchar(32)"
                + ")");
        db.execSQL("create table if not exists " + tableItemType + "("
                + keyTypeIndex + " integer primary key autoincrement, "
                + keyTypeId + " integer(8), "
                + keyTypeName + " varchar(64)"
                + ")");
        db.execSQL("create table if not exists " + tableItem + "("
                + keyItemIndex + " integer primary key autoincrement, "
                + keyItemId + " integer(8), "
                + keyItemSequenceNumber + " integer(8), "
                + keyItemName + " varchar(32), "
                + keyItemWeight + " varchar(8), "
                + keyItemPackage + " varchar(12), "
                + keyItemStockQty + " double(10), "
                + keyItemWholeSalePrice + " double(8), "
                + keyItemConsumerPrice + " double(8), "
                + keyFlavourId + " integer(8), "
                + keyItemImageUri + " varchar(128), "
                + keyItemHasFlavours + " boolean default 0, "
                + keyItemCategoryId + " integer(4), "
                + keyTypeId + " integer(8), "
                + keyItemUnit + " varchar(16), "
                + keyItemUnitQty + " integer(8)"
                + ")");
        db.execSQL("create table if not exists " + tableErrorTracker + "("
                + keyErrorIndex + " integer primary key autoincrement, "
                + keyErrorTime + " long(18), "
                + keyErrorFunction + " varchar(32), "
                + keyErrorStackTrace + " text, "
                + keyErrorResponse + " text, "
                + flagIsSynced + " boolean default 0"
                + ")");
        db.execSQL("create table if not exists " + tableUnproductiveCall + "("
                + keyUnproductiveIndex + " integer primary key autoincrement, "
                + keyUnproductiveTime + " long(18), "
                + keyUnproductiveReason + " varchar(32), "
                + keyUnproductiveRemark + " varchar(32), "
                + keyOutletId + " integer(8), "
                + keyLatitude + " double(12), "
                + keyLongitude + " varchar(12), "
                + keyBatteryLevel + " integer(3), "
                + flagIsSynced + " boolean default 0"
                + ")");
        db.execSQL("create table if not exists " + tableBank + "("
                + keyBankId + " integer primary key, "
                + keyBankName + " varchar(64)"
                + ")");
        db.execSQL("create table if not exists " + tableBankBranch + "("
                + keyBankBranchIndex + " integer primary key autoincrement, "
                + keyBankBranchId + " integer(8), "
                + keyBankBranchName + " varchar(64), "
                + keyBankId + " integer(8)"
                + ")");
        db.execSQL("create table if not exists " + tableDistributorStock + "("
                + keyDistributorStockIndex + " integer primary key autoincrement, "
                + keyDistributorStockId + " integer(8), "
                + keyDistributorStockQty + " integer(8)"
                + ")");
        db.execSQL("create table if not exists " + tableMobileStock + "("
                + keyMobileStockIndex + " integer primary key autoincrement, "
                + keyMobileStockId + " integer(8), "
                + keyMobileStockQty + " double(10)"
                + ")");
        db.execSQL("create table if not exists " + tableOrder + "("
                + keyOrderIndex + " integer primary key autoincrement, "
                + keyOrderId + " long(18), "
                + keyOutletId + " integer(8), "
                + keyOrderDiscount + " double(12), "
                + keyLatitude + " varchar(16), "
                + keyLongitude + " varchar(16), "
                + keyOrderTime + " long(16), "
                + keyBatteryLevel + " integer(3), "
                + flagIsSynced + " boolean default 0, "
                + keyGrossAmount + " double(12), "
                + keyReturnAmount + " double(12), "
                + keyEligibleAmount + " double(12), "
                + keyMarginAmount + " double(12), "
                + keyNetAmount + " double(12), "
                + keyDiscountPercentage + " double(12)"
                + ")");
        db.execSQL("create table if not exists " + tableOrderDetail + "("
                + keyDetailIndex + " integer primary key autoincrement, "
                + keyOrderId + " long(18), "
                + keyItemId + " integer(8), "
                + keyFlavourId + " integer(8), "
                + keyDetailSelectedQty + " double(8), "
                + keyDetailFreeQty + " integer(8), "
                + keyReturnQty + " integer(8)"
                + ")");
        db.execSQL("create table if not exists " + tableReturnDetail + "("
                + keyReturnIndex + " integer primary key autoincrement, "
                + keyOrderId + " long(18), "
                + keyItemId + " integer(8), "
                + keyFlavourId + " integer(8), "
                + keyReturnQty + " integer(12), "
                + keyReturnPrice + " double(12)"
                + ")");
        db.execSQL("create table if not exists " + tableOrderPayment + "("
                + keyPaymentIndex + " integer primary key autoincrement, "
                + keyOrderId + " long(18), "
                + keyCashAmount + " double(12), "
                + keyChequeNo + " varchar(18), "
                + keyChequeAmount + " double(16), "
                + keyChequeDate + " long(18), "
                + keyBankId + " integer(8), "
                + keyBankBranchId + " integer(8)"
                + ")");
        db.execSQL("create table if not exists " + tableVisitDetails + "("
                + keyVisitIndex + " integer primary key autoincrement, "
                + keyOutletId + " integer(8), "
                + keyVisitStatus + " integer(2), "
                + keyLocalSession + " integer(8)"
                + ")");
        db.execSQL("create table if not exists " + tableAttendance + "("
                + keyAttendanceIndex + " integer primary key autoincrement, "
                + keyAttendanceTime + " long(16), "
                + keyAttendanceStatus + " integer(2), "
                + keyAttendanceLocation + " varchar(128), "
                + keyLatitude + " double(12), "
                + keyLongitude + " double(12), "
                + keyLocalSession + " integer(8), "
                + flagIsSynced + " boolean default 0"
                + ")");
        db.execSQL("create table if not exists " + tableFlavour + "("
                + keyFlavourIndex + " integer primary key autoincrement, "
                + keyFlavourId + " integer(8), "
                + keyFlavourName + " varchar(32), "
                + keyFlavourColour + " varchar(16)"
                + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableSession + " ("
                + keyUserId + " integer(8) primary key, "
                + keyLoggedInTimestamp + " integer(8)"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + tableUser);
        db.execSQL("drop table if exists " + tableRoute);
        db.execSQL("drop table if exists " + tableOutlet);
        db.execSQL("drop table if exists " + tableOutletHistory);
        db.execSQL("drop table if exists " + tableCashPayment);
        db.execSQL("drop table if exists " + tableChequePayment);
        db.execSQL("drop table if exists " + tableItem);
        db.execSQL("drop table if exists " + tableItemCategory);
        db.execSQL("drop table if exists " + tableItemType);
        db.execSQL("drop table if exists " + tableErrorTracker);
        db.execSQL("drop table if exists " + tableUnproductiveCall);
        db.execSQL("drop table if exists " + tableBank);
        db.execSQL("drop table if exists " + tableBankBranch);
        db.execSQL("drop table if exists " + tableOrder);
        db.execSQL("drop table if exists " + tableOrderDetail);
        db.execSQL("drop table if exists " + tableReturnDetail);
        db.execSQL("drop table if exists " + tableOrderPayment);
        db.execSQL("drop table if exists " + tableVisitDetails);
        db.execSQL("drop table if exists " + tableAttendance);
        db.execSQL("drop table if exists " + tableFlavour);
        db.execSQL("drop table if exists " + tableSession);
        onCreate(db);
    }

    public void setLoggedInTime(int userId) {
        long unixTime = System.currentTimeMillis() / 1000L;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + tableSession);
        ContentValues contentValues = new ContentValues();
        contentValues.put(keyUserId, userId);
        contentValues.put(keyLoggedInTimestamp, String.valueOf(unixTime));
        db.insert(tableSession, null, contentValues);
        db.close();
    }

    public long getLoggedInTime(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "
                + keyLoggedInTimestamp + " "
                + "FROM " + tableSession + " "
                + "WHERE " + keyUserId + "=" + String.valueOf(userId);
        Log.i(LOG_TAG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            long timestamp = cursor.getLong(0);
            cursor.close();
            db.close();
            return timestamp;
        } else {
            return 0;
        }
    }

    /* **************************************** User **************************************** */

    public void storeUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + tableUser);

//        Log.d(LOG_TAG, "Storing User");

        ContentValues values = new ContentValues();
        values.put(keyUserId, user.getId());
        values.put(keyUserType, user.getType());
        values.put(keyUserLocationId, user.getLocationId());
        values.put(keyUserName, user.getName());
        values.put(keyUserUsername, user.getUsername());
        values.put(keyUserPosition, user.getPosition());
        values.put(keyUserContact, user.getContact());
        values.put(keyUserTerritory, user.getTerritory());
        values.put(keyUserImageUri, user.getImageURI());
        values.put(keyUserTerritoryId, user.getTerritoryId());
        values.put(keyUserSalesType, user.getSalesType());
        db.insert(tableUser, null, values);
        db.close();
    }

    public User getUser() {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select "
                + keyUserId + ", "
                + keyUserName + ", "
                + keyUserPosition + ", "
                + keyUserContact + ", "
                + keyUserTerritory + ", "
                + keyUserImageUri + ", "
                + keyUserUsername + ", "
                + keyUserTerritoryId + ", "
                + keyUserType + ", "
                + keyUserLocationId + ", "
                + keyUserSalesType
                + " from " + tableUser;
        Cursor cursor = db.rawQuery(selectQuery, null);
        User user = new User();
        if (cursor.moveToFirst()) {
            user = new User(cursor.getInt(0), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getString(1), cursor.getString(6),
                    cursor.getString(2), cursor.getString(3), cursor.getInt(7), cursor.getString(4), cursor.getString(5));
        }

        cursor.close();
        db.close();

        return user;
    }

    public String getImageURIOfUser(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select " + keyUserImageUri + " from " + tableUser
                + " where " + keyUserId + "=?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            String imageURI = cursor.getString(0);

            cursor.close();

            return imageURI;
        } else {

            return null;
        }
    }

    /* ********************************* ****************** ********************************* */

    /* *************************************** Stock **************************************** */

//    public void storeDistributorStockItems(List<Stock> stocks) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        for(Stock stock : stocks) {
//            values.put(keyDistributorStockId, stock.getStockItemId());
//            values.put(keyDistributorStockQty, stock.getStockItemQty());
//            db.insert(tableDistributorStock, null, values);
//        }
//        db.close();
//    }
//
//    public List<Stock> getDistributorStock() {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "select "
//                + keyDistributorStockId + ", "
//                + keyDistributorStockQty
//                + " from " + tableDistributorStock;
//        Cursor cursor = db.rawQuery(query, null);
//        if(cursor.moveToFirst()){
//            List<Stock> stocks = new ArrayList<Stock>();
//            do {
//                stocks.add(new Stock(cursor.getInt(0), cursor.getInt(1)));
//            } while(cursor.moveToNext());
//            return stocks;
//        }
//
//        return null;
//    }
//
//    public void storeMobileStockItems(List<Stock> stocks) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        for(Stock stock : stocks) {
//            values.put(keyMobileStockId, stock.getStockItemId());
//            values.put(keyMobileStockQty, stock.getStockItemQty());
//            db.insert(tableMobileStock, null, values);
//        }
//        db.close();
//    }
//
//    public List<Stock> getMobileStock() {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "select "
//                + keyMobileStockId + ", "
//                + keyMobileStockQty
//                + " from " + tableMobileStock;
//        Cursor cursor = db.rawQuery(query, null);
//        if(cursor.moveToFirst()){
//            List<Stock> stocks = new ArrayList<Stock>();
//            do {
//                stocks.add(new Stock(cursor.getInt(0), cursor.getInt(1)));
//            } while(cursor.moveToNext());
//            return stocks;
//        }
//
//        return null;
//    }

    public void updateStockDetails(List<Stock> stockDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Stock stock : stockDetails) {
            values.put(keyItemStockQty, stock.getStockItemQty());
            db.update(tableItem, values, keyItemId + "=?", new String[]{String.valueOf(stock.getStockItemId())});
        }
        db.close();
    }

    /* ********************************* ****************** ********************************* */

    /* *************************************** Order **************************************** */

    public void storePayment(Payment payment) {
        SQLiteDatabase writableDatabase = getWritableDatabase();

        CashPayment cashPayment = payment.getCashPayment();
        if (cashPayment != null && cashPayment.getPaymentAmount() > 0) {
            ContentValues cashPaymentValues = new ContentValues();
            cashPaymentValues.put(keyInvoiceNo, payment.getOrderId());
            cashPaymentValues.put(keyCashDate, cashPayment.getPaymentTime());
            cashPaymentValues.put(keyCashAmount, cashPayment.getPaymentAmount());
            writableDatabase.insert(tableCashPayment, null, cashPaymentValues);
        }

        Cheque cheque = payment.getCheque();
        if (cheque != null && cheque.getAmount() > 0) {
            ContentValues checkPaymentValues = new ContentValues();
            checkPaymentValues.put(keyInvoiceNo, payment.getOrderId());
            checkPaymentValues.put(keyChequeNo, cheque.getChequeNo());
            checkPaymentValues.put(keyChequeDate, cheque.getChequeDate());
            checkPaymentValues.put(keyChequeAmount, cheque.getAmount());
            checkPaymentValues.put(keyBankId, cheque.getBankId());
            checkPaymentValues.put(keyBankBranchId, cheque.getBranchid());
            writableDatabase.insert(tableChequePayment, null, checkPaymentValues);
        }

        /*
        *
        db.execSQL("create table if not exists " + tableChequePayment + "("
                + keyChequeIndex + " integer primary key autoincrement, "
                + keyInvoiceNo + " integer(8), "
                + keyChequeNo + " varchar(8), "
                + keyChequeDate + " long(16), "
                + keyChequeAmount + " double(8), "
                + keyBankId + " integer(8), "
                + keyBankBranchId + " integer(8), "
                + flagIsSynced + " boolean default 0"
                + ")");
        * */
    }

    public void storeOrder(Order order) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        ContentValues detailValues = new ContentValues();
        ContentValues returnValues = new ContentValues();
        ContentValues paymentValues = new ContentValues();
        ContentValues updateValues = new ContentValues();
        ContentValues outletHistory = new ContentValues();

        orderValues.put(keyOrderId, order.getOrderId());
        orderValues.put(keyOutletId, order.getOutletId());
        orderValues.put(keyLatitude, order.getLatitude());
        orderValues.put(keyLongitude, order.getLongitude());
        orderValues.put(keyOrderDiscount, order.getDiscount());
        orderValues.put(keyOrderTime, order.getOrderTime());
        orderValues.put(keyBatteryLevel, order.getBatteryLevel());
        orderValues.put(keyGrossAmount, order.getGrossAmount());
        orderValues.put(keyReturnAmount, order.getReturnAmount());
        orderValues.put(keyEligibleAmount, order.getEligibleAmount());
        orderValues.put(keyMarginAmount, order.getMarginAmount());
        orderValues.put(keyNetAmount, order.getNetAmount());
        orderValues.put(keyDiscountPercentage, order.getDiscountPercentage());
        orderValues.put(flagIsSynced, order.isSynced() ? 1 : 0);
        db.insert(tableOrder, null, orderValues);

        List<OrderDetail> orderDetails = order.getOrderDetails();
        if (orderDetails != null) {
            for (OrderDetail orderDetail : orderDetails) {

                detailValues.put(keyOrderId, order.getOrderId());
                detailValues.put(keyItemId, orderDetail.getItem().getItemNo());
                detailValues.put(keyFlavourId, 0); // Must change this to accommodate flavours later
                detailValues.put(keyDetailSelectedQty, orderDetail.getItem().getSelectedQty());
                detailValues.put(keyDetailFreeQty, orderDetail.getFreeQty());
                detailValues.put(keyReturnQty, orderDetail.getItem().getReturnQty());
                db.insert(tableOrderDetail, null, detailValues);

                // Reduce the stock item qty
                double newQty = orderDetail.getItem().getStockQty() - orderDetail.getItem().getSelectedQty();
                updateValues.put(keyItemStockQty, newQty);
                db.update(tableItem, updateValues, keyItemId + "=?", new String[]{String.valueOf(orderDetail.getItem().getItemNo())});

            }

        }

        List<ReturnDetail> returnDetails = order.getReturnDetails();
        if (returnDetails != null) {
            for (ReturnDetail returnDetail : returnDetails) {
                returnValues.put(keyOrderId, order.getOrderId());
                returnValues.put(keyItemId, returnDetail.getItem().getItemNo());
                returnValues.put(keyFlavourId, 0);
                returnValues.put(keyReturnQty, returnDetail.getQty());
                returnValues.put(keyReturnPrice, returnDetail.getReturnPrice());
                db.insert(tableReturnDetail, null, returnValues);
            }
        }


        outletHistory.put(keyOutletId, order.getOutletId());
        outletHistory.put(keyInvoiceDate, order.getOrderTime());
        outletHistory.put(keyInvoiceNo, order.getOrderId());
        outletHistory.put(keyInvoiceAmount, order.getGrossAmount());
        outletHistory.put(keyOrderDiscount, order.getDiscount());
        outletHistory.put(keyReturnAmount, order.calculateTotalReturns());
        outletHistory.put(keyInvoiceRemark, "left for edit");
        outletHistory.put(keyOutletHistoryType, 1);
        db.insert(tableOutletHistory, null, outletHistory);

        /*db.execSQL("create table if not exists " + tableOutletHistory + "("
                + keyOutletHistoryIndex + " integer primary key autoincrement, "
                + keyOutletId + " integer(8), "
                + keyInvoiceDate + " long(16), "
                + keyInvoiceNo + " integer(8), "
                + keyInvoiceAmount + " double(8), "
                + keyInvoiceRemark + " varchar(64), "
                + keyOutletHistoryType + " integer(1)"
                + ")");*/

//        Payment payment = order.getPayment();
//        if (payment != null) {
//            paymentValues.put(keyOrderId, order.getOrderId());
//            if (payment.getCashPayment() != null) {
//                paymentValues.put(keyCashAmount, order.getPayment().getCashPayment().getPaymentAmount());
//            }
//            if (payment.getCheque() != null) {
//                paymentValues.put(keyChequeNo, payment.getCheque().getChequeNo());
//                paymentValues.put(keyChequeDate, payment.getCheque().getChequeDate());
//                paymentValues.put(keyChequeAmount, payment.getCheque().getAmount());
//                paymentValues.put(keyBankId, payment.getCheque().getBankId());
//                paymentValues.put(keyBankBranchId, payment.getCheque().getBranchid());
//            }
//            db.insert(tableOrderPayment, null, paymentValues);
//        }

//        Log.d("DatabaseHandler.java -> storeOrder()", "ORDER TABLE\n" + DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableOrder, null)));
//        Log.d("DatabaseHandler.java -> storeOrder()", "ORDER DETAILS TABLE\n" + DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableOrderDetail, null)));
//        Log.d("DatabaseHandler.java -> storeOrder()", "ORDER RETURN DETAILS TABLE\n" + DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableReturnDetail, null)));
//        Log.d("DatabaseHandler.java -> storeOrder()", "ORDER PAYMENT TABLE\n" + DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableOrderPayment, null)));

        db.close();
    }

    public List<Order> getAllOrders() {

        SQLiteDatabase db = this.getReadableDatabase();

        String orderQuery = "select "
                + keyOrderId + ", "
                + keyOutletId + ", "
                + keyOrderDiscount + ", "
                + keyLatitude + ", "
                + keyLongitude + ", "
                + keyOrderTime + ", "
                + flagIsSynced + ", "
                + keyBatteryLevel + ", "
                + keyGrossAmount + ", "
                + keyReturnAmount + ", "
                + keyEligibleAmount + ", "
                + keyMarginAmount + ", "
                + keyNetAmount
                + " from " + tableOrder;
        String orderDetailQuery = "select "
                + keyItemId + ", "
                + keyFlavourId + ", "
                + keyDetailSelectedQty + ", "
                + keyDetailFreeQty + ", "
                + keyReturnQty
                + " from " + tableOrderDetail + " where " + keyOrderId + "=?";
        String returnDetailQuery = "select "
                + keyItemId + ", "
                + keyFlavourId + ", "
                + keyReturnQty + ", "
                + keyReturnPrice
                + " from " + tableReturnDetail + " where " + keyOrderId + "=?";
        String paymentQuery = "select "
                + keyCashAmount + ", "
                + keyChequeNo + ", "
                + keyChequeAmount + ", "
                + keyChequeDate + ", "
                + keyBankId + ", "
                + keyBankBranchId
                + " from " + tableOrderPayment + " where " + keyOrderId + "=?";

        List<Order> orders = new ArrayList<Order>();

        Cursor orderCursor = db.rawQuery(orderQuery, null);
        if (orderCursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderId(orderCursor.getLong(0));
                order.setOutletId(orderCursor.getInt(1));
                order.setDiscount(orderCursor.getDouble(2));
                order.setLatitude(Double.parseDouble(orderCursor.getString(3)));
                order.setLongitude(Double.parseDouble(orderCursor.getString(4)));
                order.setOrderTime(orderCursor.getLong(5));
                order.setSynced(orderCursor.getInt(6) == 1);
                order.setBatteryLevel(orderCursor.getInt(7));
                order.setGrossAmount(orderCursor.getInt(8));
                order.setReturnAmount(orderCursor.getInt(9));
                order.setEligibleAmount(orderCursor.getInt(10));
                order.setMarginAmount(orderCursor.getInt(11));
                order.setNetAmount(orderCursor.getInt(12));
                order.setRouteId(getRouteOfOutlet(db, order.getOutletId()));

                long orderId = order.getOrderId();

                Item item = null;
                List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
                Cursor detailCursor = db.rawQuery(orderDetailQuery, new String[]{String.valueOf(orderId)});
                if (detailCursor.moveToFirst()) {
                    do {
                        int itemId = detailCursor.getInt(0);
                        item = getItem(db, itemId);
                        item.setSelectedQty(detailCursor.getInt(2));
                        OrderDetail orderDetail = new OrderDetail(orderId, item, detailCursor.getInt(3));
                        orderDetail.setFlavourId(detailCursor.getInt(1));

                        item.setReturnQty(detailCursor.getInt(4));

                        orderDetails.add(orderDetail);
                    } while (detailCursor.moveToNext());
                }
                detailCursor.close();

                order.setOrderDetails(orderDetails);

                List<ReturnDetail> returnDetails = new ArrayList<ReturnDetail>();
                Cursor returnCursor = db.rawQuery(returnDetailQuery, new String[]{String.valueOf(orderId)});
                if (returnCursor.moveToFirst()) {
                    do {
                        int itemId = returnCursor.getInt(0);
                        if (item == null) {
                            item = getItem(db, itemId);
                        }
                        ReturnDetail returnDetail = new ReturnDetail(orderId, item, returnCursor.getInt(1), returnCursor.getInt(2), returnCursor.getDouble(3));

                        returnDetails.add(returnDetail);
                    } while (returnCursor.moveToNext());
                }
                returnCursor.close();

                order.setReturnDetails(returnDetails);

                // Get Payment info here

                Payment payment = new Payment();
                Cursor paymentCursor = db.rawQuery(paymentQuery, new String[]{String.valueOf(orderId)});
                if (paymentCursor.moveToFirst()) {
                    payment.setOrderId(orderId);
                    payment.setCashPayment(new CashPayment(0, paymentCursor.getDouble(0)));
                    // Set cheque values here
                    payment.setCheque(new Cheque(paymentCursor.getString(1), paymentCursor.getLong(3), paymentCursor.getDouble(2), paymentCursor.getInt(4), paymentCursor.getInt(5)));
                }
                paymentCursor.close();

                order.setPayment(payment);

                orders.add(order);

            } while (orderCursor.moveToNext());

        }

        return orders;
    }

    public List<Order> getUnsyncedOrders() {

        SQLiteDatabase db = this.getReadableDatabase();

        String orderQuery = "select "
                + keyOrderId + ", "
                + keyOutletId + ", "
                + keyOrderDiscount + ", "
                + keyLatitude + ", "
                + keyLongitude + ", "
                + keyOrderTime + ", "
                + flagIsSynced + ", "
                + keyBatteryLevel
                + " from " + tableOrder + " where not " + flagIsSynced;
        String orderDetailQuery = "select "
                + keyItemId + ", "
                + keyFlavourId + ", "
                + keyDetailSelectedQty + ", "
                + keyDetailFreeQty + ", "
                + keyReturnQty
                + " from " + tableOrderDetail + " where " + keyOrderId + "=?";
        String returnDetailQuery = "select "
                + keyItemId + ", "
                + keyFlavourId + ", "
                + keyReturnQty + ", "
                + keyReturnPrice
                + " from " + tableReturnDetail + " where " + keyOrderId + "=?";
//        String paymentQuery = "select "
//                + keyCashAmount + ", "
//                + keyChequeNo + ", "
//                + keyChequeAmount + ", "
//                + keyChequeDate + ", "
//                + keyBankId + ", "
//                + keyBankBranchId
//                + " from " + tableOrderPayment + " where " + keyOrderId + "=?";

        Cursor orderCursor = db.rawQuery(orderQuery, null);
        if (orderCursor.moveToFirst()) {

            List<Order> orders = new ArrayList<Order>();

            do {
                Order order = new Order();
                order.setOrderId(orderCursor.getLong(0));
                order.setOutletId(orderCursor.getInt(1));
                order.setDiscount(orderCursor.getDouble(2));
                order.setLatitude(Double.parseDouble(orderCursor.getString(3)));
                order.setLongitude(Double.parseDouble(orderCursor.getString(4)));
                order.setOrderTime(orderCursor.getLong(5));
                order.setSynced(orderCursor.getInt(6) == 1);
                order.setBatteryLevel(orderCursor.getInt(7));

                order.setRouteId(getRouteOfOutlet(db, order.getOutletId()));

                long orderId = order.getOrderId();

                List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
                Cursor detailCursor = db.rawQuery(orderDetailQuery, new String[]{String.valueOf(orderId)});
                if (detailCursor.moveToFirst()) {
                    do {
                        int itemId = detailCursor.getInt(0);
                        Item item = getItem(db, itemId);
                        item.setSelectedQty(detailCursor.getDouble(2));
                        OrderDetail orderDetail = new OrderDetail(orderId, item, detailCursor.getInt(3));
                        orderDetail.setFlavourId(detailCursor.getInt(1));

                        item.setReturnQty(detailCursor.getInt(4));

                        orderDetails.add(orderDetail);
                    } while (detailCursor.moveToNext());
                }

                order.setOrderDetails(orderDetails);

                List<ReturnDetail> returnDetails = new ArrayList<ReturnDetail>();
                Cursor returnCursor = db.rawQuery(returnDetailQuery, new String[]{String.valueOf(orderId)});
                if (returnCursor.moveToFirst()) {
                    do {
                        int itemId = returnCursor.getInt(0);
                        Item item = getItem(db, itemId);
                        ReturnDetail returnDetail = new ReturnDetail(orderId, item, returnCursor.getInt(1), returnCursor.getInt(2), returnCursor.getDouble(3));

                        returnDetails.add(returnDetail);
                    } while (returnCursor.moveToNext());
                }

                order.setReturnDetails(returnDetails);

                // Get Payment info here

//                Payment payment = new Payment();
//                Cursor cursor = db.rawQuery(paymentQuery, new String[]{String.valueOf(orderId)});
//                if (cursor.moveToFirst()) {
//                    payment.setOrderId(orderId);
//                    payment.setCashPayment(new CashPayment(0, cursor.getDouble(0)));
//                    // Set cheque values here
//                    payment.setCheque(new Cheque(cursor.getString(1), cursor.getLong(3), cursor.getDouble(2), cursor.getInt(4), cursor.getInt(5)));
//                }
//
//                order.setPayment(payment);

                orders.add(order);

            } while (orderCursor.moveToNext());

            return orders;

        }

        orderCursor.close();

        return null;
    }

    public void setOrderAsSynced(long orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(flagIsSynced, 1);
        db.update(tableOrder, values, keyOrderId + "=?", new String[]{String.valueOf(orderId)});
        db.close();
    }

    public int getUnsyncedOrderCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String orderQuery = "select "
                + keyOrderId
                + " from " + tableOrder + " where not " + flagIsSynced;
        Cursor cursor = db.rawQuery(orderQuery, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getCount();
            db.close();
            return count;
        }

        db.close();
        return 0;
    }

    /* ********************************* ****************** ********************************* */

    /* ************************************** Payments ************************************** */

    public void storeCashPayment(CashPayment cashPayment, long invoiceNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(keyCashDate, cashPayment.getPaymentTime());
        values.put(keyCashAmount, cashPayment.getPaymentAmount());
        values.put(flagIsSynced, cashPayment.isSynced() ? 1 : 0);
        values.put(keyInvoiceNo, invoiceNo);
        db.insert(tableCashPayment, null, values);
        db.close();
    }

    public void storeChequePayment(Cheque cheque, long invoiceNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(keyChequeDate, cheque.getChequeDate());
        values.put(keyChequeAmount, cheque.getAmount());
        values.put(keyChequeNo, cheque.getChequeNo());
        values.put(keyBankId, cheque.getBankId());
        values.put(keyBankBranchId, cheque.getBranchid());
        values.put(flagIsSynced, cheque.isSynced() ? 1 : 0);
        values.put(keyInvoiceNo, invoiceNo);
        db.insert(tableChequePayment, null, values);
        db.close();
    }

    public void storeAllocatedPayments(List<PaymentAllocator> allocations) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cashValues = new ContentValues();
        ContentValues chequeValues = new ContentValues();

        for (PaymentAllocator allocation : allocations) {
            if (allocation.getCashPayment() != null) {
                cashValues.put(keyInvoiceNo, allocation.getInvoiceId());
                cashValues.put(keyCashDate, allocation.getCashPayment().getPaymentTime());
                cashValues.put(keyCashAmount, allocation.getCashPayment().getPaymentAmount());
                cashValues.put(flagIsSynced, allocation.getCashPayment().isSynced());
                db.insert(tableCashPayment, null, cashValues);
            }

            if (allocation.getCheque() != null) {
                chequeValues.put(keyInvoiceNo, allocation.getInvoiceId());
                chequeValues.put(keyChequeNo, allocation.getCheque().getChequeNo());
                chequeValues.put(keyChequeDate, allocation.getCheque().getChequeDate());
                chequeValues.put(keyChequeAmount, allocation.getCheque().getAmount());
                chequeValues.put(keyBankId, allocation.getCheque().getBankId());
                chequeValues.put(keyBankBranchId, allocation.getCheque().getBranchid());
                chequeValues.put(flagIsSynced, allocation.getCheque().isSynced());
                db.insert(tableChequePayment, null, chequeValues);
            }
        }

        db.close();

    }

    public List<CashPayment> getAllCashPaymentsOfInvoice(long invoiceId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select "
                + keyCashDate + ", "
                + keyCashAmount + ", "
                + flagIsSynced
                + " from " + tableCashPayment + " where " + keyInvoiceNo + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(invoiceId)});

        if (cursor.moveToFirst()) {
            List<CashPayment> cashPayments = new ArrayList<>();

            do {
                CashPayment cashPayment = new CashPayment(cursor.getLong(0), cursor.getDouble(1));
                cashPayment.setSynced(cursor.getInt(2) == 1);
                cashPayments.add(cashPayment);
            } while (cursor.moveToNext());

            return cashPayments;

        }

        return null;
    }

    public List<CashPayment> getUnsyncedCashPaymentsOfInvoice(long invoiceId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select "
                + keyCashDate + ", "
                + keyCashAmount + ", "
                + flagIsSynced
                + " from " + tableCashPayment
                + " where " + keyInvoiceNo + "=? and not " + flagIsSynced;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(invoiceId)});

        if (cursor.moveToFirst()) {
            List<CashPayment> cashPayments = new ArrayList<>();

            do {
                CashPayment cashPayment = new CashPayment(cursor.getLong(0), cursor.getDouble(1));
                cashPayment.setSynced(cursor.getInt(2) == 1);
                cashPayments.add(cashPayment);
            } while (cursor.moveToNext());

            return cashPayments;

        }

        return null;
    }

    public List<CashPayment> getUnsyncedCashPayments() {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select "
                + keyCashDate + ", "
                + keyCashAmount + ", "
                + flagIsSynced + ", "
                + keyInvoiceNo
                + " from " + tableCashPayment
                + " where not " + flagIsSynced;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            List<CashPayment> cashPayments = new ArrayList<>();

            do {
                CashPayment cashPayment = new CashPayment(cursor.getLong(0), cursor.getDouble(1));
                cashPayment.setSynced(cursor.getInt(2) == 1);
                cashPayment.setInvoiceId(cursor.getLong(3));
                cashPayments.add(cashPayment);
            } while (cursor.moveToNext());

            return cashPayments;

        }

        return null;
    }

    public List<Cheque> getAllChequePaymentsOfInvoice(long invoiceId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select "
                + keyChequeNo + ", "
                + keyChequeDate + ", "
                + keyChequeAmount + ", "
                + keyBankId + ", "
                + keyBankBranchId + ", "
                + flagIsSynced + ", "
                + keyInvoiceNo
                + " from " + tableChequePayment
                + " where " + keyInvoiceNo + " =?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(invoiceId)});

        if (cursor.moveToFirst()) {
            List<Cheque> cheques = new ArrayList<>();
            do {
                Cheque cheque = new Cheque(cursor.getString(0), cursor.getLong(1), cursor.getDouble(2), cursor.getInt(3), cursor.getInt(4));
                cheque.setSynced(cursor.getInt(5) == 1);
                cheque.setInvoiceId(cursor.getLong(6));
                cheques.add(cheque);
            } while (cursor.moveToNext());

            return cheques;
        }

        return null;
    }

    public List<Cheque> getUnsyncedChequePaymentsOfInvoice(long invoiceId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select "
                + keyChequeNo + ", "
                + keyChequeDate + ", "
                + keyChequeAmount + ", "
                + keyBankId + ", "
                + keyBankBranchId + ", "
                + flagIsSynced
                + " from " + tableChequePayment
                + " where " + keyInvoiceNo + " =? and not " + flagIsSynced;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(invoiceId)});

        if (cursor.moveToFirst()) {
            List<Cheque> cheques = new ArrayList<>();
            do {
                Cheque cheque = new Cheque(cursor.getString(0), cursor.getLong(1), cursor.getDouble(2), cursor.getInt(3), cursor.getInt(4));
                cheque.setSynced(cursor.getInt(5) == 1);
                cheques.add(cheque);
            } while (cursor.moveToNext());

            return cheques;
        }

        return null;
    }

    public List<Cheque> getUnsyncedChequePayments() {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select "
                + keyChequeNo + ", "
                + keyChequeDate + ", "
                + keyChequeAmount + ", "
                + keyBankId + ", "
                + keyBankBranchId + ", "
                + flagIsSynced + ", "
                + keyInvoiceNo
                + " from " + tableChequePayment
                + " where not " + flagIsSynced;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            List<Cheque> cheques = new ArrayList<>();
            do {
                Cheque cheque = new Cheque(cursor.getString(0), cursor.getLong(1), cursor.getDouble(2), cursor.getInt(3), cursor.getInt(4));
                cheque.setSynced(cursor.getInt(5) == 1);
                cheque.setInvoiceId(cursor.getLong(6));
                cheques.add(cheque);
            } while (cursor.moveToNext());

            return cheques;
        }

        return null;
    }

    ;

    public void setCashPaymentAsSynced(List<CashPayment> cashPayments) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "update " + tableCashPayment
                + " set " + flagIsSynced + "=?"
                + " where " + keyInvoiceNo + "=? and " + keyCashAmount + "=? and " + keyCashDate + "=?";

        for (CashPayment cashPayment : cashPayments) {
            db.execSQL(query, new Object[]{1, cashPayment.getInvoiceId(), cashPayment.getPaymentAmount(), cashPayment.getPaymentTime()});
        }

//        ContentValues values = new ContentValues();
//        values.put(flagIsSynced, 1);
//
//        for(CashPayment cashPayment : cashPayments) {
//            if(cashPayment.isSynced()){
//                db.update(tableCashPayment, values, keyInvoiceNo + "=? and " + keyCashDate + " =?", new String[]{String.valueOf(cashPayment.getInvoiceId()), String.valueOf(cashPayment.getPaymentTime())});
//            }
//        }

        db.close();

    }

    public void setChequePaymentAsSynced(List<Cheque> cheques) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "update " + tableChequePayment
                + " set " + flagIsSynced + "=?"
                + " where " + keyInvoiceNo + "=? and " + keyChequeAmount + "=? and " + keyChequeDate + "=?";

        for (Cheque cheque : cheques) {
            db.execSQL(query, new Object[]{1, cheque.getInvoiceId(), cheque.getAmount(), cheque.getChequeDate()});
        }

//        ContentValues values = new ContentValues();
//        values.put(flagIsSynced, 1);
//
//        for(Cheque cheque : cheques) {
//            if(cheque.isSynced()){
//                db.update(tableCashPayment, values, keyInvoiceNo + "=? and " + keyChequeDate + " =?", new String[]{String.valueOf(cheque.getInvoiceId()), String.valueOf(cheque.getChequeDate())});
//            }
//        }

        db.close();

    }

    /* ********************************* ****************** ********************************* */

    /* ********************************* Routes and Outlets ********************************* */

//    /**
//     * Store routes and outlets in Database
//     *
//     * @param routeList list of routes and outlets to store
//     */
//    public void storeRoutesAndOutlets(List<Route> routeList) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        for (Route route : routeList) {
//            ContentValues routeInsert = new ContentValues();
//            routeInsert.put(keyRouteId, route.getRouteId());
//            routeInsert.put(keyRouteName, route.getRouteName());
//            routeInsert.put(keyRouteFixedTarget, route.getFixedTarget());
//            routeInsert.put(keyRouteSelectedTarget, route.getSelectedTarget());
//            db.insert(tableRoute, null, routeInsert);
//
//            if (route.getOutlets() != null) {
//                for (Outlet outlet : route.getOutlets()) {
//                    ContentValues outletInsert = new ContentValues();
//                    outletInsert.put(keyOutletId, outlet.getOutletId());
//                    outletInsert.put(keyOutletName, outlet.getOutletName());
//                    outletInsert.put(keyRouteId, route.getRouteId());j
//                    outletInsert.put(keyOutletAddress, outlet.getAddress());
//                    outletInsert.put(keyOutletOwnerName, outlet.getOwnerName());
//                    outletInsert.put(keyOutletOwnerDOB, outlet.getOwnerDOB());
//                    outletInsert.put(keyOutletContactLand, outlet.getContactLand());
//                    outletInsert.put(keyOutletContactMobile, outlet.getContactMobile());
//                    outletInsert.put(keyOutletAssistantName, outlet.getAssistantName());
//                    outletInsert.put(keyOutletAssistantDOB, outlet.getAssistantDOB());
//                    db.insert(tableOutlet, null, outletInsert);
//
//                    if (outlet.getOutletHistory() != null) {
//                        for (HistoryDetail history : outlet.getOutletHistory()) {
//                            ContentValues historyInsert = new ContentValues();
//                            historyInsert.put(keyOutletId, outlet.getOutletId());
//                            if (history.getHistoryType() == 0) {
//
//                                // No Invoice
//                                historyInsert.put(keyInvoiceDate, history.getDate());
//                                historyInsert.put(keyInvoiceRemark, history.getRemark());
//                                historyInsert.put(keyOutletHistoryType, 0);
//                                db.insert(tableOutletHistory, null, historyInsert);
//                            } else {
//                                // Invoice
//                                Invoice invoice = history.getInvoice();
//                                historyInsert.put(keyInvoiceDate, invoice.getInvoiceTime());
//                                historyInsert.put(keyInvoiceNo, invoice.getInvoiceId());
//                                historyInsert.put(keyInvoiceAmount, invoice.getTotalAmount());
//                                historyInsert.put(keyOrderDiscount, invoice.getTotalDiscount());
//                                historyInsert.put(keyReturnAmount, invoice.getReturnAmount());
//                                historyInsert.put(keyOutletHistoryType, 1);
//                                db.insert(tableOutletHistory, null, historyInsert);
//
//                                if (invoice.getCashPayments() != null) {
//                                    ContentValues cashInsert = new ContentValues();
//                                    for (CashPayment cashPayment : invoice.getCashPayments()) {
//                                        cashInsert.put(keyCashDate, cashPayment.getPaymentTime());
//                                        cashInsert.put(keyCashAmount, cashPayment.getPaymentAmount());
//                                        cashInsert.put(keyInvoiceNo, invoice.getInvoiceId());
//                                        db.insert(tableCashPayment, null, cashInsert);
//                                    }
//                                }
//                                if (invoice.getChequePayments() != null) {
//                                    ContentValues cheqInsert = new ContentValues();
//                                    for (Cheque cheque : invoice.getChequePayments()) {
//                                        cheqInsert.put(keyChequeDate, cheque.getChequeDate());
//                                        cheqInsert.put(keyChequeAmount, cheque.getAmount());
//                                        cheqInsert.put(keyBankId, cheque.getBankId());
//                                        cheqInsert.put(keyBankBranchId, cheque.getBranchid());
//                                        db.insert(tableChequePayment, null, cheqInsert);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Log.d(LOG_TAG, DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableRoute, null)));
//        Log.d(LOG_TAG, DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableOutlet, null)));
//        Log.d(LOG_TAG, DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableOutletHistory, null)));
//        Log.d(LOG_TAG, DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableCashPayment, null)));
//        Log.d(LOG_TAG, DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableChequePayment, null)));
//
//        db.close();
//    }

    public void storeOutletTypes(List<OutletType> outletTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (OutletType outletType : outletTypes) {
            values.put(keyOutletTypeId, outletType.getTypeId());
            values.put(keyOutletTypeName, outletType.getTypeName());
            db.insert(tableOutletType, null, values);
        }

        db.close();
    }



    public List<OutletType> getOutletTypes() {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select "
                + keyOutletTypeId + ", "
                + keyOutletTypeName
                + " from " + tableOutletType;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            List<OutletType> outletTypes = new ArrayList<>();
            do {
                OutletType outletType = new OutletType(cursor.getInt(0), cursor.getString(1));
                outletTypes.add(outletType);
            } while (cursor.moveToNext());
            return outletTypes;
        }

        return null;
    }

    public void storeOutletClasses(List<OutletClass> outletClasses) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (OutletClass outletClass : outletClasses) {
            values.put(keyOutletClassId, outletClass.getClassId());
            values.put(keyOutletClassName, outletClass.getClassName());
            db.insert(tableOutletClass, null, values);
        }

        db.close();
    }

    public List<OutletClass> getOutletClasses() {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select "
                + keyOutletClassId + ", "
                + keyOutletClassName
                + " from " + tableOutletClass;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            List<OutletClass> outletClasses = new ArrayList<>();
            do {
                OutletClass outletClass = new OutletClass(cursor.getInt(0), cursor.getString(1));
                outletClasses.add(outletClass);
            } while (cursor.moveToNext());
            return outletClasses;
        }

        return null;
    }

    public void storeRouteList(List<Route> routeList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Route route : routeList) {
            ContentValues routeInsert = new ContentValues();
            routeInsert.put(keyRouteId, route.getRouteId());
            routeInsert.put(keyRouteCode, route.getRouteCode());
            routeInsert.put(keyRouteName, route.getRouteName());
            routeInsert.put(keyRouteFixedTarget, route.getFixedTarget());
            routeInsert.put(keyRouteSelectedTarget, route.getSelectedTarget());
            db.insert(tableRoute, null, routeInsert);

            if (route.getOutlets() != null) {
                for (Outlet outlet : route.getOutlets()) {
                    ContentValues outletInsert = new ContentValues();
                    outletInsert.put(keyOutletId, outlet.getOutletId());
                    outletInsert.put(keyOutletTypeId, outlet.getOutletType());
                    outletInsert.put(keyOutletName, outlet.getOutletName());
                    outletInsert.put(keyOutletClassId, outlet.getOutletClass());
                    outletInsert.put(keyOutletCode, outlet.getOutletCode());
                    outletInsert.put(keyRouteId, route.getRouteId());
                    outletInsert.put(keyOutletAddress, outlet.getAddress());
                    outletInsert.put(keyOutletOwnerName, outlet.getOwnerName());
//                    outletInsert.put(keyOutletOwnerDOB, outlet.getOwnerDOB());
                    outletInsert.put(keyOutletContactLand, outlet.getContactLand());
//                    outletInsert.put(keyOutletContactMobile, outlet.getContactMobile());
//                    outletInsert.put(keyOutletAssistantName, outlet.getAssistantName());
//                    outletInsert.put(keyOutletAssistantDOB, outlet.getAssistantDOB());
                    db.insert(tableOutlet, null, outletInsert);

                    if (outlet.getOutletHistory() != null) {
                        for (HistoryDetail history : outlet.getOutletHistory()) {
                            ContentValues historyInsert = new ContentValues();
                            historyInsert.put(keyOutletId, outlet.getOutletId());
                            if (history.getHistoryType() == HistoryDetail.TYPE_OTHER) {
                                // No Invoice
                                historyInsert.put(keyInvoiceDate, history.getDate());
                                historyInsert.put(keyInvoiceRemark, history.getRemark());
                                historyInsert.put(keyOutletHistoryType, 0);
                                db.insert(tableOutletHistory, null, historyInsert);
                            } else {
                                // Invoice
                                Invoice invoice = history.getInvoice();

                                Log.wtf("Storing Invoice", invoice.toString());

                                historyInsert.put(keyInvoiceDate, invoice.getInvoiceTime());
                                historyInsert.put(keyInvoiceNo, invoice.getInvoiceId());
                                historyInsert.put(keyInvoiceAmount, invoice.getTotalAmount());
                                historyInsert.put(keyOrderDiscount, invoice.getTotalDiscount());
                                historyInsert.put(keyReturnAmount, invoice.getReturnAmount());
                                historyInsert.put(keyOutletHistoryType, 1);
                                db.insert(tableOutletHistory, null, historyInsert);

                                if (invoice.getCashPayments() != null) {
                                    ContentValues cashInsert = new ContentValues();
                                    for (CashPayment cashPayment : invoice.getCashPayments()) {
                                        cashInsert.put(keyCashDate, cashPayment.getPaymentTime());
                                        cashInsert.put(keyCashAmount, cashPayment.getPaymentAmount());
                                        cashInsert.put(keyInvoiceNo, invoice.getInvoiceId());
                                        db.insert(tableCashPayment, null, cashInsert);
                                    }
                                }

                                if (invoice.getChequePayments() != null) {
                                    ContentValues cheqInsert = new ContentValues();
                                    for (Cheque cheque : invoice.getChequePayments()) {
                                        cheqInsert.put(keyChequeNo, cheque.getChequeNo());
                                        cheqInsert.put(keyChequeDate, cheque.getChequeDate());
                                        cheqInsert.put(keyChequeAmount, cheque.getAmount());
                                        cheqInsert.put(keyBankId, cheque.getBankId());
                                        cheqInsert.put(keyBankBranchId, cheque.getBranchid());
                                        cheqInsert.put(keyInvoiceNo, invoice.getInvoiceId());
                                        db.insert(tableChequePayment, null, cheqInsert);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void storeRoutes(List<Route> routeList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Route route : routeList) {
            ContentValues routeInsert = new ContentValues();
            routeInsert.put(keyRouteId, route.getRouteId());
            routeInsert.put(keyRouteName, route.getRouteName());
            routeInsert.put(keyRouteFixedTarget, route.getFixedTarget());
            routeInsert.put(keyRouteSelectedTarget, route.getSelectedTarget());
            db.insert(tableRoute, null, routeInsert);
        }
    }

    public void updateRoute(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(keyRouteName, route.getRouteName());
        contentValues.put(keyRouteFixedTarget, route.getFixedTarget());
        contentValues.put(keyRouteSelectedTarget, route.getSelectedTarget());
        db.update(tableRoute, contentValues, keyRouteId + "=?", new String[]{String.valueOf(route.getRouteId())});
    }

    public void updateOutlet(Outlet outlet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(keyOutletId, outlet.getOutletId());
        contentValues.put(keyOutletTypeId, outlet.getOutletType());
        contentValues.put(keyOutletName, outlet.getOutletName());
        contentValues.put(keyOutletClassId, outlet.getOutletClass());
        contentValues.put(keyOutletCode, outlet.getOutletCode());
        contentValues.put(keyOutletAddress, outlet.getAddress());
        contentValues.put(keyOutletOwnerName, outlet.getOwnerName());
        contentValues.put(keyOutletContactLand, outlet.getContactLand());
        contentValues.put(keyOutletFrontImageUri, outlet.getFrontImageURI());
        contentValues.put(keyOutletShowcaseImageUri, outlet.getShowcaseImageUri());
        contentValues.put(keyOutletPromotion1ImageUri, outlet.getPromotion1ImageUri());
        contentValues.put(keyOutletPromotion2ImageUri, outlet.getPromotion2ImageUri());

        db.update(tableOutlet, contentValues, keyOutletId + "=?", new String[]{String.valueOf(outlet.getOutletId())});
    }

    public void storeOutlet(Outlet outlet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(keyOutletId, outlet.getOutletId());
        contentValues.put(keyRouteId, outlet.getRouteId());
        contentValues.put(keyOutletTypeId, outlet.getOutletType());
        contentValues.put(keyOutletName, outlet.getOutletName());
        contentValues.put(keyOutletClassId, outlet.getOutletClass());
        contentValues.put(keyOutletCode, outlet.getOutletCode());
        contentValues.put(keyOutletAddress, outlet.getAddress());
        contentValues.put(keyOutletOwnerName, outlet.getOwnerName());
        contentValues.put(keyOutletContactLand, outlet.getContactLand());
        contentValues.put(keyOutletFrontImageUri, outlet.getFrontImageURI());
        contentValues.put(keyOutletShowcaseImageUri, outlet.getShowcaseImageUri());
        contentValues.put(keyOutletPromotion1ImageUri, outlet.getPromotion1ImageUri());
        contentValues.put(keyOutletPromotion2ImageUri, outlet.getPromotion2ImageUri());
        db.insert(tableOutlet, null, contentValues);
    }

    public void storeOutlets(List<Outlet> outletList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Outlet outlet : outletList) {
            ContentValues outletInsert = new ContentValues();
            outletInsert.put(keyOutletId, outlet.getOutletId());
            outletInsert.put(keyOutletName, outlet.getOutletName());
            outletInsert.put(keyRouteId, outlet.getRouteId());
            outletInsert.put(keyOutletAddress, outlet.getAddress());
            outletInsert.put(keyOutletOwnerName, outlet.getOwnerName());
//            outletInsert.put(keyOutletOwnerDOB, outlet.getOwnerDOB());
            outletInsert.put(keyOutletContactLand, outlet.getContactLand());
//            outletInsert.put(keyOutletContactMobile, outlet.getContactMobile());
//            outletInsert.put(keyOutletAssistantName, outlet.getAssistantName());
//            outletInsert.put(keyOutletAssistantDOB, outlet.getAssistantDOB());
            db.insert(tableOutlet, null, outletInsert);
        }
    }

    /**
     * Get all routes and corresponding outlets
     *
     * @return list of all routes
     */
    public List<Route> getRoutes() {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRoute = "select "
                + keyRouteId + ", "
                + keyRouteName + ", "
                + keyRouteFixedTarget + ", "
                + keyRouteSelectedTarget + ", "
                + keyRouteCode
                + " from " + tableRoute;
        String selectOutlet = "select "
                + keyOutletId + ", " //0
                + keyOutletName + ", " //1
                + keyOutletAddress + ", " //2
                + keyOutletOwnerName + ", " //3
                + keyOutletTypeId + ", " //4
                + keyOutletContactLand + ", " //5
                + keyOutletClassId + ", " //6
                + keyOutletCode + ", " //7
                + keyOutletFrontImageUri + ", " //8
                + keyOutletShowcaseImageUri + ", " //9
                + keyOutletTarget //10
                + " from " + tableOutlet + " where " + keyRouteId + "=?";

        List<Route> routeList = new ArrayList<Route>();

        Cursor routeCursor = db.rawQuery(selectRoute, null);
        String dump = DatabaseUtils.dumpCursorToString(routeCursor);

        if (routeCursor.moveToFirst()) {
            do {
                Route route = new Route();
                route.setRouteId(routeCursor.getInt(0));
                route.setRouteName(routeCursor.getString(1));
                route.setFixedTarget(routeCursor.getDouble(2));
                route.setSelectedTarget(routeCursor.getDouble(3));
                route.setRouteCode(routeCursor.getString(4));

                List<Outlet> outletList = new ArrayList<Outlet>();
                Cursor outletCursor = db.rawQuery(selectOutlet, new String[]{String.valueOf(route.getRouteId())});
                if (outletCursor.moveToFirst()) {
                    do {
//                        Outlet outlet = new Outlet(outletCursor.getInt(0), outletCursor.getString(1));
//                        outletList.add(outlet);
                        Outlet outlet = new Outlet(
                                outletCursor.getInt(0),
                                route.getRouteId(),
                                outletCursor.getInt(4), // outletType
                                outletCursor.getInt(6), // outletClass
                                outletCursor.getString(1), // outletName
                                outletCursor.getString(7), // outletCode
                                outletCursor.getString(2), // address
                                outletCursor.getString(3), // ownerName
                                outletCursor.getString(5), // contact
                                outletCursor.getString(10), // frontImage
                                outletCursor.getString(9), // showcaseImage
                                new ArrayList<HistoryDetail>());  // outletHistory

//                        outlet.setOutletHistory(getOutletHistory(db, outlet.getOutletId()));
                        outlet.setOutletHistory(getOutstandingPayments(outlet.getOutletId()));

                        outletList.add(outlet);
                    } while (outletCursor.moveToNext());
                }

                route.setOutlets(outletList);
                routeList.add(route);

            } while (routeCursor.moveToNext());
        }

        db.close();

        return routeList;
    }

    /**
     * Returns a list of outstanding details of the outlet.
     *
     * @param outletId
     * @return
     */
    public List<HistoryDetail> getOutstandingPayments(int outletId) {
        SQLiteDatabase db = getReadableDatabase();
        String historyQuery = "select "
                + keyInvoiceNo + ", "
                + keyInvoiceDate + ", "
                + keyInvoiceAmount + ", "
                + keyInvoiceRemark + ", "
                + keyOutletHistoryType + ", "
                + keyOrderDiscount + ", "
                + keyReturnAmount
                + " from " + tableOutletHistory
                + " where " + keyOutletId + "=?" + " and " + keyOutletHistoryType + "=?";

        String cashQuery = "select "
                + keyCashDate + ", "
                + keyCashAmount + ", "
                + keyInvoiceNo
                + " from " + tableCashPayment + " where " + keyInvoiceNo + "=?";

        String chequeQuery = "select "
                + keyChequeNo + ", "
                + keyChequeDate + ", "
                + keyChequeAmount + ", "
                + keyBankId + ", "
                + keyBankBranchId + ", "
                + keyInvoiceNo
                + " from " + tableChequePayment + " where " + keyInvoiceNo + "=?";

        Cursor historySelect = db.rawQuery(historyQuery, new String[]{String.valueOf(outletId), String.valueOf(HistoryDetail.TYPE_INVOICE)});
        List<HistoryDetail> historyDetails = new ArrayList<HistoryDetail>();

        Log.d(LOG_TAG, "History Select dump\n" + DatabaseUtils.dumpCursorToString(historySelect));

        if (historySelect.moveToFirst()) {
            do {
                if (historySelect.getInt(4) == 0) {
                    // No invoice
                    historyDetails.add(new HistoryDetail(0, historySelect.getString(3), historySelect.getLong(1)));
                } else {
                    // Invoice
                    long invoiceNo = historySelect.getLong(0);
                    Invoice invoice = new Invoice(invoiceNo, historySelect.getLong(1), historySelect.getDouble(2), historySelect.getDouble(5), historySelect.getDouble(6));

                    List<CashPayment> cashPayments = new ArrayList<CashPayment>();
                    Cursor cashSelect = db.rawQuery(cashQuery, new String[]{String.valueOf(invoiceNo)});

                    String dump1 = DatabaseUtils.dumpCursorToString(cashSelect);
                    String dump2 = DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableCashPayment, null));

                    Log.d(LOG_TAG, "Cash Select Cursor Dump\n" + dump1);
//                    Log.d(LOG_TAG, "All Cash selecl dump\n" + dump2);

                    if (cashSelect.moveToFirst()) {
                        do {
                            cashPayments.add(new CashPayment(cashSelect.getLong(0), cashSelect.getDouble(1)));
                        } while (cashSelect.moveToNext());
                    }

                    invoice.setCashPayments(cashPayments);

                    List<Cheque> chequePayments = new ArrayList<Cheque>();
                    Cursor chequeSelect = db.rawQuery(chequeQuery, new String[]{String.valueOf(invoiceNo)});
                    if (chequeSelect.moveToFirst()) {
                        do {
                            chequePayments.add(new Cheque(chequeSelect.getString(0), chequeSelect.getLong(1), chequeSelect.getDouble(2), chequeSelect.getInt(3), chequeSelect.getInt(4)));
                        } while (chequeSelect.moveToNext());
                    }

                    invoice.setChequePayments(chequePayments);

                    historyDetails.add(new HistoryDetail(0, invoice, historySelect.getString(3), historySelect.getLong(1)));
                }
            } while (historySelect.moveToNext());
        }

        return historyDetails;
    }

    private List<HistoryDetail> getOutletHistory(SQLiteDatabase db, int outletId) {

        String historyQuery = "select "
                + keyInvoiceNo + ", "
                + keyInvoiceDate + ", "
                + keyInvoiceAmount + ", "
                + keyInvoiceRemark + ", "
                + keyOutletHistoryType + ", "
                + keyOrderDiscount + ", "
                + keyReturnAmount + ", "
                + keyOutletId
                + " from " + tableOutletHistory + " where " + keyOutletId + "=?";

        String cashQuery = "select "
                + keyCashDate + ", "
                + keyCashAmount
                + " from " + tableCashPayment + " where " + keyInvoiceNo + "=?";

        String chequeQuery = "select "
                + keyChequeNo + ", "
                + keyChequeDate + ", "
                + keyChequeAmount + ", "
                + keyBankId + ", "
                + keyBankBranchId
                + " from " + tableChequePayment + " where " + keyInvoiceNo + "=?";

        Cursor historySelect = db.rawQuery(historyQuery, new String[]{String.valueOf(outletId)});
        List<HistoryDetail> historyDetails = new ArrayList<HistoryDetail>();
        if (historySelect.moveToFirst()) {
            do {
                if (historySelect.getInt(4) == 0) {
                    // No invoice
                    historyDetails.add(new HistoryDetail(0, historySelect.getString(3), historySelect.getLong(1)));
                } else {
                    // Invoice
                    int invoiceNo = historySelect.getInt(0);
                    Invoice invoice = new Invoice(invoiceNo, historySelect.getLong(1), historySelect.getDouble(2), historySelect.getDouble(5), historySelect.getDouble(6));

                    List<CashPayment> cashPayments = new ArrayList<CashPayment>();
                    Cursor cashSelect = db.rawQuery(cashQuery, new String[]{String.valueOf(invoiceNo)});
                    if (cashSelect.moveToFirst()) {
                        do {
                            cashPayments.add(new CashPayment(cashSelect.getLong(0), cashSelect.getDouble(1)));
                        } while (cashSelect.moveToNext());
                    }

                    invoice.setCashPayments(cashPayments);

                    List<Cheque> chequePayments = new ArrayList<Cheque>();
                    Cursor chequeSelect = db.rawQuery(chequeQuery, new String[]{String.valueOf(invoiceNo)});
                    if (chequeSelect.moveToFirst()) {
                        do {
                            chequePayments.add(new Cheque(chequeSelect.getString(0), chequeSelect.getLong(1), chequeSelect.getDouble(2), chequeSelect.getInt(3), chequeSelect.getInt(4)));
                        } while (chequeSelect.moveToNext());
                    }

                    invoice.setChequePayments(chequePayments);

                    historyDetails.add(new HistoryDetail(0, invoice, historySelect.getString(3), historySelect.getLong(1)));
                }
            } while (historySelect.moveToNext());
        }

        return historyDetails;
    }

    /**
     * Get outlets of a particular route
     *
     * @param routeId the relevant route id
     * @return list of outlets of the particular route
     */
    public List<Outlet> getOutletsOfRoute(int routeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectOutlet = "select "
                + keyOutletId + ", " //0
                + keyOutletName + ", " //1
                + keyOutletAddress + ", " //2
                + keyOutletOwnerName + ", " //3
                + keyOutletTypeId + ", " //4
                + keyOutletContactLand + ", " //5
                + keyOutletClassId + ", " //6
                + keyOutletCode + ", " //7
                + keyOutletFrontImageUri + ", " //8
                + keyOutletShowcaseImageUri + ", " //9
                + keyOutletTarget //10
                + " from " + tableOutlet + " where " + keyRouteId + "=?";

        List<Outlet> outletList = new ArrayList<Outlet>();
        Cursor outletCursor = db.rawQuery(selectOutlet, new String[]{String.valueOf(routeId)});
        if (outletCursor.moveToFirst()) {
            do {
//                Outlet outlet = new Outlet(
//                        outletCursor.getInt(0),
//                        routeId,
//                        outletCursor.getString(1),
//                        outletCursor.getString(2),
//                        outletCursor.getString(3),
//                        outletCursor.getString(5),
//                        outletCursor.getString(6),
//                        outletCursor.getString(7),
//                        outletCursor.getString(9),
//                        outletCursor.getString(10),
//                        outletCursor.getLong(4),
//                        outletCursor.getLong(8),
//                        new ArrayList<HistoryDetail>());
                Outlet outlet = new Outlet(
                        outletCursor.getInt(0),
                        routeId,
                        outletCursor.getInt(4), // outletType
                        outletCursor.getInt(6), // outletClass
                        outletCursor.getString(1), // outletName
                        outletCursor.getString(7), // outletCode
                        outletCursor.getString(2), // address
                        outletCursor.getString(3), // ownerName
                        outletCursor.getString(5), // contact
                        outletCursor.getString(10), // frontImage
                        outletCursor.getString(9), // showcaseImage
                        new ArrayList<HistoryDetail>());  // outletHistory
                outlet.setOutletHistory(getOutletHistory(db, outlet.getOutletId()));
                outletList.add(outlet);
            } while (outletCursor.moveToNext());
        }

//        Cursor outletCursor = db.rawQuery(selectOutlet, new String[]{String.valueOf(routeId)});
//        if(outletCursor.moveToFirst()) {
//            do {
//                Outlet outlet = new Outlet(outletCursor.getInt(0), outletCursor.getString(1));
//                outletList.add(outlet);
//            } while(outletCursor.moveToNext());
//        }
        db.close();

        return outletList;
    }

    /**
     * Get all outlets from Database
     *
     * @return list of all outlets
     */
    public List<Outlet> getAllOutlets() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectOutlet = "select "
                + keyOutletId + ", " //0
                + keyOutletName + ", " //1
                + keyOutletAddress + ", " //2
                + keyOutletOwnerName + ", " //3
                + keyOutletTypeId + ", " //4
                + keyOutletContactLand + ", " //5
                + keyOutletClassId + ", " //6
                + keyOutletCode + ", " //7
                + keyOutletFrontImageUri + ", " //8
                + keyOutletShowcaseImageUri + ", " //9
                + keyOutletTarget + ", " //10
                + keyRouteId
                + " from " + tableOutlet;

        List<Outlet> outletList = new ArrayList<Outlet>();
        Cursor outletCursor = db.rawQuery(selectOutlet, null);
        if (outletCursor.moveToFirst()) {
            do {
//                Outlet outlet = new Outlet(
//                        outletCursor.getInt(0),
//                        outletCursor.getInt(12),
//                        outletCursor.getString(1),
//                        outletCursor.getString(2),
//                        outletCursor.getString(3),
//                        outletCursor.getString(5),
//                        outletCursor.getString(6),
//                        outletCursor.getString(7),
//                        outletCursor.getString(9),
//                        outletCursor.getString(10),
//                        outletCursor.getLong(4),
//                        outletCursor.getLong(8),
//                        new ArrayList<HistoryDetail>());
                Outlet outlet = new Outlet(
                        outletCursor.getInt(0),
                        outletCursor.getInt(11), // routeId
                        outletCursor.getInt(4), // outletType
                        outletCursor.getInt(6), // outletClass
                        outletCursor.getString(1), // outletName
                        outletCursor.getString(7), // outletCode
                        outletCursor.getString(2), // address
                        outletCursor.getString(3), // ownerName
                        outletCursor.getString(5), // contact
                        outletCursor.getString(10), // frontImage
                        outletCursor.getString(9), // showcaseImage
                        new ArrayList<HistoryDetail>());  // outletHistory
                outlet.setOutletHistory(getOutletHistory(db, outlet.getOutletId()));
                outletList.add(outlet);
            } while (outletCursor.moveToNext());
        }

        db.close();

        return outletList;
    }

    public boolean isOutletIdAvailable(int outletId){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + keyOutletId + " FROM " + tableOutlet + "WHERE " +keyOutletId + "=?";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(outletId)});
        if(cursor.getCount()>0){
            //Not Available to use
            return false;
        }else{
            //available to use
            return true;
        }
    }

    public boolean isOutletTypesAvailable(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + keyOutletTypeId + " FROM " + tableOutletType;
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.getCount()>0){
            //Not Available to use
            return true;
        }else{
            //available to use
            return false;
        }
    }

    public Outlet getOutletOfId(int outId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectOutlet = "select "
                + keyOutletId + ", " //0
                + keyOutletName + ", " //1
                + keyOutletAddress + ", " //2
                + keyOutletOwnerName + ", " //3
                + keyOutletTypeId + ", " //4
                + keyOutletContactLand + ", " //5
                + keyOutletClassId + ", " //6
                + keyOutletCode + ", " //7
                + keyOutletFrontImageUri + ", " //8
                + keyOutletShowcaseImageUri + ", " //9
                + keyOutletTarget + ", " //10
                + keyRouteId + ", " //11
                + keyOutletPromotion1ImageUri + ", " //12
                + keyOutletPromotion2ImageUri  //13
                + " from " + tableOutlet + " where " + keyOutletId + "=?";
        Cursor outletCursor = db.rawQuery(selectOutlet, new String[]{String.valueOf(outId)});
        if (outletCursor.moveToFirst()) {
//            Outlet outlet = new Outlet(
//                    outId,
//                    cursor.getInt(11),
//                    cursor.getString(0),
//                    cursor.getString(1),
//                    cursor.getString(2),
//                    cursor.getString(4),
//                    cursor.getString(5),
//                    cursor.getString(6),
//                    cursor.getString(8),
//                    cursor.getString(9),
//                    cursor.getLong(3),
//                    cursor.getLong(7),
//                    new ArrayList<HistoryDetail>());
            Outlet outlet = new Outlet(
                    outletCursor.getInt(0),
                    outletCursor.getInt(11), // routeId
                    outletCursor.getInt(4), // outletType
                    outletCursor.getInt(6), // outletClass
                    outletCursor.getString(1), // outletName
                    outletCursor.getString(7), // outletCode
                    outletCursor.getString(2), // address
                    outletCursor.getString(3), // ownerName
                    outletCursor.getString(5), // contact
                    outletCursor.getString(8), // frontImage
                    outletCursor.getString(9), // showcaseImage
                    outletCursor.getString(12), // Promo1Image
                    outletCursor.getString(13), // Promo2Image

                    new ArrayList<HistoryDetail>());  // outletHistory
            outlet.setOutletHistory(getOutletHistory(db, outlet.getOutletId()));
            return outlet;
        }

        return null;
    }

    private int getRouteOfOutlet(SQLiteDatabase db, int outletId) {
//        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + keyRouteId + " from " + tableOutlet + " where " + keyOutletId + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(outletId)});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);

            return id;
        }
        return 0;
    }

    public int getRouteOfOutlet(int outletId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + keyRouteId + " from " + tableOutlet + " where " + keyOutletId + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(outletId)});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            db.close();
            return id;
        }
        db.close();
        return 0;
    }

    public Route getRouteOfId(int routeId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRoute = "select "
                + keyRouteId + ", "
                + keyRouteName + ", "
                + keyRouteFixedTarget + ", "
                + keyRouteSelectedTarget + ", "
                + keyRouteCode
                + " from " + tableRoute
                + " where " + keyRouteId + " =?";
        String selectOutlet = "select "
                + keyOutletId + ", " //0
                + keyOutletName + ", " //1
                + keyOutletAddress + ", " //2
                + keyOutletOwnerName + ", " //3
                + keyOutletTypeId + ", " //4
                + keyOutletContactLand + ", " //5
                + keyOutletClassId + ", " //6
                + keyOutletCode + ", " //7
                + keyOutletFrontImageUri + ", " //8
                + keyOutletShowcaseImageUri + ", " //9
                + keyOutletTarget + ", " //10
                + keyRouteId
                + " from " + tableOutlet + " where " + keyRouteId + "=?";

        Cursor routeCursor = db.rawQuery(selectRoute, new String[]{String.valueOf(routeId)});

        if (routeCursor.moveToFirst()) {
            Route route = new Route();
            route.setRouteId(routeCursor.getInt(0));
            route.setRouteName(routeCursor.getString(1));
            route.setFixedTarget(routeCursor.getDouble(2));
            route.setSelectedTarget(routeCursor.getDouble(3));
            route.setRouteCode(routeCursor.getString(4));

            List<Outlet> outletList = new ArrayList<Outlet>();
            Cursor outletCursor = db.rawQuery(selectOutlet, new String[]{String.valueOf(route.getRouteId())});
            if (outletCursor.moveToFirst()) {
                do {
//                        Outlet outlet = new Outlet(outletCursor.getInt(0), outletCursor.getString(1));
//                        outletList.add(outlet);
//                    Outlet outlet = new Outlet(
//                            outletCursor.getInt(0),
//                            route.getRouteId(),
//                            outletCursor.getString(1),
//                            outletCursor.getString(2),
//                            outletCursor.getString(3),
//                            outletCursor.getString(5),
//                            outletCursor.getString(6),
//                            outletCursor.getString(7),
//                            outletCursor.getString(9),
//                            outletCursor.getString(10),
//                            outletCursor.getLong(4),
//                            outletCursor.getLong(8),
//                            new ArrayList<HistoryDetail>());

                    Outlet outlet = new Outlet(
                            outletCursor.getInt(0),
                            outletCursor.getInt(11), // routeId
                            outletCursor.getInt(4), // outletType
                            outletCursor.getInt(6), // outletClass
                            outletCursor.getString(1), // outletName
                            outletCursor.getString(7), // outletCode
                            outletCursor.getString(2), // address
                            outletCursor.getString(3), // ownerName
                            outletCursor.getString(5), // contact
                            outletCursor.getString(10), // frontImage
                            outletCursor.getString(9), // showcaseImage
                            new ArrayList<HistoryDetail>());  // outletHistory

//                        outlet.setOutletHistory(getOutletHistory(db, outlet.getOutletId()));
                    outlet.setOutletHistory(getOutstandingPayments(outlet.getOutletId()));

                    outletList.add(outlet);
                } while (outletCursor.moveToNext());
            }

            route.setOutlets(outletList);

            return route;

        }

        return null;
    }

    public List<HistoryDetail> getInvoicesOfTimeFrame(long timeBegin, long timeEnd) {

        Log.d(LOG_TAG, String.valueOf(timeBegin) + " - " + String.valueOf(timeEnd));

        SQLiteDatabase db = this.getReadableDatabase();

        String historyQuery = "select "
                + keyInvoiceNo + ", "
                + keyInvoiceDate + ", "
                + keyInvoiceAmount + ", "
                + keyInvoiceRemark + ", "
                + keyOutletHistoryType + ", "
                + keyOrderDiscount + ", "
                + keyReturnAmount + ", "
                + keyOutletId
                + " from " + tableOutletHistory
                + " where " + keyOutletHistoryType + "=?" + " and " + keyInvoiceDate + " >= ? and " + keyInvoiceDate + " < ?";

        String cashQuery = "select "
                + keyCashDate + ", "
                + keyCashAmount + ", "
                + keyInvoiceNo
                + " from " + tableCashPayment + " where " + keyInvoiceNo + "=?";

        String chequeQuery = "select "
                + keyChequeNo + ", "
                + keyChequeDate + ", "
                + keyChequeAmount + ", "
                + keyBankId + ", "
                + keyBankBranchId + ", "
                + keyInvoiceNo
                + " from " + tableChequePayment + " where " + keyInvoiceNo + "=?";

        Cursor historySelect = db.rawQuery(historyQuery, new String[]{String.valueOf(HistoryDetail.TYPE_INVOICE), String.valueOf(timeBegin), String.valueOf(timeEnd)});
        List<HistoryDetail> historyDetails = new ArrayList<HistoryDetail>();
//        List<Invoice> invoices = new ArrayList<>();

        if (historySelect.moveToFirst()) {
            do {
                if (historySelect.getInt(4) == 0) {
                    // No invoice
//                    historyDetails.add(new HistoryDetail(historySelect.getString(3), historySelect.getLong(1)));
                } else {
                    // Invoice
                    long invoiceNo = historySelect.getLong(0);
                    Invoice invoice = new Invoice(invoiceNo, historySelect.getLong(1), historySelect.getDouble(2), historySelect.getDouble(5), historySelect.getDouble(6));

                    List<CashPayment> cashPayments = new ArrayList<CashPayment>();
                    Cursor cashSelect = db.rawQuery(cashQuery, new String[]{String.valueOf(invoiceNo)});

//                    String dump1 = DatabaseUtils.dumpCursorToString(cashSelect);
//                    String dump2 = DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableCashPayment, null));

                    if (cashSelect.moveToFirst()) {
                        do {
                            cashPayments.add(new CashPayment(cashSelect.getLong(0), cashSelect.getDouble(1)));
                        } while (cashSelect.moveToNext());
                    }

                    invoice.setCashPayments(cashPayments);

                    List<Cheque> chequePayments = new ArrayList<Cheque>();
                    Cursor chequeSelect = db.rawQuery(chequeQuery, new String[]{String.valueOf(invoiceNo)});
                    if (chequeSelect.moveToFirst()) {
                        do {
                            chequePayments.add(new Cheque(chequeSelect.getString(0), chequeSelect.getLong(1), chequeSelect.getDouble(2), chequeSelect.getInt(3), chequeSelect.getInt(4)));
                        } while (chequeSelect.moveToNext());
                    }

                    invoice.setChequePayments(chequePayments);

                    historyDetails.add(new HistoryDetail(historySelect.getInt(7), invoice, historySelect.getString(3), historySelect.getLong(1)));
//                    invoices.add(invoice);
                }
            } while (historySelect.moveToNext());
        }

        return historyDetails;
    }

    public List<Object> getPaymentsOfTimeFrame(long timeBegin, long timeEnd) {

        SQLiteDatabase db = this.getReadableDatabase();

        String cashQuery = "select "
                + tableCashPayment + "." + keyCashDate + ", "
                + tableCashPayment + "." + keyCashAmount + ", "
                + tableCashPayment + "." + keyInvoiceNo + ", "
                + tableOutletHistory + "." + keyOutletId + ", "
                + tableCashPayment + "." + flagIsSynced
                + " from " + tableCashPayment + " left join " + tableOutletHistory
                + " on " + tableCashPayment + "." + keyInvoiceNo + " = " + tableOutletHistory + "." + keyInvoiceNo
                + " where " + keyCashDate + "> ? and " + keyCashDate + " < ?"
                + " order by " + tableCashPayment + "." + keyCashDate + " desc";

        String chequeQuery = "select "
                + tableChequePayment + "." + keyChequeNo + ", "
                + tableChequePayment + "." + keyChequeDate + ", "
                + tableChequePayment + "." + keyChequeAmount + ", "
                + tableChequePayment + "." + keyBankId + ", "
                + tableChequePayment + "." + keyBankBranchId + ", "
                + tableChequePayment + "." + keyInvoiceNo + ", "
                + tableOutletHistory + "." + keyOutletId + ", "
                + tableChequePayment + "." + flagIsSynced
                + " from " + tableChequePayment + " left join " + tableOutletHistory
                + " on " + tableChequePayment + "." + keyInvoiceNo + " = " + tableOutletHistory + "." + keyInvoiceNo
                + " where " + keyChequeDate + "> ? and " + keyChequeDate + " < ?"
                + " order by " + tableChequePayment + "." + keyChequeDate + " desc";

        List<CashPayment> cashPayments = new ArrayList<>();
        List<Cheque> cheques = new ArrayList<>();

        Cursor cashCursor = db.rawQuery(cashQuery, new String[]{String.valueOf(timeBegin), String.valueOf(timeEnd)});
        Cursor chequeCursor = db.rawQuery(chequeQuery, new String[]{String.valueOf(timeBegin), String.valueOf(timeEnd)});

        if (cashCursor.moveToFirst()) {
            do {
                CashPayment cashPayment = new CashPayment(cashCursor.getLong(0), cashCursor.getDouble(1));
                cashPayment.setInvoiceId(cashCursor.getLong(2));
                cashPayment.setOutletId(cashCursor.getInt(3));
                cashPayment.setSynced(cashCursor.getInt(4) == 1);
                cashPayments.add(cashPayment);
            } while (cashCursor.moveToNext());
        }

        if (chequeCursor.moveToFirst()) {
            do {
                Cheque cheque = new Cheque(chequeCursor.getString(0), chequeCursor.getLong(1), chequeCursor.getDouble(2), chequeCursor.getInt(3), chequeCursor.getInt(4));
                cheque.setInvoiceId(chequeCursor.getLong(5));
                cheque.setOutletId(chequeCursor.getInt(6));
                cheque.setSynced(chequeCursor.getInt(7) == 1);
                cheques.add(cheque);
            } while (chequeCursor.moveToNext());
        }

        int cashIndex = 0;
        int cheqIndex = 0;
        List<Object> finalList = new ArrayList<>();

        while (cashIndex < cashPayments.size() && cheqIndex < cheques.size()) {

            CashPayment cashPayment = cashPayments.get(cashIndex);
            Cheque cheque = cheques.get(cheqIndex);

            if (cashPayment.getPaymentAmount() < cheque.getChequeDate()) {
                finalList.add(cashPayment);
                cashIndex++;
            } else {
                finalList.add(cheque);
                cheqIndex++;
            }

        }


//        String chequeQuery = "select "
//                + keyChequeNo + ", "
//                + keyChequeDate + ", "
//                + keyChequeAmount + ", "
//                + keyBankId + ", "
//                + keyBankBranchId + ", "
//                + keyInvoiceNo
//                + " from " + tableChequePayment + " where " + keyInvoiceNo + "=?";

        return null;

//        Log.d(LOG_TAG, String.valueOf(timeBegin) + " - " + String.valueOf(timeEnd));
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String historyQuery = "select "
//                + keyInvoiceNo + ", "
//                + keyInvoiceDate + ", "
//                + keyInvoiceAmount + ", "
//                + keyInvoiceRemark + ", "
//                + keyOutletHistoryType + ", "
//                + keyOrderDiscount + ", "
//                + keyReturnAmount + ", "
//                + keyOutletId
//                + " from " + tableOutletHistory
//                + " where " + keyOutletHistoryType + "=?" + " and " + keyInvoiceDate + " >= ? and " + keyInvoiceDate + " < ?";
//

//
//        Cursor historySelect = db.rawQuery(historyQuery, new String[]{String.valueOf(HistoryDetail.TYPE_INVOICE), String.valueOf(timeBegin), String.valueOf(timeEnd)});
//        List<HistoryDetail> historyDetails = new ArrayList<HistoryDetail>();
////        List<Invoice> invoices = new ArrayList<>();
//
//        if (historySelect.moveToFirst()) {
//            do {
//                if (historySelect.getInt(4) == 0) {
//                    // No invoice
////                    historyDetails.add(new HistoryDetail(historySelect.getString(3), historySelect.getLong(1)));
//                } else {
//                    // Invoice
//                    long invoiceNo = historySelect.getLong(0);
//                    Invoice invoice = new Invoice(invoiceNo, historySelect.getLong(1), historySelect.getDouble(2), historySelect.getDouble(5), historySelect.getDouble(6));
//
//                    List<CashPayment> cashPayments = new ArrayList<CashPayment>();
//                    Cursor cashSelect = db.rawQuery(cashQuery, new String[]{String.valueOf(invoiceNo)});
//
////                    String dump1 = DatabaseUtils.dumpCursorToString(cashSelect);
////                    String dump2 = DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableCashPayment, null));
//
//                    if (cashSelect.moveToFirst()) {
//                        do {
//                            cashPayments.add(new CashPayment(cashSelect.getLong(0), cashSelect.getDouble(1)));
//                        } while (cashSelect.moveToNext());
//                    }
//
//                    invoice.setCashPayments(cashPayments);
//
//                    List<Cheque> chequePayments = new ArrayList<Cheque>();
//                    Cursor chequeSelect = db.rawQuery(chequeQuery, new String[]{String.valueOf(invoiceNo)});
//                    if (chequeSelect.moveToFirst()) {
//                        do {
//                            chequePayments.add(new Cheque(chequeSelect.getString(0), chequeSelect.getLong(1), chequeSelect.getDouble(2), chequeSelect.getInt(3), chequeSelect.getInt(4)));
//                        } while (chequeSelect.moveToNext());
//                    }
//
//                    invoice.setChequePayments(chequePayments);
//
//                    historyDetails.add(new HistoryDetail(historySelect.getInt(7), invoice, historySelect.getString(3), historySelect.getLong(1)));
////                    invoices.add(invoice);
//                }
//            } while (historySelect.moveToNext());
//        }
//
//        return historyDetails;
    }

    public List<PaymentPinHolder> getTimeFramedPayments(long timeBegin, long timeEnd) {

        SQLiteDatabase db = this.getReadableDatabase();

        String historyQuery = "select "
                + keyInvoiceNo + ", "
                + keyInvoiceDate + ", "
                + keyInvoiceAmount + ", "
                + keyInvoiceRemark + ", "
                + keyOutletHistoryType + ", "
                + keyOrderDiscount + ", "
                + keyReturnAmount + ", "
                + keyOutletId
                + " from " + tableOutletHistory
                + " where " + keyOutletHistoryType + "=?";

        String cashQuery = "select "
                + keyCashDate + ", "
                + keyCashAmount + ", "
                + keyInvoiceNo + ", "
                + flagIsSynced
                + " from " + tableCashPayment
                + " where " + keyCashDate + "> ? and " + keyCashDate + " < ?"
                + " order by " + keyCashDate + " desc";

        String chequeQuery = "select "
                + keyChequeNo + ", "
                + keyChequeDate + ", "
                + keyChequeAmount + ", "
                + keyBankId + ", "
                + keyBankBranchId + ", "
                + keyInvoiceNo + ", "
                + flagIsSynced
                + " from " + tableChequePayment
                + " where " + keyChequeDate + "> ? and " + keyChequeDate + " < ?"
                + " order by " + keyChequeDate + " desc";

        List<HistoryDetail> allHistoryDetails = new ArrayList<>();

        Cursor historyCursor = db.rawQuery(historyQuery, new String[]{String.valueOf(HistoryDetail.TYPE_INVOICE)});

        if (historyCursor.moveToFirst()) {
            do {
                Invoice invoice = new Invoice(historyCursor.getLong(0), historyCursor.getLong(1), historyCursor.getDouble(2), historyCursor.getDouble(5), historyCursor.getDouble(6));
                allHistoryDetails.add(new HistoryDetail(historyCursor.getInt(7), invoice, historyCursor.getString(3), historyCursor.getLong(1)));
            } while (historyCursor.moveToNext());
        }

        Cursor cashCursor = db.rawQuery(cashQuery, new String[]{String.valueOf(timeBegin), String.valueOf(timeEnd)});
        List<CashPayment> cashPayments = new ArrayList<>();

        if (cashCursor.moveToFirst()) {
            do {
                CashPayment cashPayment = new CashPayment(cashCursor.getLong(0), cashCursor.getDouble(1));
                cashPayment.setInvoiceId(cashCursor.getLong(2));
                cashPayment.setSynced(cashCursor.getInt(3) == 1);
                cashPayments.add(cashPayment);
            } while (cashCursor.moveToNext());
        }

        Cursor chequeCursor = db.rawQuery(chequeQuery, new String[]{String.valueOf(timeBegin), String.valueOf(timeEnd)});
        List<Cheque> cheques = new ArrayList<>();

        Log.d(LOG_TAG, "Time frame\n" + String.valueOf(timeBegin) + " - " + String.valueOf(timeEnd));
        Log.d(LOG_TAG, "All Cheque dates dump\n" + DatabaseUtils.dumpCursorToString(db.rawQuery("select " + keyChequeDate + " from " + tableChequePayment, null)));
        Log.d(LOG_TAG, "Cheque between time dump\n" + DatabaseUtils.dumpCursorToString(chequeCursor));

        if (chequeCursor.moveToFirst()) {
            do {
                Cheque cheque = new Cheque(chequeCursor.getString(0), chequeCursor.getLong(1), chequeCursor.getDouble(2), chequeCursor.getInt(3), chequeCursor.getInt(4));
                cheque.setInvoiceId(chequeCursor.getLong(5));
                cheque.setSynced(chequeCursor.getInt(6) == 1);
                cheques.add(cheque);
            } while (chequeCursor.moveToNext());
        }

        for (CashPayment cashPayment : cashPayments) {
            for (HistoryDetail historyDetail : allHistoryDetails) {
                if (historyDetail.getInvoice().getInvoiceId() == cashPayment.getInvoiceId()) {
                    historyDetail.getInvoice().addCashPayment(cashPayment);
                    break;
                }
            }
        }

        for (Cheque cheque : cheques) {
            for (HistoryDetail historyDetail : allHistoryDetails) {
                if (historyDetail.getInvoice().getInvoiceId() == cheque.getInvoiceId()) {
                    historyDetail.getInvoice().addChequePayment(cheque);
                    break;
                }
            }
        }

        List<HistoryDetail> filteredHistoryDetails = new ArrayList<>();

        for (HistoryDetail historyDetail : allHistoryDetails) {
            boolean todayInvoice = historyDetail.getInvoice().getInvoiceTime() > timeBegin && historyDetail.getInvoice().getInvoiceTime() < timeEnd;
            boolean invoiceWithPayment = historyDetail.getInvoice().getCashPayments() != null || historyDetail.getInvoice().getChequePayments() != null;

            // Add the history detail tp the filtered list if it's either an invoice from today or has a payment made today
            if (todayInvoice || invoiceWithPayment) filteredHistoryDetails.add(historyDetail);
        }

        List<PaymentPinHolder> pinHolders = new ArrayList<>();

//        pinHolders.add(new PaymentPinHolder("Collections of the date " + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(timeBegin))));

        for (HistoryDetail historyDetail : filteredHistoryDetails) {
            boolean todayInvoice = historyDetail.getInvoice().getInvoiceTime() > timeBegin && historyDetail.getInvoice().getInvoiceTime() < timeEnd;
            if (todayInvoice) {
                pinHolders.add(new PaymentPinHolder(PaymentPinHolder.TYPE_DAY, historyDetail));
            }
        }

//        pinHolders.add(new PaymentPinHolder("Collections of other invoices"));

        for (HistoryDetail historyDetail : filteredHistoryDetails) {
            boolean todayInvoice = historyDetail.getInvoice().getInvoiceTime() > timeBegin && historyDetail.getInvoice().getInvoiceTime() < timeEnd;
//            boolean invoiceWithPayment = historyDetail.getHistoryDetail().getCashPayments() != null || historyDetail.getHistoryDetail().getChequePayments() != null;

            if (!todayInvoice) {
                pinHolders.add(new PaymentPinHolder(PaymentPinHolder.TYPE_OTHER, historyDetail));
            }
        }

        return pinHolders;
    }

    /* ********************************* ****************** ********************************* */

    /* *************************************** Banks **************************************** */

    public void storeBanks(List<Bank> bankList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues bankValues = new ContentValues();
        ContentValues branchValues = new ContentValues();
        for (Bank bank : bankList) {
            bankValues.put(keyBankId, bank.getBankId());
            bankValues.put(keyBankName, bank.getBankName());
            db.insert(tableBank, null, bankValues);

            for (BankBranch branch : bank.getBranches()) {
                branchValues.put(keyBankId, bank.getBankId());
                branchValues.put(keyBankBranchId, branch.getBranchId());
                branchValues.put(keyBankBranchName, branch.getBranchName());
                db.insert(tableBankBranch, null, branchValues);
            }
        }
        db.close();
    }

    public List<Bank> getBanks() {

        SQLiteDatabase db = this.getReadableDatabase();
        String bankQuery = "select "
                + keyBankId + ", "
                + keyBankName
                + " from " + tableBank;
        String branchQuery = "select "
                + keyBankBranchId + ", "
                + keyBankBranchName
                + " from " + tableBankBranch + " where " + keyBankId + "=?";

        List<Bank> banks = new ArrayList<Bank>();
        Cursor bankCursor = db.rawQuery(bankQuery, null);
        if (bankCursor.moveToFirst()) {
            do {
                Bank bank = new Bank(bankCursor.getInt(0), bankCursor.getString(1));
                List<BankBranch> branches = new ArrayList<BankBranch>();

                Cursor branchCursor = db.rawQuery(branchQuery, new String[]{String.valueOf(bank.getBankId())});
                if (branchCursor.moveToFirst()) {
                    do {
                        branches.add(new BankBranch(branchCursor.getInt(0), branchCursor.getString(1)));
                    } while (branchCursor.moveToNext());
                }

                bank.setBranches(branches);

                banks.add(bank);

            } while (bankCursor.moveToNext());
        }

        return banks;
    }

    public List<BankBranch> getBranchesOfBank(int bankId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String branchQuery = "select "
                + keyBankBranchId + ", "
                + keyBankBranchName
                + " from " + tableBankBranch + " where " + keyBankId + "=?";

        List<BankBranch> branches = new ArrayList<BankBranch>();

        Cursor branchCursor = db.rawQuery(branchQuery, new String[]{String.valueOf(bankId)});
        if (branchCursor.moveToFirst()) {
            do {
                branches.add(new BankBranch(branchCursor.getInt(0), branchCursor.getString(1)));
            } while (branchCursor.moveToNext());
        }

        return branches;
    }

    /* ********************************* ****************** ********************************* */

    /* *************************************** Types **************************************** */

    public void storeTypes(List<ProductType> productTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (ProductType productType : productTypes) {
            values.put(keyTypeId, productType.getTypeId());
            values.put(keyTypeName, productType.getTypeName());
            db.insert(tableItemType, null, values);
        }
        db.close();
    }

    /* ********************************* ****************** ********************************* */

    /* *************************************** Items **************************************** */

    /**
     * Store items in Database
     *
     * @param itemCategories list of item categories
     */
//    public void storeItems(List<ItemCategory> itemCategories){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        for(ItemCategory category : itemCategories) {
//            // Add to DB
//            ContentValues values = new ContentValues();
//            values.put(keyItemCategoryId, category.getCategoryId());
//            values.put(keyItemCategoryName, category.getCategoryName());
//            db.insert(tableItemCategory, null, values);
//
//            List<Item> itemList = category.getItemList();
//            if(itemList != null) {
//                for(Item item : itemList){
//                    ContentValues itemValues = new ContentValues();
//                    itemValues.put(keyItemId, item.getItemNo());
//                    itemValues.put(keyItemName, item.getItemName());
//                    itemValues.put(keyItemCategoryId, item.getCategoryId());
//                    itemValues.put(keyItemWeight, item.getItemWeight());
//                    itemValues.put(keyItemPackage, item.getPackaging());
//                    itemValues.put(keyItemStockQty, item.getStockQty());
//                    itemValues.put(keyItemWholeSalePrice, item.getWholesalePrice());
//                    itemValues.put(keyItemConsumerPrice, item.getConsumerPrice());
//                    itemValues.put(keyItemImageUri, item.getImageUri());
//                    itemValues.put(keyItemHasFlavours, item.hasFlavours() ? 1 : 0);
//                    db.insert(tableItem, null, itemValues);
//
//                    // Flavours are not added right now
////            if(item.getFlavours() != null){
////                for(Flavour flavour : item.getFlavours()){
////                    ContentValues values2 = new ContentValues();
////                    values2.put();
////                }
////            }
//
//                }
//            }
//
//        }
//    }
    public void storeItems(List<ItemCategory> itemCategories) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (ItemCategory category : itemCategories) {
            ContentValues values = new ContentValues();
            values.put(keyItemCategoryId, category.getCategoryId());
            values.put(keyItemCategoryName, category.getCategoryName());
            db.insert(tableItemCategory, null, values);

            List<Item> itemList = category.getItemList();
            if (itemList != null) {
                for (Item item : itemList) {
                    ContentValues itemValues = new ContentValues();
                    itemValues.put(keyItemId, item.getItemNo());
                    itemValues.put(keyItemSequenceNumber, item.getSequenceNo());
                    itemValues.put(keyItemName, item.getItemName());
                    itemValues.put(keyItemCategoryId, item.getCategoryId());
                    itemValues.put(keyItemWeight, item.getItemWeight());
                    itemValues.put(keyItemPackage, item.getPackaging());
                    itemValues.put(keyItemStockQty, item.getStockQty());
                    itemValues.put(keyItemWholeSalePrice, item.getWholesalePrice());
                    itemValues.put(keyItemConsumerPrice, item.getConsumerPrice());
                    itemValues.put(keyItemImageUri, item.getImageUri());
                    itemValues.put(keyTypeId, item.getTypeId());
                    itemValues.put(keyItemUnit, item.getUnit());
                    itemValues.put(keyItemUnitQty, item.getUnitQty());

                    if (item.hasFlavours()) {
                        for (Flavour flavour : item.getFlavours()) {
                            itemValues.put(keyFlavourId, flavour.getFlavourId());
                            itemValues.put(keyItemHasFlavours, 1);
                            db.insert(tableItem, null, itemValues);
                        }
                    } else {
                        itemValues.put(keyFlavourId, 0);
                        itemValues.put(keyItemHasFlavours, 0);
                        db.insert(tableItem, null, itemValues);
                    }

                    // Flavours are not added right now
//            if(item.getFlavours() != null){
//                for(Flavour flavour : item.getFlavours()){
//                    ContentValues values2 = new ContentValues();
//                    values2.put();
//                }
//            }

                }
            }

        }
    }

    //    public List<String> getProductImageURIs() {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "select distinct " + keyItemImageUri + " from " + tableItem;
//        Cursor cursor = db.rawQuery(query, null);
//
//        String dump = DatabaseUtils.dumpCursorToString(cursor);
//        String dump2 = DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableItem, null));
//
//        if(cursor.moveToFirst()){
//            List<String> uris = new ArrayList<String>();
//            do {
//                uris.add(cursor.getString(0));
//            } while (cursor.moveToNext());
//            return uris;
//        }
//
//        return null;
//    }

    /**
     * Get items under categories from DB
     *
     * @return list of categories in which the items lie
     */
    public List<ItemCategory> getCategorizedItems() {

        SQLiteDatabase db = this.getReadableDatabase();

        String categoryQuery = "select "
                + keyItemCategoryId + ", "
                + keyItemCategoryName
                + " from " + tableItemCategory;

//        String itemQuery = "select "
//                + tableItem + "." + keyItemId + ", " // 0
//                + tableItem + "." + keyItemName + ", " // 1
//                + tableItem + "." + keyItemWeight + ", " // 2
//                + tableItem + "." + keyItemPackage + ", " // 3
//                + tableItem + "." + keyItemStockQty + ", " // 4
//                + tableItem + "." + keyItemWholeSalePrice + ", " // 5
//                + tableItem + "." + keyItemConsumerPrice + ", " // 6
//                + tableItem + "." + keyItemImageUri + ", " // 7
//                + tableItem + "." + keyItemCategoryId + ", " // 8
//                + tableItem + "." + keyFlavourId + ", " // 9
//                + tableFlavour + "." + keyFlavourName + ", " // 10
//                + tableFlavour + "." + keyFlavourColour + ", " // 11
//                + tableItem + "." + keyTypeId + ", " // 12
//                + tableItem + "." + keyItemSequenceNumber // 13
//                + " from " + tableItem + " left join " + tableFlavour
//                + " on " + tableItem + "." + keyFlavourId + "=" + tableFlavour + "." + keyFlavourId
//                + " order by " + keyItemSequenceNumber + " asc";

        List<ItemCategory> categories = new ArrayList<ItemCategory>();

        Cursor categoryCursor = db.rawQuery(categoryQuery, null);

        if (categoryCursor.moveToFirst()) {
            do {
                categories.add(new ItemCategory(categoryCursor.getInt(0), categoryCursor.getString(1), new ArrayList<Item>()));
            } while (categoryCursor.moveToNext());
        }

        categoryCursor.close();
        db.close();

//        List<Item> allItems = new ArrayList<Item>();
//
//        Cursor cursor = db.rawQuery(itemQuery, null);
//
//        String dump = DatabaseUtils.dumpCursorToString(cursor);
//
//        if(cursor.moveToFirst()) {
//            do {
//                Item item = new Item();
//                item.setItemNo(cursor.getInt(0));
//                item.setItemName(cursor.getString(1));
//                item.setItemWeight(cursor.getString(2));
//                item.setPackaging(cursor.getString(3));
//                item.setStockQty(cursor.getInt(4));
//                item.setWholesalePrice(cursor.getDouble(5));
//                item.setConsumerPrice(cursor.getDouble(6));
//                item.setImageUri(cursor.getString(7));
//                item.setCategoryId(cursor.getInt(8));
//                item.setTypeId(cursor.getInt(12));
//
//                int flavourId = cursor.getInt(9);
//                if(flavourId != 0) {
//                    item.setHasFlavours(true);
//
//                    Flavour flavour = new Flavour(flavourId, cursor.getString(10), cursor.getString(11));
//                    flavour.setItemId(item.getItemNo());
//                    List<Flavour> flavours = new ArrayList<Flavour>();
//                    flavours.add(flavour);
//
//                    item.setFlavours(flavours);
//
//                }
//
//                allItems.add(item);
//            } while (cursor.moveToNext());
//        }
//
//        for(Item tmpItem : allItems) {
//            for(int i=0; i<categories.size(); i++) {
//                if(tmpItem.getCategoryId() == categories.get(i).getCategoryId()){
////                    List<Item> newItemsList = categories.get(i).getItemList();
////                    newItemsList.add(tmpItem);
////                    categories.get(i).setItemList(newItemsList);
//                    categories.get(i).getItemList().add(tmpItem);
//                    break;
//                }
//            }
//        }

        return categories;
    }

    public List<CustomItemGrouping> getGroupedItems() {

        SQLiteDatabase db = this.getReadableDatabase();

//        String itemQuery = "select "
//                + tableItem + "." + keyItemId + ", " // 0
//                + tableItem + "." + keyItemName + ", " // 1
//                + tableItem + "." + keyItemWeight + ", " // 2
//                + tableItem + "." + keyItemPackage + ", " // 3
//                + tableItem + "." + keyItemStockQty + ", " // 4
//                + tableItem + "." + keyItemWholeSalePrice + ", " // 5
//                + tableItem + "." + keyItemConsumerPrice + ", " // 6
//                + tableItem + "." + keyItemImageUri + ", " // 7
//                + tableItem + "." + keyItemCategoryId + ", " // 8
//                + tableItem + "." + keyFlavourId + ", " // 9
//                + tableFlavour + "." + keyFlavourName + ", " // 10
//                + tableFlavour + "." + keyFlavourColour + ", " // 11
//                + tableItem + "." + keyTypeId // 12
//                + " from " + tableItem + " left join " + tableFlavour
//                + " on " + tableItem + "." + keyFlavourId + "=" + tableFlavour + "." + keyFlavourId;

        String itemQuery = "select "
                + keyItemId + ", " // 0
                + keyItemName + ", " // 1
                + keyItemWeight + ", " // 2
                + keyItemPackage + ", " // 3
                + keyItemStockQty + ", " // 4
                + keyItemWholeSalePrice + ", " // 5
                + keyItemConsumerPrice + ", " // 6
                + keyItemImageUri + ", " // 7
                + keyItemCategoryId + ", " // 8
                + keyFlavourId + ", " // 9
                + keyTypeId + ", "// 10
                + keyItemUnit + ", " // 11
                + keyItemUnitQty + ", " // 12
                + keyItemSequenceNumber
                + " from " + tableItem + " order by " + keyItemSequenceNumber + " asc";

        List<Flavour> flavourList = getFlavours(db);

        Cursor cursor = db.rawQuery(itemQuery, null);

        List<Item> allItems = new ArrayList<Item>();

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemNo(cursor.getInt(0));
                item.setItemName(cursor.getString(1));
                item.setItemWeight(cursor.getString(2));
                item.setPackaging(cursor.getString(3));
                item.setStockQty(cursor.getInt(4));
                item.setWholesalePrice(cursor.getDouble(5));
                item.setConsumerPrice(cursor.getDouble(6));
                item.setImageUri(cursor.getString(7));
                item.setCategoryId(cursor.getInt(8));
                item.setTypeId(cursor.getInt(10));
                item.setUnit(cursor.getString(11));
                item.setUnitQty(cursor.getInt(12));
                item.setSequenceNo(cursor.getInt(13));

                int flavourId = cursor.getInt(9);
                if (flavourId != 0) {
                    item.setHasFlavours(true);

                    for (Flavour flavour : flavourList) {
                        if (flavourId == flavour.getFlavourId()) {
                            item.setFlavour(flavour);
                            break;
                        }
                    }

                }

                allItems.add(item);
            } while (cursor.moveToNext());
        }

        List<CustomItemGrouping> groupings = new ArrayList<CustomItemGrouping>();

        for (Item item : allItems) {

            boolean exists = false;
            for (int i = 0; i < groupings.size(); i++) {
                if (groupings.get(i).getCategoryId() == item.getCategoryId() && groupings.get(i).getTypeId() == item.getTypeId()) {
                    // Exists
                    groupings.get(i).getItems().add(item);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                groupings.add(new CustomItemGrouping(item));
            }

        }

        return groupings;
    }

    public List<CustomItemGrouping> getAllGroupedItems() {

        SQLiteDatabase db = this.getReadableDatabase();

        String itemQuery = "select "
                + keyItemId + ", " // 0
                + keyItemName + ", " // 1
                + keyItemWeight + ", " // 2
                + keyItemPackage + ", " // 3
                + keyItemStockQty + ", " // 4
                + keyItemWholeSalePrice + ", " // 5
                + keyItemConsumerPrice + ", " // 6
                + keyItemImageUri + ", " // 7
                + keyItemCategoryId + ", " // 8
                + keyFlavourId + ", " // 9
                + keyTypeId + ", "// 10
                + keyItemUnit + ", " // 11
                + keyItemUnitQty + ", " // 12
                + keyItemSequenceNumber
                + " from " + tableItem
                + " order by " + keyItemSequenceNumber + " asc";

        List<Flavour> flavourList = getFlavours(db);

        Cursor cursor = db.rawQuery(itemQuery, null);

        List<Item> allItems = new ArrayList<Item>();

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemNo(cursor.getInt(0));
                item.setItemName(cursor.getString(1));
                item.setItemWeight(cursor.getString(2));
                item.setPackaging(cursor.getString(3));
                item.setStockQty(cursor.getDouble(4));
                item.setWholesalePrice(cursor.getDouble(5));
                item.setConsumerPrice(cursor.getDouble(6));
                item.setImageUri(cursor.getString(7));
                item.setCategoryId(cursor.getInt(8));
                item.setTypeId(cursor.getInt(10));
                item.setUnit(cursor.getString(11));
                item.setUnitQty(cursor.getInt(12));
                item.setSequenceNo(cursor.getInt(13));

                int flavourId = cursor.getInt(9);
                if (flavourId != 0) {
                    item.setHasFlavours(true);

                    for (Flavour flavour : flavourList) {
                        if (flavourId == flavour.getFlavourId()) {
                            item.setFlavour(flavour);
                            break;
                        }
                    }

                }

                allItems.add(item);
            } while (cursor.moveToNext());
        }

        List<CustomItemGrouping> groupings = new ArrayList<CustomItemGrouping>();

        for (Item item : allItems) {

            boolean exists = false;
            for (int i = 0; i < groupings.size(); i++) {
                if (groupings.get(i).getCategoryId() == item.getCategoryId() && groupings.get(i).getTypeId() == item.getTypeId()) {
                    // Exists
                    groupings.get(i).getItems().add(item);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                groupings.add(new CustomItemGrouping(item));
            }

        }

        return groupings;
    }

    public List<CustomItemGrouping> getAvailableGroupedItems() {

        SQLiteDatabase db = this.getReadableDatabase();

        String itemQuery = "select "
                + keyItemId + ", " // 0
                + keyItemName + ", " // 1
                + keyItemWeight + ", " // 2
                + keyItemPackage + ", " // 3
                + keyItemStockQty + ", " // 4
                + keyItemWholeSalePrice + ", " // 5
                + keyItemConsumerPrice + ", " // 6
                + keyItemImageUri + ", " // 7
                + keyItemCategoryId + ", " // 8
                + keyFlavourId + ", " // 9
                + keyTypeId + ", "// 10
                + keyItemUnit + ", " // 11
                + keyItemUnitQty + ", " // 12
                + keyItemSequenceNumber
                + " from " + tableItem
                + " where " + keyItemStockQty + ">0"
                + " order by " + keyItemSequenceNumber + " asc";

        List<Flavour> flavourList = getFlavours(db);

        Cursor cursor = db.rawQuery(itemQuery, null);

        List<Item> allItems = new ArrayList<Item>();

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemNo(cursor.getInt(0));
                item.setItemName(cursor.getString(1));
                item.setItemWeight(cursor.getString(2));
                item.setPackaging(cursor.getString(3));
                item.setStockQty(cursor.getDouble(4));
                item.setWholesalePrice(cursor.getDouble(5));
                item.setConsumerPrice(cursor.getDouble(6));
                item.setImageUri(cursor.getString(7));
                item.setCategoryId(cursor.getInt(8));
                item.setTypeId(cursor.getInt(10));
                item.setUnit(cursor.getString(11));
                item.setUnitQty(cursor.getInt(12));
                item.setSequenceNo(cursor.getInt(13));

                int flavourId = cursor.getInt(9);
                if (flavourId != 0) {
                    item.setHasFlavours(true);

                    for (Flavour flavour : flavourList) {
                        if (flavourId == flavour.getFlavourId()) {
                            item.setFlavour(flavour);
                            break;
                        }
                    }

                }

                allItems.add(item);
            } while (cursor.moveToNext());
        }

        List<CustomItemGrouping> groupings = new ArrayList<CustomItemGrouping>();

        for (Item item : allItems) {

            boolean exists = false;
            for (int i = 0; i < groupings.size(); i++) {
                if (groupings.get(i).getCategoryId() == item.getCategoryId() && groupings.get(i).getTypeId() == item.getTypeId()) {
                    // Exists
                    groupings.get(i).getItems().add(item);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                groupings.add(new CustomItemGrouping(item));
            }

        }

        return groupings;
    }

    /**
     * Get all items from Database
     *
     * @return list of all items
     */

    public List<Item> getAllItems() {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select "
                + keyItemId + ", " //0
                + keyItemCategoryId + ", " //1
                + keyItemName + ", " //2
                + keyItemWeight + ", " //3
                + keyItemPackage + ", " //4
                + keyItemStockQty + ", " //5
                + keyItemWholeSalePrice + ", " //6
                + keyItemConsumerPrice + ", " //7
                + keyItemImageUri + ", " //8
                + keyFlavourId //9
                + " from " + tableItem;
        List<Item> itemList = new ArrayList<Item>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemNo(cursor.getInt(0));
                item.setCategoryId(cursor.getInt(1));
                item.setItemName(cursor.getString(2));
                item.setItemWeight(cursor.getString(3));
                item.setPackaging(cursor.getString(4));
                item.setStockQty(cursor.getInt(5));
                item.setWholesalePrice(cursor.getDouble(6));
                item.setConsumerPrice(cursor.getDouble(7));
                item.setImageUri(cursor.getString(8));
                item.setHasFlavours(cursor.getInt(9) != 0);

                // Set flavours

                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    private Item getItem(SQLiteDatabase db, int itemId) {

        String selectQuery = "select "
                + keyItemId + ", " //0
                + keyItemCategoryId + ", " //1
                + keyItemName + ", " //2
                + keyItemWeight + ", " //3
                + keyItemPackage + ", " //4
                + keyItemStockQty + ", " //5
                + keyItemWholeSalePrice + ", " //6
                + keyItemConsumerPrice + ", " //7
                + keyItemImageUri + ", " //8
                + keyItemHasFlavours + ", " //9
                + keyItemUnit + ", " //10
                + keyItemUnitQty //11
                + " from " + tableItem + " where " + keyItemId + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(itemId)});
        if (cursor.moveToFirst()) {
            Item item = new Item();
            item.setItemNo(cursor.getInt(0));
            item.setCategoryId(cursor.getInt(1));
            item.setItemName(cursor.getString(2));
            item.setItemWeight(cursor.getString(3));
            item.setPackaging(cursor.getString(4));
            item.setStockQty(cursor.getDouble(5));
            item.setWholesalePrice(cursor.getDouble(6));
            item.setConsumerPrice(cursor.getDouble(7));
            item.setImageUri(cursor.getString(8));
            item.setHasFlavours(cursor.getInt(9) == 1);
            item.setUnit(cursor.getString(10));
            item.setUnitQty(cursor.getInt(11));

            return item;
        }

        return null;
    }

    public void storeCategories(List<ItemCategory> categoryList) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (ItemCategory category : categoryList) {
            // Add to DB
            ContentValues values = new ContentValues();
            values.put(keyItemCategoryId, category.getCategoryId());
            values.put(keyItemCategoryName, category.getCategoryName());
            db.insert(tableItemCategory, null, values);
        }

        Log.d(LOG_TAG, DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableItemCategory, null)));

        db.close();

    }

    public void storeFlavours(List<Flavour> flavourList) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Flavour flavour : flavourList) {
            ContentValues values = new ContentValues();
            values.put(keyFlavourId, flavour.getFlavourId());
            values.put(keyFlavourName, flavour.getFlavourName());
            values.put(keyFlavourColour, flavour.getColorCode());
            db.insert(tableFlavour, null, values);
        }

        Log.d(LOG_TAG, DatabaseUtils.dumpCursorToString(db.rawQuery("select * from " + tableFlavour, null)));

        db.close();

    }

    private List<Flavour> getFlavours(SQLiteDatabase db) {

        String query = "select "
                + keyFlavourId + ", "
                + keyFlavourName + ", "
                + keyFlavourColour
                + " from " + tableFlavour;

        Cursor cursor = db.rawQuery(query, null);

        List<Flavour> flavourList = new ArrayList<Flavour>();

        if (cursor.moveToFirst()) {
            do {
                flavourList.add(new Flavour(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }

        return flavourList;
    }

    /* ********************************* ****************** ********************************* */

    /* ******************************** Outlet Visit Status ********************************* */

    public void storeVisitStatus(VisitDetail visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(keyOutletId, visit.getOutletId());
        values.put(keyVisitStatus, visit.getVisitStatus());
        values.put(keyLocalSession, visit.getSessionId());
        db.insert(tableVisitDetails, null, values);
        db.close();
    }

    public List<VisitDetail> getVisitDetails() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyOutletId + ", "
                + keyVisitStatus + ", "
                + keyLocalSession
                + " from " + tableVisitDetails;
        Cursor cursor = db.rawQuery(query, null);
        List<VisitDetail> visitDetails = new ArrayList<VisitDetail>();
        if (cursor.moveToFirst()) {
            do {
                visitDetails.add(new VisitDetail(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
        }

        return visitDetails;
    }

    public List<VisitDetail> getVisitsOfCurrentSession(int sessionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyOutletId + ", "
                + keyVisitStatus
                + " from " + tableVisitDetails + " where " + keyLocalSession + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sessionId)});
        List<VisitDetail> visitDetails = new ArrayList<VisitDetail>();
        if (cursor.moveToFirst()) {
            do {
                visitDetails.add(new VisitDetail(cursor.getInt(0), cursor.getInt(1), sessionId));
            } while (cursor.moveToNext());
        }

        return visitDetails;
    }

    public List<Integer> getVisitStatusOfOutlet(int sessionId, int outletId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyVisitStatus
                + " from " + tableVisitDetails + " where "
                + keyLocalSession + "=? and " + keyOutletId + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sessionId), String.valueOf(outletId)});
        if (cursor.moveToFirst()) {
            List<Integer> visitStatuses = new ArrayList<Integer>();
            do {
                visitStatuses.add(cursor.getInt(0));
            } while (cursor.moveToNext());

            return visitStatuses;
        }

        return null;
    }

//    public void clearVisitDetails() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from " + tableVisitDetails);
//        db.close();
//    }

    /* ********************************* ****************** ********************************* */

    /* ************************************* Attendance ************************************* */

    public void storeAttendance(Attendance attendance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(keyAttendanceTime, attendance.getLogTime());
        values.put(keyAttendanceStatus, attendance.getLogStatus());
        values.put(keyLatitude, attendance.getLatitude());
        values.put(keyLongitude, attendance.getLongitude());
        values.put(keyLocalSession, attendance.getLocalSession());
        values.put(flagIsSynced, attendance.isSynced() ? 1 : 0);
        values.put(keyAttendanceLocation, attendance.getLoc());
        db.insert(tableAttendance, null, values);
        db.close();
    }

    public List<Attendance> getAllAttendances() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyAttendanceTime + ", "
                + keyAttendanceStatus + ", "
                + keyLatitude + ", "
                + keyLongitude + ", "
                + flagIsSynced + ", "
                + keyLocalSession + ", "
                + keyAttendanceLocation
                + " from " + tableAttendance;
        Cursor cursor = db.rawQuery(query, null);
        List<Attendance> attendances = new ArrayList<Attendance>();
        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance(cursor.getLong(0), cursor.getInt(1), cursor.getInt(4) == 1, cursor.getInt(5), cursor.getString(6));
                attendance.setLatitude(cursor.getDouble(2));
                attendance.setLongitude(cursor.getDouble(3));
                attendances.add(attendance);
            } while (cursor.moveToNext());
        }

        return attendances;
    }

    public List<Attendance> getAttendancesToSync() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyAttendanceTime + ", "
                + keyAttendanceStatus + ", "
                + keyLatitude + ", "
                + keyLongitude + ", "
                + flagIsSynced + ", "
                + keyLocalSession + ", "
                + keyAttendanceLocation
                + " from " + tableAttendance + " where not " + flagIsSynced;
        Cursor cursor = db.rawQuery(query, null);
        List<Attendance> attendances = new ArrayList<Attendance>();
        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance(cursor.getLong(0), cursor.getInt(1), cursor.getInt(4) == 1, cursor.getInt(5), cursor.getString(6));
                attendance.setLatitude(cursor.getDouble(2));
                attendance.setLongitude(cursor.getDouble(3));
                attendances.add(attendance);
            } while (cursor.moveToNext());
        }

        return attendances;
    }

    public Attendance getAttendanceOfCurrentSession(int sessionId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String aQuery = "select "
                + keyAttendanceTime + ", "
                + keyAttendanceStatus + ", "
                + keyLatitude + ", "
                + keyLongitude + ", "
                + flagIsSynced + ", "
                + keyLocalSession + ", "
                + keyAttendanceLocation
                + " from " + tableAttendance + " where " + keyLocalSession + "=?";
        Cursor cursor = db.rawQuery(aQuery, new String[]{String.valueOf(sessionId)});


//        List<Attendance> attendances = new ArrayList<Attendance>();
//        if(cursor.moveToFirst()) {
//            do {
//                Attendance attendance = new Attendance(cursor.getLong(0), cursor.getInt(1), cursor.getInt(4)==1, cursor.getInt(5));
//                attendance.setLatitude(cursor.getDouble(2));
//                attendance.setLongitude(cursor.getDouble(3));
//                attendances.add(attendance);
//            } while (cursor.moveToNext());
//        }
        if (cursor.moveToFirst()) {
            Attendance attendance = new Attendance(cursor.getLong(0), cursor.getInt(1), cursor.getInt(4) == 1, cursor.getInt(5), cursor.getString(6));
            attendance.setLatitude(cursor.getDouble(2));
            attendance.setLongitude(cursor.getDouble(3));

            return attendance;
        }


        return null;
    }

    /* ********************************* ****************** ********************************* */

    /* *************************************** Errors *************************************** */

    public void storeError(TrackedError error) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(keyErrorTime, error.getTime());
        values.put(keyErrorFunction, error.getFunction());
        values.put(keyErrorStackTrace, error.getStackTrace());
        values.put(keyErrorResponse, error.getResponse());
        db.insert(tableErrorTracker, null, values);
        db.close();
    }

    public List<TrackedError> getUnsyncedErrors() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyErrorTime + ", "
                + keyErrorFunction + ", "
                + keyErrorStackTrace + ", "
                + keyErrorResponse
                + " from " + tableErrorTracker + " where not " + flagIsSynced;
        Cursor cursor = db.rawQuery(query, null);
        List<TrackedError> errors = new ArrayList<TrackedError>();
        if (cursor.moveToFirst()) {
            do {
                TrackedError error = new TrackedError();
                error.setTime(cursor.getLong(0));
                error.setFunction(cursor.getString(1));
                error.setStackTrace(cursor.getString(2));
                error.setResponse(cursor.getString(3));
                errors.add(error);
            } while (cursor.moveToNext());
        }

        return errors;
    }

    public List<TrackedError> getAllErrors() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyErrorTime + ", "
                + keyErrorFunction + ", "
                + keyErrorStackTrace + ", "
                + keyErrorResponse
                + " from " + tableErrorTracker;
        Cursor cursor = db.rawQuery(query, null);
        List<TrackedError> errors = new ArrayList<TrackedError>();
        if (cursor.moveToFirst()) {
            do {
                TrackedError error = new TrackedError();
                error.setTime(cursor.getLong(0));
                error.setFunction(cursor.getString(1));
                error.setStackTrace(cursor.getString(2));
                error.setResponse(cursor.getString(3));
                errors.add(error);
            } while (cursor.moveToNext());
        }

        return errors;
    }

    /* ********************************* ****************** ********************************* */

    /* ********************************* Unproductive Calls ********************************* */

    public void storeUnproductiveCall(UnproductiveCall unproductiveCall) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(keyUnproductiveTime, unproductiveCall.getTime());
        values.put(keyUnproductiveReason, unproductiveCall.getReason());
        values.put(keyUnproductiveRemark, unproductiveCall.getRemark());
        values.put(keyOutletId, unproductiveCall.getOutletId());
        values.put(keyLatitude, unproductiveCall.getLatitude());
        values.put(keyLongitude, unproductiveCall.getLongitude());
        values.put(keyBatteryLevel, unproductiveCall.getBatteryLevel());
        if (unproductiveCall.isSynced()) {
            values.put(flagIsSynced, 1);
        }
        db.insert(tableUnproductiveCall, null, values);
        db.close();
    }

    public List<UnproductiveCall> getAllUnproductiveCalls() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyUnproductiveTime + ", "
                + keyUnproductiveReason + ", "
                + keyOutletId + ", "
                + keyLatitude + ", "
                + keyLongitude + ", "
                + flagIsSynced + ", "
                + keyBatteryLevel + ", "
                + keyUnproductiveRemark
                + " from " + tableUnproductiveCall;
        List<UnproductiveCall> unproductiveCalls = new ArrayList<UnproductiveCall>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                UnproductiveCall call = new UnproductiveCall();
                call.setTime(cursor.getLong(0));
                call.setReason(cursor.getString(1));
                call.setOutletId(cursor.getInt(2));
                try {
                    call.setLatitude(Double.parseDouble(cursor.getString(3)));
                } catch (NumberFormatException e) {
                    call.setLatitude(0);
                }
                try {
                    call.setLongitude(Double.parseDouble(cursor.getString(4)));
                } catch (NumberFormatException e) {
                    call.setLongitude(0);
                }
                call.setSynced(cursor.getInt(5) == 1);
                call.setBatteryLevel(cursor.getInt(6));
                call.setRemark(cursor.getString(7));
                unproductiveCalls.add(call);
            } while (cursor.moveToNext());
        }
        return unproductiveCalls;
    }

    public List<UnproductiveCall> getUnsyncedUnproductiveCalls() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "
                + keyUnproductiveTime + ", "
                + keyUnproductiveReason + ", "
                + keyOutletId + ", "
                + keyLatitude + ", "
                + keyLongitude + ", "
                + keyUnproductiveRemark + ", "
                + keyBatteryLevel
                + " from " + tableUnproductiveCall + " where not " + flagIsSynced;
        List<UnproductiveCall> unproductiveCalls = new ArrayList<UnproductiveCall>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                UnproductiveCall call = new UnproductiveCall();
                call.setTime(cursor.getLong(0));
                call.setReason(cursor.getString(1));
                call.setOutletId(cursor.getInt(2));
                call.setLatitude(cursor.getDouble(3));
                call.setLongitude(cursor.getDouble(4));
                call.setSynced(false);
                call.setBatteryLevel(cursor.getInt(6));
                call.setRemark(cursor.getString(5));
                unproductiveCalls.add(call);
            } while (cursor.moveToNext());
        }
        return unproductiveCalls;
    }

    /* ********************************* ****************** ********************************* */

    /**
     * Clear Database
     */
    public void clearTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists " + tableRoute);
        db.execSQL("drop table if exists " + tableOutlet);
        db.execSQL("drop table if exists " + tableOutletHistory);
        db.execSQL("drop table if exists " + tableItem);
        db.execSQL("drop table if exists " + tableCashPayment);
        db.execSQL("drop table if exists " + tableChequePayment);
        db.execSQL("drop table if exists " + tableItemCategory);
        db.execSQL("drop table if exists " + tableItemType);
        db.execSQL("drop table if exists " + tableUnproductiveCall);
        db.execSQL("drop table if exists " + tableBank);
        db.execSQL("drop table if exists " + tableBankBranch);
//        db.execSQL("drop table if exists " + tableOrder);
//        db.execSQL("drop table if exists " + tableOrderDetail);
//        db.execSQL("drop table if exists " + tableReturnDetail);
//        db.execSQL("drop table if exists " + tableOrderPayment);
        db.execSQL("drop table if exists " + tableVisitDetails);
        db.execSQL("drop table if exists " + tableFlavour);
        onCreate(db);
    }
}
