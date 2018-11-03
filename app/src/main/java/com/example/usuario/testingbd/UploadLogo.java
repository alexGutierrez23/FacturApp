package com.example.usuario.testingbd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import appImplementations.CommonMethodsImplementation;
import appInterfaces.CommonMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.Taxi_driver_accountDML;

public class UploadLogo extends AppCompatActivity {

    private TextView error;
    private Button cancelar, confirmar, subirLogo;
    private ImageView verLogo;

    private byte[] logo = null;
    private byte[] logoFromDb = null;
    private Bitmap bitmapLogo = null;
    private Uri selectedImage = null;
    private String logoPath = null;
    private String logoPathFromDb = null;
    private HashMap<String, byte[]> logoFullInfo = null;  // logo path and image stored in this map

    public static final int GET_FROM_GALLERY = 1;

    private Taxi_driver_account tda = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_logo);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_uploadLogoImage);
        act.setDisplayHomeAsUpEnabled(true);

        confirmar = (Button) findViewById(R.id.confirmarLogo);
        cancelar = (Button) findViewById(R.id.cancelarLogo);
        subirLogo = (Button) findViewById(R.id.uploadLogo);
        error = (TextView) findViewById(R.id.errorUploadLogo);
        verLogo = (ImageView) findViewById(R.id.viewLogo);

        verLogo.setVisibility(View.GONE);

        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(this);
        String tdaId = cm.getTdaIdGivenTxdId(this, String.valueOf(txd.getTxd_id()));
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        tda = tdaDml.getTdaById(new DatabaseHelper(this), tdaId, this);
        if(tda.getTda_logo() != null){
            if(tda.getTda_logo_path() != null && !tda.getTda_logo_path().isEmpty()){
                logoFullInfo = cm.getFullLogo(tdaId, this);
            }
        }
        if(logoFullInfo != null){
            logoPathFromDb = logoFullInfo.keySet().iterator().next();  //Solo hay un valor guardado en el mapa asi que podemos coger el valor directamente
            logoFromDb = logoFullInfo.get(logoPathFromDb);
            if(logoFromDb != null){
                verLogo.setVisibility(View.VISIBLE);
                Bitmap image = cm.getImage(logoFromDb);
                verLogo.setImageBitmap(image);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MenuPrincipal.class);
        startActivity(i);
        finish();
    }

    public void uploadLogo(View v) {
        //PRIMER INTENTO FALLIDO
        /*Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GET_FROM_GALLERY);

        if(bitmapLogo != null && logoPath != null){
            verLogo.setVisibility(View.VISIBLE);
            verLogo.setImageBitmap(BitmapFactory.decodeFile(logoPath));
            Toast.makeText(this, getString(R.string.success_subirImagen), Toast.LENGTH_SHORT).show();
        } */

        Intent i = new Intent(Intent.ACTION_PICK);
        File directorioImagenes = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String rutaDirectorio = directorioImagenes.getPath();
        Uri uri = Uri.parse(rutaDirectorio);

        //Cogemos todos los tipos de imagen
        i.setDataAndType(uri, "image/*");
        startActivityForResult(i, GET_FROM_GALLERY);
        Toast.makeText(this, getString(R.string.info_subirImagen), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            logoPath = selectedImage.getPath();
            try {
                //bitmapLogo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                InputStream is = getContentResolver().openInputStream(selectedImage);
                bitmapLogo = BitmapFactory.decodeStream(is);

                if(bitmapLogo != null && logoPath != null){
                    //verLogo.setImageBitmap(BitmapFactory.decodeFile(logoPath));
                    verLogo.setImageBitmap(bitmapLogo);
                    verLogo.setVisibility(View.VISIBLE);

                    CommonMethods cm = new CommonMethodsImplementation();
                    logo = cm.getBytes(bitmapLogo);
                }
            } catch (IOException e) {
                Log.e("Upload image", "FNFE: ", e.getCause());
                Toast.makeText(this, "No se puede cargar la imagen", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void confirmarLogo(View v){
        if(logo == null){
            if(logoFromDb == null) {
                error.setVisibility(View.VISIBLE);
                error.setText(R.string.error_subirImagen);
            }
            else{
                error.setVisibility(View.GONE);

                Intent i = new Intent(this, MenuPrincipal.class);
                startActivity(i);
                finish();
                Toast.makeText(this, getString(R.string.success_subirImagen), Toast.LENGTH_LONG).show();
            }
        }
        else{
            error.setVisibility(View.GONE);

            CommonMethods cm = new CommonMethodsImplementation();
            tda.setTda_logo_path(logoPath);
            tda.setTda_logo(logo);
            cm.updateTaxiDriverAccount(tda, this);

            Intent i = new Intent(this, MenuPrincipal.class);
            startActivity(i);
            Toast.makeText(this, getString(R.string.success_subirImagen), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void cancelarLogo(View v){
        logoPath = null;
        logo = null;
        logoFromDb = null;
        logoPathFromDb = null;

        Intent i = new Intent(this, MenuPrincipal.class);
        startActivity(i);
        finish();
    }
}
