package com.orm.db;
import java.util.*;
public class Table
{
private String name;
private List<TableColumn> tableColumns;
private List<ForeignKey> foreignKeys;
public Table()
{
this.tableColumns = new ArrayList<>();
this.foreignKeys = new ArrayList<>();
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setTableColumns(List<TableColumn> tableColumns)
{
this.tableColumns=tableColumns;
}
public List<TableColumn> getTableColumns()
{
retunr tableColumns;
}
public void setForeignKeys(List<ForeignKey> foreignKeys)
{
this.foreignKeys = foreignKeys;
}
public List<ForeignKey> getForeignKeys()
{
return foreignKeys;
}
}
