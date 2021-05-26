package huffman;

public class Node<T> {
    public T Value;
    public int Count;
    public Node<T> Rigth;
    public Node<T> Left;

    public Node(T value){
        Value = value;
        Count = 1;
    }

    public Node(T value, int count){
        Value = value;
        Count = count;
    }

    public Boolean isLeaf(){
        return Rigth == null && Left == null;
    }
}
