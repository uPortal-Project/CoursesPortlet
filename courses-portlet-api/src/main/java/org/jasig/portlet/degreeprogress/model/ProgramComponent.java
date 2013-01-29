package org.jasig.portlet.degreeprogress.model;

public class ProgramComponent {

    private final String key;
    private final String name;

    public ProgramComponent(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}