import com.orm.annotation.*;
@Table(name="department")
public class Department
{
@Column
@PrimaryKey
private int code;
@NotNull
@Unique
@Column
@MaxLength(50)
private String name;
// constructors
// setter/getter
// toString
// equals
// compareTo
// hashCode
}