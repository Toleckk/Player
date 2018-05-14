package com.example.tolek.player.Repository;


import com.example.tolek.player.domain.Entities.Entity;

import java.util.ArrayList;

public class Repository<T extends Entity> implements EntityKeeper<T>{
    protected ArrayList<T> list;

    public Repository(ArrayList<T> list){
        this.list = list;
    }

    public T get(int index){
        return list.get(index);
    }

    public int indexOf(T object){
        return list.indexOf(object);
    }

    public int size(){
        return list.size();
    }

    public void add(T object){
        list.add(object);
    }

    @Override
    public ArrayList<T> getList() {
        return list;
    }
}
