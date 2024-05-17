package home.task14.app.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "phone", length = 25)
    private String phone;

    @ElementCollection
    @CollectionTable(name = "shopping_carts", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "amount")
    private Map<Long, Integer> shoppingCart = new HashMap<>();

    public static User EMPTY() {
        return new User();
    }
}
