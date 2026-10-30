package com.orm.db;
import java.util.*;
public class ForeignKey
{
private String parentTableName; 
private List<ForeignKeyColumnMapping> foreignKeyColumnMappings;
public ForeignKey()
{
this.foreignKeyColumnMappings=new ArrayList<>();
}
public void setParentTableName(String parentTableName)
{
this.parentTableName = parentTableName;
}
public String getParentTableName()
{
return parentTableName;
}
public List<ForeignKeyColumnMapping> getForeignKeyColumnMappings()
{
return foreignKeyColumnMappings;
}
public void setForeignKeyColumnMappings(List<ForeignKeyColumnMapping> foreignKeyColumnMappings)
{
this.foreignKeyColumnMappings=foreignKeyColumnMappings;
}
}