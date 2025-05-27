package org.example.controller.action;

import org.example.model.Farm;

public interface Action {
    boolean canExecute(Farm farm);
    void execute(Farm farm);
    String getActionName();
}

