package com.blogspot.fravalle.data.orm.derby.cayenne.iw3d;

import com.blogspot.fravalle.data.orm.derby.cayenne.iw3d.auto._Datamap_iw3d;

public class Datamap_iw3d extends _Datamap_iw3d {

    private static Datamap_iw3d instance;

    private Datamap_iw3d() {}

    public static Datamap_iw3d getInstance() {
        if(instance == null) {
            instance = new Datamap_iw3d();
        }

        return instance;
    }
}
