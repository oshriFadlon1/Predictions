package enums;

public enum Type {
    DECIMAL{
        int getValue(Object value) {return (int)value;}
    },
    FLOAT{
        float getValue(Object value){return (float)value;}
    },
    BOOLEAN{
        boolean getValue(Object value){return (boolean)value;}
    },
    STRING{
        String  getValue(Object value) {return (String)value;}
    }

}
