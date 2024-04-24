package main.java.it.polimi.remotedemo.controller;

import main.java.it.polimi.remotedemo.model.AdderModel;

public class AdderController {
    final AdderModel adderModel;
    public AdderController() {
        this.adderModel = new AdderModel();
    }

    public boolean add(Integer number) {
        synchronized (this.adderModel) {
            return this.adderModel.add(number);
        }
    }

    public Integer getCurrent() {
        synchronized (this.adderModel) {
            return this.adderModel.get();
        }
    }

    public boolean reset() {
        synchronized (this.adderModel) {
            return this.adderModel.add(- this.adderModel.get());
        }
    }
}
