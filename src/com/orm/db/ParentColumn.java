package com.orm.db;
public class ParentColumn
{
private String name;
private int keySequence;
public ParentColumn(String name,int keySequence)
{
this.name=name;
this.keySequence=keySequence;
}
public String getName()
{
return this.name;
}
public int getKeySequence()
{
return this.keySequence;
}
}