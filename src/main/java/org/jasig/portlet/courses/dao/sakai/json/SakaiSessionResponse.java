package org.jasig.portlet.courses.dao.sakai.json;

import java.io.Serializable;
import java.util.List;

public class SakaiSessionResponse implements Serializable {

    private String entityPrefix;
    private List<SakaiSession> session_collection;

    public String getEntityPrefix() {
        return entityPrefix;
    }

    public void setEntityPrefix(String entityPrefix) {
        this.entityPrefix = entityPrefix;
    }

    public List<SakaiSession> getSession_collection() {
        return session_collection;
    }

    public void setSession_collection(List<SakaiSession> session_collection) {
        this.session_collection = session_collection;
    }

}
