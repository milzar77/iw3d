package com.blogspot.fravalle.iw3d.jme;

import com.blogspot.fravalle.data.*;
import com.blogspot.fravalle.data.chrome.ChromeBookmarkImporter;
import com.blogspot.fravalle.iw3d.jme.simpleapplication.WindowJme3DSimpleApplication;
import com.blogspot.fravalle.iw3d.jme.sources.ESourceSelector;
import com.jme3.scene.Spatial;
import org.apache.camel.ProducerTemplate;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.concurrent.Callable;

public class SwingBookmarkImporter extends JPanel
        implements ActionListener,
        PropertyChangeListener {

    public static volatile boolean isSetupFinished = false;

    private WindowJme3DSimpleApplication appInstance;

    private File selectedFile = new File("/tmp/bookmarks.html");

    private volatile String infoMessage = null;

    private JProgressBar progressBar;
    private JButton startButton;
    private JTextArea taskOutput;
    private ImportTask importTask;

    private final IProgressRunner[] progressRunners = {
            /*new SQLDataSetup()
            ,*/
            new ChromeBookmarkImporter(selectedFile)
            /*,
            new SQLDataLoader(DataConfiguration.getDmlData())
            */
            /*,
            new SQLDataLoader("/tmp/import-traceroutes.sql")*/
    };

    public class ImportTask extends SwingWorker<Void, Void> {

        public ImportTask(SwingBookmarkImporter instanceSwingBookmarkImporter) {
            this.addPropertyChangeListener(instanceSwingBookmarkImporter);
        }

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {

            //System.err.printf("[%1$s] INSPECT: %2$s\n", this.getClass().getSimpleName(), progressRunners[2]);

            for (IProgressRunner runner : progressRunners) {
                System.err.printf("[%1$s] has %2$s total max steps\n", runner.getClass().getSimpleName(), runner.maxSteps());

                if (runner instanceof ChromeBookmarkImporter) {
                    ChromeBookmarkImporter.setInputBookmarkFile( SwingBookmarkImporter.this.selectedFile.getAbsolutePath() );
                }

                int progress = 0;
                //Initialize progress property.
                setProgress(progress);

                Integer storeSteps = runner.maxSteps();

                int myint = -1;

                for (ProgressMessageBean myStep : runner.getOperationsInProgress()
                        /*
                int myint=0;myint<runner.getOperationInProgressSize()//runner.maxSteps();myint++
                */) {
                    //Sleep for up to one second.
                    try {
                        if (!runner.skipThreadSleep()) Thread.sleep(runner.sleepingTime());

                        //ProgressMessageBean myStep = runner.executeProgress(myint);

                        long millis = System.currentTimeMillis();
                        System.err.printf("[%1$s] ==> %2$s\n", runner.getClass().getSimpleName(), myStep.getMessage());
                        myStep.getProgressStep().run();
                        System.err.printf("[%1$s] TIME ELAPSED ==> %2$s\n", runner.getClass().getSimpleName(), (System.currentTimeMillis()-millis));


                        SwingBookmarkImporter.this.infoMessage = String.format("==> %1$s#%2$s USING STEP:%3$s; CURRENT STEP: %4$s", myStep.getMessage(), myint + 1, myStep.getDynamicStep(), myStep.getStep());

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
            SwingBookmarkImporter.this.isSetupFinished =true;

            //call 3D window data loader
            if (appInstance!=null) {
                //appInstance.applyRandomWebMatrix(false);
                appInstance.enqueue(new Callable<Spatial>(){
                    public Spatial call() throws Exception{
                        appInstance.selectedSource = ESourceSelector.BOOKMARKS;
                        appInstance.initRandomWebMatrix(false);
                        return null;
                    }
                });
            }

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            //startButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
        }
    }

    public SwingBookmarkImporter(WindowJme3DSimpleApplication instance) {
        super(new BorderLayout());

        this.appInstance = instance;

        importTask = new ImportTask(this);
        BrowserIWorld3D.camelContext.getRegistry().bind("bookmarkImporter", ImportTask.class, importTask);

        //Create the demo's UI.
        startButton = new JButton("Import Chrome Bookmarks from a file");
        startButton.setActionCommand("importBookmarks");
        startButton.addActionListener(this);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(800, 12));
        progressBar.setStringPainted(true);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    }

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {

        //load a file
        JFileChooser jFileChooser = new JFileChooser();
        //System.out.println("BROWSING: " + System.getProperty("user.home"));
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        jFileChooser.addChoosableFileFilter(new FileFilter(){
            @Override
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                else
                    return f.getName().contains("bookmark") && f.getName().contains("html");
            }
            @Override
            public String getDescription() {
                return "Chrome Exported Bookmarks file (name must be in the form bookmark*.html)";
            }
        });
        jFileChooser.setAcceptAllFileFilterUsed(true);
        int result = jFileChooser.showOpenDialog(new JFrame("Choose an exported Chrome bookmark file"));
        if (result == JFileChooser.APPROVE_OPTION) {
            this.selectedFile = jFileChooser.getSelectedFile();
            //System.out.println("Selected file: " + this.selectedFile.getAbsolutePath());
            if (!this.selectedFile.getName().contains("bookmark") && !this.selectedFile.getName().contains("html"))
                return;
        } else {
            //System.err.println("Invalid selection!");
            return;
        }

        //startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        //importTask = new ImportTask(this);
        //FIX: importTask.execute();
        //startButton.setEnabled(false);

        //TODO:
        ProducerTemplate template = BrowserIWorld3D.camelContext.createProducerTemplate();
        template.sendBody("direct:inputCamel","<hello>World!</hello>");


    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            /*taskOutput.append(String.format(
                    "Completed %d%% of task.\n", importTask.getProgress()));*/
            taskOutput.append(SwingBookmarkImporter.this.infoMessage + String.format(" (%d%%)\n", importTask.getProgress()));

        }
    }


    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    public static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("iWorld 3D Resource Loader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new SwingBookmarkImporter(null);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        frame.setPreferredSize(new Dimension(1024, 250));

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        //DataLoader.getInstance(newContentPane);

    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                //DataLoader.getInstance(SwingJmeStarsImporter.this);
            }
        });
    }
}