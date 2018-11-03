package com.example.usuario.testingbd;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Patterns;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.usuario.testingbd", appContext.getPackageName());
    }

    @Test
    public void testPatternsProfileData(){
        String data = "Avenida Padre Piquer, 20. Piso 7A, Portal 1/b";
        assertTrue(isValidData(data));
    }

    private final static boolean isValidData(String enteredData){
        return Pattern.compile("[a-zA-ZñÑáéíóúÁÉÍÓÚ_0-9 \\t\\n\\x0B\\f\\r ,.:;@#~/-_<>!?]*").matcher(enteredData).matches();
    }

    @Test
    public void testEmailAddresses(){
        String email = "alexguti9";
        String email2 = "alex@gmail.com";
        String email3 = "alex@hotmail.com";
        String email4 = "alex@gmailcom";
        assertFalse(Patterns.EMAIL_ADDRESS.matcher(email).matches());
        assertTrue(Patterns.EMAIL_ADDRESS.matcher(email2).matches());
        assertTrue(Patterns.EMAIL_ADDRESS.matcher(email3).matches());
        assertFalse(Patterns.EMAIL_ADDRESS.matcher(email4).matches());
    }


}
