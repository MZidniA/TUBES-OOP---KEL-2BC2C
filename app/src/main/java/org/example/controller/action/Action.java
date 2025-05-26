// Lokasi: src/main/java/org/example/controller/action/Action.java
package org.example.controller.action;

import org.example.model.Farm;

public interface Action {
    boolean canExecute(Farm farm);
    void execute(Farm farm);
    String getActionName();
}

