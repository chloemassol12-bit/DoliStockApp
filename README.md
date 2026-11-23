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
- Depuis l’accueil, « Nouvelle liste » ouvre « Nouvelle saisie ». Les deux écrans partagent une navigation latérale à partir de 600 dp. L’entrepôt, le libellé et le mode sont modifiables dans la maquette. Les entrepôts proposés sont des exemples ; le dépôt facultatif par défaut devra provenir des paramètres de la tablette. Le bouton « Ajouter des articles » annonce le prochain écran.

## Interface responsive

- Adapter les écrans à la largeur disponible de la fenêtre, en portrait et en paysage. Utiliser les dp pour la disposition, les sp pour le texte et une hauteur de contenu flexible.
- Pour Connexion et Synchronisation, conserver 16 dp de marge latérale. La colonne occupe la largeur restante sur téléphone et se centre à 484 dp dès que la fenêtre atteint 516 dp.
- Garder les champs et les boutons accessibles par défilement lorsque le clavier réduit la hauteur disponible ou que le texte est agrandi. Prévoir au moins 48 dp pour les cibles tactiles.
- Pour chaque nouvel écran, vérifier le parcours sur téléphone compact (360 × 800 dp) et sur fenêtre large, puis contrôler le retour arrière et la rotation. Avec plusieurs émulateurs ouverts, sélectionner la cible avec `adb -s <numéro-de-série>`.

## Vérification

Depuis la racine sous Windows, exécuter `.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest`. Installer la variante debug, ouvrir la page, vérifier le formulaire et le comparer au cadre Figma sur téléphone et tablette.
