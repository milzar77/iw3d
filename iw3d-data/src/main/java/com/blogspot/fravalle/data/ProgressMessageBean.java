package com.blogspot.fravalle.data;

import java.util.Vector;

public class ProgressMessageBean {

    public Vector<IProgressAction> runnableStepsVector = new Vector<>();

    IProgressStep progressStep;

    private Integer vectorIndex = 0;

    private Integer step;
    private String message;
    private Boolean isDynamicStep;
    private Integer dynamicStep;

    public ProgressMessageBean() {
        this.step = 0;
        this.message = "Step not defined for provided iteration#";
        this.isDynamicStep = Boolean.FALSE;
    }

    public ProgressMessageBean(IProgressStep progressStep, Integer step, String message, Boolean isDynamicStep) {
        this.progressStep = progressStep;
        this.step = step;
        this.message = message;
        this.isDynamicStep = isDynamicStep;
    }

    public ProgressMessageBean(String s) {
        this.progressStep = progressStep;
        this.step = step;
        this.message = s;
        this.isDynamicStep = isDynamicStep;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isDynamicStep() {
        return isDynamicStep;
    }

    public void setIsDynamicStep(Boolean hasDynamicStep) {
        this.isDynamicStep = hasDynamicStep;
    }

    public Integer getDynamicStep() {
        return dynamicStep;
    }

    public void setDynamicStep(Integer stepDinamico) {
        dynamicStep = stepDinamico;
    }

    public IProgressStep getProgressStep() {
        return progressStep;
    }

    public void setProgressStep(IProgressStep progressStep) {
        this.progressStep = progressStep;
    }

    public Integer getVectorIndex() {
        return vectorIndex;
    }

    public void setVectorIndex(Integer vectorIndex) {
        this.vectorIndex = vectorIndex;
    }

    public boolean addRunnableStep(IProgressAction runnable) {
        /*if (runnableVector.isEmpty()) {
            Vector<Runnable> runnables;
            runnableVector.add(this.getVectorIndex(), runnables = new Vector<Runnable>());
        }*/
        runnableStepsVector.add(runnable);
        return true;
    }

    public Integer getTotalVectorStepSize(){
        return runnableStepsVector.size();
    }
    /*
    public boolean addRunnable(Runnable runnable) {
        if (runnableVector.isEmpty()) {
            Vector<Runnable> runnables;
            runnableVector.add(this.getVectorIndex(), runnables = new Vector<Runnable>());
        }
        runnableVector.get(this.getVectorIndex()).add(runnable);
        return true;
    }

    public Integer getTotalVectorStepSize(){
        return runnableVector.get(this.getVectorIndex()).size();
    }
*/
}
