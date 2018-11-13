package org.thingsboard.server.dao.model.wrapper;


import com.datastax.driver.core.ResultSet;

public class EntityResultSet<T> {

  private ResultSet resultSet;
  private T entity;

  public EntityResultSet(ResultSet resultSet, T entity) {
    this.resultSet = resultSet;
    this.entity = entity;
  }

  public T getEntity() {
    return entity;
  }

  public boolean wasApplied() {
    return resultSet.wasApplied();
  }
}
