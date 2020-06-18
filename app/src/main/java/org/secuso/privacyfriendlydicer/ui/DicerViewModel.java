package org.secuso.privacyfriendlydicer.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.secuso.privacyfriendlydicer.dicer.Dicer;

public class DicerViewModel extends ViewModel {

    private final Dicer dicer = new Dicer();
    private int faceNumber = 6;
    private int diceNumber = 5;

    private final MutableLiveData<int[]> dicerLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> diceNumberLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> faceNumberLiveData = new MutableLiveData<>();

    public DicerViewModel() {
        dicerLiveData.postValue(new int[0]);
    }

    public LiveData<int[]> getDicerLiveData() {
        return dicerLiveData;
    }
    public LiveData<Integer> getDiceNumberLiveData() {
        return diceNumberLiveData;
    }
    public LiveData<Integer> getFaceNumberLiveData() {
        return faceNumberLiveData;
    }

    public int getDiceNumber() {
        return diceNumber;
    }
    public int getFaceNumber() {
        return faceNumber;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
        diceNumberLiveData.postValue(diceNumber);
    }

    public void setFaceNumber(int faceNumber) {
        this.faceNumber = faceNumber;
        faceNumberLiveData.postValue(faceNumber);
    }

    public void rollDice() {
        dicerLiveData.postValue(dicer.rollDice(diceNumber, faceNumber));
    }
}
