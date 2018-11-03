package com.example.usuario.testingbd;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.HojaRutaMethods;
import fragment.ContratoFragment;
import sqlite.model.Contrato;

public class VistaContratos extends AppCompatActivity{

    private ViewPager viewPagerContrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_contratos);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle("CONTRATOS");
        act.setDisplayHomeAsUpEnabled(true);

        viewPagerContrato = (ViewPager) findViewById(R.id.viewpagerVistaContratos);
        ContratoPagerAdapter pagerAdapter = new ContratoPagerAdapter(getSupportFragmentManager(), this);
        viewPagerContrato.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public class ContratoPagerAdapter extends FragmentPagerAdapter {
        Context context;
        //String contratoId = null;

        public ContratoPagerAdapter(FragmentManager fragmentManager, Context context){
            super(fragmentManager);
            this.context = context;
            //this.contratoId = contratoId;
        }

        @Override
        public int getCount(){
            return 1;
        }  //truco para no obtener null

        @Override
        public Fragment getItem(int position){
            Bundle bundle = new Bundle();
            //bundle.putString("contratoId", contratoId);

            //set Fragmentclass Arguments
            ContratoFragment contratoFragment = new ContratoFragment();
            //hrf.setArguments(bundle);
            return contratoFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "CONTRATOS";
        }
    }

    public ArrayList<Contrato> getContratosList(String yearId) {
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        return hrm.getContratosByYear(yearId, this);
    }

    public ArrayList<Contrato> getContratos() {
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        return hrm.getContratos(this);
    }

    public ArrayList<Contrato> getContratosByArrendador(String arrendador){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        return hrm.getContratosByArrendador(arrendador, this);
    }

}
