import com.orm.db.*;
import java.sql.*;
class ForeignKey
{
public static void main(String gg[])
{
try
{
Connection connection=DBConnector.connect();
DatabaseMetaData databaseMetaData=connection.getMetaData();
ResultSet foreignKeyResultSet;
String catalog = connection.getCatalog();

System.out.println("Catalog = " + catalog);
foreignKeyResultSet=databaseMetaData.getImportedKeys(catalog,null,"employee_project_mapping");
int keySequence;
String foreignKeyName;
String foreignKeyColumnName;
String foreignKeyTableName;
String parentTableName;
String parentTableColumnName;
while(foreignKeyResultSet.next())
{
parentTableName=foreignKeyResultSet.getString("PKTABLE_NAME");
parentTableColumnName=foreignKeyResultSet.getString("PKCOLUMN_NAME");
foreignKeyTableName=foreignKeyResultSet.getString("FKTABLE_NAME");
foreignKeyColumnName=foreignKeyResultSet.getString("FKCOLUMN_NAME");
keySequence=foreignKeyResultSet.getInt("KEY_SEQ");
foreignKeyName=foreignKeyResultSet.getString("FK_NAME");
System.out.println(parentTableName+" , "+parentTableColumnName+" , "+foreignKeyTableName+" , "+foreignKeyColumnName+" , "+keySequence+" , "+foreignKeyName);
}
foreignKeyResultSet.close();
connection.close();

}catch(Exception exception)
{
System.out.println(exception.getMessage());
exception.printStackTrace();
}
}
}