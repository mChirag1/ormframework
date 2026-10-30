import com.orm.db.*;
import java.sql.*;
class TestDBConnector
{
public static void main(String gg[])
{
Connection connection=DBConnector.connect();
Database database=DatabaseUtil.getDatabase(connection);
System.out.println(database);
try
{
connection.close();
}catch(Exception exception)
{
exception.printStackTrace();
}
System.out.println("Connection test successful");
}
}