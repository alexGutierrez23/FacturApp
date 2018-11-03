package appImplementations;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.widget.Toast;

import java.util.Random;

import appInterfaces.RegistrationMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.AddressDML;
import sqlite.repo.Taxi_DriverDML;
import sqlite.repo.Taxi_driver_accountDML;

/**
 * Created by Usuario on 26/11/2017.
 */

public class RegistrationMethodsImplementation implements RegistrationMethods {

    public String registrarTaxiDriver(Context context, Address adr, Taxi_driver_account tda){
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(context);
        AddressDML adrDml = new AddressDML(context);

        String adrId = adrDml.insertarAddress(new DatabaseHelper(context), adr, context);

        tda.setTda_adr_id(adrId);
        tda.setTda_enabled("1");
        String tdaId = tdaDml.insertarTaxiDriverAccount(new DatabaseHelper(context), tdaDml, tda, context);
        String txdId = null;
        int tda_id = Integer.parseInt(tdaId);
        if(tda_id != 0 && tda_id != -1){
            String confirmationCode = generateConfirmationCode();
            String confirmationCodeEncoded = generateEncodedToken(confirmationCode);
            Taxi_DriverDML txdDml = new Taxi_DriverDML(context);
            txdId = txdDml.insertarTaxiDriver(new DatabaseHelper(context), tda.getTda_email(),
                    confirmationCode, confirmationCodeEncoded, tda_id, context);
            int txd_id = Integer.parseInt(txdId);
            if(txd_id == -1 || txd_id == 0){
                Toast.makeText(context, "No se pudo registrar su perfil. " +
                                "Contacte con el administrador por email (alexguti9@gmail.com) o móvil (659 785 158)."
                        , Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, "Su perfil se ha registrado.", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(context, "No se pudo registrar su perfil. " +
                            "Contacte con el administrador por email (alexguti9@gmail.com) o móvil (659 785 158)."
                    , Toast.LENGTH_LONG).show();
        }
        return txdId;
    }

    private String generateConfirmationCode(){
        String upperLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerLetters = "abcdefghijklmnopqrstuvwzyz";
        String symbols = "_#!?-%&*%£$€";
        String numbers = "1234567890";
        StringBuilder sbPassword = new StringBuilder();
        Random rnd = new Random();
        while (sbPassword.length() < 24) { // length of the random string.
            int upperLettersIndex = (int) (rnd.nextFloat() * upperLetters.length());
            int lowerLettersIndex = (int) (rnd.nextFloat() * lowerLetters.length());
            int symbolsIndex = (int) (rnd.nextFloat() * symbols.length());
            int numbersIndex = (int) (rnd.nextFloat() * numbers.length());

            sbPassword.append(upperLetters.charAt(upperLettersIndex));
            sbPassword.append(lowerLetters.charAt(lowerLettersIndex));
            sbPassword.append(symbols.charAt(symbolsIndex));
            sbPassword.append(numbers.charAt(numbersIndex));
        }

        return sbPassword.toString();
    }

    public String generateEncodedToken(String confirmationCode){
        return Base64.encodeToString(confirmationCode.getBytes(), Base64.DEFAULT);
    }

    public byte[] decodeToken(String token){
        return Base64.decode(token, Base64.DEFAULT);
    }

    public Intent enviarEmail(String[] emailTo, String asunto, String mensaje){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTo);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

        emailIntent.setType("message/rfc822");
        return emailIntent;
    }



    public int getTdaNumber(Context context){
        Taxi_driver_accountDML tda = new Taxi_driver_accountDML(context);
        return tda.getTaxiDriverAccountCount(new DatabaseHelper(context), context);
    }

    public int getTxdNumber(Context context){
        Taxi_DriverDML txd = new Taxi_DriverDML(context);
        return txd.getTaxiDriverCount(new DatabaseHelper(context), context);
    }

    public void activateTaxiDriverAccount(Context context, Taxi_driver_account tda){
        Taxi_driver_accountDML taxi_driver_accountDML = new Taxi_driver_accountDML(context);
        taxi_driver_accountDML.update(tda, new DatabaseHelper(context).getWritableDatabase());
    }

    public boolean isTdaActivated(Context context, String tdaId){
        boolean activated = false;
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(context);
        return tdaDml.isActivated(context, new DatabaseHelper(context).getWritableDatabase(), tdaId);
    }

}
