package me.bright.util;

public enum Exceptions {
    REMOVEENCHANTMENT("Удаление зачарования",45);


    private String name;
    private int number;
    Exceptions(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
