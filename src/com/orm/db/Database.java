package com.orm.db;
import java.util.*;
public class Database
{
private List<Table> tables;
public Database()
{
this.tables=new ArrayList<>();
}
public void setTables(List<Table> tables)
{
this.tables = tables;
}
public List<Table> getTables()
{
return tables;
}
public void addTable(Table table)
{
tables.add(table);
}
public Table getTable(String tableName)
{
for(Table table : tables)
{
if(table.getName().equalsIgnoreCase(tableName))
{
return table;
}
}
return null;
}
}