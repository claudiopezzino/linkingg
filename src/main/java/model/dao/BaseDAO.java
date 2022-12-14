package model.dao;

import control.controlexceptions.InternalException;
import model.Filter;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;

import java.util.Map;

public interface BaseDAO {
    void createEntity(Map<String, String> creationInfo) throws DuplicatedEntityException, InternalException;
    <V> Object readEntity(Map<String, V> filter, Filter type) throws InternalException, NoEntityException;
    <V> Map<String, Object> readEntities(Map<String, V> filter, Filter type) throws InternalException, NoEntityException;
    <V> void updateEntity(Map<String, V> filter, Filter type) throws InternalException;
}
