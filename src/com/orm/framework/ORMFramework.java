package com.orm.framework;
import com.orm.db.*;
import java.sql.*;
import com.orm.annotation.*;
import com.orm.exceptions.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
class SQLStatement
{
Map<String,Method> setterMethodForForeignKeys;
boolean hasAutoIncrementColumn;
Method setterMethodForAutoIncrementColumn;
ArrayList<ArrayList<Method>> listOfInsertMethods;
ArrayList<ArrayList<Method>> listOfUpdateMethods;
String insertStatement;
String updateStatement;
String deleteStatement;
String selectStatement;
String selectByPrimaryKeyStatement;
}
public class ORMFramework
{
private static ORMFramework ormFramework;
static private Map<String,SQLStatement> sqlStatementsMap;
private ORMFramework()
{
}
static private void init()
{
sqlStatementsMap=new HashMap<>();
try
{
File file=new File("orm.conf");
if(file.exists()==false) throw new Exception("orm.conf missing");
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"r");
String packageContainer=null;
String dtoPackage=null;
String line;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
line=randomAccessFile.readLine();
line=line.replace(" ","");
if(line.startsWith("package-container=")) packageContainer=line.substring(18);
else if(line.startsWith("dto-package=")) dtoPackage=line.substring(12);
}
randomAccessFile.close();
if(packageContainer==null && dtoPackage==null)
{
throw new  Exception("package-container and dto-package missing  in orm.conf");
}
if(packageContainer==null)
{
throw new  Exception("package-container missing  in orm.conf");
}
if(dtoPackage==null)
{
throw new  Exception("dto-package missing  in orm.conf");
}
File packageContainerFile=new File(packageContainer);
if(packageContainerFile.exists()==false)
{
throw new  Exception(packageContainer+" does not exist");
}
if(packageContainerFile.isDirectory()==false)
{
throw new  Exception(packageContainer+" is not a directory");
}
String classContainer=packageContainer;
if(classContainer.endsWith(File.separator)==false)
{
classContainer+=File.separator;
}
classContainer+=(dtoPackage.replace(".",File.separator));
File classContainerFile=new File(classContainer);
if(classContainerFile.exists()==false)
{
throw new Exception(classContainer+" does not exist");
}
if(classContainerFile.isDirectory()==false)
{
throw new Exception(classContainer+" is not a directory");
}
File classFiles[]=classContainerFile.listFiles(new FileFilter(){
public boolean accept(File f)
{
return f.getName().endsWith(".class");
}
});
String  className;
Class c;
SQLStatement sqlStatements;
for(File classFile:classFiles)
{
className=dtoPackage+"."+classFile.getName();
className=className.substring(0,className.length()-6);
try
{
c=Class.forName(className);
sqlStatements=getSQLStatements(c);
if(sqlStatements!=null)
{
sqlStatementsMap.put(c.getName(),sqlStatements);
}
}catch(Exception e)
{
System.out.println(e);
continue;
}
}
}catch(Exception exception)
{
System.out.println(exception);
System.exit(1);
}
}
public static ORMFramework getInstance() throws ORMException
{
if(ormFramework==null) init();
ormFramework=new ORMFramework();
return ormFramework;
}
public static SQLStatement getSQLStatements(Class c) throws ORMException
{
Class preparedStatementClass=java.sql.PreparedStatement.class;
Map<String,Method> preparedStatementSetterMethodsMap;
preparedStatementSetterMethodsMap=new HashMap<>();
try
{
try
{
preparedStatementSetterMethodsMap.put("long",preparedStatementClass.getMethod("setLong",int.class,long.class));
preparedStatementSetterMethodsMap.put("int",preparedStatementClass.getMethod("setInt",int.class,int.class));
preparedStatementSetterMethodsMap.put("short",preparedStatementClass.getMethod("setShort",int.class,short.class));
preparedStatementSetterMethodsMap.put("byte",preparedStatementClass.getMethod("setByte",int.class,byte.class));
preparedStatementSetterMethodsMap.put("double",preparedStatementClass.getMethod("setDouble",int.class,double.class));
preparedStatementSetterMethodsMap.put("float",preparedStatementClass.getMethod("setFloat",int.class,float.class));
preparedStatementSetterMethodsMap.put("boolean",preparedStatementClass.getMethod("setBoolean",int.class,boolean.class));
preparedStatementSetterMethodsMap.put("java.sql.Date",preparedStatementClass.getMethod("setDate",int.class,java.sql.Date.class));
preparedStatementSetterMethodsMap.put("java.sql.Timestamp",preparedStatementClass.getMethod("setTimestamp",int.class,java.sql.Timestamp.class));
preparedStatementSetterMethodsMap.put("java.math.BigDecimal",preparedStatementClass.getMethod("setBigDecimal",int.class,java.math.BigDecimal.class));
preparedStatementSetterMethodsMap.put("java.lang.String",preparedStatementClass.getMethod("setString",int.class,java.lang.String.class));
}catch(Exception e)
{
System.out.println(e);
System.exit(1);
}
SQLStatement sqlStatements;
sqlStatements=new SQLStatement();
com.orm.annotation.Table table;
table=(com.orm.annotation.Table)c.getAnnotation(com.orm.annotation.Table.class);
if(table==null) return null;
String tableName=table.name();
String selectStatement;
String selectByPrimaryKeyStatement;
selectStatement="select * from "+tableName;
String insertStatement;
insertStatement="insert into "+tableName+" (";
String updateStatement;
updateStatement="update "+tableName+" set ";
String questionMarks;
String columnName;
Field fields[]=c.getDeclaredFields();
String fieldName;
com.orm.annotation.ForeignKey foreignKey;
com.orm.annotation.Column column;
int i;
Set<String> primaryKeys;
primaryKeys=new HashSet<>();
List<Field> primaryKeyFields=new ArrayList<>();
String setterMethodName;
sqlStatements.hasAutoIncrementColumn=false;
if(c.isAnnotationPresent(com.orm.annotation.CompositeKey.class))
{
com.orm.annotation.CompositeKey compositeKey;
compositeKey=(com.orm.annotation.CompositeKey)c.getAnnotation(com.orm.annotation.CompositeKey.class);
String [] primaryKeyColumns=compositeKey.columns();
for(String p:primaryKeyColumns) 
{
primaryKeys.add(p);
for(Field fff:fields)
{
if(fff.isAnnotationPresent(com.orm.annotation.Column.class))
{
com.orm.annotation.Column ccc=(com.orm.annotation.Column)fff.getAnnotation(com.orm.annotation.Column.class);
if(ccc.name().equals(p)) 
{
primaryKeyFields.add(fff);
}
}
else if(fff.isAnnotationPresent(com.orm.annotation.ForeignKey.class)){
com.orm.annotation.ForeignKey ddd=(com.orm.annotation.ForeignKey)fff.getAnnotation(com.orm.annotation.ForeignKey.class);
if(ddd.columnName().equals(p)) 
{
System.out.println("Adding "+fff.getName()+" from  "+c.getName()+" to primary key fields in case of composite key");
primaryKeyFields.add(fff);
}
}
}
}
	
}
else
{
for(i=0;i<fields.length;++i)
{
fieldName=fields[i].getName();
if(fields[i].isAnnotationPresent(com.orm.annotation.Auto.class))
{
setterMethodName="set"+fieldName.substring(0,1).toUpperCase();
if(fieldName.length()>1) setterMethodName+=fieldName.substring(1);
sqlStatements.hasAutoIncrementColumn=true;
sqlStatements.setterMethodForAutoIncrementColumn=c.getMethod(setterMethodName,fields[i].getType());
}
if(fields[i].isAnnotationPresent(com.orm.annotation.PrimaryKey.class))
{
primaryKeyFields.add(fields[i]);
if(fields[i].isAnnotationPresent(com.orm.annotation.Column.class))
{
column=(com.orm.annotation.Column)fields[i].getAnnotation(com.orm.annotation.Column.class);
columnName=column.name();
primaryKeys.add(columnName);
}else if(fields[i].isAnnotationPresent(com.orm.annotation.ForeignKey.class))
{
foreignKey=(com.orm.annotation.ForeignKey)fields[i].getAnnotation(com.orm.annotation.ForeignKey.class);
columnName=foreignKey.columnName();
primaryKeys.add(columnName);
}
}
}
}
questionMarks="";
String getterMethodName;
sqlStatements.listOfUpdateMethods=new ArrayList<>();
ArrayList<Method> methodsListForUpdateStatement;
ArrayList<ArrayList<Method>> list=new ArrayList<>();
ArrayList<Method> methodsList;
Method method;
for(i=0;i<fields.length;++i)
{
fieldName=fields[i].getName();
methodsList=new ArrayList<>();
columnName=null;
if(fields[i].isAnnotationPresent(com.orm.annotation.Column.class))
{
column=(com.orm.annotation.Column)fields[i].getAnnotation(com.orm.annotation.Column.class);
columnName=column.name();
insertStatement+=columnName;
if(primaryKeys.contains(columnName)==false)
{
updateStatement+=columnName+"=?";
methodsListForUpdateStatement=new ArrayList<>();
methodsListForUpdateStatement.add(preparedStatementSetterMethodsMap.get(fields[i].getType().getName()));
getterMethodName="get"+fieldName.substring(0,1).toUpperCase();
if(fieldName.length()>0)
{
getterMethodName+=fieldName.substring(1);
}
methodsListForUpdateStatement.add(c.getMethod(getterMethodName));
sqlStatements.listOfUpdateMethods.add(methodsListForUpdateStatement);
}
questionMarks+="?";
// which setter part
methodsList.add(preparedStatementSetterMethodsMap.get(fields[i].getType().getName()));
getterMethodName="get"+fieldName.substring(0,1).toUpperCase();
if(fieldName.length()>0)
{
getterMethodName+=fieldName.substring(1);
}
methodsList.add(c.getMethod(getterMethodName));
list.add(methodsList);
if(i<fields.length-1) 
{
insertStatement+=",";
questionMarks+=",";
if(primaryKeys.contains(columnName)==false)
{
updateStatement+=",";
}
}
}else if(fields[i].isAnnotationPresent(com.orm.annotation.ForeignKey.class))
{
foreignKey=(com.orm.annotation.ForeignKey)fields[i].getAnnotation(com.orm.annotation.ForeignKey.class);
columnName=foreignKey.columnName();
insertStatement+=columnName;
questionMarks+="?";
// populate list with 1 setter and  many getters
list.add(methodsList);
if(i<fields.length-1) 
{
insertStatement+=",";
questionMarks+=",";
}
}
}
insertStatement+=(") values("+questionMarks+")");
String whereClause=" where ";
int index=0;
Field field;
for(String pk:primaryKeys) 
{
whereClause+=(pk+"=? and ");
methodsListForUpdateStatement=new ArrayList<>();
field=primaryKeyFields.get(index);
methodsListForUpdateStatement.add(preparedStatementSetterMethodsMap.get(field.getType().getName()));
fieldName=field.getName();
getterMethodName="get"+fieldName.substring(0,1).toUpperCase();
if(fieldName.length()>0)
{
getterMethodName+=fieldName.substring(1);
}
methodsListForUpdateStatement.add(c.getMethod(getterMethodName));
sqlStatements.listOfUpdateMethods.add(methodsListForUpdateStatement);
index++;
}
whereClause=whereClause.substring(0,whereClause.length()-4);
updateStatement+=whereClause;
String deleteStatement;
deleteStatement="delete from "+tableName+whereClause;
selectByPrimaryKeyStatement="select * from "+tableName+whereClause;
sqlStatements.insertStatement=insertStatement;
sqlStatements.updateStatement=updateStatement;
sqlStatements.deleteStatement=deleteStatement;
sqlStatements.selectStatement=selectStatement;
sqlStatements.selectByPrimaryKeyStatement=selectByPrimaryKeyStatement;
sqlStatements.listOfInsertMethods=list;
//System.out.println(insertStatement);
//System.out.println(updateStatement);
//System.out.println(deleteStatement);
//System.out.println(selectStatement);
//System.out.println(selectByPrimaryKeyStatement);
return sqlStatements;
}catch(Exception exception)
{
System.out.println("Cool");
exception.printStackTrace();
throw new ORMException(exception.getMessage());
}
}
public void insert(Object obj) throws ORMException
{
try
{
String className=obj.getClass().getName();
SQLStatement sqlStatements=sqlStatementsMap.get(className);
if(sqlStatements==null) throw new Exception(className+" is not an mapped to any table");
String insertStatement=sqlStatements.insertStatement;
Connection connection=DBConnector.connect();
PreparedStatement preparedStatement;
if(sqlStatements.hasAutoIncrementColumn)
{
preparedStatement=connection.prepareStatement(insertStatement,Statement.RETURN_GENERATED_KEYS);
}
else
{
preparedStatement=connection.prepareStatement(insertStatement);
}
int questionMarkNumber=1;
ArrayList<ArrayList<Method>> list;
list=sqlStatements.listOfInsertMethods;
ArrayList<Method> methodsList;
Method setterMethod;
Method getterMethod;
int index=0;
int idx;
while(index<list.size())
{
methodsList=list.get(index);
setterMethod=methodsList.get(0); 
// method  related to setter method to
// be called for replacing  question mark will be at 0 index
Object k=obj;
Object result=null;
idx=1;
while(idx<methodsList.size())
{
getterMethod=methodsList.get(idx);
result=getterMethod.invoke(k);
k=result;
idx++;
}
setterMethod.invoke(preparedStatement,questionMarkNumber,result);
++index;
questionMarkNumber++;
}
preparedStatement.executeUpdate();
if(sqlStatements.hasAutoIncrementColumn)
{
ResultSet resultSet;
resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int generatedKey=resultSet.getInt(1);
resultSet.close();
sqlStatements.setterMethodForAutoIncrementColumn.invoke(obj,generatedKey);
}
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
exception.printStackTrace();
throw new ORMException(exception.getMessage());
}
}
public void update(Object obj) throws ORMException
{
try
{
String className=obj.getClass().getName();
SQLStatement sqlStatements=sqlStatementsMap.get(className);
if(sqlStatements==null) throw new Exception(className+" is not an mapped to any table");
String updateStatement=sqlStatements.updateStatement;
Connection connection=DBConnector.connect();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement(updateStatement);
int questionMarkNumber=1;
ArrayList<ArrayList<Method>> list;
list=sqlStatements.listOfUpdateMethods;
ArrayList<Method> methodsList;
Method setterMethod;
Method getterMethod;
int index=0;
int idx;
while(index<list.size())
{
methodsList=list.get(index);
setterMethod=methodsList.get(0); 
// method  related to setter method to
// be called for replacing  question mark will be at 0 index
Object k=obj;
Object result=null;
idx=1;
while(idx<methodsList.size())
{
getterMethod=methodsList.get(idx);
result=getterMethod.invoke(k);
k=result;
idx++;
}
setterMethod.invoke(preparedStatement,questionMarkNumber,result);
++index;
questionMarkNumber++;
}
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
exception.printStackTrace();
throw new ORMException(exception.getMessage());
}
}
/*
public void delete(Object obj,Class c) throws ORMException
{
try
{
String className=c.getName();
SQLStatement sqlStatements=sqlStatementsMap.get(className);
if(sqlStatements==null) throw new Exception(className+" is not an mapped to any table");
String deleteStatement=sqlStatements.updateStatement;
Connection connection=DBConnector.connect();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement(deleteStatement);
int questionMarkNumber=1;
ArrayList<ArrayList<Method>> list;
list=sqlStatements.listOfDeleteMethods;
ArrayList<Method> methodsList;
Method setterMethod;
Method getterMethod;
int index=0;
int idx;
while(index<list.size())
{
methodsList=list.get(index);
setterMethod=methodsList.get(0); 
// method  related to setter method to
// be called for replacing  question mark will be at 0 index
Object k=obj;
Object result=null;
idx=1;
while(idx<methodsList.size())
{
getterMethod=methodsList.get(idx);
result=getterMethod.invoke(k);
k=result;
idx++;
}
setterMethod.invoke(preparedStatement,questionMarkNumber,result);
++index;
questionMarkNumber++;
}
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
exception.printStackTrace();
throw new ORMException(exception.getMessage());
}
}*/
}
