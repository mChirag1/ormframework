import com.orm.DTO.*;
import com.orm.framework.*;
import com.orm.exceptions.*;
public class TestInsert
{
public static void main(String gg[])
{
try
{
Department d=new Department("Sales");
ORMFramework ormFramework;
ormFramework=ORMFramework.getInstance();
ormFramework.insert(d);
//System.out.println("Department added with code as : "+d.getCode());
}catch(ORMException exception)
{
exception.printStackTrace();
}
}
}
