package com.example.criptabit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Connect.CommonUtils;
import Connect.CommonUtilsCoins;
import Connect.Constants;


public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


  private  DashboardItem dashboardItem;

  //



    private String query;
    ListView dashListView;
    DashboardAdapter mAdapter;
    ArrayList<DashboardItem> dashList;
    String ALLSYMBOLS = "BTC,ETH,DOGE";

    //




    //variable for drawer down below
    DrawerLayout drawerLayout;
  NavigationView navigationView;
    Toolbar toolbar;
   DashboardAdapter adapter;
   ListView listView;
   LinearLayout stock,coin;
   ImageView ham,share,help;
   boolean isLoggedIn=false;
    private FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dashboard);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        ham=findViewById(R.id.ham);
        share=findViewById(R.id.share);
        help=findViewById(R.id.help);
        stock=findViewById(R.id.stock);
        coin=findViewById(R.id.coin);
        mAuth = FirebaseAuth.getInstance(); //for firebase auth

        dashListView = findViewById(R.id.favstock);//stock

        dashList = new ArrayList<>();
        new Dashboard.AsyncTaskImpl().execute("market/v2/get-quotes?region=US&symbols=" +
                "MSFT%2CLUV%2CA%2CT%2CGLD%2CAMC%2CALL%2CADPT%2CACB%2IBM%2CAAPL%2CAMZN", null);






        /*-------------------Hooks for drawer---------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar =  findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setTitle("");
//        toolbar.setSubtitle("");

        /*-------------------Toolbar for drawer---------------------*/
//        setSupportActionBar(toolbar);


        /*-------------------Navigation Drawer menu for drawer---------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);

        /*-------------------Hide or show  for drawer---------------------*/

