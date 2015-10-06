package com.colombosoft.ednasalespad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.dialog.CustomProgressDialog;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.NetworkFunctions;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.Bank;
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

public class LoginActivity extends ActionBarActivity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();

    //PIN Code to activate developer mode
    private final String PIN_CODE = "19920703";

    private final String SERVER_URL_LIVE = "http://edna.com/api";
    private final String SERVER_URL_GATEWAY = "http://gateway.edna.com/api";
    private final String SERVER_URL_TESTING = "http://gateway.ceylonlinux.com/edna_sfa2/android_service/";
    private final String SERVER_URL_TESTING_IP = "http://124.43.26.21/edna_sfa2/android_service/";


    private Toolbar toolbar;

    private EditText etUsername, etPassword, pin;
    private Button btnSignIn;
    private TextView tvError;

    private DatabaseHandler dbHandler;
    private SharedPref pref;
    private NetworkFunctions networkFunctions;
    private CustomAnimations customAnimations;

    private Handler handler = new Handler();
    private Runnable hideRunnable;
    private boolean canHide = false;

    private List<Integer> ids;
    private List<String> uris;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHandler = DatabaseHandler.getDbHandler(LoginActivity.this);
        pref = SharedPref.getInstance(LoginActivity.this);
        networkFunctions = new NetworkFunctions();
        customAnimations = new CustomAnimations(LoginActivity.this);

        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("Edna Sales Pad");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        etUsername = (EditText) findViewById(R.id.login_et_username);
        etPassword = (EditText) findViewById(R.id.login_et_password);

        Toast.makeText(LoginActivity.this, networkFunctions.getBaseURL(), Toast.LENGTH_LONG).show();

        //Initialize Spinner
        final Spinner spinner = (Spinner) findViewById(R.id.server_list_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.server_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //TODO: Remove on the Production
        spinner.setSelection(2);

        //PIN Code check and shows the spinner if the PIN Code is valid
        pin = (EditText) findViewById(R.id.PIN);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pin.getText().toString().equals(PIN_CODE)) {
                    pin.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String server_url = "";
                switch (position) {
                    case 0:
                        server_url = SERVER_URL_LIVE;
                        break;
                    case 1:
                        server_url = SERVER_URL_GATEWAY;
                        break;
                    case 2:
                        server_url = SERVER_URL_TESTING;
                        break;
                    case 3:
                        server_url = SERVER_URL_TESTING_IP;
                }
                networkFunctions.baseURL = server_url;
                pref.setServer(server_url);
                Log.i(LOG_TAG, "Server URL Set to: " + server_url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Used to hide the error view
         */
        hideRunnable = new Runnable() {
            @Override
            public void run() {
                hideErrorText();
            }
        };

        tvError = (TextView) findViewById(R.id.login_tv_error);
        tvError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideErrorText();
            }
        });

        btnSignIn = (Button) findViewById(R.id.login_btn_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().equals("")) {
                    showErrorText("Please enter a username");
                    etUsername.requestFocus();
                } else if (etPassword.getText().toString().equals("")) {
                    showErrorText("Please enter a password");
                    etPassword.requestFocus();
                } else {
                    if (NetworkUtil.isNetworkAvailable(LoginActivity.this)) {
//                        Log.d(LOG_TAG, "Username : " + etUsername.getText().toString() + ", Password : " + etPassword.getText().toString());
                        String username = etUsername.getText().toString();
                        String password = etPassword.getText().toString();
                        new Authenticate().execute(username, password);
                    } else {
                        showErrorText("PLEASE ENABLE INTERNET");
                    }
                }
            }
        });

    }

    /**
     * Hide the Error View
     */
    private void hideErrorText() {
        if (canHide) {
            ViewCompat.animate(tvError).translationY(500).setDuration(400);
            canHide = false;
        }
    }

    /**
     * Show the Error View
     *
     * @param error
     */
    private void showErrorText(String error) {
        tvError.setTranslationY(500);
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);

        ViewCompat.animate(tvError).translationY(0).setDuration(400);
        canHide = true;
        handler.postDelayed(hideRunnable, 4000);
    }

    private class Authenticate extends AsyncTask<String, Void, Boolean> {

        private CustomProgressDialog pDialog;
        private List<String> errors = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new CustomProgressDialog(LoginActivity.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setMessage("Authenticating...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                String loginResponse = networkFunctions.authenticate(params[0], params[1]);
                JSONObject loginJSON = new JSONObject(loginResponse);
                if (loginJSON.getBoolean("result")) {
                    dbHandler.clearTables();
                    // Login successful. Proceed to download other items
                    User user = User.parseUser(loginJSON);
                    user.setUsername(params[0]);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Data...");
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Product Types...");
                        }
                    });

                    // Types
                    String types = networkFunctions.getProductTypes(user.getLocationId());
                    JSONArray typesJSONArray = new JSONArray(types);
                    List<ProductType> typesList = new ArrayList<ProductType>();
                    for (int i = 0; i < typesJSONArray.length(); i++) {
                        typesList.add(ProductType.parseType(typesJSONArray.getJSONObject(i)));
                    }
                    dbHandler.storeTypes(typesList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Flavours...");
                        }
                    });
                    // Flavours
                    String flavours = networkFunctions.getFlavours(user.getLocationId());
                    JSONArray flavoursJSONArray = new JSONArray(flavours);
                    List<Flavour> flavourList = new ArrayList<Flavour>();
                    for (int i = 0; i < flavoursJSONArray.length(); i++) {
                        flavourList.add(Flavour.parseFlavour(flavoursJSONArray.getJSONObject(i)));
                    }
                    dbHandler.storeFlavours(flavourList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Routes & Outlets...");
                        }
                    });
                    // Routes and outlets
                    String routes = networkFunctions.getRoutesAndOutlets(user.getLocationId(), user.getTerritoryId());
                    JSONArray routesJSONArray = new JSONArray(routes);
                    List<Route> routeList = new ArrayList<Route>();
                    for (int i = 0; i < routesJSONArray.length(); i++) {
                        routeList.add(Route.parseRoute(routesJSONArray.getJSONObject(i)));
                    }
                    dbHandler.storeRouteList(routeList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Item Catagories...");
                        }
                    });
                    // Item categories
                    String categories = networkFunctions.getItemCategories(user.getLocationId());
                    JSONArray categoriesJSONArray = new JSONArray(categories);
                    List<ItemCategory> categoryList = new ArrayList<ItemCategory>();
                    for (int i = 0; i < categoriesJSONArray.length(); i++) {
                        categoryList.add(ItemCategory.parseCategory(categoriesJSONArray.getJSONObject(i)));
                    }

                    String distStock = networkFunctions.getStockDetails(user.getLocationId(), 1);
                    Log.i("DIST", distStock);
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
                                if (!item.getImageUri().equalsIgnoreCase("N/A") && !uris.contains(item.getImageUri())) {
                                    uris.add(item.getImageUri());
                                }
                            }
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Banks...");
                        }
                    });
                    String bankResponse = networkFunctions.getBanks(user.getLocationId());
                    JSONArray bankResponseJSONArray = new JSONArray(bankResponse);
                    List<Bank> bankList = new ArrayList<Bank>();
                    for (int i = 0; i < bankResponseJSONArray.length(); i++) {
                        bankList.add(Bank.parseBank(bankResponseJSONArray.getJSONObject(i)));
                    }
                    dbHandler.storeBanks(bankList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.setMessage("Authenticated\nDownloading Images...");
                        }
                    });

                    downloadImages();

                    dbHandler.storeUser(user);
                    pref.storeLoginUser(user);

                    return true;
                } else {
                    errors.add("Please enter correct username and password");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                errors.add("Unable to reach the server.");
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("ERRORRRR", e.getMessage());
                errors.add("Received an invalid response from the server.");
                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                errors.add("Received an invalid response from the server.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pDialog.setMessage("Finalizing data");

            if (result) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(LoginActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                pref.setLoginStatus(true);
                Intent intent = new Intent(LoginActivity.this, MarkAttendanceActivity.class);
                intent.putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true);
                startActivity(intent);
                finish();
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
                        bitmap = networkFunctions.downloadImage(fileName);

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

}
