package dat.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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
    @Setter
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

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Unicorn unicorn)) return false;
        return Objects.equals(id, unicorn.id) && Objects.equals(name, unicorn.name) && Objects.equals(age, unicorn.age) && Objects.equals(color, unicorn.color) && Objects.equals(powerStrength, unicorn.powerStrength);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, age, color, powerStrength);
    }
}
