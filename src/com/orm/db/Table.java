package com.orm.db;
import java.util.*;
public class Table
{
private String name;
private List<TableColumn> tableColumns;
private Map<String,String> foreignKeyNamesMap; 
private Map<String,ForeignKeyColumnMappings> foreignKeyColumnMappingsMap;
public void setForeignKeyNamesMap(Map<String,String> foreignKeyNamesMap)
{
this.foreignKeyNamesMap=foreignKeyNamesMap;
}
public void setForeignKeyColumnMappingsMap(Map<String,ForeignKeyColumnMappings> foreignKeyColumnMappingsMap)
{
this.foreignKeyColumnMappingsMap=foreignKeyColumnMappingsMap;
}
public Map<String,String> getForeignKeyNamesMap()
{
return this.foreignKeyNamesMap;
}
public Map<String,ForeignKeyColumnMappings> getForeignKeyColumnMappingsMap()
{
return this.foreignKeyColumnMappingsMap;
}

public void setName(String name)
{
this.name = name;
}
public String getName()
{
return name;
}
public void setTableColumns(List<TableColumn> tableColumns)
{
this.tableColumns = tableColumns;
}
public List<TableColumn> getTableColumns()
{
return tableColumns;
}
public void addTableColumn(TableColumn tableColumn)
{
tableColumns.add(tableColumn);
}
public TableColumn getTableColumn(String columnName)
{
for(TableColumn tableColumn : tableColumns)
{
if(tableColumn.getName().equalsIgnoreCase(columnName))
{
return tableColumn;
}
}
return null;
}
}