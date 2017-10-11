package com.company;

import java.util.*;

public class IgnoreList<T> implements List<T> {

    LinkedList<T> infoElements;
    Set<T> ignoreElements;

    public IgnoreList(){
        infoElements = new LinkedList<T>();
        ignoreElements = new HashSet<T>();
    }

    public IgnoreList(Collection<? extends T> infoElements, Collection<? extends T> ignoreElements){
        this.infoElements = new LinkedList<T>();
        if(infoElements!=null) {
            this.infoElements.addAll(infoElements);
        }
        this.ignoreElements = new HashSet<T>();
        if(ignoreElements!=null) {
            this.ignoreElements.addAll(ignoreElements);
        }
    }

    public <R> List<R> map(Mapper<? super T,R> mapper){
        if (mapper==null) throw new NullPointerException("Mapper is null");
        List<R> result = new ArrayList<R>();
        for(T element : this){
            R mappedElement = mapper.map(element);
            result.add(mappedElement);
        }
        return result;
    }

    public T reduce(Reducer<T> reducer){
        if (reducer==null) throw new NullPointerException("Reducer is null");
        if (this.size()<2) throw new NotEnoughElementsException("At least 2 elements expected for reduce");
        Iterator<T> iterator = this.iterator();
        T elem1 = iterator.next();
        T elem2 = iterator.next();
        T result = reducer.reduce(elem1,elem2);
        while(iterator.hasNext()){
            elem1 = result;
            elem2 = iterator.next();
            result = reducer.reduce(elem1,elem2);
        }
        return result;
    }

    @Override
    public int size() {
        Iterator<T> iterator = infoElements.iterator();
        int size=0;
        while (iterator.hasNext()){
            if(!ignoreElements.contains(iterator.next())) size++;
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size()==0);
    }

    @Override
    public boolean contains(Object o) {
        if(ignoreElements.contains(o)) return false;
        else return infoElements.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new IgnoreIterator<>();
    }

