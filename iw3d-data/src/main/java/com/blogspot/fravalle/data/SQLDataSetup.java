package com.blogspot.fravalle.data;

import com.blogspot.fravalle.core.DataConfiguration;

import java.io.*;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class SQLDataSetup extends com.blogspot.fravalle.data.AProgressRunner {

    private Long sleepTime = 100L;

    @Override
    public ProgressMessageBean executeProgress(int SWITCH_FLAG) {

        switch (this.myCurrentIterationStep()) {
            case 0:
                System.err.println("Step 0 INHIBITED");
                sleepTime=1L;
                return new ProgressMessageBean();
            case 1:
                sleepTime=100L;
                return new ProgressMessageBean(()->{this.step1();},1,"Scrittura su disco dei file di setup database", Boolean.FALSE);
            case 2:
                sleepTime=100L;
                return new ProgressMessageBean(()->{this.step2();},2,"Eventuale pulizia nella directory base di Derby delle risorse pre-esistenti necessarie alla creazione del db", Boolean.FALSE);
            case 3:
                sleepTime=100L;
                return new ProgressMessageBean(()->{this.step3();},3,"Creazione del database schema di supporto", Boolean.FALSE);
            default:;
        }

        return super.executeProgress(SWITCH_FLAG);
    }

    @Override
    public Long sleepingTime() {
        return sleepTime;
    }

    private Vector<File> directoriesToDelete;

    public Integer step1() {
        if (!DataConfiguration.isDerbyCached()) {
            //TODO: sistemare recupero risorse da jar o classpath
            this.writeSetupConfiguration();
        }
        return nextIterationStep =1;
    }

    public Integer step2() {
        if (!DataConfiguration.isDerbyCached()) {
            this.cleanDerbyDbDirectory(DataConfiguration.getBaseDerbyDirectory() + DataConfiguration.getDdlSchemaName());
        }
        return nextIterationStep =2;
    }

    public Integer step3() {
        if (!DataConfiguration.isDerbyCached()) {
            this.runCreateSchema();
        }
        return nextIterationStep =3;
    }

    public SQLDataSetup() {
        super.steps = 3;

        ProgressMessageBean pmbStep1 = null;
        sleepTime= Long.valueOf(SKIP_THREAD_SLEEP);//TODO: move to ProgressMessageBean constructor
        operationsInProgress.add(pmbStep1=new ProgressMessageBean(()->{this.step1();},1,"Scrittura su disco dei file di setup database", Boolean.FALSE));

        sleepTime=100L;
        operationsInProgress.add(new ProgressMessageBean(()->{this.step2();},2,"Eventuale pulizia nella directory base di Derby delle risorse pre-esistenti necessarie alla creazione del db", Boolean.FALSE));

        sleepTime=100L;
        operationsInProgress.add(new ProgressMessageBean(()->{this.step3();},3,"Creazione del database schema di supporto", Boolean.FALSE));

    }

    public Short createSqlImport() {
        //if (true) System.exit(0);

/*
        try {

            if (!DataConfiguration.isDerbyCached()) {
                String derbyDirToUse = DataConfiguration.getBaseDerbyDirectory();

                //TODO: sistemare recupero risorse da jar o classpath

                this.setupConfiguration(derbyDirToUse);

                ImportHygCatalogue hygCatalogue = new ImportHygCatalogue(progressLoaderPane, rbConfig.getString(KEY_IMPORT_URL), rbConfig.getString(KEY_IMPORT_ARCHIVE));
                this.cleanDerbyDbDirectory(derbyDirToUse + rbConfig.getString(KEY_DDL_SCHEMA_NAME));
                this.runCreateSchema();
                hygCatalogue.importFromCsv(
                        Boolean.valueOf(rbConfig.getString(KEY_DOWNLOAD_FILE)),
                        Boolean.valueOf(rbConfig.getString(KEY_USE_DOWNLOAD_CACHED)),
                        Boolean.valueOf(rbConfig.getString(KEY_USE_DERBY_CACHED)),
                        rbConfig.getString(KEY_DDL_SCHEMA_NAME),
                        rbConfig.getString(KEY_IMPORT_DATA),
                        rbConfig.getString(KEY_DML_DATA),
                        "",
                        importMaxRows,
                        derbyDirToUse+rbConfig.getString(KEY_DDL_SCHEMA_NAME)
                );
                this.runImport();


            }

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        */
        return 0;
    }

    private void writeSetupConfiguration() {
        try {
            Class<?> cls = this.getClass();
            ProtectionDomain domain = cls.getProtectionDomain();
            CodeSource codeSource = domain.getCodeSource();
            URL sourceLocation = codeSource.getLocation();
            System.err.println("DEBUG>>> BASE SOURCE CODE: " + sourceLocation.toString());

            // TODO: attenzione al recupero da jar, adeguare se in presenza di sviluppo Maven o esecuzione da jar
            String fNameDdlCreate = "iw3d-schema-define.ddl";
            String prepend = "/";//sourceLocation.toString().contains("jar") ? "/" + sourceLocation.toString();
            this.writeSetupResource(prepend + fNameDdlCreate, DataConfiguration.getBaseDerbyDirectory() + "/" + fNameDdlCreate + DataConfiguration.TEMP_FILE_SUFFIX);
            String fNameDdlClean = "iw3d-schema-clean.ddl";
            //this.writeResource(sourceLocation.toString()+"/"+fNameDdlClean, derbyDirToUse+"/"+fNameDdlClean+TEMP_FILE_SUFFIX);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //TODO: lavorarci sopra, solito problema del codebase o similare
    public void writeSetupResource(String fInputName, String fOutputName) throws IOException {
        InputStream is = getClass().getResourceAsStream(fInputName);
        //getClassLoader if loaded by jar
        System.err.println("DEBUG>>> DDL IS A VALID INPUT STREAM? " + (is!=null));
        FileOutputStream configurationLocal = new FileOutputStream(fOutputName);
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            configurationLocal.write(buffer, 0, bytesRead);
        }
        is.close();
        configurationLocal.flush();
        configurationLocal.close();
        /*
        byte buf[] = new byte[4096];
        int len;
        if (f != null) {
            while ((len = f.read(buf)) > 0) {
                configurationLocal.write(buf, 0, len);
            }
            f.close();
        }
        configurationLocal.close();
        */
    }

    private boolean delDirs(File dir) {
        if (directoriesToDelete==null) {
            directoriesToDelete = new Vector<File>();
            directoriesToDelete.add(dir);
        }
        boolean end = true;
        for (File f : dir.listFiles()) {
            //System.out.println("Deleting file: "+f.getAbsolutePath());
            if (f.isDirectory()&&f.listFiles().length>0) {
                directoriesToDelete.add(f);
                end = delDirs(f);
            } else if (f.isDirectory()) {
                directoriesToDelete.add(f);
            }
        }
        return end;
    }

    public void cleanDerbyDbDirectory(String cleanDir) {
        if (!cleanDir.equals("")) {
            File fDir = new File(cleanDir);
            if (!fDir.exists()|| !fDir.isDirectory()) {
                return;
            }
            boolean rt = this.delDirs(fDir);
            File fTest = null;
            while((fTest=directoriesToDelete.lastElement())!=null) {
                System.out.println("==> MARK FOR RECURSIVE DELETION: " + fTest.getName());
                for (File fDelete : fTest.listFiles()) {
                    fDelete.delete();
                }
                fTest.delete();
                directoriesToDelete.removeElement(fTest);
                if (directoriesToDelete.isEmpty()) {
                    fTest = null;
                    break;
                }
            }
        }
    }

    public void createDdlSchema(Statement statement, String ddlSchema) throws IOException, SQLException {
        BufferedReader brSchema = new BufferedReader(new FileReader(ddlSchema+DataConfiguration.TEMP_FILE_SUFFIX));
        String lineSchema = null;
        StringBuffer schemaAppender = new StringBuffer(256);
        while ((lineSchema=brSchema.readLine())!=null) {
            lineSchema = lineSchema.replaceAll("\r\n"," ");
            if (lineSchema.indexOf("--")==-1) {
                schemaAppender.append(lineSchema);
            }
            if (lineSchema.endsWith(";")) {
                System.err.println(schemaAppender.toString());
                statement.execute(schemaAppender.toString().replaceAll(";"," "));
                schemaAppender = new StringBuffer(128);
            }

        }
        brSchema.close();

    }

    public void runCreateSchema() {
        try {
            String urlConnection = "jdbc:derby:"+ DataConfiguration.getBaseDerbyDirectory() + DataConfiguration.getDdlSchemaName() +";create=true";
            Connection con = DriverManager.getConnection(urlConnection);
            Statement statement = con.createStatement();
            this.createDdlSchema(statement, DataConfiguration.getDdlSchemaCreation());
            statement.close();
            con.close();
            System.err.println("END CONN!");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

}
