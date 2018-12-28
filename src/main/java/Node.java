public class Node<E extends Comparable<E>> {

    public Node<E> left, right;
    public int height = 1;
    public E value;

    public Node(E val) {
        this.value = val;
    }
}
