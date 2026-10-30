@Table(name="bill") 
@CompositeForeignKey(table="booking",columns=["booking_sno","booking_date"])
public class bill
{
@Column(name="bill_number")
@PrimaryKey
private int billNumber;
@NotNull
@Column(name="bill_date")
private java.sql.Date billDate;
@NotNull
private Booking booking;
}