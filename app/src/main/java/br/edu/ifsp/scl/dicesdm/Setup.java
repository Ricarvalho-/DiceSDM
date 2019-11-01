package br.edu.ifsp.scl.dicesdm;

import java.io.Serializable;

public class Setup implements Serializable {
    int dicesAmount, diceFacesAmount;

    public static Setup getDefault() {
        return new Setup(1, 6);
    }

    public Setup(int dicesAmount, int diceFacesAmount) {
        this.dicesAmount = dicesAmount;
        this.diceFacesAmount = diceFacesAmount;
    }
}
