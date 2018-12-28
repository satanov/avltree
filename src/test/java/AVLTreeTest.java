import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AVLTreeTest {

    private static Method height;
    private Set<Integer> tree;

    @BeforeClass
    public static void initClass() throws Exception {
        height = AVLTree.class.getDeclaredMethod("height", null);
        height.setAccessible(true);
    }

    @Before
    public void init() {
        tree = new AVLTree<>();
        IntStream.range(1, 11).forEach(i -> tree.add(i));
    }

    @Test
    public void testTreeHeight() throws ReflectiveOperationException {
        assertEquals(height.invoke(tree), 4);
        IntStream.range(11, 16).forEach(i -> tree.add(i));
        assertEquals(height.invoke(tree), 4);
        tree.add(17);
        assertEquals(height.invoke(tree), 5);
        tree.clear();
        assertEquals(height.invoke(tree), 0);
        tree.add(1);
        assertEquals(height.invoke(tree), 1);
    }

    @Test
    public void testAdd() {
        assertEquals(tree.size(), 10);
        assertFalse(tree.add(1));
        assertTrue(tree.add(11));
        assertEquals(tree.size(), 11);

        tree.addAll(Arrays.asList(11, 12, 13));
        assertEquals(tree.size(), 13);
    }

    @Test
    public void testContains() {
        assertTrue(tree.contains(1));
        assertFalse(tree.contains(11));

        assertTrue(tree.containsAll(Arrays.asList(1, 2, 3, 4, 5)));
        assertFalse(tree.containsAll(Arrays.asList(1, 2, -3, 4, 5)));
    }

    @Test
    public void testRemove() {
        assertTrue(tree.remove(1));
        assertTrue(tree.remove(2));
        assertTrue(tree.remove(3));
        assertTrue(tree.remove(10));
        assertFalse(tree.contains(3));
        assertFalse(tree.remove(11));
        assertEquals(tree.size(), 6);

        tree.removeAll(Arrays.asList(7, 8));
        assertEquals(tree.size(), 4);
    }

    @Test
    public void testToArray() {
        Object[] arr = tree.toArray();
        assertEquals(arr.length, 10);
        Arrays.asList(arr).containsAll(IntStream.range(1, 11).boxed().collect(Collectors.toList()));

        Integer[] intArr = new Integer[10];
        tree.toArray(intArr);
        assertTrue(Arrays.equals(arr, intArr));
    }


    @Test
    public void testRetainAll() {
        List<Integer> toRetain = Arrays.asList(1, 3, 5, 7, 9);
        tree.retainAll(toRetain);
        assertTrue(tree.containsAll(toRetain));
        assertFalse(tree.contains(2));
        assertFalse(tree.contains(4));
    }

    @Test
    public void testClear() {
        assertEquals(tree.size(), 10);
        tree.clear();
        assertEquals(tree.size(), 0);
    }

    @Test
    public void testIterator() {
        List<Integer> treeIntegers = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        int iteratorCounter = 0;
        for (Integer i : tree) {
            iteratorCounter++;
            assertTrue(treeIntegers.contains(i));
        }
        assertEquals(iteratorCounter, 10);
    }
}
