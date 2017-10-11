package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        //инициализация:
        LinkedList<Integer> infoElements = new LinkedList<>();
        LinkedList<Integer> ignoredElements = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            infoElements.add(i);
            if(i%2==0){
                ignoredElements.add(i); //игнорирование чётных элементов до ста
            }
        }
        IgnoreList<Integer> list = new IgnoreList<Integer>(infoElements, ignoredElements);
        //тесты:
        System.out.println("Исходный список:");
        listOutput(list);
        List<String> mappedList = list.map(new Mapper<Integer, String>() {
            @Override
            public String map(Integer element) {
                String mapped;
                switch (element){
                    case 0:
                        mapped = "ноль";
                        break;
                    case 1:
                        mapped = "один";
                        break;
                    case 2:
                        mapped = "два";
                        break;
                    case 3:
                        mapped = "три";
                        break;
                    case 4:
                        mapped = "четыре";
                        break;
                    case 5:
                        mapped = "пять";
                        break;
                    case 6:
                        mapped = "шесть";
                        break;
                    case 7:
                        mapped = "семь";
                        break;
                    case 8:
                        mapped = "восемь";
                        break;
                    case 9:
                        mapped = "девять";
                        break;
                    default:
                        mapped = "длинная_чиселка";
                        break;
                }
                return mapped;
            }
        });
        System.out.println("\nОтредактированный список:");
        listOutput(mappedList);
        Integer reduced = list.reduce(new Reducer<Integer>() {
            @Override
            public Integer reduce(Integer firstelement, Integer secondElement) {
                return firstelement+secondElement;
            }
        });
        System.out.println("\n\"Схлопнутый\" список: " + reduced);
    }

    public static void listOutput(List list){
        Iterator<Integer> iterator = list.iterator();
        while(iterator.hasNext()){
            System.out.print(iterator.next()+ " ");
        }
        System.out.println();
    }

}