@Table(name="booking")
@CompositeKey(columns=["sno","booking_date"])
public class Booking
{
@Column(name="sno")
private int serialNumber;
@Column(name="booking_date")
private java.sql.Date date;
@NotNull
@Column
@MaxLength(50)
private String name;
}