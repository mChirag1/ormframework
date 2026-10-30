package com.orm.framework;
import com.orm.db.*;
import com.orm.annotation.*;
import com.orm.exceptions.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
public class ORMFramework 
{
class SQLStatement
{

}
public static ORMFramework ormFramework;
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
if(file.exists()==false) throw new Exception("orm.conf is missing");
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"r");
String packageContainer=null;
String dtoPackage=null;
String line;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
line=randomAccessFile.readLine();
line=line.replace(" ","");
if(line.startsWith("package-container="))packageContainer=line.substring(18);
else if(line.startsWith("dto-package=")) dtoPackage=line.substring(12);
}
randomAccessFile.close();
if(packageContainer==null && dtoPackage==null)
{
throw new Exception("package-container and dto-package missing in orm.conf");
}
if(packageContainer==null)
{
throw new Exception("package-conatiner missing in orm.conf");
}
if(dtoPackage==null)
{
throw new Exception("dto-package missing in orm.conf");
}
File packageContainerFile=new File(packageContainer);
if(packageContainerFile.exists()==false)
{
throw new Exception("package-container "+"does not exist.");
}
if(packageContainerFile.isDirectory()==false)
{
throw new Exception("packageContainer "+"is not a directory.");
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
throw new Exception(classContainer+"does not exists.");
}
if(classContainerFile.isDirectory()==false)
{
throw new Exception(classContainer+" is not a directory.");
}
File classFiles[]=classContainerFile.listFiles(new FileFilter(){
public boolean accept(File f)
{
return f.getName().endsWith(".class");
}
});
String className;
Class c;
SQLStatement sqlStatements;
for(File classFile:classFiles)
{
className=dtoPackage+"."+classFile.getName();
className=className.substring(0,className.length()-6);
try
{
System.out.println("Loading : "+className);
c=Class.forName(className);
sqlStatements=getSQLStatements(c);
if(sqlStatements!=null)
{
sqlStatementsMap.put(c.getName(),getSQLStatements(c));
System.out.prinln("added "+c.getName()+"to Map");
}
}catch(Exception e)
{
System.out.println(e.getMessage());
continue;
}
}
}catch(Exception exception)
{
System.out.println(exception.getMessage());
System.exit(1);
}
}
public static ORMFramework getInstance()
{
if(ormFramework==null) init();
return ormFramework;
}
}