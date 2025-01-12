import lombok.Getter;

@Getter
public class Employee
{
    private String dni;

    private String name;

    private String password;

    private boolean isOnline;

    public Employee(String name, boolean isOnline)
    {
        this.name = name;
        this.isOnline = isOnline;
    }
}
