package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID; // Pour l'ID unique

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInOrderDTO {

    private UUID id;          // Identifiant unique du produit (peut être l'ID de l'offre ou du produit réel)
    private String ref;       // Numéro de référence du produit
    private String productName; // Nom du produit
    private Integer quantity; // Quantité de ce produit spécifique     // Unité de mesure (ex: "kg", "unité", "L")

    // Vous pouvez ajouter des méthodes toString, equals, hashCode si nécessaire
}