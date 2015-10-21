package com.colombosoft.ednasalespad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.dialog.CustomProgressDialog;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.NetworkFunctions;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.Flavour;
import com.colombosoft.ednasalespad.model.Item;
import com.colombosoft.ednasalespad.model.ItemCategory;
import com.colombosoft.ednasalespad.model.ProductType;
import com.colombosoft.ednasalespad.model.Route;
import com.colombosoft.ednasalespad.model.Stock;
import com.colombosoft.ednasalespad.model.User;
import com.colombosoft.ednasalespad.utils.CustomAnimations;
import com.colombosoft.ednasalespad.utils.NetworkUtil;
import com.colombosoft.ednasalespad.utils.RequestCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnlockActivity extends ActionBarActivity {

    private final String LOG_TAG = UnlockActivity.class.getSimpleName();

    private User user;

    private TextView tvError;
    private EditText etPassword;

    private SharedPref pref;
    private DatabaseHandler dbHandler;
    private NetworkFunctions funcs;
    private CustomAnimations customAnimations;

    private Handler handler = new Handler();
    private Runnable hideRunnable;
    private boolean canHide = false;

    private List<String> uris;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.unlock_toolbar);
//        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        title.setText("Edna Salespad");

        pref = SharedPref.getInstance(UnlockActivity.this);
        dbHandler = DatabaseHandler.getDbHandler(UnlockActivity.this);
        customAnimations = new CustomAnimations(UnlockActivity.this);
        funcs = new NetworkFunctions();

        hideRunnable = new Runnable() {
            @Override
            public void run() {
                hideErrorText();
            }
        };

        Intent intent = getIntent();
        if (intent.hasExtra("user")) {
            user = (User) getIntent().getExtras().get("user");

            etPassword = (EditText) findViewById(R.id.unlock_et_password);
            Button btnLogin = (Button) findViewById(R.id.unlock_btn_sign_in);

            etPassword.setText("viduranga");

            tvError = (TextView) findViewById(R.id.unlock_tv_error);
            tvError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideErrorText();
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etPassword.getText().toString().equals("")) {
                        showErrorText("Please enter a password");
                    } else {
                        if (NetworkUtil.isNetworkAvailable(UnlockActivity.this)) {
                            String password = etPassword.getText().toString();
                            new Authenticate().execute(password);
                        } else {
                            showErrorText("Please enable Internet");
                        }
                    }
                }
            });

//            Log.d("UnlockActivity.java -> onCreate()", "Getting user :\n" + user.toString());

            ImageView profilePic = (ImageView) findViewById(R.id.unlock_profile_pic);
            TextView name = (TextView) findViewById(R.id.unlock_rep_name);
//            TextView role = (TextView) findViewById(R.id.unlock_rep_role);
//            TextView territory = (TextView) findViewById(R.id.unlock_rep_territory);
//            TextView contact = (TextView) findViewById(R.id.unlock_rep_contact);

            name.setText(user.getName());
