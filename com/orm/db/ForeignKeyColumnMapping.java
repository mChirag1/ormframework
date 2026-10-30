package com.orm.db;
public class ForeignKeyColumnMapping
{
private String childTableColumnName;
private String parentTableColumnName;
public ForeignKeyColumnMapping()
{
}
public String getChildTableColumnName()
{
return childTableColumnName;
}
public void setChildTableColumnName(String childTableColumnName)
{
this.childTableColumnName=childTableColumnName;
}
public String getParentTableColumnName()
{
return parentTableColumnName;
}
public void setParentTableColumnName(String parentTableColumnName)
{
this.parentTableColumnName=parentTableColumnName;
}
}
