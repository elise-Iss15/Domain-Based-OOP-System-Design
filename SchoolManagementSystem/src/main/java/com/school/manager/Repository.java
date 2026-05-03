package com.school.manager;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Repository<T> {

    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(items);
    }

    public List<T> search(Predicate<T> predicate) {
        return items.stream().filter(predicate).collect(Collectors.toList());
    }


    public List<T> sort(Comparator<T> comparator) {
        return items.stream().sorted(comparator).collect(Collectors.toList());
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void setAll(List<T> newItems) {
        items.clear();
        items.addAll(newItems);
    }
}
