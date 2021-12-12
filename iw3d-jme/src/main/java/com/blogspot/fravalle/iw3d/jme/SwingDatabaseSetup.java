package com.blogspot.fravalle.iw3d.jme;

import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.*;
import com.blogspot.fravalle.data.chrome.ChromeBookmarkImporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SwingDatabaseSetup
        implements
        PropertyChangeListener {

    public static volatile boolean isSetupFinished = false;

    private volatile String infoMessage = null;

    private ImportTask importTask;

    private final IProgressRunner[] progressRunners = {
            new SQLDataSetup()
    };

    public class ImportTask extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {

            //System.err.printf("[%1$s] INSPECT: %2$s\n", this.getClass().getSimpleName(), progressRunners[2]);

            for (IProgressRunner runner : progressRunners) {
                System.err.printf("[%1$s] has %2$s total max steps\n", runner.getClass().getSimpleName(), runner.maxSteps());

                int progress = 0;
                //Initialize progress property.
                setProgress(progress);

                Integer storeSteps = runner.maxSteps();

                int myint = -1;

                for (ProgressMessageBean myStep : runner.getOperationsInProgress()) {
                    //Sleep for up to one second.
                    try {
                        if (!runner.skipThreadSleep()) Thread.sleep(runner.sleepingTime());

                        //ProgressMessageBean myStep = runner.executeProgress(myint);

                        long millis = System.currentTimeMillis();
                        System.err.printf("[%1$s] ==> %2$s\n", runner.getClass().getSimpleName(), myStep.getMessage());
                        myStep.getProgressStep().run();
                        System.err.printf("[%1$s] TIME ELAPSED ==> %2$s\n", runner.getClass().getSimpleName(), (System.currentTimeMillis()-millis));


                        SwingDatabaseSetup.this.infoMessage = String.format("==> %1$s#%2$s USING STEP:%3$s; CURRENT STEP: %4$s", myStep.getMessage(), myint + 1, myStep.getDynamicStep(), myStep.getStep());

                        if (!runner.hasDynamicSteps()) {
                            //System.err.println(">>>>");
                            //TODO: calcolare percentile
                            progress = runner.calculatePercentile();
                        } else {
                            //System.err.println("#myStep:"+myStep.isDynamicStep());
                            if (!myStep.isDynamicStep()) {
                                //System.err.println("--->");
                                //TODO: calcolare percentile
                                progress = runner.calculatePercentile();
                            } else {
                                System.err.println("===>"+"VECTOR SIZE: " + myStep.getTotalVectorStepSize());
                                for (IProgressAction r : myStep.runnableStepsVector) {
                                    r.run();
                                    //TODO: calcolare percentile
                                    progress = runner.calculatePercentile();
                                    //System.err.printf("==>DONE PERCENTUALE=%1d%%; MAXSTEPS=%2$s; DYNAMIC STEP=%3$s; getStep=%4$s; myCurrentIterationStep=%5$s; nextIterationStep=%6$s;\n", progress, runner.maxSteps(), myStep.getDynamicStep(), myStep.getStep(), runner.myCurrentIterationStep(), runner.nextIterationStep());
                                    //System.err.printf("BUFFERED DATA:>>> %1$s", r.getBufferedData());
                                    /*r.getOutputStream().append(String.format("%1$s\n", r.getBufferedData()));
                                    r.getOutputStream().flush();*/
                                    setProgress(progress);
                                }

                            }
                        }

                        //this fires the property "progress"
                        if (!runner.hasDynamicSteps()) {
                            setProgress(progress);
                        }
/*
                        System.err.println("<:::BEFORE FINAL RESET:::>"+runner.getClass().getSimpleName()+" ==> MAXSTEPS: " + runner.maxSteps());
                        runner.setSteps(storeSteps);
                        System.err.println("<:::AFTER FINAL RESET:::>"+runner.getClass().getSimpleName()+" ==> MAXSTEPS: " + runner.maxSteps());
*/

                    } catch (Exception ignore) {
                        ignore.printStackTrace(System.err);
                    } finally {
                        //TODO: here goes all final instructions for every iteration executable after the end of all iteration
                        //vedi esecuzione messaggi di debug molto dopo la fine al try della iterazione del ciclo
                        //System.err.println("<:::AFTER FINAL RESET:::>"+runner.getClass().getSimpleName()+" ==> MAXSTEPS: " + runner.maxSteps());
                        //SwingJmeStarsImporter.this.progressStatusOk=true;
                    }

                }

            }

            //System.out.close();
            SwingDatabaseSetup.this.isSetupFinished = true;

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            //startButton.setEnabled(true);
            //TODO:setCursor(null); //turn off the wait cursor
            //TODO:taskOutput.append("Done!\n");
        }
    }

    public SwingDatabaseSetup() {

    }

    public void doDbSetup() {
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        importTask = new ImportTask();
        importTask.addPropertyChangeListener(this);
        importTask.execute();
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            //TODO:progressBar.setValue(progress);
            /*taskOutput.append(String.format(
                    "Completed %d%% of task.\n", importTask.getProgress()));*/
            //TODO:taskOutput.append(SwingDatabaseSetup.this.infoMessage + String.format(" (%d%%)\n", importTask.getProgress()));

        }
    }

}