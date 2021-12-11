package com.blogspot.fravalle.data;

import java.util.List;

public interface IMyActionExecutor {

    //TODO: in futuro passare oggetto bean rappresentante i dati da ricercare
    public void loadFilter(ColumnToSearch[] columns);

}
