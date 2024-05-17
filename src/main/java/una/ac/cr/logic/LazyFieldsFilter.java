package una.ac.cr.logic;

import jakarta.persistence.Persistence;

public class LazyFieldsFilter {
    @Override
    public boolean equals(Object obj){
        return obj==null || !Persistence.getPersistenceUtil().isLoaded(obj);
    }
}
