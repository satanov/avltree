import java.util.Arrays;
import java.util.Iterator;

public class MainTree {

    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(11);
        tree.add(12);
        tree.add(13);
        tree.add(14);
        tree.add(9);
        tree.add(8);
        tree.add(7);
        tree.add(6);
        tree.add(5);
        tree.add(5);
        tree.print();

        Iterator<Integer> it = tree.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println("\nretainAll(Arrays.asList(10, 12, 13, 20, 11, 7, 5)");
        tree.retainAll(Arrays.asList(10, 12, 13, 20, 11, 7, 5));
        tree.print();
        Integer[] arr = new Integer[tree.size()];
        tree.toArray(arr);
        System.out.println(Arrays.toString(arr));
    }

}
