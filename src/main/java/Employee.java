import lombok.Getter;

@Getter
public class Employee
{
    private String dni;

    private String name;

    private String password;

    private boolean isOnline;

    public Employee(String dni, String name, boolean isOnline)
    {
        this.dni = dni;
        this.name = name;
        this.isOnline = isOnline;
    }
}
