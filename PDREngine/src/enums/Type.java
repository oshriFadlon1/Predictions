package enums;

import java.nio.charset.StandardCharsets;

public enum Type {
    DECIMAL{
        int getValue(String value) {return Integer.parseInt(value);}
    },
    FLOAT{
        float getValue(String value){return Float.parseFloat(value);}
    },
    BOOLEAN{
        boolean getValue(String value){return value.toLowerCase().equals("true")||value.toLowerCase().equals("False");}
    },
    STRING{
        String  getValue(String value) {return value;}
    };

    public Integer convertInt(Object propValue) {
        return (Integer)propValue;
    }
    public Float convertFloat(Object propValue)
    {
        return (float)propValue;
    }
    public Boolean convertBoolean(Object propValue)
    {
        return (boolean)propValue;
    }
    public String convertString(Object propValue)
    {
        return (String)propValue;
    }
}
