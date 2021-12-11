package com.blogspot.fravalle.data;

import com.blogspot.fravalle.core.DataConfiguration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;

public class SQLDataLoader extends AProgressRunner {

    private Long sleepTime = 100L;

    private ProgressMessageBean pmbCurrent = null;

    private String theImportFile = "/tmp/nofile.tmp";

    @Override
    public ProgressMessageBean executeProgress(int SWITCH_INDEX) {

        ProgressMessageBean pmbCurrent = null;

        System.err.printf("STEP NUMBER #%1$s\n", SWITCH_INDEX);

        return this.getOperationInProgress(SWITCH_INDEX);

    }

    @Override
    public Boolean skipThreadSleep() {
        return this.sleepingTime()==SKIP_THREAD_SLEEP;
    }

    @Override
    public Long sleepingTime() {
        return sleepTime;
    }

    @Override
    public Boolean hasDynamicSteps() {
        return hasDynamicSteps;
    }

    public Integer step1_runImport1() {
        if (!DataConfiguration.isDerbyCached()) {
            this.runImport(this.theImportFile);
        }
        nextIterationStep=1;
        return nextIterationStep;
    }

    private Integer step2_closeChannels() {
        closeChannels();
        nextIterationStep=2;
        return nextIterationStep;
    }

    public SQLDataLoader(String theImportFile) {

        super.steps = 1;

        this.theImportFile = theImportFile;

        sleepTime= Long.valueOf(SKIP_THREAD_SLEEP);//TODO: move to ProgressMessageBean constructor
        operationsInProgress.add(pmbCurrent=new ProgressMessageBean(()->{this.step1_runImport1();},1,"Importazione 1 da HTML a DB SQL", Boolean.TRUE));
        pmbCurrent.setStep(1);
        pmbCurrent.setIsDynamicStep(Boolean.TRUE);
        pmbCurrent.setDynamicStep(this.myCurrentIterationStep());
        pmbCurrent.setVectorIndex(0);

        sleepTime=100L;
        operationsInProgress.add(new ProgressMessageBean(()->{this.step2_closeChannels();},2,"Chiusura canali dati", Boolean.FALSE));

        this.hasDynamicSteps = Boolean.TRUE;
        this.setSteps(operationsInProgress.size());

    }

    public void runImport(String importFile) {
        System.err.println("TEST RUN IMPORT FROM " + importFile);
        try {
            String urlConnection = "jdbc:derby:"+ DataConfiguration.getBaseDerbyDirectory() + DataConfiguration.getDdlSchemaName() +";create=true";
            con = DriverManager.getConnection(urlConnection);
            statement = con.createStatement();
            this.populateWithDmlData(statement, importFile);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        //DataLoader
    }

    private String lineInput = null;
    private Connection con;
    private Statement statement;

    public void closeChannels() {
        System.err.println("CLOSING DATA CHANNELS");
        try {
            if (statement!=null) statement.close();
            if (con!=null) con.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void populateWithDmlData(Statement statement, String inputData) throws IOException, SQLException {
        BufferedReader brInput = new BufferedReader(new FileReader(inputData));

        int importCounter = 0;
        while ((lineInput=brInput.readLine())!=null) {
            lineInput = lineInput.replace("\r\n","").replace(";","");
            if (!lineInput.startsWith("--")) {
                try {
                    //statement.execute(lineInput);
                    //System.err.printf("-- Star '%1$s' imported into jme-db\n", importCounter);

                    IProgressAction rCurrent = new IProgressAction() {

                        private final String lContent = lineInput.toString();

                        @Override
                        public final StringBuffer getBufferedData() {
                            return new StringBuffer(lContent);
                        }

                        @Override
                        public final PrintStream getOutputStream() {
                            return System.out;
                        }

                        @Override
                        public final void run() {
                            try {
                                //Statement statement1 = SQLDataLoader.this.con.createStatement();
                                /*PreparedStatement pstm = SQLDataLoader.this.con.prepareStatement(lContent);
                                pstm.execute();
                                pstm.close();*/
                                SQLDataLoader.this.statement.execute(lContent);
                                //statement1.close();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                                System.err.printf("DML SQL=%1$s\n", lContent);
                            }
                            //HygCatalogueImporter.this.sbContent.append(lContent+"\n");
                            SQLDataLoader.this.pmbCurrent.setDynamicStep(SQLDataLoader.this.myCurrentIterationStep());
                            //HygCatalogueImporter.this.pmbLetturaFile.addRunnableStep(this);
                            SQLDataLoader.this.setSteps(SQLDataLoader.this.pmbCurrent.getTotalVectorStepSize());
                        }
                    };
                    pmbCurrent.addRunnableStep(rCurrent);



                } catch (Exception e) {
                    System.err.println("--ERROR DEBUG INFO:\n----\n" + lineInput+"\n----");
                    throw e;
                } finally {
                    importCounter++;
                }
            }

        }
        brInput.close();
    }

}