    @Override
    public Object[] toArray() {
        ArrayList res = new ArrayList();
        IgnoreIterator iterator = new IgnoreIterator();
        while(iterator.hasNext()){
            res.add(iterator.next());
        }
        return  res.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        int size = size();
        if (a.length < size)
            a = (T1[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        IgnoreIterator iterator = new IgnoreIterator();
        int i=0;
        while (iterator.hasNext()){
            a[i] = (T1)iterator.next();
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        return infoElements.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return infoElements.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c){
            if (!ignoreElements.contains(o) && !infoElements.contains(o)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return infoElements.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return infoElements.addAll(index,c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return infoElements.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        IgnoreIterator iterator = new IgnoreIterator();
        boolean modified=false;
        while (iterator.hasNext()){
            Object o = iterator.next();
            if(!c.contains(o)){
                iterator.remove();
                modified=true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        infoElements = new LinkedList<T>();
    }

    @Override
    public T get(int index) {
        if(index<0) throw new IllegalArgumentException("Negative index: " + index);
        int i = 0;
        IgnoreIterator iterator = new IgnoreIterator();
        Object o=iterator.next();
        while(i<index){
            o = iterator.next();
            i++;
        }
        return (T)o;
    }

    @Override
    public T set(int index, T element) {
        if(index<0) throw new IllegalArgumentException("Negative index: " + index);
        int i = 0;
        ListIterator infoIterator = infoElements.listIterator();
        Object o = null;
        while(i<=index){
            o = infoIterator.next();
            if(!ignoreElements.contains(o)) i++;
        }
        infoIterator.set(element);
        return (T)o;
    }

    @Override
    public void add(int index, T element) {
        if(index<0) throw new IllegalArgumentException("Negative index: " + index);
        int i = 0;
        ListIterator infoIterator = infoElements.listIterator();
        Object o = null;
        while(i<=index){
            o = infoIterator.next();
            if(!ignoreElements.contains(o)) i++;
        }
        infoIterator.add(element);
    }

    @Override
    public T remove(int index) {
        if(index<0) throw new IllegalArgumentException("Negative index: " + index);
        int i = 0;
        ListIterator infoIterator = infoElements.listIterator();
        Object o = null;
        while(i<=index){
            o = infoIterator.next();
            if(!ignoreElements.contains(o)) i++;
        }
        infoIterator.remove();
        return (T)o;
    }

    @Override
    public int indexOf(Object obj) {
        if(ignoreElements.contains(obj)){
            return -1;
        } else {
            int i = -1;
            ListIterator infoIterator = infoElements.listIterator();
            Object o = null;
            while (!obj.equals(o)) {
                o = infoIterator.next();
                if(!ignoreElements.contains(o)) i++;
            }
            return i;
        }
    }

    @Override
    public int lastIndexOf(Object obj) {
        if(ignoreElements.contains(obj)){
            return -1;
        } else {
            int i = size()-1;
            Iterator infoIterator = infoElements.descendingIterator();
            Object o = infoIterator.next();
            while (!obj.equals(o) && infoIterator.hasNext()) {
                o = infoIterator.next();
                if(!ignoreElements.contains(o)) i--;
            }
            if (!obj.equals(o)) i=-1;   //check last element
            return i;
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return new IgnoreListIterator<>();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new IgnoreListIterator<>(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if(fromIndex>toIndex) throw new IllegalArgumentException("fromIndex was greater then toIndex: " + fromIndex+ " > " + toIndex);
        ArrayList result = new ArrayList();
        ListIterator listIterator = this.listIterator(fromIndex);
        int i=fromIndex;
        while(i<=toIndex){
            result.add(listIterator.next());
            i++;
        }
        return result;
    }

    class IgnoreListIterator<T> implements ListIterator<T> {

        ListIterator infoListIterator = infoElements.listIterator();
        int curIndex=0;
        int nextCount=0;
        int previousCount=0;


        IgnoreListIterator(){}

        IgnoreListIterator(int index){
            if(index<0) throw new IndexOutOfBoundsException("Negative index: " + index);
            try {
                for (int i = 0; i < index; i++) {
                    next();
                }
                curIndex = index;
            } catch (NoSuchElementException e){
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
        }

        @Override
        public boolean hasNext() {
            boolean result = hasNextImpl();
            while (previousCount>0){
                infoListIterator.previous();
                previousCount--;
            }
            return result;
        }
        private boolean hasNextImpl() {
            if(!infoListIterator.hasNext()) return false;
            else{
                Object element = infoListIterator.next();
                previousCount++;
                if (ignoreElements.contains(element)){
                    return hasNextImpl();
                } else {
                    return true;
                }
            }
        }

        @Override
        public T next() {
            curIndex++;
            Object element = infoListIterator.next();
            while(ignoreElements.contains(element)) element = infoListIterator.next();
            return (T)element;
        }

        @Override
        public boolean hasPrevious() {
            boolean result = hasPreviousImpl();
            while (nextCount>0){
                infoListIterator.next();
                nextCount--;
            }
            return result;
        }
        private boolean hasPreviousImpl() {
            if(!infoListIterator.hasPrevious()) return false;
            else{
                Object element = infoListIterator.previous();
                nextCount++;
                if (ignoreElements.contains(element)){
                    return hasPreviousImpl();
                } else {
                    return true;
                }
            }
        }

        @Override
        public T previous() {
            curIndex--;
            Object element = infoListIterator.previous();
            while(ignoreElements.contains(element)) element = infoListIterator.previous();
            return (T)element;
        }

        @Override
        public int nextIndex() {
            return curIndex+1;
        }

        @Override
        public int previousIndex() {
            return curIndex-1;
        }

        @Override
        public void remove() {
            infoListIterator.remove();
        }

        @Override
        public void set(T t) {
            infoListIterator.set(t);
        }

        @Override
        public void add(T t) {
            infoListIterator.add(t);
        }
    }

    class IgnoreIterator<T> implements Iterator<T>{

        private ListIterator infoIterator = infoElements.listIterator();
        private int previousCount;

        @Override
        public boolean hasNext() {
            boolean result = hasNextImpl();
            while (previousCount>0){
                infoIterator.previous();
                previousCount--;
            }
            return result;
        }

        private boolean hasNextImpl() {
            if(!infoIterator.hasNext()) return false;
            else{
                Object element = infoIterator.next();
                previousCount++;
                if (ignoreElements.contains(element)){
                    return hasNextImpl();
                } else {
                    return true;
                }
            }
        }

        @Override
        public T next() {
            Object element = infoIterator.next();
            if(ignoreElements.contains(element)) return next();
            else return (T)element;
        }

        @Override
        public void remove(){
            infoIterator.remove();
        }
    }
}