/**
 * Created by Ricardo on 11/27/2016.
 */

public class MyLinkedList<E> extends MyAbstractList<E> {
    public Node<E> head;
    public Node<E> tail;

    public static class Node<E> {
        E element;
        Node<E> next;

        public Node(E element) {
            this.element = element;
        }

        public Node() {
        }
    }

    /** Create a default list */
    public MyLinkedList() {
    }

    /** Create a list from an array of objects */
    public MyLinkedList(E[] objects) {
        super(objects);
    }

    /** Return the head element in the list */
    public E getFirst() {
        if (head == null)
            return null;
        return head.element;
    }

    /** Return the last element in the list */
    public E getLast() {
        if (tail == null)
            return null;
        return tail.element;
    }

    /** Add an element to the beginning of the list */
    public void addFirst(E e) {
        Node<E> temp = new Node<E>(e); // Create a new node
        temp.next = head; // link the new node with the head
        head = temp; // head points to the new node
        size++; // Increase list size

        if (tail == null) // the new node is the only node in list
            tail = head;
    }

    /** Add an element to the end of the list */
    public void addLast(E e) {
        Node<E> newNode = new Node<E>(e); // Create a new for element e

        if (tail == null)
        {
            head = tail = newNode; // The new node is the only node in list
        }
        else
        {
            tail = tail.next = newNode; // Link the new with the last node
        }

        size++; // Increase size
    }

    @Override
    /**
     * Add a new element at the specified index in this list. The index of the
     * head element is 0
     */
    public void add(int index, E e) {
        if (index == 0)
        {
            addFirst(e);
        }
        else if (index >= size)
        {
            addLast(e);
        }
        else
        {
            Node<E> current = head;
            for (int i = 1; i < index; i++)
            {
                current = current.next;
            }
            Node<E> next = current.next;
            Node<E> temp = new Node<E>(e);
            temp.next = next;
            current.next = temp;
            size++;
        }
    }

    /**
     * Remove the head node and return the object that is contained in the
     * removed node.
     */
    public E removeFirst() {
        if (head == null)
        {
            return null;
        }
        Node<E> temp = head;
        head = head.next;
        size--;
        if (head == null)
        {
            tail = null;
        }
        return temp.element;
    }

    /**
     * Remove the last node and return the object that is contained in the
     * removed node.
     */
    public E removeLast() {
        if (tail == null)
        {
            return null;
        }
        if (head.next == null)
        {
            Node<E> temp = head;
            head = tail = null;
            size = 0;
            return temp.element;
        }
        Node<E> prev = null;
        Node<E> current = head;

        while (current.next != null)
        {
            prev = current;
            current = current.next;
        }

        tail = prev;
        tail.next = null;
        size--;
        return current.element;
    }

    /**
     * Remove the element at the specified position in this list. Return the
     * element that was removed from the list.
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size)
        {
            return null;
        }
        if (index == 0)
        {
            return removeFirst();
        }
        if (index == size - 1)
        {
            return removeLast();
        }

        Node<E> prev = null;
        Node<E> current = head;

        for (int i = 0; i < index; i++)
        {
            prev = current;
            current = current.next;
        }

        prev.next = current.next;
        size--;
        return current.element;
    }

    @Override /** Override toString() to return elements in the list */
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        Node<E> current = head;
        while (current != null)
        {
            result.append(current.element);
            current = current.next;
            if (current != null)
            {
                result.append(", "); // Separate two elements with a comma
            }
            else
            {
                result.append("]"); // Insert the closing ] in the string
            }
        }

        return result.toString();
    }

    @Override /** Clear the list */
    public void clear() {
        size = 0;
        head = tail = null;
    }

    @Override /** Return true if this list contains the element e */
    public boolean contains(E e) {
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            if (current.element.equals(e))
                return true;
            current = current.next;
        }

        return false;
    }

    @Override /** Return the element at the specified index */
    public E get(int index) {
        checkIndex(index);
        Node<E> current = head;

        for (int i = 0; i < size; i++) {
            if (i == index)
                return current.element;
            current = current.next;
        }
        return null;
    }

    @Override /**
     * Return the index of the head matching element in this list.
     * Return -1 if no match.
     */
    public int indexOf(E e) {
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            if (current.element.equals(e))
                return i;
            current = current.next;
        }
        return -1;
    }

    @Override /**
     * Return the index of the last matching element in this list.
     * Return -1 if no match.
     */
    public int lastIndexOf(E e) {
        Node<E> current = head;
        int lastIndex = -1;
        for (int i = 0; i < size; i++) {
            if (current.element.equals(e))
                lastIndex = i;
            current = current.next;
        }
        return lastIndex;
    }

    /**
     * Replace the element at the specified position in this list with the
     * specified element.
     */
    @Override
    public E set(int index, E e) {
        checkIndex(index);

        Node<E> current = head;
        E oldValue = null;
        for (int i = 0; i < size; i++) {
            if (i == index) {
                oldValue = current.element;
                current.element = e;
                break;
            }
            current = current.next;
        }
        return oldValue;
    }

    @Override /** Override iterator() defined in Iterable */
    public java.util.Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private class LinkedListIterator implements java.util.Iterator<E> {
        private Node<E> current = head; // Current index

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public E next() {
            E e = current.element;
            current = current.next;
            return e;
        }

        @Override
        public void remove() {
        }
    }

}