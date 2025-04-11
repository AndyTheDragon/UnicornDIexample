package dat.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Unicorn
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Integer id;
    @Setter
    private String name;
    private Integer age;
    private String color;
    private Double powerStrength;

    public Unicorn() {    }
    public Unicorn(String name, int age, String color, double powerStrength)
    {
        this.name = name;
        this.age = age;
        this.color = color;
        this.powerStrength = powerStrength;
    }

    @Override
    public String toString()
    {
        return String.format("[ID %d]: %s the %s Unicorn is %d years old and has powerlevel %.2f", id, name, color, age, powerStrength);
    }
}
