package edu.tcu.cs.frogcrewbackend.system.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String objectName, String criteria) {
        super("Could not find " + objectName + " with criteria " + criteria);
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find " + objectName + " with Id " + id);
    }
}
