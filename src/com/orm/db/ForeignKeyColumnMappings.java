package com.orm.db;
import java.util.*;
public class ForeignKeyColumnMappings
{
private String parentTableName; 
private Map<String,ParentColumn> parentColumnsMap; 
public ForeignKeyColumnMappings(String parentTableName)
{
this.parentTableName=parentTableName;
this.parentColumnsMap = new HashMap<>();
}
public String getParentTableName()
{
return this.parentTableName;
}
public Map<String,ParentColumn> getParentColumnsMap()
{
return this.parentColumnsMap;
}
public void setParentColumnsMap(Map<String,ParentColumn> parentColumnsMap)
{
this.parentColumnsMap=parentColumnsMap;
}
}