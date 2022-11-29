package controller.exestack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class MyDeque<Value> implements Deque<Value> {

    private List<Value> elems;

    public MyDeque() {
        this.elems = new ArrayList<Value>();
    }

    @Override
    public boolean isEmpty() {
        return this.elems.size() == 0;
    }

    @Override
    public Object[] toArray() {
        Object[] telems = new Object[this.elems.size()];
        for (int i = 0; i < this.elems.size(); i++) {
            telems[i] = this.elems.get(i);
        }
        return telems;
    }

    @Override
    public void clear() {
        while (this.elems.isEmpty()) {
            this.elems.remove(0);
        }
    }

    @Override
    public void addFirst(Value e) {
        this.elems.add(0, e);
    }

    @Override
    public void addLast(Value e) {
        this.elems.add(e);
    }

    @Override
    public Value removeFirst() {
        Value v = this.elems.remove(0);
        return v;
    }

    @Override
    public Value removeLast() {
        Value v = this.elems.remove(this.elems.size() - 1);
        return v;
    }

    @Override
    public Value getFirst() {
        if (this.size() == 0) {
            return null;
        }
        return this.elems.get(0);
    }

    public Value get(int index) {
        return this.elems.get(index);
    }

    @Override
    public Value getLast() {
        if (this.elems.size() == 0) {
            return null;
        }
        return this.elems.get(this.elems.size() - 1);
    }

    @Override
    public int size() {
        return this.elems.size();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean offerFirst(Value e) {
        return false;
    }

    @Override
    public boolean offerLast(Value e) {
        return false;
    }

    @Override
    public Value pollFirst() {
        return null;
    }

    @Override
    public Value pollLast() {
        return null;
    }

    @Override
    public Value peekFirst() {
        return null;
    }

    @Override
    public Value peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(Value e) {
        return false;
    }

    @Override
    public boolean offer(Value e) {
        return false;
    }

    @Override
    public Value remove() {
        return null;
    }

    @Override
    public Value poll() {
        return null;
    }

    @Override
    public Value element() {
        return null;
    }

    @Override
    public Value peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends Value> c) {
        return false;
    }

    @Override
    public void push(Value e) {
    }

    @Override
    public Value pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Value> iterator() {
        return null;
    }

    @Override
    public Iterator<Value> descendingIterator() {
        return null;
    }

}
