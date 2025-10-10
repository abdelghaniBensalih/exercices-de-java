# Système de Rapports de Ventes - Restaurant

## Description

Ce projet implémente un système simple desktop de gestion et de rapports de ventes pour un restaurant pour apprendre Java. Il permet de :

1. **Générer des rapports quotidiens par serveur**
2. **Identifier les serveurs les plus performants**
3. **Analyser les catégories les plus vendues par jour de la semaine**
4. **Tests unitaires complets avec JUnit 5**

### Classes principales

- **`Ticket`** : Représente une commande avec ses lignes d'articles
- **`Serveur`** : Informations sur les serveurs du restaurant
- **`Produit`** : Articles vendus avec prix et catégorie
- **`Categorie`** : Classification des produits
- **`Promo`** : Gestion des promotions et réductions
- **`RapportService`** : Service principal pour générer les rapports

### Package Structure

```text
src/
├── main/java/
│   ├── app/          # Classes métier principales
│   └── data/         # Classes de données
└── test/java/
    ├── app/          # Tests des classes métier
    └── data/         # Tests des classes de données
```