//        Menu menu =navigationView.getMenu();
//        menu.findItem(R.id.logout).setVisible(false);
//        menu.findItem(R.id.profile).setVisible(false);
//  this option above is used to create sign in and signup effect
//  *---------------------------------*/





        stock.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Stockmarket.class);
                startActivity(intent);
            }
        });

        coin.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Cryptomarket.class);
                startActivity(intent);
            }
        });


        updateUI(); //for checking login logout status


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareText="Welcome To Criptabit \nDownload This App now:-\nhttps://google.com";
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                String chooserTitle="Share Criptabit Via";
                startActivity(Intent.createChooser(sendIntent,chooserTitle));
            }
        });
        ham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.bringToFront();
                ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(Dashboard.this,
                        drawerLayout,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();
            }
        });
    }


    @Override
     public void onBackPressed() {

        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
     }

     private void updateUI(){  //for checking loging or logout
         FirebaseUser currentUser = mAuth.getCurrentUser();
         if(currentUser != null){

             navigationView.getMenu().findItem(R.id.login).setVisible(false);
             navigationView.getMenu().findItem(R.id.signup).setVisible(false);
             navigationView.getMenu().findItem(R.id.logout).setVisible(true);
             navigationView.getMenu().findItem(R.id.fav).setVisible(true);
             navigationView.getMenu().findItem(R.id.profile).setVisible(true);
         }
         else{
             navigationView.getMenu().findItem(R.id.login).setVisible(true);
             navigationView.getMenu().findItem(R.id.signup).setVisible(true);
             navigationView.getMenu().findItem(R.id.logout).setVisible(false);
             navigationView.getMenu().findItem(R.id.fav).setVisible(false);
             navigationView.getMenu().findItem(R.id.profile).setVisible(false);
         }
         invalidateOptionsMenu();
     }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.home:
                break;

            case R.id.fav:
                break;

            case R.id.coinmarket:
                Intent intentC = new Intent(Dashboard.this,Cryptomarket.class);
                startActivity(intentC);
                break;
            case R.id.stockmarket:
                Intent intentS = new Intent(Dashboard.this,Stockmarket.class);
                startActivity(intentS);
                break;
            case R.id.profile:
                Intent intent = new Intent(Dashboard.this,UserProfile.class);
                startActivity(intent);
                    break;
            case R.id.signup:
                Intent signup_intent = new Intent(Dashboard.this,Signup.class);
                startActivity(signup_intent);
                break;
            case R.id.login:
                Intent login_intent = new Intent(Dashboard.this,Login.class);
                startActivity(login_intent);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this,"Logout", Toast.LENGTH_SHORT).show();
                Intent intentLogin = new Intent(Dashboard.this, Login.class);
                startActivity(intentLogin);
                break;
            case R.id.share:
                String shareText="Welcome To Criptabit \nDownload This App now:-\nhttps://google.com";
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                String chooserTitle="Share Criptabit Via";
                startActivity(Intent.createChooser(sendIntent,chooserTitle));
                break;
            case R.id.help:
                break;
            case R.id.about:
                Intent about_intent = new Intent(Dashboard.this,About.class);
                startActivity(about_intent);
                break;
        }

        drawerLayout.closeDrawer((GravityCompat.START));
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class GetSearchAsyncTask extends AsyncTask<String, String, String> {


        KProgressHUD kProgressHUD;

        @Override
        protected String doInBackground(String... args) {
            if (CommonUtilsCoins.checkInternetStatus(Dashboard.this)) {
                try {

                    String reqUrl = args[0];
                    String reqBody = args[1];

                    return CommonUtilsCoins.executeWebRequest(reqUrl, reqBody);
                } catch (Exception e) {
                    Snackbar.make(findViewById(android.R.id.content), Constants.ERROR_MSG, Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                    return "Error";
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), Constants.NO_INTERNET, Snackbar.LENGTH_LONG).show();
                return "Error";
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            kProgressHUD = KProgressHUD.create(Dashboard.this);
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            kProgressHUD.dismiss();
            String symbol_arr[] = {"BTC", "ETH", "DOGE"};
            if (!result.matches("Error")) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject display = obj.getJSONObject("DISPLAY");
                    JSONObject raw = obj.getJSONObject("RAW");

                    for (int i = 0; i <symbol_arr.length; i++) {
                        JSONObject sym = display.getJSONObject(symbol_arr[i]);
                        JSONObject currentObj = sym.getJSONObject("INR");
                        JSONObject sym1 = raw.getJSONObject(symbol_arr[i]);
                        JSONObject currentObj1 = sym1.getJSONObject("INR");

                        DashboardItem dashboardItem = new DashboardItem();
                        dashboardItem.Symbol = currentObj1.getString("FROMSYMBOL");
                        dashboardItem.MarketCap = currentObj.getString("MKTCAP");
                        dashboardItem.Price = currentObj1.getString("PRICE");
                        dashboardItem.Volume = currentObj.getString("SUPPLY");
                        dashboardItem.Percentage = currentObj1.getString("CHANGEPCTHOUR");
                        dashList.add(dashboardItem);
                    }

                    mAdapter = new DashboardAdapter(getApplicationContext(),dashList);
                    dashListView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class AsyncTaskImpl extends AsyncTask<String, String, String> {


        KProgressHUD kProgressHUD;

        @Override
        protected String doInBackground(String... args) {
            if (CommonUtils.checkInternetStatus(Dashboard.this)) {
                try {

                    String reqUrl = args[0];
                    String reqBody = args[1];

                    return CommonUtils.executeWebRequest(reqUrl, reqBody);
                } catch (Exception e) {
                    Snackbar.make(findViewById(android.R.id.content), Constants.ERROR_MSG, Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                    return "Error";
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), Constants.NO_INTERNET, Snackbar.LENGTH_LONG).show();
                return "Error";
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            kProgressHUD = KProgressHUD.create(Dashboard.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            kProgressHUD.dismiss();
            new Dashboard.GetSearchAsyncTask().execute("pricemultifull?fsyms=" + ALLSYMBOLS +
                    "&api_key=fa9844413727a02fe2322204004108e1bb499680eaebaa7cedd170f7f93aa463&tsyms=INR", null);
            if (!result.matches("Error")) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject objec = obj.getJSONObject("quoteResponse");
                    JSONArray arr = objec.getJSONArray("result");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject currentObj = arr.getJSONObject(i);
                        DashboardItem dashboardItem = new DashboardItem();
                        dashboardItem.Symbol = currentObj.getString("symbol");
                        dashboardItem.MarketCap = currentObj.getString("marketCap");
                        dashboardItem.Price = currentObj.getString("regularMarketPrice");
                        dashboardItem.Volume = currentObj.getString("regularMarketVolume");
                        dashboardItem.Percentage = currentObj.getString("regularMarketChangePercent");
                        dashList.add(dashboardItem);
                    }
                    mAdapter = new DashboardAdapter(getApplicationContext(),dashList);
                    dashListView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}