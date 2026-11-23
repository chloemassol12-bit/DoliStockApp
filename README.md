# DoliStocks Android

## Travailler page par page

- À chaque écran ou étape cohérente vérifiée, créer un commit conventionnel puis pousser la branche de travail sur `origin`. Exemple de message : `feat(saisie): ajouter le choix de l’entrepôt`.
- Développer chaque écran sur une branche dédiée. La page Connexion se trouve sur `feature/interface-connexion`.
- Partir du sujet de SAE, des retours client dans `docs/` et du cadre Figma correspondant. Demander une précision lorsqu’une règle métier manque.
- Conserver les images de référence et les captures du rendu Android dans `capture maquette/`, avec le numéro de page et leur provenance.

## Organisation du code

- Java 17, fragments AndroidX et vues XML. MainActivity héberge la barre de titre et le conteneur de fragments.
- Regrouper les écrans par fonctionnalité sous `ui/`. Garder le réseau et le stockage dans `data/remote/` et `data/local/`. Les fichiers package-info.java précisent les tâches en attente pour la connexion.
- Utiliser des noms explicites en anglais, UpperCamelCase pour les classes, lowerCamelCase pour les méthodes et snake_case pour les ressources. Indenter de quatre espaces avec le formateur Android Studio.
- Centraliser les libellés français et les styles réutilisés dans les ressources. Associer les libellés aux champs et vérifier la saisie au clavier.
- Donner une responsabilité précise à chaque classe. Commenter les intentions et les contraintes difficiles à déduire du code. Documenter les intégrations en attente par un TODO précis.
- Dans le parcours statique, « Se connecter » ouvre la page Synchronisation. Le statut « Connecté » illustre la maquette. Aucune donnée saisie ne doit être envoyée ou journalisée.
- La validation client limite la synchronisation aux entrepôts et supprime le passage manuel hors ligne. Dans la maquette, « Synchroniser maintenant » ouvre « Appareil prêt », puis « Accéder à l’accueil » ouvre l’accueil connecté. Les quantités, les dates et l’activité récente sont des exemples fixes.
- Depuis l’accueil, « Nouvelle liste » ouvre la saisie de l’entrepôt, du libellé et du mode. La saisie des articles accepte une référence tapée ou lue par une douchette clavier. La recherche illustrée par référence ou désignation commence à trois caractères. Elle apparaît à côté de la saisie à partir de 840 dp.
- La file permet de modifier une liste, de supprimer une sélection et de confirmer un envoi groupé. Le résultat détaille chaque ligne. Dans la démonstration, les lignes réussies quittent la file et les erreurs restent corrigeables. Les articles, quantités et résultats sont simulés en mémoire pendant la session.
- Après une première synchronisation simulée, couper le réseau de l’émulateur affiche les états hors ligne de l’accueil, de la saisie, de la file et des paramètres. La recherche distante et l’envoi redeviennent disponibles au retour du réseau.
- Dans les paramètres, choisir un dépôt par défaut ou « Aucun dépôt par défaut ». Ce réglage et l’initialisation sont conservés sur la tablette. Le branchement REST, la conservation durable des listes et la gestion réelle des identifiants restent à réaliser.

## Interface responsive

- Après la synchronisation initiale, le bouton de menu dans la barre de titre donne accès à Accueil, Listes et Paramètres sur téléphone et tablette. Le bouton Retour ferme le menu ouvert.

- Adapter les écrans à la largeur disponible de la fenêtre, en portrait et en paysage. Utiliser les dp pour la disposition, les sp pour le texte et une hauteur de contenu flexible.
- Pour Connexion et Synchronisation, conserver 16 dp de marge latérale. La colonne occupe la largeur restante sur téléphone et se centre à 484 dp dès que la fenêtre atteint 516 dp.
- Garder les champs et les boutons accessibles par défilement lorsque le clavier réduit la hauteur disponible ou que le texte est agrandi. Prévoir au moins 48 dp pour les cibles tactiles.
- Pour chaque nouvel écran, vérifier le parcours sur téléphone compact (360 × 800 dp) et sur fenêtre large, puis contrôler le retour arrière et la rotation. Avec plusieurs émulateurs ouverts, sélectionner la cible avec `adb -s <numéro-de-série>`.

## Vérification

Depuis la racine sous Windows, exécuter `.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest`. Installer la variante debug, ouvrir la page, vérifier le formulaire et le comparer au cadre Figma sur téléphone et tablette.
