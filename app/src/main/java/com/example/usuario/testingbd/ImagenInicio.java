package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import appImplementations.CommonMethodsImplementation;
import appImplementations.RegistrationMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.RegistrationMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Taxi_Driver;
import sqlite.repo.Taxi_driver_accountDML;

public class ImagenInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_inicio);

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 1000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    RegistrationMethods rm = new RegistrationMethodsImplementation();
                    String txdId = getTxdId();
                    if(txdId == null){
                        Intent i = new Intent(ImagenInicio.this.getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        String tdaId = getTdaId(txdId);
                        boolean activated = rm.isTdaActivated(ImagenInicio.this.getApplicationContext(), tdaId);
                        if(activated){
                            //Handler h = new Handler();
                            //   h.postDelayed(new Runnable() {
                            //    public void run() {
                            Intent i = new Intent(ImagenInicio.this.getApplicationContext(), MenuPrincipal.class);
                            startActivity(i);
                            finish();
                            //     }
                            //   }, 0);
                        }
                        else{
                            Intent i = new Intent(ImagenInicio.this.getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                }
            }
        };
        splashThread.start();
    }

    private String getTxdId(){
        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(this);
        if(txd == null){
            return null;
        }
        else{
            return String.valueOf(txd.getTxd_id());
        }
    }

    private String getTdaId(String txdId){
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        int tda_id = tdaDml.getTdaIdByTxdId(new DatabaseHelper(this),
                txdId, this);
        return String.valueOf(tda_id);
    }
}
