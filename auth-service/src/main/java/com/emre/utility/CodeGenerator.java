package com.emre.utility;

import java.util.UUID;

//Kayıt olan kullanıcıya bir aktivasyon kodu dönecektir.
//Bu kod ile kullanıcı giriş yapabilir. Olmazsa hata verecektir.
//CodeGenerator sınıfıda yanlızca bu kodun üretilmesinden sorumludur.
public class CodeGenerator {
    public static String generateCode() {
        String code = UUID.randomUUID().toString();
        String[] data = code.split("-");
        String newCode = "";
        for (String str : data) {
            newCode += str.charAt(0);
        }
        return newCode;
    }
}
