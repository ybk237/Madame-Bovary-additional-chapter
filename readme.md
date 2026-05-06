*Version française: [Français](README.fr.md)*

# The 36th Chapter of Madame Bovary - Text Generator

This Java project is a pseudo-random text generator. Its goal is to create an additional chapter for the novel *Madame Bovary* by imitating Gustave Flaubert's style, training on the vocabulary and structure of the novel's first 35 chapters.

## 🧠 Generation Principle: Markov Chains

The project relies on **Markov chains**. The fundamental principle is that the choice of the next word to generate depends solely on a finite history composed of the $n$ preceding words (called the **prefix**).

The process is divided into two main phases: training and generation.

### 1. Training Phase (Table Construction)

*   **The Rule:** The program reads the source text and slides a "window" of $n$ words (the prefix). This prefix becomes a **key**. The word immediately following this window is added to a **list of values** associated with this key. If a prefix appears multiple times in the text, the words following it are added to the list each time (which naturally preserves their probability of occurrence). `<START>` and `<END>` tags are added to mark the beginning and end of sentences/chapters.

*   **Mini-example:** 
    Let's imagine a very short source text: *"Emma likes the piano. Emma likes to read."* and choose a prefix size of $n = 2$.
    The program will extract the following mappings:
    *   Key: `[<START>, <START>]` ➔ Added value: `"Emma"`
    *   Key: `[<START>, Emma]` ➔ Added value: `"likes"`
    *   Key: `[Emma, likes]` ➔ Added values: `"the"`, then later in the text, `"to"`
    *   Key: `[likes, the]` ➔ Added value: `"piano."`
    *   ...
    *   Key: `[to, read.]` ➔ Added value: `"<END>"`

### 🗺️ Memory Representation: The `HMap` Structure

To store these thousands of associations optimally, we use a custom-built hash table (`HMap`). The key (the prefix) is transformed into a numerical index via a hash function.

Here is a basic sketch of our `HMap`'s state after training on the mini-example above:

```text
Hash Table (HMap)
=======================
Index | Content (Linked list to handle collisions)
---------------------------------------------------------
[ 0 ] -> { Key: [<START>, <START>] | Values: ["Emma"] } -> null
[ 1 ] -> { Key: [<START>, Emma] | Values: ["likes"] } -> null
[ 2 ] -> { Key: [Emma, likes] | Values:["the", "to"] } -> null
[ 3 ] -> { Key: [to, read.] | Values: [<END>] } -> null
[ 4 ] -> { Key: [likes, the]  | Values: ["piano."] } -> { Key: [likes, to] | Values: ["read."] } -> null
...
```
*(Note on index 4: A collision occurred, so the two distinct entries are chained).*

### 2. Text Generation Phase

*   **The Rule:** The algorithm starts with a prefix composed entirely of `<START>` tags. It queries the `HMap` with this key to retrieve the list of possible next words. It randomly and **uniformly picks** a word from this list, appends it to the final text, and then "slides" its prefix one step forward by incorporating this new word. The cycle repeats until the `<END>` tag is drawn.

*   **Mini-example (based on the HMap above):**
    1.  **Initial state:** The current prefix is `[<START>, <START>]`.
    2.  **Search:** The `HMap` returns the list `["Emma"]`. We pick "Emma".
    3.  **Update:** The generated text is: *"Emma"*. The new prefix becomes `[<START>, Emma]`.
    4.  **Search:** For the key `[<START>, Emma]`, the `HMap` (index 1) returns `["likes"]`. We pick "likes".
    5.  **Update:** The text becomes: *"Emma likes"*. The new prefix becomes `[Emma, likes]`.
    6.  **Search:** For the key `[Emma, likes]`, the `HMap` (index 2) returns the list `["the", "to"]`. 
    7.  **Random draw:** The algorithm picks at random: let's say it picks *"the"*.
    8.  **Update:** The generated text is: *"Emma likes the"*. The new prefix becomes `[likes, the]`.
    9.  **Search:** The `HMap` (index 4) returns `["piano."]`. End of the sentence.

*The larger the prefix size ($n$), the more the generated text will resemble exact sentences from the original book (because the value lists in the `HMap` will often only have one possible choice). The smaller $n$ is, the more grammatically chaotic but original the text will be.*

## ⚙️ Implementation and Data Structures

For educational purposes, this project does not use standard Java collections, but rather custom-built data structures implemented from scratch (specifically, linked lists and hash tables).

This `HMap` (paired with a custom hash function handling collisions) allows for an insertion and search complexity of $O(1)$ (on average).

## 🚀 Usage

The main program takes an integer $n$ as an argument, which defines the size of the Markov chain (the size of the prefix).

**Execution example (with a prefix size of 3):**
Right from the root folder of the project :

```bash
javac src/Bovary/*.java
java -cp src Bovary.Bovary 3
```

*(Note: the source text files of Madame Bovary must be present in the `/bovary` directory at the root of the project).*
