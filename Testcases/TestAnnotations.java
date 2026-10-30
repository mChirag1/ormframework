import com.orm.annotation.*;
import java.lang.reflect.*;

@Table(name="employee")
class Employee
{
    @PrimaryKey
    @Column(name="employee_id")
    private int id;

    @Column(name="employee_name")
    @NotNull
    private String name;

    @Column(name="email")
    @Unique
    private String email;

    @Column
    private double salary;
}


public class TestAnnotations
{
    public static void main(String args[])
    {
        System.out.println("======================================");
        System.out.println("       ANNOTATION TEST STARTED");
        System.out.println("======================================");


        /*
         * Step 1:
         * Employee class ka Class object obtain karna
         */
        Class employeeClass = Employee.class;

        System.out.println();
        System.out.println("SOP 1: Class object obtained");
        System.out.println("Class Name : " + employeeClass.getName());


        /*
         * Step 2:
         * @Table annotation check karna
         */
        System.out.println();
        System.out.println("SOP 2: Checking @Table annotation");

        if(employeeClass.isAnnotationPresent(Table.class))
        {
            Table table =
                (Table)employeeClass.getAnnotation(Table.class);

            System.out.println("@Table found");

            System.out.println(
                "Table Name : " + table.name()
            );
        }
        else
        {
            System.out.println("@Table NOT found");
        }


        /*
         * Step 3:
         * Employee class ke saare fields obtain karna
         */
        System.out.println();
        System.out.println("SOP 3: Getting fields");

        Field fields[] =
            employeeClass.getDeclaredFields();

        System.out.println(
            "Total Fields : " + fields.length
        );


        /*
         * Step 4:
         * Har field ko process karna
         */
        for(Field field : fields)
        {
            System.out.println();
            System.out.println("--------------------------------------");

            System.out.println(
                "SOP 4: Processing Field : "
                + field.getName()
            );


            /*
             * @Column check
             */
            if(field.isAnnotationPresent(Column.class))
            {
                Column column =
                    (Column)field.getAnnotation(Column.class);

                System.out.println("@Column : YES");

                System.out.println(
                    "Column Name : " + column.name()
                );
            }
            else
            {
                System.out.println("@Column : NO");
            }


            /*
             * @PrimaryKey check
             */
            if(field.isAnnotationPresent(PrimaryKey.class))
            {
                System.out.println(
                    "@PrimaryKey : YES"
                );
            }
            else
            {
                System.out.println(
                    "@PrimaryKey : NO"
                );
            }


            /*
             * @NotNull check
             */
            if(field.isAnnotationPresent(NotNull.class))
            {
                System.out.println(
                    "@NotNull : YES"
                );
            }
            else
            {
                System.out.println(
                    "@NotNull : NO"
                );
            }


            /*
             * @Unique check
             */
            if(field.isAnnotationPresent(Unique.class))
            {
                System.out.println(
                    "@Unique : YES"
                );
            }
            else
            {
                System.out.println(
                    "@Unique : NO"
                );
            }
        }


        System.out.println();
        System.out.println("======================================");
        System.out.println("       ANNOTATION TEST COMPLETED");
        System.out.println("======================================");
    }
}