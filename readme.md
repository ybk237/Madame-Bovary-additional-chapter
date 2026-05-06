# Le 36e Chapitre de Madame Bovary - Générateur de Texte

Ce projet en Java est un générateur de texte pseudo-aléatoire. Il a pour but de créer un chapitre supplémentaire du roman Madame Bovary en imitant le style de Gustave Flaubert, en s'entraînant sur le vocabulaire et la structure des 35 chapitres du roman.

## 🧠 Principe de Génération : Les Chaînes de Markov

Le projet repose sur les **chaînes de Markov**. Le principe fondamental est que le choix du prochain mot à générer dépend uniquement d'un historique fini composé des $n$ mots précédents (appelé le **préfixe**).

Le processus se déroule en deux grandes phases : l'apprentissage et la génération.

### 1. Phase d'apprentissage (Construction de la table)

*   **La Règle :** Le programme lit le texte source et fait glisser une "fenêtre" de $n$ mots (le préfixe). Ce préfixe devient une **clé**. Le mot qui suit immédiatement cette fenêtre est ajouté à une **liste de valeurs** associée à cette clé. Si un préfixe apparaît plusieurs fois dans le texte, les mots qui le suivent sont ajoutés à la liste à chaque fois (ce qui préserve naturellement les probabilités d'apparition). Des balises `<START>` et `<END>` sont ajoutées pour marquer le début et la fin des phrases/chapitres.

*   **Mini-exemple :** 
    Imaginons un texte source très court : *"Emma aime le piano. Emma aime la lecture."* et choisissons une taille de préfixe $n = 2$.
    Le programme va extraire les correspondances suivantes :
    *   Clé : `[<START>, <START>]` ➔ Valeur ajoutée : `"Emma"`
    *   Clé : `[<START>, Emma]` ➔ Valeur ajoutée : `"aime"`
    *   Clé : `[Emma, aime]` ➔ Valeurs ajoutées : `"le"`, puis plus loin dans le texte, `"la"`
    *   Clé : `[aime, le]` ➔ Valeur ajoutée : `"piano."`
    *   ...
    *   Clé : `[la, lecture.]` ➔ Valeur ajoutée : `"<END>"`

### 🗺️ Représentation en mémoire : La structure `HMap`

Pour stocker ces milliers d'associations de manière optimale, nous utilisons une table de hachage implémentée de zéro (`HMap`). La clé (le préfixe) est transformée en un index numérique via une fonction de hachage.

Voici un croquis rudimentaire de l'état de notre `HMap` après l'apprentissage du mini-exemple ci-dessus :

```text
Table de Hachage (HMap)
=======================
Index | Contenu (Liste chaînée pour gérer les collisions)
---------------------------------------------------------
[ 0 ] -> { Clé: [<START>, <START>] | Valeurs: ["Emma"] } -> null
[ 1 ] -> { Clé: [<START>, Emma] | Valeurs: ["aime"] } -> null
[ 2 ] -> { Clé: [Emma, aime] | Valeurs:["le", "la"] } -> null
[ 3 ] -> { Clé: [la, lecture.] | Valeurs: [<END>] } -> null
[ 4 ] -> { Clé: [aime, le]  | Valeurs: ["piano."] } -> { Clé: [aime, la] | Valeurs: ["lecture."] } -> null
...
```
*(Note sur l'index 4 : Une collision a eu lieu, les deux entrées distinctes sont chaînées).*

### 2. Phase de génération de texte

*   **La Règle :** L'algorithme démarre avec un préfixe composé uniquement de balises `<START>`. Il interroge la `HMap` avec cette clé pour récupérer la liste des mots suivants possibles. Il tire **au hasard et de manière uniforme** un mot de cette liste, l'ajoute au texte final, puis fait "glisser" son préfixe d'un cran en y intégrant ce nouveau mot. Le cycle recommence jusqu'à tirer la balise `<END>`.

*   **Mini-exemple (en s'appuyant sur la HMap ci-dessus) :**
    1.  **État initial :** Le préfixe courant est `[<START>, <START>]`.
    2.  **Recherche :** La `HMap` nous renvoie la liste `["Emma"]`. On pioche "Emma".
    3.  **Mise à jour :** Le texte généré est : *"Emma"*. Le nouveau préfixe devient `[<START>, Emma]`.
    4.  **Recherche :** Pour la clé `[<START>, Emma]`, la `HMap` (index 1) renvoie `["aime"]`. On pioche "aime".
    5.  **Mise à jour :** Le texte devient : *"Emma aime"*. Le nouveau préfixe devient `[Emma, aime]`.
    6.  **Recherche :** Pour la clé `[Emma, aime]`, la `HMap` (index 2) renvoie la liste `["le", "la"]`. 
    7.  **Tirage aléatoire :** L'algorithme tire au sort : disons qu'il pioche *"la"*.
    8.  **Mise à jour :** Le texte généré est : *"Emma aime la"*. Le nouveau préfixe devient `[aime, la]`.
    9.  **Recherche :** La `HMap` (index 4) renvoie `["lecture."]`. Fin de la phrase.

*Plus la taille du préfixe ($n$) est grande, plus le texte généré ressemblera à des phrases exactes du livre original (car les listes de valeurs dans la `HMap` n'auront souvent qu'un seul choix possible). Plus $n$ est petit, plus le texte sera grammaticalement chaotique mais original.*

## ⚙️ Implémentation et Structures de Données

Pour des raisons d'apprentissage, ce projet n'utilise pas les collections standards de Java, mais des structures de données implémentées à la main (plus précisément les listes chaînées et des tables de hachage)

Cette `HMap` (associée à une fonction de hachage personnalisée gérant les collisions) permet une complexité d'insertion et de recherche de $O(1)$ (en moyenne).

## 🚀 Utilisation

Le programme principal prend en argument un entier $n$ qui définit la taille de la chaîne de Markov (la taille du préfixe).

**Exemple d'exécution (avec un préfixe de taille 3) :**
```bash
java Bovary 3
```

*(Note : les fichiers textes sources de Madame Bovary doivent être présents dans le répertoire `/bovary` à la racine du projet).*