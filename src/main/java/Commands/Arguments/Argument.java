package Commands.Arguments;

public class Argument <E>{
    public String argName;
    public String description;

    private E value;

    public Argument(String name, String description){
        argName = name;
        this.description = description;
    }

    public void setValue(E value){
        this.value = value;
    }

    public E getValue(){
        return value;
    }
}