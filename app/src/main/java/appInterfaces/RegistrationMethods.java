package appInterfaces;

import android.content.Context;
import android.content.Intent;

import sqlite.model.Address;
import sqlite.model.Taxi_driver_account;

/**
 * Created by Usuario on 26/11/2017.
 */

public interface RegistrationMethods {

    String registrarTaxiDriver(Context context, Address adr, Taxi_driver_account tda);

    String generateEncodedToken(String confirmationCode);

    byte[] decodeToken(String token);

    Intent enviarEmail(String[] emailTo, String asunto, String mensaje);

    int getTdaNumber(Context context);

    int getTxdNumber(Context context);

    void activateTaxiDriverAccount(Context context, Taxi_driver_account tda);

    boolean isTdaActivated(Context context, String tdaId);
}
