package com.liangdekai.anime.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.liangdekai.anime.R;
import com.liangdekai.anime.adapter.CatalogueAdapter;
import com.liangdekai.anime.bean.CartoonBean;
import com.liangdekai.anime.db.CartoonDB;
import com.liangdekai.anime.util.HandleSaveResponse;
import com.liangdekai.anime.util.TransformString;
import com.liangdekai.anime.util.VolleyHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , AdapterView.OnItemClickListener {
    private ListView mListView ;
    private CatalogueAdapter mCatalogueAdapter ;
    private List<CartoonBean> mCartoonList;
    private DrawerLayout mDrawerLayout ;
    private CartoonDB mCartoonDB ;
    private ProgressDialog mProgressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mListView = (ListView) findViewById(R.id.activity_main_shelf) ;
        mCartoonList = new ArrayList<>();
        mCartoonDB = CartoonDB.getInstance(MainActivity.this) ;
        mListView.setOnItemClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListView.setSelection(0);
                }
            });
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String name = "" ;
        if (id == R.id.adult_cartoon) {
            name = "少年漫画" ;
        } else if (id == R.id.teen_cartoon) {
            name = "青年漫画" ;
        } else if (id == R.id.girl_cartoon) {
            name = "少女漫画" ;
        } else if (id == R.id.beauty_cartoon) {
            name = "耽美漫画" ;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        Log.d("test" , name);
        loadData(name);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadData(String name){
        mCartoonList = mCartoonDB.loadCartoonInfo(name);
        if (mCartoonList.size() == 0){
            showDialog();
            sendRequest(name);
        }else {
            mCatalogueAdapter = new CatalogueAdapter(MainActivity.this , mCartoonList);
            mListView.setAdapter(mCatalogueAdapter);
            mCatalogueAdapter.notifyDataSetChanged();
        }
    }

    private void sendRequest(final String name){
        String type = TransformString.transform(name) ;
        String url = "http://japi.juhe.cn/comic/book?name=&type="+type+"&skip=&finish=&key=eff3d8473c455edbc5e47e2b0b2d21ea";
        VolleyHelper.sendByVolley(url, new VolleyHelper.VolleyListener() {
            @Override
            public void onSucceed(String jsonString) {
                closeDialog();
                HandleSaveResponse.handleSaveResponse(mCartoonDB , jsonString);
                if (mCartoonList != null){
                    mCartoonList.clear();
                }
                mCartoonList = mCartoonDB.loadCartoonInfo(name);
                mCatalogueAdapter = new CatalogueAdapter(MainActivity.this , mCartoonList);
                mListView.setAdapter(mCatalogueAdapter);
                mCatalogueAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                closeDialog();
                Toast.makeText(MainActivity.this , "请检查网络设置" , Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     *创建展示对话框
     */
    private void showDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("请耐心等待");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DetailActivity.startActivity(MainActivity.this , mCartoonList.get(i).getName());
    }
}
