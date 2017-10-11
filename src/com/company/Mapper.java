package com.company;

/**
 * Функциональный интерфейс, объявляющий функцию преобразования элемента типа T1 в элемент типа T2
 */
public interface Mapper<T,R> {

    public R map(T element);

}
