package API;

public class Parameter {
    private String parameter;
    private String value;

    public Parameter(String parameter, String value){
        this.parameter = parameter;
        this.value = value;
    }

    public String getParameter(){
        return parameter;
    }

    public String getValue() {
        return value;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
