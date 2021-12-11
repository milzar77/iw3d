package com.blogspot.fravalle.data;

import java.io.PrintStream;

public interface IProgressAction extends Runnable {

    public StringBuffer getBufferedData();

    public PrintStream getOutputStream();

}
