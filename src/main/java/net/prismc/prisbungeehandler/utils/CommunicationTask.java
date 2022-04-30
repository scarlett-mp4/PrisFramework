package net.prismc.prisbungeehandler.utils;

public abstract class CommunicationTask {
    protected final String NAME;

    protected CommunicationTask(String pName) {
        NAME = pName;
    }

    public boolean isApplicable(String pName) {
        return pName.equals(NAME);
    }
}