package ui.javaFx.scenes.sceneNewExecution;

import javafx.beans.property.SimpleStringProperty;

public class tableViewModel {
    private SimpleStringProperty Name;
    private SimpleStringProperty Value;

    public tableViewModel(String name, String value) {
       this.Name = new SimpleStringProperty(name);
       this.Value = new SimpleStringProperty(value);
    }

    public String getName() {
        return Name.get();
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public void setName(String name) {
        this.Name.set(name);
    }

    public String getValue() {
        return Value.get();
    }

    public SimpleStringProperty valueProperty() {
        return Value;
    }

    public void setValue(String value) {
        this.Value.set(value);
    }
}
