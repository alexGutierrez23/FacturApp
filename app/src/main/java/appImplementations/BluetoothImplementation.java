package appImplementations;

import android.text.TextUtils;

import java.util.ArrayList;

import appInterfaces.Bluetooth;

/**
 * Created by Usuario on 17/01/2018.
 */

public class BluetoothImplementation implements Bluetooth {
    // android built in classes for bluetooth operations

    public String anyadirCero(String pr1){
        if(pr1.charAt(0) == '.'){
            pr1 = "0" + pr1;
        }
        return pr1;
    }

    public ArrayList<String> maximumTam(String st){
        ArrayList<String> as = new ArrayList<>();
        String s = "";
        for (int i = 0; i < st.length(); i++) {
            if ((st.charAt(i) != ' ')){
                s = s + st.charAt(i);
            } else {
                as.add(s + st.charAt(i));

                s = "";
            }
        }
        as.add(s);
        int total = 0;
        String str = "";
        ArrayList<String> ast = new ArrayList<>();
        for (int i = 0; i < as.size(); i++) {
            //total = total + a.length();
            total = total + as.get(i).length();
            if(total <= 25){
                str = str + as.get(i) + " ";
            }
            else{
                ast.add(str);
                str = "";
                total = 0;
                i = i - 1;
            }
        }
        ast.add(str);
        return ast;
    }

    public String reemplazar(String str){
        return str.replaceAll("\\. ", "\\.\n");
    }

    public String reemplaza_Char(String st) {
        st =  st.replace("ñ", "n");
        st = st.replace("Ñ", "N");
        //st = st.replace("º", Character.toString((char)167));
        //st = st.replace("ª", Character.toString((char)166));
        st = st.replace("á", "a");
        //Character.toString((char)160))
        //st = st.replace("é", Character.toString((char)130));
        //st = st.replace("í", Character.toString((char)161));
        //st = st.replace("ó", Character.toString((char)162));
        //st = st.replace("ú", Character.toString((char)163));
        st = st.replace("é", "e");
        st = st.replace("í","i");
        st = st.replace("ó", "o");
        st = st.replace("ú", "u");
        st = st.replace(Character.toString((char)194), "");
        //st = st.replace("€",  "\u20ac");
        st = st.replace("Á", "A");
        st = st.replace("É", "E");
        st = st.replace("Í", "I");
        st = st.replace("Ó", "O");
        st = st.replace("Ú", "U");
        return st;
    }

    public String comprobarNull(String st){
        String sol = "";
        if(TextUtils.isEmpty(st)){
            sol = " ";
        }
        else
            sol = sol + st;
        return sol;
    }

}
