import com.orm.db.*;
import java.util.*;
import com.orm.exceptions.*;
import java.sql.*;
class TestDatabaseMetaData
{
public static void main(String gg[])
{
Connection connection=DBConnector.connect();
try
{
Database database=DatabaseUtil.getDatabase(connection);
List<Table> tables=database.getTables();
List<TableColumn> tableColumns;
for(Table table:tables)
{
System.out.println("Table : "+table.getName());
tableColumns=table.getTableColumns();
for(TableColumn tc:tableColumns)
{
System.out.println("name:"+tc.getName());
System.out.println("width:"+tc.getWidth());
System.out.println("precision:"+tc.getPrecision());
System.out.println("type:"+tc.getType()); 
System.out.println("isPrimaryKey:"+tc.getHasPrimaryKeyConstraintApplied()); 
System.out.println("isAuto:"+tc.getIsAutoIncrementable()); 
System.out.println("isNotNull:"+tc.getHasNotNullConstraintApplied()); 
System.out.println("isUnique:"+tc.getHasUniqueConstraintApplied()); 
System.out.println("defaultValue:"+tc.getDefaultValue());
System.out.println("***********************************");
}
System.out.println("--------------------------------------");
}
connection.close();
System.out.println("Connection test successful");
}catch(SQLException sqlException)
{
System.out.println(sqlException);
}
catch(DBException dbException)
{
System.out.println(dbException);
}
}
}