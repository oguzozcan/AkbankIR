package com.mallardduckapps.akbankir.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit2.Converter;


/**
 * Created by oguzemreozcan on 28/03/16.
 */
public class StringConverter {//implements Converter {
//    @Override
//    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
//        String text = null;
//        try {
//            text = fromStream(typedInput.in());
//        } catch (IOException ignored) {/*NOP*/ }
//        return text;
//    }
//
//    @Override
//    public TypedOutput toBody(Object o) {
//        return null;
//    }

    public static String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }
}
