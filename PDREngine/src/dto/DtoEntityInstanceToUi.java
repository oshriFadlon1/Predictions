package dto;

import java.util.Map;

public class DtoEntityInstanceToUi {
    private int id;
    private Map<String, Object> nameOfProperty2Value;

    public DtoEntityInstanceToUi(int id, Map<String, Object> valueOfPropertyInstance) {
        this.id = id;
        this.nameOfProperty2Value = valueOfPropertyInstance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Map<String, Object> getNameOfProperty2Value() {
        return nameOfProperty2Value;
    }

    public void setNameOfProperty2Value(Map<String, Object> nameOfProperty2Value) {
        this.nameOfProperty2Value = nameOfProperty2Value;
    }
}
