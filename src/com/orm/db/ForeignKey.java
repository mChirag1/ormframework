package com.orm.db;
public class ForeignKey
{
String foreignKeyName;
String foreignKeyColumnName;
String foreignKeyTableName;
String parentTableName;
String parentTableColumnName;
int keySequence;
public ForeignKey()
{
this.foreignKeyName="";
this.foreignKeyColumnName="";
this.foreignKeyTableName="";
this.parentTableName="";
this.parentTableColumnName="";
this.keySequence=0;
}
public void setForeignKeyName(String foreignKeyName)
{
this.foreignKeyName=foreignKeyName;
}
public String getForeignKeyName()
{
return this.foreignKeyName;
}
public void setForeignKeyColumnName(String foreignKeyColumnName)
{
this.foreignKeyColumnName=foreignKeyColumnName;
}
public String getForeignKeyColumnName()
{
return this.foreignKeyColumnName;
}
public void setForeignKeyTableName(String foreignKeyTableName)
{
this.foreignKeyTableName=foreignKeyTableName;
}
public String getForeignKeyTableName()
{
return this.foreignKeyTableName;
}
public void setParentTableName(String parentTableName)
{
this.parentTableName=parentTableName;
}
public String getParentTableName()
{
return this.parentTableName;
}
public void setParentTableColumnName(String parentTableColumnName)
{
this.parentTableColumnName=parentTableColumnName;
}
public String getParentTableColumnName()
{
return this.parentTableColumnName;
}
public void setKeySequence(int keySequence)
{
this.keySequence=keySequence;
}
public int getKeySequence()
{
return this.keySequence;
}
}