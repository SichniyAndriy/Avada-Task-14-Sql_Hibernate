package home.task14.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;

    @Column(name = "product", length = 50, unique = true, nullable = false)
    private String name;

    @Column(name = "price", precision = 8, scale = 2)
    private BigDecimal price;

    public static Product EMPTY() {
        return new Product();
    }
}
