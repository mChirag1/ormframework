import com.orm.DTO.*;
import com.orm.framework.*;
import com.orm.exceptions.*;
import java.sql.*;
class TestInsertSales
{
public static void main(String gg[])
{
try
{
Sales sales=new Sales(3,new java.sql.Date(126,9,30),new java.math.BigDecimal("1300.00"));
ORMFramework ormFramework;
ormFramework=ORMFramework.getInstance();
System.out.println(ormFramework);
ormFramework.insert(sales);
System.out.println("sales added");
}catch(ORMException ormException)
{
ormException.printStackTrace();
//System.out.println(ormException);
}
}
}
