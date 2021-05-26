package huffman.lists;

import java.util.function.Function;

public class LinkedList<T> {
    public ListNode<T> Primeiro;
    private ListNode<T> ultimo;

    public void Add(T value) {
        ListNode<T> node = new ListNode<T>(value);

        if (Primeiro == null) {
            Primeiro = node;
            return;
        } else if (ultimo == null) {
            ultimo = node;
            Primeiro.Next = node;
        } else {
            ultimo.Next = node;
            ultimo = node;
        }
    }

    public void Remove(ListNode<T> node) {
        if (Primeiro == node) {
            Primeiro = Primeiro.Next;
            return;
        } else {
            ListNode<T> nodeAtual = Primeiro;

            while (nodeAtual.Next != null && nodeAtual.Next != node) {
                nodeAtual = nodeAtual.Next;
            }

            if (nodeAtual.Next == null) {
                return;
            } else if (nodeAtual.Next == ultimo) {
                ultimo = nodeAtual;
                ultimo.Next = null;
            } else {
                nodeAtual.Next = node.Next;
            }

        }
    }

    public ListNode<T> FirstOrDefault(Function<ListNode<T>, Boolean> predicate) {
        ListNode<T> nodeAtual = Primeiro;

        while (nodeAtual != null) {
            if (predicate.apply(nodeAtual)) {
                return nodeAtual;
            }

            nodeAtual = nodeAtual.Next;
        }

        return null;
    }

    public Boolean isEmpty() {
        return Primeiro == null;
    }

    public Boolean hasOnlyOneElement() {
        return Primeiro != null && Primeiro.Next == null;
    }
}
