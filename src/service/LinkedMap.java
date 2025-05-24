package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LinkedMap<T> {
    private final Map<Integer, Node<T>> map;
    private Node<T> first;
    private Node<T> last;

    public LinkedMap() {
        this.map = new HashMap<>();
        this.first = null;
        this.last = null;
    }

    public void add(Integer id, T value) {
        if (map.containsKey(id)) {
            removeItemById(id);
        }

        LinkedMap.Node<T> l = last;
        LinkedMap.Node<T> newNode = new LinkedMap.Node<>(l, value, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        map.put(id, newNode);
    }

    public void removeItemById(Integer id) {
        LinkedMap.Node<T> value = map.get(id);

        if (value == null) {
            return;
        }

        LinkedMap.Node<T> next = value.next;
        LinkedMap.Node<T> prev = value.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            value.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            value.next = null;
        }

        value.item = null;
        map.remove(id);
    }

    public List<T> getAllValues() {
        List<T> result = new ArrayList<>();
        for (LinkedMap.Node<T> x = first; x != null; x = x.next) {
            result.add(x.item);
        }
        return result;
    }

    private static class Node<T> {
        private T item;
        private Node<T> prev;
        private Node<T> next;

        public Node(Node<T> prev, T item, Node<T> next) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }
}
