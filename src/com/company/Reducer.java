package com.company;

/**
 * Функциональный интерфейс,  объявляющий функцию преобразования пары элементов типа T1 в элемент типа T2
 */
public interface Reducer<T> {
    public T reduce(T firstelement,T secondElement);
}