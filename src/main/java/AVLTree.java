import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class AVLTree<E extends Comparable<E>> implements Set<E> {

    private Node<E> root;
    private int size = 0;

    // Метод для получения высоты узла
    private int height(Node<E> node) {
        return node == null ? 0 : node.height;
    }
    private int height() {
        return height(root);
    }

    // Метод для добавления узла
    private Node<E> insert(Node<E> node, E value) {
        // Если узел = null - это то место, куда нужно добавить новые элемент
        if (node == null) {
            // При добавлении увеличиваем размер дерева
            size++;
            return (new Node<E>(value));
        }
        // В зависимсоти от значения идем в нужную часть дерева
        if (value.compareTo(node.value) < 0) {
            node.left = insert(node.left, value);
        } else if (value.compareTo(node.value) > 0) {
            node.right = insert(node.right, value);
        }
        // Увеличиваем высоту поддерева
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        // Считаем текущее значение баланса
        int balance = getBalance(node);
        // Если дерево несбалансировано - имеем 4 варианта для балансирования
        // Left Left Case
        if (balance > 1 && value.compareTo(node.left.value) < 0) {
            return rightRotate(node);
        }
        // Right Right Case
        if (balance < -1 && value.compareTo(node.right.value) > 0) {
            return leftRotate(node);
        }
        // Left Right Case
        if (balance > 1 && value.compareTo(node.left.value) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // Right Left Case
        if (balance < -1 && value.compareTo(node.right.value) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    // Правый поворот
    private Node<E> rightRotate(Node<E> y) {
        Node<E> x = y.left;
        Node<E> T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    // Левый поворот
    private Node<E> leftRotate(Node<E> x) {
        Node<E> y = x.right;
        Node<E> T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    // Баланс для текущего поддерева
    private int getBalance(Node<E> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    public void preOrder(Consumer<E> consumer) {
        preOrder(root, consumer);
    }
    // Обход по типу левое поддерево - текущее значение - правое поддерево
    // Передаем сюда Косьюмер, который будет выполняться
    private void preOrder(Node<E> current, Consumer<E> consumer) {
        if (current != null) {
            preOrder(current.left, consumer);
            consumer.accept(current.value);
            preOrder(current.right, consumer);
        }
    }

    // Аналогично предыдущему, только: правое поддерево - текущее значение - левое поддерево
    private void postOrder(Consumer<E> consumer) {
        postOrder(root, consumer);
    }
    private void postOrder(Node<E> current, Consumer<E> consumer) {
        if (current != null) {
            postOrder(current.right, consumer);
            consumer.accept(current.value);
            postOrder(current.left, consumer);
        }
    }

    // Метод для получения самого маленького значения
    private Node<E> minValueNode(Node<E> node) {
        Node<E> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Рекурсивный метод для удаления узла
    private Node<E> deleteNode(Node<E> root, E value) {
        if (root == null) {
            return root;
        }

        // Переходим в нужное поддерево в зависимости от удаляемого значения
        // Если текущее значение равно удаляемому - выполняем удаление
        if (value.compareTo(root.value) < 0) {
            root.left = deleteNode(root.left, value);
        } else if (value.compareTo(root.value) > 0) {
            root.right = deleteNode(root.right, value);
        } else {
            size--;
            // Удаления для узла без дочерних или с одним дочерним узлом
            if ((root.left == null) || (root.right == null)) {
                Node<E> temp = root.left != null ? root.left : root.right;
                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    root = temp;
                }
                temp = null;
            } else {
                // Случай с 2мя дочерними узлами
                Node<E> temp = minValueNode(root.right);
                root.value = temp.value;
                root.right = deleteNode(root.right, temp.value);
            }
        }

        if (root == null) {
            return root;
        }
        // Обновляем высоту
        root.height = Math.max(height(root.left), height(root.right)) + 1;

        // Балансировка аналогичная той, что была при добавлении
        int balance = getBalance(root);
        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }
        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }
        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    public void print() {
        print(root);
    }
    // Метод для визуализации дерева
    private void print(Node<E> root) {
        if (root == null) {
            System.out.println("(XXXXXX)");
            return;
        }
        int height = root.height;
        int width = (int) Math.pow(2, height - 1);
        List<Node<E>> current = new ArrayList<>(1);
        List<Node<E>> next = new ArrayList<>(2);
        current.add(root);

        final int maxHalfLength = 4;
        int elements = 1;

        StringBuilder sb = new StringBuilder(maxHalfLength * width);
        for (int i = 0; i < maxHalfLength * width; i++)
            sb.append(' ');
        String textBuffer;

        for (int i = 0; i < height; i++) {
            sb.setLength(maxHalfLength * ((int) Math.pow(2, height - 1 - i) - 1));
            textBuffer = sb.toString();
            for (Node<E> n : current) {
                System.out.print(textBuffer);
                if (n == null) {
                    System.out.print("        ");
                    next.add(null);
                    next.add(null);
                } else {
                    System.out.printf("(%6s)", n.value.toString());
                    next.add(n.left);
                    next.add(n.right);
                }
                System.out.print(textBuffer);
            }
            System.out.println();
            if (i < height - 1) {
                for (Node n : current) {
                    System.out.print(textBuffer);
                    if (n == null)
                        System.out.print("        ");
                    else
                        System.out.printf("%s      %s",
                                n.left == null ? " " : "/", n.right == null ? " " : "\\");
                    System.out.print(textBuffer);
                }
                System.out.println();
            }
            elements *= 2;
            current = next;
            next = new ArrayList<>(elements);
        }
    }

    // Метод для поиска элемента в дерева
    private boolean contains(Node<E> current, E value) {
        // В зависимости от значения узла переходим в нужное поддерево и ищем элемент
        if (current == null) {
            return false;
        } else if (current.value.compareTo(value) < 0) {
            return contains(current.right, value);
        } else if (current.value.compareTo(value) > 0) {
            return contains(current.left, value);
        } else {
            return true;
        }
    }

    // Возвращает размер дерева
    @Override
    public int size() {
        return size;
    }

    // Проверяет пустое ли дерево
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Вызывает рекурсивный метод contains
    @Override
    public boolean contains(Object o) {
        return contains(root, (E)o);
    }

    // Вовзращает кастомный итератор для дерева
    // Возвращает отсортированые по возрастанию элементы
    @Override
    public Iterator<E> iterator() {
        return new TreeIterator<>();
    }

    // Возвращает массив всех элементов дерева
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        preOrder((item) -> result[counter.getAndSet(counter.get() + 1)] = item);
        return result;
    }

    // Аналогично предыдущему, но уже вовзращает массив типа E
    // Если массив-аргумент имеет подходящую длину - апдейтится он
    @Override
    public <E> E[] toArray(E[] a) {
        E[] result = a.length >= size ? a : (E[]) Array.newInstance(a.getClass().getComponentType(), size);
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        preOrder(item -> result[counter.getAndSet(counter.get() + 1)] = (E) item);
        return result;
    }

    // добавление в дерево
    @Override
    public boolean add(E e) {
        if (contains(root, e)) {
            return false;
        }
        root = insert(root, e);
        return true;
    }

    // удаление с дерева
    @Override
    public boolean remove(Object o) {
        if (contains(root, (E)o)) {
            root = deleteNode(root, (E)o);
            return true;
        }
        return false;
    }

    // Првоерка на то, есть ли все элементы коллекции в дереве
    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    // Добавлении коллекции элементов
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return c.stream().filter(this::add).count() == c.size();
    }

    // Оставляет в дереве только те элементы, которые были в переданой коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        forEach((item) -> {
            if (!c.contains(item)) {
                this.remove(item);
            }
        });
        return true;
    }

    // Удаляет все элементы с коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        return c.stream().filter(this::remove).count() == c.size();
    }

    // Очищает дерево
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Итератор для дерева
    // Создаем стек на основе дерева о работаем с ним
    public class TreeIterator<E extends Comparable<E>> implements Iterator<E> {
        private Stack<E> stack;

        public TreeIterator() {
            stack = new Stack<>();
            postOrder((item) -> stack.add((E) item));
        }

        public boolean hasNext(){
            return !stack.isEmpty();
        }

        public E next(){
            if(!hasNext()) throw new NoSuchElementException();
            return stack.pop();
        }
    }
}
