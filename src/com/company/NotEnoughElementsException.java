package com.company;

class NotEnoughElementsException extends RuntimeException{
    public NotEnoughElementsException(){
        super();
    }
    public NotEnoughElementsException(String s){
        super(s);
    }
}
