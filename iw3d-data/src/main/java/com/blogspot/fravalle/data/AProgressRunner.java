package com.blogspot.fravalle.data;

import java.util.Vector;

abstract public class AProgressRunner implements IProgressRunner, Runnable {

    public Integer myCurrentIterationStep = 1;

    public Integer nextIterationStep = 1;

    public Integer steps = 0;

    public Boolean skipThreadsleep = false;

    public Boolean hasDynamicSteps = false;

    public Vector<ProgressMessageBean> operationsInProgress = new Vector<ProgressMessageBean>();

    @Override
    public void run() {

    }

    @Override
    public Boolean hasDynamicSteps() {
        return hasDynamicSteps;
    }

    @Override
    public Boolean skipThreadSleep() {
        return false;
    }

    @Override
    public Long sleepingTime() {
        return 1L;
    }

    @Override
    public ProgressMessageBean executeProgress(int SWITCH_FLAG) {
        return new ProgressMessageBean("PROGRESS TASK COMPLETED" );//PROGRESS_TASK_COMPLETED;
    }

    @Override
    public Integer nextIterationStep() {
        return nextIterationStep;
    }

    @Override
    public Integer myCurrentIterationStep() {
        return myCurrentIterationStep;
    }

    @Override
    public Integer maxSteps() {
        return this.steps;
    }

    @Override
    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    @Override
    public int calculatePercentile() {
        /*
        steps : 100 = currentStep : x; x=(100*currentStep)/steps
         */
        int perc = (((short)100)* myCurrentIterationStep())/maxSteps();
        this.myCurrentIterationStep++;
        return perc;
    }

    @Override
    public ProgressMessageBean getOperationInProgress(int index){
        return operationsInProgress.get(index);
    }

    @Override
    public Integer getOperationInProgressSize(){
        return operationsInProgress.size();
    }

    @Override
    public Vector<ProgressMessageBean> getOperationsInProgress() {
        return operationsInProgress;
    }

}
