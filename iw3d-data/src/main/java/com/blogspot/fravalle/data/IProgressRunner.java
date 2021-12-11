package com.blogspot.fravalle.data;

import java.util.Vector;

public interface IProgressRunner {

    public final int PROGRESS_TASK_COMPLETED = 100;

    public final short SKIP_THREAD_SLEEP = -1;

    public ProgressMessageBean executeProgress(int SWITCH_FLAG);

    public Boolean skipThreadSleep();

    public Long sleepingTime();

    public Integer nextIterationStep();

    public Integer myCurrentIterationStep();

    public Integer maxSteps();

    public void setSteps(Integer steps);

    public int calculatePercentile();

    public Boolean hasDynamicSteps();

    public ProgressMessageBean getOperationInProgress(int index);

    public Integer getOperationInProgressSize();

    public Vector<ProgressMessageBean> getOperationsInProgress();

}
