package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate; // Pour la date de collecte
import java.time.LocalTime; // Pour l'heure de collecte
import java.util.List;    // Pour la liste des produits
import java.util.UUID;     // Pour l'ID unique

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsDTO {

    private UUID id;
    private String ref; // Numéro de référence de la commande
    private String status; // Statut actuel de la commande (ex: "received", "pending", "completed")

    private Instant collectionDate; // Date pour la collecte de la commande
    private Instant collectionTime; // Heure pour la collecte de la commande

    private String promoCode; // Code promo appliqué (si applicable)

    private String deliveryAddress; // Adresse de livraison
    private String phoneNumber;     // Numéro de téléphone de contact pour la commande

    private Integer quantity; // Quantité totale d'articles dans la commande
    private String deliveryMethod; // Méthode de livraison (ex: "Livraison", "Collecte")

    private String contactName;   // Nom de la personne de contact
    private String paymentMethod; // Méthode de paiement utilisée (ex: "Carte bancaire", "Espèces")
    private String contactPhone;  // Numéro de téléphone de la personne de contact

    private String deliveryCompany;    // Nom de la société de livraison
    private String deliveryDriverName; // Nom du chauffeur-livreur
    private String deliveryDriverPhone; // Numéro de téléphone du chauffeur-livreur

    private String category; // Catégorie de la commande (ex: "Frait & legumes")
    private Boolean seen;    // Indique si la commande a été vue/traitée

    private List<ProductInOrderDTO> products; // Liste des produits dans la commande

    // Vous pouvez ajouter des méthodes toString, equals, hashCode si nécessaire
}