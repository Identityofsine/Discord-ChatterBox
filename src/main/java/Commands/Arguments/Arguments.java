package Commands.Arguments;

import java.util.LinkedList;

public class Arguments <E>{
    public LinkedList<Argument<E>> args = new LinkedList<Argument<E>>();

    public boolean addArgs(Argument<E> arg){
        return args.add(arg);
    }
}

