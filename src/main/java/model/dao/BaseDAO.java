package model.dao;

import control.controlexceptions.InternalException;
import model.modelexceptions.DuplicatedEntityException;

import java.util.Map;

public interface BaseDAO {
    Object createEntity(Map<String, String> creationInfo) throws DuplicatedEntityException, InternalException;
}
