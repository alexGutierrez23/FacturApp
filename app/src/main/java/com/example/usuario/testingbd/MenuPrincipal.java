package com.example.usuario.testingbd;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import fragment.InvoiceFragment;
import fragment.PresupuestoFragment;
import fragment.RendimientoFacturasFragment;
import fragment.RendimientoTicketsFragment;
import fragment.TicketFragment;
import sqlite.data.DatabaseHelper;
import sqlite.model.Invoice;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.repo.InvoiceDML;
import sqlite.repo.PresupuestoDML;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.TicketDML;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TabLayout rdtoTabLayout;
    private ViewPager rdtoViewPager;

    private ViewPager contratoViewPager;

    private Toolbar toolbar;

    private DrawerLayout drawer;
    private NavigationView nv;

    private Constants constants = new Constants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //android.support.v7.app.ActionBar act = getSupportActionBar();
       //act.setDisplayHomeAsUpEnabled(true);
        Constants c = new Constants();
        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        // NAV DRAWER
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        nv = (NavigationView) drawer.findViewById(R.id.nav_view);
        View ll = nv.getHeaderView(0);
        TextView tvNombre = (TextView) ll.findViewById(R.id.subtituloNavDrawer);
        Taxi_Driver txd = commonMethodsImplementation.getTxd(this);
        String txdId = String.valueOf(txd.getTxd_id());
        Taxi_driver_account tda = new Taxi_driver_account();
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        int tda_id = tdaDml.getTdaIdByTxdId(new DatabaseHelper(this),
                txdId, this);
        tda = tdaDml.getTdaById(new DatabaseHelper(this),
                String.valueOf(tda_id), this);
        tvNombre.setText(tda.getTda_name());
        // end nav drawer
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), MenuPrincipal.this);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        rdtoViewPager = (ViewPager) findViewById(R.id.rdto_viewpager);
        RendimientoPagerAdapter rendimientosPagerAdapter = new RendimientoPagerAdapter(getSupportFragmentManager(), MenuPrincipal.this);
        rdtoViewPager.setAdapter(rendimientosPagerAdapter);

        rdtoTabLayout = (TabLayout) findViewById(R.id.rdto_tabs);
        rdtoTabLayout.setupWithViewPager(rdtoViewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < rdtoTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = rdtoTabLayout.getTabAt(i);
            tab.setCustomView(rendimientosPagerAdapter.getTabView(i));
        }

        // NAV DRAWER
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.w("before","Logcat save");
        // File logFile = new File( + "/log.txt" );
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            process = Runtime.getRuntime().exec( "logcat -f " + "/storage/emulated/0/"+"Logging.txt");
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        //copiar pdf del assets a directorio
        try {
            AssetManager assetFiles = getAssets();

            // MyHtmlFiles is the name of folder from inside our assets folder
            String[] files = assetFiles.list("Guias");

            // Initialize streams
            InputStream in = null;
            OutputStream out = null;

            for (int i = 0; i < files.length; i++) {
                if (files[i].toString().equalsIgnoreCase("images")
                        || files[i].toString().equalsIgnoreCase("js")) {


                    //  @Do nothing. images and js are folders but they will be
                    //  interpreted as files.

                    //  @This is to prevent the app from throwing file not found
                    // exception.
                } else {
                    // @Folder name is also case sensitive
                    // @MyHtmlFiles is the folder from our assets
                    in = assetFiles.open("Guias/" + files[i]);
                    // Currently we will copy the files to the root directory
                    // but you should create specific directory for your app
                    File pdfDir = new File(Environment.getExternalStorageDirectory()
                            + File.separator + c.NOMBRE_CARPETA_APP + File.separator + c.GUIAS);
                    if (!pdfDir.exists()) {
                        pdfDir.mkdir();
                    }

                    out = new FileOutputStream(pdfDir + File.separator + files[i]);
                    commonMethodsImplementation.copyAssetFiles(in, out, 1024);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
*/
    @Override
    public void onResume(){
        super.onResume();
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        String titulosTab[] = new String[] {"TICKETS", "FACTURAS", "PRESUPUESTOS"};
        Context context;

        public PagerAdapter(FragmentManager fragmentManager, Context context){
            super(fragmentManager);
            this.context = context;
        }

        @Override
        public int getCount(){
            return titulosTab.length;
        }

        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return new TicketFragment(); // Aqui va cada fragment creado (Tickets, Facturas y Presupuestos
                case 1:
                    return new InvoiceFragment();
                case 2:
                    return new PresupuestoFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulosTab[position];
        }

        public View getTabView(int position){
            View tab = LayoutInflater.from(MenuPrincipal.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(titulosTab[position]);
            return tab;
        }
    }

    public class RendimientoPagerAdapter extends FragmentPagerAdapter {
        String titulosTab[] = new String[] {"TICKETS", "FACTURAS"};
        Context context;

        public RendimientoPagerAdapter(FragmentManager fragmentManager, Context context){
            super(fragmentManager);
            this.context = context;
        }

        @Override
        public int getCount(){
            return titulosTab.length;
        }

        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return new RendimientoTicketsFragment();
                case 1:
                    return new RendimientoFacturasFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulosTab[position];
        }

        public View getTabView(int position){
            View tab = LayoutInflater.from(MenuPrincipal.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(titulosTab[position]);
            return tab;
        }
    }

    public ArrayList<Ticket> getTicketList(){
        TicketDML tckDml = new TicketDML(this);
        return tckDml.getAllTickets(new DatabaseHelper(this), this);
    }

    public ArrayList<Invoice> getInvoiceList(){
        InvoiceDML invDml = new InvoiceDML(this);
        return invDml.getAllInvoices(new DatabaseHelper(this), this);
    }

    public ArrayList<sqlite.model.Presupuesto> getPresupuestoList(){
        PresupuestoDML preDml = new PresupuestoDML(this);
        return preDml.getAllPresupuestos(new DatabaseHelper(this), this);
    }

    public ArrayList<Ticket> getRendimientoTicketList(){
        TicketDML tckDml = new TicketDML(this);
        return tckDml.getAllRendimientoTickets(new DatabaseHelper(this), this);
    }

    public ArrayList<Invoice> getRendimientoFacturaList(){
        InvoiceDML invDml = new InvoiceDML(this);
        return invDml.getAllRendimientoFacturas(new DatabaseHelper(this), this);
    }

    //NAV DRAWER
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home){
            toolbar.setTitle("FacturApp");
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            rdtoTabLayout.setVisibility(View.GONE);
            rdtoViewPager.setVisibility(View.GONE);
            //contratoViewPager.setVisibility(View.GONE);
        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(this, EditProfile.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_logo) {  //activar en siguiente version!!
            Intent i = new Intent(this, UploadLogo.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_rendimientos){
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            //contratoViewPager.setVisibility(View.GONE);
            toolbar.setTitle("Rendimiento Mensual");
            rdtoTabLayout.setVisibility(View.VISIBLE);
            rdtoViewPager.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_hojaRuta) {
/*            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            rdtoTabLayout.setVisibility(View.GONE);
            rdtoViewPager.setVisibility(View.GONE);
            toolbar.setTitle("CONTRATOS");
            contratoViewPager.setVisibility(View.VISIBLE);
  */        startActivity(new Intent(this, VistaContratos.class));
        } else if (id == R.id.nav_ayudaGeneral) {
            //Manana seguir aqui!! Pensar en como mostrar la guia
            final String pdfTitle = "guia_uso_FacturApp.pdf";
            Intent i = new Intent(this, DisplayPdf.class);
            i.putExtra("var_docId", pdfTitle);
            i.putExtra("var_idType", "PDF_GUIA");
            startActivity(i);
            finish();
        } else if (id == R.id.nav_ayudaBluetooth) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*
    public class ContratoPagerAdapter extends FragmentPagerAdapter {
        Context context;

        public ContratoPagerAdapter(FragmentManager fragmentManager, Context context){
            super(fragmentManager);
            this.context = context;
        }

        @Override
        public int getCount(){
            return 1;
        }

        @Override
        public Fragment getItem(int position){
            return new ContratoFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "CONTRATOS";
        }
    }
*/
}
