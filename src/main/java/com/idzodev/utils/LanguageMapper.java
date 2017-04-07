package com.idzodev.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladimir on 22.05.16.
 */
public class LanguageMapper {
    private static final Map<String, String> ua_en;
    private static final Map<String, String> en_ua;
    private static final Map<String, String> commands;
    private static final String fileClassTranslation;


    static {
        commands = new HashMap<>();
        en_ua = new HashMap<>();
        ua_en = new HashMap<>();
        // укр мова
        fileClassTranslation = ".jua";
        initUALanguage();
        initUACommands();
    }

    public static String[] generateLanguageKeywords(){
        int count = commands.keySet().size() * 2;
        String[] kewords = new String[count];
        int i = 0;
        for (String key : commands.keySet()){
            kewords[i] = key;
            i++;
        }

        for (String key : commands.values()){
            kewords[i] = key;
            i++;
        }

        return kewords;
    }

    public static String translate(String text){
        text = text.trim();
        for (String command : commands.keySet()){
            if (text.contains(command))
                text = text.replace(command, commands.get(command));
        }

        char[] chars = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        boolean isLapki = false;
        for (char symbol : chars){
            if (symbol == '\"' || symbol == '\''){
                isLapki = !isLapki;
                builder.append(symbol);
                continue;
            }

            if (!isLapki){
                String tr = ua_en.get(String.valueOf(symbol));
                if (tr == null)
                    tr = String.valueOf(symbol);
                builder.append(tr);
            } else
                builder.append(symbol);
        }

        return builder.toString();
    }

    public static String translateFileName(String text){
        if (text.contains(fileClassTranslation))
            return translateDirName(text.substring(0, text.indexOf(".")))+".java";
        else
            return translateDirName(text);
    }

    public static String translateDirName(String text){
        char[] chars = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char symbol : chars){
            String tr = ua_en.get(symbol+"");
            if (tr == null)
                tr = symbol+"";
            builder.append(tr);
        }
        return builder.toString();
    }

    public static String getFileClassTranslation(){
        return fileClassTranslation;
    }

    public static String getDefaultClassTemplate(String fileName){
        return "\n\n\n" +
                "публічний клас " + fileName + "{\n" +
                "" +
                "}";
    }


    public static String getDefaultInterfaceTemplate(String fileName){
        return "\n\n\n" +
                "публічний інтерфейс " + fileName + "{\n" +
                "" +
                "}";
    }

    private static void initUACommands(){
        commands.put("буловий", "boolean");
        commands.put("байт", "byte");
        commands.put("короткий", "short");
        commands.put("цілий", "int");
        commands.put("вцілий", "long");
        commands.put("дробовий", "float");
        commands.put("вдробовий", "double");
        commands.put("символ", "char");
        commands.put("текст", "String");
        commands.put("публічний", "public");
        commands.put("публічна", "public");
        commands.put("приватний", "private");
        commands.put("приватна", "private");
        commands.put("захищений", "protected");
        commands.put("захищена", "protected");
        commands.put("статичний", "static");
        commands.put("статична", "static");
        commands.put("завершена", "final");
        commands.put("завершений", "final");
        commands.put("клас", "class");
        commands.put("інтерфейс", "interface");
        commands.put("порожньо", "void");
        commands.put("якщо", "if");
        commands.put("інше", "else");
        commands.put("для", "for");
        commands.put("поки", "while");
        commands.put("роби", "do");
        commands.put("спробуй", "try");
        commands.put("злови", "catch");
        commands.put("випадкок", "switch");
        commands.put("при", "case");
        commands.put("нова", "new");
        commands.put("новий", "new");
        commands.put("нул", "null");
        commands.put("зупинити", "break");
        commands.put("продовжити", "continue");
        commands.put("повернути", "return");
        commands.put("підключити", "import");
        commands.put("пакет", "package");
        commands.put("розширити", "extends");
        commands.put("реалізувати", "implements");
        commands.put("абстрактний", "abstract");
        commands.put("абстрактно", "abstract");
        commands.put("абстрактна", "abstract");
        commands.put("підкінець", "finally");
        commands.put("виду", "instanceof");
        commands.put("рідний", "native");
        commands.put("рідний", "native");
        commands.put("рідний", "native");
        commands.put("батько", "super");
        commands.put("синхронний", "synchronized");
        commands.put("синхронно", "synchronized");
        commands.put("синхронна", "synchronized");
        commands.put("цей", "this");
        commands.put("викинути", "throw");
        commands.put("кидки", "throws");
        commands.put("головний", "main");
    }

    private static void initUALanguage(){


        en_ua.put("a","а");
        en_ua.put("b","б");
        en_ua.put("c","с");
        en_ua.put("d","д");
        en_ua.put("e","у");
        en_ua.put("f","ф");
        en_ua.put("g","г");
        en_ua.put("h","х");
        en_ua.put("i","і");
        en_ua.put("j","ж");
        en_ua.put("k","к");
        en_ua.put("l","л");
        en_ua.put("m","м");
        en_ua.put("n","н");
        en_ua.put("o","о");
        en_ua.put("p","п");
        en_ua.put("q","к");
        en_ua.put("r","р");
        en_ua.put("s","с");
        en_ua.put("t","т");
        en_ua.put("u","у");
        en_ua.put("v","в");
        en_ua.put("w","в");
        en_ua.put("x","х");
        en_ua.put("y","й");
        en_ua.put("z","з");


        ua_en.put("a","a");
        ua_en.put("б","b");
        ua_en.put("в","v");
        ua_en.put("г","h");
        ua_en.put("д","d");
        ua_en.put("е","e");
        ua_en.put("ж","j");
        ua_en.put("з","z");
        ua_en.put("ї","yi");
        ua_en.put("й","y");
        ua_en.put("є","ye");
        ua_en.put("и","i");
        ua_en.put("і","i");
        ua_en.put("к","k");
        ua_en.put("л","l");
        ua_en.put("м","m");
        ua_en.put("н","n");
        ua_en.put("о","o");
        ua_en.put("п","p");
        ua_en.put("р","r");
        ua_en.put("с","s");
        ua_en.put("т","t");
        ua_en.put("у","u");
        ua_en.put("ф","f");
        ua_en.put("х","h");
        ua_en.put("ц","ts");
        ua_en.put("ч","ch");
        ua_en.put("ш","sh");
        ua_en.put("щ","sch");
        ua_en.put("ь","");
        ua_en.put("ю","yu");
        ua_en.put("я","ya");

        ua_en.put("А","A");
        ua_en.put("Б","B");
        ua_en.put("В","V");
        ua_en.put("Г","H");
        ua_en.put("Д","D");
        ua_en.put("Е","E");
        ua_en.put("Ж","J");
        ua_en.put("З","Z");
        ua_en.put("Ї","YI");
        ua_en.put("Є","YE");
        ua_en.put("И","I");
        ua_en.put("І","I");
        ua_en.put("К","K");
        ua_en.put("Л","L");
        ua_en.put("М","M");
        ua_en.put("Н","N");
        ua_en.put("О","O");
        ua_en.put("П","P");
        ua_en.put("Р","R");
        ua_en.put("С","S");
        ua_en.put("Т","T");
        ua_en.put("У","U");
        ua_en.put("Ф","F");
        ua_en.put("Х","H");
        ua_en.put("Ц","TS");
        ua_en.put("Ч","CH");
        ua_en.put("Ш","SH");
        ua_en.put("Щ","SCH");
        ua_en.put("Ь","");
        ua_en.put("Ю","YU");
        ua_en.put("Я","YA");
    }
}
