package pebble.ast.node;

import pebble.run.environment.Environment;
import pebble.run.environment.NestedEnvironment;

import java.util.List;

public class IfStatement extends ASTList {
    public IfStatement(List<ASTree> c) { super(c); }

    public Environment makeEnv(Environment env){
        return new NestedEnvironment(env);
    }

    public ASTree condition() { return child(0); }
    public ASTree thenBlock() { return child(1); }
    public List<ASTree> elseBlock() {
        return numChildren() > 2 ? subChildren(2,numChildren()) : null;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(if " + condition() + " " + thenBlock());
        for(int i = 2; i< numChildren();i++){
            // 该block有两个child, 说明一个是condition, 一个是elseBlock, 即这是elif的情况
            if(child(i).numChildren() ==2){
                sb.append(" else (if " + child(i).child(0) + " " + child(i).child(1));
            }
            if(i == numChildren() -1){
                if(child(i).numChildren() == 1){
                    sb.append(" else ("+child(i).child(0)+")");
                }
                for (int j = 0; j < numChildren() - 2; j++) {
                    sb.append(")");
                }
            }
        }

        return sb.toString();
    }
}
