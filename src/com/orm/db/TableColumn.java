package com.orm.db;
public class TableColumn
{
private String name;
private int width;
private int precision;
private String type;
private boolean hasPrimaryKeyConstraintApplied;
private boolean isAutoIncrementable;
private boolean hasNotNullConstraintApplied;
private boolean hasUniqueConstraintApplied;
private String defaultValue;

public TableColumn()
{

}
public void setPrecision(int precision)
{
this.precision=precision;
}

public void setName(String name)
{
this.name=name;
}
public int getPrecision()
{
return precision;
}
public void setType(String type)
{
this.type=type;
}
public String getType()
{
return type;
}
public String getName()
{
return name;
}
public void setWidth(int width)
{
this.width=width;
}
public int getWidth()
{
return width;
}
public void setHasPrimaryKeyConstraintApplied(boolean hasPrimaryKeyConstraintApplied)
{
this.hasPrimaryKeyConstraintApplied=hasPrimaryKeyConstraintApplied;
}
public boolean getHasPrimaryKeyConstraintApplied()
{
return hasPrimaryKeyConstraintApplied;
}
public void setIsAutoIncrementable(boolean autoIncrementable)
{
isAutoIncrementable=autoIncrementable;
}
public boolean getIsAutoIncrementable()
{
return isAutoIncrementable;
}
public boolean getHasNotNullConstraintApplied()
{
return hasNotNullConstraintApplied;
}
public void setHasNotNullConstraintApplied(boolean hasNotNullConstraintApplied)
{
this.hasNotNullConstraintApplied=hasNotNullConstraintApplied;
}
public boolean getHasUniqueConstraintApplied()
{        
return hasUniqueConstraintApplied;
}
public void setHasUniqueConstraintApplied(boolean hasUniqueConstraintApplied)
{
this.hasUniqueConstraintApplied=hasUniqueConstraintApplied;
}
public String getDefaultValue()
{
return defaultValue;
}

public void setDefaultValue(String defaultValue)
{
this.defaultValue=defaultValue;
}
}