//            role.setText(user.getPosition());
//            territory.setText(user.getTerritory());
//            contact.setText(user.getContact());

            String imageURI = user.getImageURI();
            if (imageURI != null) {
                user.setImageURI(imageURI);

                Drawable pic = Drawable.createFromPath(imageURI);
                if (pic != null) {
                    profilePic.setImageDrawable(pic);
                }
            }

        } else {
            startActivity(new Intent(UnlockActivity.this, LoginActivity.class));
            finish();
        }

    }

    private class Authenticate extends AsyncTask<String, Void, Boolean> {

        private CustomProgressDialog pDialog;
        private List<String> errors = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            funcs.baseURL = pref.getServer();
            pDialog = new CustomProgressDialog(UnlockActivity.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setMessage("Authenticating...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                String loginResponse = funcs.authenticate(user.getUsername(), params[0]);
                JSONObject loginJSON = new JSONObject(loginResponse);
                if (loginJSON.getBoolean("result")) {
                    dbHandler.clearTables();
                    User latestUser = User.parseUser(loginJSON);
                    latestUser.setUsername(user.getUsername());
                    dbHandler.storeUser(latestUser);
                    pref.storeLoginUser(latestUser);

                    user = latestUser;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Data...");
                        }
                    });

                    // Types
                    String types = funcs.getProductTypes(user.getLocationId());
                    JSONArray typesJSONArray = new JSONArray(types);
                    List<ProductType> typesList = new ArrayList<ProductType>();
                    for (int i = 0; i < typesJSONArray.length(); i++) {
                        typesList.add(ProductType.parseType(typesJSONArray.getJSONObject(i)));
                    }
                    dbHandler.storeTypes(typesList);

                    // Flavours
                    String flavours = funcs.getFlavours(user.getLocationId());
                    JSONArray flavoursJSONArray = new JSONArray(flavours);
                    List<Flavour> flavourList = new ArrayList<Flavour>();
                    for (int i = 0; i < flavoursJSONArray.length(); i++) {
                        flavourList.add(Flavour.parseFlavour(flavoursJSONArray.getJSONObject(i)));
                    }
                    dbHandler.storeFlavours(flavourList);

                    // Routes and outlets
                    String routes = funcs.getRoutesAndOutlets(user.getLocationId(), user.getTerritoryId());
                    JSONArray routesJSONArray = new JSONArray(routes);
                    List<Route> routeList = new ArrayList<Route>();
                    for (int i = 0; i < routesJSONArray.length(); i++) {
                        routeList.add(Route.parseRoute(routesJSONArray.getJSONObject(i)));
                    }
                    dbHandler.storeRouteList(routeList);

                    // Item categories
                    String categories = funcs.getItemCategories(user.getLocationId());
                    JSONArray categoriesJSONArray = new JSONArray(categories);
                    List<ItemCategory> categoryList = new ArrayList<ItemCategory>();
                    for (int i = 0; i < categoriesJSONArray.length(); i++) {
                        categoryList.add(ItemCategory.parseCategory(categoriesJSONArray.getJSONObject(i)));
                    }

                    String distStock = funcs.getStockDetails(user.getLocationId(), user.getType());
                    JSONArray distStockJSONArray = new JSONArray(distStock);
                    List<Stock> distStocks = new ArrayList<Stock>();
                    for (int i = 0; i < distStockJSONArray.length(); i++) {
                        Stock stock = Stock.parseStock(distStockJSONArray.getJSONObject(i));
                        if (stock != null) {
                            distStocks.add(stock);
                        }
                    }

                    for (Stock stock : distStocks) {
                        for (ItemCategory category : categoryList) {
                            boolean found = false;
                            for (int i = 0; i < category.getItemList().size(); i++) {
                                Item item = category.getItemList().get(i);
                                if (item.getItemNo() == stock.getStockItemId()) {
                                    Log.d(LOG_TAG, item.toString() + " is available in stock. Qty : " + String.valueOf(stock.getStockItemQty()));
                                    item.setStockQty(stock.getStockItemQty());
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                break;
                            }
                        }
                    }
                    dbHandler.storeItems(categoryList);

                    uris = new ArrayList<String>();
                    for (int i = 0; i < categoryList.size(); i++) {

                        if (categoryList.get(i).getItemList() != null) {
                            for (Item item : categoryList.get(i).getItemList()) {
//                                ids.add(item.getItemNo());
                                if (item.getImageUri().equalsIgnoreCase("N/A") || uris.contains(item.getImageUri())) {

                                } else {
                                    uris.add(item.getImageUri());
                                }
                            }
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Images...");
                        }
                    });
                    downloadImages();

                    return true;

                } else {
                    errors.add("Please enter correct username and password");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                errors.add("Unable to reach the server.");
//                TrackedError error = new TrackedError(System.currentTimeMillis(),
//                        "UnlockActivity.java -> Authenticate -> doInBackground()",
//                        e.getMessage());
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                errors.add("Received an invalid response from the server.");
                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                errors.add("Received an invalid response from the server.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            pDialog.setMessage("Finalizing data");

            if (result) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                pref.setLoginStatus(true);
                DatabaseHandler.getDbHandler(UnlockActivity.this).setLoggedInTime(pref.getLoginUser().getId());

                Toast.makeText(UnlockActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(UnlockActivity.this, DashboardActivity.class));
                if (pref.isDayStarted()) {
                    if (pref.getSelectedRoute() == null) {
                        Intent intent = new Intent(UnlockActivity.this, ViewRoutesActivity.class);
                        intent.putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(UnlockActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(UnlockActivity.this, MarkAttendanceActivity.class);
                    intent.putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true);
                    startActivity(intent);
                    finish();
                }
            } else {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                StringBuilder sb = new StringBuilder();
                if (errors.size() == 1) {
                    sb.append(errors.get(0));
                } else {
                    sb.append("Following errors occurred");
                    for (String error : errors) {
                        sb.append("\n - ").append(error);
                    }
                }
                showErrorText(sb.toString());
            }
        }
    }

    private void showErrorText(String error) {
        tvError.setTranslationY(500);
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);

        ViewCompat.animate(tvError).translationY(0).setDuration(400);

//        Log.wtf("LOGIN ERROR", error);
//        tvError.startAnimation(customAnimations.getPushUpIn());
//        tvError.setVisibility(View.VISIBLE);

        canHide = true;
        handler.postDelayed(hideRunnable, 4000);
    }
    private void hideErrorText() {
        if (canHide) {
//            tvError.startAnimation(customAnimations.getPushDownOut());
//            tvError.setVisibility(View.GONE);

            ViewCompat.animate(tvError).translationY(500).setDuration(400);

            canHide = false;
        }
    }

    public void downloadImages() {

        final String PRODUCT_DIR = new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/EDNA/Products/").toString();
        boolean flag3;
        if (uris == null) {
            return;
        }
        File file = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/EDNA/Products/").toString());
        flag3 = true;
        if (!file.exists()) {
            flag3 = file.mkdirs();
        }

        if (!flag3) {
            Log.d(LOG_TAG, "Cannot create directory");
        } else {
            int i = 0;
            String fileName;

            for (i = 0; i < uris.size(); i++) {
                fileName = uris.get(i);

                if ((new File(PRODUCT_DIR, fileName)).exists()) {
                    Log.i(LOG_TAG, new StringBuilder().append(PRODUCT_DIR).append(fileName).append(" already exists. Skipping Download").toString());
                } else {
                    Bitmap bitmap = null;
                    bitmap = funcs.downloadImage(fileName);

                    if (bitmap == null) {
                        //If unable to fetch the image, assign a default image
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_product);
                    }

                    //Saving the image
                    File image_file = new File(PRODUCT_DIR, fileName);
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(image_file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(LOG_TAG, "Loaded: " + fileName);
                }
            }

        }
    }

}
