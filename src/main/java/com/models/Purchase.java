package com.models;


import org.springframework.stereotype.Component;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Component
@Entity
@Table(name = "PURCHASES")
public class Purchase {
    @Id
    @SequenceGenerator(name = "PURCHASE_SEQ", sequenceName = "PURCHASE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PURCHASE_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "DATE_OF_PURCHASE")
    private LocalDate dateOfPurchase;
    @Column(name = "AMOUNT")
    private float amount;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "SOUVENIR_NAME")
    private String name;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Float.compare(purchase.amount, amount) == 0 &&
                Objects.equals(id, purchase.id) &&
                Objects.equals(dateOfPurchase, purchase.dateOfPurchase) &&
                Objects.equals(currency, purchase.currency) &&
                Objects.equals(name, purchase.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateOfPurchase, amount, currency, name);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", dateOfPurchase=" + dateOfPurchase +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}