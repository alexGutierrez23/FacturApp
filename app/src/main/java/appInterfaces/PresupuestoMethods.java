package appInterfaces;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sqlite.model.Address;
import sqlite.model.Information;
import sqlite.model.Presupuesto;
import sqlite.model.Trip;

/**
 * Created by Usuario on 16/01/2018.
 */

public interface PresupuestoMethods {
    /** Crear e insertar factura nueva y devolver su ID.
     *
             * @param context
     * @param presupuesto
     * @param tdaId
     * @param tckId
     * @param cusId
     * @return Presupuesto ID inserted.
     */
    String createPresupuesto(Context context, Presupuesto presupuesto, String tdaId, String tckId, String cusId, String lastPresupuestoNumber);

    void deletePresupuesto(Context context, Presupuesto presupuesto);

    String createInformation(Information information, String presupuestoId, Trip trip, Context context);

    String createTrip(Trip trip, Context context);

  //  String createCustomer(Customer customer, Address address, Context context);

    String createAddress(Address address, Context context);

    int getTotalInsertedRows(SQLiteDatabase db, String tableName);

    String buildPresupuestoRed(String date, Presupuesto presupuesto, int yeaId, Context context);

    Presupuesto getPresupuesto(int presupuestoId, Context context);

    ArrayList<Information> getInformationLinkedToPresupuestoId(String invId, Context context);

    Trip getTrip(String trpId, Context context);

    void borrarPresupuesto(String presupuestoId, Context context);

    void uploadPdfPresupuesto(String presupuestoId, String ruta, Context context);

    String getPresupuestoPath(int preId, Context context);

    void updateInformationForPresupuesto(ArrayList<String> infoIds, String presupuestoId, Context context);

  //  public Customer getCustomerByPresupuestoId(String preId, Context context);
}
