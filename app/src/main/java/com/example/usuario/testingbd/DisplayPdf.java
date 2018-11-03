package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

import Constants.Constants;
import appImplementations.HojaRutaMethodsImplementation;
import appImplementations.InvoiceMethodsImplementation;
import appImplementations.PresupuestoMethodsImplementation;
import appImplementations.TicketMethodsImplementation;
import appInterfaces.HojaRutaMethods;
import appInterfaces.InvoiceMethods;
import appInterfaces.PresupuestoMethods;
import appInterfaces.TicketMethods;
import sqlite.model.HojaRuta;
import sqlite.model.Invoice;
import sqlite.model.Presupuesto;
import sqlite.model.Ticket;

public class DisplayPdf extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener{

    private PDFView pdfView;
    private TextView tituloPdf;
    private Integer pageNumber = 0;
    private String pdfFileName;
    private String docId = null;
    private String idType = null;  // Esto sera ticket, factura, etc.

    private boolean atrasHojaRuta = false;
    private HojaRuta hr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pdf);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);

        tituloPdf = (TextView) findViewById(R.id.tituloPdf);

        Bundle formulario = this.getIntent().getExtras();
        docId = formulario.getString("var_docId");
        idType = formulario.getString("var_idType");

        String file = null;
        Invoice invoice = null;
        Ticket tck = null;
        Presupuesto pre = new Presupuesto();

        Constants c = new Constants();
        if(idType == null || TextUtils.isEmpty(idType)){
            Toast.makeText(this, "Esta factura no existe.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(idType.equals(c.FACTURA_TYPE)){
                InvoiceMethods im = new InvoiceMethodsImplementation();
                file = im.getInvoicePath(Integer.parseInt(docId), this);
                invoice = im.getInvoice(Integer.parseInt(docId), this);
                tituloPdf.setText(invoice.getInv_numFactRed());
                act.setTitle(invoice.getInv_numFactRed());
            }
            else if(idType.equals(c.TICKET_TYPE)){
                TicketMethods tm = new TicketMethodsImplementation();
                file = tm.getTicketPath(Integer.parseInt(docId), this);
                tck = tm.getTicket(docId, this);
                tituloPdf.setText(tck.getTck_numTickRed());
                act.setTitle(tck.getTck_numTickRed());
            }
            else if(idType.equals(c.PRESUPUESTO_TYPE)){
                PresupuestoMethods pm = new PresupuestoMethodsImplementation();
                file = pm.getPresupuestoPath(Integer.parseInt(docId), this);
                pre = pm.getPresupuesto(Integer.parseInt(docId), this);
                tituloPdf.setText(pre.getPre_numPresupuestoRed());
                act.setTitle(pre.getPre_numPresupuestoRed());
            }
            else if(idType.equals(c.RENDIMIENTO_TICKET_TYPE)){
                TicketMethods tm = new TicketMethodsImplementation();
                file = tm.getTicketPath(Integer.parseInt(docId), this);
                tck = tm.getTicket(docId, this);
                tituloPdf.setText(tck.getTck_numTickRed());
                act.setTitle(tck.getTck_numTickRed());
            }
            else if(idType.equals(c.RENDIMIENTO_FACTURA_TYPE)){
                InvoiceMethods im = new InvoiceMethodsImplementation();
                file = im.getInvoicePath(Integer.parseInt(docId), this);
                invoice = im.getInvoice(docId, this);
                tituloPdf.setText(invoice.getInv_numFactRed());
                act.setTitle(invoice.getInv_numFactRed());
            }
            else if(idType.equals(c.HOJARUTA_TYPE)){
                HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                file = hrm.getHojaRutaPath(Integer.parseInt(docId), this);
                hr = new HojaRuta();
                hr = hrm.getHojaRuta(docId, this);
                tituloPdf.setText(hr.getHjr_alias());
                act.setTitle(hr.getHjr_alias());
                atrasHojaRuta = true;
            }
            else if(idType.equals("PDF_GUIA")){
                tituloPdf.setText(docId);
                act.setTitle(docId);
                //displayFromAsset(docId);
                String guiaUso = new File(Environment.getExternalStorageDirectory()
                        + File.separator + c.NOMBRE_CARPETA_APP + File.separator
                        + c.GUIAS + File.separator + docId).getAbsolutePath();
                file = guiaUso;
            }
        }
        if(file == null || TextUtils.isEmpty(file)){
            finish();
            Log.e("Ruta factura: ", "No existe esta factura");
            Toast.makeText(this, "Esta factura no existe.", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.e("Ruta factura: ", file);
            pdfView = (PDFView) findViewById(R.id.pdf);
            displayPdf(file);
        }
    }

    private void displayPdf(String fileName) {
        pdfFileName = fileName;
        File file = new File(fileName);
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(DisplayPdf.class.getSimpleName(), String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
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
        Intent intent = null;
        if(atrasHojaRuta){
            intent = new Intent(this, com.example.usuario.testingbd.HojaRuta.class);
            intent.putExtra("contratoId", hr.getHjr_con_id());
        }
        else{
            intent = new Intent(this, MenuPrincipal.class);
        }
        startActivity(intent);
        finish();
    }

}
