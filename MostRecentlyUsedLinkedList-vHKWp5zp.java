/**
 * Created by Ricardo on 12/2/2016.
 */

public class MostRecentlyUsedLinkedList<E extends String> extends MyLinkedList<E> {
    

    public boolean remove(E e) {
        if (head == null)
            return false;

        if (head.element.equalsIgnoreCase(e)) {
            if (size == 1) clear();
            else {
                head = head.next;
                size--;
            }
            return true;
        }

            Node<E> previous = head;
            while (previous != tail && !previous.next.element.equalsIgnoreCase(e))
                previous = previous.next;
            if (previous != tail) {
                Node<E> next = previous.next.next;
                previous.next = next;
                size--;
                if (next == null) {
                    tail = previous;
                    return true;
                }
            }
            return false;

    }

    @Override
    public boolean contains(E e) {
        if (indexOf(e) != -1) {
            remove(e);
            addFirst(e);
            return true;
        }
        return false;
    }
    
}