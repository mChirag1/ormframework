/*
Assignment
Make changes,
get (2) maps related to foreign key from table
Phase 1
while generating private properties, constructors and setter
and getter make the necessary changes
*/
package  com.orm;
import java.io.*;
import java.sql.*;
import com.orm.db.*;
import com.orm.exceptions.*;
import com.orm.annotation.*;
import java.util.*;
public class DTOGenerator
{
private static Map<String,String> javaTypes;
private static Map<String,String> defaultValues;
static
{
javaTypes=new HashMap<String,String>();
javaTypes.put("INT","int");
javaTypes.put("BIGINT","long");
javaTypes.put("SMALLINT","short");
javaTypes.put("TINYINT","byte");
javaTypes.put("DECIMAL","java.math.BigDecimal");
javaTypes.put("FLOAT","float");
javaTypes.put("DOUBLE","double");
javaTypes.put("VARCHAR","String");
javaTypes.put("TEXT","String");
javaTypes.put("CHAR","String");
javaTypes.put("DATE","java.sql.Date");
javaTypes.put("DATETIME","java.sql.TimeStamp");
javaTypes.put("TIMESTAMP","java.sql.TimeStamp");
javaTypes.put("BOOLEAN","boolean");
javaTypes.put("BIT","boolean");
// add more (do it on your own)
defaultValues=new HashMap<String,String>();
defaultValues.put("int","0");
defaultValues.put("long","0");
defaultValues.put("short","0");
defaultValues.put("byte","0");
defaultValues.put("java.math.BigDecimal","new java.math.BigDecimal(\"0.0\")");
defaultValues.put("float","0.0f");
defaultValues.put("double","0.0");
defaultValues.put("String","\"\"");
defaultValues.put("String","\"\"");
defaultValues.put("String","\"\"");
defaultValues.put("java.sql.Date","null");
defaultValues.put("java.sql.TimeStamp","null");
defaultValues.put("java.sql.TimeStamp","null");
defaultValues.put("boolean","false");
// add more (do it on your own)
}
public static void main(String args[])
{
if(args.length!=2)
{
System.out.println("Pass target-path:dirname package-name:package_name as arguments");
System.exit(1);
}
try
{
String targetPath=null;
String packageName=null;
String key;
int index;
for(int i=0;i<args.length;++i)
{
index=args[i].indexOf(':');
if(index!=-1)
{
key=args[i].substring(0,index);
if(key.equalsIgnoreCase("package-name")) packageName=args[i].substring(index+1);
else if(key.equalsIgnoreCase("target-path")) targetPath=args[i].substring(index+1);
}
}
if(packageName==null || targetPath==null)
{
boolean printedSomething=false;
System.out.print("Specify :");
if(targetPath==null)
{
System.out.print("target-name");
printedSomething=true;
}
if(packageName==null)
{
if(printedSomething) System.out.print(" and ");
System.out.print("package-name");
}
System.out.print("\n");
System.exit(1);
}
Connection connection=DBConnector.connect();
Database database=DatabaseUtil.getDatabase(connection);
File targetDirectory=new File(targetPath);
if(targetDirectory.exists()==false) 
{
if(!targetDirectory.mkdirs())
{
throw new IOException("Unable to create : "+targetDirectory);
}
}
else if(!targetDirectory.isDirectory())
{
throw new IOException(targetDirectory+" is not a directory");
}
String packageDirectoryPath=packageName.replace(".",File.separator);
File packageDirectoryFile=new File(targetPath,packageDirectoryPath);
if(packageDirectoryFile.exists()==false)
{
if(packageDirectoryFile.mkdirs()==false)
{
throw new  Exception("Unable to create package directory : "+packageName);
}
}else if(packageDirectoryFile.isDirectory()==false)
{
throw new  Exception(packageName+" path exists and  is not a directory");
}
generateDTO(database,packageDirectoryFile,packageName);
connection.close();
}catch(Throwable throwable)
{
System.out.print(throwable.getMessage());
System.exit(1);
}
}
private static void generateDTO(Database database,File packageDirectory,String packageName) throws Exception
{
try
{
int index;
Vector<String> properties=new Vector<>();
Vector<String> propertyTypes=new Vector<>();
Vector<String> tablePrimaryKeys=new Vector<>();
Vector<String> primaryKeys=new Vector<>();
Vector<String> primaryKeyTypes=new Vector<>();
List<com.orm.db.Table> tables=database.getTables();
List<TableColumn> tableColumns;
if(tables.size()==0) throw new Exception("Nothing to generate");
String javaFileName;
File javaFile;
String defaultValue;
String className;
String propertyName;
String tableName;
RandomAccessFile  randomAccessFile;
String line;
String javaPropertyName;
String  javaPropertyType;
int counter;
String classPointerName;
String pkName,pkType;
TableColumn tc;
ForeignKeyColumnMappings foreignKeyColumnMappings;
Map<String,ForeignKeyColumnMappings> foreignKeyColumnMappingsMap;
Map<String,ParentColumn> parentColumnsMap;
String  foreignKeyName;
Map<String,String> foreignKeyNamesMap;
String  parentTableColumnName;
String  parentTableName;
ParentColumn parentColumn;
for(com.orm.db.Table t:tables)
{
foreignKeyColumnMappingsMap=t.getForeignKeyColumnMappingsMap();
foreignKeyNamesMap=t.getForeignKeyNamesMap();
tablePrimaryKeys.clear();
primaryKeys.clear();
primaryKeyTypes.clear();
properties.clear();
propertyTypes.clear();
tableName=t.getName();
className=getClassName(tableName);
classPointerName=className.substring(0,1).toLowerCase();
if(className.length()>1) classPointerName+=className.substring(1);
javaFileName=className+".java";
tableColumns=t.getTableColumns();
for(TableColumn tableColumn:tableColumns)
{
foreignKeyName=foreignKeyNamesMap.get(tableColumn.getName());
if(foreignKeyName!=null)
{
foreignKeyColumnMappings=foreignKeyColumnMappingsMap.get(foreignKeyName);
parentTableName=foreignKeyColumnMappings.getParentTableName();
parentColumnsMap=foreignKeyColumnMappings.getParentColumnsMap();
parentColumn=parentColumnsMap.get(tableColumn.getName());
if(parentColumn.getKeySequence()>1) continue;
javaPropertyName=getPropertyName(parentTableName);
properties.add(javaPropertyName);
javaPropertyType=javaPropertyName.substring(0,1).toUpperCase();
if(javaPropertyName.length()>0) 
{
javaPropertyType=javaPropertyType+javaPropertyName.substring(1);
}
propertyTypes.add(javaPropertyType);
if(tableColumn.getHasPrimaryKeyConstraintApplied()) tablePrimaryKeys.add(tableColumn.getName());
if(tableColumn.getHasPrimaryKeyConstraintApplied())
{
primaryKeys.add(javaPropertyName);
primaryKeyTypes.add(javaPropertyType);
}
}
else
{
if(tableColumn.getHasPrimaryKeyConstraintApplied()) tablePrimaryKeys.add(tableColumn.getName());
javaPropertyName=getPropertyName(tableColumn.getName());
properties.add(javaPropertyName);
javaPropertyType=javaTypes.get(tableColumn.getType());
propertyTypes.add(javaPropertyType);
if(tableColumn.getHasPrimaryKeyConstraintApplied())
{
primaryKeys.add(javaPropertyName);
primaryKeyTypes.add(javaPropertyType);
}
}
}
javaFile=new File(packageDirectory,javaFileName);
if(javaFile.exists()) javaFile.delete();
randomAccessFile=new RandomAccessFile(javaFile,"rw");
line="package "+packageName+";\n";
randomAccessFile.writeBytes(line);
line="import com.orm.annotation.*;\n";
randomAccessFile.writeBytes(line);
line="@Table(name=\""+tableName+"\")\n";
randomAccessFile.writeBytes(line);
if(tablePrimaryKeys.size()>1)
{
line="@CompositeKey(columns={";
randomAccessFile.writeBytes(line);
index=0;
while(index<tablePrimaryKeys.size())
{
if(index<tablePrimaryKeys.size()-1)
{
randomAccessFile.writeBytes("\""+tablePrimaryKeys.get(index)+"\",");
}
else
{
randomAccessFile.writeBytes("\""+tablePrimaryKeys.get(index)+"\"");
}
++index;
}
randomAccessFile.writeBytes("})\n");
}
line="public class "+className+" implements  java.io.Serializable,Comparable<"+className+">\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
// property declaration starts
for(index=0;index<properties.size();++index)
{
tc=tableColumns.get(index);
foreignKeyName=foreignKeyNamesMap.get(tc.getName());
if(foreignKeyName==null)
{
if(tc.getHasNotNullConstraintApplied())
{
randomAccessFile.writeBytes("@NotNull\n");
}
randomAccessFile.writeBytes("@MaxLength("+tc.getWidth()+")\n");
if(tc.getType().equals("DECIMAL"))
{
randomAccessFile.writeBytes("@Precision("+tc.getPrecision()+")\n");
}
if(tc.getIsAutoIncrementable())
{
randomAccessFile.writeBytes("@Auto\n");
}
if(tc.getHasUniqueConstraintApplied() && tc.getHasPrimaryKeyConstraintApplied()==false)
{
randomAccessFile.writeBytes("@Unique\n");
}
if(tc.getHasPrimaryKeyConstraintApplied() && tablePrimaryKeys.size()==1)
{ 
randomAccessFile.writeBytes("@PrimaryKey\n");
}
randomAccessFile.writeBytes("@Column(name=\""+tc.getName()+"\")\n");
}
else
{
foreignKeyColumnMappings=foreignKeyColumnMappingsMap.get(foreignKeyName);
parentColumnsMap=foreignKeyColumnMappings.getParentColumnsMap();
parentColumn=parentColumnsMap.get(tc.getName());
if(tc.getHasNotNullConstraintApplied())
{
randomAccessFile.writeBytes("@NotNull\n");
}
if(tc.getHasUniqueConstraintApplied() && tc.getHasPrimaryKeyConstraintApplied()==false)
{
randomAccessFile.writeBytes("@Unique\n");
}
if(tc.getHasPrimaryKeyConstraintApplied() && tablePrimaryKeys.size()==1)
{
randomAccessFile.writeBytes("@PrimaryKey\n");
}
randomAccessFile.writeBytes("@ForeignKey(columnName=\""+tc.getName()+"\",parentColumnName=\""+parentColumn.getName()+"\")\n");
}
line="private "+propertyTypes.get(index)+" "+properties.get(index)+";\n";
randomAccessFile.writeBytes(line);
randomAccessFile.writeBytes("\n");
}
//default constructor starts
line="public "+className+"()\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
index=0;
for(String prop:properties)
{
defaultValue=defaultValues.get(propertyTypes.get(index));
line="this."+prop+"="+defaultValue+";\n";
randomAccessFile.writeBytes(line);
++index;
}
line="}\n";
randomAccessFile.writeBytes(line);
// parameterized  constructor starts
line="public "+className+"(";
randomAccessFile.writeBytes(line);
index=0;
line=propertyTypes.get(0)+" "+properties.get(0);
index=1;
while(index<properties.size())
{
line+=(","+propertyTypes.get(index)+" "+properties.get(index));
index++;
}
randomAccessFile.writeBytes(line);
line=")\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
for(String prop:properties)
{
line="this."+prop+"="+prop+";\n";
randomAccessFile.writeBytes(line);
}
line="}\n";
randomAccessFile.writeBytes(line);
// parameterized  constructor starts (leave auto fields)
for(counter=0,index=0;index<tableColumns.size();++index)
{
if(tableColumns.get(index).getIsAutoIncrementable()) counter++;
}
if(counter>0  && counter<tableColumns.size())
{
line="public "+className+"(";
randomAccessFile.writeBytes(line);
line="";
index=0;
while(index<properties.size())
{
if(tableColumns.get(index).getIsAutoIncrementable()) 
{
++index;
continue;
}
line+=(","+propertyTypes.get(index)+" "+properties.get(index));
index++;
}
line=line.substring(1);
randomAccessFile.writeBytes(line);
line=")\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
index=0;
for(String prop:properties)
{
if(tableColumns.get(index).getIsAutoIncrementable()) 
{
line="this."+prop+"="+defaultValues.get(propertyTypes.get(index))+";\n";
randomAccessFile.writeBytes(line);
++index;
continue;
}
line="this."+prop+"="+prop+";\n";
randomAccessFile.writeBytes(line);
index++;
}
line="}\n";
randomAccessFile.writeBytes(line);
}
// setter starts
index=0;
for(String prop:properties)
{
line="public void "+getSetterName(prop)+"("+propertyTypes.get(index)+" "+prop+")\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
line="this."+prop+"="+prop+";\n";
randomAccessFile.writeBytes(line);
line="}\n";
randomAccessFile.writeBytes(line);
index++;
}
// getter starts
index=0;
for(String prop:properties)
{
line="public "+propertyTypes.get(index)+" "+getGetterName(prop)+"()\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
line="return this."+prop+";\n";
randomAccessFile.writeBytes(line);
line="}\n";
randomAccessFile.writeBytes(line);
index++;
}
// equals starts
line="public boolean equals("+className+" "+classPointerName+")\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
randomAccessFile.writeBytes("return ");
index=0;
for(String prop:primaryKeys)
{
if(isPrimitive(primaryKeyTypes.get(index)))
{
randomAccessFile.writeBytes("this."+prop+"=="+classPointerName+"."+prop);
}
else
{
randomAccessFile.writeBytes("this."+prop+".equals("+classPointerName+"."+prop+")");
}
if(index<primaryKeys.size()-1)
{
randomAccessFile.writeBytes(" && ");
}
index++;
}
randomAccessFile.writeBytes(";\n");
line="}\n";
randomAccessFile.writeBytes(line);
// compareTo starts
randomAccessFile.writeBytes("public int compareTo("+className+" "+classPointerName+")\n");
randomAccessFile.writeBytes("{\n");
randomAccessFile.writeBytes("int ret;\n");
index=0;
while(index<primaryKeys.size())
{
pkName=primaryKeys.get(index);
pkType=primaryKeyTypes.get(index);
if(isPrimitive(pkType))
{
if(pkType.equals("boolean"))
{
randomAccessFile.writeBytes("ret=new  Boolean(this."+pkName+").compareTo("+classPointerName+"."+pkName+");\n");
}
else
{
randomAccessFile.writeBytes("ret=this."+pkName+"-"+classPointerName+"."+pkName+";\n");
}
}
else
{
randomAccessFile.writeBytes("ret=this."+pkName+".compareTo("+classPointerName+"."+pkName+");\n");
}
if(index<primaryKeys.size()-1)
{
randomAccessFile.writeBytes("if(ret!=0) return ret;\n");
}
else
{
randomAccessFile.writeBytes("return ret;\n");
}
++index;
}
randomAccessFile.writeBytes("}\n");
// hashCode starts
randomAccessFile.writeBytes("public int hashCode()\n");
randomAccessFile.writeBytes("{\n");
randomAccessFile.writeBytes("return java.util.Objects.hash(");
index=0;
while(index<primaryKeys.size())
{
pkName=primaryKeys.get(index);
if(index<primaryKeys.size()-1)
{
randomAccessFile.writeBytes("this."+pkName+",");
}
else
{
randomAccessFile.writeBytes("this."+pkName);
}
++index;
}
randomAccessFile.writeBytes(");\n");
randomAccessFile.writeBytes("}\n");
// optional, toString strats
// do it yourself
line="}\n"; // class ends
randomAccessFile.writeBytes(line);
randomAccessFile.close();
}
}catch(Throwable t)
{
throw new Exception(t.getMessage());
}
}
private static String  getClassName(String  tableName)
{
String className="";
//String []splits=tableName.split("[\\s_-]+");
String []splits=tableName.split("_");
for(String part:splits)
{
if(part.trim().length()==0) continue;
className+=part.substring(0,1).toUpperCase();
if(tableName.length()>0) className+=part.substring(1).toLowerCase();
}
return className;
}
public static String getPropertyName(String columnName)
{
String propertyName="";
//String []splits=columnName.split("[\\s_-]+");
String []splits=columnName.split("_");
for(String part:splits)
{
if(part.trim().length()==0) continue;
propertyName+=part.substring(0,1).toUpperCase();
if(propertyName.length()>0) propertyName+=part.substring(1).toLowerCase();
}
if(propertyName.length()>1) return propertyName.substring(0,1).toLowerCase()+propertyName.substring(
1);
else return propertyName.toLowerCase();
}
static private String getSetterName(String propertyName)
{
String str="set"+propertyName.substring(0,1).toUpperCase();
if(propertyName.length()>1)
{
str+=propertyName.substring(1);
}
return str;
}
static private String getGetterName(String propertyName)
{
String str="get"+propertyName.substring(0,1).toUpperCase();
if(propertyName.length()>1)
{
str+=propertyName.substring(1);
}
return str;
}
static private boolean isPrimitive(String javaType)
{
if(javaType.equals("long")) return true;
if(javaType.equals("int")) return true;
if(javaType.equals("short")) return true;
if(javaType.equals("byte")) return true;
if(javaType.equals("double")) return true;
if(javaType.equals("float")) return true;
if(javaType.equals("char")) return true;
if(javaType.equals("boolean")) return true;
return false;
}
}
