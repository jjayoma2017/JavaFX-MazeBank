package com.jtj.mazebank.Models;

import com.jtj.mazebank.Views.ViewFactory;

public class Model {
    private  static Model model;
    private final ViewFactory viewFactory;

    private Model(){
        this.viewFactory = new ViewFactory(); // Singleton
    }

    public  static  synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return  model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
}
