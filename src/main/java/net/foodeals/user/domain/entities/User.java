package net.foodeals.user.domain.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import net.foodeals.common.entities.DeletionReason;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.contract.domain.entities.UserContract;
import net.foodeals.crm.domain.entities.Event;
import net.foodeals.crm.domain.entities.Prospect;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.product.domain.entities.Rayon;
import net.foodeals.user.domain.entities.enums.Gender;
import net.foodeals.user.domain.valueObjects.Name;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User
 */
@Entity
@Table(name = "users")

@Getter
public class User extends AbstractEntity<Integer> implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;

    @Embedded
    private Name name;

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    private String avatarPath;

    @Column(unique = true)
    private String email;

    private String phone;

    private String password;

    private String nationality;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Role role;

    @ManyToOne
    private OrganizationEntity organizationEntity;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<Solution> solutions = new HashSet<>();

    public Set<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(Set<Solution> solutions) {
        this.solutions = solutions;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    private SubEntity subEntity;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserActivities> userActivities;

    @OneToMany(mappedBy = "deliveryBoy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserContract> userContracts;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "lead")
    private List<Prospect> managedProspects;

    @OneToMany(mappedBy = "creator")
    private List<Prospect> createdProspects;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "rayon_id")
    private Rayon rayon;

    public Rayon getRayon() {
        return rayon;
    }

    public void setRayon(Rayon rayon) {
        this.rayon = rayon;
    }


    @OneToMany(mappedBy = "lead")
    private List<Event> events;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id")
    private User responsible; // The user who is responsible for this user

    @OneToMany(mappedBy = "responsible", cascade = CascadeType.ALL)
    @Builder.Default
    private List<User> managedUsers = new ArrayList<>(); // Users managed by this user


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeletionReason> deletionReasons = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Gender gender;


    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    @Column(name = "national_id")
    private String nationalId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkingHours> workingHours;

    // ... (existing methods)

    public User setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public User setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public User setNationalId(String nationalId) {
        this.nationalId = nationalId;
        return this;
    }

    public User setWorkingHours(List<WorkingHours> workingHours) {
        this.workingHours = workingHours;
        return this;
    }




    public User(Name name, String email, String phone, String password, Boolean isEmailVerified, Role role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isEmailVerified = isEmailVerified;
        this.role = role;
    }

    public List<Event> getEvents() {
        return events;
    }

    public User() {

    }

    public void setSubEntity(SubEntity subEntity) {
        this.subEntity = subEntity;
    }

    public static User create(Name name, String email, String phone, String password,
                              Boolean isEmailVerified, Role role) {
        return new User(
                name,
                email,
                phone,
                password,
                isEmailVerified,
                role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || role.getAuthorities() == null) {
            return Collections.emptyList();
        }

        final List<SimpleGrantedAuthority> authorities = role.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setName(Name name) {
        this.name = name;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public User setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
        return this;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public User setOrganizationEntity(OrganizationEntity organizationEntity) {
        this.organizationEntity = organizationEntity;
        return this;
    }

    public User setAccount(Account account) {
        this.account = account;
        return this;
    }

    public User setStatus(UserStatus st)
    {
        this.status = st;
        return this;
    }

    public User setAddress(Address st)
    {
        this.address = st;
        return this;
    }

    public User setManagedProspects(ArrayList<Prospect> prospects) {
        this.managedProspects = prospects;
        return this;
    }

    public User setCreatedProspects(ArrayList<Prospect> prospects) {
        this.managedProspects = prospects;
        return this;
    }

    public User setEvents(ArrayList<Event> events) {
        this.events = events;
        return this;
    }
}
