package home.task14.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @Column(name = "street", length = 50)
    private String street;

    @Column(name = "house", length = 10)
    private String house;

    @Column(name = "ipn", length = 10)
    private String ipn;

    @Column(name = "passport", length = 8)
    private String passport;

    public static UserDetails EMPTY() {
        return new UserDetails();
    }
}
