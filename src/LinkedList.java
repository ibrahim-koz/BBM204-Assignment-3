public class LinkedList<T> {
    private Node<T> head = null;
    private Node<T> tail = null;

    public void insert(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.setNext(head);
        head = newNode;
    }

    public void append(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = head;
        }
        else{
            tail.next = newNode;
            tail = tail.next;
        }
    }

    public void remove() {
        if (head.getNext() != null)
            head = head.getNext();
        else head = null;
    }

    private void printList(Node<T> node) {
        System.out.println("Node is " + node.getValue());
        if (node.getNext() != null) printList(node.getNext());
    }

    public void print() {
        printList(head);
    }

    public Node<T> getHead() {
        return head;
    }

    public static class Node<T> {
        private T value;
        private Node<T> next;

        public Node(T value) {
            this.value = value;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node<T> getNext() {
            return next;
        }

        public T getValue() {
            return value;
        }
    }
}



